package br.com.blackdog.montisol.almoxarife.web.rest;

import br.com.blackdog.montisol.almoxarife.MontisolTeste03App;
import br.com.blackdog.montisol.almoxarife.domain.Municipio;
import br.com.blackdog.montisol.almoxarife.repository.MunicipioRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.blackdog.montisol.almoxarife.domain.enumeration.Status;

/**
 * Test class for the MunicipioResource REST controller.
 *
 * @see MunicipioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MontisolTeste03App.class)
@WebAppConfiguration
@IntegrationTest
public class MunicipioResourceIntTest {

    private static final String DEFAULT_NOME = "AA";
    private static final String UPDATED_NOME = "BB";
    private static final String DEFAULT_CODIGO_IBGE = "AA";
    private static final String UPDATED_CODIGO_IBGE = "BB";

    private static final Status DEFAULT_STATUS = Status.INATIVO;
    private static final Status UPDATED_STATUS = Status.ATIVO;

    @Inject
    private MunicipioRepository municipioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMunicipioMockMvc;

    private Municipio municipio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MunicipioResource municipioResource = new MunicipioResource();
        ReflectionTestUtils.setField(municipioResource, "municipioRepository", municipioRepository);
        this.restMunicipioMockMvc = MockMvcBuilders.standaloneSetup(municipioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        municipio = new Municipio();
        municipio.setNome(DEFAULT_NOME);
        municipio.setCodigoIBGE(DEFAULT_CODIGO_IBGE);
        municipio.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createMunicipio() throws Exception {
        int databaseSizeBeforeCreate = municipioRepository.findAll().size();

        // Create the Municipio

        restMunicipioMockMvc.perform(post("/api/municipios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(municipio)))
                .andExpect(status().isCreated());

        // Validate the Municipio in the database
        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeCreate + 1);
        Municipio testMunicipio = municipios.get(municipios.size() - 1);
        assertThat(testMunicipio.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMunicipio.getCodigoIBGE()).isEqualTo(DEFAULT_CODIGO_IBGE);
        assertThat(testMunicipio.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = municipioRepository.findAll().size();
        // set the field null
        municipio.setNome(null);

        // Create the Municipio, which fails.

        restMunicipioMockMvc.perform(post("/api/municipios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(municipio)))
                .andExpect(status().isBadRequest());

        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoIBGEIsRequired() throws Exception {
        int databaseSizeBeforeTest = municipioRepository.findAll().size();
        // set the field null
        municipio.setCodigoIBGE(null);

        // Create the Municipio, which fails.

        restMunicipioMockMvc.perform(post("/api/municipios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(municipio)))
                .andExpect(status().isBadRequest());

        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = municipioRepository.findAll().size();
        // set the field null
        municipio.setStatus(null);

        // Create the Municipio, which fails.

        restMunicipioMockMvc.perform(post("/api/municipios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(municipio)))
                .andExpect(status().isBadRequest());

        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMunicipios() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get all the municipios
        restMunicipioMockMvc.perform(get("/api/municipios?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(municipio.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].codigoIBGE").value(hasItem(DEFAULT_CODIGO_IBGE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);

        // Get the municipio
        restMunicipioMockMvc.perform(get("/api/municipios/{id}", municipio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(municipio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.codigoIBGE").value(DEFAULT_CODIGO_IBGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMunicipio() throws Exception {
        // Get the municipio
        restMunicipioMockMvc.perform(get("/api/municipios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);
        int databaseSizeBeforeUpdate = municipioRepository.findAll().size();

        // Update the municipio
        Municipio updatedMunicipio = new Municipio();
        updatedMunicipio.setId(municipio.getId());
        updatedMunicipio.setNome(UPDATED_NOME);
        updatedMunicipio.setCodigoIBGE(UPDATED_CODIGO_IBGE);
        updatedMunicipio.setStatus(UPDATED_STATUS);

        restMunicipioMockMvc.perform(put("/api/municipios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMunicipio)))
                .andExpect(status().isOk());

        // Validate the Municipio in the database
        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeUpdate);
        Municipio testMunicipio = municipios.get(municipios.size() - 1);
        assertThat(testMunicipio.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMunicipio.getCodigoIBGE()).isEqualTo(UPDATED_CODIGO_IBGE);
        assertThat(testMunicipio.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteMunicipio() throws Exception {
        // Initialize the database
        municipioRepository.saveAndFlush(municipio);
        int databaseSizeBeforeDelete = municipioRepository.findAll().size();

        // Get the municipio
        restMunicipioMockMvc.perform(delete("/api/municipios/{id}", municipio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Municipio> municipios = municipioRepository.findAll();
        assertThat(municipios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
