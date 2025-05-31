package top.wjstar.xiaohashu.auth.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.wjstar.framework.biz.operationlog.aspect.ApiOperationLog;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.auth.model.vo.veritificationcode.SendVerificationCodeReqVO;
import top.wjstar.xiaohashu.auth.service.VerificationCodeService;

@RestController
@Slf4j
public class VerificationCodeController {

    @Resource
    private VerificationCodeService verificationCodeService;

    @PostMapping("/verification/code/send")
    @ApiOperationLog(description = "发送短信验证码")
    public Response<?> send(@Validated @RequestBody SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        return verificationCodeService.send(sendVerificationCodeReqVO);
    }
}
