package com.et.web.rest;

import com.et.domain.MembershipDiscountProducts;
import com.et.repository.MembershipDiscountProductsRepository;
import com.et.service.MembershipDiscountService;
import com.et.service.mapper.MembershipDiscountMapper;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.service.dto.MembershipDiscountDTO;

import com.et.web.rest.vm.MembershipDiscountRequest;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.et.domain.MembershipDiscount}.
 */
@RestController
@RequestMapping("/api")
public class MembershipDiscountResource {

    private final Logger log = LoggerFactory.getLogger(MembershipDiscountResource.class);

    private static final String ENTITY_NAME = "membershipDiscount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipDiscountService membershipDiscountService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MembershipDiscountMapper membershipDiscountMapper;

    @Autowired
    private MembershipDiscountProductsRepository membershipDiscountProductsRepository;
    public MembershipDiscountResource(MembershipDiscountService membershipDiscountService) {
        this.membershipDiscountService = membershipDiscountService;
    }

    /**
     * {@code POST  /membership-discounts} : Create a new membershipDiscount.
     *
     * @param membershipDiscountDTO the membershipDiscountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipDiscountDTO, or with status {@code 400 (Bad Request)} if the membershipDiscount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-discounts")
    public ResponseEntity<MembershipDiscountDTO> createMembershipDiscount(@RequestBody MembershipDiscountRequest membershipDiscountDTO) throws URISyntaxException {
        log.debug("REST request to save MembershipDiscount : {}", membershipDiscountDTO);
        if (membershipDiscountDTO.getMembershipDiscount().getId() != null) {
            throw new BadRequestAlertException("A new membershipDiscount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        String shop = commonUtils.getShop();
        MembershipDiscountDTO result = membershipDiscountService.createOrUpdate(shop, membershipDiscountDTO);
        return ResponseEntity.created(new URI("/api/membership-discounts/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "New Membership Discount Created.", ""))
            .body(result);
    }

    /**
     * {@code PUT  /membership-discounts} : Updates an existing membershipDiscount.
     *
     * @param membershipDiscountDTO the membershipDiscountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipDiscountDTO,
     * or with status {@code 400 (Bad Request)} if the membershipDiscountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipDiscountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-discounts")
    public ResponseEntity<MembershipDiscountDTO> updateMembershipDiscount(@RequestBody MembershipDiscountRequest membershipDiscountDTO) throws URISyntaxException {
        log.debug("REST request to update MembershipDiscount : {}", membershipDiscountDTO);
        if (membershipDiscountDTO.getMembershipDiscount().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        String shop = commonUtils.getShop();
        MembershipDiscountDTO result = membershipDiscountService.createOrUpdate(shop, membershipDiscountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "Membership Discount Updated.", ""))
            .body(result);
    }

    /**
     * {@code GET  /membership-discounts} : get all the membershipDiscounts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipDiscounts in body.
     */
    @GetMapping("/membership-discounts")
    public List<MembershipDiscountDTO> getAllMembershipDiscounts() {
        log.debug("REST request to get all MembershipDiscounts");
        String shop = commonUtils.getShop();
        return membershipDiscountService.findByShop(shop);
    }

    /**
     * {@code GET  /membership-discounts/:id} : get the "id" membershipDiscount.
     *
     * @param id the id of the membershipDiscountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipDiscountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-discounts/{id}")
    public ResponseEntity<MembershipDiscountRequest> getMembershipDiscount(@PathVariable Long id) {
        log.debug("REST request to get MembershipDiscount : {}", id);
        Optional<MembershipDiscountDTO> membershipDiscountDTO = membershipDiscountService.findOne(id);
        String shop = commonUtils.getShop();
        MembershipDiscountRequest membershipDiscountRequest = new MembershipDiscountRequest();
        if(membershipDiscountDTO.isPresent()){
            membershipDiscountRequest.setMembershipDiscount(membershipDiscountMapper.toEntity(membershipDiscountDTO.get()));
            List<MembershipDiscountProducts> membershipDiscountProducts = membershipDiscountProductsRepository.findByShopAndMembershipDiscountId(shop,membershipDiscountDTO.get().getId());
            membershipDiscountRequest.setMembershipDiscountProducts(membershipDiscountProducts);
        }

        return ResponseEntity.ok().body(membershipDiscountRequest);
    }

    /**
     * {@code DELETE  /membership-discounts/:id} : delete the "id" membershipDiscount.
     *
     * @param id the id of the membershipDiscountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-discounts/{id}")
    public ResponseEntity<Void> deleteMembershipDiscount(@PathVariable Long id) {
        log.debug("REST request to delete MembershipDiscount : {}", id);
        String shop = commonUtils.getShop();
        membershipDiscountService.delete(shop, id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
