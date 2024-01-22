package com.et.web.rest;

import com.et.api.mailchimp.model.SenderDomain;
import com.et.api.yotpo.model.CustomerDetails;
import com.et.service.MailChimpService;
import com.et.service.ShopInfoService;
import com.et.service.dto.ShopInfoDTO;
import com.et.utils.CommonUtils;
import com.et.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Api(tags = "Mail Chimp Resource")
public class MailChimpResource {

    private final Logger log = LoggerFactory.getLogger(MailChimpResource.class);

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private MailChimpService mailChimpService;

    @Autowired
    private ShopInfoService shopInfoService;

    @GetMapping("/mailchimp/senders/domains")
    public ResponseEntity<List<SenderDomain>> getSenderDomains() throws Exception {

        String shop = commonUtils.getShop();

        log.info("REST request to get mailchimp sender domains for shop={}", shop);

        Optional<ShopInfoDTO> shopInfoDTO = shopInfoService.findByShop(shop);

        if(shopInfoDTO.isEmpty()){
            throw new BadRequestAlertException("No shop setting found for shop: "+shop, "", "");
        }

        if(StringUtils.isBlank(shopInfoDTO.get().getMailchimpApiKey())){
            throw new BadRequestAlertException("Please enable Mailchimp integration for shop", "", "");
        }

        List<SenderDomain> result = mailChimpService.getDomains(shop, shopInfoDTO.get().getMailchimpApiKey());
        return ResponseEntity.ok(result);
    }
}
