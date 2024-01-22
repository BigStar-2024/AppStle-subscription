package com.et.constant;

public class EmailTemplateConstants {

    //Subscription Email common constants
    public static final String LOGO = "https://ik.imagekit.io/mdclzmx6brh/subscription-logo_2VUOWqVgN.png";
    public static final String MANAGE_SUBSCRIPTION_BUTTON_COLOR = "#20AE96";
    public static final String MANAGE_SUBSCRIPTION_BUTTON_TEXT = "Manage Subscription";
    public static final String MANAGE_SUBSCRIPTION_BUTTON_TEXT_COLOR ="#ffffff";
    public static final String SHIPPING_ADDRESS_TEXT = "Shipping Address";
    public static final String BILLING_ADDRESS_TEXT = "Billing Address";
    public static final String NEXT_ORDER_DATE_TEXT = "Next Order Date";
    public static final String PAYMENT_METHOD_TEXT = "Payment Method";
    public static final String ENDING_IN_TEXT = "Ending in";
    public static final String QUANTITY_TEXT = "Quantity";
    public static final String LOGO_HEIGHT = "90";
    public static final String LOGO_WIDTH = "200";
    public static final String THANKS_IMAGE_HEIGHT = "30";
    public static final String THANKS_IMAGE_WIDTH = "150";
    public static final String LOGO_ALIGNMENT = "center";
    public static final String THANKS_IMAGE_ALIGNMENT = "center";
    public static final String SHIPPING_ADDRESS_FORMAT_TEXT = "{% case shipping_type %}\n" +
        "{% when \"SHIPPING\" %}\n" +
        "Standard Shipping\n" +
        "{{ shipping_first_name }} {{ shipping_last_name }}\n" +
        "{{ shipping_address1 }}\n" +
        "{{shipping_city}},{{shipping_province_code}} - {{shipping_zip}}\n" +
        "{% when \"LOCAL\" %}\n" +
        "Local Delivery\n" +
        "{{ shipping_first_name }} {{ shipping_last_name }}\n" +
        "{{ shipping_address1 }}\n" +
        "{{shipping_city}},{{shipping_province_code}} - {{shipping_zip}}\n" +
        "{% when \"PICK_UP\" %}\n" +
        "Pickup Address\n" +
        "{{ shipping_first_name }}\n" +
        "{{ shipping_address1 }}\n" +
        "{{shipping_city}},{{shipping_province_code}} - {{shipping_zip}}\n" +
        "{% endcase %}";
    public static final String BILLING_ADDRESS_FORMAT_TEXT = "{{ billing_full_name }}\n" +
        "{{ billing_address1 }}\n" +
        "{{billing_city}},{{billing_province_code}} - {{billing_zip}}";


    //Subscriptions created Email constants
    public static final String SUBSCRIPTION_CREATED_SUBJECT = "Your new subscription!";
    public static final String SUBSCRIPTION_CREATED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_CREATED_HEADING = "Welcome Aboard";
    public static final String SUBSCRIPTION_CREATED_HEADING_TEXT_COLOR = "#495661";
    public static final String THANKS_IMAGE_URL = "https://i.ibb.co/CzChcZm/thanks.png";

    public static final String SUBSCRIPTION_CREATED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_CREATED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_CREATED_CONTENT_TEXT = "Hello {{ customer.display_name }},\n" +
        "\n" +
        "Thank you for your subscription purchase!\n" +
        "\n" +
        "Please view the details of your subscription below.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_CREATED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_CREATED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_CREATED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    //Transaction failed Email constants
    public static final String TRANSACTION_FAILED_SUBJECT = "Transaction failed";
    public static final String TRANSACTION_FAILED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String TRANSACTION_FAILED_HEADING = "Transaction Failed";
    public static final String TRANSACTION_FAILED_HEADING_TEXT_COLOR = "#495661";
    public static final String OHNO_IMAGE_URL = "https://i.ibb.co/5jvdCrb/oh-no.png";

    public static final String TRANSACTION_FAILED_CONTENT_TEXT_COLOR = "#495661";
    public static final String TRANSACTION_FAILED_CONTENT_LINK_COLOR = "#000000";
    public static final String TRANSACTION_FAILED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        "\n" +
        "We are writing to let you know that the auto renewal on your subscribed product with our store did not go through. Please visit the Manage Subscription section (link below), to update your payment details.\n" +
        "\n" +
        "Sometimes issuing banks decline the charge if the name or account details entered do not match the bank's records.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String TRANSACTION_FAILED_FOOTER_TEXT_COLOR = "#495661";
    public static final String TRANSACTION_FAILED_FOOTER_LINK_COLOR = "#777777";
    public static final String TRANSACTION_FAILED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";
    public static final String TRANSACTION_FAILED_HEADING_IMAGE_URL = "https://i.ibb.co/V2tGxzh/close.png";

