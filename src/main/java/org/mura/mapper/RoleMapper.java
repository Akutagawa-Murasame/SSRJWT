package org.mura.mapper;

import org.mura.model.RoleDto;
import org.mura.model.UserDto;
import org.mura.model.entity.Role;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:27
 */
public interface RoleMapper extends Mapper<Role> {
    /**
     * 根据User查询Role，用户名是模糊查询
     */
    List<RoleDto> findRoleByUser(UserDto userDto);
}