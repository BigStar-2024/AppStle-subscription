import React, {Component} from 'react';
// import { IntercomAPI } from 'react-intercom';
import {connect} from 'react-redux';
import {Alert, Button, Input, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {getEntitiesLite} from 'app/entities/theme-code/theme-code.reducer';
import MySaveButton from '../Utilities/MySaveButton';
import {THEME_APP_EXTENSION_HANDLE, THEME_APP_EXTENSION_UUID} from 'app/config/constants';
import {ShopifyThemeInstallationVersion} from 'app/shared/model/enumerations/shopify-theme-installation-version.model';
import {getShopInfoEntity, updateEntity} from "app/entities/shop-info/shop-info.reducer";

export class AppEmbeddedPopUp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      themeName: 'atlantic',
      modal: true
    };
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      modal: !this.state.modal
    });
  }

  componentDidMount() {
    this.props.getEntitiesLite();
    this.setState({themeName: this.props.themeSettingsEntity.themeName});
  }

  render() {
    const {themeCodeList, themeSettingsEntity} = this.props;

    let step1Component =
      themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2 ? (
        <>
          {/*<h6>
        <b>Step 1</b>
      </h6>*/}

          <div>
            <div className="mt-2">
              <h6>
                <b>Before you continue</b> please enable the Appstle's embedded block on your live theme. Please watch the video
                below on how to
                enable Appstle's embedded block.
                <br/>
                <br/>
                This popup will disappear once Appstle has been enabled on your theme.
                <div style={{textAlign: 'center'}}>
                  {/*<img
                src="https://ik.imagekit.io/mdclzmx6brh/Capture_dr6T4cDpMPv.PNG?ik-sdk-version=javascript-1.4.3&updatedAt=1642973805490"
                style={{height: "250px"}}/>*/}
                  <a
                    className="btn btn-primary primary"
                    color="primary"
                    href={`https://${themeSettingsEntity.shop}/admin/themes/current/editor?context=apps&appEmbed=gid://shopify/OnlineStoreThemeAppEmbed/${THEME_APP_EXTENSION_HANDLE}?app_embed_uuid=${THEME_APP_EXTENSION_UUID}`}
                    target="_blank"
                    style={{display: 'inline-block', marginTop: '45px'}}
                  >
                    Click Here To Activate Embedded Block
                  </a>
                  {/* <div style={{position: "relative", paddingBottom: "51.33079847908745%", height: "0"}}>
                <iframe src="https://www.loom.com/embed/82afe4cb67544f30883f8ab60a728b35?autoplay=1"
                  frameborder="0"
                  webkitallowfullscreen
                  mozallowfullscreen
                  allowfullscreen
                  style={{position: "absolute", top: "0", left: "0", width: "100%", height: "100%"}}></iframe>
              </div> */}
                  <div style={{margin: '50px auto 0'}}>
                    <video autoplay="true" loop="true" muted="true" preload="auto" style={{width: '100%'}}>
                      <source
                        src="https://ik.imagekit.io/jfw8ob54zet/test-membership-v9___Home___Shopify_-_22_January_2022_moQtYVqgB.mp4?ik-sdk-version=javascript-1.4.3&updatedAt=1642902953718"
                        type="video/mp4"
                      ></source>
                    </video>
                  </div>
                </div>
                {/*<br/><br/>
            <b>You can de-activate it anytime you would like to disable the widget.</b><br/><br/>
            The widget
            can be customized under <b>Customizations > Membership Widget Settings.</b>*/}
              </h6>
            </div>
          </div>

          {/*<div className="divider"/>*/}
        </>
      ) : null;
    let step2Heading =
      themeSettingsEntity.shopifyThemeInstallationVersion === ShopifyThemeInstallationVersion.V2 ? (
        <h6>
          <b>Step 2</b>
        </h6>
      ) : null;
    return (
      <Modal isOpen={this.state.modal} toggle={this.toggle} size={'lg'}>
        <ModalHeader toggle={this.toggle}>Configure Subscription Widget on your theme</ModalHeader>
        <ModalBody>
          {/*<Alert className="mbg-3" color="warning">
            <span className="pr-2">
              <FontAwesomeIcon icon={faInfoCircle}/>
            </span>
            The theme of your store was not detected. Please select your theme from dropdown below.
          </Alert>*/}
          {step1Component}
          {/*{step2Heading}

          <div>
            <div className="mt-2">
              <h6>
                Please select your theme
              </h6>
            </div>
            <Input
              className="mb-2"
              type="select"
              onChange={e => {
                this.setState({themeName: e.target.value});
              }}
              value={this.state.themeName}
            >
              {themeCodeList.map(option => {
                return <option value={option.themeName}>{option.themeNameFriendly}</option>;
              })}
            </Input>
          </div>

          <div className="divider"/>
          <h6>
            <b>Can't find your theme?</b>
          </h6>
          <div>
            If you don't find your theme in the list, contact us with the name of your theme and we can
            add Membership Widget to match your brand.
          </div>*/}
        </ModalBody>
        {/*<ModalFooter>
          <Button
            className="btn btn-shadow"
            color="primary"
            onClick={() => {
              this.props.onSkip(true);

              Intercom('showNewMessage', 'I need some help with theme integration for my shop. Can you please help?');
            }}
          >
            Contact Support
          </Button>

          <MySaveButton
            updating={this.props.themeUpdating}
            onClick={() => {
              this.props.onSave(this.state.themeName);
            }}
          >
            Save
          </MySaveButton>
        </ModalFooter>*/}
      </Modal>
    );
  }
}

const mapStateToProps = ({themeCode, themeSettings, shopInfo}) => ({
  themeCodeList: themeCode.entities,
  themeSettingsEntity: themeSettings.entity,
  shopInfo: shopInfo.entity
});

const mapDispatchToProps = {
  getEntitiesLite,
  getShopInfoEntity,
  updateEntity
};

export default connect(mapStateToProps, mapDispatchToProps)(AppEmbeddedPopUp);
