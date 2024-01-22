package com.et.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.et.api.shipperHq.ShipperHqApi;
import com.et.api.shipperHq.model.Cart;
import com.et.api.shipperHq.model.CartItem;
import com.et.api.shipperHq.model.Destination;
import com.et.api.shipperHq.model.RatingInfo;
import com.et.service.dto.ShopInfoDTO;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.ChangeShippingAddressVM;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class ShipperHqService {

    private final Logger logger = LoggerFactory.getLogger(ShipperHqService.class);

    @Autowired
    private ShopInfoService shopInfoService;

    private Boolean checkTokenValidity(String token) {
        DecodedJWT jwt = JWT.decode(token);

        if(jwt.getExpiresAt().before(new Date())) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkAddressValidity(ShopInfoDTO shopinfo, ChangeShippingAddressVM changeShippingAddressVM) throws Exception {

        String token;

        ShipperHqApi shipperHqApi = new ShipperHqApi();

        if (StringUtils.isNotBlank(shopinfo.getShipperHqAccessToken())) {
            if(checkTokenValidity(shopinfo.getShipperHqAccessToken())) {
                token = shopinfo.getShipperHqAccessToken();
            } else {
                token = shipperHqApi.createSecretToken(shopinfo.getShipperHqApiKey(), shopinfo.getShipperHqAuthCode());
                shopinfo.setShipperHqAccessToken(token);
                shopInfoService.save(shopinfo);
            }
        } else {
            token = shipperHqApi.createSecretToken(shopinfo.getShipperHqApiKey(), shopinfo.getShipperHqAuthCode());
            shopinfo.setShipperHqAccessToken(token);
            shopInfoService.save(shopinfo);
        }


        CartItem cartItem = new CartItem();

        cartItem.setType("SIMPLE");
        cartItem.setQty(1L);

        Cart cart = new Cart();

        cart.setItems(Collections.singletonList(cartItem));

        Destination destination = new Destination();

        destination.setCountry(changeShippingAddressVM.getCountryCode());
        destination.setCity(changeShippingAddressVM.getCity());
        destination.setRegion(changeShippingAddressVM.getProvinceCode());
        destination.setStreet(changeShippingAddressVM.getAddress1());
        destination.setZipcode(changeShippingAddressVM.getZip());

        RatingInfo ratingInfo = new RatingInfo();

        ratingInfo.setCart(cart);
        ratingInfo.setDestination(destination);

        HttpResponse<JsonNode> response = shipperHqApi.checkIsValidForRates(token, ratingInfo);

        JSONObject data;

        if(response.getBody().getObject().has("data")) {
            data = response.getBody().getObject().getJSONObject("data");
            if(!data.isNull("retrieveShippingQuote")) {
                JSONObject retrieveShippingQuote = data.getJSONObject("retrieveShippingQuote");
                if(retrieveShippingQuote.isNull("errors")) {
                    JSONArray carriers = retrieveShippingQuote.getJSONArray("carriers");
                    JSONObject carrier = null;
                    if(carriers.length() > 0) {
                        carrier = carriers.getJSONObject(0);
                    }
                    if(!carrier.isNull("error")) {
                        throw new BadRequestAlertException("Address is not valid. No valid carriers found for given address","","");
                    } else {
                        return true;
                    }
                }
            }
        }

        throw new BadRequestAlertException("Error occurred while getting address validity","","");

    }
}
