import React from 'react';
import PopupMessaging from './PopupMessaging';

const SplitContact = props => {
  const {
    showSplitContractMessaging,
    splitSuccess,
    splitError,
    customerPortalSettingEntity,
    subscriptionEntities,
    handleSplitContractProducts,
    splitContractProducts,
    shopName,
    splitWithOrder,
    setSplitWithOrder,
    productSelectValid
  } = props;
  return (
    <>
      {showSplitContractMessaging && <PopupMessaging showSuccess={splitSuccess} showError={splitError} />}
      {!showSplitContractMessaging && (
        <div className="as-text-sm as-text-gray-500">
          <div>
            {subscriptionEntities?.lines?.edges?.length > 1 && customerPortalSettingEntity?.enableSplitContract ? (
              <div>
               <div className="as-mt-4 as-ml-4">
                  <p className="as-text-gray-500 as-text-sm as-my-4">
                    {customerPortalSettingEntity?.splitContractMessage ||
                      'Once you confirm the split contract. A new, separate contract will be created.'}
                  </p>

                  {!productSelectValid && (
                      <p class="as-mt-2 as-text-sm as-text-red-600">
                        {'Please Select Products'}
                      </p>
                  )}
                  
                  {subscriptionEntities?.lines?.edges?.map(product => {
                      return product?.node && product?.node != null && product?.node?.productId != null ? (
                        <div className="as-flex as-items-center">
                          <input
                            className="!as-w-4 as-h-4 as-text-blue-600 as-bg-white-100 as-rounded as-border-gray-300 as-focus:ring-blue-500 as-focus:ring-2"
                            id={product?.node?.id}
                            checked={splitContractProducts.findIndex(item => item === product?.node?.id) > -1}
                            type="checkbox"
                            onChange={() => handleSplitContractProducts(product?.node?.id)}
                            style={{ width: '16px' }}
                          />
                          <label
                            htmlFor="default-checkbox"
                            className="as-ml-2 as-text-sm as-font-medium as-text-gray-900 as-flex as-items-center"
                          >
                            <img
                              src={product?.node?.variantImage?.transformedSrc}
                              width={50}
                              style={{ borderRadius: '29%', padding: '10px' }}
                            />
                            <span>
                              {' '}
                              {product?.node?.title}{' '}
                              {product?.node?.variantTitle && product?.node?.variantTitle !== '-' && '-' + product?.node?.variantTitle}
                            </span>
                          </label>
                        </div>
                      ) : (
                        ''
                      );
                    })}
                </div>
                <div className="as-grid as-gap-4 as-mb-4 as-py-2">
                    <div>
                      <label htmlFor="methodType" className="as-block as-text-sm as-font-medium as-text-gray-700 as-py-2">
                        {customerPortalSettingEntity?.selectSplitMethodLabelText || 'Select Split Method'}
                      </label>
                      <select name="methodType" type="select"
                        onChange={(event) => setSplitWithOrder(event.target.value)}
                        value={splitWithOrder}
                        className="focus:as-ring-indigo-500 focus:as-border-indigo-500 as-block as-w-full as-pr-12 sm:as-text-sm as-border-gray-300 as-rounded-md">
                          <option value={false}>{customerPortalSettingEntity?.splitWithoutOrderPlacedSelectOptionText || "Split without order placed"}</option>
                          <option value={true}>{customerPortalSettingEntity?.splitWithOrderPlacedSelectOptionText || "Split with order placed"}</option>
                      </select>
                    </div>
                  </div>
              </div>
            ) : (
              ''
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default SplitContact;
