package br.com.blackdog.montisol.almoxarife.repository;

import br.com.blackdog.montisol.almoxarife.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
