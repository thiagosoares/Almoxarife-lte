package br.com.blackdog.montisol.almoxarife.repository;

import br.com.blackdog.montisol.almoxarife.domain.Estado;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estado entity.
 */
@SuppressWarnings("unused")
public interface EstadoRepository extends JpaRepository<Estado,Long> {

}
