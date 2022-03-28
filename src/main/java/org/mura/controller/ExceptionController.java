package org.mura.controller;

import org.apache.shiro.ShiroException;
import org.mura.exception.UnauthorizedException;
import org.mura.model.common.ResponseBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 23:08
 *
 * 全局异常处理
 */
@RestControllerAdvice
public class ExceptionController {
    /**
     * 捕捉Shiro的异常，未授权异常
     *
     * @param e 异常
     * @return 响应状态bean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        return new ResponseBean(401, e.getMessage(), null);
    }

    /**
     * 捕捉UnauthorizedException
     *
     * @return 响应状态bean
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseBean handle401() {
        return new ResponseBean(401, "Unauthorized", null);
    }

    /**
     * 捕捉其他所有异常
     * 异常时会跳转/error，所以这里是设置request
     *
     * @param request 异常转发请求
     * @param ex 异常
     * @return 响应状态bean
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        return new ResponseBean(this.getStatus(request).value(), ex.getMessage(), null);
    }

    /**
     * 获取状态码
     *
     * @param request 异常转发请求
     * @return 响应状态bean
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.valueOf(statusCode);
    }
}
