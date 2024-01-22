<div id="appstle_initialLoader" style="flex-direction: column; display: flex; justify-content: center; align-items: center; position: relative;">
    <div class="appstle_preloader appstle_loader--big"></div>
</div>
${customerPortalTopHtml}
<iframe id="appstle_iframe"></iframe>
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
<style>
    iframe{
        width: 1px;
        min-width: 100%;
        border: none;
    }
    body {
        margin: 0;
    }
    .appstle_preloader {
        width: 60px;
        height: 60px;
        position: absolute;
        top: 70px;
        left: calc(50% - 15px);
        z-index: 98765;
    }

    .appstle_modal_popup {
        z-index: 99999999999;
        position:relative;
    }
    .appstle-modal-backdrop-popup {
        position: fixed;
        top: 0;
        left: 0;
        z-index: 2000;
        width: 100vw;
        height: 100vh;
        background-color: #000;
        opacity: 0.15;
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.3.2/iframeResizer.min.js" integrity="sha512-dnvR4Aebv5bAtJxDunq3eE8puKAJrY9GBJYl9GC6lTOEC76s1dbDfJFcL9GyzpaDW4vlI/UjR8sKbc1j6Ynx6w==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
    function pollIframeResize() {
        if (window['iFrameResize']) {
            iFrameResize({
                heightCalculationMethod : 'bodyOffset',
                checkOrigin: false
            }, '#appstle_iframe');
        } else {
            setTimeout(pollIframeResize, 30);
        }
    }
    pollIframeResize()
</script>
<script>
    var appstle_app_proxy_path_prefix = "${appProxyPathPrefix}";
    var getGeneratedPageURL = () => {
        var getBlobURL = (code, type) => {
            var blob = new Blob([code], { type })
            return URL.createObjectURL(blob)
        }
        var appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};
        var appstle__st = JSON.stringify(window?.__st)
        var appstle_url_params = location.search;
        var appstle_money_format = <#if renderType == 'html'>"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;
        var appstle_public_domain = "${publicDomain}";
        // var appstle_customer_portal_external_token = window.sessionStorage["jwtToken"] || "${appstleSubscriptionsExternalToken}";

        window.addEventListener('message', appstleHandleMessageToRedirect, false);


        function appstleHandleMessageToRedirect(event) {
            switch(event.data) {
                case "appstle_message_to_redirect_to_account":
                    window.location.href = 'http://' + Shopify.shop + '/account'
                    break;
                case "appstle_popup_opened":
                    $("#appstle_iframe").parents("*:not(body, html)").addClass("appstle_modal_popup");
                    $("body").append('<div class="appstle-modal-backdrop-popup"></div>');
                    break;
                case "appstle_popup_closed":
                    $(".appstle_modal_popup").removeClass("appstle_modal_popup");
                    $(".appstle-modal-backdrop-popup").remove();
                    break;
                case "appstle_scroll_top":
                    document.body.scrollTop = document.documentElement.scrollTop = 0;
                    break;
                case "appstle_scroll_iframe_top":
                    $('html,body').animate({scrollTop: $("body").offset().top}, 'slow');
                    break;

            }
        }
        var source = '<html>'
                +' <head>'
                +'    <script>'
                +'        window["Shopify"] = {};'
                +'        window["__st"] = ' + appstle__st + ';'
                +'        window.sessionStorage.setItem("appstle_url_params", "' + appstle_url_params + '");'
                +'        window.sessionStorage.setItem("customerToken", "' + window.sessionStorage["customerToken"] + '");'
                +'        window.appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};'
                +'        window.appstle_api_key = "";'
                +'        window.appstle_money_format = "' + appstle_money_format + '";'
                +'        window.appstle_public_domain = "' + location.host + '";'
                +'        window["Shopify"]["shop"] = "' + location.host + '";'
                +'        window["isAppstleCustomerPortal"] = true;'
                +'        window["appstleCustomerData"] = {};'
                +'        window["appstleCustomerData"]["customerId"] = "' + (appstleCustomerData.customerId || '') + '";'
                +'        window["appstleCustomerData"]["customerName"] = "' + (appstleCustomerData.customerName || '') + '";'
                +'        window["appstleCustomerData"]["firstName"] = "' + (appstleCustomerData.firstName || '') + '";'
                +'        window["appstleCustomerData"]["lastName"] = "' + (appstleCustomerData.lastName || '') + '";'
                +'    <\/script>'
                +'    <link rel="stylesheet" href="${customerCss}">'
                +'    <script defer type="text/javascript" src="${customerJavascript}"><\/script>'
                +'<script defer src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.3.2/iframeResizer.contentWindow.min.js" integrity="sha512-14SY6teTzhrLWeL55Q4uCyxr6GQOxF3pEoMxo2mBxXwPRikdMtzKMYWy2B5Lqjr6PHHoGOxZgPaxUYKQrSmu0A==" crossorigin="anonymous" referrerpolicy="no-referrer"><\/script>'
                +'<style>'
                +'    body {'
                +'        height: auto !important; /* Essential for resizing */'
                +'        min-height: 0 !important; /* Essential for resizing */'
                +'     }'
                +'    .appstle_preloader {'
                +'       width: 60px;'
                +'       height: 60px;'
                +'       position: absolute;'
                +'       top: 70px;'
                +'       left: calc(50% - 15px);'
                +'       z-index: 98765;'
                +'    }'
                +'   .appstle_loader--big {'
                +'        border: 5px solid #f3f3f3;'
                +'        -webkit-animation: appstle_spin 1s linear infinite;'
                +'        animation: appstle_spin 1s linear infinite;'
                +'        border-top: 5px solid #555;'
                +'        border-radius: 50%;'
                +'        width: 70px;'
                +'        height: 70px;'
                +'        margin: 0 auto;'
                +'    }'
                +'   .sm\\:as-align-middle {'
                +'      vertical-align: top !important;'
                +'   }'
                +'   .swal-overlay--show-modal .swal-modal {'
                +'      vertical-align: top !important;'
                +'   }'
                +'   .as-modal-root {'
                +'      align-items: flex-start !important;'
                +'   }'

                +'   @keyframes appstle_spin {'
                +'    0% {'
                +'    -webkit-transform: rotate(0deg);'
                +'    -ms-transform: rotate(0deg);'
                +'    transform: rotate(0deg);'
                +'   }'

                +'   100% {'
                +'    -webkit-transform: rotate(360deg);'
                +'    -ms-transform: rotate(360deg);'
                +'    transform: rotate(360deg);'
                +'  }'
                +' }'
                +'  <\/style>'
                +'${customerPortalDynamicScript}'
                +'</head>'
                +'<body>'
                +'    <div style="overflow: auto;" id="root">'
                +'    </div>'
                +'        <div style="margin: 10% 0 0 43%; flex-direction: column; display: flex; justify-content: center; align-items: center;" id="appstle_initialLoader">'
                +'           <div class="appstle_preloader appstle_loader--big"></div>'
                +'        </div>'
                +'</' + 'body' + '>'
                +'</html>'

        var iframe = document.querySelectorAll('#appstle_iframe')
        iframe.forEach(function(item) {
            item.contentWindow.document.open();
            item.contentWindow.document.write(source);
            item.contentWindow.document.close();
        })

        // var url = getBlobURL(source, 'text/html')
        // var iframe = document.querySelector('#appstle_iframe')
        // iframe.src = url
        var loaderElement = document.querySelectorAll('#appstle_initialLoader');
        loaderElement.forEach(function(item) {
              item.remove()
        })
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

    setTimeout(appstlePollInitData, 100);




</script>
