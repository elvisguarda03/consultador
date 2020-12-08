package br.com.guacom.consultador.service;

import br.com.guacom.consultador.application.dto.ClienteDTO;
import br.com.guacom.consultador.application.form.ClienteForm;
import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.repository.CidadeRepository;
import br.com.guacom.consultador.domain.repository.ClienteRepository;
import br.com.guacom.consultador.domain.service.ClienteService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static br.com.guacom.consultador.mock.ClienteMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {
    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegisterCliente() {
        ClienteForm clienteForm = buildClienteForm();
        ClienteDTO clienteDTO = buildClienteDTO();
        Cliente cliente = buildCliente();
        Cidade cidade = buildCidade();

        when(modelMapper.map(clienteForm, Cliente.class)).thenReturn(cliente);
        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.of(cidade));
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        ClienteDTO clienteDTORetornado = clienteService.save(clienteForm);

        verify(modelMapper, atLeastOnce()).map(clienteForm, Cliente.class);
        verify(modelMapper, atLeastOnce()).map(cliente, ClienteDTO.class);
        verify(cidadeRepository, atLeastOnce()).findById(anyLong());
        verify(clienteRepository, atLeastOnce()).save(any());

        assertThat(clienteDTORetornado, equalTo(clienteDTO));
        assertThat(clienteDTORetornado.getCidade(), equalTo(clienteDTO.getCidade()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntRegisterClienteWhenCidadeNotExists() {
        ClienteForm clienteForm = buildClienteForm();
        clienteForm.setIdCidade(2L);

        Cliente cliente = buildCliente();

        when(modelMapper.map(clienteForm, Cliente.class)).thenReturn(cliente);
        when(cidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        clienteService.save(clienteForm);

        verify(modelMapper, atLeastOnce()).map(clienteForm, Cliente.class);
        verify(cidadeRepository, atLeastOnce()).findById(anyLong());
        verify(modelMapper, never()).map(cliente, ClienteDTO.class);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void shouldFindClienteById() {
        Cliente cliente = buildCliente();
        ClienteDTO clienteDTO = buildClienteDTO();

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        ClienteDTO clienteDTORetornado = clienteService.findById(cliente.getId());

        verify(modelMapper, atLeastOnce()).map(cliente, ClienteDTO.class);
        verify(clienteRepository, atLeastOnce()).findById(anyLong());

        assertThat(clienteDTORetornado, equalTo(clienteDTO));
        assertThat(clienteDTORetornado.getCidade(), equalTo(clienteDTO.getCidade()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntFindClienteByIdWhenIdNotExists() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        clienteService.findById(2L);

        verify(modelMapper, never()).map(any(), any());
        verify(clienteRepository, atLeastOnce()).findById(anyLong());
    }

    @Test
    public void shouldFindClienteByNome() {
        Cliente cliente = buildCliente();
        ClienteDTO clienteDTO = buildClienteDTO();

        when(clienteRepository.findByNome(anyString())).thenReturn(Optional.of(cliente));
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);

        ClienteDTO clienteDTORetornado = clienteService.findByNome(cliente.getNome());

        verify(modelMapper, atLeastOnce()).map(cliente, ClienteDTO.class);
        verify(clienteRepository, atLeastOnce()).findByNome(anyString());

        assertThat(clienteDTORetornado, equalTo(clienteDTO));
        assertThat(clienteDTORetornado.getCidade(), equalTo(clienteDTO.getCidade()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntFindClienteByNomeWhenNomeNotExists() {
        when(clienteRepository.findByNome(anyString())).thenReturn(Optional.empty());

        clienteService.findByNome("");

        verify(modelMapper, never()).map(any(), any());
        verify(clienteRepository, atLeastOnce()).findByNome(anyString());
    }

    @Test
    public void shouldUpdateNomeById() {
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteDTO.setNome("Elvis de Sousa da Guarda");

        Cliente cliente = buildCliente();

        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        cliente.setNome(clienteDTO.getNome());
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        ClienteDTO clienteDTORetornado = clienteService.updateNomeById(cliente.getId(), "Elvis de Sousa da Guarda");

        verify(modelMapper, atLeastOnce()).map(cliente, ClienteDTO.class);
        verify(clienteRepository, atLeastOnce()).findById(anyLong());
        verify(clienteRepository, atLeastOnce()).save(any());

        assertThat(clienteDTORetornado, equalTo(clienteDTO));
        assertThat(clienteDTORetornado.getNome(), equalTo(clienteDTO.getNome()));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntUpdateNomeByIdWhenIdNotExists() {
        ClienteDTO clienteDTO = buildClienteDTO();

        Cliente cliente = buildCliente();

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        clienteService.updateNomeById(cliente.getId(), "Elvis de Sousa da Guarda");

        verify(modelMapper, never()).map(cliente, ClienteDTO.class);
        verify(clienteRepository, atLeastOnce()).findById(anyLong());
        verify(clienteRepository, never()).findByNome(anyString());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void shouldDeleteById() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .id(1L)
                .build();

        Cliente cliente = buildCliente();

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        clienteService.deleteById(clienteDTO.getId());

        verify(clienteRepository, atLeastOnce()).findById(anyLong());
        verify(clienteRepository, atLeastOnce()).delete(any());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void shouldntDeleteByIdWhenIdNotExists() {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .id(1L)
                .build();

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        clienteService.deleteById(clienteDTO.getId());

        verify(clienteRepository, atLeastOnce()).findById(anyLong());
        verify(clienteRepository, never()).delete(any());
    }
}