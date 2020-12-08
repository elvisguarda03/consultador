package br.com.guacom.consultador.domain.repository;

import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.entity.Cliente;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static br.com.guacom.consultador.mock.ClienteMock.buildCidade;
import static br.com.guacom.consultador.mock.ClienteMock.buildCliente;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldSaveCliente() {
        Cliente cliente = buildCliente();
        cliente.setId(null);
        cliente.getCidade().setId(null);

        cidadeRepository.save(cliente.getCidade());
        clienteRepository.save(cliente);

        assertThat(cliente.getId()).isNotNull();
        assertThat(cliente.getNome()).isNotBlank();
        assertThat(cliente.getCidade()).isNotNull();
        assertThat(cliente.getNome()).isEqualTo("Bruno da Silva Condenado");
    }

    @Test
    public void shouldntSaveClienteWhenNomeIsNullShouldThrownDataIntegrityViolationException() {
        thrown.expect(DataIntegrityViolationException.class);

        Cliente cliente = buildCliente();
        cliente.setNome(null);
        cliente.getCidade().setId(null);

        cidadeRepository.save(cliente.getCidade());
        clienteRepository.save(cliente);
    }

    @Test
    public void shouldntSaveClienteWhenCidadeNotExistsShouldThrownJpaObjectRetrievalFailureException() {
        thrown.expect(JpaObjectRetrievalFailureException.class);

        Cliente cliente = buildCliente();

        clienteRepository.save(cliente);
    }

    @Test
    public void shouldUpdateNomeById() {
        Cliente cliente = buildCliente();
        cliente.setId(null);
        cliente.getCidade().setId(null);

        cidadeRepository.save(cliente.getCidade());
        cliente = clienteRepository.save(cliente);

        assertThat(cliente.getId()).isNotNull();
        assertThat(cliente.getNome()).isNotBlank();
        assertThat(cliente.getCidade()).isNotNull();
        assertThat(cliente.getNome()).isEqualTo("Bruno da Silva Condenado");

        cliente.setNome("Elvis de Sousa da Guarda");
        cliente = clienteRepository.save(cliente);

        assertThat(cliente.getNome()).isEqualTo("Elvis de Sousa da Guarda");
    }

    @Test
    public void shouldFindClienteById() {
        Cliente cliente = buildCliente();
        cliente.setId(null);
        cliente.getCidade().setId(null);

        cidadeRepository.save(cliente.getCidade());
        clienteRepository.save(cliente);

        Optional<Cliente> record = clienteRepository.findById(cliente.getId());

        assertThat(record).isNotEmpty();
        assertThat(record.get().getId()).isNotNull();
        assertThat(record.get().getNome()).isNotBlank();
        assertThat(record.get().getDataNascimento()).isNotNull();
        assertThat(record.get().getIdade()).isNotNull();
        assertThat(record.get().getSexo()).isNotNull();
        assertThat(record.get().getNome()).isEqualTo("Bruno da Silva Condenado");
        assertThat(record.get().getCidade().getNome()).isEqualTo("Salvador");
        assertThat(record.get().getCidade().getEstado()).isEqualTo("BA");
    }

    @Test
    public void shouldntFindClienteByIdWhenIdNotExists() {
        Optional<Cliente> record = clienteRepository.findById(1L);

        assertThat(record).isEmpty();
    }

    @Test
    public void shouldFindClienteByNome() {
        Cliente cliente = buildCliente();
        Cidade cidade = buildCidade();

        cidadeRepository.save(cidade);
        clienteRepository.save(cliente);

        Optional<Cliente> record = clienteRepository.findByNome(cliente.getNome());

        assertThat(record).isNotEmpty();
        assertThat(record.get().getId()).isNotNull();
        assertThat(record.get().getNome()).isNotBlank();
        assertThat(record.get().getDataNascimento()).isNotNull();
        assertThat(record.get().getIdade()).isNotNull();
        assertThat(record.get().getSexo()).isNotNull();
        assertThat(record.get().getNome()).isEqualTo("Bruno da Silva Condenado");
        assertThat(record.get().getCidade().getNome()).isEqualTo("Salvador");
        assertThat(record.get().getCidade().getEstado()).isEqualTo("BA");
    }

    @Test
    public void shouldntFindClienteByNomeWhenNomeNotExists() {
        Optional<Cliente> record = clienteRepository.findByNome("");

        assertThat(record).isEmpty();
    }
}