    //Transaction Success Email constants
    public static final String TRANSACTION_SUCCESS_SUBJECT = "Your order with us!";
    public static final String TRANSACTION_SUCCESS_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String TRANSACTION_SUCCESS_HEADING = "Subscription Order";
    public static final String TRANSACTION_SUCCESS_HEADING_TEXT_COLOR = "#495661";
    public static final String TRANSACTION_SUCCESS_CONTENT_TEXT_COLOR = "#495661";
    public static final String TRANSACTION_SUCCESS_CONTENT_LINK_COLOR = "#000000";
    public static final String TRANSACTION_SUCCESS_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        "\n" +
        "We are writing to let you know that your subscription order has been placed. \n" +
        " Please review your order using the below link.\n" +
        " \n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String TRANSACTION_SUCCESS_FOOTER_TEXT_COLOR = "#495661";
    public static final String TRANSACTION_SUCCESS_FOOTER_LINK_COLOR = "#777777";
    public static final String TRANSACTION_SUCCESS_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";
    public static final String TRANSACTION_SUCCESS_HEADING_IMAGE_URL = null;



    //Security Challenge Email constants
    public static final String SECURITY_CHALLENGE_SUBJECT = "Security Challenge";
    public static final String SECURITY_CHALLENGE_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SECURITY_CHALLENGE_HEADING = "Security Challenge";
    public static final String SECURITY_CHALLENGE_HEADING_TEXT_COLOR = "#495661";
    public static final String SECURITY_CHALLENGE_OHNO_IMAGE_URL = "https://i.ibb.co/5jvdCrb/oh-no.png";

    public static final String SECURITY_CHALLENGE_CONTENT_TEXT_COLOR = "#495661";
    public static final String SECURITY_CHALLENGE_CONTENT_LINK_COLOR = "#000000";
    public static final String SECURITY_CHALLENGE_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        "\n" +
        "We are writing to let you know that we have received a security challenge from your credit card processor. We will process your order after you verify the following challenge URL.\n" +
        "\n" +
        "<a href=\"{{challengeUrl}}\">Security Challenge</a>\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SECURITY_CHALLENGE_FOOTER_TEXT_COLOR = "#495661";
    public static final String SECURITY_CHALLENGE_FOOTER_LINK_COLOR = "#777777";
    public static final String SECURITY_CHALLENGE_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";
    public static final String SECURITY_CHALLENGE_HEADING_IMAGE_URL = "https://i.ibb.co/V2tGxzh/close.png";


    //Expiring Credits card Email constants
    public static final String EXPIRING_CREDIT_CARD_SUBJECT = "Your card on file is expiring soon";
    public static final String EXPIRING_CREDIT_CARD_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String EXPIRING_CREDIT_CARD_HEADING = "Expiring Credit Card";
    public static final String EXPIRING_CREDIT_CARD_HEADING_TEXT_COLOR = "#495661";
    public static final String EXPIRING_IMAGE_URL = "https://i.ibb.co/8mz8Zy2/Expiring-soon.png";
    public static final String EXPIRING_CREDIT_CARD_HEADING_IMAGE_URL = "https://i.ibb.co/zS1jr0J/exp-cred-card.png";

    public static final String EXPIRING_CREDIT_CARD_CONTENT_TEXT_COLOR = "#495661";
    public static final String EXPIRING_CREDIT_CARD_CONTENT_LINK_COLOR = "#000000";
    public static final String EXPIRING_CREDIT_CARD_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your card on file with us, ending in {{ lastFourDigits }}, is set to expire before your next upcoming delivery. Please visit the Manage Subscription section (link below), to update your payment details.\n" +
        "\n" +
        "Please refer to the details of your recurring order below which will be affected if your payment information is not updated. Feel free to email us if you have any questions or concerns.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String EXPIRING_CREDIT_CARD_FOOTER_TEXT_COLOR = "#495661";
    public static final String EXPIRING_CREDIT_CARD_FOOTER_LINK_COLOR = "#777777";
    public static final String EXPIRING_CREDIT_CARD_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Shipping address updated constants
    public static final String SHIPPING_ADDRESS_UPDATED_SUBJECT = "Your shipping address has been updated!";
    public static final String SHIPPING_ADDRESS_UPDATED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SHIPPING_ADDRESS_UPDATED_HEADING = "Shipping address updated";
    public static final String SHIPPING_ADDRESS_UPDATED_HEADING_TEXT_COLOR = "#495661";
    public static final String SHIPPING_ADDRESS_UPDATED_URL = "https://i.ibb.co/WW5H6fZ/upcoming.png"; //TODO- image url
    public static final Integer SHIPPING_ADDRESS_UPDATED_EMAIL_BUFFER = 1;

