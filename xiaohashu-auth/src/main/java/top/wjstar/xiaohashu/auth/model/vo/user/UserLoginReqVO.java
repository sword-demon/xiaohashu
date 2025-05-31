package top.wjstar.xiaohashu.auth.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wjstar.framework.common.validator.PhoneNumber;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginReqVO {

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @PhoneNumber
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录类型：手机号验证码登录、账户密码登录
     */
    @NotNull(message = "登录类型不能为空")
    private Integer type;
}
