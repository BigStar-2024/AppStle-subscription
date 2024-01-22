${customerPortalTopHtml}
<div id="root"></div>
${customerPortalBottomHtml}
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
<script>
    var appstle_app_proxy_path_prefix = "${appProxyPathPrefix}";
    var appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};
    var appstle_api_key = "";
    var appstle_money_format = <#if renderType == 'html'>"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;
    var appstle_public_domain = location.host;
    var appstle_customer_portal_external_token = "${appstleSubscriptionsExternalToken}";
    window['isAppstleCustomerPortal'] = true;
    // window.sessionStorage.setItem("external-customer-portal-token", appstle_customer_portal_external_token);
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
                .then(res => res.json())
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
        var customerId = window?.__st?.cid;
        var token = queryStringParams?.token;
        if (!token && location.pathname.indexOf('/cp/') > -1) {
            token = location.pathname.substr(location.pathname.indexOf('/cp/') + 4);
        }
        window.sessionStorage.setItem("external-customer-portal-token", (token || 'external-customer-portal-token'));
        window.sessionStorage["customerToken"] = customerId || token;
        getGeneratedPageURL();
    }

    function getGeneratedPageURL() {
      var scripts = ["https://subscription-admin.appstle.com/app/vendors.chunk.js?v=${version}", "https://subscription-admin.appstle.com/app/customer.bundle.js?v=${version}"];
      scripts.forEach(function(src) {
           var newScript = document.createElement("script");
           newScript.src = src;
           newScript.type = "text/javascript";
           document.body.appendChild(newScript)
      })

      var styles = ["https://subscription-admin.appstle.com/content/vendors.css?v=${version}", "https://subscription-admin.appstle.com/content/customer.css?v=${version}"]
      styles.forEach(function(src) {
           var newScript = document.createElement("link");
           newScript.href = src;
           newScript.rel = "stylesheet";
           document.body.appendChild(newScript)
      })
    }

    setTimeout(appstlePollInitData, 100);
</script>

