package org.mura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 17:18
 *
 * 使用的是tk的MapperScan注解
 */
@SpringBootApplication
@MapperScan("org.mura.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
