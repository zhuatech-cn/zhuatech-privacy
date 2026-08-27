/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.privacy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class DomainDecisionApiTests {
 @Autowired MockMvc mvc;
 @Test void domainDecisionReturnsAuditableScoreMetricsAndActions() throws Exception {
  mvc.perform(post("/api/domain/decision").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"activityNo\":\"ROPA-2026-018\",\"lawfulBasis\":\"履行合同\",\"retentionDays\":365,\"consentRequired\":false,\"consentRecorded\":true,\"crossBorder\":false,\"impactAssessmentDone\":true,\"sensitiveData\":false}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").isString()).andExpect(jsonPath("$.data.score").isNumber()).andExpect(jsonPath("$.data.metrics").isMap()).andExpect(jsonPath("$.data.actions").isArray());
 }
 @Test void domainRiskScenarioReturnsExpectedBlockingDecision() throws Exception {
  mvc.perform(post("/api/domain/decision").with(httpBasic("operator","operator123")).contentType(MediaType.APPLICATION_JSON).content("{\"activityNo\":\"ROPA-2026-018\",\"lawfulBasis\":\"待确认\",\"retentionDays\":5000,\"consentRequired\":true,\"consentRecorded\":false,\"crossBorder\":true,\"impactAssessmentDone\":false,\"sensitiveData\":true}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.data.decision").value("STOP_PROCESSING")).andExpect(jsonPath("$.data.actions").isNotEmpty());
 }
 @Test void domainDecisionRequiresAuthentication() throws Exception {mvc.perform(post("/api/domain/decision").contentType(MediaType.APPLICATION_JSON).content("{}" )).andExpect(status().isUnauthorized());}
}
