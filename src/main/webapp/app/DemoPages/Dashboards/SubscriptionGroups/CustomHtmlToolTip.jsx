import React from 'react';
import { styled } from '@mui/material/styles';
import Tooltip, { tooltipClasses } from '@mui/material/Tooltip';

const CustomHtmlToolTip = styled(({ className, ...props }) => <Tooltip {...props} classes={{ popper: className }} />)({
  [`& .${tooltipClasses.tooltip}, & .${tooltipClasses.tooltip} div`]: {
    backgroundColor: '#337AB7',
    color: '#fff',
    maxWidth: 300,
    fontSize: 14,
  },
});

export default CustomHtmlToolTip;
