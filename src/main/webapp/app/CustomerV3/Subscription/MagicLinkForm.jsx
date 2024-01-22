import React, {useState, useEffect} from 'react';
import axios from 'axios';
import Loader from "./Loader";

export default function MagicLinkForm(props) {
  const {customerPortalSettingEntity} = props;
const [email, setEmail] = useState("");
const [isEmailValid, setEmailValid] = useState(true);
const [sendEmailInProgress, setSendEmailInProgress] = useState(false);
const [emailInvalidText, setEmailInvalidText] = useState("")
const [emailSuccessMessage, setEmailSuccessMessage] = useState("");
const [showEmailSuccessMessage, setShowEmailSuccessMessage] = useState(false)

const validateEmail = () => {
    return String(email)
    .toLowerCase()
    .match(
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
    );
}

const sendEmail = (event) => {
    event.preventDefault();
    setEmailValid(true);
    setShowEmailSuccessMessage(false);
    if (validateEmail()) {
        setSendEmailInProgress(true)
        axios.get(`/api/subscription-contracts-email-magic-link?email=${encodeURIComponent(email)}&shop=${Shopify.shop}`)
        .then(res => {
            console.log(res.data)
            setSendEmailInProgress(false)
            setShowEmailSuccessMessage(true)
            setEmailSuccessMessage(res.data)
        })
        .catch(err => {
            console.log(err)
            setSendEmailInProgress(false)
            setEmailInvalidText(err?.response?.data?.message)
            setEmailValid(false);
        })
    } else {
        setEmailInvalidText(customerPortalSettingEntity?.validEmailMessageV2 || "Please enter valid email.")
        setEmailValid(false);
    }
    
}
  return (
    <>
      <div className="as-min-h-full as-flex as-items-center as-justify-center as-py-12 as-px-4 sm:as-px-6 lg:as-px-8 as-h-screen">
        <div className="as-max-w-md as-w-full as-space-y-8">
          <div>
            <h2 className="as-mt-6 as-text-center as-text-3xl as-font-extrabold as-text-gray-900 as-magic-link-title">{customerPortalSettingEntity?.retriveMagicLinkTextV2 || "Retrieve Magic Link"}</h2>
            <p className="as-mt-3 as-text-center as-text-sm as-text-gray-500 as-magic-link-description">{customerPortalSettingEntity?.retrieveMagicLinkDescriptionV2 || "Your magic link has expired. Enter your email to get a new link."}</p>
          </div>
          <form className="as-mt-8">
            <div className="as-rounded-md as-shadow-sm -as-space-y-px">
              <div>
                <label htmlFor="email-address" className="as-sr-only">
                 {customerPortalSettingEntity?.emailAddressTextV2 ||  "Email address"}
                </label>
                <input
                  id="email-address"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  className="as-appearance-none as-relative as-block as-w-full as-px-3 as-py-2 as-border as-border-gray-300 as-placeholder-gray-500 as-text-gray-900 as-rounded-md focus:as-outline-none focus:as-ring-indigo-500 focus:as-border-indigo-500 focus:as-z-10 sm:as-text-sm"
                  placeholder="Email address"
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>
            </div>
            {!isEmailValid && <p class="as-mt-2 as-text-sm as-text-red-600">{emailInvalidText}</p>}
            {showEmailSuccessMessage && <p class="as-mt-2 as-text-sm as-text-green-500">{emailSuccessMessage}</p>}


            <div className="as-mt-6">
              <button
                type="submit"
                className="as-group as-relative as-w-full as-flex as-justify-center as-py-2 as-px-4 as-border as-border-transparent as-text-sm as-font-medium as-rounded-md as-text-white as-bg-indigo-600 hover:as-bg-indigo-700 focus:as-outline-none focus:as-ring-2 focus:as-ring-offset-2 focus:as-ring-indigo-500"
                onClick={(e) => sendEmail(e)}
              >
                {!sendEmailInProgress ? customerPortalSettingEntity?.emailMagicLinkTextV2 || "Email Magic Link" : <Loader/>}
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  )
}
