package br.com.blackdog.montisol.almoxarife.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Teste.
 */
@Entity
@Table(name = "teste")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Teste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Pattern(regexp = "undefined")
    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "descricao_2", nullable = false)
    private String descricao2;

    @Column(name = "estoque")
    private Long estoque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao2() {
        return descricao2;
    }

    public void setDescricao2(String descricao2) {
        this.descricao2 = descricao2;
    }

    public Long getEstoque() {
        return estoque;
    }

    public void setEstoque(Long estoque) {
        this.estoque = estoque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Teste teste = (Teste) o;
        if(teste.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, teste.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Teste{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            ", descricao2='" + descricao2 + "'" +
            ", estoque='" + estoque + "'" +
            '}';
    }
}
