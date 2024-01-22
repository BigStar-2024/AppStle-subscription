import React from 'react';
import draftActiveProducts from '../../../../static/theme/assets/utils/images/widget-setting/active_product_setting.png';
import { Alert as ReactStrapAlert } from 'reactstrap';
import { Help } from '@mui/icons-material';
import { styled } from '@mui/material/styles';
import Tooltip, { tooltipClasses } from '@mui/material/Tooltip';

const PhotoTooltip = () => {
  const StyledToolTip = styled(({ className, ...props }) => <Tooltip {...props} classes={{ popper: className }} />)({
    [`& .${tooltipClasses.tooltip}, & .${tooltipClasses.tooltip} div`]: {
      backgroundColor: '#337AB7',
      color: '#fff',
      maxWidth: 500,
      fontSize: 14,
    },
  });

  return (
    <StyledToolTip
      interactive
      placement="right"
      arrow
      enterDelay={0}
      title={
        <div style={{ maxMidth: '500px' }}>
          <img src={draftActiveProducts} width="300px"></img>
        </div>
      }
    >
      <Help style={{ fontSize: '1rem', marginBottom: '3px', marginLeft: '4px' }} />
    </StyledToolTip>
  );
};

export default function ActiveProductAlert() {
  return (
    <>
      <ReactStrapAlert className="appstle_error_box mb-0" color="warning">
        <h6>
          <b>CAUTION - PRODUCTS IN DRAFT STATE</b>
        </h6>
        <p>
          The following of your products are in <b>Draft</b> state of Shopify. You are requested to make them active in order to show them
          on the frontend.
        </p>
        <p>
          In order, to change a product from Draft state to Active state please go to <b>Product</b> section in Shopify and open the product
          by searching and clicking on it’s name and from the <b>Product Status</b> section make sure the product is Active.{' '}
        </p>
        <p>
          <b>Note:</b> Please make sure the in <b>Sales Channels and Apps</b> Online Store is set to green. Please refer to the below
          screenshot for further information.
          <PhotoTooltip />
        </p>
      </ReactStrapAlert>
    </>
  );
}
