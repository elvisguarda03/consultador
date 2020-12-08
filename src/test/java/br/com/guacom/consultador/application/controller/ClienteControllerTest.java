package br.com.guacom.consultador.application.controller;

import br.com.guacom.consultador.BaseTest;
import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.dto.ClienteDTO;
import br.com.guacom.consultador.application.form.ClienteForm;
import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.service.CidadeService;
import br.com.guacom.consultador.domain.service.ClienteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;

import static br.com.guacom.consultador.mock.CidadeMock.buildCidadeDTO;
import static br.com.guacom.consultador.mock.ClienteMock.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest extends BaseTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CidadeService cidadeService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void shouldSaveClienteWhenIsValid() throws Exception {
        ClienteForm clienteForm = buildClienteForm();
        Cliente cliente = buildCliente();
        ClienteDTO clienteDTO = buildClienteDTO();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(cidadeService.findById(anyLong())).thenReturn(cidadeDTO);
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(clienteService.save(clienteForm)).thenReturn(clienteDTO);

        mvc.perform(post("/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteForm)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(clienteDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(clienteDTO.getNome())))
                .andExpect(jsonPath("$.idade", is(clienteDTO.getIdade())))
                .andExpect(jsonPath("$.dataNascimento", is(clienteDTO.getDataNascimento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))))
                .andExpect(jsonPath("$.sexo", is(clienteDTO.getSexo().getCodigo())))
                .andExpect(jsonPath("$.cidade.id", is(clienteDTO.getCidade().getId().intValue())));
    }

    @Test
    public void shouldntSaveClienteWhenCidadeIsInValid() throws Exception {
        ClienteForm clienteForm = buildClienteForm();

        when(clienteService.save(clienteForm)).thenThrow(new ResourceNotFoundException("Cidade não encontrada"));

        mvc.perform(post("/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clienteForm)))
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cidade não encontrada")))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldFindCidadeById() throws Exception {
        Cliente cliente = buildCliente();
        ClienteDTO clienteDTO = buildClienteDTO();

        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(clienteService.findById(any())).thenReturn(clienteDTO);

        mvc.perform(get("/v1/clients/{id}", clienteDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(clienteDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(clienteDTO.getNome())));
    }

    @Test
    public void shouldntFindCidadeByIdWhenIdNotExists() throws Exception {
        ClienteDTO clienteDTO = buildClienteDTO();

        when(clienteService.findById(any())).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mvc.perform(get("/v1/clients/{id}", clienteDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cliente não encontrado")))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldFindByNome() throws Exception {
        Cliente cliente = buildCliente();
        ClienteDTO clienteDTO = buildClienteDTO();

        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(clienteService.findByNome(anyString())).thenReturn(clienteDTO);

        mvc.perform(get("/v1/clients/filter").param("nome", clienteDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(clienteDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(clienteDTO.getNome())));
    }

    @Test
    public void shouldntFindByNomeWhenNomeNotExists() throws Exception {
        ClienteDTO clienteDTO = buildClienteDTO();

        when(clienteService.findByNome(anyString())).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mvc.perform(get("/v1/clients/filter").param("nome", clienteDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cliente não encontrado")))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shouldUpdateNomeById() throws Exception {
        ClienteForm clienteForm = buildClienteForm();
        clienteForm.setNome("Elvis de Sousa da Guarda");
        ClienteDTO clienteDTO = buildClienteDTO();
        clienteDTO.setNome(clienteForm.getNome());

        Cliente cliente = buildCliente();

        cliente.setNome(clienteDTO.getNome());
        when(modelMapper.map(cliente, ClienteDTO.class)).thenReturn(clienteDTO);
        when(clienteService.updateNomeById(anyLong(), anyString())).thenReturn(clienteDTO);

        mvc.perform(put("/v1/clients/{id}", clienteDTO.getId())
                .content(asJsonString(clienteForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(clienteDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(clienteDTO.getNome())));
    }

    @Test
    public void shouldntUpdateNomeByIdWhenIdNotExists() throws Exception {
        ClienteForm clienteForm = buildClienteForm();
        clienteForm.setNome("Elvis de Sousa da Guarda");
        ClienteDTO clienteDTO = buildClienteDTO();

        when(clienteService.updateNomeById(anyLong(), anyString())).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mvc.perform(put("/v1/clients/{id}", clienteDTO.getId())
                .content(asJsonString(clienteForm))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cliente não encontrado")))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }

    @Test
    public void shoudlDeleteCidadeById() throws Exception {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .id(1L)
                .build();

        when(clienteService.deleteById(clienteDTO.getId())).thenReturn(clienteDTO.getId());

        mvc.perform(delete("/v1/clients/{id}", clienteDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(clienteDTO.getId().intValue())));
    }

    @Test
    public void shoudlntDeleteCidadeByIdWhenIdNotExists() throws Exception {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .id(1L)
                .build();

        when(clienteService.deleteById(clienteDTO.getId())).thenThrow(new ResourceNotFoundException("Cliente não encontrado"));

        mvc.perform(delete("/v1/clients/{id}", clienteDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cliente não encontrado")))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.value())));
    }
}