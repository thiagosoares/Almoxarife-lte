package br.com.blackdog.montisol.almoxarife.web.rest;

import br.com.blackdog.montisol.almoxarife.MontisolTeste03App;
import br.com.blackdog.montisol.almoxarife.domain.Estado;
import br.com.blackdog.montisol.almoxarife.repository.EstadoRepository;

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
 * Test class for the EstadoResource REST controller.
 *
 * @see EstadoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MontisolTeste03App.class)
@WebAppConfiguration
@IntegrationTest
public class EstadoResourceIntTest {

    private static final String DEFAULT_NOME = "AA";
    private static final String UPDATED_NOME = "BB";
    private static final String DEFAULT_UF = "AA";
    private static final String UPDATED_UF = "BB";
    private static final String DEFAULT_CODIGO_IBGE = "AA";
    private static final String UPDATED_CODIGO_IBGE = "BB";

    private static final Status DEFAULT_STATUS = Status.INATIVO;
    private static final Status UPDATED_STATUS = Status.ATIVO;

    @Inject
    private EstadoRepository estadoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEstadoMockMvc;

    private Estado estado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstadoResource estadoResource = new EstadoResource();
        ReflectionTestUtils.setField(estadoResource, "estadoRepository", estadoRepository);
        this.restEstadoMockMvc = MockMvcBuilders.standaloneSetup(estadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estado = new Estado();
        estado.setNome(DEFAULT_NOME);
        estado.setUf(DEFAULT_UF);
        estado.setCodigoIBGE(DEFAULT_CODIGO_IBGE);
        estado.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createEstado() throws Exception {
        int databaseSizeBeforeCreate = estadoRepository.findAll().size();

        // Create the Estado

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estado)))
                .andExpect(status().isCreated());

        // Validate the Estado in the database
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeCreate + 1);
        Estado testEstado = estados.get(estados.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testEstado.getUf()).isEqualTo(DEFAULT_UF);
        assertThat(testEstado.getCodigoIBGE()).isEqualTo(DEFAULT_CODIGO_IBGE);
        assertThat(testEstado.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        // set the field null
        estado.setNome(null);

        // Create the Estado, which fails.

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estado)))
                .andExpect(status().isBadRequest());

        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUfIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        // set the field null
        estado.setUf(null);

        // Create the Estado, which fails.

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estado)))
                .andExpect(status().isBadRequest());

        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoIBGEIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        // set the field null
        estado.setCodigoIBGE(null);

        // Create the Estado, which fails.

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estado)))
                .andExpect(status().isBadRequest());

        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoRepository.findAll().size();
        // set the field null
        estado.setStatus(null);

        // Create the Estado, which fails.

        restEstadoMockMvc.perform(post("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estado)))
                .andExpect(status().isBadRequest());

        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEstados() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get all the estados
        restEstadoMockMvc.perform(get("/api/estados?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estado.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].uf").value(hasItem(DEFAULT_UF.toString())))
                .andExpect(jsonPath("$.[*].codigoIBGE").value(hasItem(DEFAULT_CODIGO_IBGE.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);

        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", estado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.uf").value(DEFAULT_UF.toString()))
            .andExpect(jsonPath("$.codigoIBGE").value(DEFAULT_CODIGO_IBGE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstado() throws Exception {
        // Get the estado
        restEstadoMockMvc.perform(get("/api/estados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeUpdate = estadoRepository.findAll().size();

        // Update the estado
        Estado updatedEstado = new Estado();
        updatedEstado.setId(estado.getId());
        updatedEstado.setNome(UPDATED_NOME);
        updatedEstado.setUf(UPDATED_UF);
        updatedEstado.setCodigoIBGE(UPDATED_CODIGO_IBGE);
        updatedEstado.setStatus(UPDATED_STATUS);

        restEstadoMockMvc.perform(put("/api/estados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEstado)))
                .andExpect(status().isOk());

        // Validate the Estado in the database
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeUpdate);
        Estado testEstado = estados.get(estados.size() - 1);
        assertThat(testEstado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testEstado.getUf()).isEqualTo(UPDATED_UF);
        assertThat(testEstado.getCodigoIBGE()).isEqualTo(UPDATED_CODIGO_IBGE);
        assertThat(testEstado.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteEstado() throws Exception {
        // Initialize the database
        estadoRepository.saveAndFlush(estado);
        int databaseSizeBeforeDelete = estadoRepository.findAll().size();

        // Get the estado
        restEstadoMockMvc.perform(delete("/api/estados/{id}", estado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estado> estados = estadoRepository.findAll();
        assertThat(estados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
