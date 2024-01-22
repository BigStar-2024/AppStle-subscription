import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { THEME_APP_EXTENSION_HANDLE, THEME_APP_EXTENSION_UUID } from 'app/config/constants';
import { Announcement as AnnouncementIcon } from '@mui/icons-material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faExternalLinkAlt } from '@fortawesome/free-solid-svg-icons';
import { Container, Card, CardBody, Button } from 'reactstrap';
import { OnboardingPage } from '../OnboardingExperience';

const OnboardingWidgetSetup: OnboardingPage = ({ goToNextStep }) => {
  const themeSettingsShop = useSelector((state: IRootState) => state.themeSettings.entity?.shop);

  const [isAppEmbedded, setIsAppEmbedded] = useState(false);

  function goToShopify() {
    const shopifyUrl = `https://${themeSettingsShop}/admin/themes/current/editor?context=apps&appEmbed=gid://shopify/OnlineStoreThemeAppEmbed/${THEME_APP_EXTENSION_HANDLE}?app_embed_uuid=${THEME_APP_EXTENSION_UUID}`;
    window.open(shopifyUrl, '_blank');
  }

  const verifyAppEmbed = useCallback(() => {
    const fetchAssetKey = async () => {
      try {
        const res = await axios.get('api/asset-key?assetKey=config/settings_data.json');
        const assetData = JSON.stringify(res.data);
        if (assetData.includes('appstle-subscription')) {
          for (let key of Object.keys(res.data.current.blocks)) {
            if (res.data.current.blocks[key]?.['type'].includes('appstle-subscription') && !res.data.current.blocks[key]?.['disabled']) {
              setIsAppEmbedded(true);
              return;
            }
          }
        }
        setIsAppEmbedded(false);
        setTimeout(fetchAssetKey, 3000);
      } catch (error) {
        console.error(error);
      }
    };
    fetchAssetKey();
  }, []);

  useEffect(() => {
    verifyAppEmbed();
  }, []);

  return (
    <Container className="d-flex justify-content-center align-items-center py-5 h-100" style={{ maxWidth: '768px' }}>
      <Card>
        <CardBody>
          <h3>Go Live with the Appstle Subscription Widget</h3>
          <p>
            To go live with your subscriptions and allow customer to subscribe, you need to enable{' '}
            <strong style={{ whiteSpace: 'nowrap' }}>Appstle's embedded block</strong> on your live theme. This enables us to configure the
            Subscription widget on your product pages when products are set up for a subscription plan.
          </p>
          <br />
          <p>
            <b>Follow these steps:</b>
          </p>
          <p>
            <span className="text-primary font-weight-bold">Step 1: </span>
            Click the "<strong>Go to Shopify</strong>" button below to open up to the live theme editor for your store.
          </p>
          <p>
            <span className="text-primary font-weight-bold">Step 2: </span>
            On the left-hand side, <strong>toggle ON</strong> the Appstle Subscriptions app embed.
          </p>
          <p>
            <span className="text-primary font-weight-bold">Step 3: </span>
            In the top, right-hand corner, click "<strong>Save</strong>" to save your changes.
          </p>
          <p>
            <span className="text-primary font-weight-bold">Success! </span>
            That's it! You're ready to start selling subscriptions with Appstle Subscriptions!
          </p>
          <br />
          <p>
            Feel free to{' '}
            <a
              href="https://apps.shopify.com/subscriptions-by-appstle"
              target="_blank"
            >
              visit our demo store
            </a>{' '}
            to check out all the features we offer in Appstle Subscriptions!
          </p>
          <p className="font-weight-bold">Go to Shopify to enable your widget:</p>
          <div className="d-flex" style={{ gap: '1rem' }}>
            <Button color="primary" size="lg" onClick={goToShopify}>
              Go to Shopify <FontAwesomeIcon icon={faExternalLinkAlt} style={{ width: '1rem', height: '1rem' }} />
            </Button>
            <Button
              color={isAppEmbedded ? 'success' : 'secondary'}
              size="lg"
              outline={!isAppEmbedded}
              disabled={!isAppEmbedded}
              onClick={goToNextStep}
            >
              <strong>{isAppEmbedded ? 'Start Selling!' : 'Widget not enabled yet...'}</strong>
            </Button>
          </div>
          <p className="text-muted mb-0 mt-3">
            <AnnouncementIcon fontSize="small" /> Be sure to check out the extensive widget customizations available on the widget settings
            page.
          </p>
        </CardBody>
      </Card>
    </Container>
  );
};

export default OnboardingWidgetSetup;
