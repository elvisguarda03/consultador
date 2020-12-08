package br.com.guacom.consultador.domain.repository;

import br.com.guacom.consultador.domain.entity.Cidade;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static br.com.guacom.consultador.mock.CidadeMock.buildCidade;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CidadeRepositoryTest {
    @Autowired
    private CidadeRepository cidadeRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldSaveCidade() {
        Cidade cidade = buildCidade();
        cidadeRepository.save(cidade);

        assertThat(cidade.getId()).isNotNull();
        assertThat(cidade.getNome()).isNotBlank();
        assertThat(cidade.getEstado()).isNotBlank();
        assertThat(cidade.getNome()).isEqualTo("Salvador");
        assertThat(cidade.getEstado()).isEqualTo("BA");
    }

    @Test
    public void shouldntSaveCidadeWhenNomeIsNullShouldThrownDataIntegrityViolationException() {
        thrown.expect(DataIntegrityViolationException.class);

        Cidade cidade = buildCidade();
        cidade.setNome(null);

        cidadeRepository.save(cidade);
    }

    @Test
    public void shouldFindCidadeById() {
        Cidade cidade = cidadeRepository.save(buildCidade());
        Optional<Cidade> record = cidadeRepository.findById(cidade.getId());

        assertThat(record).isNotEmpty();
        assertThat(record.get().getId()).isNotNull();
        assertThat(record.get().getNome()).isNotBlank();
        assertThat(record.get().getEstado()).isNotBlank();
        assertThat(record.get().getNome()).isEqualTo("Salvador");
        assertThat(record.get().getEstado()).isEqualTo("BA");
    }

    @Test
    public void shouldntFindCidadeByIdWhenIdNotExists() {
        Optional<Cidade> record = cidadeRepository.findById(1L);
        assertThat(record).isEmpty();
    }

    @Test
    public void shouldFindCidadeByEstado() {
        cidadeRepository.save(buildCidade());
        Optional<Cidade> record = cidadeRepository.findByEstado("BA");

        assertThat(record).isNotEmpty();
        assertThat(record.get().getId()).isNotNull();
        assertThat(record.get().getNome()).isNotBlank();
        assertThat(record.get().getEstado()).isNotBlank();
        assertThat(record.get().getNome()).isEqualTo("Salvador");
        assertThat(record.get().getEstado()).isEqualTo("BA");
    }

    @Test
    public void shouldntFindCidadeByEstadoWhenEstadoNotExists() {
        Optional<Cidade> record = cidadeRepository.findByEstado("BA");
        assertThat(record).isEmpty();
    }
}