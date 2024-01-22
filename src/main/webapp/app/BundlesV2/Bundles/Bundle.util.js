export const getToken = () => {
    const queryStringParams = urlParamsToObject(location.search || window.sessionStorage.getItem('appstle_url_params'));
    const token = queryStringParams?.token;
    if (token) {
        window.sessionStorage["bundleToken"] = token;
    }
    const localStorageBundleToken = window.sessionStorage["bundleToken"]
    let pathNameToken = '';
    if (location.pathname.indexOf('/bb/') !== -1) {
        pathNameToken = location.pathname.split('/bb/')[1];
    }
    return token || localStorageBundleToken || pathNameToken;
}

export function urlParamsToObject(search) {
    var queryStringTokens = search?.substr(1)?.split("&") || location.search.substr(1).split("&");
    var result = {};
    for (var index = 0; index < queryStringTokens.length; index++) {
      var keyValues = queryStringTokens[index].split("=")
      result[keyValues[0]] = keyValues[1];
    }
    return result
  }

export const getCurrentConvertedCurrencyPrice = (price) => {
    // return price * (Shopify?.currency?.rate ? parseFloat(Shopify?.currency?.rate) : 1)
    return price;
}

export const formatPrice = (price) => {

    var moneyFormat = window?.appstle_money_format;

    if (typeof price === 'string') {
        price = price.replace('.', '');
    }

    var value = '';
    var placeholderRegex = /\{\{\s*(\w+)\s*\}\}/;
    var shopifyMoneyFormat = typeof Shopify !== 'undefined' && Shopify.money_format && Shopify.money_format.length > 1 ? Shopify.money_format : '';
    var themeMoneyFormat;
    if (typeof theme !== 'undefined') {
        if (theme.moneyFormat) {
            themeMoneyFormat = theme.moneyFormat;
        } else if (theme.money_format) {
            themeMoneyFormat = theme.money_format;
        } else if (theme.settings && theme.settings.moneyFormat) {
            themeMoneyFormat = theme.settings.moneyFormat;
        } else {
            themeMoneyFormat = theme.strings ? theme.strings.moneyFormat : '';
        }
    } else {
        themeMoneyFormat = '';
    }

    function getMoneyFormat(moneyFormat) {
        let format = "";
        if (moneyFormat !== undefined) {
            if (moneyFormat.includes("{% raw %}")) {
                format = moneyFormat.replace("{% raw %}", "")
            }
            if (format.includes("{% endraw %}")) {
                format = format.replace("{% endraw %}", "")
            }
            return format;
        }
    }

    var formatString = window?.shopifyCurrencyFormat || window?.moneyFormat || themeMoneyFormat || moneyFormat || shopifyMoneyFormat || window?.RSConfig?.moneyFormat || "{{amount}}";

    function formatWithDelimiters(number, precision, thousands, decimal) {
        thousands = thousands || ',';
        decimal = decimal || '.';

        if (isNaN(number) || number === null) {
            return 0;
        }

        number = (number / 100.0).toFixed(precision);

        var parts = number.split('.');
        var dollarsAmount = parts[0].replace(
            /(\d)(?=(\d\d\d)+(?!\d))/g,
            '$1' + thousands
        );
        var centsAmount = parts[1] ? decimal + parts[1] : '';

        return dollarsAmount + centsAmount;
    }

    function appendCurrencyCode(formatString) {
        if (Shopify?.currency && Shopify?.currency?.active && shopifyMoneyFormat && !window?.appstle_money_format) {
            formatString = `${formatString} ${Shopify?.currency?.active}`
        }
        return formatString
    }

    switch (formatString.match(placeholderRegex)[1]) {
        case 'amount':
            value = formatWithDelimiters(price, 2);
            break;
        case 'amount_no_decimals':
            value = formatWithDelimiters(price, 0);
            break;
        case 'amount_with_comma_separator':
            value = formatWithDelimiters(price, 2, '.', ',');
            break;
        case 'amount_no_decimals_with_comma_separator':
            value = formatWithDelimiters(price, 0, '.', ',');
            break;
        case 'amount_no_decimals_with_space_separator':
            value = formatWithDelimiters(price, 0, ' ');
            break;
        case 'amount_with_apostrophe_separator':
            value = formatWithDelimiters(price, 2, "'");
            break;
    }
    var spanElement = document.createElement('span');
    spanElement.innerHTML = formatString.replace(placeholderRegex, value);
    return spanElement.textContent || spanElement.innerText;
}
