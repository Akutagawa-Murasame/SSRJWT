package org.mura.mapper;

import org.mura.model.UserRoleDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:27
 */
@Repository
public interface UserRoleMapper extends Mapper<UserRoleDto> {
}