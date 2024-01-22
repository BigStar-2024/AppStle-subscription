import React from 'react';
import HelpPopUp from 'app/DemoPages/Components/HelpPopUp/HelpPopUp';

const HelpPopupItem = ({title, src}: {title: string, src: string}) => (
    <div className="py-4 border-bottom">
      <h5>{title}</h5>
      <div className="embed-responsive embed-responsive-21by9 h-75 mt-3">
        <iframe
          width="560"
          height="315"
          src={src}
          title="YouTube video player"
          frameBorder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
        ></iframe>
      </div>
    </div>
)

const CreateSubscriptionGroupHelpPopup = () => (
  <HelpPopUp>
    <HelpPopupItem title="Pay As You Go" src="https://www.youtube.com/embed/AKR4gfRdEMU"/>
    <HelpPopupItem title="Prepaid Auto Renew" src="https://www.youtube.com/embed/P0ncMfwwz_s"/>
    <HelpPopupItem title="Date-Picker" src="https://www.youtube.com/embed/f6DuDchH-QA"/>
    <HelpPopupItem title="Free Trial" src="https://www.youtube.com/embed/4zR59ga5jKk"/>
    <HelpPopupItem title="Monthly Cutoff Days" src="https://www.youtube.com/embed/FVyb76d7X9Y"/>
    <HelpPopupItem title="Specific Tags" src="https://www.youtube.com/embed/LpEO3tKhcJc"/>
    <HelpPopupItem title="Prepaid One Time" src="https://www.youtube.com/embed/zNJSLOhOJYM"/>
    <HelpPopupItem title="Advanced Features" src="https://www.youtube.com/embed/v1G3_-uhj3M"/>
    <HelpPopupItem title="Weekly Cutoff Days" src="https://www.youtube.com/embed/Sc2DI31ow5o"/>
  </HelpPopUp>
);

export default CreateSubscriptionGroupHelpPopup;
