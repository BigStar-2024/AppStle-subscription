export const data = {
  emailSenderSettings: {
    displayName: 'Settings',

    automationType: {
      id: 'smsSettingType',
      displayName: 'Sms type ',
      dropdownValues: [
        { name: 'Subscription Created', value: 'SUBSCRIPTION_CREATED' },
        { name: 'Transaction failed', value: 'TRANSACTION_FAILED' },
        { name: 'Upcoming Order', value: 'UPCOMING_ORDER' },
        { name: 'Expiring Credit Card', value: 'EXPIRING_CREDIT_CARD' },
        // { name: 'Shipping Confirmation', value: 'SHIPPING_CONFIRMATION' },
        // { name: 'New Subscriber Confirmation', value: 'NEW_SUBSCRIBER_CONFIRMATION' },
        // { name: 'Customer Winback', value: 'CUSTOMER_WINBACK' },
        // { name: 'Product Upsell', value: 'PRODUCT_UPSELL' },
        // { name: 'Shipping Update', value: 'SHIPPING_UPDATE' },
        // { name: 'Order Delivered Feedback', value: 'ORDER_DELIVERED_FEEDBACK' },
        // { name: 'Cash on Delivery', value: 'CASH_ON_DELIVERY' },
        // { name: 'Order Paid', value: 'ORDER_PAID' },
        // { name: 'Order Cancelled', value: 'ORDER_CANCELLED' },
        // { name: 'Order Refund', value: 'ORDER_REFUND' },
        // { name: 'Customer Deleted', value: 'CUSTOMER_DELETED' },
        // { name: 'Product Created', value: 'PRODUCT_CREATED' },
        // { name: 'Product Deleted', value: 'PRODUCT_DELETED' },
      ],
      type: 'select',
      helpText: '<div class="automation_settings_help_text">Select the trigger for your text message.</div>'
    },

    sms: {
      type: 'smsContent'
    },

    // automationName: {
    //   id: 'automationName',
    //   displayName: 'Automation name',
    //   placeholder: 'SMS Automation name.',
    //   type: 'input',
    //   validation: 'required',
    //   helpText: '<div class="automation_settings_help_text">Give your SMS automation a relevant name.</div>'
    // },

    // recipient: {
    //   id: 'recipient',
    //   displayName: 'Recipient',
    //   dropdownValues: [
    //     { name: 'Customer', value: 'CUSTOMER' },
    //     { name: 'Admin', value: 'ADMIN' }
    //   ],
    //   type: 'select',
    //   helpText: '<div class="automation_settings_help_text">Select who receives the text messages.</div>'
    // },

    // delay: {
    //   id: 'delay',
    //   displayName: 'Delay',
    //   dropdownValues: [
    //     { name: 'Disabled', value: 'DISABLED' },
    //     { name: 'Enabled', value: 'ENABLED' }
    //   ],
    //   type: 'select',
    //   helpText:
    //     '<div class="automation_settings_help_text">Set the timing of your text messages. Use Minutes, Hours, and Days to set the sending delay of messages. Disabling the delay will trigger your messages instantly.</div>'
    // },

    // attachImageOrGif: {
    //   id: 'attachImageOrGif',
    //   displayName: 'Attach image or GIF link (U.S. recipients only)',
    //   type: 'input',
    //   helpText:
    //     '<div class="automation_settings_help_text">An MMS contains 1600 characters, including emojis and special characters. A single message (SMS) contains up to 160 characters, but if you use emojis or special characters, its length will go down to 66 characters.</div>'
    // },

    // smsContent: {
    //   id: 'smsContent',
    //   displayName: 'Text Message',
    //   type: 'textarea',
    //   validation: 'required',
    //   helpText: '<div class="automation_settings_help_text">Set the text of the message your customers will receive.</div>'
    // }
  }
};
