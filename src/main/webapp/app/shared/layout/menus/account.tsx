import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';

import { NavDropdown } from './menu-components';

const accountMenuItemsAuthenticated = (
  <>
    <MenuItem icon="wrench" to="/admin/account/settings">
      Settings
    </MenuItem>
    <MenuItem icon="lock" to="/admin/account/password">
      Password
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/admin/logout">
      Sign out
    </MenuItem>
  </>
);

const accountMenuItems = (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/admin/login">
      Sign in
    </MenuItem>
    <MenuItem icon="sign-in-alt" to="/admin/account/register">
      Register
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => (
  <NavDropdown icon="user" name="Account" id="account-menu">
    {isAuthenticated ? accountMenuItemsAuthenticated : accountMenuItems}
  </NavDropdown>
);

export default AccountMenu;
