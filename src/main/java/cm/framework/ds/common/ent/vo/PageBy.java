package cm.framework.ds.common.ent.vo;

public class PageBy {

    private int page;
    private int size;

    public PageBy(int page, int size) {
        switch (page) {
            case 0:
                this.page = page;
                break;
            default:
                this.page = (page * size);
                break;
        }
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
