package br.com.blackdog.montisol.almoxarife.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.blackdog.montisol.almoxarife.domain.enumeration.Status;

/**
 * A Estado.
 */
@Entity
@Table(name = "estado")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 2, max = 200)
    @Pattern(regexp = "undefined")
    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @NotNull
    @Size(min = 2, max = 200)
    @Pattern(regexp = "undefined")
    @Column(name = "uf", length = 200, nullable = false)
    private String uf;

    @NotNull
    @Size(min = 2, max = 200)
    @Pattern(regexp = "undefined")
    @Column(name = "codigo_ibge", length = 200, nullable = false)
    private String codigoIBGE;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCodigoIBGE() {
        return codigoIBGE;
    }

    public void setCodigoIBGE(String codigoIBGE) {
        this.codigoIBGE = codigoIBGE;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Estado estado = (Estado) o;
        if(estado.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, estado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Estado{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", uf='" + uf + "'" +
            ", codigoIBGE='" + codigoIBGE + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
