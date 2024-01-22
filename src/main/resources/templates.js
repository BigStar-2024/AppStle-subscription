Commerceinstruments = window.Commerceinstruments || {};
Commerceinstruments.templates = {
  Platform: 'shopify',
  StoreName: 'test-cp-286',
  AutocompleteLayout: 'multicolumn',
  AutocompleteStyle: 'ITEMS_MULTICOLUMN_LIGHT',
  AutocompleteShowMoreLink: 'Y',
  AutocompleteIsMulticolumn: 'Y',
  AutocompleteTemplate: '<div class="aikon-ac-results-content"><div class="aikon-results-html" style="cursor:auto;" id="aikon-ac-results-html-container"></div><div class="aikon-ac-results-columns"><div class="aikon-ac-results-column"><ul class="aikon-ac-results-list" id="aikon-ac-items-container-1"></ul><ul class="aikon-ac-results-list" id="aikon-ac-items-container-2"></ul><ul class="aikon-ac-results-list" id="aikon-ac-items-container-3"></ul></div><div class="aikon-ac-results-column"><ul class="aikon-ac-results-multicolumn-list" id="aikon-ac-items-container-4"></ul></div></div></div>',
  AutocompleteMobileTemplate: '<div class="aikon-ac-results-content"><div class="aikon-mobile-top-panel"><div class="aikon-close-button"><button type="button" class="aikon-close-button-arrow"></button></div><form action="#" style="margin: 0px"><div class="aikon-search"><input id="aikon-mobile-search-input" autocomplete="off" class="aikon-input-style aikon-mobile-input-style"></div><div class="aikon-clear-button-container"><button type="button" class="aikon-clear-button" style="visibility: hidden"></button></div></form></div><ul class="aikon-ac-results-list" id="aikon-ac-items-container-1"></ul><ul class="aikon-ac-results-list" id="aikon-ac-items-container-2"></ul><ul class="aikon-ac-results-list" id="aikon-ac-items-container-3"></ul><ul id="aikon-ac-items-container-4"></ul><div class="aikon-results-html" style="cursor:auto;" id="aikon-ac-results-html-container"></div><div class="aikon-close-area" id="aikon-ac-close-area"></div></div>',
  AutocompleteItem: '<li class="aikon-product" id="aikon-ac-product-${product_id}"><a href="${autocomplete_link}" class="aikon-item" draggable="false"><div class="aikon-thumbnail"><img src="${image_link}" class="aikon-item-image" alt=""></div>${autocomplete_product_code_html}${autocomplete_product_attribute_html}<span class="aikon-title">${title}</span><span class="aikon-description">${description}</span>${autocomplete_prices_html}${autocomplete_in_stock_status_html}${reviews_html}</a></li>',
  AutocompleteMobileItem: '<li class="aikon-product" id="aikon-ac-product-${product_id}"><a href="${autocomplete_link}" class="aikon-item"><div class="aikon-thumbnail"><img src="${image_link}" class="aikon-item-image" alt=""></div><div class="aikon-product-info">${autocomplete_product_code_html}${autocomplete_product_attribute_html}<span class="aikon-title">${title}</span><span class="aikon-description">${description}</span><div class="aikon-ac-prices-container">${autocomplete_prices_html}${autocomplete_in_stock_status_html}</div>${reviews_html}</div></a></li>',
  ResultsShow: 'Y',
  ShowBestsellingSorting: 'Y',
  ShowDiscountSorting: 'Y',
  ColorsCSS: '.aikon-ac-over-nodrop { background: #EEEEEE; }'
}
