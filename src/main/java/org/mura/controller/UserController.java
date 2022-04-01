package org.mura.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.mura.exception.UnauthorizedException;
import org.mura.model.UserDto;
import org.mura.model.common.ResponseBean;
import org.mura.service.IUserService;
import org.mura.util.EncryptAESUtil;
import org.mura.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 23:26
 */
@RestController
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
     */
    @GetMapping("/user")
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public Map<String, Object> user() {
        List<UserDto> userDtos = userService.selectAll();

        Map<String, Object> map = new HashMap<>(16);

        map.put("code", "200");
        map.put("data", userDtos);

        return map;
    }

    /**
     * 获取指定用户
     */
    @GetMapping("/user/{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public Map<String, Object> findById(@PathVariable("id") Integer id) {
        UserDto userDto = userService.selectByPrimaryKey(id);

        Map<String, Object> map = new HashMap<>(16);

        map.put("code", "200");
        map.put("data", userDto);

        return map;
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
    @PostMapping("/user")
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public Map<String, Object> add(UserDto userDto) {
        userDto.setRegTime(new Date());

//        密码以帐号 + 密码的形式进行AES加密
        String key = EncryptAESUtil.Encryptor(userDto.getAccount() + userDto.getPassword());
        userDto.setPassword(key);

        int count = userService.insert(userDto);

        Map<String, Object> map = new HashMap<>(16);

        map.put("code", "200");
        map.put("count", count);
        map.put("data", userDto);

        return map;
    }

    /**
     * 更新用户
     */
    @PutMapping("/user")
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public Map<String, Object> update(UserDto userDto) {
//        密码以帐号+密码的形式进行AES加密
        String key = EncryptAESUtil.Encryptor(userDto.getAccount() + userDto.getPassword());
        userDto.setPassword(key);

        int count = userService.updateByPrimaryKeySelective(userDto);

        Map<String, Object> map = new HashMap<>(16);

        map.put("code", "200");
        map.put("count", count);
        map.put("data", userDto);

        return map;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/user/{id}")
    @RequiresPermissions(logical = Logical.AND, value = {"user:edit"})
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        int count = userService.deleteByPrimaryKey(id);

        Map<String, Object> map = new HashMap<>(16);

        map.put("code", "200");
        map.put("count", count);

        return map;
    }

    /**
     * 登录授权
     */
    @PostMapping("/user/login")
    public ResponseBean login(UserDto userDto) {
        UserDto userDtoTemp = new UserDto();

        userDtoTemp.setAccount(userDto.getAccount());
        userDtoTemp = userService.selectOne(userDtoTemp);

        // 进行AES解密
        String key = EncryptAESUtil.Decryptor(userDtoTemp.getPassword());

//        对比，因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码
        assert key != null;
        if (key.equals(userDto.getAccount() + userDto.getPassword())) {
//            ResponseBean中携带了token
            return new ResponseBean(200, "Login success", JWTUtil.sign(userDto.getAccount(), userDto.getPassword()));
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * 测试登录
     */
    @GetMapping("/user/article")
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
    @GetMapping("/user/article2")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are already logged in", null);
    }

    /**
     * 401没有权限异常
     */
    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean unauthorized() {
        return new ResponseBean(401, "Unauthorized", null);
    }
}
