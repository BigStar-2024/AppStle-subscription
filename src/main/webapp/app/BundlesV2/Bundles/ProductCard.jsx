import React from 'react';
const ProductCard = ({ product, selectedVariant, filterVariantsByAllocationsId }) => {
  return (
    <>
      {product?.images && product?.images.length > 1 && filterVariantsByAllocationsId?.length <= 1 ? (
        <>
          <div className="container">
            {product?.images.map((image, index) => {
              return (
                <div key={index} className={`mySlides mySlides__${product?.id}`} style={{ display: index == 0 ? 'block' : 'none' }}>
                  <img className="vertical-align-middle appstle-image-height" src={image} style={{ width: '100%' }} />
                </div>
              );
            })}
          </div>
        </>
      ) : product?.variants?.length === 1 ? (
        <>
          <div
            style={{
              backgroundImage: `url(${
                product?.featured_image
                  ? product?.featured_image
                  : product?.variants[0]?.featured_image?.src
                  ? product?.variants[0]?.featured_image?.src
                  : require('./BlankImage.jpg')
              })`,
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat',
              backgroundSize: 'cover',
              filter: 'blur(10px)',
              position: 'absolute',
              width: '100%',
              top: 0,
              left: '0',
              zIndex: '0',
              height: '100%',
              display: 'block',
            }}
            className="blurredBackgroundProductImage"
          />
          <img
            src={
              product?.featured_image
                ? product?.featured_image
                : product?.variants[0]?.featured_image?.src
                ? product?.variants[0]?.featured_image?.src
                : require('./BlankImage.jpg')
            }
            alt=""
            class="appstle-image-height as-h-auto as-relative hi"
          />
        </>
      ) : selectedVariant?.featured_image?.src ? (
        <>
          <div
            style={{
              backgroundImage: `url(${
                selectedVariant?.featured_image?.src
                  ? selectedVariant?.featured_image?.src
                  : product?.featured_image
                  ? product?.featured_image
                  : require('./BlankImage.jpg')
              })`,
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat',
              backgroundSize: 'cover',
              filter: 'blur(10px)',
              position: 'absolute',
              width: '100%',
              top: 0,
              left: '0',
              zIndex: '0',
              height: '100%',
              display: 'block',
            }}
            className="blurredBackgroundProductImage"
          />
          <img
            src={
              selectedVariant?.featured_image?.src
                ? selectedVariant?.featured_image?.src
                : product?.featured_image
                ? product?.featured_image
                : require('./BlankImage.jpg')
            }
            alt=""
            class="appstle-image-height as-h-auto as-relative"
          />
        </>
      ) : product?.media && product?.media.length > 0 ? (
        <>
          <div
            style={{
              backgroundImage: `url(${
                product?.media[0]?.preview_image?.src
                  ? product?.media[0]?.preview_image?.src
                  : product?.featured_image
                  ? product?.featured_image
                  : require('./BlankImage.jpg')
              })`,
              backgroundPosition: 'center',
              backgroundRepeat: 'no-repeat',
              backgroundSize: 'cover',
              filter: 'blur(10px)',
              position: 'absolute',
              width: '100%',
              top: 0,
              left: '0',
              zIndex: '0',
              height: '100%',
              display: 'block',
            }}
            className="blurredBackgroundProductImage"
          />
          <img
            src={
              product?.media[0]?.preview_image?.src
                ? product?.media[0]?.preview_image?.src
                : product?.featured_image
                ? product?.featured_image
                : require('./BlankImage.jpg')
            }
            alt=""
            class="appstle-image-height as-h-auto as-relative"
          />
        </>
      ) : (
        <img src={require('./BlankImage.jpg')} alt="" class="appstle-image-height as-h-auto as-relative" />
      )}
    </>
  );
};

export default ProductCard;
