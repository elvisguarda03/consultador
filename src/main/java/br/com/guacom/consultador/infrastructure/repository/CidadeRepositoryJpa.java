package br.com.guacom.consultador.infrastructure.repository;

import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.repository.CidadeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepositoryJpa extends CidadeRepository, JpaRepository<Cidade, Long> {
}
