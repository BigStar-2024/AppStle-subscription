package com.et.web.rest;

import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.asset.Asset;
import com.et.api.shopify.asset.UpdateAssetRequest;
import com.et.api.shopify.asset.UpdateAssetResponse;
import com.et.api.shopify.scripttag.CreateScriptTagRequest;
import com.et.api.shopify.scripttag.CreateScriptTagResponse;
import com.et.api.shopify.scripttag.GetScriptTagsResponse;
import com.et.api.shopify.scripttag.ScriptTag;
import com.et.api.shopify.theme.GetThemesResponse;
import com.et.domain.SocialConnection;
import com.et.security.SecurityUtils;
import com.et.service.SocialConnectionService;
import com.et.utils.CommonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hemantpurswani on 9/15/17.
 */

@RestController
@RequestMapping("/api")
public class ThemeAssetResource {

    private final Logger log = LoggerFactory.getLogger(ThemeAssetResource.class);

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private CommonUtils commonUtils;

    @RequestMapping(value = "/asset-key", method = RequestMethod.GET)
    public String getAsset(@RequestParam("assetKey") String assetKey) {

        String userId = SecurityUtils.getCurrentUserLogin().get();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId("notifier", userId);

        if (!socialConnection.isPresent()) {
            throw new RuntimeException();
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(userId);

        Optional<GetThemesResponse.BasicThemeInfo> publishedTheme = api.getThemes().getThemes().stream().filter(t -> t.getRole().equals("main")).findFirst();

        if (!publishedTheme.isPresent()) {
            throw new RuntimeException();
        }

        return api.getAsset(assetKey, publishedTheme.get().getId()).getAsset().getValue();

    }


    @RequestMapping(value = "/asset-key", method = RequestMethod.POST)
    public String saveAsset(@RequestBody final AssetVM assetVM) {

        String userId = SecurityUtils.getCurrentUserLogin().get();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId("notifier", userId);

        if (!socialConnection.isPresent()) {
            throw new RuntimeException();
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(userId);

        Optional<GetThemesResponse.BasicThemeInfo> publishedTheme = api.getThemes().getThemes().stream().filter(t -> t.getRole().equals("main")).findFirst();

        if (!publishedTheme.isPresent()) {
            throw new RuntimeException();
        }

        Asset asset = new Asset();
        asset.setKey(assetVM.getAssetKey());
        asset.setValue(assetVM.getValue());
        UpdateAssetRequest updateAssetRequest = new UpdateAssetRequest();
        updateAssetRequest.setAsset(asset);
        UpdateAssetResponse updateAssetResponse = api.updateAsset(publishedTheme.get().getId(), updateAssetRequest);

        if (assetVM.getAssetKey().contains("appstle-subscription.js")) {
            String public_url = updateAssetResponse.getAsset().getPublic_url();
            generateScriptTags(api, public_url);
        }

        return assetVM.getValue();
    }

    private void generateScriptTags(ShopifyAPI api, String... scriptUrls) {
        try {

            GetScriptTagsResponse scriptTags = api.getScriptTags();

            for (ScriptTag tag : scriptTags.getScript_tags()) {
                api.deleteScriptTag(tag.getId());
            }

            for (String scriptUrl : scriptUrls) {
                CreateScriptTagRequest createScriptTagRequest = new CreateScriptTagRequest();
                CreateScriptTagRequest.ScriptTagRequest scriptTag = new CreateScriptTagRequest.ScriptTagRequest();
                scriptTag.setSrc(scriptUrl);
                scriptTag.setEvent("onload");
                createScriptTagRequest.setScript_tag(scriptTag);
                CreateScriptTagResponse scriptTagResponse = api.createScriptTag(createScriptTagRequest);
                String a = "b";
            }
        } catch (Exception ex) {
            log.error("An error occurred while creating scriptTag. shop=" + api.getShopName() + " ex=" + ExceptionUtils.getStackTrace(ex), ex);
        }
    }


    @RequestMapping(value = "/asset-keys", method = RequestMethod.GET)
    public List<String> getAssetKeys() {

        String userId = SecurityUtils.getCurrentUserLogin().get();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId("notifier", userId);

        if (!socialConnection.isPresent()) {
            throw new RuntimeException();
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(userId);

        Optional<GetThemesResponse.BasicThemeInfo> publishedTheme = api.getThemes().getThemes().stream().filter(t -> t.getRole().equals("main")).findFirst();

        if (!publishedTheme.isPresent()) {
            throw new RuntimeException();
        }

        return api.getAssets(publishedTheme.get().getId()).getAssets().stream().map(Asset::getKey).collect(Collectors.toList());
    }


    @RequestMapping(value = "/theme-details", method = RequestMethod.GET)
    public String getThemeDetails() {

        String userId = SecurityUtils.getCurrentUserLogin().get();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId("notifier", userId);

        if (!socialConnection.isPresent()) {
            throw new RuntimeException();
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(userId);

        Optional<GetThemesResponse.BasicThemeInfo> publishedTheme = api.getThemes().getThemes().stream().filter(t -> t.getRole().equals("main")).findFirst();

        if (!publishedTheme.isPresent()) {
            throw new RuntimeException();
        }

        return publishedTheme.get().getName();
    }

    @RequestMapping(value = "/all-theme-details", method = RequestMethod.GET)
    public  List<GetThemesResponse.BasicThemeInfo> getAllThemeDetails() {

        String userId = SecurityUtils.getCurrentUserLogin().get();

        Optional<SocialConnection> socialConnection = socialConnectionService.findByProviderIdAndUserId("notifier", userId);

        if (!socialConnection.isPresent()) {
            throw new RuntimeException();
        }

        ShopifyAPI api = commonUtils.prepareShopifyResClient(userId);

        List<GetThemesResponse.BasicThemeInfo> themes = api.getThemes().getThemes();

        return themes;
    }

    public static class AssetVM {
        public String value;
        public String assetKey;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAssetKey() {
            return assetKey;
        }

        public void setAssetKey(String assetKey) {
            this.assetKey = assetKey;
        }
    }
}
