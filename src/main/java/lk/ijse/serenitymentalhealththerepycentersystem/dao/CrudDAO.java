package lk.ijse.serenitymentalhealththerepycentersystem.dao;

import java.util.List;

public interface CrudDAO <T> extends SuperDAO{

    boolean save(T entity);
    boolean update(T entity);
    boolean delete(String id);
    T search(String id);
    List<T> getAll();

}
