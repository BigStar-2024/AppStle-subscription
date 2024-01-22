package com.et.web.rest;

import com.et.SubscriptionApp;
import com.et.domain.WidgetTemplate;
import com.et.repository.WidgetTemplateRepository;
import com.et.service.WidgetTemplateService;
import com.et.service.dto.WidgetTemplateDTO;
import com.et.service.mapper.WidgetTemplateMapper;
import com.et.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.et.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.et.domain.enumeration.WidgetTemplateType;
/**
 * Integration tests for the {@link WidgetTemplateResource} REST controller.
 */
@SpringBootTest(classes = SubscriptionApp.class)
public class WidgetTemplateResourceIT {

    private static final WidgetTemplateType DEFAULT_TYPE = WidgetTemplateType.WIDGET_TYPE_1;
    private static final WidgetTemplateType UPDATED_TYPE = WidgetTemplateType.WIDGET_TYPE_2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_HTML = "AAAAAAAAAA";
    private static final String UPDATED_HTML = "BBBBBBBBBB";

    @Autowired
    private WidgetTemplateRepository widgetTemplateRepository;

    @Autowired
    private WidgetTemplateMapper widgetTemplateMapper;

    @Autowired
    private WidgetTemplateService widgetTemplateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWidgetTemplateMockMvc;

    private WidgetTemplate widgetTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WidgetTemplateResource widgetTemplateResource = new WidgetTemplateResource(widgetTemplateService);
        this.restWidgetTemplateMockMvc = MockMvcBuilders.standaloneSetup(widgetTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WidgetTemplate createEntity(EntityManager em) {
        WidgetTemplate widgetTemplate = new WidgetTemplate()
            .type(DEFAULT_TYPE)
            .title(DEFAULT_TITLE)
            .html(DEFAULT_HTML);
        return widgetTemplate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WidgetTemplate createUpdatedEntity(EntityManager em) {
        WidgetTemplate widgetTemplate = new WidgetTemplate()
            .type(UPDATED_TYPE)
            .title(UPDATED_TITLE)
            .html(UPDATED_HTML);
        return widgetTemplate;
    }

    @BeforeEach
    public void initTest() {
        widgetTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createWidgetTemplate() throws Exception {
        int databaseSizeBeforeCreate = widgetTemplateRepository.findAll().size();

        // Create the WidgetTemplate
        WidgetTemplateDTO widgetTemplateDTO = widgetTemplateMapper.toDto(widgetTemplate);
        restWidgetTemplateMockMvc.perform(post("/api/widget-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(widgetTemplateDTO)))
            .andExpect(status().isCreated());

        // Validate the WidgetTemplate in the database
        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        WidgetTemplate testWidgetTemplate = widgetTemplateList.get(widgetTemplateList.size() - 1);
        assertThat(testWidgetTemplate.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testWidgetTemplate.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWidgetTemplate.getHtml()).isEqualTo(DEFAULT_HTML);
    }

    @Test
    @Transactional
    public void createWidgetTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = widgetTemplateRepository.findAll().size();

        // Create the WidgetTemplate with an existing ID
        widgetTemplate.setId(1L);
        WidgetTemplateDTO widgetTemplateDTO = widgetTemplateMapper.toDto(widgetTemplate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWidgetTemplateMockMvc.perform(post("/api/widget-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(widgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WidgetTemplate in the database
        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = widgetTemplateRepository.findAll().size();
        // set the field null
        widgetTemplate.setType(null);

        // Create the WidgetTemplate, which fails.
        WidgetTemplateDTO widgetTemplateDTO = widgetTemplateMapper.toDto(widgetTemplate);

        restWidgetTemplateMockMvc.perform(post("/api/widget-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(widgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWidgetTemplates() throws Exception {
        // Initialize the database
        widgetTemplateRepository.saveAndFlush(widgetTemplate);

        // Get all the widgetTemplateList
        restWidgetTemplateMockMvc.perform(get("/api/widget-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(widgetTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].html").value(hasItem(DEFAULT_HTML.toString())));
    }
    
    @Test
    @Transactional
    public void getWidgetTemplate() throws Exception {
        // Initialize the database
        widgetTemplateRepository.saveAndFlush(widgetTemplate);

        // Get the widgetTemplate
        restWidgetTemplateMockMvc.perform(get("/api/widget-templates/{id}", widgetTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(widgetTemplate.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.html").value(DEFAULT_HTML.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWidgetTemplate() throws Exception {
        // Get the widgetTemplate
        restWidgetTemplateMockMvc.perform(get("/api/widget-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWidgetTemplate() throws Exception {
        // Initialize the database
        widgetTemplateRepository.saveAndFlush(widgetTemplate);

        int databaseSizeBeforeUpdate = widgetTemplateRepository.findAll().size();

        // Update the widgetTemplate
        WidgetTemplate updatedWidgetTemplate = widgetTemplateRepository.findById(widgetTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedWidgetTemplate are not directly saved in db
        em.detach(updatedWidgetTemplate);
        updatedWidgetTemplate
            .type(UPDATED_TYPE)
            .title(UPDATED_TITLE)
            .html(UPDATED_HTML);
        WidgetTemplateDTO widgetTemplateDTO = widgetTemplateMapper.toDto(updatedWidgetTemplate);

        restWidgetTemplateMockMvc.perform(put("/api/widget-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(widgetTemplateDTO)))
            .andExpect(status().isOk());

        // Validate the WidgetTemplate in the database
        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeUpdate);
        WidgetTemplate testWidgetTemplate = widgetTemplateList.get(widgetTemplateList.size() - 1);
        assertThat(testWidgetTemplate.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testWidgetTemplate.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWidgetTemplate.getHtml()).isEqualTo(UPDATED_HTML);
    }

    @Test
    @Transactional
    public void updateNonExistingWidgetTemplate() throws Exception {
        int databaseSizeBeforeUpdate = widgetTemplateRepository.findAll().size();

        // Create the WidgetTemplate
        WidgetTemplateDTO widgetTemplateDTO = widgetTemplateMapper.toDto(widgetTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWidgetTemplateMockMvc.perform(put("/api/widget-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(widgetTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WidgetTemplate in the database
        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWidgetTemplate() throws Exception {
        // Initialize the database
        widgetTemplateRepository.saveAndFlush(widgetTemplate);

        int databaseSizeBeforeDelete = widgetTemplateRepository.findAll().size();

        // Delete the widgetTemplate
        restWidgetTemplateMockMvc.perform(delete("/api/widget-templates/{id}", widgetTemplate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WidgetTemplate> widgetTemplateList = widgetTemplateRepository.findAll();
        assertThat(widgetTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
