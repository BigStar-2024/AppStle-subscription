import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { Form } from 'react-final-form';
import arrayMutators from 'final-form-arrays';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';
import { Col, Row } from 'reactstrap';
import Loader from 'react-loaders';
import { IRootState } from 'app/shared/reducers';
import {
  createEntity as createSubscriptionBundlingEntity,
  getEntity as getSubscriptionBundlingEntity,
  getEntities as getSubscriptionBundlingEntities,
  reset as resetSubscriptionBundling,
  updateEntity as updateSubscriptionBundlingEntity,
} from 'app/entities/subscription-bundling/subscription-bundling.reducer';
import { ISubscriptionBundling } from 'app/shared/model/subscription-bundling.model';
import {
  getEntities as getSubscriptionGroupEntities,
  getEntity as getSubscriptionGroupEntity,
} from 'app/entities/subscription-group/subscription-group.reducer';
import { ISubscriptionGroup } from 'app/shared/model/subscription-group.model';
import PageTitle from 'app/Layout/AppMain/PageTitle';
import FrequencyAlertModal from './FrequencyAlertModal';
import BuildABoxEnableSettings from './ManageSubscriptionBundling/BuildABoxEnableSettings';
import BuildABoxGeneralSettings from './ManageSubscriptionBundling/BuildABoxGeneralSettings';
import BuildABoxClassicSettings from './ManageSubscriptionBundling/BuildABoxClassicSettings';
import BuildABoxSingleProductSettings from './ManageSubscriptionBundling/BuildABoxSingleProductSettings';
import BuildABoxAdvancedSettings from './ManageSubscriptionBundling/BuildABoxAdvancedSettings';
import '../SubscriptionGroups/CreateSubscriptionGroup.scss';
import { BuildABoxRedirect } from 'app/shared/model/enumerations/build-a-box-redirect.model';

type BuildABoxFormValues =
  | ISubscriptionBundling
  | {
      bundleRedirect?: BuildABoxRedirect | string;
    };

export const buildABoxFormMutators = {
  setValue: ([fieldName, newValue], state: any, { changeValue }) => {
    changeValue(state, fieldName, () => newValue);
  },
  sortDiscounts: (_args: any[], state: any, { changeValue }) => {
    changeValue(state, 'tieredDiscount', (oldValue: { quantity: number }[]) => oldValue.sort((a, b) => a.quantity - b.quantity));
  },
};

