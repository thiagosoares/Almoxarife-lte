package br.com.blackdog.montisol.almoxarife.repository;

import br.com.blackdog.montisol.almoxarife.domain.Teste;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Teste entity.
 */
@SuppressWarnings("unused")
public interface TesteRepository extends JpaRepository<Teste,Long> {

}
