import React, {Fragment} from 'react';
import {
    Button
} from 'reactstrap';

export default class WizardStep4 extends React.Component {

    constructor(props) {
        super(props);
        console.log(this.props);
    }

    render() {
        let hasErrors = this?.props?.errors?.some(iter => {
            return iter.hasOwnProperty('err');
        })
        return (
            <Fragment>
                <div className="form-wizard-content">
                    <div className="no-results">
                        <div className="sa-icon sa-success animate">
                            <span className="sa-line sa-tip animateSuccessTip"/>
                            <span className="sa-line sa-long animateSuccessLong"/>

                            <div className="sa-placeholder"/>
                            <div className="sa-fix"/>
                        </div>
                        <div className="results-subtitle mt-5">{this.props?.customerPortalSettingEntity?.addProductFinishedMessageTextV2}</div>
                        { !hasErrors &&
                            <div className="results-title">{this.props?.customerPortalSettingEntity?.contractUpdateMessageTextV2}</div>
                        }
                        { hasErrors &&
                            <>
                            <div className="results-title mt-2" style={{color: "#d92550"}}>{this.props?.customerPortalSettingEntity?.contractErrorMessageTextV2 ? this.props?.customerPortalSettingEntity?.contractErrorMessageTextV2  : "Error. Please contact merchant"}</div>
                            <div className="mt-3">
                                <div style={{fontSize: "14px", textAlign: "left"}}>
                                    {
                                        this?.props?.errors?.map((iter) => {
                                            if (iter?.err) {
                                                return (
                                                    <p>{iter?.item?.title}: {iter?.err}</p>
                                                )
                                            }
                                        })
                                    }
                                </div>
                            </div>
                            </>
                        }
                        {
                            (this?.props?.purchaseOption !== "SUBSCRIBE") ? <div className="mb-3 mt-3 oneTimePurchaseInfo">{this.props?.customerPortalSettingEntity?.oneTimePurchaseDisplayMessageTextV2}</div> : ""
                        }
                        <div className="mt-3 mb-3"/>
                    </div>
                </div>
            </Fragment>
        );
    }
}
