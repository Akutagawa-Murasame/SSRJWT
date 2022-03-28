package org.mura.model;

import org.mura.model.entity.Permission;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:03
 *
 * dto是data transfer object，用作数据载体，不包含任何业务代码
 *
 * 权限
 */
@Table(name = "permission")
public class PermissionDto extends Permission implements Serializable {
    private static final long serialVersionUID = 2342125681951782518L;
}