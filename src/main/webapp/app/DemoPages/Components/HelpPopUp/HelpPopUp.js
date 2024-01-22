import React, {useEffect, useState} from 'react';
import "./HelpPopUp.css"
import { BsFillPlayFill } from "react-icons/bs";
import { FaChevronDown } from "react-icons/fa";
import { MdClose } from "react-icons/md";
import {updateEntity} from "app/entities/shop-info/shop-info.reducer";
import {connect} from "react-redux";

const HelpPopUp = ({onBoardingFlag, children, defaultOpen, ...props}) => {
  const [open, setOpen] = useState(false)

  useEffect(() => {
    if(onBoardingFlag === false && onBoardingFlag !== undefined && onBoardingFlag !== null){
      setOpen(true)
    }
  },[onBoardingFlag])

  useEffect(() => {
     if(defaultOpen){
      setOpen(true);
    } else if(onBoardingFlag == undefined && onBoardingFlag !== false){
      setOpen(false);
    }
  },[defaultOpen])

  const handlePopUp = () => {
    setOpen(!open);
    if(onBoardingFlag === false){
      const entity = {...props.shopInfo};
      entity.onboardingSeen = true;
      props.updateEntity(entity);
    }
  }

  return (
    <div className="action">
      <div className={`floating__button ${open && "shadow-lg"}`} onClick={handlePopUp}>
        {open ? <FaChevronDown className={`mr-1 ${open && "arrow__icon"}`} size={20}/> :
          <BsFillPlayFill className={`${!open && "play__icon"}`} size={26}/>}
        <span className="floating__icon__text">Help Videos</span>
      </div>
      {
        open && (
          <div className={`content overflow-auto ${open && "shadow-lg content_animate_in"}`}>
            <div className="d-flex justify-content-between">
              <h3/>
              <MdClose size={22} onClick={handlePopUp} className="close__icon"/>
            </div>
            <div className="mt-2">
                {children}
            </div>
          </div>
        )
      }
    </div>
  );
};

const mapStateToProp = state => ({
  shopInfo: state.shopInfo.entity
});

const mapDispatchToProps = {
  updateEntity
};

export default connect(mapStateToProp, mapDispatchToProps)(HelpPopUp);
