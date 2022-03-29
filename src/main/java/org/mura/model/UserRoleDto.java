package org.mura.model;

import org.mura.model.entity.UserRole;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:03
 *
 * 用户角色中转表
 */
@Table(name = "user_role")
public class UserRoleDto extends UserRole implements Serializable {
    private static final long serialVersionUID = 1095151870902690202L;
}
