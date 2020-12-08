package br.com.guacom.consultador.mock;

import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.dto.ClienteDTO;
import br.com.guacom.consultador.application.form.ClienteForm;
import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.enums.SexoEnum;

import java.time.LocalDate;
import java.time.Month;

public class ClienteMock {

    public static Cliente buildCliente() {
        return Cliente.builder()
                .id(1L)
                .nome("Bruno da Silva Condenado")
                .dataNascimento(LocalDate.of(1998, Month.JUNE, 05))
                .idade(22)
                .sexo(SexoEnum.MASCULINO)
                .cidade(buildCidade())
                .build();
    }

    public static ClienteDTO buildClienteDTO() {
        return ClienteDTO.builder()
                .id(1L)
                .idade(22)
                .sexo(SexoEnum.MASCULINO)
                .nome("Bruno da Silva Condenado")
                .dataNascimento(LocalDate.of(1998, Month.JUNE, 05))
                .cidade(builCidadeDTO())
                .build();
    }

    public static ClienteForm buildClienteForm() {
        return ClienteForm.builder()
                .nome("Bruno da Silva Condenado")
                .dataNascimento(LocalDate.of(1998, Month.JUNE, 05))
                .idade(22)
                .sexo(SexoEnum.MASCULINO)
                .idCidade(1L)
                .build();
    }

    public static CidadeDTO builCidadeDTO() {
        return CidadeDTO.builder()
                .id(1L)
                .estado("BA")
                .nome("Salvador")
                .build();
    }

    public static Cidade buildCidade() {
        return Cidade.builder()
                .id(1L)
                .nome("Salvador")
                .estado("BA")
                .build();
    }
}
