package br.com.guacom.consultador.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum SexoEnum {
    MASCULINO("M"),
    FEMININO("F");

    private String codigo;

    SexoEnum(String codigo) {
        this.codigo = codigo;
    }

    @JsonValue
    public String getCodigo() {
        return codigo;
    }

    @JsonCreator
    public static SexoEnum fromText(String text) {
        return Stream.of(SexoEnum.values()).filter(targetEnum -> targetEnum.getCodigo().equals(text)).findFirst().orElse(null);
    }
}
