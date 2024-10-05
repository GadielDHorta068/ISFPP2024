package org.isfpp.dao;
import org.isfpp.modelo.Connection;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {
    void insert(T entity);

    void update(T entity);

    void erase(T entity);

    List<T> searchAll();

}
