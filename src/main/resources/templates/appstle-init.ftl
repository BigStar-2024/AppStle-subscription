(function (window, k) {
    if (!window.AppstleIncluded && (!urlIsProductPage() || 'V1' === '${shopifyThemeInstallationVersion}')) {
      window.AppstleIncluded = true;
      appstleLoadScript = function (src, callback) {
        var script = document.createElement("script");
        script.charset = "utf-8";
        <#if enableSlowScriptLoad == 'true'>
            script.defer = true;
        <#else>
            script.async = true;
        </#if>
        script.src = src;
        script.onload = script.onreadystatechange = function () {
          if (!script.readyState || /loaded|complete/.test(script.readyState)) {
            script.onload = script.onreadystatechange = null;
            script = k;
            callback && callback();
          }
        };
        <#if enableSlowScriptLoad == 'true'>
            document.getElementsByTagName("body")[0].appendChild(script)
        <#else>
            document.getElementsByTagName("head")[0].appendChild(script)
        </#if>
      };


      appstleLoadScript("${appstle_subscription_path}");

    <#if bundleEnabled == true>
        appstleLoadScript("${appstle_bundle_path}");
    </#if>

      window.RS = Window.RS || {};
      RS.Config = {
        "selectors": {
            "payment_button_selectors": "form[action$='/cart/add'] .shopify-payment-button",
            "subscriptionLinkSelector": "${subscriptionLinkSelector}",
            "atcButtonPlacement": "${atcButtonPlacement}",
            "subscriptionLinkPlacement": "${subscriptionLinkPlacement}",
            "cartRowSelector": "${cartRowSelector}",
            "cartLineItemSelector": "${cartLineItemSelector}",
            "cartLineItemPerQuantityPriceSelector": "${cartLineItemPerQuantityPriceSelector}",
            "cartLineItemTotalPriceSelector": "${cartLineItemTotalPriceSelector}",
            "cartLineItemSellingPlanNameSelector": "${cartLineItemSellingPlanNameSelector}",
            "cartSubTotalSelector" : "${cartSubTotalSelector}",
            "cartLineItemPriceSelector": "${cartLineItemPriceSelector}",
        },
        "enableCartWidgetFeature": "${enableCartWidgetFeature}",
        "useUrlWithCustomerId": "${useUrlWithCustomerId}",
        "atcButtonSelector": "${atcButtonSelector}",
        "moneyFormat": "${moneyFormat}",
        "oneTimePurchaseText": "${oneTimePurchaseText}",
        "shop": "${shop}",
        "deliveryText": "${deliveryText}",
        "purchaseOptionsText": "${purchaseOptionsText}",
        "manageSubscriptionButtonText": "${manageSubscriptionButtonText}",
        "subscriptionOptionText": "${subscriptionOptionText}",
        "sellingPlanSelectTitle": "${sellingPlanSelectTitle}",
        "subscriptionPriceDisplayText": "${subscriptionPriceDisplayText}",
        "tooltipTitle": "${tooltipTitle}",
        "showTooltipOnClick": "${showTooltipOnClick}",
        "tooltipDesctiption": "${tooltipDesctiption}",
        "tooltipDescriptionOnPrepaidPlan": "${tooltipDescriptionOnPrepaidPlan}",
        "tooltipDescriptionOnMultipleDiscount": "${tooltipDescriptionOnMultipleDiscount}",
        "tooltipDescriptionCustomization": "${tooltipDescriptionCustomization}",
        "orderStatusManageSubscriptionTitle": "${orderStatusManageSubscriptionTitle}",
        "orderStatusManageSubscriptionDescription": "${orderStatusManageSubscriptionDescription}",
        "orderStatusManageSubscriptionButtonText": "${orderStatusManageSubscriptionButtonText}",
        "subscriptionOptionSelectedByDefault" : ${subscriptionOptionSelectedByDefault},
        "totalPricePerDeliveryText" : "${totalPricePerDeliveryText}",
        "memberOnlySellingPlansJson": ${memberOnlySellingPlansJson},
        "nonMemberOnlySellingPlansJson": ${nonMemberOnlySellingPlansJson},
        "sellingPlansJson": ${sellingPlansJson},
        "widgetEnabled": ${widgetEnabled},
        "showTooltip" : ${showTooltip},
        "sortByDefaultSequence": ${sortByDefaultSequence},
        "showSubOptionBeforeOneTime": ${showSubOptionBeforeOneTime},
        "detectVariantFromURLParams": ${detectVariantFromURLParams},
        "showStaticTooltip": ${showStaticTooltip},
        "showAppstleLink": ${showAppstleLink},
        "sellingPlanTitleText" : "${sellingPlanTitleText}",
        "oneTimePriceText" : "${oneTimePriceText}",
        "selectedPayAsYouGoSellingPlanPriceText" : "${selectedPayAsYouGoSellingPlanPriceText}",
        "selectedPrepaidSellingPlanPriceText" : "${selectedPrepaidSellingPlanPriceText}",
        "selectedDiscountFormat" : "${selectedDiscountFormat}",
        "manageSubscriptionBtnFormat" : "${manageSubscriptionBtnFormat}",
        "manageSubscriptionUrl" : "${manageSubscriptionUrl}",
        "appstlePlanId": ${appstlePlanId},
        "showCheckoutSubscriptionBtn": ${showCheckoutSubscriptionBtn},
        "disableLoadingJquery": ${disableLoadingJquery},
        "widgetEnabledOnSoldVariant": "${widgetEnabledOnSoldVariant}",
        "switchRadioButtonWidget": ${switchRadioButtonWidget},
        "appstlePlanName": "${appstlePlanName}",
        "appstlePlanFeatures": ${appstlePlanFeatures},
        "formMappingAttributeName": "${formMappingAttributeName}",
        "formMappingAttributeSelector": "${formMappingAttributeSelector}",
        "quickViewModalPollingSelector": "${quickViewModalPollingSelector}",
        "scriptLoadDelay": "${scriptLoadDelay}",
        "formatMoneyOverride": "${formatMoneyOverride}",
        "appstle_app_proxy_path_prefix": "${appProxyPathPrefix}",
        "updatePriceOnQuantityChange": "${updatePriceOnQuantityChange}",
        "widgetParentSelector": "${widgetParentSelector}",
        "quantitySelector": "${quantitySelector}",
        "enableAddJSInterceptor": "${enableAddJSInterceptor}",
        "reBuyEnabled": "${reBuyEnabled}",
        "loyaltyDetailsLabelText": "${loyaltyDetailsLabelText}",
        "loyaltyPerkDescriptionText": "${loyaltyPerkDescriptionText}",
        "widgetType": "${widgetType}",
        "widgetTemplateHtml": `${widgetTemplateHtml}`,
        "bundle": ${bundle},
        "labels": "${labels}",
        "css": {
            "appstle_subscription_widget": {
                "margin-top": "${subscriptionWidgetMarginTop}" ,
                "margin-bottom": "${subscriptionWidgetMarginBottom}",
            },

            "appstle_subscription_wrapper": {
                "border-width": "${subscriptionWrapperBorderWidth}",
                "border-color": "${subscriptionWrapperBorderColor}",
            },

            "appstle_circle": {
                "border-color": "${circleBorderColor}",
            },

            "appstle_dot": {
                "background-color": "${dotBackgroundColor}",
            },

            "appstle_select": {
                "padding-top": "${selectPaddingTop}",
                "padding-bottom": "${selectPaddingBottom}",
                "padding-left": "${selectPaddingLeft}",
                "padding-right": "${selectPaddingRight}",
                "border-width": "${selectBorderWidth}",
                "border-style": "${selectBorderStyle}",
                "border-color": "${selectBorderColor}",
                "border-radius": "${selectBorderRadius}",
            },

            "tooltip_subscription_svg": {
                "fill": "${tooltipSubscriptionSvgFill}",
            },

            "appstle_tooltip": {
                "color": "${tooltipColor}",
                "background-color": "${tooltipBackgroundColor}",
            },

            "appstle_tooltip_border_top_color": {
                "border-top-color": "${tooltipBorderTopColorBorderTopColor}",
            },

            "appstle_subscription_final_price": {
                "color": "${subscriptionFinalPriceColor}",
            },
            "appstle_widget_text_color": {
                "color": "${subscriptionWidgetTextColor}",
            },
            "appstle_selected_background": {
                "background": "transparent",
            },
            "customCSS": "${customCss}",
            "elementCSS": "${elementCSS}",
            "customerPortalCss": "${customerPortalCss}",
            "priceSelector": "${priceSelector}",
            "landingPagePriceSelector": "${landingPagePriceSelector}",
            "quickViewClickSelector": "${quickViewClickSelector}",
            "badgeTop": "${badgeTop}",
            "pricePlacement": "${pricePlacement}"
        }
      };

    }

    function urlIsProductPage() {
    // return null != decodeURIComponent(window.location.pathname).match(/\/products\/(([a-zA-Z0-9]|[\-\.\_\~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[\ud83c\ud83d\ud83e][\ud000-\udfff]){1,})\/?/)
    return decodeURIComponent(window.location.pathname).includes('/products/');
    }
  }
)(window);

