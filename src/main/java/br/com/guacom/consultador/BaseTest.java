package br.com.guacom.consultador;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseTest {
    protected String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
