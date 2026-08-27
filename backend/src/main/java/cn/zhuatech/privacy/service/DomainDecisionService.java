/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.privacy.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class DomainDecisionService {
 public DecisionResult assess(DecisionRequest request) { int score=100;List<String> actions=new ArrayList<>();if(request.lawfulBasis().isBlank()){score-=50;actions.add("确认合法性基础");}if(request.consentRequired()&&!request.consentRecorded()){score-=40;actions.add("取得并留存有效同意");}if((request.crossBorder()||request.sensitiveData())&&!request.impactAssessmentDone()){score-=35;actions.add("完成个人信息保护影响评估");}if(request.retentionDays()>3650){score-=15;actions.add("复核超长期限数据保留必要性");}return result(score,actions,"COMPLIANT","REMEDIATE","STOP_PROCESSING",Map.of("retentionDays",request.retentionDays(),"crossBorder",request.crossBorder(),"sensitiveData",request.sensitiveData())); }
 private DecisionResult result(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=80?good:score>=50?warn:bad;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 private DecisionResult riskResult(int raw,List<String> actions,String good,String warn,String bad,Map<String,Object> metrics) { int score=Math.max(0,Math.min(100,raw));String decision=score>=70?bad:score>=40?warn:good;return new DecisionResult(decision,score,metrics,List.copyOf(actions)); }
 public record DecisionRequest(
        @NotBlank String activityNo,
        @NotBlank String lawfulBasis,
        @Positive int retentionDays,
        boolean consentRequired,
        boolean consentRecorded,
        boolean crossBorder,
        boolean impactAssessmentDone,
        boolean sensitiveData) {}
 public record DecisionResult(String decision,int score,Map<String,Object> metrics,List<String> actions) {}
}
