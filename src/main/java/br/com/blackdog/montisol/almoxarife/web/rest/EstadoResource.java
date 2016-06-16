package br.com.blackdog.montisol.almoxarife.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.blackdog.montisol.almoxarife.domain.Estado;
import br.com.blackdog.montisol.almoxarife.repository.EstadoRepository;
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
 * REST controller for managing Estado.
 */
@RestController
@RequestMapping("/api")
public class EstadoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoResource.class);
        
    @Inject
    private EstadoRepository estadoRepository;
    
    /**
     * POST  /estados : Create a new estado.
     *
     * @param estado the estado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estado, or with status 400 (Bad Request) if the estado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estado> createEstado(@Valid @RequestBody Estado estado) throws URISyntaxException {
        log.debug("REST request to save Estado : {}", estado);
        if (estado.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("estado", "idexists", "A new estado cannot already have an ID")).body(null);
        }
        Estado result = estadoRepository.save(estado);
        return ResponseEntity.created(new URI("/api/estados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estado", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estados : Updates an existing estado.
     *
     * @param estado the estado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estado,
     * or with status 400 (Bad Request) if the estado is not valid,
     * or with status 500 (Internal Server Error) if the estado couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estado> updateEstado(@Valid @RequestBody Estado estado) throws URISyntaxException {
        log.debug("REST request to update Estado : {}", estado);
        if (estado.getId() == null) {
            return createEstado(estado);
        }
        Estado result = estadoRepository.save(estado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estado", estado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estados : get all the estados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estados in body
     */
    @RequestMapping(value = "/estados",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estado> getAllEstados() {
        log.debug("REST request to get all Estados");
        List<Estado> estados = estadoRepository.findAll();
        return estados;
    }

    /**
     * GET  /estados/:id : get the "id" estado.
     *
     * @param id the id of the estado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estado, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/estados/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estado> getEstado(@PathVariable Long id) {
        log.debug("REST request to get Estado : {}", id);
        Estado estado = estadoRepository.findOne(id);
        return Optional.ofNullable(estado)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estados/:id : delete the "id" estado.
     *
     * @param id the id of the estado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/estados/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        log.debug("REST request to delete Estado : {}", id);
        estadoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estado", id.toString())).build();
    }

}
