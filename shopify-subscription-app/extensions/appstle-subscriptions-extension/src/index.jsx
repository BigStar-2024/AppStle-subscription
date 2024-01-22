import React, {useState, useEffect, useMemo, useCallback} from "react";
import {
    Banner,
    Button,
    Card,
    Checkbox,
    TextField,
    Text,
    InlineStack,
    BlockStack,
    extend,
    render,
    useData,
    useContainer,
    useSessionToken,
    useLocale,
    StackItem,
    Select,
    Spinner
} from "@shopify/admin-ui-extensions-react";

const translations = {
    de: {
        hello: "Guten Tag",
    },
    en: {
        hello: "Hello",
    },
    fr: {
        hello: "Bonjour",
    },
};

const frequencyOptions = [
    {
        label: "DAY",
        value: "DAY",
    },
    {
        label: "WEEK",
        value: "WEEK",
    },
    {
        label: "MONTH",
        value: "MONTH",
    },
    {
        label: "YEAR",
        value: "YEAR",
    },
];

const discountTypeOptions = [
    {
        label: "Percent off (%)",
        value: "PERCENTAGE",
    },
    {
        label: "Amount off ($)",
        value: "FIXED",
    },
];


const weeksOption = [
    {
        label: "Sunday",
        value: "7"
    },
    {
        label: "Monday",
        value: "1"
    },
    {
        label: "Tuesday",
        value: "2"
    },
    {
        label: "Wednesday",
        value: "3"
    },
    {
        label: "Thursday",
        value: "4"
    },
    {
        label: "Friday",
        value: "5"
    },
    {
        label: "Saturday",
        value: "6"
    }
]


function Actions({onPrimary, onClose, title}) {
    return (
        <InlineStack spacing="tight" distribution="trailing">
            <StackItem>
                <Button title={title} onPress={onPrimary} kind="primary" />
            </StackItem>
            <StackItem>
                <Button title="Cancel" onPress={onClose} />
            </StackItem>
        </InlineStack>
    );
}

// "Add" mode should allow a user to add the current product to an existing selling plan
// [Shopify admin renders this mode inside a modal container]
const baseBackendUrl = `https://subscription-admin.appstle.com/api/shopify/`;

function Add() {
    // Information about the product and/or plan your extension is editing.
    // Your extension receives different data in each mode.
    let data = useData();

    // The UI your extension renders inside
    const {close, done, setPrimaryAction, setSecondaryAction} = useContainer();

    // Information about the merchant"s selected language. Use this to support multiple languages.
    const locale = useLocale();

    // Use locale to set translations with a fallback
    const localizedStrings = useMemo(() => {
        return translations[locale] || translations.en;
    }, [locale]);

    // Session token contains information about the current user. Use it to authenticate calls
    // from your extension to your app server.
    const {getSessionToken} = useSessionToken();

    const [selectedPlans, setSelectedPlans] = useState([]);
    // const mockPlans = [
    //   {name: "Subscription Plan A", id: "a"},
    //   {name: "Subscription Plan B", id: "b"},
    //   {name: "Subscription Plan C", id: "c"},
    // ];

    let [subscriptionGroups, setSubscriptionGroups] = useState([])

    useEffect(async () => {
        // Get a fresh session token before every call to your app server.
        const token = await getSessionToken();

        // Here, send the form data to your app server to add the product to an existing plan.
        // TODO: this URL may needed to be updated with query params
        const url = `${baseBackendUrl}v2/subscription-groups-list`;
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                'appstle-authorisation': token || ''
            },
        })
        if (!response.ok) {
            throw new Error('Error in fetching data for Add plan');
        }

        const parsedJson = await response.json();
        setSubscriptionGroups([...parsedJson])

    }, [])

    // Configure the extension container UI
    useEffect(() => {
        setPrimaryAction({
            content: "Add to plan",
            onAction: async () => {
                // Get a fresh session token before every call to your app server.
                const token = await getSessionToken();

                // Here, send the form data to your app server to add the product to an existing plan.
                // TODO: this URL needed to be added as per the API requirement
                const url = `${baseBackendUrl}v2/add-plans`;
                const response = await fetch(url, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'appstle-authorisation': token || ''
                    },
                    body: JSON.stringify({...data, plans: selectedPlans})
                })
                if (!response.ok) {
                    setLoading(false);
                    throw new Error('Error in setting plan to product for Add plan');
                }

                // Upon completion, call done() to trigger a reload of the resource page
                // and terminate the extension.
                done();
            },
        });

        setSecondaryAction({
            content: "Cancel",
            onAction: () => close(),
        });
    }, [getSessionToken, close, done, setPrimaryAction, setSecondaryAction, data, selectedPlans]);

    return (
        <>
            <InlineStack vertical spacing="loose">
                <StackItem>
                    <InlineStack vertical spacing="extraTight">
                        <Text size="titleSmall">Add Plans</Text>
                        <Text size="small">
                            Please select from the below Subscription plans to add it on the product.
                        </Text>
                    </InlineStack>
                </StackItem>

                <StackItem>
                    <InlineStack vertical spacing="tight">
                        {subscriptionGroups.map((plan) => (
                            <Checkbox
                                key={plan.id}
                                label={plan.groupName}
                                onChange={(checked) => {
                                    let plans;
                                    if (checked) {
                                        plans = selectedPlans.concat(plan.id);
                                    } else {
                                        plans = selectedPlans.filter((id) => id !== plan.id);
                                    }
                                    setSelectedPlans(plans);
                                }}
                                checked={selectedPlans.includes(plan.id)}
                            />
                        ))}
                    </InlineStack>
                </StackItem>
            </InlineStack>
        </>
    );
}

