package org.mura.controller;

import org.mura.exception.UnauthorizedException;
import org.mura.model.common.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 * @author Akutagawa Murasame
 * @date 2022/4/1 14:00
 */
@RestController
public class OtherController {
    /**
     * 401没有权限异常
     */
    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean unauthorized(HttpServletRequest request) {
        throw new UnauthorizedException(request.getAttribute("msg").toString());
    }
}
