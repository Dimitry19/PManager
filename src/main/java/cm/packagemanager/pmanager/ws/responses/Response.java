package cm.packagemanager.pmanager.ws.responses;

public class Response {

    private long retCode;
    private String retDescription;

    public Response() {
    }

    public long getRetCode() {
        return retCode;
    }

    public void setRetCode(long retCode) {
        this.retCode = retCode;
    }

    public String getRetDescription() {
        return retDescription;
    }

    public void setRetDescription(String retDescription) {
        this.retDescription = retDescription;
    }
}
