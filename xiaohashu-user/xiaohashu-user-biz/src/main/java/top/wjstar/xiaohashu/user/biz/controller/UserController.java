package top.wjstar.xiaohashu.user.biz.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.user.biz.model.vo.UpdateUserInfoReqVO;
import top.wjstar.xiaohashu.user.biz.service.UserService;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @ApiOperationLog(description = "修改用户信息") // 包含文件上传,json 序列化会有问题
    public Response<?> updateUserInfo(@Validated @RequestBody UpdateUserInfoReqVO updateUserInfoReqVO) {
        return userService.updateUserInfo(updateUserInfoReqVO);
    }
}
