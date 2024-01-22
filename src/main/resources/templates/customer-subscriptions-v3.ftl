<div id="appstle_initialLoader" style="flex-direction: column; display: flex; justify-content: center; align-items: center; position: relative; padding: 80px 0;">
    <div class="appstle_preloader appstle_loader--big"></div>
</div>
<script>
var appstleCustomerData = {};
<#if renderType != 'html'>
{% if customer %}
    appstleCustomerData.customerId = {{ customer.id }};
    appstleCustomerData.customer_tags = {{ customer.tags | json }};
    appstleCustomerData.customerName = "{{ customer.name }}";
    appstleCustomerData.firstName = "{{ customer.first_name }}";
    appstleCustomerData.lastName = "{{ customer.last_name }}";
{% endif %}
</#if>
</script>
${customerPortalTopHtml}
<div id="root"></div>
${customerPortalBottomHtml}
<script>
    var appstle_app_proxy_path_prefix = "${appProxyPathPrefix}";
    var appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};
    var appstle_api_key = "";
    var appstle_money_format = <#if renderType == 'html'>"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;
    var appstle_public_domain = location.host;
    var appstle_customer_portal_external_token = "${appstleSubscriptionsExternalToken}";
    window['isAppstleCustomerPortal'] = true;
    //window.sessionStorage.setItem("external-customer-portal-token", appstle_customer_portal_external_token);
    if(!window["Shopify"])
    {
        window["Shopify"] = {};
    }
    if(!window["Shopify"]["shop"])
    {
        window["Shopify"]["shop"] = appstle_public_domain;
    }

    function urlParamsToObject() {
        var queryStringTokens = location.search.substr(1).split("&");
        var result = {};
        for (var index = 0; index < queryStringTokens.length; index++) {
            var keyValues = queryStringTokens[index].split("=")
            result[keyValues[0]] = keyValues[1];
        }
        return result
    }

    function getToken() {
        var queryStringParams = urlParamsToObject();
        let customerId = window?.__st?.cid;
        let token = queryStringParams?.token;

        if (!token && location.pathname.indexOf('/cp/') > -1) {
            token = location.pathname.substr(location.pathname.indexOf('/cp/') + 4);
        }

        if (token || customerId) {
            window.sessionStorage["customerToken"] = '';
        }

        if (customerId) {
            fetch(location.origin + "/" + appstle_app_proxy_path_prefix + "?action=customer_payment_token&customer_id=" + customerId)
                .then(res => res.json())
                .then(data => {
                    if (data?.token) {
                        window.sessionStorage["customerToken"] = data?.token;
                    }
                    if (data?.appstleExternalToken) {
                        window.sessionStorage.setItem("external-customer-portal-token", data?.appstleExternalToken);
                    }
                })
        } else if (token) {
            fetch(location.origin + "/" + appstle_app_proxy_path_prefix + "?action=customer_payment_token&customer_token=" + token)
                .then(res => res.text())
                .then(res => {
                    if (res.length) {
                        return JSON.parse(res);
                    } else {
                        window.sessionStorage["customerToken"] = token;
                        window.sessionStorage.setItem("external-customer-portal-token", appstle_customer_portal_external_token);
                    }
                })
                .then(data => {
                    if (data?.token) {
                        window.sessionStorage["customerToken"] = data?.token;
                    }
                    if (data?.appstleExternalToken) {
                        window.sessionStorage.setItem("external-customer-portal-token", data?.appstleExternalToken);
                    }
                })
        }

        /*if (token && !customerId) {
            window.sessionStorage["customerToken"] = token;
        }*/
    }

    function appstlePollInitData() {
        var queryStringParams = urlParamsToObject();
        let customerId = window?.__st?.cid;
        let token = queryStringParams?.token;
        if (!token && location.pathname.indexOf('/cp/') > -1) {
            token = location.pathname.substr(location.pathname.indexOf('/cp/') + 4);
        }
        if (token || customerId) {
            window.sessionStorage.setItem("external-customer-portal-token", (token || customerId || 'external-customer-portal-token'));
            window.sessionStorage["customerToken"] = customerId || token;
        }
        getGeneratedPageURL();
    }

    function getGeneratedPageURL() {
        var scripts = ["${customerJavascript}"];
        scripts.forEach(function(src) {
            var newScript = document.createElement("script");
            newScript.src = src;
            newScript.type = "text/javascript";
            document.body.appendChild(newScript)
        })

        var styles = ["${customerCss}"]
        styles.forEach(function(src) {
            var newScript = document.createElement("link");
            newScript.href = src;
            newScript.rel = "stylesheet";
            document.body.appendChild(newScript)
        })
    }

    setTimeout(appstlePollInitData, 100);
</script>
<style>
    .appstle_preloader {
        width: 40px;
        height: 40px;
        position: absolute;
        top: 50%;
        left: 50%;
        z-index: 98765;
        transform: translate(-50%, -50%)
    }

    .appstle_loader--big {
        border: 5px solid #f3f3f3;
        -webkit-animation: appstle_spin 1s linear infinite;
        animation: appstle_spin 1s linear infinite;
        border-top: 5px solid #555;
        border-radius: 50%;
        width: 70px;
        height: 70px;
        margin: 0 auto;
    }

    @keyframes appstle_spin {
        0% {
            -webkit-transform: rotate(0deg);
            -ms-transform: rotate(0deg);
            transform: rotate(0deg);
        }

        100% {
            -webkit-transform: rotate(360deg);
            -ms-transform: rotate(360deg);
            transform: rotate(360deg);
        }
    }
</style>
