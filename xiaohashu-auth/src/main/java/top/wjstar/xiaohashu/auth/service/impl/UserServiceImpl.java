package top.wjstar.xiaohashu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import top.wjstar.framework.biz.context.holder.LoginUserContextHolder;
import top.wjstar.framework.common.enums.DeletedEnum;
import top.wjstar.framework.common.enums.StatusEnum;
import top.wjstar.framework.common.exception.BizException;
import top.wjstar.framework.common.response.Response;
import top.wjstar.framework.common.util.JsonUtils;
import top.wjstar.xiaohashu.auth.constant.RedisKeyConstants;
import top.wjstar.xiaohashu.auth.constant.RoleConstants;
import top.wjstar.xiaohashu.auth.domain.dataobject.UserDO;
import top.wjstar.xiaohashu.auth.domain.dataobject.UserRoleDO;
import top.wjstar.xiaohashu.auth.domain.mapper.UserDOMapper;
import top.wjstar.xiaohashu.auth.domain.mapper.UserRoleDOMapper;
import top.wjstar.xiaohashu.auth.enums.LoginTypeEnum;
import top.wjstar.xiaohashu.auth.enums.ResponseCodeEnum;
import top.wjstar.xiaohashu.auth.model.vo.user.UpdatePasswordReqVO;
import top.wjstar.xiaohashu.auth.model.vo.user.UserLoginReqVO;
import top.wjstar.xiaohashu.auth.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserRoleDOMapper userRoleDOMapper;

    /**
     * 编程式事务
     */
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 登录与注册
     *
     * @param userLoginReqVO 请求参数实体
     * @return 字符串自定义响应
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        Long userId = null;

        switch (loginTypeEnum) {
            case VERIFICATION_CODE:
                String verificationCode = userLoginReqVO.getCode();

                // 校验入参验证码是否为空
                // 会抛出一个 IllegalArgumentException 异常 ,所以需要捕捉
                Preconditions.checkArgument(
                        StringUtils.isNotBlank(verificationCode),
                        "验证码不能为空"
                );

                // 校验验证码是否为空
//                if (StringUtils.isBlank(verificationCode)) {
//                    return Response.fail(ResponseCodeEnum.PARAM_NOT_VALID.getErrorCode(), "验证码不能为空");
//                }

                // 构建验证码 redis key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                // 判断用户提交的验证码与redis中的验证码是否一致
                if (!StringUtils.equals(verificationCode, sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 通过手机号码查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);
                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));

                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 没有注册，系统自动注册
                    userId = registerUser(phone);
                } else {
                    // 已经注册，获取其用户id
                    userId = userDO.getId();
                }
                break;
            case PASSWORD:
                String password = userLoginReqVO.getPassword();
                UserDO userDO1 = userDOMapper.selectByPhone(phone);

                // 判断该手机号是否注册
                if (Objects.isNull(userDO1)) {
                    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
                }

                // 拿到密文密码
                String encodePassword = userDO1.getPassword();
                // 匹配密码是否一致
                boolean isPasswordCorrect = passwordEncoder.matches(password, encodePassword);
                if (!isPasswordCorrect) {
                    throw new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
                }
                userId = userDO1.getId();
                break;
            default:
                break;
        }

        // SaToken 登录用户，并返回 token 令牌
        StpUtil.login(userId);
        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 返回token令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 退出登录
     *
     * @return 响应
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
        log.info("==> 用户退出登录, userId: {}", userId);

        StpUtil.logout(userId);
        return Response.success();
    }

    /**
     * 修改密码
     *
     * @param updatePasswordReqVO 新密码
     * @return
     */
    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        String newPassword = updatePasswordReqVO.getNewPassword();
        String encodePassword = passwordEncoder.encode(newPassword);

        // 获取当前请求对应的用户 id
        Long userId = LoginUserContextHolder.getUserId();

        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(encodePassword)
                .updateTime(LocalDateTime.now())
                .build();

        // 更新密码
        userDOMapper.updateByPrimaryKeySelective(userDO);
        return Response.success();
    }

    /**
     * 系统自动注册用户
     *
     * @param phone 手机号码
     */
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小哈书id
                Long xiaoHaShuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHASHU_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .xiaohashuId(String.valueOf(xiaoHaShuId))
                        .nickname("小红薯" + xiaoHaShuId)
                        .status(StatusEnum.ENABLE.getValue())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();

                // 添加入库
                userDOMapper.insert(userDO);

                // 获取刚刚添加入库的用户ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleDO userRoleDO = UserRoleDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleDOMapper.insert(userRoleDO);

                // 将该用户的角色 ID 存入 Redis 中
                List<Long> roles = Lists.newArrayList();
                roles.add(RoleConstants.COMMON_USER_ROLE_ID);
                String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly(); // 标记事务为回滚
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });
    }
}
