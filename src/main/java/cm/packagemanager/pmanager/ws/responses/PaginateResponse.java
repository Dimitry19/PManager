package cm.packagemanager.pmanager.ws.responses;

import java.util.List;

public  class PaginateResponse {

	List  results;
	int count;


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