    public static final String SHIPPING_ADDRESS_UPDATED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SHIPPING_ADDRESS_UPDATED_CONTENT_LINK_COLOR = "#000000";
    public static final String SHIPPING_ADDRESS_UPDATED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "The shipping address tied to your subscription has been updated.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SHIPPING_ADDRESS_UPDATED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SHIPPING_ADDRESS_UPDATED_FOOTER_LINK_COLOR = "#777777";
    public static final String SHIPPING_ADDRESS_UPDATED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    // Order frequency updated constants
    public static final String ORDER_FREQUENCY_UPDATED_SUBJECT = "Your order frequency has been updated!";
    public static final String ORDER_FREQUENCY_UPDATED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String ORDER_FREQUENCY_UPDATED_HEADING = "Order frequency updated";
    public static final String ORDER_FREQUENCY_UPDATED_HEADING_TEXT_COLOR = "#495661";
    public static final String ORDER_FREQUENCY_UPDATED_URL = "https://i.ibb.co/WW5H6fZ/upcoming.png"; //TODO- image url
    public static final Integer ORDER_FREQUENCY_UPDATED_EMAIL_BUFFER = 1;

    public static final String ORDER_FREQUENCY_UPDATED_CONTENT_TEXT_COLOR = "#495661";
    public static final String ORDER_FREQUENCY_UPDATED_CONTENT_LINK_COLOR = "#000000";
    public static final String ORDER_FREQUENCY_UPDATED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "The order frequency for your subscription has been updated.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String ORDER_FREQUENCY_UPDATED_FOOTER_TEXT_COLOR = "#495661";
    public static final String ORDER_FREQUENCY_UPDATED_FOOTER_LINK_COLOR = "#777777";
    public static final String ORDER_FREQUENCY_UPDATED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Next Order date updated constants
    public static final String NEXT_ORDER_DATE_UPDATED_SUBJECT = "Your next order date has been updated!";
    public static final String NEXT_ORDER_DATE_UPDATED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String NEXT_ORDER_DATE_UPDATED_HEADING = "Next order date updated";
    public static final String NEXT_ORDER_DATE_UPDATED_HEADING_TEXT_COLOR = "#495661";
    public static final String NEXT_ORDER_DATE_UPDATED_URL = "https://i.ibb.co/WW5H6fZ/upcoming.png"; //TODO- image url
    public static final Integer NEXT_ORDER_DATE_UPDATED_EMAIL_BUFFER = 1;

