package top.wjstar.xiaohashu.auth.service;

import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.auth.model.vo.veritificationcode.SendVerificationCodeReqVO;

public interface VerificationCodeService {

    /**
     * 发送短信验证码
     * @param sendVerificationCodeReqVO 发送短信验证码参数实体
     * @return 自定义响应
     */
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}
