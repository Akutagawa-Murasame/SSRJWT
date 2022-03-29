package org.mura.model;

import org.mura.model.entity.RolePermission;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:03
 *
 * 角色权限中转表
 */
@Table(name = "role_permission")
public class RolePermissionDto extends RolePermission implements Serializable {
    private static final long serialVersionUID = 5847835639840746609L;
}
