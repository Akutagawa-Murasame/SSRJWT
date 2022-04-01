package org.mura.exception;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/28 23:07
 *
 * 用户未授权异常
 */
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 7235159671799647426L;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
