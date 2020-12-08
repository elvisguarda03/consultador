package br.com.guacom.consultador.mock;

import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.form.CidadeForm;
import br.com.guacom.consultador.domain.entity.Cidade;

public class CidadeMock {
    public static CidadeDTO buildCidadeDTO() {
        return CidadeDTO.builder()
                .id(1L)
                .estado("BA")
                .nome("Salvador")
                .build();
    }

    public static CidadeForm buildCidadeForm() {
        return CidadeForm.builder()
                .estado("BA")
                .nome("Salvador")
                .build();
    }

    public static Cidade buildCidade() {
        return Cidade.builder()
                .id(1L)
                .estado("BA")
                .nome("Salvador")
                .build();
    }
}
