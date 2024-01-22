package com.et.web.rest;

import com.et.security.SecurityUtils;
import com.et.service.ProductInfoService;
import com.et.service.dto.CollectionVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppstleMenuResource {

    private final Logger log = LoggerFactory.getLogger(AppstleMenuResource.class);

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping(value = "/appstle-menu/get-all-collections", method = RequestMethod.GET)
    public List<CollectionVM> getAllCollections(@RequestParam(required = false, name = "searchText") String searchText) {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        return productInfoService.getAllCollectionList(shop, searchText);
    }

}
