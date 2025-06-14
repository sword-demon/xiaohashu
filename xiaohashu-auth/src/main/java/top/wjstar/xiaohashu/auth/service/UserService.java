package top.wjstar.xiaohashu.auth.service;

import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.auth.model.vo.user.UpdatePasswordReqVO;
import top.wjstar.xiaohashu.auth.model.vo.user.UserLoginReqVO;

public interface UserService {

    /**
     * 登录与注册
     *
     * @param userLoginReqVO 请求参数实体
     * @return 字符串自定义响应
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);

    /**
     * 退出登录
     *
     * @return 响应
     */
    Response<?> logout();

    /**
     * 修改密码
     *
     * @param updatePasswordReqVO 新密码
     * @return
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
