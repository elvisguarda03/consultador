package br.com.guacom.consultador.application.form;

import br.com.guacom.consultador.domain.enums.SexoEnum;
import br.com.guacom.consultador.infrastructure.web.LocalDateDeserializer;
import br.com.guacom.consultador.infrastructure.web.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteForm {
    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotNull(message = "O campo sexo é obrigatório")
    private SexoEnum sexo;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull(message = "O campo dataNascimento é obrigatório")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo idade é obrigatório")
    private Integer idade;

    @NotNull(message = "O campo idCidade é obrigatório")
    private Long idCidade;
}
