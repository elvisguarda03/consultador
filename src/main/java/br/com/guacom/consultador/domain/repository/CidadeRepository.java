package br.com.guacom.consultador.domain.repository;

import br.com.guacom.consultador.domain.entity.Cidade;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CidadeRepository extends BaseRepository<Cidade> {
    Optional<Cidade> findByEstado(@Param("pEstado") String estado);
}
