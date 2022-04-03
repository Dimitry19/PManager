package cm.packagemanager.pmanager.ws.responses;

import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;

import java.util.ArrayList;
import java.util.List;

public class PaginateResponse extends WSCommonResponseVO {

    List results;
    int count;


    public PaginateResponse(int count, List results) {
        this.count = count;
        this.results = results;
    }

    public PaginateResponse() {
        this.count = 0;
        this.results = new ArrayList();
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
