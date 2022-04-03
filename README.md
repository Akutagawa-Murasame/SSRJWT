[学习了ShiroJWT项目](https://github.com/dolyw/ShiroJwt)

---

环境搭建：
- 1、执行sql/MySQL.sql脚本
- 2、配置application.yml
- 3、在redisUtil中配置redis连接地址
- 3、运行springboot项目即可

---

在dto类中使用到了serialVersionUID，这个其实是自动生成的（就算不写，jdk也会自动生成）
[IDEA自动生成serialVersionUID](https://www.jianshu.com/p/5dfa065b7890)

---

AES是对称加密算法，如果想实现非对称请使用shiro的MD5摘要算法

---

[docker安装redis](https://cloud.tencent.com/developer/article/1670205)

---

只有redis和token都没过期才能认证成功；Redis中的Value数据就是在线用户，如果删除某个Value数据，那这个Token之后也无法通过认证了，就相当于控制了用户的登录，可以踢出用户

---

