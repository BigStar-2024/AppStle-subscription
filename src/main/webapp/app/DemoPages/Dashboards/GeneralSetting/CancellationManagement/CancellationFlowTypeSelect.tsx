import React, { Fragment, useState, useEffect, useRef } from 'react';
import { Badge, Card, CardHeader, CardBody, CardTitle, CardText, Collapse, Button, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronRight } from '@fortawesome/free-solid-svg-icons';

import { CancellationTypeStatus } from 'app/shared/model/enumerations/cancellation-type-status.model';

interface CancellationFlowTypeCardProps {
  title: string;
  description: string;
  cancellationType: CancellationTypeStatus;
  currentCancellationType: CancellationTypeStatus;
  onSelect: (selectedType: CancellationTypeStatus) => void;
  isRecommended?: boolean;
}

const CancellationFlowTypeCard = (props: CancellationFlowTypeCardProps) => {
  const { title, description, cancellationType, currentCancellationType, onSelect, isRecommended = false } = props;

  const isSelected = cancellationType === currentCancellationType;

  return (
    <Col className="my-2" md={6}>
      <Card style={{ height: '100%' }}>
        <CardBody className="d-flex flex-column justify-content-start">
          <CardTitle className="mb-0 text-primary">
            <span className="mr-2">{title}</span>
            {isRecommended && <Badge color="info">Recommended by Appstle</Badge>}
          </CardTitle>
          <hr className="mx-0" />

          <CardText>{description}</CardText>
          <Button
            className="mt-auto align-self-start"
            onClick={() => onSelect(cancellationType)}
            color={isSelected ? 'success' : 'secondary'}
          >
            {isSelected ? 'Selected' : 'Select to configure'}
          </Button>
        </CardBody>
      </Card>
    </Col>
  );
};

interface CancellationFlowTypeSelectProps {
  cancellationType: CancellationTypeStatus;
  onSelect: (selectedType: CancellationTypeStatus) => void;
}

const CancellationFlowTypeSelect = (props: CancellationFlowTypeSelectProps) => {
  const { cancellationType, onSelect } = props;

  const [isOpen, setIsOpen] = useState(false);
  const isCancellationTypeUpdated = useRef(false);

  const toggleOpen = () => {
    setIsOpen(!isOpen);
  };

  // Open if Cancellation Type is CANCEL_IMMEDIATELY and only when the cancellationType
  // is first loaded.
  useEffect(() => {
    if (cancellationType !== undefined && !isCancellationTypeUpdated.current) {
      setIsOpen(cancellationType === CancellationTypeStatus.CANCEL_IMMEDIATELY);
      isCancellationTypeUpdated.current = true;
    }
  }, [cancellationType]);

  const dynamicChevronIcon = isOpen ? faChevronDown : faChevronRight;

  const cancellationTypeLabels = {
    [CancellationTypeStatus.CANCEL_IMMEDIATELY]: 'Cancel Immediately',
    [CancellationTypeStatus.CUSTOMER_RETENTION_FLOW]: 'Customer Retention Flow',
    [CancellationTypeStatus.CANCELLATION_INSTRUCTIONS]: 'Provide Cancellation Instructions',
    [CancellationTypeStatus.CANCEL_AFTER_PAUSE]: 'Pause Option (as a Retention) Step before Cancellation',
  };

  const handleSelect = (cancellationType: CancellationTypeStatus) => {
    setIsOpen(false);
    onSelect(cancellationType);
  };

  return (
    <Fragment>
      <div className="mb-3">
        <h5 className="font-weight-bold">Cancellation Flow</h5>
        <h6>
          Selected Cancellation Flow: <span className="text-primary font-weight-bold">{cancellationTypeLabels[cancellationType]}</span>
          <Button
            className="ml-2"
            color="primary"
            size="sm"
            onClick={() => {
              setIsOpen(true);
            }}
          >
            Change
          </Button>
        </h6>
      </div>

      <Card>
        <CardHeader style={{ cursor: 'pointer' }} onClick={toggleOpen}>
          Cancellation Flow Options
          <FontAwesomeIcon className="ml-1" icon={dynamicChevronIcon} />
        </CardHeader>
        <Collapse className="p-4" style={{ backgroundColor: '#fafbfc' }} isOpen={isOpen}>
          <p>
            Select an optional flow for customers that attempt to cancel their subscriptions. Not selecting an option will allow your
            customers to cancel their subscriptions without any additional steps.
          </p>
          <Row className="d-flex">
            <CancellationFlowTypeCard
              title="Cancel Immediately"
              description="Customers will be able to cancel subscriptions easily, and all future orders will be cancelled automatically."
              cancellationType={CancellationTypeStatus.CANCEL_IMMEDIATELY}
              currentCancellationType={cancellationType}
              onSelect={handleSelect}
            />
            <CancellationFlowTypeCard
              title="Customer Retention Flow"
              description="Allow your customers to select reasons for cancellation and utilize retention actions."
              cancellationType={CancellationTypeStatus.CUSTOMER_RETENTION_FLOW}
              currentCancellationType={cancellationType}
              onSelect={handleSelect}
              isRecommended
            />
            <CancellationFlowTypeCard
              title="Provide Cancellation Instructions"
              description="Customers will not be able to cancel subscriptions themselves. Instead, you can display a customised message so that they can contact you. This will be helpful to retain your customers before cancellation."
              cancellationType={CancellationTypeStatus.CANCELLATION_INSTRUCTIONS}
              currentCancellationType={cancellationType}
              onSelect={handleSelect}
            />
            <CancellationFlowTypeCard
              title="Pause Option (as a Retention) Step before Cancellation"
              description="Customers who choose to cancel will be initially prompted with X pause duration options on their subscription when enabled."
              cancellationType={CancellationTypeStatus.CANCEL_AFTER_PAUSE}
              currentCancellationType={cancellationType}
              onSelect={handleSelect}
            />
          </Row>
          <Row className="d-flex justify-content-center">
            <Col className="pt-3" md={2}>
              <Button
                block
                onClick={() => {
                  setIsOpen(false);
                }}
              >
                <span className="h6">Close</span>
              </Button>
            </Col>
          </Row>
        </Collapse>
      </Card>
    </Fragment>
  );
};

export default CancellationFlowTypeSelect;
