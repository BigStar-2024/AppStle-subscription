import React, { useEffect, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Button, Col, Row } from 'reactstrap';
import { connect } from 'react-redux';
import { ICrudGetAction } from 'react-jhipster';
import { getEntity } from 'app/entities/shop-info/shop-info.reducer';
import { IShopInfo } from 'app/shared/model/shop-info.model';
import IntegrationCard, { IntegrationCardProps } from './IntegrationCard';

import AppstleLoyaltyIntegration from './AppstleLoyalty/AppstleLoyaltyIntegration';
import AppstleMembershipsIntegration from './AppstleMemberships/AppstleMembershipsIntegration';
import LoyaltyLion from 'app/DemoPages/Dashboards/Integrations/LoyaltyLion/LoyaltyLion';
import KlaviyoEmail from './Klaviyo/KlaviyoEmail';
import KlaviyoContactSync from './Klaviyo/KlaviyoContactSync';
import Mailchimp from './Mailchimp/Mailchimp';
import Zapier from './Zapier/Zapier';
import PageFly from './PageFly/PageFly';
import AffiliateMarket from './AffiliateMarket/AffiliateMarket';
import Gorgias from './Gorgias/Gorgias';
import Bundler from './Bundler/bundler';
import PricingbyCountry from './PricingByCountry/index';
import UpPromote from './UpPromote/UpPromote';
import Transcy from './Transcy/Transcy';
import Langify from './Langify/Langify';
import YotpoIntegration from './Yotpo/YotpoIntegration';
import OmnisendContactSync from './Omnisend/OmnisendContactSync';
import SalesNotification from './SalesNotification/SalesNotification';
import ConciergeCustomerAccount from './ConciergeCustomerAccount/ConciergeCustomerAccount';
import FlitsCustomerAccount from './FltizCustomerAccount/FltizCustomerAccount';
import OneClickUpsell from './OneClickUpsell/oneClickUpsell';
import CustomerAccountsHub from './CustomerAccountsHub/CustomerAccountsHub';
import Rebuy from './Rebuy/Rebuy';
import DiscountedPricing from './DiscountedPricing/DiscountedPricing';
import EComposer from './Ecomposer/Ecomposer';
import Zapiet from './Zapiet/Zapiet';
import Mechanic from './Mechanic/Mechanic';
import ShopifyFlow from './ShopifyFlow/ShopifyFlow';
import EberLoyalty from './EberLoyalty/EberLoyalty';
import SkyPilot from './SkyPilot/SkyPilot';
import ShipperHq from './ShipperHq/ShipperHq';
import ReferralCandy from 'app/DemoPages/Dashboards/Integrations/ReferralCandy/ReferralCandy';
import EasyBundles from './EasyBundles/EasyBundles';
import BYOB from './BYOB/BYOB';
import FastBundle from './FastBundle/FastBundle';
import MBCBundle from './MBCBundle/MBCBundle';
import MageNative from './MageNative/MageNative';
import Debutify from './Debutify/Debutify';
import GemPages from './GemPages/GemPages';
import MonsterCart from 'app/DemoPages/Dashboards/Integrations/MonsterCart/MonsterCart';
import UpCart from './UpCart';
import MultiVariants from './MultiVariants';
import Growave from 'app/DemoPages/Dashboards/Integrations/Growave/Growave';
import ShipInsure from './ShipInsure/ShipInsure';

const appIntegrationTags = [
  'appstle',
  'automation',
  'bundling',
  'customerAccounts',
  'customerService',
  'email',
  'loyalty',
  'marketing',
  'pageBuilder',
  'sales',
  'translation',
  'shipping',
  'reviews'
] as const;

type AppIntegrationTag = typeof appIntegrationTags[number];

type AppIntegrationCategory = AppIntegrationTag | 'all' | 'premium';

interface AppIntegration extends IntegrationCardProps {
  tags: Array<AppIntegrationTag>;
}

