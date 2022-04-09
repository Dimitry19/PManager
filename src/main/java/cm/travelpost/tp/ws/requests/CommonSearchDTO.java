package cm.travelpost.tp.ws.requests;

public class CommonSearchDTO {

    protected boolean and;

    protected Integer page;

    protected  Integer size;

    public boolean isAnd() {
        return and;
    }

    public void setAnd(boolean and) {
        this.and = and;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
