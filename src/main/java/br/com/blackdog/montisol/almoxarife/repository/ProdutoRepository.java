package br.com.blackdog.montisol.almoxarife.repository;

import br.com.blackdog.montisol.almoxarife.domain.Produto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Produto entity.
 */
@SuppressWarnings("unused")
public interface ProdutoRepository extends JpaRepository<Produto,Long> {

}
