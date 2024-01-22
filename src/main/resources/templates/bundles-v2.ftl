<div id="appstle_initialLoader" style="flex-direction: column; display: flex; justify-content: center; align-items: center; position: relative; padding: 80px 0;">
  <svg role="status" class="inline w-6 h-6 text-gray-200 animate-spin fill-blue-600 appstle-loader-small" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor"></path>
    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill"></path>
  </svg>
</div>
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
${bundleTopHtml}
<div id="root"></div>
${bundleBottomHtml}
<script>

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
    var appstle_custom_bundle_redirect_url = "${customBundleRedirectUrl}"
    window['isAppstleBuildABox'] = true;
    window.sessionStorage.setItem("external-bundle-token", appstle_bundle_external_token);
    window.sessionStorage["bundleToken"] = "${token}" || "";
</script>
<script defer src="${bundleJavascript}" type="text/javascript"></script>
<link href="${bundleCss}" rel="stylesheet">

