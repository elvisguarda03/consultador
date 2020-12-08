package br.com.guacom.consultador.domain.entity;

import br.com.guacom.consultador.domain.enums.SexoEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity(name = "CLIENTE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME_COMPLETO", unique = true, nullable = false)
    private String nome;

    @Column(name = "SEXO")
    private SexoEnum sexo;

    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Column(name = "IDADE")
    private Integer idade;

    @ManyToOne
    private Cidade cidade;
}
