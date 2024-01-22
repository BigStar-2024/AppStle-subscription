import './header.scss';

import React, { useState } from 'react';

import { Navbar, Nav, NavbarToggler, NavbarBrand, Collapse, NavItem, NavLink, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from './header-components';
import { AdminMenu, EntitiesMenu, AccountMenu } from '../menus';
import { faMoneyBill } from '@fortawesome/free-solid-svg-icons';
import Axios from 'axios';
import { toast } from 'react-toastify';
import { ImportCSVMenu } from '../menus/import-csv';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isSwaggerEnabled: boolean;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">Development</a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  const generateCustomerPortalHtml =() => {
    Axios.get("/api/miscellaneous/generate-customer-portal-html")
          .then((res)=> toast.success("Successfully generated the portal"))
          .catch(err => toast.error("Something error occured"))
  }
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Navbar dark expand="sm" fixed="top" className="jh-navbar">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ml-auto" navbar>
            {props.isAuthenticated && (
                <a href="/" className="btn btn-primary">Dashboard</a>
            )}

            <Home />

            {props.isAuthenticated && props.isAdmin && (
              <>
              <Button onClick={generateCustomerPortalHtml}>Generate Customer Portal</Button>
              <NavItem>
              <NavLink tag={Link} to="/admin/auto-subscription-plan" className="d-flex align-items-center">
                <FontAwesomeIcon icon={faMoneyBill} />
                <span>Auto Subscription Plan</span>
              </NavLink>
            </NavItem>
            </>
            )}
            {/*props.isAuthenticated && props.isAdmin &&
            <NavItem>
              <NavLink tag={Link} to="/admin/liquid-editor" className="d-flex align-items-center">
                <FontAwesomeIcon icon="home"/>
                <span>Liquid Editor</span>
              </NavLink>
            </NavItem>
            */}
            {props.isAuthenticated && props.isAdmin &&
              <NavItem>
                <NavLink tag={Link} to="/admin/lock-billing-plan" className="d-flex align-items-center">
                  <FontAwesomeIcon icon="lock"/>
                  <span>Lock Billing Plan</span>
                </NavLink>
              </NavItem>
            }
            {props.isAuthenticated && props.isAdmin && <ImportCSVMenu />}
            {props.isAuthenticated && props.isAdmin && <EntitiesMenu />}
            {/* {props.isAuthenticated && props.isAdmin && (
              <AdminMenu showSwagger={props.isSwaggerEnabled} showDatabase={!props.isInProduction} />
            )} */}
            <AccountMenu isAuthenticated={props.isAuthenticated} />
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
