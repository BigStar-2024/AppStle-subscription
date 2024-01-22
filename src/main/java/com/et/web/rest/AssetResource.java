package com.et.web.rest;

import com.et.domain.ThemeSettings;
import com.et.service.ThemeSettingsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/assets")
public class AssetResource {

    @Autowired
    ThemeSettingsService themeSettingsService;


    @RequestMapping(method = RequestMethod.GET, value = "/custom/{fileName}")
    @CrossOrigin
    public ResponseEntity<String> getFileContent(@PathVariable String fileName, @RequestParam(value = "shop") String shop) {

        String fileContent = "";
        HttpHeaders responseHeaders = new HttpHeaders();

        if(StringUtils.isNotBlank(shop)) {

            Optional<ThemeSettings> themeSettings = themeSettingsService.findByShop(shop);

            if(themeSettings.isPresent()) {
                if (StringUtils.isNotBlank(themeSettings.get().getCustomJavascript())) {
                    fileContent = themeSettings.get().getCustomJavascript();
                }
            }
        }

        if("appstle-subscription.js".equalsIgnoreCase(fileName) && StringUtils.isNotBlank(fileContent)) {
            responseHeaders.set(HttpHeaders.CONTENT_TYPE, "application/javascript");

            return ResponseEntity.ok().headers(responseHeaders).body(fileContent);
        }

        return ResponseEntity.notFound().build();
    }

}
