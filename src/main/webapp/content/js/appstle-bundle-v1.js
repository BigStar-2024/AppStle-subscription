var appstleBundleInit = function() {
  var head = document.getElementsByTagName('head')[0];
  var startingTime = new Date().getTime();

  var appstleLoadScript = function(url, callback) {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    if (script.readyState) {
        script.onreadystatechange = function() {
            if (script.readyState == 'loaded' || script.readyState == 'complete') {
                script.onreadystatechange = null;
                callback();
            }
        };
    } else {
        script.onload = function() {
            callback();
        };
    }
    script.src = url;
    head.appendChild(script);
   };

  if (!RS?.Config?.disableLoadingJquery) {
    // var jQueryScript = document.createElement('script');
    // jQueryScript.src = 'https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js';
    // jQueryScript.type = 'text/javascript';
    // head.appendChild(jQueryScript);
    appstleLoadScript('https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js', function() {
        window['appstle_jQuery'] = jQuery.noConflict(true);
    });
  }

  if (typeof Mustache == 'undefined') {
    var mustacheScript = document.createElement('script');
    mustacheScript.src = 'https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.js';
    mustacheScript.type = 'text/javascript';
    head.appendChild(mustacheScript);
  }

  // Poll for jQuery to come into existance
  var checkReady = function(callback) {
    if ((RS?.Config?.disableLoadingJquery ? window.jQuery : window.appstle_jQuery) && window.Mustache && window.Shopify) {
      callback((RS?.Config?.disableLoadingJquery ? window.jQuery : window.appstle_jQuery));
    } else {
      window.setTimeout(function() {
        checkReady(callback);
      }, 20);
    }
  };

  const urlParams = new URLSearchParams(window.location.search);
  var globalUrlParameter = urlParams.get('variant');
  var prevChangeResponse = '';
  // Start polling...
  checkReady(function($) {
    var jQuery = $;
    $(function() {
      var endingTime = new Date().getTime();
      var tookTime = endingTime - startingTime;
      console.log('jQuery is loaded, after ' + tookTime + ' milliseconds!');

      function renderWidget(standAloneProduct, standAloneElement, widgetId) {
        if (!window['Shopify']) {
          return;
        }
        RS.Config = Object.assign(RS.Config, 'undefined' != typeof _RSConfig && null !== _RSConfig ? _RSConfig : {});

        window.RSConfig = RS.Config;

        var product = RSConfig.product;

        if (standAloneProduct) {
          product = standAloneProduct;
        }

        processProductVariants(product);

        var localVariantsByTitle = RSConfig.variantsByTitle;
        var localVariantsById = RSConfig.variantsById;
        var localWindowVariant;

        if (!window['products']) {
          window['products'] = {};
        }
        if (RSConfig.product) {
          window['products'][RSConfig.product.handle] = RSConfig.product;
        }

        function processProductVariants(product) {
          if (!product) {
            return;
          }

          var variants = product.variants;
          var _variantsByTitle = {};
          var _variantsById = {};
          for (var index = 0; index < variants.length; index++) {
            var variant = variants[index];
            _variantsByTitle[variant.title] = Object.assign({}, variant);
            _variantsById[variant.id] = Object.assign({}, variant);
          }
          RSConfig.variantsByTitle = _variantsByTitle;
          RSConfig.variantsById = _variantsById;
        }

        function urlParamsToObject() {
          var queryStringTokens = location.search.substr(1).split('&');
          var result = {};
          for (var index = 0; index < queryStringTokens.length; index++) {
            var keyValues = queryStringTokens[index].split('=');
            result[keyValues[0]] = keyValues[1];
          }
          return result;
        }

        function urlParam(key) {
          return urlParamsToObject()[key] || null;
        }

        function detectVariant(previousVariantId, selector, product) {
          var variantId = urlParam('variant');

          if (
            selector.closest('form[action$="/cart/add"]').find('[name=id]').length > 0 &&
            selector.closest('form[action$="/cart/add"]').find('[name=id]')[0].value
          ) {
            variantId = selector.closest('form[action$="/cart/add"]').find('[name=id]')[0].value;
            return localVariantsById[variantId];
          } else if (variantId) {
            return localVariantsById[variantId];
          } else if (Object.values(localVariantsById).some(data => data?.title == $("form select[name='options[Bundle]']")?.val())) {
            return Object.values(localVariantsById).find(data => data?.title == $("select[name='options[Bundle]']").val());
          } else {
            var titleTokens;

            titleTokens = (function() {
              var variantTokens = [];
              var singleOptionSelector = selector.closest('form[action$="/cart/add"]').find('select.single-option-selector');
              if (singleOptionSelector != null && singleOptionSelector.selectedIndex != null && singleOptionSelector.selectedIndex !== -1) {
                variantTokens.push(singleOptionSelector[singleOptionSelector.selectedIndex].value);
              } else {
                variantTokens.push(void 0);
              }
              return variantTokens;
            })();

            var variant = localVariantsByTitle[titleTokens.join(' / ')];
            if (variant) {
              return variant;
            } else {
              var variant = product.variants[0];
              return variant;
            }
          }
        }
        if (urlIsProductPage() && RSConfig?.bundle?.showOnProductPage === 'true') {
          bundleInit();
          attatchEventHandlerToBundleCheckoutButton();
          variantChangeHandler();
        }
        attatchEventHandlerToBundlePopupButton();
        attatchEventHandlerToBundleCancelButton();
        attachEventHandlerToItemCheckbox();
        attatchChangeHandlerToVariant();
        attatchChangeHandlerToSellingPlan();
        attatchEventHandlerToBundleCheckoutSellingPlanSelect();
        addCss();
        jQuery('.as-variant-select').trigger('change')
        if (RSConfig.bundle?.bundleRules && JSON.parse(RSConfig.bundle?.bundleRules).length) {
          listenToCartChanges();
        }  
      }

      function compareCustomerTags(array1, array2) {
        return array1.filter(function(n) {
          return array2.indexOf(n) != -1;
        });
      }

      // deferJquery(appstleSubscriptionFunction);

      function deferJquery(method) {
        if ((RS?.Config?.disableLoadingJquery ? window.jQuery : window.appstle_jQuery)) method();
        else
          setTimeout(function() {
            deferJquery(method);
          }, 50);
      }

      function attachMutationObserver(selector, callback) {
        const targetNode = document.querySelector(selector);
        const config = { attributes: true, childList: true, subtree: true };
        const observer = new MutationObserver(callback);
        if (targetNode && config) {
          observer.observe(targetNode, config);
        }
      }

      function variantChangeHandler() {
        attachMutationObserver('[name=id]', () => {
          jQuery('.as-bundle-wrapper').remove();
          setTimeout(bundleInit, 100);
        });
      }

      function getProductData(handle) {
        if(handle) {
          fetch(`/products/${handle}.js`)
        }
      }

      function bundleInit() {
        var { variantLevelBundles, productLevelBundles } = getBundlesRulesWithCurrentProduct();
        getProductHandles(variantLevelBundles, productLevelBundles, () => createRenderData(variantLevelBundles, productLevelBundles));
      }

      function parseBundleData() {
        var bundleRules = JSON.parse(RSConfig.bundle.bundleRules);
        bundleRules.forEach(item => {
          if (item?.variants) {
            item.variants = JSON.parse(item.variants);
          }
          if (item?.products) {
            item.products = JSON.parse(item.products);
          }
        });
        bundleRules = bundleRules.filter(bundle => bundle?.status === 'ACTIVE');
        return bundleRules;
      }

      function getBundlesRulesWithCurrentProduct() {
        var bundleRules = parseBundleData();
        var currentVariant = getCurrentVariant();
        var currentProductId = RSConfig?.product?.id || '';
        var variantLevelBundles = bundleRules.filter(rules => {
          return rules?.variants?.find(item => item?.id === parseInt(currentVariant));
        });

        var productLevelBundles = bundleRules.filter(rules => {
          return rules?.products?.find(item => item?.id === currentProductId);
        });
        return {
          variantLevelBundles,
          productLevelBundles
        };
      }

      function getCurrentVariantData() {
        return RSConfig?.product?.variants?.filter(item => item?.id === parseInt(getCurrentVariant()));
      }

      function filterCurrentVariant(variantLevelBundles) {
        return variantLevelBundles.filter(item => item?.id !== parseInt(getCurrentVariant()));
      }

      function filterVariants(variantLevelBundles) {
        return variantLevelBundles.filter(item => {
          if (item?.id !== parseInt(getCurrentVariant()) && item.productData) {
            return item;
          }
        });
      }

      function filterCurrentProduct(productLevelBundles) {
        return productLevelBundles.filter(item => item?.id !== RSConfig?.product?.id);
      }

      function createRenderData(variantLevelBundles, productLevelBundles) {
        var { variantLevelBundles, productLevelBundles } = processBundleItems(variantLevelBundles, productLevelBundles);
        var currentVariantData = getCurrentVariantData();
        var currentProductData = RSConfig.product;
        variantLevelBundles = createVariantBundleHTML(variantLevelBundles, currentVariantData, currentProductData);
        productLevelBundles = createProductBundleHTML(productLevelBundles, currentProductData);
        var bundleData = sortBundles(mergeBundles(variantLevelBundles, productLevelBundles), 'sequenceNo');
        attachBundleToDom(bundleData);
      }

      function attachBundleToDom(bundleData) {
        if (RSConfig?.bundle?.showMultipleOnProductPage === 'true') {
          bundleData.forEach(item => {
            insertBundleToDom(item?.template);
          });
        } else {
          insertBundleToDom(bundleData[0]?.template);
        }
        jQuery('.as-variant-select').trigger('change');
      }

      function processBundleItems(variantLevelBundles, productLevelBundles) {
        var { variantLevelBundles, productLevelBundles } = attachProductDataToBundles(variantLevelBundles, productLevelBundles);
        variantLevelBundles.forEach((bundle, index) => {
          bundle.variants.forEach((variant, index) => {
            if (variant.productData) {
              return processLabels(variant, bundle);
            }
          });
        });
        productLevelBundles.forEach(bundle => {
          bundle.products.forEach((product, index) => {
            if (product.productData) {
              return processLabels(product, bundle);
            }
          });
        });
        return {
          variantLevelBundles,
          productLevelBundles
        };
      }

      function processLabels(item, bundle) {
        if (bundle.bundleLevel === 'VARIANT') {
          getVariantTitle(item);
          getVariantImage(item);
        }
        getPrice(item, bundle);
        processItems(item, bundle);
        getBundleLevelDataAcrossItems(bundle);
      }

      function processItems(item, bundle) {
        if (bundle?.bundleType === "CLASSIC") {
          item['isChecked'] = true;
        }

        if (bundle?.bundleType === 'MIX_AND_MATCH') {
          item['isChecked'] = false;
          if (bundle?.maximumNumberOfItems) {
            item['isChecked'] = false;
          }
        }
      }

      function getBundleLevelDataAcrossItems(bundle) {
        if (bundle.bundleLevel === 'VARIANT') {
          bundle.totalDiscountedPrice = bundle.variants.reduce((partialSum, a) => partialSum + a.discountAmount, 0);
          bundle.formattedTotalDiscountedPrice = formatPrice(bundle.totalDiscountedPrice);
        } else {
          bundle.totalDiscountedPrice = bundle.products.reduce((partialSum, a) => partialSum + a.discountAmount, 0);
          bundle.formattedTotalDiscountedPrice = formatPrice(bundle.totalDiscountedPrice);
        }
        if (bundle.discountType === 'PERCENTAGE') {
          bundle.formattedDiscount = (Number.isInteger(parseFloat(bundle.discountValue)) ? parseInt(bundle.discountValue) : parseFloat(bundle.discountValue).toFixed(2)) + '%';
        } else if (bundle.discountType === 'FIXED_AMOUNT') {
          bundle.formattedDiscount = formatPrice(bundle.discountValue * 100);
        }

        if (bundle?.bundleType === "CLASSIC") {
          bundle['isCheckoutButtonDisabledByDefault'] = false;
          bundle['isBundleTypeClassic'] = true
        } else {
          bundle['isBundleTypeClassic'] = false
        }

        if (bundle?.bundleType === 'MIX_AND_MATCH') {
          bundle['isCheckoutButtonDisabledByDefault'] = false;
          bundle['isBundleTypeMixAndMatch'] = true
          if (bundle?.maximumNumberOfItems) {
            bundle['isCheckoutButtonDisabledByDefault'] = true;
          }
        } else {
          bundle['isBundleTypeMixAndMatch'] = false
        }
      }

      function getVariantTitle(item) {
        item.bundleVariantTitle = `${item?.productData?.title}${
          item?.productData?.variants?.length > 1 ? ` - ${item?.variantData?.title}` : ''
        }`;
      }

      function getVariantImage(item) {
            item.variantImage = item?.variantData?.featured_image?.src || item?.productData?.featured_image || item?.imageSrc;
      }

      function getPrice(item, bundle) {
        item.hasDiscount = bundle?.discountValue ? true : false;
        if (bundle?.bundleLevel === 'VARIANT') {
          item.originalPrice = item?.variantData?.price;
          item.formattedOriginalPrice = formatPrice(item?.originalPrice);
          if (bundle.discountType === 'PERCENTAGE') {
            item.discountAmount = item?.variantData?.price * (bundle?.discountValue / 100);
            item.discountedPrice = item?.variantData?.price - item?.discountAmount;
            item.formattedPrice = formatPrice(item?.discountedPrice);
          } else if (bundle?.discountType === 'FIXED_AMOUNT') {
            var totalItems = bundle?.variants?.length;
            var discountPerItem = bundle?.discountValue / totalItems;
            item.discountAmount = discountPerItem;
            item.discountedPrice = item?.variantData?.price - item.discountAmount;
            item.formattedPrice = formatPrice(item?.discountedPrice);
          }
        } else {
          item.originalPrice = item?.productData?.price;
          item.hasMultipleVariant = item?.productData?.variants?.length > 1 ? true : false;
          item.formattedOriginalPrice = formatPrice(item?.originalPrice);
          if (bundle?.discountType === 'PERCENTAGE') {
            item.discountAmount = item?.productData?.price * (bundle?.discountValue / 100);
            item.discountedPrice = item?.productData?.price - item?.discountAmount;
            item.formattedPrice = formatPrice(item?.discountedPrice);
          } else if (bundle?.discountType === 'FIXED_AMOUNT') {
            var totalItems = bundle?.products?.length;
            var discountPerItem = bundle?.discountValue / totalItems;
            item.discountAmount = discountPerItem;
            item.discountedPrice = item?.productData?.price - item.discountAmount;
            item.formattedPrice = formatPrice(item?.discountedPrice);
          }
        }
      }

      function getVariantById(productData, variantId) {
        if (productData?.variants) {
          return productData.variants.filter(item => item?.id === variantId).pop();
        }
      }

      function attachProductDataToBundles(variantLevelBundles, productLevelBundles) {
        variantLevelBundles.forEach(bundle => {
          // var variants = filterCurrentVariant(bundle.variants)
          bundle.variants.forEach(variant => {
            variant.productData = window['products'][variant.productHandle];
            variant.variantData = getVariantById(variant.productData, variant.id);
            if (variant?.productHandle && variant.variantData) {
              variant.variantData.handle = variant.productHandle;
            }
          });
        });

        productLevelBundles.forEach(bundle => {
          // var products = filterCurrentProduct(bundle.products)
          bundle.products.forEach(product => {
            product.productData = window['products'][product.productHandle];
            var currentVariantId = getCurrentVariant();
            var variants = [];
            product?.productData?.variants?.forEach(variant => {
              if (variant.id === parseInt(currentVariantId)) {
                variants.unshift(variant);
              } else {
                variants.push(variant);
              }
            });
            if (product?.productData?.variants) {
              product.productData.variants = variants;
            }
          });
        });
        return { variantLevelBundles, productLevelBundles };
      }

      function getProductHandles(variantLevelBundles, productLevelBundles, callback) {
        var productHandles = [];
        var variantsToFetch = variantLevelBundles.forEach(bundle => {
          var variants = filterCurrentVariant(bundle.variants);
          variants.forEach(variant => productHandles.push(variant?.productHandle));
        });
        productLevelBundles.forEach(bundle => {
          var products = filterCurrentProduct(bundle.products);
          products.forEach(product => productHandles.push(product?.productHandle));
        });
        productHandles = productHandles.filter(function(item, pos) {
          return productHandles.indexOf(item) == pos;
        });
        getAllProductData(productHandles, callback);
      }

      function getAllProductData(productHandles, callback) {
        var currentProductHandle = productHandles.shift();
        if (currentProductHandle) {
        if (!window?.['products']?.[currentProductHandle]) {
          fetch(`/products/${currentProductHandle}.js`)
            .then(res => {
              if (res.ok) {
                return res.json();
              }
            })
            .then(res => {
              if (!window['products']) {
                window['products'] = {};
              }
              window['products'][currentProductHandle] = res;
              if (productHandles.length) {
                getAllProductData(productHandles, callback);
              } else {
                callback();
              }
            });
        } else {
          if (productHandles.length) {
            getAllProductData(productHandles, callback);
          } else {
            callback();
          }
        }
        } else {
            if(productHandles.length) {
              getAllProductData(productHandles, callback)
            } else {
              callback();
            }
        }
      }

      function createVariantBundleHTML(variantLevelBundles, currentVariantData, currentProductData) {
        var bundleTemplate = `<div class="as-bundle-wrapper" data-bundle-id={{bundle.id}}>
                  <div class="as-bundle-detail-wrapper">
                     <div class="as-bundle-title">{{{bundle.title}}}</div>
                     <div class="as-bundle-description">{{{bundle.description}}}</div>
                  </div>
                  <div class="as-bundle-product-wrapper">
                  <div class="as-current-product" data-product-handle="{{{currentProductData.handle}}}">
                    <div class="as-checkmark-round {{#bundle.isBundleTypeClassic}}as-pointer-none{{/bundle.isBundleTypeClassic}}">
                      <input type="checkbox" id="{{currentVariantData.id}}{{bundle.id}}" {{#currentVariantData.isChecked}}checked{{/currentVariantData.isChecked}} />
                      <label for="{{currentVariantData.id}}{{bundle.id}}"></label>
                    </div>
                    <a href="/products/{{{currentProductData.handle}}}?variant={{currentVariantData.id}}" target="_blank"><img src="{{currentProductData.featured_image}}" class="as-current-product_image"></a>
                    <div class="as-bundle-product_details">
                      <a class="as-current-product_name" href="/products/{{{currentProductData.handle}}}?variant={{currentVariantData.id}}" target="_blank">{{currentVariantData.bundleVariantTitle}}</a>
                      <div class="as-current-product_price">
                          <span class="as-sale-price">{{{currentVariantData.formattedPrice}}}</span>{{#bundle.showCompareAtPrice}}&nbsp;<span class="as-original-price">{{{currentVariantData.formattedOriginalPrice}}}</span>{{/bundle.showCompareAtPrice}}
                      </div>
                      <div class="as-variant-select-wrapper" style="display: none;">
                        <label class="as-select-label">${RS?.Config?.bundle?.variant || 'Variant'}</label>
                        <select class="as-variant-select as-bundle-variant">
                          <option value="{{currentVariantData.id}}">{{currentVariantData.bundleVariantTitle}}</option>
                        <select>
                      </div>
                      <div class="as-select-wrapper">
                        <div class="as-quantity-select-wrapper">
                          <label class="as-select-label">${RS?.Config?.bundle?.quantity || 'Quantity'}</label>
                          <select class="as-quantity-select as-bundle-quantity">
                          {{#quantityList}}
                            <option value="{{.}}">{{.}}</option>
                          {{/quantityList}}
                          <select>
                        </div>
                        <div class="as-sellingPlan-select-wrapper">
                        </div>
                      </div>
                    </div>
                  </div>
                  {{#variants}}
                  <div class="as-bundle-product" data-product-handle="{{{productHandle}}}">
                    <div class="as-checkmark-round {{#bundle.isBundleTypeClassic}}as-pointer-none{{/bundle.isBundleTypeClassic}}">
                      <input type="checkbox" id="{{id}}{{bundle.id}}" {{#isChecked}}checked{{/isChecked}} />
                      <label for="{{id}}{{bundle.id}}"></label>
                    </div>
                    <a href="/products/{{{productHandle}}}?variant={{id}}"><img src="{{variantImage}}" target="_blank" class="as-bundle-product_image"></a>
                    <div class="as-bundle-product_details">
                      <a class="as-bundle-product_name" href="/products/{{{productHandle}}}?variant={{id}}" target="_blank">{{bundleVariantTitle}}</a>
                      <div class="as-bundle-product_price">
                         <span class="as-sale-price">{{{formattedPrice}}}</span>{{#bundle.showCompareAtPrice}}&nbsp;<span class="as-original-price">{{{formattedOriginalPrice}}}</span>{{/bundle.showCompareAtPrice}}
                      </div>
                      <div class="as-variant-select-wrapper" style="display: none;">
                        <label class="as-select-label">${RS?.Config?.bundle?.variant || 'Variant'}</label>
                        <select class="as-variant-select as-bundle-variant">
                          <option value="{{id}}">{{bundleVariantTitle}}</option>
                        <select>
                      </div>
                      <div class="as-select-wrapper">
                        <div class="as-quantity-select-wrapper">
                          <label class="as-select-label">${RS?.Config?.bundle?.quantity || 'Quantity'}</label>
                          <select class="as-quantity-select as-bundle-quantity">
                          {{#quantityList}}
                            <option value="{{.}}">{{.}}</option>
                          {{/quantityList}}
                          <select>
                        </div>
                        <div class="as-sellingPlan-select-wrapper">
                        </div>
                      </div>
                    </div>
                  </div>
                  {{/variants}}
                  </div>
                  <div class="as-bundle-checkout-wrapper">
                  <div class="as-sellingPlan-select-wrapper-checkout" style="display: none;">
                    <label class="as-sellingPlan-select-label as-select-label">${RS?.Config?.bundle?.deliveryFrequency || 'Delivery Fequency'}</label>
                    <select class="as-sellingPlan-select-checkout">
                      <option value="">${RS.Config.oneTimePurchaseText || 'Select Delivery Option'}</option>
                    </select>
                  </div>
                    <div class="as-bundle-primary-button as-bundle-cart {{#bundle.isCheckoutButtonDisabledByDefault}}as-button-disabled{{/bundle.isCheckoutButtonDisabledByDefault}}">
                      <span class="as-bundle-primary-button-text">{{{bundle.actionButtonText}}}</span>
                      <div class="as-bundle-primary-button-loader"><div class='appstle_loader'></div></div>
                    </div>
                    <div class="as-bundle-checkout-description">{{{bundle.actionButtonDescription}}}</div>
                  </div>
               </div>`;
        variantLevelBundles.forEach(bundle => {
          var currentVariant = bundle.variants.find(variant => variant.id === parseInt(getCurrentVariant()));
          if (currentVariant?.variantData?.available) {
          var data = {
            variants: filterVariantByAvailability(filterVariants(bundle.variants)),
            currentVariantData: currentVariant,
            currentProductData: currentProductData,
            bundle: jQuery.extend(bundle, {showCompareAtPrice: Boolean(bundle.discountValue)}),
            quantityList: Array.from({length: 100}, (_, i) => i + 1)
          };
          var template = Mustache.render(bundleTemplate, data);
          bundle.template = template;
          }
          // insertBundleToDom(template)
        });
        return variantLevelBundles;
      }

      function createProductBundleHTML(productLevelBundles, productData) {
        var bundleTemplate = `<div class="as-bundle-wrapper" data-bundle-id={{bundle.id}}>
                  <div class="as-bundle-detail-wrapper">
                     <div class="as-bundle-title">{{{bundle.title}}}</div>
                     <div class="as-bundle-description">{{{bundle.description}}}</div>
                  </div>
                  <div class="as-bundle-product-wrapper">
                  <div class="as-current-product" data-product-handle="{{{currentProductData.productData.handle}}}">
                    <div class="as-checkmark-round {{#bundle.isBundleTypeClassic}}as-pointer-none{{/bundle.isBundleTypeClassic}}">
                      <input type="checkbox" id="{{currentProductData.productData.id}}{{bundle.id}}"  {{#currentProductData.isChecked}}checked{{/currentProductData.isChecked}} />
                      <label for="{{currentProductData.productData.id}}{{bundle.id}}"></label>
                    </div>
                    <a href="/products/{{{currentProductData.productData.handle}}}" target="_blank"><img src="{{currentProductData.productData.featured_image}}" class="as-bundle-product_image"></a>
                    <div class="as-bundle-product_details">
                      <a class="as-bundle-product_name" href="/products/{{{currentProductData.productData.handle}}}" target="_blank">{{currentProductData.title}}</a>
                      <div class="as-bundle-product_price">
                          <span class="as-sale-price">{{{currentProductData.formattedPrice}}}</span>{{#bundle.showCompareAtPrice}}&nbsp;<span class="as-original-price">{{{currentProductData.formattedOriginalPrice}}}</span>{{/bundle.showCompareAtPrice}}
                      </div>
                      <div class="as-variant-select-wrapper" {{^currentProductData.hasMultipleVariant}}style="display: none;"{{/currentProductData.hasMultipleVariant}}>
                        <label class="as-select-label">${RS?.Config?.bundle?.variant || 'Variant'}</label>
                        <select class="as-variant-select as-bundle-variant">
                        {{#currentProductData.productData.variants}}
                          <option value="{{id}}">{{title}}</option>
                        {{/currentProductData.productData.variants}}
                        <select>
                      </div>
                      <div class="as-select-wrapper">
                        <div class="as-quantity-select-wrapper">
                          <label class="as-select-label">${RS?.Config?.bundle?.quantity || 'Quantity'}</label>
                          <select class="as-quantity-select as-bundle-quantity">
                          {{#quantityList}}
                            <option value="{{.}}">{{.}}</option>
                          {{/quantityList}}
                          <select>
                        </div>
                        <div class="as-sellingPlan-select-wrapper">
                        </div>
                      </div>
                    </div>
                  </div>
                  {{#products}}
                  <div class="as-bundle-product" data-product-handle="{{{productData.handle}}}">
                          <div class="as-checkmark-round {{#bundle.isBundleTypeClassic}}as-pointer-none{{/bundle.isBundleTypeClassic}}">
                            <input type="checkbox" id="{{{productData.handle}}}{{bundle.id}}"  {{#isChecked}}checked{{/isChecked}} />
                            <label for="{{{productData.handle}}}{{bundle.id}}"></label>
                          </div>
                          <a href="/products/{{{productData.handle}}}" target="_blank"><img src="{{productData.featured_image}}" class="as-bundle-product_image"></a>
                          <div class="as-bundle-product_details">
                              <a class="as-bundle-product_name" href="/products/{{{productData.handle}}}" target="_blank">{{title}}</a>
                              <div class="as-bundle-product_price">
                                  <span class="as-sale-price">{{{formattedPrice}}}</span>{{#bundle.showCompareAtPrice}}&nbsp;<span class="as-original-price">{{{formattedOriginalPrice}}}</span>{{/bundle.showCompareAtPrice}}
                              </div>
                              <div class="as-variant-select-wrapper" {{^hasMultipleVariant}}style="display: none;"{{/hasMultipleVariant}}>
                                <label class="as-select-label">${RS?.Config?.bundle?.variant || 'Variant'}</label>
                                <select class="as-variant-select as-bundle-variant">
                                  {{#productData.variants}}
                                  <option value="{{id}}">{{title}}</option>
                                  {{/productData.variants}}
                                <select>
                              </div>
                              <div class="as-select-wrapper">
                                <div class="as-quantity-select-wrapper">
                                  <label class="as-select-label">${RS?.Config?.bundle?.quantity || 'Quantity'}</label>
                                  <select class="as-quantity-select as-bundle-quantity">
                                    {{#quantityList}}
                                      <option value="{{.}}">{{.}}</option>
                                    {{/quantityList}}
                                  <select>
                                </div>
                                <div class="as-sellingPlan-select-wrapper">

                                </div>
                              </div>
                          </div>
                  </div>
                  {{/products}}
                  </div>
                  <div class="as-bundle-checkout-wrapper">
                  <div class="as-sellingPlan-select-wrapper-checkout" style="display: none;">
                    <label class="as-sellingPlan-select-label as-select-label">${RS?.Config?.bundle?.deliveryFrequency || 'Delivery Fequency'}</label>
                    <select class="as-sellingPlan-select-checkout">
                      <option value="">${RS.Config.oneTimePurchaseText || 'Select Delivery Option'}</option>
                    </select>
                  </div>
                    <div class="as-bundle-primary-button as-bundle-cart {{#bundle.isCheckoutButtonDisabledByDefault}}as-button-disabled{{/bundle.isCheckoutButtonDisabledByDefault}}">
                      <span class="as-bundle-primary-button-text">{{{bundle.actionButtonText}}}</span>
                      <div class="as-bundle-primary-button-loader"><div class='appstle_loader'></div></div>
                    </div>
                    <div class="as-bundle-checkout-description">{{{bundle.actionButtonDescription}}}</div>
                  </div>
              </div>`;
        productLevelBundles.forEach(bundle => {
          var currentProduct = bundle.products.find(product => product.id === RSConfig.product.id)
          if (currentProduct?.productData?.available) {
         var data = {
            products: filterProductByAvailability(filterCurrentProduct(bundle.products)),
            currentProductData: currentProduct,
            bundle: jQuery.extend(bundle, {showCompareAtPrice: Boolean(bundle.discountValue)}),
            quantityList: Array.from({length: 100}, (_, i) => i + 1)
          };
          var template = Mustache.render(bundleTemplate, data);
          bundle.template = template;
          // insertBundleToDom(template)
          }
        });
        return productLevelBundles;
            
      }

      function filterProductByAvailability(productLevelBundles) {
        return productLevelBundles.filter(item => item?.productData?.available);
      }

      function filterVariantByAvailability(variantLevelBundles) {
        return variantLevelBundles.filter(item => item?.variantData?.available);
      }

      function insertBundleToDom(template) {
        RSConfig.bundle.selector = RSConfig?.bundle?.selector || '.product-form';
        if (RSConfig?.bundle?.placement === 'BEFORE') {
          jQuery(template).insertBefore(RSConfig.bundle.selector);
        } else if (RSConfig?.bundle?.placement === 'AFTER') {
          jQuery(template).insertAfter(RSConfig.bundle.selector);
        } else if (RSConfig?.bundle?.placement === 'FIRST_CHILD') {
          jQuery(template).prependTo(RSConfig.bundle.selector);
        } else if (RSConfig?.bundle?.placement === 'LAST_CHILD') {
          jQuery(template).appendTo(RSConfig.bundle.selector);
        }
      }

      function attatchEventHandlerToBundleCheckoutButton() {
        jQuery('body').on('click', '.as-bundle-cart', function() {
          setBundleInProgress(this);
          jQuery('.as-bundle-popup-wrapper').remove();
          var variantIds = getCurrentVariantIds(this);
          addBundleToCart(this, variantIds);
        });
      }

      function attatchEventHandlerToBundleCheckoutSellingPlanSelect() {
        jQuery('body').on('change', '.as-sellingPlan-select-checkout', function(event) {
          var instance = this;
          $(instance).parents('.as-bundle-wrapper').find('.as-current-product, .as-bundle-product').each((index, item) => {
            var productItem = jQuery(item);
            if (productItem.find(`.as-sellingPlan-select-wrapper option[value="${event.target.value}"]`).length) {
              productItem.find('.as-sellingPlan-select').val(event.target.value)
            }
          })
        })
      }

      function attatchEventHandlerToBundlePopupButton() {
        jQuery('body').on('click', '.as-bundle-checkout', function() {
          setBundleInProgress(this);
          getDiscountCodeAndCheckout(this);
        });
      }

      function attatchEventHandlerToBundleCancelButton() {
        jQuery('body').on('click', '.as-bundle-cancel', function() {
          jQuery(this)
            .parents('.as-bundle-popup-wrapper')
            .remove();
        });
      }

      function attachEventHandlerToItemCheckbox() {
        jQuery('body').on('change', '.as-checkmark-round input[type=checkbox]', function(event) {
          var instance = this;
          var bundle = getCurrentBundle(instance);
          var isBundleValid = checkBundleValidity(instance, bundle);
          var checkoutButton = getBundleCheckoutButton(instance);
          var numberOfItemsSelected = getCurrentNumberOfItemsSelected(instance);

          if (isBundleValid) {
            checkoutButton.removeClass('as-button-disabled');
          } else {
            checkoutButton.addClass('as-button-disabled');
          }


          if (bundle?.bundleType === 'MIX_AND_MATCH') {
            var uncheckedItems = getUncheckedItems(instance);
            if (bundle?.maximumNumberOfItems) {
                if ((numberOfItemsSelected >= bundle?.maximumNumberOfItems)) {
                  uncheckedItems.each((index, item) => {
                    jQuery(item).parents('.as-current-product, .as-bundle-product').addClass('as-item-disabled')
                  })
              } else {
                uncheckedItems.each((index, item) => {
                  jQuery(item).parents('.as-current-product, .as-bundle-product').removeClass('as-item-disabled')
                })
              }
            }
          }

        })
      }

      function getUncheckedItems(instance) {
        return jQuery(instance).parents('.as-bundle-wrapper').find('.as-checkmark-round input[type=checkbox]:not(:checked)');
      }

      function getBundleCheckoutButton(instance) {
        return jQuery(instance)
        .parents('.as-bundle-wrapper')
        .find('.as-bundle-cart')
      }

      function checkBundleValidity(instance, bundle) {
        var isValid = true;
        var numberOfItemsSelected = getCurrentNumberOfItemsSelected(instance);

        if (!numberOfItemsSelected) {
          isValid = false;
        }

        if (bundle?.bundleType === 'MIX_AND_MATCH') {

          if (bundle?.minimumNumberOfItems) {
            if ((numberOfItemsSelected < bundle?.minimumNumberOfItems)) {
              isValid = false;
            }
          }

          if (bundle?.maximumNumberOfItems) {
            if ((numberOfItemsSelected > bundle?.maximumNumberOfItems)) {
              isValid = false;
            }
          }

        }
        return isValid;
      }

      function getCurrentVariantIds(instance) {
        var variants = [];
        var bundle = getCurrentBundle(instance);
        if (bundle?.bundleType === "MIX_AND_MATCH") {
          if (!checkBundleValidity(instance, bundle)){
            return
          }
        }
        jQuery(instance)
          .parents('.as-bundle-wrapper')
          .find('.as-bundle-variant')
          .each((index, item) => {
            if (bundle?.bundleType === 'MIX_AND_MATCH' ? (jQuery(item).parents('.as-bundle-product, .as-current-product').find('.as-checkmark-round input[type=checkbox]').is(":checked")) : true) {
              variants.push({
                id: jQuery(item).val(),
                quantity:  jQuery(item)
                .parents('.as-bundle-product, .as-current-product')
                ?.find('.as-quantity-select')
                ?.val() || 1,
                selling_plan:
                  jQuery(item)
                    .parents('.as-bundle-product, .as-current-product')
                    ?.find('.as-sellingPlan-select')
                    ?.val() || ''
              });
            }

          });
        return variants;
      }

      function getCurrentNumberOfItemsSelected(instance) {
        return jQuery(instance)
        .parents('.as-bundle-wrapper')
        .find('.as-checkmark-round input[type=checkbox]:checked').length
      }

      function setBundleInProgress(instance) {
        jQuery(instance)
          .parents('.as-bundle-wrapper,.as-bundle-popup-wrapper')
          .addClass('as-bundle-loading');
      }

      function removeBundleInProgress(instance) {
        jQuery(instance)
          .parents('.as-bundle-wrapper,.as-bundle-popup-wrapper')
          .removeClass('as-bundle-loading');
      }

      function addBundleToCart(instance, variants) {
        fetch(window.Shopify.routes.root + 'cart/add.js', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ items: variants })
        })
        .then(function(res){
              if (res.ok) {
              return getDiscountCodeAndCheckout(instance);
            } else {
              removeBundleInProgress(instance);
            }

        })
        .catch(function(err){
              console.log(err)
                removeBundleInProgress(instance);
        })
      }

      function getDiscountCodeAndCheckout(instance) {
        // jQuery
        //   .get('/cart.js')
        //   .done(function(cartData) {
        //     sendCartDataAndGetDiscount(cartData, instance);
        //   })
        //   .fail(function() {
        //     removeBundleInProgress(instance);
        //   });

      fetch(window.Shopify.routes.root + 'cart.js', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
        })
        .then(function(res){
            if (res.ok) {
              return res.json();
            } else {
              removeBundleInProgress(instance);
            }

        })
        .then(function(cartData){
            return sendCartDataAndGetDiscount(cartData, instance);
        })
        .catch(function(err) {
            console.log(err);
            removeBundleInProgress(instance);
        })
      }

      function sendCartDataAndGetDiscount(cartData, instance) {
        // jQuery.ajax({
        //   url: '/apps/subscriptions/cart-discount',
        //   type: 'POST',
        //   headers: {
        //     'content-type': 'application/json'
        //   },
        //   data: JSON.parse(cartData),
        //   success: function(discountData) {
        //     if (Boolean(discountData?.discountNeeded)) {
        //       window.location.href = `/checkout?discount=${discountData?.discountCode}`;
        //     } else {
        //       window.location.href = `/checkout`;
        //     }
        //     removeBundleInProgress(instance);
        //   },
        //   fail: function() {
        //     removeBundleInProgress(instance);
        //   }
        // });

        fetch(`/${RSConfig.appstle_app_proxy_path_prefix}/cart-discount`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(cartData)
        })
        .then(function(res){
              if (res.ok) {
              return res.json();
            } else {
              removeBundleInProgress(instance);
            }
        })
        .then(function(discountData){
          var redirectURL = '/checkout';
          if (RSConfig?.bundle?.redirectTo === 'CART') {
            if (Boolean(discountData?.discountNeeded)) {
              redirectURL = `/discount/${discountData?.discountCode}?redirect=/cart`;
            } else {
              redirectURL = `/cart`;
            }
          }
          if (Boolean(discountData?.discountNeeded)) {
              window.location.href = `${redirectURL}?discount=${discountData?.discountCode}`;
            } else {
              window.location.href = `${redirectURL}`;
            }
            removeBundleInProgress(instance);
        })
        .catch(function(err){
              console.log(err)
              removeBundleInProgress(instance);
        })


      }

      function getCartData() {
        jQuery('.as-bundle-popup-wrapper').remove();
        fetch('/cart.js')
          .then(res => {
            if (res.ok) {
              return res.json();
            }
          })
          .then(res => {
            matchBundlesWithCartData(res);
          });
      }

      function matchBundlesWithCartData(cartData) {
        var bundleData = parseBundleData();
        var matchedBundle = [];
        cartProductIds = cartData.items.map(lineItem => lineItem?.product_id);
        cartVariantIds = cartData.items.map(lineItem => lineItem?.variant_id);
        bundleData = sortBundles(bundleData, 'sequenceNo');
        bundleData.forEach(bundle => {
          if (bundle?.variants?.length) {
            var bundleVariantids = bundle.variants.map(variant => variant.id);
            if (bundleVariantids.every(val => cartVariantIds.includes(val))) {
              matchedBundle.push(bundle);
            }
          } else if (bundle?.products?.length) {
            var bundleProductids = bundle.products.map(products => products.id);
            if (bundleProductids.every(val => cartProductIds.includes(val))) {
              matchedBundle.push(bundle);
            }
          }
        });
        processMatchedCartBundle(matchedBundle, cartData);
      }

      function processMatchedCartBundle(matchedBundle, cartData) {
        var productHandles = [];
        var variantLevelBundles = [];
        var productLevelBundles = [];
        matchedBundle.forEach(bundle => {
          if (bundle?.variants?.length) {
            variantLevelBundles.push(bundle);
          } else if (bundle?.products?.length) {
            productLevelBundles.push(bundle);
          }
        });
        getProductHandles(variantLevelBundles, productLevelBundles, () =>
          createCartPopupData(variantLevelBundles, productLevelBundles, cartData)
        );
      }

      function createCartPopupData(variantLevelBundles, productLevelBundles, cartData) {
        var { variantLevelBundles, productLevelBundles } = processBundleItems(variantLevelBundles, productLevelBundles);
        buildSlidePopupView(sortBundles(mergeBundles(variantLevelBundles, productLevelBundles), 'sequenceNo'), cartData);
      }
          
      function mergeBundles(variantLevelBundles, productLevelBundles) {
        return jQuery.merge(jQuery.merge([], variantLevelBundles), productLevelBundles);
      }

      function buildSlidePopupView(bundles, cartData) {
        jQuery('.appstle-bundle-subtotal').remove();
        jQuery('.appstle-bundle-line-through').removeClass('appstle-bundle-line-through');
        if (bundles.length && bundles[0]?.discountValue) {
          var bundleTemplate = `<div class="as-bundle-popup-wrapper custom-popup" id="popup" style="display: block;">
                      <div class="icon-cross as-bundle-cancel">
                          X
                      </div>
                      <div class="popup-body">
                          <div class="alert">${RS?.Config?.bundle?.discountPopupHeader || 'You got'}</div>
                          <div class="message">{{{formattedDiscount}}} ${RS?.Config?.bundle?.discountPopupAmount || 'OFF your order!'}</div>
                          <div class="discount">${RS?.Config?.bundle?.discountPopupCheckoutMessage ||
                            'Apply discount and go to checkout?'}</div>
                          <div class="buttons">
                              <button class="as-bundle-primary-button as-bundle-checkout" id="yes">
                                <span class="as-bundle-primary-button-text">${RS?.Config?.bundle?.discountPopupBuy ||
                                  '{{{actionButtonText}}}'}</span>
                                <div class="as-bundle-primary-button-loader"><div class='appstle_loader'></div></div>
                              </button>
                              <button class="as-bundle-secondary-button as-bundle-cancel">${RS?.Config?.bundle?.discountPopupNo ||
                                'No'}</button>
                          </div>
                      </div>
                  </div>`;
          var template = Mustache.render(bundleTemplate, bundles[0]);
          // bundle.popupSlideTemplate = template;
          if (!jQuery('.as-bundle-loading').length && RSConfig?.bundle?.showDiscountPopup) {
            jQuery('body').append(template);
          }
          if (RSConfig?.bundle?.showDiscountInCart === 'true') {
            var newCartSubTotal = cartData.items_subtotal_price - bundles[0].totalDiscountedPrice;
            var cartSubTotalElement = jQuery(RSConfig.selectors.cartSubTotalSelector || '.totals__subtotal-value');
            var clonedSubTotalElement = cartSubTotalElement.clone();
            clonedSubTotalElement.addClass('appstle-bundle-subtotal');
            clonedSubTotalElement.html(`${formatPrice(newCartSubTotal)} ${cartData?.currency}`);
            cartSubTotalElement.addClass('appstle-bundle-line-through');
            cartSubTotalElement.before(clonedSubTotalElement);
          }
        }
      }

      function getBundleDataById(id) {
        var bundleRules = parseBundleData();
        return bundleRules.filter(bundle => bundle?.id === parseInt(id)).pop();
      }

      function attatchChangeHandlerToVariant() {
        jQuery('body').on('change', '.as-variant-select', function(event) {
          var productData =
            window.products[
              jQuery(this)
                .parents('.as-bundle-product, .as-current-product')
                .data('product-handle')
            ];
          var variantData = getVariantById(productData, parseInt(event.target.value));
          populateSellingPlanDropdownOnCart(productData, variantData, this);
        });
      }

      function attatchChangeHandlerToSellingPlan() {
        jQuery('body').on('change', '.as-sellingPlan-select', function(event) {
          var bundleCardElement = jQuery(this).parents('.as-bundle-product, .as-current-product');
          var productData = window.products[bundleCardElement.data('product-handle')];
          var variantId = parseInt(bundleCardElement.find('.as-variant-select').val());
          var variantData = getVariantById(productData, variantId);
          modifyPricesBasedOnSellingPlan(productData, variantData, variantId, parseInt(event.target.value), this);
        });
      }

      function getBundleId(instance) {
        return parseInt(
          jQuery(instance)
            .parents('.as-bundle-wrapper')
            .data('bundle-id')
        );
      }

      function getCurrentBundle(instance) {
        var bundleId = getBundleId(instance)
        return getBundleDataById(bundleId);
      }

      function modifyPricesBasedOnSellingPlan(productData, variantData, variantId, sellingPlanId, instance) {
        var price = 0;
        var per_delivery_price;
        var compare_at_price;
        var bundleCardElement = jQuery(instance).parents('.as-bundle-product, .as-current-product');
        var bundle = getCurrentBundle(instance);
        bundleCardElement.find('.as-perDelivery-price').remove();
        if (sellingPlanId) {
          var sellingPlanPriceDetails = variantData?.selling_plan_allocations
            ?.filter(sellingPlan => sellingPlan?.selling_plan_id === sellingPlanId)
            .pop();
          price = sellingPlanPriceDetails?.price;
          per_delivery_price = sellingPlanPriceDetails?.per_delivery_price;
          compare_at_price = sellingPlanPriceDetails?.compare_at_price;
          var discountedPerDeilveryPrice = getBundleProductPriceWithSellingPlan(per_delivery_price, bundle);
          if (per_delivery_price !== price) {
            var formattedPerDeliveryPrice = formatPrice(discountedPerDeilveryPrice);
            jQuery(
              `<span class="as-perDelivery-price">${formattedPerDeliveryPrice}/${RS?.Config?.bundle?.perDelivery || 'delivery'}</span>`
            ).insertAfter(bundleCardElement.find('.as-original-price'));
          }
        } else {
          price = variantData.price;
          compare_at_price = variantData?.compare_at_price;
        }

        if (variantData?.price > compare_at_price) {
              compare_at_price = variantData?.price;
        }

        var discountedPrice = getBundleProductPriceWithSellingPlan(price, bundle);

        if (discountedPrice || discountedPrice === 0) {
          var formattedDiscountedPrice = formatPrice(discountedPrice);
          bundleCardElement.find('.as-sale-price').html(formattedDiscountedPrice);
        }

        if (compare_at_price || compare_at_price === 0) {
          var formattedCompareAtPrice = formatPrice(compare_at_price);
          bundleCardElement.find('.as-original-price').html(formattedCompareAtPrice);
        }
      }

      function getBundleProductPriceWithSellingPlan(price, bundle) {
        var discountAmount;
        var discountedPrice;
        if (bundle.bundleLevel === 'VARIANT') {
          if (bundle.discountType === 'PERCENTAGE') {
            discountAmount = price * (bundle?.discountValue / 100);
            discountedPrice = price - discountAmount;
          } else if (bundle.discountType === 'FIXED_AMOUNT') {
            var totalItems = bundle?.variants?.length;
            var discountPerItem = bundle?.discountValue / totalItems;
            discountAmount = discountPerItem;
            discountedPrice = price - discountAmount;
          }
        } else {
          if (bundle.discountType === 'PERCENTAGE') {
            discountAmount = price * (bundle?.discountValue / 100);
            discountedPrice = price - discountAmount;
          } else if (bundle.discountType === 'FIXED_AMOUNT') {
            var totalItems = bundle?.products?.length;
            var discountPerItem = bundle?.discountValue / totalItems;
            discountAmount = discountPerItem;
            discountedPrice = price - discountAmount;
          }
        }

        return discountedPrice;
      }

      function populateSellingPlanDropdownOnCart(product, variant, instance) {
        var selectLabel = jQuery(
          `<label class="as-sellingPlan-select-label as-select-label">${RS?.Config?.bundle?.deliveryFrequency ||
            'Delivery Fequency'}</label>`
        );
        var selectItem = jQuery(`<select class="as-sellingPlan-select"></select>`);
        var bundle = getCurrentBundle(instance);
        var sellingPlanVariants = [];
        jQuery.each(product?.selling_plan_groups, function(index, sellingPlanGroup) {
          if (sellingPlanGroup.app_id === 'appstle') {
            jQuery.each(sellingPlanGroup.selling_plans, function(subIndex, sellingPlan) {
              var visible = isSellingPlanVisible(sellingPlan.id);
              if (visible) {
                var sellingPlanAllocation = variant?.selling_plan_allocations.find(function(plan) {
                  return plan.selling_plan_id === sellingPlan.id;
                });
                if (!sellingPlanAllocation) {
                  return;
                }
                var price = sellingPlanAllocation.per_delivery_price;
                var totalPrice = formatPrice(sellingPlanAllocation?.price);
                var formattedPrice = formatPrice(price);

                var secondPrice = null;
                var secondFormattedPrice = null;

                if (
                  sellingPlanAllocation &&
                  sellingPlanAllocation.price_adjustments &&
                  sellingPlanAllocation.price_adjustments.length === 2
                ) {
                  secondPrice = sellingPlanAllocation.price_adjustments[1].price;
                  secondFormattedPrice = formatPrice(secondPrice);
                } else {
                  secondPrice = price;
                  secondFormattedPrice = formattedPrice;
                }

                var price_adjustments = sellingPlan.price_adjustments.shift();

                sellingPlanVariants.push({
                  name: sellingPlan.name,
                  id: sellingPlan.id,
                  formattedPrice: formattedPrice,
                  price: price,
                  totalPrice: totalPrice,
                  secondPrice: secondPrice,
                  secondFormattedPrice: secondFormattedPrice,
                  discount: price_adjustments
                    ? price_adjustments.value_type === 'percentage'
                      ? `${price_adjustments.value}%`
                      : formatPrice(price_adjustments.value)
                    : '',
                  deliveryText: sellingPlan.options[0].name
                });

                if (bundle?.showCombinedSellingPlan) {
                  var checkoutSelect = jQuery(instance).parents('.as-bundle-wrapper').find('.as-sellingPlan-select-checkout');
                  checkoutSelect.parents('.as-sellingPlan-select-wrapper-checkout').show();
                  if (variant?.requires_selling_plan) {
                     jQuery(checkoutSelect).find('option[value=""]').remove();
                  }
                  if (!checkoutSelect.find(`option[value=${sellingPlan.id}]`).length) {
                    var isSelected = bundle?.selectSubscriptionByDefault ? (checkoutSelect.find(`option`).length === (variant?.requires_selling_plan ? 0 : 1)) : false;
                    checkoutSelect.append(`<option value="${sellingPlan?.id}" ${isSelected ? 'selected' : ''}>${sellingPlan?.name}</option>`)
                  }
                }
              }
            });
          }
        });
        if (sellingPlanVariants.length > 0) {
          sellingPlanVariants.sort(function(sellingPlanA, sellingPlanB) {
            return sellingPlanA.price - sellingPlanB.price;
          });

          if (!variant?.['requires_selling_plan']) {
            jQuery('<option />', {
              html: RS.Config.oneTimePurchaseText || 'Select Delivery Option',
              value: ''
            }).appendTo(selectItem);
          }

          jQuery(sellingPlanVariants).each(function(index, sellingPlan) {
            var sellingPlanDisplayText = sellingPlan?.name; // buildSellingPlantText(sellingPlan);

            jQuery('<option />', {
              value: sellingPlan.id,
              html: sellingPlanDisplayText,
              selected: bundle?.selectSubscriptionByDefault ? (index === 0 ? true : false) : false
            }).appendTo(selectItem);
          });
          var placeholderElement = jQuery(instance)
            .parents('.as-bundle-product, .as-current-product')
            .find('.as-sellingPlan-select-wrapper');
          placeholderElement.children().remove();
          jQuery(selectLabel).appendTo(placeholderElement);
          jQuery(selectItem).appendTo(placeholderElement);
          selectItem.trigger('change');
          if (bundle?.showCombinedSellingPlan) {
            jQuery('.as-sellingPlan-select-checkout').trigger('change');
            placeholderElement.hide();
          }
        }
      }

      function isSellingPlanVisible(sellingPlanId) {
        var customerId = __st.cid;
        var userTags = RSConfig.customer_tags || [];
        var isVisible = true;

        if (!customerId && RSConfig.memberOnlySellingPlansJson && RSConfig.memberOnlySellingPlansJson[sellingPlanId]) {
          isVisible = false;
        }

        if (customerId && RSConfig.nonMemberOnlySellingPlansJson && RSConfig.nonMemberOnlySellingPlansJson[sellingPlanId]) {
          isVisible = false;
        }

        if (isVisible && customerId && RSConfig.memberOnlySellingPlansJson && RSConfig.memberOnlySellingPlansJson[sellingPlanId]) {
          if (
            RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags &&
            RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.trim()
          ) {
            var sellingPlanTags = RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.split(',');
            var tagFound = compareCustomerTags(userTags, sellingPlanTags);
            isVisible = tagFound.length > 0;
          }
        }

        return isVisible;
      }

      function buildSellingPlantText(sellingPlan) {
        var sellingPlanModel =
          sellingPlan.totalPrice == sellingPlan.formattedPrice
            ? {
                sellingPlanName: sellingPlan.name,
                sellingPlanPrice: `<span class="transcy-money">${sellingPlan.formattedPrice}</span>`,
                secondSellingPlanPrice: `<span class="transcy-money">${sellingPlan.secondFormattedPrice}</span>`,
                discountText: sellingPlan?.discountText,
                totalPrice: `<span class="transcy-money">${sellingPlan?.totalPrice}</span>`
              }
            : {
                sellingPlanName: sellingPlan.name,
                totalPrice: `<span class="transcy-money">${sellingPlan?.totalPrice}</span>`,
                sellingPlanPrice: `<span class="transcy-money">${sellingPlan.formattedPrice}</span>`,
                secondSellingPlanPrice: `<span class="transcy-money">${sellingPlan.secondFormattedPrice}</span>`,
                discountText: sellingPlan.discountText
              };

        var sellingPlanDisplayText = Mustache.render(RS.Config.sellingPlanTitleText, sellingPlanModel);
        return wrapPriceWithSpanTag(sellingPlanDisplayText);
      }

      function wrapPriceWithSpanTag(price) {
        var textArea = document.createElement('textarea');
        textArea.innerHTML = decodeURI(encodeURI(price));
        return textArea.value;
      }

      function sortBundles(bundles, params) {
        bundles.sort(function(a, b) {
          return a[params] - b[params];
        });
        return bundles;
      }

      getCartData();

      function getParamsFromURL(param) {
        const urlParams = new URLSearchParams(window.location.search);
        var globalUrlParameter = urlParams.get(param);
      }

      function getCurrentVariant() {
        return getParamsFromURL('variant') || jQuery('[name=id]').val();
      }

      function renderStandAloneWidget() {
        var standaloneElements = Array.prototype.slice.call(jQuery('.appstle_stand_alone_selector'));
        let index = -1;
        function attatchWidgetToStandAloneElement() {
          if (standaloneElements?.length) {
            let standAloneElement = standaloneElements.shift();
            let product = $(standAloneElement).data('product-data');
            index = index + 1;
            if (
              !jQuery(standAloneElement)
                .parents('form')
                .find('.appstle_sub_widget').length
            ) {
              if (!product) {
                fetch(location.origin + `/products/${jQuery(standAloneElement).data('product-handle')}.js`)
                  .then(response => {
                    if (!response.ok) {
                      throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                  })
                  .then(productHandleData => {
                    jQuery(standAloneElement).attr('data-product-data', productHandleData);
                    renderWidget(productHandleData, $(standAloneElement), index);
                    attatchWidgetToStandAloneElement();
                  })
                  .catch(err => {
                    console.log(err);
                  });
              } else {
                renderWidget(product, $(standAloneElement), index);
                attatchWidgetToStandAloneElement();
              }
            } else {
              attatchWidgetToStandAloneElement();
            }
          }
        }
        attatchWidgetToStandAloneElement();
      }

      function urlIsProductPage() {
        // return null != decodeURIComponent(window.location.pathname).match(/\/products\/(([a-zA-Z0-9]|[\-\.\_\~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[\ud83c\ud83d\ud83e][\ud000-\udfff]){1,})\/?/)
        return decodeURIComponent(window.location.pathname).includes('/products/');
      }

      jQuery('.appstle_sub_widget').remove();
      // Init appstle render widget
      if (appstleStandAloneSelectorExists()) {
        renderStandAloneWidget();
      } else {
        renderWidget(undefined, undefined, 0);
      }

      function wrapPriceWithSpanTag(price) {
        var textArea = document.createElement('textarea');
        textArea.innerHTML = decodeURI(encodeURI(price));
        return textArea.value;
      }

      function formatPrice(price) {
        var configMoneyFormat = RS.Config.moneyFormat;

        var moneyFormat = configMoneyFormat;
        if (configMoneyFormat) {
          moneyFormat = configMoneyFormat?.replace('{% raw %}', '')?.replace('{% endraw %}', '');
        }

        if (typeof price === 'string') {
          price = price.replace('.', '');
        }

        var value = '';
        var placeholderRegex = /\{\{\s*(\w+)\s*\}\}/;
        var shopifyMoneyFormat =
          typeof Shopify !== 'undefined' && Shopify.money_format && Shopify.money_format.length > 1 ? Shopify.money_format : '';
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

        function htmlDecode(input) {
          var doc = new DOMParser().parseFromString(input, 'text/html');
          return doc.documentElement.textContent;
        }

        var formatString = '';

        if (RS.Config?.formatMoneyOverride === 'true') {
          formatString = RS.Config.moneyFormat;
        } else {
          formatString =
            window?.shopifyCurrencyFormat ||
            window?.moneyFormat ||
            window?.Currency?.money_format_no_currency ||
            themeMoneyFormat ||
            RSConfig?.shopMoneyFormat ||
            moneyFormat ||
            shopifyMoneyFormat ||
            htmlDecode(RSConfig.shopMoneyFormatWithCurrencyFormat);
        }

        function formatWithDelimiters(number, precision, thousands, decimal) {
          thousands = thousands || ',';
          decimal = decimal || '.';

          if (isNaN(number) || number === null) {
            return 0;
          }

          number = (number / 100.0).toFixed(precision);

          var parts = number.split('.');
          var dollarsAmount = parts[0].replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1' + thousands);
          var centsAmount = parts[1] ? decimal + parts[1] : '';

          return dollarsAmount + centsAmount;
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
        return wrapPriceWithSpanTag(formatString.replace(placeholderRegex, value));
      }

      function appstleStandAloneSelectorExists() {
        return jQuery('.appstle_stand_alone_selector').length > 0;
      }

      function urlEligibleToIntercept(url) {
        var interceptingUrl = ['/add', '/change'];
        var flag = false;
        for (var i = 0; i < interceptingUrl.length; i++) {
          if (url.indexOf(interceptingUrl[i]) !== -1) {
            flag = true;
            break;
          }
        }
        return flag;
      }

      function listenToCartChanges() {
        var origOpen = window.XMLHttpRequest.prototype.open;
        window.XMLHttpRequest.prototype.open = function() {
          this.addEventListener('load', function() {
            var url = this.responseURL;
            console.log('origOpe.n -> url=' + url);
            if (urlEligibleToIntercept(url)) {
              if (prevChangeResponse !== JSON.stringify(this.response)) {
                if (!jQuery('#appstle_overlay').length) {
                  // addLoader();
                }
                setTimeout(getCartData, 100);
                prevChangeResponse = JSON.stringify(this.response);
              }
            }
          });
          origOpen.apply(this, arguments);
        };
        const originalFetch = window.fetch;
        window.fetch = function() {
          return new Promise((resolve, reject) => {
            originalFetch
              .apply(this, arguments)
              .then(response => {
                var url = response.url;
                if (urlEligibleToIntercept(url)) {
                  if (!jQuery('#appstle_overlay').length) {
                    //  addLoader();
                  }
                  setTimeout(getCartData, 100);
                }

                resolve(response);
              })
              .catch(error => {
                console.error(error);
              });
          });
        };
      }

      function addCss() {
        jQuery('head').append(`<style>
    .as-bundle-wrapper {
      margin: 30px 0;
    }
    
    .as-current-product, .as-bundle-product {
      padding: 16px;
      border-bottom: 1px solid rgb(234,234,234);
      border-left:  1px solid rgb(234,234,234);
      border-right:  1px solid rgb(234,234,234);
      display: flex;
    }
    .as-current-product {
    border-top:  1px solid rgb(234,234,234);
    }
    
    .as-current-product_image, .as-bundle-product_image {
      width: 100px;
      height: 100px;
    }
    
    .as-current-product_name, .as-bundle-product_name, .as-sale-price, .as-original-price {
      font-size: 14px;
      line-height: 18px;
      text-decoration: none;
      color: currentColor;
    }
    .as-sale-price, .as-original-price {
     font-weight: bold;
    }
    
    .as-original-price {
    text-decoration: line-through;
    font-size: 12px;
    color: red;
    }
    
    .as-perDelivery-price {
      font-size: 10px;
      opacity: 0.8;
      color: currentColor;
      line-height: 18px;
      margin-top: -7px !important;
      display: block;
      margin: 0;
    }
    
    
    .as-bundle-product_details {
      margin-left: 12px;
      display: flex;
      flex-direction: column;
      flex-grow: 1;
    }
    
    .as-checkmark {
      position: absolute;
      left: 8px;
      top: 8px;
      height: 18px;
      width: 18px;
    }
    
    .as-current-product, .as-bundle-product {
    position: relative;
    }
    
    .as-bundle-primary-button, .as-bundle-secondary-button  {
      width: 100%;
      background-color: rgb(18, 18, 18);
      color: #fff;
      border: none;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      padding: 16px 20px;
      font-size: 14px;
    }
    
    .custom-popup .as-bundle-primary-button {
      background-color: #fff;
      color: rgb(18, 18, 18);
    }
    .as-bundle-checkout-wrapper {
    margin-top: 16px;
    
    }
    
    .as-bundle-checkout-description {
      font-size: 12px;
      text-align: center;
      margin-top: 6px;
    }
    
    .as-bundle-title {
      font-size: 16px;
      text-align: center;
      font-weight: bold;
      line-height: 1.4;
    margin-bottom: 5px;
    }
    
    .as-bundle-description {
      font-size: 14px;
      line-height: 18px;
      text-align: center;
    }
    .as-bundle-detail-wrapper {
    margin-bottom: 12px;
    }
    
    .as-variant-select, .as-sellingPlan-select, .as-sellingPlan-select-checkout, .as-quantity-select {
      padding: 5px;
      border-radius: 8px;
    }
    
    .as-variant-select-wrapper {
      flex-grow: 1;
      margin-top: auto;
      margin-bottom: 10px;
    }

    .as-variant-select {
      width: 100%;
    }

    .as-quantity-select-wrapper {
      margin-right: 6px;
    }

    .as-quantity-select {
      width: 100%;
    }
    .as-bundle-product_price {
      margin-bottom: 10px;
    }
    
    .as-select-wrapper {
      display: flex;
      margin-top: auto;
      justify-content: space-between;
    }
    
    .as-select-label {
      font-size: 10px;
      text-transform: uppercase;
      display: block;
    }
    
    .as-sellingPlan-select-wrapper, .as-sellingPlan-select-wrapper-checkout {
      flex-shrink: 1;
      width: 100%;
    }
    
    .as-sellingPlan-select-wrapper-checkout {
      margin-bottom: 12px;
    }
    
    .as-sellingPlan-select, .as-sellingPlan-select-checkout {
      width: 100%;
    }
    
    .custom-popup{
      max-width: 300px;
      width: 100%;
      border: 1px solid rgba(33,43,54,1);
      border-radius: 3px;
      padding: 2.5rem 3.5rem;
      background-color: rgba(33,43,54,1);
      font-size: 14px;
      position: fixed;
      bottom: 3.25rem;
      right: 2.25rem;
      animation-name: popup-modal;
      animation-duration: 1s;
      animation-iteration-count: 1;
      display: none;
      color: white;
      z-index: 999999;
    }
    @keyframes popup-modal {
    0%   {background:none; bottom:0px; }
    }
    .custom-popup .icon-cross{
      text-align: end;
    }
    .custom-popup .icon-cross .fa-times{
      padding: .25rem .25rem 0 0;
      font-size: 18px;
    }
    .custom-popup .popup-body{
    
    }
    .custom-popup .popup-body .alert{
      text-align: center;
      font-style: italic;
    font-family: Roboto, Arial, sans-serif;
    margin-bottom: .75rem;
    }
    .custom-popup .popup-body .message{
      text-align: center;
      font-size: 18px;
      font-weight: bold;
     font-family: Roboto, Arial, sans-serif;
    }
    .custom-popup .popup-body .discount{
      margin-top: 1rem;
      text-align: center;
      font-family: Roboto, Arial, sans-serif;
    }
    .custom-popup .popup-body hr{
      border: 1px solid black;
    }
    .custom-popup .popup-body .buttons{
     text-align: center;
     margin-top: 1rem;
    }
    .custom-popup .popup-body .buttons .button-yes{
     padding: .25rem .5rem .25rem .5rem;
     color: white;
     background-color: #48a767;
     border: 1px solid black;
     border-radius: 3px;
     margin-right: .25rem;
    }
    .custom-popup .popup-body .buttons .button-no{
      padding: .25rem .75rem .25rem .75rem;
      color: black;
      background: none;
      border: 1px solid black;
      border-radius:3px ;
      margin-left: .25rem;
      font-size: 12px;
    
    }
    
    .as-bundle-secondary-button  {
      background-color: #454f5b;
      color: white;
      margin-top: 16px;
    }
    
    .as-bundle-cancel {
      cursor: pointer;
    }
    
    input.as-bundle-variant {
      display: none;
    }
    
    
    .as-bundle-primary-button-text {
      display: inline-block;
    }
    
    .as-bundle-primary-button-loader {
      display: none;
    }
    .as-bundle-loading .as-bundle-primary-button {
      opacity: 0.7;
      pointer-events: none;
      cursor: not-allowed;
    }
    .as-bundle-loading .as-bundle-primary-button-text {
      display: none;
    }
    
    .as-bundle-loading .as-bundle-primary-button-loader {
      display: flex;
    }
    
    .appstle_loader {
      border: 2px solid rgba(255, 255, 255, 0.2);
      border-top: 2px solid #fff;
      border-radius: 50%;
      width: 20px;
      height: 20px;
      animation: appstle_loading_spin 0.7s linear infinite; }
    
      .appstle_loader:empty {
        display: block !important;
      }
    
      .appstle-bundle-line-through {
        text-decoration: line-through;
      }
    
      .as-checkmark-round label {
        background-color: white;
        border: 1px solid #767676;
        border-radius: 3px;
        cursor: pointer;
        height: 22px;
        left: 8px;
        position: absolute;
        top: 8px;
        width: 22px;
        // pointer-events: none;
    }
    
    .as-checkmark-round label:hover {
      border-color: black;
    }
    
      .as-checkmark-round label:after {
        border: 2px solid #fff;
        border-top: none;
        border-right: none;
        content: "";
        height: 6px;
        left: 4px;
        opacity: 1;
        position: absolute;
        top: 6px;
        transform: rotate(-45deg);
        width: 11px;
      //  pointer-events: none;
    }
    
      .as-checkmark-round input[type="checkbox"] {
        visibility: hidden;
      //  pointer-events: none;
      }
    
      .as-checkmark-round input[type="checkbox"]:checked + label {
        background-color: #66bb6a;
        border-color: #66bb6a;
      }
    
      .as-checkmark-round input[type="checkbox"]:checked + label:after {
        opacity: 1;
      }
    
      .as-button-disabled, .as-item-disabled {
        opacity: 0.8;
        pointer-events: none;
        cursor: not-allowed;
      }
    
      .as-pointer-none {
        pointer-events: none;
      }
    
    @keyframes appstle_loading_spin {
      0% {
        transform: rotate(0deg); }
    
      100% {
        transform: rotate(360deg); } }
    
    @-webkit-keyframes spin {
      0% {
        transform: rotate(0deg); }
    
      100% {
        transform: rotate(360deg); } }
    
          </style>
          ${parseElementCSS()}
          <style>${RS?.Config?.bundle?.customCss}</style>`);
      }
    });
  });


  function parseElementCSS() {
    let customCSSElement = "";
    try {
      let cssList = JSON.parse(RS?.Config?.bundle?.elementCSS);
      if (cssList) {
        for (let i = 0; i < cssList?.length; i++) {
          customCSSElement += `<style>${cssList[i]}</style>`;
        }
      }
    } catch (e) {}
    return customCSSElement;
  }
};
try {
  appstleBundleInit();
} catch (e) {
  
}



