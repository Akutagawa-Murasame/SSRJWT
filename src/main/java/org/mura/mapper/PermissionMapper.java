package org.mura.mapper;

import org.mura.model.PermissionDto;
import org.mura.model.RoleDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:27
 *
 * 加不加Repository注解都可以，只是不加的话别的地方总提示没有这个bean
 */
@Repository
public interface PermissionMapper extends Mapper<PermissionDto> {
    /**
     * 根据Role查询Permission
     */
    List<PermissionDto> findPermissionByRole(RoleDto roleDto);
}