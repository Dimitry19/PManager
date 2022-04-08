package cm.travelpost.tp.common.exception;

public class DashboardException extends RuntimeException {

    private static final long serialVersionUID = -3128681006635769411L;

    private String code ;
    public DashboardException(String message) {
        super(message);
    }

    public DashboardException(String code, String message){
        super(message);
        this.code=code;
        throw this;
    }

    public String getCode() {
        return code;
    }
}
