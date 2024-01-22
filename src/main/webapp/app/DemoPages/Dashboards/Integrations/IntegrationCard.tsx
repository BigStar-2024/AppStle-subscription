import React, { useState } from 'react';
import { Badge, Card, CardBody, CardSubtitle, CardTitle } from 'reactstrap';

export interface IntegrationCardProps {
  title: string;
  subtitle: string;
  imageSrc: string;
  isShowAsEnabled?: boolean;
  isRecommended?: boolean;
  isPremium?: boolean;
  IntegrationModalComponent: React.ElementType;
}

const defaultProps: Partial<IntegrationCardProps> = {
  isRecommended: false,
  isShowAsEnabled: false,
  isPremium: false,
};

function IntegrationCard(props: IntegrationCardProps) {
  props = { ...defaultProps, ...props };
  const { title, subtitle, imageSrc, isShowAsEnabled, isRecommended, isPremium, IntegrationModalComponent } = props;
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      <Card
        style={{
          maxHeight: '320px',
          textAlign: 'center',
          height: '100%',
        }}
        className="card-border card-hover-shadow-2x position-relative"
        onClick={() => {
          setIsModalOpen(true);
        }}
      >
        <CardBody className="top-elem">
          <div className="d-flex flex-column align-items-end position-absolute" style={{ right: '10px', top: '10px', gap: '5px' }}>
            {isRecommended && (
              <Badge color="info" pill>
                Recommended
              </Badge>
            )}
            {isPremium && (
              <Badge color="warning" pill>
                Premium
              </Badge>
            )}
          </div>
          <div className="mt-3">
            <img height="50px" className="mb-4" src={imageSrc} />
            <CardTitle>{title}</CardTitle>
            {isShowAsEnabled ? <div className="mb-3 badge badge-pill badge-success">Enabled</div> : null}
            <CardSubtitle>{subtitle}</CardSubtitle>
          </div>
        </CardBody>
      </Card>

      <IntegrationModalComponent
        isOpen={isModalOpen}
        onClose={() => {
          setIsModalOpen(false);
        }}
      />
    </>
  );
}

export default IntegrationCard;