    public static final String NEXT_ORDER_DATE_UPDATED_CONTENT_TEXT_COLOR = "#495661";
    public static final String NEXT_ORDER_DATE_UPDATED_CONTENT_LINK_COLOR = "#000000";
    public static final String NEXT_ORDER_DATE_UPDATED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "The next order date for your subscription has been updated.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String NEXT_ORDER_DATE_UPDATED_FOOTER_TEXT_COLOR = "#495661";
    public static final String NEXT_ORDER_DATE_UPDATED_FOOTER_LINK_COLOR = "#777777";
    public static final String NEXT_ORDER_DATE_UPDATED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Subscription paused constants
    public static final String SUBSCRIPTION_PAUSED_SUBJECT = "Your subscription has been paused!";
    public static final String SUBSCRIPTION_PAUSED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_PAUSED_HEADING = "Subscription paused";
    public static final String SUBSCRIPTION_PAUSED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PAUSED_URL = null;
    public static final Integer SUBSCRIPTION_PAUSED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_PAUSED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PAUSED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_PAUSED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your subscription has been paused.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_PAUSED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PAUSED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_PAUSED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Subscription canceled constants
    public static final String SUBSCRIPTION_CANCELED_SUBJECT = "Your subscription has been cancelled!";
    public static final String SUBSCRIPTION_CANCELED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_CANCELED_HEADING = "Subscription cancelled";
    public static final String SUBSCRIPTION_CANCELED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_CANCELED_URL = null;
    public static final Integer SUBSCRIPTION_CANCELED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_CANCELED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_CANCELED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_CANCELED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your subscription has been cancelled.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_CANCELED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_CANCELED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_CANCELED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    // Subscription resumed constants
    public static final String SUBSCRIPTION_RESUMED_SUBJECT = "Your subscription has been resumed!";
    public static final String SUBSCRIPTION_RESUMED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_RESUMED_HEADING = "Subscription resumed";
    public static final String SUBSCRIPTION_RESUMED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_RESUMED_URL = null;
    public static final Integer SUBSCRIPTION_RESUMED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_RESUMED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_RESUMED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_RESUMED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your subscription has been resumed. \n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_RESUMED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_RESUMED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_RESUMED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Subscription product added constants
    public static final String SUBSCRIPTION_PRODUCT_ADDED_SUBJECT = "Product added to your subscription!";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_HEADING = "Subscription product added";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_URL = null;
    public static final Integer SUBSCRIPTION_PRODUCT_ADDED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_PRODUCT_ADDED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "A product(s) has been added to your subscription.\n" +
        " \n" +
        "{% for productName in productsAdded %}   * {{productName}} \n" +
        "{% endfor %}" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_PRODUCT_ADDED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_PRODUCT_ADDED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Subscription product removed constants
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_SUBJECT = "Product removed from your subscription!";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_HEADING = "Subscription product removed";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_URL = null;
    public static final Integer SUBSCRIPTION_PRODUCT_REMOVED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "A product(s) has been removed from your subscription.\n" +
        " \n" +
        " {% for productName in productsRemoved %}   * {{productName}} " +
        "{% endfor %}" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_PRODUCT_REMOVED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    // Subscription product replaced constants
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_SUBJECT = "Product replaced in your subscription!";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_HEADING = "Subscription product replaced";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_HEADING_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_URL = null;
    public static final Integer SUBSCRIPTION_PRODUCT_REPLACED_EMAIL_BUFFER = 1;

    public static final String SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "A product(s) has been replaced in your subscription.\n" +
        " \n" +
        "Product(s) Added: \n" +
        " {% for productName in productsAdded %}   * {{productName}} \n" +
        "{% endfor %}" +
        " \n" +
        " Product(s) Removed: \n" +
        " {% for productName in productsRemoved %}" +
        "   * {{productName}} \n" +
        "{% endfor %}" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_PRODUCT_REPLACED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    //Upcoming Order Email constants
    public static final String UPCOMING_ORDER_SUBJECT = "Your upcoming order with us!";
    public static final String UPCOMING_ORDER_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String UPCOMING_ORDER_HEADING = "Upcoming Subscription";
    public static final String UPCOMING_ORDER_HEADING_TEXT_COLOR = "#495661";
    public static final String UPCOMING_IMAGE_URL = "https://i.ibb.co/WW5H6fZ/upcoming.png";
    public static final Integer UPCOMING_ORDER_EMAIL_BUFFER = 1;

    public static final String UPCOMING_ORDER_CONTENT_TEXT_COLOR = "#495661";
    public static final String UPCOMING_ORDER_CONTENT_LINK_COLOR = "#000000";
    public static final String UPCOMING_ORDER_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your next subscription order will be automatically placed on {{ nextOrderDate }}.\n" +
        " \n" +
        "Please review your upcoming order using the link below.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String UPCOMING_ORDER_FOOTER_TEXT_COLOR = "#495661";
    public static final String UPCOMING_ORDER_FOOTER_LINK_COLOR = "#777777";
    public static final String UPCOMING_ORDER_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    //Subscription Management Link constants
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_SUBJECT = "Link to manage your Subscriptions";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_HEADING = "Subscription Management Link";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_HEADING_TEXT_COLOR = "#495661";

    public static final String SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_LINK_COLOR = "#000000";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        "\n" +
        "Please use the button below to manage your subscriptions.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_TEXT_COLOR = "#495661";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_LINK_COLOR = "#777777";
    public static final String SUBSCRIPTION_MANAGEMENT_LINK_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    //Order skipped constants
    public static final String ORDER_SKIPPED_SUBJECT = "Order skipped for your subscription!";
    public static final String ORDER_SKIPPED_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String ORDER_SKIPPED_HEADING = "Order Skipped";
    public static final String ORDER_SKIPPED_HEADING_TEXT_COLOR = "#495661";

