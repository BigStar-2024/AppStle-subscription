import React, { Fragment, useState } from 'react';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import {
  Card, CardHeader,
  Collapse,
} from 'reactstrap';
import { connect } from 'react-redux';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import FeatureAccessCheck from 'app/shared/auth/featureAccessCheck';
import { ChevronForward } from 'react-ionicons';
import SuccessPastOrderReport from './SuccessPastOrderReport';
import FailedPastOrderReport from './FailedPastOrderReport';
import UpcomingOrderReport from './UpcomingOrderReport';
import SkipOrderReport from './SkipOrderReport';
import SkippedDunningMgmtReport from './SkippedDunningMgmtReport';
import SkippedInventoryMgmtReport from './SkippedInventoryMgmtReport';

const reports = (props ) => {
  const [accordionState, setAccordionState] = useState([false, false, false, false, false, false]);

  const toggleAccordion = tab => {
    const state = accordionState.map((x, index) => (tab === index ? !x : false));
    setAccordionState(state);
  };

  const forward_arrow_icon = {
    marginLeft: 'auto',
    transition: 'transform 0.2s',
    transformOrigin: 'center'
  };

  return (
      <Fragment>
        <ReactCSSTransitionGroup
          component="div"
          transitionName="TabsAnimation"
          transitionAppear
          transitionAppearTimeout={0}
          transitionEnter={false}
          transitionLeave={false}
        >
          <PageTitle heading="Reports" icon="pe-7s-display1 icon-gradient bg-mean-fruit" sticky={false}/>
          <Card className="main-card">
            <CardHeader
              onClick={() => toggleAccordion(0)}
              aria-expanded={accordionState[0]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-checkmark-circle icon-gradient bg-plum-plate"> </i> Successful Past Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[0] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[0]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <SuccessPastOrderReport></SuccessPastOrderReport>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(1)}
              aria-expanded={accordionState[1]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-cross-circle icon-gradient bg-plum-plate"> </i> Failed Past Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[1] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[1]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <FailedPastOrderReport></FailedPastOrderReport>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(2)}
              aria-expanded={accordionState[2]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-arrow-up-circle icon-gradient bg-plum-plate"> </i> Upcoming Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[2] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[2]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <UpcomingOrderReport></UpcomingOrderReport>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(3)}
              aria-expanded={accordionState[3]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-circle-minus icon-gradient bg-plum-plate"> </i> Skipped Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[3] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[3]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <SkipOrderReport></SkipOrderReport>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(4)}
              aria-expanded={accordionState[4]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-circle-minus icon-gradient bg-plum-plate"> </i> Skipped by Dunning Management Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[4] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[4]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <SkippedDunningMgmtReport></SkippedDunningMgmtReport>
            </Collapse>
          </Card>
          <Card className="main-card mt-3">
            <CardHeader
              onClick={() => toggleAccordion(5)}
              aria-expanded={accordionState[5]}
              aria-controls="widgetLabelSettingsWrapper"
              style={{ cursor: 'pointer' }}
            >
              <i className="header-icon lnr-circle-minus icon-gradient bg-plum-plate"> </i> Skipped by Inventory Management Order
              <span style={{ ...forward_arrow_icon, transform: accordionState[4] ? 'rotate(90deg)' : '' }}>
                <ChevronForward width="20px" height="20px" color="rgba(18, 21, 78, 0.7)" />
              </span>
            </CardHeader>
            <Collapse isOpen={accordionState[5]} data-parent="#accordion" id="widgetLabelSettingsWrapper" aria-labelledby="WidgetLabel">
              <SkippedInventoryMgmtReport></SkippedInventoryMgmtReport>
            </Collapse>
          </Card>
        </ReactCSSTransitionGroup>
      </Fragment>
  );
};

const mapStateToProps = state => ({
});

const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(reports);
