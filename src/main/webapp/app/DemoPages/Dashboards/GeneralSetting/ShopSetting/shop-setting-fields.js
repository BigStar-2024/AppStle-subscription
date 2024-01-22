export const data = {
  orderTaggingSettings: {
    displayName: 'Order Tagging Settings',
    helpText: 'Min 1 - Max 40 characters can be added in the below input fields. You can also add comma separated tags and insert supported dynamic variables. For best practices <a  target="_blank" href="https://help.shopify.com/en/manual/products/details/tags">click here to know more.</a>',

    firstTimeOrderTag: {
      id: 'firstTimeOrderTag',
      displayName: 'First Time Order Tag',
      placeholder: 'Enter First Time Order Tag',
      type: 'inputWithVariables',
      validation: 'required',
      maxLength: 40


    },
    recurringOrderTag: {
      id: 'recurringOrderTag',
      displayName: 'Recurring Order Tag',
      placeholder: 'Enter Recurring Order Tag',
      type: 'inputWithVariables',
      validation: 'reuired',
      maxLength: 40

    }
  },

  customerTaggingSettings: {
    displayName: 'Customer Tagging Settings',
    helpText: 'Min 1 - Max 255 characters can be added in the below input fields. You can also add comma separated Dynamic variables from the button below to further customize you tagging. For best practices <a target="_blank" href="https://help.shopify.com/en/manual/products/details/tags">click here to know more.</a>',
    customerActiveSubscriptionTag: {
      id: 'customerActiveSubscriptionTag',
      displayName: 'Customer Active Subscription Tag',
      placeholder: 'Please Enter Customer Active Subscription Tag',
      type: 'inputWithVariables',
      validation: 'required',
      maxLength: 255
    },

    customerInActiveSubscriptionTag: {
      id: 'customerInActiveSubscriptionTag',
      displayName: 'Customer Cancelled Subscription Tag',
      placeholder: 'Enter the Customer Cancelled Subscription Tag',
      type: 'inputWithVariables',
      validation: 'required',
      maxLength: 255
    },

    customerPausedSubscriptionTag: {
      id: 'customerPausedSubscriptionTag',
      displayName: 'Customer Paused Subscription Tag',
      placeholder: 'Enter the Customer Paused Subscription Tag',
      type: 'inputWithVariables',
      validation: 'required',
      maxLength: 255
    },
  }
};
