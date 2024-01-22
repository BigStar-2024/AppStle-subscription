<div id="appstle_initialLoader" style="flex-direction: column; display: flex; justify-content: center; align-items: center; position: relative;">
            <div class="appstle_preloader appstle_loader--big"></div>
</div>
${bundleTopHtml}
<iframe id="appstle_iframe"></iframe>
${bundleBottomHtml}
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
<style>
  *::-webkit-scrollbar {
      background-color: #fff;
      width: 16px;
  }

  *::-webkit-scrollbar-track {
      background-color: #fff;
  }

  *::-webkit-scrollbar-thumb {
      background-color: #babac0;
      border-radius: 16px;
      border: 4px solid #fff;
  }

  *::-webkit-scrollbar-button {
      display:none;
  }

  <#if bundleWithoutScroll>

  <#else>
      #appstle_iframe {
      height: 100vh !important;
      z-index: 987654329876;
      position: relative;
    }
  </#if>
    ${bundle_iframe_css}
</style>

<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.3.2/iframeResizer.min.js" integrity="sha512-dnvR4Aebv5bAtJxDunq3eE8puKAJrY9GBJYl9GC6lTOEC76s1dbDfJFcL9GyzpaDW4vlI/UjR8sKbc1j6Ynx6w==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script>
    iFrameResize({
        heightCalculationMethod : 'bodyOffset',
        checkOrigin: false
    }, '#appstle_iframe');
</script>
<script>
var getGeneratedPageURL = () => {
  var getBlobURL = (code, type) => {
    var blob = new Blob([code], { type })
    return URL.createObjectURL(blob)
  }

  var appstle_app_proxy_path_prefix = "${appProxyPathPrefix}";
  var appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};
  var appstle_api_key = "";
  var appstle_shop = JSON.stringify({shop: window?.Shopify?.shop});
  var appstle_bundleRedirectOnCartPage = "${bundleRedirectOnCartPage}";
  var appstle__st = JSON.stringify(window?.__st)
  var appstle_url_params = location.search;
  var appstle_RS = JSON.stringify(window?.RS);
  var appstle_RSConfig = JSON.stringify(window?.RSConfig);
  var appstle_money_format = <#if renderType == 'html'>"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;
  var appstle_public_domain = "${publicDomain}";
  var appstle_bundle_external_token = "${appstleSubscriptionsExternalToken}";
  var appstle_custom_bundle_redirect_url = "${customBundleRedirectUrl}";
  window['isAppstleBuildABox'] = true;
  window.addEventListener('message', appstleHandleMessageToRedirect, false);

    function appstleHandleMessageToRedirect(event) {
        if (String(event?.data)?.indexOf("appstle_message_to_redirect_to_checkout") !== -1) {
            var bundleCheckoutData = (JSON.parse(event.data));
            var discountCode = bundleCheckoutData.discountCode;
            var redirectType = bundleCheckoutData.redirectType;
            var redirectURL = bundleCheckoutData.redirectURL;
            if (redirectType === 'CART' || Shopify?.shop === "222meals.myshopify.com") {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = 'http://' + appstle_public_domain + '/cart'
              } else {
                window.location.href = 'http://' + appstle_public_domain + '/discount/' + discountCode + '?redirect=/cart'
              }
            } else if (redirectType === 'CHECKOUT') {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = 'http://' + appstle_public_domain + '/checkout'
              } else {
                window.location.href = 'http://' + appstle_public_domain + '/discount/' + discountCode + '?redirect=/checkout'
              }
            } else if (redirectType === 'CUSTOM') {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = redirectURL;
              } else {
                fetch('http://' + appstle_public_domain + '/discount/' + discountCode)
                .then(function(res) {
                  window.location.href = redirectURL;
                })
                .catch(function(err) {
                  window.location.href = redirectURL;
                })
              }
            }
        }
    }
  // document.getElementById("appstle_iframe").scrollIntoView();
  var source = '<html>'
      +'<' + 'head' + '>'
      +'    <script>'
      +'        window["Shopify"] = ' + appstle_shop + ';'
      +'        window["__st"] = ' + appstle__st + ';'
      +'        window.sessionStorage.setItem("appstle_url_params", "' + appstle_url_params + '");'
      +'        window.sessionStorage.setItem("external-bundle-token", "' + appstle_bundle_external_token + '");'
      +'        window.appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};'
      +'        window.appstle_money_format = {% raw %}"${moneyFormat}"{% endraw %};'
      +'        window.appstle_public_domain = "${publicDomain}";'
      +'        window["RS"] = ' + appstle_RS + ';'
      +'        window["RSConfig"] = ' + appstle_RSConfig + ';'
      +'        window["appstle_app_proxy_path_prefix"] = "' + appstle_app_proxy_path_prefix + '";'
      +'        window["isAppstleBuildABox"] = true;'
      +'    <\/script>'
      +'    <link href="https://subscription-admin.appstle.com/content/vendors.css?v=${version}" rel="stylesheet">'
      +'    <link href="https://subscription-admin.appstle.com/content/customer.css?v=${version}" rel="stylesheet">'
      +'    <script defer type="text/javascript" src="https://subscription-admin.appstle.com/app/vendors.chunk.js?v=${version}"><\/script>'
      +'    <script defer type="text/javascript" src="https://subscription-admin.appstle.com/app/bundles.bundle.js?v=${version}"><\/script>'
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
      +'   *::-webkit-scrollbar {'
      +'      background-color: #fff;'
      +'      width: 16px;'
      +'   }'

      +'   *::-webkit-scrollbar-track {'
      +'      background-color: #fff;'
      +'   }'

      +'   *::-webkit-scrollbar-thumb {'
      +'      background-color: #babac0;'
      +'      border-radius: 16px;'
      +'      border: 4px solid #fff;'
      +'   }'

      +'   *::-webkit-scrollbar-button {'
      +'      display:none;'
      +'   }'
      +'  <\/style>'
      + '${bundleDynamicScript}'
      +'</' + 'head' + '>'
       +'<' + 'body' + '>'
      +'    <div style="overflow: auto;" id="root">'
      +'        <div style="margin: 10% 0 0 43%; flex-direction: column; display: flex; justify-content: center; align-items: center;">'
      +'           <div class="appstle_preloader appstle_loader--big"></div>'
      +'        </div>'
      +'    </div>'
      +'</' + 'body' + '>'
    +'</html>'

  var iframe = document.querySelector('#appstle_iframe')
  iframe.contentWindow.document.open();
  iframe.contentWindow.document.write(source);
  iframe.contentWindow.document.close();
  // var url = getBlobURL(source, 'text/html')
  // var iframe = document.querySelector('#appstle_iframe')
  // iframe.src = url
  var loaderElement = document.getElementById('appstle_initialLoader');
  while (loaderElement.firstChild) {
    loaderElement.removeChild(loaderElement.firstChild);
  }
}

function appstlePollInitData() {

  if (window?.Shopify && window?.__st) {
    // if (location.search.indexOf('token') !== -1 || location.search.indexOf('customerId') !== -1 || window?.__st?.cid) {
    //   getGeneratedPageURL()
    // } else {
    //   window.location.href = 'http://' + Shopify.shop + '/account'
    // }
    getGeneratedPageURL()
  } else {
    setTimeout(appstlePollInitData, 100);
  }
}

setTimeout(appstlePollInitData, 100);




</script>
