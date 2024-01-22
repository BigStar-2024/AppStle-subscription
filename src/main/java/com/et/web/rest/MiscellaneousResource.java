package com.et.web.rest;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloHttpException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.et.api.constants.ShopifyIdPrefix;
import com.et.api.graphql.ShopifyGraphqlClient;
import com.et.api.graphql.ShopifyGraphqlSubscriptionContractService;
import com.et.api.graphql.data.Product;
import com.et.api.graphql.data.ProductData;
import com.et.api.graphql.pojo.SubscriptionContractUpdateResult;
import com.et.api.shopify.ShopifyAPI;
import com.et.api.shopify.ShopifyWithRateLimiter;
import com.et.api.shopify.order.Order;
import com.et.api.shopify.order.UpdateOrderRequest;
import com.et.api.shopify.product.*;
import com.et.api.shopify.shop.Shop;
import com.et.api.shopify.shop.ShopInfo;
import com.et.api.utils.CurrencyUtils;
import com.et.api.utils.ShopifyGraphQLUtils;
import com.et.api.utils.SubscriptionUtils;
import com.et.constant.AppstleAttribute;
import com.et.constant.Constants;
import com.et.constant.EmailTemplateConstants;
import com.et.constant.MailgunProperties;
import com.et.domain.*;
import com.et.domain.enumeration.*;
import com.et.handler.OnBoardingHandler;
import com.et.liquid.Customer;
import com.et.liquid.LiquidUtils;
import com.et.liquid.SubscriptionContract;
import com.et.liquid.TagModel;
import com.et.liquid.ShippingAddressModel;
import com.et.pojo.*;
import com.et.pojo.SubscriptionBillingAttemptInfo;
import com.et.repository.*;
import com.et.security.SecurityUtils;
import com.et.service.*;
import com.et.service.dto.*;
import com.et.service.dto.DiscountType;
import com.et.utils.*;
import com.et.web.rest.errors.BadRequestAlertException;
import com.et.web.rest.vm.*;
import com.et.web.rest.vm.bundling.DiscountCodeResponse;
import com.et.web.rest.vm.mailgun.Domain;
import com.et.web.rest.vm.mailgun.DomainList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopify.java.graphql.client.queries.*;
import com.shopify.java.graphql.client.type.*;
import kong.unirest.JsonNode;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONTokener;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import tech.jhipster.web.util.HeaderUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.et.api.constants.ShopifyIdPrefix.*;
import static com.et.constant.Constants.*;
import static com.et.utils.SubscribeItScriptUtils.METAFIELD_NAMESPACE;
import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/api/miscellaneous/")
@Api(tags = "Miscellaneous Resource")
@RefreshScope
public class MiscellaneousResource {

    @Autowired
    private SocialConnectionService socialConnectionService;

    @Autowired
    private CustomerPortalSettingsService customerPortalSettingsService;

    @Autowired
    private OnBoardingHandler onBoardingHandler;

    @Autowired
    private SubscriptionDataService subscriptionDataService;

    @Autowired
    private SubscriptionGroupResource subscriptionGroupResource;

    @Autowired
    private SubscriptionWidgetSettingsService subscriptionWidgetSettingsService;

    @Autowired
    private CustomerPaymentService customerPaymentService;

    @Autowired
    private SubscriptionContractDetailsService subscriptionContractDetailsService;

    @Autowired
    private SubscriptionBillingAttemptService subscriptionBillingAttemptService;

    @Autowired
    private EmailTemplateSettingRepository emailTemplateSettingRepository;

    @Autowired
    private SubscriptionGroupService subscriptionGroupService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private SubscriptionContractService subscriptionContractService;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private SubscriptionBundleSettingsService subscriptionBundleSettingsService;

    @Autowired
    private SubscriptionContractDetailsResource subscriptionContractDetailsResource;

    @Autowired
    private CartWidgetSettingsService cartWidgetSettingsService;

    @Autowired
    private PlanInfoService planInfoService;

    @Autowired
    private SubscriptionGroupPlanRepository subscriptionGroupPlanRepository;
//    @Autowired
//    private ImportDefaultHeaders importDefaultHeaders;

    @Autowired
    private ShopSettingsService shopSettingsService;

    @Autowired
    private WebhookUtils webhookUtils;

    @Autowired
    private SubscriptionContractDetailsRepository subscriptionContractDetailsRepository;

    @Autowired
    private SubscriptionGroupPlanService subscriptionGroupPlanService;

    @Autowired
    private AwsUtils awsUtils;

    @Autowired
    private EmailTemplateSettingService emailTemplateSettingService;

    @Autowired
    private ActivityUpdatesSettingsRepository activityUpdatesSettingsRepository;

    @Autowired
    private SummaryReportService summaryReportService;

    private final Logger log = LoggerFactory.getLogger(MiscellaneousResource.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String ENTITY_NAME = "misellaneous";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ShopLabelService shopLabelService;

    @Autowired
    private MailgunProperties mailgunProperties;

    @Autowired
    private SubscriptionBundlingService subscriptionBundlingService;

    @Autowired
    private CustomerPaymentRepository customerPaymentRepository;

    @Autowired
    private OnboardingInfoService onboardingInfoService;

    /*@PutMapping("sync-customer-portal-labels")
    public void syncCustomerPortalSettingLabel(@RequestBody SyncLabelsInfo syncLabelsInfo) throws Exception {
        customerPortalSettingsService.syncLabels(syncLabelsInfo);
    }


    @DeleteMapping("delete-customer-portal-labels")
    public void deleteCustomerPortalSettingLabel(@RequestBody DeleteLabelKey deleteLabelKey) throws Exception {
        customerPortalSettingsService.deleteLabelKey(deleteLabelKey.getKey());
    }*/

    @GetMapping("/sync-product-info")
    public void syncProductInfo(@RequestParam(required = false) String apiKey, @RequestParam Long productId, HttpServletRequest request) {

        String shop = apiKey != null ? commonUtils.getShopByAPIKey(apiKey).get() : SecurityUtils.getCurrentUserLogin().get();

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLProductId(productId));

        if (!CollectionUtils.isEmpty(subscriptionContractDetailsList)) {

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            String gqlProductId = ShopifyGraphQLUtils.getGraphQLProductId(productId);

            for (SubscriptionContractDetails scd : subscriptionContractDetailsList) {
                try {
                    String gqlContractId = scd.getGraphSubscriptionContractId();

                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
                    Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                    List<SubscriptionContractQuery.Node> existingLineItems =
                        requireNonNull(subscriptionContractQueryResponse.getData())
                            .map(d -> d.getSubscriptionContract()
                                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                                .map(SubscriptionContractQuery.Lines::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .filter(g -> g.getVariantId().isPresent() && g.getProductId().get().equals(gqlProductId))
                            .collect(Collectors.toList());

                    if (CollectionUtils.isEmpty(existingLineItems)) {
                        continue;
                    }

                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(gqlContractId);
                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                    long countOfErrors = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                        .orElse(new ArrayList<>()).stream()
                        .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                        .peek(message -> log.info("REST request for Update subscription contract is failed {} ", message)).count();

                    if (countOfErrors == 0) {
                        // get draft Id from the response
                        Optional<String> optionalDraftId = optionalResponse.getData()
                            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                            .map(draft -> draft.get().getId());


                        if (optionalDraftId.isPresent()) {

                            String draftId = optionalDraftId.get();

                            for (SubscriptionContractQuery.Node existingLineNode : existingLineItems) {
                                // Add Line Item
                                SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput.builder();

                                subscriptionLineInputBuilder
                                    .productVariantId(existingLineNode.getVariantId().get())
                                    .quantity(existingLineNode.getQuantity())
                                    .currentPrice(existingLineNode.getCurrentPrice().getAmount());

                                if (existingLineNode.getSellingPlanId().isPresent()) {
                                    subscriptionLineInputBuilder.sellingPlanId(existingLineNode.getSellingPlanId().get());
                                }

                                if (existingLineNode.getSellingPlanName().isPresent()) {
                                    subscriptionLineInputBuilder.sellingPlanName(existingLineNode.getSellingPlanName().get());
                                }

                                // Copy Pricing Policy
                                if (existingLineNode.getPricingPolicy().isPresent()) {
                                    SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder()
                                        .basePrice(existingLineNode.getPricingPolicy().get().getBasePrice().getAmount());

                                    List<SubscriptionContractQuery.CycleDiscount> existingCycleDiscounts = existingLineNode.getPricingPolicy().get().getCycleDiscounts();

                                    if (!CollectionUtils.isEmpty(existingCycleDiscounts)) {
                                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscountsInputs = new ArrayList<>(existingCycleDiscounts.size());

                                        for (SubscriptionContractQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {

                                            SubscriptionPricingPolicyCycleDiscountsInput.Builder subscriptionPricingPolicyCycleDiscountsInputBuilder =
                                                SubscriptionPricingPolicyCycleDiscountsInput.builder();

                                            subscriptionPricingPolicyCycleDiscountsInputBuilder
                                                .afterCycle(existingCycleDiscount.getAfterCycle())
                                                .adjustmentType(existingCycleDiscount.getAdjustmentType())
                                                .computedPrice(existingCycleDiscount.getComputedPrice().getAmount());

                                            if (SellingPlanPricingPolicyAdjustmentType.PERCENTAGE.equals(existingCycleDiscount.getAdjustmentType())) {
                                                SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();

                                                subscriptionPricingPolicyCycleDiscountsInputBuilder
                                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build());
                                            } else if (SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT.equals(existingCycleDiscount.getAdjustmentType())) {
                                                SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                                                subscriptionPricingPolicyCycleDiscountsInputBuilder
                                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build());
                                            } else if (SellingPlanPricingPolicyAdjustmentType.PRICE.equals(existingCycleDiscount.getAdjustmentType())) {
                                                SubscriptionContractQuery.AsMoneyV2 adjustmentValue = (SubscriptionContractQuery.AsMoneyV2) existingCycleDiscount.getAdjustmentValue();

                                                subscriptionPricingPolicyCycleDiscountsInputBuilder
                                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(adjustmentValue.getAmount()).build());
                                            }

                                            cycleDiscountsInputs.add(subscriptionPricingPolicyCycleDiscountsInputBuilder.build());
                                        }
                                        pricingPolicyInputBuilder.cycleDiscounts(cycleDiscountsInputs);
                                    }
                                    subscriptionLineInputBuilder.pricingPolicy(pricingPolicyInputBuilder.build());
                                }

                                // Copy Custom Attributes
                                if (!CollectionUtils.isEmpty(existingLineNode.getCustomAttributes())) {
                                    List<AttributeInput> attributeInputList = new ArrayList<>();

                                    for (SubscriptionContractQuery.CustomAttribute customAttribute : existingLineNode.getCustomAttributes()) {
                                        AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                                        attributeInputBuilder.key(customAttribute.getKey());
                                        if (customAttribute.getValue().isPresent()) {
                                            attributeInputBuilder.value(customAttribute.getValue().get());
                                        }
                                        attributeInputList.add(attributeInputBuilder.build());
                                    }
                                    subscriptionLineInputBuilder.customAttributes(attributeInputList);
                                }

                                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInputBuilder.build());
                                Response<Optional<SubscriptionDraftLineAddMutation.Data>> draftLineAddMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                                if (draftLineAddMutationResponse.hasErrors()) {
                                    log.error("Error while adding line item." + draftLineAddMutationResponse.getErrors().get(0).getMessage());
                                    continue;
                                }

                                List<SubscriptionDraftLineAddMutation.UserError> draftLineAddMutationResponseUserErrors =
                                    requireNonNull(draftLineAddMutationResponse.getData())
                                        .map(d -> d.getSubscriptionDraftLineAdd()
                                            .map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors)
                                            .orElse(new ArrayList<>()))
                                        .orElse(new ArrayList<>());

                                if (draftLineAddMutationResponseUserErrors.size() > 0) {
                                    log.error("Error while adding line item. User error = " + draftLineAddMutationResponseUserErrors.get(0).getMessage());
                                    continue;
                                }

                                // Remove old line item
                                SubscriptionDraftLineRemoveMutation subscriptionDraftLineRemoveMutation = new SubscriptionDraftLineRemoveMutation(draftId, existingLineNode.getId());
                                Response<Optional<SubscriptionDraftLineRemoveMutation.Data>> draftLineRemoveMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineRemoveMutation);

                                if (!CollectionUtils.isEmpty(draftLineRemoveMutationResponse.getErrors())) {
                                    log.error("Error while removing line item." + draftLineRemoveMutationResponse.getErrors().get(0).getMessage());
                                    continue;
                                }

                                List<SubscriptionDraftLineRemoveMutation.UserError> draftLineRemoveutationResponseUserErrors =
                                    requireNonNull(draftLineRemoveMutationResponse.getData())
                                        .map(d -> d.getSubscriptionDraftLineRemove()
                                            .map(SubscriptionDraftLineRemoveMutation.SubscriptionDraftLineRemove::getUserErrors)
                                            .orElse(new ArrayList<>()))
                                        .orElse(new ArrayList<>());

                                if (draftLineRemoveutationResponseUserErrors.size() > 0) {
                                    log.error("Error while removing line item. User error = " + draftLineRemoveutationResponseUserErrors.get(0).getMessage());
                                    continue;
                                }
                            }

                            // Commit subscription draft
                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                            List<SubscriptionDraftCommitMutation.UserError> userErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(s -> s.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                            if (!userErrors.isEmpty()) {
                                log.error("Error while committing draft subscription. User errors = " + userErrors.get(0).getMessage());
                                continue;
                            }
                        }
                    }
                } catch (JsonProcessingException e) {
                    log.error("Json proceeing error while syncing product info. ex={}", e);
                } catch (Exception e) {
                    log.error("Error while syncing product info. ex={}", e);
                }
            }
        }
    }

    @GetMapping("/find-removed-variants-contracts")
    public List<String> findRemovedVariantsContracts(@RequestParam(required = false) String apiKey) throws Exception {


        String shop = apiKey != null ? commonUtils.getShopByAPIKey(apiKey).get() : SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<String> result = new ArrayList<>();

        try {

            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            while (isNextPage) {
                SubscriptionContractsLineItemsQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsLineItemsQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                List<SubscriptionContractsLineItemsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsLineItemsQueryResponse.getData()).map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().map(SubscriptionContractsLineItemsQuery.Edge::getNode).collect(Collectors.toList());

                isNextPage = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getPageInfo).map(SubscriptionContractsLineItemsQuery.PageInfo::isHasNextPage).orElse(Boolean.FALSE);

                Optional<SubscriptionContractsLineItemsQuery.Edge> last = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsLineItemsQuery.Edge::getCursor).orElse(null);

                Set<Long> contractIds = subscriptionContracts.stream().map(s -> Long.parseLong(s.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                List<SubscriptionContractDetails> subscriptionContractDetailsSet = new ArrayList<>();
                if (!contractIds.isEmpty()) {
                    subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(contractIds);
                }

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                for (SubscriptionContractsLineItemsQuery.Node subscriptionContract : subscriptionContracts) {
                    i++;
                    log.info("iterating record i=" + i);

                    long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                    if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                        continue;
                    }

                    List<SubscriptionContractsLineItemsQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractsLineItemsQuery.Edge1::getNode).collect(Collectors.toList());

                    boolean hasRemovedVariant = subscriptionLineItems.stream().anyMatch(s -> s.getVariantId().isEmpty());

                    if (hasRemovedVariant) {
                        result.add(subscriptionContract.getId());
                    }

                }
            }
        } catch (Exception ex) {
        }

        return result;
    }

    @PostMapping("/csv-bulk-update-selling-plan")
    public List<String> csvBulkUpdateSellingPlan(@RequestParam(value = "api_key") String apiKey,
                                                 @RequestParam(required = false, value = "isReplaceSellingPlan", defaultValue = "false") boolean isReplaceSellingPlan,
                                                 @RequestBody String csvContent) throws Exception {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        List<String> errorList = new ArrayList<>();

        Reader subscriptionDataReader = new BufferedReader(new StringReader(csvContent));
        CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Subscription ID")) {
            errorList.add("'Subscription ID' column missing.");
        }

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Selling Plan ID")) {
            errorList.add("'Selling Plan ID' column missing.");
        }

        if (errorList.size() > 0) {
            return errorList;
        }

        Map<Long, Long> sellingPlanIdBySubscriptionId = new LinkedHashMap<>();

        for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {

            String subscriptionContractIdStr = subscriptionDataRecord.get("Subscription ID");
            String sellingPlanIdStr = subscriptionDataRecord.get("Selling Plan ID");

            if (!NumberUtils.isParsable(subscriptionContractIdStr) || !NumberUtils.isParsable(sellingPlanIdStr)) {
                errorList.add("Invalid Subscription ID=" + subscriptionContractIdStr + " or Selling Plan ID=" + sellingPlanIdStr);
                continue;
            }

            Long subscriptionContactId = Long.parseLong(subscriptionContractIdStr);
            Long sellingPlanId = Long.parseLong(sellingPlanIdStr);

            sellingPlanIdBySubscriptionId.put(subscriptionContactId, sellingPlanId);
        }

        if (errorList.size() > 0) {
            return errorList;
        }

        return setSellingPlanIds(apiKey, isReplaceSellingPlan, sellingPlanIdBySubscriptionId);
    }

    @PutMapping("/set-selling-plan-ids")
    public List<String> setSellingPlanIds(@RequestParam("api_key") String apiKey,
                                          @RequestParam(required = false, value = "isReplaceSellingPlan", defaultValue = "false") boolean isReplaceSellingPlan,
                                          @RequestBody Map<Long, Long> sellingPlanIdBySubscriptionId) throws Exception {

        String shop = commonUtils.getShopInfoByAPIKey(apiKey).get().getShop();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String cusrsor = null;
        boolean isNextPage = true;
        List<String> messages = new ArrayList<>();


        Map<Long, String> sellingPlanNameBySellingPlanId = new HashMap<>();

        while (isNextPage) {

            SellingPlanGroupsBriefQuery sellingPlanGroupsBriefQuery = new SellingPlanGroupsBriefQuery(Input.fromNullable(null), Input.fromNullable(cusrsor));
            Response<Optional<SellingPlanGroupsBriefQuery.Data>> sellingPlanGroupsBriefQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupsBriefQuery);

            isNextPage = sellingPlanGroupsBriefQueryResponse
                .getData()
                .map(SellingPlanGroupsBriefQuery.Data::getSellingPlanGroups)
                .map(SellingPlanGroupsBriefQuery.SellingPlanGroups::getPageInfo)
                .map(SellingPlanGroupsBriefQuery.PageInfo::isHasNextPage)
                .orElse(Boolean.FALSE);

            List<SellingPlanGroupsBriefQuery.Edge> sellingPlanGroups = sellingPlanGroupsBriefQueryResponse
                .getData()
                .map(SellingPlanGroupsBriefQuery.Data::getSellingPlanGroups)
                .map(SellingPlanGroupsBriefQuery.SellingPlanGroups::getEdges)
                .orElse(new ArrayList<>());

            Optional<SellingPlanGroupsBriefQuery.Edge> last = sellingPlanGroups
                .stream().reduce((first, second) -> second);

            cusrsor = last.map(SellingPlanGroupsBriefQuery.Edge::getCursor).orElse(null);

            for (SellingPlanGroupsBriefQuery.Edge sellingPlanGroup : sellingPlanGroups) {
                sellingPlanNameBySellingPlanId.putAll(sellingPlanGroup
                    .getNode()
                    .getSellingPlans()
                    .getEdges()
                    .stream()
                    .map(SellingPlanGroupsBriefQuery.Edge1::getNode)
                    .collect(Collectors.toMap(d -> Long.parseLong(d.getId().replace(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX, "")), SellingPlanGroupsBriefQuery.Node1::getName)));
            }
        }


        for (Map.Entry<Long, Long> entry : sellingPlanIdBySubscriptionId.entrySet()) {
            try {
                Long contractId = entry.getKey();
                Long sellingPlanId = entry.getValue();
                String sellingPlanName = sellingPlanNameBySellingPlanId.get(sellingPlanId);

                if (sellingPlanName == null) {
                    messages.add("couldn't update contractId=" + contractId + " sellingplanId=" + sellingPlanId + " is invalid.");
                    continue;
                }

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                List<SubscriptionContractQuery.Node> lineItems = requireNonNull(subscriptionContractQueryResponse
                    .getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

                boolean anyLineItemHasSellingPlan = lineItems.stream().anyMatch(l -> l.getSellingPlanId().isPresent());

                if (!isReplaceSellingPlan && anyLineItemHasSellingPlan) {
                    messages.add("couldn't update contractId=" + contractId + ". One of the items already have the selling plan Id.");
                    continue;
                }

                for (SubscriptionContractQuery.Node lineItem : lineItems) {

                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                    long countOfErrors = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                        .orElse(new ArrayList<>()).stream()
                        .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                        .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


                    if (countOfErrors == 0) {
                        // get draft Id from the response
                        Optional<String> optionalDraftId = optionalResponse.getData()
                            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                            .map(draft -> draft.get().getId());


                        if (optionalDraftId.isPresent()) {
                            String draftId = optionalDraftId.get();
                            SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                            subscriptionLineUpdateInputBuilder.sellingPlanId(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX + sellingPlanId);
                            subscriptionLineUpdateInputBuilder.sellingPlanName(sellingPlanName);

                            SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                            SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineItem.getId(), subscriptionLineUpdateInput);
                            Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                            if (optionalMutationResponse.hasErrors()) {
                                throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                            }

                            List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalMutationResponseUserErrors.isEmpty()) {
                                throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                            }

                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                            if (optionalDraftCommitResponse.hasErrors()) {
                                throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                            }

                            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                            }
                        }
                    }
                }

                messages.add("contractId=" + contractId + " updated successfully.");
            } catch (Exception ex) {

            }
        }

        return messages;
    }

    @GetMapping("/update-subscription-pricing-policy")
    public void updateSubscriptionPricingPolicy(@RequestParam(value = "api_key") String apiKey) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop, 20);

        List<String> result = new ArrayList<>();

        List<SubscriptionContractDetails> subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findByShop(shop);
        SellingPlanGroupsWithSellingPlansQuery sellingPlanGroupsQuery = new SellingPlanGroupsWithSellingPlansQuery(Input.fromNullable(null), Input.fromNullable(null));

        Response<Optional<SellingPlanGroupsWithSellingPlansQuery.Data>> optionalSellingPlanGroupsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupsQuery);
        List<SellingPlanGroupsWithSellingPlansQuery.Node> subscriptionGroupPlans = optionalSellingPlanGroupsQueryResponse
            .getData()
            .map(SellingPlanGroupsWithSellingPlansQuery.Data::getSellingPlanGroups)
            .map(SellingPlanGroupsWithSellingPlansQuery.SellingPlanGroups::getEdges)
            .map(f -> f.stream().map(SellingPlanGroupsWithSellingPlansQuery.Edge::getNode).collect(Collectors.toList()))
            .orElse(new ArrayList<>());

        Map<String, SellingPlanGroupsWithSellingPlansQuery.Node1> sellingPlanNodeById = subscriptionGroupPlans.stream()
            .flatMap(s -> s.getSellingPlans().getEdges().stream())
            .map(SellingPlanGroupsWithSellingPlansQuery.Edge1::getNode)
            .collect(Collectors.toMap(s -> s.getId(), s -> s));

        DecimalFormat df = new DecimalFormat("#.##");

        try {

            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            Map<String, String> priceByVariantId = new HashMap<>();

            while (isNextPage) {
                SubscriptionContractsLineItemsQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsLineItemsQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                List<SubscriptionContractsLineItemsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsLineItemsQueryResponse.getData()).map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().map(SubscriptionContractsLineItemsQuery.Edge::getNode).collect(Collectors.toList());

                isNextPage = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getPageInfo).map(SubscriptionContractsLineItemsQuery.PageInfo::isHasNextPage).orElse(Boolean.FALSE);

                Optional<SubscriptionContractsLineItemsQuery.Edge> last = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsLineItemsQuery.Edge::getCursor).orElse(null);

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                for (SubscriptionContractsLineItemsQuery.Node subscriptionContract : subscriptionContracts) {
                    i++;
                    log.info("iterating record i=" + i);

                    long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                    if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                        continue;
                    }

                    List<SubscriptionContractsLineItemsQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractsLineItemsQuery.Edge1::getNode).collect(Collectors.toList());


                    for (SubscriptionContractsLineItemsQuery.Node1 subscriptionLineItem : subscriptionLineItems) {
                        if (subscriptionLineItem.getPricingPolicy().isEmpty()
                            && subscriptionLineItem.getSellingPlanId().isPresent()
                            && subscriptionLineItem.getVariantId().isPresent()) {
                            String lineId = subscriptionLineItem.getId();
                            log.info("subscriptionLineItem.getPricingPolicy().isEmpty() " + lineId);

                            String variantId = subscriptionLineItem.getVariantId().get();

                            if (!priceByVariantId.containsKey(variantId)) {
                                ProductVariantQuery productVariantQuery = new ProductVariantQuery(variantId);
                                Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
                                priceByVariantId.put(variantId, optionalProductVariantQueryResponse.getData()
                                    .flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice))
                                    .orElse(null).toString());
                            }

                            Double basePrice = Double.parseDouble(priceByVariantId.get(variantId));

                            String sellingPlanId = subscriptionLineItem.getSellingPlanId().get();

                            SellingPlanGroupsWithSellingPlansQuery.Node1 sellingPlanNode = sellingPlanNodeById.get(sellingPlanId);

                            if (sellingPlanNode == null) {
                                continue;
                            }

                            List<SellingPlanGroupsWithSellingPlansQuery.PricingPolicy> pricingPolicies = sellingPlanNode.getPricingPolicies();

                            List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                            for (SellingPlanGroupsWithSellingPlansQuery.PricingPolicy pricingPolicy : pricingPolicies) {

                                if (pricingPolicy instanceof SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanFixedPricingPolicy) {
                                    SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanFixedPricingPolicy sellingPlanFixedPricingPolicy = (SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanFixedPricingPolicy) pricingPolicy;

                                    if (sellingPlanFixedPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT) {
                                        cycleDiscounts.add(SubscriptionPricingPolicyCycleDiscountsInput.builder().afterCycle(0).build());
                                    } else if (sellingPlanFixedPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE) {
                                        cycleDiscounts.add(SubscriptionPricingPolicyCycleDiscountsInput.builder().afterCycle(0).build());
                                    } else if (sellingPlanFixedPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE) {
                                        double percentage = ((SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanPricingPolicyPercentageValue) sellingPlanFixedPricingPolicy
                                            .getAdjustmentValue())
                                            .getPercentage();
                                        double computedPrice = Double.parseDouble(df.format(basePrice * ((100 - percentage) / 100)));

                                        cycleDiscounts.add(SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                            .afterCycle(0)
                                            .adjustmentValue(SellingPlanPricingPolicyValueInput.builder()
                                                .percentage(percentage)
                                                .build())
                                            .computedPrice(computedPrice)
                                            .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE)
                                            .build());
                                    }

                                } else if (pricingPolicy instanceof SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanRecurringPricingPolicy) {
                                    SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanRecurringPricingPolicy sellingPlanRecurringPricingPolicy = (SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanRecurringPricingPolicy) pricingPolicy;

                                    if (sellingPlanRecurringPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT) {

                                    } else if (sellingPlanRecurringPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PRICE) {

                                    } else if (sellingPlanRecurringPricingPolicy.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE) {

                                        if (sellingPlanRecurringPricingPolicy.getAfterCycle().isPresent()) {
                                            double percentage = ((SellingPlanGroupsWithSellingPlansQuery.AsSellingPlanPricingPolicyPercentageValue1) sellingPlanRecurringPricingPolicy.getAdjustmentValue()).getPercentage();
                                            double computedPrice = Double.parseDouble(df.format(basePrice * ((100 - percentage) / 100)));

                                            cycleDiscounts.add(SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                                .afterCycle(sellingPlanRecurringPricingPolicy.getAfterCycle().get())
                                                .adjustmentValue(SellingPlanPricingPolicyValueInput.builder()
                                                    .percentage(percentage)
                                                    .build())
                                                .computedPrice(computedPrice)
                                                .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE)
                                                .build());
                                        }
                                    }
                                }

                            }

                            if (!cycleDiscounts.isEmpty()) {
                                String a = "b";

                                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());
                                Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                                String draftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                                    .map(draft -> draft.get().getId()).orElse(null);


                                SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder();
                                pricingPolicyInputBuilder.basePrice(basePrice);
                                pricingPolicyInputBuilder.cycleDiscounts(cycleDiscounts);
                                SubscriptionPricingPolicyInput pricingPolicyInput = pricingPolicyInputBuilder.build();

                                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();
                                SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                                    .currentPrice(cycleDiscounts.get(0).computedPrice())
                                    .pricingPolicy(pricingPolicyInput)
                                    .build();

                                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                                if (optionalMutationResponse.hasErrors()) {
                                    continue;
                                }

                                List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                if (!optionalMutationResponseUserErrors.isEmpty()) {
                                    continue;
                                }

                                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                                if (optionalDraftCommitResponse.hasErrors()) {
                                    continue;
                                }

                                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                    continue;
                                }

                                String c = "b";
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            String a = "b";
        }
    }


    @GetMapping("/update-subscription-current-price")
    public void updateSubscriptionCurrentPrice(@RequestParam(value = "api_key") String apiKey) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop, 20);

        List<String> result = new ArrayList<>();

        List<SubscriptionContractDetails> subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findByShop(shop);

        DecimalFormat df = new DecimalFormat("#.##");

        try {

            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            Map<String, String> priceByVariantId = new HashMap<>();

            while (isNextPage) {
                SubscriptionContractsLineItemsQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsLineItemsQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                List<SubscriptionContractsLineItemsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsLineItemsQueryResponse.getData()).map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().map(SubscriptionContractsLineItemsQuery.Edge::getNode).collect(Collectors.toList());

                isNextPage = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getPageInfo).map(SubscriptionContractsLineItemsQuery.PageInfo::isHasNextPage).orElse(Boolean.FALSE);

                Optional<SubscriptionContractsLineItemsQuery.Edge> last = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsLineItemsQuery.Edge::getCursor).orElse(null);

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                for (SubscriptionContractsLineItemsQuery.Node subscriptionContract : subscriptionContracts) {
                    i++;
                    log.info("iterating record i=" + i);

                    long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                    if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                        continue;
                    }

                    List<SubscriptionContractsLineItemsQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractsLineItemsQuery.Edge1::getNode).collect(Collectors.toList());


                    for (SubscriptionContractsLineItemsQuery.Node1 subscriptionLineItem : subscriptionLineItems) {
                        if (subscriptionLineItem.getPricingPolicy().isEmpty()) {
                            continue;
                        }

                        String lineId = subscriptionLineItem.getId();


                        double currentPrice = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString());
                        SubscriptionContractsLineItemsQuery.PricingPolicy pricingPolicy = subscriptionLineItem.getPricingPolicy().get();

                        for (SubscriptionContractsLineItemsQuery.CycleDiscount cycleDiscount : pricingPolicy.getCycleDiscounts()) {

                        }

                        if (currentPrice == Double.parseDouble(pricingPolicy.getBasePrice().getAmount().toString())
                            && pricingPolicy.getCycleDiscounts().size() == 1
                            && Double.parseDouble(pricingPolicy.getCycleDiscounts().get(0).getComputedPrice().getAmount().toString()) != currentPrice) {
                            log.info("contractId=" + subscriptionContract.getId());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            String a = "b";
        }
    }

    @GetMapping("/order-details")
    public OrderDetailsQuery.Order getOrderDetails(@RequestParam(value = "api_key") String apiKey, @RequestParam(value = "orderId") Long orderId) throws Exception {

        OrderDetailsQuery orderDetailsQuery = new OrderDetailsQuery(ORDER_ID_PREFIX + orderId);
        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(commonUtils.getShopByAPIKey(apiKey).get());

        Response<Optional<OrderDetailsQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderDetailsQuery);

        OrderDetailsQuery.Order order = optionalQueryResponse.getData().map(d -> d.getOrder().orElse(null)).orElse(null);

        return order;
    }


    @GetMapping("/subscription-plan-details")
    public List<SellingPlanGroupQuery.SellingPlanGroup> getSellingPlanDetails(@RequestParam(value = "api_key") String apiKey) throws Exception {

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(commonUtils.getShopByAPIKey(apiKey).get());

        SellingPlanGroupsQuery sellingPlanGroupsQuery = new SellingPlanGroupsQuery(Input.fromNullable(null), Input.fromNullable(null));
        Response<Optional<SellingPlanGroupsQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupsQuery);

        List<SellingPlanGroupsQuery.Node> sellingPlanGroups = optionalQueryResponse.getData().map(d -> d.getSellingPlanGroups()).orElse(null).getEdges().stream().map(d -> d.getNode()).collect(Collectors.toList());

        List<SellingPlanGroupQuery.SellingPlanGroup> sellingPlanGroupList = new ArrayList<>();

        for (SellingPlanGroupsQuery.Node sellingPlanGroup : sellingPlanGroups) {
            SellingPlanGroupQuery sellingPlanGroupQuery = new SellingPlanGroupQuery(sellingPlanGroup.getId());
            Response<Optional<SellingPlanGroupQuery.Data>> optionalQueryResponse1 = shopifyGraphqlClient.getOptionalQueryResponse(sellingPlanGroupQuery);

            SellingPlanGroupQuery.SellingPlanGroup sellingPlanGroup1 = optionalQueryResponse1.getData().map(e -> e.getSellingPlanGroup().orElse(null)).orElse(null);
            sellingPlanGroupList.add(sellingPlanGroup1);
        }

        return sellingPlanGroupList;
    }



    @GetMapping("/remove-pricing-policy")
    public void removePricingPolicy(@RequestParam(value = "api_key") String apiKey, @RequestParam String contractIds) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Set<Long> contractIdSet = Arrays.stream(contractIds.split(","))
            .filter(StringUtils::hasText)
            .map(StringUtils::trimAllWhitespace)
            .map(Long::parseLong)
            .collect(Collectors.toSet());


        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndSubscriptionContractIdIn(shop, contractIdSet);
        subscriptionContractDetailsList.sort(Comparator.comparingLong(SubscriptionContractDetails::getSubscriptionContractId));

        for (SubscriptionContractDetails scd : subscriptionContractDetailsList) {
            Long contractId = scd.getSubscriptionContractId();
            SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);

            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            long countOfErrors = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

            if (subscriptionContract != null && countOfErrors == 0) {

                try {
                    Optional<String> optionalDraftId = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId());

                    if (optionalDraftId.isPresent()) {
                        String draftId = optionalDraftId.get();

                        removePricingPolicyForContract(subscriptionContract, draftId, shopifyGraphqlClient);

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (optionalDraftCommitResponse.hasErrors()) {
                            throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                        }
                    }
                } catch (Exception e) {
                    log.info("Error while removing pricing policy. error={}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

    private void removePricingPolicyForContract(SubscriptionContractQuery.SubscriptionContract subscriptionContract, String draftId, ShopifyGraphqlClient shopifyGraphqlClient) throws Exception {

        List<SubscriptionContractQuery.Node> lines = subscriptionContract.getLines().getEdges()
            .stream().map(SubscriptionContractQuery.Edge::getNode).collect(Collectors.toList());

        for (SubscriptionContractQuery.Node line : lines) {
            String lineId = line.getId();

            if (line.getPricingPolicy().isPresent()) {

                double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

                Double currentPrice = Double.parseDouble(line.getPricingPolicy().get().getBasePrice().getAmount().toString()) * fulfillmentCountMultiplier;

                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                    .currentPrice(currentPrice)
                    .pricingPolicy(null)
                    .build();

                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineId, subscriptionLineUpdateInput);
                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);
            }
        }
    }

    @PutMapping("/update-pricing-policy")
    public void updatePricingPolicy(@RequestParam(value = "api_key") String apiKey, @RequestParam String contractIds, @RequestBody List<AppstleCycle> cycles) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Set<Long> contractIdSet = Arrays.stream(contractIds.split(","))
            .filter(StringUtils::hasText)
            .map(StringUtils::trimAllWhitespace)
            .map(Long::parseLong)
            .collect(Collectors.toSet());

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndSubscriptionContractIdIn(shop, contractIdSet);
        subscriptionContractDetailsList.sort(Comparator.comparingLong(SubscriptionContractDetails::getSubscriptionContractId));

        for (SubscriptionContractDetails scd : subscriptionContractDetailsList) {
            Long contractId = scd.getSubscriptionContractId();
            SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractDetailsService.getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);

            if (subscriptionContract != null) {
                try {
                    List<SubscriptionContractQuery.Node> lines = subscriptionContract.getLines().getEdges()
                        .stream().map(SubscriptionContractQuery.Edge::getNode).collect(Collectors.toList());

                    for (SubscriptionContractQuery.Node line : lines) {
                        String lineId = line.getId();

                        double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

                        Double basePrice = Double.parseDouble(line.getCurrentPrice().getAmount().toString()) / fulfillmentCountMultiplier;

                        if (line.getPricingPolicy().isPresent()) {
                            basePrice = Double.parseDouble(line.getPricingPolicy().get().getBasePrice().getAmount().toString()) * fulfillmentCountMultiplier;
                        }

                        subscriptionContractDetailsService.subscriptionContractUpdateLineItemPricingPolicy(shop, subscriptionContract, lineId, basePrice, cycles, ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    }
                } catch (Exception e) {
                    log.info("Error while updating pricing policy. error={}", ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }

    @PostMapping("/csv-bulk-update-pricing-policy")
    public List<String> csvBulkUpdatePricingPolicy(@RequestParam(value = "api_key") String apiKey,
                                                   @RequestParam(value = "updateExistingPrice", required = false, defaultValue = "false") boolean updateExistingPrice,
                                                   @RequestBody String csvContent) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        List<String> errorList = new ArrayList<>();

        Reader subscriptionDataReader = new BufferedReader(new StringReader(csvContent));
        CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Subscription ID")) {
            errorList.add("'Subscription ID' column missing.");
        }

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Discount Percentage")) {
            errorList.add("'Discount Percentage' column missing.");
        }

        if (errorList.size() > 0) {
            return errorList;
        }

        List<String> messages = new ArrayList<>();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {

            try {
                String subscriptionContractIdStr = subscriptionDataRecord.get("Subscription ID");
                String discountPercentageStr = subscriptionDataRecord.get("Discount Percentage");

                if (!NumberUtils.isParsable(subscriptionContractIdStr) || !NumberUtils.isParsable(discountPercentageStr)) {
                    errorList.add("Invalid Subscription ID=" + subscriptionContractIdStr + " or Discount Percentage=" + discountPercentageStr);
                    continue;
                }

                Long subscriptionContactId = Long.parseLong(subscriptionContractIdStr);
                Double discountPercentage = Double.parseDouble(discountPercentageStr);


                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContactId);
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                List<SubscriptionContractQuery.Node> lineItems = requireNonNull(subscriptionContractQueryResponse
                    .getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

                boolean anyLineItemHasPricingPolicy = lineItems.stream().anyMatch(l -> l.getPricingPolicy().isPresent());

                if (anyLineItemHasPricingPolicy) {
                    messages.add("couldn't update contractId=" + subscriptionContactId + ". One of the items already have the pricing policy.");
                    continue;
                }

                List<AppstleCycle> cycles = new ArrayList<>();
                AppstleCycle appstleCycle = new AppstleCycle();
                appstleCycle.setAfterCycle(0);
                appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                appstleCycle.setValue(discountPercentage);
                cycles.add(appstleCycle);
                updatePricingPolicy(apiKey, subscriptionContractIdStr, cycles);


                messages.add("contractId=" + subscriptionContactId + " updated successfully.");
            } catch (Exception ex) {

            }

        }

        return messages;
    }

    @GetMapping("/update-line-item-discount")
    public void updateLineItemDiscount(@RequestParam("apiKey") String apiKey, @RequestParam("percentage") double percentage) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        try {
            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            while (isNextPage) {
                SubscriptionContractsLineItemsWithDiscountQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsWithDiscountQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsLineItemsWithDiscountQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                List<SubscriptionContractsLineItemsWithDiscountQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsLineItemsQueryResponse.getData()).map(SubscriptionContractsLineItemsWithDiscountQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsWithDiscountQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().map(SubscriptionContractsLineItemsWithDiscountQuery.Edge::getNode).collect(Collectors.toList());

                isNextPage = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsWithDiscountQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsWithDiscountQuery.SubscriptionContracts::getPageInfo).map(SubscriptionContractsLineItemsWithDiscountQuery.PageInfo::isHasNextPage).orElse(Boolean.FALSE);

                Optional<SubscriptionContractsLineItemsWithDiscountQuery.Edge> last = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsWithDiscountQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsWithDiscountQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsLineItemsWithDiscountQuery.Edge::getCursor).orElse(null);

                Set<Long> contractIds = subscriptionContracts.stream().map(s -> Long.parseLong(s.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                List<SubscriptionContractDetails> subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(contractIds);

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                for (SubscriptionContractsLineItemsWithDiscountQuery.Node subscriptionContract : subscriptionContracts) {
                    try {
                        i++;
                        log.info("iterating record i=" + i + " contractId=" + subscriptionContract.getId());

                        long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                        /*if (subscriptionContractId != 1670578331) {
                            continue;
                        }*/

                        if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                            continue;
                        }

                        List<SubscriptionContractsLineItemsWithDiscountQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractsLineItemsWithDiscountQuery.Edge1::getNode).collect(Collectors.toList());

                        List<SubscriptionContractsLineItemsWithDiscountQuery.Node2> discountNodes = subscriptionContract.getDiscounts().getEdges().stream().map(SubscriptionContractsLineItemsWithDiscountQuery.Edge2::getNode).collect(Collectors.toList());

                        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());
                        Response<Optional<SubscriptionContractUpdateMutation.Data>> subscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                        String draftId = requireNonNull(subscriptionContractUpdateMutationResponse.getData()).flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId()).get();

                        boolean isUpdate = false;

                        for (SubscriptionContractsLineItemsWithDiscountQuery.Node2 discountNode : discountNodes) {
                            if (discountNode.getValue() instanceof SubscriptionContractsLineItemsWithDiscountQuery.AsSubscriptionDiscountPercentageValue) {
                                SubscriptionContractsLineItemsWithDiscountQuery.AsSubscriptionDiscountPercentageValue percentageValue = (SubscriptionContractsLineItemsWithDiscountQuery.AsSubscriptionDiscountPercentageValue) discountNode.getValue();
                                if (percentageValue.getPercentage() == percentage) {

                                    isUpdate = true;

                                    for (SubscriptionContractsLineItemsWithDiscountQuery.Node1 subscriptionLineItem : subscriptionLineItems) {

                                        if (subscriptionLineItem.getPricingPolicy().isPresent()) {
                                            continue;
                                        }

                                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();
                                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();
                                        DecimalFormat df = new DecimalFormat("#.##");

                                        double finalPrice = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString()) * (((double) (100 - percentageValue.getPercentage()) / 100));

                                        SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput.builder().afterCycle(0).adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE).adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage((double) percentageValue.getPercentage()).build()).computedPrice(df.format(finalPrice)).build();
                                        cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                                        SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder().basePrice(df.format(finalPrice)).cycleDiscounts(cycleDiscounts).build();
                                        subscriptionLineUpdateInputBuilder.pricingPolicy(pricingPolicyInput);
                                        subscriptionLineUpdateInputBuilder.currentPrice(df.format(finalPrice));

                                        SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, subscriptionLineItem.getId(), subscriptionLineUpdateInput);
                                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> subscriptionDraftLineUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                                        if (subscriptionDraftLineUpdateMutationResponse.hasErrors()) {
                                        }

                                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = subscriptionDraftLineUpdateMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                                        }
                                    }

                                    SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(draftId, discountNode.getId());
                                    Response<Optional<SubscriptionDraftDiscountRemoveMutation.Data>> subscriptionDraftDiscountRemoveMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);

                                    if (!CollectionUtils.isEmpty(subscriptionDraftDiscountRemoveMutationResponse.getErrors())) {

                                    }
                                }
                            }
                        }

                        if (isUpdate) {
                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

                            if (optionalDraftCommitResponse.hasErrors()) {
                            }

                            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }

        } catch (Exception ex) {
        }
    }

    @GetMapping("/order-details/{orderId}")
    public Optional<Optional<OrderQuery.Order>> getOrderDetails(@PathVariable String orderId, @RequestParam("apiKey") String apiKey) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        OrderQuery orderQuery = new OrderQuery(ShopifyIdPrefix.ORDER_ID_PREFIX + orderId);
        Response<Optional<OrderQuery.Data>> orderQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderQuery);
        Optional<Optional<OrderQuery.Order>> order = orderQueryResponse.getData().map(d -> d.getOrder());
        return order;
    }


    @GetMapping("/update-selling-plan-id-for-contract-id")
    public void updateSellingPlanIdForSubscription(@RequestParam("apiKey") String apiKey, @RequestParam("sellingPlanId") Long sellingPlanId, @RequestParam("sellingPlanName") String sellingPlanName, @RequestParam("contractId") Long contractId) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

        SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractQueryResponse.getData().flatMap(SubscriptionContractQuery.Data::getSubscriptionContract).orElse(null);

        if (subscriptionContract == null) {
            return;
        }

        List<SubscriptionContractQuery.Node> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractQuery.Edge::getNode).collect(Collectors.toList());

        for (SubscriptionContractQuery.Node itemsWithoutSellingPlan : subscriptionLineItems) {

            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());

            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            String draftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId()).get();

            SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

            subscriptionLineUpdateInputBuilder.sellingPlanId(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX + sellingPlanId);


            subscriptionLineUpdateInputBuilder.sellingPlanName(sellingPlanName);


            SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                .build();

            SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, itemsWithoutSellingPlan.getId(), subscriptionLineUpdateInput);
            Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

            if (optionalMutationResponse.hasErrors()) {
                //throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = requireNonNull(optionalMutationResponse.getData())
                .map(d -> d.getSubscriptionDraftLineUpdate()
                    .map(SubscriptionDraftLineUpdateMutation.SubscriptionDraftLineUpdate::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            if (!optionalMutationResponseUserErrors.isEmpty()) {
                //throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
            }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                .getOptionalMutationResponse(subscriptionDraftCommitMutation);

            if (optionalDraftCommitResponse.hasErrors()) {
                //throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                //throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
            }
        }
    }


    @GetMapping("/update-selling-plan-ids")
    public void updateSellingPlanIds(@RequestParam("apiKey") String apiKey, @RequestParam("percentage") double percentage, @RequestParam("sellingPlanId") int sellingPlanId, @RequestParam("sellingPlanNameParam") String sellingPlanNameParam) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        try {
            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            while (isNextPage) {
                SubscriptionContractsLineItemsQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsLineItemsQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                List<SubscriptionContractsLineItemsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsLineItemsQueryResponse.getData()).map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().map(SubscriptionContractsLineItemsQuery.Edge::getNode).collect(Collectors.toList());

                isNextPage = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getPageInfo).map(SubscriptionContractsLineItemsQuery.PageInfo::isHasNextPage).orElse(Boolean.FALSE);

                Optional<SubscriptionContractsLineItemsQuery.Edge> last = subscriptionContractsLineItemsQueryResponse.getData().map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts).map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges).orElse(new ArrayList<>()).stream().reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsLineItemsQuery.Edge::getCursor).orElse(null);

                Set<Long> contractIds = subscriptionContracts.stream().map(s -> Long.parseLong(s.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                List<SubscriptionContractDetails> subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(contractIds);

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                for (SubscriptionContractsLineItemsQuery.Node subscriptionContract : subscriptionContracts) {
                    try {
                        i++;
                        log.info("iterating record i=" + i + " contractId=" + subscriptionContract.getId());

                        long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                        if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                            continue;
                        }

                        List<SubscriptionContractsLineItemsQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(d -> d.getNode()).collect(Collectors.toList());

                        boolean hasAnyItemWithSellingPlan = subscriptionLineItems.stream().map(SubscriptionContractsLineItemsQuery.Node1::getSellingPlanId).anyMatch(Optional::isPresent);

                        String sellingPlanToBeAssigned = ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX + sellingPlanId;
                        Optional<String> sellingPlanName = Optional.of(sellingPlanNameParam);

                        if (hasAnyItemWithSellingPlan) {
                            SubscriptionContractsLineItemsQuery.Node1 node1 = subscriptionLineItems.stream().filter(j -> j.getSellingPlanId().isPresent()).findFirst().get();
                            sellingPlanToBeAssigned = node1.getSellingPlanId().get();
                            sellingPlanName = node1.getSellingPlanName();
                        }

                        for (SubscriptionContractsLineItemsQuery.Node1 subscriptionLineItem : subscriptionLineItems) {
                            try {
                                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();
                                boolean isUpdateRequired = false;
                                if (subscriptionLineItem.getSellingPlanId().isEmpty()) {
                                    isUpdateRequired = true;

                                    subscriptionLineUpdateInputBuilder.sellingPlanId(sellingPlanToBeAssigned);


                                    if (sellingPlanName.isPresent()) {
                                        subscriptionLineUpdateInputBuilder.sellingPlanName(sellingPlanName.get());
                                    }
                                }

                                if (subscriptionLineItem.getPricingPolicy().isEmpty()) {
                                    isUpdateRequired = true;

                                    Double price = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString());


                                    ArrayList<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                                    DecimalFormat df = new DecimalFormat("#.##");

                                    SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput = SubscriptionPricingPolicyCycleDiscountsInput
                                        .builder()
                                        .afterCycle(0)
                                        .adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE)
                                        .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(percentage).build())
                                        .computedPrice(df.format(price))
                                        .build();

                                    cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);

                                    SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput.builder().basePrice(price).cycleDiscounts(cycleDiscounts).build();
                                    subscriptionLineUpdateInputBuilder.pricingPolicy(pricingPolicyInput);
                                }

                                if (isUpdateRequired) {
                                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());

                                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                                    String draftId = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId()).get();

                                    SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                                    SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, subscriptionLineItem.getId(), subscriptionLineUpdateInput);
                                    Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                                    if (optionalMutationResponse.hasErrors()) {
                                    }

                                    List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                    if (!optionalMutationResponseUserErrors.isEmpty()) {
                                    }

                                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

                                    if (optionalDraftCommitResponse.hasErrors()) {
                                    }

                                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                    }
                                }
                            } catch (Exception ex) {

                            }
                        }
                    } catch (Exception ex) {

                    }
                }
            }

        } catch (Exception ex) {
        }
    }

    /*@GetMapping("/update-all-selling-plan-ids")
    public void updateSellingPlanIds() {

        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnectionList) {
            asyncExecutor.execute(() -> {
                try {

                    String cusrsor = null;
                    boolean isNextPage = true;
                    String shop = socialConnection.getUserId();
                    ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                    while (isNextPage) {

                        SubscriptionContractsLineItemsQuery subscriptionContractsLineItemsQuery = new SubscriptionContractsLineItemsQuery(Input.fromNullable(cusrsor));
                        Response<Optional<SubscriptionContractsLineItemsQuery.Data>> subscriptionContractsLineItemsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsLineItemsQuery);

                        List<SubscriptionContractsLineItemsQuery.Node> subscriptionContracts =
                            requireNonNull(subscriptionContractsLineItemsQueryResponse.getData())
                                .map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts)
                                .map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges)
                                .orElse(new ArrayList<>()).stream()
                                .map(SubscriptionContractsLineItemsQuery.Edge::getNode)
                                .collect(Collectors.toList());

                        isNextPage = subscriptionContractsLineItemsQueryResponse
                            .getData()
                            .map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getPageInfo)
                            .map(SubscriptionContractsLineItemsQuery.PageInfo::isHasNextPage)
                            .orElse(Boolean.FALSE);

                        Optional<SubscriptionContractsLineItemsQuery.Edge> last = subscriptionContractsLineItemsQueryResponse
                            .getData()
                            .map(SubscriptionContractsLineItemsQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsLineItemsQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream().reduce((first, second) -> second);

                        cusrsor = last.map(SubscriptionContractsLineItemsQuery.Edge::getCursor).orElse(null);

                        Set<Long> contractIds = subscriptionContracts.stream().map(s -> Long.parseLong(s.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                        List<SubscriptionContractDetails> subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(contractIds);

                        Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(s -> s.getSubscriptionContractId(), s -> s));

                        for (SubscriptionContractsLineItemsQuery.Node subscriptionContract : subscriptionContracts) {

                            long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                            if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                                continue;
                            }

                            List<SubscriptionContractsLineItemsQuery.Node1> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(d -> d.getNode()).collect(Collectors.toList());

                            boolean hasAnyItemWithSellingPlan = subscriptionLineItems.stream()
                                .map(SubscriptionContractsLineItemsQuery.Node1::getSellingPlanId)
                                .anyMatch(Optional::isPresent);

                            boolean hasAnyItemWithoutSellingPlan = subscriptionLineItems.stream()
                                .map(SubscriptionContractsLineItemsQuery.Node1::getSellingPlanId)
                                .anyMatch(Optional::isEmpty);

                            Optional<SubscriptionContractsLineItemsQuery.Node1> nodeOptional = subscriptionLineItems.stream()
                                .filter(j -> j.getSellingPlanId().isPresent())
                                .filter(j -> !j.getPricingPolicy().map(SubscriptionContractsLineItemsQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>()).isEmpty())
                                .findFirst();

                            if (hasAnyItemWithSellingPlan) {
                                if (nodeOptional.isEmpty()) {
                                    log.info("subscriptionContractId=" + subscriptionContractId + " has no pricing policy. shop=" + shop);
                                }
                            }

                            if (hasAnyItemWithSellingPlan && hasAnyItemWithoutSellingPlan) {

                                if (nodeOptional.isEmpty()) {
                                    nodeOptional = subscriptionLineItems.stream()
                                        .filter(j -> j.getSellingPlanId().isPresent())
                                        .findFirst();
                                }

                                SubscriptionContractsLineItemsQuery.Node1 node = nodeOptional.get();

                                List<SubscriptionContractsLineItemsQuery.Node1> itemsWithoutSellingPlans = subscriptionLineItems.stream()
                                    .filter(s -> s.getSellingPlanId().isEmpty()).collect(Collectors.toList());

                                for (SubscriptionContractsLineItemsQuery.Node1 itemsWithoutSellingPlan : itemsWithoutSellingPlans) {

                                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContract.getId());

                                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                                    String draftId = optionalResponse.getData()
                                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                                        .map(draft -> draft.get().getId()).get();

                                    SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                                    subscriptionLineUpdateInputBuilder.sellingPlanId(node.getSellingPlanId().get());

                                    Double price = Double.parseDouble(itemsWithoutSellingPlan.getCurrentPrice().getAmount().toString());

                                    if (node.getSellingPlanName().isPresent()) {
                                        subscriptionLineUpdateInputBuilder.sellingPlanName(node.getSellingPlanName().get());
                                    }

                                    List<SubscriptionContractsLineItemsQuery.CycleDiscount> existingCycleDiscounts = node.getPricingPolicy().map(SubscriptionContractsLineItemsQuery.PricingPolicy::getCycleDiscounts).orElse(new ArrayList<>());

                                    if (!existingCycleDiscounts.isEmpty()
                                        && existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentType() == SellingPlanPricingPolicyAdjustmentType.PERCENTAGE)
                                        && existingCycleDiscounts.stream().allMatch(s -> s.getAdjustmentValue() instanceof SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue)) {

                                        ArrayList<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscounts = new ArrayList<>();

                                        DecimalFormat df = new DecimalFormat("#.##");

                                        for (SubscriptionContractsLineItemsQuery.CycleDiscount existingCycleDiscount : existingCycleDiscounts) {
                                            SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue adjustmentValue = (SubscriptionContractQuery.AsSellingPlanPricingPolicyPercentageValue) existingCycleDiscount.getAdjustmentValue();
                                            SubscriptionPricingPolicyCycleDiscountsInput subscriptionPricingPolicyCycleDiscountsInput =
                                                SubscriptionPricingPolicyCycleDiscountsInput.builder()
                                                    .afterCycle(existingCycleDiscount.getAfterCycle())
                                                    .adjustmentType(existingCycleDiscount.getAdjustmentType())
                                                    .adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(adjustmentValue.getPercentage()).build())
                                                    .computedPrice(df.format(price))
                                                    .build();
                                            cycleDiscounts.add(subscriptionPricingPolicyCycleDiscountsInput);
                                        }

                                        SubscriptionPricingPolicyInput pricingPolicyInput = SubscriptionPricingPolicyInput
                                            .builder()
                                            .basePrice(price)
                                            .cycleDiscounts(cycleDiscounts)
                                            .build();
                                        subscriptionLineUpdateInputBuilder.pricingPolicy(pricingPolicyInput);
                                    }

                                    SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                                        //.quantity(itemsWithoutSellingPlan.getQuantity())
                                        //.productVariantId(itemsWithoutSellingPlan.getVariantId())
                                        //.currentPrice(price)
                                        .build();

                                    SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, itemsWithoutSellingPlan.getId(), subscriptionLineUpdateInput);
                                    Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                                    if (optionalMutationResponse.hasErrors()) {
                                        //throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                                    }

                                    List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                    if (!optionalMutationResponseUserErrors.isEmpty()) {
                                        //throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                                    }

                                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                                    if (optionalDraftCommitResponse.hasErrors()) {
                                        //throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                                    }

                                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                        //throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                                    }
                                }

                                log.info("subscriptionContractId=" + subscriptionContractId + " updated.");
                            }
                        }
                    }
                } catch (Exception ex) {
                }
            });

        }
    }*/

    @Autowired
    private SubscriptionBundlingRepository subscriptionBundlingRepository;

    @Autowired
    private SubscriptionBundleSettingsRepository subscriptionBundleSettingsRepository;

    /*@GetMapping("/update-subscription-bundle-settings")
    public void updateSubscriptionBundleSettings() {

        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        List<SubscriptionBundling> subscriptionBundlingRepositoryAll = subscriptionBundlingRepository.findAll();

        Map<String, List<SubscriptionBundling>> collect = subscriptionBundlingRepositoryAll.stream().collect(Collectors.groupingBy(s -> s.getShop()));

        for (Map.Entry<String, List<SubscriptionBundling>> entry : collect.entrySet()) {
            asyncExecutor.execute(() -> {
                try {
                    String shop = entry.getKey();
                    List<SubscriptionBundling> subscriptionBundlingList = entry.getValue();

                    if (!subscriptionBundlingList.isEmpty()) {
                        Optional<SubscriptionBundleSettings> subscriptionBundleSettingsOptional = subscriptionBundleSettingsRepository.findByShop(shop);

                        if (subscriptionBundleSettingsOptional.isPresent()) {
                            SubscriptionBundleSettings subscriptionBundleSettings = subscriptionBundleSettingsOptional.get();

                            for (SubscriptionBundling subscriptionBundling : subscriptionBundlingList) {
                                subscriptionBundling.setBundleRedirect(BuildABoxRedirect.valueOf(subscriptionBundleSettings.getBundleRedirect().toString()));
                                subscriptionBundling.setCustomRedirectURL(subscriptionBundleSettings.getCustomRedirectURL());
                                subscriptionBundlingRepository.save(subscriptionBundling);
                            }
                        }
                    }

                } catch (Exception ex) {
                }
            });
        }
    }*/

    /*@GetMapping("/fix-subscription-status-issue")
    public void fixSubscriptionStatusIssue() {

        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnectionList) {
            asyncExecutor.execute(() -> {
                try {
                    String shop = socialConnection.getUserId();

                    ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, socialConnection.getAccessToken(), 2);

                    String cusrsor = null;
                    boolean isNextPage = true;

                    while (isNextPage) {
                        SubscriptionContractsBriefQuery subscriptionContractsBriefQuery = new SubscriptionContractsBriefQuery(Input.fromNullable(cusrsor));
                        Response<Optional<SubscriptionContractsBriefQuery.Data>> subscriptionContractsBriefQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsBriefQuery);

                        List<SubscriptionContractsBriefQuery.Node> shopifySubscriptionContracts =
                            requireNonNull(subscriptionContractsBriefQueryResponse
                                .getData())
                                .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                                .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getEdges)
                                .orElse(new ArrayList<>()).stream()
                                .map(SubscriptionContractsBriefQuery.Edge::getNode)
                                .collect(Collectors.toList());


                        isNextPage = subscriptionContractsBriefQueryResponse.getData().map(e -> e.getSubscriptionContracts()).map(f -> f.getPageInfo()).map(f -> f.isHasNextPage()).orElse(Boolean.FALSE);

                        Optional<SubscriptionContractsBriefQuery.Edge> last = subscriptionContractsBriefQueryResponse
                            .getData()
                            .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream().reduce((first, second) -> second);

                        cusrsor = last.map(SubscriptionContractsBriefQuery.Edge::getCursor).orElse(null);

                        for (SubscriptionContractsBriefQuery.Node shopifySubscriptionContract : shopifySubscriptionContracts) {
                            long subscriptionContractId = Long.parseLong(shopifySubscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));
                            Optional<SubscriptionContractDetails> optionalSubscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId);
                            if (optionalSubscriptionContractDetails.isEmpty()) {
                                continue;
                            }

                            SubscriptionContractDetails subscriptionContractDetails = optionalSubscriptionContractDetails.get();
                            SubscriptionContractSubscriptionStatus subscriptionContractSubscriptionStatus = shopifySubscriptionContract.getStatus();

                            if (subscriptionContractDetails.getStatus() != null && !subscriptionContractSubscriptionStatus.rawValue().toLowerCase().equalsIgnoreCase(subscriptionContractDetails.getStatus())) {

                                if ((subscriptionContractSubscriptionStatus == SubscriptionContractSubscriptionStatus.PAUSED || subscriptionContractSubscriptionStatus == SubscriptionContractSubscriptionStatus.CANCELLED) && subscriptionContractDetails.getStatus().equals("active")) {
                                    subscriptionContractDetails.setStatus(subscriptionContractSubscriptionStatus.rawValue().toLowerCase());
                                    subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                                }

                                if (subscriptionContractSubscriptionStatus == SubscriptionContractSubscriptionStatus.ACTIVE && subscriptionContractDetails.getStatus().equals("paused")) {
                                    subscriptionContractDetails.setStatus(subscriptionContractSubscriptionStatus.rawValue().toLowerCase());
                                    subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                                }

                                log.info("subscriptionContractId=" + subscriptionContractDetails.getSubscriptionContractId() + " has status discrepancy. shop=" + shop + " DBStatus=" + subscriptionContractDetails.getStatus() + " ShopifyStatus=" + subscriptionContractSubscriptionStatus.rawValue());
                            }

                        }
                    }
                } catch (Exception ex) {
                }
            });
        }
    }*/

    @GetMapping("/deleted-subscriptions-data")
    public void deletedSubscriptionsData(@RequestParam("api_key") String apiKey, @RequestParam("email") String email) throws IOException {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        File tempFile = null;

        try {
            String[] headers = {
                "ID",
                "Status",
                "Customer ID",
                "Customer Name",
                "Customer email",
                "Customer phone number",
                "Delivery type",
                "Delivery first name",
                "Delivery last name",
                "Delivery address 1",
                "Delivery address 2",
                "Delivery province code",
                "Delivery city",
                "Delivery zip",
                "Delivery country code",
                "Delivery phone",
                "Delivery company",
                "Shipping Price",
                "Delivery price currency",
                "Created at",
                "Next order date",
                "Billing interval type",
                "Billing interval count",
                "Billing min cycles",
                "Billing max cycles",
                "Delivery interval type",
                "Delivery interval count",
                "Payment ID",
                "Payment method",
                "Billing full name",
                "Payment method brand",
                "Payment method expiry year",
                "Payment method expiry month",
                "Payment method last digits",
                "Line title",
                "Line SKU",
                "Line variant quantity",
                "Line variant price",
                "Line price currency",
                "Line product ID",
                "Line variant ID",
                "Line selling plan ID",
                "Line selling plan name",
                "Line Attributes",
                "Subscription Attributes"
            };

            tempFile = File.createTempFile("Deleted-Subscriptions-Data-Export", ".csv");

            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            String cusrsor = null;
            boolean isNextPage = true;

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                while (isNextPage) {

                    try {

                        SubscriptionContractsQuery subscriptionContractsQuery = new SubscriptionContractsQuery(Input.fromNullable(cusrsor));
                        Response<Optional<SubscriptionContractsQuery.Data>> subscriptionContractsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsQuery);

                        List<SubscriptionContractsQuery.Node> shopifySubscriptionContracts =
                            requireNonNull(subscriptionContractsQueryResponse
                                .getData())
                                .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                                .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                                .orElse(new ArrayList<>()).stream()
                                .map(SubscriptionContractsQuery.Edge::getNode)
                                .collect(Collectors.toList());

                        isNextPage = subscriptionContractsQueryResponse.getData().map(e -> e.getSubscriptionContracts()).map(f -> f.getPageInfo()).map(f -> f.isHasNextPage()).orElse(Boolean.FALSE);

                        Optional<SubscriptionContractsQuery.Edge> last = subscriptionContractsQueryResponse
                            .getData()
                            .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream().reduce((first, second) -> second);

                        cusrsor = last.map(SubscriptionContractsQuery.Edge::getCursor).orElse(null);

                        for (SubscriptionContractsQuery.Node shopifySubscriptionContract : shopifySubscriptionContracts) {
                            try {

                                long subscriptionContractId = Long.parseLong(shopifySubscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));
                                Optional<SubscriptionContractDetails> optionalSubscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId);

                                if (optionalSubscriptionContractDetails.isPresent()) {
                                    continue;
                                }

                                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(shopifySubscriptionContract.getId());
                                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractQueryResponse.getData().flatMap(SubscriptionContractQuery.Data::getSubscriptionContract).orElse(null);

                                if (subscriptionContract == null) {
                                    continue;
                                }

                                List<SubscriptionContractQuery.Node> subscriptionLineItems = subscriptionContract.getLines().getEdges().stream().map(SubscriptionContractQuery.Edge::getNode).collect(Collectors.toList());
                                Optional<ShippingAddressModel> shippingAddressModel = commonUtils.getContractShippingAddress(subscriptionContract);

                                String subscriptionAttributes = "";

                                if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(subscriptionContract.getCustomAttributes())) {
                                    StringBuilder sb = new StringBuilder();
                                    subscriptionContract.getCustomAttributes().forEach(customAttribute -> {
                                        sb.append("{\"").append(customAttribute.getKey())
                                            .append("\": \"").append(customAttribute.getValue().orElse(""))
                                            .append("\"}, ");
                                    });
                                    subscriptionAttributes = sb.substring(0, sb.length() - 2);
                                }

                                final String finalSubscriptionAttributes = subscriptionAttributes;

                                subscriptionLineItems.forEach(product -> {

                                    try {

                                        String lastFourDigits = "";
                                        Optional<SubscriptionContractQuery.BillingAddress> billingAddressOptional = null;
                                        Optional<SubscriptionContractQuery.BillingAddress1> paypalBillingAddressOptional = null;
                                        String billingFullName = "";
                                        String cardBrand = "";
                                        String billingAddress1 = null;
                                        String billingCity = null;
                                        String billingProvinceCode = null;
                                        String billingZip = null;
                                        Integer expirationMonth = null;
                                        Integer expirationYear = null;
                                        String paymentId = null;
                                        String lineAttributes = "";
                                        String paymentMethodName = "";

                                        if (subscriptionContract.getCustomerPaymentMethod().isPresent()) {
                                            Optional<SubscriptionContractQuery.Instrument> instrument = subscriptionContract.getCustomerPaymentMethod().map(d -> d.getInstrument().get());

                                            paymentId = subscriptionContract.getCustomerPaymentMethod().get().getId();

                                            if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerPaypalBillingAgreement")) {
                                                lastFourDigits = "";

                                                paypalBillingAddressOptional = subscriptionContract.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) cpi).getBillingAddress()));

                                                billingFullName = "";

                                                cardBrand = "";

                                                billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getAddress1).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getCity).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getProvinceCode).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerPaypalBillingAgreement) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress1::getZip).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                paymentMethodName = "CustomerPaypalBillingAgreement";
                                            } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerCreditCard")) {

                                                lastFourDigits = subscriptionContract.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getLastDigits())).get();

                                                billingAddressOptional = subscriptionContract.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .flatMap(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getBillingAddress()));

                                                billingFullName = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getName()).orElse(org.apache.commons.lang3.StringUtils.EMPTY);

                                                cardBrand = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBrand()).orElse(org.apache.commons.lang3.StringUtils.EMPTY);

                                                billingAddress1 = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getAddress1).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingCity = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getCity).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingProvinceCode = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getProvinceCode).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                billingZip = instrument.map(i -> ((SubscriptionContractQuery.AsCustomerCreditCard) i).getBillingAddress().flatMap(SubscriptionContractQuery.BillingAddress::getZip).orElse(org.apache.commons.lang3.StringUtils.EMPTY)).get();

                                                expirationMonth = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getExpiryMonth()).get();

                                                expirationYear = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerCreditCard) cpi).getExpiryYear()).get();

                                                paymentMethodName = "CustomerCreditCard";

                                            } else if (instrument.isPresent() && instrument.get().get__typename().equals("CustomerShopPayAgreement")) {
                                                lastFourDigits = subscriptionContract.getCustomerPaymentMethod()
                                                    .flatMap(cp -> cp.getInstrument()
                                                        .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getLastDigits())).get();

                                                expirationMonth = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getExpiryMonth()).get();

                                                expirationYear = instrument
                                                    .map(cpi -> ((SubscriptionContractQuery.AsCustomerShopPayAgreement) cpi).getExpiryYear()).get();

                                                paymentMethodName = "CustomerShopPayAgreement";
                                            }
                                        }

                                        if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(product.getCustomAttributes())) {
                                            StringBuilder sb = new StringBuilder();
                                            product.getCustomAttributes().forEach(customAttribute -> {
                                                sb.append("{\"").append(customAttribute.getKey())
                                                    .append("\": \"").append(customAttribute.getValue().orElse(""))
                                                    .append("\"}, ");
                                            });
                                            lineAttributes = sb.substring(0, sb.length() - 2);
                                        }

                                        csvPrinter.printRecord(
                                            Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, "")),
                                            subscriptionContract.getStatus(),
                                            Long.parseLong(subscriptionContract.getCustomer().get().getId().replace("gid://shopify/Customer/", "")),
                                            subscriptionContract.getCustomer().get().getDisplayName(),
                                            subscriptionContract.getCustomer().get().getEmail().orElse(""),
                                            subscriptionContract.getCustomer().get().getPhone().orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_type).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_first_name).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_last_name).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_address1).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_address2).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_province_code).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_city).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_zip).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_country_code).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_phone).orElse(""),
                                            shippingAddressModel.map(ShippingAddressModel::getShipping_company).orElse(""),
                                            subscriptionContract.getDeliveryPrice().getAmount(),
                                            subscriptionContract.getDeliveryPrice().getCurrencyCode(),
                                            subscriptionContract.getCreatedAt(),
                                            Optional.of(subscriptionContract.getNextBillingDate().get()).orElse(""),
                                            subscriptionContract.getBillingPolicy().getInterval(),
                                            subscriptionContract.getBillingPolicy().getIntervalCount(),
                                            subscriptionContract.getBillingPolicy().getMinCycles().map(Object::toString).orElse(""),
                                            subscriptionContract.getBillingPolicy().getMaxCycles().map(Object::toString).orElse(""),
                                            subscriptionContract.getDeliveryPolicy().getInterval(),
                                            subscriptionContract.getDeliveryPolicy().getIntervalCount(),
                                            Optional.ofNullable(paymentId).orElse("").replace(ShopifyIdPrefix.CUSTOMER_PAYMENT_METHOD_ID_PREFIX, ""),
                                            paymentMethodName,
                                            billingFullName,
                                            cardBrand,
                                            Optional.ofNullable(expirationYear).map(Object::toString).orElse(""),
                                            Optional.ofNullable(expirationMonth).map(Object::toString).orElse(""),
                                            lastFourDigits,
                                            product.getVariantTitle().isPresent()
                                                && !product.getVariantTitle().get().trim().toLowerCase().equals("default")
                                                && !product.getVariantTitle().get().trim().equals("-") ? product.getTitle() + " - " + product.getVariantTitle().orElse("") : product.getTitle(),
                                            product.getSku().orElse(""),
                                            product.getQuantity(),
                                            product.getCurrentPrice().getAmount(),
                                            product.getCurrentPrice().getCurrencyCode(),
                                            product.getProductId().orElse("").replace(PRODUCT_ID_PREFIX, ""),
                                            product.getVariantId().orElse("").replace(PRODUCT_VARIANT_ID_PREFIX, ""),
                                            product.getSellingPlanId().orElse("").replace("gid://shopify/SellingPlan/", ""),
                                            product.getSellingPlanName().orElse(""),
                                            lineAttributes,
                                            finalSubscriptionAttributes
                                        );
                                    } catch (IOException e) {
                                        log.error("ex=" + ExceptionUtils.getStackTrace(e));
                                    }
                                });
                            } catch (Exception e) {
                                log.error("ex=" + ExceptionUtils.getStackTrace(e));
                            }
                        }

                    } catch (Exception ex) {
                        log.error("ex=" + ExceptionUtils.getStackTrace(ex));
                    }
                }
            }
        } catch (Exception ex) {
            log.error("ex=" + ExceptionUtils.getStackTrace(ex));
        }
        mailgunService.sendEmailWithAttachment(tempFile, "Deleted Subscriptions Contracts Exported", "Check attached csv file for your deleted subscriptions data list", "subscription-support@appstle.com", shop, email);

    }


    @Value("${shop_overrides}")
    private String shopOverrides;
    @GetMapping("/shop-overrides")
    public String getShopOverrides() {
        return shopOverrides;
    }

    @GetMapping("/update-webhook-for-shop")
    public void updateWebhookForShop(@RequestParam("api_key") String apiKey, @RequestParam("callbackUrl") String callbackUrl) {

        Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
        String shop = shopInfoOptional.get().getShop();

        SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();

        try {
            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            WebhookSubscriptionsQuery webhookSubscriptionsQuery = new WebhookSubscriptionsQuery();
            Response<Optional<WebhookSubscriptionsQuery.Data>> webhookSubscriptionsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(webhookSubscriptionsQuery);

            List<WebhookSubscriptionsQuery.Node> webhooks = requireNonNull(webhookSubscriptionsQueryResponse.getData())
                .map(WebhookSubscriptionsQuery.Data::getWebhookSubscriptions)
                .map(WebhookSubscriptionsQuery.WebhookSubscriptions::getEdges)
                .orElse(new ArrayList<>())
                .stream()
                .map(WebhookSubscriptionsQuery.Edge::getNode)
                .collect(Collectors.toList());

            for (WebhookSubscriptionsQuery.Node webhook : webhooks) {
                WebhookSubscriptionTopic topic = webhook.getTopic();

                if (!topic.equals(WebhookSubscriptionTopic.APP_UNINSTALLED)) {

                    WebhookSubscriptionInput webhookSubscriptionInput = WebhookSubscriptionInput.builder().callbackUrl(callbackUrl).build();
                    WebhookSubscriptionUpdateMutation webhookSubscriptionUpdateMutation = new WebhookSubscriptionUpdateMutation(webhook.getId(), webhookSubscriptionInput);
                    Response<Optional<WebhookSubscriptionUpdateMutation.Data>> webhookSubscriptionUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(webhookSubscriptionUpdateMutation);

                    if (webhookSubscriptionUpdateMutationResponse.hasErrors()) {
                        log.info("error=" + webhookSubscriptionUpdateMutationResponse.getErrors().get(0).getMessage() + " shop=" + shop);
                        continue;
                    }

                    List<WebhookSubscriptionUpdateMutation.UserError> webhookSubscriptionUpdateMutationResponseUserErrors =
                        requireNonNull(webhookSubscriptionUpdateMutationResponse.getData())
                            .map(d -> d.getWebhookSubscriptionUpdate()
                                .map(WebhookSubscriptionUpdateMutation.WebhookSubscriptionUpdate::getUserErrors)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>());

                    if (!webhookSubscriptionUpdateMutationResponseUserErrors.isEmpty()) {
                        log.info("error=" + webhookSubscriptionUpdateMutationResponseUserErrors.get(0).getMessage() + " shop=" + shop);
                        continue;
                    }

                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
                    shopInfoDTO.setShopifyWebhookUrl(callbackUrl);
                    shopInfoService.save(shopInfoDTO);
                    String a = "b";

                }
            }

            log.info("shop=" + shop + " callbackUrl=" + callbackUrl);

        } catch (Exception ex) {
        }
    }


    @GetMapping("/get-webhooks-for-shop")
    public List<WebhookSubscriptionsQuery.Node> getWebhooksForShop(String apiKey) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        return webhookUtils.getWebhooks(shop);
    }

    @GetMapping("/correct-webhooks")
    public void updateWebhooksForShop() {

        List<ShopInfoDTO> shopInfoList = shopInfoService.findAll();

        for(ShopInfoDTO shopInfo: shopInfoList) {
            try {
                ShopifyGraphqlClient graphqlClient = commonUtils.prepareShopifyGraphqlClient(shopInfo.getShop());

                List<WebhookSubscriptionsQuery.Node> webhooks = webhookUtils.getWebhooks(graphqlClient);

                for (WebhookSubscriptionsQuery.Node webhook: webhooks) {
                    if(webhook.getTopic() == WebhookSubscriptionTopic.THEMES_CREATE
                        || webhook.getTopic() == WebhookSubscriptionTopic.THEMES_UPDATE
                        || webhook.getTopic() == WebhookSubscriptionTopic.THEMES_PUBLISH) {
                        webhookUtils.deleteWebhook(graphqlClient, webhook.getId());
                    }
                }

                String subscriptionWebhookUrl = shopInfo.getShopifyWebhookUrl();

//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_CONTRACTS_CREATE, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_CONTRACTS_UPDATE, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_BILLING_ATTEMPTS_SUCCESS, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_BILLING_ATTEMPTS_FAILURE, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.SUBSCRIPTION_BILLING_ATTEMPTS_CHALLENGED, subscriptionWebhookUrl);
//
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.CUSTOMER_PAYMENT_METHODS_CREATE, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.CUSTOMER_PAYMENT_METHODS_UPDATE, subscriptionWebhookUrl);
//        webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.CUSTOMER_PAYMENT_METHODS_REVOKE, subscriptionWebhookUrl);

                webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.THEMES_CREATE, subscriptionWebhookUrl);
                webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.THEMES_UPDATE, subscriptionWebhookUrl);
                webhookUtils.createWebhook(graphqlClient, WebhookSubscriptionTopic.THEMES_PUBLISH, subscriptionWebhookUrl);

            } catch (Exception e) {

            }
        }

    }

    @Autowired
    private OauthController oauthController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/regenerate-password")
    public void regeneratePassword(String shop) {

        Optional<User> optionalUser = userRepository.findOneByLogin(shop);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String random = RandomStringUtils.random(10, true, true);
            String encryptedPassword = passwordEncoder.encode(random);
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            oauthController.mayBeTryAddingItemToDynamo(user.getLogin(), random);
        }
    }


    /*@GetMapping("/update-passwords")
    public void updatePasswords() {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getLogin().equals("utterly-rawsome-ltd.myshopify.com")) {
                asyncExecutor.execute(() -> {
                    String random = RandomStringUtils.random(10, true, true);
                    String encryptedPassword = passwordEncoder.encode(random);
                    user.setPassword(encryptedPassword);
                    userRepository.save(user);
                    oauthController.mayBeTryAddingItemToDynamo(user.getLogin(), random);
                });
            }

        }
    }


    @GetMapping("/update-webhook")
    public void updateWebhook() {

        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            String username = socialConnection.getUserId();
            Optional<User> oneByLogin = userRepository.findOneByLogin(username);
            if (oneByLogin.isPresent()) {
                User user = oneByLogin.get();
                String random = RandomStringUtils.random(10, true, true);
                String encryptedPassword = passwordEncoder.encode(random);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                oauthController.mayBeTryAddingItemToDynamo(username, random);
            }
        }
    }*/

    /*@GetMapping("/fix-prepaid-pricing")
    public void fixPrePaidPricing() throws Exception {

        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnectionList) {
            asyncExecutor.execute(() -> {
                try {
                    String shop = socialConnection.getUserId();
                    List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByShop(shop);

                    String accessToken = socialConnection.getAccessToken();

                    ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

                    for (SubscriptionContractDetails subscriptionContractDetail : subscriptionContractDetails) {

                        if (subscriptionContractDetail.getBillingPolicyIntervalCount().equals(subscriptionContractDetail.getDeliveryPolicyIntervalCount())) {
                            continue;
                        }

                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetail.getGraphSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                        List<SubscriptionContractQuery.Node> subscriptionLineItems = Objects.requireNonNull(subscriptionContractQueryResponse
                            .getData())
                            .map(d -> d.getSubscriptionContract()
                                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                                .map(SubscriptionContractQuery.Lines::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .collect(Collectors.toList());

                        for (SubscriptionContractQuery.Node subscriptionLineItem : subscriptionLineItems) {
                            double subscriptionLinePrice = Double.parseDouble(subscriptionLineItem.getCurrentPrice().getAmount().toString());
                            double subscriptionLineItemUnitPrice = subscriptionLinePrice / (subscriptionContractDetail.getBillingPolicyIntervalCount() / subscriptionContractDetail.getDeliveryPolicyIntervalCount());

                            if (subscriptionLineItem.getVariantId().isPresent()) {
                                ProductVariantQuery productVariantQuery = new ProductVariantQuery(subscriptionLineItem.getVariantId().get());
                                Response<Optional<ProductVariantQuery.Data>> productVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                                Double variantPrice = Objects.requireNonNull(productVariantQueryResponse
                                    .getData())
                                    .flatMap(d -> d.getProductVariant()
                                        .map(ProductVariantQuery.ProductVariant::getPrice)
                                        .map(Object::toString)
                                        .map(Double::parseDouble))
                                    .orElse(null);

                                double diff1 = Math.abs(variantPrice - subscriptionLineItemUnitPrice);
                                double diff2 = Math.abs(variantPrice - subscriptionLinePrice);

                                if (diff2 < diff1) {
                                    String c = "b";

                                    if (diff2 == 0) {
                                        subscriptionContractUpdateLineItem(subscriptionContractDetail.getSubscriptionContractId(), shop, subscriptionLineItem.getQuantity(), subscriptionLineItem.getVariantId().orElse(""), subscriptionLineItem.getId(), subscriptionLinePrice * (subscriptionContractDetail.getBillingPolicyIntervalCount() / subscriptionContractDetail.getDeliveryPolicyIntervalCount()), "", ActivityLogEventSource.MERCHANT_PORTAL);
                                        log.info("subscriptionContractId=" + subscriptionContractDetail.getSubscriptionContractId() + " can be fixed. shop=" + shop);
                                    } else {
                                        //log.info("subscriptionContractId=" + subscriptionContractDetail.getSubscriptionContractId() + " diff2=" + diff2 + " diff=" + diff1);
                                    }
                                } else {
                                    //log.info("subscriptionContractId=" + subscriptionContractDetail.getSubscriptionContractId() + " diff1=" + diff1 + " diff2=" + diff2);
                                }

                                String a = "b";
                            } else {
                                String b = "c";
                            }
                        }
                    }
                } catch (Exception ex) {

                }
            });
        }

    }*/

    @Autowired
    private SubscriptionBillingAttemptRepository subscriptionBillingAttemptRepository;


    /*@GetMapping("/bulk-customer-portal-settings")
    public void updateCustomerPortalSettings() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            String shop = socialConnection.getUserId();

            CustomerPortalSettingsDTO customerPortalSettingsDTO = customerPortalSettingsService.findByShop(shop).orElse(new CustomerPortalSettingsDTO());

            customerPortalSettingsDTO.setShop(shop);

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getOrderFrequencyText()))
                customerPortalSettingsDTO.setOrderFrequencyText("Order frequency");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getTotalProductsText()))
                customerPortalSettingsDTO.setTotalProductsText("Total Products");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getNoSubscriptionMessage()))
                customerPortalSettingsDTO.setNoSubscriptionMessage("Subscription");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getNextOrderText()))
                customerPortalSettingsDTO.setNextOrderText("Next Order");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getStatusText()))
                customerPortalSettingsDTO.setStatusText("Status");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getSubscriptionNoText()))
                customerPortalSettingsDTO.setSubscriptionNoText("Subscription");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCancelSubscriptionBtnText()))
                customerPortalSettingsDTO.setCancelSubscriptionBtnText("Cancel Subscription");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getNoSubscriptionMessage()))
                customerPortalSettingsDTO.setNoSubscriptionMessage("No subscriptions found. Once you subscribe, you can see it here.");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getUpdatePaymentMessage()))
                customerPortalSettingsDTO.setUpdatePaymentMessage("The request has been sent to your email for change your payment details. If you do not receive mail send another request.");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCardLastFourDigitText()))
                customerPortalSettingsDTO.setCardLastFourDigitText("Card Last 4 Digit");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCardExpiryText()))
                customerPortalSettingsDTO.setCardExpiryText("Card Expiry");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCardHolderNameText()))
                customerPortalSettingsDTO.setCardHolderNameText("Card Holder Name");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCardTypeText()))
                customerPortalSettingsDTO.setCardTypeText("Card Type");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getPaymentMethodTypeText()))
                customerPortalSettingsDTO.setPaymentMethodTypeText("Payment Method Type");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCancelAccordionTitle()))
                customerPortalSettingsDTO.setCancelAccordionTitle("Cancel Subscription");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getPaymentDetailAccordionTitle()))
                customerPortalSettingsDTO.setPaymentDetailAccordionTitle("Payment Details");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getUpcomingOrderAccordionTitle()))
                customerPortalSettingsDTO.setUpcomingOrderAccordionTitle("Upcoming Order");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getPaymentInfoText()))
                customerPortalSettingsDTO.setPaymentInfoText("Payment Info");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getUpdatePaymentBtnText()))
                customerPortalSettingsDTO.setUpdatePaymentBtnText("Update Payment");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getNextOrderDateLbl()))
                customerPortalSettingsDTO.setNextOrderDateLbl("Next Order Date");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getStatusLbl()))
                customerPortalSettingsDTO.setStatusLbl("Status");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getQuantityLbl()))
                customerPortalSettingsDTO.setQuantityLbl("Quantity");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getAmountLbl()))
                customerPortalSettingsDTO.setAmountLbl("Amount");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getOrderNoLbl()))
                customerPortalSettingsDTO.setOrderNoLbl("ORDER");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getEditFrequencyBtnText()))
                customerPortalSettingsDTO.setEditFrequencyBtnText("Edit Frequency");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getCancelFreqBtnText()))
                customerPortalSettingsDTO.setCancelFreqBtnText("Cancel");

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getUpdateFreqBtnText()))
                customerPortalSettingsDTO.setUpdateFreqBtnText("Update");

            if (customerPortalSettingsDTO.isCancelSub() == null) {
                customerPortalSettingsDTO.setCancelSub(true);
            }

            if (customerPortalSettingsDTO.isChangeNextOrderDate() == null) {
                customerPortalSettingsDTO.setChangeNextOrderDate(true);
            }

            if (customerPortalSettingsDTO.isChangeOrderFrequency() == null) {
                customerPortalSettingsDTO.setChangeOrderFrequency(true);
            }

            if (customerPortalSettingsDTO.isCreateAdditionalOrder() == null) {
                customerPortalSettingsDTO.setCreateAdditionalOrder(true);
            }

            if (customerPortalSettingsDTO.isPauseResumeSub() == null) {
                customerPortalSettingsDTO.setPauseResumeSub(true);
            }

            if (StringUtils.isEmpty(customerPortalSettingsDTO.getOpenBadgeText()))
                customerPortalSettingsDTO.setOpenBadgeText("Open");

            customerPortalSettingsService.save(customerPortalSettingsDTO);
        }
    }*/

    /*@GetMapping("/bulk-subscription-bundle-settings")
    public void updateSubscriptionBundleSettings() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            String shop = socialConnection.getUserId();

            SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO = subscriptionBundleSettingsService.findByShop(shop).orElse(new SubscriptionBundleSettingsDTO());

            subscriptionBundleSettingsDTO.setShop(shop);

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getSelectedFrequencyLabelText()))
                subscriptionBundleSettingsDTO.setSelectedFrequencyLabelText("Selected Frequency");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getAddButtonText()))
                subscriptionBundleSettingsDTO.setAddButtonText("Add");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getSelectMinimumProductButtonText()))
                subscriptionBundleSettingsDTO.setSelectMinimumProductButtonText("Please select minimum {{minProduct}} product");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getProductsToProceedText()))
                subscriptionBundleSettingsDTO.setProductsToProceedText("Please select products to proceed");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getProceedToCheckoutButtonText()))
                subscriptionBundleSettingsDTO.setProceedToCheckoutButtonText("Proceed to checkout");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getMyDeliveryText()))
                subscriptionBundleSettingsDTO.setMyDeliveryText("My {{selectedSellingPlanDisplayName}} delivery");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getPageBackgroundColor()))
                subscriptionBundleSettingsDTO.setPageBackgroundColor("#f6f6f7");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getProductTitleFontColor()))
                subscriptionBundleSettingsDTO.setProductTitleFontColor("#3a3a3a");

            if (StringUtils.isEmpty(subscriptionBundleSettingsDTO.getButtonBackgroundColor()))
                subscriptionBundleSettingsDTO.setButtonBackgroundColor("#3a3a3a");

            subscriptionBundleSettingsService.save(subscriptionBundleSettingsDTO);
        }
    }*/

    /*@GetMapping("/bulk-cart-widget-settings")
    public void updateCartWidgetSettings() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            String shop = socialConnection.getUserId();

            CartWidgetSettings cartWidgetSettings = cartWidgetSettingsService.findByShop(shop).orElse(new CartWidgetSettings());

            cartWidgetSettings.setShop(shop);
            cartWidgetSettings.setEnable_cart_widget_settings(false);

            //TODO: need to verify value whether it's null or has default value.
            if (StringUtils.isEmpty(cartWidgetSettings.getCartWidgetSettingApproach()))
                cartWidgetSettings.setCartWidgetSettingApproach(CartWidgetSettingApproach.V1);

            if (StringUtils.isEmpty(cartWidgetSettings.getCartFormSelector()))
                cartWidgetSettings.setCartFormSelector("form[action-'/cart']");

            if (StringUtils.isEmpty(cartWidgetSettings.getAppstelCustomeSelector()))
                cartWidgetSettings.setAppstelCustomeSelector("[data-appstel-selector]");

            cartWidgetSettingsService.save(cartWidgetSettings);
        }
    }*/

    @GetMapping("/do-reporting")
    public void doReporting() {
        List<ActivityUpdatesSettings> reportsToBeSent = new ArrayList<>();
        try {

            refreshCustomerPaymentToken();

            ZonedDateTime yesterday = ZonedDateTime.now().minusHours(24);
            ZonedDateTime week = ZonedDateTime.now().minusDays(7);
            ZonedDateTime month = ZonedDateTime.now().minusDays(30);
            ZonedDateTime quarter = ZonedDateTime.now().minusDays(90);
            //select * from activityUpdates where isReportingEnabled = true and  last_reporting_date > 24 hours  && isCurrentlyprocessing = false;
            reportsToBeSent = activityUpdatesSettingsRepository.findShopsByReportToBeSent(true, yesterday, week, month, quarter);

            //mark them as processing
            for (ActivityUpdatesSettings activityUpdatesSettings : reportsToBeSent) {
                activityUpdatesSettings.setSummaryReportProcessing(true);
            }
            activityUpdatesSettingsRepository.saveAll(reportsToBeSent);

            summaryReportService.sendActivityReports(reportsToBeSent);

            //once processed, update last_reporting_date;
            for (ActivityUpdatesSettings activityUpdatesSettings : reportsToBeSent) {
                activityUpdatesSettings.setSummaryReportLastSent(ZonedDateTime.now());
            }
            activityUpdatesSettingsRepository.saveAll(reportsToBeSent);

            log.info("reporting invoked.");
        } catch (Exception ex) {
            log.error("Error while sending activity summery report. ex=" + ExceptionUtils.getStackTrace(ex));
        } finally {
            //unmark them
            for (ActivityUpdatesSettings activityUpdatesSettings : reportsToBeSent) {
                activityUpdatesSettings.setSummaryReportProcessing(false);
            }
            activityUpdatesSettingsRepository.saveAll(reportsToBeSent);

        }
    }

    private void refreshCustomerPaymentToken() {
        try {
            List<CustomerPayment> customers = customerPaymentRepository.findCustomerByTokenCreatedTime(ZonedDateTime.now().minusDays(7));

            for (CustomerPayment customerPayment : customers) {
                customerPayment.setCustomerUid(CommonUtils.generateRandomUid());
                customerPayment.setTokenCreatedTime(ZonedDateTime.now());
            }
            customerPaymentRepository.saveAll(customers);
        } catch (Exception e) {
            log.error("Something went wrong while refreshing token. Error: {}" + e.getMessage());
        }
    }

    @GetMapping("/remove-duplicate-billing-attempts")
    public void removeDuplicateBillingAttempts() throws IOException {

        log.info("executing remove-duplicate-billing-attempts");

        //List<SocialConnection> socialConnections = socialConnectionService.findAll();

        List<Long> contractIds = subscriptionBillingAttemptService.findDuplicateBillingAttemptContractIds();

        for (Long contractId : contractIds) {

            List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatus(contractId, BillingAttemptStatus.QUEUED);

            subscriptionBillingAttemptDTOList = subscriptionBillingAttemptDTOList.stream().sorted(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate)).collect(Collectors.toList());

            SubscriptionBillingAttemptDTO prev = null;
            for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : subscriptionBillingAttemptDTOList) {
                if (prev != null) {
                    if (prev.getBillingDate().toLocalDate().compareTo(subscriptionBillingAttemptDTO.getBillingDate().toLocalDate()) == 0) {
                        log.info("shop=" + subscriptionBillingAttemptDTO.getShop() + " prev=" + prev.getId() + " current=" + subscriptionBillingAttemptDTO.getId() + " contractId=" + subscriptionBillingAttemptDTO.getContractId() + " deleting billingattempt");
                        subscriptionBillingAttemptService.delete(prev.getId());
                    }
                }
                prev = subscriptionBillingAttemptDTO;
            }
        }

        log.info("executed duplicate billing attempts successfully for contractIdsSize=" + contractIds.size());
    }

    @GetMapping("/refresh-faulty-billing-attempts")
    public ResponseEntity<Map<String, Object>> refreshFaultyBillingAttempts(@RequestParam("api_key") String apiKey) throws IOException {

        log.info("executing refresh-faulty-billing-attempts");

        List<Long> fixedContractIds = new ArrayList<>();
        List<String> error = new ArrayList<>();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<Long> contractIds = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop);

        for (Long contractId : contractIds) {
            try {
                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);
                if (subscriptionContractDetailsDTOOptional.isPresent() && subscriptionContractDetailsDTOOptional.get().getStatus().equalsIgnoreCase("active")) {

                    SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsDTOOptional.get();
                    boolean isFaulty = false;

                    ChronoUnit chronoUnit = null;
                    switch (subscriptionContractDetailsDTO.getBillingPolicyInterval().toLowerCase()) {
                        case "day":
                            chronoUnit = ChronoUnit.DAYS;
                            break;
                        case "week":
                            chronoUnit = ChronoUnit.WEEKS;
                            break;
                        case "month":
                            chronoUnit = ChronoUnit.MONTHS;
                            break;
                        case "year":
                            chronoUnit = ChronoUnit.YEARS;
                            break;
                    }

                    List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatus(subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);
                    subscriptionBillingAttemptDTOList.sort(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));

                    for (int i = 0; i < subscriptionBillingAttemptDTOList.size(); i++) {
                        if (i == 0) {
                            continue;
                        }

                        ZonedDateTime nextPredictedDate = subscriptionBillingAttemptDTOList.get(i - 1).getBillingDate().plus(subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), chronoUnit);

                        if (subscriptionBillingAttemptDTOList.get(i).getBillingDate().isAfter(subscriptionBillingAttemptDTOList.get(i - 1).getBillingDate()) &&
                            subscriptionBillingAttemptDTOList.get(i).getBillingDate().isBefore(nextPredictedDate)) {
                            isFaulty = true;
                            break;
                        }
                    }

                    if (isFaulty) {
                        fixedContractIds.add(subscriptionContractDetailsDTO.getSubscriptionContractId());
//                Optional<Long> nexBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(subscriptionContractDetailsDTO.getSubscriptionContractId());
//                subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, subscriptionContractDetailsDTO.getSubscriptionContractId());
//                commonUtils.updateQueuedAttempts(
//                    subscriptionContractDetailsDTO.getNextBillingDate(),
//                    shop,
//                    subscriptionContractDetailsDTO.getSubscriptionContractId(),
//                    subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(),
//                    subscriptionContractDetailsDTO.getBillingPolicyInterval(),
//                    subscriptionContractDetailsDTO.getMaxCycles(),
//                    nexBillingId.orElse(null));
                    }
                }
            }catch (Exception e){
                error.add("ContractId: " + contractId + ", error = " + e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("FixedContractIds", fixedContractIds);
        result.put("errors", error);

        log.info("refreshFaultyBillingAttempts result="+result.toString());
        return ResponseEntity.ok().body(result);
    }

    /*@GetMapping("/update-order-note-attributes")
    public void updateOrderNoteAttributes() throws IOException {
        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnectionList) {
            String shop = socialConnection.getUserId();

            asyncExecutor.execute(() -> {

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

                for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {
                    SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                    if (org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionContractDetails.getOrderNoteAttributes())) {
                        try {
                            OrderNoteAttributesWrapper orderNoteAttributesWrapper = OBJECT_MAPPER.readValue(subscriptionContractDetails.getOrderNoteAttributes(), OrderNoteAttributesWrapper.class);

                            if (orderNoteAttributesWrapper.getOrderNoteAttributesList().isEmpty()) {
                                continue;
                            }

                            List<AttributeInput> customAttributesList = new ArrayList<>();

                            for (OrderNoteAttribute orderNoteAttribute : orderNoteAttributesWrapper.getOrderNoteAttributesList()) {
                                customAttributesList.add(AttributeInput.builder().key(orderNoteAttribute.getKey()).value(orderNoteAttribute.getValue()).build());
                            }

                            subscriptionDraftInputBuilder.customAttributes(customAttributesList);

                            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();
                            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContract(shopifyGraphqlClient, shop, subscriptionContractDetails.getSubscriptionContractId(), subscriptionDraftInput);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }


    @GetMapping("/update-order-note")
    public void updateOrderNote() throws IOException {

        List<SocialConnection> socialConnectionList = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnectionList) {
            String shop = socialConnection.getUserId();

            asyncExecutor.execute(() -> {

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

                for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {
                    String nextOrderNote = getNextOrderNote(subscriptionContractDetails);
                    if (nextOrderNote != null) {
                        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();


                        subscriptionDraftInputBuilder.note(getNextOrderNote(subscriptionContractDetails));

                        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();
                        try {
                            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContract(shopifyGraphqlClient, shop, subscriptionContractDetails.getSubscriptionContractId(), subscriptionDraftInput);
                        } catch (Exception e) {

                        }
                    }
                }
            });
        }
    }*/

    public String getNextOrderNote(SubscriptionContractDetails subscriptionContractDetails) {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        List<SubscriptionBillingAttempt> queuedOrders = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(subscriptionContractDetails.getShop(), subscriptionContractDetails.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);
        queuedOrders = queuedOrders.stream().sorted(Comparator.comparing(SubscriptionBillingAttempt::getBillingDate)).collect(Collectors.toList());

        Optional<SubscriptionBillingAttempt> firstByContractIdAndStatusEqualsOrderByBillingDateAsc = Optional.empty();
        for (SubscriptionBillingAttempt queuedOrder : queuedOrders) {
            if (queuedOrder.getBillingDate().isAfter(now)) {
                firstByContractIdAndStatusEqualsOrderByBillingDateAsc = Optional.of(queuedOrder);
                break;
            }
        }

        if (firstByContractIdAndStatusEqualsOrderByBillingDateAsc.isPresent()) {
            SubscriptionBillingAttempt subscriptionBillingAttempt = firstByContractIdAndStatusEqualsOrderByBillingDateAsc.get();
            if (subscriptionBillingAttempt.getOrderNote() != null) {
                return subscriptionBillingAttempt.getOrderNote();
            } else {
                return subscriptionContractDetails.getOrderNote();
            }
        } else {
            return subscriptionContractDetails.getOrderNote();
        }
    }

    /*@GetMapping("/shop-next-billing-date-discrepancy")
    public List<SubscriptionContractDetails> fixNextBillingDateDiscrepancyForShop(@RequestParam("shop") String shop) throws Exception {

        log.info("executing fix-next-billing-date-discrepancy");

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findNextOrderDateDiscrepancyForShop(shop);

        return subscriptionContractDetails;
    }*/


    @GetMapping("/fix-next-billing-date-discrepancy")
    public void fixNextBillingDateDiscrepancy() throws Exception {

        log.info("executing fix-next-billing-date-discrepancy");

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findNextOrderDateDiscrepancy();

        Map<String, List<SubscriptionContractDetailsDTO>> subscriptionContractDetailsByShop = subscriptionContractDetailsDTOS.stream().collect(Collectors.groupingBy(SubscriptionContractDetailsDTO::getShop));

        for (Map.Entry<String, List<SubscriptionContractDetailsDTO>> entry : subscriptionContractDetailsByShop.entrySet()) {
            String shop = entry.getKey();
            Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByUserId(shop);

            if (socialConnectionOptional.isEmpty()) {

                Set<Long> contractIds = entry.getValue().stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toSet());

                subscriptionBillingAttemptService.deleteByContractIdIn(contractIds);
                subscriptionContractDetailsService.deleteBySubscriptionContractIdIn(contractIds);

                continue;
            }

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : entry.getValue()) {
                try {
                    Long contractId = subscriptionContractDetailsDTO.getSubscriptionContractId();

                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
                    Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                    Object nextBillingDate = Objects.requireNonNull(optionalSubscriptionContractQueryResponse.getData()).flatMap(d -> d.getSubscriptionContract().flatMap(SubscriptionContractQuery.SubscriptionContract::getNextBillingDate)).orElse(null);

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
                    ZonedDateTime formattedContractTime = ZonedDateTime.parse(nextBillingDate.toString(), dateTimeFormatter);
                    formattedContractTime = formattedContractTime.withZoneSameInstant(ZoneId.of("UTC"));

                    ZonedDateTime dbBillingDateInUTC = subscriptionContractDetailsDTO.getNextBillingDate().withZoneSameInstant(ZoneId.of("UTC"));

                    List<SubscriptionBillingAttemptDTO> queuedSubscriptionBillingAttempts = subscriptionBillingAttemptService.findByContractIdAndStatus(contractId, BillingAttemptStatus.QUEUED);
                    SubscriptionBillingAttemptDTO upcomingOrder = queuedSubscriptionBillingAttempts.stream().min(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate)).get();

                    ZonedDateTime upcomingOrderBillingDate = upcomingOrder.getBillingDate().withZoneSameInstant(ZoneId.of("UTC"));

                    List<SubscriptionBillingAttemptDTO> pastOrders = new ArrayList<>();

                    int page = 0;
                    int pageSize = 100;
                    Page<SubscriptionBillingAttemptDTO> pageResult = null;
                    do {
                        Pageable pageable = PageRequest.of(page, pageSize);
                        pageResult = subscriptionBillingAttemptService.getPastOrdersByContractId(pageable, contractId, shop);

                        if (pageResult.hasContent()) {
                            pastOrders.addAll(pageResult.getContent());
                        }
                        page++;
                    } while (!pageResult.isLast());


                    pastOrders = pastOrders.stream().sorted((o1, o2) -> o2.getBillingDate().compareTo(o1.getBillingDate())).collect(Collectors.toList());

                    if (dbBillingDateInUTC.equals(formattedContractTime)) {

                        List<SubscriptionBillingAttemptDTO> failedSubscriptionBillingAttempts = subscriptionBillingAttemptService.findByContractIdAndStatus(contractId, BillingAttemptStatus.FAILURE);
                        Optional<SubscriptionBillingAttemptDTO> lastFailedAttemptOrder = failedSubscriptionBillingAttempts.stream().min((o1, o2) -> o2.getBillingDate().compareTo(o1.getBillingDate()));

                        final ZonedDateTime formattedContractTimeConstant = formattedContractTime;

                        if (pastOrders.stream().anyMatch(f -> f.getBillingDate().withZoneSameInstant(ZoneId.of("UTC")).equals(formattedContractTimeConstant))) {

                        } else {
                            log.info("Mismatch in contract date and upcoming order date. shop=" + shop + " contractId=" + contractId + " dbNextBillingDate=" + dbBillingDateInUTC + " contractBillingDate=" + formattedContractTime + " upcomingOrderBillingDate=" + upcomingOrderBillingDate);
                        }
                    } else {

                        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                        subscriptionDraftInputBuilder.nextBillingDate(nextBillingDate);

                        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                        log.info("shop=" + shop + " contractId=" + contractId + " date mismatch between contract and DB. " + " dbNextBillingDate=" + dbBillingDateInUTC + " contractBillingDate=" + formattedContractTime + " upcomingOrderBillingDate=" + upcomingOrderBillingDate);

                        //SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContract(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);
                    }

                } catch (Exception ex) {

                }
            }
        }
    }

    @GetMapping("memory-status")
    public MemoryStats getMemoryStatistics() {
        log.info("invoking memory stats");
        MemoryStats stats = new MemoryStats();
        stats.setHeapSize(Runtime.getRuntime().totalMemory());
        stats.setHeapMaxSize(Runtime.getRuntime().maxMemory());
        stats.setHeapFreeSize(Runtime.getRuntime().freeMemory());
        return stats;
    }

    @GetMapping("/fix-billing-attempts-progress-state")
    public void fixBillingAttemptsInProgressState() throws Exception {

        log.info("executing fix-billing-attempts-progress-state");

        ZonedDateTime twentyFourHoursBack = ZonedDateTime.now(ZoneId.of("UTC")).minusHours(10L);

        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOS = subscriptionBillingAttemptService.findBillingAttemptsInProgressState(twentyFourHoursBack);

        log.info("fix-billing-attempts-progress-state subscriptionBillingAttemptDTOS.size=" + subscriptionBillingAttemptDTOS.size());

        Map<String, List<SubscriptionBillingAttemptDTO>> subscriptionBillingAttemptsByShop = subscriptionBillingAttemptDTOS.stream().collect(Collectors.groupingBy(SubscriptionBillingAttemptDTO::getShop));

        for (Map.Entry<String, List<SubscriptionBillingAttemptDTO>> entry : subscriptionBillingAttemptsByShop.entrySet()) {
            String shop = entry.getKey();

            if (shop.equals("alibaba-affilate-marketing.myshopify.com") || shop.equals("pelaearth.myshopify.com") || shop.equals("us-birchbox-staging.myshopify.com")) {
                continue;
            }

            try {
                Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByUserId(shop);

                if (socialConnectionOptional.isEmpty()) {
                    log.info("fix-billing-attempts-progress-state social connection is missing.");
                    for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : entry.getValue()) {
                        subscriptionBillingAttemptDTO.setStatus(BillingAttemptStatus.SHOPIFY_EXCEPTION);
                        subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);
                    }
                    continue;
                }

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : entry.getValue()) {
                    Long contractId = subscriptionBillingAttemptDTO.getContractId();

                    Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOptional = subscriptionContractDetailsService.findByContractId(contractId);

                    if (subscriptionContractDetailsDTOOptional.isEmpty()) {
                        subscriptionBillingAttemptService.deleteByContractId(contractId);
                    }

                    try {
                        SubscriptionBillingAttemptQuery subscriptionBillingAttemptQuery = new SubscriptionBillingAttemptQuery(subscriptionBillingAttemptDTO.getBillingAttemptId());
                        Response<Optional<SubscriptionBillingAttemptQuery.Data>> optionalSubscriptionBillingAttemptQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionBillingAttemptQuery);
                        Optional<SubscriptionBillingAttemptQuery.SubscriptionBillingAttempt> optionalSubscriptionBillingAttempt = Objects.requireNonNull(optionalSubscriptionBillingAttemptQueryResponse.getData()).flatMap(SubscriptionBillingAttemptQuery.Data::getSubscriptionBillingAttempt);

                        if (optionalSubscriptionBillingAttempt.isPresent()) {
                            SubscriptionBillingAttemptQuery.SubscriptionBillingAttempt subscriptionBillingAttempt = optionalSubscriptionBillingAttempt.get();
                            Optional<String> optionalOrderId = subscriptionBillingAttempt.getOrder().map(SubscriptionBillingAttemptQuery.Order::getId);

                            if (optionalOrderId.isPresent()) {
                                String adminGraphqlApiOrderId = optionalOrderId.get();
                                log.info("order is already processed for billingAttemptId=" + subscriptionBillingAttempt.getId() + " orderId=" + adminGraphqlApiOrderId + " shop=" + shop + " contractId=" + contractId);

                                SubscriptionBillingAttemptInfo subscriptionBillingAttemptInfo = new SubscriptionBillingAttemptInfo();
                                subscriptionBillingAttemptInfo.setAdminGraphqlApiId(subscriptionBillingAttemptDTO.getBillingAttemptId());
                                subscriptionBillingAttemptInfo.setAdminGraphqlApiSubscriptionContractId(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                                subscriptionBillingAttemptInfo.setSubscriptionContractId(contractId);
                                subscriptionBillingAttemptInfo.setAdminGraphqlApiOrderId(adminGraphqlApiOrderId);
                                subscriptionBillingAttemptInfo.setOrderId(Long.parseLong(adminGraphqlApiOrderId.replace(ShopifyIdPrefix.ORDER_ID_PREFIX, "")));

                                HttpResponse<String> response = Unirest.post("https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/").header("X-Shopify-Shop-Domain", shop).header("X-Shopify-Topic", SUBSCRIPTION_BILLING_ATTEMPTS_SUCCESS).header("Content-Type", "application/json").body(OBJECT_MAPPER.writeValueAsString(subscriptionBillingAttemptInfo)).asString();

                            } else {
                                Optional<SubscriptionBillingAttemptErrorCode> optionalErrorCode = subscriptionBillingAttempt.getErrorCode();
                                Optional<String> optionalErrorMessage = subscriptionBillingAttempt.getErrorMessage();
                                if (optionalErrorCode.isPresent() || optionalErrorMessage.isPresent()) {
                                    log.info("order is already processed for billingAttemptId=" + subscriptionBillingAttempt.getId() + " errorCode=" + optionalErrorCode.get().toString() + " shop=" + shop + " contractId=" + contractId);

                                    SubscriptionBillingAttemptInfo subscriptionBillingAttemptInfo = new SubscriptionBillingAttemptInfo();
                                    subscriptionBillingAttemptInfo.setAdminGraphqlApiId(subscriptionBillingAttemptDTO.getBillingAttemptId());
                                    subscriptionBillingAttemptInfo.setAdminGraphqlApiSubscriptionContractId(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                                    subscriptionBillingAttemptInfo.setSubscriptionContractId(contractId);
                                    subscriptionBillingAttemptInfo.setErrorCode(optionalErrorCode.orElse(null));
                                    subscriptionBillingAttemptInfo.setErrorMessage(optionalErrorMessage.orElse(null));

                                    HttpResponse<String> response = Unirest.post("https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/").header("X-Shopify-Shop-Domain", shop).header("X-Shopify-Topic", SUBSCRIPTION_BILLING_ATTEMPTS_FAILURE).header("Content-Type", "application/json").body(OBJECT_MAPPER.writeValueAsString(subscriptionBillingAttemptInfo)).asString();

                                } else if (subscriptionBillingAttempt.getNextActionUrl().isPresent()) {
                                    String nextActionUrl = subscriptionBillingAttempt.getNextActionUrl().get().toString();
                                    SubscriptionBillingAttemptInfo subscriptionBillingAttemptInfo = new SubscriptionBillingAttemptInfo();
                                    subscriptionBillingAttemptInfo.setAdminGraphqlApiId(subscriptionBillingAttempt.getId());
                                    subscriptionBillingAttemptInfo.setAdminGraphqlApiSubscriptionContractId(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                                    subscriptionBillingAttemptInfo.setSubscriptionContractId(contractId);
                                    HttpResponse<String> response = Unirest.post("https://6j1um3olz6.execute-api.us-west-1.amazonaws.com/prod/").header("X-Shopify-Shop-Domain", shop).header("X-Shopify-Topic", SUBSCRIPTION_BILLING_ATTEMPTS_CHALLENGED).header("Content-Type", "application/json").body(OBJECT_MAPPER.writeValueAsString(subscriptionBillingAttemptInfo)).asString();

                                } else {
                                    log.info("order is still processing" + " shop=" + shop + " contractId=" + contractId + " nextActionUrl=" + subscriptionBillingAttempt.getNextActionUrl().map(Object::toString).orElse(""));
                                }
                            }
                        } else {
                            log.info("order is still processing" + " shop=" + shop + " contractId=" + contractId);
                        }
                    } catch (Exception ex) {

                        if (ex instanceof ApolloHttpException) {
                            int code = ((ApolloHttpException) ex).code();
                            log.error("fixBillingAttemptsInProgressState" + " code=" + code + " shop=" + shop + " contractId=" + contractId);
                        } else {
                            log.error("fixBillingAttemptsInProgressState" + " ex=" + ExceptionUtils.getStackTrace(ex) + " shop=" + shop + " contractId=" + contractId);
                        }

                        for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO1 : entry.getValue()) {
                            subscriptionBillingAttemptDTO1.setStatus(BillingAttemptStatus.SHOPIFY_EXCEPTION);
                            subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO1);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("fixBillingAttemptsInProgressState" + " ex=" + ExceptionUtils.getStackTrace(ex) + " shop=" + shop);
            }
        }
    }

/*    @GetMapping("/update-pricing-policy")
    public void updatePricingPolicy() throws Exception {
        String shop = "happy-flame.myshopify.com";

        List<SubscriptionContractDetailsDTO> subscriptions = subscriptionContractDetailsService.findByShop(shop);

        List<FrequencyInfoDTO> sellingPlans = subscriptionGroupService.getAllSellingPlans(shop);

        for(SubscriptionContractDetailsDTO scd: subscriptions) {

            Optional<FrequencyInfoDTO> sellingPlan = sellingPlans.stream().filter(sp ->
                sp.getBillingFrequencyInterval().name().equalsIgnoreCase(scd.getBillingPolicyInterval())
                && sp.getBillingFrequencyCount().equals(scd.getBillingPolicyIntervalCount())).findFirst();


            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

            SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(scd.getGraphSubscriptionContractId());
            Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

            SubscriptionContractQuery.SubscriptionContract subscriptionContract = optionalSubscriptionContractQueryResponse.getData().get().getSubscriptionContract().get();

            //subscriptionContractDetailsService.updateDetails(shop, subscriptionContract);

            if(sellingPlan.isPresent()) {

                List<AppstleCycle> cycles = new ArrayList<>();

                if(sellingPlan.get().getAfterCycle1() != null) {
                    AppstleCycle appstleCycle = new AppstleCycle();
                    appstleCycle.setAfterCycle(sellingPlan.get().getAfterCycle1());
                    appstleCycle.setValue(sellingPlan.get().getDiscountOffer());

                    if(DiscountTypeUnit.PERCENTAGE.equals(sellingPlan.get().getDiscountType())) {
                        appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                    } else {
                        appstleCycle.setDiscountType(DiscountType.FIXED);
                    }

                    cycles.add(appstleCycle);
                }

                if(sellingPlan.get().getAfterCycle2() != null) {
                    AppstleCycle appstleCycle = new AppstleCycle();
                    appstleCycle.setAfterCycle(sellingPlan.get().getAfterCycle2());
                    appstleCycle.setValue(sellingPlan.get().getDiscountOffer2());

                    if(DiscountTypeUnit.PERCENTAGE.equals(sellingPlan.get().getDiscountType2())) {
                        appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                    } else {
                        appstleCycle.setDiscountType(DiscountType.FIXED);
                    }

                    cycles.add(appstleCycle);
                }

                List<SubscriptionProductInfo> products = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, scd.getContractDetailsJSON());

                for(SubscriptionProductInfo product: products) {
                    String lineId = product.getLineId();
                    Double basePrice = product.getBasePrice() != null ? Double.parseDouble(product.getBasePrice()) : Double.parseDouble(product.getCurrentPrice());
                    subscriptionContractDetailsService.subscriptionContractUpdateLineItemPricingPolicy(shop, subscriptionContract, lineId, basePrice, cycles, ActivityLogEventSource.MERCHANT_PORTAL);
                }
            }
        }
    }*/

    /*@GetMapping("/bulk-email-settings")
    public void bulkEmailSettings() throws IOException {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            ShopifyWithRateLimiter shopifyWithRateLimiter = new ShopifyWithRateLimiter(socialConnection.getAccessToken(), socialConnection.getUserId());
            Shop shop = shopifyWithRateLimiter.getShopInfo().getShop();

            onBoardingHandler.insertEmailTemplateSetting(socialConnection.getUserId(), shop);
        }
    }*/

    @GetMapping("/fix-onboarding")
    public void fixOnboarding(@RequestParam("shop") String shop) throws IOException {
        SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();

        onBoardingHandler.handle(shop, socialConnection.getAccessToken(), socialConnection.getProverId());
    }

    @GetMapping("/canny-sso-token")
    public String generateCannySSOToken() {

        String shopName = SecurityUtils.getCurrentUserLogin().get();

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shopName);
        ShopInfo shopInfo = api.getShopInfo();
        Shop shop = shopInfo.getShop();

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("email", shop.getCustomerEmail());
        userData.put("id", shopName);
        userData.put("name", shop.getShopOwner());

        return Jwts.builder().setClaims(userData).signWith(SignatureAlgorithm.HS256, Constants.CannyPrivateKey.getBytes(StandardCharsets.UTF_8)).compact();
    }


    /*@GetMapping("/update-min-cyles")
    public String updateMinCylces() {

        String shop = "saucebox2020.myshopify.com";

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);


        try {

            String cusrsor = null;
            boolean isNextPage = true;
            int i = 0;

            while (isNextPage) {
                SubscriptionContractsQuery subscriptionContractsQuery = new SubscriptionContractsQuery(Input.fromNullable(cusrsor));
                Response<Optional<SubscriptionContractsQuery.Data>> subscriptionContractsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsQuery);

                List<SubscriptionContractsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsQueryResponse.getData())
                    .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                    .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                    .orElse(new ArrayList<>()).stream()
                    .map(SubscriptionContractsQuery.Edge::getNode)
                    .collect(Collectors.toList());

                isNextPage = subscriptionContractsQueryResponse.getData().map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                    .map(SubscriptionContractsQuery.SubscriptionContracts::getPageInfo)
                    .map(SubscriptionContractsQuery.PageInfo::isHasNextPage)
                    .orElse(Boolean.FALSE);

                Optional<SubscriptionContractsQuery.Edge> last = subscriptionContractsQueryResponse.getData()
                    .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                    .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                    .orElse(new ArrayList<>()).stream()
                    .reduce((first, second) -> second);

                cusrsor = last.map(SubscriptionContractsQuery.Edge::getCursor).orElse(null);

                Set<Long> contractIds = subscriptionContracts.stream().map(s -> Long.parseLong(s.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                List<SubscriptionContractDetails> subscriptionContractDetailsSet = new ArrayList<>();
                if (!contractIds.isEmpty()) {
                    subscriptionContractDetailsSet = subscriptionContractDetailsRepository.findBySubscriptionContractIdIn(contractIds);
                }

                Map<Long, SubscriptionContractDetails> subscriptionContractDetailByContractId = subscriptionContractDetailsSet.stream().collect(Collectors.toMap(SubscriptionContractDetails::getSubscriptionContractId, s -> s));

                for (SubscriptionContractsQuery.Node subscriptionContract : subscriptionContracts) {
                    i++;
                    log.info("iterating record i=" + i);

                    long subscriptionContractId = Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

                    if (!subscriptionContractDetailByContractId.containsKey(subscriptionContractId)) {
                        continue;
                    }

                    if (subscriptionContract.getBillingPolicy().getInterval() == SellingPlanInterval.MONTH && subscriptionContract.getBillingPolicy().getIntervalCount() == 1 && subscriptionContract.getBillingPolicy().getMinCycles().isPresent() && subscriptionContract.getBillingPolicy().getMinCycles().get() == 2) {
                        log.info("contractId=" + subscriptionContract.getId());
                        subscriptionContractDetailsResource.updateMinCycles(Long.parseLong(subscriptionContract.getId().replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, "")), null, null);
                    }
                }
            }
        } catch (Exception ex) {
        }


        return null;
    }*/

    /*@GetMapping("/update-subscription-widget-settings")
    public void updateSubscriptionWidgetSettings() {
        List<SubscriptionWidgetSettingsDTO> subscriptionWidgetSettingsDTOS = subscriptionWidgetSettingsService.findAll();
        for (SubscriptionWidgetSettingsDTO subscriptionWidgetSettingsDTO : subscriptionWidgetSettingsDTOS) {
            boolean updateSubscriptionWidgetSettingsDTO = false;
            if (subscriptionWidgetSettingsDTO.getOrderStatusManageSubscriptionTitle() == null) {
                subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionTitle("Subscription");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getOrderStatusManageSubscriptionButtonText() == null) {
                subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionButtonText("Manage your subscription");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getOrderStatusManageSubscriptionDescription() == null) {
                subscriptionWidgetSettingsDTO.setOrderStatusManageSubscriptionDescription("Continue to your account to view and manage your subscriptions.");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isShowTooltipOnClick() == null) {
                subscriptionWidgetSettingsDTO.setShowTooltipOnClick(false);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isSubscriptionOptionSelectedByDefault() == null) {
                subscriptionWidgetSettingsDTO.setSubscriptionOptionSelectedByDefault(false);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isWidgetEnabled() == null) {
                subscriptionWidgetSettingsDTO.setWidgetEnabled(true);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isShowTooltip() == null) {
                subscriptionWidgetSettingsDTO.setShowTooltip(true);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getSellingPlanTitleText() == null) {
                subscriptionWidgetSettingsDTO.setSellingPlanTitleText("{{sellingPlanName}} ({{sellingPlanPrice}}/delivery)");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getOneTimePriceText() == null) {
                subscriptionWidgetSettingsDTO.setOneTimePriceText("{{price}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getSelectedPayAsYouGoSellingPlanPriceText() == null) {
                subscriptionWidgetSettingsDTO.setSelectedPayAsYouGoSellingPlanPriceText("{{price}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getSelectedPrepaidSellingPlanPriceText() == null) {
                subscriptionWidgetSettingsDTO.setSelectedPrepaidSellingPlanPriceText("{{pricePerDelivery}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getSelectedDiscountFormat() == null) {
                subscriptionWidgetSettingsDTO.setSelectedDiscountFormat("SAVE {{selectedDiscountPercentage}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getManageSubscriptionBtnFormat() == null) {
                subscriptionWidgetSettingsDTO.setManageSubscriptionBtnFormat("<a href='" + shopInfoUtils.getManageSubscriptionUrl(subscriptionWidgetSettingsDTO.getShop()) + "' class='appstle_manageSubBtn' ><button class='btn' style='padding: 2px 20px'>Manage Subscription</button><a><br><br>");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getTooltipDescriptionOnPrepaidPlan() == null) {
                subscriptionWidgetSettingsDTO.setTooltipDescriptionOnPrepaidPlan("<b>Prepaid Plan Details</b></br> Total you are charged for {{totalPrice}} ( At every order {{pricePerDelivery}})");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getTooltipDescriptionOnMultipleDiscount() == null) {
                subscriptionWidgetSettingsDTO.setTooltipDescriptionOnMultipleDiscount("<b>Discount Details</b></br> Hello there my discount is {{discountOne}} and then {{discountTwo}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.getTooltipDescriptionCustomization() == null) {
                subscriptionWidgetSettingsDTO.setTooltipDescriptionCustomization("Save and subscribe the product </br>  {{prepaidDetails}} </br> {{discountDetails}}");
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isShowStaticTooltip() == null) {
                subscriptionWidgetSettingsDTO.setShowStaticTooltip(false);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isWidgetEnabledOnSoldVariant() == null) {
                subscriptionWidgetSettingsDTO.setWidgetEnabled(false);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (subscriptionWidgetSettingsDTO.isSwitchRadioButtonWidget() == null) {
                subscriptionWidgetSettingsDTO.setSwitchRadioButtonWidget(false);
                updateSubscriptionWidgetSettingsDTO = true;
            }
            if (updateSubscriptionWidgetSettingsDTO)
                subscriptionWidgetSettingsService.save(subscriptionWidgetSettingsDTO);
        }

    }*/

/*    @GetMapping("/update-customer-payment")
    public void updateCustomerPayment() {

        List<SocialConnection> socialConnections = socialConnectionService.findAll();
        socialConnections = socialConnections.stream().sorted(Comparator.comparing(SocialConnection::getId).reversed()).collect(Collectors.toList());

        for (SocialConnection socialConnection: socialConnections) {
            String shop = socialConnection.getUserId();

            List<CustomerPaymentDTO> customerPaymentDTOList = customerPaymentService.findByShop(shop);

            for (CustomerPaymentDTO customerPaymentDTO : customerPaymentDTOList) {
                boolean updateCustomerPaymentDto = false;
                if (StringUtils.isEmpty(customerPaymentDTO.getCustomerUid()) || customerPaymentDTO.getCustomerUid().length() < 16) {
                    customerPaymentDTO.setCustomerUid(CommonUtils.generateRandomUid());
                    customerPaymentDTO.setTokenCreatedTime(ZonedDateTime.now());
                    updateCustomerPaymentDto = true;
                }

                if (updateCustomerPaymentDto)
                    customerPaymentService.save(customerPaymentDTO);
            }
        }
    }*/

    /*@GetMapping("/update-subscription-contract-details")
    public void updateSubscriptionContractDetails() {
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findAll();

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            try {
                boolean updateSubscriptionContractDetailsDTO = false;

                if (StringUtils.isEmpty(subscriptionContractDetailsDTO.getSubscriptionCreatedEmailSentStatus())) {
                    if (subscriptionContractDetailsDTO.isSubscriptionCreatedEmailSent()) {
                        subscriptionContractDetailsDTO.setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus.SENT);
                    } else {
                        subscriptionContractDetailsDTO.setSubscriptionCreatedEmailSentStatus(subscriptionCreatedEmailSentStatus.UNSENT);
                    }
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (subscriptionContractDetailsDTO.isAutoCharge() == null) {
                    subscriptionContractDetailsDTO.setAutoCharge(true);
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (subscriptionContractDetailsDTO.isStopUpComingOrderEmail() == null) {
                    subscriptionContractDetailsDTO.setStopUpComingOrderEmail(false);
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (subscriptionContractDetailsDTO.isPausedFromActive() == null) {
                    subscriptionContractDetailsDTO.setPausedFromActive(false);
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (subscriptionContractDetailsDTO.getPhone() == null) {
                    SocialConnection socialConnection = socialConnectionService.findByUserId(subscriptionContractDetailsDTO.getShop()).get();
                    String accessToken = socialConnection.getAccessToken();
                    ShopifyGraphqlClient shopifyGraphqlClient = ShopifyGraphqlClientUtils.getShopifyGraphqlClient(subscriptionContractDetailsDTO.getShop(), accessToken);

                    CustomerBriefQuery customerBriefQuery = new CustomerBriefQuery(subscriptionContractDetailsDTO.getGraphCustomerId());
                    Response<Optional<CustomerBriefQuery.Data>> customerResponse = shopifyGraphqlClient.getOptionalQueryResponse(customerBriefQuery);

                    Objects.requireNonNull(customerResponse.getData()).flatMap(CustomerBriefQuery.Data::getCustomer).ifPresent(e -> {
                        if (e.getPhone().isPresent()) {
                            subscriptionContractDetailsDTO.setPhone(e.getPhone().get());
                        }
                    });
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (updateSubscriptionContractDetailsDTO)
                    subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
            } catch (Exception e) {

            }
        }
    }*/

    public String readFileAsString(String fileName) throws Exception {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

    /*@GetMapping("/remove-uninstallation-records")
    public void removeUninstallationRecords() throws Exception {

        String a = readFileAsString("/Users/hemantpurswani/Downloads/log-events-viewer-result-5.csv");

        final String regex = "[A-Za-z0-9]+.myshopify.com";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(a);

        Set<String> shops = new HashSet<>();
        while (matcher.find()) {
            String group1 = matcher.group(0);
            log.info("Full match: " + group1);
            shops.add(group1);

            for (int i = 1; i <= matcher.groupCount(); i++) {
                String group = matcher.group(i);
                log.info("Group " + i + ": " + group);
            }
        }

        for (String shop : shops) {
            Optional<PaymentPlan> paymentPlanOptional = paymentPlanService.findByShop(shop);
            if (paymentPlanOptional.isPresent()) {
                PaymentPlan paymentPlan = paymentPlanOptional.get();
                if (BooleanUtils.isFalse(paymentPlan.isTestCharge()) && paymentPlan.getPrice() > 0) {

                    Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByUserId(shop);

                    if (socialConnectionOptional.isPresent()) {
                        String accessToken = socialConnectionOptional.get().getAccessToken();
                        ShopifyWithRateLimiter api = new ShopifyWithRateLimiter(accessToken, shop);

                        try {
                            ShopInfo shopInfo = api.getShopInfo();
                        } catch (Exception ex) {
                            paymentPlanService.delete(paymentPlan.getId());
                            log.info("deleting shop=" + shop + ": price=" + paymentPlan.getPrice());
                        }
                    } else {
                        paymentPlanService.delete(paymentPlan.getId());
                        log.info("deleting shop=" + shop + ": price=" + paymentPlan.getPrice());
                    }

                    //log.info("shop=" + shop + ": price=" + paymentPlan.getPrice());
                }
            }
        }
    }*/

    @GetMapping("/update-customer-payment-records")
    public void updateCustomerPaymentRecords() {

        log.info("invoking updateCustomerPaymentRecords");

        Set<Long> customerIds = customerPaymentService.findDuplicateRecords();

        for (Long customerId : customerIds) {
            if(customerId > 1) {
                List<CustomerPaymentDTO> customerPaymentDTOList = customerPaymentService.findByCustomerId(customerId);
                if (customerPaymentDTOList.size() > 1) {
                    for (int i = 1; i < customerPaymentDTOList.size(); i++) {
                        CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOList.get(i);
                        customerPaymentService.delete(customerPaymentDTO.getId());
                    }
                }
            }
        }
    }

    /*@GetMapping("/remove-customer-payment-method")
    public void removeCustomerPaymentMethod(@RequestParam("customerId") Long customerId) {
        List<CustomerPaymentDTO> customerPaymentDTOList = customerPaymentService.findByCustomerId(customerId);
        if (customerPaymentDTOList.size() > 1) {
            for (int i = 1, customerPaymentDTOListSize = customerPaymentDTOList.size(); i < customerPaymentDTOListSize; i++) {
                CustomerPaymentDTO customerPaymentDTO = customerPaymentDTOList.get(i);
                customerPaymentService.delete(customerPaymentDTO.getId());
            }
        }
    }*/

    @Autowired
    private CurrencyConversionInfoRepository currencyConversionInfoRepository;

    /*@GetMapping("/update-billing-attempt-order-amount")
    public void updateBillingAttemptOrderAmount() throws Exception {
        log.info("invoking updateSubscriptionContractLineAmount");

        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (int i = socialConnections.size() - 1; i >= 0; i--) {
            int finalI = i;
            asyncExecutor.execute(() -> {
                SocialConnection socialConnection = socialConnections.get(finalI);
                String shop = socialConnection.getUserId();
                String accessToken = socialConnection.getAccessToken();

                ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

                Set<Long> successfulOrderIds = subscriptionBillingAttemptRepository.findSuccessfulOrderIdsFor(shop);

                Iterable<List<Long>> partition = Iterables.partition(successfulOrderIds, 20);

                for (List<Long> longs : partition) {
                    List<String> collect = longs.stream().map(s -> ShopifyIdPrefix.ORDER_ID_PREFIX + s).collect(Collectors.toList());
                    OrderLineItemsWithPresentmentCurrencyNodesQuery orderLineItemsWithPresentmentCurrencyNodesQuery = new OrderLineItemsWithPresentmentCurrencyNodesQuery(collect);
                    Response<Optional<OrderLineItemsWithPresentmentCurrencyNodesQuery.Data>> orderLineItemsWithPresentmentCurrencyNodesQueryResponse = null;
                    try {
                        orderLineItemsWithPresentmentCurrencyNodesQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderLineItemsWithPresentmentCurrencyNodesQuery);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    List<OrderLineItemsWithPresentmentCurrencyNodesQuery.AsOrder> orders = orderLineItemsWithPresentmentCurrencyNodesQueryResponse.getData().map(d -> d.getNodes()).orElse(new ArrayList<>()).stream().map(s -> (OrderLineItemsWithPresentmentCurrencyNodesQuery.AsOrder) s).collect(Collectors.toList());

                    LocalDate currentDate = LocalDate.now();

                    for (OrderLineItemsWithPresentmentCurrencyNodesQuery.AsOrder order : orders) {
                        Double orderUsdAmount = 0d;

                        CurrencyCode orderCurrency = order.getTotalPriceSet().getShopMoney().getCurrencyCode();
                        if (orderCurrency != CurrencyCode.USD) {
                            if (order.getTotalPriceSet().getPresentmentMoney().getCurrencyCode() != CurrencyCode.USD) {

                                Optional<CurrencyConversionInfo> currencyConversionInfoOptional = currencyConversionInfoRepository.findCurrencyConversionInfoByFrom(orderCurrency.toString());

                                if (currencyConversionInfoOptional.isPresent()) {
                                    CurrencyConversionInfo currencyConversionInfo = currencyConversionInfoOptional.get();
                                    if (Math.abs(ChronoUnit.DAYS.between(currencyConversionInfo.getStoredOn(), currentDate)) <= 2) {
                                        orderUsdAmount = orderUsdAmount + (Double.parseDouble(order.getTotalPriceSet().getShopMoney().getAmount().toString()) * currencyConversionInfo.getCurrencyRate());
                                    }*//* else {
                                    CurrencyConversionResult currencyConversionResult = getCurrencyConversionResult(orderCurrency.toString());

                                    if (currencyConversionResult == null || currencyConversionResult.getInfo() == null) {
                                        String a = "b";
                                    }

                                    Double rate = currencyConversionResult.getInfo().getRate();

                                    currencyConversionInfo.setCurrencyRate(rate);
                                    currencyConversionInfo.setStoredOn(currentDate);

                                    currencyConversionInfoRepository.save(currencyConversionInfo);
                                    orderUsdAmount = orderUsdAmount + (Double.parseDouble(order.getTotalPriceSet().getShopMoney().getAmount().toString()) * rate);
                                }*//*
                                } *//*else {

                                CurrencyConversionResult currencyConversionResult = getCurrencyConversionResult(orderCurrency.toString());

                                CurrencyConversionInfo currencyConversionInfo = new CurrencyConversionInfo();

                                if (currencyConversionResult == null || currencyConversionResult.getInfo() == null) {
                                    String a = "b";
                                }

                                Double rate = currencyConversionResult.getInfo().getRate();

                                currencyConversionInfo.setCurrencyRate(rate);
                                currencyConversionInfo.setStoredOn(currentDate);
                                currencyConversionInfo.setFrom(orderCurrency.toString());
                                currencyConversionInfo.setTo("USD");
                                currencyConversionInfoRepository.save(currencyConversionInfo);

                                orderUsdAmount = orderUsdAmount + (Double.parseDouble(order.getTotalPriceSet().getShopMoney().getAmount().toString()) * rate);
                            }*//*
                            } else {
                                orderUsdAmount = orderUsdAmount + (Double.parseDouble(order.getTotalPriceSet().getPresentmentMoney().getAmount().toString()));
                            }
                        } else {
                            orderUsdAmount = orderUsdAmount + (Double.parseDouble(order.getTotalPriceSet().getShopMoney().getAmount().toString()));
                        }

                        if (orderUsdAmount > 0D) {
                            try {
                                subscriptionBillingAttemptService.updateUSDOrderAmount(order.getId(), orderUsdAmount);
                            } catch (Exception ex) {
                                String a = "b";
                            }
                        }
                    }
                }
            });
        }
    }*/

    /*@GetMapping("/update-subscription-order-amount")
    public void updateSubscriptionOrderAmount() throws Exception {
        log.info("invoking updateSubscriptionOrderAmount");

        List<SocialConnection> socialConnections = socialConnectionService.findAll();


        for (int i = socialConnections.size() - 1; i >= 0; i--) {
            int finalI = i;
            asyncExecutor.execute(() -> {
                SocialConnection socialConnection = socialConnections.get(finalI);
                String shop = socialConnection.getUserId();
                String accessToken = socialConnection.getAccessToken();

                ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

                Set<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findOrderIdsForShop(shop);

                for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                    Double orderUsdAmount = 0d;
                    if (subscriptionContractDetails.getCurrencyCode().equals("USD")) {
                        orderUsdAmount = subscriptionContractDetails.getOrderAmount();
                        subscriptionContractDetails.setOrderAmountUSD(orderUsdAmount);
                        subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                    } else {
                        Optional<CurrencyConversionInfo> currencyConversionInfoOptional = currencyConversionInfoRepository.findCurrencyConversionInfoByFrom(subscriptionContractDetails.getCurrencyCode());
                        if (currencyConversionInfoOptional.isPresent()) {
                            CurrencyConversionInfo currencyConversionInfo = currencyConversionInfoOptional.get();
                            orderUsdAmount = orderUsdAmount + (subscriptionContractDetails.getOrderAmount() * currencyConversionInfo.getCurrencyRate());
                            subscriptionContractDetails.setOrderAmountUSD(orderUsdAmount);
                            subscriptionContractDetailsRepository.save(subscriptionContractDetails);
                        }
                    }
                }
            });
        }
    }*/

    private CurrencyConversionResult getCurrencyConversionResult(String from) throws IOException {

        HttpResponse<String> response = Unirest.get("https://api.apilayer.com/fixer/convert?to=USD&from=" + from + "&amount=1")
            .header("apikey", "oAR02xWnc5micHZ5GYy8O5dA1rU2VlC5")
            .asString();

        CurrencyConversionResult currencyConversionResult = OBJECT_MAPPER.readValue(response.getBody(), CurrencyConversionResult.class);
        return currencyConversionResult;
    }


    @Autowired SubscriptionContractProcessingService subscriptionContractProcessingService;

    @GetMapping("/update-subscription-contract-line-amount")
    public void updateSubscriptionContractLineAmount() {
        log.info("invoking updateSubscriptionContractLineAmount");

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findSubscriptionWithNullOrderAmount();

        Map<String, List<SubscriptionContractDetailsDTO>> subscriptionListByShop = subscriptionContractDetailsDTOList.stream().collect(Collectors.groupingBy(SubscriptionContractDetailsDTO::getShop));

        for (Map.Entry<String, List<SubscriptionContractDetailsDTO>> entry : subscriptionListByShop.entrySet()) {
            asyncExecutor.execute(() -> {
                String shop = entry.getKey();
                Optional<SocialConnection> socialConnectionOptional = socialConnectionService.findByUserId(shop);

                if (socialConnectionOptional.isEmpty()) {
                    return;
                }

                SocialConnection socialConnection = socialConnectionOptional.get();
                String accessToken = socialConnection.getAccessToken();

                ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

                for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : entry.getValue()) {
                    if (ObjectUtils.isNotEmpty(subscriptionContractDetailsDTO.getOrderAmount()) && !subscriptionContractDetailsDTO.getOrderAmount().equals(0.0)) {
                        continue;
                    }

                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
                    try {
                        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                        SubscriptionContractQuery.SubscriptionContract subscriptionContract = Objects.requireNonNull(subscriptionContractResponse
                                .getData())
                            .flatMap(SubscriptionContractQuery.Data::getSubscriptionContract)
                            .orElse(null);

                        Double linePriceAmount = 0D;
                        for (SubscriptionContractQuery.Edge edge : subscriptionContract.getLines().getEdges()) {
                            Object amount = edge.getNode().getLineDiscountedPrice().getAmount();
                            double v = Double.parseDouble(amount.toString());
                            linePriceAmount = linePriceAmount + v;
                        }

                        linePriceAmount = CommonUtils.round(linePriceAmount, 2);

                        double deliveryPriceAmount = Double.parseDouble(subscriptionContract.getDeliveryPrice().getAmount().toString());

                        double totalSubscriptionContractAmount = linePriceAmount + deliveryPriceAmount;

                        subscriptionContractDetailsDTO.setOrderAmount(totalSubscriptionContractAmount);

                        Double orderUsdAmount = 0d;
                        if (subscriptionContractDetailsDTO.getCurrencyCode().equals("USD")) {
                            orderUsdAmount = subscriptionContractDetailsDTO.getOrderAmount();
                            subscriptionContractDetailsDTO.setOrderAmountUSD(orderUsdAmount);
                        } else {
                            Optional<CurrencyConversionInfo> currencyConversionInfoOptional = currencyConversionInfoRepository.findCurrencyConversionInfoByFrom(subscriptionContractDetailsDTO.getCurrencyCode());
                            if (currencyConversionInfoOptional.isPresent()) {
                                CurrencyConversionInfo currencyConversionInfo = currencyConversionInfoOptional.get();
                                orderUsdAmount = orderUsdAmount + (subscriptionContractDetailsDTO.getOrderAmount() * currencyConversionInfo.getCurrencyRate());
                                subscriptionContractDetailsDTO.setOrderAmountUSD(orderUsdAmount);
                            }
                        }


                        subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

                        Optional<SubscriptionContractProcessingDTO> subscriptionContractProcessingDTOOptional = subscriptionContractProcessingService.findByContractId(subscriptionContractDetailsDTO.getSubscriptionContractId());

                        SubscriptionContractProcessingDTO subscriptionContractProcessingDTO = subscriptionContractProcessingDTOOptional.orElseGet(SubscriptionContractProcessingDTO::new);
                        subscriptionContractProcessingDTO.setContractId(subscriptionContractDetailsDTO.getSubscriptionContractId());
                        subscriptionContractProcessingDTO.setAttemptCount(ObjectUtils.isNotEmpty(subscriptionContractProcessingDTO.getAttemptCount()) ?  subscriptionContractProcessingDTO.getAttemptCount() + 1 : 1);
                        subscriptionContractProcessingService.save(subscriptionContractProcessingDTO);


                    } catch (Exception exception) {
                        String a = "B";
                    }
                }
            });
        }

    }

    /*@GetMapping("/update-firstname-lastname-subscriptioncontractdetails")
    public void updateFirstNameLastNameInSubscriptionContractDetails() {
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findAll();

        Map<String, List<SubscriptionContractDetailsDTO>> SubscriptionContractDetailsDTOByShop = subscriptionContractDetailsDTOList.stream().collect(Collectors.groupingBy(SubscriptionContractDetailsDTO::getShop));

        if (SubscriptionContractDetailsDTOByShop.isEmpty()) {
            return;
        }

        Set<String> shops = SubscriptionContractDetailsDTOByShop.keySet();

        List<SocialConnection> socialConnections = socialConnectionService.findByUserIdIn(shops);

        Map<String, SocialConnection> socialConnectionByShop = socialConnections.stream().collect(Collectors.toMap(SocialConnection::getUserId, s -> s));

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            try {
                boolean updateSubscriptionContractDetailsDTO = false;

                if (StringUtils.isEmpty(subscriptionContractDetailsDTO.getCustomerFirstName()) && StringUtils.isEmpty(subscriptionContractDetailsDTO.getCustomerLastName())) {

                    String shop = subscriptionContractDetailsDTO.getShop();
                    SocialConnection socialConnection = socialConnectionByShop.get(shop);

                    ShopifyGraphqlClient shopifyGraphqlClient = ShopifyGraphqlClientUtils.getShopifyGraphqlClient(shop, socialConnection.getAccessToken());

                    CustomerBriefQuery customerBriefQuery = new CustomerBriefQuery(subscriptionContractDetailsDTO.getGraphCustomerId());
                    Response<Optional<CustomerBriefQuery.Data>> customerResponse = shopifyGraphqlClient.getOptionalQueryResponse(customerBriefQuery);

                    Objects.requireNonNull(customerResponse.getData()).flatMap(CustomerBriefQuery.Data::getCustomer).ifPresent(e -> {
                        subscriptionContractDetailsDTO.setCustomerFirstName(e.getFirstName().orElse(""));
                        subscriptionContractDetailsDTO.setCustomerLastName(e.getLastName().orElse(""));
                    });
                    updateSubscriptionContractDetailsDTO = true;
                }

                if (updateSubscriptionContractDetailsDTO)
                    subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
            } catch (Exception e) {

            }
        }
    }*/

    @Autowired
    private ShopInfoService shopInfoService;

    /*@GetMapping("/update-tags")
    public void updateTags() {
        List<ShopInfoDTO> shopInfoDTOS = shopInfoService.findAll();

        for (ShopInfoDTO shopInfoDTO : shopInfoDTOS) {
            try {
               if(shopInfoDTO.getShop().equals("co2-you.myshopify.com"))
               {
                   shopInfoDTO.setEnableChangeFromNextBillingDate(true);
               }
               else {
                    shopInfoDTO.setEnableChangeFromNextBillingDate(false);
               }
               shopInfoService.save(shopInfoDTO);
            } catch (Exception ex) {

            }
        }
    }*/

    /*@GetMapping("/update-subscription-billing-attempt")
    public void updateSubscriptionBillingAttempt() {
        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findAll();

        for (SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO : subscriptionBillingAttemptDTOList) {
            try {
                boolean updateSubscriptionBillingAttemptDTO = false;

                if (subscriptionBillingAttemptDTO.getStatus() == BillingAttemptStatus.FAILURE) {
                    if (subscriptionBillingAttemptDTO.getTransactionFailedEmailSentStatus() == null) {
                        subscriptionBillingAttemptDTO.setTransactionFailedEmailSentStatus(transactionFailedEmailSentStatus.SENT);
                        updateSubscriptionBillingAttemptDTO = true;
                    }
                }

                if (updateSubscriptionBillingAttemptDTO)
                    subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);
            } catch (Exception e) {

            }
        }
    }*/

    /*@GetMapping("/update-payment-plans")
    public void updatePaymentPlans() {
        List<PaymentPlan> paymentPlans = paymentPlanService.findAll();

        for (PaymentPlan paymentPlan : paymentPlans) {
            try {
                Long recurringChargeId = paymentPlan.getRecurringChargeId();
                if (recurringChargeId == null || recurringChargeId.equals(1L)) {
                    paymentPlan.setTestCharge(true);
                } else {
                    String shop = paymentPlan.getShop();
                    SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();
                    String accessToken = socialConnection.getAccessToken();

                    ShopifyAPI api = new ShopifyWithRateLimiter(accessToken, shop);

                    RecurringApplicationChargeResponse recurringCharge = api.getRecurringCharge(recurringChargeId).getRecurring_application_charge();

                    if (BooleanUtils.isTrue(recurringCharge.isTest())) {
                        paymentPlan.setTestCharge(true);
                    } else {
                        paymentPlan.setTestCharge(false);
                    }
                }

                paymentPlanService.save(paymentPlan);
            } catch (Exception ex) {

            }
        }
    }*/

    private boolean updateDefaultValue(JSONObject customerPortalSettingJson, String key, Object value) {
        try {
            customerPortalSettingJson.putOnce(key, value);
            return true;
        } catch (JSONException je) {
            return false;
        }
    }

    @GetMapping("/update-customer-portal-settings")
    public void BulkUpdateCustomerPortalSettings() {
        List<CustomerPortalSettingsDTO> customerPortalSettingsDTOList = customerPortalSettingsService.findAll();
        customerPortalSettingsDTOList.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        for (CustomerPortalSettingsDTO customerPortalSettingsDTO : customerPortalSettingsDTOList) {
            asyncExecutor.execute(() -> {
                try {
                    JSONObject customerPortalSettingJson = new JSONObject(customerPortalSettingsDTO.getCustomerPortalSettingJson());
                    customerPortalSettingJson.put("enableSplitContract", false);
                    customerPortalSettingJson.put("splitContractMessage", "Once you confirm the split contract. A new, separate contract will be created.");
                    customerPortalSettingJson.put("splitContractText", "Split Contract");
                    customerPortalSettingsDTO.setCustomerPortalSettingJson(customerPortalSettingJson.toString());
                    customerPortalSettingsService.save(customerPortalSettingsDTO);
                } catch (Exception e) {

                }
            });
        }
    }

    /*@GetMapping("/update-subscription-bundle-settings")
    public void BulkUpdateSubscriptionBundleSettings() {
        List<SubscriptionBundleSettingsDTO> subscriptionBundleSettingsDTOList = subscriptionBundleSettingsService.findAll();

        for (SubscriptionBundleSettingsDTO subscriptionBundleSettingsDTO : subscriptionBundleSettingsDTOList) {
            try {

                if (subscriptionBundleSettingsDTO.getSelectedFrequencyLabelText() == null) {
                    subscriptionBundleSettingsDTO.setSelectedFrequencyLabelText("Selected Frequency");
                }

                if (subscriptionBundleSettingsDTO.getAddButtonText() == null) {
                    subscriptionBundleSettingsDTO.setAddButtonText("Add");
                }

                if (subscriptionBundleSettingsDTO.getSelectMinimumProductButtonText() == null) {
                    subscriptionBundleSettingsDTO.setSelectMinimumProductButtonText("Please select minimum {{minProduct}} product");
                }

                if (subscriptionBundleSettingsDTO.getProductsToProceedText() == null) {
                    subscriptionBundleSettingsDTO.setProductsToProceedText("Please select products to proceed");
                }

                if (subscriptionBundleSettingsDTO.getProceedToCheckoutButtonText() == null) {
                    subscriptionBundleSettingsDTO.setProceedToCheckoutButtonText("Proceed to checkout");
                }

                if (subscriptionBundleSettingsDTO.getMyDeliveryText() == null) {
                    subscriptionBundleSettingsDTO.setMyDeliveryText("My {{selectedSellingPlanDisplayName}} delivery");
                }

                subscriptionBundleSettingsService.save(subscriptionBundleSettingsDTO);
            } catch (Exception e) {

            }
        }
    }*/

    @GetMapping("/hide-subscriptions")
    public void hideSubscriptionIds(@RequestParam("shop") String shop, @RequestParam("comaSeparatedSubscriptionIds") String comaSeparatedSubscriptionIds) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();
        boolean isExternal = commonUtils.isExternal();

        hideSubscriptions(shop, comaSeparatedSubscriptionIds, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @GetMapping("/hide-all-subscriptions")
    public void hideAllSubscriptionIds() throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        boolean isExternal = commonUtils.isExternal();
        List<Long> subscriptionIds = subscriptionContractDetailsRepository.findSubscriptionContractIdsByShop(shop);

        hideSubscriptions(shop, subscriptionIds, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
    }

    @GetMapping("/external/v2/hide-subscriptions")
    @ApiOperation("Hide Subscriptions. (Will cancle active/paused subscriptions and delete them.)")
    public void hideSubscriptionIdsExternal(@ApiParam("Your API Key") @RequestParam(value = "api_key") String apiKey,
                                            @ApiParam("Subscription Contact Ids (coma separated)") @RequestParam("comaSeparatedSubscriptionIds") String comaSeparatedSubscriptionIds) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        hideSubscriptions(shop, comaSeparatedSubscriptionIds, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
    }

    private void hideSubscriptions(String shop, String comaSeparatedSubscriptionIds, ActivityLogEventSource eventSource) throws Exception {

        Set<Long> specificSubscriptionIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedSubscriptionIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());

        if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(specificSubscriptionIds)) {
            for (Long specificSubscriptionId : specificSubscriptionIds) {
                subscriptionContractDetailsService.hideSubscription(shop, specificSubscriptionId, eventSource);
            }
        }
    }

    private void hideSubscriptions(String shop, List<Long> specificSubscriptionIds, ActivityLogEventSource eventSource) throws Exception {

        if (!com.amazonaws.util.CollectionUtils.isNullOrEmpty(specificSubscriptionIds)) {
            for (Long specificSubscriptionId : specificSubscriptionIds) {
                subscriptionContractDetailsService.hideSubscription(shop, specificSubscriptionId, eventSource);
            }
        }
    }

    @GetMapping("/set-stop-upcoming-order-flag")
    public void setStopUpcomingOrderFlag(@RequestParam("shop") String shop, @RequestParam(value = "stopUpcomingOrderFlag", required = true) Boolean stopUpcomingOrderFlag) throws Exception {
        shop = SecurityUtils.getCurrentUserLogin().get();
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            subscriptionContractDetailsDTO.setStopUpComingOrderEmail(stopUpcomingOrderFlag);
            log.info("shop=" + shop + " setting stopUpcomingOrderFlag to " + stopUpcomingOrderFlag);
            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
        }
    }

    /*@GetMapping("/update-cart-widget-settings")
    public void BulkUpdateCartWidgetSettings() {
        List<CartWidgetSettings> cartWidgetSettingsList = cartWidgetSettingsService.findAll();

        for (CartWidgetSettings cartWidgetSettings : cartWidgetSettingsList) {
            cartWidgetSettings.setEnable_cart_widget_settings(false);
            //TODO: need to verify value whether it's null or has default value.
            if (StringUtils.isEmpty(cartWidgetSettings.getCartWidgetSettingApproach()))
                cartWidgetSettings.setCartWidgetSettingApproach(CartWidgetSettingApproach.V1);

            if (StringUtils.isEmpty(cartWidgetSettings.getCartFormSelector()))
                cartWidgetSettings.setCartFormSelector("form[action-'/cart']");

            if (StringUtils.isEmpty(cartWidgetSettings.getAppstelCustomeSelector()))
                cartWidgetSettings.setAppstelCustomeSelector("[data-appstel-selector]");

            cartWidgetSettingsService.save(cartWidgetSettings);
        }
    }*/


    @GetMapping("/update-all-subscription")
    public void activeAllSubscriptionForShop(@RequestParam("shop") String shop) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {

            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
            subscriptionDraftInputBuilder.status(SubscriptionContractSubscriptionStatus.safeValueOf(subscriptionContractDetailsDTO.getStatus().toUpperCase()));
            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContractDetailsDTO.getGraphSubscriptionContractId());
            Response<Optional<SubscriptionContractUpdateMutation.Data>> subscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            if (subscriptionContractUpdateMutationResponse.hasErrors()) {
                log.info(subscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                continue;
            }

            List<SubscriptionContractUpdateMutation.UserError> subscriptionContractUpdateMutationUserErrors = Objects.requireNonNull(subscriptionContractUpdateMutationResponse.getData()).map(d -> d.getSubscriptionContractUpdate().map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (subscriptionContractUpdateMutationUserErrors.size() > 0) {
                log.info(subscriptionContractUpdateMutationUserErrors.get(0).getMessage());
                continue;
            }

            String draftId = subscriptionContractUpdateMutationResponse.getData().flatMap(d -> d.getSubscriptionContractUpdate().flatMap(e -> e.getDraft().map(SubscriptionContractUpdateMutation.Draft::getId))).orElse(null);


            SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(draftId, subscriptionDraftInput);
            Response<Optional<SubscriptionDraftUpdateMutation.Data>> optionalDraftUpdateResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftUpdateMutation);

            if (!CollectionUtils.isEmpty(optionalDraftUpdateResponse.getErrors())) {
                log.info(optionalDraftUpdateResponse.getErrors().get(0).getMessage());
                continue;
            }

            List<SubscriptionDraftUpdateMutation.UserError> optionalDraftUpdateResponseUserErrors = optionalDraftUpdateResponse.getData().map(d -> d.getSubscriptionDraftUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
            if (!optionalDraftUpdateResponseUserErrors.isEmpty()) {
                log.info(optionalDraftUpdateResponseUserErrors.get(0).getMessage());
                continue;
            }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

            if (!CollectionUtils.isEmpty(optionalDraftCommitResponse.getErrors())) {
                log.info(optionalDraftCommitResponse.getErrors().get(0).getMessage());
                continue;
            }

            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                log.info(optionalDraftCommitResponseUserErrors.get(0).getMessage());
                continue;
            }
        }
    }


    @PutMapping("/update-subscriptions")
    public void updateSubscriptions(@RequestParam("api_key") String apiKey) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByShop(shop);

        for (SubscriptionContractDetails subscriptionContractDetail : subscriptionContractDetails) {
            asyncExecutor.execute(() -> {
                try {
                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetail.getGraphSubscriptionContractId());
                    Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                    subscriptionContractDetailsService.updateDetails(shop, subscriptionContractQueryResponse.getData().map(d -> d.getSubscriptionContract().orElse(null)).orElse(null));
                } catch (Exception e) {
                    log.info("Error while bulk updating subscription details for id={}", subscriptionContractDetail.getSubscriptionContractId());
                    log.info("Exception={}", ExceptionUtils.getStackTrace(e));
                }
            });
        }
    }

    @GetMapping("/sync-shop-subscription-contracts")
    public void syncShopContractsFromShopify(@RequestParam("api_key") String apiKey, @RequestParam("includeCancelled") Boolean includeCancelled) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByShop(shop);

        List<Long> contractIds = subscriptionContractDetails.stream().map(SubscriptionContractDetails::getSubscriptionContractId).collect(Collectors.toList());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String cursor = null;
        boolean hasNextPage = true;

        while (hasNextPage) {
            SubscriptionContractsQuery subscriptionContractsQuery = new SubscriptionContractsQuery(Input.fromNullable(cursor));
            Response<Optional<SubscriptionContractsQuery.Data>> subscriptionContractsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsQuery);

            List<SubscriptionContractsQuery.Node> subscriptionContracts = requireNonNull(subscriptionContractsQueryResponse.getData())
                .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                .orElse(new ArrayList<>()).stream()
                .map(SubscriptionContractsQuery.Edge::getNode)
                .collect(Collectors.toList());

            hasNextPage = subscriptionContractsQueryResponse.getData().map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                .map(SubscriptionContractsQuery.SubscriptionContracts::getPageInfo)
                .map(SubscriptionContractsQuery.PageInfo::isHasNextPage)
                .orElse(Boolean.FALSE);

            Optional<SubscriptionContractsQuery.Edge> last = subscriptionContractsQueryResponse.getData()
                .map(SubscriptionContractsQuery.Data::getSubscriptionContracts)
                .map(SubscriptionContractsQuery.SubscriptionContracts::getEdges)
                .orElse(new ArrayList<>()).stream()
                .reduce((first, second) -> second);

            cursor = last.map(SubscriptionContractsQuery.Edge::getCursor).orElse(null);

            for (SubscriptionContractsQuery.Node subscriptionContract : subscriptionContracts) {
                if(!contractIds.contains(ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId()))){
                    if(includeCancelled || !subscriptionContract.getStatus().toString().equalsIgnoreCase("CANCELLED")) {
                        asyncExecutor.execute(() -> {
                            try {
                                subscriptionContractDetailsService.getSubscriptionContractRawInternal(ShopifyGraphQLUtils.getSubscriptionContractId(subscriptionContract.getId()), shop, shopifyGraphqlClient, includeCancelled);
                            } catch (Exception e) {
                                log.error("Error while syncing contract with Id: {}, Exception: {}", subscriptionContract.getId(), ExceptionUtils.getStackTrace(e));
                            }
                        });
                    }
                }
            }
        }
    }

    /*@GetMapping("/subscription-outliers")
    public List<Long> subscriptionOutliers(@RequestParam("shop") String shop) throws Exception {

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findByShop(shop);

        List<Long> result = new ArrayList<>();
        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOS) {

            if (!subscriptionContractDetailsDTO.getStatus().equals("active")) {
                continue;
            }

            try {
                List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatusAndShop(subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.QUEUED, shop);
                SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO = subscriptionBillingAttemptDTOList.get(0);
                if (subscriptionContractDetailsDTO.getNextBillingDate().equals(subscriptionBillingAttemptDTO.getBillingDate())) {
                    String a = "b";
                } else {
                    log.info("Invalid Subscription ContractId=" + subscriptionContractDetailsDTO.getSubscriptionContractId());
                    result.add(subscriptionContractDetailsDTO.getSubscriptionContractId());
                }
            } catch (Exception ex) {
                String a = "b";
            }
        }

        return result;
    }*/


    @GetMapping("/external/activate-imported-subscriptions")
    public void activeAllSubscriptionForShop(@RequestParam("api_key") String apiKey,
                                             @RequestParam("status") String status,
                                             @RequestParam("onlyActivateImportedActiveSubscriptions") boolean onlyActivateImportedActiveSubscriptions,
                                             @RequestParam(value = "comaSeparatedSubscriptionIds", required = false) String comaSeparatedSubscriptionIds) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        Set<Long> specificSubscriptionIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedSubscriptionIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findByShop(shop);

        if (onlyActivateImportedActiveSubscriptions) {
            subscriptionContractDetailsDTOS = subscriptionContractDetailsDTOS.stream().filter(s -> s.getImportedId() != null).filter(s -> BooleanUtils.isTrue(s.isPausedFromActive())).collect(Collectors.toList());
        } else {
            subscriptionContractDetailsDTOS = subscriptionContractDetailsDTOS.stream().filter(s -> specificSubscriptionIds.contains(s.getSubscriptionContractId())).collect(Collectors.toList());
        }

        subscriptionContractDetailsDTOS.forEach(subscriptionContractDetailsDTO -> {
            try {
                subscriptionContractDetailsResource.updateStatus(subscriptionContractDetailsDTO.getSubscriptionContractId(), status, null, null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
    }

    @Autowired
    private LiquidUtils liquidUtils;

    /*@GetMapping("/update-recurring-order-tags")
    public void updateRecurringOrderTags(
        @RequestParam("shop") String shop,
        @RequestParam(value = "sinceDays", defaultValue = "2") int sinceDays) throws Exception {

        SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();
        String accessToken = socialConnection.getAccessToken();

        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();

        ShopifyWithRateLimiter shopifyRestApi = new ShopifyWithRateLimiter(accessToken, shop);
        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

        ZonedDateTime dayBeforeYesterday = ZonedDateTime.now().minusDays(sinceDays);
        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findRecentAttemptsWithStatus(shop, BillingAttemptStatus.SUCCESS, dayBeforeYesterday);

        for (SubscriptionBillingAttemptDTO subscriptionBillingAttempt : subscriptionBillingAttemptDTOList) {

            SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionBillingAttempt.getContractId()).get();
            SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionBillingAttempt.getContractId());
            Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

            TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);

            String recurringOrderTagValue = liquidUtils.getValue(tagModel, shopInfo.getRecurringOrderTag());

            String note = null;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionBillingAttempt.getOrderNote())) {
                note = subscriptionBillingAttempt.getOrderNote();
            } else if (org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionContractDetails.getOrderNote())) {
                note = subscriptionContractDetails.getOrderNote();
            }

            updateOrderTags(shopifyRestApi, subscriptionBillingAttempt.getOrderId(), recurringOrderTagValue, note, shopifyGraphqlClient);

            updateOrderMetafields(shopifyRestApi, subscriptionContractDetails, tagModel);
        }
    }*/

    protected void updateOrderMetafields(SubscriptionContractDetails subscriptionContractDetails, TagModel tagModel, Long orderId, ShopifyGraphqlClient shopifyGraphqlClient) throws JsonProcessingException {
        String shop = subscriptionContractDetails.getShop();

        if (orderId == null) {
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String tagModelJsonString = objectMapper.writeValueAsString(tagModel);

            List<MetafieldsSetInput> metafields = new ArrayList<>();
            metafields.add(MetafieldsSetInput.builder()
                .key("details")
                .value(tagModelJsonString)
                .namespace(METAFIELD_NAMESPACE)
                .ownerId(ShopifyIdPrefix.ORDER_ID_PREFIX + orderId)
                .type("json")
                .build());

            MetafieldsSetMutation metafieldsSetMutation = new MetafieldsSetMutation(metafields);
            Response<Optional<MetafieldsSetMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(metafieldsSetMutation);

            if (optionalMutationResponse.hasErrors()) {
                log.error("An error occurred while updating order metafields. shop=" + shop + " orderId=" + orderId + " ex=" + optionalMutationResponse.getErrors().get(0).getMessage() + " orderTag=" + tagModel);
            }

            List<MetafieldsSetMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getMetafieldsSet().map(MetafieldsSetMutation.MetafieldsSet::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.error("An error occurred while updating order metafields. shop=" + shop + " orderId=" + orderId + " ex=" + userErrors.get(0).getMessage() + " orderTag=" + tagModel);
            }

            log.info("Metafield has been updated. shop=" + shop + " orderId=" + orderId + " orderTag=" + tagModel + " tagModelJsonString=" + tagModelJsonString);
        } catch (Exception ex) {
            log.error("An error occurred while updating order metafields. shop=" + shop + " orderId=" + orderId + " ex=" + ExceptionUtils.getStackTrace(ex) + " orderTag=" + tagModel);
        }
    }

    protected void updateOrderTags(ShopifyWithRateLimiter shopifyRestApi, Long orderId, String orderTag, String note, ShopifyGraphqlClient shopifyGraphqlClient) {
        if (orderId == null) {
            return;
        }

        try {
            UpdateOrderRequest updateOrderRequest = new UpdateOrderRequest();
            Order order = shopifyRestApi.getOrder(orderId).getOrder();
            Set<String> orderTags = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(order.getTags()).orElse(org.apache.commons.lang3.StringUtils.EMPTY), ",")).map(String::trim).collect(Collectors.toSet());
            orderTags.add(orderTag);

            order.setTags(org.apache.commons.lang3.StringUtils.join(orderTags, ","));

            if (org.apache.commons.lang3.StringUtils.isNotBlank(note)) {
                order.setNote(note);
            }

            OrderInput.Builder orderInputBuilder = OrderInput.builder();

            if (org.apache.commons.lang3.StringUtils.isNotBlank(note)) {
                order.setNote(note);
                orderInputBuilder.note(note);
            }

            OrderInput orderInput = orderInputBuilder.id(order.getAdminGraphqlApiId()).tags(new ArrayList<>(orderTags)).build();

            OrderUpdateMutation orderUpdateMutation = new OrderUpdateMutation(orderInput);
            Response<Optional<OrderUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(orderUpdateMutation);

            updateOrderRequest.setOrder(order);
            //logger.info("updating order tags for shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " orderTags=" + order.getTags());
            //shopifyRestApi.updateOrder(updateOrderRequest);
            //logger.info("updating order tags successfully for shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " orderTags=" + order.getTags());
        } catch (Exception ex) {
            log.error("An error occurred while updating order tags. shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " ex=" + ExceptionUtils.getStackTrace(ex) + " orderTag=" + Optional.ofNullable(orderTag).orElse("") + " note=" + Optional.ofNullable(note).orElse(""));
        }
    }

    protected TagModel buildTagModel(SubscriptionContractDetails subscriptionContractDetails, Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse) {

        Set<Long> sellingPlanIds = Objects.requireNonNull(subscriptionContractResponse.getData()).map(r -> r.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getLines).map(SubscriptionContractQuery.Lines::getEdges).orElse(new ArrayList<>())).orElse(new ArrayList<>()).stream().map(SubscriptionContractQuery.Edge::getNode).map(SubscriptionContractQuery.Node::getSellingPlanId).filter(Optional::isPresent).map(Optional::get).map(s -> s.replace(ShopifyIdPrefix.SELLING_PLAN_ID_PREFIX, "")).map(Long::parseLong).collect(Collectors.toSet());


        Set<Long> variantIds = Objects.requireNonNull(subscriptionContractResponse.getData()).map(r -> r.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getLines).map(SubscriptionContractQuery.Lines::getEdges).orElse(new ArrayList<>())).orElse(new ArrayList<>()).stream().map(SubscriptionContractQuery.Edge::getNode).map(SubscriptionContractQuery.Node::getVariantId).filter(Optional::isPresent).map(Optional::get).map(s -> s.replace(ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX, "")).map(Long::parseLong).collect(Collectors.toSet());

        Set<String> sellingPlanNames = Objects.requireNonNull(subscriptionContractResponse.getData()).map(r -> r.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getLines).map(SubscriptionContractQuery.Lines::getEdges).orElse(new ArrayList<>())).orElse(new ArrayList<>()).stream().map(SubscriptionContractQuery.Edge::getNode).map(SubscriptionContractQuery.Node::getSellingPlanName).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());

        Set<String> variantNames = Objects.requireNonNull(subscriptionContractResponse.getData()).map(r -> r.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getLines).map(SubscriptionContractQuery.Lines::getEdges).orElse(new ArrayList<>())).orElse(new ArrayList<>()).stream().map(SubscriptionContractQuery.Edge::getNode).map(SubscriptionContractQuery.Node::getVariantTitle).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());

        TagModel tagModel = new TagModel();

        Customer customer = new Customer();
        customer.setId(subscriptionContractDetails.getCustomerId());
        tagModel.setCustomer(customer);

        SubscriptionContract subscriptionContract = new SubscriptionContract();
        subscriptionContract.setId(subscriptionContractDetails.getSubscriptionContractId());
        subscriptionContract.setSellingPlanIds(sellingPlanIds);
        subscriptionContract.setVariantIds(variantIds);
        subscriptionContract.setSellingPlanNames(sellingPlanNames);
        subscriptionContract.setVariantNames(variantNames);
        tagModel.setSubscriptionContract(subscriptionContract);

        com.et.liquid.Order firstOrder = new com.et.liquid.Order();
        firstOrder.setId(subscriptionContractDetails.getOrderId());
        if(subscriptionContractDetails.getOrderId() != null && subscriptionContractDetails.getCreatedAt() != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String formattedDate = dateTimeFormatter.format(subscriptionContractDetails.getCreatedAt().withZoneSameInstant(ZoneId.of("UTC")));
            firstOrder.setCreatedAt(formattedDate);
        }
        tagModel.setFirstOrder(firstOrder);
        return tagModel;
    }

//    @GetMapping("/apply-missed-order-tags")
//    public void applyMissedOrderTags(@RequestParam(value = "api_key") String apiKey) throws Exception {
//
//        String shop = commonUtils.getShopByAPIKey(apiKey).get();
//
//        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
//
//        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
//
//        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);
//        if(shopInfoDTOOpt.isEmpty()){
//            throw new BadRequestAlertException("Shop info not found for shop= " + shop, "", "");
//        }
//
//        ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(shop);
//
//        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();
//
//        for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {
//            try {
//                log.info("Checking for missed order tags for contract:{} and orderId:{}", subscriptionContractDetails.getSubscriptionContractId(), subscriptionContractDetails.getOrderId());
//                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetails.getSubscriptionContractId());
//                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
//
//                TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);
//
//                List<String> firstTimeOrderTagValues = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getFirstTimeOrderTag()).orElse(""), ","))
//                    .map(String::trim)
//                    .map(s -> liquidUtils.getValue(tagModel, s))
//                    .collect(Collectors.toList());
//
//                updateShopifyOrderDetails(shopifyRestApi, subscriptionContractDetails.getOrderId(), firstTimeOrderTagValues, shopifyGraphqlClient);
//
//                updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionContractDetails.getOrderId(), shopifyGraphqlClient);
//
//                List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractDetails.getSubscriptionContractId(), BillingAttemptStatus.SUCCESS);
//
//                for(SubscriptionBillingAttempt subscriptionBillingAttempt : subscriptionBillingAttempts) {
//                    try {
//                        log.info("Checking for missed order tags for contract:{} and orderId{}", subscriptionContractDetails.getSubscriptionContractId(), subscriptionBillingAttempt.getOrderId());
//                        List<String> recurringOrderTagValue = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getRecurringOrderTag()).orElse(""), ","))
//                            .map(String::trim)
//                            .map(s -> liquidUtils.getValue(tagModel, s))
//                            .collect(Collectors.toList());
//
//                        updateShopifyOrderDetails(shopifyRestApi, subscriptionBillingAttempt.getOrderId(), recurringOrderTagValue, shopifyGraphqlClient);
//
//                        updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionBillingAttempt.getOrderId(), shopifyGraphqlClient);
//                    } catch (Exception e) {
//                        log.error("Exception occurred while applying missed order tags for recurring order = {}, exception = {}", subscriptionBillingAttempt.getOrderId(), e);
//                    }
//                }
//
//            } catch (Exception e) {
//                log.error("Exception occurred while applying missed order tags for contract = {}", subscriptionContractDetails.getSubscriptionContractId());
//            }
//        }
//    }

    @GetMapping("/apply-missed-order-tags")
    public void applyMissedOrderTags(@RequestParam(value = "api_key") String apiKey, @RequestParam(value = "sinceDays", defaultValue = "2") int sinceDays) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);


        ZonedDateTime toDate = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime fromDate = toDate.minusDays(sinceDays).withHour(0).withMinute(0).withSecond(0);

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop, fromDate, toDate, null, null, null, null, null, null, null, null, null, null, null, null, null);

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);
        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop info not found for shop= " + shop, "", "");
        }

        ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(shop);

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();

        Map<Long, SubscriptionContractDetails> processedContracts = new HashMap<>();

        for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {
            try {
                log.info("Checking for missed order tags for contract:{} and orderId:{}", subscriptionContractDetails.getSubscriptionContractId(), subscriptionContractDetails.getOrderId());
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetails.getSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);

                List<String> firstTimeOrderTagValues = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getFirstTimeOrderTag()).orElse(""), ","))
                    .map(String::trim)
                    .map(s -> liquidUtils.getValue(tagModel, s))
                    .collect(Collectors.toList());

                updateShopifyOrderDetails(shopifyRestApi, subscriptionContractDetails.getOrderId(), firstTimeOrderTagValues, shopifyGraphqlClient);

                updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionContractDetails.getOrderId(), shopifyGraphqlClient);

                List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractDetails.getSubscriptionContractId(), BillingAttemptStatus.SUCCESS);

                for(SubscriptionBillingAttempt subscriptionBillingAttempt : subscriptionBillingAttempts) {
                    try {
                        log.info("Checking for missed order tags for contract:{} and orderId{}", subscriptionContractDetails.getSubscriptionContractId(), subscriptionBillingAttempt.getOrderId());
                        List<String> recurringOrderTagValue = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getRecurringOrderTag()).orElse(""), ","))
                            .map(String::trim)
                            .map(s -> liquidUtils.getValue(tagModel, s))
                            .collect(Collectors.toList());

                        updateShopifyOrderDetails(shopifyRestApi, subscriptionBillingAttempt.getOrderId(), recurringOrderTagValue, shopifyGraphqlClient);

                        updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionBillingAttempt.getOrderId(), shopifyGraphqlClient);
                    } catch (Exception e) {
                        log.error("Exception occurred while applying missed order tags for recurring order = {}, exception = {}", subscriptionBillingAttempt.getOrderId(), e.getMessage());
                    }
                }

                processedContracts.put(subscriptionContractDetails.getSubscriptionContractId(), subscriptionContractDetails);

            } catch (Exception e) {
                log.error("Exception occurred while applying missed order tags for contract = {}", subscriptionContractDetails.getSubscriptionContractId());
            }
        }

        List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findRecentAttemptsWithStatus(shop, BillingAttemptStatus.SUCCESS, fromDate);

        subscriptionBillingAttemptDTOList = subscriptionBillingAttemptDTOList.stream().filter(attempt -> !processedContracts.containsKey(attempt.getContractId())).collect(Collectors.toList());

        for (SubscriptionBillingAttemptDTO subscriptionBillingAttempt : subscriptionBillingAttemptDTOList) {
            try{
                SubscriptionContractDetails subscriptionContractDetails;
                if(processedContracts.containsKey(subscriptionBillingAttempt.getContractId())){
                    subscriptionContractDetails = processedContracts.get(subscriptionBillingAttempt.getContractId());
                }else{
                    subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionBillingAttempt.getContractId()).get();
                    processedContracts.put(subscriptionBillingAttempt.getContractId(), subscriptionContractDetails);
                }

                log.info("Checking for missed order tags for contract:{} and orderId{}", subscriptionContractDetails.getSubscriptionContractId(), subscriptionBillingAttempt.getOrderId());

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionBillingAttempt.getContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);

                List<String> recurringOrderTagValue = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getRecurringOrderTag()).orElse(""), ","))
                    .map(String::trim)
                    .map(s -> liquidUtils.getValue(tagModel, s))
                    .collect(Collectors.toList());

                updateShopifyOrderDetails(shopifyRestApi, subscriptionBillingAttempt.getOrderId(), recurringOrderTagValue, shopifyGraphqlClient);

                updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionBillingAttempt.getOrderId(), shopifyGraphqlClient);
            } catch (Exception e) {
                log.error("Exception occurred while applying missed order tags for recurring order = {}, exception = {}", subscriptionBillingAttempt.getOrderId(), e.getMessage());
            }
        }
    }

    @GetMapping("/update-email-template-settings")
    public void updateEmailTemplateSettings() {

        try {
            String shop = commonUtils.getShop();

            ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

            Shop shopDetails = api.getShopInfo().getShop();

            onBoardingHandler.insertEmailTemplateSetting(shop, shopDetails);

        } catch (Exception e) {
            log.error("Error while updating email template settings", e);
        }
    }

    @GetMapping("/add-new-email-template-setting")
    public void addNewEmailTemplateSettings() {

        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        socialConnections.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

        for (SocialConnection socialConnection : socialConnections) {
            asyncExecutor.execute(() -> {

                try {
                    String shop = socialConnection.getUserId();

                    Optional<EmailTemplateSetting> emailTemplateSettingOptional = emailTemplateSettingRepository.findByShopAndEmailSettingType(shop, EmailSettingType.OUT_OF_STOCK);

                    if (emailTemplateSettingOptional.isEmpty()) {

                        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

                        Shop shopDetails = api.getShopInfo().getShop();

                        EmailTemplateSetting emailTemplateSetting = emailTemplateSettingService.buildEmailSettingForOutOfStock(shop, shopDetails);
                        emailTemplateSetting.setSendEmailDisabled(true);
                        emailTemplateSettingRepository.save(emailTemplateSetting);
                    }
                } catch (Exception e) {
                    log.error("Error while updating email template settings", e);
                }
            });
        }
    }


    @GetMapping("/add-new-bulk-email-template-setting")
    public void addNewBulkEmailTemplateSettings() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        socialConnections.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

        for (SocialConnection socialConnection : socialConnections) {
            asyncExecutor.execute(() -> {
                try {
                    String shop = socialConnection.getUserId();
                    ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);
                    log.info("Adding bulk emails template settings for shop : {}", shop);
                    onBoardingHandler.insertBulkEmailsTemplateSetting(shop, api.getShopInfo().getShop());
                } catch (Exception e) {
                    log.error("Error while updating email template settings", e);
                }
            });
        }
    }

    @Autowired
    private ShopifyGraphqlSubscriptionContractService shopifyGraphqlSubscriptionContractService;

    @GetMapping("/update-contract-status")
    public void updateContractStatus() throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            if (!subscriptionContractDetailsDTO.getOrderName().equals("UNKNOWN")) {
                continue;
            }

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

            subscriptionDraftInputBuilder.status(SubscriptionContractSubscriptionStatus.safeValueOf("PAUSED"));

            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionDraftInput);

            if (subscriptionContractUpdateResult.isSuccess()) {
                //TODO update DB
                subscriptionContractDetailsDTO.setStatus("paused");
                subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
            } else {
                log.info("errorMessage=" + subscriptionContractUpdateResult.getErrorMessage());
            }
        }

    }

    private SellingPlanInterval getIntervalType(String billingIntervalType) {
        if (billingIntervalType.equalsIgnoreCase("month") || billingIntervalType.equalsIgnoreCase("months")) {
            return SellingPlanInterval.MONTH;
        } else if (billingIntervalType.equalsIgnoreCase("week") || billingIntervalType.equalsIgnoreCase("weeks")) {
            return SellingPlanInterval.WEEK;
        } else if (billingIntervalType.equalsIgnoreCase("day") || billingIntervalType.equalsIgnoreCase("days")) {
            return SellingPlanInterval.DAY;
        }
        return SellingPlanInterval.YEAR;
    }

    private void setDeliveryIntervalType(SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder, String deliveryIntervalType) {
        if (deliveryIntervalType.equalsIgnoreCase("month")) {
            deliveryPolicyBuilder.interval(SellingPlanInterval.MONTH);
        } else if (deliveryIntervalType.equalsIgnoreCase("week")) {
            deliveryPolicyBuilder.interval(SellingPlanInterval.WEEK);
        } else if (deliveryIntervalType.equalsIgnoreCase("day")) {
            deliveryPolicyBuilder.interval(SellingPlanInterval.DAY);
        }
    }

    @GetMapping("/customer-payment-methods")
    public List<CustomerPaymentMethodsQuery.Node> customerPaymentMethods(@RequestParam("shop") String shop, @RequestParam("customerId") Long customerId) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, Input.optional(false));

        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

        List<CustomerPaymentMethodsQuery.Node> paymentMethods = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(CustomerPaymentMethodsQuery.Edge::getNode).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        return paymentMethods;

    }

    @GetMapping("/update subscription-payment-method")
    public void updateSubscriptionPaymentMethod(@RequestParam("shop") String shop, @RequestParam("customerId") Long customerId, @RequestParam("paymentMethodId") String paymentMethodId) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByCustomerId(customerId);

        for (SubscriptionContractDetails subscriptionContractDetail : subscriptionContractDetails) {
            Long contractId = subscriptionContractDetail.getSubscriptionContractId();

            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

            if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                throw new BadRequestAlertException(optionalSubscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionContractUpdateMutation.UserError> subscriptionContractUpdateMutationUserErrors = optionalSubscriptionContractUpdateMutationResponse.getData().map(d -> d.getSubscriptionContractUpdate().map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!subscriptionContractUpdateMutationUserErrors.isEmpty()) {
                throw new BadRequestAlertException(subscriptionContractUpdateMutationUserErrors.get(0).getMessage(), "", "");
            }

            String draftId = optionalSubscriptionContractUpdateMutationResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId()).get();

            SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
            subscriptionDraftInputBuilder.paymentMethodId(paymentMethodId);

            SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

            SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(draftId, subscriptionDraftInput);
            Response<Optional<SubscriptionDraftUpdateMutation.Data>> optionalDraftUpdateResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftUpdateMutation);

            if (!CollectionUtils.isEmpty(optionalDraftUpdateResponse.getErrors())) {
                throw new BadRequestAlertException(optionalDraftUpdateResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionDraftUpdateMutation.UserError> optionalDraftUpdateResponseUserErrors = optionalDraftUpdateResponse.getData().map(d -> d.getSubscriptionDraftUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
            if (!optionalDraftUpdateResponseUserErrors.isEmpty()) {
                throw new BadRequestAlertException(optionalDraftUpdateResponseUserErrors.get(0).getMessage(), "", "");
            }

            // commit draft
            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

            if (!CollectionUtils.isEmpty(optionalDraftCommitResponse.getErrors())) {
                throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
            }

            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                throw new BadRequestAlertException(optionalDraftCommitResponseUserErrors.get(0).getMessage(), "", "");
            }
        }
    }

    private List<CustomerPaymentMethodsQuery.Node> getPaymentMethodList(long customerId, ShopifyGraphqlClient shopifyGraphqlClient, CSVRecord subscriptionDataRecord, List<String> customerCreatedStripePaymentIds) throws Exception {
        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, Input.optional(true));

        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

        List<CustomerPaymentMethodsQuery.Node> paymentMethods = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!customerCreatedStripePaymentIds.isEmpty()) {
            String customerCreatedStripePaymentId = customerCreatedStripePaymentIds.get(0);
            List<String> paymentMethodIds = paymentMethods.stream().map(CustomerPaymentMethodsQuery.Node::getId).collect(Collectors.toList());
            if (paymentMethodIds.contains(customerCreatedStripePaymentId)) {
                paymentMethods.removeIf(paymentMethod -> !customerCreatedStripePaymentId.equalsIgnoreCase(paymentMethod.getId()));
                return paymentMethods;
            }
        }
        paymentMethods.removeIf(paymentMethod -> paymentMethod.getRevokedReason().isPresent());

        if (paymentMethods.size() > 1 && subscriptionDataRecord.isMapped("Last 4 Digits") && subscriptionDataRecord.isSet("Last 4 Digits") && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Last 4 Digits"))) {

            paymentMethods = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().filter(e -> {
                String last4Digits = subscriptionDataRecord.get("Last 4 Digits");

                if ("paypal".equalsIgnoreCase(last4Digits)) {
                    if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerPaypalBillingAgreement) {
                        return !((CustomerPaymentMethodsQuery.AsCustomerPaypalBillingAgreement) e.getNode().getInstrument().get()).isInactive();
                    } else {
                        return false;
                    }
                } else {
                    if (last4Digits.length() == 3) {
                        last4Digits = "0" + last4Digits.trim();
                    }

                    if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerCreditCard) {
                        return ((CustomerPaymentMethodsQuery.AsCustomerCreditCard) e.getNode().getInstrument().get()).getLastDigits().equalsIgnoreCase(last4Digits);
                    } else if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerShopPayAgreement) {
                        return ((CustomerPaymentMethodsQuery.AsCustomerShopPayAgreement) e.getNode().getInstrument().get()).getLastDigits().equalsIgnoreCase(last4Digits);
                    } else {
                        return false;
                    }
                }
            }).map(e -> e.getNode()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            while (paymentMethods.size() > 1) {
                paymentMethods.remove(paymentMethods.size() - 1);
            }
        }

        return paymentMethods;
    }

    private String buildNextBillingDate(String nextOrderDate, int billingIntervalCount, String intervalType) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_STAMP_FORMAT);
        if (org.apache.commons.lang3.StringUtils.isBlank(nextOrderDate) || org.apache.commons.lang3.StringUtils.isBlank(nextOrderDate.trim())) {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(10);
            String nextBillingDate = dateTimeFormatter.format(now);
            return nextBillingDate;
        }
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd'T'HH:mm:ssz", "yy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "MM/dd/yyyy", "M/dd/yyyy", "M/dd/yyyy", "M/d/yyyy", "M/d/yyyy", "M/dd/yy", "MM/dd/yy", "M/d/yy", "MM/d/yy", "d/M/yyyy H:mm", "dd/M/yyyy H:mm", "dd/MM/yyyy H:mm", "M/dd/yyyy H:mm", "MM/dd/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd H:mm:ss");
        for (String formatString : formatStrings) {
            try {
                return getNextOrderDateObj(nextOrderDate, billingIntervalCount, intervalType, dateTimeFormatter, formatString);
            } catch (Exception e) {
            }
        }
        return null;
    }

    @NotNull
    private String getNextOrderDateObj(String nextOrderDate, int billingIntervalCount, String intervalType, DateTimeFormatter dateTimeFormatter, String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(s);
        LocalDate parse1 = LocalDate.parse(nextOrderDate, formatter);
        ZonedDateTime parse = parse1.atStartOfDay().atZone(ZoneId.of("UTC"));
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        while (parse.toLocalDate().compareTo(utcNow.toLocalDate()) <= 0) {
            parse = SubscriptionUtils.getNextBillingDate(billingIntervalCount, intervalType, parse);
        }
        String nextBillingDate = dateTimeFormatter.format(parse);
        return nextBillingDate;
    }

    /*@GetMapping("/subscription-groups-sync-all")
    public String getAllInsert() throws Exception {

        subscriptionGroupService.insertAllShopSubscriptionGroupsV2();
        return "success - its async process - so process will be in background";
    }*/

    @Autowired
    private PaymentPlanService paymentPlanService;

    /*@GetMapping("/update-payment-plan-permissions")
    public String updatePaymentPlayPermissions() throws Exception {
        List<PaymentPlan> paymentPlanList = paymentPlanService.findAll();
        for (PaymentPlan paymentPlan : paymentPlanList) {
            asyncExecutor.execute(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(paymentPlan.getAdditionalDetails());

                    Set<String> keySet = jsonObject.keySet();

                    if (!keySet.contains("accessWidgetDesignOptions")) {
                        jsonObject.put("accessWidgetDesignOptions", true);
                    }

                    if (!keySet.contains("accessCustomWidgetLocations")) {
                        jsonObject.put("accessCustomWidgetLocations", true);
                    }

                    if (!keySet.contains("accessSubscriptionActivityLogs")) {
                        jsonObject.put("accessSubscriptionActivityLogs", true);
                    }

                    if (!keySet.contains("accessBuildABox")) {
                        jsonObject.put("accessBuildABox", true);
                    }

                    if (!keySet.contains("accessResendEmail")) {
                        jsonObject.put("accessResendEmail", true);
                    }

                    if (!keySet.contains("accessKlaviyoContactSync")) {
                        jsonObject.put("accessKlaviyoContactSync", true);
                    }

                    if (!keySet.contains("accessOneTimeProductUpsells")) {
                        jsonObject.put("accessOneTimeProductUpsells", true);
                    }

                    if (!keySet.contains("accessAdvanceSubscriptionPlanOptions")) {
                        jsonObject.put("accessAdvanceSubscriptionPlanOptions", true);
                    }

                    if (!keySet.contains("accessSplitContract")) {
                        jsonObject.put("accessSplitContract", true);
                    }

                    if (!keySet.contains("accessDiscountOnCancellationAttempt")) {
                        jsonObject.put("accessDiscountOnCancellationAttempt", true);
                    }

                    if (!keySet.contains("accessQuickCheckout")) {
                        jsonObject.put("accessQuickCheckout", true);
                    }

                    if (!keySet.contains("accessSubscriberLoyaltyFeatures")) {
                        jsonObject.put("accessSubscriberLoyaltyFeatures", true);
                    }

                    if (!keySet.contains("webhookAccess")) {
                        jsonObject.put("webhookAccess", false);
                    }

                    paymentPlan.setAdditionalDetails(jsonObject.toString());

                    paymentPlanService.save(paymentPlan);

                } catch (Exception ex) {
                    log.info("An error occurred while updating shop=" + paymentPlan.getShop());
                }
            });
        }
        return null;
    }*/

    /*@GetMapping("/delete-subscription-plans")
    @CrossOrigin
    public String deleteEverPlanButFirst(@RequestParam("shop") String shop, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v1 /delete-subscription-plans RequestURL: {}, shop: {}", RequestURL, shop);

        return deleteSubscriotionPlans(shop);
    }*/

    /*@GetMapping("/external/v2/delete-subscription-plans")
    @CrossOrigin
    @ApiOperation("Delete Subscription Plan")
    public String deleteEverPlanButFirstV2(@ApiParam("Your API Key") @RequestParam(value = "api_key", required = true) String apiKey, HttpServletRequest request) throws Exception {

        commonUtils.restrictV1APIRequestFromCustomerPortal(request);
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("REST request v2 RequestURL: {} /external/v2/delete-subscription-plans api_key: {}", RequestURL, apiKey);

        String shop = SecurityUtils.getCurrentUserLogin().get();
        return deleteSubscriotionPlans(shop);
    }*/

    @NotNull
    private String deleteSubscriotionPlans(String shop) throws Exception {
        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(shop);

        for (int j = 1, shopSubscriptionGroupsSize = shopSubscriptionGroups.size(); j < shopSubscriptionGroupsSize; j++) {
            SubscriptionGroupV2DTO shopSubscriptionGroup = shopSubscriptionGroups.get(j);
            subscriptionGroupService.delete(shop, shopSubscriptionGroup.getId());
        }

        return "success - bulk subscription created";
    }


    @PostMapping("/bulk-attach-products-in-subscription-groups")
    public String bulkAttachProductsInSubscriptionGroups(@RequestParam(value = "product-data", required = false) MultipartFile productDataFile) throws Exception {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        CSVParser productDataCSVParser = null;
        Reader customerDataReader = new BufferedReader(new InputStreamReader(productDataFile.getInputStream()));
        productDataCSVParser = new CSVParser(customerDataReader, csvFormat);

        for (String header : ImportConstants.PRODUCT_DATA_HEADERS) {
            if (!productDataCSVParser.getHeaderNames().contains(header)) {
                throw new BadRequestAlertException(header + " is missing in product-data file", "", "");
            }
        }

        if (productDataCSVParser.getHeaderNames().contains("external_variant_id") && !productDataCSVParser.getHeaderNames().contains("variant_title")) {
            throw new BadRequestAlertException("variant_title is required when external_variant_id is provided in product-data file", "", "");
        }

        Map<String, List<ProductDetails>> productDetailsMap = new HashMap<>();
        for (CSVRecord csvRecord : productDataCSVParser) {
            String productId = csvRecord.get("external_product_id");
            String title = csvRecord.get("title");
            String planName = csvRecord.get("Plan name");

            List<ProductDetails> planProducts = productDetailsMap.get(planName);
            if (CollectionUtils.isEmpty(planProducts)) {
                planProducts = new ArrayList<>();
            }

            ProductDetails productDetails = new ProductDetails();
            productDetails.setId(Long.parseLong(productId));
            productDetails.setTitle(title);


            if (csvRecord.isMapped("external_variant_id") && csvRecord.isMapped("variant_title")) {
                String variantId = csvRecord.get("external_variant_id");
                String variantTitle = csvRecord.get("variant_title");
                if (variantId != null && variantTitle != null) {
                    productDetails.setVariantId(Long.parseLong(variantId));
                    productDetails.setVariantTitle(variantTitle);
                }
            }

            planProducts.add(productDetails);

            productDetailsMap.put(planName, planProducts);
        }

        String shop = commonUtils.getShop();
        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(shop);

        List<String> incomingPlanNames = new ArrayList<String>(productDetailsMap.keySet());
        List<String> createdPlanNames = shopSubscriptionGroups.stream().map(SubscriptionGroupV2DTO::getGroupName).collect(Collectors.toList());

        List<String> unavailable = incomingPlanNames.stream().filter(e -> (createdPlanNames.stream().filter(d -> d.equals(e)).count()) < 1).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(unavailable)) {
            throw new BadRequestAlertException(String.join(",", unavailable) + " subscription plan exists in csv but not in appstle app.", "", "");
        }
        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
        duplicateSubscriptionPlansFromGivenPlan(productDetailsMap, shop, shopSubscriptionGroups, null, shopifyAPI);

        return "success - bulk subscription created";
    }

    private void duplicateSubscriptionPlansFromGivenPlan(Map<String, List<ProductDetails>> productDetailsMap, String shop, List<SubscriptionGroupV2DTO> shopSubscriptionGroups, String newPlanName, ShopifyAPI shopifyAPI) {

//        List<Long> missingProductIds = getMissingProductIdsInShopify(productDetailsMap, shopifyAPI);
        ignoreMissingProductIdsAndAddTitleForRemaining(productDetailsMap, shopifyAPI);
//        if(!CollectionUtils.isEmpty(missingProductIds)) {
//            throw new BadRequestAlertException(org.apache.commons.lang3.StringUtils.join(missingProductIds, ',') + " products exists in csv but on Shopify.", "", "");
//        }

        for (SubscriptionGroupV2DTO subscriptionGroupV2DTO : shopSubscriptionGroups) {
            try {
                SubscriptionGroupV2DTO subscriptionGroupDTO = subscriptionGroupService.findSingleSubscriptionGroupV2(shop, subscriptionGroupV2DTO.getId()).get();

                List<ProductDetails> incomingPlanProducts = productDetailsMap.get(subscriptionGroupDTO.getGroupName());
                if (!CollectionUtils.isEmpty(incomingPlanProducts)) {
                    List<ProductDetails> planProducts = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                    }, subscriptionGroupDTO.getProductIds());
                    planProducts.addAll(incomingPlanProducts);
                    planProducts = planProducts.stream().distinct().collect(Collectors.toList());

                    int productPartitionCounter = 1;
                    for (List<ProductDetails> productPartition : Lists.partition(planProducts, 5000)) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();

                        String productIdsJson = gson.toJson(productPartition.stream().filter(p -> p.getVariantId() == null).map(p -> {
                            ProductDetails productDetails = new ProductDetails();
                            productDetails.setId(p.getId());
                            productDetails.setTitle(p.getTitle());
                            return productDetails;
                        }).collect(Collectors.toList()));

                        String variantIdJson = gson.toJson(productPartition.stream().filter(p -> p.getVariantId() != null).map(p -> {
                            ProductDetails productDetails = new ProductDetails();
                            productDetails.setId(p.getVariantId());
                            productDetails.setTitle(p.getVariantTitle());
                            return productDetails;
                        }).collect(Collectors.toList()));
//                        String groupName = subscriptionGroupV2DTO.getGroupName() + (productPartitionCounter != 0 ? (" " + productPartitionCounter) : "");
                        String groupName = org.apache.commons.lang3.StringUtils.isNotBlank(newPlanName) ? (newPlanName + " " + productPartitionCounter) : getGroupName(subscriptionGroupV2DTO, productPartitionCounter);
                        subscriptionGroupDTO.setGroupName(groupName);
                        subscriptionGroupDTO.setProductIds(productIdsJson);
                        subscriptionGroupDTO.setVariantIds(variantIdJson);
//                        if (productPartitionCounter == 0) {
//                            subscriptionGroupResource.updateSubscriptionGroupV2(subscriptionGroupDTO);
//                        } else {
                        SubscriptionGroupV2DTO subscriptionGroupV2DTO1 = shopSubscriptionGroups.stream().filter(f -> f.getGroupName().equals(groupName)).findFirst().orElse(null);
                        if (subscriptionGroupV2DTO1 != null) {
                            subscriptionGroupService.delete(shop, subscriptionGroupV2DTO1.getId());
                        }
                        subscriptionGroupDTO.setId(null);
                        subscriptionGroupResource.createSubscriptionGroupV2(subscriptionGroupDTO);
//                        }
                        productPartitionCounter++;
                    }
                }
            } catch (Exception e) {
                log.info("Something went wrong while importing products: error=" + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    @NotNull
    private String getGroupName(SubscriptionGroupV2DTO subscriptionGroupV2DTO, int productPartitionCounter) {
        return subscriptionGroupV2DTO.getGroupName() + (productPartitionCounter != 0 ? (" " + productPartitionCounter) : "");
    }

    @NotNull
    private List<Long> getMissingProductIdsInShopify(Map<String, List<ProductDetails>> productDetailsMap, ShopifyAPI shopifyAPI) {
        List<Long> missingProductIds = new ArrayList<>();
        for (var entry : productDetailsMap.entrySet()) {
            List<Long> productIdsList = entry.getValue().stream().map(ProductDetails::getId).collect(Collectors.toList());
            for (List<Long> productIdsPartition : Lists.partition(productIdsList, 50)) {
                GetProductsResponse getProductsResponse = shopifyAPI.getProducts(new HashSet<Long>(productIdsPartition));
                missingProductIds.addAll(productIdsPartition.stream().filter(e -> (getProductsResponse.getProducts().stream().filter(d -> d.getId().equals(e)).count()) < 1).collect(Collectors.toList()));
            }
        }
        return missingProductIds;
    }

    private void ignoreMissingProductIdsAndAddTitleForRemaining(Map<String, List<ProductDetails>> productDetailsMap, ShopifyAPI shopifyAPI) {
        for (var entry : productDetailsMap.entrySet()) {
            List<ProductDetails> entryValue = entry.getValue();
            List<Long> productIdsList = entryValue.stream().map(ProductDetails::getId).collect(Collectors.toList());
            for (List<Long> productIdsPartition : Lists.partition(productIdsList, 50)) {
                GetProductsResponse getProductsResponse = shopifyAPI.getProducts(new HashSet<Long>(productIdsPartition));
                List<Long> missingProductIdsForPlan = productIdsPartition.stream().filter(e -> (getProductsResponse.getProducts().stream().filter(d -> d.getId().equals(e)).count()) < 1).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(missingProductIdsForPlan)) {
                    log.info("MissingProductIdsForPlan: {}", org.apache.commons.lang3.StringUtils.join(missingProductIdsForPlan, ','));
                    entryValue.removeAll(entryValue.stream().filter(f -> missingProductIdsForPlan.contains(f.getId())).collect(Collectors.toList()));
                    productDetailsMap.put(entry.getKey(), entryValue);
                }

                Map<Long, ProductDetails> productDetailsByProductIdMap = entryValue.stream().collect(Collectors.toMap(ProductDetails::getId, p -> p, (firstValue, secondValue) -> firstValue));
                Map<Long, String> productsByProductIdMap = getProductsResponse.getProducts().stream().collect(Collectors.toMap(d -> d.getId(), p -> p.getTitle()));

                Map<Long, String> variantIdMap = new HashMap<>();
                for (com.et.api.shopify.product.Product product : getProductsResponse.getProducts()) {
                    for (Variant variant : product.getVariants()) {
                        variantIdMap.put(variant.getId(), product.getTitle() + " - " + variant.getTitle());
                    }
                }

                productIdsPartition.forEach(productId -> {
                    ProductDetails productDetails = productDetailsByProductIdMap.get(productId);
                    String title = productsByProductIdMap.get(productId);
                    if (productDetails != null && org.apache.commons.lang3.StringUtils.isNotBlank(title)) {
                        productDetails.setTitle(title);
                    }

                    if (productDetails != null && productDetails.getVariantId() != null) {
                        productDetails.setVariantTitle(variantIdMap.get(productDetails.getVariantId()));
                    }
                });
            }
        }
    }

    @PostMapping("/bulk-subscription-groups")
    public String createBulkSubscription(@RequestBody SubscriptionGroupV2DTO subscriptionGroupDTO1,
                                         @RequestParam(value = "planName", required = true) String planName,
                                         @RequestParam(value = "existingPlanName", required = false) String existingPlanName) throws Exception {

        String shop = SecurityUtils.getCurrentUserLogin().get();

        List<SubscriptionGroupV2DTO> shopSubscriptionGroups = subscriptionGroupService.findShopSubscriptionGroupsV2(shop);
        List<SubscriptionGroupV2DTO> subscriptionGroupV2DTOS = shopSubscriptionGroups.stream().filter(s -> s.getGroupName().equals(existingPlanName)).collect(Collectors.toList());
        Optional<SubscriptionGroupV2DTO> subscriptionGroupDTO = null;
        for (SubscriptionGroupV2DTO subscriptionGroupV2DTO : subscriptionGroupV2DTOS) {
            subscriptionGroupDTO = subscriptionGroupService.findSingleSubscriptionGroupV2(commonUtils.getShop(), subscriptionGroupV2DTO.getId());
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(subscriptionGroupDTO1.getProductIds())) {
            createPlanForAllProducts(subscriptionGroupDTO.get(), planName);
        } else {
            ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

            List<String> productIdList = Arrays.asList(subscriptionGroupDTO1.getProductIds().split("[\\s,]+"));
            List<ProductDetails> productDetailsList = productIdList.stream().map(p -> new ProductDetails(Long.parseLong(p))).collect(Collectors.toList());
            Map<String, List<ProductDetails>> productDetailsMap = new HashMap<>();
            productDetailsMap.put(existingPlanName, productDetailsList);
            duplicateSubscriptionPlansFromGivenPlan(productDetailsMap, shop, subscriptionGroupV2DTOS, planName, shopifyAPI);
        }

        return "success - bulk subscription created";
    }

    private void createPlanForAllProducts(SubscriptionGroupV2DTO subscriptionGroupDTO1, String planName) throws Exception {
        boolean isFirst = true;
        String cusrsor = "";
        boolean isNextPage = true;
        String shop = SecurityUtils.getCurrentUserLogin().get(); //"test-gaurang-subscription-007.myshopify.com";
        int maxProd = 5000;
        List<Product> products = new ArrayList<>();
        Integer i = 1;

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        while (true) {
            while (products.size() < maxProd && isNextPage) {
                ProductData productData = subscriptionDataService.findShopProducts(shop, "", isFirst ? false : isNextPage, isFirst ? "" : cusrsor, shopifyGraphqlClient);
                products.addAll(productData.getProducts());
                isNextPage = productData.getPageInfo().isHasNextPage();
                cusrsor = productData.getPageInfo().getCursor();
                isFirst = false;
            }

            buildSubscriptionGroupV2DTO(subscriptionGroupDTO1, products, i, planName);
            i++;

            if (products.size() < maxProd) {
                break;
            } else {
                products.clear();
            }
        }
    }


    private ResponseEntity<SubscriptionGroupV2DTO> buildSubscriptionGroupV2DTO(SubscriptionGroupV2DTO subscriptionGroupDTO, List<Product> products, Integer i, String planName) throws Exception {
        List<ProductDetails> productDetailsArrayList = new ArrayList<>();
        for (Product product : products) {
            ProductDetails productDetails = new ProductDetails();
            productDetails.setId(product.getId());
            productDetails.setTitle(product.getTitle());
            productDetailsArrayList.add(productDetails);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String JSONObject = gson.toJson(productDetailsArrayList);
        subscriptionGroupDTO.setId(null);
        subscriptionGroupDTO.setGroupName(planName + " " + i);
        subscriptionGroupDTO.setProductIds(JSONObject);

        return subscriptionGroupResource.createSubscriptionGroupV2(subscriptionGroupDTO);
    }

    /*@GetMapping("/update-shop-queued-attempts")
    public String updateQueuedAttempts() throws Exception {

        String shop = "motorized-coffee-company.myshopify.com";

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            commonUtils.updateQueuedAttempts(subscriptionContractDetailsDTO.getNextBillingDate(), subscriptionContractDetailsDTO.getShop(), subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.getBillingPolicyInterval(), subscriptionContractDetailsDTO.getMaxCycles());
        }

        return "success - updateQueuedAttempts";
    }*/

    @PutMapping("/fix-variant-price")
    public List<String> fixVariantPrice(@RequestParam(value = "shop", required = false) String shop, @RequestBody Map<Long, Double> priceByVariantId) throws Exception {
        List<String> result = new ArrayList<>();

        //if (shop == null) {
        shop = SecurityUtils.getCurrentUserLogin().get();
        //}

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (int i = 0; i < subscriptionContractDetailsList.size(); i++) {
            try {
                log.info("iterating i=" + i + " from size=" + subscriptionContractDetailsList.size());
                SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsList.get(i);

                String graphSubscriptionContractId = subscriptionContractDetails.getGraphSubscriptionContractId();
                SubscriptionContractLineItemsQuery subscriptionContractQuery = new SubscriptionContractLineItemsQuery(graphSubscriptionContractId);
                Response<Optional<SubscriptionContractLineItemsQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                List<SubscriptionContractLineItemsQuery.Node> lineItems = requireNonNull(subscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().map(SubscriptionContractLineItemsQuery.SubscriptionContract::getLines).map(SubscriptionContractLineItemsQuery.Lines::getEdges).orElse(new ArrayList<>())).orElse(new ArrayList<>()).stream().map(SubscriptionContractLineItemsQuery.Edge::getNode).collect(Collectors.toList());

                for (SubscriptionContractLineItemsQuery.Node lineItem : lineItems) {

                    try {
                        if (lineItem.getVariantId().isEmpty()) {
                            continue;
                        }

                        long variantIdLong = Long.parseLong(lineItem.getVariantId().get().replace(PRODUCT_VARIANT_ID_PREFIX, ""));
                        if (priceByVariantId.containsKey(variantIdLong)) {

                            Double price = priceByVariantId.get(variantIdLong);

                            if (price.equals(Double.parseDouble(lineItem.getCurrentPrice().getAmount().toString()))) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " price is already same. price=" + price);
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " price is already same. price=" + price);
                                continue;
                            }

                            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(graphSubscriptionContractId);
                            Response<Optional<SubscriptionContractUpdateMutation.Data>> subscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                            if (subscriptionContractUpdateMutationResponse.hasErrors()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                                continue;
                            }

                            List<SubscriptionContractUpdateMutation.UserError> subscriptionContractUpdateMutationResponseUserErrors = subscriptionContractUpdateMutationResponse.getData().map(d -> d.getSubscriptionContractUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                            if (!subscriptionContractUpdateMutationResponseUserErrors.isEmpty()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionContractUpdateMutationResponseUserErrors.get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionContractUpdateMutationResponseUserErrors.get(0).getMessage());
                                continue;
                            }

                            String draftId = subscriptionContractUpdateMutationResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId()).get();

                            SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();
                            subscriptionLineUpdateInputBuilder.currentPrice(price);
                            SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                            SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, lineItem.getId(), subscriptionLineUpdateInput);
                            Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> subscriptionDraftLineUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                            if (subscriptionDraftLineUpdateMutationResponse.hasErrors()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionDraftLineUpdateMutationResponse.getErrors().get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionDraftLineUpdateMutationResponse.getErrors().get(0).getMessage());
                                continue;
                            }

                            List<SubscriptionDraftLineUpdateMutation.UserError> subscriptionDraftLineUpdateMutationResponseUserErrors = subscriptionDraftLineUpdateMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!subscriptionDraftLineUpdateMutationResponseUserErrors.isEmpty()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionDraftLineUpdateMutationResponseUserErrors.get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + subscriptionDraftLineUpdateMutationResponseUserErrors.get(0).getMessage());
                                continue;
                            }


                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

                            if (optionalDraftCommitResponse.hasErrors()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + optionalDraftCommitResponse.getErrors().get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + optionalDraftCommitResponse.getErrors().get(0).getMessage());
                                continue;
                            }

                            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + optionalDraftCommitResponseUserErrors.get(0).getMessage());
                                log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " couldn't be updated Successfully. Errors=" + optionalDraftCommitResponseUserErrors.get(0).getMessage());
                                continue;
                            }


                            result.add("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " updated Successfully. price=" + price);
                            log.error("subscriptionId=" + subscriptionContractDetails.getSubscriptionContractId() + " variantId=" + variantIdLong + " updated Successfully. price=" + price);
                        }
                    } catch (Exception ex) {

                    }
                }
            } catch (Exception ex) {

            }
        }

        log.info("priceInfoUpdateFinalLogs=" + result);
        return result;
    }

    @PostMapping(value = "/csv-bulk-update-line-price", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void csvBulkUpdateLinePrice(@RequestParam(value = "api_key") String apiKey,@RequestParam(value = "product-price-data", required = false) MultipartFile productPriceDataFile) throws Exception {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        CSVParser productPriceDataCSVParser = null;
        Reader productPriceDataReader = new BufferedReader(new InputStreamReader(productPriceDataFile.getInputStream()));
        productPriceDataCSVParser = new CSVParser(productPriceDataReader, csvFormat);

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        int recordNum = 0;

        for (CSVRecord subscriptionDataRecord : productPriceDataCSVParser) {
            try {

                recordNum++;

                log.info("Iterating record " + (recordNum + 1) + " shop=" + shop);

                String variantId = subscriptionDataRecord.get("Line variant ID");

                String price = subscriptionDataRecord.get("New Price");

                String contractId = null;

                if(subscriptionDataRecord.isSet("ID")) {
                   contractId = subscriptionDataRecord.get("ID");
                }

                List<Long> contractIdList = new ArrayList<>();

                if(!StringUtils.hasText(contractId)) {
                    List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId));
                    contractIdList = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());

                } else {
                    contractIdList.add(Long.parseLong(contractId));
                }

                if (StringUtils.isEmpty(variantId)) {
                    log.info("At record= {} no variant is null for shop= {}", (recordNum + 1), shop);
                    continue;
                }

                for(Long contract : contractIdList) {
                    log.info("Updating price for contract id= {}", contract);
                    try {
                        subscriptionContractDetailsService.updateLinePrice(shop, contract, Long.parseLong(variantId), Double.parseDouble(price), ActivityLogEventSource.MERCHANT_PORTAL_BULK_AUTOMATION);
                    } catch (Exception e) {
                        log.error("Some error occurred while running csv Bulk price update for contractId= {} and variantId= {} message: {}", contractId, variantId, e.getMessage());
                    }
                }
            } catch (Exception ex) {
                log.error("Some error occurred while running csv Bulk price update message: {}", ex.getMessage());
            }
        }
    }

    @GetMapping("/fix-empty-queue-issue")
    public void fixEmptyQueueIssue() throws Exception {

        List<Long> subscriptionContractIds = subscriptionContractDetailsRepository.findEmptyQueueIssue();

        log.info("fixEmptyQueueIssue=" + subscriptionContractIds);

        Map<String, ShopifyGraphqlClient> map = new HashMap<>();

        for (Long subscriptionContractId : subscriptionContractIds) {
            try {
                SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId).get();
                ShopifyGraphqlClient shopifyGraphqlClient = null;

                String shop = subscriptionContractDetails.getShop();

                if (map.containsKey(shop)) {
                    shopifyGraphqlClient = map.get(shop);
                } else {
                    ShopifyGraphqlClient shopifyGraphqlClient1 = commonUtils.prepareShopifyGraphqlClient(shop);
                    map.put(shop, shopifyGraphqlClient1);
                    shopifyGraphqlClient = shopifyGraphqlClient1;
                }

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                SubscriptionContractSubscriptionStatus status = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(d -> d.getSubscriptionContract().map(SubscriptionContractQuery.SubscriptionContract::getStatus)).orElse(null);

                SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                subscriptionDraftInputBuilder.status(status);
                SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();


                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(subscriptionContractDetails.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractUpdateMutation.Data>> subscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                if (subscriptionContractUpdateMutationResponse.hasErrors()) {
                    throw new BadRequestAlertException("", "", "");
                }

                List<SubscriptionContractUpdateMutation.UserError> subscriptionContractUpdateMutationResponseUserErrors = subscriptionContractUpdateMutationResponse.getData().map(d -> d.getSubscriptionContractUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!subscriptionContractUpdateMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException("", "", "");
                }

                Optional<String> optionalDraftId = subscriptionContractUpdateMutationResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId());

                SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(optionalDraftId.get(), subscriptionDraftInput);
                Response<Optional<SubscriptionDraftUpdateMutation.Data>> subscriptionDraftUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftUpdateMutation);

                if (!CollectionUtils.isEmpty(subscriptionDraftUpdateMutationResponse.getErrors())) {
                    throw new BadRequestAlertException("", "", "");
                }

                List<SubscriptionDraftUpdateMutation.UserError> subscriptionDraftUpdateMutationResponseUserErrors = subscriptionDraftUpdateMutationResponse.getData().map(d -> d.getSubscriptionDraftUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!subscriptionDraftUpdateMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException("", "", "");
                }

                // commit draft
                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> subscriptionDraftCommitMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);

                if (!CollectionUtils.isEmpty(subscriptionDraftCommitMutationResponse.getErrors())) {
                    throw new BadRequestAlertException("", "", "");
                }

                List<SubscriptionDraftCommitMutation.UserError> subscriptionDraftCommitMutationResponseUserErrors = subscriptionDraftCommitMutationResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                if (!subscriptionDraftCommitMutationResponseUserErrors.isEmpty()) {
                    throw new BadRequestAlertException("", "", "");
                }

                /*subscriptionContractDetails.setDisableFixEmptyQueue(true);
                subscriptionContractDetailsRepository.save(subscriptionContractDetails);*/
            } catch (Exception ex) {
                SubscriptionContractDetails subscriptionContractDetails = subscriptionContractDetailsRepository.findBySubscriptionContractId(subscriptionContractId).get();
                subscriptionContractDetails.setDisableFixEmptyQueue(true);
                subscriptionContractDetailsRepository.save(subscriptionContractDetails);
            }
        }
    }

    @GetMapping("/subscription-to-import-id-mapping")
    public Map<Long, String> subscriptionToImportIdMapping(@RequestParam("api_key") String apiKey, @RequestParam(value = "comaSeparatedImportedIds", required = false) String comaSeparatedImportedIds, @RequestParam(value = "comaSeparatedSubscriptionIds", required = false) String comaSeparatedSubscriptionIds) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        Set<Long> specificSubscriptionIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedSubscriptionIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());

        Set<String> specificImportedIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedImportedIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).collect(Collectors.toSet());

        Map<Long, String> map = subscriptionContractDetailsDTOList.stream().filter(s -> s.getImportedId() != null).filter(s -> specificSubscriptionIds.isEmpty() || specificSubscriptionIds.contains(s.getSubscriptionContractId())).filter(s -> specificImportedIds.isEmpty() || specificImportedIds.contains(s.getImportedId())).collect(Collectors.toMap(SubscriptionContractDetailsDTO::getSubscriptionContractId, SubscriptionContractDetailsDTO::getImportedId));

        return map;
    }

    /*@GetMapping("/remove-discount-from-subscription")
    public void removeDiscountFromSubscription() throws Exception {

        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(1117651127L).get();

        String graphSubscriptionContractId = subscriptionContractDetailsDTO.getGraphSubscriptionContractId();


        String shop = subscriptionContractDetailsDTO.getShop();
        SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();
        String accessToken = socialConnection.getAccessToken();

        ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

        String draftId = generateDraftId(graphSubscriptionContractId, shopifyGraphqlClient);

        String discountId = "gid://shopify/SubscriptionManualDiscount/f3ebfa6a-16c7-463f-b1a1-857503c8772f";
        SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(draftId, discountId);
        Response<Optional<SubscriptionDraftDiscountRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
            return;
        }

        List<SubscriptionDraftDiscountRemoveMutation.UserError> userErrors = Objects
            .requireNonNull(optionalMutationResponse.getData())
            .map(d -> d.getSubscriptionDraftDiscountRemove()
                .map(SubscriptionDraftDiscountRemoveMutation.SubscriptionDraftDiscountRemove::getUserErrors)
                .orElse(new ArrayList<>()))
            .orElse(new ArrayList<>());

        if (!CollectionUtils.isEmpty(userErrors)) {
            return;
        }

        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);
    }*/


    @GetMapping("/update-delivery-price")
    public void updateDeliveryPrice(@RequestParam("shop") String shop, @RequestParam("deliveryPrice") Double deliveryPrice) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        List<Long> contractIds = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop);

        for (Long contractId : contractIds) {
            try {
                subscriptionContractDetailsService.updateDeliveryPriceBySubscriptionContractId(shop, contractId, deliveryPrice, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
            } catch (Exception e) {
                log.error("An error occurred while bulk updating delivery price for shop: {}, Subscription Contract Id: {}, with message: {}", shop, contractId, e.getMessage());
            }
        }
    }

    @GetMapping("/update-override-delivery-price-flag")
    public void updateDeliveryPrice(@RequestParam("shop") String shop, @RequestParam("override-delivery-price") Boolean overrideDeliveryPriceFlag, @RequestParam("coma-separated-subscription-ids") String comaSeparatedSubscriptionIds) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        Set<Long> specificSubscriptionIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedSubscriptionIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        subscriptionContractDetailsDTOList = subscriptionContractDetailsDTOList.stream().filter(i -> specificSubscriptionIds.contains(i.getSubscriptionContractId())).collect(Collectors.toList());

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            subscriptionContractDetailsDTO.setAllowDeliveryPriceOverride(overrideDeliveryPriceFlag);
            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
        }


    }

    private static String cleanTextContent(String text) {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        return text.trim();
    }

    @Autowired
    private SubscribeItScriptUtils subscribeItScriptUtils;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor asyncExecutor;

    @GetMapping("/regenerate-scripts")
    public boolean runScriptUtils() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (int i = socialConnections.size() - 1; i >= 0; i--) {
            SocialConnection socialConnection = socialConnections.get(i);
            try {
                asyncExecutor.execute(() -> {
                    try {
                        String shop = socialConnection.getUserId();
                        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);
                    } catch (Exception e1) {

                    }
                });
            } catch (Exception e) {

            }
        }
        return true;
    }

    /*
    * Don't change this api. It is being used for theme update webhook
    * */
    @GetMapping("/regenerate-scripts-for-shop")
    public boolean runScriptUtilsForShop(@RequestParam(value = "api_key") String apiKey) {
        String shop = commonUtils.getShopByAPIKey(apiKey).get();
        ShopInfoDTO shopInfo = shopInfoService.findByShop(shop).get();
        subscribeItScriptUtils.createOrUpdateFileInCloud(shop);

        return true;
    }

    /*@GetMapping("/cancel-hidden-subscriptions")
    public boolean cancelHiddenSubscriptions() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (int i = socialConnections.size() - 1; i >= 0; i--) {

            log.info("executing cancel-hidden-subscriptions i=" + i);
            final int index = i;

            SocialConnection socialConnection = socialConnections.get(index);

            try {
                try {

                    String shop = socialConnection.getUserId();
                    String accessToken = socialConnection.getAccessToken();
                    ShopifyGraphqlClient shopifyGraphqlClient = new ShopifyGraphqlClient(shop, accessToken);

                    SubscriptionContractsBriefQuery subscriptionContractsQuery = null;
                    Response<Optional<SubscriptionContractsBriefQuery.Data>> subscriptionContractsQueryResponse = null;
                    SubscriptionContractsBriefQuery.PageInfo pageInfo = null;
                    Input<String> cursor = Input.fromNullable(null);

                    int counter = 0;
                    while (pageInfo == null || pageInfo.isHasNextPage()) {
                        counter++;
                        subscriptionContractsQuery = new SubscriptionContractsBriefQuery(cursor);
                        subscriptionContractsQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractsQuery);

                        Set<String> subscriptionContractIds = Objects.requireNonNull(subscriptionContractsQueryResponse
                            .getData())
                            .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(SubscriptionContractsBriefQuery.Edge::getNode)
                            .filter(n -> !n.getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED))
                            .map(SubscriptionContractsBriefQuery.Node::getId)
                            .collect(Collectors.toSet());

                        log.info("counter=" + counter + " executing cancel-hidden-subscriptions i=" + i + " shop=" + shop + " subscriptionContractIds.size=" + subscriptionContractIds.size());

                        Set<Long> subscriptionContractIdsLong = subscriptionContractIds.stream().map(s -> Long.parseLong(s.replace(SUBSCRIPTION_CONTRACT_ID_PREFIX, ""))).collect(Collectors.toSet());

                        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsList = subscriptionContractDetailsService.findByContractIdIn(subscriptionContractIdsLong);
                        Map<Long, SubscriptionContractDetailsDTO> subscriptionByContractId = subscriptionContractDetailsList.stream().collect(Collectors.toMap(SubscriptionContractDetailsDTO::getSubscriptionContractId, s -> s));

                        for (Long subscriptionContractId : subscriptionContractIdsLong) {
                            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionByContractId.get(subscriptionContractId);
                            if (subscriptionContractDetailsDTO == null) {
                                log.info("shop=" + shop + " subscriptionContractId=" + subscriptionContractId + " not present");

                                Input<SubscriptionContractSubscriptionStatus> cancelledStatus = Input.optional(SubscriptionContractSubscriptionStatus.CANCELLED);
                                boolean cancelled = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractStatus(shopifyGraphqlClient, shop, subscriptionContractId, cancelledStatus);

                            }
                        }

                        pageInfo = subscriptionContractsQueryResponse
                            .getData()
                            .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getPageInfo)
                            .orElse(null);

                        Optional<String> optionalCursor = subscriptionContractsQueryResponse
                            .getData()
                            .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(SubscriptionContractsBriefQuery.Edge::getCursor)
                            .reduce((first, second) -> second);

                        List<String> cursorList = subscriptionContractsQueryResponse.getData()
                            .map(SubscriptionContractsBriefQuery.Data::getSubscriptionContracts)
                            .map(SubscriptionContractsBriefQuery.SubscriptionContracts::getEdges)
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(SubscriptionContractsBriefQuery.Edge::getCursor)
                            .collect(Collectors.toList());

                        if (optionalCursor.isPresent()) {
                            cursor = Input.optional(optionalCursor.get());
                        }
                    }
                } catch (Exception e1) {
                    String a = "b";
                    log.info("cancel-hidden-subscriptions e1=" + ExceptionUtils.getStackTrace(e1));
                }
            } catch (Exception e) {
                String a = "b";
                log.info("cancel-hidden-subscriptions e=" + ExceptionUtils.getStackTrace(e));
            }


        }
        return true;
    }*/

    @GetMapping("/generate-customer-portal-html")
    public void generateCustomerPortalPageHtml() {
        subscribeItScriptUtils.createOrUpdateFileInCloud(SecurityUtils.getCurrentUserLogin().get(), true, null);
    }

/*    @GetMapping("/verify-custom-email-domain")
    public void updateCustomEmailDomainVerification() {
        List<ShopInfoDTO> shopInfos = shopInfoService.findAll();

        shopInfos.sort(Comparator.comparingLong(ShopInfoDTO::getId).reversed());

        for(ShopInfoDTO shopInfoDB: shopInfos) {
            try {
                asyncExecutor.execute(() -> {
                    try {
                        if (org.apache.commons.lang3.StringUtils.isNotBlank(shopInfoDB.getEmailCustomDomain())) {
                            HttpResponse<String> response = Unirest.get(mailgunProperties.getBaseUrl() + "domains/" + shopInfoDB.getEmailCustomDomain())
                                .basicAuth("api", mailgunProperties.getApiKey())
                                .asString();

                            MailgunDomainVM mailgunDomainVM = new ObjectMapper().readValue(response.getBody(), MailgunDomainVM.class);

                            boolean isCustomDomainValid = false;
                            if (!CollectionUtils.isEmpty(mailgunDomainVM.getSendingDnsRecords())) {
                                isCustomDomainValid = mailgunDomainVM.getSendingDnsRecords().stream().allMatch(sr -> "valid".equalsIgnoreCase(sr.getValid()));
                            }

                            if (shopInfoDB.isVerifiedEmailCustomDomain() == null || shopInfoDB.isVerifiedEmailCustomDomain().booleanValue() != isCustomDomainValid) {
                                // Update status change in database
                                shopInfoDB.setVerifiedEmailCustomDomain(isCustomDomainValid);
                                shopInfoService.save(shopInfoDB);
                            }
                        }
                    } catch (Exception e1) {

                    }
                });
            } catch (Exception e) {

            }
        }

    }*/

    @GetMapping("/clear-custom-email-domains")
    public void clearCustomEmailDomains() {
        try {
            HttpResponse<String> response = Unirest.get(mailgunProperties.getBaseUrl() + "domains")
                .basicAuth("api", mailgunProperties.getApiKey())
                .queryString("state", "unverified")
                .queryString("limit", 1000)
                .asString();

            DomainList domainList = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {}, response.getBody());

            List<Domain> domains = domainList.getDomains();

            for (Domain domain : domains) {
                Date createdDate = DateUtils.parseDate(domain.getCreatedAt(), "EEE, dd MMM yyyy HH:mm:ss z");

                if(DateUtils.addMonths(createdDate, 1).before(new Date())) {

                    HttpResponse<String> deleteResponse = Unirest.delete(mailgunProperties.getBaseUrl() + "domains/" + domain.getName())
                        .basicAuth("api", mailgunProperties.getApiKey())
                        .asString();

                    if (deleteResponse.isSuccess()) {
                        log.info("Mailgun domain deleted. domain={}, deleteResponse={}", domain.getName(), deleteResponse);
                    } else {
                        log.error("Mailgun error while delteting domain. domain={}, deleteResponse={}", domain.getName(), deleteResponse.getBody());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @GetMapping("/add-new-email-template")
    @Transactional
    public boolean addNewEmailTemplate() throws IOException {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            String shop = socialConnection.getUserId();
//            String shop = "test-subscription-v131.myshopify.com";
            try {
                emailTemplateSettingRepository.deleteByShopAndEmailSettingTypeIn(shop, Arrays.asList(EmailSettingType.SUBSCRIPTION_PRODUCT_ADDED, EmailSettingType.SUBSCRIPTION_PRODUCT_REMOVED));

                Shop shopDetails = commonUtils.prepareShopifyResClient(shop).getShopInfo().getShop();
                EmailTemplateSetting subscriptionProductAddedEmailSetting = emailTemplateSettingService.buildSubscriptionProductAddedEmailSetting(shop, shopDetails);
                EmailTemplateSetting subscriptionProductRemovedEmailSetting = emailTemplateSettingService.buildSubscriptionProductRemovedEmailSetting(shop, shopDetails);

                emailTemplateSettingRepository.save(subscriptionProductAddedEmailSetting);
                emailTemplateSettingRepository.save(subscriptionProductRemovedEmailSetting);
            } catch (Exception e) {
                log.error("Something went wrong while inserting new email template for: " + shop);
            }
        }
        return true;
    }

    /*@GetMapping("/generate-api-key")
    public boolean generateApiKey() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            try {
                Optional<ShopInfoDTO> optionalShopInfo = shopInfoService.findByShop(socialConnection.getUserId());
                if (optionalShopInfo.isPresent()) {

                    ShopInfoDTO shopInfo = optionalShopInfo.get();
                    if (com.amazonaws.util.StringUtils.isNullOrEmpty(shopInfo.getApiKey())) {
                        shopInfo.setApiKey(CommonUtils.generateRandomUid());
                        shopInfoService.save(shopInfo);
                    }
                } else {

                }
            } catch (Exception e) {

            }
        }
        return true;
    }*/

    /*@GetMapping("/growave-testing")
    public boolean growaveTesting() {

        AuthentificationApi authentificationApi = new AuthentificationApi();
        InlineResponse200 inlineResponse200 = authentificationApi.accessToken("06fb296abdd902e5f13d23ca62776772", "a2b2b84e885561771c03c6810c578416", "client_credentials", "read_user write_user read_wishlist write_wishlist read_review write_review read_gallery read_reward write_reward app_install");
        String accessToken = inlineResponse200.getAccessToken();
        log.info("accessToken=" + accessToken);

        *//*HttpResponse<String> response = Unirest.get("https://growave.io/api/users/search?field=email&value=fdsdd@dsd.com")
            .header("Authorization", "Bearer 78e12bc533fadf8b0235abdc848ddb96419a40a7")
            .asString();*//*


        HttpResponse<String> response = Unirest.post("https://growave.io/api/reward/earn")
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json")
            .body("{\n\"email\": \"john.doe@example.com\",\n\"rule_type\": \"custom_action\",\n\"points\": 50\n}")
            .asString();


        return true;
    }*/

    @GetMapping("/update-next-billing-date")
    public void updateNextBillingDate(@RequestParam("shop") String shop, @RequestParam("comaSeparatedSubscriptionIds") String comaSeparatedSubscriptionIds, @RequestParam("nextBillingDate") ZonedDateTime nextBillingDate, HttpServletRequest request) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        boolean isExternal = commonUtils.isExternal();

        Set<Long> specificSubscriptionIds = Arrays.stream(Objects.requireNonNull(Optional.ofNullable(comaSeparatedSubscriptionIds).orElse("").split(","))).map(String::trim).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Long::parseLong).collect(Collectors.toSet());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long contractId : specificSubscriptionIds) {
            try {
                subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shop, nextBillingDate, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
            } catch (Exception ex) {

            }
        }
    }


    @GetMapping("/update-next-billing-date-time")
    public void updateNextBillingDate(@RequestParam("shop") String shop, @RequestParam("hour") Integer hour, @RequestParam("minute") Integer minute, @RequestParam("zonedOffsetHours") int zonedOffSetHours, HttpServletRequest request) throws Exception {

        shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOS = subscriptionContractDetailsService.findByShop(shop);

        subscriptionContractDetailsDTOS = subscriptionContractDetailsDTOS.stream().sorted(Comparator.comparing(SubscriptionContractDetailsDTO::getNextBillingDate)).collect(Collectors.toList());

        for (int i = 0; i < subscriptionContractDetailsDTOS.size(); i++) {
            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsDTOS.get(i);
            try {
                subscriptionContractDetailsService.updateNextBillingDateTime(shop, hour, minute, zonedOffSetHours, subscriptionContractDetailsDTO, shopifyGraphqlClient);
            } catch (Exception ex) {
                log.error("An error occurred while updating next order date during bulk automations. shop=" + shop + " ex=" + ExceptionUtils.getStackTrace(ex), ex);
            }
        }
    }

    @Autowired
    private ShopInfoUtils shopInfoUtils;


    /*@GetMapping("/retry-payment-exceptions")
    public void retryPaymentExceptions() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        Path fileName = Path.of("/Users/hemantpurswani/Downloads/log-events-viewer-result-v3.csv");
        String fileContent = Files.readString(fileName);


        final String regex = "occurred while processing billing attempt. BillingAttemptId=(\\d*) ex=";
        final String string = fileContent;

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);

        while (matcher.find()) {
            //System.out.println("Full match: " + matcher.group(0));

            for (int i = 1; i <= matcher.groupCount(); i++) {
                String billingAttemptIdString = matcher.group(i);
                long billingAttemptId = Long.parseLong(billingAttemptIdString);
                SubscriptionBillingAttemptDTO subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findById(billingAttemptId).get();

                if (subscriptionBillingAttemptDTO.getStatus().equals(BillingAttemptStatus.CONTRACT_CANCELLED)) {
                    System.out.println(subscriptionBillingAttemptDTO.getId() + " shop=" + subscriptionBillingAttemptDTO.getShop() + " contractId=" + subscriptionBillingAttemptDTO.getContractId());
                    subscriptionBillingAttemptDTO.setStatus(BillingAttemptStatus.QUEUED);
                    subscriptionBillingAttemptService.save(subscriptionBillingAttemptDTO);
                }
            }
        }
    }*/

    public static String urlReader(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (InputStream in = url.openStream()) {
            byte[] bytes = in.readAllBytes();
            return new String(bytes);
        }
    }

    @Autowired
    private MailgunService mailgunService;

    @PostMapping("/recharge-to-appstle-one-off")
    public String rechargeToAppstleOneOff(@RequestBody Map<String, String> rechargeUrls, @RequestParam(required = false) String apiKey) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        String shop = apiKey != null ? commonUtils.getShopByAPIKey(apiKey).get() : SecurityUtils.getCurrentUserLogin().get();

        if (!StringUtils.hasText(shop)) {
            throw new BadRequestAlertException("API key is required", "", "");
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String chargesQueuedContent = urlReader(rechargeUrls.get("CHARGES_QUEUED_URL"));
        StringReader chargesQueuedReader = new StringReader(chargesQueuedContent);
        CSVParser chargesQueuedDataCSVParser = new CSVParser(chargesQueuedReader, csvFormat);

        Map<Long, List<CSVRecord>> chargesQueuedByChargeId = chargesQueuedDataCSVParser.getRecords().stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.get("charge_id"))));

        try {
            for (Map.Entry<Long, List<CSVRecord>> entry : chargesQueuedByChargeId.entrySet()) {
                int i = 0;
                for (CSVRecord chargeQueueRow : entry.getValue()) {
                    if (chargeQueueRow.get("line_item_purchase_item_type") == null || !chargeQueueRow.get("line_item_purchase_item_type").toString().equals("ONETIME")) {
                        continue;
                    }
                    i++;
                    Long chargeId = entry.getKey();

                    Optional<SubscriptionContractDetails> importedSubscriptionOptional = subscriptionContractDetailsRepository.findByShopAndImportedId(shop, chargeId.toString());

                    if (importedSubscriptionOptional.isPresent()) {
                        SubscriptionContractDetails subscriptionContractDetails = importedSubscriptionOptional.get();
                        String variantId = chargeQueueRow.get("line_item_external_variant_id");
                        String lineItemPrice = chargeQueueRow.get("line_item_price");
                        String lineItemQuantity = chargeQueueRow.get("line_item_quantity");

                        ProductVariantQuery productVariantQuery = new ProductVariantQuery(PRODUCT_VARIANT_ID_PREFIX + variantId);
                        Response<Optional<ProductVariantQuery.Data>> productVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                        String handle = productVariantQueryResponse.getData().flatMap(d -> d.getProductVariant().map(ProductVariantQuery.ProductVariant::getProduct).map(ProductVariantQuery.Product::getHandle)).orElse(null);

                        List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByContractIdAndStatus(subscriptionContractDetails.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);

                        Optional<SubscriptionBillingAttempt> subscriptionBillingAttemptOptional = subscriptionBillingAttempts.stream().sorted(Comparator.comparing(SubscriptionBillingAttempt::getBillingDate)).findFirst();

                        if (subscriptionBillingAttemptOptional.isPresent()) {
                            SubscriptionBillingAttempt subscriptionBillingAttempt = subscriptionBillingAttemptOptional.get();
                            log.info("contractId=" + subscriptionContractDetails.getSubscriptionContractId() + " handle=" + handle + " price=" + lineItemPrice + " quantity=" + lineItemQuantity + " billingAttemptDate=" + subscriptionBillingAttempt.getBillingDate());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    @Autowired
    private SubscriptionContractOneOffService subscriptionContractOneOffService;

    private void subscriptionContractSaveOneOff(String shop, Long contractId, Long billingAttemptId, Long variantId, String variantHandle) {
        List<SubscriptionContractOneOffDTO> subscriptionContractsList = subscriptionContractOneOffService.findByShopAndSubscriptionContractId(shop, contractId);

        Optional<SubscriptionContractOneOffDTO> subscriptionContractOneOffDTOOptional = subscriptionContractsList.stream().filter(sc -> sc.getBillingAttemptId().equals(billingAttemptId) && sc.getVariantId().equals(variantId)).findFirst();

        if (subscriptionContractOneOffDTOOptional.isPresent()) {
            return;
        }

        SubscriptionContractOneOffDTO subscriptionContractOneOffDTO = new SubscriptionContractOneOffDTO();
        subscriptionContractOneOffDTO.setShop(shop);
        subscriptionContractOneOffDTO.setBillingAttemptId(billingAttemptId);
        subscriptionContractOneOffDTO.setSubscriptionContractId(contractId);
        subscriptionContractOneOffDTO.setVariantId(variantId);
        subscriptionContractOneOffDTO.setVariantHandle(variantHandle);

        subscriptionContractOneOffService.save(subscriptionContractOneOffDTO);
    }

    @PostMapping("/recharge-to-appstle")
    public String rechargeToAppstle(@RequestParam(value = "api_key") String apiKey, @RequestBody Map<String, String> rechargeUrls) throws IOException {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();
        String allSubscriptionsContent = urlReader(rechargeUrls.get("ALL_SUBSCRIPTIONS_URL"));
        StringReader allSubscriptionsReader = new StringReader(allSubscriptionsContent);
        CSVParser allSubscriptionsDataCSVParser = new CSVParser(allSubscriptionsReader, csvFormat);


        Map<Long, List<CSVRecord>> csvRecordsBySubscriptionId = allSubscriptionsDataCSVParser.getRecords().stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.get("subscription_id"))));


        String customerShippingAddressContent = urlReader(rechargeUrls.get("SHIPPING_ADDRESS_URL"));
        StringReader customerShippingAddressReader = new StringReader(customerShippingAddressContent);
        CSVParser customerShippingAddressDataCSVParser = new CSVParser(customerShippingAddressReader, csvFormat);


        Map<Long, List<CSVRecord>> addressRecordsByAddressId = customerShippingAddressDataCSVParser.getRecords().stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.get("address_id"))));


        String chargesQueuedContent = urlReader(rechargeUrls.get("CHARGES_QUEUED_URL"));
        StringReader chargesQueuedReader = new StringReader(chargesQueuedContent);
        CSVParser chargesQueuedDataCSVParser = new CSVParser(chargesQueuedReader, csvFormat);

        Map<Long, List<CSVRecord>> chargesQueuedBySubscriptionId = new HashMap<>();

        Map<Long, List<CSVRecord>> chargesQueuedByChargeId = chargesQueuedDataCSVParser.getRecords().stream().collect(Collectors.groupingBy(s -> Long.parseLong(s.get("charge_id"))));

        List<SubscriptionContractDetails> subscriptionContractDetailsList = new ArrayList<>();

        if (StringUtils.hasText(shop)) {
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
        }

        Set<String> importedIds = subscriptionContractDetailsList.stream().map(SubscriptionContractDetails::getImportedId).filter(Objects::nonNull).collect(Collectors.toSet());

        Map<String, List<SubscriptionContractDetails>> importedContractionsByEmail = subscriptionContractDetailsList.stream().filter(s -> s.getCustomerEmail() != null).collect(Collectors.groupingBy(SubscriptionContractDetails::getCustomerEmail));

        printActiveData(csvRecordsBySubscriptionId, addressRecordsByAddressId, chargesQueuedBySubscriptionId, chargesQueuedByChargeId, importedIds, importedContractionsByEmail);

        printCancelledData(csvRecordsBySubscriptionId, addressRecordsByAddressId);

        return null;
    }

    private void printCancelledData(Map<Long, List<CSVRecord>> csvRecordsBySubscriptionId, Map<Long, List<CSVRecord>> addressRecordsByAddressId) {


        File tempFile = null;
        try {
            String[] headers = {"ID", "Status", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID", "Customer Email"};
            tempFile = File.createTempFile("subscription-data-v1", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT.withAutoFlush(true).withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            List<CSVRecord> cancelledRecords = csvRecordsBySubscriptionId.values().stream().flatMap(Collection::stream).filter(s -> s.get("status").equals("CANCELLED")).collect(Collectors.toList());

            for (CSVRecord cancelledRecord : cancelledRecords) {

                String id = UUID.randomUUID().toString();

                String variantId = cancelledRecord.get("external_variant_id");
                String lineItemPrice = cancelledRecord.get("price");
                String lineItemQuantity = cancelledRecord.get("quantity");


                long addressId = Long.parseLong(cancelledRecord.get("address_id"));

                if (!addressRecordsByAddressId.containsKey(addressId)) {
                    continue;
                }

                String billingIntervalType = null;
                String billingIntervalCount = null;
                String deliveryIntervalType = null;
                String deliveryIntervalCount = null;


                deliveryIntervalCount = cancelledRecord.get("order_interval_frequency");
                billingIntervalCount = cancelledRecord.get("charge_interval_frequency");
                billingIntervalType = cancelledRecord.get("order_interval_unit");
                deliveryIntervalType = billingIntervalType;

                String nextOrderDate = "2022-02-01 00:00:00";


                String email = cancelledRecord.get("customer_email");

                CSVRecord addressRecord = addressRecordsByAddressId.get(addressId).get(0);
                String shippingFirstName = addressRecord.get("shipping_first_name");
                String shippingLastName = addressRecord.get("shipping_last_name");
                String shippingAddress1 = addressRecord.get("address_1");
                String shippingAddress2 = addressRecord.get("address_2");
                String province = addressRecord.get("province");
                String city = addressRecord.get("city");
                String zip = addressRecord.get("zip");
                String country = addressRecord.get("country");
                String phone = addressRecord.get("phone");

                Double deliveryFee = 0.0;

                try {
                    JSONArray jsonArray = new JSONArray(addressRecord.get("shipping_lines_override"));
                    if (!jsonArray.isEmpty()) {
                        for (Object o : jsonArray) {
                            if (o instanceof JSONObject) {
                                JSONObject o1 = (JSONObject) o;
                                if (o1.has("price")) {
                                    deliveryFee = Double.parseDouble(o1.getString("price"));
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    String a = "B";
                }


                csvPrinter.printRecord(id, "cancelled", shippingFirstName, shippingLastName, shippingAddress1, shippingAddress2, province, city, zip, country, phone, deliveryFee, nextOrderDate, billingIntervalType, billingIntervalCount, deliveryIntervalType, deliveryIntervalCount, lineItemQuantity, lineItemPrice, variantId, email);
            }


            csvPrinter.close(true);
        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Subscription Contract Canceled Exported", "Check attached csv file for your all subscription list details.", "subscription-support@appstle.com", "test-store-sumeet@appstle.com", "support@appstle.com");
        String a = "B";
    }

    private void printActiveData(Map<Long, List<CSVRecord>> csvRecordsBySubscriptionId, Map<Long, List<CSVRecord>> addressRecordsByAddressId, Map<Long, List<CSVRecord>> chargesQueuedBySubscriptionId, Map<Long, List<CSVRecord>> chargesQueuedByChargeId, Set<String> importedIds, Map<String, List<SubscriptionContractDetails>> importedContractionsByEmail) {
        File tempFile = null;
        try {
            String[] headers = {"ID", "Status", "Delivery first name", "Delivery last name", "Delivery address 1", "Delivery address 2", "Delivery province code", "Delivery city", "Delivery zip", "Delivery country code", "Delivery phone", "Shipping Price", "Next order date", "Billing interval type", "Billing interval count", "Delivery interval type", "Delivery interval count", "Variant quantity", "Variant price", "Variant ID", "Customer Email"};
            tempFile = File.createTempFile("subscription-data-v1", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT.withAutoFlush(true).withHeader(headers);

            CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat);

            for (Map.Entry<Long, List<CSVRecord>> entry : chargesQueuedByChargeId.entrySet()) {
                try {
                    int i = 0;
                    boolean firstRowSkipped = false;
                    for (CSVRecord chargeQueueRow : entry.getValue()) {
                        try {
                            if (chargeQueueRow.get("line_item_purchase_item_type") != null && chargeQueueRow.get("line_item_purchase_item_type").toString().equals("ONETIME")) {
                                continue;
                            }

                            String email = chargeQueueRow.get("email");
                            Long chargeId = entry.getKey();

                            if (importedIds.contains(chargeId.toString())) {
                                firstRowSkipped = true;
                                continue;
                            }

                            if (importedContractionsByEmail.containsKey(email)) {
                                firstRowSkipped = true;
                                continue;
                            }

                            if (firstRowSkipped) {
                                continue;
                            }

                            i++;

                            String variantId = chargeQueueRow.get("line_item_external_variant_id");
                            String lineItemPrice = chargeQueueRow.get("line_item_price");
                            String lineItemQuantity = chargeQueueRow.get("line_item_quantity");

                            if (i == 1) {

                                long addressId = Long.parseLong(chargeQueueRow.get("address_id"));

                                if (!addressRecordsByAddressId.containsKey(addressId)) {
                                    continue;
                                }

                                String lineItemProperties = chargeQueueRow.get("line_item_properties");
                                JSONArray jsonArray = new JSONArray(lineItemProperties);

                                String billingIntervalType = null;
                                String billingIntervalCount = null;
                                String deliveryIntervalType = null;
                                String deliveryIntervalCount = null;

                                long subscriptionId = Long.parseLong(chargeQueueRow.get("line_item_subscription_id"));
                                List<CSVRecord> subscriptionRecords = csvRecordsBySubscriptionId.get(subscriptionId);

                                if (!CollectionUtils.isEmpty(subscriptionRecords)) {
                                    CSVRecord subscriptionRecord = subscriptionRecords.get(0);
                                    deliveryIntervalCount = subscriptionRecord.get("order_interval_frequency");
                                    billingIntervalCount = subscriptionRecord.get("charge_interval_frequency");
                                    billingIntervalType = subscriptionRecord.get("order_interval_unit");
                                    deliveryIntervalType = billingIntervalType;
                                }

                                if (billingIntervalType == null) {
                                    for (Object o : jsonArray) {
                                        if (o instanceof JSONObject) {
                                            JSONObject o1 = (JSONObject) o;
                                            if (o1.has("name")) {
                                                String name = o1.getString("name");
                                                if (name.equals("charge_interval_frequency")) {
                                                    billingIntervalCount = String.valueOf(o1.getInt("value"));
                                                } else if (name.equals("shipping_interval_frequency")) {
                                                    deliveryIntervalCount = String.valueOf(o1.getInt("value"));
                                                } else if (name.equals("shipping_interval_unit_type")) {
                                                    billingIntervalType = o1.getString("value");
                                                    deliveryIntervalType = billingIntervalType;
                                                }
                                            }
                                        }
                                    }
                                }

                                String nextOrderDate = chargeQueueRow.get("scheduled_at");


                                CSVRecord addressRecord = addressRecordsByAddressId.get(addressId).get(0);
                                String shippingFirstName = addressRecord.get("shipping_first_name");
                                String shippingLastName = addressRecord.get("shipping_last_name");
                                String shippingAddress1 = addressRecord.get("address_1");
                                String shippingAddress2 = addressRecord.get("address_2");
                                String province = addressRecord.get("province");
                                String city = addressRecord.get("city");
                                String zip = addressRecord.get("zip");
                                String country = addressRecord.get("country");
                                String phone = addressRecord.get("phone");

                                csvPrinter.printRecord(chargeId, "active", shippingFirstName, shippingLastName, shippingAddress1, shippingAddress2, province, city, zip, country, phone, chargeQueueRow.get("total_shipping"), nextOrderDate, billingIntervalType, billingIntervalCount, deliveryIntervalType, deliveryIntervalCount, lineItemQuantity, lineItemPrice, variantId, email);
                            } else {
                                csvPrinter.printRecord(chargeId, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, lineItemQuantity, lineItemPrice, variantId, null);
                            }
                        } catch (Exception ex2) {
                            String b = "c";
                        }
                    }
                } catch (Exception ex1) {
                    String a = "b";
                }
            }

            csvPrinter.close(true);
        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Subscription Contract Exported", "Check attached csv file for your all subscription list details.", "subscription-support@appstle.com", "test-store-sumeet@appstle.com", "support@appstle.com");
    }

    @PostMapping("/import-csv-attributes")
    public void importCSVAttributes(@RequestParam(value = "api_key") String apiKey, @RequestBody String text) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        log.info("came here " + "subscriptionContractDetailsDTOList=" + subscriptionContractDetailsDTOList.size());

        Set<Long> subscriptionContractIds = subscriptionContractDetailsDTOList.stream().filter(s -> s.getSubscriptionContractId() != null).map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toSet());


        List<String> errorList = new ArrayList<>();


        try {
            Reader subscriptionDataReader = new BufferedReader(new StringReader(text));
            CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);


            CSVParser customerDataCSVParser = null;


            int recordNum = 0;

            for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {

                if (recordNum % 10 == 0) {
                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
                    if (BooleanUtils.isTrue(shopInfoDTO.isStopImportProcess())) {
                        log.info("Stopping import process as stopImportProcess flag is On.");
                        break;
                    }
                }

                recordNum++;

                log.info("Iterating record " + (recordNum + 1) + " shop=" + shop);


                String id = subscriptionDataRecord.get("ID");

                if (StringUtils.isEmpty(id)) {
                    continue;
                }

                try {

                    long contractId = Long.parseLong(id);
                    if (!subscriptionContractIds.contains(contractId)) {
                        continue;
                    }

                    String productAttributes = subscriptionDataRecord.get("Product Attribute");

                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                    Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                    List<SubscriptionContractQuery.Node> subscriptionLineItems = requireNonNull(subscriptionContractQueryResponse.getData())
                        .map(d -> d.getSubscriptionContract()
                            .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                            .map(SubscriptionContractQuery.Lines::getEdges)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>()).stream()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .collect(Collectors.toList());

                    String variantIdString = subscriptionDataRecord.get("Variant ID");

                    Optional<SubscriptionContractQuery.Node> lineItemOptional = subscriptionLineItems.stream().filter(f -> f.getVariantId().isPresent() && f.getVariantId().get().equals(PRODUCT_VARIANT_ID_PREFIX + variantIdString)).findFirst();

                    if (lineItemOptional.isEmpty()) {
                        continue;
                    }

                    List<AttributeInfo> attributeInfoList = new ArrayList<>();


                    JSONObject productAttributesJson = new JSONObject(productAttributes);

                    for (String attributeKey : productAttributesJson.keySet()) {
                        AttributeInfo attributeInfo = new AttributeInfo();

                        try {

                            if (productAttributesJson.has(attributeKey)) {
                                if (productAttributesJson.isNull(attributeKey)) {
                                    continue;
                                } else {
                                    attributeInfo.setKey(attributeKey);
                                    attributeInfo.setValue(productAttributesJson.getString(attributeKey));
                                    attributeInfoList.add(attributeInfo);
                                }
                            }
                        } catch (Exception ex) {
                            String a = "b";
                        }
                    }


                    subscriptionContractDetailsResource.subscriptionContractUpdateLineItemAttributes(contractId, shop, lineItemOptional.get().getId(), attributeInfoList);

                    String a = "b";


                } catch (Exception ex) {
                    log.info("ex=" + ex.toString() + " ID=" + id);
                    errorList.add("ex=" + ex.toString() + " ID=" + id);
                }
            }

        } catch (Exception ex) {

        }
    }

    @PostMapping("/import-order-note-attributes")
    public void importOrderNoteAttributes(@RequestParam(value = "api_key") String apiKey, @RequestBody String orderNotes) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        CSVParser subscriptionDataCSVParser = null;
        Reader subscriptionDataReader = new BufferedReader(new StringReader(orderNotes));
        subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        Set<Long> subscriptionContractIds = subscriptionContractDetailsDTOList.stream().filter(s -> s.getSubscriptionContractId() != null).map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toSet());

        List<String> errorList = new ArrayList<>();

        try {

            int recordNum = 0;

            for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {
                SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                if (recordNum % 10 == 0) {
                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
                    if (BooleanUtils.isTrue(shopInfoDTO.isStopImportProcess())) {
                        log.info("Stopping import process as stopImportProcess flag is On.");
                        break;
                    }
                }

                recordNum++;

                log.info("Iterating record " + (recordNum + 1) + " shop=" + shop);

                String id = subscriptionDataRecord.get("ID");

                if (StringUtils.isEmpty(id)) {
                    continue;
                }

                try {

                    long contractId = Long.parseLong(id);
                    if (!subscriptionContractIds.contains(contractId)) {
                        continue;
                    }

                   String orderNoteAttributes = subscriptionDataRecord.get("Order Note Attributes");

                    List<OrderNoteAttribute> orderNoteAttributesList = OBJECT_MAPPER.readValue(orderNoteAttributes,new TypeReference<List<OrderNoteAttribute>>() {});

                    List<AttributeInput> customAttributesList = new ArrayList<>();


                    for (OrderNoteAttribute orderNoteAttribute : orderNoteAttributesList) {
                        customAttributesList.add(AttributeInput.builder().key(orderNoteAttribute.getKey()).value(orderNoteAttribute.getValue()).build());
                    }

                    OrderNoteAttributesWrapper orderNoteAttributesWrapper = new OrderNoteAttributesWrapper();
                    orderNoteAttributesWrapper.setOrderNoteAttributesList(orderNoteAttributesList);

                    Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsOptional = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);
                    if (subscriptionContractDetailsOptional.isPresent()) {
                        SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsOptional.get();
                        subscriptionContractDetailsDTO.setOrderNoteAttributes(OBJECT_MAPPER.writeValueAsString(orderNoteAttributesWrapper));
                        subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
                    }

                    subscriptionDraftInputBuilder.customAttributes(customAttributesList);
                    SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();
                    shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);
                } catch (Exception ex) {
                    log.info("ex=" + ex.toString() + " ID=" + id);
                    errorList.add("ex=" + ex.toString() + " ID=" + id);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @PostMapping("/import-subscription-contract")
    public ResponseEntity<Void> importSubscriptionContract(@Valid @RequestBody ImportSubscriptionContractDTO importSubscriptionContractDTO) {
        String shop = SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);

        CurrencyCode currencyCode = CurrencyCode.valueOf(shopInfo.get().getCurrency());

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        Set<String> importedIds = subscriptionContractDetailsDTOList.stream().filter(s -> s.getImportedId() != null).map(SubscriptionContractDetailsDTO::getImportedId).collect(Collectors.toSet());
        Set<String> currentIterationImportedIds = new HashSet<>();
        List<String> errorList = new ArrayList<>();
        List<String> processedIds = new ArrayList<>();

        try {
            ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
            if (BooleanUtils.isTrue(shopInfoDTO.isStopImportProcess())) {
                log.info("Stopping import process as stopImportProcess flag is On.");
            }

            try {
                if (!importedIds.contains(importSubscriptionContractDTO.getId())) {
                    String status = importSubscriptionContractDTO.getStatus();

                    String variantId = importSubscriptionContractDTO.getLineZeroVariantID();

                    String productVariantId = ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId;

                    ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                    Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                    if (Objects.requireNonNull(productVariantResponse.getData()).isEmpty() || productVariantResponse.getData().get().getProductVariant().isEmpty()) {
                        errorList.add("Invalid VariantId=" + variantId + " ID=" + importSubscriptionContractDTO.getId());
                        log.info(errorList.get(errorList.size() - 1));
                        throw new BadRequestAlertException("Invalid VariantId=" + variantId + " ID=" + importSubscriptionContractDTO.getId(), ENTITY_NAME, "invalidVarientIdForSubscriptionContractImport");
                    }

                    if (currentIterationImportedIds.contains(importSubscriptionContractDTO.getId())) {
                        if (org.apache.commons.lang3.StringUtils.isBlank(variantId)) {
                            errorList.add("Id=" + importSubscriptionContractDTO.getId() + " already processed but contract not update due to product variant id is empty");
                        }

                        addVariantToContractForImportSubscriptionContract(importSubscriptionContractDTO);
                        throw new BadRequestAlertException("Subscription contract is exist for import id = " + importSubscriptionContractDTO.getId(), ENTITY_NAME, "subscriptionContractIsExist");
                    }

                    Long customerId = null;
                    List<String> customerCreatedStripePaymentIds = new ArrayList<>();
                    if (importSubscriptionContractDTO.getImportType().equalsIgnoreCase("stripe")) {

                        String email = importSubscriptionContractDTO.getCustomerEmail();
                        email = email.toLowerCase();

                        List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                        if (importSubscriptionContractDTO.getCustomerID() != null && importSubscriptionContractDTO.getCustomerID().trim() != null) {
                            customerId = Long.parseLong(importSubscriptionContractDTO.getCustomerID());
                        }

                        if (customerId == null) {
                            if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                            } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                errorList.add("More than one customer found for: " + email);
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("More than one customer found for: " + email, ENTITY_NAME, "moreThanOneCustomerFound");
                            } else {
                                customerId = createCustomerId(shopifyGraphqlClient, email);
                                if (customerId == null) {
                                    errorList.add("Error in creating customer id with emil: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    throw new BadRequestAlertException("Error in creating customer id with emil: " + email, ENTITY_NAME, "errorInCustomerCreating");
                                }
                            }
                        }

                        if (associateShopifyCustomerWithStripeCustomerForImportedSubscriptionsContract(shopifyGraphqlClient, customerId, importSubscriptionContractDTO, customerCreatedStripePaymentIds)) {
                            errorList.add("Error in associating shopify customer to stripe customer with customer id: " + customerId);
                            log.info(errorList.get(errorList.size() - 1));
                            throw new BadRequestAlertException("Error in associating shopify customer to stripe customer with customer id: " + customerId, ENTITY_NAME, "errorInAssociatingShopifyCustomerToStripeCustomer");

                        }
                    } else if (importSubscriptionContractDTO.getImportType().equalsIgnoreCase("paypal")) {
                        String email = importSubscriptionContractDTO.getCustomerEmail();

                        List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                        if (importSubscriptionContractDTO.getCustomerID() != null && importSubscriptionContractDTO.getCustomerID().trim() != null) {
                            customerId = Long.parseLong(importSubscriptionContractDTO.getCustomerID());
                        }

                        if (customerId == null) {

                            if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                            } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                errorList.add("More than one customer found for: " + email);
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("More than one customer found for: " + email, ENTITY_NAME, "moreThanOneCustomerFound");
                            } else {
                                customerId = createCustomerId(shopifyGraphqlClient, email);
                                if (customerId == null) {
                                    errorList.add("Error in creating customer id with email: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    throw new BadRequestAlertException("Error in creating customer id with emil: " + email, ENTITY_NAME, "errorInCustomerCreating");
                                }
                            }
                        }
                        if (associateShopifyCustomerWithPayPalCustomerForImportedSubscriptionsContract(shopifyGraphqlClient, customerId, importSubscriptionContractDTO)) {
                            errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId);
                            log.info(errorList.get(errorList.size() - 1));
                            throw new BadRequestAlertException("Error in associating shopify customer to paypal customer with customer id: " + customerId, ENTITY_NAME, "errorInAssociatingShopifyCustomerToPaypalCustomer");
                        }
                    } else if (importSubscriptionContractDTO.getImportType().equalsIgnoreCase("authorize_net")) {
                        String email = importSubscriptionContractDTO.getCustomerEmail();

                        List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                        if (importSubscriptionContractDTO.getCustomerID() != null && importSubscriptionContractDTO.getCustomerID().trim() != null) {
                            customerId = Long.parseLong(importSubscriptionContractDTO.getCustomerID());
                        }

                        if (customerId == null) {

                            if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                            } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                errorList.add("More than one customer found for: " + email);
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("More than one customer found for: " + email, ENTITY_NAME, "moreThanOneCustomerFound");
                            } else {
                                customerId = createCustomerId(shopifyGraphqlClient, email);
                                if (customerId == null) {
                                    errorList.add("Error in creating customer id with emil: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    throw new BadRequestAlertException("Error in creating customer id with emil: " + email, ENTITY_NAME, "errorInCustomerCreating");
                                }
                            }
                        }

                        if (associateShopifyCustomerWithAuthorizeNetCustomerForImportedSubscriptionsContract(shopifyGraphqlClient, customerId, importSubscriptionContractDTO, customerCreatedStripePaymentIds)) {
                            errorList.add("Error in associating shopify customer to stripe customer with customer id: " + customerId);
                            log.info(errorList.get(errorList.size() - 1));
                            throw new BadRequestAlertException("Error in associating shopify customer to stripe customer with customer id: " + customerId, ENTITY_NAME, "errorInAssociatingShopifyCustomerToStripeCustomer");
                        }
                    } else {
                        customerId = Long.parseLong(importSubscriptionContractDTO.getCustomerID());
                    }

                    List<String> paymentMethodIds = getPaymentMethodListForImportSubscriptionContract(customerId, shopifyGraphqlClient, importSubscriptionContractDTO);

                    if (paymentMethodIds.size() != 1) {
                        if (paymentMethodIds.size() > 1) {
                            if (customerCreatedStripePaymentIds.size() > 0) {
                                String customerCreatedStripePaymentId = customerCreatedStripePaymentIds.get(0);
                                if (paymentMethodIds.contains(customerCreatedStripePaymentId)) {
                                    paymentMethodIds.removeIf(paymentMethodId -> !customerCreatedStripePaymentId.equalsIgnoreCase(paymentMethodId));
                                } else {
                                    errorList.add("More than one Payment method found for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId);
                                    log.info(errorList.get(errorList.size() - 1));
                                    throw new BadRequestAlertException("More than one Payment method found for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId, ENTITY_NAME, "moreThanOnePaymentFoundForCustomer");
                                }
                            } else {
                                errorList.add("More than one Payment method found for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId);
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("More than one Payment method found for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId, ENTITY_NAME, "moreThanOnePaymentFoundForCustomer");
                            }

                        } else {
                            String email = importSubscriptionContractDTO.getCustomerEmail();
                            email = email.toLowerCase();

                            if (importSubscriptionContractDTO.getCustomerProfileID() != null && importSubscriptionContractDTO.getCardID() != null) {
                                String stripeCustomerId = importSubscriptionContractDTO.getCustomerProfileID();
                                String paymentId = importSubscriptionContractDTO.getCardID();
                                errorList.add("Payment method empty for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId + " StripeCustomerId='" + stripeCustomerId + "' StripeCardId='" + paymentId + "' email='" + email + "'");
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("Payment method empty for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId + " StripeCustomerId='" + stripeCustomerId + "' StripeCardId='" + paymentId + "' email='" + email + "'", ENTITY_NAME, "stripePaymentEmptyForCustomer");
                            } else {
                                errorList.add("Payment method empty for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId);
                                log.info(errorList.get(errorList.size() - 1));
                                throw new BadRequestAlertException("Payment method empty for Id=" + importSubscriptionContractDTO.getId() + " and Customer Id is=" + customerId, ENTITY_NAME, "paymentEmptyForCustomer");
                            }
                        }
                    }

                    String nextOrderDate = importSubscriptionContractDTO.getNextOrderDate();

                    SubscriptionBillingPolicyInput.Builder billingPolicyBuilder = SubscriptionBillingPolicyInput.builder();
                    String billingIntervalType = importSubscriptionContractDTO.getBillingIntervalType();

                    if (org.apache.commons.lang3.StringUtils.isNotBlank(billingIntervalType) && billingIntervalType.toLowerCase().equals("months")) {
                        billingIntervalType = "month";
                    }

                    billingPolicyBuilder.interval(getIntervalType(billingIntervalType));
                    int billingIntervalCount = Integer.parseInt(importSubscriptionContractDTO.getBillingIntervalCount());
                    billingPolicyBuilder.intervalCount(billingIntervalCount);
                    String nextBillingDate = buildNextBillingDate(nextOrderDate, billingIntervalCount, billingIntervalType);
                    SubscriptionContractCreateInput.Builder subscriptionContractCreateInputBuilder = SubscriptionContractCreateInput.builder();

                    subscriptionContractCreateInputBuilder.customerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId);

                    if (nextBillingDate != null) {
                        subscriptionContractCreateInputBuilder.nextBillingDate(nextBillingDate);
                    }

                    subscriptionContractCreateInputBuilder.currencyCode(currencyCode);

                    SubscriptionDraftInput.Builder contractBuilder = SubscriptionDraftInput.builder();

                    if (status.equalsIgnoreCase("active")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.PAUSED);
                    } else if (status.equalsIgnoreCase("cancelled")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.CANCELLED);
                    } else if (status.equalsIgnoreCase("paused")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.PAUSED);
                    } else if (status.equalsIgnoreCase("failed")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.FAILED);
                    }

                    contractBuilder.paymentMethodId(paymentMethodIds.get(0));

                    SubscriptionBillingPolicyInput billingPolicy = billingPolicyBuilder.build();
                    contractBuilder.billingPolicy(billingPolicy);

                    SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder = SubscriptionDeliveryPolicyInput.builder();

                    String deliveryIntervalType = importSubscriptionContractDTO.getDeliveryIntervalType();

                    deliveryPolicyBuilder.interval(getIntervalType(deliveryIntervalType));

                    int deliveryIntervalCount = Integer.parseInt(importSubscriptionContractDTO.getDeliveryIntervalCount());
                    deliveryPolicyBuilder.intervalCount(deliveryIntervalCount);

                    SubscriptionDeliveryPolicyInput deliveryPolicy = deliveryPolicyBuilder.build();
                    contractBuilder.deliveryPolicy(deliveryPolicy);

                    SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
                    SubscriptionDeliveryMethodShippingInput.Builder shippingBuilder = SubscriptionDeliveryMethodShippingInput.builder();
                    MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();

                    addressBuilder.firstName(importSubscriptionContractDTO.getDeliveryFirstName());
                    addressBuilder.lastName(importSubscriptionContractDTO.getDeliveryLastName());
                    addressBuilder.address1(importSubscriptionContractDTO.getDeliveryAddress1());
                    addressBuilder.address2(importSubscriptionContractDTO.getDeliveryAddress2());
                    addressBuilder.city(Optional.ofNullable(importSubscriptionContractDTO.getDeliveryCity()).orElse(""));
                    addressBuilder.provinceCode(importSubscriptionContractDTO.getDeliveryProvinceCode());
                    CountryCode deliveryCountryCode = CountryCode.valueOf(importSubscriptionContractDTO.getDeliveryCountryCode().toUpperCase());
                    addressBuilder.countryCode(deliveryCountryCode);

                    String deliveryZip = importSubscriptionContractDTO.getDeliveryZip();

                    if (deliveryCountryCode.equals(CountryCode.US)) {
                        if (!StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                            deliveryZip = "0" + deliveryZip;
                        }
                    }

                    addressBuilder.zip(deliveryZip);
                    addressBuilder.phone(importSubscriptionContractDTO.getDeliveryPhone());

                    MailingAddressInput address = addressBuilder.build();
                    shippingBuilder.address(address);
                    SubscriptionDeliveryMethodShippingInput shipping = shippingBuilder.build();
                    deliveryMethodBuilder.shipping(shipping);

                    SubscriptionDeliveryMethodInput deliveryMethod = deliveryMethodBuilder.build();
                    contractBuilder.deliveryMethod(deliveryMethod);

                    String deliveryPriceAmount = Optional.ofNullable(importSubscriptionContractDTO.getDeliveryPriceAmount()).map(String::trim).orElse("");
                    double deliveryPrice = Double.parseDouble(deliveryPriceAmount.equals("") ? "0" : deliveryPriceAmount);
                    contractBuilder.deliveryPrice(deliveryPrice);

                    SubscriptionDraftInput contract = contractBuilder.build();

                    subscriptionContractCreateInputBuilder.contract(contract);

                    SubscriptionContractCreateInput subscriptionContractCreateInput = subscriptionContractCreateInputBuilder.build();

                    boolean result = trySavingSubscriptionContractForImportSubscriptionContract(shop, shopifyGraphqlClient, importedIds, errorList, processedIds, importSubscriptionContractDTO, subscriptionContractCreateInput, currentIterationImportedIds);
                    log.info("/////Start/////////");
                    log.error("comaSeparatedErrors=" + org.apache.commons.lang3.StringUtils.join(errorList, ","));
                    for (String s : errorList) {
                        log.info(s);
                    }
                    log.info("/////End/////////");
                } else {
                    throw new BadRequestAlertException("Already Processed Id :- " + importSubscriptionContractDTO.getId(), ENTITY_NAME, "subscriptionContractExists");
                }

            } catch (Exception ex) {
                log.info("ex=" + ex.toString() + " ID=" + importSubscriptionContractDTO.getId());
                throw new BadRequestAlertException("ex=" + ex.toString() + " ID=" + importSubscriptionContractDTO.getId(), ENTITY_NAME, "importSubscriptionContractError");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException("ex=" + e.toString() + " ID=" + importSubscriptionContractDTO.getId(), ENTITY_NAME, "importSubscriptionContractError");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "Subscription contract imported successfully.", ENTITY_NAME)).build();
    }

    private boolean trySavingSubscriptionContractForImportSubscriptionContract(String shop, ShopifyGraphqlClient shopifyGraphqlClient, Set<String> importedIds, List<String> errorList, List<String> processedId, ImportSubscriptionContractDTO importSubscriptionContractDTO, SubscriptionContractCreateInput subscriptionContractCreateInput, Set<String> currentIterationImportedIds) {
        try {
            SubscriptionContractCreateMutation subscriptionContractCreateMutation = new SubscriptionContractCreateMutation(subscriptionContractCreateInput);
            Response<Optional<SubscriptionContractCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractCreateMutation);

            String draftId = Objects.requireNonNull(optionalMutationResponse.getData()).flatMap(s -> s.getSubscriptionContractCreate().flatMap(c -> c.getDraft().map(SubscriptionContractCreateMutation.Draft::getId))).orElse(null);

            if (draftId == null) {
                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors() + " ID=" + importSubscriptionContractDTO.getId());
                    errorList.add("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors() + " ID=" + importSubscriptionContractDTO.getId());
                }

                List<SubscriptionContractCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getSubscriptionContractCreate().map(SubscriptionContractCreateMutation.SubscriptionContractCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {

                    log.info("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()) + " ID=" + importSubscriptionContractDTO.getId());
                    errorList.add("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()) + " ID=" + importSubscriptionContractDTO.getId());
                }

                log.info("draftId is null. ID=" + importSubscriptionContractDTO.getId());
                return true;
            }


            SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput.builder();
            subscriptionLineInputBuilder.quantity(Integer.parseInt(importSubscriptionContractDTO.getLineZeroQuantity()));
            String variantId = importSubscriptionContractDTO.getLineZeroVariantID();

            if (org.apache.commons.lang3.StringUtils.isBlank(variantId)) {
                log.info("variantId is empty=" + variantId + " ID=" + importSubscriptionContractDTO.getId());
                errorList.add("variantId is empty=" + variantId + " ID=" + importSubscriptionContractDTO.getId());
                return true;
            }

            String productVariantId = ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId;

            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            if (Objects.requireNonNull(productVariantResponse.getData()).isEmpty() || productVariantResponse.getData().get().getProductVariant().isEmpty()) {
                errorList.add("Invalid VariantId=" + variantId + " ID=" + importSubscriptionContractDTO.getId());
                return true;
            }

            subscriptionLineInputBuilder.productVariantId(productVariantId);
            subscriptionLineInputBuilder.currentPrice(Double.parseDouble(importSubscriptionContractDTO.getLineZeroPriceAmount()));


            SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder.build();
            SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
            Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse1 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

            if (!CollectionUtils.isEmpty(optionalMutationResponse1.getErrors())) {
                log.info("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage() + " ID=" + importSubscriptionContractDTO.getId());
                errorList.add("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage() + " ID=" + importSubscriptionContractDTO.getId());
                return true;
            }


            List<SubscriptionDraftLineAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse1.getData()).map(d -> d.getSubscriptionDraftLineAdd().map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.info("userErrors=" + userErrors + " ID=" + importSubscriptionContractDTO.getId());
                errorList.add("userErrors=" + userErrors + " ID=" + importSubscriptionContractDTO.getId());
                return true;
            }


//                Integer discountPercentage = !subscriptionDataRecord.isMapped("Discount") ? 0 : Optional.ofNullable(subscriptionDataRecord.get(Optional.ofNullable(subscriptionHeaderMap.get("Discount")).orElse("Discount"))).map(Integer::parseInt).orElse(0);
//
//                if (discountPercentage > 0) {
//                    if (!applyDiscount(shopifyGraphqlClient, draftId, discountPercentage, errorList, importSubscriptionContractDTO.getId())) {
//                        return true;
//                    }
//                }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);


            if (!CollectionUtils.isEmpty(optionalMutationResponse2.getErrors())) {
                log.info("optionalMutationResponse2.getErrors()=" + optionalMutationResponse2.getErrors() + " ID=" + importSubscriptionContractDTO.getId());
                errorList.add("optionalMutationResponse2.getErrors()=\" + optionalMutationResponse2.getErrors() + \" ID=\" + id");
                return true;
            }

            List<SubscriptionDraftCommitMutation.UserError> userErrors1 = Objects.requireNonNull(optionalMutationResponse2.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors1.isEmpty()) {
                log.info("userErrors1=" + userErrors1 + " ID=" + importSubscriptionContractDTO.getId());
                errorList.add("userErrors1=" + userErrors1 + " ID=" + importSubscriptionContractDTO.getId());
                return true;
            }

            String graphContractId = optionalMutationResponse2.getData().flatMap(d -> d.getSubscriptionDraftCommit().flatMap(e -> e.getContract().map(SubscriptionDraftCommitMutation.Contract::getId))).orElse(null);

            log.info("processed ID=" + importSubscriptionContractDTO.getId() + " graphContractId=" + graphContractId);
            processedId.add("processed ID=" + importSubscriptionContractDTO.getId() + " graphContractId=" + graphContractId);

            long numContractId = Long.parseLong(graphContractId.replace(ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

            Optional<SubscriptionContractDetailsDTO> contractDetailsDTOOptional = subscriptionContractDetailsService.findByContractId(numContractId);

            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = contractDetailsDTOOptional.orElse(new SubscriptionContractDetailsDTO());
            subscriptionContractDetailsDTO.setShop(shop);
            subscriptionContractDetailsDTO.setGraphSubscriptionContractId(graphContractId);
            subscriptionContractDetailsDTO.setSubscriptionContractId(numContractId);
            subscriptionContractDetailsDTO.setImportedId(importSubscriptionContractDTO.getId());
            subscriptionContractDetailsDTO.setStopUpComingOrderEmail(false);
            if (importSubscriptionContractDTO.getStatus().equalsIgnoreCase("active")) {
                subscriptionContractDetailsDTO.setPausedFromActive(true);
            } else {
                subscriptionContractDetailsDTO.setPausedFromActive(false);
            }

            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

            //importedIds.add(id);
            currentIterationImportedIds.add(importSubscriptionContractDTO.getId());

        } catch (Exception ex) {
            //log.info("ex=" + ex.toString() + " ID=" + id);
            errorList.add("ex=" + ex.toString() + " ID=" + importSubscriptionContractDTO.getId());
        }
        return false;
    }

    private boolean associateShopifyCustomerWithStripeCustomerForImportedSubscriptionsContract(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, ImportSubscriptionContractDTO importSubscriptionContractDTO, List<String> customerCreatedPaymentIds) throws Exception {
        String stripeCustomerId = importSubscriptionContractDTO.getCustomerProfileID();
        String paymentId = importSubscriptionContractDTO.getCardID();

        CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().stripePaymentMethod(RemoteStripePaymentMethodInput.builder().customerId(stripeCustomerId).paymentMethodId(paymentId).build()).build();
        CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, customerPaymentMethodRemoteInput);
        Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);


        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            return true;
        }

        List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
            return false;
        }

        String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

        if (customerCreatedPaymentId != null) {
            customerCreatedPaymentIds.add(customerCreatedPaymentId);
        }
        return false;
    }

    private boolean associateShopifyCustomerWithPayPalCustomerForImportedSubscriptionsContract(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, ImportSubscriptionContractDTO importSubscriptionContractDTO) throws Exception {
        String billingAgreementId = importSubscriptionContractDTO.getCardID();

        CustomerPaymentMethodPaypalBillingAgreementCreateMutation createRemotePaypalMutation = new CustomerPaymentMethodPaypalBillingAgreementCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, billingAgreementId);
        Response<Optional<CustomerPaymentMethodPaypalBillingAgreementCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(createRemotePaypalMutation);

        return !CollectionUtils.isEmpty(optionalMutationResponse3.getErrors());
    }

    private boolean associateShopifyCustomerWithAuthorizeNetCustomerForImportedSubscriptionsContract(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, ImportSubscriptionContractDTO importSubscriptionContractDTO, List<String> customerCreatedPaymentIds) throws Exception {
        String customerPaymentProfileId = importSubscriptionContractDTO.getCardID();
        String customerProfileId = importSubscriptionContractDTO.getCustomerProfileID();

        RemoteAuthorizeNetCustomerPaymentProfileInput remoteAuthorizeNetCustomerPaymentProfileInput = RemoteAuthorizeNetCustomerPaymentProfileInput.builder().customerProfileId(customerProfileId).customerPaymentProfileId(customerPaymentProfileId).build();

        CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().authorizeNetCustomerPaymentProfile(remoteAuthorizeNetCustomerPaymentProfileInput).build();

        CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, customerPaymentMethodRemoteInput);
        Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            return true;
        }

        List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
            return false;
        }

        String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

        if (customerCreatedPaymentId != null) {
            customerCreatedPaymentIds.add(customerCreatedPaymentId);
        }
        return false;
    }

    private List<String> getPaymentMethodListForImportSubscriptionContract(long customerId, ShopifyGraphqlClient shopifyGraphqlClient, ImportSubscriptionContractDTO importSubscriptionContractDTO) throws Exception {
        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, Input.optional(false));

        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

        List<CustomerPaymentMethodsQuery.Node> paymentMethods = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        for (CustomerPaymentMethodsQuery.Node paymentMethod : paymentMethods) {
            String a = "b";
        }

        List<String> paymentMethodIds = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode().getId()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (paymentMethodIds.size() > 1 && org.apache.commons.lang3.StringUtils.isNotBlank(importSubscriptionContractDTO.getLastFourDigits())) {

            paymentMethodIds = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().filter(e -> {
                String last4Digits = importSubscriptionContractDTO.getLastFourDigits();
                if ("paypal".equalsIgnoreCase(last4Digits)) {
                    if (e.getNode().getInstrument().get() instanceof CustomerPaymentMethodsQuery.AsCustomerPaypalBillingAgreement) {
                        return !((CustomerPaymentMethodsQuery.AsCustomerPaypalBillingAgreement) e.getNode().getInstrument().get()).isInactive();
                    } else {
                        return false;
                    }
                } else {
                    if (last4Digits.length() == 3) {
                        last4Digits = "0" + last4Digits.trim();
                    }
                    return ((CustomerPaymentMethodsQuery.AsCustomerCreditCard) e.getNode().getInstrument().get()).getLastDigits().equalsIgnoreCase(last4Digits);
                }
            }).map(e -> e.getNode().getId()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            while (paymentMethodIds.size() > 1) {
                paymentMethodIds.remove(paymentMethodIds.size() - 1);
            }
        }

        return paymentMethodIds;
    }

    private void addVariantToContractForImportSubscriptionContract(ImportSubscriptionContractDTO importSubscriptionContractDTO) throws Exception {
        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByShopAndImportedId(shop, importSubscriptionContractDTO.getId());

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractService.findSubscriptionContractByContractId(subscriptionContractDetailsDTO.get().getSubscriptionContractId(), subscriptionContractDetailsDTO.get().getShop());

        Set<String> variantId = new HashSet<>();

        subscriptionContract.get().getLines().getEdges().stream().forEach(p -> {
            variantId.add(p.getNode().getVariantId().get());
        });

        log.info("{} Calling shopify graphql for update subscription contract {}", shop, subscriptionContractDetailsDTO.get().getSubscriptionContractId());
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetailsDTO.get().getSubscriptionContractId());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.debug("{} Response received from graphql update subscription contract {} ", shop, subscriptionContractDetailsDTO.get().getSubscriptionContractId());

        long countOfErrors = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors).orElse(new ArrayList<>()).stream().map(SubscriptionContractUpdateMutation.UserError::getMessage).peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                SubscriptionLineInput subscriptionLineInput = SubscriptionLineInput.builder().currentPrice(Double.parseDouble(importSubscriptionContractDTO.getLineZeroPriceAmount())).productVariantId(PRODUCT_VARIANT_ID_PREFIX + importSubscriptionContractDTO.getLineZeroVariantID()).quantity(Integer.parseInt(importSubscriptionContractDTO.getLineZeroQuantity())).build();

                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(optionalDraftId.get(), subscriptionLineInput);
                Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);
            }

            //processedIds.add("Id=" + csvRecord.get("ID") + " processed with add new line item into subscription");
        }
    }

    private CsvImportResult runImporter(MultipartFile subscriptionDataFile, MultipartFile customerDataFile, String importType, String headerMappingJsonString, boolean isValidation) throws IOException {

        Reader subscriptionDataReader = new BufferedReader(new InputStreamReader(subscriptionDataFile.getInputStream()));

        Reader customerDataReader = null;
        if (importType.equalsIgnoreCase("stripe")
            || importType.equalsIgnoreCase("paypal")
            || importType.equalsIgnoreCase("authorize_net")) {
            customerDataReader = new BufferedReader(new InputStreamReader(customerDataFile.getInputStream()));
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();

        MigrationInputRequest migrationInputRequest = new MigrationInputRequest(importType, headerMappingJsonString, isValidation, shop);
        /*migrationInputRequest.setSubscriptionDataS3Key("us-birchbox-staging.myshopify.com/subscription-data.csv");
        migrationInputRequest.setCustomerDataS3Key("us-birchbox-staging.myshopify.com/customer-data.csv");

        StartExecutionRequest startExecutionRequest = new StartExecutionRequest();
        startExecutionRequest.setStateMachineArn("arn:aws:states:us-west-1:114479152947:stateMachine:migration_subscriptions");
        startExecutionRequest.setName(shop + "_" + System.currentTimeMillis());
        startExecutionRequest.setInput(OBJECT_MAPPER.writeValueAsString(migrationInputRequest));
        awsStepFunctions.startExecution(startExecutionRequest);*/

        return executeMigration(migrationInputRequest, subscriptionDataReader, customerDataReader);
    }

    public CsvImportResult executeMigration(MigrationInputRequest migrationInputRequest, Reader subscriptionDataReader, Reader customerDataReader) {

        if (subscriptionDataReader == null) {
            subscriptionDataReader = awsUtils.getDataReaderForMigration(migrationInputRequest.getSubscriptionDataS3Key());
        }

        if (customerDataReader == null && org.apache.commons.lang3.StringUtils.isNotBlank(migrationInputRequest.getCustomerDataS3Key())) {
            customerDataReader = awsUtils.getDataReaderForMigration(migrationInputRequest.getCustomerDataS3Key());
        }

        JSONObject shopifyCountries = getShopifyCountriesJson();

        Map<String, String> countryCodeMap = getCountryCodeMap(shopifyCountries);

        Map<String, Map<String, String>> provinceCodeMap = getProvinceCode(shopifyCountries);

        CsvImportResult csvImportResult = new CsvImportResult();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        AddVariantToContract addVariantToInterface = null;
        SavingSubscriptionContract savingSubscriptionContract = null;
        if (migrationInputRequest.isValidation()) {
            addVariantToInterface = (csvRecord, subscriptionHeaderMap) -> {
            };
            savingSubscriptionContract = (shop, shopifyGraphqlClient, importedIds, errorList, processedId, subscriptionHeaderMap, subscriptionDataRecord, id, status, subscriptionContractCreateInput, currentIterationImportedIds) -> {
                currentIterationImportedIds.add(id);
                return false;
            };
        } else {
            addVariantToInterface = (csvRecord, subscriptionHeaderMap) -> addVariantToContract(csvRecord, subscriptionHeaderMap, migrationInputRequest.getShop());
            savingSubscriptionContract = this::trySavingSubscriptionContract;
        }

        String shop = migrationInputRequest.getShop();
        SocialConnection socialConnection = socialConnectionService.findByUserId(shop).get();
        String accessToken = socialConnection.getAccessToken();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);

        CurrencyCode currencyCode = CurrencyCode.valueOf(shopInfo.get().getCurrency());

        Set<String> currentIterationImportedIds = new HashSet<>();


        List<String> subscriptionDataMissingHeaders = new ArrayList<>();

        List<String> customerDataMissingHeaders = new ArrayList<>();

        List<String> errorList = new ArrayList<>();

        List<String> alreadyProcessedId = new ArrayList<>();

        List<String> processedIds = new ArrayList<>();

        List<CSVHeaderMapping> headerMappingList = commonUtils.fromJSON(new TypeReference<List<CSVHeaderMapping>>() {
        }, migrationInputRequest.getHeaderMappingJsonString());

        Map<String, String> subscriptionHeaderMap = headerMappingList.stream().collect(Collectors.toMap(CSVHeaderMapping::getSystemColumnHeader, CSVHeaderMapping::getSheetColumnOption));

        try {
            CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);


            if (migrationInputRequest.getImportType().equalsIgnoreCase("stripe")) {
                for (String header : ImportConstants.SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1) {
                    if ("".equalsIgnoreCase(subscriptionHeaderMap.get(header))) {
                        subscriptionDataMissingHeaders.add(header);
                    }
                }
            } else if (migrationInputRequest.getImportType().equalsIgnoreCase("braintree")) {
                for (String header : ImportConstants.SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1) {
                    if ("".equalsIgnoreCase(subscriptionHeaderMap.get(header))) {
                        subscriptionDataMissingHeaders.add(header);
                    }
                }
            } else if (migrationInputRequest.getImportType().equalsIgnoreCase("paypal")) {
                for (String header : ImportConstants.SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1) {
                    if ("".equalsIgnoreCase(subscriptionHeaderMap.get(header))) {
                        subscriptionDataMissingHeaders.add(header);
                    }
                }
            } else if (migrationInputRequest.getImportType().equalsIgnoreCase("authorize_net")) {
                for (String header : ImportConstants.SUBSCRIPTION_DATA_DEFAULT_HEADERS_V1) {
                    if ("".equalsIgnoreCase(subscriptionHeaderMap.get(header))) {
                        subscriptionDataMissingHeaders.add(header);
                    }
                }
            } else {
                for (String header : ImportConstants.SUBSCRIPTION_DATA_DEFAULT_HEADERS_V2) {
                    //String sanatizedHeader = cleanTextContent(header);
                    if ("".equalsIgnoreCase(subscriptionHeaderMap.get(header)) && !List.of("Delivery city", "Customer ID", "Customer Email").contains(header)) {
                        subscriptionDataMissingHeaders.add(header);
                    }
                }

                if (org.apache.commons.lang3.StringUtils.isAllBlank(subscriptionHeaderMap.get("Customer ID"), subscriptionHeaderMap.get("Customer Email"))) {
                    subscriptionDataMissingHeaders.add("Customer ID / Customer Email");
                }
            }

            CSVParser customerDataCSVParser = null;
            if (migrationInputRequest.getImportType().equalsIgnoreCase("stripe")) {
                customerDataCSVParser = new CSVParser(customerDataReader, csvFormat);

                for (String header : ImportConstants.CUSTOMER_DATA_STRIPE_HEADERS) {
                    if (!customerDataCSVParser.getHeaderNames().contains(header)) {
                        customerDataMissingHeaders.add(header);
                    }
                }
            }

            if (migrationInputRequest.getImportType().equalsIgnoreCase("braintree")) {
                customerDataCSVParser = new CSVParser(customerDataReader, csvFormat);

                for (String header : ImportConstants.CUSTOMER_DATA_BRAINTREE_HEADERS) {
                    if (!customerDataCSVParser.getHeaderNames().contains(header)) {
                        customerDataMissingHeaders.add(header);
                    }
                }
            }

            if (migrationInputRequest.getImportType().equalsIgnoreCase("paypal")) {
                customerDataCSVParser = new CSVParser(customerDataReader, csvFormat);

                for (String header : ImportConstants.CUSTOMER_DATA_PAYPAL_HEADERS) {
                    if (!customerDataCSVParser.getHeaderNames().contains(header)) {
                        customerDataMissingHeaders.add(header);
                    }
                }
            }

            if (migrationInputRequest.getImportType().equalsIgnoreCase("authorize_net")) {
                customerDataCSVParser = new CSVParser(customerDataReader, csvFormat);

                for (String header : ImportConstants.CUSTOMER_DATA_AUTHORIZE_NET_HEADERS) {
                    if (!customerDataCSVParser.getHeaderNames().contains(header)) {
                        customerDataMissingHeaders.add(header);
                    }
                }
            }

            if (!subscriptionDataMissingHeaders.isEmpty() || !customerDataMissingHeaders.isEmpty()) {
                csvImportResult.setMissingHeaders(subscriptionDataMissingHeaders);
                csvImportResult.setCustomerDataMissingHeaders(customerDataMissingHeaders);
                return csvImportResult;
            }

            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

            log.info("came here " + "subscriptionContractDetailsDTOList=" + subscriptionContractDetailsDTOList.size());

            Set<String> importedIds = subscriptionContractDetailsDTOList.stream().filter(s -> s.getImportedId() != null).map(SubscriptionContractDetailsDTO::getImportedId).collect(Collectors.toSet());


            Map<String, CSVRecord> customerDataByEmail = new HashMap<>();

            if (migrationInputRequest.getImportType().equalsIgnoreCase("stripe") || migrationInputRequest.getImportType().equalsIgnoreCase("braintree") || migrationInputRequest.getImportType().equalsIgnoreCase("paypal") || migrationInputRequest.getImportType().equalsIgnoreCase("authorize_net")) {
                for (CSVRecord csvRecord : customerDataCSVParser) {
                    String email = csvRecord.get("Email");
                    email = email.toLowerCase();
                    customerDataByEmail.putIfAbsent(email, csvRecord);
                }
            }

            int recordNum = 0;
            for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {

                if (recordNum % 10 == 0) {
                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();
                    if (BooleanUtils.isTrue(shopInfoDTO.isStopImportProcess())) {
                        log.info("Stopping import process as stopImportProcess flag is On.");
                        errorList.add("Stopping import process as stopImportProcess flag is On.");
                        break;
                    }
                }

                recordNum++;

                log.info("Iterating record " + (recordNum + 1) + " shop=" + shop);

                Map<String, Integer> headerToIndexMap = subscriptionDataRecord.getParser().getHeaderMap().entrySet().stream().collect(Collectors.toMap(k -> cleanTextContent(k.getKey()), Map.Entry::getValue));

                String id = subscriptionDataRecord.get(headerToIndexMap.get(subscriptionHeaderMap.get("ID")));

                if (StringUtils.isEmpty(id)) {
                    continue;
                }

                try {

                    if (importedIds.contains(id)) {
                        alreadyProcessedId.add("Already Processed Id :- " + id);
                        continue;
                    }

                    String status = subscriptionDataRecord.get(subscriptionHeaderMap.get("Status"));

                    /*if (!status.equalsIgnoreCase("active")) {
                        errorList.add("Id=" + id + " not processed due to contract status " + status);
                        log.info(errorList.get(errorList.size() - 1));
                        continue;
                    }*/
                    String variantId = subscriptionDataRecord.get(subscriptionHeaderMap.get("Variant ID"));

                    String productVariantId = ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId;

                    ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                    Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                    if (Objects.requireNonNull(productVariantResponse.getData()).isEmpty() || productVariantResponse.getData().get().getProductVariant().isEmpty()) {
                        errorList.add("Invalid VariantId=" + variantId + " ID=" + id);
                        log.info(errorList.get(errorList.size() - 1));
                        continue;
                    }

                    if (currentIterationImportedIds.contains(id)) {
                        if (org.apache.commons.lang3.StringUtils.isBlank(variantId)) {
                            errorList.add("Id=" + id + " already processed but contract not update due to product variant id is empty");
                        }

                        addVariantToInterface.addVariantToContract(subscriptionDataRecord, subscriptionHeaderMap);
                        continue;
                    }

                    Long customerId = null;
                    List<String> customerCreatedStripePaymentIds = new ArrayList<>();
                    if (migrationInputRequest.getImportType().equalsIgnoreCase("stripe")) {

                        String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));
                        email = email.toLowerCase();

                        if (!customerDataByEmail.containsKey(email)) {
                            errorList.add("Couldn't find respective email for email: " + email + " in customer-data file: id=" + id);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        } else {
                            CSVRecord customerDataRecord = customerDataByEmail.get(email);

                            List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                            if (subscriptionDataRecord.isMapped("Customer ID")
                                && subscriptionDataRecord.isSet("Customer ID")
                                && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Customer ID"))) {
                                customerId = Long.parseLong(subscriptionDataRecord.get("Customer ID"));
                            }

                            if (customerId == null) {
                                if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                    customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                                } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                    errorList.add("More than one customer found for: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    continue;
                                } else {
                                    customerId = createCustomerId(shopifyGraphqlClient, email);
                                    if (customerId == null) {
                                        errorList.add("Error in creating customer id with emil: " + email);
                                        log.info(errorList.get(errorList.size() - 1));
                                        continue;
                                    }
                                }
                            }


                            if (associateShopifyCustomerWithStripeCustomer(shopifyGraphqlClient, customerId, customerDataRecord, customerCreatedStripePaymentIds)) {
                                errorList.add("Error in associating shopify customer to stripe customer with customer id: " + customerId);
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            }
                        }
                    } else if (migrationInputRequest.getImportType().equalsIgnoreCase("braintree")) {

                        String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));
                        email = email.toLowerCase();

                        if (!customerDataByEmail.containsKey(email)) {
                            errorList.add("Couldn't find respective email for email: " + email + " in customer-data file: id=" + id);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        } else {
                            CSVRecord customerDataRecord = customerDataByEmail.get(email);

                            List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                            if (subscriptionDataRecord.isMapped("Customer ID")
                                && subscriptionDataRecord.isSet("Customer ID")
                                && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Customer ID"))) {
                                customerId = Long.parseLong(subscriptionDataRecord.get("Customer ID"));
                            }

                            if (customerId == null) {
                                if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                    customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                                } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                    errorList.add("More than one customer found for: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    continue;
                                } else {
                                    customerId = createCustomerId(shopifyGraphqlClient, email);
                                    if (customerId == null) {
                                        errorList.add("Error in creating customer id with emil: " + email);
                                        log.info(errorList.get(errorList.size() - 1));
                                        continue;
                                    }
                                }
                            }


                            if (associateShopifyCustomerWithBraintreeCustomer(shopifyGraphqlClient, customerId, customerDataRecord, customerCreatedStripePaymentIds)) {
                                errorList.add("Error in associating shopify customer to braintree customer with customer id: " + customerId);
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            }
                        }
                    } else if (migrationInputRequest.getImportType().equalsIgnoreCase("paypal")) {
                        String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));
                        email = email != null ? email.toLowerCase() : email;

                        if (!customerDataByEmail.containsKey(email)) {
                            errorList.add("Couldn't find respective email for email: " + email + " in customer-data file: id=" + id);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        }

                        CSVRecord customerDataRecord = customerDataByEmail.get(email);

                        List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                        if (subscriptionDataRecord.isMapped("Customer ID")
                            && subscriptionDataRecord.isSet("Customer ID")
                            && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Customer ID"))) {
                            customerId = Long.parseLong(subscriptionDataRecord.get("Customer ID"));
                        }

                        if (customerId == null) {

                            if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                            } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                errorList.add("More than one customer found for: " + email);
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            } else {
                                customerId = createCustomerId(shopifyGraphqlClient, email);
                                if (customerId == null) {
                                    errorList.add("Error in creating customer id with email: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    continue;
                                }
                            }
                        }
                        if (associateShopifyCustomerWithPayPalCustomer(shopifyGraphqlClient, customerId, customerDataRecord, subscriptionDataRecord, errorList, customerCreatedStripePaymentIds)) {
                            errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        }

                    } else if (migrationInputRequest.getImportType().equalsIgnoreCase("authorize_net")) {
                        String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));
                        email = email != null ? email.toLowerCase() : email;

                        if (!customerDataByEmail.containsKey(email)) {
                            errorList.add("Couldn't find respective email for email: " + email + " in customer-data file: id=" + id);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        } else {
                            CSVRecord customerDataRecord = customerDataByEmail.get(email);

                            List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                            if (subscriptionDataRecord.isMapped("Customer ID")
                                && subscriptionDataRecord.isSet("Customer ID")
                                && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Customer ID"))) {
                                customerId = Long.parseLong(subscriptionDataRecord.get("Customer ID"));
                            }

                            if (customerId == null) {

                                if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                    customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                                } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                    errorList.add("More than one customer found for: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    continue;
                                } else {
                                    customerId = createCustomerId(shopifyGraphqlClient, email);
                                    if (customerId == null) {
                                        errorList.add("Error in creating customer id with emil: " + email);
                                        log.info(errorList.get(errorList.size() - 1));
                                        continue;
                                    }
                                }
                            }

                            if (associateShopifyCustomerWithAuthorizeNetCustomer(shopifyGraphqlClient, customerId, customerDataRecord, customerCreatedStripePaymentIds)) {
                                errorList.add("Error in associating shopify customer to authorize net customer with customer id: " + customerId);
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            }
                        }
                    } else {

                        if (subscriptionDataRecord.isMapped("Customer ID")
                            && subscriptionDataRecord.isSet("Customer ID")
                            && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Customer ID"))) {
                            customerId = Long.parseLong(subscriptionDataRecord.get("Customer ID"));
                        } else {
                            String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));

                            if (!StringUtils.hasText(email)) {
                                errorList.add("Customer email is empty for record no=" + (recordNum + 1));
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            }
                            email = email.toLowerCase();

                            List<String> existingCustomerIdsBasedOnEmail = findCustomerIdsByEmail(shopifyGraphqlClient, email);

                            if (existingCustomerIdsBasedOnEmail.size() == 1) {
                                customerId = Long.parseLong(existingCustomerIdsBasedOnEmail.get(0).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
                            } else if (existingCustomerIdsBasedOnEmail.size() > 1) {
                                errorList.add("More than one customer found for: " + email);
                                log.info(errorList.get(errorList.size() - 1));
                                continue;
                            } else {
                                customerId = createCustomerId(shopifyGraphqlClient, email);
                                if (customerId == null) {
                                    errorList.add("Error in creating customer id with emil: " + email);
                                    log.info(errorList.get(errorList.size() - 1));
                                    continue;
                                }
                            }
                        }
                    }

                    List<CustomerPaymentMethodsQuery.Node> paymentMethods = getPaymentMethodList(customerId, shopifyGraphqlClient, subscriptionDataRecord, customerCreatedStripePaymentIds);

                    if (paymentMethods.size() != 1) {
                        if (paymentMethods.size() > 1) {
                            errorList.add("More than one Payment method found for Id=" + id + " and Customer Id is=" + customerId);
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        } else {
                            String email = subscriptionDataRecord.get(subscriptionHeaderMap.get("Customer Email"));
                            email = email != null ? email.toLowerCase() : email;
                            CSVRecord customerDataRecord = customerDataByEmail.get(email);

                            if (customerDataRecord != null) {
                                if (migrationInputRequest.getImportType().equalsIgnoreCase("paypal")) {
                                    String paypalId = customerDataRecord.get("Paypal ID");
                                    errorList.add("Payment method empty for Id=" + id + " and Customer Id is=" + customerId + " PaypalCustomerId='" + paypalId + "' email='" + email + "'");

                                } else if (migrationInputRequest.getImportType().equalsIgnoreCase("braintree")) {

                                    String braintreeCustomerId = customerDataRecord.get("id");
                                    String paymentMethodToken = customerDataRecord.get("Payment Method Token");
                                    errorList.add("Payment method empty for Id=" + id + " and Customer Id is=" + customerId + " BraintreeCustomerId='" + braintreeCustomerId + "' Payment Method Token='" + paymentMethodToken + "' email='" + email + "'");

                                } else {

                                    String stripeCustomerId = customerDataRecord.get("id");
                                    String paymentId = customerDataRecord.get("Card ID");
                                    errorList.add("Payment method empty for Id=" + id + " and Customer Id is=" + customerId + " StripeCustomerId='" + stripeCustomerId + "' StripeCardId='" + paymentId + "' email='" + email + "'");

                                }

                            } else {
                                errorList.add("Payment method empty for Id=" + id + " and Customer Id is=" + customerId);
                            }
                            log.info(errorList.get(errorList.size() - 1));
                            continue;
                        }

                    }

                    if (paymentMethods.get(0).getRevokedReason().isPresent()) {
                        errorList.add("Payment Method is revoked for Id="+ id +" Reason :" + paymentMethods.get(0).getRevokedReason().get() + " and Customer Id is=" + customerId);
                        log.info(errorList.get(errorList.size() - 1));
                        continue;
                    }

                    String nextOrderDate = subscriptionDataRecord.get(subscriptionHeaderMap.get("Next order date"));

                    SubscriptionBillingPolicyInput.Builder billingPolicyBuilder = SubscriptionBillingPolicyInput.builder();

                    if (subscriptionDataRecord.isSet("Min Cycles") && !StringUtils.isEmpty(subscriptionDataRecord.get("Min Cycles"))) {
                        try {
                            Integer minCycles = Integer.parseInt(subscriptionDataRecord.get("Min Cycles").trim());
                            if (minCycles > 0) {
                                billingPolicyBuilder.minCycles(minCycles);
                            } else {
                                errorList.add("Invalid Min Cycles for ID=" + id);
                            }
                        } catch (NumberFormatException nfe) {
                            errorList.add("Invalid Min Cycles for ID=" + id);
                        }
                    }

                    if (subscriptionDataRecord.isSet("Max Cycles") && !StringUtils.isEmpty(subscriptionDataRecord.get("Max Cycles"))) {
                        try {
                            Integer maxCycles = Integer.parseInt(subscriptionDataRecord.get("Max Cycles").trim());
                            if (maxCycles > 0) {
                                billingPolicyBuilder.maxCycles(maxCycles);
                            } else {
                                errorList.add("Invalid Max Cycles for ID=" + id);
                            }
                        } catch (NumberFormatException nfe) {
                            errorList.add("Invalid Max Cycles for ID=" + id);
                        }
                    }

                    String billingIntervalType = subscriptionDataRecord.get(subscriptionHeaderMap.get("Billing interval type"));

                    if (org.apache.commons.lang3.StringUtils.isNotBlank(billingIntervalType) && billingIntervalType.toLowerCase().equals("months")) {
                        billingIntervalType = "month";
                    }

                    billingPolicyBuilder.interval(getIntervalType(billingIntervalType));
                    int billingIntervalCount = Integer.parseInt(subscriptionDataRecord.get(subscriptionHeaderMap.get("Billing interval count")));
                    billingPolicyBuilder.intervalCount(billingIntervalCount);
                    String nextBillingDate = buildNextBillingDate(nextOrderDate, billingIntervalCount, billingIntervalType);
                    SubscriptionContractCreateInput.Builder subscriptionContractCreateInputBuilder = SubscriptionContractCreateInput.builder();

                    subscriptionContractCreateInputBuilder.customerId(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId);

                    if (nextBillingDate != null) {
                        subscriptionContractCreateInputBuilder.nextBillingDate(nextBillingDate);
                    }

                    if (subscriptionDataRecord.isSet("Currency Code") && !StringUtils.isEmpty(subscriptionDataRecord.get("Currency Code"))) {
                        subscriptionContractCreateInputBuilder.currencyCode(CurrencyCode.valueOf(subscriptionDataRecord.get("Currency Code").trim()));
                    } else {
                        subscriptionContractCreateInputBuilder.currencyCode(currencyCode);
                    }

                    SubscriptionDraftInput.Builder contractBuilder = SubscriptionDraftInput.builder();

                    if (status.equalsIgnoreCase("active")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.PAUSED);
                    } else if (status.equalsIgnoreCase("cancelled")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.CANCELLED);
                    } else if (status.equalsIgnoreCase("paused")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.PAUSED);
                    } else if (status.equalsIgnoreCase("failed")) {
                        contractBuilder.status(SubscriptionContractSubscriptionStatus.FAILED);
                    }

                    contractBuilder.paymentMethodId(paymentMethods.get(0).getId());

                    SubscriptionBillingPolicyInput billingPolicy = billingPolicyBuilder.build();
                    contractBuilder.billingPolicy(billingPolicy);

                    SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder = SubscriptionDeliveryPolicyInput.builder();

                    String deliveryIntervalType = subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery interval type"));

                    deliveryPolicyBuilder.interval(getIntervalType(deliveryIntervalType));

                    int deliveryIntervalCount = Integer.parseInt(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery interval count")));
                    deliveryPolicyBuilder.intervalCount(deliveryIntervalCount);

                    SubscriptionDeliveryPolicyInput deliveryPolicy = deliveryPolicyBuilder.build();
                    contractBuilder.deliveryPolicy(deliveryPolicy);

                    String deliveryType = "SHIPPING";

                    if (subscriptionDataRecord.isSet("Delivery type")
                        && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Delivery type"))){
                        deliveryType = subscriptionDataRecord.get("Delivery Type");
                    }

                    SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();

                    String country = subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery country code")).toUpperCase();
                    String province = subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery province code")).toUpperCase();
                    CountryCode deliveryCountryCode = null;

                    if(country.length() > 2) {
                        if(countryCodeMap.containsKey(country)) {
                            deliveryCountryCode = CountryCode.valueOf(countryCodeMap.get(country));
                        }else {
                            errorList.add("Invalid Country Code for ID=" + id);
                            continue;
                        }
                    } else {
                        deliveryCountryCode = CountryCode.valueOf(country);
                    }

                    if(provinceCodeMap.containsKey(deliveryCountryCode.toString())) {
                        Map<String, String> provinceCode = provinceCodeMap.get(deliveryCountryCode.toString());
                        if(provinceCode.containsKey(province)) {
                            province = provinceCode.get(province);
                        }
                    }

                    if (deliveryType.equalsIgnoreCase("SHIPPING")) {

                        if (subscriptionDataRecord.isSet(subscriptionHeaderMap.get("Delivery zip"))
                            && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery zip")))) {
                            SubscriptionDeliveryMethodShippingInput.Builder shippingBuilder = SubscriptionDeliveryMethodShippingInput.builder();
                            MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();
                            addressBuilder.firstName(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery first name")));
                            addressBuilder.lastName(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery last name")));
                            addressBuilder.address1(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery address 1")));
                            addressBuilder.address2(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery address 2")));
                            addressBuilder.city(Optional.ofNullable(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery city"))).orElse(""));
                            addressBuilder.countryCode(deliveryCountryCode);
                            addressBuilder.provinceCode(province);
                            if(subscriptionDataRecord.isSet("Delivery company") && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Delivery company"))) {
                                addressBuilder.company(subscriptionDataRecord.get("Delivery company"));
                            }

                            String deliveryZip = subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery zip"));

                            if (deliveryCountryCode.equals(CountryCode.US)) {
                                if (!StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                                    deliveryZip = "0" + deliveryZip;
                                }
                            }

                            addressBuilder.zip(deliveryZip);
                            addressBuilder.phone(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery phone")));

                            MailingAddressInput address = addressBuilder.build();
                            shippingBuilder.address(address);
                            SubscriptionDeliveryMethodShippingInput shipping = shippingBuilder.build();
                            deliveryMethodBuilder.shipping(shipping);

                        }
                    } else if (deliveryType.equalsIgnoreCase("LOCAL")) {
                        if (!subscriptionDataRecord.isSet(subscriptionHeaderMap.get("Delivery phone")) ||
                            org.apache.commons.lang3.StringUtils.isBlank(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery phone")))) {
                            errorList.add("Delivery phone is empty for Id=" + id);
                            continue;
                        } else {
                            if (subscriptionDataRecord.isSet(subscriptionHeaderMap.get("Delivery zip"))
                                && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery zip")))) {
                                MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();

                                addressBuilder.firstName(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery first name")));
                                addressBuilder.lastName(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery last name")));
                                addressBuilder.address1(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery address 1")));
                                addressBuilder.address2(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery address 2")));
                                addressBuilder.city(Optional.ofNullable(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery city"))).orElse(""));
                                addressBuilder.provinceCode(province);
                                addressBuilder.countryCode(deliveryCountryCode);
                                if(subscriptionDataRecord.isSet("Delivery company") && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Delivery company"))) {
                                    addressBuilder.company(subscriptionDataRecord.get("Delivery company"));
                                }

                                String deliveryZip = subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery zip"));

                                if (deliveryCountryCode.equals(CountryCode.US)) {
                                    if (!StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                                        deliveryZip = "0" + deliveryZip;
                                    }
                                }

                                addressBuilder.zip(deliveryZip);
                                addressBuilder.phone(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery phone")));

                                MailingAddressInput address = addressBuilder.build();
                                SubscriptionDeliveryMethodLocalDeliveryOptionInput.Builder deliveryOptionInputBuilder = SubscriptionDeliveryMethodLocalDeliveryOptionInput.builder();

                                deliveryOptionInputBuilder.phone(subscriptionDataRecord.get(subscriptionHeaderMap.get("Delivery phone")));

                                SubscriptionDeliveryMethodLocalDeliveryOptionInput deliveryOptionInput = deliveryOptionInputBuilder.build();

                                SubscriptionDeliveryMethodLocalDeliveryInput localDeliveryInput = SubscriptionDeliveryMethodLocalDeliveryInput.builder()
                                    .address(address)
                                    .localDeliveryOption(deliveryOptionInput)
                                    .build();
                                deliveryMethodBuilder.localDelivery(localDeliveryInput);
                            }else {
                                log.info("Delivery phone is empty for Id= {}", id);
                            }
                        }

                    } else if (deliveryType.equalsIgnoreCase("PICK_UP")) {
                        if (subscriptionDataRecord.isSet("Location id")
                            && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Location id"))) {

                            SubscriptionDeliveryMethodPickupOptionInput subscriptionDeliveryMethodPickupOptionInput = SubscriptionDeliveryMethodPickupOptionInput.builder()
                                .locationId(ShopifyGraphQLUtils.getGraphQlLocationId(subscriptionDataRecord.get("Location id"))).build();

                            SubscriptionDeliveryMethodPickupInput subscriptionDeliveryMethodPickupInput = SubscriptionDeliveryMethodPickupInput.builder()
                                .pickupOption(subscriptionDeliveryMethodPickupOptionInput)
                                .build();

                            deliveryMethodBuilder.pickup(subscriptionDeliveryMethodPickupInput);

                        } else {
                            errorList.add("Location id is empty for Id=" + id);
                            log.info("Location id is empty for Id= {}", id);
                            continue;
                        }
                    }
                    SubscriptionDeliveryMethodInput deliveryMethod = deliveryMethodBuilder.build();
                    contractBuilder.deliveryMethod(deliveryMethod);

                    String deliveryPriceAmount = Optional.ofNullable(subscriptionDataRecord.get(subscriptionHeaderMap.get("Shipping Price"))).map(String::trim).orElse("");
                    double deliveryPrice = Double.parseDouble(deliveryPriceAmount.equals("") ? "0" : deliveryPriceAmount);
                    contractBuilder.deliveryPrice(deliveryPrice);

                    SubscriptionDraftInput contract = contractBuilder.build();

                    subscriptionContractCreateInputBuilder.contract(contract);

                    SubscriptionContractCreateInput subscriptionContractCreateInput = subscriptionContractCreateInputBuilder.build();

                    boolean result = savingSubscriptionContract.trySavingSubscriptionContract(shop, shopifyGraphqlClient, importedIds, errorList, processedIds, subscriptionHeaderMap, subscriptionDataRecord, id, status, subscriptionContractCreateInput, currentIterationImportedIds);
                    if (result) {

                    }

                } catch (Exception ex) {
                    log.info("ex=" + ex.toString() + " ID=" + id);
                    errorList.add("ex=" + ex.toString() + " ID=" + id);
                }
            }
        } catch (IOException e) {
            log.info("an error occurred while running import. ex=" + ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

        csvImportResult.setMissingHeaders(subscriptionDataMissingHeaders);
        csvImportResult.setErrorList(errorList);
        csvImportResult.setAlreadyProcessedId(alreadyProcessedId);
        csvImportResult.setProcessedId(processedIds);

        log.info("/////Start/////////");
        log.error("comaSeparatedErrors=" + org.apache.commons.lang3.StringUtils.join(errorList, ","));
        for (String s : errorList) {
            log.info(s);
        }
        log.info("/////End/////////");

        return csvImportResult;
    }

    private boolean trySavingSubscriptionContract(String shop, ShopifyGraphqlClient shopifyGraphqlClient, Set<String> importedIds, List<String> errorList, List<String> processedId, Map<String, String> subscriptionHeaderMap, CSVRecord subscriptionDataRecord, String id, String status, SubscriptionContractCreateInput subscriptionContractCreateInput, Set<String> currentIterationImportedIds) {
        try {
            SubscriptionContractCreateMutation subscriptionContractCreateMutation = new SubscriptionContractCreateMutation(subscriptionContractCreateInput);
            Response<Optional<SubscriptionContractCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractCreateMutation);

            String draftId = Objects.requireNonNull(optionalMutationResponse.getData()).flatMap(s -> s.getSubscriptionContractCreate().flatMap(c -> c.getDraft().map(SubscriptionContractCreateMutation.Draft::getId))).orElse(null);

            if (draftId == null) {
                if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                    log.info("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors() + " ID=" + id);
                    errorList.add("optionalMutationResponse.getErrors()=" + optionalMutationResponse.getErrors() + " ID=" + id);
                }

                List<SubscriptionContractCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData()).map(d -> d.getSubscriptionContractCreate().map(SubscriptionContractCreateMutation.SubscriptionContractCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {

                    log.info("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()) + " ID=" + id);
                    errorList.add("userErrors=" + userErrors.stream().map(SubscriptionContractCreateMutation.UserError::getMessage).collect(Collectors.toList()) + " ID=" + id);
                }

                log.info("draftId is null. ID=" + id);
                return true;
            }


            SubscriptionLineInput.Builder subscriptionLineInputBuilder = SubscriptionLineInput.builder();
            subscriptionLineInputBuilder.quantity(Integer.parseInt(subscriptionDataRecord.get(subscriptionHeaderMap.get("Variant quantity"))));
            String variantId = subscriptionDataRecord.get(subscriptionHeaderMap.get("Variant ID"));

            if (org.apache.commons.lang3.StringUtils.isBlank(variantId)) {
                log.info("variantId is empty=" + variantId + " ID=" + id);
                errorList.add("variantId is empty=" + variantId + " ID=" + id);
                return true;
            }

            String productVariantId = ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + variantId;

            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

            if (Objects.requireNonNull(productVariantResponse.getData()).isEmpty() || productVariantResponse.getData().get().getProductVariant().isEmpty()) {
                errorList.add("Invalid VariantId=" + variantId + " ID=" + id);
                return true;
            }

            subscriptionLineInputBuilder.productVariantId(productVariantId);
            subscriptionLineInputBuilder.currentPrice(Double.parseDouble(subscriptionDataRecord.get(subscriptionHeaderMap.get("Variant price"))));


            SubscriptionLineInput subscriptionLineInput = subscriptionLineInputBuilder.build();
            SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(draftId, subscriptionLineInput);
            Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse1 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

            if (!CollectionUtils.isEmpty(optionalMutationResponse1.getErrors())) {
                log.info("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage() + " ID=" + id);
                errorList.add("optionalMutationResponse1.getErrors()=" + optionalMutationResponse1.getErrors().get(0).getMessage() + " ID=" + id);
                return true;
            }


            List<SubscriptionDraftLineAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse1.getData()).map(d -> d.getSubscriptionDraftLineAdd().map(SubscriptionDraftLineAddMutation.SubscriptionDraftLineAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.info("userErrors=" + userErrors + " ID=" + id);
                errorList.add("userErrors=" + userErrors + " ID=" + id);
                return true;
            }


            Integer discountPercentage = !subscriptionDataRecord.isMapped("Discount") ? 0 : Optional.ofNullable(subscriptionDataRecord.get(Optional.ofNullable(subscriptionHeaderMap.get("Discount")).orElse("Discount"))).map(Integer::parseInt).orElse(0);

            if (discountPercentage > 0) {
                if (!applyDiscount(shopifyGraphqlClient, draftId, discountPercentage, errorList, id)) {
                    return true;
                }
            }

            String discountCode = !subscriptionDataRecord.isMapped("Discount Code") ? null : Optional.ofNullable(subscriptionDataRecord.get(Optional.ofNullable(subscriptionHeaderMap.get("Discount Code")).orElse("Discount Code"))).orElse(null);

            if (org.apache.commons.lang3.StringUtils.isNotBlank(discountCode)) {
                applyDiscountCode(draftId, discountCode, shopifyGraphqlClient);
            }

            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);


            if (!CollectionUtils.isEmpty(optionalMutationResponse2.getErrors())) {
                log.info("optionalMutationResponse2.getErrors()=" + optionalMutationResponse2.getErrors() + " ID=" + id);
                errorList.add("optionalMutationResponse2.getErrors()=\" + optionalMutationResponse2.getErrors() + \" ID=\" + id");
                return true;
            }

            List<SubscriptionDraftCommitMutation.UserError> userErrors1 = Objects.requireNonNull(optionalMutationResponse2.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors1.isEmpty()) {
                log.info("userErrors1=" + userErrors1 + " ID=" + id);
                errorList.add("userErrors1=" + userErrors1 + " ID=" + id);
                return true;
            }

            String graphContractId = optionalMutationResponse2.getData().flatMap(d -> d.getSubscriptionDraftCommit().flatMap(e -> e.getContract().map(SubscriptionDraftCommitMutation.Contract::getId))).orElse(null);

            log.info("processed ID=" + id + " graphContractId=" + graphContractId);
            processedId.add("processed ID=" + id + " graphContractId=" + graphContractId);

            long numContractId = Long.parseLong(graphContractId.replace(ShopifyIdPrefix.SUBSCRIPTION_CONTRACT_ID_PREFIX, ""));

            Optional<SubscriptionContractDetailsDTO> contractDetailsDTOOptional = subscriptionContractDetailsService.findByContractId(numContractId);

            SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = contractDetailsDTOOptional.orElse(new SubscriptionContractDetailsDTO());
            subscriptionContractDetailsDTO.setShop(shop);
            subscriptionContractDetailsDTO.setGraphSubscriptionContractId(graphContractId);
            subscriptionContractDetailsDTO.setSubscriptionContractId(numContractId);
            subscriptionContractDetailsDTO.setImportedId(id);
            subscriptionContractDetailsDTO.setStopUpComingOrderEmail(false);
            if (status.equalsIgnoreCase("active")) {
                subscriptionContractDetailsDTO.setPausedFromActive(true);
            } else {
                subscriptionContractDetailsDTO.setPausedFromActive(false);
            }

            subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);

            //importedIds.add(id);
            currentIterationImportedIds.add(id);

        } catch (Exception ex) {
            //log.info("ex=" + ex.toString() + " ID=" + id);
            errorList.add("ex=" + ex.toString() + " ID=" + id);
        }
        return false;
    }

    private boolean applyDiscount(ShopifyGraphqlClient shopifyGraphqlClient, String draftId, Integer discountPercentage, List<String> errorList, String id) throws Exception {
        SubscriptionManualDiscountValueInput subscriptionManualDiscountValueInput = SubscriptionManualDiscountValueInput.builder().percentage(discountPercentage).build();

        SubscriptionManualDiscountEntitledLinesInput entitledLines = SubscriptionManualDiscountEntitledLinesInput.builder().all(true).build();

        SubscriptionManualDiscountInput subscriptionManualDiscountInput = SubscriptionManualDiscountInput.builder().title("Contract Discount").entitledLines(entitledLines).value(subscriptionManualDiscountValueInput).build();
        SubscriptionDraftDiscountAddMutation subscriptionDraftDiscountAddMutation = new SubscriptionDraftDiscountAddMutation(draftId, subscriptionManualDiscountInput);
        Response<Optional<SubscriptionDraftDiscountAddMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountAddMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse2.getErrors())) {
            log.info("optionalMutationResponse1.getErrors()=" + optionalMutationResponse2.getErrors().get(0).getMessage() + " ID=" + id);
            errorList.add("optionalMutationResponse1.getErrors()=" + optionalMutationResponse2.getErrors().get(0).getMessage() + " ID=" + id);
            return false;
        }


        List<SubscriptionDraftDiscountAddMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse2.getData()).map(d -> d.getSubscriptionDraftDiscountAdd().map(SubscriptionDraftDiscountAddMutation.SubscriptionDraftDiscountAdd::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty()) {
            log.info("userErrors=" + userErrors + " ID=" + id);
            errorList.add("userErrors=" + userErrors + " ID=" + id);
            return false;
        }

        return true;
    }

    private boolean applyDiscountCode(String draftId, String discountCode, ShopifyGraphqlClient shopifyGraphqlClient){
        try {
            SubscriptionDraftDiscountCodeApplyMutation subscriptionDraftDiscountCodeApplyMutation = new SubscriptionDraftDiscountCodeApplyMutation(draftId, discountCode);
            Response<Optional<SubscriptionDraftDiscountCodeApplyMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountCodeApplyMutation);

            if (!CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private boolean associateShopifyCustomerWithAuthorizeNetCustomer(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, CSVRecord customerDataRecord, List<String> customerCreatedPaymentIds) throws Exception {
        String customerPaymentProfileId = customerDataRecord.get("Customer Payment Profile Id");
        String customerProfileId = customerDataRecord.get("Customer Profile Id");

        RemoteAuthorizeNetCustomerPaymentProfileInput remoteAuthorizeNetCustomerPaymentProfileInput = RemoteAuthorizeNetCustomerPaymentProfileInput.builder().customerProfileId(customerProfileId).customerPaymentProfileId(customerPaymentProfileId).build();

        CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().authorizeNetCustomerPaymentProfile(remoteAuthorizeNetCustomerPaymentProfileInput).build();

        CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, customerPaymentMethodRemoteInput);
        Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            return true;
        }

        List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
            return false;
        }

        String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

        if (customerCreatedPaymentId != null) {
            customerCreatedPaymentIds.add(customerCreatedPaymentId);
        }
        return false;
    }

    private boolean associateShopifyCustomerWithStripeCustomer(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, CSVRecord customerDataRecord, List<String> customerCreatedPaymentIds) throws Exception {
        String stripeCustomerId = customerDataRecord.get("id");
        String paymentId = customerDataRecord.get("Card ID");

        CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().stripePaymentMethod(RemoteStripePaymentMethodInput.builder().customerId(stripeCustomerId).paymentMethodId(paymentId).build()).build();
        CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, customerPaymentMethodRemoteInput);
        Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);


        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            return true;
        }

        List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
            return false;
        }

        String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

        if (customerCreatedPaymentId != null) {
            customerCreatedPaymentIds.add(customerCreatedPaymentId);
        }
        return false;
    }

    private boolean associateShopifyCustomerWithBraintreeCustomer(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, CSVRecord customerDataRecord, List<String> customerCreatedPaymentIds) throws Exception {
        String braintreeCustomerId = customerDataRecord.get("id");
        String paymentId = customerDataRecord.get("Payment Method Token");

        CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().braintreePaymentMethod(RemoteBraintreePaymentMethodInput.builder().customerId(braintreeCustomerId).paymentMethodToken(paymentId).build()).build();
        CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, customerPaymentMethodRemoteInput);
        Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);


        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            return true;
        }

        List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
            return false;
        }

        String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

        if (customerCreatedPaymentId != null) {
            customerCreatedPaymentIds.add(customerCreatedPaymentId);
        }
        return false;
    }

    private boolean associateShopifyCustomerWithPayPalCustomer(ShopifyGraphqlClient shopifyGraphqlClient, Long customerId, CSVRecord customerDataRecord, CSVRecord subscriptionDataRecord, List<String> errorList, List<String> customerCreatedPaymentIds) throws Exception {
        String billingAgreementId = customerDataRecord.get("Paypal ID");

        if (subscriptionDataRecord.isSet("Billing zip")
            && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Billing zip"))) {

            MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();

            addressBuilder.firstName(subscriptionDataRecord.get("Billing first name"));
            addressBuilder.lastName(subscriptionDataRecord.get("Billing last name"));
            addressBuilder.address1(subscriptionDataRecord.get("Billing address 1"));
            addressBuilder.address2(subscriptionDataRecord.get("Billing address 2"));
            addressBuilder.city(Optional.ofNullable(subscriptionDataRecord.get("Billing city")).orElse(""));
            addressBuilder.provinceCode(subscriptionDataRecord.get("Billing province code"));
            CountryCode deliveryCountryCode = CountryCode.valueOf(subscriptionDataRecord.get("Billing country code").toUpperCase());
            addressBuilder.countryCode(deliveryCountryCode);

            String deliveryZip = subscriptionDataRecord.get("Billing zip");

            if (deliveryCountryCode.equals(CountryCode.US)) {
                if (!StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                    deliveryZip = "0" + deliveryZip;
                }
            }

            addressBuilder.zip(deliveryZip);
            addressBuilder.phone(subscriptionDataRecord.get("Billing phone"));

            MailingAddressInput billingAddress = addressBuilder.build();

            CustomerPaymentMethodPaypalBillingAgreementCreateV2Mutation createRemotePaypalMutation = new CustomerPaymentMethodPaypalBillingAgreementCreateV2Mutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, billingAgreementId, billingAddress, false);
            Response<Optional<CustomerPaymentMethodPaypalBillingAgreementCreateV2Mutation.Data>> optionalMutationResponse1 = shopifyGraphqlClient.getOptionalMutationResponse(createRemotePaypalMutation);

            if (!CollectionUtils.isEmpty(optionalMutationResponse1.getErrors())) {
                errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId + " ErrorMessage:" + optionalMutationResponse1.getErrors().get(0).getMessage());
            }

            List<CustomerPaymentMethodPaypalBillingAgreementCreateV2Mutation.UserError> userErrors = optionalMutationResponse1.getData().map(d -> d.getCustomerPaymentMethodPaypalBillingAgreementCreate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
                errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId + " ErrorMessage:" + userErrors.get(0).getMessage());
            }

            String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse1.getData()).flatMap(d -> d.getCustomerPaymentMethodPaypalBillingAgreementCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodPaypalBillingAgreementCreateV2Mutation.CustomerPaymentMethod::getId))).orElse(null);

            if (customerCreatedPaymentId != null) {
                customerCreatedPaymentIds.add(customerCreatedPaymentId);
            }

            return !CollectionUtils.isEmpty(optionalMutationResponse1.getErrors()) || !userErrors.isEmpty();

        } else {
            CustomerPaymentMethodPaypalBillingAgreementCreateMutation createRemotePaypalMutation = new CustomerPaymentMethodPaypalBillingAgreementCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId, billingAgreementId);
            Response<Optional<CustomerPaymentMethodPaypalBillingAgreementCreateMutation.Data>> optionalMutationResponse2 = shopifyGraphqlClient.getOptionalMutationResponse(createRemotePaypalMutation);

            if (!CollectionUtils.isEmpty(optionalMutationResponse2.getErrors())) {
                errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId + " ErrorMessage:" + optionalMutationResponse2.getErrors().get(0).getMessage());
            }

            List<CustomerPaymentMethodPaypalBillingAgreementCreateMutation.UserError> userErrors = optionalMutationResponse2.getData().map(d -> d.getCustomerPaymentMethodPaypalBillingAgreementCreate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
                errorList.add("Error in associating shopify customer to paypal customer with customer id: " + customerId + " ErrorMessage:" + userErrors.get(0).getMessage());
            }

            String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse2.getData()).flatMap(d -> d.getCustomerPaymentMethodPaypalBillingAgreementCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodPaypalBillingAgreementCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

            if (customerCreatedPaymentId != null) {
                customerCreatedPaymentIds.add(customerCreatedPaymentId);
            }

            return !CollectionUtils.isEmpty(optionalMutationResponse2.getErrors()) || !userErrors.isEmpty();
        }

    }

    @Nullable
    private Long createCustomerId(ShopifyGraphqlClient shopifyGraphqlClient, String email) throws Exception {
        Long customerId;
        CustomerInput customerInput = CustomerInput.builder().email(email).build();
        CustomerCreateMutation customerCreateMutation = new CustomerCreateMutation(customerInput);
        Response<Optional<CustomerCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerCreateMutation);

        if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
            log.info(optionalMutationResponse3.getErrors().get(0).getMessage());
            return null;
        }

        List<CustomerCreateMutation.UserError> userErrors2 = Objects.requireNonNull(optionalMutationResponse3.getData()).map(d -> d.getCustomerCreate().map(CustomerCreateMutation.CustomerCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

        if (!userErrors2.isEmpty()) {
            log.info(userErrors2.get(0).getMessage());
            return null;
        }

        customerId = Long.parseLong(optionalMutationResponse3.getData().flatMap(f -> f.getCustomerCreate().flatMap(g -> g.getCustomer().map(CustomerCreateMutation.Customer::getId))).orElse(null).replace(ShopifyIdPrefix.CUSTOMER_ID_PREFIX, ""));
        return customerId;
    }

    @NotNull
    private List<String> findCustomerIdsByEmail(ShopifyGraphqlClient shopifyGraphqlClient, String email) throws Exception {
        CustomerSearchQuery customerSearchQuery = new CustomerSearchQuery(Input.fromNullable(email), Input.fromNullable(null));
        Response<Optional<CustomerSearchQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(customerSearchQuery);

        return Objects.requireNonNull(optionalQueryResponse.getData()).map(CustomerSearchQuery.Data::getCustomers).map(c -> c.getEdges().stream().map(CustomerSearchQuery.Edge::getNode).map(CustomerSearchQuery.Node::getId).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    private void addVariantToContract(CSVRecord csvRecord, Map<String, String> subscriptionHeaderMap, String shop) throws Exception {
        Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByShopAndImportedId(shop, csvRecord.get(subscriptionHeaderMap.get("ID")));

        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractService.findSubscriptionContractByContractId(subscriptionContractDetailsDTO.get().getSubscriptionContractId(), subscriptionContractDetailsDTO.get().getShop());

        Set<String> variantId = new HashSet<>();

        subscriptionContract.get().getLines().getEdges().stream().forEach(p -> {
            variantId.add(p.getNode().getVariantId().get());
        });

        log.info("{} Calling shopify graphql for update subscription contract {}", shop, subscriptionContractDetailsDTO.get().getSubscriptionContractId());
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetailsDTO.get().getSubscriptionContractId());

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        log.debug("{} Response received from graphql update subscription contract {} ", shop, subscriptionContractDetailsDTO.get().getSubscriptionContractId());

        long countOfErrors = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors).orElse(new ArrayList<>()).stream().map(SubscriptionContractUpdateMutation.UserError::getMessage).peek(message -> log.info("Update subscription contract is failed {} ", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId());


            if (optionalDraftId.isPresent()) {
                SubscriptionLineInput subscriptionLineInput = SubscriptionLineInput.builder().currentPrice(Double.parseDouble(csvRecord.get(subscriptionHeaderMap.get("Variant price")))).productVariantId(PRODUCT_VARIANT_ID_PREFIX + csvRecord.get(subscriptionHeaderMap.get("Variant ID"))).quantity(Integer.parseInt(csvRecord.get(subscriptionHeaderMap.get("Variant quantity")))).build();

                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(optionalDraftId.get(), subscriptionLineInput);
                Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);

                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);
            }

            //processedIds.add("Id=" + csvRecord.get("ID") + " processed with add new line item into subscription");
        }
    }

    private JSONObject getShopifyCountriesJson() {
        Reader shopifyCountries = awsUtils.getDataReader(AwsUtils.APPSTLE_ASSETS, AwsUtils.APPSTLE_ASSETS_S3_KEY);

        JSONObject data = new JSONObject(new JSONTokener(shopifyCountries));
        return data;
    }

    private Map<String, String> getCountryCodeMap(JSONObject shopifyCountries) {
        Map<String, String> countryCodeMap = new HashMap<>();

        Iterator<String> countryNames = shopifyCountries.keys();

        while(countryNames.hasNext()) {
            String countryName = countryNames.next();
            if(shopifyCountries.get(countryName) instanceof JSONObject) {
                countryCodeMap.put(countryName.toUpperCase(), shopifyCountries.getJSONObject(countryName).getString("code").toUpperCase());
            }
        }

        return countryCodeMap;
    }

    private Map<String, Map<String, String>> getProvinceCode(JSONObject shopifyCountries) {
        Map<String, Map<String, String>> provinceCodeMap = new HashMap<>();

        Iterator<String> countryNames = shopifyCountries.keys();
        while (countryNames.hasNext()) {
            String countryName = countryNames.next();
            if (!shopifyCountries.isNull(countryName)) {
                JSONObject countryData = shopifyCountries.optJSONObject(countryName);
                if (countryData != null) {
                    JSONObject provincesData = countryData.optJSONObject("provinces");
                    if (provincesData != null) {
                        Map<String, String> provinceMap = new HashMap<>();
                        Iterator<String> provinceNames = provincesData.keys();
                        while (provinceNames.hasNext()) {
                            String provinceName = provinceNames.next();
                            JSONObject provinceData = provincesData.optJSONObject(provinceName);
                            if (provinceData != null) {
                                String provinceCode = provinceData.optString("code");
                                if (provinceCode != null && !provinceCode.isEmpty()) {
                                    provinceMap.put(provinceName.toUpperCase(), provinceCode.toUpperCase());
                                }
                            }
                        }
                        if (!provinceMap.isEmpty()) {
                            String countryCode = countryData.optString("code");
                            if (countryCode != null && !countryCode.isEmpty()) {
                                provinceCodeMap.put(countryCode.toUpperCase(), provinceMap);
                            }
                        }
                    }
                }
            }
        }
        return provinceCodeMap;
    }

    /*@GetMapping("/product-info-sync-all")
    public void syncAllProducts() {
        log.debug("REST request to get all ProductInfos");
        productInfoService.syncAllProductInfo();
    }*/

    @GetMapping("/update-plan-info")
    public void updatePlanInfoFiledJson() throws IllegalAccessException {
        log.debug("REST request to update plan info filed json");
        List<PlanInfoDTO> planInfoDTOList = planInfoService.findAll();

        for (PlanInfoDTO planInfoDTO : planInfoDTOList) {
            //if (!planInfoDTO.isArchived()) {
            try {
                AdditionalDetailsDTO additionalDetailsDTO = CommonUtils.fromJSONIgnoreUnknownProperty(
                    new TypeReference<>() {
                    },
                    planInfoDTO.getAdditionalDetails()
                );
                if (planInfoDTO.getBasePlan().equals(BasePlan.ENTERPRISE) || planInfoDTO.getBasePlan().equals(BasePlan.ENTERPRISE_ANNUAL)
                    || planInfoDTO.getBasePlan().equals(BasePlan.ENTERPRISE_PLUS) || planInfoDTO.getBasePlan().equals(BasePlan.ENTERPRISE_PLUS_ANNUAL)) {
                    additionalDetailsDTO.setEnableCartWidget(true);
                    additionalDetailsDTO.setEnableAutoSync(true);
                } else {
                    additionalDetailsDTO.setEnableCartWidget(false);
                    additionalDetailsDTO.setEnableAutoSync(false);
                }
                planInfoDTO.setAdditionalDetails(new ObjectMapper().writeValueAsString(additionalDetailsDTO));
                planInfoService.save(planInfoDTO);
            } catch (Exception e) {
                log.error("Error while processing " + planInfoDTO.getName() + " plan :", e.getMessage());
            }
            //}
        }
    }

    @GetMapping("/update-payment-plan-additional-details")
    public void updatePlanAdditionalDetails() throws IllegalAccessException {
        log.debug("REST request to update payment plan additional details");
        List<PaymentPlan> paymentPlans = paymentPlanService.findAll();

        paymentPlans.sort((pp1, pp2) -> pp2.getId().compareTo(pp1.getId()));

        for (PaymentPlan paymentPlan : paymentPlans) {
            try {
                AdditionalDetailsDTO additionalDetailsDTO = CommonUtils.fromJSONIgnoreUnknownProperty(
                    new TypeReference<>() {
                    },
                    paymentPlan.getAdditionalDetails()
                );
                if (additionalDetailsDTO.getEnableCartWidget() == null || additionalDetailsDTO.getEnableAutoSync() == null) {
                    if (paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE) || paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE_ANNUAL)
                        || paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE_PLUS) || paymentPlan.getBasePlan().equals(BasePlan.ENTERPRISE_PLUS_ANNUAL)) {
                        additionalDetailsDTO.setEnableCartWidget(true);
                        additionalDetailsDTO.setEnableAutoSync(true);
                    } else {
                        additionalDetailsDTO.setEnableCartWidget(false);
                        additionalDetailsDTO.setEnableAutoSync(false);
                    }
                    paymentPlan.setAdditionalDetails(new ObjectMapper().writeValueAsString(additionalDetailsDTO));
                    paymentPlanService.save(paymentPlan);
                }
            } catch (Exception e) {
                log.error("Error while processing " + paymentPlan.getName() + " plan for shop" + paymentPlan.getShop() + ":", e.getMessage());
            }
        }
    }

    @GetMapping("/sync-contract-details-json")
    public void updateContractDetailsJSON(@RequestParam("shop") String shop) throws Exception {

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByShop(shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);


        for (SubscriptionContractDetails scd : subscriptionContractDetails) {

            asyncExecutor.execute(() -> {

                try {

                    //            if(scd.getContractDetailsJSON().contains("gid://shopify/Product/6988436963417")
                    //                || scd.getContractDetailsJSON().contains("gid://shopify/Product/6988438241369")) {

                    SubscriptionContractQuery query = SubscriptionContractQuery.builder().id(scd.getGraphSubscriptionContractId()).build();

                    Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(query);

                    List<SubscriptionProductInfo> products = subscriptionContractDetailsService.getProductData(subscriptionContractResponse.getData().get().getSubscriptionContract().get());

                    String scdJson = OBJECT_MAPPER.writeValueAsString(products);

                    scd.setContractDetailsJSON(scdJson);


                    subscriptionContractDetailsRepository.save(scd);
                    //}

                } catch (Exception ex) {

                }
            });
        }

    }



    /*@GetMapping("/update-discount-code")
    public int updateDunningManagementSettings() {

        int updatedShopCount = 0;

        List<Long> subscriptionIds = List.of(11778981923L,11779014691L,11779047459L,11779080227L,11779112995L,11779145763L,11779178531L,11779211299L,11779244067L,11779276835L,11779309603L,11779342371L,11779375139L,11779407907L,11779440675L,11779473443L,11779506211L,11779538979L,11779571747L,11779604515L,11779637283L,11779670051L,11779866659L,11779899427L,11779932195L,11779964963L,11779997731L,11780030499L,11780063267L,11780096035L,11780194339L,11780227107L,11780259875L,11780292643L,11780325411L,11780358179L,11780390947L,11780423715L,11780456483L,11780489251L,11780522019L,11780554787L,11780587555L,11780620323L,11780653091L,11780685859L,11783012387L,11783045155L,11783077923L,11783143459L,11783176227L,11783208995L,11783241763L,11783274531L,11783307299L,11783340067L,11783372835L,11783405603L,11783536675L,11783569443L,11783602211L,11783634979L,11783667747L,11783700515L,11783733283L,11783766051L,11783798819L,11783831587L,11783864355L,11783897123L,11783929891L,11783962659L,11783995427L,11784028195L,11784060963L,11784093731L,11784126499L,11784159267L,11784192035L,11784224803L,11784257571L,11784290339L,11784323107L,11784355875L,11784388643L,11784421411L,11784683555L,11784716323L,11784749091L,11784781859L,11784814627L,11784847395L,11784880163L,11784912931L,11784945699L,11784978467L,11785011235L,11785044003L,11785109539L,11785142307L,11785175075L,11785207843L,11785240611L,11785273379L,11785306147L,11785371683L,11785404451L,11785469987L,11785535523L,11785568291L,11785601059L,11785633827L,11785666595L,11785699363L,11785732131L,11785797667L,11785830435L,11785863203L,11785895971L,11785928739L,11785961507L,11785994275L,11786027043L,11786059811L,11786092579L,11786125347L,11786158115L,11786190883L,11786223651L,11786256419L,11786321955L,11786354723L,11786387491L,11786420259L,11786453027L,11786518563L,11786551331L,11786584099L,11786616867L,11786649635L,11786682403L,11786715171L,11786747939L,11786780707L,11786813475L,11786846243L,11786911779L,11786944547L,11786977315L,11787010083L,11787042851L,11787075619L,11787108387L,11787141155L,11793661987L,12703137827L,12707037219L,12724273187L,12773818403L,12778242083L);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient("genialdaypads.myshopify.com");

        subscriptionIds.forEach(contractId -> {
            System.out.println("Processing: " + contractId);
            SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
            Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = null;
            try {
                optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors()) && optionalQueryResponse.getErrors().get(0).getMessage().equals("Throttled")) {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(optionalQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

           // Check if has discount than remove

            if(!CollectionUtils.isEmpty(subscriptionContractOptional.get().getDiscounts().getEdges())) {
                subscriptionContractOptional.get().getDiscounts().getEdges().forEach(e -> {
                    String discountId = e.getNode().getId();
                    System.out.println("Removing discount: " + discountId);
                    try {
                        subscriptionContractDetailsResource.removeDiscount(contractId, discountId, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            // Add discount
            try {
                subscriptionContractDetailsResource.applyDiscountCode(contractId, "Subscribe15", null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        return updatedShopCount;
    }*/


    @PostMapping("/csv-bulk-update-variant-price")
    public List<String> csvBulkUpdateVariantPrice(@RequestParam(value = "api_key") String apiKey, @RequestBody String csvContent) throws IOException {


        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        List<String> messages = new ArrayList<>();

        Reader subscriptionDataReader = new BufferedReader(new StringReader(csvContent));
        CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Variant ID")) {
            messages.add("'Variant ID' column missing.");
        }

        if (!subscriptionDataCSVParser.getHeaderNames().contains("Price")) {
            messages.add("'Price' column missing.");
        }

        if (messages.size() > 0) {
            return messages;
        }

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        final ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {

            String variantIdStr = subscriptionDataRecord.get("Variant ID");
            String priceStr = subscriptionDataRecord.get("Price");

            if (!NumberUtils.isParsable(variantIdStr) || !NumberUtils.isParsable(priceStr)) {
                messages.add("Invalid variant or price for Variant ID=" + variantIdStr);
                continue;
            }

            Long variantId = Long.parseLong(variantIdStr);
            double price = Double.parseDouble(priceStr);

            log.info("Updating bulk price for variantId={}, price={}", variantId, price);

            List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId));

            for (SubscriptionContractDetails scd : subscriptionContractDetails) {
                asyncExecutor.execute(() -> {

                    try {
                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + scd.getSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);


                        while (!CollectionUtils.isEmpty(optionalQueryResponse.getErrors()) && optionalQueryResponse.getErrors().get(0).getMessage().equals("Throttled")) {
                            Thread.sleep(1000l);
                            optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                        }

                        String graphqlVariantId = PRODUCT_VARIANT_ID_PREFIX + variantId;
                        Optional<SubscriptionContractQuery.Node> optionalNodeToBeUpdated = requireNonNull(optionalQueryResponse.getData())
                            .map(d -> d.getSubscriptionContract()
                                .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                                .map(SubscriptionContractQuery.Lines::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>()).stream()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .filter(j -> j.getVariantId().isPresent() && j.getVariantId().get().equalsIgnoreCase(graphqlVariantId)).findFirst();

                        if (optionalNodeToBeUpdated.isPresent()) {
                            long contractId = scd.getSubscriptionContractId();
                            String lineId = optionalNodeToBeUpdated.get().getId();
                            int quantity = optionalNodeToBeUpdated.get().getQuantity();
                            String sellingPlanName = optionalNodeToBeUpdated.get().getSellingPlanName().orElse(null);

                            subscriptionContractDetailsService.subscriptionContractUpdateLineItem(contractId, shop, quantity, graphqlVariantId, lineId, sellingPlanName, price, true, "/import-csv-bulk-price-update", ActivityLogEventSource.MERCHANT_PORTAL);
                        }

                    } catch (Exception e) {
                        log.info("Error while updating bulk price for variantId={}", variantId);
                        log.info("Exception={}", ExceptionUtils.getStackTrace(e));
                    }

                });
            }

            messages.add("Bulk prise update started for variantId=" + variantId);
        }

        return messages;
    }

    @GetMapping("/cancel-subscriptions-by-product")
    public void cancelSubscriptionsByProductOrVariant(@RequestParam(value = "api_key") String apiKey,
                                                      @RequestParam(value = "productId", required = false) Long productId,
                                                      @RequestParam(value = "variantId", required = false) Long variantId,
                                                      @RequestParam(value = "cancellationFeedback", required = false) String cancellationFeedback,
                                                      @RequestParam(value = "cancellationNote", required = false) String cancellationNote
    ) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (productId == null && variantId == null) {
            throw new BadRequestAlertException("Any one of Product Id or Variant Id is required.", ENTITY_NAME, "");
        }

        final ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        Pageable page = PageRequest.of(0, 5000);

        Page<SubscriptionContractDetails> subscriptionContractDetailsPage;

        if (variantId != null) {
            subscriptionContractDetailsPage = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId), page);
        } else {
            subscriptionContractDetailsPage = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLProductId(productId), page);
        }

        List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsPage.getContent().stream().filter(scd -> !scd.getStatus().equalsIgnoreCase("cancelled")).collect(Collectors.toList());

        for (SubscriptionContractDetails scd : subscriptionContractDetails) {
            try {
                subscriptionContractService.delete(shop, scd.getSubscriptionContractId(), true, cancellationFeedback, cancellationNote, shopifyGraphqlClient, ActivityLogEventSource.MERCHANT_PORTAL);
            } catch (Exception e) {
                log.info("Error while cancelling subscription id={}", scd.getSubscriptionContractId());
                log.info("Exception={}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

    @PostMapping("/product-update-webhook")
    public void productUpdateWebhook(HttpServletRequest request, @RequestParam(value = "api_key") String apiKey, @RequestBody com.et.api.shopify.product.Product product) throws IOException {

        /*String shop = commonUtils.getShopByAPIKey(apiKey).get();
        log.info("Product Update webhook for productId={}", product.getId());

        final ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

        if (BooleanUtils.isTrue(shopInfoDTO.isPriceSyncEnabled())) {

            for (Variant variant : product.getVariants()) {
                final Long variantId = variant.getId();
                final double price = Double.parseDouble(variant.getPrice());

                log.info("Updating bulk price for variantId={}, price={}", variantId, price);
                List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, variantId);

                for (SubscriptionContractDetails scd : subscriptionContractDetails) {

                    try {

                        List<SubscriptionProductInfo> productData = commonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                        }, scd.getContractDetailsJSON());

                        for (SubscriptionProductInfo productDatum : productData) {

                            if (!productDatum.getVariantId().equals(PRODUCT_VARIANT_ID_PREFIX + variantId)) {
                                continue;
                            }

                            double finalPrice = (Double.parseDouble(productDatum.getCurrentPrice()) * scd.getDeliveryPolicyIntervalCount()) / scd.getBillingPolicyIntervalCount();

                            if (finalPrice != price && productDatum.getLineId() != null) {
                                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(scd.getGraphSubscriptionContractId());
                                Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                                if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                                    continue;
                                }

                                // get draft Id from the response
                                Optional<String> optionalDraftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                                    .map(draft -> draft.get().getId());


                                String draftId = optionalDraftId.get();
                                SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();


                                subscriptionLineUpdateInputBuilder.pricingPolicy(null);

                                SubscriptionLineUpdateInput subscriptionLineUpdateInput = null;
                                subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                                    .currentPrice((price * scd.getBillingPolicyIntervalCount()) / scd.getDeliveryPolicyIntervalCount())
                                    .build();


                                SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, productDatum.getLineId(), subscriptionLineUpdateInput);
                                Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                                if (optionalMutationResponse.hasErrors()) {
                                    continue;
                                }

                                List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                if (!optionalMutationResponseUserErrors.isEmpty()) {
                                    continue;
                                }

                                SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                                Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                    .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                                if (optionalDraftCommitResponse.hasErrors()) {
                                    continue;
                                }

                                List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                                if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.info("Error while updating price for variantId={}", variantId);
                        log.info("Exception={}", ExceptionUtils.getStackTrace(e));
                    }

                }

            }
        } else if (BooleanUtils.isTrue(shopInfoDTO.isSkuSyncEnabled())) {
            for (Variant variant : product.getVariants()) {
                Long variantId = variant.getId();
                String sku = variant.getSku();
                log.info("Updating bulk price for variantId={}, SKU={}", variantId, sku);
                List<SubscriptionContractDetails> subscriptionContractDetails = subscriptionContractDetailsRepository.findByProductOrVariantId(shop, variantId);

                for (SubscriptionContractDetails scd : subscriptionContractDetails) {
                    List<SubscriptionProductInfo> productInfos = CommonUtils.fromJSONIgnoreUnknownProperty(
                        new TypeReference<List<SubscriptionProductInfo>>() {
                        },
                        scd.getContractDetailsJSON()
                    );

                    Optional<SubscriptionProductInfo> productInfoOptional = productInfos.stream().filter(pi -> pi.getVariantId().contains(variant + "") && !pi.getSku().equals(sku)).findFirst();

                    if (productInfoOptional.isPresent()) {
                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + scd.getSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> optionalQueryResponse = null;
                        try {
                            optionalQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(optionalQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                            if (subscriptionContractOptional.isPresent()) {

                                List<SubscriptionContractQuery.Node> subscriptionLineItems = subscriptionContractOptional.get().getLines().getEdges().stream().map(SubscriptionContractQuery.Edge::getNode).collect(Collectors.toList());

                                //{prd.node.title} {prd?.node.variantTitle && '-' + prd?.node.variantTitle}
                                Optional<SubscriptionContractQuery.Node> lineToUpdate = subscriptionLineItems.stream()
                                    .filter(s -> s.getVariantId().isPresent() && s.getVariantId().get().equals(PRODUCT_VARIANT_ID_PREFIX + variantId))
                                    .findFirst();

                                if (lineToUpdate.isPresent()) {
                                    subscriptionContractDetailsResource.updateLineItemQuantity(
                                        request,
                                        scd.getSubscriptionContractId(),
                                        lineToUpdate.get().getQuantity(),
                                        lineToUpdate.get().getVariantId().get(),
                                        lineToUpdate.get().getId(),
                                        false);
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                }
            }
        }*/

    }

    @GetMapping("/subscription-contracts-bulk-update-billing-interval")
    public void bulkUpdateBillingInterval(
        @RequestParam("contractIds") String contractIds,
        @RequestParam("intervalCount") int billingIntervalCount,
        @RequestParam("interval") SellingPlanInterval billingInterval,
        HttpServletRequest request) throws Exception {

        if (org.apache.commons.lang3.StringUtils.isNotBlank(contractIds)) {

            log.info("Start sending SQS messages for bulk update intervals for ContractIds: {}, Interval type: {}, Interval: {}",
                contractIds, billingInterval, billingIntervalCount);
            String shop = SecurityUtils.getCurrentUserLogin().get();
            String[] contractIdList = contractIds.split(",");
            for (String contractIdStr : contractIdList) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(contractIdStr)) {
                    JSONObject json = new JSONObject();
                    json.put("contractId", contractIdStr);
                    json.put("intervalCount", billingIntervalCount);
                    json.put("interval", billingInterval);
                    json.put("shop", shop);
                    // awsUtils.send(awsUtils.BULK_UPDATE_INTERVAL_SQS_URLBULK_UPDATE_INTERVAL_SQS_URL, json.toString(), "bulk-update-billing-intervals");

                    // TODO : remove if SQS is used
                    Long contractId = Long.valueOf(contractIdStr);
                    subscriptionContractDetailsService.subscriptionContractsUpdateBillingInterval(contractId, shop, billingIntervalCount, billingInterval, ActivityLogEventSource.MERCHANT_PORTAL);

                } else {
                    log.warn("Contract Id empty, skipping sending SQS");
                }
            }
        } else {
            log.warn("Contract Ids cannot be empty");
            throw new BadRequestAlertException("Contract Ids cannot be Empty", ENTITY_NAME, "idnull");
        }
    }

    @GetMapping("/export-discount-code")
    public int exportSubscriptionContracts(@RequestParam(value = "api_key") String apiKey, @RequestParam(value = "email") String email) {
        File tempFile = null;
        String shop = commonUtils.getShopByAPIKey(apiKey).get();
        try {
            String[] headers = {
                "ID",
                "Status",
                "Customer ID",
                "Customer email",
                "Customer name",
                "Discount Code"
            };
            tempFile = File.createTempFile("Subscriptions-Discount-Code-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
                if (!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)) {
                    ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
                    log.info("subscriptionContractDetailsDTOS.size.=" + subscriptionContractDetailsDTOList.size());
                    for (int j = 0; j < subscriptionContractDetailsDTOList.size(); j++) {
                        try {
                            SubscriptionContractDetailsDTO subscription = subscriptionContractDetailsDTOList.get(j);
                            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractByContract = shopifyGraphqlSubscriptionContractService.getSubscriptionContractRaw(shopifyGraphqlClient, subscription.getSubscriptionContractId());
                            if (subscriptionContractByContract.isPresent()) {
                                SubscriptionContractQuery.SubscriptionContract sc = subscriptionContractByContract.get();

                                String discountCode = "";

                                if (sc.getDiscounts() != null && !CollectionUtils.isEmpty(sc.getDiscounts().getEdges())) {
                                    discountCode = sc.getDiscounts().getEdges().stream()
                                        .map(SubscriptionContractQuery.Edge2::getNode)
                                        .map(SubscriptionContractQuery.Node2::getTitle)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .collect(Collectors.joining(","));
                                }

                                if (StringUtils.hasText(discountCode)) {
                                    csvPrinter.printRecord(
                                        subscription.getSubscriptionContractId(),
                                        subscription.getStatus(),
                                        subscription.getCustomerId(),
                                        subscription.getCustomerEmail(),
                                        subscription.getCustomerName(),
                                        discountCode
                                    );
                                }
                            } else {
                                log.info("Subscription Contract Details is not present. j=" + j + " shop=" + shop);
                            }
                        } catch (Exception ex) {
                            log.error("ex=" + ExceptionUtils.getStackTrace(ex));
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, "Subscription Contract Discount Code Exported", "Check attached csv file for your all subscription list details.", "subscription-support@appstle.com", shop, email);
        return 1;
    }

    @GetMapping("/remove-anchor-day")
    public ResponseEntity<Map<Long, String>> removeAnchorDay(@RequestParam(value = "api_key") String apiKey,
                                                             @RequestParam(required = false) String commaSeparatedContractIds,
                                                             @RequestParam(required = false) boolean allSubscriptions) {

        Map<Long, String> errorMap = new HashMap<>();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if(!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)){
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        }else{
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long contractId : contractIds) {

            try {

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    throw new Exception("Subscription contract not found");
                }
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

                SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                SubscriptionBillingPolicyInput subscriptionBillingPolicyInput = SubscriptionBillingPolicyInput
                    .builder()
                    .intervalCount(subscriptionContract.getBillingPolicy().getIntervalCount())
                    .interval(subscriptionContract.getBillingPolicy().getInterval())
                    .maxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null))
                    .minCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null))
                    .anchors(new ArrayList<>())
                    .build();
                subscriptionDraftInputBuilder.billingPolicy(subscriptionBillingPolicyInput);

                SubscriptionDeliveryPolicyInput subscriptionDeliveryPolicyInput = SubscriptionDeliveryPolicyInput
                    .builder()
                    .intervalCount(subscriptionContract.getDeliveryPolicy().getIntervalCount())
                    .interval(subscriptionContract.getDeliveryPolicy().getInterval())
                    .anchors(new ArrayList<>())
                    .build();

                subscriptionDraftInputBuilder.deliveryPolicy(subscriptionDeliveryPolicyInput);

                SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);
                if (!subscriptionContractUpdateResult.isSuccess()) {
                    errorMap.put(contractId, subscriptionContractUpdateResult.getErrorMessage());
                }
            } catch (Exception e) {
                errorMap.put(contractId, e.getMessage());
            }
        }
        return ResponseEntity.ok(errorMap);
    }

    @GetMapping("/overwrite-anchor-day")
    public ResponseEntity<Map<Long, String>> overwriteAnchorDay(@RequestParam(value = "api_key") String apiKey,
                                                             @RequestParam(required = false) String commaSeparatedContractIds,
                                                             @RequestParam(required = false) boolean allSubscriptions,
                                                             @RequestParam int currentWeekDay,
                                                             @RequestParam int newWeekDay) {

        Map<Long, String> errorMap = new HashMap<>();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if(!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)){
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        }else{
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long contractId : contractIds) {
            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    throw new Exception("Subscription contract not found");
                }
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

                boolean matchFound = subscriptionContract.getBillingPolicy().getAnchors().stream().anyMatch(anchor -> anchor.getType().equals(SellingPlanAnchorType.WEEKDAY) && anchor.getDay() == currentWeekDay);

                if(matchFound) {
                    SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                    SellingPlanAnchorInput sellingPlanAnchorInput = SellingPlanAnchorInput.builder()
                        .cutoffDay(null)
                        .day(newWeekDay)
                        .month(null)
                        .type(SellingPlanAnchorType.WEEKDAY)
                        .build();

                    SubscriptionBillingPolicyInput subscriptionBillingPolicyInput = SubscriptionBillingPolicyInput
                        .builder()
                        .intervalCount(subscriptionContract.getBillingPolicy().getIntervalCount())
                        .interval(subscriptionContract.getBillingPolicy().getInterval())
                        .maxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null))
                        .minCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null))
                        .anchors(List.of(sellingPlanAnchorInput))
                        .build();
                    subscriptionDraftInputBuilder.billingPolicy(subscriptionBillingPolicyInput);

                    SubscriptionDeliveryPolicyInput subscriptionDeliveryPolicyInput = SubscriptionDeliveryPolicyInput
                        .builder()
                        .intervalCount(subscriptionContract.getDeliveryPolicy().getIntervalCount())
                        .interval(subscriptionContract.getDeliveryPolicy().getInterval())
                        .anchors(List.of(sellingPlanAnchorInput))
                        .build();

                    subscriptionDraftInputBuilder.deliveryPolicy(subscriptionDeliveryPolicyInput);

                    SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                    SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);
                    if (!subscriptionContractUpdateResult.isSuccess()) {
                        errorMap.put(contractId, subscriptionContractUpdateResult.getErrorMessage());
                    }
                }
            } catch (Exception e) {
                errorMap.put(contractId, e.getMessage());
            }
        }
        return ResponseEntity.ok(errorMap);
    }

    @PutMapping("/update-anchor-day")
    public ResponseEntity<Map<Long, String>> updateAnchorDay(@RequestParam(value = "api_key") String apiKey,
                                                             @RequestParam(required = false) String commaSeparatedContractIds,
                                                             @RequestParam(required = false) boolean allSubscriptions,
                                                             @RequestParam(required = false) Optional<Integer> cutoffDays,
                                                             @RequestParam int anchorDay,
                                                             @RequestParam(required = false) Optional<Integer> anchorMonth,
                                                             @RequestParam SellingPlanAnchorType anchorType) {

        Map<Long, String> errorMap = new HashMap<>();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)) {
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        if (anchorType.equals(SellingPlanAnchorType.YEARDAY) && anchorMonth.isEmpty()) {
            throw new BadRequestAlertException("Month day cannot be null when Selling plan interval is for year","","");
        }

        List<Long> contractIds = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if (!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)) {
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        } else {
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long contractId : contractIds) {
            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    throw new Exception("Subscription contract not found");
                }
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

                SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                List<SellingPlanAnchorInput> anchors = new ArrayList<>();

                SellingPlanAnchorInput.Builder anchorInputBuilder = SellingPlanAnchorInput.builder();

                if (anchorType.equals(SellingPlanAnchorType.YEARDAY)) {
                    anchorInputBuilder.month(anchorMonth.get());
                }

                anchorInputBuilder.type(anchorType);
                anchorInputBuilder.day(anchorDay);
                anchorInputBuilder.cutoffDay(cutoffDays.orElse(null));

                SellingPlanAnchorInput sellingPlanAnchorInput = anchorInputBuilder.build();
                anchors.add(sellingPlanAnchorInput);

                SubscriptionBillingPolicyInput subscriptionBillingPolicyInput = SubscriptionBillingPolicyInput
                    .builder()
                    .intervalCount(subscriptionContract.getBillingPolicy().getIntervalCount())
                    .interval(subscriptionContract.getBillingPolicy().getInterval())
                    .maxCycles(subscriptionContract.getBillingPolicy().getMaxCycles().orElse(null))
                    .minCycles(subscriptionContract.getBillingPolicy().getMinCycles().orElse(null))
                    .anchors(anchors)
                    .build();
                subscriptionDraftInputBuilder.billingPolicy(subscriptionBillingPolicyInput);

                SubscriptionDeliveryPolicyInput subscriptionDeliveryPolicyInput = SubscriptionDeliveryPolicyInput
                    .builder()
                    .intervalCount(subscriptionContract.getDeliveryPolicy().getIntervalCount())
                    .interval(subscriptionContract.getDeliveryPolicy().getInterval())
                    .anchors(anchors)
                    .build();

                subscriptionDraftInputBuilder.deliveryPolicy(subscriptionDeliveryPolicyInput);

                SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);
                if (!subscriptionContractUpdateResult.isSuccess()) {
                    errorMap.put(contractId, subscriptionContractUpdateResult.getErrorMessage());
                }
            } catch (Exception e) {
                errorMap.put(contractId, e.getMessage());
            }
        }
        return ResponseEntity.ok(errorMap);
    }

    @GetMapping("/refresh-billing-queue")
    public void refreshBillingQueue(@RequestParam(value = "api_key") String apiKey) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();
        log.info("Request to refresh billing attempt queue for shop: {}", shop);
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            try {
                List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatus(subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);
                if (!CollectionUtils.isEmpty(subscriptionBillingAttemptDTOList)) {
                    Optional<Long> nexBillingId = subscriptionBillingAttemptService.findNextBillingIdForContractId(subscriptionContractDetailsDTO.getSubscriptionContractId());
                    subscriptionBillingAttemptService.deleteByStatusAndContractId(BillingAttemptStatus.QUEUED, subscriptionContractDetailsDTO.getSubscriptionContractId());
                    subscriptionBillingAttemptService.deleteByContractIdAndStatusAndBillingDateAfter(subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.SKIPPED, subscriptionContractDetailsDTO.getNextBillingDate());
                    commonUtils.updateQueuedAttempts(
                        subscriptionContractDetailsDTO.getNextBillingDate(),
                        shop,
                        subscriptionContractDetailsDTO.getSubscriptionContractId(),
                        subscriptionContractDetailsDTO.getBillingPolicyIntervalCount(),
                        subscriptionContractDetailsDTO.getBillingPolicyInterval(),
                        subscriptionContractDetailsDTO.getMaxCycles(),
                        nexBillingId.orElse(null));
                }
            } catch (Exception e) {
                log.error("Error occurred while refreshing billing attempt queue for shop {}, contractId {}", shop, subscriptionContractDetailsDTO.getSubscriptionContractId());
            }
        }
    }

    @GetMapping("/refresh-line-info")
    public ResponseEntity<Void> refreshLineInfo(
        @RequestParam("api_key") String apiKey,
        @RequestParam(required = false) String commaSeparatedContractIds,
        @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions,
        @RequestParam Long variantId) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByProductOrVariantId(shop, ShopifyGraphQLUtils.getGraphQLVariantId(variantId));
            if(!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)){
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        }else{
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        for(Long contractId : contractIds){
            subscriptionContractDetailsService.refreshLineInfo(shop, contractId, variantId);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/update-country-info")
    public void updateAllShopsCountryInfo(){
        List<ShopInfoDTO> shopInfoDTOList = shopInfoService.findAllByCountryCodeIsNullAndCountryNameIsNull();
        for(ShopInfoDTO shopInfoDTO : shopInfoDTOList){
            asyncExecutor.execute(() -> {
                try {
                    ShopifyAPI api = commonUtils.prepareShopifyResClient(shopInfoDTO.getShop());

                    Shop shopDetails = api.getShopInfo().getShop();

                    shopInfoDTO.setCountryCode(shopDetails.getCountryCode());
                    shopInfoDTO.setCountryName(shopDetails.getCountryName());

                    shopInfoService.save(shopInfoDTO);
                }catch (Exception e){
                    log.error("Exception occurred while updating county info for shop: {}, error:{}", shopInfoDTO.getShop(), ExceptionUtils.getStackTrace(e));
                }
            });
        }
    }

    @GetMapping("/get-contracts-with-price-discrepancy")
    public ResponseEntity<Void> getContractWithPriceDiscrepancy(@RequestParam("api_key") String apiKey, @RequestParam String email) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);
        asyncExecutor.execute(() -> {
                File tempFile = null;
                List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

                Map<String, com.et.api.shopify.product.Product> shopifyProductData = new HashMap<>();

                try {
                    String[] headers = {
                        "Contract id",
                        "Line item id",
                        "Variant ID",
                        "Shopify price",
                        "Existing price",
                        "Pricing Policy 1",
                        "Pricing Policy 2",
                        "Price discrepancy"

                    };
                    tempFile = File.createTempFile("Price-Discrepancies-Export", ".csv");
                    Writer writer = Files.newBufferedWriter(tempFile.toPath());

                    CSVFormat csvFormat = CSVFormat.DEFAULT
                        .withAutoFlush(true)
                        .withHeader(headers);
                    try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

                        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {

                            if (scd.getStatus().equalsIgnoreCase("cancelled")) {
                                continue;
                            }
                            try {
                                List<SubscriptionProductInfo> productDataList = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                                }, scd.getContractDetailsJSON());

                                for (SubscriptionProductInfo productData : productDataList) {

                                    com.et.api.shopify.product.Product product;
                                    if (shopifyProductData.containsKey(productData.getProductId())) {
                                        product = shopifyProductData.get(productData.getProductId());
                                    } else {
                                        GetProductResponse productsResponse = shopifyAPI.getProduct(ShopifyGraphQLUtils.getProductId(productData.getProductId()));
                                        product = productsResponse.getProduct();
                                        shopifyProductData.put(productData.getProductId(), product);
                                    }

                                    Variant variant = product.getVariants().stream().filter(v -> v.getId().equals(ShopifyGraphQLUtils.getVariantId(productData.getVariantId()))).findFirst().orElse(null);

                                    if (Objects.isNull(variant)) {
                                        continue;
                                    }

                                    String variantId = productData.getVariantId();

                                    if (org.apache.commons.lang3.StringUtils.isAllBlank(productData.getBasePrice(), productData.getCurrentPrice())) {
                                        log.info("Not updating price as base price and current price both not stored in database for contractId={}, variantId={}, lineId={}", scd.getSubscriptionContractId(), variantId, productData.getLineId());
                                        continue;
                                    }

                                    if (org.apache.commons.lang3.StringUtils.isBlank(productData.getLineId())) {
                                        log.info("Not updating price as line id not stored in database for contractId={}, variantId={}, lineId={}", scd.getSubscriptionContractId(), variantId, productData.getLineId());
                                        continue;
                                    }

                                    double fulfillmentCountMultiplier = scd.getBillingPolicyIntervalCount() / scd.getDeliveryPolicyIntervalCount();

                                    double existingBasePrice = org.apache.commons.lang3.StringUtils.isNotBlank(productData.getBasePrice())
                                        ? Double.parseDouble(productData.getBasePrice())
                                        : (Double.parseDouble(productData.getCurrentPrice()) / fulfillmentCountMultiplier);


                                    double price = Double.parseDouble(variant.getPrice());

                                    String currencyCode = scd.getCurrencyCode();

                                    if (!CollectionUtils.isEmpty(variant.getPresentmentPrices())) {
                                        Optional<Double> presentmentPrice = variant.getPresentmentPrices().stream()
                                            .map(PresentmentPrice::getPrice)
                                            .filter(p -> p.getCurrencyCode().equalsIgnoreCase(currencyCode))
                                            .map(Price::getAmount)
                                            .map(Double::parseDouble)
                                            .findFirst();

                                        if (presentmentPrice.isPresent()) {
                                            price = presentmentPrice.get();
                                        }
                                    }
                                    String pricingPolicy1 = "";
                                    String pricingPolicy2 = "";

                                    PricingPolicy pricingPolicy = productData.getPricingPolicy();
                                    if (pricingPolicy != null && !CollectionUtils.isEmpty(pricingPolicy.getCycleDiscounts())) {

                                        for (int i = 0; i < pricingPolicy.getCycleDiscounts().size(); i++) {
                                            if(i == 0){
                                                pricingPolicy1 = OBJECT_MAPPER.writeValueAsString(pricingPolicy.getCycleDiscounts().get(i));
                                            }
                                            if(i == 1){
                                                pricingPolicy2 = OBJECT_MAPPER.writeValueAsString(pricingPolicy.getCycleDiscounts().get(i));
                                            }
                                        }
                                    }

                                    double newBasePrice = price;
                                    Boolean discrepancy = existingBasePrice != newBasePrice;

                                    csvPrinter.printRecord(scd.getSubscriptionContractId(),
                                    productData.getLineId(),
                                    variantId,
                                    newBasePrice,
                                    existingBasePrice,
                                    pricingPolicy1,
                                    pricingPolicy2,
                                    discrepancy);
                                }
                            } catch (Exception e) {
                                log.info("Error while updating price for contract={}", scd.getSubscriptionContractId());
                                log.info("Exception={}", ExceptionUtils.getStackTrace(e));
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            mailgunService.sendEmailWithAttachment(tempFile, "Contracts with price discrepancy", "Check attached csv file for all subscription contracts that has price discrepancy", "subscription-support@appstle.com", shop, email);
        });
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "When process is done you will receive an email with csv file attachment on email id " + email, "")).build();
    }

    @GetMapping("/sync-contract-price")
    public void contractPriceSyncForShop(@RequestParam("api_key") String apiKey,
                                         @RequestParam(required = false) String commaSeparatedContractIds,
                                         @RequestParam(required = false) Boolean allSubscriptions) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
             subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopifyAPI shopifyAPI = commonUtils.prepareShopifyResClient(shop);

        Map<String, com.et.api.shopify.product.Product> shopifyProductData = new HashMap<>();

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {

            try {

                if(scd.getStatus().equalsIgnoreCase("cancelled")){
                    continue;
                }

                List<SubscriptionProductInfo> productDataList = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                }, scd.getContractDetailsJSON());

                for (SubscriptionProductInfo productData : productDataList) {

                    com.et.api.shopify.product.Product product;
                    if(shopifyProductData.containsKey(productData.getProductId())){
                        product = shopifyProductData.get(productData.getProductId());
                    }else{
                        GetProductResponse productsResponse = shopifyAPI.getProduct(ShopifyGraphQLUtils.getProductId(productData.getProductId()));
                        product = productsResponse.getProduct();
                        shopifyProductData.put(productData.getProductId(), product);
                    }

                    Variant variant = product.getVariants().stream().filter(v -> v.getId().equals(ShopifyGraphQLUtils.getVariantId(productData.getVariantId()))).findFirst().orElse(null);

                    if (Objects.isNull(variant)) {
                        continue;
                    }

                    String variantId = productData.getVariantId();

                    if (org.apache.commons.lang3.StringUtils.isAllBlank(productData.getBasePrice(), productData.getCurrentPrice())) {
                        log.info("Not updating price as base price and current price both not stored in database for contractId={}, variantId={}, lineId={}", scd.getSubscriptionContractId(), variantId, productData.getLineId());
                        continue;
                    }

                    if (org.apache.commons.lang3.StringUtils.isBlank(productData.getLineId())) {
                        log.info("Not updating price as line id not stored in database for contractId={}, variantId={}, lineId={}", scd.getSubscriptionContractId(), variantId, productData.getLineId());
                        continue;
                    }

                    double fulfillmentCountMultiplier = scd.getBillingPolicyIntervalCount() / scd.getDeliveryPolicyIntervalCount();

                    double existingBasePrice = org.apache.commons.lang3.StringUtils.isNotBlank(productData.getBasePrice())
                        ? Double.parseDouble(productData.getBasePrice())
                        : (Double.parseDouble(productData.getCurrentPrice()) / fulfillmentCountMultiplier);


                    double price = Double.parseDouble(variant.getPrice());

                    String currencyCode = scd.getCurrencyCode();

                    if(!CollectionUtils.isEmpty(variant.getPresentmentPrices())) {
                        Optional<Double> presentmentPrice = variant.getPresentmentPrices().stream()
                            .map(PresentmentPrice::getPrice)
                            .filter(p -> p.getCurrencyCode().equalsIgnoreCase(currencyCode))
                            .map(Price::getAmount)
                            .map(Double::parseDouble)
                            .findFirst();

                        if(presentmentPrice.isPresent()) {
                            price = presentmentPrice.get();
                        }
                    }

                    double newBasePrice = price;

                    if (existingBasePrice != newBasePrice) {
                        log.info("Updating price for variantId={}, price={}, currencyCode={}", variantId, price, currencyCode);

                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                        // Update price
                        double computedPrice = newBasePrice * fulfillmentCountMultiplier;

                        PricingPolicy pricingPolicy = productData.getPricingPolicy();
                        if (pricingPolicy != null && !CollectionUtils.isEmpty(pricingPolicy.getCycleDiscounts())) {

                            SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder();
                            pricingPolicyInputBuilder.basePrice(newBasePrice);

                            int totalCycles = 1;
                            List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, scd.getSubscriptionContractId(), BillingAttemptStatus.SUCCESS);
                            totalCycles = totalCycles + subscriptionBillingAttempts.size();

                            List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscountInputList = new ArrayList<>();

                            for (CycleDiscount cycleDiscount : pricingPolicy.getCycleDiscounts()) {
                                SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountInput = buildCycleDiscountInput(newBasePrice, cycleDiscount.getAfterCycle(), DiscountTypeUnit.valueOf(cycleDiscount.getAdjustmentType()), Double.parseDouble(cycleDiscount.getAdjustmentValue()), fulfillmentCountMultiplier);
                                cycleDiscountInputList.add(cycleDiscountInput);
                                if (totalCycles >= cycleDiscount.getAfterCycle()) {
                                    computedPrice = Double.parseDouble(cycleDiscountInput.computedPrice().toString());
                                }
                            }

                            pricingPolicyInputBuilder.cycleDiscounts(cycleDiscountInputList);
                            subscriptionLineUpdateInputBuilder.pricingPolicy(pricingPolicyInputBuilder.build());
                        }

                        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(scd.getGraphSubscriptionContractId());
                        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                        if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                            continue;
                        }

                        // get draft Id from the response
                        Optional<String> optionalDraftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                            .map(draft -> draft.get().getId());


                        String draftId = optionalDraftId.get();

                        SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder
                            .currentPrice(computedPrice)
                            .build();

                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, productData.getLineId(), subscriptionLineUpdateInput);
                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                        if (optionalMutationResponse.hasErrors()) {
                            continue;
                        }

                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                            continue;
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (optionalDraftCommitResponse.hasErrors()) {
                            continue;
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            continue;
                        }

                        Map<String, Object> map = new HashMap<>();
                        map.put("oldPrice", existingBasePrice);
                        map.put("newPrice", newBasePrice);
                        map.put("variantId", PRODUCT_VARIANT_ID_PREFIX + variantId);
                        commonUtils.writeActivityLog(shop, scd.getSubscriptionContractId(), ActivityLogEntityType.SUBSCRIPTION_CONTRACT_DETAILS, ActivityLogEventSource.SYSTEM_EVENT, ActivityLogEventType.PRICE_CHANGE_SYNC, ActivityLogStatus.SUCCESS, map);

                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(scd.getGraphSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                        Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                        if (subscriptionContractOptional.isPresent()) {
                            List<SubscriptionProductInfo> products = subscriptionContractDetailsService.getProductData(subscriptionContractOptional.get());
                            scd.setContractDetailsJSON(OBJECT_MAPPER.writeValueAsString(products));

                            subscriptionContractDetailsService.save(scd);
                        }

                    }
                }
            } catch (Exception e) {
                log.info("Error while updating price for contract={}", scd.getSubscriptionContractId());
                log.info("Exception={}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

    @GetMapping("/sync-contract-product-details")
    @ApiOperation("API to sync contract product details like SKU, Title etc. from Shopify")
    public void syncContractProductDetailsForShop(@RequestParam("api_key") String apiKey,
                                                  @RequestParam(required = false) String commaSeparatedContractIds,
                                                  @RequestParam(required = false) Boolean allSubscriptions) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for(SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(scd.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).get().getSubscriptionContract().get();

                List<SubscriptionContractQuery.Node> lines = subscriptionContract.getLines().getEdges().stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(n -> n.getVariantId().isPresent())
                    .collect(Collectors.toList());

                if(!CollectionUtils.isEmpty(lines)) {
                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(scd.getGraphSubscriptionContractId());
                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                    String draftId = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId())
                        .get();

                    for (SubscriptionContractQuery.Node line : lines) {
                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                        subscriptionLineUpdateInputBuilder.productVariantId(line.getVariantId().get());

                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, line.getId(), subscriptionLineUpdateInputBuilder.build());
                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                        if (optionalMutationResponse.hasErrors()) {
                            throw new BadRequestAlertException(optionalMutationResponse.getErrors().get(0).getMessage(), "", "");
                        }

                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                            throw new BadRequestAlertException(optionalMutationResponseUserErrors.get(0).getMessage(), "", "");
                        }
                    }

                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(optionalDraftCommitResponse.getErrors().get(0).getMessage(), "", "");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponseUserErrors.get(0).getMessage()), "", "");
                    }
                }

                log.info("Product details updated for shop={} contract={}, Exception={}", shop, scd.getSubscriptionContractId());

            } catch (Exception e) {
                log.info("Error while updating product details for shop={} contract={}, Exception={}", shop, scd.getSubscriptionContractId(), ExceptionUtils.getStackTrace(e));
            }
        }

        log.info("Product details updated for all provided contracts. shop={}", shop);
    }

    @GetMapping("/sync-delivery-price")
    public void contractDeliveryPriceSyncForShop(@RequestParam("api_key") String apiKey,
                                         @RequestParam(required = false) String commaSeparatedContractIds,
                                         @RequestParam(required = false) Boolean allSubscriptions) {


        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        } else {
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {

            if(scd.getStatus().equalsIgnoreCase("cancelled")){
                continue;
            }

            try {
                commonUtils.mayBeUpdateShippingPrice(scd.getSubscriptionContractId(), shop, shopifyGraphqlClient);
            } catch (Exception e) {
                log.info("Error while updating delivery price for contract={}, ex={}", scd.getSubscriptionContractId(), ExceptionUtils.getStackTrace(e));
            }
        }

    }

    private SubscriptionPricingPolicyCycleDiscountsInput buildCycleDiscountInput(Double basePrice, Integer afterCycle, DiscountTypeUnit discountTypeUnit, Double discountOffer, double fulfillmentCountMultiplier) {
        if (afterCycle == null) {
            return null;
        }

        DecimalFormat df = new DecimalFormat("#.##");

        SubscriptionPricingPolicyCycleDiscountsInput.Builder subscriptionPricingPolicyCycleDiscountsInputBuilder =
            SubscriptionPricingPolicyCycleDiscountsInput.builder();
        subscriptionPricingPolicyCycleDiscountsInputBuilder.afterCycle(afterCycle);

        if (discountTypeUnit == DiscountTypeUnit.PERCENTAGE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PERCENTAGE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().percentage(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, basePrice * ((100d - discountOffer) / 100) * fulfillmentCountMultiplier)));
        } else if (discountTypeUnit == DiscountTypeUnit.FIXED) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.FIXED_AMOUNT);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, (basePrice * fulfillmentCountMultiplier) - discountOffer)));
        } else if (discountTypeUnit == DiscountTypeUnit.PRICE) {
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentType(SellingPlanPricingPolicyAdjustmentType.PRICE);
            subscriptionPricingPolicyCycleDiscountsInputBuilder.adjustmentValue(SellingPlanPricingPolicyValueInput.builder().fixedValue(discountOffer).build());
            subscriptionPricingPolicyCycleDiscountsInputBuilder.computedPrice(df.format(Math.max(0, (discountOffer * fulfillmentCountMultiplier))));
        }

        return subscriptionPricingPolicyCycleDiscountsInputBuilder.build();
    }



    @GetMapping("/standard-to-local-shipping")
    public ResponseEntity<List<String>> convertStandardToLocalShipping(@RequestParam(value = "contractIds", required = false) String contractIds,
                                                                       @RequestParam(value = "allSubscriptions", required = false) Boolean allSubscriptions) throws Exception {

        String shop =  SecurityUtils.getCurrentUserLogin().get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<Long> contractIdList = getBulkContractIds(shop, contractIds, allSubscriptions);

        List<String> errorList = new ArrayList<>();
        for(Long contractId : contractIdList) {
            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponseOptional = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(subscriptionContractResponseOptional.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    throw new BadRequestAlertException("Subscription contract not found", "", "");
                }

                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

                if (subscriptionContract.getDeliveryMethod().isPresent() &&
                    subscriptionContract.getDeliveryMethod().get() instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) {

                    Optional<SubscriptionContractQuery.Address> address = Optional.ofNullable((SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping)
                            subscriptionContract.getDeliveryMethod().orElse(null))
                        .map(SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping::getAddress);

                    if (address.isPresent()) {
                        MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();
                        addressBuilder.firstName(address.map(SubscriptionContractQuery.Address::getFirstName).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.lastName(address.map(SubscriptionContractQuery.Address::getLastName).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.address1(address.map(SubscriptionContractQuery.Address::getAddress1).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.address2(address.map(SubscriptionContractQuery.Address::getAddress2).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.city(address.map(SubscriptionContractQuery.Address::getCity).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.provinceCode(address.map(SubscriptionContractQuery.Address::getProvinceCode).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.countryCode(address.map(SubscriptionContractQuery.Address::getCountryCode).filter(Optional::isPresent).map(Optional::get).orElse(null));
                        addressBuilder.zip(address.map(SubscriptionContractQuery.Address::getZip).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.phone(address.map(SubscriptionContractQuery.Address::getPhone).filter(Optional::isPresent).map(Optional::get).orElse(""));
                        addressBuilder.company(address.map(SubscriptionContractQuery.Address::getCompany).filter(Optional::isPresent).map(Optional::get).orElse(""));

                        SubscriptionDeliveryMethodLocalDeliveryInput.Builder localDeliveryBuilder = SubscriptionDeliveryMethodLocalDeliveryInput.builder();
                        localDeliveryBuilder.address(addressBuilder.build());

                        SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
                        deliveryMethodBuilder.localDelivery(localDeliveryBuilder.build());

                        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                        subscriptionDraftInputBuilder.deliveryMethod(deliveryMethodBuilder.build());

                        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

                        if (!subscriptionContractUpdateResult.isSuccess()) {
                            throw new BadRequestAlertException(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()), "", "");
                        }
                    } else {
                        throw new BadRequestAlertException("Address not found", "", "");
                    }
                } else {
                    throw new BadRequestAlertException("Current Delivery Method is not standard shipping", "", "");
                }
            }catch (Exception e){
                errorList.add("ex = " + e.getMessage() + ", ID = " + contractId);
            }
        }
        return ResponseEntity.ok().body(errorList);
    }



    @PutMapping(value = {"/subscription-contracts-update-shipping-address-all"})
    public ResponseEntity updateShippingAddressAll(
        @RequestParam("subscriptionDataS3Key") String subscriptionDataS3Key,
        @RequestParam(value = "api_key") String apiKey,
        HttpServletRequest request)  {

        String shopName = null;

        boolean isExternal = false;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(apiKey)) {
            Optional<ShopInfoDTO> shopInfoOptional = shopInfoService.findByApiKey(apiKey);
            if (shopInfoOptional.isPresent()) {
                shopName = shopInfoOptional.get().getShop();
                isExternal = true;
            }
        }

        if (org.apache.commons.lang3.StringUtils.isBlank(shopName)) {
            shopName = SecurityUtils.getCurrentUserLogin().get();
        }
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");
        log.info("{} REST request to update shipping address in bulk for shop : {}", RequestURL, shopName);

        Reader subscriptionDataReader = awsUtils.getDataReaderForMigration(subscriptionDataS3Key);
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();
        try {
            CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);
            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shopName);
            for(CSVRecord subscriptionDataRecord: subscriptionDataCSVParser) {
                log.info("record number : {}", subscriptionDataRecord.getRecordNumber());
                ChangeShippingAddressVM changeShippingAddressVM = new ChangeShippingAddressVM();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Delivery zip"))) {

                    Long contractId = Long.parseLong(subscriptionDataRecord.get("ID"));
                    changeShippingAddressVM.setFirstName(subscriptionDataRecord.get("Delivery first name"));
                    changeShippingAddressVM.setLastName(subscriptionDataRecord.get("Delivery last name"));
                    changeShippingAddressVM.setAddress1(subscriptionDataRecord.get("Delivery address 1"));
                    changeShippingAddressVM.setAddress2(subscriptionDataRecord.get("Delivery address 2"));
                    changeShippingAddressVM.setCity(Optional.ofNullable(subscriptionDataRecord.get("Delivery city")).orElse(""));
                    changeShippingAddressVM.setProvinceCode(subscriptionDataRecord.get("Delivery province"));
                    CountryCode deliveryCountryCode = CountryCode.valueOf(subscriptionDataRecord.get("Delivery country").toUpperCase());
                    changeShippingAddressVM.setCountryCode(deliveryCountryCode.toString());
                    String deliveryZip = subscriptionDataRecord.get("Delivery zip");

                    if (deliveryCountryCode.equals(CountryCode.US)) {
                        if (!org.springframework.util.StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                            deliveryZip = "0" + deliveryZip;
                        }
                    }

                    changeShippingAddressVM.setZip(deliveryZip);
                    changeShippingAddressVM.setPhone(subscriptionDataRecord.get("Delivery phone"));

                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                    Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                    SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(subscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract).orElse(null);

                    if(Objects.nonNull(subscriptionContract)){
                        if(subscriptionContract.getDeliveryMethod().isPresent()) {
                            SubscriptionContractQuery.DeliveryMethod deliveryMethod = subscriptionContract.getDeliveryMethod().get();
                            if (deliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodShipping) {
                                changeShippingAddressVM.setMethodType("SHIPPING");
                            }else if (deliveryMethod instanceof SubscriptionContractQuery.AsSubscriptionDeliveryMethodLocalDelivery) {
                                changeShippingAddressVM.setMethodType("LOCAL");
                            }
                            if(org.apache.commons.lang3.StringUtils.isNotBlank(changeShippingAddressVM.getMethodType())) {
                                subscriptionContractDetailsResource.subscriptionContractUpdateShippingAddressAll(contractId, shopName, changeShippingAddressVM, isExternal ? ActivityLogEventSource.CUSTOMER_PORTAL : ActivityLogEventSource.MERCHANT_PORTAL);
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            log.error("Error appear while updating shipping adreess : {}", e.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    private List<Long> getBulkContractIds(String shop, String contractIds, Boolean allSubscriptions) {
        List<Long> contractIdList;
        if (org.apache.commons.lang3.StringUtils.isBlank(contractIds)) {
            if (BooleanUtils.isTrue(allSubscriptions)) {
                contractIdList = subscriptionContractDetailsService.findSubscriptionContractIdsByShop(shop);
            } else {
                throw new BadRequestAlertException("If All subs flag is false or empty, contractIds cannot be empty", ENTITY_NAME, "idnull");
            }
        } else {
            contractIdList = Arrays.stream(contractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        // Remove duplicate
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

        return contractIdList;
    }

//    @GetMapping("/attempt-billing")
//    public ResponseEntity<List<String>> attemptBilling(@RequestParam String commaSeparatedContractIds){
//
//        List<String> errorList = new ArrayList<>();
//        String shop = commonUtils.getShop();
//
//        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds)){
//            throw new BadRequestAlertException("Contract Ids cant be empty", "", "idnull");
//        }
//
//        List<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
//
//        for(Long contractId : contractIds){
//            try {
//
//                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId);
//                if(subscriptionContractDetailsDTO.isPresent() && subscriptionContractDetailsDTO.get().getStatus().equalsIgnoreCase("active")) {
//                    List<SubscriptionBillingAttemptDTO> queuedSubscriptionBillingAttempts = subscriptionBillingAttemptService.findByContractIdAndStatus(contractId, BillingAttemptStatus.QUEUED);
//                    Optional<SubscriptionBillingAttemptDTO> upcomingOrderOpt = queuedSubscriptionBillingAttempts.stream().min(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));
//                    if (upcomingOrderOpt.isPresent()) {
//                        SubscriptionBillingAttemptDTO upcomingOrder = upcomingOrderOpt.get();
//                        if(upcomingOrder.getBillingDate().isBefore(ZonedDateTime.of(2023,1,1,00,00,00,000,ZoneId.of("UTC")))){
//                            subscriptionBillingAttemptService.attemptBilling(shop, upcomingOrder.getId(), ActivityLogEventSource.MERCHANT_EXTERNAL_API);
//                        }
//                    }
//                }
//            }catch (Exception e){
//                errorList.add("Error= " + e.getMessage() + ", ID= " + contractId);
//            }
//        }
//        return ResponseEntity.ok(errorList);
//    }

    /*@PostMapping("/update-contract-data-from-file")
    public ResponseEntity<List<String>> updateContractDataFromFile(@RequestParam String shop, @RequestParam String fileS3key) throws Exception{
        Reader variantDataReader = awsUtils.getDataReaderForMigration(fileS3key);
        Reader subscriptionDataReader = awsUtils.getDataReaderForMigration(fileS3key);

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<String> errorList = new ArrayList<>();
        CSVParser subscriptionVariantDataCSVParser = new CSVParser(variantDataReader, csvFormat);
        CSVParser subscriptionDataCSVParser = new CSVParser(subscriptionDataReader, csvFormat);

        List<String> processedContracts = new ArrayList<>();
        Map<String, List<VariantInfoDTO>> contractVariants = new HashMap<>();

        for (CSVRecord subscriptionVariantDataRecord : subscriptionVariantDataCSVParser) {
            String contractIdString = subscriptionVariantDataRecord.get("ID");
            String variantId = subscriptionVariantDataRecord.get("Variant ID");
            String variantPrice = subscriptionVariantDataRecord.get("Variant price");
            List<VariantInfoDTO> variantInfoDTOList = contractVariants.getOrDefault(contractIdString, new ArrayList<>());
            VariantInfoDTO variantInfoDTO = new VariantInfoDTO();
            variantInfoDTO.setId(Long.parseLong(variantId));
            variantInfoDTO.setVariantPrice(variantPrice);
            variantInfoDTOList.add(variantInfoDTO);
            contractVariants.put(contractIdString, variantInfoDTOList);
        }

        for (CSVRecord subscriptionDataRecord : subscriptionDataCSVParser) {
            try {
                String contractIdString = subscriptionDataRecord.get("ID");

                if(!processedContracts.contains(contractIdString)) {

                    String nextOrderDate = subscriptionDataRecord.get("Next order date");

                    SubscriptionBillingPolicyInput.Builder billingPolicyBuilder = SubscriptionBillingPolicyInput.builder();

                    String billingIntervalType = subscriptionDataRecord.get("Billing interval type");

                    if (org.apache.commons.lang3.StringUtils.isNotBlank(billingIntervalType) && billingIntervalType.toLowerCase().equals("months")) {
                        billingIntervalType = "month";
                    }

                    billingPolicyBuilder.interval(getIntervalType(billingIntervalType));
                    int billingIntervalCount = Integer.parseInt(subscriptionDataRecord.get("Billing interval count"));
                    billingPolicyBuilder.intervalCount(billingIntervalCount);
                    String nextBillingDate = buildNextBillingDate(nextOrderDate, billingIntervalCount, billingIntervalType);

                    SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                    if (nextBillingDate != null) {
                        subscriptionDraftInputBuilder.nextBillingDate(nextBillingDate);
                    }


                    SubscriptionBillingPolicyInput billingPolicy = billingPolicyBuilder.build();
                    subscriptionDraftInputBuilder.billingPolicy(billingPolicy);

                    SubscriptionDeliveryPolicyInput.Builder deliveryPolicyBuilder = SubscriptionDeliveryPolicyInput.builder();

                    String deliveryIntervalType = subscriptionDataRecord.get("Delivery interval type");

                    deliveryPolicyBuilder.interval(getIntervalType(deliveryIntervalType));

                    int deliveryIntervalCount = Integer.parseInt(subscriptionDataRecord.get("Delivery interval count"));
                    deliveryPolicyBuilder.intervalCount(deliveryIntervalCount);

                    SubscriptionDeliveryPolicyInput deliveryPolicy = deliveryPolicyBuilder.build();
                    subscriptionDraftInputBuilder.deliveryPolicy(deliveryPolicy);

                    if (subscriptionDataRecord.isSet("Delivery zip")
                        && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionDataRecord.get("Delivery zip"))) {
                        SubscriptionDeliveryMethodInput.Builder deliveryMethodBuilder = SubscriptionDeliveryMethodInput.builder();
                        SubscriptionDeliveryMethodShippingInput.Builder shippingBuilder = SubscriptionDeliveryMethodShippingInput.builder();
                        MailingAddressInput.Builder addressBuilder = MailingAddressInput.builder();

                        addressBuilder.firstName(subscriptionDataRecord.get("Delivery first name"));
                        addressBuilder.lastName(subscriptionDataRecord.get("Delivery last name"));
                        addressBuilder.address1(subscriptionDataRecord.get("Delivery address 1"));
                        addressBuilder.address2(subscriptionDataRecord.get("Delivery address 2"));
                        addressBuilder.city(Optional.ofNullable(subscriptionDataRecord.get("Delivery city")).orElse(""));
                        addressBuilder.provinceCode(subscriptionDataRecord.get("Delivery province code"));
                        CountryCode deliveryCountryCode = CountryCode.valueOf(subscriptionDataRecord.get("Delivery country").toUpperCase());
                        addressBuilder.countryCode(deliveryCountryCode);

                        String deliveryZip = subscriptionDataRecord.get("Delivery zip");

                        if (deliveryCountryCode.equals(CountryCode.US)) {
                            if (!StringUtils.isEmpty(deliveryZip) && deliveryZip.length() == 4) {
                                deliveryZip = "0" + deliveryZip;
                            }
                        }

                        addressBuilder.zip(deliveryZip);
                        addressBuilder.phone(subscriptionDataRecord.get("Delivery phone"));

                        MailingAddressInput address = addressBuilder.build();
                        shippingBuilder.address(address);
                        SubscriptionDeliveryMethodShippingInput shipping = shippingBuilder.build();
                        deliveryMethodBuilder.shipping(shipping);

                        SubscriptionDeliveryMethodInput deliveryMethod = deliveryMethodBuilder.build();
                        subscriptionDraftInputBuilder.deliveryMethod(deliveryMethod);
                    }

                    String deliveryPriceAmount = Optional.ofNullable(subscriptionDataRecord.get("Shipping Price")).map(String::trim).orElse("");
                    double deliveryPrice = Double.parseDouble(deliveryPriceAmount.equals("") ? "0" : deliveryPriceAmount);
                    subscriptionDraftInputBuilder.deliveryPrice(deliveryPrice);

                    SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                    Long contractId = Long.parseLong(contractIdString);

                    SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

                    Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                    long countOfErrors = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors).orElse(new ArrayList<>()).stream().map(SubscriptionContractUpdateMutation.UserError::getMessage).peek(message -> errorList.add("Update failed for contract: " + contractId + " line: " + subscriptionDataRecord.getRecordNumber() + " error= " + message)).count();

                    if (countOfErrors == 0) {
                        // get draft Id from the response
                        Optional<String> optionalDraftId = optionalResponse.getData().flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate).map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft).map(draft -> draft.get().getId());

                        if (optionalDraftId.isPresent()) {

                            if(contractIdString.equalsIgnoreCase("9283141800")){
                                System.out.println("hello");
                            }

                            Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractService.findSubscriptionContractByContractId(contractId, shop);

                            List<String> oldLines = subscriptionContract.get().getLines().getEdges().stream()
                                .map(SubscriptionContractQuery.Edge::getNode)
                                .map(SubscriptionContractQuery.Node::getId).collect(Collectors.toList());

                            List<VariantInfoDTO> variantInfoDTOList = contractVariants.getOrDefault(contractId.toString(), new ArrayList<>());
                            for (VariantInfoDTO variantInfoDTO : variantInfoDTOList) {
                                SubscriptionLineInput subscriptionLineInput = SubscriptionLineInput.builder().currentPrice(Double.parseDouble(variantInfoDTO.getVariantPrice())).productVariantId(PRODUCT_VARIANT_ID_PREFIX + variantInfoDTO.getId()).quantity(1).build();

                                SubscriptionDraftLineAddMutation subscriptionDraftLineAddMutation = new SubscriptionDraftLineAddMutation(optionalDraftId.get(), subscriptionLineInput);
                                Response<Optional<SubscriptionDraftLineAddMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineAddMutation);
                            }

                            for (String oldLineId : oldLines) {
                                SubscriptionDraftLineRemoveMutation subscriptionDraftLineRemoveMutation = new SubscriptionDraftLineRemoveMutation(optionalDraftId.get(), oldLineId);
                                Response<Optional<SubscriptionDraftLineRemoveMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineRemoveMutation);
                            }

                            SubscriptionDraftUpdateMutation subscriptionDraftUpdateMutation = new SubscriptionDraftUpdateMutation(optionalDraftId.get(), subscriptionDraftInput);
                            Response<Optional<SubscriptionDraftUpdateMutation.Data>> optionalDraftUpdateResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftUpdateMutation);

                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation((String)optionalDraftId.get());
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftCommitMutation);
                            if (!CollectionUtils.isEmpty(optionalDraftCommitResponse.getErrors())) {
                                errorList.add("Error occurred at line: " + subscriptionDataRecord.getRecordNumber() + " error= " + ((Error)optionalDraftCommitResponse.getErrors().get(0)).getMessage());
                            }

                            List<SubscriptionDraftCommitMutation.UserError> userErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(s -> s.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                            if (!userErrors.isEmpty()) {
                                errorList.add("Error occurred at line: " + subscriptionDataRecord.getRecordNumber() + " error= " + ((Error)optionalDraftCommitResponse.getErrors().get(0)).getMessage());
                            }

                            SubscriptionContractQuery.SubscriptionContract updatedData = subscriptionContractDetailsService.getSubscriptionContractRawInternal(contractId, shop, shopifyGraphqlClient);
                            System.out.println("updated");
                        }
                    }
                    processedContracts.add(contractIdString);
                }
            }catch (Exception e){
                errorList.add("Error occurred at line: " + subscriptionDataRecord.getRecordNumber() + " error= " + e.getMessage());
            }
        }
        return  ResponseEntity.ok(errorList);
    }*/

    @GetMapping("/refresh-selling-plan-products-data")
    public void updateSellingPlanProductsData(@RequestParam("api_key") String apiKey) throws Exception{

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        Set<Long> sellingPlanGroupIds = subscriptionGroupService.findShopSubscriptionGroupsV2(shop)
            .stream().map(SubscriptionGroupV2DTO::getId).collect(Collectors.toSet());

        List<SubscriptionGroupPlanDTO> dbPlans = subscriptionGroupPlanService.findByShop(shop);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for(Long sellingPlanGroupId : sellingPlanGroupIds) {
            try {
                Optional<SubscriptionGroupPlanDTO> subscriptionGroupPlanDTOOpt = dbPlans.stream().filter(plan -> plan.getSubscriptionId().equals(sellingPlanGroupId)).findFirst();

                if (subscriptionGroupPlanDTOOpt.isPresent()) {

                    SubscriptionGroupPlanDTO subscriptionGroupPlanDTO = subscriptionGroupPlanDTOOpt.get();

                    SubscriptionGroupV2DTO subscriptionGroupV2DTO = OBJECT_MAPPER.readValue(subscriptionGroupPlanDTO.getInfoJson(), SubscriptionGroupV2DTO.class);

                    ProductData productData = subscriptionGroupService.getSellingGroupProducts(shopifyGraphqlClient, sellingPlanGroupId, false, null);

                    List<SubscribedProductVariantInfo> productsData = productData.getProducts().stream().map(product -> {
                        SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                        subscribedProductVariantInfo.setId(product.getId());
                        subscribedProductVariantInfo.setTitle(product.getTitle());
                        subscribedProductVariantInfo.setStatus(product.getStatus());
                        subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                        return subscribedProductVariantInfo;
                    }).collect(Collectors.toList());
                    subscriptionGroupV2DTO.setProductCount((long) productsData.size());
                    subscriptionGroupV2DTO.setProductIds(OBJECT_MAPPER.writeValueAsString(productsData));

                    ProductData productVariantData = subscriptionGroupService.getSellingGroupProductVariants(shopifyGraphqlClient, sellingPlanGroupId, false, null);

                    List<SubscribedProductVariantInfo> variantData = productVariantData.getProducts().stream().map(product -> {
                        SubscribedProductVariantInfo subscribedProductVariantInfo = new SubscribedProductVariantInfo();
                        subscribedProductVariantInfo.setId(product.getId());
                        subscribedProductVariantInfo.setTitle(product.getTitle());
                        subscribedProductVariantInfo.setHandle(product.getHandle() != null ? product.getHandle() : "");
                        subscribedProductVariantInfo.setAdditionalProperty("imageSrc", product.getImageSrc() != null ? product.getImageSrc() : "");
                        return subscribedProductVariantInfo;
                    }).collect(Collectors.toList());
                    subscriptionGroupV2DTO.setVariantIds(OBJECT_MAPPER.writeValueAsString(variantData));

                    subscriptionGroupPlanDTO.setProductCount(subscriptionGroupV2DTO.getProductCount().intValue());
                    subscriptionGroupPlanDTO.setInfoJson(OBJECT_MAPPER.writeValueAsString(subscriptionGroupV2DTO));

                    subscriptionGroupPlanService.save(subscriptionGroupPlanDTO);

                    Set<Long> productIds = productsData.stream().map(SubscribedProductVariantInfo::getId).collect(Collectors.toSet());
                    productIds.addAll(variantData.stream().map(SubscribedProductVariantInfo::getId).collect(Collectors.toSet()));

                    productInfoService.createOrUpdateProductByIds(shop, productIds);
                }
            }catch (Exception e){
                log.error("Error occurred while refreshing products data for Selling plan group: {}", sellingPlanGroupId);
            }
        }
    }

    @GetMapping("/get-customers-with-customer-tags-discrepancy")
    public void getCustomersWithCustomerTagsDiscrepancy(@RequestParam("api_key") String apiKey,
                                                        @RequestParam("email") String email) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<SubscriptionContractDetails> subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);

        if (CollectionUtils.isEmpty(subscriptionContractDetailsList)) {
            throw new BadRequestAlertException("No contracts found for shop= " + shop, "", "");
        }

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);
        if (shopInfoDTOOpt.isEmpty()) {
            throw new BadRequestAlertException("Shop info not found for shop= " + shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(shop);

        Set<Long> processedCustomers = new HashSet<>();

        File tempFile = null;

        String[] headers = {
            "Customer ID",
            "Customer name",
            "Customer email",
            "Existing Customer Tags",
            "Missing tag"
        };
        tempFile = File.createTempFile("Customers-with-tag-discrepancy", ".csv");
        Writer writer = Files.newBufferedWriter(tempFile.toPath());

        CSVFormat csvFormat = CSVFormat.DEFAULT
            .withAutoFlush(true)
            .withHeader(headers);

        Long record = 0L;

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {

            for (SubscriptionContractDetails subscriptionContractDetails : subscriptionContractDetailsList) {

                try {
                    log.info("Iterating record : {} of {} for shop : {}", record++, subscriptionContractDetailsList.size(), shop);
                    if (!processedCustomers.contains(subscriptionContractDetails.getCustomerId())) {
                        SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetails.getSubscriptionContractId());
                        Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                        TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);

                        processedCustomers.add(subscriptionContractDetails.getCustomerId());

                        String customerTag = "";
                        try {
                            List<SubscriptionContractDetailsDTO> customerContracts = subscriptionContractDetailsService.findByShopAndCustomerId(shop, subscriptionContractDetails.getCustomerId());

                            List<SubscriptionContractDetailsDTO> activeContracts = customerContracts.stream().filter(contract -> contract.getStatus().equalsIgnoreCase("active")).collect(Collectors.toList());
                            List<SubscriptionContractDetailsDTO> pausedContracts = customerContracts.stream().filter(contract -> contract.getStatus().equalsIgnoreCase("paused")).collect(Collectors.toList());

                            String customerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerActiveSubscriptionTag()).orElse(""));
                            String inActiveCustomerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerInActiveSubscriptionTag()).orElse(""));
                            String pausedCustomerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerPausedSubscriptionTag()).orElse(""));

                            if (!CollectionUtils.isEmpty(activeContracts)) {
                                customerTag = customerActiveSubscriptionTagValue;
                            } else if (!CollectionUtils.isEmpty(pausedContracts) && org.apache.commons.lang3.StringUtils.isNotBlank(pausedCustomerActiveSubscriptionTagValue)) {
                                customerTag = pausedCustomerActiveSubscriptionTagValue;
                            } else {
                                customerTag = inActiveCustomerActiveSubscriptionTagValue;
                            }

                            com.et.api.shopify.customer.Customer shopifyCustomer = shopifyRestApi.getCustomer(subscriptionContractDetails.getCustomerId()).getCustomer();
                            Set<String> existingCustomerTags = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopifyCustomer.getTags()).orElse(""), ",")).map(String::trim).collect(Collectors.toSet());

                            if (!existingCustomerTags.contains(customerTag)) {
                                log.info("Customer : {} that have missed tag", subscriptionContractDetails.getCustomerId());
                                csvPrinter.printRecord(
                                    subscriptionContractDetails.getCustomerId(),
                                    subscriptionContractDetails.getCustomerName(),
                                    subscriptionContractDetails.getCustomerEmail(),
                                    existingCustomerTags,
                                    customerTag
                                );
                            }

                        } catch (Exception e) {
                            log.error("Error occurred while checking customer tags for customer: {}", subscriptionContractDetails.getCustomerId());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error occurred while checking customer tags for contract: {}", subscriptionContractDetails.getSubscriptionContractId());
                }
            }
        }
        mailgunService.sendEmailWithAttachment(tempFile, "List of Customers with customer tags discrepancy", "Check attached csv file for your all customers list.", "subscription-support@appstle.com", shop, email);
    }

    @GetMapping("/update-customer-tags")
    public void refreshCustomerTags(@RequestParam("api_key") String apiKey,
                                    @RequestParam(required = false) String commaSeparatedContractIds,
                                    @RequestParam(required = false) Boolean allSubscriptions){

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetails> subscriptionContractDetailsList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsList = subscriptionContractDetailsRepository.findByShopAndSubscriptionContractIdIn(shop, contractIds);
        }

        if(CollectionUtils.isEmpty(subscriptionContractDetailsList)){
            throw new BadRequestAlertException("No contracts found for shop= " + shop, "", "");
        }

        Optional<ShopInfoDTO> shopInfoDTOOpt = shopInfoService.findByShop(shop);
        if(shopInfoDTOOpt.isEmpty()){
            throw new BadRequestAlertException("Shop info not found for shop= " + shop, "", "");
        }

        ShopInfoDTO shopInfoDTO = shopInfoDTOOpt.get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        ShopifyAPI shopifyRestApi = commonUtils.prepareShopifyResClient(shop);

        Set<Long> processedCustomers = new HashSet<>();
        for(SubscriptionContractDetails subscriptionContractDetails: subscriptionContractDetailsList){

            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + subscriptionContractDetails.getSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                TagModel tagModel = buildTagModel(subscriptionContractDetails, subscriptionContractResponse);

                List<String> firstTimeOrderTagValues = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopInfoDTO.getFirstTimeOrderTag()).orElse(""), ","))
                    .map(String::trim)
                    .map(s -> liquidUtils.getValue(tagModel, s))
                    .collect(Collectors.toList());

                updateShopifyOrderDetails(shopifyRestApi, subscriptionContractDetails.getOrderId(), firstTimeOrderTagValues, shopifyGraphqlClient);

                updateOrderMetafields(subscriptionContractDetails, tagModel, subscriptionContractDetails.getOrderId(), shopifyGraphqlClient);

                if(!processedCustomers.contains(subscriptionContractDetails.getCustomerId())) {

                    processedCustomers.add(subscriptionContractDetails.getCustomerId());

                    updateCustomerTags(
                        shop,
                        shopifyRestApi,
                        subscriptionContractDetails.getCustomerId(),
                        tagModel,
                        shopifyGraphqlClient,
                        shopInfoDTO);
                }
            }catch (Exception e){
                log.error("Error occurred while updating customer tags for contract: {}", subscriptionContractDetails.getSubscriptionContractId());
            }
        }
    }

    protected void updateShopifyOrderDetails(ShopifyAPI shopifyRestApi, Long orderId, List<String> incomingOrderTags, ShopifyGraphqlClient shopifyGraphqlClient) {
        if (orderId == null) {
            return;
        }

        try {
            Order order = shopifyRestApi.getOrder(orderId).getOrder();
            Set<String> orderTags = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(order.getTags()).orElse(""), ",")).map(String::trim).collect(Collectors.toSet());

            boolean orderTagModified = false;
            if (incomingOrderTags != null && !incomingOrderTags.isEmpty()) {
                for (String incomingOrderTag : incomingOrderTags) {
                    if (orderTags.add(incomingOrderTag)) {
                        orderTagModified = true;
                        log.info("Order tags are missed for order = {}, applying missed order tags", order.getOrderNumber());
                    }
                }
            }

            OrderInput.Builder orderInputBuilder = OrderInput
                .builder();

            boolean orderNoteModified = false;
            boolean orderNoteAttributesModified = false;

            OrderInput orderInput = orderInputBuilder
                .id(order.getAdminGraphqlApiId())
                .tags(new ArrayList<>(orderTags))
                .build();

            if (orderTagModified || orderNoteModified || orderNoteAttributesModified) {
                OrderUpdateMutation orderUpdateMutation = new OrderUpdateMutation(orderInput);
                Response<Optional<OrderUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(orderUpdateMutation);

                if (optionalMutationResponse.hasErrors()) {
                    log.error("An error occurred while updating order tags. shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " ex=" + optionalMutationResponse.getErrors().get(0).getMessage() + " orderTag=" + Optional.ofNullable(incomingOrderTags).orElse(new ArrayList<>()));
                }

                List<OrderUpdateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                    .map(d -> d.getOrderUpdate()
                        .map(OrderUpdateMutation.OrderUpdate::getUserErrors)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());

                if (!userErrors.isEmpty()) {
                    log.error("An error occurred while updating order tags. shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " ex=" + userErrors.get(0).getMessage() + " orderTag=" + Optional.ofNullable(incomingOrderTags).orElse(new ArrayList<>()));
                }
            } else {
                log.info("No order modification recorded.");
            }
        } catch (Exception ex) {
            log.error("An error occurred while updating order tags. shop=" + shopifyRestApi.getShopName() + " orderId=" + orderId + " ex=" + ExceptionUtils.getStackTrace(ex) + " orderTag=" + Optional.ofNullable(incomingOrderTags).orElse(new ArrayList<>()));
        }
    }

    protected void updateCustomerTags(String shop, ShopifyAPI shopifyRestApi, Long customerId, TagModel tagModel, ShopifyGraphqlClient shopifyGraphqlClient, ShopInfoDTO shopInfoDTO) {
        String customerTag = "";
        Set<String> removedCustomerTags = new HashSet<>();
        try {
            List<SubscriptionContractDetailsDTO> customerContracts = subscriptionContractDetailsService.findByShopAndCustomerId(shop, customerId);

            List<SubscriptionContractDetailsDTO> activeContracts = customerContracts.stream().filter(contract -> contract.getStatus().equalsIgnoreCase("active")).collect(Collectors.toList());
            List<SubscriptionContractDetailsDTO> pausedContracts = customerContracts.stream().filter(contract -> contract.getStatus().equalsIgnoreCase("paused")).collect(Collectors.toList());

            String customerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerActiveSubscriptionTag()).orElse(""));
            String inActiveCustomerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerInActiveSubscriptionTag()).orElse(""));
            String pausedCustomerActiveSubscriptionTagValue = liquidUtils.getValue(tagModel, Optional.ofNullable(shopInfoDTO.getCustomerPausedSubscriptionTag()).orElse(""));

            if(!CollectionUtils.isEmpty(activeContracts)){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(inActiveCustomerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(inActiveCustomerActiveSubscriptionTagValue);
                }

                if (org.apache.commons.lang3.StringUtils.isNotBlank(pausedCustomerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(pausedCustomerActiveSubscriptionTagValue);
                }

                customerTag = customerActiveSubscriptionTagValue;
            }else if (!CollectionUtils.isEmpty(pausedContracts) && org.apache.commons.lang3.StringUtils.isNotBlank(pausedCustomerActiveSubscriptionTagValue)){
                if (org.apache.commons.lang3.StringUtils.isNotBlank(customerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(customerActiveSubscriptionTagValue);
                }

                if (org.apache.commons.lang3.StringUtils.isNotBlank(inActiveCustomerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(inActiveCustomerActiveSubscriptionTagValue);
                }

                customerTag = pausedCustomerActiveSubscriptionTagValue;
            } else {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(customerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(customerActiveSubscriptionTagValue);
                }

                if (org.apache.commons.lang3.StringUtils.isNotBlank(pausedCustomerActiveSubscriptionTagValue)) {
                    removedCustomerTags.add(pausedCustomerActiveSubscriptionTagValue);
                }

                customerTag = inActiveCustomerActiveSubscriptionTagValue;
            }

            com.et.api.shopify.customer.Customer shopifyCustomer = shopifyRestApi.getCustomer(customerId).getCustomer();
            Set<String> existingCustomerTags = Arrays.stream(org.apache.commons.lang3.StringUtils.split(Optional.ofNullable(shopifyCustomer.getTags()).orElse(""), ",")).map(String::trim).collect(Collectors.toSet());

            if (!CollectionUtils.isEmpty(removedCustomerTags)) {
                removedCustomerTags.stream()
                    .filter(Objects::nonNull)
                    .forEach(existingCustomerTags::remove);
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(customerTag)) {
                existingCustomerTags.add(customerTag);
            }

            CustomerInput customerInput = CustomerInput.builder().id(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + customerId).tags(new ArrayList<>(existingCustomerTags)).build();
            CustomerUpdateMutation customerUpdateMutation = new CustomerUpdateMutation(customerInput);
            Response<Optional<CustomerUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(customerUpdateMutation);

            if (optionalMutationResponse.hasErrors()) {
                log.error("An error occurred while updating customer tags. shop=" + shopifyRestApi.getShopName() + " customerId=" + customerId + " ex=" + optionalMutationResponse.getErrors().get(0).getMessage() + " customerTag=" + Optional.ofNullable(customerTag).orElse("") + " removeCustomerTags=" + Optional.ofNullable(removedCustomerTags).orElse(new HashSet<>()));
                return;
            }

            List<CustomerUpdateMutation.UserError> userErrors = optionalMutationResponse.getData().map(d -> d.getCustomerUpdate().map(e -> e.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.error("An error occurred while updating customer tags. shop=" + shopifyRestApi.getShopName() + " customerId=" + customerId + " ex=" + userErrors.get(0).getMessage() + " customerTag=" + Optional.ofNullable(customerTag).orElse("") + " removeCustomerTags=" + Optional.ofNullable(removedCustomerTags).orElse(new HashSet<>()));
                return;
            }
            log.info("updating customer tags for shop=" + shopifyRestApi.getShopName() + " customerId=" + customerId + " existingCustomerTags=" + existingCustomerTags + " customerTag=" + Optional.ofNullable(customerTag).orElse("") + " removeCustomerTag=" + Optional.ofNullable(removedCustomerTags).orElse(new HashSet<>()));
        } catch (Exception ex) {
            log.error("An error occurred while updating customer tags. shop=" + shopifyRestApi.getShopName() + " customerId=" + customerId + " ex=" + ExceptionUtils.getStackTrace(ex) + " customerTag=" + Optional.ofNullable(customerTag).orElse("") + " removeCustomerTags=" + Optional.ofNullable(removedCustomerTags).orElse(new HashSet<>()));
        }
    }

    @GetMapping("/resend-magic-link")
    public ResponseEntity<List<String>> resendMagicLink(@RequestParam("api_key") String apiKey,
                                                        @RequestParam(required = false) String commaSeparatedCustomerEmails,
                                                        @RequestParam(value = "allCustomers", required = false) Boolean allCustomers){
        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        Set<String> customerEmails = new HashSet<>();
        if(BooleanUtils.isTrue(allCustomers)){
            customerEmails = new HashSet<>(subscriptionContractDetailsRepository.findCustomerEmailsByShop(shop));
        }else{
            customerEmails = Arrays.stream(commaSeparatedCustomerEmails.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).collect(Collectors.toSet());
        }

        List<String> errors = new ArrayList<>();
        for(String email : customerEmails) {
            try {
                subscriptionContractDetailsResource.emailMagicLink(shop, email);
            }catch (Exception e){
                errors.add("An error occurred while sending magic link to customer: " + email + " message= "+ e.getMessage());
            }
        }
        return ResponseEntity.ok(errors);
    }

    @GetMapping("/export-merchant-data-of-feature-usage")
    public ResponseEntity exportMerchantDataBasedOnFeatureUsage(ExportMerchantsRequest exportMerchantsRequest){
        String shop = commonUtils.getShopByAPIKey(exportMerchantsRequest.getApiKey()).get();
        try {
            exportMerchantData(exportMerchantsRequest, shop);
        } catch (Exception e) {
            log.error("Error occure while exporting merchant data", e.getMessage());
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "On exporting done you receive email with csv file attachment on email id " + exportMerchantsRequest.getEmail(), "")).build();
    }

    @Async
    public void exportMerchantData(ExportMerchantsRequest exportMerchantsRequest, String shop) {
        File tempFile = null;
        Pageable pageable = null;

        if(exportMerchantsRequest.getPageSize() == 0) {
            pageable = PageRequest.of(0, 1000);
        } else {
            pageable = PageRequest.of(exportMerchantsRequest.getPageNumber(), exportMerchantsRequest.getPageSize());
        }

        try {
            String[] headers = {
                "Shop",
                "Public Domain",
                "Shopify Plan Display Name",
                "Shopify Plan Name",
                "Appstle Plan Name",
                "Currency",
                "Country Name",
                "Country Code",
                "Activation Date",
                "Total Revenue"
            };
            tempFile = File.createTempFile("Merchants-Export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            List<ExportMerchantsResponse> shopInfoDTOS = new ArrayList<>();
            if (exportMerchantsRequest.getFeatureWeOffer().equals(FeatureWeOffer.BUILD_A_BOX)) {
                shopInfoDTOS = shopInfoService.findAllShopWhichUseBuildABoxFeature(pageable);
            } else if(exportMerchantsRequest.getFeatureWeOffer().equals(FeatureWeOffer.APPSTLE_MENU)) {
                shopInfoDTOS = shopInfoService.findAllShopWhichUseAppstleMenu(pageable);
            }
            if (shopInfoDTOS.size() == 0) {
                return;
            }
            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                log.info("total shop found =" + shopInfoDTOS.size());
                for (int j = 0; j < shopInfoDTOS.size(); j++) {
                    ExportMerchantsResponse merchantsResponse  = shopInfoDTOS.get(j);
                    log.info("Came to Record. No=" + j + " shop=" + merchantsResponse.getShop());
                    csvPrinter.printRecord(merchantsResponse.getShop(),
                        merchantsResponse.getPublicDomain(),
                        merchantsResponse.getShopifyPlanDisplayName(),
                        merchantsResponse.getShopifyPlanName(),
                        merchantsResponse.getName(),
                        merchantsResponse.getCurrency(),
                        merchantsResponse.getCountryName(),
                        merchantsResponse.getCountryCode(),
                        merchantsResponse.getActivationDate(),
                        merchantsResponse.getTotalRevenue()
                    );
                }
            } catch (Exception e){
                log.error("ex=" + ExceptionUtils.getStackTrace(e));
            }

        } catch (Exception e) {
            log.error("ex=" + ExceptionUtils.getStackTrace(e));
        }

        mailgunService.sendEmailWithAttachment(tempFile, exportMerchantsRequest.getFeatureWeOffer() + " Merchant List Exported", "Check attached csv file for your all merchant list details.", "subscription-support@appstle.com", shop, exportMerchantsRequest.getEmail());
    }

    @GetMapping("/apply-build-a-box-discount")
    public void applyBuildABoxDiscount(@RequestParam("api_key") String apiKey,
                                       @RequestParam(required = false) String commaSeparatedContractIds,
                                       @RequestParam(value = "allSubscriptions", required = false) boolean allSubscriptions,
                                       @RequestParam String buildABoxUniqueRef){

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if(!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)){
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        }else{
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for(Long contractId: contractIds){
            try {
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(contractId));
                Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    throw new Exception("Subscription contract not found");
                }

                if(subscriptionContractOptional.get().getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED)){
                    throw new Exception("Contract cancelled");
                }

                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.findByContractId(contractId);

                if(!subscriptionContractDetailsDTO.isPresent()) {
                    throw new Exception("Subscription contract not found");
                }

                if(subscriptionContractDetailsDTO.get().getSubscriptionType().equals(SubscriptionType.BUILD_A_BOX_CLASSIC)){
                    resyncBuildABoxDiscount(shop, contractId, buildABoxUniqueRef, shopifyGraphqlClient, optionalSubscriptionContractQueryResponse);
                }

                log.info("Build a box discount applied to contract {}", contractId);
            }catch (Exception e){
                log.error("Error occurred while updating build a box discount for contract = {}, error = {}", contractId, e.getMessage());
            }
        }
    }

    private void resyncBuildABoxDiscount(String shop, Long contractId, String buildABoxToken, ShopifyGraphqlClient shopifyGraphqlClient, Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse) throws Exception {
        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);
        long countOfErrors = optionalResponse.getData()
            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
            .orElse(new ArrayList<>()).stream()
            .map(SubscriptionContractUpdateMutation.UserError::getMessage)
            .peek(message -> log.info("{} REST request for Update subscription contract is failed {} ", "", message)).count();


        if (countOfErrors == 0) {
            // get draft Id from the response
            Optional<String> optionalDraftId = optionalResponse.getData()
                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                .map(draft -> draft.get().getId());


            List<SubscriptionContractQuery.Node> existingLineItems =
                requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

            List<SubscriptionContractQuery.Node2> appliedDiscounts = requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                .map(data -> data.getSubscriptionContract().stream()
                    .map(SubscriptionContractQuery.SubscriptionContract::getDiscounts)
                    .map(discounts -> discounts.getEdges()
                        .stream().map(SubscriptionContractQuery.Edge2::getNode).collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
                ).stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

            SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

            Optional<SubscriptionContractQuery.Node2> build_a_box_discount = appliedDiscounts.stream().filter(node2 -> node2.getTitle().get().contains("BUILD_A_BOX_DISCOUNT")).findFirst();
            String buildABoxUniqueRef;
            if(build_a_box_discount.isPresent()) {
                List<String> stringList = Arrays.stream(build_a_box_discount.get().getTitle().get().split("_")).collect(Collectors.toList());
                buildABoxUniqueRef = stringList.get(stringList.size() - 1);
            }else{
                buildABoxUniqueRef = buildABoxToken;
            }
            int totalQuantity = existingLineItems.stream().map(SubscriptionContractQuery.Node::getQuantity).mapToInt(Integer::intValue).sum();

            double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

            double totalAmount = existingLineItems.stream().map(node -> {
                if(node.getPricingPolicy().isPresent()){
                    return Double.parseDouble(node.getPricingPolicy().get().getBasePrice().getAmount().toString()) * fulfillmentCountMultiplier;
                }else{
                    return Double.parseDouble(node.getCurrentPrice().getAmount().toString());
                }
            }).reduce(Double::sum).orElse(0D);

            SubscriptionBundlingResponse subscriptionBundlingResponse = subscriptionBundlingService.getBundleDetails(shop, buildABoxUniqueRef);
            DiscountCodeResponse discountCodeResponse = generateDiscountCode(shop, subscriptionBundlingResponse, totalQuantity, totalAmount, existingLineItems);

            List<SubscriptionContractQuery.Node2> build_a_box_discount_list = appliedDiscounts.stream().filter(node2 -> node2.getTitle().get().contains("BUILD_A_BOX_DISCOUNT")).collect(Collectors.toList());
            if (discountCodeResponse.getDiscountCode() != null) {
                if (build_a_box_discount.isEmpty() || !build_a_box_discount.get().getValue().toString().contains(String.valueOf(discountCodeResponse.getDiscountAmount().longValue()))) {

                    //Removing existing pricing policy
                    removePricingPolicyForContract(subscriptionContract, optionalDraftId.get(), shopifyGraphqlClient);

                    //Removing Existing build a box discount because previous discount is not matched with current applicable discount
                    for (SubscriptionContractQuery.Node2 item : build_a_box_discount_list) {
                        SubscriptionDraftDiscountRemoveMutation subscriptionDraftDiscountRemoveMutation = new SubscriptionDraftDiscountRemoveMutation(optionalDraftId.get(), item.getId());
                        shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountRemoveMutation);
                    }

                    //Applying current applicable discount
                    SubscriptionDraftDiscountCodeApplyMutation subscriptionDraftDiscountCodeApplyMutation = new SubscriptionDraftDiscountCodeApplyMutation(optionalDraftId.get(), discountCodeResponse.getDiscountCode());
                    shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftDiscountCodeApplyMutation);


                    SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(optionalDraftId.get());
                    Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                        .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                    if (optionalDraftCommitResponse.hasErrors()) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(optionalDraftCommitResponse.getErrors().get(0).getMessage()), ENTITY_NAME, "idexists");
                    }

                    List<SubscriptionDraftCommitMutation.UserError> draftCommitResponseUserErrors = requireNonNull(optionalDraftCommitResponse.getData()).map(d -> d.getSubscriptionDraftCommit().map(SubscriptionDraftCommitMutation.SubscriptionDraftCommit::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                    if (draftCommitResponseUserErrors.size() > 0) {
                        throw new BadRequestAlertException(StringEscapeUtils.escapeJson(draftCommitResponseUserErrors.get(0).getMessage()), ENTITY_NAME, "idexists");
                    }
                }
            }
        }
    }

    private DiscountCodeResponse generateDiscountCode(String shop, SubscriptionBundlingResponse subscriptionBundlingResponse, int totalQuantity, double totalAmount, List<SubscriptionContractQuery.Node> remaningItemList) throws Exception {

        DiscountCodeResponse discountCodeResponse = new DiscountCodeResponse();
        TieredDiscountDTO applicableDiscount = new TieredDiscountDTO();
        SubscriptionBundling bundle = subscriptionBundlingResponse.getBundle();
        List<SubscriptionGroupPlan> subscriptions = subscriptionBundlingResponse.getSubscriptions();

        if (bundle != null && bundle.getTieredDiscount() != null) {
            applicableDiscount = findApplicableTieredDiscount(bundle, totalAmount, totalQuantity);
        }

        if (bundle != null && !CollectionUtils.isEmpty(subscriptions) && (applicableDiscount.getDiscount() != null || bundle.getDiscount() != null)) {

            List<SubscribedProductVariantInfo> products = new ArrayList<>();
            List<SubscribedProductVariantInfo> variants = new ArrayList<>();

            for(SubscriptionGroupPlan sgp: subscriptions) {
                SubscriptionGroupV2DTO subscriptionGroupDTO = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                }, sgp.getInfoJson());

                List<SubscribedProductVariantInfo> productsJSON = CommonUtils.fromJSON(new TypeReference<>() {
                }, subscriptionGroupDTO.getProductIds());
                products.addAll(Optional.ofNullable(productsJSON).orElse(new ArrayList<>()));

                List<SubscribedProductVariantInfo> variantsJSON = CommonUtils.fromJSON(new TypeReference<>() {
                }, subscriptionGroupDTO.getVariantIds());
                variants.addAll(Optional.ofNullable(variantsJSON).orElse(new ArrayList<>()));
            }

            Set<Long> cartProductIds = remaningItemList
                .stream()
                .map(node -> ShopifyGraphQLUtils.getProductId(node.getProductId().get())).collect(Collectors.toSet());


            Set<Long> cartVariantIds = remaningItemList
                .stream()
                .map(node -> ShopifyGraphQLUtils.getVariantId(node.getVariantId().get()))
                .collect(Collectors.toSet());

            Map<Long, Set<Long>> variantIdsByProductIds = new HashMap<>();

            for (SubscriptionContractQuery.Node item : remaningItemList) {
                Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(ShopifyGraphQLUtils.getProductId(item.getProductId().get()), new HashSet<>());
                productVariantIds.add(ShopifyGraphQLUtils.getVariantId(item.getVariantId().get()));
                variantIdsByProductIds.put(ShopifyGraphQLUtils.getProductId(item.getProductId().get()), productVariantIds);
            }

            List<String> productsToAdd = products.stream().filter(p -> {
                    boolean found = cartProductIds.contains(p.getId());

                    if (found) {
                        Set<Long> productVariantIds = variantIdsByProductIds.getOrDefault(p.getId(), new HashSet<>());
                        cartVariantIds.removeAll(productVariantIds);
                    }

                    return found;
                }).map(p -> ShopifyIdPrefix.PRODUCT_ID_PREFIX + p.getId())
                .collect(Collectors.toList());

            List<String> variantsToAdd = variants.stream().filter(v -> {
                    return cartVariantIds.contains(v.getId());
                }).map(p -> ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + p.getId())
                .collect(Collectors.toList());

            List<String> variantsToRemove = variants.stream().filter(v -> {
                    return cartVariantIds.contains(v.getId());
                }).map(p -> ShopifyIdPrefix.PRODUCT_VARIANT_ID_PREFIX + p.getId())
                .collect(Collectors.toList());

            DiscountProductsInput discountProductsInput = DiscountProductsInput
                .builder()
                .productsToAdd(productsToAdd)
                .productVariantsToAdd(variantsToAdd)
                .build();

            DiscountItemsInput discountItemsInput = DiscountItemsInput
                .builder()
                .products(discountProductsInput)
                .build();
            double discountAmountToApply = subscriptionBundlingService.prepareDiscountAmount(shop, bundle, applicableDiscount, totalAmount, totalQuantity) / 100;
            DiscountCustomerGetsInput discountCustomerGetsInput = DiscountCustomerGetsInput
                .builder()
                .appliesOnSubscription(true)
                .appliesOnOneTimePurchase(true)
                .items(discountItemsInput)
                .value(
                    DiscountCustomerGetsValueInput.builder()
                        .percentage(discountAmountToApply)
                        .build()
                )//if applicable discount not found the default discount will be applied
                .build();

            DiscountCustomerSelectionInput discountCustomerSelectionInput = DiscountCustomerSelectionInput
                .builder()
                .all(true)
                .build();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_STAMP_FORMAT);
            String formattedDate = dateTimeFormatter.format(ZonedDateTime.now().minusDays(1));

            DiscountMinimumQuantityInput discountMinimumQuantityInput =
                DiscountMinimumQuantityInput
                    .builder()
                    .greaterThanOrEqualToQuantity(Optional.ofNullable(applicableDiscount.getQuantity() != null && applicableDiscount.getDiscountBasedOn().equals("QUANTITY") ? applicableDiscount.getQuantity() :
                        applicableDiscount.getQuantity() != null && applicableDiscount.getDiscountBasedOn().equals("AMOUNT") ? 1 : bundle.getMinProductCount()).orElse(0).toString())
                    .build();

            DiscountMinimumRequirementInput discountMinimumRequirementInput =
                DiscountMinimumRequirementInput
                    .builder()
                    .quantity(discountMinimumQuantityInput)
                    .build();

            DiscountCodeBasicInput discountCodeBasicInput = DiscountCodeBasicInput
                .builder()
                .code("BUILD_A_BOX_DISCOUNT_" + RandomStringUtils.randomAlphabetic(10) + "_" + bundle.getUniqueRef())
                .customerGets(discountCustomerGetsInput)
                .title("BUILD_A_BOX_DISCOUNT" + "_" + bundle.getUniqueRef())
                .minimumRequirement(discountMinimumRequirementInput)
                .customerSelection(discountCustomerSelectionInput)
                .recurringCycleLimit(Integer.MAX_VALUE)
                .startsAt(formattedDate)
                .build();

            DiscountCodeBasicCreateMutation discountCodeBasicCreateMutation = new DiscountCodeBasicCreateMutation(discountCodeBasicInput);

            ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);
            Response<Optional<DiscountCodeBasicCreateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(discountCodeBasicCreateMutation);

            if (!org.springframework.util.CollectionUtils.isEmpty(optionalMutationResponse.getErrors())) {
                log.info("errors=" + optionalMutationResponse.getErrors().get(0).getMessage() + " shop=" + shop);
                discountCodeResponse.setDiscountNeeded(false);
                return discountCodeResponse;
            }

            List<DiscountCodeBasicCreateMutation.UserError> userErrors = Objects.requireNonNull(optionalMutationResponse.getData())
                .map(d -> d.getDiscountCodeBasicCreate().map(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getUserErrors)
                    .orElse(new ArrayList<>()))
                .orElse(new ArrayList<>());

            if (!userErrors.isEmpty()) {
                log.info("errors=" + userErrors.toString() + " shop=" + shop);
                discountCodeResponse.setErrorMessage(userErrors.get(0).getMessage());
                discountCodeResponse.setDiscountNeeded(false);
                return discountCodeResponse;
            }

            DiscountCodeBasicCreateMutation.CodeDiscountNode codeDiscountNode = optionalMutationResponse
                .getData()
                .flatMap(e -> e.getDiscountCodeBasicCreate()
                    .flatMap(DiscountCodeBasicCreateMutation.DiscountCodeBasicCreate::getCodeDiscountNode))
                .orElse(null);

            String discountCodeString = null;
            if (codeDiscountNode != null) {
                discountCodeString = ((DiscountCodeBasicCreateMutation.AsDiscountCodeBasic) codeDiscountNode.getCodeDiscount())
                    .getCodes().getEdges().get(0).getNode().getCode();
            }

            discountCodeResponse.setDiscountCode(discountCodeString);
            discountCodeResponse.setDiscountNeeded(true);
            discountCodeResponse.setDiscountAmount(discountAmountToApply * 100);
        } else {
            discountCodeResponse.setDiscountNeeded(false);
        }
        return discountCodeResponse;
    }

    private TieredDiscountDTO findApplicableTieredDiscount(SubscriptionBundling bundle, double totalAmount, int totalQuantity) throws IOException {
        TieredDiscountDTO applicableDiscount = new TieredDiscountDTO();


        ObjectMapper mapper = new ObjectMapper();
        List<TieredDiscountDTO> tieredDiscount = mapper.readValue(bundle.getTieredDiscount(), new TypeReference<List<TieredDiscountDTO>>() {
        });

        Optional<TieredDiscountDTO> applicableQuantityBasedDiscountOptional = tieredDiscount.stream()
            .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("QUANTITY"))
            .filter(tieredDiscountDTO -> totalQuantity >= tieredDiscountDTO.getQuantity())
            .reduce((first, second) -> {
                if (first.getDiscount() > second.getDiscount()) {
                    return first;
                } else {
                    return second;
                }
            });

        Optional<TieredDiscountDTO> applicableSpendAmountBasedDiscountOptional = tieredDiscount.stream()
            .filter(tieredDiscountDTO -> tieredDiscountDTO.getDiscountBasedOn().equals("AMOUNT"))
            .filter(tieredDiscountDTO -> totalAmount >= tieredDiscountDTO.getQuantity())
            .reduce((first, second) -> {
                if (first.getDiscount() > second.getDiscount()) {
                    return first;
                } else {
                    return second;
                }
            });

        if (applicableQuantityBasedDiscountOptional.isPresent() && applicableSpendAmountBasedDiscountOptional.isPresent()) {
            TieredDiscountDTO quantityBased = applicableQuantityBasedDiscountOptional.get();
            TieredDiscountDTO amountBased = applicableSpendAmountBasedDiscountOptional.get();
            if (quantityBased.getDiscount() > amountBased.getDiscount()) {
                applicableDiscount = quantityBased;
            } else {
                applicableDiscount = amountBased;
            }
        } else if (applicableQuantityBasedDiscountOptional.isPresent()) {
            applicableDiscount = applicableQuantityBasedDiscountOptional.get();
        } else if (applicableSpendAmountBasedDiscountOptional.isPresent()) {
            applicableDiscount = applicableSpendAmountBasedDiscountOptional.get();
        }
        return applicableDiscount;
    }

    @GetMapping("/sync-pricing-policy")
    public void syncContractPricingPolicy(@RequestParam(value = "api_key") String apiKey,
                                         @RequestParam(required = false) String commaSeparatedContractIds,
                                         @RequestParam(required = false) boolean allSubscriptions,
                                          @RequestParam(value = "overwriteExistingPolicy", defaultValue = "false") boolean overwriteExistingPolicy) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)) {
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if (!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)) {
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        } else {
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for(Long subscriptionContractId : contractIds) {
            try {
                String contractGraphId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(subscriptionContractId);
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(contractGraphId);
                Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

                if(subscriptionContract.getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED)){
                    continue;
                }

                double fulfillmentCountMultiplier = subscriptionContract.getBillingPolicy().getIntervalCount() / subscriptionContract.getDeliveryPolicy().getIntervalCount();

                List<SubscriptionContractQuery.Edge> lineItemEdges = Objects.requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>());

                int totalCycles = 1;
                List<SubscriptionBillingAttempt> subscriptionBillingAttempts = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatus(shop, subscriptionContractId, BillingAttemptStatus.SUCCESS);
                totalCycles = totalCycles + subscriptionBillingAttempts.size();

                List<SubscriptionGroupPlan> subscriptionGroupPlanList = subscriptionGroupPlanRepository.findByShop(shop);

                Map<String, FrequencyInfoDTO> frequencyInfoBySellingPlanId = subscriptionGroupPlanList.stream()
                    .map(subscriptionGroupPlan -> {
                        try {
                            return Optional.of(OBJECT_MAPPER.readValue(subscriptionGroupPlan.getInfoJson(), SubscriptionGroupV2DTO.class));
                        } catch (JsonProcessingException e) {
                            return Optional.<SubscriptionGroupV2DTO>empty();
                        }
                    })
                    .filter(Optional::isPresent)
                    .map(s -> s.get().getSubscriptionPlans())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(FrequencyInfoDTO::getId, s -> s));

                for (SubscriptionContractQuery.Edge lineItemEdge : lineItemEdges) {
                    SubscriptionContractQuery.Node node = lineItemEdge.getNode();

                    if (node.getSellingPlanId().isEmpty() || (!overwriteExistingPolicy && node.getPricingPolicy().isPresent())) {
                        continue;
                    }

                    FrequencyInfoDTO frequencyInfoDTO = frequencyInfoBySellingPlanId.get(node.getSellingPlanId().get());

                    List<AppstleCycle> cycles = frequencyInfoDTO.getAppstleCycles();

                    if(BooleanUtils.isTrue(frequencyInfoDTO.getDiscountEnabled())){
                        AppstleCycle appstleCycle = new AppstleCycle();
                        appstleCycle.setAfterCycle(frequencyInfoDTO.getAfterCycle1());
                        appstleCycle.setValue(frequencyInfoDTO.getDiscountOffer());
                        if (frequencyInfoDTO.getDiscountType().equals(DiscountTypeUnit.FIXED)) {
                            appstleCycle.setDiscountType(DiscountType.FIXED);
                        } else if (frequencyInfoDTO.getDiscountType().equals(DiscountTypeUnit.PERCENTAGE)) {
                            appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                        } else if (frequencyInfoDTO.getDiscountType().equals(DiscountTypeUnit.PRICE)) {
                            appstleCycle.setDiscountType(DiscountType.PRICE);
                        }
                        cycles.add(appstleCycle);
                    }

                    if(BooleanUtils.isTrue(frequencyInfoDTO.getDiscountEnabled2())){
                        AppstleCycle appstleCycle = new AppstleCycle();
                        appstleCycle.setAfterCycle(frequencyInfoDTO.getAfterCycle2());
                        appstleCycle.setValue(frequencyInfoDTO.getDiscountOffer2());
                        if (frequencyInfoDTO.getDiscountType2().equals(DiscountTypeUnit.FIXED)) {
                            appstleCycle.setDiscountType(DiscountType.FIXED);
                        } else if (frequencyInfoDTO.getDiscountType2().equals(DiscountTypeUnit.PERCENTAGE)) {
                            appstleCycle.setDiscountType(DiscountType.PERCENTAGE);
                        } else if (frequencyInfoDTO.getDiscountType2().equals(DiscountTypeUnit.PRICE)) {
                            appstleCycle.setDiscountType(DiscountType.PRICE);
                        }
                        cycles.add(appstleCycle);
                    }



                    int finalTotalCycles = totalCycles;
                    Optional<AppstleCycle> optionalAppstleCycle = cycles.stream().sorted(Comparator.comparing(AppstleCycle::getAfterCycle).reversed()).filter(s -> s.getAfterCycle() <= finalTotalCycles).findFirst();

                    if (optionalAppstleCycle.isPresent() && node.getVariantId().isPresent()) {
                        String productVariantId = node.getVariantId().get();
                        AppstleCycle appstleCycle = optionalAppstleCycle.get();
                        Optional<AppstleCycle> optionalAppstleCycle1 = cycles.stream().sorted(Comparator.comparing(AppstleCycle::getAfterCycle)).filter(s -> s.getAfterCycle() > appstleCycle.getAfterCycle()).findFirst();

                        ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

                        Optional<String> optionalContractCurrencyCode = requireNonNull(optionalSubscriptionContractQueryResponse
                            .getData())
                            .map(d -> d.getSubscriptionContract()
                                .map(SubscriptionContractQuery.SubscriptionContract::getLines).
                                map(SubscriptionContractQuery.Lines::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>()).stream()
                            .findFirst()
                            .map(SubscriptionContractQuery.Edge::getNode)
                            .map(SubscriptionContractQuery.Node::getCurrentPrice)
                            .map(SubscriptionContractQuery.CurrentPrice::getCurrencyCode)
                            .map(CurrencyCode::rawValue);


                        Object price = null;
                        if (optionalContractCurrencyCode.isPresent() && !optionalContractCurrencyCode.get().equals(shopInfoDTO.getCurrency())) {
                            ContextualPricingContext contextualPricingContext = ContextualPricingContext.builder().country(CurrencyUtils.getCountryCode(optionalContractCurrencyCode.get())).build();
                            ProductVariantContextualPricingQuery productVariantQuery = new ProductVariantContextualPricingQuery(productVariantId, contextualPricingContext);
                            Response<Optional<ProductVariantContextualPricingQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            price = requireNonNull(productVariantResponse.getData())
                                .flatMap(e -> e.getProductVariant()
                                    .map(ProductVariantContextualPricingQuery.ProductVariant::getContextualPricing)
                                    .map(ProductVariantContextualPricingQuery.ContextualPricing::getPrice)
                                    .map(ProductVariantContextualPricingQuery.Price::getAmount))
                                .orElse(null);

                        } else {
                            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                            Response<Optional<ProductVariantQuery.Data>> productVariantResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);

                            price = requireNonNull(productVariantResponse.getData()).flatMap(e -> e.getProductVariant().map(ProductVariantQuery.ProductVariant::getPrice)).orElse(null);

                        }

                        Double basePrice = Double.parseDouble(price.toString());

                        List<SubscriptionPricingPolicyCycleDiscountsInput> cycleDiscountInputList = new ArrayList<>();

                        SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountInput = buildCycleDiscountInput(basePrice, appstleCycle.getAfterCycle(), appstleCycle.getDiscountType().toDiscountTypeUnit(), appstleCycle.getValue(), fulfillmentCountMultiplier);
                        cycleDiscountInputList.add(cycleDiscountInput);
                        double computedPrice = Double.parseDouble(cycleDiscountInput.computedPrice().toString());

                        if(optionalAppstleCycle1.isPresent()){
                            SubscriptionPricingPolicyCycleDiscountsInput cycleDiscountInput1 = buildCycleDiscountInput(basePrice, optionalAppstleCycle1.get().getAfterCycle(), optionalAppstleCycle1.get().getDiscountType().toDiscountTypeUnit(), optionalAppstleCycle1.get().getValue(), fulfillmentCountMultiplier);
                            cycleDiscountInputList.add(cycleDiscountInput1);
                        }

                        SubscriptionPricingPolicyInput.Builder pricingPolicyInputBuilder = SubscriptionPricingPolicyInput.builder();
                        pricingPolicyInputBuilder.basePrice(basePrice);

                        pricingPolicyInputBuilder.cycleDiscounts(cycleDiscountInputList);

                        SubscriptionLineUpdateInput subscriptionLineUpdateInput = SubscriptionLineUpdateInput.builder()
                            .pricingPolicy(pricingPolicyInputBuilder.build())
                            .currentPrice(computedPrice)
                            .build();

                        SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(contractGraphId);
                        Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                        if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                            throw new Exception(optionalSubscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                        }

                        // get draft Id from the response
                        Optional<String> optionalDraftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                            .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                            .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                            .map(draft -> draft.get().getId());


                        String draftId = optionalDraftId.get();


                        SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, node.getId(), subscriptionLineUpdateInput);
                        Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                        if (optionalMutationResponse.hasErrors()) {
                            throw new Exception(optionalMutationResponse.getErrors().get(0).getMessage());
                        }

                        List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalMutationResponseUserErrors.isEmpty()) {
                            throw new Exception(optionalMutationResponseUserErrors.get(0).getMessage());
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (optionalDraftCommitResponse.hasErrors()) {
                            throw new Exception(optionalDraftCommitResponse.getErrors().get(0).getMessage());
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            throw new Exception(optionalDraftCommitResponseUserErrors.get(0).getMessage());
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("An error occurred while syncing appstle cycle discounts for contract={} ex={}",subscriptionContractId, ExceptionUtils.getStackTrace(ex));
            }
        }
    }

    @GetMapping("/sync-subscriptions-selling-plan")
    public void subscriptionsSellingPlanSync(@RequestParam("api_key") String apiKey,
                                             @RequestParam(required = false) String commaSeparatedContractIds,
                                             @RequestParam(required = false) boolean allSubscriptions) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)) {
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<Long> contractIds = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
            if (!CollectionUtils.isEmpty(subscriptionContractDetailsDTOList)) {
                contractIds = subscriptionContractDetailsDTOList.stream().map(SubscriptionContractDetailsDTO::getSubscriptionContractId).collect(Collectors.toList());
            }
        } else {
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toList());
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (Long subscriptionContractId : contractIds) {
                try {
                    String contractGraphId = ShopifyGraphQLUtils.getGraphQLSubscriptionContractId(subscriptionContractId);
                    SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(contractGraphId);
                    Response<Optional<SubscriptionContractQuery.Data>> optionalSubscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                    SubscriptionContractQuery.SubscriptionContract subscriptionContract = requireNonNull(optionalSubscriptionContractQueryResponse.getData()).map(d -> d.getSubscriptionContract().get()).get();

                    if(subscriptionContract.getStatus().equals(SubscriptionContractSubscriptionStatus.CANCELLED)){
                        continue;
                    }

                    List<SubscriptionContractQuery.Edge> lineItemEdges = Objects.requireNonNull(optionalSubscriptionContractQueryResponse.getData())
                        .map(d -> d.getSubscriptionContract()
                            .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                            .map(SubscriptionContractQuery.Lines::getEdges)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>());

                    for (SubscriptionContractQuery.Edge lineItemEdge : lineItemEdges) {
                        SubscriptionContractQuery.Node node = lineItemEdge.getNode();

                        if (node.getSellingPlanId().isEmpty() && node.getVariantId().isPresent()) {
                            String productVariantId = node.getVariantId().get();

                            ProductVariantQuery productVariantQuery = new ProductVariantQuery(productVariantId);
                            Response<Optional<ProductVariantQuery.Data>> optionalProductVariantQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(productVariantQuery);
                            String gqlProductId = optionalProductVariantQueryResponse.getData().map(d -> d.getProductVariant().map(e -> e.getProduct().getId()).orElse(null)).orElse(null);

                            Long variantId = ShopifyGraphQLUtils.getVariantId(productVariantId);
                            Long productId = ShopifyGraphQLUtils.getProductId(gqlProductId);

                            SubscriptionContractQuery.BillingPolicy billingPolicy = subscriptionContract.getBillingPolicy();

                            List<FrequencyInfoDTO> matchingSellingPlans = subscriptionGroupService.findSellingPlansForProductVariant(
                                shop,
                                productId,
                                variantId,
                                billingPolicy.getIntervalCount(),
                                billingPolicy.getInterval());

                            FrequencyInfoDTO firstMatchingSellingPlan = matchingSellingPlans.get(0);

                            SubscriptionLineUpdateInput subscriptionLineUpdateInput = SubscriptionLineUpdateInput.builder()
                                .sellingPlanId(firstMatchingSellingPlan.getId())
                                .sellingPlanName(firstMatchingSellingPlan.getFrequencyName())
                                .build();

                            SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(contractGraphId);
                            Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalSubscriptionContractUpdateMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                            if (optionalSubscriptionContractUpdateMutationResponse.hasErrors()) {
                                throw new Exception(optionalSubscriptionContractUpdateMutationResponse.getErrors().get(0).getMessage());
                            }

                            // get draft Id from the response
                            Optional<String> optionalDraftId = optionalSubscriptionContractUpdateMutationResponse.getData()
                                .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                                .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                                .map(draft -> draft.get().getId());

                            String draftId = optionalDraftId.get();

                            SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, node.getId(), subscriptionLineUpdateInput);
                            Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                            if (optionalMutationResponse.hasErrors()) {
                                throw new Exception(optionalMutationResponse.getErrors().get(0).getMessage());
                            }

                            List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalMutationResponseUserErrors.isEmpty()) {
                                throw new Exception(optionalMutationResponseUserErrors.get(0).getMessage());
                            }

                            SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                            Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                                .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                            if (optionalDraftCommitResponse.hasErrors()) {
                                throw new Exception(optionalDraftCommitResponse.getErrors().get(0).getMessage());
                            }

                            List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                                throw new Exception(optionalDraftCommitResponseUserErrors.get(0).getMessage());
                            }
                        }
                    }

                } catch (Exception e) {
                    log.info("Error while syncing selling plan for contract id ={} Exception = {}", subscriptionContractId, ExceptionUtils.getStackTrace(e));
                }
        }
    }

    @GetMapping("/find-duplicate-subscriptions")
    public Map<String, List<Long>> findDuplicateContracts(@RequestParam(value = "api_key") String apiKey){
        Map<String, List<Long>> duplicateContractMap = new HashMap<>();

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        Map<String, List<SubscriptionContractDetailsDTO>> customerContractsMap = new HashMap<>();
        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            if(org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionContractDetailsDTO.getImportedId()) && org.apache.commons.lang3.StringUtils.isNotBlank(subscriptionContractDetailsDTO.getCustomerEmail())) {
                if (customerContractsMap.containsKey(subscriptionContractDetailsDTO.getCustomerEmail())) {
                    List<SubscriptionContractDetailsDTO> customerContracts = customerContractsMap.get(subscriptionContractDetailsDTO.getCustomerEmail());
                    customerContracts.add(subscriptionContractDetailsDTO);
                    customerContractsMap.put(subscriptionContractDetailsDTO.getCustomerEmail(), customerContracts);
                } else {
                    List<SubscriptionContractDetailsDTO> customerContracts = new ArrayList<>();
                    customerContracts.add(subscriptionContractDetailsDTO);
                    customerContractsMap.put(subscriptionContractDetailsDTO.getCustomerEmail(), customerContracts);
                }
            }
        }

        for(Map.Entry<String, List<SubscriptionContractDetailsDTO>> entry : customerContractsMap.entrySet()){
            List<Long> duplicateContractIds = new ArrayList<>();
            List<SubscriptionContractDetailsDTO> customerContracts = entry.getValue();
            for(SubscriptionContractDetailsDTO contract : customerContracts){
                for(SubscriptionContractDetailsDTO compareContract : customerContracts){
                    if(!contract.getId().equals(compareContract.getId())){
                        if(!contract.getBillingPolicyInterval().equals(compareContract.getBillingPolicyInterval())){
                            continue;
                        }
                        if(!contract.getBillingPolicyIntervalCount().equals(compareContract.getBillingPolicyIntervalCount())){
                            continue;
                        }
                        if(!contract.getDeliveryPolicyInterval().equals(compareContract.getDeliveryPolicyInterval())){
                            continue;
                        }
                        if(!contract.getDeliveryPolicyIntervalCount().equals(compareContract.getDeliveryPolicyIntervalCount())){
                            continue;
                        }
                        if(org.apache.commons.lang3.StringUtils.isNotBlank(contract.getContractDetailsJSON())){
                            if(org.apache.commons.lang3.StringUtils.isBlank(compareContract.getContractDetailsJSON())){
                                continue;
                            }
                            List<SubscriptionProductInfo> contractProducts = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                            }, contract.getContractDetailsJSON());

                            List<String> productsIds = contractProducts.stream().map(SubscriptionProductInfo::getProductId).collect(Collectors.toList());
                            List<String> variantIds = contractProducts.stream().map(SubscriptionProductInfo::getVariantId).collect(Collectors.toList());

                            List<SubscriptionProductInfo> compareProducts = CommonUtils.fromJSONIgnoreUnknownProperty(new TypeReference<>() {
                            }, contract.getContractDetailsJSON());

                            List<String> compareProductsIds = compareProducts.stream().map(SubscriptionProductInfo::getProductId).collect(Collectors.toList());
                            List<String> compareVariantIds = compareProducts.stream().map(SubscriptionProductInfo::getVariantId).collect(Collectors.toList());

                            if(org.apache.commons.collections.CollectionUtils.intersection(productsIds, compareProductsIds).size() != productsIds.size()){
                                continue;
                            }

                            if(org.apache.commons.collections.CollectionUtils.intersection(variantIds, compareVariantIds).size() != variantIds.size()){
                                continue;
                            }

                        } else if (org.apache.commons.lang3.StringUtils.isNotBlank(compareContract.getContractDetailsJSON())){
                            continue;
                        }

                        duplicateContractIds.add(contract.getSubscriptionContractId());
                        break;
                    }
                }
            }
            if(!CollectionUtils.isEmpty(duplicateContractIds)) {
                duplicateContractMap.put(entry.getKey(), duplicateContractIds);
            }
        }
        return duplicateContractMap;
    }

    @PostMapping("/toggle-billing-plan-lock")
    public ResponseEntity<String> toggleBillingPlanLock() throws Exception {
        log.info("Toggling billing plan lock for shop: {}", SecurityUtils.getCurrentUserLogin().get());

        boolean isPresent = SecurityUtils.getCurrentUserLogin().isPresent();
        if (!isPresent) {
            log.error("Shop is not present");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Shop is not present");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);
        boolean isBillingPlanLocked = BooleanUtils.isFalse(shopInfo.get().isEnableChangeBillingPlan());

        try {
            shopInfo.get().setEnableChangeBillingPlan(isBillingPlanLocked);
            shopInfoService.save(shopInfo.get());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while toggling billing plan lock for shop: " + shop);
        }

        log.info("Billing plan lock successfully toggled for shop: {}", shop);

        return ResponseEntity.status(HttpStatus.OK).body("Billing plan lock successfully toggled for shop: " + shop);
    }

    @GetMapping("/get-lock-status")
    public ResponseEntity<Boolean> getBillingPlanLock() throws Exception {
        log.info("Getting billing plan lock status for shop: {}", SecurityUtils.getCurrentUserLogin().get());

        boolean isPresent = SecurityUtils.getCurrentUserLogin().isPresent();
        if (!isPresent) {
            log.error("Shop is not present");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);
        boolean isBillingPlanLocked = BooleanUtils.isFalse(shopInfo.get().isEnableChangeBillingPlan());

        log.info("Billing plan lock status successfully fetched for shop: {}", shop);
        return ResponseEntity.status(HttpStatus.OK).body(isBillingPlanLocked);
    }

    @GetMapping("/get-lock-billing-plan-comments")
    public ResponseEntity<String> getLockBillingPlanComments() throws Exception {
        log.info("Getting billing plan lock comments for shop: {}", SecurityUtils.getCurrentUserLogin().get());

        boolean isPresent = SecurityUtils.getCurrentUserLogin().isPresent();
        if (!isPresent) {
            log.error("Shop is not present");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);
        String billingPlanLockComments = shopInfo.get().getLockBillingPlanComments();

        log.info("Billing plan lock comments successfully fetched for shop: {}", shop);
        return ResponseEntity.status(HttpStatus.OK).body(billingPlanLockComments);
    }

    @PostMapping("/set-lock-billing-plan-comments")
    public ResponseEntity<String> setLockBillingPlanComments(@RequestBody String comments) throws Exception {
        log.info("Setting billing plan lock comments for shop: {}", SecurityUtils.getCurrentUserLogin().get());

        boolean isPresent = SecurityUtils.getCurrentUserLogin().isPresent();
        if (!isPresent) {
            log.error("Shop is not present");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

        String shop = SecurityUtils.getCurrentUserLogin().get();
        Optional<ShopInfoDTO> shopInfo = shopInfoService.findByShop(shop);
        shopInfo.get().setLockBillingPlanComments(comments);
        shopInfoService.save(shopInfo.get());

        log.info("Billing plan lock comments successfully set for shop: {}", shop);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }


    @GetMapping("/resave-customer-payment")
    public ResponseEntity<Void> reSaveCustomerPayment(@RequestParam(value = "api_key") String apiKey,
                                                      @RequestParam(required = false) String commaSeparatedContractIds,
                                                      @RequestParam(required = false) boolean allSubscriptions) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)) {
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        Set<Long> contractIds = new HashSet<>();
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();

        if (BooleanUtils.isTrue(allSubscriptions)) {
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        } else {
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            try {
                Optional<CustomerPayment> customerPaymentOptional = customerPaymentRepository.findTop1ByCustomerId(subscriptionContractDetailsDTO.getCustomerId());

                if (customerPaymentOptional.isEmpty()) {

                    CustomerPaymentDTO customerPaymentDTO = new CustomerPaymentDTO();

                    if (subscriptionContractDetailsDTO.getCustomerId() != null) {

                        customerPaymentDTO.setCustomerUid(CommonUtils.generateRandomUid());
                        customerPaymentDTO.setTokenCreatedTime(ZonedDateTime.now());

                        customerPaymentDTO.setCardExpiryNotificationCounter(0L);
                        customerPaymentDTO.setCustomerId(subscriptionContractDetailsDTO.getCustomerId());
                        customerPaymentDTO.setShop(shop);
                        customerPaymentDTO.setToken(customerPaymentDTO.getToken());
                        customerPaymentDTO.setAdminGraphqlApiCustomerId(subscriptionContractDetailsDTO.getGraphCustomerId());


                        customerPaymentService.save(customerPaymentDTO);
                    } else {
                        log.error("Customer id is null for contract: {}", subscriptionContractDetailsDTO.getSubscriptionContractId());
                    }
                }
            } catch (Exception ex) {
                log.error("Error occurrred while resaving customer payment for contract: {}", subscriptionContractDetailsDTO.getSubscriptionContractId());
            }
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/associate-shopify-customer-with-authorize-net-customer")
    public ResponseEntity<Map<Long, String>> associateShopifyCustomerWithAuthorizeNetCustomer(@RequestParam("api_key") String apiKey,
                                                                                 @RequestParam(value = "auth-data", required = false) MultipartFile authDataFile) throws IOException {

        Map<Long, String> errorList = new HashMap<>();
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        CSVParser authDataCSVParser = null;
        Reader customerDataReader = new BufferedReader(new InputStreamReader(authDataFile.getInputStream()));
        authDataCSVParser = new CSVParser(customerDataReader, csvFormat);

        Map<String, String> profileIds;
        HashMap<String, Map<String, String>> authDetailsMap = new HashMap<>();

        for (CSVRecord csvRecord : authDataCSVParser) {
            profileIds = new HashMap<>();
            if(csvRecord.get("AnetPaymentProfileId").isEmpty()) {
                continue;
            }

            profileIds.put("paymentProfileId", csvRecord.get("AnetPaymentProfileId"));
            profileIds.put("profileId", csvRecord.get("AnetProfileId"));

            authDetailsMap.put(csvRecord.get("Email Address"), profileIds);
        }

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        int index = 0;
        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            log.info("--------------------- processing contract {} of {}", ++index, subscriptionContractDetailsDTOList.size());
            try {
                if(authDetailsMap.containsKey(scd.getCustomerEmail())) {

                    RemoteAuthorizeNetCustomerPaymentProfileInput remoteAuthorizeNetCustomerPaymentProfileInput = RemoteAuthorizeNetCustomerPaymentProfileInput.builder().customerProfileId(authDetailsMap.get(scd.getCustomerEmail()).get("profileId")).customerPaymentProfileId(authDetailsMap.get(scd.getCustomerEmail()).get("paymentProfileId")).build();

                    CustomerPaymentMethodRemoteInput customerPaymentMethodRemoteInput = CustomerPaymentMethodRemoteInput.builder().authorizeNetCustomerPaymentProfile(remoteAuthorizeNetCustomerPaymentProfileInput).build();

                    CustomerPaymentMethodRemoteCreateMutation customerPaymentMethodRemoteCreateMutation = new CustomerPaymentMethodRemoteCreateMutation(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + scd.getCustomerId(), customerPaymentMethodRemoteInput);
                    Response<Optional<CustomerPaymentMethodRemoteCreateMutation.Data>> optionalMutationResponse3 = shopifyGraphqlClient.getOptionalMutationResponse(customerPaymentMethodRemoteCreateMutation);

                    if (!CollectionUtils.isEmpty(optionalMutationResponse3.getErrors())) {
                        errorList.put(scd.getSubscriptionContractId(), "Error occurred while running create mutation for payment method" + optionalMutationResponse3.getErrors().get(0).getMessage());
                        log.info("Error occurted while running create mutation for payment method" + optionalMutationResponse3.getErrors().get(0).getMessage());
                        continue;
                    }

                    List<CustomerPaymentMethodRemoteCreateMutation.UserError> userErrors = optionalMutationResponse3.getData().map(d -> d.getCustomerPaymentMethodRemoteCreate().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethodRemoteCreate::getUserErrors).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                    if (!userErrors.isEmpty() && !userErrors.get(0).getMessage().equals("has already been taken")) {
                        errorList.put(scd.getSubscriptionContractId(), "Error occurted while running create mutation for payment method" + userErrors.get(0).getMessage());
                        log.info("Error occurted while running create mutation for payment method" + userErrors.get(0).getMessage());
                        continue;
                    }

                    String customerCreatedPaymentId = Objects.requireNonNull(optionalMutationResponse3.getData()).flatMap(d -> d.getCustomerPaymentMethodRemoteCreate().flatMap(e -> e.getCustomerPaymentMethod().map(CustomerPaymentMethodRemoteCreateMutation.CustomerPaymentMethod::getId))).orElse(null);

                    if (customerCreatedPaymentId != null) {
                        CustomerPaymentMethodsQuery customerPaymentMethodsQuery = new CustomerPaymentMethodsQuery(ShopifyIdPrefix.CUSTOMER_ID_PREFIX + scd.getCustomerId(), Input.optional(false));

                        Response<Optional<CustomerPaymentMethodsQuery.Data>> response = shopifyGraphqlClient.getOptionalQueryResponse(customerPaymentMethodsQuery);

                        List<String> paymentMethodIds = Objects.requireNonNull(response.getData()).map(d -> d.getCustomer().map(c -> c.getPaymentMethods().getEdges().stream().map(e -> e.getNode().getId()).collect(Collectors.toList())).orElse(new ArrayList<>())).orElse(new ArrayList<>());

                        if (paymentMethodIds.size() != 1) {
                            if (paymentMethodIds.size() > 1) {
                                if (paymentMethodIds.contains(customerCreatedPaymentId)) {
                                    paymentMethodIds.removeIf(paymentMethodId -> !customerCreatedPaymentId.equalsIgnoreCase(paymentMethodId));
                                } else {
                                    errorList.put(scd.getSubscriptionContractId(), "More than one Payment method found for Customer Id =" + scd.getCustomerId());
                                    log.info("More than one Payment method found for Customer Id =" + scd.getCustomerId());
                                    continue;
                                }
                            } else {
                                errorList.put(scd.getSubscriptionContractId(), "No Payment method found for Customer Id =" + scd.getCustomerId());
                                log.info("No Payment method found for Customer Id =" + scd.getCustomerId());
                                continue;
                            }
                        }
                        SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();
                        subscriptionDraftInputBuilder.paymentMethodId(paymentMethodIds.get(0));

                        SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                        SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, scd.getSubscriptionContractId(), subscriptionDraftInput);

                        if (!subscriptionContractUpdateResult.isSuccess()) {
                           errorList.put(scd.getSubscriptionContractId(), StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()));
                            log.info(StringEscapeUtils.escapeJson(subscriptionContractUpdateResult.getErrorMessage()));
                        }
                    }
                }
            }catch (Exception e){
                errorList.put(scd.getSubscriptionContractId(), "ex = " + e.getMessage() + ", ID = " + scd.getSubscriptionContractId());
                log.info("ex = " + e.getMessage() + ", ID = " + scd.getSubscriptionContractId());
            }
        }

        return ResponseEntity.ok().body(errorList);
    }

    @GetMapping("/contract-immediate-billing-attempt")
    public void contractImmediateBillingAttempt(@RequestParam(value = "api_key") String apiKey,
                                                @RequestParam(required = false) String commaSeparatedContractIds,
                                                @RequestParam(required = false) boolean allSubscriptions,
                                                @RequestParam String email) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if (org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)) {
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        Set<Long> contractIds = new HashSet<>();
        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();

        if (BooleanUtils.isTrue(allSubscriptions)) {
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        } else {
            contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        List<Long> attemptedContracts = new ArrayList<>();
        Map<Long, Long> attemptedBillingIds = new HashMap<>();
        for (SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList) {
            try {
                ZonedDateTime dateToCompare = null;
                List<SubscriptionBillingAttemptDTO> pastOrders = subscriptionBillingAttemptService.findByContractIdAndStatus(subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.SUCCESS);

                if (!CollectionUtils.isEmpty(pastOrders)) {
                    dateToCompare = pastOrders.stream().max(Comparator.comparing(SubscriptionBillingAttemptDTO::getAttemptTime)).map(SubscriptionBillingAttemptDTO::getAttemptTime).orElse(null);
                }

                if (Objects.isNull(dateToCompare)) {
                    dateToCompare = subscriptionContractDetailsDTO.getCreatedAt();
                }

                long days = ChronoUnit.DAYS.between(dateToCompare, ZonedDateTime.now(ZoneId.of("UTC")));

                if (days > 28) {
                    List<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTOList = subscriptionBillingAttemptService.findByContractIdAndStatusAndShop(shop, subscriptionContractDetailsDTO.getSubscriptionContractId(), BillingAttemptStatus.QUEUED);

                    Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTO = subscriptionBillingAttemptDTOList.stream().min(Comparator.comparing(SubscriptionBillingAttemptDTO::getBillingDate));

                    if (subscriptionBillingAttemptDTO.isPresent()) {
                        subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.attemptBilling(shop, subscriptionBillingAttemptDTO.get().getId(), ActivityLogEventSource.SYSTEM_EVENT);
                        attemptedContracts.add(subscriptionContractDetailsDTO.getSubscriptionContractId());
                        attemptedBillingIds.put(subscriptionContractDetailsDTO.getSubscriptionContractId(), subscriptionBillingAttemptDTO.get().getId());
                    }
                }
            }catch (Exception e){
                log.error("Error occurred while attempting immediate billing for shop:{}, contract:{}, error={}", shop, subscriptionContractDetailsDTO.getSubscriptionContractId(), ExceptionUtils.getStackTrace(e));
            }
        }
        exportAttemptedContractsStatus(shop, attemptedContracts, attemptedBillingIds, email);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void exportAttemptedContractsStatus(String shop, List<Long> attemptedContracts, Map<Long, Long> attemptedBillingIds, String email){
        log.info("Sending status update for attempted billing contracts for shop:{}", shop);

        try {
            Thread.sleep(300000L);

            String[] headers = {
                "Contract ID",
                "Billing attempt Time",
                "Billing Status"
            };
            File tempFile = File.createTempFile("Immediate-billing-status-export", ".csv");
            Writer writer = Files.newBufferedWriter(tempFile.toPath());

            CSVFormat csvFormat = CSVFormat.DEFAULT
                .withAutoFlush(true)
                .withHeader(headers);

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                for (Long contractId : attemptedContracts) {
                    try {
                        Long billingId = attemptedBillingIds.get(contractId);
                        Optional<SubscriptionBillingAttemptDTO> subscriptionBillingAttemptDTO = subscriptionBillingAttemptService.findById(billingId);
                        if (subscriptionBillingAttemptDTO.isPresent()) {
                            if (subscriptionBillingAttemptDTO.get().getStatus().equals(BillingAttemptStatus.SUCCESS)) {
                                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTO = subscriptionContractDetailsService.getSubscriptionByContractId(contractId);
                                if (subscriptionContractDetailsDTO.isPresent()) {
                                    ZonedDateTime nextBillingDate = SubscriptionUtils.getNextBillingDate(subscriptionContractDetailsDTO.get().getBillingPolicyIntervalCount(), subscriptionContractDetailsDTO.get().getBillingPolicyInterval(), subscriptionBillingAttemptDTO.get().getAttemptTime());
                                    subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(contractId, shop, nextBillingDate, ActivityLogEventSource.SYSTEM_EVENT);
                                }
                            }
                            csvPrinter.printRecord(
                                contractId,
                                subscriptionBillingAttemptDTO.get().getAttemptTime(),
                                subscriptionBillingAttemptDTO.get().getStatus()
                            );
                        }
                    } catch (Exception e) {
                        log.error("An error occurred while updating next billing date for billing attempted contract: {}", contractId);
                    }
                }
            }
            mailgunService.sendEmailWithAttachment(tempFile, "Immediate billing attempt status export", "Check attached csv file for status of bulk immediate billing attempt.", "subscription-support@appstle.com", shop, email);
        }catch (Exception e){
            log.error("Error occurred while sending status update for attempted billing contracts for shop:{}", shop);
        }
    }

    @GetMapping("/bulk-send-payment-update-email")
    public void bulkSendPaymentUpdateEmail(@RequestParam(value = "api_key") String apiKey,
                                           @RequestParam(required = false) String commaSeparatedContractIds,
                                           @RequestParam(required = false) boolean allSubscriptions){
        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        log.info("Start Sending Bulk payment update emails for shop: {}", shop);

        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        for(SubscriptionContractDetailsDTO subscriptionContractDetailsDTO : subscriptionContractDetailsDTOList){
            try {
                if(subscriptionContractDetailsDTO.getStatus().equalsIgnoreCase("cancelled")){
                    continue;
                }
                subscriptionContractDetailsService.updatePaymentInfo(subscriptionContractDetailsDTO.getSubscriptionContractId(), shop);

            }catch (Exception e){
                log.error("An error occurred while bulk sending payment update email to customer for contract= {}", subscriptionContractDetailsDTO.getSubscriptionContractId());
            }
        }

        log.info("Bulk payment update emails sent for shop: {}", shop);
    }

    @PostMapping(value = "/convert-contract-to-single-product-bab", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void convertContractToSingleProductBab(@RequestParam(value = "api_key") String apiKey,
                                                  @RequestParam("buildABoxUniqueRef") String uniqueRef,
                                                  @RequestPart("contract-data") MultipartFile contractDataFile) throws Exception{
        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);

        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().withIgnoreEmptyLines().withIgnoreHeaderCase();

        CSVParser contractDataCSVParser = null;
        Reader contractDataReader = new BufferedReader(new InputStreamReader(contractDataFile.getInputStream()));
        contractDataCSVParser = new CSVParser(contractDataReader, csvFormat);

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (CSVRecord csvRecord : contractDataCSVParser) {
            try {
                String contractId = csvRecord.get("ID");
                String productTitle = csvRecord.get("Source Product");
                String attributes = csvRecord.get("Product Name");

                if(org.apache.commons.lang3.StringUtils.isAnyBlank(contractId, productTitle, attributes)){
                    throw new Exception("csv fields cannot be blank");
                }

                Optional<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOOpt = subscriptionContractDetailsDTOList.stream().filter(dto -> dto.getSubscriptionContractId().toString().equals(contractId)).findFirst();

                if(subscriptionContractDetailsDTOOpt.isEmpty()){
                    throw new Exception("Contract data not found in DB for ID:"+contractId);
                }

                if(subscriptionContractDetailsDTOOpt.get().getStatus().equalsIgnoreCase("cancelled")){
                    throw new Exception("Contract cancelled for ID:"+contractId);
                }

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);
                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContract = subscriptionContractQueryResponse.getData().flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if(subscriptionContract.isEmpty()){
                    throw new Exception("Shopify Contract data not found for ID: "+contractId);
                }

                Optional<SubscriptionContractQuery.Node> line = subscriptionContract.get().getLines().getEdges().stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .filter(s -> s.getTitle().equalsIgnoreCase(productTitle)).findFirst();

                if(line.isEmpty()){
                    throw new Exception("Contract line item not found for ID: "+contractId+", Product: "+productTitle);
                }

                List<AttributeInfo> attributeInfoList = new ArrayList<>();
                attributeInfoList.add(new AttributeInfo("products", attributes));
                attributeInfoList.add(new AttributeInfo("_appstle-bb-id", uniqueRef));

                subscriptionContractDetailsResource.subscriptionContractUpdateLineItemAttributes(subscriptionContractDetailsDTOOpt.get().getSubscriptionContractId(), shop, line.get().getId(), attributeInfoList);

                SubscriptionContractDetailsDTO subscriptionContractDetailsDTO = subscriptionContractDetailsDTOOpt.get();
                subscriptionContractDetailsDTO.setSubscriptionType(SubscriptionType.BUILD_A_BOX_SINGLE_PRODUCT);
                subscriptionContractDetailsDTO.setSubscriptionTypeIdentifier(uniqueRef);
                subscriptionContractDetailsService.save(subscriptionContractDetailsDTO);
            }catch (Exception e){
                log.error("Error occurred while converting contract to single product BaB at line number: {}, error= {}", csvRecord.getRecordNumber(), e.getMessage());
            }
        }
    }

    @GetMapping("/subscriptions-first-order-attribute-sync")
    public void subscriptionsFirstOrderAttributeSync(@RequestParam("api_key") String apiKey,
                                                     @RequestParam(required = false) String commaSeparatedContractIds,
                                                     @RequestParam(required = false) boolean allSubscriptions) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        log.info("Start syncing subscription first order attributes for shop: {}", shop);

        if(org.apache.commons.lang3.StringUtils.isBlank(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        for (SubscriptionContractDetailsDTO subscriptionContractDetail : subscriptionContractDetailsDTOList) {
            try {

                if(subscriptionContractDetail.getStatus().equalsIgnoreCase("cancelled")){
                    log.info("Skipping first order attribute sync for contract:{}, cause = contract cancelled", subscriptionContractDetail.getSubscriptionContractId());
                    continue;
                }

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(subscriptionContractDetail.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                Optional<SubscriptionContractQuery.SubscriptionContract> subscriptionContractOptional = subscriptionContractQueryResponse.getData().flatMap(SubscriptionContractQuery.Data::getSubscriptionContract);

                if (subscriptionContractOptional.isEmpty()) {
                    log.info("Skipping first order attribute sync for contract:{}, cause = shopify contract not found", subscriptionContractDetail.getSubscriptionContractId());
                    continue;
                }
                SubscriptionContractQuery.SubscriptionContract subscriptionContract = subscriptionContractOptional.get();

                List<SubscriptionContractQuery.Node> subscriptionLineItems = Objects.requireNonNull(subscriptionContractQueryResponse
                        .getData())
                    .map(d -> d.getSubscriptionContract()
                        .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                        .map(SubscriptionContractQuery.Lines::getEdges)
                        .orElse(new ArrayList<>()))
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(SubscriptionContractQuery.Edge::getNode)
                    .collect(Collectors.toList());

                Long contractId = subscriptionContractDetail.getSubscriptionContractId();

                Boolean syncAttributes = false;

                OrderBriefQuery orderBriefQuery = new OrderBriefQuery(subscriptionContractDetail.getGraphOrderId());
                Response<Optional<OrderBriefQuery.Data>> orderQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderBriefQuery);
                List<OrderBriefQuery.CustomAttribute> customAttributes = orderQueryResponse.getData().map(OrderBriefQuery.Data::getOrder).flatMap(order -> order.map(OrderBriefQuery.Order::getCustomAttributes)).orElse(new ArrayList<>());
                Optional<String> note = orderQueryResponse.getData().map(OrderBriefQuery.Data::getOrder).flatMap(order -> order.get().getNote());

                SubscriptionDraftInput.Builder subscriptionDraftInputBuilder = SubscriptionDraftInput.builder();

                if (customAttributes.size() > 0) {
                    if (subscriptionContract.getCustomAttributes().isEmpty()) {
                        syncAttributes = true;
                        List<AttributeInput> attributeInputList = new ArrayList<>();
                        for (OrderBriefQuery.CustomAttribute customAttribute : customAttributes) {
                            AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                            attributeInputBuilder.key(customAttribute.getKey());
                            if (customAttribute.getValue().isPresent()) {
                                attributeInputBuilder.value(customAttribute.getValue().get());
                            }
                            attributeInputList.add(attributeInputBuilder.build());
                        }
                        subscriptionDraftInputBuilder.customAttributes(attributeInputList);
                    }
                }

                if (note.isPresent()) {
                    if (subscriptionContract.getNote().isEmpty()) {
                        syncAttributes = true;
                        subscriptionDraftInputBuilder.note(note.get());
                    }
                }

                if (syncAttributes) {
                    SubscriptionDraftInput subscriptionDraftInput = subscriptionDraftInputBuilder.build();

                    SubscriptionContractUpdateResult subscriptionContractUpdateResult = shopifyGraphqlSubscriptionContractService.updatedSubscriptionContractWithRetry(shopifyGraphqlClient, shop, contractId, subscriptionDraftInput);

                    if (!subscriptionContractUpdateResult.isSuccess()) {
                        log.error("An error occurred while syncing first order attributes for subscription contractId=" + contractId + " errorMessage=" + subscriptionContractUpdateResult.getErrorMessage());
                    }
                }

                SubscriptionContractUpdateMutation subscriptionContractUpdateMutation = new SubscriptionContractUpdateMutation(SUBSCRIPTION_CONTRACT_ID_PREFIX + contractId);

                Response<Optional<SubscriptionContractUpdateMutation.Data>> optionalResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionContractUpdateMutation);

                long countOfErrors = optionalResponse.getData()
                    .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                    .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getUserErrors)
                    .orElse(new ArrayList<>()).stream()
                    .map(SubscriptionContractUpdateMutation.UserError::getMessage)
                    .peek(message -> log.info("Update subscription contract is failed {} ", message)).count();

                if (countOfErrors == 0) {
                    // get draft Id from the response
                    Optional<String> optionalDraftId = optionalResponse.getData()
                        .flatMap(SubscriptionContractUpdateMutation.Data::getSubscriptionContractUpdate)
                        .map(SubscriptionContractUpdateMutation.SubscriptionContractUpdate::getDraft)
                        .map(draft -> draft.get().getId());

                    if (optionalDraftId.isPresent()) {
                        String draftId = optionalDraftId.get();

                        OrderLineItemsQuery orderLineItemsQuery = new OrderLineItemsQuery(subscriptionContractDetail.getGraphOrderId());

                        Response<Optional<OrderLineItemsQuery.Data>> orderLineItemsResponse = shopifyGraphqlClient.getOptionalQueryResponse(orderLineItemsQuery);

                        List<OrderLineItemsQuery.Node> orderLineItems = Objects.requireNonNull(orderLineItemsResponse.getData())
                            .map(d -> d.getOrder().map(OrderLineItemsQuery.Order::getLineItems)
                                .map(OrderLineItemsQuery.LineItems::getEdges)
                                .orElse(new ArrayList<>()))
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(OrderLineItemsQuery.Edge::getNode)
                            .collect(Collectors.toList());

                        SubscriptionLineUpdateInput.Builder subscriptionLineUpdateInputBuilder = SubscriptionLineUpdateInput.builder();

                        for (OrderLineItemsQuery.Node orderLineItem : orderLineItems) {

                            if (orderLineItem.getVariant().isEmpty() || orderLineItem.getCustomAttributes().isEmpty()) {
                                continue;
                            }

                            Optional<SubscriptionContractQuery.Node> subscriptionLineItem = subscriptionLineItems.stream().filter(node -> node.getVariantId().isPresent() && node.getVariantId().get().equals(orderLineItem.getVariant().get().getId())).findFirst();

                            if (subscriptionLineItem.isEmpty() || !subscriptionLineItem.get().getCustomAttributes().isEmpty()) {
                                continue;
                            }

                            List<AttributeInput> attributeInputList1 = new ArrayList<>();

                            for (OrderLineItemsQuery.CustomAttribute orderCustomAttribute : orderLineItem.getCustomAttributes()) {
                                AttributeInput.Builder attributeInputBuilder = AttributeInput.builder();
                                attributeInputBuilder.key(orderCustomAttribute.getKey());
                                if (orderCustomAttribute.getValue().isPresent()) {
                                    attributeInputBuilder.value(orderCustomAttribute.getValue().get());
                                }
                                attributeInputList1.add(attributeInputBuilder.build());
                            }
                            subscriptionLineUpdateInputBuilder.customAttributes(attributeInputList1);

                            SubscriptionLineUpdateInput subscriptionLineUpdateInput = subscriptionLineUpdateInputBuilder.build();

                            SubscriptionDraftLineUpdateMutation subscriptionDraftLineUpdateMutation = new SubscriptionDraftLineUpdateMutation(draftId, subscriptionLineItem.get().getId(), subscriptionLineUpdateInput);
                            Response<Optional<SubscriptionDraftLineUpdateMutation.Data>> optionalMutationResponse = shopifyGraphqlClient.getOptionalMutationResponse(subscriptionDraftLineUpdateMutation);

                            if (optionalMutationResponse.hasErrors()) {
                                log.error("An error occurred while syncing first order line attributes for subscription contractId={}, lineId={}, errorMessage=", contractId, subscriptionLineItem.get().getId(), optionalMutationResponse.getErrors().get(0).getMessage());
                            }

                            List<SubscriptionDraftLineUpdateMutation.UserError> optionalMutationResponseUserErrors = optionalMutationResponse.getData().map(d -> d.getSubscriptionDraftLineUpdate().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                            if (!optionalMutationResponseUserErrors.isEmpty()) {
                                log.error("An error occurred while syncing first order line attributes for subscription contractId={}, lineId={}, errorMessage=", contractId, subscriptionLineItem.get().getId(), optionalMutationResponseUserErrors);
                            }
                        }

                        SubscriptionDraftCommitMutation subscriptionDraftCommitMutation = new SubscriptionDraftCommitMutation(draftId);
                        Response<Optional<SubscriptionDraftCommitMutation.Data>> optionalDraftCommitResponse = shopifyGraphqlClient
                            .getOptionalMutationResponse(subscriptionDraftCommitMutation);

                        if (optionalDraftCommitResponse.hasErrors()) {
                            log.error("An error occurred while syncing first order line attributes for subscription contractId=" + contractId);
                        }

                        List<SubscriptionDraftCommitMutation.UserError> optionalDraftCommitResponseUserErrors = optionalDraftCommitResponse.getData().map(d -> d.getSubscriptionDraftCommit().map(f -> f.getUserErrors()).orElse(new ArrayList<>())).orElse(new ArrayList<>());
                        if (!optionalDraftCommitResponseUserErrors.isEmpty()) {
                            log.error("An error occurred while syncing first order line attributes for subscription contractId=" + contractId + " errorMessage=" + optionalDraftCommitResponseUserErrors);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error occurred while syncing attributes for subscription contract:" + subscriptionContractDetail.getSubscriptionContractId());
            }
        }
    }

    @GetMapping("/bulk-add-one-time-product")
    public void bulkAddOneTimeProduct(@RequestParam("api_key") String apiKey,
                                      @RequestParam(required = false) String commaSeparatedContractIds,
                                      @RequestParam(required = false) Boolean allSubscriptions,
                                      @RequestParam Long variantId,
                                      @RequestParam(required = false) Double price,
                                      HttpServletRequest request) throws Exception {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();
        String RequestURL = Optional.ofNullable(request).map(r -> r.getHeader("origin")).orElse("ORIGIN_URL_NOT_FOUND");

        String productVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(variantId);

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            try {

                if (scd.getStatus().equalsIgnoreCase("cancelled")) {
                    continue;
                }
                if(Objects.isNull(price)) {
                    subscriptionContractDetailsService.subscriptionContractsAddLineItem(scd.getSubscriptionContractId(), shop, 1, productVariantId, true, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
                } else {
                    subscriptionContractDetailsService.subscriptionContractAddLineItem(scd.getSubscriptionContractId(), shop, 1, productVariantId, price, true, RequestURL, ActivityLogEventSource.MERCHANT_EXTERNAL_API);
                }

            } catch (Exception e) {
                log.error("Error occured while adding one time product. error={}", e.getMessage());
            }
        }
    }

    @GetMapping("/sync-shop-and-userinfo")
    public void syncShopAndUserInfo(@RequestParam(value = "api_key") String apiKey) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        ShopifyAPI api = commonUtils.prepareShopifyResClient(shop);

        Shop shopDetails = api.getShopInfo().getShop();

        Optional<ShopInfoDTO> optionalShopInfo = shopInfoService.findByShop(shop);

        if (optionalShopInfo.isPresent()) {
            try {

                ShopInfoDTO shopInfoDTO = optionalShopInfo.get();

                shopInfoDTO.setCountryCode(shopDetails.getCountryCode());
                shopInfoDTO.setCountryName(shopDetails.getCountryName());

                String timezone = shopDetails.getTimezone();

                timezone = timezone.contains("(GMT-05:00) Eastern Time (US & Canada)") ? "(GMT-05:00) EST" : timezone;
                shopInfoDTO.setShopTimeZone(timezone);
                shopInfoDTO.setShopifyPlanDisplayName(shopDetails.getPlanDisplayName());
                shopInfoDTO.setShopifyPlanName(shopDetails.getPlanName());
                shopInfoDTO.setIanaTimeZone(shopDetails.getIanaTimezone());

                shopInfoService.save(shopInfoDTO);

            } catch (Exception e) {
                log.error("Exception occurred while syncing shop info for shop: {}, error:{}", shop, ExceptionUtils.getStackTrace(e));
            }
        }

        Optional<User> optionalUser = userRepository.findOneByLogin(shop);

        if (optionalUser.isPresent()) {
            try {
                String email = Optional.ofNullable(shopDetails.getCustomerEmail()).orElse(shopDetails.getEmail());

                User user = optionalUser.get();

                user.setFirstName(org.apache.commons.lang3.StringUtils.isEmpty(shopDetails.getName()) ? "UNKNOWN" : shopDetails.getName());
                user.setEmail(email);

                userRepository.save(user);
            } catch (Exception e) {
                log.error("Exception occurred while syncing user info for shop: {}, error:{}", shop, ExceptionUtils.getStackTrace(e));
            }
        }
    }


    @PostMapping("/onboarding-info-entity-creation")
    public ResponseEntity<String> createOnboardingInfoEntities() {
        List<ShopInfoDTO> shopInfoList = shopInfoService.findAll();
        for (ShopInfoDTO shopInfo : shopInfoList) {
            asyncExecutor.execute(() -> {

                String shop = shopInfo.getShop();
                Optional<OnboardingInfoDTO> onboardingInfoDTOOptional = onboardingInfoService.findByShop(shop);
                if (onboardingInfoDTOOptional.isEmpty()) {
                    OnboardingInfoDTO newOnboardingInfoDTO = new OnboardingInfoDTO();
                    newOnboardingInfoDTO.setShop(shop);
                    newOnboardingInfoDTO.setUncompletedChecklistSteps(List.of(OnboardingChecklistStep.values()));
                    newOnboardingInfoDTO.setCompletedChecklistSteps(List.of());
                    newOnboardingInfoDTO.setChecklistCompleted(true);
                    onboardingInfoService.save(newOnboardingInfoDTO);
                }
            });

        }
        return ResponseEntity.ok("Created Onboarding Info entities for all shops without one");
    }

    @GetMapping("/get-hard-white-listed-shop-names")
    public List<String> getWhiteListedShopNames() {
        return subscribeItScriptUtils.getWhitelistedShopNames();
    }

    @PostMapping("/resync-build-a-box-discount")
    public void resyncBABDiscount(@RequestParam("api_key") String apiKey,
                                  @RequestParam(required = false) String commaSeparatedContractIds,
                                  @RequestParam(required = false) Boolean allSubscriptions) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        log.info("Rest request for syncing build a box discount for shop {}", shop);

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if(BooleanUtils.isTrue(allSubscriptions)){
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        }else{
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            try {
                log.info("Syncing BAB discount for contract {}", scd.getSubscriptionContractId());
                subscriptionContractDetailsService.resyncBuildABoxDiscount(shop, scd.getSubscriptionContractId());
            } catch (Exception e) {
                log.error("Error occured while syncing BAB discount for contract {} with exception {}", scd.getSubscriptionContractId(), e.getMessage());
            }
        }
    }

    @GetMapping("/update-free-product-with-one-time-product")
    public void updateFreeProductWithOneTimeProduct(@RequestParam("api_key") String apiKey,
                                                 @RequestParam(required = false) String commaSeparatedContractIds,
                                                 @RequestParam(required = false) Boolean allSubscriptions,
                                                 @RequestParam Long variantId) {

        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        } else {
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        String gqlVariantId = ShopifyGraphQLUtils.getGraphQLVariantId(variantId);

        int counter = 0;

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            try {
                log.info("Iterating record {} of total records {}", counter++, subscriptionContractDetailsDTOList.size());
                String gqlContractId = scd.getGraphSubscriptionContractId();

                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(gqlContractId);
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                List<SubscriptionContractQuery.Node> existingLineItems =
                    requireNonNull(subscriptionContractQueryResponse.getData())
                        .map(d -> d.getSubscriptionContract()
                            .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                            .map(SubscriptionContractQuery.Lines::getEdges)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .filter(g -> g.getVariantId().isPresent() && g.getVariantId().get().equals(gqlVariantId))
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(existingLineItems)) {
                    continue;
                }

                for(SubscriptionContractQuery.Node existingLineItem : existingLineItems) {
                    if(existingLineItem.getCustomAttributes().isEmpty()) {
                        continue;
                    }
                    boolean isFreeProduct = commonUtils.isAttributePresent(existingLineItem.getCustomAttributes(), AppstleAttribute.FREE_PRODUCT.getKey());
                    if(isFreeProduct) {
                        log.info("updating contract {} by adding one time product attribute", scd.getSubscriptionContractId());
                        List<AttributeInfo> attributeInfoList = new ArrayList<>();
                        attributeInfoList.add(new AttributeInfo(AppstleAttribute.ONE_TIME_PRODUCT.getKey(), "true"));
                        attributeInfoList.add(new AttributeInfo(AppstleAttribute.FREE_PRODUCT.getKey(), "true"));
                        subscriptionContractDetailsResource.subscriptionContractUpdateLineItemAttributes(scd.getSubscriptionContractId(), shop, existingLineItem.getId(), attributeInfoList);
                    }
                }

            } catch (Exception e) {
                log.info("Error while updating attribute for contract={}, ex={}", scd.getSubscriptionContractId(), ExceptionUtils.getStackTrace(e));
            }
        }

    }

    @GetMapping("/update-iana-timezone")
    public boolean generateApiKey() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            try {
                Optional<ShopInfoDTO> optionalShopInfo = shopInfoService.findByShop(socialConnection.getUserId());
                if (optionalShopInfo.isPresent()) {
                    ShopInfoDTO shopInfo = optionalShopInfo.get();
                    if(ObjectUtils.isEmpty(shopInfo.getIanaTimeZone())) {
                        ShopifyAPI api = commonUtils.prepareShopifyResClient(shopInfo.getShop());

                        Shop shopDetails = api.getShopInfo().getShop();
                        shopInfo.setIanaTimeZone(shopDetails.getIanaTimezone());
                        shopInfoService.save(shopInfo);
                    }
                } else {

                }
            } catch (Exception e) {

            }
        }
        return true;
    }

    @GetMapping("/update-script-version")
    public boolean updateScriptVersion() {
        List<SocialConnection> socialConnections = socialConnectionService.findAll();

        for (SocialConnection socialConnection : socialConnections) {
            try {
                Optional<ShopInfoDTO> optionalShopInfo = shopInfoService.findByShop(socialConnection.getUserId());
                if (optionalShopInfo.isPresent()) {
                    ShopInfoDTO shopInfo = optionalShopInfo.get();
                    if (ScriptVersion.V2.equals(shopInfo.getScriptVersion())) {
                        shopInfo.setScriptVersion(ScriptVersion.V1);
                        shopInfoService.save(shopInfo);
                        subscribeItScriptUtils.createOrUpdateFileInCloud(shopInfo.getShop());
                    }
                } else {

                }
            } catch (Exception e) {

            }
        }
        return true;
    }

    @GetMapping("/sync-next-order-dates-with-selling-plan")
    public void syncNextOrderDatesWithSellingPlan(@RequestParam("api_key") String apiKey,
                                                  @RequestParam(required = false) String commaSeparatedContractIds,
                                                  @RequestParam(required = false) Boolean allSubscriptions) throws Exception {
        String shop = commonUtils.getShopByAPIKey(apiKey).get();

        if(StringUtils.isEmpty(commaSeparatedContractIds) && BooleanUtils.isNotTrue(allSubscriptions)){
            throw new BadRequestAlertException("Contract Ids cant be empty when all subscription flag is false or empty", "", "idnull");
        }

        List<SubscriptionContractDetailsDTO> subscriptionContractDetailsDTOList = new ArrayList<>();
        if (BooleanUtils.isTrue(allSubscriptions)) {
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByShop(shop);
        } else {
            Set<Long> contractIds = Arrays.stream(commaSeparatedContractIds.split(",")).filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(String::trim).map(Long::parseLong).collect(Collectors.toSet());
            subscriptionContractDetailsDTOList = subscriptionContractDetailsService.findByContractIdIn(contractIds);
        }

        ShopifyGraphqlClient shopifyGraphqlClient = commonUtils.prepareShopifyGraphqlClient(shop);

        int counter = 0;

        for (SubscriptionContractDetailsDTO scd : subscriptionContractDetailsDTOList) {
            try {

                log.info("Iterating record {} out of total {}", counter++, subscriptionContractDetailsDTOList.size());

                if (!scd.getStatus().equalsIgnoreCase("active")) {
                    continue;
                }

                List<SubscriptionBillingAttempt> reccurringOrders = subscriptionBillingAttemptRepository.findByShopAndContractIdAndStatusIn(shop, scd.getSubscriptionContractId(), Arrays.asList(BillingAttemptStatus.PROGRESS, BillingAttemptStatus.IMMEDIATE_TRIGGERED, BillingAttemptStatus.REQUESTING, BillingAttemptStatus.SUCCESS, BillingAttemptStatus.FAILURE, BillingAttemptStatus.CONTRACT_PAUSED, BillingAttemptStatus.CONTRACT_CANCELLED, BillingAttemptStatus.CONTRACT_PAUSED_MAX_CYCLES, BillingAttemptStatus.SHOPIFY_EXCEPTION, BillingAttemptStatus.SKIPPED_DUNNING_MGMT, BillingAttemptStatus.CONTRACT_ENDED, BillingAttemptStatus.SOCIAL_CONNECTION_NULL, BillingAttemptStatus.SKIPPED_INVENTORY_MGMT, BillingAttemptStatus.SKIPPED, BillingAttemptStatus.AUTO_CHARGE_DISABLED));

                if (reccurringOrders.size() > 0) {
                    continue;
                }
                SubscriptionContractQuery subscriptionContractQuery = new SubscriptionContractQuery(scd.getGraphSubscriptionContractId());
                Response<Optional<SubscriptionContractQuery.Data>> subscriptionContractQueryResponse = shopifyGraphqlClient.getOptionalQueryResponse(subscriptionContractQuery);

                List<SubscriptionContractQuery.Node> existingLineItems =
                    requireNonNull(subscriptionContractQueryResponse.getData())
                        .map(d -> d.getSubscriptionContract()
                            .map(SubscriptionContractQuery.SubscriptionContract::getLines)
                            .map(SubscriptionContractQuery.Lines::getEdges)
                            .orElse(new ArrayList<>()))
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(SubscriptionContractQuery.Edge::getNode)
                        .filter(g -> g.getSellingPlanId().isPresent())
                        .collect(Collectors.toList());

                String sellingPlanId = existingLineItems.get(0).getSellingPlanId().get();

                FrequencyInfoDTO frequencyInfoDTO = subscriptionGroupService.getSellingPlanById(shop, ShopifyGraphQLUtils.getSellingPlanId(sellingPlanId));

                FrequencyIntervalUnit frequencyIntervalUnit;
                Integer intervalCount;
                long daysToAdd = 0;
                if (frequencyInfoDTO.isFreeTrialEnabled()) {
                    frequencyIntervalUnit = frequencyInfoDTO.getFreeTrialInterval();
                    intervalCount = frequencyInfoDTO.getFreeTrialCount();
                } else {
                    frequencyIntervalUnit = frequencyInfoDTO.getFrequencyInterval();
                    intervalCount = frequencyInfoDTO.getFrequencyCount();
                }

                if (frequencyIntervalUnit.equals(FrequencyIntervalUnit.DAY)) {
                    daysToAdd = intervalCount;
                } else if (frequencyIntervalUnit.equals(FrequencyIntervalUnit.WEEK)) {
                    daysToAdd = 7L * intervalCount;
                } else if (frequencyIntervalUnit.equals(FrequencyIntervalUnit.MONTH)) {
                    daysToAdd = 30L * intervalCount;
                } else if (frequencyIntervalUnit.equals(FrequencyIntervalUnit.YEAR)) {
                    daysToAdd = 365L * intervalCount;
                }

                if (scd.getCreatedAt().plusDays(daysToAdd).compareTo(scd.getNextBillingDate()) < 0) {

                    ZonedDateTime nextBillingDate = scd.getCreatedAt().plusDays(daysToAdd);
                    log.info("Updating next billing date for contract {}", scd.getSubscriptionContractId());

                    ShopInfoDTO shopInfoDTO = shopInfoService.findByShop(shop).get();

                    if (ObjectUtils.allNotNull(shopInfoDTO.getZoneOffsetHours(), shopInfoDTO.getZoneOffsetMinutes())) {
                        ZoneId zoneId = CommonUtils.getZoneIdFromOffset(shopInfoDTO.getZoneOffsetHours().intValue(), shopInfoDTO.getZoneOffsetMinutes().intValue());
                        nextBillingDate = nextBillingDate.withZoneSameInstant(zoneId);
                    }

                    if (ObjectUtils.allNotNull(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute())) {
                        nextBillingDate = nextBillingDate.with(LocalTime.of(shopInfoDTO.getLocalOrderHour(), shopInfoDTO.getLocalOrderMinute()));
                    } else if (shopInfoDTO.getRecurringOrderHour() != null && shopInfoDTO.getRecurringOrderMinute() != null) {
                        nextBillingDate = nextBillingDate.with(LocalTime.of(shopInfoDTO.getRecurringOrderHour(), shopInfoDTO.getRecurringOrderMinute()));
                    }

                    nextBillingDate = nextBillingDate.withZoneSameInstant(ZoneId.of("UTC"));

                    subscriptionContractDetailsService.subscriptionContractUpdateNextBillingDate(scd.getSubscriptionContractId(), shop, nextBillingDate, ActivityLogEventSource.SYSTEM_EVENT);
                }

            } catch(Exception e) {
                log.error("Error occurred while updating next billing date for contract : {}", scd.getSubscriptionContractId());
            }
        }
    }

    private class MemoryStats {
        private long heapSize;
        private long heapMaxSize;
        private long heapFreeSize;

        public void setHeapSize(long heapSize) {
            this.heapSize = heapSize;
        }

        public long getHeapSize() {
            return heapSize;
        }

        public void setHeapMaxSize(long heapMaxSize) {
            this.heapMaxSize = heapMaxSize;
        }

        public long getHeapMaxSize() {
            return heapMaxSize;
        }

        public void setHeapFreeSize(long heapFreeSize) {
            this.heapFreeSize = heapFreeSize;
        }

        public long getHeapFreeSize() {
            return heapFreeSize;
        }
    }
}
