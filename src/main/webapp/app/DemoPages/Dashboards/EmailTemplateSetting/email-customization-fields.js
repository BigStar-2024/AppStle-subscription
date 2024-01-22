export const data = {
  emailSenderSettings: {
    displayName: 'Email Sender Settings',
    subject: {
      id: 'subject',
      displayName: 'Subject',
      placeholder: 'Please Enter email subject',
      type: 'input',
      validation: ''
    },

    fromEmail: {
      id: 'fromEmail',
      displayName: 'From Email',
      placeholder: 'Enter the email id from which you want to send',
      type: 'input',
      validation: ''
    },

    bccEmail: {
      id: 'bccEmail',
      displayName: 'Admin Notification / BCC Email',
      placeholder: 'Enter the email id which you want to add as an Admin Notification.',
      type: 'input',
      validation: ''
    },

    bccEmailFlag: {
      displayName: "send BCC Email Flag",
      id: 'sendBCCEmailFlag',
      type: 'toggle',
    },
    replyTo: {
      id: 'replyTo',
      displayName: 'Reply To Email',
      placeholder: 'Enter the email id Reply To which you want to add',
      type: 'input',
      validation: ''
    },

    logo: {
      id: 'logo',
      helpText: `<a target="_blank" href="https://intercom.help/appstle/en/articles/5088992-how-to-add-a-logo-to-your-email-template" style="font-size: 12px;">Learn how to get the logo URL</a>`,
      displayName: 'Logo',
      placeholder: 'Enter a link for your Logo',
      type: 'input',
      validation: '',

    },
    logoHeight: {
      id: 'logoHeight',
      displayName: 'Logo Height(px)',
      placeholder: 'Enter height for your Logo',
      type: 'input',
      validation: 'NUMBER'
    },
    logoWidth: {
      id: 'logoWidth',
      displayName: 'Logo Width(px)',
      placeholder: 'Enter width for your Logo',
      type: 'input',
      validation: 'NUMBER'
    },
    logoAlignment: {
      id: 'logoAlignment',
      displayName: 'Logo Alignment',
      placeholder: 'Select alignment of logo',
      type: 'select',
      options: [{label: "center",value: "center"}, {label: "left",value: "left"}, {label: "right",value: "right"}]
    },
    textImage: {
      id: 'textImageUrl',
      displayName: {
        'SUBSCRIPTION_CREATED': 'Thanks Image URL',
        'TRANSACTION_FAILED': 'Failed Image URL',
        'UPCOMING_ORDER': 'Upcoming Image URL',
        'EXPIRING_CREDIT_CARD': 'Expiring soon Image URL',
        'NEXT_ORDER_DATE_UPDATED': 'Next Order Date Update Image URL',
        'ORDER_FREQUENCY_UPDATED': 'Order Frequency Update Image URL',
        'ORDER_SKIPPED': 'Order Skip Image URL',
        'SECURITY_CHALLENGE': 'Security Challenge Image URL',
        'SUBSCRIPTION_MANAGEMENT_LINK': 'Subscription Management Link Image URL',
        'SUBSCRIPTION_PRODUCT_ADDED': 'Subscription Product Add Image URL',
        'SUBSCRIPTION_PRODUCT_REMOVED': 'Subscription Product Remove Image URL',
        'SUBSCRIPTION_RESUMED': 'Subscription Resume Image URL',
        'SUBSCRIPTION_CANCELLED': 'Subscription Cancel Image URL',
        'SUBSCRIPTION_PAUSED': 'Subscription Pause Image URL',
        'SHIPPING_ADDRESS_UPDATED': 'Shipping Address Update Image URL',
      },
      placeholder: 'Enter a link for your text image',
      type: 'input',
      validation: '',
      helpText: ``
    },
    thanksImageHeight: {
      id: 'thanksImageHeight',
      displayName: 'Thanks Image Height(px)',
      placeholder: 'Enter height for your Thanks Image',
      type: 'input',
      validation: 'NUMBER'
    },
    thanksImageWidth: {
      id: 'thanksImageWidth',
      displayName: 'Thanks Image Width(px)',
      placeholder: 'Enter width for your Thanks Image',
      type: 'input',
      validation: 'NUMBER'
    },
    thanksImageAlignment: {
      id: 'thanksImageAlignment',
      displayName: 'Thanks Image Alignment',
      placeholder: 'Select alignment of Thanks Image',
      type: 'select',
      options: [{label: "Center",value: "center"}, {label: "Left",value: "left"}, {label: "Right",value: "right"}]
    },
    upcomingOrderEmailBuffer: {
      id: 'upcomingOrderEmailBuffer',
      displayName: {
        'EXPIRING_CREDIT_CARD': '',
        'SUBSCRIPTION_CREATED': '',
        'TRANSACTION_FAILED': '',
        'UPCOMING_ORDER': 'Upcoming Order Email Buffer',
        'NEXT_ORDER_DATE_UPDATED': 'Next Order Date Updated Email Buffer',
        'ORDER_FREQUENCY_UPDATED': 'Order Frequency Updated Email Buffer',
        'ORDER_SKIPPED': '',
        'SECURITY_CHALLENGE': '',
        'SUBSCRIPTION_MANAGEMENT_LINK': '',
        'SUBSCRIPTION_PRODUCT_ADDED': 'Subscription Product Added Email Buffer',
        'SUBSCRIPTION_PRODUCT_REMOVED': 'Subscription Product Removed Email Buffer',
        'SUBSCRIPTION_RESUMED': 'Subscription Resumed Email Buffer',
        'SUBSCRIPTION_CANCELLED': 'Subscription Cancelled Email Buffer',
        'SUBSCRIPTION_PAUSED': 'Subscription Paused Email Buffer',
        'SHIPPING_ADDRESS_UPDATED': 'Shipping Address Updated Email Buffer',
      },
      placeholder: 'Upcoming Order Email Buffer',
      // type: 'select',
      validation: '',
      helpText: ``,
      dropdownValues: [...Array(10).keys()]
    },

  },
  // templateBackground: {
  //   displayName: 'Template Background',
  //   templateBackgroundColor: {
  //     id: 'templateBackgroundColor',
  //     displayName: 'Background Color',
  //     placeholder: 'Choose a background color for you email template.',
  //     type: 'color',
  //     validation: ''
  //   },

  // },
  headingSection: {
    displayName: 'Heading',
    heading: {
      id: 'heading',
      displayName: 'Text',
      placeholder: 'Enter text for your heading',
      type: 'input',
      validation: ''
    },

    headingTextColor: {
      id: 'headingTextColor',
      displayName: 'Text color',
      placeholder: 'Click here to choose a color for you heading',
      type: 'color',
      validation: ''
    },

    headingImageUrl: {
      id: 'headingImageUrl',
      displayName: 'Heading Image URL',
      placeholder: 'Enter a link for your heading image',
      type: 'input',
      validation: '',
      helpText: ``
    }
  },
  contentSection: {
    displayName: 'Content',
    contentTextColor: {
      id: 'contentTextColor',
      displayName: 'Text Color',
      placeholder: 'Click here to choose a color.',
      type: 'color',

      validation: ''
    },

    content: {
      id: 'content',
      displayName: 'Text',
      placeholder: 'Enter text for your email content.',
      type: 'textarea',
      validation: '',
      hint: 'You can use {{subscriptionContractId}}, {{customer.email}}, {{customer.first_name}}, {{customer.last_name}}, {{customer.display_name}}, {{customer.token}}, {{shop.name}}, {{minCycles}}, {{maxCycles}}, {{orderCreatedDate}} variable in mail content'
    }
  },
  shippingAddressSection: {
    displayName: 'Shipping Address',
    shippingAddress: {
      id: 'shippingAddress',
      displayName: 'Text',
      placeholder: 'Format shipping address.',
      type: 'textarea',
      validation: '',
      hint: 'You can use {{shipping_type}}, {{shipping_first_name}}, {{shipping_last_name}}, {{shipping_address1}}, {{shipping_city}}, {{shipping_province_code}}, {{shipping_zip}}, {{shipping_province}}, {{shipping_company}}  variable to format shipping address'
    }
  },
  billingAddressSection: {
    displayName: 'Billing Address',
    billingAddress: {
      id: 'billingAddress',
      displayName: 'Text',
      placeholder: 'Format billing address.',
      type: 'textarea',
      validation: '',
      hint: 'You can use {{billing_full_name}}, {{billing_address1}}, {{billing_city}}, {{billing_province_code}}, {{billing_zip}}, {{billing_province}}  variable to format billing address'
    }
  },
  manageSubscription: {
    displayName: 'Manage Suscription',
    manageSubscriptionButtonText: {
      id: 'manageSubscriptionButtonText',
      displayName: 'Button Text',
      placeholder: 'Customize button text here',
      type: 'input',
      validation: ''
    },
    manageSubscriptionButtonUrl: {
      id: 'manageSubscriptionButtonUrl',
      displayName: 'Button URL',
      placeholder: 'By default, it will be customer magic link',
      type: 'input',
      validation: ''
    },
    manageSubscriptionButtonTextColor: {
      id: 'manageSubscriptionButtonTextColor',
      displayName: 'Text Color',
      placeholder: 'Click here to choose a color.',
      type: 'color',
      validation: ''
    },
    manageSubscriptionButtonColor: {
      id: 'manageSubscriptionButtonColor',
      displayName: 'Button Background Color',
      placeholder: 'Click here to choose a color.',
      type: 'color',
      validation: ''
    },
  },
  placeHolderTexts : {
    displayName: 'Placeholder Texts',
    shippingAddressText: {
      id: 'shippingAddressText',
      displayName: 'Shipping Address Text (Localised)',
      placeholder: 'Please enter localised Shipping Address text.',
      type: 'input',
      validation: ''
    },
    billingAddressText: {
      id: 'billingAddressText',
      displayName: 'Billing Address Text (Localised)',
      placeholder: 'Please enter localised Billing Address text.',
      type: 'input',
      validation: ''
    },
    nextOrderdateText: {
      id: 'nextOrderdateText',
      displayName: 'Next Order Date Text (Localised)',
      placeholder: 'Please enter localised Next Order Date text.',
      type: 'input',
      validation: ''
    },
    paymentMethodText: {
      id: 'paymentMethodText',
      displayName: 'Payment Method Text (Localised)',
      placeholder: 'Please enter localised Payment Method text.',
      type: 'input',
      validation: ''
    },
    endingInText: {
      id: 'endingInText',
      displayName: 'Ending In  Text (Localised)',
      placeholder: 'Please enter localised Ending In text.',
      type: 'input',
      validation: ''
    },
    quantityText: {
      id: 'quantityText',
      displayName: 'Quantity Text (Localised)',
      placeholder: 'Please enter localised Quantity text.',
      type: 'input',
      validation: ''
    },
    sellingPlanNameText: {
      id: 'sellingPlanNameText',
      displayName: 'Plan Name Text (Localised)',
      placeholder: 'Please enter localised Plan Name text.',
      type: 'input',
      validation: ''
    },
    variantSkuText: {
      id: 'variantSkuText',
      displayName: 'SKU Text (Localised)',
      placeholder: 'Please enter localised Sku text.',
      type: 'input',
      validation: ''
    },

  },
  footerSection: {
    displayName: 'Footer',
    footerTextColor: {
      id: 'footerTextColor',
      displayName: 'Text Color',
      placeholder: 'Click here to choose a color.',
      type: 'color',
      validation: ''
    },

    footerText: {
      id: 'footerText',
      displayName: 'Text',
      placeholder: 'Please enter Footer text',
      type: 'textarea',
      validation: ''
    }
  }/*,
  customEmailHtml: {
    displayName: "Custom Email HTML",
    helpText: `<a target="_blank" href="https://intercom.help/appstle/en/articles/5129455-how-to-customize-emails" style="font-size: 12px; color: #545cd8">Learn how to  customize emails</a>`,
    html: {
      id: 'html',
      displayName: 'HTML',
      placeholder: 'Please enter Email HTML',
      type: 'textarea',
      validation: '',
      // helpText: `<a target="_blank" href="https://intercom.help/appstle/en/articles/5129455-how-to-customize-emails" style="font-size: 12px; color: #495057">Learn how to  customize emails</a>`


    }
  }*/
};
