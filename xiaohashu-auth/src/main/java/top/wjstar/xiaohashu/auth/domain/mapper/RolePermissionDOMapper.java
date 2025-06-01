package top.wjstar.xiaohashu.auth.domain.mapper;

import org.apache.ibatis.annotations.Param;
import top.wjstar.xiaohashu.auth.domain.dataobject.RolePermissionDO;

import java.util.List;

public interface RolePermissionDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionDO record);

    int insertSelective(RolePermissionDO record);

    RolePermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionDO record);

    int updateByPrimaryKey(RolePermissionDO record);

    /**
     * 根据角色 id 集合批量查询权限数据
     *
     * @param roleIds 角色 id 集合
     * @return
     */
    List<RolePermissionDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);
}