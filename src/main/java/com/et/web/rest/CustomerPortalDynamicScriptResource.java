package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.CustomerPortalDynamicScriptService;
import com.et.service.dto.CustomerPortalDynamicScriptDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.CustomerPortalDynamicScript}.
 */
@RestController
@RequestMapping("/api")
public class CustomerPortalDynamicScriptResource {

    private final Logger log = LoggerFactory.getLogger(CustomerPortalDynamicScriptResource.class);

    private static final String ENTITY_NAME = "customerPortalDynamicScript";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerPortalDynamicScriptService customerPortalDynamicScriptService;

    public CustomerPortalDynamicScriptResource(CustomerPortalDynamicScriptService customerPortalDynamicScriptService) {
        this.customerPortalDynamicScriptService = customerPortalDynamicScriptService;
    }

    /**
     * {@code POST  /customer-portal-dynamic-scripts} : Create a new customerPortalDynamicScript.
     *
     * @param customerPortalDynamicScriptDTO the customerPortalDynamicScriptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerPortalDynamicScriptDTO, or with status {@code 400 (Bad Request)} if the customerPortalDynamicScript has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-portal-dynamic-scripts")
    public ResponseEntity<CustomerPortalDynamicScriptDTO> createCustomerPortalDynamicScript(@Valid @RequestBody CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerPortalDynamicScript : {}", customerPortalDynamicScriptDTO);
        if (customerPortalDynamicScriptDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerPortalDynamicScript cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPortalDynamicScriptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScriptDTO = customerPortalDynamicScriptService.findByShop(commonUtils.getShop());

        if (optionalCustomerPortalDynamicScriptDTO.isPresent()) {
            throw new BadRequestAlertException("A new bundleDynamicScript cannot already have an ID", ENTITY_NAME, "idexists");
        }

        CustomerPortalDynamicScriptDTO result = customerPortalDynamicScriptService.save(customerPortalDynamicScriptDTO);
        return ResponseEntity.created(new URI("/api/customer-portal-dynamic-scripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-portal-dynamic-scripts} : Updates an existing customerPortalDynamicScript.
     *
     * @param customerPortalDynamicScriptDTO the customerPortalDynamicScriptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerPortalDynamicScriptDTO,
     * or with status {@code 400 (Bad Request)} if the customerPortalDynamicScriptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerPortalDynamicScriptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-portal-dynamic-scripts")
    public ResponseEntity<CustomerPortalDynamicScriptDTO> updateCustomerPortalDynamicScript(@Valid @RequestBody CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerPortalDynamicScript : {}", customerPortalDynamicScriptDTO);
        if (customerPortalDynamicScriptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        if (!shop.equals(customerPortalDynamicScriptDTO.getShop())) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        CustomerPortalDynamicScriptDTO result = customerPortalDynamicScriptService.save(customerPortalDynamicScriptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerPortalDynamicScriptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-portal-dynamic-scripts} : get all the customerPortalDynamicScripts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerPortalDynamicScripts in body.
     */
    @GetMapping("/customer-portal-dynamic-scripts")
    public List<CustomerPortalDynamicScriptDTO> getAllCustomerPortalDynamicScripts() {
        log.debug("REST request to get all CustomerPortalDynamicScripts");
        List<CustomerPortalDynamicScriptDTO> customerPortalDynamicScriptDTOList = new ArrayList<>();
        String shop = commonUtils.getShop();
        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScript = customerPortalDynamicScriptService.findByShop(shop);
        optionalCustomerPortalDynamicScript.ifPresent(customerPortalDynamicScriptDTOList::add);
        return customerPortalDynamicScriptDTOList;
    }

    @Autowired
    private CommonUtils commonUtils;

    /**
     * {@code GET  /customer-portal-dynamic-scripts/:id} : get the "id" customerPortalDynamicScript.
     *
     * @param id the id of the customerPortalDynamicScriptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerPortalDynamicScriptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-portal-dynamic-scripts/{id}")
    public ResponseEntity<CustomerPortalDynamicScriptDTO> getCustomerPortalDynamicScript(@PathVariable Long id) {
        log.debug("REST request to get CustomerPortalDynamicScript : {}", id);

        String shop = commonUtils.getShop();
        Optional<CustomerPortalDynamicScriptDTO> optionalCustomerPortalDynamicScriptDTO = customerPortalDynamicScriptService.findByShop(shop);
        CustomerPortalDynamicScriptDTO other = new CustomerPortalDynamicScriptDTO();
        other.setShop(shop);
        CustomerPortalDynamicScriptDTO customerPortalDynamicScriptDTO = optionalCustomerPortalDynamicScriptDTO.orElse(other);

        Optional<CustomerPortalDynamicScriptDTO> customerPortalDynamicScriptDTO1 = Optional.of(customerPortalDynamicScriptDTO);
        return ResponseUtil.wrapOrNotFound(customerPortalDynamicScriptDTO1);
    }

    /**
     * {@code DELETE  /customer-portal-dynamic-scripts/:id} : delete the "id" customerPortalDynamicScript.
     *
     * @param id the id of the customerPortalDynamicScriptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-portal-dynamic-scripts/{id}")
    public ResponseEntity<Void> deleteCustomerPortalDynamicScript(@PathVariable Long id) {
        log.debug("REST request to delete CustomerPortalDynamicScript : {}", id);
        customerPortalDynamicScriptService.deleteByShop(commonUtils.getShop());
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
