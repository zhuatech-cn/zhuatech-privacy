/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.privacy.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class DataSubjectRequestClosureServiceTest {
    private final DataSubjectRequestClosureService service = new DataSubjectRequestClosureService();

    @Test void completesControlledDataSubjectRequest() {
        var result = service.assess(request(true, true, true, true));
        assertThat(result.decision()).isEqualTo(DataSubjectRequestClosureService.Decision.COMPLETE);
        assertThat(result.blockers()).isEmpty();
    }

    @Test void reviewsRequestWithOutstandingActions() {
        var result = service.assess(request(false, false, false, false));
        assertThat(result.decision()).isEqualTo(DataSubjectRequestClosureService.Decision.REVIEW);
        assertThat(result.actions()).hasSize(4);
    }

    @Test void blocksUncontrolledRequestClosure() {
        var result = service.assess(new DataSubjectRequestClosureService.Request("DSR-003", false, false,
                false, true, false, true, true, false, true, false, false, false));
        assertThat(result.decision()).isEqualTo(DataSubjectRequestClosureService.Decision.BLOCKED);
        assertThat(result.blockers()).hasSize(8);
    }

    private DataSubjectRequestClosureService.Request request(boolean sources, boolean exemptions,
                                                             boolean thirdParties, boolean deletionProof) {
        return new DataSubjectRequestClosureService.Request("DSR-001", true, true, true, sources, true,
                exemptions, thirdParties, true, deletionProof, true, true, true);
    }
}
