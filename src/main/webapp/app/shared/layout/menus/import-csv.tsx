import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const ImportCSVMenu = props => (
  <NavDropdown icon="th-list" name="Import" id="import-csv-menu">
    <MenuItem icon="asterisk" to="/admin/import-csv">
      Import CSV
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin/import-plan-product-csv">
      Import Plan Product CSV
    </MenuItem>
  </NavDropdown>
);
