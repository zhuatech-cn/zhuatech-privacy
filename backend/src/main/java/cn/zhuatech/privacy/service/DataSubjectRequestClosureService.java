/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.privacy.service;

import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSubjectRequestClosureService {
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.identityVerified()) blockers.add("数据主体身份尚未核验");
        if (!request.requestTypeClassified()) blockers.add("访问、更正、删除等请求类型未分类");
        if (!request.jurisdictionConfirmed()) blockers.add("适用法律与地域未确认");
        if (!request.legalHoldChecked()) blockers.add("未核对诉讼保全与法定留存要求");
        if (!request.responsePackageVerified()) blockers.add("对外响应材料未完成复核");
        if (!request.deadlineMet()) blockers.add("请求即将或已经超过法定时限");
        if (!request.reviewerSeparated()) blockers.add("经办人与复核人未职责分离");
        if (!request.auditReady()) blockers.add("身份、检索、处置及送达证据链不完整");
        if (!request.dataSourcesSearched()) actions.add("补齐结构化及非结构化数据源检索");
        if (!request.exemptionsReviewed()) actions.add("复核法定例外、第三方权益与最小披露范围");
        if (!request.thirdPartiesNotified()) actions.add("通知受托处理方同步执行请求");
        if (!request.deletionProofCaptured()) actions.add("保存删除、匿名化或保留决定证明");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED : !actions.isEmpty() ? Decision.REVIEW : Decision.COMPLETE;
        return new Assessment(request.requestId(), decision, List.copyOf(blockers), List.copyOf(actions));
    }

    public record Request(@NotBlank String requestId, boolean identityVerified,
                          boolean requestTypeClassified, boolean jurisdictionConfirmed,
                          boolean dataSourcesSearched, boolean legalHoldChecked,
                          boolean exemptionsReviewed, boolean thirdPartiesNotified,
                          boolean responsePackageVerified, boolean deletionProofCaptured,
                          boolean deadlineMet, boolean reviewerSeparated, boolean auditReady) {}
    public record Assessment(String requestId, Decision decision, List<String> blockers, List<String> actions) {}
    public enum Decision { COMPLETE, REVIEW, BLOCKED }
}
