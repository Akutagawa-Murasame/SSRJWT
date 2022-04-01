package org.mura.exception;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/1 13:55
 *
 * 自定义异常
 */
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 5983190700703961468L;

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    protected CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
