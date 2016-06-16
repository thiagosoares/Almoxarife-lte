package br.com.blackdog.montisol.almoxarife.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.blackdog.montisol.almoxarife.domain.Municipio;
import br.com.blackdog.montisol.almoxarife.repository.MunicipioRepository;
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
 * REST controller for managing Municipio.
 */
@RestController
@RequestMapping("/api")
public class MunicipioResource {

    private final Logger log = LoggerFactory.getLogger(MunicipioResource.class);
        
    @Inject
    private MunicipioRepository municipioRepository;
    
    /**
     * POST  /municipios : Create a new municipio.
     *
     * @param municipio the municipio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new municipio, or with status 400 (Bad Request) if the municipio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/municipios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Municipio> createMunicipio(@Valid @RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to save Municipio : {}", municipio);
        if (municipio.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("municipio", "idexists", "A new municipio cannot already have an ID")).body(null);
        }
        Municipio result = municipioRepository.save(municipio);
        return ResponseEntity.created(new URI("/api/municipios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("municipio", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /municipios : Updates an existing municipio.
     *
     * @param municipio the municipio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated municipio,
     * or with status 400 (Bad Request) if the municipio is not valid,
     * or with status 500 (Internal Server Error) if the municipio couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/municipios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Municipio> updateMunicipio(@Valid @RequestBody Municipio municipio) throws URISyntaxException {
        log.debug("REST request to update Municipio : {}", municipio);
        if (municipio.getId() == null) {
            return createMunicipio(municipio);
        }
        Municipio result = municipioRepository.save(municipio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("municipio", municipio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /municipios : get all the municipios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of municipios in body
     */
    @RequestMapping(value = "/municipios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Municipio> getAllMunicipios() {
        log.debug("REST request to get all Municipios");
        List<Municipio> municipios = municipioRepository.findAll();
        return municipios;
    }

    /**
     * GET  /municipios/:id : get the "id" municipio.
     *
     * @param id the id of the municipio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the municipio, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/municipios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Municipio> getMunicipio(@PathVariable Long id) {
        log.debug("REST request to get Municipio : {}", id);
        Municipio municipio = municipioRepository.findOne(id);
        return Optional.ofNullable(municipio)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /municipios/:id : delete the "id" municipio.
     *
     * @param id the id of the municipio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/municipios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMunicipio(@PathVariable Long id) {
        log.debug("REST request to delete Municipio : {}", id);
        municipioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("municipio", id.toString())).build();
    }

}
