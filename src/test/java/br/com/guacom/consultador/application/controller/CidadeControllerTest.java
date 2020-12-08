package br.com.guacom.consultador.application.controller;

import br.com.guacom.consultador.BaseTest;
import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.form.CidadeForm;
import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.service.CidadeService;
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

import static br.com.guacom.consultador.mock.CidadeMock.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CidadeController.class)
public class CidadeControllerTest extends BaseTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CidadeService cidadeService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void shouldSaveCidadeWhenIsValid() throws Exception {
        CidadeForm cidadeForm = buildCidadeForm();
        CidadeDTO cidadeDTO = buildCidadeDTO();
        Cidade cidade = buildCidade();

        when(modelMapper.map(cidadeForm, Cidade.class)).thenReturn(cidade);
        when(cidadeService.save(any())).thenReturn(cidadeDTO);
        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);

        mvc.perform(post("/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cidadeForm)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(cidadeDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(cidadeDTO.getNome())))
                .andExpect(jsonPath("$.estado", is(cidadeDTO.getEstado())));
    }

    @Test
    public void shouldntSaveCidadeWhenEstadoIsEmpty() throws Exception {
        CidadeForm cidadeForm = buildCidadeForm();
        cidadeForm.setEstado("");

        mvc.perform(post("/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cidadeForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.errors", is(singletonList(("O campo estado é obrigatório")))))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void shouldntSaveCidadeWhenEstadoAndNomeIsNull() throws Exception {
        CidadeForm cidadeForm = buildCidadeForm();
        cidadeForm.setEstado(null);
        cidadeForm.setNome(null);

        mvc.perform(post("/v1/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(cidadeForm)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors", containsInAnyOrder("O campo estado é obrigatório", "O campo nome é obrigatório")))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void shouldFindCidadeById() throws Exception {
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(cidadeService.findById(any())).thenReturn(cidadeDTO);

        mvc.perform(get("/v1/cities/{id}", cidadeDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(cidadeDTO.getId().intValue())));
    }

    @Test
    public void shouldntFindCidadeByIdWhenIdNotExists() throws Exception {
        when(cidadeService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Cidade não encontrada"));

        mvc.perform(get("/v1/cities/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.error", is("Cidade não encontrada")))
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    public void shouldFindCidadeByEstado() throws Exception {
        Cidade cidade = buildCidade();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(cidadeService.findByEstado(any())).thenReturn(cidadeDTO);
        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);

        mvc.perform(get("/v1/cities/filter").param("estado", cidadeDTO.getEstado())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(cidadeDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(cidadeDTO.getNome())))
                .andExpect(jsonPath("$.estado", is(cidadeDTO.getEstado())));
    }

    @Test
    public void shouldFindCidadeByNome() throws Exception {
        Cidade cidade = buildCidade();
        CidadeDTO cidadeDTO = buildCidadeDTO();

        when(cidadeService.findByNome(any())).thenReturn(cidadeDTO);
        when(modelMapper.map(cidade, CidadeDTO.class)).thenReturn(cidadeDTO);

        mvc.perform(get("/v1/cities/filter").param("nome", cidadeDTO.getNome())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(new MediaType("application", "*+json")))
                .andExpect(jsonPath("$.id", is(cidadeDTO.getId().intValue())))
                .andExpect(jsonPath("$.nome", is(cidadeDTO.getNome())))
                .andExpect(jsonPath("$.estado", is(cidadeDTO.getEstado())));
    }
}