package com.resolutions.application.ports.out;

public interface DataPersist<T> {
    Integer insert(T dto);
    void update(T dto);
    void delete(T dto);
    T get(Integer cod);
}
