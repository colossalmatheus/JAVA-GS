package br.com.fiap.javags.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_ELETRO")
public class Eletro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ELETRO")
    @SequenceGenerator(name = "SQ_ELETRO", sequenceName = "SQ_ELETRO", allocationSize = 1)
    @Column(name = "ID_ELETRO")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "TIPO", nullable = false)
    private String tipo;

    @Column(name = "POTENCIA", nullable = false)
    private Double potencia;

    @Column(name = "USO", nullable = false)
    private Double uso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_USUARIO",
            referencedColumnName = "ID_USUARIO",
            foreignKey = @ForeignKey(name = "FK_USUARIO")
    )
    private User user;
}
