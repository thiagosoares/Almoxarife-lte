package br.com.blackdog.montisol.almoxarife.repository;

import br.com.blackdog.montisol.almoxarife.domain.Municipio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Municipio entity.
 */
@SuppressWarnings("unused")
public interface MunicipioRepository extends JpaRepository<Municipio,Long> {

}
