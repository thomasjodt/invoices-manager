package org.jodt5.invoices.repositories;

import java.sql.SQLException;
import java.util.List;

public interface IRepository<T> {
    List<T> getAll() throws SQLException;
    T getById(Long id) throws SQLException;
    void save(T t) throws SQLException;
    void delete(Long id) throws SQLException;
}
