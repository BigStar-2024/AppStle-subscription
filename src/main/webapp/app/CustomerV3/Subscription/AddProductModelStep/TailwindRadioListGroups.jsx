import React, { useState, useEffect } from 'react'
import { RadioGroup } from '@headlessui/react'

const plans = [
  {
    name: 'Startup',
    ram: '12GB',
    cpus: '6 CPUs',
    disk: '160 GB SSD disk',
  },
  {
    name: 'Business',
    ram: '16GB',
    cpus: '8 CPUs',
    disk: '512 GB SSD disk',
  },
  {
    name: 'Enterprise',
    ram: '32GB',
    cpus: '12 CPUs',
    disk: '1024 GB SSD disk',
  },
]

export default function TailwindRadioListGroup(props) {
  let {purchaseOption, setPurchaseOption, customerPortalSettingEntity} = props;
  const [purchasePlans, setPurchasePlans] = useState([])

  useEffect(() => {
    setPurchasePlans([
        {disabled: !customerPortalSettingEntity?.addAdditionalProduct,  value: "SUBSCRIBE", label: customerPortalSettingEntity?.addToSubscriptionTitleCPV2 || "Add to Subscription", description: customerPortalSettingEntity?.applySubscriptionDiscount ? customerPortalSettingEntity?.upSellMessage : ''},
        {disabled: !customerPortalSettingEntity?.addOneTimeProduct, value: "ONE_TIME", label: customerPortalSettingEntity?.oneTimePurchaseTitleCPV2 || "One Time Purchase", description: customerPortalSettingEntity?.oneTimePurchaseMessageTextV2 || "Get your product along with your next subscription order."},
    ])
  }, [customerPortalSettingEntity])

  return (
    <div className="as-w-full as-px-4 as-py-16 appstle-purchase-option-popup">
      <div className="as-mx-auto as-w-full as-max-w-md">
        <RadioGroup value={purchaseOption} onChange={setPurchaseOption}>
          <RadioGroup.Label className="as-sr-only">Purchase Options</RadioGroup.Label>
          <div className="as-space-y-2">
            {purchasePlans?.map((plan) => (
              <RadioGroup.Option
                key={plan.value}
                value={plan.value}
                disabled={plan.disabled}
                className={({ active, checked }) =>
                  `${
                    active
                      ? 'as-ring-2 as-ring-white as-ring-opacity-60 as-ring-offset-2 as-ring-offset-indigo-500'
                      : ''
                  }
                  ${
                    checked ? 'as-bg-indigo-700 as-bg-opacity-75 as-text-white' : 'as-bg-white'
                  }
                    as-relative as-flex as-cursor-pointer as-rounded-lg as-px-5 as-py-4 as-shadow-md focus:as-outline-none ${ plan.disabled ? `as-opacity-75 as-cursor-not-allowed` : ``}`
                }
              >
                {({ active, checked }) => (
                  <>
                    <div className="as-flex as-w-full as-items-center as-justify-between">
                      <div className="as-flex as-items-center">
                        <div className="as-text-sm">
                          <RadioGroup.Label
                            as="p"
                            className={`as-font-medium  ${
                              checked ? 'as-text-white' : 'as-text-gray-900'
                            }`}
                          >
                            {plan?.label}
                          </RadioGroup.Label>
                          <RadioGroup.Description
                            as="span"
                            className={`as-inline ${
                              checked ? 'as-text-indigo-100' : 'as-text-gray-500'
                            }`}
                          >
                            {plan?.description}
                          </RadioGroup.Description>
                        </div>
                      </div>
                      {checked && (
                        <div className="as-shrink-0 as-text-white">
                          <CheckIcon className="as-h-6 as-w-6" />
                        </div>
                      )}
                    </div>
                  </>
                )}
              </RadioGroup.Option>
            ))}
          </div>
        </RadioGroup>
      </div>
    </div>
  )
}

function CheckIcon(props) {
  return (
    <svg viewBox="0 0 24 24" fill="none" {...props}>
      <circle cx={12} cy={12} r={12} fill="#fff" opacity="0.2" />
      <path
        d="M7 13l3 3 7-7"
        stroke="#fff"
        strokeWidth={1.5}
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  )
}