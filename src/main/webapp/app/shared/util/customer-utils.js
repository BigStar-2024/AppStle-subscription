import moment from 'moment';

export const getCustomerIdentifier = () => {
  // const queryStringParams = urlParamsToObject(location.search || window.sessionStorage.getItem('appstle_url_params'));
  // let customerId = window?.__st?.cid;
  // const token = queryStringParams?.token;

  // if (token) {
  //   window.localStorage["customerToken"] = token;
  // }

  // const localStorageCustomerToken = window.localStorage["customerToken"]

  // return "iWXMWT2HE7BNN0cT";
  return window.sessionStorage["customerToken"];
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

export const getBillingCycleText = (billingCycle) => {
  var j = billingCycle % 10,
    k = billingCycle % 100;
  if (j == 1 && k != 11) {
    return billingCycle + `st`;
  }
  if (j == 2 && k != 12) {
    return billingCycle + `nd`;
  }
  if (j == 3 && k != 13) {
    return billingCycle + `rd`;
  }
  return billingCycle + `th`;
}

export const redirectToAccountPage = () => {
  window?.parent?.postMessage("appstle_message_to_redirect_to_account");
  window.location.href = `http://${Shopify.shop}/account`
}

export const checkIfPreventCancellationBeforeDays = (days, nextBillingDate) => {
  if (days && nextBillingDate) {
    let nextOrderDate = moment(nextBillingDate).format();
    let currentDateWithExtraDays = moment().add(parseInt(days), 'd').format();
    if (moment(nextOrderDate).isBefore(currentDateWithExtraDays)) {
      return true
    }
  } else {
    return false
  }
}

export const getLocaleDate = (date, locale) => {
  const options = {year: 'numeric', month: 'long', day: 'numeric'};
  return new Date(date).toLocaleDateString(`${locale || `en-US`}`, options)
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

  var formatString = window?.shopifyCurrencyFormat || window?.moneyFormat || themeMoneyFormat || moneyFormat || shopifyMoneyFormat;

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

  var format = formatString.match(placeholderRegex) && formatString.match(placeholderRegex).length > 1 ? formatString.match(placeholderRegex)[1] : 'amount';

  switch (format) {
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

export const replaceVariablesValue = (text, values) => {
  try {
    return text?.replace(/{{\s*(.*?)\s*}}/g, (match, key) => values[key] || match); 
  } catch (error) {
    return text;
  }
}


export const aryIannaTimeZones = [
  'EST',
  'Europe/Andorra',
  'Asia/Dubai',
  'Asia/Kabul',
  'Europe/Tirane',
  'Asia/Yerevan',
  'Antarctica/Casey',
  'Antarctica/Davis',
  'Antarctica/DumontDUrville',
  'Antarctica/Mawson',
  'Antarctica/Palmer',
  'Antarctica/Rothera',
  'Antarctica/Syowa',
  'Antarctica/Troll',
  'Antarctica/Vostok',
  'America/Argentina/Buenos_Aires',
  'America/Argentina/Cordoba',
  'America/Argentina/Salta',
  'America/Argentina/Jujuy',
  'America/Argentina/Tucuman',
  'America/Argentina/Catamarca',
  'America/Argentina/La_Rioja',
  'America/Argentina/San_Juan',
  'America/Argentina/Mendoza',
  'America/Argentina/San_Luis',
  'America/Argentina/Rio_Gallegos',
  'America/Argentina/Ushuaia',
  'Pacific/Pago_Pago',
  'Europe/Vienna',
  'Australia/Lord_Howe',
  'Antarctica/Macquarie',
  'Australia/Hobart',
  'Australia/Currie',
  'Australia/Melbourne',
  'Australia/Sydney',
  'Australia/Broken_Hill',
  'Australia/Brisbane',
  'Australia/Lindeman',
  'Australia/Adelaide',
  'Australia/Darwin',
  'Australia/Perth',
  'Australia/Eucla',
  'Asia/Baku',
  'America/Barbados',
  'Asia/Dhaka',
  'Europe/Brussels',
  'Europe/Sofia',
  'Atlantic/Bermuda',
  'Asia/Brunei',
  'America/La_Paz',
  'America/Noronha',
  'America/Belem',
  'America/Fortaleza',
  'America/Recife',
  'America/Araguaina',
  'America/Maceio',
  'America/Bahia',
  'America/Sao_Paulo',
  'America/Campo_Grande',
  'America/Cuiaba',
  'America/Santarem',
  'America/Porto_Velho',
  'America/Boa_Vista',
  'America/Manaus',
  'America/Eirunepe',
  'America/Rio_Branco',
  'America/Nassau',
  'Asia/Thimphu',
  'Europe/Minsk',
  'America/Belize',
  'America/St_Johns',
  'America/Halifax',
  'America/Glace_Bay',
  'America/Moncton',
  'America/Goose_Bay',
  'America/Blanc-Sablon',
  'America/Toronto',
  'America/Nipigon',
  'America/Thunder_Bay',
  'America/Iqaluit',
  'America/Pangnirtung',
  'America/Atikokan',
  'America/Winnipeg',
  'America/Rainy_River',
  'America/Resolute',
  'America/Rankin_Inlet',
  'America/Regina',
  'America/Swift_Current',
  'America/Edmonton',
  'America/Cambridge_Bay',
  'America/Yellowknife',
  'America/Inuvik',
  'America/Creston',
  'America/Dawson_Creek',
  'America/Fort_Nelson',
  'America/Vancouver',
  'America/Whitehorse',
  'America/Dawson',
  'Indian/Cocos',
  'Europe/Zurich',
  'Africa/Abidjan',
  'Pacific/Rarotonga',
  'America/Santiago',
  'America/Punta_Arenas',
  'Pacific/Easter',
  'Asia/Shanghai',
  'Asia/Urumqi',
  'America/Bogota',
  'America/Costa_Rica',
  'America/Havana',
  'Atlantic/Cape_Verde',
  'America/Curacao',
  'Indian/Christmas',
  'Asia/Nicosia',
  'Asia/Famagusta',
  'Europe/Prague',
  'Europe/Berlin',
  'Europe/Copenhagen',
  'America/Santo_Domingo',
  'Africa/Algiers',
  'America/Guayaquil',
  'Pacific/Galapagos',
  'Europe/Tallinn',
  'Africa/Cairo',
  'Africa/El_Aaiun',
  'Europe/Madrid',
  'Africa/Ceuta',
  'Atlantic/Canary',
  'Europe/Helsinki',
  'Pacific/Fiji',
  'Atlantic/Stanley',
  'Pacific/Chuuk',
  'Pacific/Pohnpei',
  'Pacific/Kosrae',
  'Atlantic/Faroe',
  'Europe/Paris',
  'Europe/London',
  'Asia/Tbilisi',
  'America/Cayenne',
  'Africa/Accra',
  'Europe/Gibraltar',
  'America/Godthab',
  'America/Danmarkshavn',
  'America/Scoresbysund',
  'America/Thule',
  'Europe/Athens',
  'Atlantic/South_Georgia',
  'America/Guatemala',
  'Pacific/Guam',
  'Africa/Bissau',
  'America/Guyana',
  'Asia/Hong_Kong',
  'America/Tegucigalpa',
  'America/Port-au-Prince',
  'Europe/Budapest',
  'Asia/Jakarta',
  'Asia/Pontianak',
  'Asia/Makassar',
  'Asia/Jayapura',
  'Europe/Dublin',
  'Asia/Jerusalem',
  'Asia/Kolkata',
  'Indian/Chagos',
  'Asia/Baghdad',
  'Asia/Tehran',
  'Atlantic/Reykjavik',
  'Europe/Rome',
  'America/Jamaica',
  'Asia/Amman',
  'Asia/Tokyo',
  'Africa/Nairobi',
  'Asia/Bishkek',
  'Pacific/Tarawa',
  'Pacific/Enderbury',
  'Pacific/Kiritimati',
  'Asia/Pyongyang',
  'Asia/Seoul',
  'Asia/Almaty',
  'Asia/Qyzylorda',
  'Asia/Qostanay',
  'Asia/Aqtobe',
  'Asia/Aqtau',
  'Asia/Atyrau',
  'Asia/Oral',
  'Asia/Beirut',
  'Asia/Colombo',
  'Africa/Monrovia',
  'Europe/Vilnius',
  'Europe/Luxembourg',
  'Europe/Riga',
  'Africa/Tripoli',
  'Africa/Casablanca',
  'Europe/Monaco',
  'Europe/Chisinau',
  'Pacific/Majuro',
  'Pacific/Kwajalein',
  'Asia/Yangon',
  'Asia/Ulaanbaatar',
  'Asia/Hovd',
  'Asia/Choibalsan',
  'Asia/Macau',
  'America/Martinique',
  'Europe/Malta',
  'Indian/Mauritius',
  'Indian/Maldives',
  'America/Mexico_City',
  'America/Cancun',
  'America/Merida',
  'America/Monterrey',
  'America/Matamoros',
  'America/Mazatlan',
  'America/Chihuahua',
  'America/Ojinaga',
  'America/Hermosillo',
  'America/Tijuana',
  'America/Bahia_Banderas',
  'Asia/Kuala_Lumpur',
  'Asia/Kuching',
  'Africa/Maputo',
  'Africa/Windhoek',
  'Pacific/Noumea',
  'Pacific/Norfolk',
  'Africa/Lagos',
  'America/Managua',
  'Europe/Amsterdam',
  'Europe/Oslo',
  'Asia/Kathmandu',
  'Pacific/Nauru',
  'Pacific/Niue',
  'Pacific/Auckland',
  'Pacific/Chatham',
  'America/Panama',
  'America/Lima',
  'Pacific/Tahiti',
  'Pacific/Marquesas',
  'Pacific/Gambier',
  'Pacific/Port_Moresby',
  'Pacific/Bougainville',
  'Asia/Manila',
  'Asia/Karachi',
  'Europe/Warsaw',
  'America/Miquelon',
  'Pacific/Pitcairn',
  'America/Puerto_Rico',
  'Asia/Gaza',
  'Asia/Hebron',
  'Europe/Lisbon',
  'Atlantic/Madeira',
  'Atlantic/Azores',
  'Pacific/Palau',
  'America/Asuncion',
  'Asia/Qatar',
  'Indian/Reunion',
  'Europe/Bucharest',
  'Europe/Belgrade',
  'Europe/Kaliningrad',
  'Europe/Moscow',
  'Europe/Simferopol',
  'Europe/Kirov',
  'Europe/Astrakhan',
  'Europe/Volgograd',
  'Europe/Saratov',
  'Europe/Ulyanovsk',
  'Europe/Samara',
  'Asia/Yekaterinburg',
  'Asia/Omsk',
  'Asia/Novosibirsk',
  'Asia/Barnaul',
  'Asia/Tomsk',
  'Asia/Novokuznetsk',
  'Asia/Krasnoyarsk',
  'Asia/Irkutsk',
  'Asia/Chita',
  'Asia/Yakutsk',
  'Asia/Khandyga',
  'Asia/Vladivostok',
  'Asia/Ust-Nera',
  'Asia/Magadan',
  'Asia/Sakhalin',
  'Asia/Srednekolymsk',
  'Asia/Kamchatka',
  'Asia/Anadyr',
  'Asia/Riyadh',
  'Pacific/Guadalcanal',
  'Indian/Mahe',
  'Africa/Khartoum',
  'Europe/Stockholm',
  'Asia/Singapore',
  'America/Paramaribo',
  'Africa/Juba',
  'Africa/Sao_Tome',
  'America/El_Salvador',
  'Asia/Damascus',
  'America/Grand_Turk',
  'Africa/Ndjamena',
  'Indian/Kerguelen',
  'Asia/Bangkok',
  'Asia/Dushanbe',
  'Pacific/Fakaofo',
  'Asia/Dili',
  'Asia/Ashgabat',
  'Africa/Tunis',
  'Pacific/Tongatapu',
  'Europe/Istanbul',
  'America/Port_of_Spain',
  'Pacific/Funafuti',
  'Asia/Taipei',
  'Europe/Kiev',
  'Europe/Uzhgorod',
  'Europe/Zaporozhye',
  'Pacific/Wake',
  'America/New_York',
  'America/Detroit',
  'America/Kentucky/Louisville',
  'America/Kentucky/Monticello',
  'America/Indiana/Indianapolis',
  'America/Indiana/Vincennes',
  'America/Indiana/Winamac',
  'America/Indiana/Marengo',
  'America/Indiana/Petersburg',
  'America/Indiana/Vevay',
  'America/Chicago',
  'America/Indiana/Tell_City',
  'America/Indiana/Knox',
  'America/Menominee',
  'America/North_Dakota/Center',
  'America/North_Dakota/New_Salem',
  'America/North_Dakota/Beulah',
  'America/Denver',
  'America/Boise',
  'America/Phoenix',
  'America/Los_Angeles',
  'America/Anchorage',
  'America/Juneau',
  'America/Sitka',
  'America/Metlakatla',
  'America/Yakutat',
  'America/Nome',
  'America/Adak',
  'Pacific/Honolulu',
  'America/Montevideo',
  'Asia/Samarkand',
  'Asia/Tashkent',
  'America/Caracas',
  'Asia/Ho_Chi_Minh',
  'Pacific/Efate',
  'Pacific/Wallis',
  'Pacific/Apia',
  'Africa/Johannesburg'
];
