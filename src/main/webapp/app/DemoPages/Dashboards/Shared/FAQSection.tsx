import React, { useState, cloneElement, CSSProperties } from 'react';
import { Card, CardBody, CardHeader, Collapse } from 'reactstrap';
import { ChevronForward } from 'react-ionicons';

type FAQSectionProps = {
  children: React.ReactElement<FAQEntryProps> | React.ReactElement<FAQEntryProps>[];
  title?: string;
  icon?: string;
  className?: string;
  style?: CSSProperties;
  isAllInitialOpen?: boolean;
};

type FAQEntryProps = {
  question: string;
  children: React.ReactNode;
  isInitialOpen?: boolean;
};

const FAQSection = ({ children, title, icon, className, style, isAllInitialOpen = false }: FAQSectionProps) => {
  if (!Array.isArray(children)) {
    children = [children];
  }

  return (
    <Card className={`${className}`} style={style}>
      {!!title && (
        <CardHeader>
          {!!icon && <i className={`header-icon ${icon} icon-gradient bg-plum-plate`}></i>}
          {title}
        </CardHeader>
      )}
      <CardBody className="px-4 d-flex flex-column" style={{ gap: '1.5rem' }}>
        {children.map(el => {
          return isAllInitialOpen ? cloneElement(el, { ...el.props, isInitialOpen: true }) : el;
        })}
      </CardBody>
    </Card>
  );
};

export const FAQEntry = ({ question, children, isInitialOpen = false }: FAQEntryProps) => {
  const [isOpen, setIsOpen] = useState(isInitialOpen);

  return (
    <div style={{ borderBottom: "1px solid #e7e7e7"}}>
      <h6
        role="button"
        onClick={() => setIsOpen(!isOpen)}
        className="d-flex align-items-center font-weight-bold text-primary"
        style={{ cursor: 'pointer ', gap: '.25rem' }}
      >
        Q: {question}
        <span>
          <ChevronForward
            width="24px" height="24px"
            color="#545cd8"
            style={{
              transform: isOpen ? 'rotate(90deg)' : '',
              transition: 'transform .2s',
            }}
          />
        </span>
      </h6>
      <Collapse isOpen={isOpen} className="mb-2 border-0">
        <div className="d-flex mb-3 pr-3" style={{ gap: '.5rem', maxWidth: "70em" }}>
          <span className="font-weight-bold">A:</span>
          <p className="mb-0">{children}</p>
        </div>
      </Collapse>
    </div>
  );
};

export default FAQSection;
