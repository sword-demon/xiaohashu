package top.wjstar.xiaohashu.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import top.wjstar.framework.common.exception.BizException;
import top.wjstar.framework.common.response.Response;
import top.wjstar.xiaohashu.auth.constant.RedisKeyConstants;
import top.wjstar.xiaohashu.auth.enums.ResponseCodeEnum;
import top.wjstar.xiaohashu.auth.model.vo.veritificationcode.SendVerificationCodeReqVO;
import top.wjstar.xiaohashu.auth.service.VerificationCodeService;
import top.wjstar.xiaohashu.auth.sms.AliyunSmsHelper;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "taskExecutor") // 一定要指定名称
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private AliyunSmsHelper aliyunSmsHelper;

    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVO 发送短信验证码参数实体
     * @return 自定义响应
     */
    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        // 手机号码
        String phone = sendVerificationCodeReqVO.getPhone();

        // 构建验证码 redis key
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);

        // 判断是否已经发送过验证码
        boolean isSent = redisTemplate.hasKey(key);
        if (isSent) {
            // 提示发送频繁
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }
        // 生成6位随机数字验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        log.info("==> 手机号: {}, 已经发送验证码: [{}]", phone, verificationCode);

        // 调用第三方短信发送服务
        threadPoolTaskExecutor.submit(() -> {
            String signName = "阿里云短信测试";
            String templateCode = "SMS_1321312";
            String templateParam = String.format("{\"code\":\"%s\"}", verificationCode);
            aliyunSmsHelper.sendMessage(signName, templateCode, phone, templateParam);
        });

        // 存储验证码到 redis，并设置过期时间为3分钟
        redisTemplate.opsForValue().set(key, verificationCode, 3, TimeUnit.MINUTES);

        return Response.success();
    }
}