    public static final String ORDER_SKIPPED_CONTENT_TEXT_COLOR = "#495661";
    public static final String ORDER_SKIPPED_CONTENT_LINK_COLOR = "#000000";
    public static final String ORDER_SKIPPED_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "An order has been skipped for your subscription.\n" +
        " \n" +
        "Please confirm the update in your customer portal.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String ORDER_SKIPPED_FOOTER_TEXT_COLOR = "#495661";
    public static final String ORDER_SKIPPED_FOOTER_LINK_COLOR = "#777777";
    public static final String ORDER_SKIPPED_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    //BULK ALL SUBSCRIBERS constants
    public static final String BULK_ALL_SUBSCRIBERS_SUBJECT = "Upto 60% off Winter clearance sale on your way!";
    public static final String BULK_ALL_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String BULK_ALL_SUBSCRIBERS_HEADING = "All Subscriptions";
    public static final String BULK_ALL_SUBSCRIBERS_HEADING_TEXT_COLOR = "#495661";
    public static final String BULK_ALL_SUBSCRIBERS_URL = null;
    public static final Integer BULK_ALL_SUBSCRIBERS_EMAIL_BUFFER = 1;
    public static final String BULK_ALL_SUBSCRIBERS_CONTENT_TEXT_COLOR = "#495661";
    public static final String BULK_ALL_SUBSCRIBERS_CONTENT_LINK_COLOR = "#000000";

    public static final String BULK_ALL_SUBSCRIBERS_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "We’re super happy to announce our winter clearance sale starting on the coming 10th." +
        " Get up to 60% off on our exclusive collection during the sale. \n" +
        " \n" +
        "Hurry and fill up your carts now!\n" +
        " \n" +
        "Please don't hesitate to reach out if you have any questions or concerns!\n" +
        " \n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String BULK_ALL_SUBSCRIBERS_FOOTER_TEXT_COLOR = "#495661";
    public static final String BULK_ALL_SUBSCRIBERS_FOOTER_LINK_COLOR = "#777777";
    public static final String BULK_ALL_SUBSCRIBERS_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";

    //BULK ACTIVE SUBSCRIBERS constants
    public static final String BULK_ACTIVE_SUBSCRIBERS_SUBJECT = "Upto 60% off Winter clearance sale on your way!";
    public static final String BULK_ACTIVE_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String BULK_ACTIVE_SUBSCRIBERS_HEADING = "Active Subscriptions";
    public static final String BULK_ACTIVE_SUBSCRIBERS_HEADING_TEXT_COLOR = "#495661";
    public static final String BULK_ACTIVE_SUBSCRIBERS_URL = null;
    public static final Integer BULK_ACTIVE_SUBSCRIBERS_EMAIL_BUFFER = 1;
    public static final String BULK_ACTIVE_SUBSCRIBERS_CONTENT_TEXT_COLOR = "#495661";
    public static final String BULK_ACTIVE_SUBSCRIBERS_CONTENT_LINK_COLOR = "#000000";

    public static final String BULK_ACTIVE_SUBSCRIBERS_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "We’re super happy to announce our winter clearance sale starting on the coming 10th." +
        " Get up to 60% off on our exclusive collection during the sale. \n" +
        " \n" +
        "Hurry and fill up your carts now!\n" +
        " \n" +
        "Please don't hesitate to reach out if you have any questions or concerns!\n" +
        " \n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String BULK_ACTIVE_SUBSCRIBERS_FOOTER_TEXT_COLOR = "#495661";
    public static final String BULK_ACTIVE_SUBSCRIBERS_FOOTER_LINK_COLOR = "#777777";
    public static final String BULK_ACTIVE_SUBSCRIBERS_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    //BULK PAUSED SUBSCRIBERS constants
    public static final String BULK_PAUSED_SUBSCRIBERS_SUBJECT = "Your subscription has been paused for a while now!";
    public static final String BULK_PAUSED_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String BULK_PAUSED_SUBSCRIBERS_HEADING = "Paused Subscriptions";
    public static final String BULK_PAUSED_SUBSCRIBERS_HEADING_TEXT_COLOR = "#495661";
    public static final String BULK_PAUSED_SUBSCRIBERS_URL = null;
    public static final Integer BULK_PAUSED_SUBSCRIBERS_EMAIL_BUFFER = 1;
    public static final String BULK_PAUSED_SUBSCRIBERS_CONTENT_TEXT_COLOR = "#495661";
    public static final String BULK_PAUSED_SUBSCRIBERS_CONTENT_LINK_COLOR = "#000000";