// CREATE STARTS FROM HERE
// "Create" mode should create a new selling plan, and add the current product to it
// [Shopify admin renders this mode inside an app overlay container]
function Create() {
    let data = useData();
    const {getSessionToken} = useSessionToken();
    const {close, done} = useContainer();
    let [loading, setLoading] = useState(false);
    let [error, setError] = useState({subscriptionPlans: [{}]});
    let [bannerVisibility, setBannerVisibility] = useState(false);
    let [initialData, setInitialData] = useState({});
    let [bannerError, setBannerError] = useState([])
    let [formState, setFormState] = useState(
        {
            groupName: 'Subscribe and Save',
            subscriptionPlans: [{
                planType: "PAY_AS_YOU_GO",
                frequencyCount: '1',
                frequencyInterval: 'DAY',
                frequencyName: '',
                billingFrequencyCount: null,
                billingFrequencyInterval: null,
                specificDayEnabled: false,
                frequencyType: "ON_PURCHASE_DAY",
                specificDayValue: '1',
                cutOff: null,
                minCycles: '1',
                maxCycles: null,
                discountEnabled: false,
                discountEnabled2: false,
                discountOffer: null,
                discountOffer2: null,
                discountType: null,
                discountType2: null,
                afterCycle2: null,
                discountEnabled2Masked: false
            }]
        })

    let subscriptionPlan =
        {
            planType: "PAY_AS_YOU_GO",
            frequencyCount: '1',
            frequencyInterval: 'DAY',
            frequencyName: '',
            billingFrequencyCount: null,
            billingFrequencyInterval: null,
            specificDayEnabled: false,
            frequencyType: "ON_PURCHASE_DAY",
            specificDayValue: '1',
            cutOff: null,
            minCycles: '1',
            maxCycles: null,
            discountEnabled: false,
            discountEnabled2: false,
            discountOffer: null,
            discountOffer2: null,
            discountType: null,
            discountType2: null,
            afterCycle2: null,
            discountEnabled2Masked: false
        }

    useEffect(function () {
        setFormState({...formState, ...initialData})
    }, [initialData])

    useEffect(function () {
        serialiseError(error)
    }, [error])

    useEffect(function () {
        finalValidationBeforeSubmit({...formState})
    }, [formState])

    const addFrequencyPlan = () => {
        let currentSubscritionPlans = [...formState?.['subscriptionPlans']];
        let updatedSubscriptionPlans = [...currentSubscritionPlans, {...subscriptionPlan}];
        setFormState({...formState, subscriptionPlans: updatedSubscriptionPlans});
    }
    const deleteFrequencyPlan = (index) => {
        let updatedSubscriptionPlans = formState?.['subscriptionPlans']?.filter((plan, idx) => index !== idx);
        setFormState({...formState, subscriptionPlans: updatedSubscriptionPlans});
    }

    const checkIfValueIsNumberOrString = (val) => {
        if (typeof val === 'string' || typeof val === 'number') {
            val = String(val);
        } else {
            val = '';
        }
        return val;
    }

    const textFieldValidation = (val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val)
        if (!val || !val?.trim()) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a text.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a text.`
            }
            // setError({...err});
            return {validationError: {...err}, valid: false};

        } else if (val) {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[index]?.[fieldKey]
                    // setError({...errCopy});
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    // setError({...errCopy});
                }
            }
            return {validationError: {...errCopy}, valid: true};
        }

    }

    const numberFieldValidation = (val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        if (isNaN(val.trim())) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should be a number. Please enter a number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should be a number. Please enter a number.`
            }

            // setError({...err});
            return {validationError: err, valid: false};
        } else if (!(val.trim())) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a number.`
            }
            // setError({...err})
            return {validationError: err, valid: false};
        } else if (parseInt(val.trim()) <= 0) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field value should be greater than 0. Please enter a valid number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field value should be greater than 0. Please enter a valid number.`
            }
            // setError({...err})
            return {validationError: err, valid: false};
        } else if (val) {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[index]?.[fieldKey]
                    // setError({...errCopy});
                } else {
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    //setError({...errCopy});
                }
            }
            return {validationError: errCopy, valid: true};
        }
    }

    const sameFieldValueValidation = (formState, val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        formState?.['subscriptionPlans']?.forEach(function (plan, idx) {
            if (idx !== index) {
                if (fieldKey === 'frequencyCount') {
                    if (`${plan?.[fieldKey]}${plan?.['frequencyInterval']}` === `${val}${formState?.['subscriptionPlans']?.[index]?.['frequencyInterval']}`) {
                        err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Multiple frequency plans cannot have same frequency value of "${val} ${formState?.['subscriptionPlans']?.[index]?.['frequencyInterval']}".`
                        isErrorSet = true;
                    }
                } else {
                    if (plan[fieldKey]?.trim().split(' ').join('').toLowerCase() === val?.trim().split(' ').join('').toLowerCase()) {
                        err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Multiple frequency plans cannot have same name as "${val}".`
                        isErrorSet = true;
                    }
                }

            }
        })

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[fieldKey]
                    // setError({...err});
                } else {
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    //setError({...err});
                }
            }
            return {validationError: errCopy, valid: true};
        }
    }

    const minValidation = (formState, val, fieldName, maxFieldName, fieldKey, maxFieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (formState['subscriptionPlans'][index]?.[maxFieldKey] &&
            (parseInt(val) > parseInt(formState['subscriptionPlans'][index]?.[maxFieldKey]))) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be less than or equal the "${maxFieldName}".`
            isErrorSet = true
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const maxValidation = (formState, val, fieldName, minFieldName, fieldKey, minFieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (formState['subscriptionPlans'][index]?.[minFieldKey] &&
            (parseInt(val) < parseInt(formState['subscriptionPlans'][index]?.[minFieldKey]))) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be less than or equal the "${minFieldName}".`
            isErrorSet = true
        } else if ((formState['subscriptionPlans'][index]?.['planType'] === "ADVANCED_PREPAID") && (parseInt(val) < 2)) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be greater than 1 for Advanced prepaid plan type".`
            isErrorSet = true
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }

    }

    const billingPeriodValidation = (formState, val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (val) {
            if (formState['subscriptionPlans'][index]?.['frequencyCount'] &&
                (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) >= parseInt(val))) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}": should be greater than the delivery frequency.`
                isErrorSet = true;
            } else if (formState['subscriptionPlans'][index]?.['frequencyCount'] && (parseInt(val) % parseInt(formState['subscriptionPlans'][index]?.['frequencyCount'])) != 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}": must be a multiple of the delivery frequency duration.`;
                isErrorSet = true;
            }
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const validatespecificDayValue = (formState, val, fieldName, fieldKey, error, index) => {
        val = parseInt(checkIfValueIsNumberOrString(val));
        let err = {...error};
        let isErrorSet = false;
        if (val) {
            if (
                formState['subscriptionPlans'][index]?.['frequencyInterval']
                && formState['subscriptionPlans'][index]?.['frequencyType']
                && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
                && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH"
                && (val > 31 || val < 1)
            ) {
                err['subscriptionPlans'][index][fieldKey] = `Specific Day is between 1st to 31st`
                isErrorSet = true;
            } else if (
                formState['subscriptionPlans'][index]?.['frequencyInterval']
                && formState['subscriptionPlans'][index]?.['frequencyType']
                && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
                && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
                && (val > 7 || val < 1)
            ) {
                err['subscriptionPlans'][index][fieldKey] = `Specific Day is between 1st to 7th`
                isErrorSet = true;
            }
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const validateCutOff = (formState, val, fieldName, fieldKey, error, index) => {
        val = parseInt(checkIfValueIsNumberOrString(val));
        let err = {...error};
        let isErrorSet = false;
        if (
            formState['subscriptionPlans'][index]?.['frequencyInterval']
            && formState['subscriptionPlans'][index]?.['frequencyType']
            && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
            && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH"
            && (val > (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 31) || val < 1)
        ) {
            err['subscriptionPlans'][index][fieldKey] = `Cutoff cannot be greater than ${(parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 31)} days or less than 1`
            isErrorSet = true;
        } else if (
            formState['subscriptionPlans'][index]?.['frequencyInterval']
            && formState['subscriptionPlans'][index]?.['frequencyType']
            && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
            && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
            && (val > (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 7) || val < 1)
        ) {
            err['subscriptionPlans'][index][fieldKey] = `Cutoff cannot be greater than ${(parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 7)} days or less than 1`
            isErrorSet = true;
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const removeFieldError = (error, index, fieldKey) => {
        let errCopy = {...error}
        if (index || index === 0) {
            if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                delete errCopy?.['subscriptionPlans']?.[fieldKey]
                // setError({...err});
            } else {
            }
        } else {
            if (fieldKey in errCopy) {
                delete errCopy[fieldKey]
                //setError({...err});
            }
        }
        return {validationError: errCopy, valid: true};
    }

    const finalValidationBeforeSubmit = (formState) => {
        let finalError = {subscriptionPlans: formState?.['subscriptionPlans']?.map(plan => ({}))};
        const groupNameValidation = textFieldValidation(formState?.['groupName'], 'Subscription Plan Name', 'planTitle', {...finalError});
        finalError = {...finalError, ...(groupNameValidation.validationError)};

        formState?.['subscriptionPlans']?.forEach((plan, index) => {
            Object.keys(plan).forEach((key, idx) => {
                if (key === 'frequencyCount') {
                    let frequencyCountValidation = numberFieldValidation(plan[key], 'Delivery frequency', 'frequencyCount', {...finalError}, index);
                    if (frequencyCountValidation.valid) {
                        frequencyCountValidation = sameFieldValueValidation(formState, plan[key], 'Delivery frequency', 'frequencyCount', {...frequencyCountValidation.validationError}, index)
                    }
                    finalError = {...finalError, ...(frequencyCountValidation.validationError)}
                } else if (key === 'frequencyName') {
                    let frequencyNameValidation = textFieldValidation(plan[key], 'Frequency Plan Name', 'frequencyName', {...finalError}, index);
                    if (frequencyNameValidation.valid) {
                        frequencyNameValidation = sameFieldValueValidation(formState, plan[key], 'Frequency Plan Name', 'frequencyName', {...frequencyNameValidation.validationError}, index)
                    }
                    finalError = {...finalError, ...(frequencyNameValidation.validationError)}
                } else if (plan?.['discountEnabled'] && key === 'discountOffer') {
                    let discountOfferValidation = numberFieldValidation(plan?.[key], 'Discount Offer', 'discountOffer', {...finalError}, index);
                    finalError = {...finalError, ...(discountOfferValidation?.validationError)}
                } else if (plan?.['discountEnabled2'] && key === 'discountOffer2') {
                    if (parseInt(plan?.[key]) !== 0) {
                        let discountOfferValidation = numberFieldValidation(plan?.[key], 'After Cycle Discount Offer', 'discountOffer2', {...finalError}, index);
                        finalError = {...finalError, ...(discountOfferValidation?.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'discountOffer2');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (plan?.['discountEnabled2'] && key === 'afterCycle2') {
                    let afterCycleValidation = numberFieldValidation(plan?.[key], 'After Cycle', 'afterCycle2', {...finalError}, index);
                    finalError = {...finalError, ...(afterCycleValidation?.validationError)}
                } else if (key === 'minCycles') {
                    if (formState['subscriptionPlans'][index]?.['minCycles'] != null) {
                        let minCyclesValidation = numberFieldValidation(plan?.[key], 'Minimum number of Orders', 'minCycles', {...finalError}, index);
                        if (minCyclesValidation.valid) {
                            minCyclesValidation = minValidation(
                                formState,
                                parseInt(formState['subscriptionPlans'][index]?.['minCycles']),
                                'Minimum number of Orders',
                                'Maximum number of Orders',
                                'minCycles',
                                'maxCycles',
                                {...finalError},
                                index)
                        }
                        finalError = {...finalError, ...(minCyclesValidation.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'minCycles');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === 'maxCycles') {
                    if (formState['subscriptionPlans'][index]?.['maxCycles'] != null) {
                        let maxCyclesValidation = numberFieldValidation(plan?.[key], 'Maximum number of Orders', 'maxCycles', {...finalError}, index);
                        if (maxCyclesValidation.valid) {
                            maxCyclesValidation = maxValidation(
                                formState,
                                parseInt(formState['subscriptionPlans'][index]?.['maxCycles']),
                                'Minimum number of Orders',
                                'Maximum number of Orders',
                                'maxCycles',
                                'minCycles',
                                {...finalError},
                                index)
                        }
                        finalError = {...finalError, ...(maxCyclesValidation.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'maxCycles');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === 'billingFrequencyCount') {
                    if (formState['subscriptionPlans'][index]?.['billingFrequencyCount'] != null || formState['subscriptionPlans'][index]?.['planType'] != "PAY_AS_YOU_GO") {
                        let billingFrequencyCountValidation = numberFieldValidation(plan?.[key], 'Billing Period', 'billingFrequencyCount', {...finalError}, index);
                        if (billingFrequencyCountValidation.valid) {
                            billingFrequencyCountValidation = billingPeriodValidation(formState, plan[key], 'Billing Period', 'billingFrequencyCount', {...billingFrequencyCountValidation.validationError}, index)
                        }
                    } else {
                        let validation = removeFieldError(finalError, index, 'billingFrequencyCount');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === "specificDayValue") {
                    if (formState['subscriptionPlans'][index]?.['specificDayValue'] != null) {
                        let specificDayValueValidation = numberFieldValidation(plan?.[key], 'Specific Day value', 'specificDayValue', {...finalError}, index);
                        if (specificDayValueValidation.valid) {
                            specificDayValueValidation = validatespecificDayValue(formState, plan[key], 'Specific Day value', 'specificDayValue', {...specificDayValueValidation.validationError}, index)
                        }
                    } else {
                        let validation = removeFieldError(finalError, index, 'specificDayValue');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === "cutOff") {
                    if (formState['subscriptionPlans'][index]?.['specificDayValue'] && formState['subscriptionPlans'][index]?.['specificDayEnabled']) {
                        if (formState['subscriptionPlans'][index]?.['cutOff'] != null) {
                            let cutOffValidation = numberFieldValidation(plan?.[key], 'Cutoff', 'cutOff', {...finalError}, index);
                            if (cutOffValidation.valid) {
                                cutOffValidation = validateCutOff(formState, plan[key], 'Cutoff', 'cutOff', {...cutOffValidation.validationError}, index)
                            }
                        } else {
                            let validation = removeFieldError(finalError, index, 'cutOff');
                            finalError = {...finalError, ...(validation.validationError)}
                        }
                    }
                }
            })
        })
        setError({...finalError})
        return finalError;
    }

    const serialiseError = (errors) => {
        let serialisedError = [];
        errors && Object.keys(errors).forEach(key => {
            if (typeof errors[key] !== 'object') {
                let trimmedSerialisedObject = serialisedError?.map(err => err?.trim().split(' ').join('').toLowerCase())
                if (trimmedSerialisedObject.indexOf(errors[key]?.trim().split(' ').join('').toLowerCase()) === -1) {
                    serialisedError.push(errors[key])
                }
            } else {
                errors[key] && errors[key]?.forEach((errObj, index) => {
                    errObj && Object.keys(errObj).forEach(errKey => {
                        let trimmedSerialisedObject = serialisedError?.map(err => err?.trim().split(' ').join('').toLowerCase())
                        if (trimmedSerialisedObject.indexOf(errObj[errKey]?.trim().split(' ').join('').toLowerCase()) === -1) {
                            serialisedError.push(errObj[errKey])
                        }
                    });
                })
            }
        })
        setBannerError(serialisedError);
        return serialisedError;
    }

    const onPrimaryAction = useCallback(async () => {
        const finalError = finalValidationBeforeSubmit({...formState})
        const finalSeriealisedError = serialiseError({...finalError})
        if (finalSeriealisedError && (finalSeriealisedError?.length > 0)) {
            setBannerVisibility(true);
            return;
        } else {
            setBannerVisibility(false);
        }
        setLoading(true);
        const token = await getSessionToken();

        data = {
            ...data,
            ...formState
        }

        // TODO: enter a correct url
        const url = `${baseBackendUrl}v2/subscription-groups`;
        const response = await fetch(url, {
            method: false ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'appstle-authorisation': token || ''
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            setLoading(false);
            throw new Error('Error in submitting plan data.');
        }
        done();
        setLoading(false);
    }, [getSessionToken, done, data, formState, error, bannerVisibility]);


    const cachedActions = useMemo(
        () => (
            <Actions onPrimary={onPrimaryAction} onClose={close} title="Save plan"/>
        ),
        [onPrimaryAction, close]
    );

    const updateFormState = (formState, fieldName, val, index) => {
        formState = updatePlanValues(formState, fieldName, val, index)
        setFormState({...formState})
    }

    const updatePlanValues = (formState, fieldName, val, index) => {
        formState = JSON.parse(JSON.stringify(formState))
        let subscriptionPlansClone = [...formState?.['subscriptionPlans']];
        let currentFreqPlan = {...subscriptionPlansClone[index]}
        currentFreqPlan[fieldName] = val;
        subscriptionPlansClone[index] = {...currentFreqPlan};
        return {...formState, subscriptionPlans: [...subscriptionPlansClone]}
    }

    return (
        <>
            {/* FOR TESTING PURPOSE ONLY DONOT UNCOMMENT
        <Text>-----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(formState)}</Text>
        <Text>----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(error)}</Text>
        <Text>-----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(bannerError)}</Text>
        <Text>-----------------------------------------------------------------</Text>
            FOR TESTING PURPOSE ONLY DONOT UNCOMMENT */}

            {loading &&
            <InlineStack vertical distribution="center" alignment="center">
                <Spinner accessibilityLabel="Loading..." size="large"/>
                <Text>Please wait. Loading.</Text>
            </InlineStack>
            }
            {!loading &&

            <>
                {(bannerVisibility) &&
                <Banner
                    status={bannerError?.length > 0 ? 'critical' : 'success'}
                    title={bannerError?.length > 0 ? "Please resolve errors before submitting" : 'You are all set'}
                    onDismiss={() => setBannerVisibility(false)}>
                    {/* {bannerError?.map((err) => {
                        return (
                            <Text size="medium" color="warning">{err}</Text>
                        )
                    })} */}
                </Banner>}
                <Card sectioned title="Create Subscription Plan">
                    <InlineStack distribution="fill">
                        <TextField
                            label="Subscription Plan Name"
                            error={error?.['planTitle']}
                            value={checkIfValueIsNumberOrString(formState?.groupName)}
                            onChange={(val) => {
                                setFormState({...formState, groupName: val})
                            }}
                            placeholder="Enter a text for Subscription plan name"
                        />
                    </InlineStack>
                </Card>
                {formState?.['subscriptionPlans']?.map((plan, index) => {
                    return (
                        <Card sectioned>

                            <BlockStack vertical spacing="loose">
                                <StackItem>
                                    <InlineStack distribution="trailing">
                                        {((formState?.['subscriptionPlans']?.length - 1) === index) && (
                                            <StackItem>
                                                <Button title={'Add more plan'}
                                                        onPress={addFrequencyPlan}
                                                        kind="primary"/>
                                            </StackItem>)}
                                        {formState?.['subscriptionPlans']?.length > 1 && (
                                            <StackItem>
                                                <Button title="Delete"
                                                        onPress={() => deleteFrequencyPlan(index)}
                                                        kind="primary"
                                                        appearance="critical"/>
                                            </StackItem>)}
                                    </InlineStack>
                                </StackItem>

                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <Select
                                            label="Plan Type"
                                            value={checkIfValueIsNumberOrString(plan?.planType)}
                                            onChange={(val) => {
                                                let formStateClone = JSON.parse(JSON.stringify(formState))
                                                if (val === "PREPAID") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", 1, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", null, index)
                                                } else if (val === "PAY_AS_YOU_GO") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", 1, index)
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", null, index)
                                                } else if (val === "ADVANCED_PREPAID") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", 2, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", null, index)
                                                }
                                                formStateClone = updatePlanValues(formStateClone, "planType", (val ? String(val)?.trim() : ""), index)
                                                setFormState({...formStateClone})
                                            }}
                                            options={[
                                                {
                                                    label: "Pay As You Go",
                                                    value: "PAY_AS_YOU_GO"
                                                },
                                                {
                                                    label: "Prepaid",
                                                    value: "PREPAID"
                                                },
                                                {
                                                    label: "Advanced Prepaid",
                                                    value: "ADVANCED_PREPAID"
                                                }
                                            ]}
                                        />
                                    </InlineStack>
                                </StackItem>

                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <Select
                                            label={formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "Fulfillment frequency" : "Order frequency"}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyCount)}
                                            onChange={(val) => {
                                                updateFormState(formState, "frequencyCount", (val ? String(val)?.trim() : ""), index)
                                            }}
                                            options={[...Array(100).keys()].map(i => ({
                                                label: `${i + 1}`,
                                                value: `${i + 1}`
                                            }))}
                                        />
                                        <Select
                                            label="Select delivery frequency type"
                                            options={frequencyOptions}
                                            onChange={(val) => {
                                                let formStateClone = JSON.parse(JSON.stringify(formState))
                                                if (formState['subscriptionPlans'][index]?.['billingFrequencyCount']) {
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", val, index);
                                                }
                                                formStateClone = updatePlanValues(formStateClone, "frequencyInterval", val, index);
                                                formStateClone = updatePlanValues(formStateClone, "specificDayEnabled", false, index);
                                                formStateClone = updatePlanValues(formStateClone, "specificDayValue", null, index);
                                                formStateClone = updatePlanValues(formStateClone, "frequencyType", "ON_PURCHASE_DAY", index);
                                                formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                setFormState({...formStateClone})

                                            }}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyInterval)}
                                        />
                                    </InlineStack>
                                </StackItem>
                                {formState['subscriptionPlans'][index]?.['planType'] != "PAY_AS_YOU_GO" && <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label="Billing Period"
                                            error={error?.['subscriptionPlans']?.[index]?.['billingFrequencyCount']}
                                            value={checkIfValueIsNumberOrString(plan?.billingFrequencyCount)}
                                            onChange={(val) => {
                                                val = val ? String(val)?.trim() : null;
                                                if (val) {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", val, index);
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", formState['subscriptionPlans'][index]?.['frequencyInterval'], index);
                                                    setFormState({...formStateClone})
                                                } else {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", null, index);
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", null, index);
                                                    setFormState({...formStateClone})
                                                }
                                            }}
                                        />
                                        <Select
                                            label="Select Billing frequency type"
                                            readonly
                                            options={[
                                                {
                                                    label: formState['subscriptionPlans'][index]?.['frequencyInterval'],
                                                    value: formState['subscriptionPlans'][index]?.['frequencyInterval'],
                                                }]}
                                            value={checkIfValueIsNumberOrString(plan?.billingFrequencyInterval)}
                                        />
                                    </InlineStack>
                                </StackItem>}
                                {(formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH" ||
                                    formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK") &&
                                <>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Checkbox
                                                label={formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "Want to set billing date?" : "Want to set order date?"}
                                                onChange={(val) => {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayEnabled", val, index);
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayValue", null, index);
                                                    formStateClone = updatePlanValues(formStateClone, "frequencyType", "ON_PURCHASE_DAY", index);
                                                    formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                    setFormState({...formStateClone})
                                                }}
                                                value={plan?.specificDayEnabled}
                                            />
                                        </InlineStack>
                                    </StackItem>
                                    {formState['subscriptionPlans'][index]?.['specificDayEnabled'] &&
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Select
                                                label="Select Option"
                                                value={checkIfValueIsNumberOrString(plan?.frequencyType)}
                                                onChange={(val) => {
                                                    let defaultSpecificDayValue = null
                                                    //TODO: Review here below condition remove the week wala condition
                                                    //  && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
                                                    if (val === "ON_SPECIFIC_DAY") {
                                                        defaultSpecificDayValue = 1;
                                                    }
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "frequencyType", (val ? String(val)?.trim() : ""), index);
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayValue", defaultSpecificDayValue, index);
                                                    formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                    setFormState({...formStateClone})
                                                }}
                                                options={[
                                                    {
                                                        label: " On Purchase Day",
                                                        value: "ON_PURCHASE_DAY"
                                                    },
                                                    {
                                                        label: " On Specific Day",
                                                        value: "ON_SPECIFIC_DAY"
                                                    },
                                                ]}
                                            />
                                            {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                            formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK" &&
                                            <Select
                                                label="Choose Day"
                                                options={weeksOption}
                                                onChange={(val) => {
                                                    updateFormState(formState, "specificDayValue", (val ? String(val)?.trim() : ""), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.specificDayValue)}
                                            />}
                                            {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                            formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH" &&
                                            <TextField
                                                label="Enter Day"
                                                error={error?.['subscriptionPlans']?.[index]?.['specificDayValue']}
                                                onChange={(val) => {
                                                    updateFormState(formState, "specificDayValue", (val ? String(val)?.trim() : null), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.specificDayValue)}
                                            />}
                                        </InlineStack>
                                    </StackItem>}

                                    {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <TextField
                                                label="Cutoff"
                                                error={error?.['subscriptionPlans']?.[index]?.['cutOff']}
                                                onChange={(val) => {
                                                    updateFormState(formState, "cutOff", (val ? String(val)?.trim() : null), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.cutOff)}
                                            />
                                        </InlineStack>
                                    </StackItem>}
                                </>}
                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label="Frequency Plan Name"
                                            error={error?.['subscriptionPlans']?.[index]?.['frequencyName']}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyName)}
                                            onChange={(val) => {
                                                updateFormState(formState, "frequencyName", (val ? String(val)?.trim() : ""), index)
                                            }}
                                            placeholder="Enter text for Frequency Plan Name"
                                        />
                                    </InlineStack>
                                </StackItem>
                                {formState['subscriptionPlans'][index]?.['planType'] != "PREPAID" &&
                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label={`Minimum number of ${formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "billing Iterations" : "Orders"}`}
                                            error={error?.['subscriptionPlans']?.[index]?.['minCycles']}
                                            value={checkIfValueIsNumberOrString(plan?.minCycles)}
                                            onChange={(val) => {
                                                updateFormState(formState, "minCycles", (val ? String(val)?.trim() : null), index)
                                            }}
                                        />
                                        <TextField
                                            label={`Maximum number of ${formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "billing Iterations" : "Orders"}`}
                                            error={error?.['subscriptionPlans']?.[index]?.['maxCycles']}
                                            onChange={(val) => {
                                                updateFormState(formState, "maxCycles", (val ? String(val)?.trim() : null), index);
                                            }}
                                            value={checkIfValueIsNumberOrString(plan?.maxCycles)}
                                        />
                                    </InlineStack>
                                </StackItem>}
                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <Checkbox
                                            label="Enable Discount"
                                            onChange={(val) => {
                                                let formStateClone = JSON.parse(JSON.stringify(formState))
                                                let discountEnabled = formStateClone?.['subscriptionPlans']?.[index]?.['discountEnabled']
                                                let discountType = formStateClone?.['subscriptionPlans']?.[index]?.['discountType']
                                                formStateClone = updatePlanValues(formStateClone, "discountEnabled", !discountEnabled, index)
                                                if (!discountEnabled && !discountType) {
                                                    formStateClone = updatePlanValues(formStateClone, "discountType", 'PERCENTAGE', index)
                                                } else if (discountEnabled) {
                                                    formStateClone = updatePlanValues(formStateClone, "discountOffer", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "discountType", null, index)
                                                }
                                                formStateClone = updatePlanValues(formStateClone, "discountOffer2", null, index)
                                                formStateClone = updatePlanValues(formStateClone, "discountType2", null, index)
                                                formStateClone = updatePlanValues(formStateClone, "afterCycle2", null, index)
                                                formStateClone = updatePlanValues(formStateClone, "discountEnabled2", false, index)
                                                setFormState({...formStateClone})
                                            }}
                                            value={plan?.discountEnabled}
                                        />
                                    </InlineStack>
                                </StackItem>
                                {plan?.discountEnabled &&

                                (<>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <TextField
                                                label="Discount Offer"
                                                error={error?.['subscriptionPlans']?.[index]?.['discountOffer']}
                                                value={checkIfValueIsNumberOrString(plan?.discountOffer)}
                                                onChange={(val) => {
                                                    updateFormState(formState, "discountOffer", (val ? String(val)?.trim() : ""), index)
                                                }}
                                                placeholder="Enter a number to offer discount"
                                            />
                                        </InlineStack>
                                    </StackItem>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Select
                                                label="Discount Type"
                                                options={discountTypeOptions}
                                                onChange={(val) => {
                                                    updateFormState(formState, "discountType", val, index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.discountType)}
                                            />
                                        </InlineStack>
                                    </StackItem>
                                </>)
                                }
                                {plan?.discountEnabled &&
                                <>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Checkbox
                                                label="Change Discount After"
                                                onChange={(val) => {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    let discountEnabled = formStateClone?.['subscriptionPlans']?.[index]?.['discountEnabled2']
                                                    let discountType = formStateClone?.['subscriptionPlans']?.[index]?.['discountType2']
                                                    formStateClone = updatePlanValues(formStateClone, "discountEnabled2", !discountEnabled, index)
                                                    if (!discountEnabled && !discountType) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountType2", 'PERCENTAGE', index)
                                                        formStateClone = updatePlanValues(formStateClone, "afterCycle2", '1', index)
                                                    } else if (discountEnabled) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountOffer2", null, index)
                                                        formStateClone = updatePlanValues(formStateClone, "discountType2", null, index)
                                                        formStateClone = updatePlanValues(formStateClone, "afterCycle2", null, index)
                                                    }
                                                    setFormState({...formStateClone})
                                                }}
                                                value={plan?.discountEnabled2}
                                            />
                                        </InlineStack>
                                    </StackItem>
                                    {plan?.discountEnabled2 &&

                                    (<>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <TextField
                                                    label="After Cycle"
                                                    error={error?.['subscriptionPlans']?.[index]?.['afterCycle2']}
                                                    value={checkIfValueIsNumberOrString(plan?.afterCycle2)}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "afterCycle2", (val ? String(val)?.trim() : null), index)
                                                    }}
                                                    placeholder="Enter a number to offer discount"
                                                />
                                            </InlineStack>
                                        </StackItem>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <TextField
                                                    label="Discount Offer"
                                                    error={error?.['subscriptionPlans']?.[index]?.['discountOffer2']}
                                                    value={checkIfValueIsNumberOrString(plan?.discountOffer2)}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "discountOffer2", (val ? String(val)?.trim() : ""), index)
                                                    }}
                                                    placeholder="Enter a number to offer discount"
                                                />
                                            </InlineStack>
                                        </StackItem>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <Select
                                                    label="Discount Type"
                                                    options={discountTypeOptions}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "discountType2", val, index)
                                                    }}
                                                    value={checkIfValueIsNumberOrString(plan?.discountType2)}
                                                />
                                            </InlineStack>
                                        </StackItem>
                                    </>)
                                    }
                                </>
                                }
                            </BlockStack>
                        </Card>
                    )
                })

                }
                <InlineStack vertical spacing="none">
                    <InlineStack vertical spacing="extraLoose">
                        <StackItem>
                            <InlineStack distribution="trailing">
                                <StackItem>
                                    {cachedActions}
                                </StackItem>
                            </InlineStack>
                        </StackItem>
                    </InlineStack>
                </InlineStack>
            </>

            }
        </>
    )

}

