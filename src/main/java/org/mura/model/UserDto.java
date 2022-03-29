package org.mura.model;

import org.mura.model.entity.User;

import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 22:03
 *
 * 用户
 */
@Table(name = "user")
public class UserDto extends User implements Serializable {
    private static final long serialVersionUID = 7081350470429029815L;
}
