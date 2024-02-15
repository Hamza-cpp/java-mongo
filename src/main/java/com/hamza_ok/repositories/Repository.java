package com.hamza_ok.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    // CRUD Operations

    Optional<T> save(T entity);
    Optional<T> find(ID id);
    List<T> findAll();
    Optional<T> update(T entity, ID id);
    boolean delete(ID id);
}