// CREATE ENDS HERE

// "Remove" mode should remove the current product from a selling plan.
// This should not delete the selling plan.
// [Shopify admin renders this mode inside a modal container]
function Remove() {
    const data = useData();
    const {close, done, setPrimaryAction, setSecondaryAction} = useContainer();
    const locale = useLocale();
    const localizedStrings = useMemo(() => {
        return translations[locale] || translations.en;
    }, [locale]);

    const {getSessionToken} = useSessionToken();

    useEffect(() => {
        setPrimaryAction({
            content: "Remove from plan",
            onAction: async () => {
                const token = await getSessionToken();

                console.log('data', JSON.stringify(data));

                const url = `${baseBackendUrl}v2/remove-plans`;
                const response = await fetch(url, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'appstle-authorisation': token || ''
                    },
                    body: JSON.stringify(data)
                });
                if (!response.ok) {
                    throw new Error('Error in Remove request');
                }
                done();
            },
        });

        setSecondaryAction({
            content: "Cancel",
            onAction: () => close(),
        });
    }, [getSessionToken, close, done, setPrimaryAction, setSecondaryAction]);

    return (
        <>
            <InlineStack vertical spacing="tight">
                <Text size="titleSmall">Are you sure to remove this plan?</Text>
                <Text size="small">
                    Note: This will remove the current product from the selling plan and
                    this will not delete the selling plan.
                </Text>
            </InlineStack>
        </>
    );
}

