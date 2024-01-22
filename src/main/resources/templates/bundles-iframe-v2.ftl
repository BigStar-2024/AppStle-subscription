<div id="appstle_initialLoader" style="height: 100px; width: 100%; display: flex; justify-content: center; align-items: center;">
  <svg role="status" class="inline w-6 h-6 text-gray-200 animate-spin fill-blue-600 appstle-loader-small" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"></path>
    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"></path>
  </svg>
</div>
${bundleTopHtml}
<iframe id="appstle_iframe"></iframe>
${bundleBottomHtml}
<style>
    .appstle-loader-small {
      position: absolute;
      top: 50%;
      left: 50%;
    }
    .inline{
      display: inline;
    }

    .h-6{
      height: 1.5rem;
    }

    .w-6{
      width: 1.5rem;
    }

    @keyframes spin{
      to{
        transform: rotate(360deg);
      }
    }

    .animate-spin{
      animation: spin 1s linear infinite;
    }

    .fill-blue-600{
      fill: #2563eb;
    }

    .text-gray-200{
      --tw-text-opacity: 1;
      color: rgb(229 231 235 / var(--tw-text-opacity));
    }

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
  var appstle_shop = JSON.stringify({shop: window?.Shopify?.shop, currency: window?.Shopify?.currency, routes: {root: window?.Shopify?.routes?.root}});
  var appstle_bundleRedirectOnCartPage = "${bundleRedirectOnCartPage}";
  var appstle__st = JSON.stringify(window?.__st)
  var appstle_url_params = location.search;
  var appstle_RS = JSON.stringify(window?.RS);
  var appstle_RSConfig = JSON.stringify(window?.RSConfig);
  var appstle_money_format = <#if renderType == 'html'>"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;
  var appstle_public_domain = "${publicDomain}" + Shopify.routes.root;
  var appstle_bundle_external_token = "${appstleSubscriptionsExternalToken}";
  var appstle_custom_bundle_redirect_url = "${customBundleRedirectUrl}";
  window['isAppstleBuildABox'] = true;
  window.addEventListener('message', appstleHandleMessageToRedirect, false);
  var bundleToken = "${token}";

    function appstleHandleMessageToRedirect(event) {
        if (String(event?.data)?.indexOf("appstle_message_to_redirect_to_checkout") !== -1) {
            var bundleCheckoutData = (JSON.parse(event.data));
            var discountCode = bundleCheckoutData.discountCode;
            var redirectType = bundleCheckoutData.redirectType;
            var redirectURL = bundleCheckoutData.redirectURL;
            if (redirectType === 'CART' || Shopify?.shop === "222meals.myshopify.com") {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = 'http://' + appstle_public_domain + 'cart'
              } else {
                window.location.href = 'http://' + appstle_public_domain + 'discount/' + discountCode + '?redirect=' + Shopify.routes.root + 'cart'
              }
            } else if (redirectType === 'CHECKOUT') {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = 'http://' + appstle_public_domain + 'checkout'
              } else {
                window.location.href = 'http://' + appstle_public_domain + 'discount/' + discountCode + '?redirect=' + Shopify.routes.root + 'checkout'
              }
            } else if (redirectType === 'CUSTOM') {
              if (discountCode === "appstle_no_discount") {
                  window.location.href = redirectURL;
              } else {
                fetch('http://' + appstle_public_domain + 'discount/' + discountCode)
                .then(function(res) {
                  window.location.href = redirectURL;
                })
                .catch(function(err) {
                  window.location.href = redirectURL;
                })
              }
            }
        } else if (String(event?.data)?.indexOf("appstle_scroll_top") !== -1) {
              document.body.scrollTop = document.documentElement.scrollTop = 0;
          } else if (String(event?.data)?.indexOf("appstle_scroll_iframe_top") !== -1) {
            appstle_jQuery('html,body').animate({scrollTop: appstle_jQuery("body").offset().top}, 'slow');
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
      +'        window.sessionStorage.setItem("bundleToken", "' + bundleToken + '");'
      +'        window.appstle_useUrlWithCustomerId = ${useUrlWithCustomerId};'
      +'        window.appstle_money_format = <#if renderType == "html">"${moneyFormat}"<#else>{% raw %}"${moneyFormat}"{% endraw %}</#if>;'
      +'        window.appstle_public_domain = "${publicDomain}";'
      +'        window["RS"] = ' + appstle_RS + ';'
      +'        window["RSConfig"] = ' + appstle_RSConfig + ';'
      +'        window["appstle_app_proxy_path_prefix"] = "' + appstle_app_proxy_path_prefix + '";'
      +'        window["isAppstleBuildABox"] = true;'
      +'    <\/script>'
      +'    <link href="${bundleCss}?v=${version}" rel="stylesheet">'
      +'    <script defer type="text/javascript" src="${bundleJavascript}?v=${version}"><\/script>'
      +'<script defer src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.3.2/iframeResizer.contentWindow.min.js" integrity="sha512-14SY6teTzhrLWeL55Q4uCyxr6GQOxF3pEoMxo2mBxXwPRikdMtzKMYWy2B5Lqjr6PHHoGOxZgPaxUYKQrSmu0A==" crossorigin="anonymous" referrerpolicy="no-referrer"><\/script>'
      +'<style>'
      +'    body {'
      +'        height: auto !important; /* Essential for resizing */'
      +'        min-height: 0 !important; /* Essential for resizing */'
      +'     }'
      +'    .inline{'
      +'      display: inline;'
      +'    }'
      +'    .h-6{'
      +'       height: 1.5rem;'
      +'    }'
      +'    w-6{'
      +'     width: 1.5rem;'
      +'    }'
      +'    @keyframes spin{'
      +'     to{'
      +'       transform: rotate(360deg);'
      +'     }'
      +'    }'
      +'    .animate-spin{'
      +'      animation: spin 1s linear infinite;'
      +'    }'
      +'    .fill-blue-600{'
      +'      fill: #2563eb;'
      +'    }'
      +'    .text-gray-200{'
      +'      --tw-text-opacity: 1;'
      +'      color: rgb(229 231 235 / var(--tw-text-opacity));'
      +'    }'
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
      +'        <div style="height: 100px; width: 100%; display: flex; justify-content: center; align-items: center;">'
      +'           <svg role="status" class="inline w-6 h-6 text-gray-200 animate-spin fill-blue-600 appstle-loader-small" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">'
      +'              <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"></path>'
      +'              <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"></path>'
      +'           </svg>'
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
