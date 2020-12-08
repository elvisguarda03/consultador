package br.com.guacom.consultador.infrastructure.repository;

import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.repository.ClienteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositoryJpa extends ClienteRepository, JpaRepository<Cliente, Long> {

    @Override
    @Query("update CLIENTE set nome = :pNome where id = :pId")
    @Modifying
    void updateNomeById(@Param("pId") Long id, @Param("pNome") String nome);
}