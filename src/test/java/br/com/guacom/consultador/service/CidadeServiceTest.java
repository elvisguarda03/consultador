package br.com.guacom.consultador.service;

import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.form.CidadeForm;
import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.repository.CidadeRepository;
import br.com.guacom.consultador.domain.service.CidadeService;
import br.com.guacom.consultador.mock.CidadeMock;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static br.com.guacom.consultador.mock.CidadeMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CidadeServiceTest {
    @InjectMocks
    private CidadeService cidadeService;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegisterCidade() {
        CidadeForm cidadeForm = buildCidadeForm();
        Cidade cidade = buildCidade();

        when(modelMapper.map(cidadeForm, Cidade.class)).thenReturn(cidade);
        when(cidadeRepository.save(cidade)).thenReturn(cidade);

        cidadeService.save(cidadeForm);

        verify(modelMapper, atLeastOnce()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).save(any());
    }

    @Test
    public void shouldFindCidadeById() {
        Cidade cidade = buildCidade();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);
        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.of(cidade));

        CidadeDTO cidadeDTORetornada = cidadeService.findById(1L);

        verify(modelMapper, atLeastOnce()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).findById(anyLong());

        assertThat(cidadeDTORetornada, equalTo(cidadeDTO));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntFindCidadeByIdWhenIdNotExists() {
        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        cidadeService.findById(2L);

        verify(modelMapper, never()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).findById(anyLong());
    }

    @Test
    public void shouldFindCidadeByNome() {
        Cidade cidade = buildCidade();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);
        when(cidadeRepository.findByNome(anyString())).thenReturn(Optional.of(cidade));

        CidadeDTO cidadeDTORetornada = cidadeService.findByNome(cidade.getNome());

        verify(modelMapper, atLeastOnce()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).findByNome(anyString());

        assertThat(cidadeDTORetornada, equalTo(cidadeDTO));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntFindCidadeByNomeWhenNomeNotExists() {
        when(cidadeRepository.findByNome(anyString())).thenReturn(Optional.empty());

        cidadeService.findByNome("");

        verify(modelMapper, never()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).findByNome(anyString());
    }

    @Test
    public void shouldFindCidadeByEstado() {
        Cidade cidade = buildCidade();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);
        when(cidadeRepository.findByEstado(anyString())).thenReturn(Optional.of(cidade));

        CidadeDTO cidadeDTORetornada = cidadeService.findByEstado(cidade.getEstado());

        verify(modelMapper, atLeastOnce()).map(any(), any());
        verify(cidadeRepository, atLeastOnce()).findByEstado(anyString());

        assertThat(cidadeDTORetornada, equalTo(cidadeDTO));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntFindCidadeByEstadoWhenEstadoNotExists() {
        when(cidadeRepository.findByEstado(anyString())).thenReturn(Optional.empty());

        cidadeService.findByEstado("");

        verify(modelMapper, never()).map(anyString(), anyString());
        verify(cidadeRepository, atLeastOnce()).findByEstado(anyString());
    }
}