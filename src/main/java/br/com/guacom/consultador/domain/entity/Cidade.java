package br.com.guacom.consultador.domain.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity(name = "CIDADE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "NOME", unique = true, nullable = false)
    private String nome;

    @Column(name = "ESTADO", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String estado;
}
