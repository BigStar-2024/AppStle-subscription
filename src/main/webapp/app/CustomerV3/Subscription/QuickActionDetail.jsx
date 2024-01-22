import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { connect, useSelector } from 'react-redux';

const QuickActionDetail = ({ ...props }) => {
    const [quickActions, setQuickActions] = useState(props?.currentQueryParams['quickAction'].split("__"));
    const [isQuickActionFailed, setIsQuickActionFailed] = useState(false);

    useEffect(() => {
        let quickActionsClone = [...quickActions];
        let action = quickActionsClone.shift()
        if (action) {
            applyQuickAction(action);
        } else {
            props?.setIsQuickActionAvailable(false);
            props?.getCustomerPortalEntity(props?.contractId);
        }
    }, [quickActions])

    let applyQuickAction = (action) => {
        switch(action) {
            case "activate":
                if (props?.contractId) {
                    axios
                    .put(`api/subscription-contracts-update-status?contractId=${props?.contractId}&status=ACTIVE&isExternal=true`)
                    .then(shiftQuickAction)
                    .catch(function() {
                        setIsQuickActionFailed(true)
                    });
                } else {
                    setIsQuickActionFailed(true)
                }
                break;
            case "add_product":
                if (props?.contractId && props?.currentQueryParams?.variantIds) {
                    let variants = props?.currentQueryParams?.variantIds.split(',')
                    let addVariant = (variants) => {
                        if (variants.length) {
                            let currentVariant = variants.shift();
                            const requestUrl = `api/v2/subscription-contracts-add-line-item?contractId=${props?.contractId}&quantity=1&variantId=gid://shopify/ProductVariant/${currentVariant}`;
                            axios.put(requestUrl)
                            .then(function() {
                                addVariant(variants);
                            })
                            .catch(function() {
                                setIsQuickActionFailed(true)
                            });
                        } else {
                            shiftQuickAction();
                        }
                        
                    }

                    addVariant(variants);
                    
                    
                } else {
                    setIsQuickActionFailed(true)
                }
              break;
            case "apply_discount":
                if (props?.contractId && props?.currentQueryParams?.discountCode) {
                    const requestUrl = `/api/subscription-contracts-apply-discount?contractId=${props?.contractId}&discountCode=${props?.currentQueryParams?.discountCode}&isExternal=true`;
                    axios.put(requestUrl)
                    .then(shiftQuickAction)
                    .catch(function() {
                        setIsQuickActionFailed(true)
                    });
                } else {
                    setIsQuickActionFailed(true)
                }
              break;
            default:
                shiftQuickAction();
        }
    }

    const shiftQuickAction = () => {
        let quickActionsCopy = [...quickActions];
        quickActionsCopy.shift()
        setQuickActions([...quickActionsCopy])
    }

    return (
        <div>
            {
                isQuickActionFailed ? 
                    <div style={{textAlign: "center", padding: "20px", fontSize: "20px"}}>Incorrect Quick Action link URL. Please contact merchant.</div> : 
                    <div style={{ margin: '10% 0 0 43%', flexDirection: 'column' }} className="loader-wrapper d-flex justify-content-center align-items-center">
                        <div class="appstle_preloader appstle_loader--big"></div>
                    </div>
            }
        </div>
    )
}

const mapStateToProps = state => ({
   
  });

const mapDispatchToProps = {
   
};
  
export default connect(mapStateToProps, mapDispatchToProps)(QuickActionDetail);