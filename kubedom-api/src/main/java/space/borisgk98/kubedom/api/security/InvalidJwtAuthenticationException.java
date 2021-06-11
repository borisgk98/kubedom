package space.borisgk98.kubedom.api.security;

public class InvalidJwtAuthenticationException extends RuntimeException {
    public InvalidJwtAuthenticationException() {
    }

    public InvalidJwtAuthenticationException(String s) {
        super(s);
    }

    public InvalidJwtAuthenticationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public InvalidJwtAuthenticationException(Throwable throwable) {
        super(throwable);
    }

    public InvalidJwtAuthenticationException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
