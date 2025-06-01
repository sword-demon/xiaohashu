package top.wjstar.xiaohashu.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.wjstar.xiaohashu.gateway.constant.RedisKeyConstants;

import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回 loginId 拥有的权限列表
        // loginId 即 登录接口中的 StpUtil.login() 传入的 userId
        log.info("## 获取用户权限列表, loginId: {}", loginId);

        // 根据用户 id,构建用户角色 key
        String userRoleKey = RedisKeyConstants.buildUserRoleKey(Long.valueOf(loginId.toString()));
        // 根据用户 id 从 redis 里获取该用户的角色集合
        String userRolesValue = redisTemplate.opsForValue().get(userRoleKey);
        if (StringUtils.isNotBlank(userRolesValue)) {
            return Collections.emptyList();
        }

        // 将 json 字符串转换为 List<String> 角色集合
        List<String> userRoleKeys = objectMapper.readValue(userRolesValue, new TypeReference<List<String>>() {
        });

        if (CollUtil.isNotEmpty(userRoleKeys)) {
            // 查询这些角色对应的权限
            // 构建角色-权限 redis key 集合
            List<String> rolePermissionsKeys = userRoleKeys.stream()
                    .map(RedisKeyConstants::buildRolePermissionsKey)
                    .toList();

            // 通过 key 集合批量查询权限,提升查询性能
            // 一次性将这些角色对于的权限标识符查询出来,保证最少的 IO 次数,提升查询性能
            List<String> rolePermissionsValues = redisTemplate.opsForValue().multiGet(rolePermissionsKeys);
            if (CollUtil.isNotEmpty(rolePermissionsValues)) {
                List<String> permissions = Lists.newArrayList();

                // 遍历所有的角色的权限集合,统一添加到 permissions 集合中
                rolePermissionsValues.forEach(jsonValue -> {
                    try {
                        // 将 JSON 字符串转换为 List<String> 权限集合
                        List<String> rolePermissions = objectMapper.readValue(jsonValue, new TypeReference<List<String>>() {
                        });
                        permissions.addAll(rolePermissions);
                    } catch (JsonProcessingException e) {
                        log.error("===> JSON 解析错误: ", e);
                    }
                });

                // 返回此用户所拥有的权限
                return permissions;
            }
        }
        return Collections.emptyList();
    }

    @SneakyThrows
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        log.info("## 获取用户角色列表, loginId: {}", loginId);

        // 构建 用户-角色 Redis Key
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(Long.valueOf(loginId.toString()));

        // 构建用户 id,从 Redis 中获取该用户的角色合集
        String useRolesValue = redisTemplate.opsForValue().get(userRolesKey);
        if (StringUtils.isBlank(useRolesValue)) {
            return Collections.emptyList();
        }

        // 将 json 字符串转换为 List<String> 集合
        return objectMapper.readValue(useRolesValue, new TypeReference<>() {
        });
    }
}
