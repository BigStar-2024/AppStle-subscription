{% comment %}
Don't edit this file.
This snippet is auto generated and will be overwritten.
{% endcomment %}


<script id="subscription-helper">
    var _RSConfig = _RSConfig || {};

    _RSConfig = {
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
        "sellingPlanSelectTitle": "${sellingPlanSelectTitle}",
        "tooltipTitle": "${tooltipTitle}",
        "showTooltipOnClick": "${showTooltipOnClick}",
        "tooltipDesctiption": "${tooltipDesctiption}",
        "orderStatusManageSubscriptionTitle": "${orderStatusManageSubscriptionTitle}",
        "orderStatusManageSubscriptionDescription": "${orderStatusManageSubscriptionDescription}",
        "orderStatusManageSubscriptionButtonText": "${orderStatusManageSubscriptionButtonText}",
        "priceSelector": "${priceSelector}",
        "landingPagePriceSelector": "${landingPagePriceSelector}",
        "quickViewClickSelector": "${quickViewClickSelector}",
        "badgeTop": "${badgeTop}",
        "pricePlacement":"${pricePlacement}",
        "subscriptionOptionSelectedByDefault" : ${subscriptionOptionSelectedByDefault},
        "sellingPlansJson": ${sellingPlansJson},
        "widgetEnabled": ${widgetEnabled},
        "showAppstleLink": ${showAppstleLink},
        "showTooltip" : ${showTooltip},
        "showStaticTooltip": ${showStaticTooltip},
        "sortByDefaultSequence": ${sortByDefaultSequence},
        "manageSubscriptionUrl" : "${manageSubscriptionUrl}",
        "showSubOptionBeforeOneTime": ${showSubOptionBeforeOneTime},
        "detectVariantFromURLParams": ${detectVariantFromURLParams},
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
        "labels": "{% raw %}${labels}{% endraw %}",
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
        }
    };


    _RSConfig.shop = '{{shop.permanent_domain}}';


    {% if product %}
    _RSConfig.product = {{ product | json }};
    _RSConfig.product.collections = {{ product.collections | json }};

    {% for variant in product.variants %}
    _RSConfig.product.variants[{{forloop.index | minus: 1 }}]['inventory_quantity'] = {{ variant.inventory_quantity }};
    {% endfor %}
    {% endif %}

    {% if customer %}
    _RSConfig.customerId = {{ customer.id }};
    _RSConfig.customer_tags = {{ customer.tags | json }};
    {% endif %}

    {% if shop %}
    _RSConfig.shopMoneyFormat = '{{ shop.money_format | escape }}';
    _RSConfig.shopMoneyFormatWithCurrencyFormat = '{{ shop.money_with_currency_format | escape }}';
    {% endif %}

    {% if order %}
    _RSConfig.order = '{{ order | escape }}';
    {% endif %}

</script>



