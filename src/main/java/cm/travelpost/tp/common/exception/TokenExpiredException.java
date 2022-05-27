package cm.travelpost.tp.common.exception;

import java.io.IOException;

public class TokenExpiredException extends IOException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
