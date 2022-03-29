package org.mura.mapper;

import org.mura.model.PermissionDto;
import org.mura.model.RoleDto;
import org.mura.model.entity.Permission;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:27
 */
public interface PermissionMapper extends Mapper<Permission> {
    /**
     * 根据Role查询Permission
     */
    List<PermissionDto> findPermissionByRole(RoleDto roleDto);
}