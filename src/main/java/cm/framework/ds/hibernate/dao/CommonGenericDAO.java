package cm.framework.ds.hibernate.dao;

import cm.framework.ds.common.ent.vo.KeyValue;
import cm.travelpost.tp.common.utils.CollectionsUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonGenericDAO extends GenericDAOImpl {

    private Map map = new HashMap();
    private String[] filters = null;


    protected void initializeMap(){
       this.map = new HashMap();
    }
    protected void initializeFilters(){
        this.filters = null;
    }

    protected void initialize(){
        initializeMap();
        initializeFilters();
    }
    public void setMap(KeyValue ...keyValues) {
        initializeMap();
        List<KeyValue> mapValues =Arrays.asList(keyValues);
        mapValues.stream().forEach(k->
            this.map.put(k.getKey(), k.getValue())
        );
    }

    public Map getMap() {
        return map;
    }
    protected void setFilters(String ...filters){

        initializeFilters();
        List<String> listFilters =Arrays.asList(filters);

        int size  = CollectionsUtils.size(listFilters);
        this.filters = new String[size];
         listFilters.stream().forEach(f->
             this.filters[listFilters.indexOf(f)]=f
         );
    }

    protected String [] getFilters(){
        return this.filters;
    }

}