<#if reBuyEnabled == "true">
const getProductJSON = async (handle) => {
    let data = JSON.parse(sessionStorage.getItem('productJSONStore'));

    if (data && data[handle]) {
        return data[handle];
    } else {
        await getStorefrontProduct(handle).then(product => {
            saveProductJSON(product);
            return product;
        });
    }
}

const saveProductJSON = (product) => {
    let data = JSON.parse(sessionStorage.getItem('productJSONStore'));

    if (data == null) {
        data = {};
    }

    if (product) {
        data[product.handle] = product;
    }

    sessionStorage.setItem('productJSONStore', JSON.stringify(data));
}

const removeProductJSON = (product) => {
    let data = JSON.parse(sessionStorage.getItem('productJSONStore'));

    if (data == null) {
        data = {};
    }

    if (product) {
        delete data[product.handle];
    }

    sessionStorage.setItem('productJSONStore', JSON.stringify(data));
}

const getStorefrontProduct = (handle) => {
    return fetch(Shopify.routes.root + 'products/' + handle + '.js').then((response) => {
        return response.json()
    });
}

const enrichWidgetProducts = (widget) => {
    const products = widget.data.products;

    for (var i = 0; i < products.length; i++) {
        updateProductJSON(products[i], () => {
            widget.initProducts(widget.data.products);
        });
    }
}

