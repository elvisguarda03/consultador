package br.com.guacom.consultador.domain.repository;

import java.util.Optional;

public interface BaseRepository<T> {
    T save(T obj);

    Optional<T> findById(Long id);

    Optional<T> findByNome(String nome);

    void delete(T obj);
}
