package cm.packagemanager.pmanager.common.session;

public interface ISessionManager<K,V> {

    public void  addToSession(K k, V v);

    public void  removeToSession(K k);

    public void  replaceIntoSession(K k, V v);

    public V getFromSession(K k);
}
