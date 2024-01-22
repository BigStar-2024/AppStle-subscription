import React from 'react';
import { Swiper, SwiperSlide } from 'swiper/react';
import 'swiper/swiper-bundle.min.css';
import 'swiper/swiper.min.css';
import './imageSwiper.css';
import { Navigation, Pagination } from 'swiper';

const ImageSwiper = ({ images, currentVariant }) => {
  return (
    <Swiper navigation={true} modules={[Navigation, Pagination]} pagination={{ clickable: true }} className="mySwiper">
      {images?.length ? (
        images?.map(item => {
          return (
            <SwiperSlide>
              <img src={item.src} />
            </SwiperSlide>
          );
        })
      ) : (
        <SwiperSlide>
          <img src={currentVariant.image} />
        </SwiperSlide>
      )}
    </Swiper>
  );
};

export default ImageSwiper;
