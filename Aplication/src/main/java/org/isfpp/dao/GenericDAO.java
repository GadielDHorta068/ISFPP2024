package org.isfpp.dao;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public interface GenericDAO<T, ID extends Serializable> {

    void save(T entity);

    Object findByCode(String code);

    List<T> findAll();

    void update(T entity);

    void delete(T entity);
}
