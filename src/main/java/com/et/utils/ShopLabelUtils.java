package com.et.utils;

import com.et.domain.SocialConnection;
import com.et.pojo.KeyLabelInfo;
import com.et.pojo.LabelValueInfo;
import com.et.service.*;
import com.et.service.dto.BundleSettingDTO;
import com.et.service.dto.CustomerPortalSettingsDTO;
import com.et.service.dto.SubscriptionWidgetSettingsDTO;
import com.et.web.rest.vm.SyncInfoItem;
import com.et.web.rest.vm.SyncLabelsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class ShopLabelUtils {

    private final Logger log = LoggerFactory.getLogger(ShopLabelUtils.class);

    @Autowired
    private ShopLabelService shopLabelService;

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    @Autowired
    private BundleSettingService bundleSettingService;

    @Async
    public void addNewLabels(String shop, String namespace, String group, Map<String, Object> additionalProperties) {
        try {
            List<KeyLabelInfo> keyLabelInfoList = new ArrayList<>();

            List<String> groups = new ArrayList<>();
            groups.add(group);

            for (Map.Entry<String, Object> entry : additionalProperties.entrySet()) {
                String key = namespace + entry.getKey();
                keyLabelInfoList.add(new KeyLabelInfo(key, new LabelValueInfo(entry.getValue().toString(), groups)));
            }
            if(!CollectionUtils.isEmpty(keyLabelInfoList)){
                shopLabelService.addKeysForShop(shop, keyLabelInfoList);
            }
        } catch (Exception e) {
            log.error("An error occurred while migrating labels for shop: {}, error message: {}", shop, e.getMessage());
        }
    }

    public void syncLabels(List<SyncLabelsInfo> syncLabelsInfoList) throws Exception {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        Map<String, LabelValueInfo> defaultLabels = shopLabelService.getDefaultLabels();

        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(socialConnections.size());
        for (SocialConnection socialConnection : socialConnections) {
            WORKER_THREAD_POOL.submit(() -> {
                try {

                    try {
                        String shop = socialConnection.getUserId();

                        List<String> groups = syncLabelsInfoList.stream().flatMap(labelInfo -> labelInfo.getGroups().stream()).distinct().collect(Collectors.toList());

                        List<KeyLabelInfo> keyLabelInfoList = new ArrayList<>();

                        if(!CollectionUtils.isEmpty(groups)) {
                            if(groups.contains("CUSTOMER_PORTAL")){
                                CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).orElse(new CustomerPortalSettingsDTO());
                                SyncLabelsInfo syncLabelsInfo = syncLabelsInfoList.stream().filter(syncLabels -> syncLabels.getGroups().contains("CUSTOMER_PORTAL")).findFirst().orElse(new SyncLabelsInfo());
                                addKeyLabelInfo(syncLabelsInfo, customerPortalSettingsDTO.getClass(), customerPortalSettingsDTO, defaultLabels, keyLabelInfoList);
                            }
                            if(groups.contains("WIDGET")){
                                SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO = subscriptionWidgetSettingsService.findByShop(shop).orElse(new SubscriptionWidgetSettingsDTO());
                                SyncLabelsInfo syncLabelsInfo = syncLabelsInfoList.stream().filter(syncLabels -> syncLabels.getGroups().contains("WIDGET")).findFirst().orElse(new SyncLabelsInfo());
                                addKeyLabelInfo(syncLabelsInfo, subscriptionWidgetSettingsDTO.getClass(), subscriptionWidgetSettingsDTO, defaultLabels, keyLabelInfoList);
                            }
                            if(groups.contains("BUNDLE")){
                                BundleSettingDTO bundleSettingDTO = bundleSettingService.findByShop(shop).orElse(new BundleSettingDTO());
                                SyncLabelsInfo syncLabelsInfo = syncLabelsInfoList.stream().filter(syncLabels -> syncLabels.getGroups().contains("BUNDLE")).findFirst().orElse(new SyncLabelsInfo());
                                addKeyLabelInfo(syncLabelsInfo, bundleSettingDTO.getClass(), bundleSettingDTO, defaultLabels, keyLabelInfoList);
                            }
                        }

                        if (!keyLabelInfoList.isEmpty()) {
                            shopLabelService.addKeysForShop(shop, keyLabelInfoList);
                            log.info("shop=" + shop + " labels synced for keyLabelInfoList=" + keyLabelInfoList);
                        }


                    } catch (Exception ex) {
                        String a = "b";
                    }

                    latch.countDown();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.await();
    }

    private void addKeyLabelInfo(SyncLabelsInfo syncLabelsInfo, Class dtoClass, Object dtoObject, Map<String, LabelValueInfo> defaultLabels, List<KeyLabelInfo> keyLabelInfoList){
        for (SyncInfoItem syncInfoItem : syncLabelsInfo.getSyncInfoItems()) {
            String oldKey = syncInfoItem.getOldKey();
            String newKey = syncLabelsInfo.getNamespace() + syncInfoItem.getNewKey();

            Object value = null;

            try {
                Field field = dtoClass.getDeclaredField(oldKey);
                field.setAccessible(true);
                value = field.get(dtoObject);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                value = null;
            }

            String valueString = "";
            if (StringUtils.isEmpty(value)) {
                if(defaultLabels.containsKey(newKey)) {
                    valueString = defaultLabels.get(newKey).getValue();
                }
            } else {
                valueString = value.toString();
            }

            if(!StringUtils.isEmpty(valueString)){
                keyLabelInfoList.add(new KeyLabelInfo(newKey, new LabelValueInfo(valueString, syncLabelsInfo.getGroups())));
            }
        }
    }


    public void deleteLabelKey(String key) throws Exception {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        Map<String, LabelValueInfo> defaultLabels = shopLabelService.getDefaultLabels();

        ExecutorService WORKER_THREAD_POOL = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(socialConnections.size());
        for (SocialConnection socialConnection : socialConnections) {
            WORKER_THREAD_POOL.submit(() -> {
                try {
                    try {
                        String shop = socialConnection.getUserId();

                        if (defaultLabels.containsKey(key)) {
                            return;
                        }

                        shopLabelService.removeKeyForShop(shop, key);
                        log.info("shop=" + shop + " deleted key=" + key);
                    } catch (Exception ex) {
                        String a = "b";
                    }

                    latch.countDown();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.await();
    }
}
