package org.mura.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.mura.util.JedisUtil;
import org.mura.exception.CustomException;
import org.mura.exception.UnauthorizedException;
import org.mura.model.UserDto;
import org.mura.model.common.Constant;
import org.mura.model.common.ResponseBean;
import org.mura.service.IUserService;
import org.mura.util.common.PropertiesUtil;
import org.mura.util.EncryptAESUtil;
import org.mura.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 23:26
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户
     * <p>
     * Logical.AND，代表权限列表中的所有权限都要满足
     * <p>
     * GetMapping == GetMapping("") --> GetMapping("/")
     */
    @GetMapping
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public ResponseBean user() {
        List<UserDto> userDtos = userService.selectAll();

        if (userDtos == null || userDtos.size() <= 0) {
            throw new CustomException("search success");
        }

        return new ResponseBean(200, "search success", userDtos);
    }

    /**
     * 查询Redis中的Token，获取在线人数
     */
    @GetMapping("/online")
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public ResponseBean online(){
        List<UserDto> userDtos = new ArrayList<>();

        // 查询所有Redis键，根据正则表达式，相当于redis中keys PREFIX_SHIRO_ACCESS*
        Set<String> keys = JedisUtil.keysS(Constant.PREFIX_SHIRO_ACCESS + "*");
        assert keys != null;
        for (String key : keys) {
            if(Boolean.TRUE.equals(JedisUtil.exists(key))){
//                null对象也会被加入
                userDtos.add((UserDto) JedisUtil.getObject(key));
            }
        }

//      null对象也会被加入，所以size大于0时不一定有用户，等于0时一定有异常
        if(userDtos.size() <= 0){
            throw new CustomException("Query Failure");
        }

        return new ResponseBean(200, "Query was successful", userDtos);
    }

    /**
     * 获取指定用户
     */
    @GetMapping("/{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public ResponseBean findById(@PathVariable("id") Integer id) {
        UserDto userDto = userService.selectByPrimaryKey(id);

        if (userDto == null) {
            throw new CustomException("search fail");
        }

        return new ResponseBean(200, "search success", userDto);
    }

    /**
     * 增加用户
     * <p>
     * 在上传文件的时候，spring框架会自动装配文件类型, 使用@RequestBody接收对象，
     * 所对应的content-type :application/json。所以当使用@RequestBody和文件上传的时候，会报错
     * 所以可以删掉这个注解，或者前端调用接口带上Content-type:application/json，或者使用RequestParam注解
     * <p>
     * 现在新增user但是没有新增权限
     */
    @PostMapping
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public ResponseBean add(UserDto userDto) {
//        判断当前账号是否存在
        UserDto userDtoTemp = new UserDto();
        userDtoTemp.setAccount(userDto.getAccount());
        userDtoTemp = userService.selectOne(userDtoTemp);
        if (userDtoTemp != null && StringUtils.isNotBlank(userDtoTemp.getPassword())) {
            throw new CustomException("account already exists");
        }

        userDto.setRegTime(new Date());

//        密码以帐号 + 密码的形式进行AES加密
        if (userDto.getPassword().length() > 8) {
            throw new CustomException("maximum size of password is 8");
        }

        String key = EncryptAESUtil.encrypt(userDto.getAccount() + userDto.getPassword());
        userDto.setPassword(key);

        int count = userService.insert(userDto);

        if (count <= 0) {
            throw new CustomException("add fail");
        }

        return new ResponseBean(200, "add success", userDto);
    }

    /**
     * 更新用户
     */
    @PutMapping
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public ResponseBean update(UserDto userDto) {
//        查询密码，如果密码没有更改就不用进行数据库的密码字段更新
        UserDto userDtoTemp = new UserDto();
        userDtoTemp.setId(userDto.getId());
        userDtoTemp = userService.selectOne(userDtoTemp);

        if (!userDtoTemp.getPassword().equals(userDto.getPassword())) {
            if (userDto.getPassword().length() > 8) {
                throw new CustomException("maximum size of password is 8");
            }

            String key = EncryptAESUtil.encrypt(userDto.getAccount() + userDto.getPassword());
            userDto.setPassword(key);
        }

        int count = userService.updateByPrimaryKeySelective(userDto);

        if (count <= 0) {
            throw new CustomException("update fail");
        }

        return new ResponseBean(200, "update success", userDto);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public ResponseBean delete(@PathVariable("id") Integer id) {
        int count = userService.deleteByPrimaryKey(id);

        if (count <= 0) {
            throw new CustomException("delete fail, id is not exists");
        }

        return new ResponseBean(200, "delete success", null);
    }

    /**
     * 剔除在线用户
     */
    @DeleteMapping("/online/{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public ResponseBean deleteOnline(@PathVariable("id") Integer id){
        UserDto userDto = userService.selectByPrimaryKey(id);
        String target = Constant.PREFIX_SHIRO_ACCESS + userDto.getAccount();
        if(Boolean.TRUE.equals(JedisUtil.exists(target))){
            Long delNumber = JedisUtil.delKey(target);
            if(delNumber != null && delNumber > 0) {
                return new ResponseBean(200, "Delete Success", null);
            }
        }

        throw new CustomException("Deletion Failed. Account does not exist.");
    }

    /**
     * 登录授权
     */
    @PostMapping("/login")
    public ResponseBean login(UserDto userDto) {
        UserDto userDtoTemp = new UserDto();

        userDtoTemp.setAccount(userDto.getAccount());
        userDtoTemp = userService.selectOne(userDtoTemp);

        if (userDtoTemp == null) {
            throw new UnauthorizedException("The account does not exist");
        }

//        进行AES解密
        String key = EncryptAESUtil.decrypt(userDtoTemp.getPassword());

//        对比，因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        if (key.equals(userDto.getAccount() + userDto.getPassword())) {
//             获取Token过期时间，读取配置文件
            PropertiesUtil.readProperties("config.properties");
            String tokenExpireTime = PropertiesUtil.getProperty("tokenExpireTime");

//             设置Redis中的Token
            JedisUtil.setObject(Constant.PREFIX_SHIRO_ACCESS + userDto.getAccount(), userDtoTemp, Integer.parseInt(tokenExpireTime));

//            ResponseBean中携带了token
            return new ResponseBean(200, "Login Success", JWTUtil.sign(userDto.getAccount(), key));
        } else {
            throw new UnauthorizedException("account or password error");
        }
    }

    /**
     * 测试登录
     */
    @GetMapping("/article")
    public ResponseBean article() {
        Subject subject = SecurityUtils.getSubject();
        // 登录了返回true
        if (subject.isAuthenticated()) {
            return new ResponseBean(200, "You are already logged in", null);
        } else {
            return new ResponseBean(200, "You are guest", null);
        }
    }

    /**
     * `@RequiresAuthentication等价于subject.isAuthenticated()返回true
     * <p>
     * 没登陆会报错
     */
    @GetMapping("/article2")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are already logged in", null);
    }
}
