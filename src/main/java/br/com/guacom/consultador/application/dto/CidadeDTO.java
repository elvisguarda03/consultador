package br.com.guacom.consultador.application.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CidadeDTO extends RepresentationModel<CidadeDTO> {
    @EqualsAndHashCode.Include
    private Long id;
    private String nome;
    private String estado;
}
