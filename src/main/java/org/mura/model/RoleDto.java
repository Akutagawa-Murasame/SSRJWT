package org.mura.model;

import org.mura.model.entity.Role;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:03
 *
 * 角色
 */
@Table(name = "role")
public class RoleDto extends Role implements Serializable {
    private static final long serialVersionUID = 1344480926839650865L;
}