const BasicIntegrationV2 = (props: { shopInfo: IShopInfo; getEntity: ICrudGetAction<IShopInfo> }) => {
  const { shopInfo } = props;

  const [selectedCategory, setSelectedTag] = useState<AppIntegrationCategory>('all');

  const tagButtonNames: Record<AppIntegrationTag, string> = {
    appstle: 'Appstle',
    automation: 'Automation',
    bundling: 'Bundling',
    customerAccounts: 'Customer Accounts',
    customerService: 'Customer Service',
    email: 'Email',
    loyalty: 'Loyalty',
    marketing: 'Marketing',
    pageBuilder: 'Page Builders',
    sales: 'Sales',
    translation: 'Translation',
    shipping: 'Shipping',
    reviews: 'Product Review'
  };

  //componentDidMount
  useEffect(() => {
    props.getEntity(-1);
  }, []);

  const appIntegrations: Array<AppIntegration> = [
    {
      title: 'Appstle℠ Loyalty and Rewards',
      subtitle: 'Comprehensive & customizable loyalty tool by Appstle℠ with rewards, store credits, & more',
      imageSrc: require('./AppstleLoyalty/appstleLoyalty.png'),
      isShowAsEnabled: shopInfo.appstleLoyaltyApiKey != null,
      isRecommended: true,
      IntegrationModalComponent: AppstleLoyaltyIntegration,
      tags: ['loyalty', 'appstle']
    },
    {
      title: 'Appstle℠ Memberships',
      subtitle:
        'Comprehensive and customizable end to end memberships tool covering plan creation, member perks, tagging, and auto billing',
      imageSrc: require('./AppstleLoyalty/appstleLoyalty.png'),
      isShowAsEnabled: shopInfo.appstleLoyaltyApiKey != null,
      isRecommended: true,
      IntegrationModalComponent: AppstleMembershipsIntegration,
      tags: ['loyalty', 'appstle']
    },
    {
      title: 'PageFly',
      subtitle: 'Enable PageFly integration.',
      imageSrc: require('./PAGEFLY_LOGO_VERTICAL_Blue.png'),
      IntegrationModalComponent: PageFly,
      tags: ['pageBuilder'],
      isRecommended: true
    },
    {
      title: 'Loyalty Lion',
      subtitle: 'Connect your LoyaltyLion account to send points to customer when they subscribe for a product.',
      imageSrc: require('./loyaltylion_logo.png'),
      isShowAsEnabled:
        shopInfo.loyaltyLionToken !== null &&
        shopInfo.loyaltyLionSecret != null &&
        shopInfo.loyaltyLionPoints != null &&
        shopInfo.loyaltyLionEnabled,
      IntegrationModalComponent: LoyaltyLion,
      tags: ['loyalty']
    },
    {
      title: 'Klaviyo Email',
      subtitle: 'Connect your Klaviyo account to send emails through your existing Klaviyo account.',
      imageSrc: require('./klaviyo_logo.png'),
      isShowAsEnabled: shopInfo.klaviyoApiKey != null && shopInfo.klaviyoPublicApiKey != null,
      isRecommended: true,
      IntegrationModalComponent: KlaviyoEmail,
      isPremium: true,
      tags: ['email', 'marketing']
    },
    {
      title: 'Sky Pilot',
      subtitle: 'Sell and deliver music, videos, books, or any digital files instantly, and without limits.',
      imageSrc: require('./sky_Pilot_logo.png'),
      IntegrationModalComponent: SkyPilot,
      tags: ['automation']
    },
    {
      title: 'Klaviyo Contacts Sync',
      subtitle: 'Connect your Klaviyo account.',
      imageSrc: require('./klaviyo_logo.png'),
      isShowAsEnabled: shopInfo.klaviyoApiKey != null && shopInfo.klaviyoListId != null,
      isRecommended: true,
      IntegrationModalComponent: KlaviyoContactSync,
      isPremium: true,
      tags: ['email', 'marketing']
    },
    {
      title: 'MailChimp',
      subtitle: 'Connect your Mailchimp account to send emails through your existing Mailchimp account.',
      imageSrc: require('./mailchimp_logo.png'),
      isShowAsEnabled: shopInfo.mailchimpApiKey != null && shopInfo.mailchimpApiKey.trim().length > 0,
      IntegrationModalComponent: Mailchimp,
      tags: ['email', 'marketing']
    },
    {
      title: 'Zapier',
      subtitle: 'Automate your flow with Zapier integration.',
      imageSrc: require('./zapier_logo.png'),
      IntegrationModalComponent: Zapier,
      isPremium: true,
      tags: ['automation']
    },
    {
      title: 'Affiliate Marketing & Referral',
      subtitle: 'Affiliate marketing, referral, Influencer, Ambassador app',
      imageSrc: require('./AffiliateMarketingReferral.png'),
      IntegrationModalComponent: AffiliateMarket,
      tags: ['marketing']
    },
    {
      title: 'Gorgias',
      subtitle: 'Centralize all your customer service channels in one place.',
      imageSrc: require('./gorgias.png'),
      IntegrationModalComponent: Gorgias,
      isPremium: true,
      tags: ['customerService']
    },
    {
      title: 'Bundler',
      subtitle: 'Bundle products, Mix&Match bundles, Volume discounts!',
      imageSrc: require('./bundler.png'),
      IntegrationModalComponent: Bundler,
      tags: ['bundling', 'sales']
    },
    {
      title: 'Pricing by Country',
      subtitle: 'Multi country pricing, Double Profit, Shipping Rate, Converter',
      imageSrc: require('./PricingByCountry/pricingbyCountry.png'),
      IntegrationModalComponent: PricingbyCountry,
      tags: ['sales', 'translation']
    },
    {
      title: 'UpPromote: Affiliate Marketing',
      subtitle: 'Grow With Ambassador, Influencer, Referral & Affiliate Program',
      imageSrc: require('./UpPromote/UpPromote.png'),
      IntegrationModalComponent: UpPromote,
      tags: ['marketing']
    },
    {
      title: 'Transcy: Language Translate & Currency',
      subtitle: 'Translate Store into Multiple Language, Auto Currency Switcher',
      imageSrc: require('./Transcy/Transcy.png'),
      IntegrationModalComponent: Transcy,
      tags: ['translation']
    },
    {
      title: 'Langify',
      subtitle: 'Translate your shopify store',
      imageSrc: require('./Langify/Langify.png'),
      IntegrationModalComponent: Langify,
      tags: ['translation']
    },
    {
      title: 'Yotpo Loyalty & Rewards',
      subtitle: 'Loyalty, Referrals & Rewards by Yotpo (formerly Swell)',
      imageSrc: require('./Yotpo/yotpoLoyalty.png'),
      isShowAsEnabled: shopInfo.yotpoApiKey != null && shopInfo.yotpoGuid != null && shopInfo.yotpoEnabled,
      IntegrationModalComponent: YotpoIntegration,
      tags: ['loyalty']
    },
    {
      title: 'Omnisend Email Marketing & SMS',
      subtitle: 'Email Marketing, Newsletter, SMS, Abandoned Cart & Pop Up',
      imageSrc: require('./Omnisend/omnisend.png'),
      IntegrationModalComponent: OmnisendContactSync,
      tags: ['email', 'marketing']
    },
    {
      title: 'ToastiBar - Sales Popup App',
      subtitle: 'Boost Sales with Sales Notification, Cart Notification & more',
      imageSrc: require('../Integrations/SalesNotification/salesnotify.png'),
      IntegrationModalComponent: SalesNotification,
      tags: ['sales']
    },
    {
      title: 'Concierge Customer Accounts',
      subtitle: 'Customer account page, Wishlist, Social Login',
      imageSrc: require('../Integrations/ConciergeCustomerAccount/ConciergeCustomerAccount.png'),
      IntegrationModalComponent: ConciergeCustomerAccount,
      tags: ['customerAccounts']
    },
    {
      title: 'Flits: Customer Account Page',
      subtitle: 'Profile, Order History, Reorder, Contact Us, Recently viewed',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/00c55d43f70d82f641a280acf6671b14/icon/CMq7jK-mp_UCEAE=.png',
      IntegrationModalComponent: FlitsCustomerAccount,
      tags: ['customerAccounts']
    },
    {
      title: 'One Click Upsell - Zipify OCU',
      subtitle: 'Build Upsells & Cross Sells - Pre & Post Purchase Funnels :-)',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/aabc0942fb57526ee1fe892775edab52/icon/CPrvkNr0lu8CEAE=.png',
      IntegrationModalComponent: OneClickUpsell,
      tags: ['sales']
    },
    {
      title: 'Customer Accounts Hub',
      subtitle: 'Try the New & Exclusive Mini Account Widget!',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/5d0a1dd0d8941b2b8b888667411d4532/icon/CMnWj4HV1_MCEAE=.png',
      IntegrationModalComponent: CustomerAccountsHub,
      tags: ['customerAccounts']
    },
    {
      title: 'Rebuy Personalization Engine',
      subtitle: 'Personalized Recommendations, Upsell, Cross Sell, Buy it Again',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/9a87fc6fa46c1e06f03627e9ef094b51/icon/CIf4uNGQtfcCEAE=.png',
      IntegrationModalComponent: Rebuy,
      tags: ['sales', 'marketing']
    },
    {
      title: 'Discounted Pricing',
      subtitle: 'Generate more sales with a no-code volume discounted pricing builder',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/1424d3f83c31321aaf26e89ce41ea8f9/icon/CIeD3ar9h_gCEAE=.png',
      IntegrationModalComponent: DiscountedPricing,
      tags: ['sales']
    },
    // {
    //   title: 'Zipify Pages',
    //   subtitle: 'Create Smarter Sales Funnels, Landing Pages, & Product Pages...w/o needing a designer or developer!',
    //   imageSrc: 'https://cdn.shopify.com/app-store/listing_images/030fe401789c6f84cc38805e7965669b/icon/COm5psL0lu8CEAE=.jpg',
    //   IntegrationModalComponent: ZipifyPages,
    //   tags: ['pageBuilder'],
    // },
    {
      title: 'EComposer',
      subtitle: 'Easy & fast page builder: landing page, homepage, product, collection, blog, footer, section & more',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/253c9864fe7cc4ba163511830f085a0f/icon/CIOUnvvPu_gCEAE=.png',
      IntegrationModalComponent: EComposer,
      tags: ['pageBuilder']
    },
    {
      title: 'Mechanic',
      subtitle: '100s of click-to-install automations and a full development platform for custom functionality.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/e9f7304f320df1ee017af1d0bf9c64f4/icon/CKmEoL2ng_sCEAE=.jpeg',
      isShowAsEnabled: shopInfo.mechanicEnabled,
      IntegrationModalComponent: Mechanic,
      isPremium: true,
      tags: ['automation']
    },
    {
      title: 'Shopify Flow',
      subtitle: 'Create the custom workflows that you need to automate tasks in your store and across your apps.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/15100ebca4d221b650a7671125cd1444/icon/CPGqz93P2fYCEAE=.png',
      isShowAsEnabled: shopInfo.shopifyFlowEnabled,
      IntegrationModalComponent: ShopifyFlow,
      isPremium: true,
      tags: ['automation']
    },
    {
      title: 'Eber Loyalty',
      subtitle: 'Loyalty Marketing Platform - Engage and Retain More Customers',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/63828d6c4599dd5d449255406f8e6565/icon/CPXBwurIwfwCEAE=.png',
      isShowAsEnabled: shopInfo.eberLoyaltyApiKey && shopInfo.eberLoyaltyEnabled,
      IntegrationModalComponent: EberLoyalty,
      tags: ['loyalty']
    },
    {
      title: 'ShipperHQ: All‑In‑One Solution',
      subtitle: 'Save More & Sell More! All-in-one solution for managing your shipping experience at checkout.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/bd60c65630e82ba5f8829cdbdaae9c8e/icon/CMrUuuTdgf0CEAE=.png',
      isShowAsEnabled: shopInfo.shipperHqApiKey != null && shopInfo.shipperHqAuthCode != null,
      IntegrationModalComponent: ShipperHq,
      tags: ['shipping']
    },
    {
      title: 'ShipInsure Shipping Insurance',
      subtitle: 'ShipInsure offers package protection for all your orders to prevent them from being lost, damaged, or stolen.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/45c78dcfe5a302500b5dd3b1a43754e4/icon/CO7M_-DMqIEDEAE=.jpeg',
      isShowAsEnabled: shopInfo?.shipInsureEnabled,
      IntegrationModalComponent: ShipInsure,
      tags: ['shipping']
    },
    {
      title: "Referral Candy & Affiliate",
      subtitle: "Create and automate customer referral programs for eCom stores and grow sales through world-of-mouth",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/79887e6752b8e806cc8490d1878d7807/icon/CNLy-4uvpP0CEAE=.png",
      IntegrationModalComponent: ReferralCandy,
      tags: ['sales', 'loyalty', 'automation']
    },
    {
      title: 'Easy Bundles',
      subtitle: 'Increase Average Order value with Easy Bundles by letting your customers build their own single or multi-step.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/10b4272bede142b02924feec498b4009/icon/CMeDtbHryf0CEAE=.png',
      IntegrationModalComponent: EasyBundles,
      tags: ['bundling']
    },
    {
      title: 'Debutify',
      subtitle: 'Debutify Reviews makes collecting, managing, and displaying customer photo and video reviews easy.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/6909b6cca828d68ef5f6c2741022e1c0/icon/CIzkwbmMg_YCEAE=.png',
      IntegrationModalComponent: Debutify,
      tags: ['reviews']
    },
    {
      title: 'MageNative',
      subtitle: 'MageNative makes it easy to build native mobile apps that engage customers & increase sales.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/e3f8ae4316afc50832ec5c0b29596b29/icon/CNWNkMrf9PwCEAE=.png',
      IntegrationModalComponent: MageNative,
      tags: ['bundling']
    },
    {
      title: 'BYOB',
      subtitle: 'Build Your Own Bundles lets you create custom bundles.Your customers can pick and mix products on a single page and subscribe to it directly.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/e8d0781deff4772b55671fbcec6d1204/icon/CPaihPeFme8CEAE=.png',
      IntegrationModalComponent: BYOB,
      tags: ['bundling']
    },
    {
      title: 'FastBundle',
      subtitle: 'FastBundle lets you easily create almost all types of product bundles and combo products to boost your revenue.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/ea091c2ab1d4650a55e0b783691fab16/icon/CKLrhqyVq_wCEAE=.jpeg',
      IntegrationModalComponent: FastBundle,
      tags: ['bundling']
    },
    {
      title: 'MBC Bundle Products',
      subtitle: 'Advanced Bundle Products App which help to Enhance Sales with multiple Upsell and Discount options.',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/f53d91652e213f8e3572120603e67349/icon/CJPPya_0lu8CEAE=.png',
      IntegrationModalComponent: MBCBundle,
      tags: ['bundling']
    },
    {
      title: "GemPages",
      subtitle: "A one-stop storefront customization solution with a Visual Editor and powerful AI-powered features",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/6d72140e25d3f444e352738033321b98/icon/CJCIkdONi_UCEAE=.jpeg",
      IntegrationModalComponent: GemPages,
      tags: ['pageBuilder']
    },
    {
      title: "Monster Cart Upsell+Free Gifts",
      subtitle: "Boost Revenue in minutes... using a Slide Cart Drawer with Unlock Offers & Free Gift Rewards.",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/b0d1f54b1219bacc5e80ab7ce7c7dba5/icon/CLbN_oefsIADEAE=.png",
      IntegrationModalComponent: MonsterCart,
      tags: ["marketing", "sales"]
    },
    {
      title: "UpCart—Cart Drawer Cart Upsell",
      subtitle: "UpCart lets you create a custom, on-brand slide cart with upsells and add-ons to boost AOV.",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/8cce16328fae7303f55e346b8a81ec07/icon/CP--q_PUyv0CEAE=.png",
      IntegrationModalComponent: UpCart,
      tags: ["marketing", "sales"]
    },
    {
      title: "MultiVariants ‑ Bulk Order",
      subtitle: "List variants in a beautiful table/grid layout. Let customers bulk order multiple variants easily.",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/8bd94c23dd747d81fc3d843cfbd14e01/icon/CInQwsL0lu8CEAE=.png",
      IntegrationModalComponent: MultiVariants,
      tags: ["bundling", "sales"]
    },
    {
      title: "Growave: Loyalty & Wishlist",
      subtitle: "Growave replaces several apps: loyalty, referrals, rewards, gift cards, VIP tiers, reviews, wishlist",
      imageSrc: "https://cdn.shopify.com/app-store/listing_images/59da3ad107e08fd5430db47cc4e0a75c/icon/CKq1o6jxo_MCEAE=.png",
      IntegrationModalComponent: Growave,
      tags: ["sales", "loyalty"]
    },
    {
      title: 'Zapiet - Pickup + Delivery',
      subtitle: 'Maximize Your Sales with Zapiet - The Ultimate Delivery, Pickup, and Shipping Scheduling App',
      imageSrc: 'https://cdn.shopify.com/app-store/listing_images/1347d3b5d3487e69248c874bad357fd4/icon/CM6My730lu8CEAE=.png',
      isShowAsEnabled: shopInfo.zapietEnabled,
      IntegrationModalComponent: Zapiet,
      tags: ['sales', 'automation']
    },
  ];

  function toggleSelectedTag(category: AppIntegrationCategory) {
    if (selectedCategory === category) {
      setSelectedTag('all');
    } else {
      setSelectedTag(category);
    }
  }

  function getTagButtonColor(category: AppIntegrationCategory) {
    if (category === 'premium') {
      return selectedCategory === category ? 'primary' : 'warning';
    }
    return selectedCategory === category ? 'primary' : 'light';
  }

  function getFilteredIntegrationList(integrationCategory: AppIntegrationCategory): Array<AppIntegration> {
    const compareIntegrations = (integrationA: AppIntegration, integrationB: AppIntegration) => {
      // Priority Appstle Apps first, always.
      if (integrationA.title.toLowerCase().includes('appstle') && !integrationB.title.toLowerCase().includes('appstle')) {
        return -1;
      }
      if (integrationB.title.toLowerCase().includes('appstle') && !integrationA.title.toLowerCase().includes('appstle')) {
        return 1;
      }

      // Then show all recommended apps
      if (integrationA.isRecommended && !integrationB.isRecommended) {
        return -1;
      }
      if (integrationB.isRecommended && !integrationA.isRecommended) {
        return 1;
      }

      // Then push PageFly to top row
      if (integrationA.title.toLowerCase().includes('pagefly') && !integrationB.title.toLowerCase().includes('pagefly')) {
        return -1;
      }
      if (integrationB.title.toLowerCase().includes('pagefly') && !integrationA.title.toLowerCase().includes('pagefly')) {
        return 1;
      }

      // Show apps in alphabetical order by group (Appstle apps, Recommended apps, other)
      return integrationA.title.localeCompare(integrationB.title);
    };

    if (integrationCategory === 'all') {
      return appIntegrations.sort(compareIntegrations);
    }

    if (integrationCategory === 'premium') {
      return appIntegrations.filter(integration => integration.isPremium).sort(compareIntegrations);
    }

    return appIntegrations.filter(integration => integration.tags.some(tag => tag === integrationCategory)).sort(compareIntegrations);
  }

  return (
    <ReactCSSTransitionGroup
      component="div"
      transitionName="TabsAnimation"
      transitionAppear
      transitionAppearTimeout={0}
      transitionEnter={false}
      transitionLeave={false}
    >
      <Row className="d-flex w-100 mx-auto mb-4" style={{ gap: '12px' }}>
        <Button
          className="font-size-md"
          style={{ minWidth: '80px' }}
          color={getTagButtonColor('all')}
          size="lg"
          onClick={() => setSelectedTag('all')}
        >
          All
        </Button>

        {appIntegrationTags.map(tag => {
          return (
            <Button
              className="font-size-md"
              style={{ minWidth: '80px' }}
              color={getTagButtonColor(tag)}
              size="lg"
              onClick={() => toggleSelectedTag(tag)}
            >
              {tagButtonNames[tag]}{' '}
            </Button>
          );
        })}

        <Button
          className="font-size-md"
          style={{ minWidth: '80px' }}
          color={getTagButtonColor('premium')}
          size="lg"
          onClick={() => setSelectedTag('premium')}
        >
          Premium
        </Button>
      </Row>

      <Row>
        {getFilteredIntegrationList(selectedCategory).map(appIntegration => {
          return (
            <Col md={6} lg={4} xl={3} className="pb-4">
              <IntegrationCard {...appIntegration} />
            </Col>
          );
        })}
      </Row>
    </ReactCSSTransitionGroup>
  );
};

const mapStateToProps = ({ shopInfo }) => ({
  shopInfo: shopInfo.entity
});

const mapDispatchToProps = {
  getEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(BasicIntegrationV2);
