import React, { Fragment } from 'react';
import Slider from 'react-slick';
import Froonze from './Froonze';
import PageFly from './PageFly';
import ToastiBar from './ToastiBar';
import TxtCart from './TxtCart';
import SkyPilot from './SkyPilot';
import MultiVariants from './MultiVariants';
import MonsterCart from './MonsterCart';
import Dropshipman from './Dropshipman';

const settings = {
  dots: true,
  infinite: true,
  speed: 500,
  slidesToShow: 2,
  slidesToScroll: 2,
  responsive: [
    {
      breakpoint: 992,
      settings: {
        slidesToShow: 2
      }
    },
    {
      breakpoint: 576,
      settings: {
        slidesToShow: 1
      }
    },
    {
      breakpoint: 320,
      settings: {
        slidesToShow: 1
      }
    }
  ]
};

export default function AppSlider() {
  return (
    <Fragment>
      <Slider {...settings}>
        <div>
          <div className="px-3">
            <PageFly />
          </div>
        </div>
        <div>
          <div className="px-3">
            <ToastiBar />
          </div>
        </div>
        <div>
          <div className="px-3">
            <TxtCart />
          </div>
        </div>
        <div>
          <div className="px-3">
            <Froonze />
          </div>
        </div>
        <div>
          <div className="px-3">
            <SkyPilot />
          </div>
        </div>
        <div>
          <div className="px-3">
            <MultiVariants />
          </div>
        </div>
        <div>
          <div className="px-3">
            <MonsterCart />
          </div>
        </div>
        <div>
          <div className="px-3">
            <Dropshipman />
          </div>
        </div>
      </Slider>
    </Fragment>
  );
}
