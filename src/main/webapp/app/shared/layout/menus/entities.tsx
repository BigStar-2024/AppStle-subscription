import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    {/*
    <MenuItem icon="asterisk" to="/admin/shop-settings">
      Shop Settings
    </MenuItem>*/}
    <MenuItem icon="asterisk" to="/admin/shop-info">
      Shop Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/payment-plan">
      Payment Plan
    </MenuItem>

    {/*<MenuItem icon="asterisk" to="/admin/theme-code">
      Theme Code
    </MenuItem>*/}
    <MenuItem icon="asterisk" to="/admin/theme-settings">
      Theme Settings
    </MenuItem>

    <MenuItem icon="asterisk" to="/admin/plan-info">
      Plan Info
    </MenuItem>

    <MenuItem icon="asterisk" to="/admin/widget-template">
      Widget Template
    </MenuItem>

    <MenuItem icon="asterisk" to="/admin/bulk-automation">
      Bulk Automation
    </MenuItem>
    {/*<MenuItem icon="asterisk" to="/admin/cart-widget-settings">
      Cart Widget Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/subscription-group">
      Subscription Plan
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/subscription-contract-details">
      Subscription Contract Details
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/subscription-widget-settings">
      Subscription Widget Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/analytics">
      Analytics
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/subscription-custom-css">
      Subscription Custom Css
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/customer-portal-settings">
      Customer Portal Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/email-template-setting">
      Email Template Setting
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/customer-payment">
      Customer Payment
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/email-template">
      Email Template
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/dunning-management">
      Dunning Management
    </MenuItem>
    <MenuItem icon="asterisk" to="/subscription-contract-settings">
      Subscription Contract Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/subscription-group-plan">
      Subscription Group Plan
    </MenuItem>
    <MenuItem icon="asterisk" to="/delivery-profile">
      Delivery Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-swap">
      Product Swap
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-cycle">
      Product Cycle
    </MenuItem>
    <MenuItem icon="asterisk" to="/member-only">
      Member Only
    </MenuItem>
    <MenuItem icon="asterisk" to="/selling-plan-member-info">
      Selling Plan Member Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/activity-updates-settings">
      Activity Updates Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/subscription-bundling">
      Subscription Bundling
    </MenuItem>
    <MenuItem icon="asterisk" to="/product-info">
      Product Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/sms-template-setting">
      Sms Template Setting
    </MenuItem>
    <MenuItem icon="asterisk" to="/subscription-bundle-settings">
      Subscription Bundle Settings
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/customer-portal-dynamic-script">
      Customer Portal Dynamic Script
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/bundle-dynamic-script">
      Bundle Dynamic Script
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/activity-log">
      Activity Log
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/subscription-billing-attempt">
      Subscription Billing Attempt
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/currency-conversion-info">
      Currency Conversion Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/order-info">
      Order Info
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/bundle-rule">
      Bundle Rule
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/bundle-setting">
      Bundle Setting
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/shop-label">
      Shop Label
    </MenuItem>
     */}
    <MenuItem icon="asterisk" to="/admin/plan-info-discount">
      Plan Info Discount
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/shop-asset-urls">
      Shop Asset Urls
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/appstle-menu-labels">
      Appstle Menu Labels
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/customization">
      Customization
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/shop-customization">
      Shop Customization
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/onboarding-info">
      Onboarding Info
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
