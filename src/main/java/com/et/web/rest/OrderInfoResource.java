package com.et.web.rest;

import com.et.service.OrderInfoService;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.OrderInfoDTO;

import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.OrderInfo}.
 */
@RestController
@RequestMapping("/api")
public class OrderInfoResource {

    private final Logger log = LoggerFactory.getLogger(OrderInfoResource.class);

    private static final String ENTITY_NAME = "orderInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderInfoService orderInfoService;

    @Autowired
    private CommonUtils commonUtils;

    public OrderInfoResource(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    /**
     * {@code POST  /order-infos} : Create a new orderInfo.
     *
     * @param orderInfoDTO the orderInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderInfoDTO, or with status {@code 400 (Bad Request)} if the orderInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-infos")
    public ResponseEntity<OrderInfoDTO> createOrderInfo(@RequestBody OrderInfoDTO orderInfoDTO) throws URISyntaxException {
        log.debug("REST request to save OrderInfo : {}", orderInfoDTO);
        if (orderInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }

        String shop = commonUtils.getShop();
        orderInfoDTO.setShop(shop);

        OrderInfoDTO result = orderInfoService.save(orderInfoDTO);
        return ResponseEntity.created(new URI("/api/order-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-infos} : Updates an existing orderInfo.
     *
     * @param orderInfoDTO the orderInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderInfoDTO,
     * or with status {@code 400 (Bad Request)} if the orderInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-infos")
    public ResponseEntity<OrderInfoDTO> updateOrderInfo(@RequestBody OrderInfoDTO orderInfoDTO) throws URISyntaxException {
        log.debug("REST request to update OrderInfo : {}", orderInfoDTO);
        if (orderInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        String shop = commonUtils.getShop();
        orderInfoDTO.setShop(shop);

        OrderInfoDTO result = orderInfoService.save(orderInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, orderInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-infos} : get all the orderInfos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderInfos in body.
     */
    @GetMapping("/order-infos")
    public ResponseEntity<List<OrderInfoDTO>> getAllOrderInfos(Pageable pageable) {
        log.debug("REST request to get a page of OrderInfos");

        String shop = commonUtils.getShop();

        Page<OrderInfoDTO> page = orderInfoService.findAllByShop(shop, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-infos/:id} : get the "id" orderInfo.
     *
     * @param orderId the id of the order in orderInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-infos/{orderId}")
    public ResponseEntity<OrderInfoDTO> getOrderInfo(@PathVariable Long orderId) {
        log.debug("REST request to get OrderInfo : {}", orderId);

        String shop = commonUtils.getShop();

        Optional<OrderInfoDTO> orderInfoDTO = orderInfoService.findByShopAndOrderId(shop, orderId);
        return ResponseUtil.wrapOrNotFound(orderInfoDTO);
    }

    /**
     * {@code DELETE  /order-infos/:id} : delete the "id" orderInfo.
     *
     * @param orderId the id of the order in orderInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-infos/{orderId}")
    public ResponseEntity<Void> deleteOrderInfo(@PathVariable Long orderId) {
        log.debug("REST request to delete OrderInfo : {}", orderId);

        String shop = commonUtils.getShop();
        orderInfoService.deleteByShopAndOrderId(shop, orderId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, orderId.toString())).build();
    }
}
