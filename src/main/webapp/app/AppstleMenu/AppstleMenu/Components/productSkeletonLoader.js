import React from 'react';

const ProductSkeletonLoader = ({skeletonCount}) => {
  return (
    <div className="as-grid as-grid-cols-2 xs:as-grid-cols-1 sm:as-grid-cols-2 md:as-grid-cols-2 lg:as-grid-cols-2 xl:as-grid-cols-2 2xl:as-grid-cols-2 as-gap-5 as-place-items-center">
      {skeletonCount.map((value, index) => (
        <div key={index} className={"as-bg-white as-rounded as-mx-auto as-rounded-2xl as-shadow-lg"} style={{width: "100%"}}>
          <div className="as-h-48 as-p-3 as-bg-gray-200 as-rounded-lg as-overflow-hidden as-animate-pulse" style={{animationDuration: '0.9s'}}>&nbsp;</div>
          <div className="as-p-3">
            <div className="as-h-8 as-w-8/12 as-bg-gray-200 as-rounded as-animate-pulse" style={{animationDuration: '0.7s'}}>&nbsp;</div>
            <div className="as-h-5 as-w-5/12 as-mt-2 as-bg-gray-200 as-rounded as-animate-pulse" style={{animationDuration: '0.7s'}}>&nbsp;</div>
            <div className="as-h-4 as-w-6/12 as-mt-2 as-bg-gray-200 as-rounded as-animate-pulse" style={{animationDuration: '0.6s'}}>&nbsp;</div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ProductSkeletonLoader;
