package cm.packagemanager.pmanager.configuration.filters;


import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

public interface IFilter extends Filter {


    void error(HttpServletResponse response) throws Exception;

}
