package br.com.guacom.consultador.infrastructure.repository;

import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.repository.ClienteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositoryJpa extends ClienteRepository, JpaRepository<Cliente, Long> {
}