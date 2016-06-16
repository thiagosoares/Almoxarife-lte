package br.com.blackdog.montisol.almoxarife.web.rest;

import br.com.blackdog.montisol.almoxarife.MontisolTeste03App;
import br.com.blackdog.montisol.almoxarife.domain.Teste;
import br.com.blackdog.montisol.almoxarife.repository.TesteRepository;

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


/**
 * Test class for the TesteResource REST controller.
 *
 * @see TesteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MontisolTeste03App.class)
@WebAppConfiguration
@IntegrationTest
public class TesteResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "A";
    private static final String UPDATED_DESCRICAO = "B";
    private static final String DEFAULT_DESCRICAO_2 = "AAAAA";
    private static final String UPDATED_DESCRICAO_2 = "BBBBB";

    private static final Long DEFAULT_ESTOQUE = 1L;
    private static final Long UPDATED_ESTOQUE = 2L;

    @Inject
    private TesteRepository testeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTesteMockMvc;

    private Teste teste;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TesteResource testeResource = new TesteResource();
        ReflectionTestUtils.setField(testeResource, "testeRepository", testeRepository);
        this.restTesteMockMvc = MockMvcBuilders.standaloneSetup(testeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        teste = new Teste();
        teste.setDescricao(DEFAULT_DESCRICAO);
        teste.setDescricao2(DEFAULT_DESCRICAO_2);
        teste.setEstoque(DEFAULT_ESTOQUE);
    }

    @Test
    @Transactional
    public void createTeste() throws Exception {
        int databaseSizeBeforeCreate = testeRepository.findAll().size();

        // Create the Teste

        restTesteMockMvc.perform(post("/api/testes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teste)))
                .andExpect(status().isCreated());

        // Validate the Teste in the database
        List<Teste> testes = testeRepository.findAll();
        assertThat(testes).hasSize(databaseSizeBeforeCreate + 1);
        Teste testTeste = testes.get(testes.size() - 1);
        assertThat(testTeste.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testTeste.getDescricao2()).isEqualTo(DEFAULT_DESCRICAO_2);
        assertThat(testTeste.getEstoque()).isEqualTo(DEFAULT_ESTOQUE);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = testeRepository.findAll().size();
        // set the field null
        teste.setDescricao(null);

        // Create the Teste, which fails.

        restTesteMockMvc.perform(post("/api/testes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teste)))
                .andExpect(status().isBadRequest());

        List<Teste> testes = testeRepository.findAll();
        assertThat(testes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricao2IsRequired() throws Exception {
        int databaseSizeBeforeTest = testeRepository.findAll().size();
        // set the field null
        teste.setDescricao2(null);

        // Create the Teste, which fails.

        restTesteMockMvc.perform(post("/api/testes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teste)))
                .andExpect(status().isBadRequest());

        List<Teste> testes = testeRepository.findAll();
        assertThat(testes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTestes() throws Exception {
        // Initialize the database
        testeRepository.saveAndFlush(teste);

        // Get all the testes
        restTesteMockMvc.perform(get("/api/testes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(teste.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].descricao2").value(hasItem(DEFAULT_DESCRICAO_2.toString())))
                .andExpect(jsonPath("$.[*].estoque").value(hasItem(DEFAULT_ESTOQUE.intValue())));
    }

    @Test
    @Transactional
    public void getTeste() throws Exception {
        // Initialize the database
        testeRepository.saveAndFlush(teste);

        // Get the teste
        restTesteMockMvc.perform(get("/api/testes/{id}", teste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(teste.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.descricao2").value(DEFAULT_DESCRICAO_2.toString()))
            .andExpect(jsonPath("$.estoque").value(DEFAULT_ESTOQUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTeste() throws Exception {
        // Get the teste
        restTesteMockMvc.perform(get("/api/testes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeste() throws Exception {
        // Initialize the database
        testeRepository.saveAndFlush(teste);
        int databaseSizeBeforeUpdate = testeRepository.findAll().size();

        // Update the teste
        Teste updatedTeste = new Teste();
        updatedTeste.setId(teste.getId());
        updatedTeste.setDescricao(UPDATED_DESCRICAO);
        updatedTeste.setDescricao2(UPDATED_DESCRICAO_2);
        updatedTeste.setEstoque(UPDATED_ESTOQUE);

        restTesteMockMvc.perform(put("/api/testes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeste)))
                .andExpect(status().isOk());

        // Validate the Teste in the database
        List<Teste> testes = testeRepository.findAll();
        assertThat(testes).hasSize(databaseSizeBeforeUpdate);
        Teste testTeste = testes.get(testes.size() - 1);
        assertThat(testTeste.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testTeste.getDescricao2()).isEqualTo(UPDATED_DESCRICAO_2);
        assertThat(testTeste.getEstoque()).isEqualTo(UPDATED_ESTOQUE);
    }

    @Test
    @Transactional
    public void deleteTeste() throws Exception {
        // Initialize the database
        testeRepository.saveAndFlush(teste);
        int databaseSizeBeforeDelete = testeRepository.findAll().size();

        // Get the teste
        restTesteMockMvc.perform(delete("/api/testes/{id}", teste.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Teste> testes = testeRepository.findAll();
        assertThat(testes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
