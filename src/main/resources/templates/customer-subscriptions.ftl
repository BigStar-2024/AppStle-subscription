<div class="appstle_preloader appstle_loadersmall"></div>
<div class="appstle_myProduct"></div>
<style><!--
    .accordion {
        background-color: #fff;
        color: #444;
        cursor: pointer;
        padding: 16px;
        width: 100%;
        border: none;
        text-align: left;
        outline: none;
        font-size: 18px;
        transition: 0.4s;
        border: 2px solid #ddd;
        border-radius: 5px;
        padding-left: 0;
        margin-top: 20px;
    }

    .accordion:first-child {
        margin-top: 0;
    }

    .appstle_accordian_line_1 {
        width: 18px;
        border-bottom: 2px solid #ddd;
    }

    .appstle_accordian_line_2 {
        flex-grow: 1;
        border-bottom: 2px solid #ddd;
        margin-right: 10px;
    }

    .text-center {
        text-align: center!important;
    }
    /* .accordion:hover {
        background-color: #eee;
    } */

    /* .accordion:after {
        content: '\002B';
        color: #777;
        font-weight: bold;
        float: right;
        margin-left: 5px;
    } */

    .accordion.active {
        padding-left: 0;
        border-color: #444;
    }

    .appstle_subscription_contract_detail_toggle_wrapper {
        cursor: pointer;
    }

    .appstle_subscription_contract_detail_toggle_wrapper.active .appstle_contract_see_more {
        display: none;
    }

    .appstle_subscription_contract_detail_toggle_wrapper.active .appstle_contract_hide_more {
        display: block !important;
    }

     .appstle_subscription_contract_detail_toggle_wrapper.active .lnr-chevron-right {
        transform: rotate(90deg);
    }

    .accordion div {
        opacity: 0;
    }

    /* .accordion.active div {
        opacity: 1;
    } */

    .lnr-chevron-right {
        transition: all 0.5s;
    }

    .accordion.active .lnr-chevron-right {
        transform: rotate(90deg);
        color: #444 !important;
    }

    .panel {
        /* padding: 0 18px; */
        background-color: white;
        overflow: hidden;

    }

    .appstle_myProduct {
        visibility: hidden;
    }

    .appstle_preloader {
        width: 60px;
        height: 60px;
        position: absolute;
        top: 70px;
        left: calc(50% - 15px);
        z-index: 98765;
    }

    .appstle_statusBadge {
        display: inline-block;
        padding: .25em .4em;
        font-size: 75%;
        font-weight: 700;
        line-height: 1;
        text-align: center;
        white-space: nowrap;
        vertical-align: baseline;
        border-radius: .25rem;
    }

    .appstle_status_ACTIVE {
        padding: 5px 15px;
        color: #fff;
        background-color: #28a745;
    }

    .appstle_status_CANCELLED {
        color: #fff;
        background-color: #6c757d;
    }

    .appstle_status_QUEUED {
        color: #fff;
        background-color: #28a745;
    }

    .appstle_status_SKIPPED {
        color: #fff;
        background-color: #6c757d;
    }

    .appstle_sub-title {
        font-size: 18px;
        font-weight: bold;
        text-transform: uppercase;
        letter-spacing: .1em;
        margin: 8px !important;
    }

    @media screen and (max-width: 1024px) {
        .appstle_sub-title {
            font-size: 13px;
        }
    }

    .appstle_orderDate_CANCELLED {
        display: none;
    }

    .appstle_upcomingAcc_CANCELLED {
        display: none;
    }

    .appstle_cancelbtn_CANCELLED {
        display: none;
    }

    .appstle_orderFrequency_CANCELLED {
        display: none;
    }

    .hideSkipbtn_SKIPPED {
        display: none;
    }

    .appstle_skipShipmentFlag_false,
    .appstle_pauseResumeSubscriptionFlag_false,
    .appstle_changeNextOrderDateFlag_false,
    .appstle_changeOrderFrequencyFlag_false,
    .appstle_createAdditionalOrderFlag_false,
    .appstle_cancelSubscriptionFlag_false {
        display: none!important;
    }

    .appstle_order-detail_cancel-button,
    .appstle_order-detail_update-button,
    .appstle_cancelButton,
    .appstle_deleteButton,
    .appstle_updateButton,
    .appstle_editButton,
    .appstle_skipOrderButton,
    .appstle_updatePaymentButton,
    .appstle_cencelSubscription,
    .appstle_show_add_products_button,
    .appstle_edit_shipping_cancel-button,
    .appstle_edit_shipping_update-button,
    .appstle_pause_subscription_button,
    .appstle_resume_subscription_button,
    .appstle_addProduct_type_next_button,
    .appstle_addProduct_type_cancel_button {
        padding: 6px 12px;
        border: none;
        border-radius: 4px;
        color: white;
        background-color: #242222;
        font-size: 12px;
        border: 1px solid #242222;
        margin: 10px 0;
    }

    @media screen and (max-width: 1024px) {
        .appstle_order-detail_cancel-button,
        .appstle_order-detail_update-button,
        .appstle_cancelButton,
        .appstle_deleteButton,
        .appstle_updateButton,
        .appstle_editButton,
        .appstle_skipOrderButton,
        .appstle_updatePaymentButton,
        .appstle_cencelSubscription,
        .appstle_show_add_products_button,
        .appstle_hide_add_products_button,
        .appstle_product-search-button,
        .ppstle_pause_subscription_button,
        .appstle_resume_subscription_button,
        .appstle_addProduct_type_next_button,
        .appstle_addProduct_type_cancel_button {
            font-size: 10px
        }
    }


    .appstle_buttonGroup {
        display: flex;
        margin-top: 15px;
        justify-content: flex-end;
        align-items: center;
    }

    .appstle_form_margin {
        margin: 8px;
    }
    .appstle_ml-10 {
        margin-left: 10px;
    }
    .appstle_deleteButton {
        background-color: white;
        border: 1px solid #eb3023;
        color: #eb3023;
    }

    .appstle_edit_shipping_cancel-button,
    .appstle_order-detail_cancel-button,
    .appstle_cancelButton {
        background-color: white;
        border: 1px solid #242222;
        color: #242222;
    }
    .appstle_skipOrderButton:hover,
    .appstle_skipOrderButton,
    .appstle_resume_subscription_button,
    .appstle_resume_subscription_button:hover {
        background-color: #3ac47d !important;
        border: 1px solid #3ac47d !important;
        color: #fff !important;
    }

    .appstle_updatePaymentButton:hover,
    .appstle_updatePaymentButton {
        background-color: #3ac47d !important;
        border: 1px solid #3ac47d !important;
        color: #fff !important;
    }

    .appstle_show_add_products_button,
    .appstle_show_add_products_button:hover,
    .appstle_addProduct_type_next_button,
    .appstle_addProduct_type_next_button:hover {
        background-color: #545cd8!important;
        border: 1px solid #545cd8!important;
    }


    .appstle_cencelSubscription:hover,
    .appstle_cencelSubscription,
    .appstle_pause_subscription_button,
    .appstle_pause_subscription_button:hover,
    .appstle_addProduct_type_cancel_button,
    .appstle_addProduct_type_cancel_button:hover {
        background-color: #d92550!important;
        border: 1px solid #d92550!important;
    }




    .appstle_form_group {
        margin-top: 15px;
    }

    .appstle_form_group select, .appstle_form_group input {
        width: 100%;
    }

    .appstle_product_search_wrapper {
        margin: 30px 0;
        display: flex;
        flex-direction: column;
    }

    .appstle_product-search-select {
        flex-grow: 1;
    }

    .appstle_delete_model_nobtn {
        padding: 5px 20px;
        background: #0d4f0d;
        border: none;
        border-radius: 8px;
        color: #fff;
        font-size: 12px;
    }

    .appstle_delete_model_yesbtn {
        padding: 5px 20px;
        background: #a13333;
        border: none;
        border-radius: 8px;
        color: #fff;
        font-size: 12px;
    }

    .appstle_order-detail-edit-button{
        padding: 5px 15px;
        border: none;
        border-radius: 4px;
        color: white;
        background-color: #242222;
    }

    .appstle_hide_add_products_button,
    .appstle_hide_add_products_button:hover,
    .appstle_product-search-button {
        padding: 0 12px;
        border: none;
        border-radius: 4px;
        color: white;
        background-color: #545cd8;
        font-size: 12px;
    }

    .appstle_hide_add_products_button:active,
    .appstle_hide_add_products_button:focus,
    .appstle_hide_add_products_button,
    .appstle_hide_add_products_button:hover {
        background-color: #d92550 !important;
    }



    .select2-container, .select2-container--default {
        flex-grow: 1;
    }

    .appstle_input_error {
      color: #eb3023;
      font-size: 10px;
      margin-top: 5px;
    }

    .appstle_order_detail_header {
        padding: 20px;
        background-color: #eee;
        margin-top: 34px;
        display: flex;
        font-weight: bold;
        font-size: 20px;
        margin-bottom: 10px;
    }

    .appstle_order_detail_row {
        display: flex;
        justify-content: space-between;
        padding: 10px 0px;
        align-items: center;
    }

    .appstle_loadersmall {
        border: 5px solid #f3f3f3;
        -webkit-animation: appstle_spin 1s linear infinite;
        animation: appstle_spin 1s linear infinite;
        border-top: 5px solid #555;
        border-radius: 50%;
        width: 60px;
        height: 60px;
        margin: 0 auto;
    }

    .appstle_loaderTiny {
        border: 2px solid #f3f3f3;
        -webkit-animation: appstle_spin 1s linear infinite;
        animation: appstle_spin 1s linear infinite;
        border-top: 2px solid #555;
        border-radius: 50%;
        width: 18px;
        height: 18px;
        margin: 0 auto;
    }

    @keyframes appstle_spin_tiny {
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

    /* The Modal (background) */
.appstle_delete_modal {
 text-align: center;
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 20; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%; /* Full width */
  height: 100%; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.appstle_delete_modal-content {
        text-align: center;
        background-color: #fefefe;
        margin: auto;
        padding: 20px;
        border: 1px solid #888;
      position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background-color: white;
        padding: 1rem 1.5rem;
        width: 27rem;
        border-radius: 0.5rem;
}

/* The Close Button */
.appstle_model_close {
    margin: 1px;
  color: #aaaaaa;
    float: right;
  font-size: 28px;
  font-weight: bold;
   text-align:right;
    width: 1.5rem;
    line-height: 1.5rem;
    text-align: center;
    cursor: pointer;
    border-radius: 0.25rem;
    background-color: lightgray;
}

.appstle_model_close:hover,
.appstle_model_close:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

    @-webkit-keyframes appstle_spin {
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

    @media screen and (max-width: 767px) {
        .appstle_product_wrapper {
            flex-direction: column;
        }
    }

    .appstle_product_wrapper {
        display: flex;
        margin-top: 16px;
        /* background-color: #eeeeee; */
        box-shadow: 0 0px 4px 0 rgb(0 0 0 / 15%);
        padding: 15px;
        border-radius: 5px;
        align-items: center;
        border: 2px solid #ddd;
    }


    .appstle_SKIPPED_status,
    .appstle_CANCELLED_status,
    .appstle_QUEUED_status,
    .appstle_SCHEDULED_status,
    .appstle_ACTIVE_status,
    .appstle_PAUSED_status {
        background-color: #3ac47d;
        color: white;
        padding: 3px 10px;
        border-radius: 4px;
        font-size: 10px;
        letter-spacing: 0.3px;
        margin-left: 5px;
        line-height: 1;
    }

    .appstle_QUEUED_status,
    .appstle_SCHEDULED_status,
    .appstle_PAUSED_status {
        background-color: #f7b924!important;
    }

    .appstle_SKIPPED_status,
    .appstle_CANCELLED_status {
        background-color: #d92550!important;
    }

    .appstle_edit_wrapper {
        margin-top: 20px
    }

    .appstle_edit_wrapper form {
        display: flex;
        justify-content: space-between;
    }


    .appstle_edit_wrapper .appstle_form_group {
        width: calc(50% - 10px);
    }

    .appstle_edit_total {
        margin-right: 30px;
        font-size: 21px;
        letter-spacing: 0.5px;
    }



    .appstle_subscription_image_wrapper {
        display: flex;
        flex-wrap: wrap;
    }

    .appstle_subscription_image_wrapper img {
    flex-basis: 46%;
        margin: 2%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .appstle_subscription_detail {
        padding-right: 30px;
        min-height: 200px;
    }

    .grid {}

.appstle_subscription_image_wrapper {
    display: flex;
    flex-wrap: wrap;
}

.appstle_subscription_image_wrapper img {
   flex-basis: 46%;
   margin: 2%;
   display: flex;
   align-items: center;
   justify-content: center;
}

.appstle_subscription_detail {
    padding-right: 30px;
    min-height: 200px;
    display: flex;
    flex-direction: column;
}

.grid {}

.appstle_order_detail_row {
    margin: 0 8px 0px 8px;
    padding: 0;
    justify-content: flex-start;
    min-height: 30px;
}

@media screen and (max-width: 1024px) {
    .appstle_order_detail_row {
        min-height: 15px;
    }
}

.appstle_font_size {
    font-size: 13px !important;
}

@media screen and (max-width: 1024px) {
    .appstle_font_size {
    font-size: 10px !important;
    }
}



span {}

.appstle_next_oder_date_wrapper {
    display: flex;
    align-items: center;
    flex-grow: 1;
}

.appstle_ml-auto {
    margin-left: auto;
}

.appstle_ml-auto {
    margin-left: auto;
}

.appstle_mb-0 {
    margin-bottom: 0 !important;
}

.appstle_order-detail-edit-button {
    background-color: transparent;
    color: black;
}

.appstle_subscription_contract_title {
    display: flex;
    align-items: center;
    /* margin-bottom: 6px !important; */
}



@media screen and (max-width: 767px) {
    .appstle_subscription_payment_wrapper {
        flex-direction: column;

    }
}

.appstle_subscription_payment_wrapper {
    display: flex;
    width: 100%;
    align-items: center;
    justify-content: space-between;

}

@media screen and (max-width: 750px) {
    .appstle_subscription_contract_content_wrapper_mobile {
        padding-left: 0;
        padding-right: 0;
    }
}



.appstle_subscription_flex_override {
        display: flex;
    }

@media screen and (max-width: 750px) {
    .appstle_subscription_flex_override {
        display: flex;
        flex-direction: column;
        padding-left: 0;
        padding: 0 18px;
    }
}

.appstle_address_item {
    margin-bottom: 20px;
}

.appstle_address_item input {
    width: 100%;
}

.appstle_address_header_title {
    font-size: 28px;
    text-align: left;
    align-self: flex-start;
    margin-bottom: 10px;
}

.appstle_play_icon {
    width: 6px;
    height: 6px;
    border-top: 4px solid transparent;
    border-left: 8px solid #fff;
    border-bottom: 4px solid transparent;
    position: relative;
    left: 1px;
}
.appstle_pause_icon {
    width: 7px;
    height: 7px;
    border-right: 3px solid #fff;
    border-left: 3px solid #fff;
    display: inline-block;
}

 .plans {
    display: flex;
    justify-content: space-between;
    box-sizing: border-box;
    background: #fff;
    border-radius: 10px;
    align-items: center;
    flex-wrap: wrap;
}

 .plans .plan input[type=radio] {
    position: absolute;
    opacity: 0;
    cursor: pointer;
    width: 100%;
    height: 100%;
    z-index: 1;
}

 .plans .plan {
    cursor: pointer;
    width: 49%;
    position: relative;
}

 .plans .plan .plan-content {
    display: flex;
    padding: 20px;
    box-sizing: border-box;
    border: 2px solid #e1e2e7;
    border-radius: 10px;
    transition: box-shadow .4s;
    position: relative;
    height: 100%
}

 .plans .plan .plan-content img {
    margin-right: 20px;
    height: 72px
}

 .plans .plan .plan-details span {
    margin-bottom: 10px;
    display: block;
    font-size: 16px;
    line-height: 24px;
    color: #252f42;
    font-weight: 700;
}



 .plans .plan .plan-details p {
    color: #646a79;
    font-size: 14px;
    line-height: 1.5;
}

 .plans .plan .plan-content:hover {
    box-shadow: 0 3px 5px 0 #e8e8e8
}

 .plans .plan input[type=radio]:checked+.plan-content:after {
    content: "";
    position: absolute;
    height: 12px;
    width: 12px;
    background: #216fe0;
    right: 20px;
    top: 20px;
    border-radius: 100%;
    border: 2px solid #fff;
    box-shadow: 0 0 0 2px #06f
}

 .plans .plan input[type=radio]:checked+.plan-content {
    border: 2px solid #216ee0;
    background: #eaf1fe;
    transition: .3s ease-in
}

@media screen and (max-width: 991px) {
     .plans {
        margin:0 20px;
        flex-direction: column;
        align-items: flex-start;
        padding: 40px
    }

     .plans .plan {
        width: 100%
    }

     .plan.complete-plan {
        margin-top: 20px
    }

     .plans .plan .plan-content .plan-details {
        width: 70%;
        display: inline-block
    }

     .plans .plan input[type=radio]:checked+.plan-content:after {
        top: 45%;
        transform: translate(-50%)
    }
}

@media screen and (max-width: 767px) {
     .plans .plan .plan-content .plan-details {
        width:60%;
        display: inline-block
    }
}

@media screen and (max-width: 540px) {
     .plans .plan .plan-content img {
        margin-bottom:20px;
        height: 56px;
        transition: height .4s
    }

     .plans .plan input[type=radio]:checked+.plan-content:after {
        top: 20px;
        right: 10px
    }

     .plans .plan .plan-content .plan-details {
        width: 100%
    }

     .plans .plan .plan-content {
        padding: 20px;
        flex-direction: column;
        align-items: baseline
    }
}

 .plansWrapper {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
    width: 100%
}

@media screen and (max-width: 700px) {
     .plansWrapper {
        flex-direction:column
    }
}

.appstle_addProduct_type_button_wrapper {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
    margin-bottom: 30px;
}

.selectize-control::before {
    -moz-transition: opacity 0.2s;
    -webkit-transition: opacity 0.2s;
    transition: opacity 0.2s;
    content: " ";
    z-index: 2;
    position: absolute;
    display: block;
    top: 50%;
    right: 34px;
    width: 16px;
    height: 16px;
    margin: -14px 0 0 0;
    background: url('https://ik.imagekit.io/mdclzmx6brh/spinner_CTSXf8R8lXr.gif');
    background-size: 16px 16px;
    opacity: 0;
}
.selectize-control.loading::before {
  opacity: 1;
}
.selectize-input {
  background: #fff !important;
}

.selectize-control {
    padding-left: 3px;
}
--></style>
<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
<script>// <![CDATA[
// Global Variable

        var urlParams = new URLSearchParams(window.location.search);
        var token = urlParams.get('token');
        var appstle_api_key = "${apiKey}";

        // Message
        var noSubscriptionMessage = "${noSubscriptionMessage}";
        var updatePaymentMessage = "${updatePaymentMessage}";
        var paymentNotificationText = "${paymentNotificationText}";

        // Title
        var cancelAccordionTitle = "${cancelAccordionTitle}";
        var paymentDetailAccordionTitle = "${paymentDetailAccordionTitle}";
        var upcomingOrderAccordionTitle = "${upcomingOrderAccordionTitle}";

        // All text
        var cardLastFourDigitText = "${cardLastFourDigitText}";
        var cardExpiryText = "${cardExpiryText}";
        var cardHolderNameText = "${cardHolderNameText}";
        var cardTypeText = "${cardTypeText}";
        var paymentMethodTypeText = "${paymentMethodTypeText}";
        var paymentInfoText = "${paymentInfoText}";
        var updatePaymentBtnText = "${updatePaymentBtnText}";
        var editFrequencyBtnText = "${editFrequencyBtnText}";
        var cancelFreqBtnText = "${cancelFreqBtnText}";
        var updateFreqBtnText = "${updateFreqBtnText}";
        var editChangeOrderBtnText = "${editChangeOrderBtnText}";
        var cancelChangeOrderBtnText = "${cancelChangeOrderBtnText}";
        var updateChangeOrderBtnText = "${updateChangeOrderBtnText}";
        var editProductButtonText = "${editProductButtonText}";
        var deleteButtonText = "${deleteButtonText}";
        var updateButtonText = "${updateButtonText}";
        var cancelButtonText = "${cancelButtonText}";
        var addProductButtonText = "${addProductButtonText}";
        var orderFrequencyText = "${orderFrequencyText}";
        var totalProductsText = "${totalProductsText}";
        var subscriptionNoText = "${subscriptionNoText}";
        var nextOrderText = "${nextOrderText}";
        var statusText = "${statusText}";
        var cancelSubscriptionBtnText = "${cancelSubscriptionBtnText}";
        var activeBadgeText = "${activeBadgeText}";
        var closedBadgeText = "${closedBadgeText}";
        var skipOrderButtonText = "${skipOrderButtonText}";
        var productLabelText = "${productLabelText}";
        var seeMoreDetailsCta = "${seeMoreDetailsText}";
        var hideDetalsCta = "${hideDetailsText}";
        var productAccordian = "${productInSubscriptionText}";
        var editQuantityLabel = "${EditQuantityLabelText}";
        var subTotalLabel = "${subTotalLabelText}";

        var nextOrderDateLbl = "${nextOrderDateLbl}";
        var statusLbl = "${statusLbl}";
        var quantityLbl = "${quantityLbl}";
        var amountLbl = "${amountLbl}";
        var orderNoLbl = "${orderNoLbl}";
        var addProductLabelText = "${addProductLabelText}";

        // new labels 31/05/2021
        var creditcartText = "${creditCardText}";
        var endingWithText = "${endingWithText}";
        var WeekText = "${weekText}";
        var DayText = "${dayText}";
        var MonthText = "${monthText}";
        var YearText = "${yearText}";


        //New Labels 02/06/2021
        var skipBadgeText = "${skipBadgeText}";
        var queueBadgeText = "${queueBadgeText}";

        // Permission Flag Var
        var pauseResumeSubscriptionFlag = "${pauseResumeSubscriptionFlag}";
        var changeNextOrderDateFlag = "${changeNextOrderDateFlag}"; // Added
        var cancelSubscriptionFlag = "${cancelSubscriptionFlag}"; // Added
        var changeOrderFrequencyFlag = "${changeOrderFrequencyFlag}"; // Added
        var addAdditionalProductFlag = "${addAdditionalProductFlag}"; // Added
        var skipOrderFlag = "true"; // pending // not Integrate yet
        var updatePaymentFlag = "true"; // pending // not integrate yet
        var editProductFlag = "${editProductFlag}";
        var deleteProductFlag = "${deleteProductFlag}";
        var skipShipmentFlag = "${showShipment}"; // not used

        // New label
        var skipOrderText = "${skipOrderButtonText}";
        var successText = "${successText}";
        var cancelSubConfirmPrepaid = "${cancelSubscriptionConfirmPrepaidText}";
        var cancelSubConfirmPayAsYouGo = "${cancelSubscriptionConfirmPayAsYouGoText}";
        var cancelSubPrepaidBtn = "${cancelSubscriptionPrepaidButtonText}";
        var cancelSubPayAsYouGoBtn = "${cancelSubscriptionPayAsYouGoButtonText}";
        var upcomingFulfillmentAccordion = "${upcomingFulfillmentText}";
        // Permission Flag Var
        // var skipOrderFlag = "true";
        // var editQuanityFlag = "true";
        // var editVariantFlag = "true";

        // Address
        var ShippingAccordianLabel = "${shippingLabelText}";
        var address1Label = "${address1LabelText}";
        var address2Label = "${address2LabelText}";
        var companyLabel = "${companyLabelText}";
        var cityLabel = "${cityLabelText}";
        var countryLabel = "${countryLabelText}";
        var firstNameLabel = "${firstNameLabelText}";
        var lastNameLabel =  "${lastNameLabelText}";
        var phoneLabel = "${phoneLabelText}";
        var provinceLabel = "${provinceLabelText}";
        var zipLabel = "${zipLabelText}";
        var addressHeaderTitleText = "${addressHeaderTitleText}";
        var changeShippingAddressFlag = "${changeShippingAddressFlag}";
        var updateEditShippingBtnText = "${updateEditShippingButtonText}";
        var cancelEditShippingBtnText = "${cancelEditShippingButtonText}";

        var pauseSubscriptionText = "${pauseSubscriptionText}";
        var resumeSubscriptionText = "${resumeSubscriptionText}";
        var pauseBadgeText = "${pauseBadgeText}"

        // Add Product filter Type
        var addProductFilterType = "ALL_PRODUCTS";
        var includeOutOfStockProduct = false;


    function appendDeletePopupBodyTag() {
            jQuery('body').append(`
            <div id="appstle_popupModal" class="appstle_delete_modal">
            <div class="appstle_delete_modal-content">
                <div class="appstle_model_close">&times;</div>
                <div style="
                margin-bottom: 1em;
                font-size: 20px;
                font-weight: bold;
                ">
                    Are you sure you want to delete ?
                </div>
                <div>
                    <button class="appstle_delete_model_nobtn">No</button>
                    <button class="appstle_delete_model_yesbtn">Yes</button>
                </div>
            </div>
            </div>`)
    }



   function deletePopupConfirm(prepaidFlag)
    {
          // Get the modal
          var appstleDeletePopupmodal = document.getElementById("appstle_popupModal");
          jQuery('body').css('overflow', 'hidden')

          var appstleModalClose = document.getElementsByClassName("appstle_model_close")[0];
            appstleDeletePopupmodal.style.display = "block";
            // When the user clicks on <span> (x), close the modal
            appstleModalClose.onclick = function() {
                hideDeleteConfirmPopup()
            }

            jQuery('.appstle_delete_model_nobtn').on('click', function(){
                hideDeleteConfirmPopup()
            })
            // When the user clicks anywhere outside of the modal, close it
            window.onclick = function(event) {
                if (event.target == appstleDeletePopupmodal) {
                    hideDeleteConfirmPopup()
                }
            }
    }

    function hideDeleteConfirmPopup () {
        jQuery("#appstle_popupModal").remove();
        appendDeletePopupBodyTag();
        jQuery('body').css('overflow', 'unset')
    }

    function subscriptionInit(){
            jQuery("body").off();
            if(jQuery('[id=appstle_popupModal]').length > 0) {
                jQuery("#appstle_popupModal").remove();
            }
            appendDeletePopupBodyTag();
            jQuery(".appstle_myProduct").empty();

            if (customerId != undefined && shopName != undefined) {
                let subscriptionUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-customers/" + customerId + "?api_key=" + window?.appstle_api_key
                let validContractIdUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-customers/valid/" + customerId + "?api_key=" + window?.appstle_api_key
                    subscriptionRender(subscriptionUrl, validContractIdUrl, shopName, customerId, null);
            } else if (token!=null){
                let subscriptionUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-customers/" + token + "?api_key=" + window?.appstle_api_key
                let validContractIdUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-customers/valid/" + token + "?api_key=" + window?.appstle_api_key
                    subscriptionRender(subscriptionUrl, validContractIdUrl, shopName, null, token);
            }
            else {
               window.location.href = 'https://'+ shopName +'/account'
            }
    }

    function accordionToggle(ev) {
        // var acc = document.getElementsByClassName("accordion");
        // var i;
        ev.classList.toggle("active");
        var panel = ev.nextElementSibling;
        $(panel).slideToggle()

    }
    function toggleContractAccordian(el) {
        var selector = $(el).attr('data-accordian-selector')
        $(el).toggleClass("active");
        $(selector).slideToggle()

    }

    function getDeviceType() {
        const ua = navigator.userAgent;
        if (/(tablet|ipad|playbook|silk)|(android(?!.*mobi))/i.test(ua)) {
            return "tablet";
        }
        if (
            /Mobile|iP(hone|od)|Android|BlackBerry|IEMobile|Kindle|Silk-Accelerated|(hpw|web)OS|Opera M(obi|ini)/.test(
            ua
            )
        ) {
            return "mobile";
        }
        return "desktop";
    };

    function appstle_adjust_container_height(imageEl) {

    }

    function updateSubtotal(selector) {
        var productEl = jQuery('#' + selector);
        var variantSelectField = productEl.find('select');
        var currencyCode = productEl.attr('data-currency-code');
        var qty = productEl.find('input').val();
        if(parseInt(qty) <= 0)
        {
            jQuery('#' + selector).find('.appstle_input_error').show();
            return;
        }
        else
            jQuery('#' + selector).find('.appstle_input_error').hide();

        var selectedVariantPrice = variantSelectField.find('option[value="' + variantSelectField.val() + '"]').attr('data-variant-price');
        productEl.find('.appstle_edit_total span').text(parseFloat(selectedVariantPrice) * parseInt(qty) + ' ' + currencyCode);

    }

    function hideAddProductsSearchBar(el) {
        var currentElement = jQuery(el);
        currentElement.parent().parent().hide()
        currentElement.parent().parent().prev().show()
    }

    function updateAddProductText(event) {
        var currentElement = $(event.target)
        var targetElement = currentElement.parents(".appstle_product_search_wrapper").find(".appstle_add_product_select_label b");
        targetElement.text(addProductLabelText + " (" + (event.target.value === "ONE_TIME" ? "One Time Purchase" : "Add to Subscription") + ")")
        currentElement.parents(".appstle_product_search_wrapper").attr('data-product-addition-type', event.target.value);
    }

    function showAddProductsSearchBar(el) {
        var currentElement = jQuery(el);
        currentElement.parent().parent().prev().show();
        currentElement.parent().parent().hide();
    }

    function showAddProductsType(el) {
        var currentElement = jQuery(el);
        currentElement.hide();
        currentElement.next().next().show();
    }

    function hideAddProductsType(el) {
        var currentElement = jQuery(el);
        currentElement.parent().parent().hide();
        currentElement.parent().parent().prev().hide();
        currentElement.parent().parent().prev().prev().show();
    }

    function subscriptionRender(subscriptionUrl, validContractIdUrl, shopName, customerId, token) {
        jQuery.ajax({
            async: "true",
            type: "GET",
            url: subscriptionUrl,
            dataType: "json",
            headers: {
                'Access-Control-Allow-Origin': '*',
            },
            success: function (result, status, xhr) {

                    jQuery.ajax({
                        async: "true",
                        type: "GET",
                        url: validContractIdUrl,
                        dataType: "json",
                        headers: {
                            'Access-Control-Allow-Origin': '*',
                        },
                        success: function (validContractId, status, xhr) {
                             renderPage(result, validContractId);
                        },
                        error: function (xhr, status, error) {
                            console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                        }
                    });
            },
            error: function (xhr, status, error) {
                console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
            }
        });
    }

    function renderPage(result, validContractId)
    {
        if (result?.subscriptionContracts?.edges?.length > 0) {
                    var subscriptionContractData = result.subscriptionContracts.edges.sort(function (a, b) {
                        if (a.node.status <b.node.status) {
                            return -1;
                        }
                        if (a.node.status > b.node.status) {
                            return 1;
                        }
                        // names must be equal
                        return 0;
                    });


                    subscriptionContractData.forEach((subItem, index) => {
                        var paymentData = subItem?.node?.customerPaymentMethod?.instrument;
                        var paymentMethod = paymentData?.__typename;
                        var contractId = subItem?.node?.id?.split('/')[4];
                        var frequencyIntervalTranslate = subItem?.node?.billingPolicy?.interval;

                        if(frequencyIntervalTranslate == "WEEK")
                        {
                            frequencyIntervalTranslate = WeekText
                        }
                        else if(frequencyIntervalTranslate == "DAY")
                        {
                            frequencyIntervalTranslate = DayText
                        }
                        else if(frequencyIntervalTranslate == "MONTH")
                        {
                            frequencyIntervalTranslate = MonthText
                        }
                        else if(frequencyIntervalTranslate == "YEAR")
                        {
                            frequencyIntervalTranslate = YearText
                        }
                        // Check the contract id is valid or not
                        if(validContractId?.length >0 && !validContractId?.includes(parseInt(contractId)))
                        {
                        	 return;
                        }


                        // Calling Contract Details API
                        //var detailedContractData = contractDetailsFromContractId(contractId);
                         jQuery.ajax({
                                async: false,
                                type: "GET",
                                url: 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts/contract-external/' + contractId + '?api_key=' + window?.appstle_api_key,
                                dataType: "json",
                                headers: {
                                    'Access-Control-Allow-Origin': '*',
                                },
                                success: function (extResult, status, xhr) {
                                            if (paymentMethod === "CustomerCreditCard") {
                                                paymentMethod = creditcartText
                                            }

                                           let isPrepaidplan = true;
                                           if(extResult?.billingPolicy?.intervalCount == extResult?.deliveryPolicy?.intervalCount)
                                           {
                                               isPrepaidplan = false;
                                           }

                                                            if (subItem.node.lines?.edges?.length == 1) {
                                                                subItem.node.lines?.edges?.forEach(contractItem => {

                                                                    if (contractItem?.node?.productId == null) {
                                                                        var accordionSection =
                                                                            '<div style="margin: 20px 0;">'
                                                                            + '    <div class="grid" style="background: #242222;color: #fff; margin-left:0px">'
                                                                            + '        <div class="grid__item small--one-whole   one-half">'
                                                                            + '             <p class="appstle_sub-title" style="color: #fff;"> ' + subscriptionNoText + ' - #' + subItem.node.id.split('/')[4] + '</p>'
                                                                            + '         </div>'
                                                                            + '    </div><br>'
                                                                            + '<div style="text-align:center;padding: 20px;background-color: oldlace;"><b>' + contractItem.node.title + ' </b>has been removed.</div>'
                                                                            + '    <button class="accordion appstle_cancelbtn_' + subItem.node.status + '" style="margin-top: 10px;" onclick="accordionToggle(this)">' + cancelAccordionTitle + '</button>'
                                                                            + '    <div class="panel appstle_cancelbtn_' + subItem.node.status + '">'
                                                                            + '          <div class="grid text-center appstle_cancelbtn_' + subItem.node.status + '" style="margin-top: 27px">'
                                                                            + '               <div class="grid__item">'
                                                                            + '                   <button type="button" style="text-align:right; padding: 4px 11px;background-color: darkred;" onclick="cancelSubscription(' + subItem.node.id.split('/')[4] + ')" class="btn appstle_cancelBtnText_' + subItem.node.id.split('/')[4] + '">' + cancelSubscriptionBtnText + '</button>'
                                                                            + '                 </div>'
                                                                            + '            </div>'
                                                                            + '    </div>'
                                                                            + '</div>'
                                                                        jQuery('.appstle_myProduct').append(accordionSection);
                                                                    }
                                                                    else {
                                                                        var subscriptionList =
                                                                        '    <div style="margin: 50px 0;">'
                                                                        +'      <div class="grid appstle_single_item_contract" style="margin-left:0px;box-shadow: 0 10px 20px 0 rgb(0 0 0 / 15%);border-radius: 10px;overflow: hidden;" data-contract-id=' + contractId +'>'
                                                                        +'          <div class="grid" style="background: #242222;color: #fff;margin-left:0px;padding: 3px 0;">'
                                                                        +'              <div class="grid__item small--one-whole one-half">'
                                                                        +'                  <p class="appstle_sub-title" style="color: #fff;"> ' + subscriptionNoText + ' - #' + subItem.node.id.split('/')[4] + '</p>'
                                                                        +'              </div>'
                                                                        +'          </div>'
                                                                        +'          <div class="grid__item appstle_subscription_flex_override" style="margin: 22px 0;">'
                                                                        +'              <div class="grid__item small--one-whole one-third" style="padding-left: 0; height: 0;">'
                                                                        +'                  <div style="height: 100%" class="appstle_subscription_contract_image_wrapper_' + contractId + '">'
                                                                        +'                  </div>'
                                                                        +'              </div>'
                                                                        +'              <div class="appstle_subscription_contract_content_wrapper_mobile grid__item small--one-whole two-thirds appstle_subscription_detail appstle_subscription_contract_content_wrapper_' + contractId + '">'
                                                                        +'                  <p class="appstle_sub-title appstle_mb-0 appstle_subscription_contract_title" style="">'
                                                                        +'                      ' + subItem.node.billingPolicy.intervalCount + ' ' + frequencyIntervalTranslate + ' ' + subscriptionNoText + ''
                                                                        +'                      <span class="appstle_' + subItem.node.status + '_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (subItem.node.status == "ACTIVE" ?  activeBadgeText : (subItem.node.status === "CANCELLED" ? closedBadgeText : (subItem.node.status === "PAUSED" ? pauseBadgeText : "")) ) + '</span>'
                                                                        +'                  </p>'
                                                                        +'' +               ((subItem.node.status === "CANCELLED" || subItem.node.status === "PAUSED") ? '' : ('<div class="appstle_orderDate_' + subItem.node.status + '" style="color: #495667;">'
                                                                        +'                      <div class="appstle_font_size appstle_order_detail_row">'
                                                                        +'                          <strong>' + nextOrderText + ': </strong>'
                                                                        +'                          <div class="appstle_ml-10 appstle_next_oder_date_wrapper">'
                                                                        +'                              <span>' + (new Date(subItem.node.nextBillingDate).toDateString()) + '</span>'
                                                                        +'                              <p style="display:none; margin: 0;margin-left: 10px;color: #2c6ecb;" class="appstle_ml-10" id="orderDateloadingText_' + contractId + '">'
                                                                        +'                                  Processing.. Please Wait'
                                                                        +'                              </p>'
                                                                        +''+                             ((changeNextOrderDateFlag == "true") ? ('<button class="appstle_font_size appstle_order-detail-edit-button editBtnOrderDate_' + contractId + ' appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '" onclick="showChangeOrderDateForm(' + contractId + ')">'
                                                                        +'                                  <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                        +'                              </button>') : '') + ''
                                                                        +'                          </div>'
                                                                        +'                      </div>'
                                                                        +'                      <div class="appstle_form_margin appstle_orderDateformDiv_' + contractId + ' appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '"  style="display: none; margin-top: 4px;">'
                                                                        +'                          <form id="orderDateform_' + contractId + '" name="orderDate_' + contractId + '"   class ="appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '">'
                                                                        +'                              <input type="date" id="changeOrderDate" style="width:100%" name="changeOrderDate"  min='+ (new Date()).toISOString().slice(0,10)+' value='+(new Date(subItem.node.nextBillingDate)).toISOString().slice(0,10)+'>'
                                                                        +'                          </form>'
                                                                        +'                           <div class="appstle_buttonGroup">'
                                                                        +'                              <button class="appstle_order-detail_cancel-button" onclick="hideOrderDateForm(' + contractId + ')">' + cancelChangeOrderBtnText + '</button>'
                                                                        +'                              <button class="appstle_order-detail_update-button appstle_ml-10"  onclick="updateOrderDate(' + contractId + ')">' + updateChangeOrderBtnText + '</button>'
                                                                        +'                           </div>'
                                                                        +'                      </div>'
                                                                        +'                  </div>')) + ''
                                                                        +'' +               ((subItem.node.status === "CANCELLED") ? '' : ('<div style="color: #465661;" class="appstle_orderFrequency_' + subItem.node.status + '">'
                                                                        +'                      <div class="appstle_font_size appstle_order_detail_row">'
                                                                        +'                          <strong>' + orderFrequencyText + ': </strong>'
                                                                        +'                          <div class="appstle_ml-10" style="DISPLAY: FLEX; align-items: center;">'
                                                                        +'                              <span>' + subItem.node.billingPolicy.intervalCount + ' ' + frequencyIntervalTranslate + '</span>'
                                                                        +'                              <p style="display:none; margin: 0;margin-left: 10px;color: #2c6ecb;" class="appstle_ml-10" id="orderloadingText_' + contractId + '">Processing.. Please Wait</p>'
                                                                        +''+                              ((subItem.node.status === "PAUSED") ? '' : ((changeOrderFrequencyFlag == "true") ? ('<button class="appstle_font_size appstle_order-detail-edit-button editBtnFrequency_' + contractId + ' appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '" onclick="showFrequencyForm(' + contractId + ')"  style="padding-top: 0; display: flex; align-items: center;">'
                                                                        +'                                  <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                        +'                              </button>') : '')) + ''
                                                                        +'                          </div>'
                                                                        +'                      </div>'
                                                                        +'                      <div class="appstle_form_margin appstle_orderFrequencyformDiv_' + contractId + ' appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '"  style="display: none; margin-top: 5px;" >'
                                                                        +'                          <form id="orderFrequencyform_' + contractId + '" name="orderfrequency_' + contractId + '" class ="appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '">'
                                                                        +'                              <input type="number" id="frequencyCount"  name="frequencyCount" value="' + subItem.node.billingPolicy.intervalCount + '" min="1" style="width:65%">'
                                                                        +'                              <select id="frequencyInterval" name="frequencyInterval" style="width:33%">'
                                                                        +'                                  <option value="DAY" ' + ((subItem.node.billingPolicy.interval === "DAY") ? "selected" : "") +'>'+DayText+'</option>'
                                                                        +'                                  <option value="WEEK" ' + ((subItem.node.billingPolicy.interval === "WEEK") ? "selected" : "") +'>'+ WeekText + '</option>'
                                                                        +'                                  <option value="MONTH" ' + ((subItem.node.billingPolicy.interval === "MONTH") ? "selected" : "") +'>'+MonthText+'</option>'
                                                                        +'                                  <option value="YEAR" ' + ((subItem.node.billingPolicy.interval === "YEAR") ? "selected" : "") +'>'+YearText+'</option>'
                                                                        +'                              </select>'
                                                                        +'                          </form>'
                                                                        +'                            <div class="appstle_buttonGroup">'
                                                                        +'                              <button class="appstle_order-detail_cancel-button" onclick="hideFrequencyForm(' + contractId + ')">' + cancelFreqBtnText + '</button>'
                                                                        +'                              <button class="appstle_order-detail_update-button appstle_ml-10" onclick="updateFrequency(' + contractId + ')">' + updateFreqBtnText + '</button>'
                                                                        +'                            </div>'
                                                                        +'                      </div>'
                                                                        +'                  </div>')) + ''
                                                                        +'                  <div style="margin: 8px;margin-top: 25px;color: #495661 !important;">'
                                                                        +'                      <div class="appstle_font_size" style="">'
                                                                        +'                          <b style="color: #495667;">' + productLabelText + '</b>'
                                                                        +'                      </div>'
                                                                        +'                      <div class="appstle_subscription_contract_product_list_wrapper_' + contractId + '"></div>'
                                                                        +'                  </div>'
                                                                        +''+                ((pauseResumeSubscriptionFlag === "true") ? ((subItem.node.status === "ACTIVE") ? ('<button style="margin-right: auto;align-self: flex-start;display: flex;align-items: center;" class="appstle_pause_subscription_button btn" onclick="pauseSubscription(' + contractId + ')">'
                                                                        +'                      <div style="display: none; margin-right: 5px;" class="appstle_loaderTiny"></div>'
                                                                        +'                      <div  class="appstle_icon_wrapper" style="display: flex; border: 1px solid #fff; padding: 5px; justify-content: center; align-items: center; margin-right: 5px; border-radius: 50%;">'
                                                                        +'                          <div class="appstle_pause_icon"></div>'

                                                                        +'                      </div>'
                                                                        +'                      ' + pauseSubscriptionText + ''
                                                                        +'                   </button>') : '') : '') + ''
                                                                        +''+                ((pauseResumeSubscriptionFlag === "true") ? ((subItem.node.status === "PAUSED") ? ('<button style="margin-right: auto;align-self: flex-start;display: flex;align-items: center;" class="appstle_resume_subscription_button btn" onclick="resumeSubscription(' + contractId + ')">'
                                                                        +'                      <div style="display: none; margin-right: 5px;" class="appstle_loaderTiny"></div>'
                                                                        +'                      <div  class="appstle_icon_wrapper" style="display: flex; border: 1px solid #fff; padding: 5px; justify-content: center; align-items: center; margin-right: 5px; border-radius: 50%;">'
                                                                        +'                          <div class="appstle_play_icon"></div>'

                                                                        +'                      </div>'
                                                                        +'                      ' + resumeSubscriptionText + ''
                                                                        +'                   </button>') : '') : '') + ''
                                                                        +'' +                  ((subItem.node.status === "CANCELLED" || subItem.node.status === "PAUSED") ? '' : ('<div class="appstle_subscription_contract_detail_toggle_wrapper appstle_font_size" style="display: flex; align-items: center; justify-content: flex-end; letter-spacing: 0.3px; margin-top: auto;" onClick=toggleContractAccordian(this) data-accordian-selector=".appstle_accordian_wrapper_' + contractId + '">'
                                                                        +'                      <span class="appstle_contract_see_more">' + seeMoreDetailsCta + '</span>'
                                                                        +'                      <span style="display: none" class="appstle_contract_hide_more">' + hideDetalsCta + '</span>'
                                                                        +'                      <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px; font-weight: bold;"></span>'
                                                                        +'                  </div>')) + ''
                                                                        +'               </div>'
                                                                        +'          </div>'
                                                                        +'     </div>'
                                                                        + '</div>'
                                                                        var addressDetails = extResult?.deliveryMethod?.address
                                                                        var accordionSection =
                                                                            '   <div style="display: none;" class="appstle_accordian_wrapper_' + contractId + '">'
                                                                            +'      <button class="accordion appstle_productAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                            +'          <div class="appstle_accordian_line_1"></div>'
                                                                            +'          <span style="margin: 0 10px;">' + productAccordian + '</span>'
                                                                            +'          <div class="appstle_accordian_line_2"></div>'
                                                                            +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                            +'      </button>'
                                                                            +'      <div style="display: none;" class="panel c' + subItem.node.status + '" id="sub' + index + '">'
                                                                            +'      </div>'
                                                                            +'      <button class="accordion appstle_ShippingAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                            +'          <div class="appstle_accordian_line_1"></div>'
                                                                            +'          <span style="margin: 0 10px;">' + ShippingAccordianLabel + '</span>'
                                                                            +'          <div class="appstle_accordian_line_2"></div>'
                                                                            +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                            +'      </button>'
                                                                            +'      <div style="display: none;" class="panel appstle_ShippingAcc_' + contractId + ' appstle_ShippingAcc_' + subItem.node.status + '" id="sub' + index + '">'
                                                                            +'      <div class="appstle_product_wrapper" style="flex-direction: column;">'
                                                                            +'        <div class="appstle_address_header_title">'
                                                                            +'          ' + addressHeaderTitleText + ''
                                                                            +''+        ((changeShippingAddressFlag == "true") ? ('<button style="padding-top: 14px;" class="appstle_font_size appstle_order-detail-edit-button editBtnAddress_' + contractId + ' appstle_changeAddressFlag_' + changeShippingAddressFlag + '" onclick="toggleChangeAddressForm(' + contractId + ')">'
                                                                            +'             <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                            +'           </button>') : '') + ''
                                                                            +'        </div>'
                                                                            +'        <div style="align-self: flex-start;" class="appstle_address_viewOnly">'
                                                                            +''+        ((addressDetails.firstName) ? ('<span>' + addressDetails.firstName  + '</span>') : '') + ' ' + ((addressDetails.lastName) ? ('<span>' + addressDetails.lastName  + '</span>') : '') + ''
                                                                            +''+        ((addressDetails.address1) ? ('<div>' + addressDetails.address1  + '</div>') : '')
                                                                            +''+        ((addressDetails.address2) ? ('<div>' + addressDetails.address2  + '</div>') : '')
                                                                            +''+        ((addressDetails.city) ? ('<span>' + addressDetails.city  + '</span>') : '')
                                                                            +''+        ((addressDetails.province) ? ('<span>, ' + addressDetails.province  + '</span>') : '')
                                                                            +''+        ((addressDetails.country) ? ('<span>, ' + addressDetails.country  + '</span>') : '')
                                                                            +''+        ((addressDetails.zip) ? ('<div>' + addressDetails.zip  + '</div>') : '')
                                                                            +''+        ((addressDetails.phone) ? ('<div>' + addressDetails.phone  + '</div>') : '')
                                                                            +'        </div>'
                                                                            +'        <div style="display: none" class="appstle_address_editForm">'
                                                                            +'        <form style="width: 100%;">'
                                                                            +'          <div class="grid">'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="firstName">' + firstNameLabel + '</label>'
                                                                            +'                  <input type="text" name="firstName" value="' + (addressDetails.firstName ? addressDetails.firstName : "")  + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="lastName">' + lastNameLabel + '</label>'
                                                                            +'                  <input type="text" name="lastName" value="' + (addressDetails.lastName ? addressDetails.lastName : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="company">' + companyLabel + '</label>'
                                                                            +'                  <input type="text" name="company" value="' + (addressDetails.company ? addressDetails.company : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="phone">' + phoneLabel + '</label>'
                                                                            +'                  <input type="text" name="phone" value="' + (addressDetails.phone ? addressDetails.phone : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="address1">' + address1Label + '</label>'
                                                                            +'                  <input type="text" name="address1" value="' + (addressDetails.address1 ? addressDetails.address1 : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="address2">' + address2Label + '</label>'
                                                                            +'                  <input type="text" name="address2" value="' + (addressDetails.address2 ? addressDetails.address2 : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="city">' + cityLabel + '</label>'
                                                                            +'                  <input type="text" name="city" value="' + (addressDetails.city ? addressDetails.city : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="country">' + countryLabel + '</label>'
                                                                            +'                  <input type="text" name="country" value="' + (addressDetails.country ? addressDetails.country : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="province">' + provinceLabel + '</label>'
                                                                            +'                  <input type="text" name="province" value="' + (addressDetails.province ? addressDetails.province : "") + '">'
                                                                            +'              </div>'
                                                                            +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                            +'                  <label for="zip">' + zipLabel + '</label>'
                                                                            +'                  <input type="text" name="zip" value="' + (addressDetails.zip ? addressDetails.zip : "") + '">'
                                                                            +'              </div>'
                                                                            +'          </div>'
                                                                            +'          <input type="hidden" name="countryCode" value="' + (addressDetails.countryCode ? addressDetails.countryCode : "") + '">'
                                                                            +'          <input type="hidden" name="provinceCode" value="' + (addressDetails.provinceCode ? addressDetails.provinceCode : "") + '">'
                                                                            +'          <input type="hidden" name="name" value="">'
                                                                            +'        </form>'
                                                                            +'        <div class="appstle_buttonGroup">'
                                                                            +'             <button class="appstle_edit_shipping_cancel-button" onclick="toggleChangeAddressForm(' + contractId + ')">' + cancelEditShippingBtnText + '</button>'
                                                                            +'             <button class="appstle_edit_shipping_update-button appstle_ml-10" onclick="updateShippingAddress(' + contractId + ')"><div style="display: none;" class="appstle_loaderTiny"></div><span>' + updateEditShippingBtnText + '</span></button>'
                                                                            +'        </div>'
                                                                            +'       </div>'
                                                                            +'      </div>'
                                                                            +'      </div>'
                                                                            +'      <button class="accordion appstle_upcomingAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)"><div class="appstle_accordian_line_1"></div><span style="margin: 0 10px;">' + (isPrepaidplan ? upcomingFulfillmentAccordion : upcomingOrderAccordionTitle ) + '</span><div class="appstle_accordian_line_2"></div> <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span></button>'
                                                                            + '     <div style="display: none;" class="panel appstle_upcomingAcc_' + subItem.node.status + '" id="orderAcc_' + subItem.node.id.split('/')[4] + '">'
                                                                            + '     </div>'
                                                                            + '     <button class="accordion" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                            +'          <div class="appstle_accordian_line_1"></div>'
                                                                            +'          <span style="margin: 0 10px;">' + paymentDetailAccordionTitle + '</span>'
                                                                            +'          <div class="appstle_accordian_line_2"></div>'
                                                                            +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                            +'      </button>'
                                                                            +'      <div class="appstle_product_wrapper" style="display: none; flex-direction: column;">'
                                                                            +'          <div class="appstle_font_size appstle_subscription_payment_wrapper">'
                                                                            +'          <div style="flex-grow: 1;align-self: flex-start;">'
                                                                            +'              <div>'
                                                                            +'                  <div>'
                                                                            +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                          <b>' + paymentMethodTypeText + '</b>: ' + paymentMethod + ' - ' + paymentData?.brand?.toUpperCase() + endingWithText + paymentData?.lastDigits + ''
                                                                            +'                      </div>'
                                                                            +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                          <b>' + cardHolderNameText + '</b>: '+ paymentData?.name +''
                                                                            +'                      </div>'
                                                                            +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                          <b>' + cardExpiryText + '</b>: ' + paymentData?.expiryMonth + ' / ' + paymentData?.expiryYear + ''
                                                                            +'                      </div>'
                                                                            +'                  </div>'
                                                                            +'              </div>'
                                                                            +'          </div>'
                                                                            +'          <button class="appstle_font_size appstle_updatePaymentButton btn appstle_order-detail-edit-button appstle_paymentBtnUpdate_' + subItem.node.id.split('/')[4] + '" onclick="updatePaymentDetails(' + contractId + ')">' + updatePaymentBtnText + '</button>'
                                                                            +'          </div>'
                                                                            +'          <p style="align-self: flex-end;font-size: 13px;font-weight: bold;">' + paymentNotificationText + '</p>'
                                                                            +'      </div>'
                                                                            + '' + (!(shopName == "powerhouse-brewing-company.myshopify.com" && isPrepaidplan) ? ('<button class="accordion appstle_cancelbtn_' + subItem.node.status + ' appstle_cancelSubscriptionFlag_' + cancelSubscriptionFlag + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)"><div class="appstle_accordian_line_1"></div><span style="margin: 0 10px;">' + cancelAccordionTitle + '</span><div class="appstle_accordian_line_2"></div> <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span></button>') : '') + ''
                                                                            + '    <div style="display: none;" class="panel appstle_cancelbtn_' + subItem.node.status + ' appstle_cancelSubscriptionFlag_' + cancelSubscriptionFlag + '">'
                                                                            + '          <div class="grid text-center appstle_cancelbtn_' + subItem.node.status + '" style="margin-top: 27px">    '
                                                                            + '               <div class="grid__item">'
                                                                            + '                   <button type="button" onclick="cancelSubscription(' + subItem.node.id.split('/')[4] + ')" class="appstle_cencelSubscription btn appstle_cancelBtnText_' + subItem.node.id.split('/')[4] + '">' + cancelSubscriptionBtnText + '</button>       '
                                                                            + '               </div>'
                                                                            + '            </div>'
                                                                            + '    </div>'
                                                                            + '   </div>'


                                                                jQuery('.appstle_myProduct').append(subscriptionList);
                                                                jQuery('.appstle_myProduct').append(accordionSection);
                                                                subItem.node.lines?.edges?.forEach((contractItem, idx) => {

                                                                    if (contractItem?.node?.productId != null) {
                                                                        var subscribedProduct =
                                                                            '<div class="appstle_product_wrapper" id="editproduct' + index + idx + '" data-line-id="' + contractItem?.node?.id + '"  data-contract-id="' + contractId + '" data-shop="' + shopName + '" data-product-id="' + contractItem?.node?.productId.split('/').pop() + '" data-variant-id="' + contractItem?.node?.variantId + '"  data-price="' + (contractItem?.node?.lineDiscountedPrice?.amount / contractItem.node.quantity) + '" data-currency-code="' + contractItem.node.lineDiscountedPrice.currencyCode + '">'
                                                                            +' <img src=' + contractItem?.node?.variantImage?.transformedSrc + ' alt="" height="auto" width="100" style="padding-left: 0; border-radius: 2px">'
                                                                            +' <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                            +'      <strong>' + contractItem.node?.title + '<span class="appstle_variantTitle_' + contractItem.node?.productId?.split('/')[4] + '"> - ' + contractItem?.node?.variantTitle + '<span></strong>'
                                                                            +'      <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                            +'          <div>'
                                                                            +'              <div> <b>' + quantityLbl + ':</b> <span>' + contractItem.node?.quantity + '</span></div>'
                                                                            +'              <div style="margin-top: 5px;"><b>' + amountLbl + ':</b> <span>' + contractItem.node?.lineDiscountedPrice?.amount + ' ' + contractItem.node?.lineDiscountedPrice?.currencyCode + '</span></div>'
                                                                            +'              <div style="margin-top: 5px;"><b>' + nextOrderText + '</b>: ' + (new Date(subItem?.node?.nextBillingDate).toDateString()) + '</div>'
                                                                            +'          </div>'
                                                                            +'      </div>'
                                                                            +'      <div style="display: none;" class="appstle_edit_wrapper">'
                                                                            +'          <form>'
                                                                            +'              <div class="appstle_form_group">'
                                                                            +'                  <label>' + editQuantityLabel + '</label>'
                                                                            +'                  <input type="number" name="quantity" onChange=updateSubtotal("editproduct' + index + idx + '") min="1" value="' + contractItem.node?.quantity + '">'
                                                                            +'                  <span class="appstle_input_error" style="display: none;">Please enter a valid number</span>'
                                                                            +'              </div>'
                                                                            +'              <div class="appstle_form_group appstle_select_wrapper">'
                                                                            +'                  <label>Edit Variant</label>'
                                                                            +'                  <select onChange=updateSubtotal("editproduct' + index + idx + '") style="width: 100%" name="variantId" data-edit="editproduct' + index + idx + '">'
                                                                            +'                  </select>'
                                                                            +'              </div>'
                                                                            +'          </form>'
                                                                            +'      </div>'
                                                                            +'      <div style="display: none;" class="appstle_buttonGroup" id="appstle_buttonGroup' + index + idx + '">'
                                                                            +'          <div class="appstle_edit_total"><b>' + subTotalLabel + ': <span>' + contractItem.node?.lineDiscountedPrice?.amount + ' ' + contractItem.node?.lineDiscountedPrice?.currencyCode + '</span></b></div>'
                                                                            +'          <button class="appstle_cancelButton" data-edit="editproduct' + index + idx + '">' + cancelButtonText + '</button>'
                                                                            +'          <button class="appstle_updateButton appstle_ml-10" data-edit="editproduct' + index + idx + '">' + updateButtonText + '</button>'
                                                                            +'      </div>'
                                                                            +'  </div>'
                                                                            +'  <div class="appstle_editDeleteGroup">'
                                                                            +''+      ((editProductFlag == "true") ? ('<button class="appstle_editButton" data-edit="editproduct' + index + idx + '"><i class="lnr lnr-pencil btn-icon-wrapper"></i></button>') : '') + ''
                                                                            +'      <button ' + (subItem.node.lines?.edges?.length <= 1 && 'style="display: none;"') + ' class="appstle_deleteButton appstle_ml-10" data-isPrepaid='+isPrepaidplan+' data-edit="editproduct' + index + idx + '"><i class="lnr lnr-trash btn-icon-wrapper"></i></button>'
                                                                            +'  </div>'
                                                                            +'</div>'
                                                                        jQuery('#sub' + index).append(subscribedProduct)
                                                                        if (contractItem.node.variantTitle == null) {
                                                                            jQuery('.appstle_variantTitle_' + contractItem.node?.productId?.split('/')[4]).hide();
                                                                        }

                                                                        jQuery('.appstle_subscription_contract_product_list_wrapper_' + contractId).append(
                                                                            '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem.node?.quantity > 1 ? '<span style="margin-left: 8px">x ' + contractItem.node?.quantity + '</span>' : '') + '</div>'
                                                                        )

                                                                        var imageElement = jQuery('<img src="' + contractItem.node?.variantImage?.transformedSrc + '" alt="" style="max-height: 100%; display: block; margin: 0 auto; align-self: flex-start; padding-left: 0; border-radius: 2px; flex-grow: 1">');
                                                                        jQuery('.appstle_subscription_contract_image_wrapper_' + contractId).append(imageElement);



                                                                    }
                                                                    else {
                                                                        var subscribedProduct =
                                                                            '<div style="display: flex; align-items: center;margin-top:15px">'
                                                                            + '   <div style="text-align: center;padding: 20px;background-color: oldlace;">'
                                                                            + '        <strong>' + contractItem.node?.title + '</strong> has been removed'
                                                                            + '   </div>'
                                                                            + '</div>'
                                                                        jQuery('#sub' + index).append(subscribedProduct)
                                                                    }

                                                        });

                                                    if(shopName != "taelor-style-dev.myshopify.com" && (addAdditionalProductFlag == "true"))
                                                        jQuery('#sub' + index).prepend(    '<div class="appstle_product_search_wrapper" data-contract-id="' + contractId + '" data-selling-plan-id="' + ((extResult?.lines?.edges?.map(line => { return line?.node?.sellingPlanId })).join("|").toString()) + '" data-product-addition-type="SUBSCRIBE">'
                                                        +       '<button style="margin-right: auto;" class="appstle_show_add_products_button btn" onclick="showAddProductsType(this)">' + addProductButtonText + '</button>'
                                                        +        '<div style="display: none;">'
                                                        +           '<label class="appstle_add_product_select_label" style="margin-bottom: 5px;"><b>' + addProductLabelText + '(Add to Subscription)</b></label>'
                                                        +           '<div style="display: flex;">'
                                                        +               '<select style="flex-grow: 1; height: 100%;" class="appstle_product-search-select" data-contract-id="' + contractId + '" ></select>'
                                                        +               '<button class="appstle_product-search-button appstle_ml-10" data-contract-id="' + contractId + '">' + addProductButtonText + '</button>'
                                                        +               '<button class="appstle_hide_add_products_button btn appstle_ml-10" onclick="hideAddProductsSearchBar(this)">' + cancelButtonText + '</button>'
                                                        +           '</div>'
                                                        +        '</div>'
                                                        +   '<div style="display: none;">'
                                                        +   '<div class="plans">'
                                                        +       '<div style="margin-bottom: 20px;"><b>Select Purchase Option</b></div>'
                                                        +       '<div class="plansWrapper">'
                                                        +           '<label class="plan basic-plan mr-3" for="isSubscriptionPurchase">'
                                                        +               '<input type="radio" id="isSubscriptionPurchase" name="purchaseOption" value="SUBSCRIBE" checked="" onChange="updateAddProductText(event)">'
                                                        +                   '<div class="plan-content">'
                                                        +                       '<img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/discount-2045_57GSxj6bEsg.png?updatedAt=1632475086786" alt="">'
                                                        +                       '<div class="plan-details">'
                                                        +                           '<span>Add to Subscription</span>'
                                                        +                           '<p>Upsell Discount</p>'
                                                        +                       '</div>'
                                                        +                   '</div>'
                                                        +           '</label>'
                                                        +           '<label class="plan complete-plan" for="isOneTimePurchase">'
                                                        +               '<input type="radio" id="isOneTimePurchase" name="purchaseOption" label="One time purchase" value="ONE_TIME" onChange="updateAddProductText(event)">'
                                                        +                   '<div class="plan-content">'
                                                        +                       '<img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/shipping-2050_7nIMMvG_4hy.png?updatedAt=1632475174135" alt="">'
                                                        +                       '<div class="plan-details">'
                                                        +                           '<span>One Time Purchase</span>'
                                                        +                           '<p>Get your product along with your next subscription order without any Shipping costs.</p>'
                                                        +                       '</div>'
                                                        +                   '</div>'
                                                        +           '</label>'
                                                        +       '</div>'
                                                        +   '</div>'
                                                        +   '<div class="appstle_addProduct_type_button_wrapper">'
                                                        +       '<button  onclick="showAddProductsSearchBar(this)" class="appstle_addProduct_type_next_button">Next</button>'
                                                        +       '<button onclick="hideAddProductsType(this)" class="appstle_addProduct_type_cancel_button appstle_ml-10">Cancel</button>'
                                                        +   '</div>'
                                                        +   '</div>'
                                                        +   '</div>')


                                                            if(isPrepaidplan)
                                                            {
                                                                  extResult?.originOrder?.fulfillmentOrders?.edges?.map((orderItem) => {
                                                                                var upcomingOrder =
                                                                                '<div class="appstle_product_wrapper appstle_upcoming_item" data-billing-id="' + orderItem.node.id + '" data-contract-id="' + contractId + '">'
                                                                            +'      <div style="max-width: 100px; display: flex; flex-wrap: wrap;" id="orderImage_' + orderItem.node.id.split("/").pop() + '">'
                                                                            +'      </div>'
                                                                            +'      <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                            +'          <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                            +'              <div>'
                                                                            +'                  <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                      <b>' + nextOrderDateLbl + '</b>: ' + (new Date(orderItem.node?.fulfillAt).toDateString()) + ''
                                                                            +'                      <span class="appstle_' + orderItem.node.status +'_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (orderItem.node.status == "SKIPPED" ? skipBadgeText : queueBadgeText) + '</span>'
                                                                            +'                  </div>'
                                                                            +'              </div>'
                                                                            +'          </div>'
                                                                            +'          <div style="margin-top: 5px;color: #495661 !important;">'
                                                                            +'              <div class="appstle_font_size" style="">'
                                                                            +'                  <b style="color: #495667;font-size: 13px;">' + productLabelText + ':</b>'
                                                                            +'              </div>'
                                                                            +'              <div  id="orderTitle_' + orderItem.node.id.split("/").pop() + '">'
                                                                            +'              </div>'
                                                                            +'         </div>'
                                                                            +'      </div>'
                                                                            +'' + (orderItem.node.status === 'SKIPPED' ? '' : '<button class="appstle_skipOrderButton btn appstle_skipShipmentFlag_'+skipShipmentFlag+' appstle_skiporder_' + orderItem.node.id.split("/").pop() + '" onclick="skipBillingOrder(' + orderItem.node.id.split("/").pop() + ')">' + skipOrderText + '</button>') + ''
                                                                            +'  </div>'


                                                                                jQuery('#orderAcc_' + subItem.node?.id?.split('/')[4]).append(upcomingOrder);

                                                                                subItem.node.lines?.edges?.forEach(contractItem => {
                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center"> <img src="' + contractItem.node?.variantImage?.transformedSrc + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1;width: 100%;">'
                                                                                    var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem?.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem?.node?.quantity > 1 ? '<span style="margin-left: 8px">X' + contractItem?.node?.quantity + '</span>' : '') + '</div>'
                                                                                    jQuery('#orderImage_' + orderItem.id.split("/").pop()).append(upcomingOrderImage);
                                                                                    jQuery('#orderTitle_' + orderItem.id.split("/").pop()).append(upcomingOrderProductTitle);
                                                                                    if (contractItem.node?.variantTitle == null) {
                                                                                        jQuery('.appstle_variantTitle_' + contractItem.node?.productId.split('/')[4]).hide();
                                                                                    }
                                                                                });
                                                                            })
                                                            }
                                                            // Upcoming Order API CALL HERE
                                                            if (subItem.node?.status == "ACTIVE" && !isPrepaidplan) {
                                                                var upcomingOrderUrl;
                                                                if (customerId != null) {
                                                                    upcomingOrderUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-billing-attempts/top-orders?contractId=" + contractId + "&customerId=" + customerId + "&api_key=" + window?.appstle_api_key;
                                                                }
                                                                else {
                                                                    upcomingOrderUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-billing-attempts/top-orders?contractId=" + contractId + "&customerUid=" + token + "&api_key=" + window?.appstle_api_key;
                                                                }
                                                                jQuery.ajax({
                                                                    type: "GET",
                                                                    url: upcomingOrderUrl,
                                                                    dataType: "json",
                                                                    headers: {
                                                                        'Access-Control-Allow-Origin': '*',
                                                                    },
                                                                    success: function (result, status, xhr) {
                                                                        if (result?.length > 0) {

                                                                            result.map((orderItem, idx) => {
                                                                                var upcomingOrder =
                                                                                '<div class="appstle_product_wrapper appstle_upcoming_item" data-billing-id="' + orderItem.id + '" data-contract-id="' + orderItem.contractId + '">'
                                                                            +'      <div style="max-width: 100px;" id="orderImage_' + orderItem.id + '">'
                                                                            +'      </div>'
                                                                            +'      <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                            +'          <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                            +'              <div>'
                                                                            +'                  <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                      <b>' + nextOrderDateLbl + '</b>: ' + (new Date(orderItem?.billingDate).toDateString()) + ''
                                                                            +'                      <span class="appstle_' + orderItem.status +'_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (orderItem.status == "SKIPPED" ? skipBadgeText : queueBadgeText) + '</span>'
                                                                            +'                  </div>'
                                                                            +'              </div>'
                                                                            +'          </div>'
                                                                            +'          <div style="margin-top: 5px;color: #495661 !important;">'
                                                                            +'              <div class="appstle_font_size" style="">'
                                                                            +'                  <b style="color: #495667;font-size: 13px;">' + productLabelText + ':</b>'
                                                                            +'              </div>'
                                                                            +'              <div  id="orderTitle_' + orderItem.id + '">'
                                                                            +'              </div>'
                                                                            +'         </div>'
                                                                            +'      </div>'
                                                                            +'' + (orderItem.status === 'SKIPPED' ? '' : '<button class="appstle_skipOrderButton btn appstle_skipShipmentFlag_'+skipShipmentFlag+' appstle_skiporder_' + orderItem.id + '" onclick="skipBillingOrder(' + orderItem.id + ')">' + skipOrderText + '</button>') + ''
                                                                            +'  </div>'


                                                                                jQuery('#orderAcc_' + subItem.node?.id?.split('/')[4]).append(upcomingOrder);

                                                                                subItem.node.lines?.edges?.forEach(contractItem => {
                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center; max-width: 46%; margin: 1% !important; "> <img src="' + contractItem.node?.variantImage?.transformedSrc + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1;width: 100%;">'
                                                                                    var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem?.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem?.node?.quantity > 1 ? '<span style="margin-left: 8px">X' + contractItem?.node?.quantity + '</span>' : '') + '</div>'
                                                                                    jQuery('#orderImage_' + orderItem.id).append(upcomingOrderImage);
                                                                                    jQuery('#orderTitle_' + orderItem.id).append(upcomingOrderProductTitle);
                                                                                    if (contractItem.node?.variantTitle == null) {
                                                                                        jQuery('.appstle_variantTitle_' + contractItem.node?.productId.split('/')[4]).hide();
                                                                                    }
                                                                                });
                                                                            if (idx === 0) {
                                                                            var upcomingOneTimeVariants = [];
                                                                                var subscriptionContractOneoff = 'https://subscription-admin.appstle.com/api/external/v2/subscription-contract-one-offs-by-contractId?contractId=' + contractId + '&api_key=' + window?.appstle_api_key;
                                                                                jQuery.get(subscriptionContractOneoff)
                                                                                .then(async resp => {
                                                                                    if(resp?.length > 0)
                                                                                    {
                                                                                    var globalIndex = 0
                                                                                    for await (let item of resp) {
                                                                                        try{
                                                                                        globalIndex = globalIndex + 1;
                                                                                        const productvariantUrl = 'https://' + Shopify.shop + '/products/' + item.variantHandle + '.js';
                                                                                        await jQuery.get(productvariantUrl)
                                                                                        .then(res => {
                                                                                            res = JSON.parse(res);
                                                                                            var varData = res.variants.find(variant => variant.id == item.variantId);
                                                                                            var upcomingOneTimeVariantsData = {
                                                                                            billingID: item?.billingAttemptId,
                                                                                            prdImage: 'https:' + res.featured_image,
                                                                                            variantName: res.title + ((res.variants?.length > 1) ? (" - " + varData?.title) : ""),
                                                                                            id: varData?.id
                                                                                            }
                                                                                            upcomingOneTimeVariants.push(upcomingOneTimeVariantsData);
                                                                                            if ((globalIndex) === resp?.length) {
                                                                                                upcomingOneTimeVariants.forEach(item => {
                                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center; max-width: 46%; margin: 1% !important; "> <img src="' + item?.prdImage + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1;width: 100%;">'
                                                                                                    jQuery('#orderImage_' + orderItem.id).append(upcomingOrderImage);
                                                                                                    var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + item?.variantName + '<span style="color: #eb3023; text-decoration: underline; cursor: pointer; margin-left: 10px;" onClick="deleteOneOffProducts(' + item.id +', ' + contractId +', ' + orderItem.id + ')">delete</span></div>';
                                                                                                    jQuery('#orderTitle_' + orderItem.id).append(upcomingOrderProductTitle);
                                                                                                })
                                                                                            }
                                                                                            }
                                                                                        )
                                                                                        .catch(err => {
                                                                                            console.log(err);
                                                                                        });
                                                                                        }
                                                                                        catch(error)
                                                                                        {
                                                                                        console.log(error);
                                                                                        continue;
                                                                                        }
                                                                                    }
                                                                                    }})
                                                                                .catch(err => {
                                                                                    console.log(err);
                                                                                });
                                                                            }

                                                                            })
                                                                        }
                                                                    },
                                                                    error: function (xhr, status, error) {
                                                                        console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                                                                    }
                                                                })
                                                            }
                                                        }
                                                    });
                                                    }
                                                    else {
                                                        var subscriptionList =
                                                                '    <div style="margin: 50px 0;">'
                                                                +'      <div class="grid" style="margin-left:0px;box-shadow: 0 10px 20px 0 rgb(0 0 0 / 15%);border-radius: 10px;overflow: hidden;">'
                                                                +'          <div class="grid" style="background: #242222;color: #fff;margin-left:0px;padding: 3px 0;">'
                                                                +'              <div class="grid__item small--one-whole one-half">'
                                                                +'                  <p class="appstle_sub-title" style="color: #fff;">' + subscriptionNoText + ' - #' + subItem.node.id.split('/')[4] + '</p>'
                                                                +'              </div>'
                                                                +'          </div>'
                                                                +'          <div class="grid__item appstle_subscription_flex_override" style="margin: 22px 0;">'
                                                                +'              <div class="grid__item small--one-whole one-third" style="padding-left: 0;">'
                                                                +'                  <div style="display: flex; flex-wrap: wrap;" class="appstle_subscription_contract_image_wrapper_' + contractId + '">'
                                                                +'                  </div>'
                                                                +'              </div>'
                                                                +'              <div class="appstle_subscription_contract_content_wrapper_mobile grid__item small--one-whole two-thirds appstle_subscription_detail appstle_subscription_contract_content_wrapper_' + contractId + '">'
                                                                +'                  <p class="appstle_sub-title appstle_mb-0 appstle_subscription_contract_title" style="">'
                                                                +'                      ' + subItem.node?.billingPolicy?.intervalCount + ' ' + frequencyIntervalTranslate + ' ' + subscriptionNoText + ''
                                                                +'                      <span class="appstle_' + subItem.node.status + '_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (subItem.node.status == "ACTIVE" ?  activeBadgeText : (subItem.node.status === "CANCELLED" ? closedBadgeText : (subItem.node.status === "PAUSED" ? pauseBadgeText : "")) )+ '</span>'
                                                                +'                  </p>'
                                                                +'' +               ((subItem.node.status === "CANCELLED" || subItem.node.status === "PAUSED") ? '' : ('<div class="appstle_orderDate_' + subItem.node?.status + '" style="color: #495667;">'
                                                                +'                      <div class="appstle_font_size appstle_order_detail_row">'
                                                                +'                          <strong>' + nextOrderText + ': </strong>'
                                                                +'                          <div class="appstle_ml-10 appstle_next_oder_date_wrapper">'
                                                                +'                              <span>' + (new Date(subItem.node?.nextBillingDate).toDateString()) + '</span>'
                                                                +'                              <p style="display:none; margin: 0;margin-left: 10px;color: #2c6ecb;" class="appstle_ml-10" id="orderDateloadingText_' + contractId + '">'
                                                                +'                                  Processing.. Please Wait'
                                                                +'                              </p>'
                                                                +''+                             ((changeNextOrderDateFlag == "true") ? ('<button class="appstle_font_size appstle_order-detail-edit-button editBtnOrderDate_' + contractId + ' appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '" onclick="showChangeOrderDateForm(' + contractId + ')">'
                                                                +'                                  <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                +'                              </button>') : '') + ''
                                                                +'                          </div>'
                                                                +'                      </div>'
                                                                +'                      <div class="appstle_form_margin appstle_orderDateformDiv_' + contractId + ' appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '"  style="display: none; margin-top: 4px;">'
                                                                +'                          <form id="orderDateform_' + contractId + '" name="orderDate_' + contractId + '"   class ="appstle_changeNextOrderDateFlag_' + changeNextOrderDateFlag + '">'
                                                                +'                              <input type="date" id="changeOrderDate" style="width:100%" name="changeOrderDate"  min='+ (new Date()).toISOString().slice(0,10)+' value='+(new Date(subItem.node.nextBillingDate)).toISOString().slice(0,10)+'>'
                                                                +'                          </form>'
                                                                +'                           <div class="appstle_buttonGroup">'
                                                                +'                              <button class="appstle_order-detail_cancel-button" onclick="hideOrderDateForm(' + contractId + ')">' + cancelChangeOrderBtnText + '</button>'
                                                                +'                              <button class="appstle_order-detail_update-button appstle_ml-10"  onclick="updateOrderDate(' + contractId + ')">' + updateChangeOrderBtnText + '</button>'
                                                                +'                           </div>'
                                                                +'                      </div>'
                                                                +'                  </div>')) + ''
                                                                +'' +               ((subItem.node.status === "CANCELLED") ? '' : ('<div style="color: #465661;" class="appstle_orderFrequency_' + subItem.node.status + '">'
                                                                +'                      <div class="appstle_font_size appstle_order_detail_row">'
                                                                +'                          <strong>' + orderFrequencyText + ': </strong>'
                                                                +'                          <div class="appstle_ml-10" style="DISPLAY: FLEX; align-items: center;">'
                                                                +'                              <span>' + subItem.node?.billingPolicy?.intervalCount + ' ' + frequencyIntervalTranslate + '</span>'
                                                                +'                              <p style="display:none; margin: 0;margin-left: 10px;color: #2c6ecb;" class="appstle_ml-10" id="orderloadingText_' + contractId + '">Processing.. Please Wait</p>'
                                                                +''+                              ((subItem.node.status === "PAUSED") ? '' : ((changeOrderFrequencyFlag == "true") ? ('<button class="appstle_font_size appstle_order-detail-edit-button editBtnFrequency_' + contractId + ' appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '" onclick="showFrequencyForm(' + contractId + ')"  style="padding-top: 0; display: flex; align-items: center;">'
                                                                +'                                  <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                +'                              </button>') : '')) + ''
                                                                +'                          </div>'
                                                                +'                      </div>'
                                                                +'                      <div class="appstle_form_margin appstle_orderFrequencyformDiv_' + contractId + ' appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '"  style="display: none; margin-top: 5px;" >'
                                                                +'                          <form id="orderFrequencyform_' + contractId + '" name="orderfrequency_' + contractId + '" class ="appstle_changeOrderFrequencyFlag_' + changeOrderFrequencyFlag + '">'
                                                                +'                              <input type="number" id="frequencyCount"  name="frequencyCount" value="' + subItem.node?.billingPolicy?.intervalCount + '" min="1" style="width:65%">'
                                                                +'                              <select id="frequencyInterval" name="frequencyInterval" style="width:33%">'
                                                                +'                                  <option value="DAY" ' + ((subItem.node?.billingPolicy?.interval === "DAY") ? "selected" : "") +'>'+DayText+'</option>'
                                                                +'                                  <option value="WEEK" ' + ((subItem.node?.billingPolicy?.interval === "WEEK") ? "selected" : "") +'>'+WeekText+'</option>'
                                                                +'                                  <option value="MONTH" ' + ((subItem.node?.billingPolicy?.interval === "MONTH") ? "selected" : "") +'>'+MonthText+'</option>'
                                                                +'                                  <option value="YEAR" ' + ((subItem.node?.billingPolicy?.interval === "YEAR") ? "selected" : "") +'>'+YearText+'</option>'
                                                                +'                              </select>'
                                                                +'                          </form>'
                                                                +'                            <div class="appstle_buttonGroup">'
                                                                +'                              <button class="appstle_order-detail_cancel-button" onclick="hideFrequencyForm(' + contractId + ')">' + cancelFreqBtnText + '</button>'
                                                                +'                              <button class="appstle_order-detail_update-button appstle_ml-10" onclick="updateFrequency(' + contractId + ')">' + updateFreqBtnText + '</button>'
                                                                +'                             </div>'
                                                                +'                      </div>'
                                                                +'                  </div>')) + ''
                                                                +'                  <div style="margin: 8px;margin-top: 25px;color: #495661 !important;">'
                                                                +'                      <div class="appstle_font_size" style="">'
                                                                +'                          <b style="color: #495667;">' + productLabelText + '</b>'
                                                                +'                      </div>'
                                                                +'                      <div class="appstle_subscription_contract_product_list_wrapper_' + contractId + '"></div>'
                                                                +'                  </div>'
                                                                +''+                ((pauseResumeSubscriptionFlag === "true") ? ((subItem.node.status === "ACTIVE") ? ('<button style="margin-right: auto;align-self: flex-start;display: flex;align-items: center;" class="appstle_pause_subscription_button btn" onclick="pauseSubscription(' + contractId + ')">'
                                                                +'                      <div style="display: none; margin-right: 5px;" class="appstle_loaderTiny"></div>'
                                                                +'                      <div class="appstle_icon_wrapper" style="display: flex; border: 1px solid #fff; padding: 5px; justify-content: center; align-items: center; margin-right: 5px; border-radius: 50%;">'
                                                                +'                          <div class="appstle_pause_icon"></div>'
                                                                +'                      </div>'
                                                                +'                      ' + pauseSubscriptionText + ''
                                                                +'                   </button>') : '') : '') + ''
                                                                +''+                ((pauseResumeSubscriptionFlag === "true") ? ((subItem.node.status === "PAUSED") ? ('<button style="margin-right: auto;align-self: flex-start;display: flex;align-items: center;" class="appstle_resume_subscription_button btn" onclick="resumeSubscription(' + contractId + ')">'
                                                                +'                      <div style="display: none; margin-right: 5px;" class="appstle_loaderTiny"></div>'
                                                                +'                      <div class="appstle_icon_wrapper" style="display: flex; border: 1px solid #fff; padding: 5px; justify-content: center; align-items: center; margin-right: 5px; border-radius: 50%;">'
                                                                +'                          <div class="appstle_play_icon"></div>'
                                                                +'                      </div>'
                                                                +'                      ' + resumeSubscriptionText + ''
                                                                +'                   </button>') : '') : '') + ''
                                                                +'' +                  ((subItem.node?.status === "CANCELLED" || subItem.node.status === "PAUSED") ? '' : ('<div class="appstle_subscription_contract_detail_toggle_wrapper appstle_font_size" style="display: flex; align-items: center; justify-content: flex-end; letter-spacing: 0.3px; margin-top: auto;" onClick=toggleContractAccordian(this) data-accordian-selector=".appstle_accordian_wrapper_' + contractId + '">'
                                                                +'                      <span class="appstle_contract_see_more">' + seeMoreDetailsCta + '</span>'
                                                                +'                      <span style="display: none" class="appstle_contract_hide_more">' + hideDetalsCta + '</span>'
                                                                +'                      <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px; font-weight: bold;"></span>'
                                                                +'                  </div>')) + ''
                                                                +'               </div>'
                                                                +'          </div>'
                                                                +'     </div>'
                                                                + '</div>'

                                                                    var addressDetails = extResult?.deliveryMethod?.address
                                                                    var accordionSection =
                                                                    '   <div style="display: none;" class="appstle_accordian_wrapper_' + contractId + '">'
                                                                    +'      <button class="accordion appstle_productAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                    +'          <div class="appstle_accordian_line_1"></div>'
                                                                    +'          <span style="margin: 0 10px;">' + productAccordian + '</span>'
                                                                    +'          <div class="appstle_accordian_line_2"></div>'
                                                                    +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                    +'      </button>'
                                                                    +'      <div style="display: none;" class="panel appstle_productAcc_' + subItem.node.status + '" id="sub' + index + '">'
                                                                    +'      </div>'
                                                                    +'      <button class="accordion appstle_ShippingAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                    +'          <div class="appstle_accordian_line_1"></div>'
                                                                    +'          <span style="margin: 0 10px;">' + ShippingAccordianLabel + '</span>'
                                                                    +'          <div class="appstle_accordian_line_2"></div>'
                                                                    +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                    +'      </button>'
                                                                    +'      <div style="display: none;" class="panel appstle_ShippingAcc_' + contractId + ' appstle_ShippingAcc_' + subItem.node.status + '" id="sub' + index + '">'
                                                                    +'      <div class="appstle_product_wrapper" style="flex-direction: column;">'
                                                                    +'        <div class="appstle_address_header_title">'
                                                                    +'          ' + addressHeaderTitleText + ''
                                                                    +''+        ((changeShippingAddressFlag == "true") ? ('<button style="padding-top: 14px;" class="appstle_font_size appstle_order-detail-edit-button editBtnAddress_' + contractId + ' appstle_changeAddressFlag_' + changeShippingAddressFlag + '" onclick="toggleChangeAddressForm(' + contractId + ')">'
                                                                    +'             <i class="lnr lnr-pencil btn-icon-wrapper"></i>'
                                                                    +'           </button>') : '') + ''
                                                                    +'        </div>'
                                                                    +'        <div style="align-self: flex-start;" class="appstle_address_viewOnly">'
                                                                    +''+        ((addressDetails.firstName) ? ('<span>' + addressDetails.firstName  + '</span>') : '') + ' ' + ((addressDetails.lastName) ? ('<span>' + addressDetails.lastName  + '</span>') : '') + ''
                                                                    +''+        ((addressDetails.address1) ? ('<div>' + addressDetails.address1  + '</div>') : '')
                                                                    +''+        ((addressDetails.address2) ? ('<div>' + addressDetails.address2  + '</div>') : '')
                                                                    +''+        ((addressDetails.city) ? ('<span>' + addressDetails.city  + '</span>') : '')
                                                                    +''+        ((addressDetails.province) ? ('<span>, ' + addressDetails.province  + '</span>') : '')
                                                                    +''+        ((addressDetails.country) ? ('<span>, ' + addressDetails.country  + '</span>') : '')
                                                                    +''+        ((addressDetails.zip) ? ('<div>' + addressDetails.zip  + '</div>') : '')
                                                                    +''+        ((addressDetails.phone) ? ('<div>' + addressDetails.phone  + '</div>') : '')
                                                                    +'        </div>'
                                                                    +'        <div style="display: none" class="appstle_address_editForm">'
                                                                    +'        <form style="width: 100%;">'
                                                                    +'          <div class="grid">'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="firstName">' + firstNameLabel + '</label>'
                                                                    +'                  <input type="text" name="firstName" value="' + (addressDetails.firstName ? addressDetails.firstName : "")  + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="lastName">' + lastNameLabel + '</label>'
                                                                    +'                  <input type="text" name="lastName" value="' + (addressDetails.lastName ? addressDetails.lastName : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="company">' + companyLabel + '</label>'
                                                                    +'                  <input type="text" name="company" value="' + (addressDetails.company ? addressDetails.company : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="phone">' + phoneLabel + '</label>'
                                                                    +'                  <input type="text" name="phone" value="' + (addressDetails.phone ? addressDetails.phone : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="address1">' + address1Label + '</label>'
                                                                    +'                  <input type="text" name="address1" value="' + (addressDetails.address1 ? addressDetails.address1 : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="address2">' + address2Label + '</label>'
                                                                    +'                  <input type="text" name="address2" value="' + (addressDetails.address2 ? addressDetails.address2 : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="city">' + cityLabel + '</label>'
                                                                    +'                  <input type="text" name="city" value="' + (addressDetails.city ? addressDetails.city : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="country">' + countryLabel + '</label>'
                                                                    +'                  <input type="text" name="country" value="' + (addressDetails.country ? addressDetails.country : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="province">' + provinceLabel + '</label>'
                                                                    +'                  <input type="text" name="province" value="' + (addressDetails.province ? addressDetails.province : "") + '">'
                                                                    +'              </div>'
                                                                    +'              <div class="grid__item small--one-whole one-half appstle_address_item">'
                                                                    +'                  <label for="zip">' + zipLabel + '</label>'
                                                                    +'                  <input type="text" name="zip" value="' + (addressDetails.zip ? addressDetails.zip : "") + '">'
                                                                    +'              </div>'
                                                                    +'          </div>'
                                                                    +'          <input type="hidden" name="countryCode" value="' + (addressDetails.countryCode ? addressDetails.countryCode : "") + '">'
                                                                    +'          <input type="hidden" name="provinceCode" value="' + (addressDetails.provinceCode ? addressDetails.provinceCode : "") + '">'
                                                                    +'          <input type="hidden" name="name" value="">'
                                                                    +'        </form>'
                                                                    +'        <div class="appstle_buttonGroup">'
                                                                    +'             <button class="appstle_edit_shipping_cancel-button" onclick="toggleChangeAddressForm(' + contractId + ')">' + cancelEditShippingBtnText + '</button>'
                                                                    +'             <button class="appstle_edit_shipping_update-button appstle_ml-10" onclick="updateShippingAddress(' + contractId + ')"><div style="display: none;" class="appstle_loaderTiny"></div><span>' + updateEditShippingBtnText + '</span></button>'
                                                                    +'        </div>'
                                                                    +'       </div>'
                                                                    +'      </div>'
                                                                    +'      </div>'
                                                                    +'      <button class="accordion appstle_upcomingAcc_' + subItem.node.status + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)"><div class="appstle_accordian_line_1"></div><span style="margin: 0 10px;">' + (isPrepaidplan ? upcomingFulfillmentAccordion : upcomingOrderAccordionTitle ) + '</span><div class="appstle_accordian_line_2"></div> <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span></button>'
                                                                    + '     <div style="display: none;" class="panel appstle_upcomingAcc_' + subItem.node.status + '" id="orderAcc_' + subItem.node?.id.split('/')[4] + '">'
                                                                    + '     </div>'
                                                                    + '     <button class="accordion" style="display: flex; align-items: center;" onclick="accordionToggle(this)">'
                                                                    +'          <div class="appstle_accordian_line_1"></div>'
                                                                    +'          <span style="margin: 0 10px;">' + paymentDetailAccordionTitle + '</span>'
                                                                    +'          <div class="appstle_accordian_line_2"></div>'
                                                                    +'          <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span>'
                                                                    +'      </button>'
                                                                    +'      <div class="appstle_product_wrapper" style="display: none; flex-direction: column;">'
                                                                    +'          <div class="appstle_font_size appstle_subscription_payment_wrapper">'
                                                                    +'          <div style="flex-grow: 1;align-self: flex-start;">'
                                                                    +'              <div>'
                                                                    +'                  <div>'
                                                                    +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                    +'                          <b>' + paymentMethodTypeText + '</b>: ' + paymentMethod + ' - ' + paymentData?.brand?.toUpperCase() + endingWithText + paymentData?.lastDigits + ''
                                                                    +'                      </div>'
                                                                    +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                    +'                          <b>' + cardHolderNameText + '</b>: '+ paymentData?.name +''
                                                                    +'                      </div>'
                                                                    +'                      <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                    +'                          <b>' + cardExpiryText + '</b>: ' + paymentData?.expiryMonth + ' / ' + paymentData?.expiryYear + ''
                                                                    +'                      </div>'
                                                                    +'                  </div>'
                                                                    +'              </div>'
                                                                    +'          </div>'
                                                                    +'          <button class="appstle_font_size appstle_updatePaymentButton btn appstle_order-detail-edit-button appstle_paymentBtnUpdate_' + subItem.node?.id?.split('/')[4] + '" onclick="updatePaymentDetails(' + contractId + ')">' + updatePaymentBtnText + '</button>'
                                                                    +'          </div>'
                                                                    +'          <p style="align-self: flex-end;font-size: 13px;font-weight: bold;">' + paymentNotificationText + '</p>'
                                                                    +'      </div>'
                                                                    + '' + (!(shopName == "powerhouse-brewing-company.myshopify.com" && isPrepaidplan)? ('<button class="accordion appstle_cancelbtn_' + subItem.node.status + ' appstle_cancelSubscriptionFlag_' + cancelSubscriptionFlag + '" style="display: flex; align-items: center;" onclick="accordionToggle(this)"><div class="appstle_accordian_line_1"></div><span style="margin: 0 10px;">' + cancelAccordionTitle + '</span><div class="appstle_accordian_line_2"></div> <span class="lnr lnr-chevron-right appstle_ml-10" style="font-size: 10px;font-weight: bold;margin-left: auto;"></span></button>') : '') + ''
                                                                    + '    <div style="display: none;" class="panel appstle_cancelbtn_' + subItem.node.status + ' appstle_cancelSubscriptionFlag_' + cancelSubscriptionFlag + '">'
                                                                    + '          <div class="grid text-center appstle_cancelbtn_' + subItem.node.status + '" style="margin-top: 27px">    '
                                                                    + '               <div class="grid__item">'
                                                                    + '                   <button type="button" onclick="cancelSubscription(' + subItem.node?.id?.split('/')[4] + ')" class="appstle_cencelSubscription btn appstle_cancelBtnText_' + subItem.node.id.split('/')[4] + '">' + cancelSubscriptionBtnText + '</button>       '
                                                                    + '               </div>'
                                                                    + '            </div>'
                                                                    + '    </div>'
                                                                    +'   </div>'



                                                        jQuery('.appstle_myProduct').append(subscriptionList);
                                                        jQuery('.appstle_myProduct').append(accordionSection);
                                                        subItem.node.lines?.edges?.forEach((contractItem, idx) => {

                                                            if (contractItem?.node?.productId != null) {
                                                                var subscribedProduct =
                                                                '<div class="appstle_product_wrapper" id="editproduct' + index + idx + '" data-line-id="' + contractItem?.node?.id + '"  data-contract-id="' + contractId + '" data-shop="' + shopName + '" data-product-id="' + contractItem?.node?.productId.split('/').pop() + '" data-variant-id="' + contractItem?.node?.variantId + '"  data-price="' + (contractItem?.node?.lineDiscountedPrice?.amount / contractItem.node.quantity) + '" data-currency-code="' + contractItem.node?.lineDiscountedPrice?.currencyCode + '">'
                                                                    +' <img src=' + contractItem.node?.variantImage?.transformedSrc + ' alt="" height="auto" width="100" style="padding-left: 0; border-radius: 2px">'
                                                                    +' <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                    +'      <strong>' + contractItem.node.title + '<span class="appstle_variantTitle_' + contractItem.node.productId.split('/')[4] + '"> - ' + contractItem?.node?.variantTitle + '<span></strong>'
                                                                    +'      <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                    +'          <div>'
                                                                    +'              <div> <b>' + quantityLbl + ':</b> <span>' + contractItem.node?.quantity + '</span></div>'
                                                                    +'              <div style="margin-top: 5px;"><b>' + amountLbl + ':</b> <span>' + contractItem.node?.lineDiscountedPrice?.amount + ' ' + contractItem.node?.lineDiscountedPrice?.currencyCode + '</span></div>'
                                                                    +'              <div style="margin-top: 5px;"><b>' + nextOrderText + '</b>: ' + (new Date(subItem.node?.nextBillingDate).toDateString()) + '</div>'
                                                                    +'          </div>'
                                                                    +'      </div>'
                                                                    +'      <div style="display: none;" class="appstle_edit_wrapper">'
                                                                    +'          <form>'
                                                                    +'              <div class="appstle_form_group">'
                                                                    +'                  <label>' + editQuantityLabel + '</label>'
                                                                    +'                  <input type="number" name="quantity" onChange=updateSubtotal("editproduct' + index + idx + '") min="1" value="' + contractItem.node.quantity + '">'
                                                                    +'                  <span class="appstle_input_error" style="display: none;">Please enter a valid number</span>'
                                                                    +'              </div>'
                                                                    +'              <div class="appstle_form_group appstle_select_wrapper">'
                                                                    +'                  <label>Edit Variant</label>'
                                                                    +'                  <select onChange=updateSubtotal("editproduct' + index + idx + '") style="width: 100%" name="variantId" data-edit="editproduct' + index + idx + '">'
                                                                    +'                  </select>'
                                                                    +'              </div>'
                                                                    +'          </form>'
                                                                    +'      </div>'
                                                                    +'      <div style="display: none;" class="appstle_buttonGroup" id="appstle_buttonGroup' + index + idx + '">'
                                                                    +'          <div class="appstle_edit_total"><b>' + subTotalLabel + ': <span>' + contractItem.node?.lineDiscountedPrice?.amount + ' ' + contractItem.node?.lineDiscountedPrice?.currencyCode + '</span></b></div>'
                                                                    +'          <button class="appstle_cancelButton" data-edit="editproduct' + index + idx + '">' + cancelButtonText + '</button>'
                                                                    +'          <button class="appstle_updateButton appstle_ml-10" data-edit="editproduct' + index + idx + '">' + updateButtonText + '</button>'
                                                                    +'      </div>'
                                                                    +'  </div>'
                                                                    +'  <div class="appstle_editDeleteGroup">'
                                                                    +''+      ((editProductFlag == "true") ? ('<button class="appstle_editButton" data-edit="editproduct' + index + idx + '"><i class="lnr lnr-pencil btn-icon-wrapper"></i></button>') : '') + ''
                                                                    +''+      ((deleteProductFlag == "true") ? ('<button ' + (subItem.node.lines?.edges?.length <= 1 && 'style="display: none;"') + ' class="appstle_deleteButton appstle_ml-10" data-edit="editproduct' + index + idx + '"><i class="lnr lnr-trash btn-icon-wrapper"></i></button>') : '')
                                                                    +'  </div>'
                                                                    +'</div>'
                                                                jQuery('#sub' + index).append(subscribedProduct)
                                                                if (contractItem.node?.variantTitle == null) {
                                                                    jQuery('.appstle_variantTitle_' + contractItem.node?.productId?.split('/')[4]).hide();
                                                                }

                                                                jQuery('.appstle_subscription_contract_product_list_wrapper_' + contractId).append(
                                                                    '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem.node?.quantity > 1 ? '<span style="margin-left: 8px">x ' + contractItem.node?.quantity + '</span>' : '') + '</div>'
                                                                )
                                                                let imageElement = jQuery('<img onload=appstle_adjust_container_height(this)  data-contract-id="' + contractId + '" src="' + contractItem?.node?.variantImage?.transformedSrc + '" alt="" style="max-width: 46%; margin: 1% !important; display: block; margin: 0 auto; align-self: flex-start; padding-left: 0; border-radius: 2px; flex-grow: 1">');
                                                                // imageElement.css('height', imageHeight);

                                                                jQuery('.appstle_subscription_contract_image_wrapper_' + contractId).append(imageElement);
                                                            }
                                                            else {
                                                                var subscribedProduct =
                                                                    '<div style="display: flex; align-items: center;margin-top:15px">'
                                                                    + '   <div style="text-align: center;padding: 20px;background-color: oldlace;">'
                                                                    + '        <strong>' + contractItem.node?.title + '</strong> has been removed'
                                                                    + '   </div>'
                                                                    + '</div>'
                                                                jQuery('#sub' + index).append(subscribedProduct)
                                                            }

                                                        });
                                                        if(shopName != "taelor-style-dev.myshopify.com" && (addAdditionalProductFlag == "true"))
                                                        jQuery('#sub' + index).prepend(    '<div class="appstle_product_search_wrapper" data-contract-id="' + contractId + '" data-selling-plan-id="' + ((extResult?.lines?.edges?.map(line => { return line?.node?.sellingPlanId })).join("|").toString()) + '" data-product-addition-type="SUBSCRIBE">'
                                                        +       '<button style="margin-right: auto;" class="appstle_show_add_products_button btn" onclick="showAddProductsType(this)">' + addProductButtonText + '</button>'
                                                        +        '<div style="display: none;">'
                                                        +           '<label class="appstle_add_product_select_label" style="margin-bottom: 5px;"><b>' + addProductLabelText + '(Add to Subscription)</b></label>'
                                                        +           '<div style="display: flex;">'
                                                        +               '<select style="flex-grow: 1; height: 100%;" class="appstle_product-search-select" data-contract-id="' + contractId + '" ></select>'
                                                        +               '<button class="appstle_product-search-button appstle_ml-10" data-contract-id="' + contractId + '">' + addProductButtonText + '</button>'
                                                        +               '<button class="appstle_hide_add_products_button btn appstle_ml-10" onclick="hideAddProductsSearchBar(this)">' + cancelButtonText + '</button>'
                                                        +           '</div>'
                                                        +        '</div>'
                                                        +   '<div style="display: none;">'
                                                        +   '<div class="plans">'
                                                        +       '<div style="margin-bottom: 20px;"><b>Select Purchase Option</b></div>'
                                                        +       '<div class="plansWrapper">'
                                                        +           '<label class="plan basic-plan mr-3" for="isSubscriptionPurchase">'
                                                        +               '<input type="radio" id="isSubscriptionPurchase" name="purchaseOption" value="SUBSCRIBE" checked="" onChange="updateAddProductText(event)">'
                                                        +                   '<div class="plan-content">'
                                                        +                       '<img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/discount-2045_57GSxj6bEsg.png?updatedAt=1632475086786" alt="">'
                                                        +                       '<div class="plan-details">'
                                                        +                           '<span>Add to Subscription</span>'
                                                        +                           '<p>Upsell Discount</p>'
                                                        +                       '</div>'
                                                        +                   '</div>'
                                                        +           '</label>'
                                                        +           '<label class="plan complete-plan" for="isOneTimePurchase">'
                                                        +               '<input type="radio" id="isOneTimePurchase" name="purchaseOption" label="One time purchase" value="ONE_TIME" onChange="updateAddProductText(event)">'
                                                        +                   '<div class="plan-content">'
                                                        +                       '<img loading="lazy" src="https://ik.imagekit.io/mdclzmx6brh/shipping-2050_7nIMMvG_4hy.png?updatedAt=1632475174135" alt="">'
                                                        +                       '<div class="plan-details">'
                                                        +                           '<span>One Time Purchase</span>'
                                                        +                           '<p>Get your product along with your next subscription order without any Shipping costs.</p>'
                                                        +                       '</div>'
                                                        +                   '</div>'
                                                        +           '</label>'
                                                        +       '</div>'
                                                        +   '</div>'
                                                        +   '<div class="appstle_addProduct_type_button_wrapper">'
                                                        +       '<button  onclick="showAddProductsSearchBar(this)" class="appstle_addProduct_type_next_button">Next</button>'
                                                        +       '<button onclick="hideAddProductsType(this)" class="appstle_addProduct_type_cancel_button appstle_ml-10">Cancel</button>'
                                                        +   '</div>'
                                                        +   '</div>'
                                                        +   '</div>'

                                                        )

                                                        if(isPrepaidplan)
                                                            {
                                                                  extResult?.originOrder?.fulfillmentOrders?.edges?.map((orderItem) => {
                                                                                var upcomingOrder =
                                                                                '<div class="appstle_product_wrapper appstle_upcoming_item" data-billing-id="' + orderItem.node.id + '" data-contract-id="' + contractId + '">'
                                                                            +'      <div style="max-width: 100px; display: flex; flex-wrap: wrap;" id="orderImage_' + orderItem.node.id.split("/").pop() + '">'
                                                                            +'      </div>'
                                                                            +'      <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                            +'          <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                            +'              <div>'
                                                                            +'                  <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                            +'                      <b>' + nextOrderDateLbl + '</b>: ' + (new Date(orderItem.node?.fulfillAt).toDateString()) + ''
                                                                            +'                      <span class="appstle_' + orderItem.node.status +'_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (orderItem.node.status == "SKIPPED" ? skipBadgeText : queueBadgeText)  + '</span>'
                                                                            +'                  </div>'
                                                                            +'              </div>'
                                                                            +'          </div>'
                                                                            +'          <div style="margin-top: 5px;color: #495661 !important;">'
                                                                            +'              <div class="appstle_font_size" style="">'
                                                                            +'                  <b style="color: #495667;font-size: 13px;">' + productLabelText + ':</b>'
                                                                            +'              </div>'
                                                                            +'              <div  id="orderTitle_' + orderItem.node.id.split("/").pop() + '">'
                                                                            +'              </div>'
                                                                            +'         </div>'
                                                                            +'      </div>'
                                                                            +'' + (orderItem.node.status === 'SKIPPED' ? '' : '<button class="appstle_skipOrderButton btn appstle_skipShipmentFlag_'+skipShipmentFlag+' appstle_skiporder_' + orderItem.node.id.split("/").pop() + '" onclick="skipBillingOrder(' + orderItem.node.id.split("/").pop() + ')">' + skipOrderText + '</button>') + ''
                                                                            +'  </div>'


                                                                                jQuery('#orderAcc_' + subItem.node?.id?.split('/')[4]).append(upcomingOrder);

                                                                                subItem.node.lines?.edges?.forEach(contractItem => {
                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center; max-width: 46%; margin: 1% !important;"> <img src="' + contractItem.node?.variantImage?.transformedSrc + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1;width: 100%;">'
                                                                                    var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem?.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem?.node?.quantity > 1 ? '<span style="margin-left: 8px">X' + contractItem?.node?.quantity + '</span>' : '') + '</div>'
                                                                                    jQuery('#orderImage_' + orderItem.node.id.split("/").pop()).append(upcomingOrderImage);
                                                                                    jQuery('#orderTitle_' + orderItem.node.id.split("/").pop()).append(upcomingOrderProductTitle);
                                                                                    if (contractItem.node?.variantTitle == null) {
                                                                                        jQuery('.appstle_variantTitle_' + contractItem.node?.productId.split('/')[4]).hide();
                                                                                    }
                                                                                });
                                                                            })
                                                            }
                                                            // Upcoming Order API CALL HERE
                                                        if (subItem.node?.status == "ACTIVE" && !isPrepaidplan) {
                                                            var upcomingOrderUrl;
                                                            if (customerId != null) {
                                                                upcomingOrderUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-billing-attempts/top-orders?contractId=" + contractId + "&customerId=" + customerId + "&api_key=" + window?.appstle_api_key;
                                                            }
                                                            else {
                                                                upcomingOrderUrl = "https://subscription-admin.appstle.com/api/external/v2/subscription-billing-attempts/top-orders?contractId=" + contractId + "&customerUid=" + token + "&api_key=" + window?.appstle_api_key;
                                                            }
                                                            jQuery.ajax({
                                                                type: "GET",
                                                                url: upcomingOrderUrl,
                                                                dataType: "json",
                                                                headers: {
                                                                    'Access-Control-Allow-Origin': '*',
                                                                },
                                                                success: function (result, status, xhr) {
                                                                    if (result?.length > 0) {

                                                                        result.map((orderItem, idx) => {

                                                                            var upcomingOrder =
                                                                                    '<div class="appstle_product_wrapper appstle_upcoming_item" data-billing-id="' + orderItem.id + '" data-contract-id="' + orderItem.contractId + '">'
                                                                                +'      <div style="max-width: 100px; display: flex; flex-wrap: wrap;" id="orderImage_' + orderItem.id + '">'
                                                                                +'      </div>'
                                                                                +'      <div style="flex-grow: 1;margin-left: 20px;align-self: flex-start;">'
                                                                                +'          <div class="appstle_font_size" style="margin-top: 5px; display: flex;">'
                                                                                +'              <div>'
                                                                                +'                  <div style="margin-top: 5px;color: #465661; display: flex; align-items: center;">'
                                                                                +'                      <b>' + nextOrderDateLbl + '</b>: ' + (new Date(orderItem?.billingDate).toDateString()) + ''
                                                                                +'                      <span class="appstle_' + orderItem.status +'_status" style="margin-left: 20px;display: flex;align-items: center;justify-content: center;">' + (orderItem.status == "SKIPPED" ? skipBadgeText : queueBadgeText)  + '</span>'
                                                                                +'                  </div>'
                                                                                +'              </div>'
                                                                                +'          </div>'
                                                                                +'          <div style="margin-top: 5px;color: #495661 !important;">'
                                                                                +'              <div class="appstle_font_size" style="">'
                                                                                +'                  <b style="color: #495667;font-size: 13px;">' + productLabelText + ':</b>'
                                                                                +'              </div>'
                                                                                +'              <div  id="orderTitle_' + orderItem.id + '">'
                                                                                +'              </div>'
                                                                                +'         </div>'
                                                                                +'      </div>'
                                                                                +'' + (orderItem.status === 'SKIPPED' ? '' : '<button class="appstle_skipOrderButton btn appstle_skipShipmentFlag_'+skipShipmentFlag+' appstle_skiporder_' + orderItem.id + '" onclick="skipBillingOrder(' + orderItem.id + ')">' + skipOrderText + '</button>') + ''
                                                                                +'  </div>'



                                                                            jQuery('#orderAcc_' + subItem.node.id.split('/')[4]).append(upcomingOrder);

                                                                            subItem.node.lines?.edges?.forEach(contractItem => {

                                                                                if (contractItem.node?.productId != null) {

                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center; max-width: 46%; margin: 1% !important; "> <img src="' + contractItem?.node?.variantImage?.transformedSrc + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1; width: 100%;">'
                                                                                        var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + contractItem?.node?.title + (contractItem?.node?.variantTitle ? (' - ' + contractItem?.node?.variantTitle) : '') + (contractItem?.node?.quantity > 1 ? '<span style="margin-left: 8px">X' + contractItem?.node?.quantity + '</span>' : '') + '</div>'
                                                                                        jQuery('#orderImage_' + orderItem?.id).append(upcomingOrderImage);
                                                                                        jQuery('#orderTitle_' + orderItem?.id).append(upcomingOrderProductTitle);
                                                                                    if (contractItem.node.variantTitle == null) {
                                                                                        jQuery('.appstle_variantTitle_' + contractItem.node?.productId?.split('/')[4]).hide();
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    var upcomingOrderImage =
                                                                                        '<div style="align-items: center; margin-top:10px">'
                                                                                        + '   <div style="text-align: center;padding: 15px;background-color: oldlace;">'
                                                                                        + '        <strong>' + contractItem.node?.title + '</strong> has been removed'
                                                                                        + '   </div>'
                                                                                        + '</div>'
                                                                                    jQuery('#orderImage_' + orderItem.id).append(upcomingOrderImage)
                                                                                }
                                                                            });
                                                                            if (idx === 0) {
                                                                            var upcomingOneTimeVariants = [];
                                                                                var subscriptionContractOneoff = 'https://subscription-admin.appstle.com/api/external/v2/subscription-contract-one-offs-by-contractId?contractId=' + contractId + '&api_key=' + window?.appstle_api_key;
                                                                                jQuery.get(subscriptionContractOneoff)
                                                                                .then(async resp => {
                                                                                    if(resp?.length > 0)
                                                                                    {
                                                                                    var globalIndex = 0
                                                                                    for await (let item of resp) {
                                                                                        try{
                                                                                        globalIndex = globalIndex + 1;
                                                                                        const productvariantUrl = 'https://' + Shopify.shop + '/products/' + item.variantHandle + '.js';
                                                                                        await jQuery.get(productvariantUrl)
                                                                                        .then(res => {
                                                                                            res = JSON.parse(res);
                                                                                            var varData = res.variants.find(variant => variant.id == item.variantId);
                                                                                            var upcomingOneTimeVariantsData = {
                                                                                            billingID: item?.billingAttemptId,
                                                                                            prdImage: 'https:' + res.featured_image,
                                                                                            variantName: res.title + ((res.variants?.length > 1) ? (" - " + varData?.title) : ""),
                                                                                            id: varData?.id
                                                                                            }
                                                                                            upcomingOneTimeVariants.push(upcomingOneTimeVariantsData);
                                                                                            if ((globalIndex) === resp?.length) {
                                                                                                upcomingOneTimeVariants.forEach(item => {
                                                                                                    var upcomingOrderImage = '<div style="display:flex; align-items:center; max-width: 46%; margin: 1% !important; "> <img src="' + item?.prdImage + '" alt="" style="display: block;margin: 0px auto;align-self: flex-start;padding-left: 0px;border-radius: 2px;flex-grow: 1;width: 100%;">'
                                                                                                    jQuery('#orderImage_' + orderItem.id).append(upcomingOrderImage);
                                                                                                    var upcomingOrderProductTitle = '<div class="appstle_font_size" style="margin-top: 5px; color: #13b5ea;">' + item?.variantName + '<span style="color: #eb3023; text-decoration: underline; cursor: pointer; margin-left: 10px;"   onClick="deleteOneOffProducts(' + item.id +', ' + contractId +', ' + orderItem.id + ')">delete</span></div>';
                                                                                                    jQuery('#orderTitle_' + orderItem.id).append(upcomingOrderProductTitle);
                                                                                                })
                                                                                            }
                                                                                            }
                                                                                        )
                                                                                        .catch(err => {
                                                                                            console.log(err);
                                                                                        });
                                                                                        }
                                                                                        catch(error)
                                                                                        {
                                                                                        console.log(error);
                                                                                        continue;
                                                                                        }
                                                                                    }
                                                                                    }})
                                                                                .catch(err => {
                                                                                    console.log(err);
                                                                                });
                                                                            }



                                                                        })

                                                                                                                                                    }
                                                                },
                                                                error: function (xhr, status, error) {
                                                                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                                                                }
                                                            })
                                                        }
                                                    }
                                },
                                error: function (xhr, status, error) {
                                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                                }
                            });
                    });


                    jQuery(".appstle_preloader").fadeOut(2000, function () {
                        jQuery(".appstle_myProduct").fadeIn(1000);
                        jQuery(".appstle_myProduct").css('visibility', 'visible');
                        if (getDeviceType() !== "mobile") {
                            jQuery('.appstle_single_item_contract').each(function(index, el) {
                                var contractId = jQuery(el).attr('data-contract-id');
                                var imageHeight = jQuery('.appstle_subscription_contract_content_wrapper_' + contractId).height();
                                jQuery('.appstle_subscription_contract_image_wrapper_' + contractId).parent().css('height', imageHeight);})
                            }
                    });

                }
        else {

                    jQuery(".appstle_preloader").fadeOut(2000, function () {
                        jQuery(".appstle_myProduct").append("<h4 style='text-align: center;margin-top: 54px;'>" + noSubscriptionMessage + "</h4>").fadeIn(1000);
                        jQuery(".appstle_myProduct").css('visibility', 'visible');
                    });
        }
    attatchListener();
    }

    function skipBillingOrder(id) {
        if (id != null && id != undefined) {
            jQuery('.appstle_skiporder_' + id).html('Processing...')
            jQuery.ajax({
                type: "PUT",
                url: "https://subscription-admin.appstle.com/api/external/v2/subscription-billing-attempts/skip-order/" + id + "?api_key=" + window?.appstle_api_key,
                dataType: "json",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
                success: function (result, status, xhr) {
                    window.location.reload();
                },
                error: function (xhr, status, error) {
                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                }
            });
        } else {
            alert("Order Id is not Found")
        }
    }

    function updatePaymentDetails(id) {
        if (id != null && id != undefined) {
            jQuery('.appstle_paymentBtnUpdate_' + id).html('Processing...');
            jQuery.ajax({
                type: "PUT",
                url: "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-payment-method?contractId=" + id + "&api_key=" + window?.appstle_api_key,
                dataType: "json",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
                success: function (result, status, xhr) {
                    jQuery(".appstle_paymentSuccessMessage_" + id).show();
                    jQuery('.appstle_paymentBtnUpdate_' + id).html(successText);
                    setTimeout(function() {jQuery('.appstle_paymentBtnUpdate_' + id).html(updatePaymentBtnText);},5000)
                },
                error: function (xhr, status, error) {
                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                    jQuery(".appstle_paymentSuccessMessage_" + id).show();
                    jQuery('.appstle_paymentBtnUpdate_' + id).html(successText);
                    setTimeout(function() {jQuery('.appstle_paymentBtnUpdate_' + id).html(updatePaymentBtnText);},5000)
                },
            });
        }
        else {
            alert("Contract Id is not Found")
        }
    }

    function cancelSubscription(id) {
        if (id != null && id != undefined) {
            jQuery('.appstle_cancelBtnText_' + id).html('Processing...')
            jQuery.ajax({
                type: "DELETE",
                url: "https://subscription-admin.appstle.com/api/subscription-contracts/" + id + "?shop=" + Shopify.shop,
                dataType: "json",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
                success: function (result, status, xhr) {
                    subscriptionInit();
                },
                error: function (xhr, status, error) {
                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                }
            });
        } else {
            alert("Product Id is not Found")
        }
    }

    function showFrequencyForm(contractId) {
        // jQuery("#orderFrequencyform_" + contractId).show();
        jQuery(".appstle_orderFrequencyformDiv_" + contractId).slideDown();
        jQuery('.editBtnFrequency_' + contractId).hide();
    }

    function showChangeOrderDateForm(contractId){
        // jQuery("#orderDateform_" + contractId).show();
        jQuery(".appstle_orderDateformDiv_" + contractId).slideDown();
        jQuery('.editBtnOrderDate_' + contractId).hide();
    }

    function hideOrderDateForm(contractId){
        // jQuery("#orderDateform_" + contractId).hide();
        jQuery(".appstle_orderDateformDiv_" + contractId).slideUp();
        jQuery('.editBtnOrderDate_' + contractId).show();
    }

    function hideFrequencyForm(contractId) {
        jQuery(".appstle_orderFrequencyformDiv_" + contractId).slideUp();
        // jQuery("#orderFrequencyform_" + contractId).hide();
        jQuery('.editBtnFrequency_' + contractId).show();
    }

      function updateOrderDate(contractId) {

        var nextBillingDate = document.forms["orderDateform_" + contractId].changeOrderDate.value;

        if (nextBillingDate != null && nextBillingDate != undefined) {
            // jQuery("#orderDateform_" + contractId).hide();
            jQuery(".appstle_orderDateformDiv_" + contractId).slideUp();
            jQuery("#orderDateloadingText_" + contractId).show();
            jQuery.ajax({
                type: "PUT",
                url: "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-billing-date?contractId=" + contractId + "&nextBillingDate=" + new Date(nextBillingDate).toISOString() + "&api_key=" + window?.appstle_api_key,
                dataType: "json",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
                success: function (result, status, xhr) {
                    subscriptionInit()
                },
                error: function (xhr, status, error) {
                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                }
            });
            // jQuery("#orderloadingText_"+contractId).hide();
        } else {
            alert("Please Provide Frequency Count or Frequency Interval")
        }

    }


    function updateFrequency(contractId) {

        var frequencyCount = document.forms["orderFrequencyform_" + contractId]?.frequencyCount?.value;
        var frequencyInterval = document.forms["orderFrequencyform_" + contractId]?.frequencyInterval?.value;

        if (frequencyCount <= 0)
            alert("Frequency Count must be greater than 1")

        if (frequencyCount != null && frequencyCount != undefined && frequencyCount >= 0 && frequencyInterval != null && frequencyInterval != undefined) {
            // jQuery("#orderFrequencyform_" + contractId).hide();
            jQuery(".appstle_orderFrequencyformDiv_" + contractId).slideUp();
            jQuery("#orderloadingText_" + contractId).show();
            jQuery.ajax({
                type: "PUT",
                url: "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-billing-interval?contractId=" + contractId + "&interval=" + frequencyInterval + "&intervalCount=" + frequencyCount + "&api_key=" + window?.appstle_api_key,
                dataType: "json",
                headers: {
                    'Access-Control-Allow-Origin': '*',
                },
                success: function (result, status, xhr) {
                    subscriptionInit();
                },
                error: function (xhr, status, error) {
                    console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
                }
            });
            // jQuery("#orderloadingText_"+contractId).hide();
        } else {
            alert("Please Provide Frequency Count or Frequency Interval")
        }

    }

    function attatchListener() {

        jQuery('.appstle_product-search-button').on('click', function() {
            var contractId = jQuery(this).attr('data-contract-id');
            var selectedProduct = jQuery('select[data-contract-id=' + contractId + ']');
            if (!selectedProduct.val()) {
                alert('Please Select a product to add.')
                return;
            }

            jQuery(".appstle_myProduct").fadeOut(500, function () {
                jQuery(".appstle_preloader").fadeIn(250);
            });

            var addProductType = jQuery(".appstle_product_search_wrapper[data-contract-id='" + contractId + "']").attr("data-product-addition-type");
            var url = "";
            var billingId = $("[data-contract-id=" + contractId + "].appstle_upcoming_item").first().attr("data-billing-id")
            var selectedValue = selectedProduct.val().split("|")
            var variantId = selectedValue[0];
            var variantHandle = selectedValue[1];
            if (addProductType === "ONE_TIME") {
                url = 'https://subscription-admin.appstle.com/api/external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id?contractId=' + contractId + '&api_key=' + window?.appstle_api_key + '&variantId=' + variantId  + '&billingAttemptId=' + billingId + '&variantHandle=' + variantHandle;
            } else {
                url = 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-add-line-item?contractId=' + contractId + '&quantity=1&variantId=gid://shopify/ProductVariant/' + variantId + '&api_key=' + window?.appstle_api_key;
            }

            var addLine_settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": url,
                    "method": "PUT",
                    "headers": {
                        "cache-control": "no-cache",
                    }
                }

                jQuery.ajax(addLine_settings).done(function (response) {
                    subscriptionInit();
                })
        })

         jQuery('.appstle_select_wrapper select').on('change', function() {
            var currentValue= jQuery(this).val();
            var selectedVariantPrice = jQuery(this).find('option[value="' + currentValue + '"]').attr('data-variant-price');
            var formid = jQuery(this).attr('data-edit');
            jQuery('#' + formid).attr('data-price', selectedVariantPrice);
        })

        jQuery('.appstle_editButton').on('click', function() {
            var formid = jQuery(this).attr('data-edit');
            jQuery('#' + formid).find('.appstle_edit_wrapper').slideDown();
            jQuery('#' + formid).find('.appstle_editDeleteGroup').hide();
            jQuery('#' + formid).find('.appstle_buttonGroup').show();
        })

        jQuery('.appstle_cancelButton').on('click', function() {
            var formid = jQuery(this).attr('data-edit');
            jQuery('#' + formid).find('.appstle_edit_wrapper').slideUp();
            jQuery('#' + formid).find('.appstle_editDeleteGroup').show();
            jQuery('#' + formid).find('.appstle_buttonGroup').hide();
        })

        jQuery('.appstle_deleteButton').on('click', function() {
            var formid = jQuery(this).attr('data-edit');
            var lineId = jQuery('#' + formid).attr('data-line-id');
            let prepaidPlanFlag = jQuery('#' + formid).attr('data-isPrepaid');
            var contractId = jQuery('#' + formid).attr('data-contract-id');
            var shop = jQuery('#' + formid).attr('data-shop');

          deletePopupConfirm(prepaidPlanFlag); // Showing Delete confirm Model

            // On click of confirm Code will execute
            jQuery('.appstle_delete_model_yesbtn').on('click', function() {
                hideDeleteConfirmPopup(); // Hide the Confirm Model
                jQuery(".appstle_myProduct").fadeOut(500, function () {
                    jQuery(".appstle_preloader").fadeIn(250);

                });

                var settings = {
                        "async": true,
                        "crossDomain": true,
                        "url": 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-remove-line-item?contractId=' + contractId + '&lineId=' + lineId + '&api_key=' + window?.appstle_api_key,
                        "method": "PUT",
                        "headers": {
                            "cache-control": "no-cache",
                            "postman-token": "06eaf057-9152-2ff9-616b-c8f3f319e47f"
                        }
                    }
                jQuery.ajax(settings).done(function (response) {
                    subscriptionInit();
                });

            })

        })

        jQuery('.appstle_updateButton').on('click', function() {
            var formid = jQuery(this).attr('data-edit');
            var lineId = jQuery('#' + formid).attr('data-line-id');
            var contractId = jQuery('#' + formid).attr('data-contract-id');
            var shop = jQuery('#' + formid).attr('data-shop');
            var price = jQuery('#' + formid).attr('data-price');
            var formData = jQuery('#' + formid + ' form').serialize();

            var inputElementValue = jQuery('#' + formid).find('input')[0].value;

            if (validateNumber(inputElementValue)) {

                jQuery(".appstle_myProduct").fadeOut(500, function () {
                    jQuery(".appstle_preloader").fadeIn(250);
                });

                jQuery('#' + formid).find('.appstle_input_error').hide();
                var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-line-item?contractId=' + contractId + '&price=' + price + '&lineId=' + lineId + '&api_key=' + window?.appstle_api_key + '&' + formData,
                    "method": "PUT",
                    "headers": {
                        "cache-control": "no-cache",
                        "postman-token": "06eaf057-9152-2ff9-616b-c8f3f319e47f"
                    }
                }
            jQuery.ajax(settings).done(function (response) {
                subscriptionInit();
            });

            } else {
                jQuery('#' + formid).find('.appstle_input_error').show();
            }
        });


        addVariant();
        addSelect2InHead();
        addIconCssInHead();
    }

    function addVariant() {
    var elementsWithProductId = Array.prototype.slice.call(jQuery('[data-product-id]'))
    const addVariantItem = async () => {
      if (elementsWithProductId.length > 0) {
        var item = elementsWithProductId.shift();
        var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": 'https://subscription-admin.appstle.com/api/data/external/v2/product?productId=' + jQuery(item).attr('data-product-id') + '&api_key=' + window?.appstle_api_key,
                    "method": "GET",
                    "headers": {
                        "cache-control": "no-cache",
                        "postman-token": "06eaf057-9152-2ff9-616b-c8f3f319e47f"
                    }
                }
        jQuery.ajax(settings).done(function (response) {
               var variants = response?.variants;
               jQuery(variants).each(function (index, variant) {
                var option = jQuery('<option />', {
                value: variant.admin_graphql_api_id,
                text: variant.title,
                selected: ((variant?.admin_graphql_api_id == jQuery(item).attr("data-variant-id")) ? true : false),
                })
                option.attr('data-variant-price', variant.price);
                option.appendTo(jQuery(item).find('select'));
            });
            if (variants.length <= 1) {
               jQuery(item).find('.appstle_select_wrapper').hide();
            }
            addVariantItem();
        });

      }
    }
    addVariantItem();
  }

  function addLineItem(items, contractId) {
    var selectedItems = items;
    jQuery(".appstle_myProduct").fadeOut(500, function () {
        jQuery(".appstle_preloader").fadeIn(250);
    });
    var addline = function() {
      if (selectedItems.length > 0) {
        var item = selectedItems.shift()

        var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": 'https://subscription-admin.appstle.com/api/external/v2/data/product?productId=' + item + '&api_key=' + window?.appstle_api_key,
                    "method": "GET",
                    "headers": {
                        "cache-control": "no-cache",
                    }
                }
            jQuery.ajax(settings).done(function (response) {
                var firstVariant = response?.variants[0]

                var addLine_settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-add-line-item?contractId=' + contractId + '&price=' + firstVariant?.price + '&quantity=1&api_key=' + window?.appstle_api_key + '&variantId=' + firstVariant?.admin_graphql_api_id,
                    "method": "PUT",
                    "headers": {
                        "cache-control": "no-cache",
                    }
                }

                jQuery.ajax(addLine_settings).done(function (response) {
                    addline();
                })
            });
        } else {
            subscriptionInit();
        }
    }
    addline();
  }

  function validateNumber(value) {
    var type = typeof value;
    if (type === 'undefined') {
      return true;
    } else if (!value.trim()) {
      return false;
    } else if (isNaN(value)) {
      return false;
    } else if (parseInt(value) < 1) {
      return false;
    } else {
      return true;
    }
  };

  function addSelect2InHead() {
       jQuery('head').append('<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" /><script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"><\/script><script src="https://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.13.3/js/standalone/selectize.js" integrity="sha512-pF+DNRwavWMukUv/LyzDyDMn8U2uvqYQdJN0Zvilr6DDo/56xPDZdDoyPDYZRSL4aOKO/FGKXTpzDyQJ8je8Qw==" crossorigin="anonymous" referrerpolicy="no-referrer"><\/script><link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/selectize@0.12.6/dist/css/selectize.bootstrap3.css">')
        checkSelect2Initialised()
  }

  function addIconCssInHead() {
    jQuery('head').append('<link rel="stylesheet" href="https://cdn.linearicons.com/free/1.0.0/icon-font.min.css">')
    jQuery('head').append('<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/pixeden-stroke-7-icon@1.2.3/pe-icon-7-stroke/dist/pe-icon-7-stroke.min.css">')
  }

  function checkSelect2Initialised() {
      try {
            initiateSelect2()
        } catch(err) {
            setTimeout(checkSelect2Initialised,2000)
        }
  }

   function initiateSelect2() {
    jQuery(".appstle_product-search-select").each(function(index, selectEl) {
        jQuery(selectEl).selectize({
            valueField: "id",
            labelField: "title",
            searchField: "title",
            options: [],
            create: false,
            render: {
              option: function (item, escape) {
                return (
                  "<div class='select2-result-repository clearfix' style='display: flex;'>" +
                    "<div class='select2-result-repository__avatar' style='max-width: 75px; max-height: 75px; padding: 10px'><img src='" + item?.imgSrc + "' /></div>" +
                    "<div style='flex-grow: 1; display: flex; justify-content: space-between;'>" +
                    "<div class='select2-result-repository__title' style='padding: 10px;'>" + item?.title + "</div>" +
                    "<div style='align-self: center; padding: 10px;' class='select2-result-repository__title' style='padding: 10px;'><b>" + formatPrice(item?.price) + " " + item?.currencyCode + "</b></div>" +
                    "</div>" +
                  "</div>"
                )
              }
            },
             load: function (query, callback) {
                if (!query.length) return callback();
                $.ajax({
                  url: "https://subscription-admin.appstle.com/api/data/external/v2/products",
                  type: "GET",
                  data: {
                    search: query, // search term
                    cursor: null,
                    next: false,
                    api_key: window?.appstle_api_key
                  },
                  error: function () {
                    callback();
                  },
                  success: function (res) {
                    var productsData = [...res.products];
                    getAllProducts(query, res.pageInfo.cursor, res.pageInfo.hasNextPage)
                    function getAllProducts(query, cursor, hasNext) {
                        if (hasNext) {
                          $.get("https://subscription-admin.appstle.com/api/data/external/v2/products" + "?search=" + query + "&cursor=" + cursor + "&next=" + String(hasNext) + "&api_key=" + window?.appstle_api_key)
                          .then(res => {
                            if (res.products.length) {
                              productsData = [...productsData, ...res.products];
                              getAllProducts(query, res.pageInfo.cursor, res.pageInfo.hasNextPage)
                            }
                          })
                        } else {
                          var listData = productsData.filter(function(line) {
                              return line?.status === 'ACTIVE'
                          });
                          var resultsData = []
                          function processVariant(listData) {
                            if (!listData.length) {
                               callback(resultsData);
                            }
                            const product = listData.shift();
                            var prdItem = {};
                            var productvariantResponse = null;
                            var productvariantUrl = location.origin + '/products/' + product.handle + '.js';
                                jQuery.get(productvariantUrl)
                                .then(productvariantResponse => {
                                    if(productvariantResponse) {
                                        let productvariants = JSON.parse(productvariantResponse);
                                        prdItem.imgSrc = 'https:' + productvariants?.featured_image;
                                        prdItem.currencyCode = productvariants?.currencyCode;
                                        prdItem.prdHandleName = productvariants?.handle;
                                        prdItem.title = productvariants?.title;
                                        prdItem.variants = [];
                                        productvariants?.variants?.forEach(function(variant, i) {
                                            var isVariantValid = true;
                                            if (($(selectEl).parents(".appstle_product_search_wrapper").attr("data-selling-plan-id").split("|"))[0] && ($(selectEl).parents(".appstle_product_search_wrapper").attr("data-product-addition-type") == "SUBSCRIBE")) {
                                                if (addProductFilterType === "PRODUCTS_FROM_ALL_PLANS") {
                                                    if (!(variant?.selling_plan_allocations?.length)) {
                                                    isVariantValid = false;
                                                    }
                                                } else if (addProductFilterType === "PRODUCTS_FROM_CURRENT_PLAN") {
                                                let hasSellingPlan = false;
                                                variant?.selling_plan_allocations?.forEach(item => {
                                                    if ((sellingPlanIds?.indexOf(item?.selling_plan_id) !== -1)) {
                                                    hasSellingPlan = true
                                                    }
                                                })
                                                if (!hasSellingPlan) {
                                                    isVariantValid = false;
                                                }
                                                }
                                            }

                                            if(isVariantValid) {
                                                var item = {};
                                                if (productvariants.variants.length === 1) {
                                                item.title = productvariants?.title;
                                                } else if (productvariants.variants.length > 1) {
                                                item.title = productvariants?.title + ' - ' + variant?.title;
                                                }
                                                item.price = variant?.price;
                                                item.id = variant?.id + "|" + productvariants?.handle;
                                                item.uniqueId = variant?.id;
                                                item.imgSrc = 'https:' + productvariants?.featured_image;
                                                item.currencyCode = product?.currencyCode;
                                                item.prdHandleName = productvariants?.handle;
                                                item.graphQlVariantId = 'gid://shopify/ProductVariant/' + variant?.id;

                                                if(variant?.available) {
                                                // resultsData.push(item);
                                                prdItem.variants.push(item);
                                                } else {
                                                if (includeOutOfStockProduct) {
                                                    // resultsData.push(item);
                                                    prdItem.variants.push(item);
                                                }
                                                }
                                            }
                                        })
                                        resultsData = [...resultsData, ...prdItem.variants];
                                        return processVariant(listData)
                                    }
                                })
                                .catch(err => {
                                    return processVariant(listData)
                                })
                        }
                        processVariant(listData)
                        }
                    }
                  },
                });
              },

        })

})
}

function contractDetailsFromContractId(contractId) {
    jQuery.ajax({
            type: "GET",
            url: 'https://subscription-admin.appstle.com/api/external/v2/subscription-contracts/contract-external/' + contractId + '?api_key=' + window?.appstle_api_key,
            dataType: "json",
            headers: {
                'Access-Control-Allow-Origin': '*',
            },
            success: function (result, status, xhr) {
                return result
               console.log("My Contrcat Data" + result);
            },
            error: function (xhr, status, error) {
                console.log("Result: " + status + " " + error + " " + xhr.status + " " + xhr.statusText)
            }
        });
}

function formatRepo (product) {

  var container = jQuery(
    "<div class='select2-result-repository clearfix' style='display: flex;'>" +
      "<div class='select2-result-repository__avatar' style='max-width: 75px; max-height: 75px; padding: 10px'><img src='" + "" + "' /></div>" +
      "<div style='flex-grow: 1; display: flex; justify-content: space-between;'>" +
      "<div class='select2-result-repository__title' style='padding: 10px;'>" + product?.title + "</div>" +
      "<div style='align-self: center; padding: 10px;' class='select2-result-repository__title' style='padding: 10px;'><b>" + product?.price + " " + product?.currencyCode + "</b></div>" +
      "</div>" +
    "</div>"
  );

  return container;
}

function formatRepoSelection (product) {
  return product.title;
}

  function addStyle() {
      var css = _RSConfig?.css;
      jQuery('<style>' + css?.customerPortalCss + '</style>').appendTo(jQuery('head'));
  }

function shopifyLoaded() {
 if (window.hasOwnProperty('Shopify')) {
    window.shopName = Shopify.shop;
    window.customerId = __st.cid;

        if (customerId == null) {
            customerId = urlParams.get('customerId');
        }
     subscriptionInit();
        addStyle();
 } else {
 setTimeout(shopifyLoaded, 2000)

 }
}

function toggleChangeAddressForm(contractId) {
    jQuery('.appstle_ShippingAcc_' + contractId).find('.editBtnAddress_' + contractId).toggle();
    jQuery('.appstle_ShippingAcc_' + contractId).find('.appstle_address_viewOnly').slideToggle();
    jQuery('.appstle_ShippingAcc_' + contractId).find('.appstle_address_editForm').slideToggle();
}

function updateShippingAddress(contractId) {
    jQuery('.appstle_ShippingAcc_' + contractId).find('.appstle_edit_shipping_update-button span').hide();
    jQuery('.appstle_ShippingAcc_' + contractId).find('.appstle_edit_shipping_update-button .appstle_loaderTiny').show();

    var formdata = jQuery('.appstle_ShippingAcc_' + contractId).find('form').serializeArray();
    var data = {};
    jQuery(formdata).each(function(index, obj){
        data[obj.name] = obj.value;
    });

    data['name'] = data['firstName'] + ' ' + data['lastName'];
    var settings = {
        "url": "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-shipping-address?contractId=" + contractId + "&api_key=" + window?.appstle_api_key,
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
  "data": JSON.stringify(data),
};

jQuery.ajax(settings).done(function (response) {
    jQuery(".appstle_myProduct").fadeOut(500, function () {
        jQuery(".appstle_preloader").fadeIn(250);
        subscriptionInit();
        });
    });
}

function pauseSubscription(contractId) {
    jQuery('.appstle_subscription_contract_content_wrapper_' + contractId).find('.appstle_pause_subscription_button .appstle_icon_wrapper').hide();
    jQuery('.appstle_subscription_contract_content_wrapper_' + contractId).find('.appstle_pause_subscription_button .appstle_loaderTiny').show();
    var settings = {
        "url": "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-status?contractId=" + contractId + "&status=PAUSED&api_key=" + window?.appstle_api_key,
        "method": "PUT",
        "timeout": 0,
    };

    jQuery.ajax(settings).done(function (response) {
        jQuery(".appstle_myProduct").fadeOut(500, function () {
        jQuery(".appstle_preloader").fadeIn(250);
        subscriptionInit();
        });
    });
}

function resumeSubscription(contractId) {
    jQuery('.appstle_subscription_contract_content_wrapper_' + contractId).find('.appstle_resume_subscription_button .appstle_icon_wrapper').hide();
    jQuery('.appstle_subscription_contract_content_wrapper_' + contractId).find('.appstle_resume_subscription_button .appstle_loaderTiny').show();
    var settings = {
        "url": "https://subscription-admin.appstle.com/api/external/v2/subscription-contracts-update-status?contractId=" + contractId + "&status=ACTIVE&api_key=" + window?.appstle_api_key,
        "method": "PUT",
        "timeout": 0,
    };

    jQuery.ajax(settings).done(function (response) {
        jQuery(".appstle_myProduct").fadeOut(500, function () {
        jQuery(".appstle_preloader").fadeIn(250);
        subscriptionInit();
        });
    });
}

function formatPrice(price) {

        var moneyFormat = RS.Config.moneyFormat;

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

        var formatString = window?.shopifyCurrencyFormat || window?.moneyFormat || window?.Currency?.money_format_no_currency || themeMoneyFormat || moneyFormat || shopifyMoneyFormat;

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
        var spanElement = document.createElement('span');
        spanElement.innerHTML = formatString.replace(placeholderRegex, value);
        return spanElement.textContent || spanElement.innerText;
      }

      function deleteOneOffProducts(variantId, contractId, billingID, appstle_api_key) {
           requestUrl = 'https://subscription-admin.appstle.com/api/external/v2/subscription-contract-one-offs-by-contractId-and-billing-attempt-id?api_key=' + window?.appstle_api_key + '&billingAttemptId=' + billingID + '&contractId=' + contractId + '&variantId=' + variantId;
           jQuery.ajax({
                url: requestUrl,
                type: 'DELETE',
                success: function(result) {
                     jQuery(".appstle_myProduct").fadeOut(500, function () {
                    jQuery(".appstle_preloader").fadeIn(250);
                    subscriptionInit();
                    });
                }
            });
      }


jQuery(document).ready(function () {
    shopifyLoaded();
});
// ]]></script>