function ManageSubscriptionBundling({ match, history }: RouteComponentProps<{ id?: string; cloneId?: string }>) {
  const id = match.params?.id;
  const cloneId = match.params?.cloneId;

  const isLoading = useSelector((state: IRootState) => state.subscriptionBundling.loading || state.subscriptionGroup.loading);
  const isUpdating = useSelector((state: IRootState) => state.subscriptionBundling.updating);
  const updateSuccess = useSelector((state: IRootState) => state.subscriptionBundling.updateSuccess);
  const subscriptionBundling = useSelector((state: IRootState) => state.subscriptionBundling.entity);
  const subscriptionBundleEntities = useSelector((state: IRootState) => state.subscriptionBundling.entities);
  const subscriptionGroupEntities = useSelector((state: IRootState) => state.subscriptionGroup.entities);
  const dispatch = useDispatch() as (a: any) => Promise<any>;

  const [isFrequencyAlertModelOpen, setIsFrequencyAlertModelOpen] = useState(false);
  const [initialValues, setInitialValues] = useState<BuildABoxFormValues>({
    ...subscriptionBundling,
    subscriptionGroup: [],
    tieredDiscount: [],
    singleProductSettings: [],
  });
  const [initialSelectedGroups, setInitialSelectedGroups] = useState([]);

  useEffect(() => {
    getInitialEntities();
    setInitialValues(getInitialValues());
  }, []);

  useEffect(() => {
    getInitialSelectedGroups(subscriptionBundling).then(groups => setInitialSelectedGroups(groups));
  }, [subscriptionBundling]);

  useEffect(() => {
    setInitialValues(getInitialValues());
  }, [subscriptionBundling, initialSelectedGroups]);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  async function getInitialEntities() {
    dispatch(resetSubscriptionBundling());
    await dispatch(getSubscriptionGroupEntities());

    dispatch(getSubscriptionBundlingEntities(null, null, null));
    if (id || cloneId) {
      dispatch(getSubscriptionBundlingEntity(id || cloneId));
    }
  }

  function getInitialValues() {
    return {
      ...subscriptionBundling,
      tieredDiscount: getInitialParsedArray(subscriptionBundling.tieredDiscount),
      singleProductSettings: getInitialParsedArray(subscriptionBundling.singleProductSettings),
      subscriptionGroup: initialSelectedGroups,
      bundleRedirect: getIsRedirectingToBuildABox() ? 'ANOTHER_BOX' : subscriptionBundling.bundleRedirect,
    };
  }

  function getInitialParsedArray(arr: string) {
    try {
      const parsedDiscounts = JSON.parse(arr);
      if (!Array.isArray(parsedDiscounts)) return [];
      return parsedDiscounts;
    } catch (err) {
      return [];
    }
  }

  async function getInitialSelectedGroups(subscriptionBundling: ISubscriptionBundling) {
    if (subscriptionBundling.subscriptionGroup) {
      try {
        const groupNames = JSON.parse(subscriptionBundling.subscriptionGroup);
        return groupNames;
      } catch (err) {
        return [];
      }
    } else {
      let groupName: string;
      if (subscriptionGroupEntities.length == 0 && subscriptionBundling?.subscriptionId) {
        groupName = ((await dispatch(getSubscriptionGroupEntity(subscriptionBundling.subscriptionId))) as any).value.data?.groupName;
      } else {
        groupName = subscriptionGroupEntities.filter(
          (group: ISubscriptionGroup) => Number(group.id) === subscriptionBundling.subscriptionId
        )[0]?.groupName;
      }

      return groupName && subscriptionBundling?.subscriptionId
        ? [
            {
              value: subscriptionBundling?.subscriptionId,
              label: groupName,
            },
          ]
        : [];
    }
  }

  function getIsRedirectingToBuildABox() {
    if (subscriptionBundling.bundleRedirect !== BuildABoxRedirect.CUSTOM) return false;
    return subscriptionBundleEntities
      .filter(bundle => bundle.subscriptionBundlingEnabled && bundle?.id != subscriptionBundling?.id)?.some(bundle => bundle.subscriptionBundleLink == subscriptionBundling.customRedirectURL);
  }

  function handleClose() {
    history.push('/dashboards/subscription-bundling');
  }

  function saveEntity(values: any) {
    const entity = { ...values };
    if (cloneId) {
      entity.id = null;
      entity.uniqueRef = null;
    }

    entity.subscriptionId = values.subscriptionId;

    if (values.subscriptionGroup?.length > 1) {
      entity.subscriptionGroup = JSON.stringify(values.subscriptionGroup);
    } else {
      entity.subscriptionGroup = null;
    }

    if (values.buildABoxType === 'CLASSIC') {
      entity.tieredDiscount = JSON.stringify(values.tieredDiscount);
      entity.singleProductSettings = null;
    }

    if (values.buildABoxType === 'SINGLE_PRODUCT') {
      entity.singleProductSettings = JSON.stringify(values.singleProductSettings);
      entity.tieredDiscount = null;
    }

    if (values.bundleRedirect == 'ANOTHER_BOX') {
      entity.bundleRedirect = BuildABoxRedirect.CUSTOM;
    }

    /* I don't see why we would set these to null when the box
     * is disabled
    if (!entity.subscriptionBundlingEnabled) {
      entity.minProductCount = null;
      entity.maxProductCount = null;
      entity.discount = null;
    }
    */

    if (entity.id) {
      dispatch(updateSubscriptionBundlingEntity(entity));
    } else {
      dispatch(createSubscriptionBundlingEntity(entity));
    }
  }

  let submit: (() => void) | (() => Promise<any>) = () => null;
  return (
    <ReactCSSTransitionGroup
      component="div"
      transitionName="TabsAnimation"
      transitionAppear
      transitionAppearTimeout={0}
      transitionEnter={false}
      transitionLeave={false}
    >
      <PageTitle
        heading={id ? 'Edit Build-A-Box Information' : 'Create Build-A-Box'}
        icon="pe-7s-network icon-gradient bg-mean-fruit"
        enablePageTitleAction
        actionTitle="Save"
        onActionClick={() => {
          submit();
        }}
        enableSecondaryPageTitleAction
        secondaryActionTitle="Cancel"
        onSecondaryActionClick={handleClose}
        onActionUpdating={isLoading || isUpdating}
        sticky
        tutorialButton={{
          show: true,
          videos: [
            {
              title: 'Build-A-Box Classic',
              url: 'https://www.youtube.com/watch?v=1sIU8pTYwd4',
            },
            {
              title: 'Build-A-Box Single Product',
              url: 'https://www.youtube.com/watch?v=4QPhUyZV0wI',
            },
          ],
          docs: [
            {
              title: "How to Set Up Build-A-Box",
              url: 'https://intercom.help/appstle/en/articles/5555314-how-to-setup-build-a-box'
            }
          ]
        }}
      />
      {isLoading ? (
        <div style={{ margin: '10% 0 0 43%' }} className="loader-wrapper d-flex justify-content-center align-items-center">
          <Loader type="line-scale" active />
        </div>
      ) : (
        <div className="p-2">
          <Form
            initialValues={{
              ...initialValues,
            }}
            mutators={{
              ...arrayMutators,
              ...buildABoxFormMutators,
            }}
            onSubmit={saveEntity}
          >
            {({ handleSubmit, values }) => {
              submit = handleSubmit;
              return (
                <form onSubmit={handleSubmit}>
                  <Row className="justify-content-center">
                    <Col md={8} className="d-flex flex-column" style={{ gap: '1rem' }}>
                      <BuildABoxEnableSettings />
                      <BuildABoxGeneralSettings setIsValidModelOpen={setIsFrequencyAlertModelOpen} />

                      {values.buildABoxType === 'CLASSIC' && (
                        <div>
                          <h5 className="my-3">Classic Build-A-Box Settings</h5>
                          <BuildABoxClassicSettings />
                        </div>
                      )}

                      {values.buildABoxType === 'SINGLE_PRODUCT' && (
                        <div>
                          <h5 className="my-3">Single Product Build-A-Box Settings</h5>
                          <BuildABoxSingleProductSettings />
                        </div>
                      )}
                      <BuildABoxAdvancedSettings />
                    </Col>
                  </Row>
                  <FrequencyAlertModal
                    isFrequencyAlertModelOpen={isFrequencyAlertModelOpen}
                    toggleUpdateOrderNoteModal={() => setIsFrequencyAlertModelOpen(!isFrequencyAlertModelOpen)}
                    seletedPlans={values.subscriptionGroup}
                  />
                </form>
              );
            }}
          </Form>
        </div>
      )}
    </ReactCSSTransitionGroup>
  );
}

export default ManageSubscriptionBundling;
