import React from 'react';
import { Button } from 'reactstrap';

const getNavStates = (indx, length) => {
  const styles = [];
  for (let i = 0; i < length; i++) {
    if (i < indx) {
      styles.push('done');
    } else if (i === indx) {
      styles.push('doing');
    } else {
      styles.push('todo');
    }
  }
  return { current: indx, styles };
};

const checkNavState = (currentStep, stepsLength) => {
  if (currentStep > 0 && currentStep < stepsLength - 1) {
    return {
      showPreviousBtn: true,
      showNextBtn: true
    };
  }
  if (currentStep === 0) {
    return {
      showPreviousBtn: false,
      showNextBtn: true
    };
  }
  return {
    showPreviousBtn: false,
    showNextBtn: false
  };
};

export default class MultiStep extends React.Component {
  constructor(props) {
    super(props);

    console.log(this.props)
}
  state = {
    showPreviousBtn: false,
    showNextBtn: true,
    compState: 0,
    navState: getNavStates(0, this.props.steps.length)
  };

  setNavState = next => {
    this.setState({
      navState: getNavStates(next, this.props.steps.length)
    });
    if (next < this.props.steps.length) {
      this.setState({ compState: next });
    }
    this.setState(checkNavState(next, this.props.steps.length));
  };

  handleKeyDown = evt => {
    if (evt.which === 13) {
      this.next();
    }
  };

  handleOnClick = evt => {
    // if (evt.currentTarget.value === this.props.steps.length - 1 && this.state.compState === this.props.steps.length - 1) {
    //   this.setNavState(this.props.steps.length);
    // } else {
    //   this.setNavState(evt.currentTarget.value);
    // }
  };

  next = async () => {
    if (this.props.selectedVariantItems.length && (this.state.compState === 1)) {
      const checkloading = () =>  {
        if (this?.props?.errors?.length) {
          this.setNavState(this.state.compState + 1);
          return;
        } else {
          setTimeout(() => {
            checkloading()
          }, 1000)
        }
      }
      checkloading();
      await this.props.addProduct(this.props.selectedVariantItems);
      // checkloading();  
    } else if (this.state.compState !== 1) {
      this.setNavState(this.state.compState + 1);
    }
    
    
  };

  previous = () => {
    if (this.state.compState > 0) {
      this.setNavState(this.state.compState - 1);
      this.props.setErrors([]);
    }
  };

  getClassName = (className, i) => {
    return `${className}-${this.state.navState.styles[i]}`;
  };

  renderSteps = () => {
    return this.props.steps.map((s, i) => (
      <li className={this.getClassName('form-wizard-step', i)} onClick={this.handleOnClick} key={i} value={i}>
        <em>{i + 1}</em>
        <span>{this.props.steps[i].name}</span>
      </li>
    ));
  };

  render() {
    return (
      <div onKeyDown={this.handleKeyDown}>
        <ol className="forms-wizard">{this.renderSteps()}</ol>
        {this.props.steps[this.state.compState].component}
        {/* <div className="divider" /> */}
        <div className="clearfix" style={{marginTop: "20px", display: "flex"}}>
          <div style={{width: "30%"}}></div>
          <div style={this.props.showNavigation ? {width:"70%", display: "flex", alignItems: "flex-end", flexDirection: "column"} : { display: 'none' }}>
            <div className="d-flex">
              <Button
                color="secondary"
                className="btn-shadow float-left btn-wide"
                outline
                style={this.state.showPreviousBtn ? {} : { display: 'none' }}
                onClick={this.previous}
              >
                {this?.props?.customerPortalSettingEntity?.previousBtnTextV2 || "Previous"}
              </Button>

              <Button
                color="primary"
                className="btn-shadow btn-wide float-right btn-hover-shine"
                style={this.state.showNextBtn ? {marginLeft: "20px"} : { display: 'none' }}
                onClick={this.next}
              >
                {(this?.props?.updateInProgress) ? <div className="appstle_loadersmall" /> : `${this?.props?.customerPortalSettingEntity?.nextBtnTextV2 || "Next"}`}
              </Button>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

MultiStep.defaultProps = {
  showNavigation: true
};
