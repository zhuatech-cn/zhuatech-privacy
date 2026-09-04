/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.privacy.controller;

import cn.zhuatech.privacy.common.ApiResponse;
import cn.zhuatech.privacy.service.DataSubjectRequestClosureService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enterprise/privacy")
public class DataSubjectRequestClosureController {
    private final DataSubjectRequestClosureService service;
    public DataSubjectRequestClosureController(DataSubjectRequestClosureService service) { this.service = service; }
    @PostMapping("/data-subject-request-closure")
    public ApiResponse<DataSubjectRequestClosureService.Assessment> assess(
            @Valid @RequestBody DataSubjectRequestClosureService.Request request) {
        return ApiResponse.ok(service.assess(request));
    }
}
