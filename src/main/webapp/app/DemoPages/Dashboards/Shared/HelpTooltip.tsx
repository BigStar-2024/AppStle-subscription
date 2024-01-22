import React from 'react';
import { Help } from '@mui/icons-material';
import { styled } from '@mui/material/styles';
import Tooltip, { TooltipProps, tooltipClasses } from '@mui/material/Tooltip';

type HelpTooltipProps = Omit<TooltipProps, 'title' | 'children'> & {
  children: React.ReactNode;
  maxWidth?: number;
  title?: React.ReactNode;
};

const defaultProps: Partial<HelpTooltipProps> = {
  maxWidth: 300,
  placement: 'right',
  arrow: true,
  enterDelay: 0,
};

const HelpTooltip = ({ children, maxWidth, ...props }: HelpTooltipProps) => {
  const StyledTooltip = styled(({ className }: Partial<TooltipProps>) => (
    <Tooltip {...defaultProps} {...props} title={<div className="p-1">{children}</div>} classes={{ popper: className }}>
      <Help style={{ fontSize: '1rem', color: '#545cd8' }} />
    </Tooltip>
  ))({
    [`& .${tooltipClasses.tooltip}, & .${tooltipClasses.tooltip} div`]: {
      backgroundColor: '#337AB7',
      color: '#fff',
      maxWidth: maxWidth,
      fontSize: 14,
    },
  });

  return <StyledTooltip />;
};

export default HelpTooltip;
