package br.com.guacom.consultador.application.dto;

import br.com.guacom.consultador.domain.enums.SexoEnum;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO extends RepresentationModel<ClienteDTO> {
    @EqualsAndHashCode.Include
    private Long id;
    private String nome;
    private SexoEnum sexo;
    private LocalDate dataNascimento;
    private Integer idade;
    private CidadeDTO cidade;
}
