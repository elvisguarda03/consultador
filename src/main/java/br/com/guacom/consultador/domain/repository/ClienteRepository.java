package br.com.guacom.consultador.domain.repository;

import br.com.guacom.consultador.domain.entity.Cliente;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends BaseRepository<Cliente> {
    void updateNomeById(@Param("pId") Long id, @Param("pNome") String nome);
}