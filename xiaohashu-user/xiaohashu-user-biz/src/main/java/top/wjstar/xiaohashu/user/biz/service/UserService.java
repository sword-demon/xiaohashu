package top.wjstar.xiaohashu.user.biz.service;

import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.user.biz.model.vo.UpdateUserInfoReqVO;

/**
 * 用户业务
 */
public interface UserService {

    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO 用户信息更新数据
     * @return
     */
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);
}
