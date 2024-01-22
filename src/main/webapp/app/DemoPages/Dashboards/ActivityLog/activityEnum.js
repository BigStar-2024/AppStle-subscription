export const ActivityLogEventType = [
  {
    type: 'NEXT_BILLING_DATE_CHANGE',
    description: "Next Billing Date Changed"
  },
  {
    type: 'NEXT_BILLING_TIME_CHANGE',
    description: "Next Billing Time Changed"
  },
  {
    type: 'BILLING_INTERVAL_CHANGE',
    description: "Billing Interval Changed"
  },
  {
    type: 'DELIVERY_INTERVAL_CHANGE',
    description: "Delivery Interval Changed"
  },
  {
    type: 'BILLING_ATTEMPT_NOTIFICATION',
    description: "Billing Attempt Notification"
  },
  {
    type: 'BILLING_ATTEMPT_TRIGGERED',
    description: "Billing Attempt Triggered"
  },
  {
    type: 'BILLING_ATTEMPT_SKIPPED',
    description: "Billing Attempt Skipped"
  },
  {
    type: 'PRODUCT_ADD',
    description: "Product Added"
  },
  {
    type: 'PRODUCT_REMOVE',
    description: "Product Removed"
  },
  {
    type: 'PRODUCT_REPLACE',
    description: "Product Replace"
  },
  {
    type: 'PRODUCT_QUANTITY_CHANGE',
    description: "Product Quantity Changed"
  },
  {
    type: 'PRODUCT_SELLING_PLAN_CHANGE',
    description: "Product Selling Plan Change"
  },
  {
    type: 'PRODUCT_PRICE_CHANGE',
    description: "Product Price Changed"
  },
  {
    type: 'CONTRACT_ACTIVATED',
    description: "Subscription Contract Activated"
  },
  {
    type: 'CONTRACT_CANCELLED',
    description: "Subscription Contract Cancelled"
  },
  {
    type: 'CONTRACT_PAUSED',
    description: "Subscription Contract Paused"
  },
  {
    type: 'SHIPPING_ADDRESS_CHANGE',
    description: "Shipping Address Changed"
  },
  {
    type: 'SYSTEM_UPDATED_DELIVERY_PRICE',
    description: "System Updated Delivery Price"
  },
  {
    type: 'MANUAL_DELIVERY_PRICE_UPDATED',
    description: "Manually Updated the Delivery Price"
  },
  {
    type: 'DELIVERY_PRICE_OVERRIDE_CHANGED',
    description: "Delivery Price Override Changed"
  },
  {
    type: 'DISCOUNT_APPLIED',
    description: "Discount Applied"
  },
  {
    type: 'DISCOUNT_REMOVED',
    description: "Discount Removed"
  },
  {
    type: 'DUNNING_CANCELLED',
    description: "Cancelled By Dunning Management"
  },
  {
    type: 'DUNNING_SKIPPED',
    description: "Skipped By Dunning Management"
  },
  {
    type: 'DUNNING_PAUSED',
    description: "Paused By Dunning Management"
  },
  {
    type: "WEBHOOK",
    description: "Webhook"
  }
]

export const ActivityLogEventSource = [
  {
    type: "CUSTOMER_PORTAL",
    description: "Customer Portal"
  },
  {
    type: "MERCHANT_PORTAL",
    description: "Merchant Portal"
  },
  {
    type: "SHOPIFY_EVENT",
    description: "Shopify Portal"
  },
  {
    type: "MERCHANT_PORTAL_BULK_AUTOMATION",
    description: "Merchant Portal Bulk Automation"
  },
  {
    type: "SYSTEM_EVENT",
    description: "System Event"
  },
  {
    type: "MERCHANT_EXTERNAL_API",
    description: "Merchant External API"
  },
]

export const ActivityLogEntityType = [
  {
    type: "SUBSCRIPTION_BILLING_ATTEMPT",
    description: "Subscription Billing Attempt"
  },
  {
    type: "SUBSCRIPTION_CONTRACT_DETAILS",
    description: "Subscription Contract"
  }
]
