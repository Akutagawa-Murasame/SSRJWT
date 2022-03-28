package org.mura.exception;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 23:07
 *
 * 用户未授权异常
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
