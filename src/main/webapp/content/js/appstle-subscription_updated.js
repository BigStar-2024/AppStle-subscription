var appstleInit = function () {
    var head = document.getElementsByTagName('head')[0];
    var startingTime = new Date().getTime();

    if (typeof jQuery == 'undefined' && !RS?.Config?.disableLoadingJquery) {
      var jQueryScript = document.createElement('script');
      jQueryScript.src = 'https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js';
      jQueryScript.type = 'text/javascript';
      head.appendChild(jQueryScript);
    }

    if (typeof Mustache == 'undefined') {
      var mustacheScript = document.createElement('script');
      mustacheScript.src = 'https://cdnjs.cloudflare.com/ajax/libs/mustache.js/3.1.0/mustache.js';
      mustacheScript.type = 'text/javascript';
      head.appendChild(mustacheScript);
    }

    // Poll for jQuery to come into existance
    var checkReady = function (callback) {
      if (window.jQuery && window.Mustache) {
        callback(jQuery);
      } else {
        window.setTimeout(function () {
          checkReady(callback);
        }, 20);
      }
    };

    const urlParams = new URLSearchParams(window.location.search);
    var globalUrlParameter = urlParams.get('variant');
    // Start polling...
    checkReady(function ($) {
      $(function () {
        var endingTime = new Date().getTime();
        var tookTime = endingTime - startingTime;
        console.log("jQuery is loaded, after " + tookTime + " milliseconds!");


        function renderWidget(standAloneProduct, standAloneElement, widgetId) {
          if(!window["Shopify"]) {
            return;
          }
          RS.Config = Object.assign(RS.Config, "undefined" != typeof _RSConfig && null !== _RSConfig ? _RSConfig : {})

          window.RSConfig = RS.Config;

          var product = RSConfig.product;
          var Selling_Plan_Variants_Global = {}
          if (standAloneProduct) {
            product = standAloneProduct;
          }

          processProductVariants(product);

          var localVariantsByTitle = RSConfig.variantsByTitle;
          var localVariantsById = RSConfig.variantsById;
          var localWindowVariant;

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
            var queryStringTokens = location.search.substr(1).split("&");
            var result = {};
            for (var index = 0; index < queryStringTokens.length; index++) {
              var keyValues = queryStringTokens[index].split("=")
              result[keyValues[0]] = keyValues[1];
            }
            return result
          }

          function urlParam(key) {
            return urlParamsToObject()[key] || null
          }

          function detectVariant(previousVariantId, selector, product) {
            var variantId = urlParam("variant");

            if (selector.closest('form[action="/cart/add"]').find('[name=id]').length > 0 && selector.closest('form[action="/cart/add"]').find('[name=id]')[0].value) {
              variantId = selector.closest('form[action="/cart/add"]').find('[name=id]')[0].value;
              return localVariantsById[variantId];
            } else if (variantId) {
              return localVariantsById[variantId];
            } else if (Object.values(localVariantsById).some(data => data?.title == $("form select[name='options[Bundle]']")?.val())) {
              return Object.values(localVariantsById).find(data => data?.title == $("select[name='options[Bundle]']").val())
            } else {
              var titleTokens;

              titleTokens = function () {
                var variantTokens = [];
                var singleOptionSelector = selector.closest('form[action="/cart/add"]').find("select.single-option-selector");
                if (singleOptionSelector != null && singleOptionSelector.selectedIndex != null && singleOptionSelector.selectedIndex !== -1) {
                  variantTokens.push(singleOptionSelector[singleOptionSelector.selectedIndex].value)
                } else {
                  variantTokens.push(void 0);
                }
                return variantTokens
              }();

              var variant = localVariantsByTitle[titleTokens.join(" / ")]
              if (variant) {
                return variant;
              } else {
                var variant = product.variants[0];
                return variant;
              }
            }
          }

          function getSellingPlanAllocation(variantId, sellingPlanId) {
            var variant = localVariantsById[variantId];
            return variant.selling_plan_allocations.find(function (plan) {
              return plan.selling_plan_id === sellingPlanId;
            });
          }

          function compareCustomerTags(array1, array2) {
            return array1.filter(function (n) {
              return array2.indexOf(n) != -1;
            });
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
              if (RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags && RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.trim()) {
                var sellingPlanTags = RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.split(",");
                var tagFound = compareCustomerTags(userTags, sellingPlanTags);
                isVisible = (tagFound.length > 0);
              }
            }

            return isVisible;
          }

          function buildSellingPlantText(sellingPlan) {
            var sellingPlanModel = sellingPlan.totalPrice == sellingPlan.formattedPrice ?
              {
                sellingPlanName: sellingPlan.name,
                sellingPlanPrice: `<span class="transcy-money">${sellingPlan.formattedPrice}</span>`,
                secondSellingPlanPrice: `<span class="transcy-money">${sellingPlan.secondFormattedPrice}</span>`,
                discountText: sellingPlan?.discountText,
                totalPrice: `<span class="transcy-money">${sellingPlan?.totalPrice}</span>`,
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

          function buildAtcButtonSelector() {
            var atcButtonSelector = RSConfig.atcButtonSelector || "form[action$='/cart/add'] [type='submit']"

            var atcButtonSelectorModel = {
              productId: product.id,
            };

            atcButtonSelector = Mustache.render(atcButtonSelector, atcButtonSelectorModel);
            return atcButtonSelector;
          }

          function buildOneTimePriceText(price) {
            var priceModel = {
              price: price,
            };

            var priceText = Mustache.render(RS.Config.oneTimePriceText, priceModel);
            return wrapPriceWithSpanTag(priceText);
          }

          function buildSubscriptionOptionText(isPrepaidPlan, discountValue) {
            let subscriptionOptionModel = {
              discountValue: discountValue
            };

            let subscriptionOptionText = Mustache.render(RS.Config.subscriptionOptionText, subscriptionOptionModel);
            return wrapPriceWithSpanTag(subscriptionOptionText);
          }

          function buildSelectedPriceText(price, isPrePaid, totalPrice) {
            var priceModel = isPrePaid ? {
              pricePerDelivery: price,
              totalPrice: totalPrice
            } : {
              price: price,
            };

            var text = isPrePaid ? RS.Config.selectedPrepaidSellingPlanPriceText : RS.Config.selectedPayAsYouGoSellingPlanPriceText;
            var priceText = Mustache.render(text, priceModel);
            return wrapPriceWithSpanTag(priceText);
          }

          function buildPrepaidPerDeliveryPriceText(price) {
            var priceModel = {
              prepaidPerDeliveryPrice: price,
            }
          return wrapPriceWithSpanTag(Mustache.render(RS.Config.totalPricePerDeliveryText, priceModel));
          }

          function buildSelectedTooltipPrePaidText(price, totalPrice) {
            var prepaidPricetooltipModel = {
              pricePerDelivery: price,
              totalPrice: totalPrice
            }

            var text = RS.Config.tooltipDescriptionOnPrepaidPlan;
            var prepaidPricetooltipText = Mustache.render(text, prepaidPricetooltipModel);
            return wrapPriceWithSpanTag(prepaidPricetooltipText);
          }

          function buildSelectedTooltipDiscountText(selectedSellingPlanAllocationObj, multipleDiscount) {
            if (selectedSellingPlanAllocationObj?.price_adjustments?.length == 2) {
              var discountModel = {
                firstPrice: formatPriceWithQuantity(selectedSellingPlanAllocationObj?.price_adjustments[0]?.price),
                secondPrice: formatPriceWithQuantity(selectedSellingPlanAllocationObj?.price_adjustments[1]?.price),
                discountOne: multipleDiscount.length > 0 ? multipleDiscount[0] : "",
                discountTwo: multipleDiscount.length == 2 ? multipleDiscount[1] : ""
              };

              return wrapPriceWithSpanTag(Mustache.render(RS.Config.tooltipDescriptionOnMultipleDiscount, discountModel));
            }
          }

          function buildTooltipDetailsText(prepaidText, isPrePaid, discountText) {
            var tooltipDetailModel = isPrePaid ? {
                prepaidDetails: prepaidText,
                discountDetails: discountText,
                defaultTooltipDescription: RS.Config.tooltipDesctiption
              }
              :
              {
                defaultTooltipDescription: RS.Config.tooltipDesctiption,
                discountDetails: discountText
              };
            // let text = RS.Config.tooltipDesctiption + (isPrePaid ?  "</br>{{prepaidDetails}}" : "") + (discountText!= undefined ? "</br>{{discountDetails}}": "");
            let text = RS.Config.tooltipDescriptionCustomization;
            var tooltipText = Mustache.render(text, tooltipDetailModel);
            return wrapPriceWithSpanTag(tooltipText);
          }

          function populateDropdown(/*purchaseOptions, */variant) {
            var sellingPlanVariants = [];
            jQuery.each(product.selling_plan_groups, function (index, sellingPlanGroup) {
              if (sellingPlanGroup.app_id === 'appstle') {
                jQuery.each(sellingPlanGroup.selling_plans, function (subIndex, sellingPlan) {
                  var visible = isSellingPlanVisible(sellingPlan.id);
                  if (visible) {
                    var sellingPlanAllocation = getSellingPlanAllocation(variant.id, sellingPlan.id);
                    if (!sellingPlanAllocation) {
                      return
                    }
                    var price = sellingPlanAllocation.per_delivery_price;
                    var totalPrice = formatPriceWithQuantity(sellingPlanAllocation?.price);
                    var formattedPrice = formatPriceWithQuantity(price);

                    var secondPrice = null;
                    var secondFormattedPrice = null;

                    if (sellingPlanAllocation
                      && sellingPlanAllocation.price_adjustments
                      && sellingPlanAllocation.price_adjustments.length === 2) {
                      secondPrice = sellingPlanAllocation.price_adjustments[1].price;
                      secondFormattedPrice = formatPriceWithQuantity(secondPrice);
                    } else {
                      secondPrice = price;
                      secondFormattedPrice = formattedPrice;
                    }
                    let discountText;
                    var priceAdjustment = sellingPlan?.price_adjustments[0];
                    if (priceAdjustment?.value_type !== 'percentage') {
                      discountText = formatPriceWithQuantity(priceAdjustment?.value);
                    } else {
                      discountText = priceAdjustment?.value + '%';
                    }
                     var sellingPlanDetails =    {
                      "name": sellingPlan.name,
                      "description": sellingPlan.description,
                      "sellingPlanId": sellingPlan.id,
                      "formattedPrice": formattedPrice,
                      "price": price,
                      "totalPrice": totalPrice,
                      "secondPrice": secondPrice,
                      "secondFormattedPrice": secondFormattedPrice,
                      "discountText": priceAdjustment?.value ? discountText : null,
                      "formattedDiscountText": priceAdjustment?.value ? buildDiscountText(discountText) : ''
                    }
                  jQuery.extend(sellingPlanDetails, JSON.parse(JSON.stringify(getSellingPlanDetails(sellingPlan.id))))
                  sellingPlanDetails["isFrequencySubsequent"] = sellingPlanDetails.frequencyCount > 1;
                  sellingPlanDetails["frequencyIntervalLowerCase"] = sellingPlanDetails.frequencyInterval.toLowerCase();
                  if(sellingPlanDetails.discountText === null) {
                      sellingPlanDetails["secondFormattedDiscountText"] = 'full price'
                  } else {
                      sellingPlanDetails["secondFormattedDiscountText"] = discountText + ' off'
                  }
                    sellingPlanVariants.push(sellingPlanDetails);
                  }
                });
              }
            });

            //Added Code by Akash
            if (!RSConfig?.sortByDefaultSequence) {
                sellingPlanVariants.sort(function (sellingPlanA, sellingPlanB) {
                  return sellingPlanA.price - sellingPlanB.price;
                })
              }

              jQuery(sellingPlanVariants).each(function (index, sellingPlan) {
                sellingPlan.sellingPlanDisplayText = buildSellingPlantText(sellingPlan);
              })

              Selling_Plan_Variants_Global.sellingPlanVariants = sellingPlanVariants

            if (sellingPlanVariants.length > 0) {
              if (sellingPlanVariants.length < 2) {
                  Selling_Plan_Variants_Global.multipleSellingPlan = false
                /*jQuery(purchaseOptions).find('.appstle_subscribe_option').children().hide();*/

                var singleSellingPlanDisplayText = buildSellingPlantText(sellingPlanVariants[0]);
                Selling_Plan_Variants_Global.singleSellingPlanDisplayText = singleSellingPlanDisplayText

                /*var planText = jQuery(`<div class="appstle_single_option_text">${singleSellingPlanDisplayText}</div>`);
                planText.appendTo(purchaseOptions.find('.appstle_subscribe_option'))
                if (sellingPlanVariants[0]?.description) {
                  jQuery(`<div class="appstleSellingPlanDescription">${sellingPlanVariants[0]?.description}</div>` ).appendTo(purchaseOptions.find('.appstle_subscribe_option'));
                }*/

              } else {
                  Selling_Plan_Variants_Global.multipleSellingPlan = true
              }
              if (!RSConfig?.sortByDefaultSequence) {
                sellingPlanVariants.sort(function (sellingPlanA, sellingPlanB) {
                  return sellingPlanA.price - sellingPlanB.price;
                })
              }

              /*jQuery(sellingPlanVariants).each(function (index, sellingPlan) {

                var sellingPlanDisplayText = buildSellingPlantText(sellingPlan);
                if (!RSConfig?.switchRadioButtonWidget) {
                  jQuery('<option />', {
                    value: sellingPlan.id,
                    html: sellingPlanDisplayText
                  }).appendTo(purchaseOptions.find('select'));
                } else {
                  jQuery(`
                  <div>
                    <label for="${sellingPlan.id}" style="margin-top: 10px; ${sellingPlanVariants.length < 2 ? 'display: none;' : ''}">
                      <input type="radio" id="${sellingPlan.id}" value="${sellingPlan.id}" ${!index ? 'checked' : ''} name="selling_plan_radio" style="display: inline;"></input><span class="sellingplan">${sellingPlanDisplayText}</span>
                    </label>
                    ${sellingPlan?.description ? `<div class="appstleSellingPlanDescription">${sellingPlan?.description}</div>` : ``}
                  </div>`).appendTo(purchaseOptions.find('.appstleRadioSellingPlanWrapper'));
                }
              });*/
            } else {
              try {
                jQuery('#appstle_subscription_widget' + widgetId).remove()
                var timer = setTimeout(function () {
                  jQuery('#appstle_subscription_widget' + widgetId).remove()
                  clearTimeout(timer)
                })
              } catch (e) {
              }
            }

            /*sellingPlanVariants.length > 0 ? purchaseOptions.find('.appstle_subscription_amount')
              .text(sellingPlanVariants[0].formattedPrice) : '';*/
            return sellingPlanVariants;
          }

          var appstleSubscriptionFunction = function () {
            if (urlIsProductPage() === true || appstleStandAloneSelectorExists()) {
              var atcButtonSelector = buildAtcButtonSelector();

              var atcButton = jQuery(atcButtonSelector).first();

              if (standAloneElement) {
                atcButton = standAloneElement;
              }

              var purchaseOptionsText;
              //var purchaseOptions;
              var variantId;
              var subscriptionWrapper;
              var subscriptionWidget;
              var subscriptionTooltip;

              atcButton.parents(RSConfig?.widgetParentSelector).find(RSConfig?.quantitySelector).on('change, click', function () {
                setTimeout(updateStateOfWidget, 100)
              });

              var reload = function () {
                // RSConfig.switchRadioButtonWidget = true;
                try {
                  // if(!product.available)
                  // {
                  //   return;
                  // }
                  atcButton = jQuery(atcButtonSelector).first();

                  if (standAloneElement) {
                    atcButton = standAloneElement;
                  }
                  var variant = detectVariant(variantId, atcButton, product);
                  if (variant) {

                    var localVariantId = variant.id;

                    if (variantId) {
                      if (variantId === localVariantId) {
                        return;
                      }
                    }
                    var appstle_selling_plan_groups;
                    if (product.selling_plan_groups && product.selling_plan_groups.length > 0) {
                      appstle_selling_plan_groups = product.selling_plan_groups.filter(function (group) {
                        return group.app_id === 'appstle'
                      })
                    }
                    if (appstle_selling_plan_groups && appstle_selling_plan_groups.length > 0 && RS.Config.widgetEnabled) {
                      localWindowVariant = variant;
                      var widgetPlacement = RS.Config.selectors.atcButtonPlacement;
                      if (!subscriptionWrapper) {

                        subscriptionWidget = jQuery('<div class="appstle_sub_widget" id="appstle_subscription_widget' + widgetId + '"></div>');
                        purchaseOptionsText = RS.Config.purchaseOptionsText ? jQuery('<div class="appstle_widget_title">' + RS.Config.purchaseOptionsText + '</div>') : '';
                        subscriptionWrapper = jQuery('<div class="appstle_subscription_wrapper"></div>');
                        subscriptionTooltip = RS.Config.tooltipTitle && RS.Config.showTooltip && !RS.Config.showStaticTooltip ?
                        jQuery(`<div data-appstle-icon="" class="appstle_tooltip_wrapper">
                          <svg width="90" height="90" viewBox="0 0 90 90" xmlns="http://www.w3.org/2000/svg" class="tooltip_subscription_svg">
                            <path d="M45 0C20.1827 0 0 20.1827 0 45C0 69.8173 20.1827 90 45 90C69.8173 90 90 69.8174 90 45C90.0056 44.6025 89.9322 44.2078 89.7839 43.8389C89.6357 43.47 89.4156 43.1342 89.1365 42.8511C88.8573 42.568 88.5247 42.3432 88.158 42.1897C87.7912 42.0363 87.3976 41.9573 87 41.9573C86.6024 41.9573 86.2088 42.0363 85.842 42.1897C85.4753 42.3432 85.1427 42.568 84.8635 42.8511C84.5844 43.1342 84.3643 43.47 84.2161 43.8389C84.0678 44.2078 83.9944 44.6025 84 45C84 66.5748 66.5747 84 45 84C23.4253 84 6 66.5747 6 45C6 23.4254 23.4253 6 45 6C56.1538 6 66.3012 10.5882 73.4375 18H65.4062C65.0087 17.9944 64.614 18.0678 64.2451 18.2161C63.8762 18.3643 63.5405 18.5844 63.2573 18.8635C62.9742 19.1427 62.7494 19.4753 62.596 19.842C62.4425 20.2088 62.3635 20.6024 62.3635 21C62.3635 21.3976 62.4425 21.7912 62.596 22.158C62.7494 22.5247 62.9742 22.8573 63.2573 23.1365C63.5405 23.4156 63.8762 23.6357 64.2451 23.7839C64.614 23.9322 65.0087 24.0056 65.4062 24H79.8125C80.6081 23.9999 81.3711 23.6838 81.9337 23.1212C82.4963 22.5586 82.8124 21.7956 82.8125 21V6.59375C82.821 6.18925 82.7476 5.78722 82.5966 5.41183C82.4457 5.03644 82.2205 4.69545 81.9344 4.40936C81.6483 4.12327 81.3073 3.898 80.9319 3.7471C80.5565 3.5962 80.1545 3.52277 79.75 3.53125C79.356 3.53941 78.9675 3.62511 78.6067 3.78344C78.2458 3.94177 77.9197 4.16963 77.6469 4.45402C77.3741 4.73841 77.16 5.07375 77.0168 5.44089C76.8737 5.80803 76.8042 6.19977 76.8125 6.59375V12.875C68.6156 4.86282 57.3081 0 45 0ZM43.75 20.75C43.356 20.7582 42.9675 20.8439 42.6067 21.0022C42.2458 21.1605 41.9197 21.3884 41.6469 21.6728C41.3741 21.9572 41.16 22.2925 41.0168 22.6596C40.8737 23.0268 40.8042 23.4185 40.8125 23.8125V47.375C40.8116 47.7693 40.8883 48.16 41.0385 48.5246C41.1886 48.8892 41.4092 49.2207 41.6875 49.5L54.0938 61.9375C54.6573 62.5011 55.4217 62.8177 56.2188 62.8177C57.0158 62.8177 57.7802 62.5011 58.3438 61.9375C58.9073 61.3739 59.224 60.6095 59.224 59.8125C59.224 59.0155 58.9073 58.2511 58.3438 57.6875L46.8125 46.1875V23.8125C46.821 23.408 46.7476 23.006 46.5966 22.6306C46.4457 22.2552 46.2205 21.9142 45.9344 21.6281C45.6483 21.342 45.3073 21.1168 44.9319 20.9658C44.5565 20.8149 44.1545 20.7415 43.75 20.75Z">
                            </path>
                          </svg>
                          <span class="appstle_tooltip_title">${RS.Config.tooltipTitle}</span>
                          <div class="appstle_tooltip">
                            <div class="appstle_tooltip_content">
                                ${RS.Config.tooltipDesctiption}
                            </div>
                            ${RS.Config.showAppstleLink ? `<div class="appstle_tooltip_appstle">
                              <a href="https://appstle.com/" class="appstle_link" target="_blank">
                                POWERED BY APPSTLE
                              </a>
                            </div>` : "<span></span>"}
                                          </div>
                                        </div>`) : RS.Config.showTooltip && RS.Config.showStaticTooltip && jQuery(`<div class="appstle_tooltip_wrapper_static">${RS.Config.tooltipDesctiption}</div>`);

                        purchaseOptionsText ? purchaseOptionsText.appendTo(subscriptionWidget) : '';

                        addSubscriptionItems(subscriptionWrapper, variant);
                        MustacheWrapperJSON = createJsonformat()
                        console.log(MustacheWrapperJSON)
                        subscriptionWrapper.appendTo(subscriptionWidget);
                        subscriptionTooltip ? subscriptionTooltip.appendTo(subscriptionWidget) : '';

                      } else {
                        unbindEventListeners();
                        subscriptionWrapper.children().remove();
                        addSubscriptionItems(subscriptionWrapper, variant);
                        MustacheWrapperJSON = createJsonformat()
                        console.log(MustacheWrapperJSON)
                        subscriptionTooltip ? subscriptionWrapper.insertBefore(subscriptionTooltip) : subscriptionWrapper.appendTo(subscriptionWidget);
                      }

                      if(!product?.available)
                      {
                        if(RS.Config?.widgetEnabledOnSoldVariant == "false")
                        {
                          return;
                        }
                      }

                      if (widgetPlacement === 'BEFORE') {
                          var render = Mustache.render(template,MustacheWrapperJSON)
                          jQuery(render).insertBefore(atcButton);
                        //subscriptionWidget.insertBefore(atcButton);
                      } else if (widgetPlacement === 'AFTER') {
                        subscriptionWidget.insertAfter(atcButton);
                      } else if (widgetPlacement === 'FIRST_CHILD') {
                        subscriptionWidget.prependTo(atcButton);
                      } else if (widgetPlacement === 'LAST_CHILD') {
                        subscriptionWidget.appendTo(atcButton);
                      }

                      updateWidgetElements();
                      jQuery(document).trigger("appstle_widget_updated");
                    }

                    variantId = localVariantId;
                  }
                } catch (e) {
                  console.log(e)
                }
              }

              setTimeout(function () {
                reload();
                updateWidgetUIBasedOnQueryParams();
              }, 13);

              //  CODE FOR CALL A FUNCTION AUTOMATICALLY WHEN URL VARIANT ID WILL CHANGE. IT WILL CHECK ON EVERY 5ms
              // setInterval(()=>{
              //   var currentURLparam = new URLSearchParams(window.location.search)
              //   var currentParameter = currentURLparam.get('variant');
              //   if(globalUrlParameter != currentParameter)
              //     reload();
              //     updateHistoryState();
              //     globalUrlParameter= currentParameter;
              // }, 500)

              if (standAloneElement) {
                standAloneElement.closest('form[action="/cart/add"]').on('change', function () {
                  setTimeout(function () {
                    reload();
                    updateHistoryState();
                    if(window?._transcy)
                      {
                        window?._transcy?.methods?.reConvertCurrency()
                      }
                }, parseInt(RSConfig?.scriptLoadDelay) > 0 ? parseInt(RSConfig?.scriptLoadDelay) : 30);
                });
                standAloneElement.closest('form[action="/cart/add"]').on('click', function () {
                  setTimeout(function () {
                    reload();
                    updateHistoryState();
                    if(window?._transcy)
                      {
                        window?._transcy?.methods?.reConvertCurrency()
                      }
                }, parseInt(RSConfig?.scriptLoadDelay) > 0 ? parseInt(RSConfig?.scriptLoadDelay) : 30);
                });
              } else {
                jQuery(document).on('change', function () {
                  setTimeout(function () {
                      reload();
                      updateHistoryState();
                      if(window?._transcy)
                      {
                        window?._transcy?.methods?.reConvertCurrency()
                      }
                  }, parseInt(RSConfig?.scriptLoadDelay) > 0 ? parseInt(RSConfig?.scriptLoadDelay) : 30);
                });
                jQuery(document).on('click', function () {
                  setTimeout(function () {
                    reload();
                    updateHistoryState();
                    if(window?._transcy)
                      {
                        window?._transcy?.methods?.reConvertCurrency()
                      }
                }, parseInt(RSConfig?.scriptLoadDelay) > 0 ? parseInt(RSConfig?.scriptLoadDelay) : 30);
                });
              }
            }

            if (urlIsAccountPage() === true) {
              var subscriptionLinkSelector = RSConfig.selectors.subscriptionLinkSelector;
              var subscriptionLinkPlacement = RSConfig.selectors.subscriptionLinkPlacement;
              var manageSubscriptionLink = RSConfig.manageSubscriptionBtnFormat
              if (subscriptionLinkPlacement.toLowerCase() === "after")
                jQuery(subscriptionLinkSelector).after(manageSubscriptionLink)
              else if (subscriptionLinkPlacement.toLowerCase() === "first_child")
                jQuery(subscriptionLinkSelector).first(manageSubscriptionLink)
              else if (subscriptionLinkPlacement.toLowerCase() === "last_child")
                jQuery(subscriptionLinkSelector).last(manageSubscriptionLink)
              else
                jQuery(subscriptionLinkSelector).before(manageSubscriptionLink)
              accountPageStyle();
            }
          }

          deferJquery(appstleSubscriptionFunction);

          function hidePaymentButtons() {
            jQuery(RSConfig.selectors.payment_button_selectors).hide();
          }

          function showPaymentButtons() {
            jQuery(RSConfig.selectors.payment_button_selectors).show();
          }


          function urlIsAccountPage() {
            return window.location.pathname === '/account' || window.location.pathname?.endsWith('/account')
            // if(window.location.pathname.includes('/account') || window.location.pathname === '/account')
            // {
            //   return true
            // }
          }

          function deferJquery(method) {
            if (window.jQuery)
              method();
            else
              setTimeout(function () {
                deferJquery(method)
              }, 50);
          }

          function addSubscriptionItems(subscriptionWrapper, variant) {
            /*if (!product.requires_selling_plan) {
              var oneTimePurchaseOption = jQuery(
                `<div class="appstle_subscription_wrapper_option ${(product.requires_selling_plan || RS.Config.subscriptionOptionSelectedByDefault ? '' : 'appstle_selected_background')}">
            <input type="radio" ${RS.Config.subscriptionOptionSelectedByDefault ? '' : 'checked'} id="appstle_selling_plan_label_1${widgetId}" name="selling_plan" value="" ${(RS.Config.formMappingAttributeName && RS.Config.formMappingAttributeSelector && jQuery(RS.Config.formMappingAttributeSelector).length && jQuery(RS.Config.formMappingAttributeSelector).attr("id")) ? `${RS.Config.formMappingAttributeName}=${jQuery(RS.Config.formMappingAttributeSelector).attr("id")}` : ``}>
            <label for="appstle_selling_plan_label_1${widgetId}" class="appstle_radio_label">
              <span class="appstle_circle"><span class="appstle_dot"></span></span>
                <span class="appstle_one_time_text">${RS.Config.oneTimePurchaseText}</span>
               </label>
            <span class="appstle_subscription_amount transcy-money"></span>
           </div>`
              );
              oneTimePurchaseOption.appendTo(subscriptionWrapper);
            }

            var subscriptionDropdown = jQuery(
              `<div class="appstle_subscription_wrapper_option appstle_include_dropdown ${(product.requires_selling_plan || RS.Config.subscriptionOptionSelectedByDefault ? 'appstle_selected_background' : '')}">
              <div class="appstle_subscription_radio_wrapper">
                  <input type="radio" id="appstle_selling_plan_label_2${widgetId}" name="selling_plan" value="Subscribe and save" ${product.requires_selling_plan || RS.Config.subscriptionOptionSelectedByDefault ? 'checked' : ''} ${(RS.Config.formMappingAttributeName && RS.Config.formMappingAttributeSelector && jQuery(RS.Config.formMappingAttributeSelector).length && jQuery(RS.Config.formMappingAttributeSelector).attr("id")) ? `${RS.Config.formMappingAttributeName}=${jQuery(RS.Config.formMappingAttributeSelector).attr("id")}` : ``}>
                  <label for="appstle_selling_plan_label_2${widgetId}" class="appstle_radio_label">
                      <span class="appstle_circle"><span class="appstle_dot"></span></span>
                      <span class="appstle_subscribe_save_text">${RS.Config.subscriptionOptionText}</span>
                  </label>
                  <div class="appstle_subscription_amount_wrapper">
                    <span class="appstle_subscription_amount transcy-money"></span>
                  </div>
              </div>
              <div class="appstle_subscribe_option ${(product.requires_selling_plan || RS.Config.subscriptionOptionSelectedByDefault ? '' : 'appstle_hide_subsOption')}">
                  ${RS.Config.sellingPlanSelectTitle ? `<label for="appstle_selling_plan${widgetId}" class="appstle_select_label">${RS.Config.sellingPlanSelectTitle}</label>` : ``}
                  ${!RSConfig?.switchRadioButtonWidget ? ` <select id="appstle_selling_plan${widgetId}" class="appstle_select">
                  </select><div class="appstleSelectedSellingPlanOptionDescription"></div>` : `<div class="appstleRadioSellingPlanWrapper"></div>`}

              </div>
          </div>`
            )*/

            var sellingPlanVariants = populateDropdown(variant)
            /*if (sellingPlanVariants && sellingPlanVariants.length > 0) {
              if(RSConfig?.showSubOptionBeforeOneTime)
              {
                subscriptionDropdown.prependTo(subscriptionWrapper);
              } else {
                subscriptionDropdown.appendTo(subscriptionWrapper);
              }
            }*/

            addStyle(widgetId);
          }

          function changeEventHandlerForRadio() {
            jQuery('#appstle_subscription_widget' + widgetId).find('.appstle_subscription_wrapper input[type=radio]').on('change',
              function () {
                var subsOption = jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscribe_option');
                if (subsOption.length > 0) {
                  if (checkIfSellingPlanGroupIsSelected()) {
                    subsOption.removeClass('appstle_hide_subsOption')
                    jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option.appstle_include_dropdown').addClass('appstle_selected_background')
                    jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option').first().removeClass('appstle_selected_background')
                  } else {
                    jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option.appstle_include_dropdown').removeClass('appstle_selected_background')
                    jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option').first().addClass('appstle_selected_background')
                    subsOption.addClass('appstle_hide_subsOption')
                  }
                }
                updateStateOfWidget();
              });
          }

          function changeHandlerForSelect() {
            /*if (!RSConfig?.switchRadioButtonWidget) {
              jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper select').on('change', function (event) {
                updateStateOfWidget();
              });
            } else {
              jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscribe_option input').on('change', function (event) {
                updateStateOfWidget();
              });
            }*/
            jQuery('#appstle_subscription_widget' + widgetId + ' .appstleSellingPlan' + widgetId).on('change', function (event) {
                updateStateOfWidget();
              });
          }

          function closeToolTipEvent() {
              let button=document.querySelector(".appstle_tooltip_wrapper");
              button.addEventListener("click", clickableFunction);
              function clickableFunction() {
                  const div = document.querySelector('div');
                  if (div.classList.contains('appstle_tooltip_hover')) {
                      $('div').removeClass('appstle_tooltip_hover');
                  } else {
                      $('div').addClass('appstle_tooltip_hover');
                  }
              }
          }

          function triggerChangeEvent(selector) {
            jQuery(selector).change();
          }

          function registerAndTriggerEventsForFormFields() {
            changeEventHandlerForRadio();
            changeHandlerForSelect();
            closeToolTipEvent();
          }

          function unbindEventListeners() {
            jQuery('.appstle_subscription_wrapper input[type=radio], .appstle_subscription_wrapper select').off('change')
          }

          function getSelectedSellingPlanPrice() {
            var selectedSellingPlan = getSelectedSellingPlanId();
            if (selectedSellingPlan) {
              var selectedSellingPlanAllocation = getSellingPlanAllocation(localWindowVariant.id, parseInt(selectedSellingPlan));
              return formatPriceWithQuantity(selectedSellingPlanAllocation.per_delivery_price);
            } else {
              return null;
            }

          }

          function getCurrentQuantity() {
            var atc = jQuery(buildAtcButtonSelector()).first();
            return Array.prototype.slice.call(atc.parents(RSConfig?.widgetParentSelector).find(RSConfig?.quantitySelector)).map((el) => Number($(el).val())).reduce(function(a, b) {
              return Math.max(a, b);
            }, -Infinity);
          }

          function getCurrentQuantityPrice(price) {
            return price  * ((RSConfig?.updatePriceOnQuantityChange === "true") ? (parseInt(getCurrentQuantity()) || 1) : 1)
          }

          function formatPriceWithQuantity(price) {
            return formatPrice(getCurrentQuantityPrice(price));
          }

          function getSelectedSellingPlanId() {
            var value = null;
            try {
              /*if (!RSConfig?.switchRadioButtonWidget) {
                value = jQuery("#appstle_subscription_widget" + widgetId).find('.appstle_subscription_wrapper_option.appstle_include_dropdown select').val();
                var inputType = $('.appstleSellingPlan').attr('type');
                console.log('inputType',inputType)
              } else {
                value = jQuery("#appstle_subscription_widget" + widgetId).find('.appstle_subscribe_option input:checked').val();
                var inputType = $('.appstleSellingPlan').attr('type');
                console.log('inputType',inputType)
              }*/
              if (($('.appstleSellingPlan' + widgetId).attr('type'))==='radio'){
                  value = $("input[type='radio'].appstleSellingPlan" + widgetId +":checked").val();
              } else {
                  value = $('.appstleSellingPlan' + widgetId).val()
              }
            } catch (e) {
            }
            return value;
          }

          function getVariantId() {
            return jQuery('#appstle_subscription_widget' + widgetId).closest('form[action="/cart/add"]').find('[name=id]')[0] == undefined ? localWindowVariant.id : jQuery('#appstle_subscription_widget' + widgetId).closest('form[action="/cart/add"]').find('[name=id]')[0].value;
          }

          function updateOneTimePurchaseValueToRadio() {
            var variantId = getVariantId();
            var price = null;
            if (standAloneElement) {
              price = ((standAloneProduct?.variants.filter(variant => String(variant?.id) === String(variantId))).pop()).price
            } else {
              price = RSConfig?.variantsById[variantId]?.price
            }
            jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option:not(.appstle_include_dropdown) .appstle_subscription_amount').html(wrapPriceWithSpanTag(buildOneTimePriceText(formatPriceWithQuantity(price))));
          }

          function updateSelectValueToRadio() {
            var selectedSellingPlan = getSelectedSellingPlanId();
            if (selectedSellingPlan) {
              var variantId = getVariantId();
              var selectedSellingPlanAllocation = getSellingPlanAllocation(variantId, parseInt(selectedSellingPlan));
              var formattedSelectedSellingPlanPrice = formatPriceWithQuantity(selectedSellingPlanAllocation.per_delivery_price);
              var sellingPlanDescription = getSellingPlanDescription(selectedSellingPlanAllocation?.selling_plan_group_id, selectedSellingPlanAllocation?.selling_plan_id)

              jQuery('#appstle_subscription_widget' + widgetId + ' #appstle_selling_plan_label_2' + widgetId).attr('value', selectedSellingPlan);
              var isPrepaidPlan = selectedSellingPlanAllocation.per_delivery_price !== selectedSellingPlanAllocation.price;
              var selectedPriceText = wrapPriceWithSpanTag(buildSelectedPriceText(
                formattedSelectedSellingPlanPrice,
                isPrepaidPlan,
                formatPriceWithQuantity(selectedSellingPlanAllocation.price)));

              let selectedDiscountSub = getSellingPlanDiscountPercentage();
              let multipleDiscount = getSellingPlanDiscountPercentage(true);
              var toolTipPrepaidText = buildSelectedTooltipPrePaidText(formattedSelectedSellingPlanPrice, formatPriceWithQuantity(selectedSellingPlanAllocation.price));
              var toolTipDiscountText = buildSelectedTooltipDiscountText(selectedSellingPlanAllocation, multipleDiscount);
              var tooltipText = buildTooltipDetailsText(toolTipPrepaidText, isPrepaidPlan, toolTipDiscountText);
              var subscribesaveText = buildSubscriptionOptionText(isPrepaidPlan, selectedDiscountSub);
              var prepaidPerDeliveryPriceText = buildPrepaidPerDeliveryPriceText(formattedSelectedSellingPlanPrice);

              jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_prepaid_description').remove();
              jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option.appstle_include_dropdown .appstle_subscription_amount').html(selectedPriceText);
              if (isPrepaidPlan && prepaidPerDeliveryPriceText) {
                jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_amount_wrapper').append(
                  `<div class="appstle_prepaid_description transcy-money">${wrapPriceWithSpanTag(prepaidPerDeliveryPriceText) || (wrapPriceWithSpanTag(formatPriceWithQuantity(selectedSellingPlanAllocation.price)) + '/delivery')}</div>`
                )
              }

              jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_subscription_wrapper_option.appstle_include_dropdown .appstle_subscription_radio_wrapper .appstle_subscribe_save_text').html(subscribesaveText);

              if (checkIfSellingPlanGroupIsSelected() && tooltipText && tooltipText.trim()) {
                if (RS.Config.showStaticTooltip) {
                  jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_tooltip_wrapper_static').html(tooltipText);
                } else {
                  jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_tooltip_wrapper .appstle_tooltip .appstle_tooltip_content').html(tooltipText);
                }
              } else {
                if (RS.Config.showStaticTooltip && RS.Config.tooltipDesctiption && RS.Config.tooltipDesctiption.trim()) {
                  jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_tooltip_wrapper_static').html(RS.Config.tooltipDesctiption);
                } else {
                  jQuery('#appstle_subscription_widget' + widgetId + ' .appstle_tooltip_wrapper .appstle_tooltip .appstle_tooltip_content').html(RS.Config.tooltipDesctiption);
                }
              }
              jQuery('#appstle_subscription_widget' + widgetId + " .appstleSelectedSellingPlanOptionDescription").html(sellingPlanDescription);
            }
          }

          function getSellingPlanDescription(groupId, sellingPlanId) {
            var sellingPlanGroup = (product?.selling_plan_groups.filter(sellingPlanGroup => sellingPlanGroup?.id === groupId)).pop();
            var sellingPlan = (sellingPlanGroup?.selling_plans?.filter(sellingPlan => sellingPlan?.id === sellingPlanId)).pop();
            return sellingPlan.description || '';
          }

          function checkIfSellingPlanGroupIsSelected() {
            var selected = false;
            try {
              selected = jQuery('#appstle_subscription_widget' + widgetId).find(`.appstleSellingPlan${widgetId}:checked`);
            } catch (e) {
            }
            return selected;
          }

          function buildDiscountText(selectedDiscount) {
            // selected discount customization start
            var selectedDiscountModel = {
              selectedDiscountPercentage: selectedDiscount
            }
            var selectedDiscountFormatDisplay = Mustache.render(RS.Config.selectedDiscountFormat, selectedDiscountModel);

            // selected discount customization end
            return wrapPriceWithSpanTag(selectedDiscountFormatDisplay);
          }

          function updateSellingPlanValueToFinalPrice() {

            var selectorsObject = {
              regular: {
                sellingPrice: RSConfig.priceSelector,
                saleBadgeTop: RSConfig.badgeTop
              }
            }

            var selectors = selectorsObject.regular;
            jQuery('.appstle_subscription_final_price').remove();
            jQuery('.appstle_subscription_element').remove();
            jQuery('.appstle_subscription_compare_price').remove();

            var formattedPrice = getSelectedSellingPlanPrice();
            var selectedDiscount = getSellingPlanDiscountPercentage();

            var sellingPrice = jQuery(selectors.sellingPrice);

            sellingPrice.css('text-decoration', '');
            // sellingPrice.css('display', '');

            if (!selectors.sellingPrice || !selectedDiscount) {
              return;
            }

            if (checkIfSellingPlanGroupIsSelected() && sellingPrice.length === 1) {

              var selectedDiscountFormatDisplay = buildDiscountText(selectedDiscount);

              if (selectedDiscountFormatDisplay) {
                var subscriptionPercentageSavingElement =
                  jQuery(`<span class="appstle_subscription_element appstle_subscription_save"> ${selectedDiscountFormatDisplay} </span>`)

                subscriptionPercentageSavingElement && subscriptionPercentageSavingElement.css('top', selectors.saleBadgeTop);

                var clonedSellingPrice = sellingPrice.first().clone();
                clonedSellingPrice.addClass('appstle_subscription_final_price transcy-money')
                clonedSellingPrice.html(formattedPrice);

                sellingPrice.css('text-decoration', 'line-through');


                (clonedSellingPrice.length > 0) && clonedSellingPrice.insertBefore(sellingPrice);
                subscriptionPercentageSavingElement.insertAfter(sellingPrice);
              }
            }
          }


          function getSellingPlanDiscountPercentage(isMultipleDiscount, selectedSellingPlan) {
            if (!selectedSellingPlan) {
                selectedSellingPlan = parseInt(getSelectedSellingPlanId());
            }
            if (!selectedSellingPlan) {
              return null;
            }
            var selectedSellingPlanGroup;
            product.selling_plan_groups.forEach(function (sellingPlanGroup) {
              if (sellingPlanGroup.app_id === 'appstle') {
                sellingPlanGroup.selling_plans.forEach(function (sellingPlan) {
                  var visible = isSellingPlanVisible(sellingPlan.id);
                  if (visible && sellingPlan.id === selectedSellingPlan) {
                    selectedSellingPlanGroup = sellingPlanGroup;
                  }
                })
              }
            })
            var selectedSellingPlan;
            selectedSellingPlanGroup.selling_plans.forEach(function (selling_plan) {
              if (selling_plan.id === selectedSellingPlan) {
                selectedSellingPlan = selling_plan;
              }
            })

            if (isMultipleDiscount && selectedSellingPlan?.price_adjustments?.length == 2) {
              var multipleDiscount = [];
              selectedSellingPlan?.price_adjustments?.forEach((discountValue) => {
                if (discountValue?.value_type !== 'percentage') {
                  multipleDiscount.push(formatPriceWithQuantity(discountValue?.value))
                } else {
                  multipleDiscount.push(discountValue?.value + '%');
                }
              })
              return multipleDiscount;

            }

            var priceAdjustment = selectedSellingPlan?.price_adjustments[0];

            if (priceAdjustment == null || priceAdjustment.value == null || priceAdjustment.value == 0) {
              return null;
            }
            var selectedPriceAdjustment;
            if (priceAdjustment?.value_type !== 'percentage') {
              selectedPriceAdjustment = formatPriceWithQuantity(priceAdjustment?.value);
            } else {
              selectedPriceAdjustment = priceAdjustment?.value + '%';
            }

            return selectedPriceAdjustment

          }

          function updateWidgetElements() {
            registerAndTriggerEventsForFormFields();
            updateStateOfWidget();
          }

          function updateStateOfWidget() {
            updateSelectValueToRadio();
            updateOneTimePurchaseValueToRadio();
            updateSellingPlanValueToFinalPrice();
          }

          function createJsonformat() {
              var jsonWrapperOutput = {}
              var variantId = getVariantId();
              var price = null;
              if (standAloneElement) {
              price = ((standAloneProduct?.variants.filter(variant => String(variant?.id) === String(variantId))).pop()).price
              } else {
              price = RSConfig?.variantsById[variantId]?.price
              }
              jsonWrapperOutput.oneTimePuchaseText = 'One Time Purchase'
              jsonWrapperOutput.oneTimePuchaseAmount = buildOneTimePriceText(formatPriceWithQuantity(price))
              jsonWrapperOutput.subscribeText = 'Subscribe and save'
              jsonWrapperOutput.widgetId = widgetId
              jsonWrapperOutput = Object.assign(Selling_Plan_Variants_Global,jsonWrapperOutput)
              jsonWrapperOutput.toolTipDescription = RS.Config.tooltipDesctiption
              jsonWrapperOutput.companyWebsite = 'https://appstle.com/'
              jsonWrapperOutput.companyName = 'POWERED BY APPSTLE'
              return jsonWrapperOutput
          }

          function getCssAsString(cssObject) {
            var cssObjectWithValidValues = {};
            if(cssObject) {
            Object.keys(cssObject).forEach(function (key) {
              if (cssObject[key]) {
                cssObjectWithValidValues[key] = cssObject[key]
              }
            })
            return JSON.stringify(cssObjectWithValidValues)
              .split('"').join('')
              .split('{').join('')
              .split('}').join('')
              .split(',').join(';');
            }
            return "";
          }

          function updateHistoryState() {
            if (!localWindowVariant) {
              return;
            }
            var variantId = localWindowVariant.id;
            if (!history.replaceState || !variantId) {
              return;
            }
            var newurl =
              window.location.protocol +
              '//' +
              window.location.host +
              window.location.pathname +
              '?';
            if (checkIfSellingPlanGroupIsSelected()) {
              var selectedSellingPlanId = getSelectedSellingPlanId();
              if (selectedSellingPlanId) {
                newurl += 'selling_plan=' + selectedSellingPlanId + '&';
              }
            }

            newurl += 'variant=' + variantId;

            if (newurl !== location.href) {
              window.history.replaceState({path: newurl}, '', newurl);
            }
          }

          function updateWidgetUIBasedOnQueryParams() {
            if (jQuery('#appstle_subscription_widget' + widgetId).length === 0) {
              return;
            }
            var paramsObj = urlParamsToObject();
            var formElement = jQuery('#appstle_subscription_widget' + widgetId).closest('form[action="/cart/add"]')
            if ((formElement.find('input[value=' + paramsObj["variant"] + ']').length > 0 || formElement.find('option[value=' + paramsObj["variant"] + ']').length > 0) && ((formElement.find('option[value=' + paramsObj["selling_plan"] + ']').length > 0) || (formElement.find('input[value=' + paramsObj["selling_plan"] + ']').length > 0))) {

              if (paramsObj['selling_plan']) {
      //           var sellingPlanGroup = jQuery('#appstle_subscription_widget' + widgetId + ' #appstle_selling_plan_label_2' + widgetId);
                  // if (sellingPlanGroup.length) {
                  // 	sellingPlanGroup[0].checked = true;
                  // }

                // var sellingPlanDropdown = null;
                // if (!RSConfig?.switchRadioButtonWidget) {
                //   sellingPlanDropdown = jQuery('#appstle_subscription_widget' + widgetId + ' select#appstle_selling_plan' + widgetId)
                // } else {
                //  // sellingPlanDropdown = jQuery('#appstle_subscription_widget' + widgetId + ` .appstleSellingPlan${widgetId}`)
                // }
                if (formElement.find('.appstleSellingPlanSelect').length) {
                  formElement.find(`select.appstleSellingPlan${widgetId} option`).each(function (index, option) {
                    if (jQuery(option).attr('value') === paramsObj['selling_plan']) {
                      formElement.find('select.appstleSellingPlan.appstleSellingPlanSelect')[0].selectedIndex = index;
                    }
                  });
                } else {
                  formElement.find('input[type=radio].appstleSellingPlan' + widgetId).each(function (index, option) {
                    $(option).removeAttr("checked");
                    if (jQuery(option).attr('value') === paramsObj['selling_plan']) {
                      $(option).attr("checked", true)
                    }
                  });
                }
                  formElement.find('.appstleSellingPlan' + widgetId).change();
                //sellingPlanGroup.change();
                //sellingPlanDropdown.change();
              }
            }
          }

          if (Shopify && Shopify.checkout && Shopify.checkout.line_items.filter(l => l.selling_plan_id).length >= 1 && RS?.Config?.showCheckoutSubscriptionBtn) {
            var destination = "/" + RS.Config.manageSubscriptionUrl;
            var message = RS.Config.orderStatusManageSubscriptionDescription || 'Continue to your account to view and manage your subscriptions.';

            if (Shopify.checkout && Shopify.checkout.order_id) {
              console.log("cusomterId=" + Shopify.checkout.order_id);

              var settings = {
                "async": true,
                "crossDomain": true,
                "url": location.origin + "/apps/subscriptions?action=customer_payment_token&order_id=" + Shopify.checkout.order_id,
                "method": "GET",
                "headers": {
                  "accept": "*/*",
                  "cache-control": "no-cache",
                }
              }

              jQuery.ajax(settings).done(function (response) {
                destination = destination + "?token=" + response.token;
                Shopify.Checkout.OrderStatus.addContentBox(
                  '<h2 class="heading-2 os-step__title">' + (RS.Config.orderStatusManageSubscriptionTitle || 'Subscription') + '</h2><div class="os-step__info--item"><p class="os-step__description" style="margin-top: 0.5714285714em;">' + message + '</p></div><a class="ui-button btn btn--subdued btn--size-small shown-if-js os-step__info" href="' + destination + '"> ' + (RS.Config.orderStatusManageSubscriptionButtonText || 'Manage your subscription') + '</a>'
                );
              });
            } else {
              Shopify.Checkout.OrderStatus.addContentBox(
                '<h2 class="heading-2 os-step__title">' + (RS.Config.orderStatusManageSubscriptionTitle || 'Subscription') + '</h2><div class="os-step__info--item"><p class="os-step__description" style="margin-top: 0.5714285714em;">' + message + '</p></div><a class="ui-button btn btn--subdued btn--size-small shown-if-js os-step__info" href="' + destination + '"> ' + (RS.Config.orderStatusManageSubscriptionButtonText || 'Manage your subscription') + '</a>'
              );
            }
          }

          function accountPageStyle() {
            var css = RSConfig.css;
            jQuery(
              `<style>${css.customCSS}</style>`).appendTo(jQuery('head'));
          }

          function addStyle(widgetId) {
            var css = RSConfig.css;
            jQuery(
              `<style>

       #appstle_subscription_widget${widgetId}{
              width: 100%;
              text-align: left;
              margin-top: 17px;
              clear: both;
              max-width: 400px;
              ${getCssAsString(css.appstle_subscription_widget)}
          }

          #appstle_subscription_widget${widgetId}{
            ${getCssAsString(css.appstle_widget_text_color)}
          }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper {
                 border: 1.5px solid #cccccc;
                // box-shadow: 0 0 0 1px #c4cdd5;
                border-radius: 5px;
                margin-bottom: 5px;
                margin-top: 10px;
                ${getCssAsString(css.appstle_subscription_wrapper)}
            }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option {
                display: flex;
                position: relative;
                padding: 16px 16px;
            }
            #appstle_subscription_widget${widgetId} .appstle_selected_background {
              ${getCssAsString(css.appstle_selected_background)}
            }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option:first-child {
               // box-shadow: 0 1px 0 0 #c4cdd5;
               border-bottom: inherit;
            }
              #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option:first-child:last-child {
               border-bottom: none;
            }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option:not(.appstle_include_dropdown) {
              align-items: center;
            }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option.appstle_include_dropdown {
              flex-direction: column;
              justify-content: center;
            }


            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option input[type='radio'] {
              display: none;
            }

            #appstle_subscription_widget${widgetId} .appstle_subscribe_option {
                margin-left: 29px;
                margin-top: 25px;
                display: flex;
                    flex-direction: column;
                align-items: flex-start;
                text-align: left;
            }

            #appstle_subscription_widget${widgetId} .appstle_subscription_amount {
                margin-left: auto;
            }

            #appstle_subscription_widget${widgetId} .appstle_circle {
                display: flex;
                height: 18px;
                width: 18px;
                border: 2px solid #3a3a3a;
                border-radius: 50%;
                margin-right: 10px;
                justify-content: center;
                align-items: center;
                flex-shrink: 0;
                ${getCssAsString(css.appstle_circle)}
              }

            #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper_option input[type=radio]:checked + label .appstle_circle .appstle_dot {
                height: 10px;
                width: 10px;
                background-color: #3a3a3a;
                border-radius: 50%;
                flex-shrink: 0;
                ${getCssAsString(css.appstle_dot)}
              }


          #appstle_subscription_widget${widgetId} .appstle_radio_label {
            display: flex !important;
            align-items: center;
            margin: 0;
            padding: 0;
            background: none;
          }

          #appstle_subscription_widget${widgetId} .appstle_select_label {
            display: flex !important;
            align-items: center;
            margin: 0;
            padding: 0;
            background: none;
            margin-bottom: 7px;
            font-size: 12px;

          }

          #appstle_subscription_widget${widgetId} .appstle_tooltip {
            -webkit-backface-visibility: hidden;
            backface-visibility: hidden;
            box-shadow: 0 2px 4px rgb(0 0 0 / 15%);
            background-color: #3a3a3a;
            border-radius: 5px;
            left: 0;
            color: #fff;
            transition: transform .2s cubic-bezier(.215,.61,.355,1);
            -ms-transform: translateY(0);
            transform: translateY(100%) scaleY(0);
            transform-origin: center top;
            opacity: 0;
            position: absolute;
            bottom: 1px;
            // border-bottom-left-radius: 0;
            min-width: 250px;
            ${getCssAsString(css.appstle_tooltip)}
          }

          #appstle_subscription_widget${widgetId} [data-appstle-icon]:hover .appstle_tooltip_hover {
            opacity: 1;
            visibility: visible;
            transform: translateY(100%) scaleY(1);
            bottom: 1px;
            z-index: 99999999;
          }


          #appstle_subscription_widget${widgetId} [data-appstle-icon] {
              -ms-flex-item-align: end;
              backface-visibility: hidden;
          }

          #appstle_subscription_widget${widgetId} [data-appstle-icon]:after {
                border: solid transparent;
                border-top-color: #3a3a3a;
                border-width: 9px;
                content: '';
                position: absolute;
                pointer-events: none;
                opacity: 0;
                left: 2px;
                bottom: 0;
                -ms-transform: translateX(-50%) translateY(10px) rotate(180deg);
                transform: translateY(10px) rotate(180deg);
                transition-delay: 0;
                visibility: hidden;
                transition: transform .2s cubic-bezier(.215,.61,.355,1);
                z-index: 99999999;
                background: none;
                padding: 0;
                ${getCssAsString(css.appstle_tooltip_border_top_color)}
          }

          #appstle_subscription_widget${widgetId} [data-appstle-icon]:hover:after {
            opacity: 1;
            visibility: visible;
            -ms-transform: translateX(-50%) translateY(0) rotate(180deg);
            transform: translateY(0) rotate(180deg);
          }

          #appstle_subscription_widget${widgetId} .appstle_tooltip_wrapper {
            position: relative;
            margin-bottom: 6px;
            display: inline-flex;
            align-items: center;
            padding-top: 11px;
            box-sizing: border-box;
            margin-left: 0px;
            padding-bottom: 11px;
          }

          #appstle_subscription_widget${widgetId} .appstle_tooltip_wrapper_static {
            font-size: 13px;
            background-color: beige;
            border-radius: 6px;
            padding: 23px;
            margin-bottom: 10px;
            ${getCssAsString(css.appstle_tooltip)}
          }

          #appstle_subscription_widget${widgetId} .tooltip_subscription_svg {
               height: 22px;
              width: 22px;
              margin-right: 10px;
              fill: black;
              ${getCssAsString(css.tooltip_subscription_svg)}
          }

          #appstle_subscription_widget${widgetId} .appstle_subscription_radio_wrapper {
              display: flex;
              align-items: flex-start;
          }

          #appstle_subscription_widget${widgetId} .appstle_subscription_amount_wrapper {
            margin-left: auto;
            text-align: right;
          }

          #appstle_subscription_widget${widgetId} .appstle_subscription_amount_wrapper .appstle_prepaid_description {
            font-size: 12px;
            opacity: 0.8;
          }

          .appstle_subscription_final_price {
              margin-right: 10px;
              text-decoration: none;
              color: #da4f49;
              ${getCssAsString(css.appstle_subscription_final_price)}
          }

          .appstle_subscription_save {
              margin-left: 10px;
                padding: 2px 6px;
                border: 1px solid #da4f49;
                border-radius: 3px;
                font-size: 10px;
                color: #da4f49;
                font-weight: bold;
                display: inline;
                position: relative;
                top: -3px;
                letter-spacing: 1px;
          }

          #appstle_subscription_widget${widgetId} .appstle_subscription_wrapper .appstle_select {
              width: 100%;
              margin: 0;
              ${getCssAsString(css.appstle_select)}
          }

          #appstle_subscription_widget${widgetId} .appstle_hide_subsOption {
            opacity: 0;
            margin-top: 0;
            visibility: hidden;
            height: 0;
          }

          #appstle_subscription_widget${widgetId} .appstle_tooltip_content {
            background-color: #3a3a3a;
            padding: 16px;
          }

          body #appstle_subscription_widget${widgetId} .appstle_tooltip_wrapper .appstle_tooltip .appstle_tooltip_appstle {
            padding: 12px !important;
            font-size: 8px !important;
            letter-spacing: 2px !important;
            text-align: right !important;
            background: #13b5ea !important;
            border-bottom-left-radius: 4px !important;
            border-bottom-right-radius: 4px !important;
            display: block !important;
          }

          .appstle_link {
            color: inherit !important;
            text-decoration: none !important;
            cursor: pointer !important;
          }

          .appstleRadioSellingPlanWrapper_CHG {
            display: flex;
            flex-direction: column;
          }


  /*Akash*/
  .widgetSellingPlanWrapper {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 0.5rem;
      grid-auto-rows: 1fr;
      margin-bottom: 6px;
  }

  .widgetSellingPlanWrapper input {
      display: none;
  }

  .widgetSellingPlanWrapper label {
      border-radius: 8px;
      border: 1px solid #E60000;
      padding: 0.5rem;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 13px;
      display: block;
      height: 100%;
      width: 100%;
      text-align: center;
  }

  .widgetSellingPlanWrapper input:checked+label {
      background-color: #FCDFDF;
  }
  .appstle_lowercase {
      text-transform: lowercase;
  }


  .appstle_input_wrapper {
      position: relative;
  }

  span.appstle_sellingPlan_discountText {
      position: absolute;
      background-color: #724cf9;
      color: white;
      border-radius: 3px;
      padding: 1px 18px;
      left: 50%;
      display: block;
      transform: translate(-50%, -50%);
      white-space: nowrap;
  }

  span.appstle_sellingPlan_discountText:empty {
      display: none;
  }

  span.appstle_lowercase.appstle_sellingPlan_price {
      font-size: 13px;
      font-weight: normal;
      display: block;
      color: red;
  }

  #appstle_subscription_widget0 .appstle_tooltip_wrapper {
      font-size: 13px;
  }


          </style>
          <style>${css.customCSS}</style>`
            ).appendTo(jQuery('head'));
          }
        }

        function renderStandAloneWidget() {
          var standaloneElements = Array.prototype.slice.call(jQuery(".appstle_stand_alone_selector"));
            let index = -1
            function attatchWidgetToStandAloneElement() {
                if (standaloneElements?.length) {
                  let standAloneElement = standaloneElements.shift();
                  let product = $(standAloneElement).data("product-data");
                  index = index + 1;
                  if(!jQuery(standAloneElement).parents('form').find('.appstle_sub_widget').length) {
                    if (!product) {
                      fetch(location.origin + `/products/${jQuery(standAloneElement).data("product-handle")}.js`)
                      .then((response) => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! Status: ${ response.status }`);
                        }
                        return response.json();
                        })
                      .then((productHandleData) => {
                          jQuery(standAloneElement).attr("data-product-data", productHandleData);
                          renderWidget(productHandleData, $(standAloneElement), index);
                          attatchWidgetToStandAloneElement();
                        })
                      .catch((err) => {
                        console.log(err)
                      })
                    } else {
                        renderWidget(product, $(standAloneElement), index);
                        attatchWidgetToStandAloneElement();
                      }
                    } else {
                     attatchWidgetToStandAloneElement();
                    }

                }
            }
            attatchWidgetToStandAloneElement()
        }

        function urlIsProductPage() {
          // return null != decodeURIComponent(window.location.pathname).match(/\/products\/(([a-zA-Z0-9]|[\-\.\_\~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[\ud83c\ud83d\ud83e][\ud000-\udfff]){1,})\/?/)
          return decodeURIComponent(window.location.pathname).includes('/products/');
        }

        jQuery(".appstle_sub_widget").remove();
        // Init appstle render widget
        if (appstleStandAloneSelectorExists()) {
          if(window?.RS?.Config?.appstlePlanName?.toLowerCase()?.includes("enterprise") || window?.RS?.Config?.appstlePlanName?.toLowerCase()?.includes("business")
          || jQuery('.appstle_stand_alone_selector')?.parents('form')?.attr('class')?.includes('pf') || urlIsProductPage()) {
            renderStandAloneWidget();
          }
          else {
            console.log("you are not in business plan or enterprise plan. please switch the plan")
          }
        } else {
          renderWidget(undefined, undefined, 0);
        }

              // Code For Quick View Page (It will load on click of quickview page)  It's compulsory to have a standalone script otherwise it won't work
              jQuery(`${window?.RS?.Config?.quickViewClickSelector}` || "a.grid-product__link").on("click",function(){

                if(jQuery(`${window?.RS?.Config?.quickViewClickSelector}` || "a.grid-product__link").length > 0)
                {
                  if(window?.RS?.Config?.appstlePlanName?.toLowerCase()?.includes("enterprise") || window?.RS?.Config?.appstlePlanName?.toLowerCase()?.includes("business"))
                    {
                      var initiateQuickView = function() {
                        if (appstleStandAloneSelectorExists()) {
                          renderStandAloneWidget();
                      } else {
                          if(jQuery("#appstle_subscription_widget0")?.length == 0)
                          {
                            renderWidget(undefined, undefined, 0);
                          }
                        }
                      }

                      if(`${window?.RS?.Config?.quickViewModalPollingSelector}` || window?.Shopify?.shop == 'mahalia-coffee.myshopify.com')
                      {
                        var pollQuickView = function() {
                          if (jQuery(".quickshop.quickshop-visible.quickshop-loaded").length || jQuery(`${window?.RS?.Config?.quickViewModalPollingSelector}`).length) {
                            initiateQuickView()
                          } else {
                            setTimeout(()=> {
                              pollQuickView();
                            },200)
                          }
                        }
                        pollQuickView();
                      }
                      else {
                        setTimeout(()=> {
                          initiateQuickView()
                        }, 500)
                      }
                  }
                }
              })

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

          function htmlDecode(input) {
            var doc = new DOMParser().parseFromString(input, "text/html");
            return doc.documentElement.textContent;
          }

          var formatString = '';

          if (RS.Config?.formatMoneyOverride === 'true') {
            formatString = RS.Config.moneyFormat;
          } else {
            formatString = window?.shopifyCurrencyFormat || window?.moneyFormat || window?.Currency?.money_format_no_currency || themeMoneyFormat || RSConfig?.shopMoneyFormat || moneyFormat  || shopifyMoneyFormat || htmlDecode(RSConfig.shopMoneyFormatWithCurrencyFormat);
          }

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

        // collection page price selector logic
        function displaySubscriptionPrice() {
          if (appstlePriceDisplaySelectorExists()) {
            addFeaturePageCSS();
            jQuery(".appstle_stand_alone_price_display_selector").each(function (index) {
              let product = $(this).data("product-data");

              let subscription_price = Number.MAX_SAFE_INTEGER;
              let has_subscription_price = false

              product?.variants?.forEach(variant => {
                variant?.selling_plan_allocations?.forEach(allocation => {
                  if (allocation?.price_adjustments[0]?.price < subscription_price) {
                    subscription_price = allocation?.price_adjustments[0]?.price
                    has_subscription_price = true;
                  }
                })
              })

              if (has_subscription_price) {
                $(this).siblings(RSConfig?.landingPagePriceSelector || RS?.Config?.landingPagePriceSelector || "span.price, span.appstle_hide_old_price, .price").hide(); // hide old price from home page
                $(this).html(wrapPriceWithSpanTag(buildSubscribeSaveSelector(subscription_price)));
              }
              $(this).show();
            })
          }
        }
        setTimeout(displaySubscriptionPrice, 30);

        function addFeaturePageCSS() {
          let cssFeaturedProduct = RSConfig?.css;
          jQuery(`<style>.appstle_subscribesavetext{
                    background-color:#c00303;
                    color:#fff;
                    padding:4px 8px;
                    font-size:13px;
                      }</style>
                  <style>${cssFeaturedProduct?.customCSS}</style>`).appendTo(jQuery('head'));
        }

        function buildSubscribeSaveSelector(subscription_price) {
          let subscribeSaveModel = {
            subscriptionPrice: formatPrice(subscription_price)
          };

          return wrapPriceWithSpanTag(Mustache.render(RSConfig?.subscriptionPriceDisplayText, subscribeSaveModel));
        }

        function appstleStandAloneSelectorExists() {
          return jQuery(".appstle_stand_alone_selector").length > 0
        }

        function appstlePriceDisplaySelectorExists() {
          return jQuery(".appstle_stand_alone_price_display_selector").length > 0
        }

        function appstleSubscriptionCustomerPortalInit(selector) {
          selector = selector || "#AppstleCustomerPortal";
          if (document.querySelector(selector)) {
              return jQuery.get(`/${RSConfig?.manageSubscriptionUrl}?renderType=html`)
              .then(result => {
                jQuery(selector).html(result);
                document.dispatchEvent( new Event('AppstleSubscription:CustomerPortal:Embedded') );
                window.dispatchEvent( new Event('AppstleSubscription:CustomerPortal:Embedded') );
              })
              .catch(error => console.log('error', error));
          }
        }

        window.appstleSubscriptionCustomerPortalInit = appstleSubscriptionCustomerPortalInit;
        appstleSubscriptionCustomerPortalInit();

        function fetchAddJsSellingPlanInterCeptor() {
          const { fetch: originalFetch } = window;

          jQuery.ajaxSetup({
                beforeSend: function (xhr,settings) {
                    console.log(xhr);
                    console.log(settings.data);
                    console.log(settings.url);
                    if(settings?.url === "/cart/add.js")
                    {
                      var sellingPlanID = $('form input[name=selling_plan]:checked').val()
                      if(sellingPlanID)
                      {
                        settings.data = settings.data + `&selling_plan=${sellingPlanID}`
                      }
                    }
                }
            });

            (function(send) {
              XMLHttpRequest.prototype.send = function(data) {
                if (this["_url"] === "/cart/add.js") {
                  var currentBodyObj = null;
                  var sellingPlan = $('form input[name=selling_plan]:checked').val()
                  if (sellingPlan) {
                    try {
                      if (data.substr(0,1) === "{" && data.substr(-1) === "}") {
                        currentBodyObj = JSON.parse(data);
                        currentBodyObj.selling_plan = sellingPlan
                        send.call(this, JSON.stringify(currentBodyObj));
                      } else if (data.substr(0,1) !== "{" && data.substr(-1) !== "}" && data.indexOf("&") !== -1) {
                        send.call(this, data + `&selling_plan=${sellingPlan}`);
                      } else {
                        send.call(this, data);
                      }
                    } catch (e) {
                      send.call(this, data);
                    }
                  } else {
                    send.call(this, data);
                  }
                } else {
                  send.call(this, data);
                }
              };
            })(XMLHttpRequest.prototype.send);

            window.fetch = function() {
                var resource = arguments[0];
                var config = arguments[1];

                if(resource === "/cart/add.js")
                {
                  var currentVariantId = urlParams.get('variant')
                  var newBodyArray = {}
                  var currentBodyArray = JSON.parse(config?.body);
                  if(getClassOf(currentBodyArray) === "Object" && getClassOf(currentBodyArray?.items) == "Array")
                  {
                      var sellingPlanVariantIndex = currentBodyArray?.items?.findIndex(data => data.id == currentVariantId);
                      if(sellingPlanVariantIndex >= 0)
                      {

                            var filteredVariant = currentBodyArray?.items?.filter(data => data.id != currentVariantId);
                            var sellingPlanIncludedVariant = JSON.parse(JSON.stringify(currentBodyArray?.items[sellingPlanVariantIndex]));
                            sellingPlanIncludedVariant[ "selling_plan"] = $('.appstle_sub_widget input[name=selling_plan]:checked').val();

                            filteredVariant.push(sellingPlanIncludedVariant);
                            newBodyArray.items = JSON.parse(JSON.stringify(filteredVariant));
                            config.body = JSON.stringify(newBodyArray);
                            return originalFetch.apply(this, arguments);
                      }
                      else {
                           return originalFetch.apply(this, arguments);
                      }
                  }
                  else if (getClassOf(currentBodyArray) === "Object") {
                    var currentBodyObj  = JSON.parse(config?.body);
                    currentBodyObj.selling_plan = $('.appstle_sub_widget input[name=selling_plan]:checked').val()
                    config.body = JSON.stringify(currentBodyObj);
                    return originalFetch.apply(this, arguments);
                  }
                }
                else {
                  return originalFetch.apply(this, arguments);
                }
            };
        }

        function getClassOf(obj) {
          return Object.prototype.toString.call(obj).slice(8, -1);
      }
              function getSellingPlanDetails(selectedSellingPlan) {
                      return RSConfig?.sellingPlansJson?.find(function(item) {return item?.id.split("/").pop() === String(selectedSellingPlan)});
              }

        // standalone selector only one or product page and no cart page.
        if(false && (jQuery(".appstle_stand_alone_selector")?.length === 1 || urlIsProductPage() === true))
        {
          fetchAddJsSellingPlanInterCeptor()
        }

        //============================ CART PAGE WIDGET START HERE  =========================================================
        if (window?.RS?.Config?.enableCartWidgetFeature !== "true") {
          return;
        }
        $(function () {
          //global variable
          var cartRowSelector = window?.RS?.Config?.selectors.cartRowSelector;
          var cartLineItemSelector = window?.RS?.Config?.selectors.cartLineItemSelector;
          var appstleCustomSelector = "[data-appstle-selector]";
          var cartLineItemPerQuantityPriceSelector = window?.RS?.Config?.selectors.cartLineItemPerQuantityPriceSelector;
          var cartLineItemTotalPriceSelector = window?.RS?.Config?.selectors.cartLineItemTotalPriceSelector;
          var lineItemSellingPlanNameSelector = window?.RS?.Config?.selectors.cartLineItemSellingPlanNameSelector;
          var cartSubTotalSelector =  window?.RS?.Config?.selectors.cartSubTotalSelector;
          var cartLineItemPriceSelector = window?.RS?.Config?.selectors.cartLineItemPriceSelector;

          var cartPageSellingPlan = [];
          var totalSubscriptionPlanOnCartPage = 0;
          var prevChangeResponse = "";
          function appstleCartPageSubscription() {
            if (!jQuery("#appstle_overlay").length) {
              addLoader();
            }
            removeEventListners();
           checkingSellingPlan();
          }

          function appstleListenForDOMChanges() {
            var elementId = "";
            if (jQuery("main").attr("id")) {
              elementId = jQuery("main").attr("id");
            } else {
              jQuery("main").attr("id", "appstleCustomId");
              elementId = "appstleCustomId";
            }
            const targetNode = document.getElementById(elementId);
            const config = { attributes: true, childList: true, subtree: true };
            const callback = function() {
              if (!jQuery(cartLineItemSelector + ".appstleCartWidgetProcessed").length && jQuery(cartRowSelector).length) {
                appstleCartPageSubscription();
              }
            };
            const observer = new MutationObserver(callback);
            observer.observe(targetNode, config);
          }

          if (location.pathname === '/cart') {

                      var origOpen = window.XMLHttpRequest.prototype.open;
                      window.XMLHttpRequest.prototype.open = function() {
                      this.addEventListener('load', function() {
                        var url = this.responseURL;
                        console.log("origOpe.n -> url=" + url);
                        if (url.indexOf("cart/change.js") !== -1) {
                          if (prevChangeResponse !== JSON.stringify(this.response)) {
                               if (!jQuery("#appstle_overlay").length) {
                                  addLoader();
                              }
                              setTimeout(appstleCartPageSubscription, 100);
                              prevChangeResponse = JSON.stringify(this.response);
                          }

                      }
                      });
                      origOpen.apply(this, arguments);
                    };

                  const originalFetch = window.fetch;
                    window.fetch = function() {
                      return new Promise((resolve, reject) => {
                        originalFetch.apply(this, arguments)
                          .then((response) => {
                            var url = response.url;
                            if (url.indexOf("cart/change.js") !== -1) {

                              if (!jQuery("#appstle_overlay").length) {
                                  addLoader();
                              }
                              setTimeout(appstleCartPageSubscription, 100);
                            }

                            resolve(response);
                          })
                          .catch((error) => {
                            console.error(error);
                          })
                      });
                    }
            appstleCartPageSubscription();
            addCartPageCSS();
            appstleListenForDOMChanges();

          }

          function addLoader(text) {
              if (!jQuery("#appstle_overlay").length) {
                jQuery('body').append("<div id='appstle_overlay'><div class='appstle_loader'></div><div class='appstle_loader_text'>" + (text || " ") + "</div></div></div>");
              }
          }


          function removeEventListners() {
              jQuery('.appstle_subscription_cart_wrapper select').each((idx, item) => {
                 jQuery(item).off()
              });
              jQuery(".appstle_subscribe_selected input").each((idx, item) => {
                  jQuery(item).off()
              });
              jQuery(".appstle_subscribe_title").each((idx, item) => {
                  jQuery(item).off()
              });
              jQuery(".appstle_selected_frequency").each((idx, item) => {
                  jQuery(item).off()
              });
          }



          function checkingSellingPlan(sellingPlanIdToBeSet, removeSellingPlan) {
            fetchCartData()
            .then((cartResponse) => cartResponse.json())
            .then((cartDataArray) => {
              var hasSubscriptionPlan;
              var handleURL = []
              var productDataList = []
              totalSubscriptionPlanOnCartPage = 0;
              function updateCartLineItems() {
                cartDataArray.items.forEach(function(cartItem, index) {
                  var productData = (productDataList.filter(prdData => prdData.handle === cartItem.handle)).pop();
                  var sellingPlanIsAvailable = checkIfSellingPlanIsAvailable(JSON.parse(JSON.stringify(productData)), cartItem?.variant_id);
                  var sellingPlanId = cartItem?.selling_plan_allocation?.selling_plan?.id;
                  if (sellingPlanIsAvailable) {
                  var appstle_cartpage_widget = jQuery(`<div class="appstle_subscription_cart_wrapper">
                          <div class="appstle_subscribe_title">
                              <input type="checkbox"></input>
                              <span class="appstle_discount_text"><span class="appstle_highest_discount">${window?.RS?.Config?.subscriptionOptionText}</span></span>
                          </div>
                          <div class="appstle_subscribe_selected_wrapper" style="display: none;">
                              <div class="appstle_subscribe_selected">
                                <input type="checkbox" checked ${productData?.requires_selling_plan ? 'disabled' : ''}></input>
                                <div class="appstle_subscribe_selected_text">${window?.RS?.Config?.subscriptionOptionText} applied.</div>
                              </div>
                              <div class="appstle_selected_frequency">
                                  <span class="appstle_deliveryText">{{deliveryText}}</span>&nbsp;
                                  <span class="appstle_applied_sellingPlanName">{{appliedSellingPlanName}}</span>
                              </div>
                          </div>
                          <div class="appstle_radio_section" style="display: none">

                              <select id="appstle_selling_plan_cart" data-product-select-id="${JSON.parse(JSON.stringify(productData)).id}-${sellingPlanId}" class="appstle_select_cart"></select>
                          </div>
                      </div>`);





                  populateSellingPlanDropdownOnCart(JSON.parse(JSON.stringify(productData)), appstle_cartpage_widget, sellingPlanId, JSON.parse(JSON.stringify(productData.variants.filter(item => item.id === cartItem.id).pop())))
                  jQuery(appstle_cartpage_widget).find('select').on('change', function (event) {
                       updateCartProductWithSelectedSellingPlan(cartDataArray?.items, cartItem, productData.id, event.target.value, false);
                      // window.location.reload();
                  });

                  jQuery(appstle_cartpage_widget).find('.appstle_subscribe_selected input').on('change', function (event) {
                      updateCartProductWithSelectedSellingPlan(cartDataArray?.items, cartItem, productData.id, jQuery(appstle_cartpage_widget).find('select').val(), true);
                      // window.location.reload();

                  });

                   jQuery(appstle_cartpage_widget).find(".appstle_subscribe_title, .appstle_selected_frequency").on("click", function() {
                     $(this).hide();
                     $(this).parents(".appstle_subscription_cart_wrapper").find('.appstle_radio_section').show();
                   })

                  var lineItemNew = jQuery(jQuery(cartRowSelector)[index]);

                  if (sellingPlanId) {
                    appstle_cartpage_widget.find(".appstle_subscribe_title").hide();
                    appstle_cartpage_widget.find(".appstle_subscribe_selected_wrapper").show();
                    lineItemNew.find(lineItemSellingPlanNameSelector).text(cartItem?.selling_plan_allocation?.selling_plan?.name);
                    lineItemNew.find(lineItemSellingPlanNameSelector).hide();
                  } else {
                    lineItemNew.find(lineItemSellingPlanNameSelector).hide();
                  }

                  lineItemNew.find(".appstle_subscription_cart_wrapper").remove();
                  if (lineItemNew.find(cartLineItemSelector).length === 1) {
                      appstle_cartpage_widget.insertBefore(lineItemNew.find(cartLineItemSelector));
                  } else {
                      appstle_cartpage_widget.insertBefore(lineItemNew.find(appstleCustomSelector));
                  }

                  lineItemNew.find(cartLineItemPerQuantityPriceSelector).html(formatPrice(cartItem.final_price))
                  lineItemNew.find(cartLineItemTotalPriceSelector).html(formatPrice(cartItem.final_line_price))

                  if (sellingPlanId && cartItem.selling_plan_allocation.compare_at_price) {
                    lineItemNew.find(cartLineItemPriceSelector).html("");
                    var priceHtml = `<span class="appstle_cart_sellingPlan_price">${formatPrice(cartItem.final_price)}</span><span  class="appstle_cart_compare_price">${formatPrice(cartItem.selling_plan_allocation.compare_at_price)}</span>`
                    lineItemNew.find(cartLineItemPriceSelector).html(priceHtml)
                  }
                }
                jQuery(jQuery(cartRowSelector)[index]).find(cartLineItemSelector).addClass("appstleCartWidgetProcessed");
              });
              jQuery(cartSubTotalSelector).html(formatPrice(cartDataArray?.total_price))
              jQuery("#appstle_overlay").remove();
              }
              function getProductData() {
                if (handleURL.length) {
                  fetch(`/products/${handleURL.shift()}.js`)
                  .then(function(prdData) {
                    return prdData.json();
                  })
                  .then(function(prdData) {
                    productDataList.push(prdData);
                    getProductData();
                  })
                } else {
                  updateCartLineItems();
                  document.dispatchEvent(new Event('AppstleSubscription:CartWidget:Updated'));
                  window.dispatchEvent(new Event('AppstleSubscription:CartWidget:Updated'));
                }
              }
              if (cartDataArray?.items?.length > 0) {
                if (cartDataArray?.items?.length === jQuery(cartRowSelector).length) {
                  cartDataArray?.items.forEach(function(item) {
                    if (handleURL.indexOf(item?.handle) === -1) {
                      handleURL.push(item?.handle)
                    }
                  })
                } else {
                  if(cartRowSelector) {
                    location.reload();
                  }
                }
                getProductData();
              } else {
                jQuery("#appstle_overlay").remove();
              }
            })
          }



        function updateCartProductWithSelectedSellingPlan(items, lineItem, productId, sellingPlanId, removeSellingPlan) {
              if (!sellingPlanId) {
                return;
              }
              if (sellingPlanId === "appstle_unsubscribe") {
                      removeSellingPlan = true;
              }
            addLoader();
            var addPayload = {items: []};

            items.forEach((cartItem) => {
                if (cartItem?.key === lineItem?.key) {
                  var payloadData = {
                  'id': cartItem?.variant_id,
                  'quantity': cartItem?.quantity
                  }

                  if (!removeSellingPlan) {
                    payloadData["selling_plan"] = sellingPlanId;
                  }
                  addPayload?.items?.unshift(payloadData)
                 } else {
                  var payloadData = {
                      'id': cartItem?.variant_id,
                      'quantity': cartItem?.quantity
                  }
                  if (cartItem?.selling_plan_allocation?.selling_plan?.id) {
                      payloadData["selling_plan"] = cartItem?.selling_plan_allocation?.selling_plan?.id
                  }
                  addPayload?.items?.unshift(payloadData)
                }
            })

            var config = {
              method: 'POST', // or 'PUT'
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(addPayload)
            }

            // set quanity is 0
           fetch("/cart/clear.js")
           .then((res) => res.json())
           .then(data => {
                   fetch("/cart/add.js", config)
                  .then((res) => res.json())
                  .then(data => {
                      setTimeout(function() {
                        appstleCartPageSubscription();
                      }, 200)
                   })
                  .catch((err) => console.log(err))
              })
              .catch((err) => console.log(err))


            // add same variant with selling plan Id

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
              if (RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags && RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.trim()) {
                var sellingPlanTags = RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberInclusiveTags.split(",");
                var tagFound = compareCustomerTags(userTags, sellingPlanTags);
                isVisible = (tagFound.length > 0);
              }

              if (RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberExclusiveTags && RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberExclusiveTags.trim()) {
                var sellingPlanExclusiveTags = RSConfig.memberOnlySellingPlansJson[sellingPlanId].memberExclusiveTags.split(",");
                var exclusiveTagFound = compareCustomerTags(userTags, sellingPlanExclusiveTags);
                isVisible = !(exclusiveTagFound.length > 0);
              }
            }

            return isVisible;
          }

          function buildSellingPlantText(sellingPlan) {
            var sellingPlanModel = sellingPlan.totalPrice == sellingPlan.formattedPrice ?
              {
                sellingPlanName: sellingPlan.name,
                sellingPlanPrice: `<span class="transcy-money">${sellingPlan.formattedPrice}</span>`,
                secondSellingPlanPrice: `<span class="transcy-money">${sellingPlan.secondFormattedPrice}</span>`,
              }
              : {
                sellingPlanName: sellingPlan.name,
                totalPrice: `<span class="transcy-money">${sellingPlan?.totalPrice}</span>`,
                sellingPlanPrice: `<span class="transcy-money">${sellingPlan.formattedPrice}</span>`,
                secondSellingPlanPrice: `<span class="transcy-money">${sellingPlan.secondFormattedPrice}</span>`
              };

            var sellingPlanDisplayText = Mustache.render(RS.Config.sellingPlanTitleText, sellingPlanModel);
            return wrapPriceWithSpanTag(sellingPlanDisplayText);
          }


          function populateSellingPlanDropdownOnCart(product, selectItem, selectedSellingPlan, variant) {

            var sellingPlanVariants = [];
            jQuery.each(product.selling_plan_groups, function (index, sellingPlanGroup) {
              if (sellingPlanGroup.app_id === 'appstle') {
                jQuery.each(sellingPlanGroup.selling_plans, function (subIndex, sellingPlan) {
                  var visible = isSellingPlanVisible(sellingPlan.id);
                  if (visible) {
                    var sellingPlanAllocation = variant.selling_plan_allocations.find(function (plan) {
                                          return plan.selling_plan_id === sellingPlan.id;
                     });
                    if (!sellingPlanAllocation) {
                      return
                    }
                    var price = sellingPlanAllocation.per_delivery_price;
                    var totalPrice = formatPrice(sellingPlanAllocation?.price);
                    var formattedPrice = formatPrice(price);

                    var secondPrice = null;
                    var secondFormattedPrice = null;

                    if (sellingPlanAllocation
                      && sellingPlanAllocation.price_adjustments
                      && sellingPlanAllocation.price_adjustments.length === 2) {
                      secondPrice = sellingPlanAllocation.price_adjustments[1].price;
                      secondFormattedPrice = formatPrice(secondPrice);
                    } else {
                      secondPrice = price;
                      secondFormattedPrice = formattedPrice;
                    }

                    var price_adjustments = sellingPlan.price_adjustments.shift();

                    sellingPlanVariants.push({
                      "name": sellingPlan.name,
                      "id": sellingPlan.id,
                      "formattedPrice": formattedPrice,
                      "price": price,
                      "totalPrice": totalPrice,
                      "secondPrice": secondPrice,
                      "secondFormattedPrice": secondFormattedPrice,
                      "discount": price_adjustments ? (price_adjustments.value_type === 'percentage' ? `${price_adjustments.value}%` : formatPrice(price_adjustments.value)) : '',
                      "deliveryText": sellingPlan.options[0].name
                    });
                  }
                });
              }
            });
            if (sellingPlanVariants.length > 0) {
              sellingPlanVariants.sort(function (sellingPlanA, sellingPlanB) {
                return sellingPlanA.price - sellingPlanB.price;
              })

                jQuery('<option />', {
                    html: "Select Delivery Option",
                    value: ""
                  }).appendTo(selectItem.find('select'));

              jQuery(sellingPlanVariants).each(function (index, sellingPlan) {

                var sellingPlanDisplayText = buildSellingPlantText(sellingPlan);

                jQuery('<option />', {
                  value: sellingPlan.id,
                  html: sellingPlanDisplayText,
                  selected: selectedSellingPlan === sellingPlan?.id
                }).appendTo(selectItem.find('select'));
              });
              if (!product?.requires_selling_plan) {
                jQuery('<option />', {
                  value: "appstle_unsubscribe",
                  html: "Unsubscribe"
                }).appendTo(selectItem.find('select'));
              }

              var selectedSellingPlanItem = sellingPlanVariants.filter((item) => item?.id === selectedSellingPlan).pop()
              if (selectedSellingPlanItem) {
                  var appstle_deliveryText = selectItem.find(".appstle_selected_frequency .appstle_deliveryText").text().split("{{deliveryText}}").join(selectedSellingPlanItem.deliveryText || "")
                  selectItem.find(".appstle_deliveryText").html(appstle_deliveryText);
                  var appstle_applied_sellingPlanName = selectItem.find(".appstle_selected_frequency .appstle_applied_sellingPlanName").text().split("{{appliedSellingPlanName}}").join(selectedSellingPlanItem.name)
                  selectItem.find(".appstle_applied_sellingPlanName").html(appstle_applied_sellingPlanName);
                  var appstle_subscribe_selected_text = selectItem.find(".appstle_subscribe_selected_text").text().split("{{{discountValue}}}").join(selectedSellingPlanItem.discount)
                  selectItem.find(".appstle_subscribe_selected_text").html(appstle_subscribe_selected_text);
              } else {
                      var appstle_highest_discount_text = selectItem.find(".appstle_highest_discount").text().split("{{{discountValue}}}").join(sellingPlanVariants[0].discount)
                      selectItem.find(".appstle_highest_discount").html(appstle_highest_discount_text);
              }


            }
            return sellingPlanVariants;

          }

          function checkIfSellingPlanIsAvailable(productData, variantId) {
            var variant = (productData?.variants.filter(function(item) {
              return item.id === variantId
            })).pop()
            if (variant && variant?.selling_plan_allocations && variant?.selling_plan_allocations.length) {
              return true;
            }
            return false;
          }


          function fetchCartData() {
            return fetch('/cart.js');
          }

          function addCartPageCSS() {
            var cssFeaturedProduct = RSConfig?.css;
            jQuery(`<style>
                   .appstle-subsccribe-cart-discount{
                         margin-left: 2px;
                   }

                  .appstle_cart_radio_selected .appstle_cart_selling_plan_dropdown{
                      display: block;
                  }
              .appstle_cart_radio_selected {
                    background-color: #eee;
                   border-radius: 6px;
              }
              .appstle-cart-radio-label{
                margin-left: 10px;
              }
              .appstle_subscription_cart_wrapper{
                max-width: 100%;
                justify-content: flex-start;
                flex-direction: column;
                font-size: 13px;
              }
                .appstle_cart_radio_label {
                  margin-left: 10px;
                  font-weight: bold;
                }

                .appstle_radio_section{
                }
                .appstle_subscribe_title {
                        cursor: pointer;
                        font-size: 13px;
                        display: flex;
                        align-items: center;
                        line-height: 1.5;
                        margin-bottom: 0.2rem;

                }
                .appstle_subscribe_title .appstle_highest_discount, .appstle_applied_sellingPlanName {
                        border-bottom: 1px solid currentColor;
                }

                .appstle_subscribe_title input {
                        margin-right: 5px;
                }

                .appstle_subscribe_selected {
                        display: flex;
                        align-items: center;
                        margin-bottom: 0.2rem;

                }

                .appstle_subscribe_selected input {
                        margin-right: 5px;
                }

                .appstle_selected_frequency  {
                       display: inline;
                       margin-bottom: 0.2rem;
                       cursor: pointer;
                }

                .appstle_selected_frequency .appstle_applied_sellingPlanName {
                         border-bottom: 1px solid currentColor;
                }

                .appstle_highest_discount, .appstle_applied_sellingPlanName {
                        color: #198946;
                }

                #appstle_overlay {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background: rgba(0, 0, 0, 0.7);
    z-index: 999999;
    text-align: center;
   }

  .appstle_loader {
    border: 6px solid rgba(255, 255, 255, 0.2);
    position: absolute;
    left: 50%;
    top: 50%;
    margin-top: -36px;
    margin-left: -36px;
    border-top: 6px solid #fff;
    border-radius: 50%;
    width: 60px;
    height: 60px;
    animation: appstle_loading_spin 0.7s linear infinite; }

    .appstle_loader_text {
      position: absolute;
      left: 50%;
      top: calc(50% + 60px);
      transform: translate(-50%, -50%);
      color: white;
    }

    .appstle_subscribe_selected input[type=checkbox] {
        height: inherit;
    }

    .appstle_cart_compare_price {
      text-decoration: line-through;
      font-size: 13px;
      margin-left: 5px;
      opacity: 0.85;
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
            <style>${cssFeaturedProduct?.customCSS}</style>`).appendTo(jQuery('head'));
          }
        })
  //============================ CART PAGE WIDGET END HERE  =========================================================

      });
    });

  };
  appstleInit();

  var template = `
  <div class="appstle_sub_widget" id="appstle_subscription_widget{{widgetId}}">
        <div class="widgetSellingPlanWrapper">
          <div class="appstle_input_wrapper">
            <input type="radio" id="appstle_selling_plan_label_1{{widgetId}}" name="selling_plan" value="" class="appstleSellingPlan{{widgetId}}" checked/>
            <label for="appstle_selling_plan_label_1{{widgetId}}">
              {{oneTimePuchaseText}}
              <span  class="appstle_lowercase appstle_sellingPlan_price">{{oneTimePuchaseAmount}} (full price)</span>
            </label>

          </div>
          {{#sellingPlanVariants}}
             <div class="appstle_input_wrapper">
               <input type="radio" class="appstleSellingPlan{{widgetId}}" id="{{sellingPlanId}}" value="{{sellingPlanId}}" name="selling_plan" />
               <label for="{{sellingPlanId}}">
                  Every {{frequencyCount}} {{frequencyIntervalLowerCase}}{{#isFrequencySubsequent}}s{{/isFrequencySubsequent}}
                  <span class="appstle_lowercase appstle_sellingPlan_price">{{formattedPrice}} ({{secondFormattedDiscountText}})
                  </span>
               </label>

             </div>
          {{/sellingPlanVariants}}
          </div>
          <div data-appstle-icon="" class="appstle_tooltip_wrapper">
        <svg width="90" height="90" viewBox="0 0 90 90" xmlns="http://www.w3.org/2000/svg" class="tooltip_subscription_svg">
          <path
            d="M45 0C20.1827 0 0 20.1827 0 45C0 69.8173 20.1827 90 45 90C69.8173 90 90 69.8174 90 45C90.0056 44.6025 89.9322 44.2078 89.7839 43.8389C89.6357 43.47 89.4156 43.1342 89.1365 42.8511C88.8573 42.568 88.5247 42.3432 88.158 42.1897C87.7912 42.0363 87.3976 41.9573 87 41.9573C86.6024 41.9573 86.2088 42.0363 85.842 42.1897C85.4753 42.3432 85.1427 42.568 84.8635 42.8511C84.5844 43.1342 84.3643 43.47 84.2161 43.8389C84.0678 44.2078 83.9944 44.6025 84 45C84 66.5748 66.5747 84 45 84C23.4253 84 6 66.5747 6 45C6 23.4254 23.4253 6 45 6C56.1538 6 66.3012 10.5882 73.4375 18H65.4062C65.0087 17.9944 64.614 18.0678 64.2451 18.2161C63.8762 18.3643 63.5405 18.5844 63.2573 18.8635C62.9742 19.1427 62.7494 19.4753 62.596 19.842C62.4425 20.2088 62.3635 20.6024 62.3635 21C62.3635 21.3976 62.4425 21.7912 62.596 22.158C62.7494 22.5247 62.9742 22.8573 63.2573 23.1365C63.5405 23.4156 63.8762 23.6357 64.2451 23.7839C64.614 23.9322 65.0087 24.0056 65.4062 24H79.8125C80.6081 23.9999 81.3711 23.6838 81.9337 23.1212C82.4963 22.5586 82.8124 21.7956 82.8125 21V6.59375C82.821 6.18925 82.7476 5.78722 82.5966 5.41183C82.4457 5.03644 82.2205 4.69545 81.9344 4.40936C81.6483 4.12327 81.3073 3.898 80.9319 3.7471C80.5565 3.5962 80.1545 3.52277 79.75 3.53125C79.356 3.53941 78.9675 3.62511 78.6067 3.78344C78.2458 3.94177 77.9197 4.16963 77.6469 4.45402C77.3741 4.73841 77.16 5.07375 77.0168 5.44089C76.8737 5.80803 76.8042 6.19977 76.8125 6.59375V12.875C68.6156 4.86282 57.3081 0 45 0ZM43.75 20.75C43.356 20.7582 42.9675 20.8439 42.6067 21.0022C42.2458 21.1605 41.9197 21.3884 41.6469 21.6728C41.3741 21.9572 41.16 22.2925 41.0168 22.6596C40.8737 23.0268 40.8042 23.4185 40.8125 23.8125V47.375C40.8116 47.7693 40.8883 48.16 41.0385 48.5246C41.1886 48.8892 41.4092 49.2207 41.6875 49.5L54.0938 61.9375C54.6573 62.5011 55.4217 62.8177 56.2188 62.8177C57.0158 62.8177 57.7802 62.5011 58.3438 61.9375C58.9073 61.3739 59.224 60.6095 59.224 59.8125C59.224 59.0155 58.9073 58.2511 58.3438 57.6875L46.8125 46.1875V23.8125C46.821 23.408 46.7476 23.006 46.5966 22.6306C46.4457 22.2552 46.2205 21.9142 45.9344 21.6281C45.6483 21.342 45.3073 21.1168 44.9319 20.9658C44.5565 20.8149 44.1545 20.7415 43.75 20.75Z"
          ></path>
        </svg>
        <span class="appstle_tooltip_title">Subscription Details</span>
        <div class="appstle_tooltip appstle_tooltip_hover">
          <div class="appstle_tooltip_content">
            {{{toolTipDescription}}}
          </div>
          <div class="appstle_tooltip_appstle">
            <a href="{{companyWebsite}}" class="appstle_link" target="_blank">
              {{companyName}}
            </a>
          </div>
        </div>
      </div>`
