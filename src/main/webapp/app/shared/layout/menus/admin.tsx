import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

const adminMenuItems = (
  <>
    <MenuItem icon="user" to="/admin/admin/user-management">
      User management
    </MenuItem>
    <MenuItem icon="tachometer-alt" to="/admin/admin/metrics">
      Metrics
    </MenuItem>
    <MenuItem icon="heart" to="/admin/admin/health">
      Health
    </MenuItem>
    <MenuItem icon="list" to="/admin/admin/configuration">
      Configuration
    </MenuItem>
    <MenuItem icon="bell" to="/admin/admin/audits">
      Audits
    </MenuItem>
    {/* jhipster-needle-add-element-to-admin-menu - JHipster will add entities to the admin menu here */}
    <MenuItem icon="tasks" to="/admin/admin/logs">
      Logs
    </MenuItem>
  </>
);

const swaggerItem = (
  <MenuItem icon="book" to="/admin/admin/docs">
    API
  </MenuItem>
);

const databaseItem = (
  <DropdownItem tag="a" href="./h2-console" target="_tab">
    <FontAwesomeIcon icon="hdd" fixedWidth /> Database
  </DropdownItem>
);

export const AdminMenu = ({ showSwagger, showDatabase }) => (
  <NavDropdown icon="user-plus" name="Administration" style={{ width: '140%' }} id="admin-menu">
    {adminMenuItems}
    {showSwagger && swaggerItem}

    {showDatabase && databaseItem}
  </NavDropdown>
);

export default AdminMenu;
