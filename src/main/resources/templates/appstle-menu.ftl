<div id='root'></div>


<#--<script src='${appstleMenuJavascript}'></script>-->
<#--<link href='${appstleMenuCss}' rel='stylesheet'/>-->


<script id="appstle-manu-liquid">
    var appstleMenu = ${appstleMenuSettings}
    var appstleMenuLabelsSetting = ${appstleMenuLabels}
    var storeFrontAccessKey = "${storeFrontAccessKey}"
    window.appstleMenu = {
        menu: appstleMenu,
        appstleMenuLabels: appstleMenuLabelsSetting,
        storeFrontAccessKey: storeFrontAccessKey,
    }
</script>

<style>
    .page-width.page-width--narrow {
        min-width: 100% !important;
        padding: 0 !important;
    }

    .main-page-title.page-title {
        display: none;
        padding: 0 !important;
    }
</style>

<script src='https://subscription-admin.appstle.com/app/appstle-menu.bundle.js' defer></script>
<link href='https://subscription-admin.appstle.com/content/appstle-menu.css' rel='stylesheet' />