    public static final String BULK_PAUSED_SUBSCRIBERS_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "Your subscription has been paused for a while now." +
        " Visit your customer’s account to activate your subscription and avail the latest discounts we’re offering on subscription purchases!\n" +
        " \n" +
        "Please don't hesitate to reach out if you have any questions or concerns!\n" +
        " \n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String BULK_PAUSED_SUBSCRIBERS_FOOTER_TEXT_COLOR = "#495661";
    public static final String BULK_PAUSED_SUBSCRIBERS_FOOTER_LINK_COLOR = "#777777";
    public static final String BULK_PAUSED_SUBSCRIBERS_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    //BULK CANCELLED SUBSCRIBERS constants
    public static final String BULK_CANCELLED_SUBSCRIBERS_SUBJECT = "Oh, you ain't with us anymore? We're missing you already!";
    public static final String BULK_CANCELLED_SUBSCRIBERS_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String BULK_CANCELLED_SUBSCRIBERS_HEADING = "Cancelled Subscriptions";
    public static final String BULK_CANCELLED_SUBSCRIBERS_HEADING_TEXT_COLOR = "#495661";
    public static final String BULK_CANCELLED_SUBSCRIBERS_URL = null;
    public static final Integer BULK_CANCELLED_SUBSCRIBERS_EMAIL_BUFFER = 1;
    public static final String BULK_CANCELLED_SUBSCRIBERS_CONTENT_TEXT_COLOR = "#495661";
    public static final String BULK_CANCELLED_SUBSCRIBERS_CONTENT_LINK_COLOR = "#000000";

    public static final String BULK_CANCELLED_SUBSCRIBERS_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "We miss you and want you back, want to get a 50% discount on any of our subscription products?" +
        " Hurry up and activate your subscription now to avail of 50% discounts on your new purchases!\n" +
        " \n" +
        "Also, feel free to let us know if there’s anything we can do to help!\n" +
        " \n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String BULK_CANCELLED_SUBSCRIBERS_FOOTER_TEXT_COLOR = "#495661";
    public static final String BULK_CANCELLED_SUBSCRIBERS_FOOTER_LINK_COLOR = "#777777";
    public static final String BULK_CANCELLED_SUBSCRIBERS_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";


    //OUT OF STOCK constants
    public static final String OUT_OF_STOCK_SUBJECT = "Product is out of stock";
    public static final String OUT_OF_STOCK_TEMPLATE_BACKGROUND_COLOR = "#fff";
    public static final String OUT_OF_STOCK_HEADING = "Product out of stock";
    public static final String OUT_OF_STOCK_HEADING_TEXT_COLOR = "#495661";
    public static final String OUT_OF_STOCK_URL = null;
    public static final Integer OUT_OF_STOCK_EMAIL_BUFFER = 1;
    public static final String OUT_OF_STOCK_CONTENT_TEXT_COLOR = "#495661";
    public static final String OUT_OF_STOCK_CONTENT_LINK_COLOR = "#000000";

    public static final String OUT_OF_STOCK_CONTENT_TEXT = "Hi {{ customer.display_name }},\n" +
        " \n" +
        "The following product(s) in your subscription are out of stock: \n" +
        " \n" +
        " {% for productName in outOfStockProducts %}" +
        "   * {{productName}} \n" +
        "{% endfor %}" +
        " \n" +
        "Orders containing these products have been skipped in your subscription.\n" +
        " \n" +
        "Please navigate to your customer portal to confirm the update.\n" +
        "\n" +
        "Thanks!\n" +
        "{{ shop.name }}";

    public static final String OUT_OF_STOCK_FOOTER_TEXT_COLOR = "#495661";
    public static final String OUT_OF_STOCK_FOOTER_LINK_COLOR = "#777777";
    public static final String OUT_OF_STOCK_FOOTER_TEXT = "If you have any questions or concerns, please reply to this email and we will get back to you as soon as we can.";
    public static final String SELLING_PLAN_NAME_TEXT = "Selling Plan";
    public static final String VARIANT_SKU_TEXT = "SKU";
}
