import React, { useState, useEffect, Fragment, useCallback } from 'react';
import { connect, useSelector } from 'react-redux';
import Loader from 'react-loaders';
import PrdVariantRadioPopup from "../PrdVariantRadioPopup";
import {
  Button,
  Table,
  InputGroup,
  Input,
  Label,
  FormGroup,
  Row,
  Col,
  FormText,
  Card,
  CardBody,
  ListGroup,
  UncontrolledButtonDropdown,
  DropdownItem,
  DropdownMenu,
  DropdownToggle,
  ListGroupItem,
  Pagination,
  PaginationItem,
  PaginationLink,
  Progress
} from 'reactstrap';
import axios from 'axios';
import Switch from 'react-switch';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExternalLinkSquareAlt } from '@fortawesome/free-solid-svg-icons';
import { Field, Form } from 'react-final-form';
import { Link } from 'react-router-dom';
import { JhiItemCount, JhiPagination } from 'react-jhipster';
import { useHistory, useLocation, useParams } from 'react-router';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import { getEntity, removeProduct } from 'app/entities/subscriptions/subscription.reducer';
import { getEntities } from 'app/entities/subscription-contract-details/subscription-contract-details.reducer';
import { toast } from 'react-toastify';
import Select, { components } from 'react-select';
import MySaveButton from 'app/DemoPages/Dashboards/Utilities/MySaveButton';
import Axios from 'axios';


let subscriptionEntitiesCopy;
let subscriptionCount = 0;

const ActiveAllSubscriptionAutomation = props => {
  const [updating, setUpdating] = useState(false);
  const accountInfo = useSelector(state => state.authentication.account)

  const killAutomation = async () => {
    setUpdating(true);
    axios.put(`api/bulk-automation/stop-automation?automationType=${props?.type}`)
    .then(res => {
      props?.getAutomationStatus();
      setUpdating(false);
    })
  }

  return (
    <div className=''>
      <div>
        <p>Status: <b>Running</b></p>
        <Progress animated={true} style={{maxWidth: "300px", width: "100%"}} striped color="success" value="100" />
      </div>
      <MySaveButton
        text="Kill Automation"
        updatingText={'Processing'}
        updating ={updating}
        className="btn-primary mt-3"
        onClick={killAutomation}
      >
        Kill Automation
      </MySaveButton>
    </div>
  );
};

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(ActiveAllSubscriptionAutomation);
