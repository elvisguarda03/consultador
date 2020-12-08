package br.com.guacom.consultador.application.controller;

import br.com.guacom.consultador.application.dto.ClienteDTO;
import br.com.guacom.consultador.application.form.ClienteForm;
import br.com.guacom.consultador.domain.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/clients")
public class ClienteController {
    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> save(@RequestBody @Valid ClienteForm clienteForm) {
        ClienteDTO clienteDTO = clienteService.save(clienteForm);

        URI location = buildURI(clienteDTO);
        buildRepresentationModel(clienteForm, clienteDTO);

        return created(location).body(clienteDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Long id) {
        return ok(clienteService.findById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<ClienteDTO> findByNome(@RequestParam String nome) {
        return ok(clienteService.findByNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> updateNomeById(@PathVariable Long id, @RequestBody ClienteForm clienteForm) {
        ClienteDTO clienteDTO = clienteService.updateNomeById(id, clienteForm.getNome());

        return accepted().body(clienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClienteDTO> delete(@PathVariable Long id) {
        return ok(ClienteDTO.builder().id(clienteService.deleteById(id))
                .build());
    }

    private URI buildURI(ClienteDTO clienteDTO) {
        URI location = fromCurrentRequest().path("/{id}")
                .buildAndExpand(clienteDTO.getId())
                .toUri();
        return location;
    }

    private void buildRepresentationModel(ClienteForm clienteForm, ClienteDTO clienteDTO) {
        clienteDTO.add(linkTo(methodOn(ClienteController.class).save(clienteForm))
                .slash(clienteDTO.getId())
                .withSelfRel());
    }
}