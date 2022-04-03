package org.mura.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.mura.config.jwt.JWTToken;
import org.mura.config.redis.JedisUtil;
import org.mura.mapper.PermissionMapper;
import org.mura.mapper.RoleMapper;
import org.mura.mapper.UserMapper;
import org.mura.model.PermissionDto;
import org.mura.model.RoleDto;
import org.mura.model.UserDto;
import org.mura.model.common.Constant;
import org.mura.util.encrypt.EncryptAESUtil;
import org.mura.config.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/29 13:01
 */
@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public UserRealm(UserMapper userMapper, RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    /**
     * 必须重写此方法，否则shiro会报错
     *
     * @return 是否支持JWTToken类型
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        UserDto userDto = new UserDto();

        userDto.setAccount(JWTUtil.getAccount(principals.toString()));

        // 查询用户角色
        List<RoleDto> roleDtos = roleMapper.findRoleByUser(userDto);
        for (RoleDto roleDto : roleDtos) {
            simpleAuthorizationInfo.addRole(roleDto.getName());

            // 根据用户角色查询权限
            List<PermissionDto> permissionDtos = permissionMapper.findPermissionByRole(roleDto);
            for (PermissionDto permissionDto : permissionDtos) {
                simpleAuthorizationInfo.addStringPermission(permissionDto.getPerCode());
            }
        }

        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行账户正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
//        获得密钥，调用的是我们自定义的JWTToken中的方法
        String token = (String) auth.getCredentials();

//        解密获得account，用于和数据库对比
        String account = JWTUtil.getAccount(token);

        if (null == account) {
            throw new AuthenticationException("account in token is null");
        }

        // 查询用户是否存在
        UserDto userDto = new UserDto();
        userDto.setAccount(account);
        userDto = userMapper.selectOne(userDto);

        if (null == userDto) {
            throw new AuthenticationException("account didn't exists");
        }

//        AES对称解密
        String key = EncryptAESUtil.decrypt(userDto.getPassword());

//        获取的密码是account+password
//        缓存过期则认证失败
        if (!JWTUtil.verify(token, key) ||
                Boolean.FALSE.equals(JedisUtil.exists(Constant.PREFIX_SHIRO_ACCESS + account))) {
            throw new AuthenticationException("Username or password error or cache expired");
        }

        return new SimpleAuthenticationInfo(token, token, "userRealm");
    }
}
