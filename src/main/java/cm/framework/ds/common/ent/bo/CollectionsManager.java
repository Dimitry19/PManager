package cm.framework.ds.common.ent.bo;

import cm.framework.ds.common.ent.vo.CommonVO;

import java.util.Collection;


public abstract class CollectionsManager<T extends CommonVO, E extends CommonVO, C extends Collection<E>> {

    protected T t;
    protected E e;
    protected C c;

    protected CollectionsManager(T t, E e, C c) {
        this.t = t;
        this.e = e;
        this.c = c;
    }

}