const enrichCartItems = () => {
    const items = Rebuy.Cart.items();
    for (var i = 0; i < items.length; i++) {
        const item = items[i];
        const product = items[i].product;

        updateProductJSON(product, () => {
            const productClone = Object.assign({}, product);

            delete productClone.subscription;
            delete productClone.subscription_discount_amount;
            delete productClone.subscription_discount_type;
            delete productClone.subscription_frequencies;
            delete productClone.subscription_frequency;
            delete productClone.subscription_interval;
            delete productClone.subscription_id;
            delete productClone.is_subscription_only;
            delete productClone.has_subscription;

            Rebuy.Cart.addEnrichedProduct(productClone);

            Rebuy.Cart.initProduct(product, {
                item: item,
                clone: true
            });
        });
    }
}

const updateProductJSON = (product, callback = function() {}) => {
    getProductJSON(product.handle).then(json => {

        if (typeof json?.requires_selling_plan != 'undefined') {
            product.is_subscription_only = json.requires_selling_plan;
        }

        if (typeof json?.selling_plan_groups != 'undefined') {
            extendProductWithSellingPlan(product, json.selling_plan_groups[0]);
        }

        callback(product);
    });
}

const extendProductWithSellingPlan = (product, selling_plan_groups) => {

    if (product && selling_plan_groups) {

        // Set discount
        selling_plan_groups.discount_amount = 0;
        selling_plan_groups.discount_type = 'none';

        // Update selling plans
        for (let i = 0; i < selling_plan_groups.selling_plans.length; i++) {
            const selling_plan = selling_plan_groups.selling_plans[i];

            // Surface discount amount and type to groups
            if (selling_plan_groups.discount_amount == 0 && selling_plan_groups.discount_type == 'none' && selling_plan.price_adjustments.length > 0) {
                let price_adjustment = selling_plan.price_adjustments[0];
                let valueType = (price_adjustment.value_type === "percentage") ? "percentage" : "fixed";
                let value = price_adjustment.value;

                if (valueType === "fixed") {
                    value = (value / 100).toFixed(2);
                }

                selling_plan_groups.discount_amount = value;
                selling_plan_groups.discount_type = valueType;
            }

            // Surface order interval to selling plan
            let interval = getOrderIntervalForOptions(selling_plan.options[0]);
            selling_plan.order_interval_frequency = interval.frequency;
            selling_plan.order_interval_unit_type = interval.unit;
        }

        // Add groups to product
        product.selling_plan_groups = [selling_plan_groups];
    }
}

const getOrderIntervalForOptions = (option) => {
    const intervalUnits = ['day', 'week', 'month', 'year'];
    const response = {
        frequency: 1,
        unit: 'day'
    };

    let frequency = option.value.toLowerCase().match(/\d+/g);

    if (frequency != null) {
        response.frequency = frequency[0];
    }

    for (let i = 0; i < intervalUnits.length; i++) {
        const unit = intervalUnits[i];
        let regex = new RegExp(unit, 'g');

        if (option.value.toLowerCase().match(regex)) {
            response.unit = unit;
            break;
        }
    }

    return response;
}

const enableSellingPlans = () => {
    const Rebuy = window.Rebuy;
    const Shop = window.Rebuy.shop;

    if (Shop) {
        if (typeof Shop.integrations == 'undefined') {
            Shop.integrations = {}
        };
        Shop.integrations.recharge = true;
        Shop.selling_plans_enabled = true;
    }
}

document.addEventListener('rebuy:cart.enriched', (event) => {
    setTimeout(enrichCartItems, 0);
});

document.addEventListener('rebuy.productsChange', (event) => {
    event.detail.widget.data.config.product_type = 'both';
    enrichWidgetProducts(event.detail.widget);
});

document.addEventListener('rebuy.loaded', () => {
    enableSellingPlans();
});

// may or may not be necessary, have needed to do this on a few stores
document.addEventListener("rebuy:cart.change", () => {
    // re-enrich cart
    setTimeout(() => {
        Rebuy.Cart.enrichCart();
    }, 300);
});
</#if>