// 'Edit' mode should modify an existing selling plan.
// Changes should affect other products that have this plan applied.
// [Shopify admin renders this mode inside an app overlay container]
function Edit() {

    let data = useData();
    const {getSessionToken} = useSessionToken();
    let [loading, setLoading] = useState(false);
    let [initialData, setInitialData] = useState({});


    useEffect(async () => {
        setLoading(true);
        const token = await getSessionToken();
        const url = `${baseBackendUrl}v2/subscription-groups/?productId=${data.productId}&variantId=${data.variantId}&sellingPlanGroupId=${data.sellingPlanGroupId}`;

        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                'appstle-authorisation': token || ''
            },
        })

        if (!response.ok) {
            throw new Error('Error in fetching data for edit plan');
        }

        const parsedJson = await response.json();
        setFormState({...formState, ...parsedJson})
        setLoading(false);
    }, [])

    const {close, done} = useContainer();

    let [error, setError] = useState({subscriptionPlans: [{}]});
    let [bannerVisibility, setBannerVisibility] = useState(false);
    let [bannerError, setBannerError] = useState([])
    let [formState, setFormState] = useState(
        {
            groupName: 'Subscribe and Save',
            subscriptionPlans: [{
                planType: "PAY_AS_YOU_GO",
                frequencyCount: '1',
                frequencyInterval: 'DAY',
                frequencyName: '',
                billingFrequencyCount: null,
                billingFrequencyInterval: null,
                specificDayEnabled: false,
                frequencyType: "ON_PURCHASE_DAY",
                specificDayValue: '1',
                cutOff: null,
                minCycles: '1',
                maxCycles: null,
                discountEnabled: false,
                discountEnabled2: false,
                discountOffer: null,
                discountOffer2: null,
                discountType: null,
                discountType2: null,
                afterCycle2: null,
                discountEnabled2Masked: false
            }]
        })

    let subscriptionPlan =
        {
            planType: "PAY_AS_YOU_GO",
            frequencyCount: '1',
            frequencyInterval: 'DAY',
            frequencyName: '',
            billingFrequencyCount: null,
            billingFrequencyInterval: null,
            specificDayEnabled: false,
            frequencyType: "ON_PURCHASE_DAY",
            specificDayValue: '1',
            cutOff: null,
            minCycles: '1',
            maxCycles: null,
            discountEnabled: false,
            discountEnabled2: false,
            discountOffer: null,
            discountOffer2: null,
            discountType: null,
            discountType2: null,
            afterCycle2: null,
            discountEnabled2Masked: false
        }

    useEffect(function () {
        setFormState({...formState, ...initialData})
    }, [initialData])

    useEffect(function () {
        serialiseError(error)
    }, [error])

    useEffect(function () {
        finalValidationBeforeSubmit({...formState})
    }, [formState])

    const addFrequencyPlan = () => {
        let currentSubscritionPlans = [...formState?.['subscriptionPlans']];
        let updatedSubscriptionPlans = [...currentSubscritionPlans, {...subscriptionPlan}];
        setFormState({...formState, subscriptionPlans: updatedSubscriptionPlans});
    }
    const deleteFrequencyPlan = (index) => {
        let updatedSubscriptionPlans = formState?.['subscriptionPlans']?.filter((plan, idx) => index !== idx);
        setFormState({...formState, subscriptionPlans: updatedSubscriptionPlans});
    }

    const checkIfValueIsNumberOrString = (val) => {
        if (typeof val === 'string' || typeof val === 'number') {
            val = String(val);
        } else {
            val = '';
        }
        return val;
    }

    const textFieldValidation = (val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val)
        if (!val || !val?.trim()) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a text.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a text.`
            }
            // setError({...err});
            return {validationError: {...err}, valid: false};

        } else if (val) {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[index]?.[fieldKey]
                    // setError({...errCopy});
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    // setError({...errCopy});
                }
            }
            return {validationError: {...errCopy}, valid: true};
        }

    }

    const numberFieldValidation = (val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        if (isNaN(val.trim())) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should be a number. Please enter a number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should be a number. Please enter a number.`
            }

            // setError({...err});
            return {validationError: err, valid: false};
        } else if (!(val.trim())) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field should not be blank. Please enter a number.`
            }
            // setError({...err})
            return {validationError: err, valid: false};
        } else if (parseInt(val.trim()) <= 0) {
            let err = {...error};
            if (index || index === 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Field value should be greater than 0. Please enter a valid number.`
            } else {
                err[fieldKey] = `"${fieldName}" - Field value should be greater than 0. Please enter a valid number.`
            }
            // setError({...err})
            return {validationError: err, valid: false};
        } else if (val) {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[index]?.[fieldKey]
                    // setError({...errCopy});
                } else {
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    //setError({...errCopy});
                }
            }
            return {validationError: errCopy, valid: true};
        }
    }

    const sameFieldValueValidation = (formState, val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        formState?.['subscriptionPlans']?.forEach(function (plan, idx) {
            if (idx !== index) {
                if (fieldKey === 'frequencyCount') {
                    if (`${plan?.[fieldKey]}${plan?.['frequencyInterval']}` === `${val}${formState?.['subscriptionPlans']?.[index]?.['frequencyInterval']}`) {
                        err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Multiple frequency plans cannot have same frequency value of "${val} ${formState?.['subscriptionPlans']?.[index]?.['frequencyInterval']}".`
                        isErrorSet = true;
                    }
                } else {
                    if (plan[fieldKey]?.trim().split(' ').join('').toLowerCase() === val?.trim().split(' ').join('').toLowerCase()) {
                        err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - Multiple frequency plans cannot have same name as "${val}".`
                        isErrorSet = true;
                    }
                }

            }
        })

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            let errCopy = {...error}
            if (index || index === 0) {
                if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                    delete errCopy?.['subscriptionPlans']?.[fieldKey]
                    // setError({...err});
                } else {
                }
            } else {
                if (fieldKey in errCopy) {
                    delete errCopy[fieldKey]
                    //setError({...err});
                }
            }
            return {validationError: errCopy, valid: true};
        }
    }

    const minValidation = (formState, val, fieldName, maxFieldName, fieldKey, maxFieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (formState['subscriptionPlans'][index]?.[maxFieldKey] &&
            (parseInt(val) > parseInt(formState['subscriptionPlans'][index]?.[maxFieldKey]))) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be less than or equal the "${maxFieldName}".`
            isErrorSet = true
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const maxValidation = (formState, val, fieldName, minFieldName, fieldKey, minFieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (formState['subscriptionPlans'][index]?.[minFieldKey] &&
            (parseInt(val) < parseInt(formState['subscriptionPlans'][index]?.[minFieldKey]))) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be less than or equal the "${minFieldName}".`
            isErrorSet = true
        } else if ((formState['subscriptionPlans'][index]?.['planType'] === "ADVANCED_PREPAID") && (parseInt(val) < 2)) {
            err['subscriptionPlans'][index][fieldKey] = `"${fieldName}" - must be greater than 1 for Advanced prepaid plan type".`
            isErrorSet = true
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }

    }

    const billingPeriodValidation = (formState, val, fieldName, fieldKey, error, index) => {
        val = checkIfValueIsNumberOrString(val);
        let err = {...error};
        let isErrorSet = false;
        if (val) {
            if (formState['subscriptionPlans'][index]?.['frequencyCount'] &&
                (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) >= parseInt(val))) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}": should be greater than the delivery frequency.`
                isErrorSet = true;
            } else if (formState['subscriptionPlans'][index]?.['frequencyCount'] && (parseInt(val) % parseInt(formState['subscriptionPlans'][index]?.['frequencyCount'])) != 0) {
                err['subscriptionPlans'][index][fieldKey] = `"${fieldName}": must be a multiple of the delivery frequency duration.`;
                isErrorSet = true;
            }
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const validatespecificDayValue = (formState, val, fieldName, fieldKey, error, index) => {
        val = parseInt(checkIfValueIsNumberOrString(val));
        let err = {...error};
        let isErrorSet = false;
        if (val) {
            if (
                formState['subscriptionPlans'][index]?.['frequencyInterval']
                && formState['subscriptionPlans'][index]?.['frequencyType']
                && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
                && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH"
                && (val > 31 || val < 1)
            ) {
                err['subscriptionPlans'][index][fieldKey] = `Specific Day is between 1st to 31st`
                isErrorSet = true;
            } else if (
                formState['subscriptionPlans'][index]?.['frequencyInterval']
                && formState['subscriptionPlans'][index]?.['frequencyType']
                && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
                && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
                && (val > 7 || val < 1)
            ) {
                err['subscriptionPlans'][index][fieldKey] = `Specific Day is between 1st to 7th`
                isErrorSet = true;
            }
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const validateCutOff = (formState, val, fieldName, fieldKey, error, index) => {
        val = parseInt(checkIfValueIsNumberOrString(val));
        let err = {...error};
        let isErrorSet = false;
        if (
            formState['subscriptionPlans'][index]?.['frequencyInterval']
            && formState['subscriptionPlans'][index]?.['frequencyType']
            && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
            && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH"
            && (val > (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 31) || val < 1)
        ) {
            err['subscriptionPlans'][index][fieldKey] = `Cutoff cannot be greater than ${(parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 31)} days or less than 1`
            isErrorSet = true;
        } else if (
            formState['subscriptionPlans'][index]?.['frequencyInterval']
            && formState['subscriptionPlans'][index]?.['frequencyType']
            && formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY"
            && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
            && (val > (parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 7) || val < 1)
        ) {
            err['subscriptionPlans'][index][fieldKey] = `Cutoff cannot be greater than ${(parseInt(formState['subscriptionPlans'][index]?.['frequencyCount']) * 7)} days or less than 1`
            isErrorSet = true;
        }

        if (isErrorSet) {
            return {validationError: err, valid: false};
        } else {
            return removeFieldError(error, index, fieldKey)
        }
    }

    const removeFieldError = (error, index, fieldKey) => {
        let errCopy = {...error}
        if (index || index === 0) {
            if (fieldKey in errCopy?.['subscriptionPlans']?.[index]) {
                delete errCopy?.['subscriptionPlans']?.[fieldKey]
                // setError({...err});
            } else {
            }
        } else {
            if (fieldKey in errCopy) {
                delete errCopy[fieldKey]
                //setError({...err});
            }
        }
        return {validationError: errCopy, valid: true};
    }

    const finalValidationBeforeSubmit = (formState) => {
        let finalError = {subscriptionPlans: formState?.['subscriptionPlans']?.map(plan => ({}))};
        const groupNameValidation = textFieldValidation(formState?.['groupName'], 'Subscription Plan Name', 'planTitle', {...finalError});
        finalError = {...finalError, ...(groupNameValidation.validationError)};

        formState?.['subscriptionPlans']?.forEach((plan, index) => {
            Object.keys(plan).forEach((key, idx) => {
                if (key === 'frequencyCount') {
                    let frequencyCountValidation = numberFieldValidation(plan[key], 'Delivery frequency', 'frequencyCount', {...finalError}, index);
                    if (frequencyCountValidation.valid) {
                        frequencyCountValidation = sameFieldValueValidation(formState, plan[key], 'Delivery frequency', 'frequencyCount', {...frequencyCountValidation.validationError}, index)
                    }
                    finalError = {...finalError, ...(frequencyCountValidation.validationError)}
                } else if (key === 'frequencyName') {
                    let frequencyNameValidation = textFieldValidation(plan[key], 'Frequency Plan Name', 'frequencyName', {...finalError}, index);
                    if (frequencyNameValidation.valid) {
                        frequencyNameValidation = sameFieldValueValidation(formState, plan[key], 'Frequency Plan Name', 'frequencyName', {...frequencyNameValidation.validationError}, index)
                    }
                    finalError = {...finalError, ...(frequencyNameValidation.validationError)}
                } else if (plan?.['discountEnabled'] && key === 'discountOffer') {
                    let discountOfferValidation = numberFieldValidation(plan?.[key], 'Discount Offer', 'discountOffer', {...finalError}, index);
                    finalError = {...finalError, ...(discountOfferValidation?.validationError)}
                } else if (plan?.['discountEnabled2'] && key === 'discountOffer2') {
                    if (parseInt(plan?.[key]) !== 0) {
                        let discountOfferValidation = numberFieldValidation(plan?.[key], 'After Cycle Discount Offer', 'discountOffer2', {...finalError}, index);
                        finalError = {...finalError, ...(discountOfferValidation?.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'discountOffer2');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (plan?.['discountEnabled2'] && key === 'afterCycle2') {
                    let afterCycleValidation = numberFieldValidation(plan?.[key], 'After Cycle', 'afterCycle2', {...finalError}, index);
                    finalError = {...finalError, ...(afterCycleValidation?.validationError)}
                } else if (key === 'minCycles') {
                    if (formState['subscriptionPlans'][index]?.['minCycles'] != null) {
                        let minCyclesValidation = numberFieldValidation(plan?.[key], 'Minimum number of Orders', 'minCycles', {...finalError}, index);
                        if (minCyclesValidation.valid) {
                            minCyclesValidation = minValidation(
                                formState,
                                parseInt(formState['subscriptionPlans'][index]?.['minCycles']),
                                'Minimum number of Orders',
                                'Maximum number of Orders',
                                'minCycles',
                                'maxCycles',
                                {...finalError},
                                index)
                        }
                        finalError = {...finalError, ...(minCyclesValidation.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'minCycles');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === 'maxCycles') {
                    if (formState['subscriptionPlans'][index]?.['maxCycles'] != null) {
                        let maxCyclesValidation = numberFieldValidation(plan?.[key], 'Maximum number of Orders', 'maxCycles', {...finalError}, index);
                        if (maxCyclesValidation.valid) {
                            maxCyclesValidation = maxValidation(
                                formState,
                                parseInt(formState['subscriptionPlans'][index]?.['maxCycles']),
                                'Minimum number of Orders',
                                'Maximum number of Orders',
                                'maxCycles',
                                'minCycles',
                                {...finalError},
                                index)
                        }
                        finalError = {...finalError, ...(maxCyclesValidation.validationError)}
                    } else {
                        let validation = removeFieldError(finalError, index, 'maxCycles');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === 'billingFrequencyCount') {
                    if (formState['subscriptionPlans'][index]?.['planType'] != "PAY_AS_YOU_GO") {
                        if (formState['subscriptionPlans'][index]?.['billingFrequencyCount'] != null) {
                            let billingFrequencyCountValidation = numberFieldValidation(plan?.[key], 'Billing Period', 'billingFrequencyCount', {...finalError}, index);
                            if (billingFrequencyCountValidation.valid) {
                                billingFrequencyCountValidation = billingPeriodValidation(formState, plan[key], 'Billing Period', 'billingFrequencyCount', {...billingFrequencyCountValidation.validationError}, index)
                            }
                        } else {
                            let validation = removeFieldError(finalError, index, 'billingFrequencyCount');
                            finalError = {...finalError, ...(validation.validationError)}
                        }
                    }

                } else if (key === "specificDayValue") {
                    if (formState['subscriptionPlans'][index]?.['specificDayValue'] != null) {
                        let specificDayValueValidation = numberFieldValidation(plan?.[key], 'Specific Day value', 'specificDayValue', {...finalError}, index);
                        if (specificDayValueValidation.valid) {
                            specificDayValueValidation = validatespecificDayValue(formState, plan[key], 'Specific Day value', 'specificDayValue', {...specificDayValueValidation.validationError}, index)
                        }
                    } else {
                        let validation = removeFieldError(finalError, index, 'specificDayValue');
                        finalError = {...finalError, ...(validation.validationError)}
                    }
                } else if (key === "cutOff") {
                    if (formState['subscriptionPlans'][index]?.['specificDayValue'] && formState['subscriptionPlans'][index]?.['specificDayEnabled']) {
                        if (formState['subscriptionPlans'][index]?.['cutOff'] != null) {
                            let cutOffValidation = numberFieldValidation(plan?.[key], 'Cutoff', 'cutOff', {...finalError}, index);
                            if (cutOffValidation.valid) {
                                cutOffValidation = validateCutOff(formState, plan[key], 'Cutoff', 'cutOff', {...cutOffValidation.validationError}, index)
                            }
                        } else {
                            let validation = removeFieldError(finalError, index, 'cutOff');
                            finalError = {...finalError, ...(validation.validationError)}
                        }
                    }
                }
            })
        })
        setError({...finalError})
        return finalError;
    }

    const serialiseError = (errors) => {
        let serialisedError = [];
        errors && Object.keys(errors).forEach(key => {
            if (typeof errors[key] !== 'object') {
                let trimmedSerialisedObject = serialisedError?.map(err => err?.trim().split(' ').join('').toLowerCase())
                if (trimmedSerialisedObject.indexOf(errors[key]?.trim().split(' ').join('').toLowerCase()) === -1) {
                    serialisedError.push(errors[key])
                }
            } else {
                errors[key] && errors[key]?.forEach((errObj, index) => {
                    errObj && Object.keys(errObj).forEach(errKey => {
                        let trimmedSerialisedObject = serialisedError?.map(err => err?.trim().split(' ').join('').toLowerCase())
                        if (trimmedSerialisedObject.indexOf(errObj[errKey]?.trim().split(' ').join('').toLowerCase()) === -1) {
                            serialisedError.push(errObj[errKey])
                        }
                    });
                })
            }
        })
        setBannerError(serialisedError);
        return serialisedError;
    }

    const onPrimaryAction = useCallback(async () => {
        const finalError = finalValidationBeforeSubmit({...formState})
        const finalSeriealisedError = serialiseError({...finalError})
        if (finalSeriealisedError && (finalSeriealisedError?.length > 0)) {
            setBannerVisibility(true);
            return;
        } else {
            setBannerVisibility(false);
        }
        setLoading(true);
        const token = await getSessionToken();

        data = {
            ...data,
            ...formState
        }

        // TODO: enter a correct url
        const url = `${baseBackendUrl}v2/subscription-groups`;
        const response = await fetch(url, {
            method: true ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json',
                'appstle-authorisation': token || ''
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            setLoading(false);
            throw new Error('Error in submitting plan data.');
        }
        done();
        setLoading(false);
    }, [getSessionToken, done, data, formState, error, bannerVisibility]);


    const cachedActions = useMemo(
        () => (
            <Actions onPrimary={onPrimaryAction} onClose={close} title="Save plan"/>
        ),
        [onPrimaryAction, close]
    );

    const updateFormState = (formState, fieldName, val, index) => {
        formState = updatePlanValues(formState, fieldName, val, index)
        setFormState({...formState})
    }

    const updatePlanValues = (formState, fieldName, val, index) => {
        formState = JSON.parse(JSON.stringify(formState))
        let subscriptionPlansClone = [...formState?.['subscriptionPlans']];
        let currentFreqPlan = {...subscriptionPlansClone[index]}
        currentFreqPlan[fieldName] = val;
        subscriptionPlansClone[index] = {...currentFreqPlan};
        return {...formState, subscriptionPlans: [...subscriptionPlansClone]}
    }

    return (
        <>
            {/* FOR TESTING PURPOSE ONLY DONOT UNCOMMENT
        <Text>-----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(formState)}</Text>
        <Text>----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(error)}</Text>
        <Text>-----------------------------------------------------------------</Text>
        <Text>{JSON.stringify(bannerError)}</Text>
        <Text>-----------------------------------------------------------------</Text>
            FOR TESTING PURPOSE ONLY DONOT UNCOMMENT */}

            {loading &&
            <InlineStack vertical distribution="center" alignment="center">
                <Spinner accessibilityLabel="Loading..." size="large"/>
                <Text>Please wait. Loading.</Text>
            </InlineStack>
            }
            {!loading &&
            <>
                {(bannerVisibility) &&
                <Banner
                    status={bannerError?.length > 0 ? 'critical' : 'success'}
                    title={bannerError?.length > 0 ? "Please resolve errors before submitting" : 'You are all set'}
                    onDismiss={() => setBannerVisibility(false)}>
                    {/* {bannerError?.map((err) => {
                    return (
                        <Text size="medium" color="warning">{err}</Text>
                    )
                })} */}
                </Banner>}
                <Card sectioned title="Edit Subscription Plan">
                    <InlineStack distribution="fill">
                        <TextField
                            label="Subscription Plan Name"
                            error={error?.['planTitle']}
                            value={checkIfValueIsNumberOrString(formState?.groupName)}
                            onChange={(val) => {
                                setFormState({...formState, groupName: val})
                            }}
                            placeholder="Enter a text for Subscription plan name"
                        />
                    </InlineStack>
                </Card>
                {formState?.['subscriptionPlans']?.map((plan, index) => {
                    return (
                        <Card sectioned>
                            <BlockStack vertical spacing="loose">

                                <StackItem>
                                    <InlineStack distribution="trailing">
                                        {((formState?.['subscriptionPlans']?.length - 1) === index) && (
                                            <StackItem>
                                                <Button title={'Add more plan'}
                                                        onPress={addFrequencyPlan}
                                                        kind="primary"/>
                                            </StackItem>)}
                                        {formState?.['subscriptionPlans']?.length > 1 && (
                                            <StackItem>
                                                <Button title="Delete"
                                                        onPress={() => deleteFrequencyPlan(index)}
                                                        kind="primary"
                                                        appearance="critical"/>
                                            </StackItem>)}
                                    </InlineStack>
                                </StackItem>

                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <Select
                                            label="Plan Type"
                                            value={checkIfValueIsNumberOrString(plan?.planType)}
                                            onChange={(val) => {
                                                let formStateClone = JSON.parse(JSON.stringify(formState))
                                                if (val === "PREPAID") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", 1, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", null, index)
                                                } else if (val === "PAY_AS_YOU_GO") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", 1, index)
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", null, index)
                                                } else if (val === "ADVANCED_PREPAID") {
                                                    formStateClone = updatePlanValues(formStateClone, "maxCycles", 2, index)
                                                    formStateClone = updatePlanValues(formStateClone, "minCycles", null, index)
                                                }
                                                formStateClone = updatePlanValues(formStateClone, "planType", (val ? String(val)?.trim() : ""), index)
                                                setFormState({...formStateClone})
                                            }}
                                            options={[
                                                {
                                                    label: "Pay As You Go",
                                                    value: "PAY_AS_YOU_GO"
                                                },
                                                {
                                                    label: "Prepaid",
                                                    value: "PREPAID"
                                                },
                                                {
                                                    label: "Advanced Prepaid",
                                                    value: "ADVANCED_PREPAID"
                                                }
                                            ]}
                                        />
                                    </InlineStack>
                                </StackItem>

                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <Select
                                            label={formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "Fulfillment frequency" : "Order frequency"}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyCount)}
                                            onChange={(val) => {
                                                updateFormState(formState, "frequencyCount", (val ? String(val)?.trim() : ""), index)
                                            }}
                                            options={[...Array(100).keys()].map(i => ({
                                                label: `${i + 1}`,
                                                value: `${i + 1}`
                                            }))}
                                        />
                                        <Select
                                            label="Select delivery frequency type"
                                            options={frequencyOptions}
                                            onChange={(val) => {
                                                let formStateClone = JSON.parse(JSON.stringify(formState))
                                                if (formState['subscriptionPlans'][index]?.['billingFrequencyCount']) {
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", val, index);
                                                }
                                                formStateClone = updatePlanValues(formStateClone, "frequencyInterval", val, index);
                                                formStateClone = updatePlanValues(formStateClone, "specificDayEnabled", false, index);
                                                formStateClone = updatePlanValues(formStateClone, "specificDayValue", null, index);
                                                formStateClone = updatePlanValues(formStateClone, "frequencyType", "ON_PURCHASE_DAY", index);
                                                formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                setFormState({...formStateClone})

                                            }}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyInterval)}
                                        />
                                    </InlineStack>
                                </StackItem>
                                {formState['subscriptionPlans'][index]?.['planType'] != "PAY_AS_YOU_GO" && <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label="Billing Period"
                                            error={error?.['subscriptionPlans']?.[index]?.['billingFrequencyCount']}
                                            value={checkIfValueIsNumberOrString(plan?.billingFrequencyCount)}
                                            onChange={(val) => {
                                                val = val ? String(val)?.trim() : null;
                                                if (val) {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", val, index);
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", formState['subscriptionPlans'][index]?.['frequencyInterval'], index);
                                                    setFormState({...formStateClone})
                                                } else {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyCount", null, index);
                                                    formStateClone = updatePlanValues(formStateClone, "billingFrequencyInterval", null, index);
                                                    setFormState({...formStateClone})
                                                }
                                            }}
                                        />
                                        <Select
                                            label="Select Billing frequency type"
                                            readonly
                                            options={[
                                                {
                                                    label: formState['subscriptionPlans'][index]?.['frequencyInterval'],
                                                    value: formState['subscriptionPlans'][index]?.['frequencyInterval'],
                                                }]}
                                            value={checkIfValueIsNumberOrString(plan?.billingFrequencyInterval)}
                                        />
                                    </InlineStack>
                                </StackItem>}
                                {(formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH" ||
                                    formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK") &&
                                <>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Checkbox
                                                label={formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "Want to set billing date?" : "Want to set order date?"}
                                                onChange={(val) => {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayEnabled", val, index);
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayValue", null, index);
                                                    formStateClone = updatePlanValues(formStateClone, "frequencyType", "ON_PURCHASE_DAY", index);
                                                    formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                    setFormState({...formStateClone})
                                                }}
                                                value={plan?.specificDayEnabled}
                                            />
                                        </InlineStack>
                                    </StackItem>
                                    {formState['subscriptionPlans'][index]?.['specificDayEnabled'] &&
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Select
                                                label="Select Option"
                                                value={checkIfValueIsNumberOrString(plan?.frequencyType)}
                                                onChange={(val) => {
                                                    let defaultSpecificDayValue = null
                                                    //TODO: Review here below condition remove the week wala condition
                                                    //  && formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK"
                                                    if (val === "ON_SPECIFIC_DAY") {
                                                        defaultSpecificDayValue = 1;
                                                    }
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    formStateClone = updatePlanValues(formStateClone, "frequencyType", (val ? String(val)?.trim() : ""), index);
                                                    formStateClone = updatePlanValues(formStateClone, "specificDayValue", defaultSpecificDayValue, index);
                                                    formStateClone = updatePlanValues(formStateClone, "cutOff", null, index);
                                                    setFormState({...formStateClone})
                                                }}
                                                options={[
                                                    {
                                                        label: " On Purchase Day",
                                                        value: "ON_PURCHASE_DAY"
                                                    },
                                                    {
                                                        label: " On Specific Day",
                                                        value: "ON_SPECIFIC_DAY"
                                                    },
                                                ]}
                                            />
                                            {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                            formState['subscriptionPlans'][index]?.['frequencyInterval'] === "WEEK" &&
                                            <Select
                                                label="Choose Day"
                                                options={weeksOption}
                                                onChange={(val) => {
                                                    updateFormState(formState, "specificDayValue", (val ? String(val)?.trim() : ""), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.specificDayValue)}
                                            />}
                                            {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                            formState['subscriptionPlans'][index]?.['frequencyInterval'] === "MONTH" &&
                                            <TextField
                                                label="Enter Day"
                                                error={error?.['subscriptionPlans']?.[index]?.['specificDayValue']}
                                                onChange={(val) => {
                                                    updateFormState(formState, "specificDayValue", (val ? String(val)?.trim() : null), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.specificDayValue)}
                                            />}
                                        </InlineStack>
                                    </StackItem>}

                                    {formState['subscriptionPlans'][index]?.['frequencyType'] === "ON_SPECIFIC_DAY" &&
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <TextField
                                                label="Cutoff"
                                                error={error?.['subscriptionPlans']?.[index]?.['cutOff']}
                                                onChange={(val) => {
                                                    updateFormState(formState, "cutOff", (val ? String(val)?.trim() : null), index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.cutOff)}
                                            />
                                        </InlineStack>
                                    </StackItem>}
                                </>}
                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label="Frequency Plan Name"
                                            error={error?.['subscriptionPlans']?.[index]?.['frequencyName']}
                                            value={checkIfValueIsNumberOrString(plan?.frequencyName)}
                                            onChange={(val) => {
                                                updateFormState(formState, "frequencyName", (val ? String(val)?.trim() : ""), index)
                                            }}
                                            placeholder="Enter text for Frequency Plan Name"
                                        />
                                    </InlineStack>
                                </StackItem>
                                {formState['subscriptionPlans'][index]?.['planType'] != "PREPAID" &&
                                <StackItem>
                                    <InlineStack distribution="fill">
                                        <TextField
                                            label={`Minimum number of ${formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "billing Iterations" : "Orders"}`}
                                            error={error?.['subscriptionPlans']?.[index]?.['minCycles']}
                                            value={checkIfValueIsNumberOrString(plan?.minCycles)}
                                            onChange={(val) => {
                                                updateFormState(formState, "minCycles", (val ? String(val)?.trim() : null), index)
                                            }}
                                        />
                                        <TextField
                                            label={`Maximum number of ${formState['subscriptionPlans'][index]?.['planType'] != 'PAY_AS_YOU_GO' ? "billing Iterations" : "Orders"}`}
                                            error={error?.['subscriptionPlans']?.[index]?.['maxCycles']}
                                            onChange={(val) => {
                                                updateFormState(formState, "maxCycles", (val ? String(val)?.trim() : null), index);
                                            }}
                                            value={checkIfValueIsNumberOrString(plan?.maxCycles)}
                                        />
                                    </InlineStack>
                                </StackItem>}
                                <StackItem>
                                    {!formState['subscriptionPlans'][index]?.['discountEnabled2Masked'] ?
                                        <InlineStack distribution="fill">
                                            <Checkbox
                                                label="Enable Discount"
                                                onChange={(val) => {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    let discountEnabled = formStateClone?.['subscriptionPlans']?.[index]?.['discountEnabled']
                                                    let discountType = formStateClone?.['subscriptionPlans']?.[index]?.['discountType']
                                                    formStateClone = updatePlanValues(formStateClone, "discountEnabled", !discountEnabled, index)
                                                    if (!discountEnabled && !discountType) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountType", 'PERCENTAGE', index)
                                                    } else if (discountEnabled) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountOffer", null, index)
                                                        formStateClone = updatePlanValues(formStateClone, "discountType", null, index)
                                                    }
                                                    formStateClone = updatePlanValues(formStateClone, "discountOffer2", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "discountType2", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "afterCycle2", null, index)
                                                    formStateClone = updatePlanValues(formStateClone, "discountEnabled2", false, index)
                                                    setFormState({...formStateClone})
                                                }}
                                                value={plan?.discountEnabled}
                                            />
                                        </InlineStack> : <Text>Discount Enabled: </Text>}
                                </StackItem>
                                {plan?.discountEnabled &&

                                (<>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <TextField
                                                label="Discount Offer"
                                                error={error?.['subscriptionPlans']?.[index]?.['discountOffer']}
                                                value={checkIfValueIsNumberOrString(plan?.discountOffer)}
                                                onChange={(val) => {
                                                    updateFormState(formState, "discountOffer", (val ? String(val)?.trim() : ""), index)
                                                }}
                                                placeholder="Enter a number to offer discount"
                                            />
                                        </InlineStack>
                                    </StackItem>
                                    <StackItem>
                                        <InlineStack distribution="fill">
                                            <Select
                                                label="Discount Type"
                                                options={discountTypeOptions}
                                                onChange={(val) => {
                                                    updateFormState(formState, "discountType", val, index)
                                                }}
                                                value={checkIfValueIsNumberOrString(plan?.discountType)}
                                            />
                                        </InlineStack>
                                    </StackItem>
                                </>)
                                }
                                {plan?.discountEnabled &&
                                <>
                                    {!formState['subscriptionPlans'][index]?.['discountEnabled2Masked'] ? <StackItem>
                                        <InlineStack distribution="fill">
                                            <Checkbox
                                                label="Change Discount After"
                                                onChange={(val) => {
                                                    let formStateClone = JSON.parse(JSON.stringify(formState))
                                                    let discountEnabled = formStateClone?.['subscriptionPlans']?.[index]?.['discountEnabled2']
                                                    let discountType = formStateClone?.['subscriptionPlans']?.[index]?.['discountType2']
                                                    formStateClone = updatePlanValues(formStateClone, "discountEnabled2", !discountEnabled, index)
                                                    if (!discountEnabled && !discountType) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountType2", 'PERCENTAGE', index)
                                                        formStateClone = updatePlanValues(formStateClone, "afterCycle2", '1', index)
                                                    } else if (discountEnabled) {
                                                        formStateClone = updatePlanValues(formStateClone, "discountOffer2", null, index)
                                                        formStateClone = updatePlanValues(formStateClone, "discountType2", null, index)
                                                        formStateClone = updatePlanValues(formStateClone, "afterCycle2", null, index)
                                                    }
                                                    setFormState({...formStateClone})
                                                }}
                                                value={plan?.discountEnabled2}
                                            />
                                        </InlineStack>
                                    </StackItem> : <Text>Further discount enabled: </Text>}
                                    {plan?.discountEnabled2 &&

                                    (<>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <TextField
                                                    label="After Cycle"
                                                    error={error?.['subscriptionPlans']?.[index]?.['afterCycle2']}
                                                    value={checkIfValueIsNumberOrString(plan?.afterCycle2)}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "afterCycle2", (val ? String(val)?.trim() : null), index)
                                                    }}
                                                    placeholder="Enter a number to offer discount"
                                                />
                                            </InlineStack>
                                        </StackItem>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <TextField
                                                    label="Discount Offer"
                                                    error={error?.['subscriptionPlans']?.[index]?.['discountOffer2']}
                                                    value={checkIfValueIsNumberOrString(plan?.discountOffer2)}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "discountOffer2", (val ? String(val)?.trim() : ""), index)
                                                    }}
                                                    placeholder="Enter a number to offer discount"
                                                />
                                            </InlineStack>
                                        </StackItem>
                                        <StackItem>
                                            <InlineStack distribution="fill">
                                                <Select
                                                    label="Discount Type"
                                                    options={discountTypeOptions}
                                                    onChange={(val) => {
                                                        updateFormState(formState, "discountType2", val, index)
                                                    }}
                                                    value={checkIfValueIsNumberOrString(plan?.discountType2)}
                                                />
                                            </InlineStack>
                                        </StackItem>
                                    </>)
                                    }
                                </>
                                }
                            </BlockStack>
                        </Card>
                    )
                })

                }
                <InlineStack vertical spacing="none">
                    <InlineStack vertical spacing="extraLoose">
                        <StackItem>
                            <InlineStack distribution="trailing">
                                <StackItem>
                                    {cachedActions}
                                </StackItem>
                            </InlineStack>
                        </StackItem>
                    </InlineStack>
                </InlineStack>
            </>
            }
        </>
    )

}

const FrequencyPlans = ({edit, initialData, data, getSessionToken}) => {

}

// Your extension must render all four modes
extend(
    'Admin::Product::SubscriptionPlan::Add',
    render(() => <Add/>)
);
extend(
    'Admin::Product::SubscriptionPlan::Create',
    render(() => <Create/>)
);
extend(
    'Admin::Product::SubscriptionPlan::Remove',
    render(() => <Remove/>)
);
extend(
    'Admin::Product::SubscriptionPlan::Edit',
    render(() => <Edit/>)
);
