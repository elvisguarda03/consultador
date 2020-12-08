package br.com.guacom.consultador.application.controller;

import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.form.CidadeForm;
import br.com.guacom.consultador.domain.service.CidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cities")
public class CidadeController {
    private final CidadeService cidadeService;

    @PostMapping
    public ResponseEntity<CidadeDTO> save(@RequestBody @Valid CidadeForm cidadeForm) {
        CidadeDTO cidadeDTO = cidadeService.save(cidadeForm);

        URI location = buildURI(cidadeDTO);
        buildRepresentationModel(cidadeForm, cidadeDTO);

        return created(location).body(cidadeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeDTO> findById(@PathVariable Long id) {
        return ok(cidadeService.findById(id));
    }


    @GetMapping("/filter")
    public ResponseEntity<CidadeDTO> findByFilter(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String nome) {
        if (!Objects.isNull(estado) && !estado.isEmpty()) {
            return ok(cidadeService.findByEstado(estado));
        }

        return ok(cidadeService.findByNome(nome));
    }

    private URI buildURI(CidadeDTO cidadeDTO) {
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(cidadeDTO.getId())
                .toUri();
        return location;
    }

    private void buildRepresentationModel(CidadeForm cidadeForm, CidadeDTO cidadeDTO) {
        cidadeDTO.add(linkTo(methodOn(CidadeController.class).save(cidadeForm))
                .slash(cidadeDTO.getId())
                .withSelfRel());
    }
}