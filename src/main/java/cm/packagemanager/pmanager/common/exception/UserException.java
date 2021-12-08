package cm.packagemanager.pmanager.common.exception;

public class UserException extends UserNotFoundException {

    private static final long serialVersionUID = -3128681006635769411L;

    public UserException(String message) {
        super(message);
    }

}
