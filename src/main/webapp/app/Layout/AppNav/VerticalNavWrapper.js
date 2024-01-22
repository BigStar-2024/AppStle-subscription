import { Redirect } from '@shopify/app-bridge/actions';
import React, { useState, Fragment } from 'react';
import MetisMenu from 'react-metismenu';
import { Link, withRouter } from 'react-router-dom';
import { SubscriptionMenu } from './NavItems';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function CustomLink(props) {
  const [showDemoStoreModal, setShowDemoStoreModal] = useState(false);
  const toggleDemoStoreModal = () => setShowDemoStoreModal(!showDemoStoreModal);

  const removeActiveMenu = (arr, nestingKey) => {
    if (arr) {
      arr.map(item => {
        if (item.to !== 'header') {
          let searchObj = document.getElementById(item.label.replace(' ', '_'));
          searchObj.classList.remove('active');
        }

        if (item[nestingKey]) removeActiveMenu(item[nestingKey], nestingKey);
      });
    }
  };

  const onClick = e => {
    if (props.label === 'Become an affiliate') {
      let appUrl = 'https://appstle.firstpromoter.com/';
      if (window.app) {
        Redirect.create(app).dispatch(Redirect.Action.REMOTE, {
          url: appUrl,
          newContext: true
        });
      } else {
        window.open(appUrl, '_blank');
      }
    } else if (props.label === 'Demo Store') {
      e.preventDefault();
      toggleDemoStoreModal();
    } else if (props.externalLink) {
      e.preventDefault();
      window.open(props.to, '_blank');
    } else {
      if (props.hasSubMenu) props.toggleSubMenu(e);
      else {
        // Your own operations using "to"
        // myGotoFunc(props.to);
        // or
        // props.activateMe(/* Parameters to pass "onSelected" event listener
        removeActiveMenu(SubscriptionMenu, 'content');
        props.activateMe({
          newLocation: props.to,
          selectedMenuLabel: props.label
        });
      }
    }
  };

  if (props?.to === 'header') {
    return <h5 className="app-sidebar__heading">{props.label}</h5>;
  } else {
    return (
      <Link
        className={`metismenu-link ${props.active ? 'active' : ''}`}
        id={props.label.replace(' ', '_')}
        to={props.label !== 'Become an affiliate' ? props.to : null}
        onClick={onClick}
      >
        <DemoStoreModal showDemoStoreModal={showDemoStoreModal} toggleShowDemoStoreModal={toggleDemoStoreModal} />
        {props.children}
      </Link>
    );
  }
}

function Nav(props) {
  const isPathActive = path => {
    return props.location.pathname.startsWith(path);
  };

  return (
    <Fragment>
      <MetisMenu
        content={SubscriptionMenu}
        activeLinkFromLocation
        LinkComponent={CustomLink}
        className="vertical-nav-menu"
        iconNamePrefix=""
        classNameStateIcon="pe-7s-angle-down"
      />
    </Fragment>
  );
}

function DemoStoreModal({ showDemoStoreModal, toggleShowDemoStoreModal }) {
  return (
    <Modal isOpen={showDemoStoreModal} toggle={toggleShowDemoStoreModal}>
      <ModalHeader toggle={toggleShowDemoStoreModal}>Demo Store</ModalHeader>
      <ModalBody>
        <p>
          You can view our demo store to see how Appstle Subscriptions works. The demo store has been configured to showcase the app's
          features.
        </p>
        <img src={require('./demo-store.png')} alt="demo store" width={350} height={300} />
      </ModalBody>
      <ModalFooter>
        <Button color="link" onClick={toggleShowDemoStoreModal}>
          Cancel
        </Button>
        <Button
          color="link"
          onClick={() => {
            window.open('https://apps.shopify.com/subscriptions-by-appstle', '_blank');
            toggleShowDemoStoreModal();
          }}
        >
          Go to Demo Store
        </Button>
      </ModalFooter>
    </Modal>
  );
}

export default withRouter(Nav);
