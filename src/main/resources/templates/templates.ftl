Commerceinstruments = window.Commerceinstruments || {};
Commerceinstruments.templates = {
    StoreName: '${store_name}',
<#if AutocompleteItem?has_content>
    AutocompleteItem: '${AutocompleteItem}',
</#if>
<#if AutocompleteStyle?has_content>
    AutocompleteStyle: '${AutocompleteStyle}',
</#if>
<#if AutocompleteShowSeparator?has_content>
    AutocompleteShowSeparator: '${AutocompleteShowSeparator}',
</#if>
<#if AutocompleteLayout?has_content>
    AutocompleteLayout: '${AutocompleteLayout}',
</#if>
<#if AutocompleteShowMoreLink?has_content>
    AutocompleteShowMoreLink: '${AutocompleteShowMoreLink}',
</#if>
<#if AutocompleteIsMulticolumn?has_content>
    AutocompleteIsMulticolumn: '${AutocompleteIsMulticolumn}',
</#if>
<#if AutocompleteTemplate?has_content>
    AutocompleteTemplate: '${AutocompleteTemplate}',
</#if>
<#if AutocompleteMobileTemplate?has_content>
    AutocompleteMobileTemplate: '${AutocompleteMobileTemplate}',
</#if>
<#if AutocompleteMobileItem?has_content>
    AutocompleteMobileItem: '${AutocompleteMobileItem}',
</#if>
<#if ColorsCSS?has_content>
    ColorsCSS: '${ColorsCSS}',
</#if>
<#if ResultsStyle?has_content>
    ResultsStyle: '${ResultsStyle}',
</#if>
<#if AutocompleteShow?has_content>
    AutocompleteShow: '${AutocompleteShow}',
</#if>
<#if AutocompleteShowMobileWidget?has_content>
    AutocompleteShowMobileWidget: '${AutocompleteShowMobileWidget}',
</#if>
<#if ResultsShow?has_content>
    ResultsShow: '${ResultsShow}',
</#if>
<#if ResultsUseAsNavigation?has_content>
    ResultsUseAsNavigation: '${ResultsUseAsNavigation}',
</#if>
<#if recommendationsLayout?has_content>
    RecommendationsLayout: {
    <#list recommendationsLayout as key, values>
        "${key}": [
        <#list values as value>
            "${value}"
            <#if !value?is_last>
                ,
            </#if>
        </#list>
        ]
        <#if !key?is_last>
            ,
        </#if>
    </#list>
    },
</#if>
<#if Redirects?has_content>
    Redirects: {
    <#list Redirects as key, values>
        "${key}": [
        <#list values as value>
            "${value}"
            <#if !value?is_last>
                ,
            </#if>
        </#list>
        ]
        <#if !key?is_last>
            ,
        </#if>
    </#list>
    },
</#if>
<#if AutocompleteResultsOrder?has_content>
    AutocompleteResultsOrder: [
    <#list AutocompleteResultsOrder as resultsOrder>
        "${resultsOrder}"
        <#if !resultsOrder?is_last>
            ,
        </#if>
    </#list>
    ],
</#if>
<#if RecommendationShowActionButton?has_content>
    RecommendationShowActionButton: '${RecommendationShowActionButton}',
</#if>
<#if RecommendationCustomCSS?has_content>
    RecommendationCustomCSS: '${RecommendationCustomCSS}',
</#if>
<#if ReviewsShowRating?has_content>
    ReviewsShowRating: '${ReviewsShowRating}',
</#if>
<#if AppmateWishlistKingSupport?has_content>
    AppmateWishlistKingSupport: '${AppmateWishlistKingSupport}',
</#if>
<#if SwymCorporationWishlistPlusSupport?has_content>
    SwymCorporationWishlistPlusSupport: '${SwymCorporationWishlistPlusSupport}',
</#if>
<#if GrowaveWishlistSupport?has_content>
    GrowaveWishlistSupport: '${GrowaveWishlistSupport}',
</#if>
<#if AppSupportCoin?has_content>
    AppSupportCoin: '${AppSupportCoin}',
</#if>
<#if BestCurrencyConverter?has_content>
    BestCurrencyConverter: '${BestCurrencyConverter}',
</#if>
<#if BoldMultiCurrency?has_content>
    BoldMultiCurrency: '${BoldMultiCurrency}',
</#if>
<#if ResultsShowProductDiscountLabel?has_content>
    ResultsShowProductDiscountLabel: '${ResultsShowProductDiscountLabel}',
    LabelResultsProductDiscountText: '${LabelResultsProductDiscountText}',
    ResultsProductDiscountBgcolor: '${ResultsProductDiscountBgcolor}',
</#if>
<#if AutocompleteProductDiscountLabelShow?has_content>
    AutocompleteProductDiscountLabelShow: '${AutocompleteProductDiscountLabelShow}',
    LabelAutocompleteProductDiscountText: '${LabelAutocompleteProductDiscountText}',
    ProductDiscountBgcolor: '${ProductDiscountBgcolor}',
</#if>
<#if ResultsShowInStockStatus?has_content>
    ResultsShowInStockStatus: '${ResultsShowInStockStatus}',
    LabelResultsInStock: '${LabelResultsInStock}',
    LabelResultsPreOrder: '${LabelResultsPreOrder}',
    LabelResultsOutOfStock: '${LabelResultsOutOfStock}',
</#if>
<#if AutocompleteShowInStockStatus?has_content>
    AutocompleteShowInStockStatus: '${AutocompleteShowInStockStatus}',
    LabelAutocompleteInStock: '${LabelAutocompleteInStock}',
    LabelAutocompletePreOrder: '${LabelAutocompletePreOrder}',
    LabelAutocompleteOutOfStock: '${LabelAutocompleteOutOfStock}',
</#if>
<#if ResultsShowActionButton?has_content>
    ResultsShowActionButton: '${ResultsShowActionButton}',
</#if>
<#if ResultsShowOptionVariants?has_content>
    ResultsShowOptionVariants: '${ResultsShowOptionVariants}',
</#if>
<#if CustomCSS?has_content>
    CustomCSS: '${CustomCSS}',
</#if>
<#if ResultsTagLabels?has_content>
    ResultsTagLabels: {
    <#list ResultsTagLabels as resultTagLabel>
        "${resultTagLabel.id}": {
            "tag_label_id": "${resultTagLabel.id}",
            "product_tag": "${resultTagLabel.productTag}",
            "type": "${resultTagLabel.type}",
            "text": "${resultTagLabel.text}",
            "bg_color": "${resultTagLabel.backgroundColor}",
            "image_type": "${resultTagLabel.imageType}",
            "image_url": "${resultTagLabel.imageUrl}",
            "position": "${resultTagLabel.position}"
        }
        <#if !resultTagLabel?is_last>
            ,
        </#if>
    </#list>
    },
</#if>
<#if AutocompleteTagLabels?has_content>
    AutocompleteTagLabels: {
    <#list AutocompleteTagLabels as resultTagLabel>
        "${resultTagLabel.id}": {
        "tag_label_id": "${resultTagLabel.id}",
        "product_tag": "${resultTagLabel.productTag}",
        "type": "text",
        "text": "${resultTagLabel.text}",
        "bg_color": "${resultTagLabel.backgroundColor}",
        "image_type": "new",
        "image_url": "",
        "position": "top-left"
        }
        <#if !resultTagLabel?is_last>
            ,
        </#if>
    </#list>
    },
</#if>
<#if ResultsManualSortingCategories?has_content>
    ResultsManualSortingCategories: {
    <#list ResultsManualSortingCategories as key, value>
        "${key}": "${value}"
        <#if !key?is_last>
            ,
        </#if>
    </#list>
    },
</#if>
<#if PriceSource?has_content>
    PriceSource: '${PriceSource}',
</#if>
<#if LabelAutocompleteSuggestions?has_content>
    LabelAutocompleteSuggestions: '${LabelAutocompleteSuggestions}',
</#if>
<#if LabelAutocompleteCategories?has_content>
    LabelAutocompleteCategories: '${LabelAutocompleteCategories}',
</#if>
<#if LabelAutocompletePages?has_content>
    LabelAutocompletePages: '${LabelAutocompletePages}',
</#if>
<#if LabelAutocompleteProducts?has_content>
    LabelAutocompleteProducts: '${LabelAutocompleteProducts}',
</#if>
<#if LabelAutocompleteMoreProducts?has_content>
    LabelAutocompleteMoreProducts: '${LabelAutocompleteMoreProducts}',
</#if>
<#if LabelAutocompleteNothingFound?has_content>
    LabelAutocompleteNothingFound: '${LabelAutocompleteNothingFound}',
</#if>
<#if LabelAutocompleteSearchInputPlaceholder?has_content>
    LabelAutocompleteSearchInputPlaceholder: '${LabelAutocompleteSearchInputPlaceholder}',
</#if>
<#if LabelAutocompleteMobileSearchInputPlaceholder?has_content>
    LabelAutocompleteMobileSearchInputPlaceholder: '${LabelAutocompleteMobileSearchInputPlaceholder}',
</#if>
<#if LabelAutocompleteFrom?has_content>
    LabelAutocompleteFrom: '${LabelAutocompleteFrom}',
</#if>
<#if LabelAutocompleteSku?has_content>
    LabelAutocompleteSku: '${LabelAutocompleteSku}',
</#if>
<#if LabelAutocompleteNoSearchQuery?has_content>
    LabelAutocompleteNoSearchQuery: '${LabelAutocompleteNoSearchQuery}',
</#if>
<#if LabelAutocompleteResultsFound?has_content>
    LabelAutocompleteResultsFound: '${LabelAutocompleteResultsFound}',
</#if>
<#if LabelAutocompleteDidYouMean?has_content>
    LabelAutocompleteDidYouMean: '${LabelAutocompleteDidYouMean}',
</#if>
<#if LabelAutocompletePopularSuggestions?has_content>
    LabelAutocompletePopularSuggestions: '${LabelAutocompletePopularSuggestions}',
</#if>
<#if LabelAutocompleteSeeAllProducts?has_content>
    LabelAutocompleteSeeAllProducts: '${LabelAutocompleteSeeAllProducts}',
</#if>
<#if LabelAutocompleteNumReviews?has_content>
    LabelAutocompleteNumReviews: '${LabelAutocompleteNumReviews}',
</#if>
<#if LabelResultsSortBy?has_content>
    LabelResultsSortBy: '${LabelResultsSortBy}',
</#if>
<#if LabelResultsFeatured?has_content>
    LabelResultsFeatured: '${LabelResultsFeatured}',
</#if>
<#if LabelResultsNoReviews?has_content>
    LabelResultsNoReviews: '${LabelResultsNoReviews}',
</#if>
<#if LabelResultsTabCategories?has_content>
    LabelResultsTabCategories: '${LabelResultsTabCategories}',
</#if>
<#if LabelResultsTabProducts?has_content>
    LabelResultsTabProducts: '${LabelResultsTabProducts}',
</#if>
<#if LabelResultsTabPages?has_content>
    LabelResultsTabPages: '${LabelResultsTabPages}',
</#if>
<#if LabelResultsFound?has_content>
    LabelResultsFound: '${LabelResultsFound}',
</#if>
<#if LabelResultsFoundWithoutQuery?has_content>
    LabelResultsFoundWithoutQuery: '${LabelResultsFoundWithoutQuery}',
</#if>
<#if LabelResultsNothingFound?has_content>
    LabelResultsNothingFound: '${LabelResultsNothingFound}',
</#if>
<#if LabelResultsNothingFoundWithoutQuery?has_content>
    LabelResultsNothingFoundWithoutQuery: '${LabelResultsNothingFoundWithoutQuery}',
</#if>
<#if LabelResultsNothingFoundSeeAllCatalog?has_content>
    LabelResultsNothingFoundSeeAllCatalog: '${LabelResultsNothingFoundSeeAllCatalog}',
</#if>
<#if LabelResultsDidYouMean?has_content>
    LabelResultsDidYouMean: '${LabelResultsDidYouMean}',
</#if>
<#if LabelResultsFilters?has_content>
    LabelResultsFilters: '${LabelResultsFilters}',
</#if>
<#if LabelResultsFrom?has_content>
    LabelResultsFrom: '${LabelResultsFrom}',
</#if>
<#if LabelResultsSku?has_content>
    LabelResultsSku: '${LabelResultsSku}',
</#if>
<#if LabelResultsAddToCart?has_content>
    LabelResultsAddToCart: '${LabelResultsAddToCart}',
</#if>
<#if LabelResultsViewProduct?has_content>
    LabelResultsViewProduct: '${LabelResultsViewProduct}',
</#if>
<#if LabelResultsQuickView?has_content>
    LabelResultsQuickView: '${LabelResultsQuickView}',
</#if>
<#if LabelResultsViewFullDetails?has_content>
    LabelResultsViewFullDetails: '${LabelResultsViewFullDetails}',
</#if>
<#if LabelResultsQuantity?has_content>
    LabelResultsQuantity: '${LabelResultsQuantity}',
</#if>
<#if LabelResultsViewCart?has_content>
    LabelResultsViewCart: '${LabelResultsViewCart}',
</#if>
<#if LabelSeoTitle?has_content>
    LabelSeoTitle: '${LabelSeoTitle}',
</#if>
<#if LabelSeoTitleFilters?has_content>
    LabelSeoTitleFilters: '${LabelSeoTitleFilters}',
</#if>
<#if LabelSeoDescription?has_content>
    LabelSeoDescription: '${LabelSeoDescription}',
</#if>
<#if LabelSeoDescriptionFilters?has_content>
    LabelSeoDescriptionFilters: '${LabelSeoDescriptionFilters}',
</#if>
<#if LabelResultsShowProducts?has_content>
    LabelResultsShowProducts: '${LabelResultsShowProducts}',
</#if>
<#if LabelResultsResetFilters?has_content>
    LabelResultsResetFilters: '${LabelResultsResetFilters}',
</#if>
<#if LabelResultsBucketsShowMore?has_content>
    LabelResultsBucketsShowMore: '${LabelResultsBucketsShowMore}',
</#if>
<#if LabelResultsInfiniteScrollingLoadMore?has_content>
    LabelResultsInfiniteScrollingLoadMore: '${LabelResultsInfiniteScrollingLoadMore}',
</#if>
<#if LabelResultsPaginationPreviousPage?has_content>
    LabelResultsPaginationPreviousPage: '${LabelResultsPaginationPreviousPage}',
</#if>
<#if LabelResultsPaginationNextPage?has_content>
    LabelResultsPaginationNextPage: '${LabelResultsPaginationNextPage}',
</#if>
<#if LabelResultsPaginationCurrentPage?has_content>
    LabelResultsPaginationCurrentPage: '${LabelResultsPaginationCurrentPage}',
</#if>
<#if AutoSpellCorrection?has_content>
    AutoSpellCorrection: '${AutoSpellCorrection}',
</#if>
<#if PersonalizationEnabled?has_content>
    PersonalizationEnabled: '${PersonalizationEnabled}',
</#if>
<#if AutocompleteShowOnlyInStock?has_content>
    AutocompleteShowOnlyInStock: '${AutocompleteShowOnlyInStock}',
</#if>
<#if AutocompleteShowOnlyInStock?has_content>
    AutocompleteShowOnlyInStock: '${AutocompleteShowOnlyInStock}',
</#if>
<#if ResultsShowOnlyInStock?has_content>
    ResultsShowOnlyInStock: '${ResultsShowOnlyInStock}',
</#if>
<#if EnableShopifyMultiCurrency?has_content>
    EnableShopifyMultiCurrency: '${EnableShopifyMultiCurrency}',
</#if>
<#if HideAddToCartButton?has_content>
    HideAddToCartButton: '${HideAddToCartButton}',
</#if>
<#if HiddenPriceTags?has_content>
    HiddenPriceTags: '${HiddenPriceTags}',
</#if>
<#if ShowPriceOnlyForLoggedCustomer?has_content>
    ShowPriceOnlyForLoggedCustomer: '${ShowPriceOnlyForLoggedCustomer}',
</#if>
<#if PriceFormatRate?has_content>
    PriceFormatRate: ${PriceFormatRate},
</#if>
<#if ResultsShowOptionVariants?has_content>
    ResultsShowOptionVariants: '${ResultsShowOptionVariants}',
</#if>
<#if ResultsShowOptionVariants?has_content>
    ResultsShowOptionVariants: '${ResultsShowOptionVariants}',
</#if>
<#if ResultsShowActionButton?has_content>
    ResultsShowActionButton: '${ResultsShowActionButton}',
</#if>
<#if ResultsEnableInfiniteScrolling?has_content>
    ResultsEnableInfiniteScrolling: '${ResultsEnableInfiniteScrolling}',
</#if>
<#if ResultsShowPrice?has_content>
    ResultsShowPrice: '${ResultsShowPrice}',
</#if>
<#if ResultsShowListPrice?has_content>
    ResultsShowListPrice: '${ResultsShowListPrice}',
</#if>
<#if ResultsShowProductCode?has_content>
    ResultsShowProductCode: '${ResultsShowProductCode}',
</#if>
<#if ResultsFlipImageOnHover?has_content>
    ResultsFlipImageOnHover: '${ResultsFlipImageOnHover}',
</#if>
<#if ResultsItemCount?has_content>
    ResultsItemCount: ${ResultsItemCount},
</#if>
<#if ResultsTitleStrings?has_content>
    ResultsTitleStrings: ${ResultsTitleStrings},
</#if>
<#if ResultsDescriptionStrings?has_content>
    ResultsDescriptionStrings: ${ResultsDescriptionStrings},
</#if>
<#if ResultsDefaultView?has_content>
    ResultsDefaultView: '${ResultsDefaultView}',
</#if>
<#if ResultsZeroPriceAction?has_content>
    ResultsZeroPriceAction: '${ResultsZeroPriceAction}',
</#if>
<#if LabelResultsZeroPriceText?has_content>
    LabelResultsZeroPriceText: '${LabelResultsZeroPriceText}',
</#if>
<#if ResultsProductAttributeName?has_content>
    ResultsProductAttributeName: '${ResultsProductAttributeName}',
</#if>
<#if ResultsProductDefaultSorting?has_content>
    ResultsProductDefaultSorting: '${ResultsProductDefaultSorting}',
</#if>
<#if ResultsShowCategoryImages?has_content>
    ResultsShowCategoryImages: '${ResultsShowCategoryImages}',
</#if>
<#if ResultsCategoriesCount?has_content>
    ResultsCategoriesCount: ${ResultsCategoriesCount},
</#if>
<#if ResultsShowPageImages?has_content>
    ResultsShowPageImages: '${ResultsShowPageImages}',
</#if>
<#if ResultsPagesCount?has_content>
    ResultsPagesCount: ${ResultsPagesCount},
</#if>
<#if AutocompleteShowEmptyFieldHTML?has_content>
    AutocompleteShowEmptyFieldHTML: '${AutocompleteShowEmptyFieldHTML}',
</#if>
<#if AutocompleteEmptyFieldHTML?has_content>
    AutocompleteEmptyFieldHTML: '${AutocompleteEmptyFieldHTML}',
</#if>
<#if AutocompleteShowResultsHTML?has_content>
    AutocompleteShowResultsHTML: '${AutocompleteShowResultsHTML}',
</#if>
<#if AutocompleteResultsHTML?has_content>
    AutocompleteResultsHTML: '${AutocompleteResultsHTML}',
</#if>
<#if AutocompleteShowNoResultsHTML?has_content>
    AutocompleteShowNoResultsHTML: '${AutocompleteShowNoResultsHTML}',
</#if>
<#if AutocompleteNoResultsHTML?has_content>
    AutocompleteNoResultsHTML: '${AutocompleteNoResultsHTML}',
</#if>
<#if AutocompleteCategoriesCount?has_content>
    AutocompleteCategoriesCount: ${AutocompleteCategoriesCount},
</#if>
<#if AutocompletePagesCount?has_content>
    AutocompletePagesCount: ${AutocompletePagesCount},
</#if>
<#if AutocompleteSuggestionCount?has_content>
    AutocompleteSuggestionCount: ${AutocompleteSuggestionCount},
</#if>
<#if AutocompleteItemCount?has_content>
    AutocompleteItemCount: ${AutocompleteItemCount},
</#if>
<#if AutocompleteHighlight?has_content>
    AutocompleteHighlight: '${AutocompleteHighlight}',
</#if>
<#if AutocompleteShowViewAllLink?has_content>
    AutocompleteShowViewAllLink: '${AutocompleteShowViewAllLink}',
</#if>
<#if AutocompleteProductAttributeName?has_content>
    AutocompleteProductAttributeName: '${AutocompleteProductAttributeName}',
</#if>
<#if AutocompleteShowPrice?has_content>
    AutocompleteShowPrice: '${AutocompleteShowPrice}',
</#if>
<#if AutocompleteShowListPrice?has_content>
    AutocompleteShowListPrice: '${AutocompleteShowListPrice}',
</#if>
<#if AutocompleteShowProductImages?has_content>
    AutocompleteShowProductImages: '${AutocompleteShowProductImages}',
</#if>
<#if AutocompleteShowProductCode?has_content>
    AutocompleteShowProductCode: '${AutocompleteShowProductCode}',
</#if>
<#if AutocompleteZeroPriceAction?has_content>
    AutocompleteZeroPriceAction: '${AutocompleteZeroPriceAction}',
</#if>
<#if LabelAutocompleteZeroPriceText?has_content>
    LabelAutocompleteZeroPriceText: '${LabelAutocompleteZeroPriceText}',
</#if>
<#if ResultsShowFiltersInSidebar?has_content>
    ResultsShowFiltersInSidebar: '${ResultsShowFiltersInSidebar}',
</#if>
<#if ResultsShowFiltersInTopSection?has_content>
    ResultsShowFiltersInTopSection: '${ResultsShowFiltersInTopSection}',
</#if>
<#if LabelStickySearchboxInputPlaceholder?has_content>
    LabelStickySearchboxInputPlaceholder: '${LabelStickySearchboxInputPlaceholder}',
</#if>
<#if LabelStickySearchboxSearchButtonText?has_content>
    LabelStickySearchboxSearchButtonText: '${LabelStickySearchboxSearchButtonText}',
</#if>
<#if StickySearchboxShow?has_content>
    StickySearchboxShow: '${StickySearchboxShow}',
</#if>
<#if StickySearchboxPosition?has_content>
    StickySearchboxPosition: '${StickySearchboxPosition}',
</#if>
<#if ProductCompareEnabled?has_content>
    ProductCompareEnabled: '${ProductCompareEnabled}',
</#if>
    LabelResultsSortRelevance: '${LabelResultsSortRelevance}',
    LabelResultsSortTitleAsc: '${LabelResultsSortTitleAsc}',
    LabelResultsSortTitleDesc: '${LabelResultsSortTitleDesc}',
    LabelResultsSortCreatedDesc: '${LabelResultsSortCreatedDesc}',
    LabelResultsSortCreatedAsc: '${LabelResultsSortCreatedAsc}',
    LabelResultsSortPriceAsc: '${LabelResultsSortPriceAsc}',
    LabelResultsSortPriceDesc: '${LabelResultsSortPriceDesc}',
    LabelResultsSortDiscountDesc: '${LabelResultsSortDiscountDesc}',
    LabelResultsSortRatingAsc: '${LabelResultsSortRatingAsc}',
    LabelResultsSortRatingDesc: '${LabelResultsSortRatingDesc}',
    LabelResultsSortTotalReviewsAsc: '${LabelResultsSortTotalReviewsAsc}',
    LabelResultsSortTotalReviewsDesc: '${LabelResultsSortTotalReviewsDesc}',
    LabelResultsBestselling: '${LabelResultsBestselling}',
    ShopifyCurrency: '${ShopifyCurrency}',
    PriceFormatSymbol: '${PriceFormatSymbol}',
    PriceFormatBefore: '${PriceFormatBefore}',
    PriceFormatAfter: '${PriceFormatAfter}',
    PriceFormatDecimals: '${PriceFormatDecimals}',
    PriceFormatDecimalSeparator: '${PriceFormatDecimalSeparator}',
    PriceFormatThousandsSeparator: '${PriceFormatThousandsSeparator}',
    SmartNavigationOverrideSeo: 'N',
    ShowBestsellingSorting: 'Y',
    ShowDiscountSorting: 'Y',
    Platform: 'shopify',
}
