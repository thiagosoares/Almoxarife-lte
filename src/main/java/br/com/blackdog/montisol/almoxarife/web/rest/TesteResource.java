package br.com.blackdog.montisol.almoxarife.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.blackdog.montisol.almoxarife.domain.Teste;
import br.com.blackdog.montisol.almoxarife.repository.TesteRepository;
import br.com.blackdog.montisol.almoxarife.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Teste.
 */
@RestController
@RequestMapping("/api")
public class TesteResource {

    private final Logger log = LoggerFactory.getLogger(TesteResource.class);
        
    @Inject
    private TesteRepository testeRepository;
    
    /**
     * POST  /testes : Create a new teste.
     *
     * @param teste the teste to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teste, or with status 400 (Bad Request) if the teste has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/testes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teste> createTeste(@Valid @RequestBody Teste teste) throws URISyntaxException {
        log.debug("REST request to save Teste : {}", teste);
        if (teste.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("teste", "idexists", "A new teste cannot already have an ID")).body(null);
        }
        Teste result = testeRepository.save(teste);
        return ResponseEntity.created(new URI("/api/testes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("teste", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /testes : Updates an existing teste.
     *
     * @param teste the teste to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teste,
     * or with status 400 (Bad Request) if the teste is not valid,
     * or with status 500 (Internal Server Error) if the teste couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/testes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teste> updateTeste(@Valid @RequestBody Teste teste) throws URISyntaxException {
        log.debug("REST request to update Teste : {}", teste);
        if (teste.getId() == null) {
            return createTeste(teste);
        }
        Teste result = testeRepository.save(teste);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("teste", teste.getId().toString()))
            .body(result);
    }

    /**
     * GET  /testes : get all the testes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of testes in body
     */
    @RequestMapping(value = "/testes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Teste> getAllTestes() {
        log.debug("REST request to get all Testes");
        List<Teste> testes = testeRepository.findAll();
        return testes;
    }

    /**
     * GET  /testes/:id : get the "id" teste.
     *
     * @param id the id of the teste to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teste, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/testes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teste> getTeste(@PathVariable Long id) {
        log.debug("REST request to get Teste : {}", id);
        Teste teste = testeRepository.findOne(id);
        return Optional.ofNullable(teste)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testes/:id : delete the "id" teste.
     *
     * @param id the id of the teste to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/testes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTeste(@PathVariable Long id) {
        log.debug("REST request to delete Teste : {}", id);
        testeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("teste", id.toString())).build();
    }

}
