import React, {useEffect, useState} from 'react';
import {Button, Card, Col} from "reactstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMinus, faPlus} from "@fortawesome/free-solid-svg-icons";

function EditSingleBundleProductContent(props) {
  const {
    product,
    renderSelectedProduct,
    handleAddRemoveProduct,
    entity,
    handleChangeEditProductQuantity,
    totalProductsAdded,
    applicableSettings,
    isProductSwap,
    prodcutToSwap,
    removeSwapProduct,
  } = props;

  const [qty, setQty] = useState(entity?.quantity || 0)
  const [isProductSwapClick, setIsProductSwapClick] =useState(false)

  useEffect(() => {
    if(!isProductSwap){
      if (qty === 0) {
        handleAddRemoveProduct(false, product)
      } else {
        handleChangeEditProductQuantity(qty, product);
      }
    }
  }, [qty])

  return (
    <Col md={6} sm={6} xs={6}>
      <Card className={"btn-shadow mt-2 mb-2 rounded"}>
        <div class="d-flex justify-content-center rounded">
          <img src={product?.imageSrc} alt="" class="w-100 relative"/>
        </div>
        <div class="mt-4 mb-6">
          <h6 className={"text-center text-body btn-shadow "} style={{color: "black"}}>
            <a href={`#`} target="_blank">
              {product?.title}
            </a>
          </h6>
        </div>
        { !isProductSwap ? 
        <div className="d-flex justify-content-center align-items-center mb-3 mt-auto px-6">
          {qty === 0 ? <>
              <Button
                className={"btn btn-primary btn-shadow primary"}
                size={"small"}
                color={"primary"}
                data-action="increment"
                disabled={totalProductsAdded >= (applicableSettings.maxQuantity ? applicableSettings.maxQuantity : 99999999999999999)}
                onClick={() => {
                  handleAddRemoveProduct(true, product)
                  setQty(1)
                }}
              >
                {'Add'}
              </Button>
            </> :


            <>
              <Button
                style={{
                  padding: '3px 6px 3px 6px',
                  borderRadius: '100%'
                }}
                color={"primary"}
                size={"small"}
                data-action="decrement"
                className={"btn btn-primary btn-shadow primary"}
                onClick={() => {
                  setQty(old => old - 1 >= 0 ? old - 1 : 0)
                }}
              >
                <FontAwesomeIcon icon={faMinus}/>
              </Button>
              <div
                className="pl-3 pr-3 font-weight-bold"
                name="custom-input-number"
              >
                {qty || 0}
              </div>
              <Button
                style={{
                  padding: '3px 6px 3px 6px',
                  borderRadius: '100%'
                }}
                data-action="increment"
                color={"primary"}
                size={"small"}
                className={"btn btn-primary btn-shadow primary"}
                disabled={totalProductsAdded >= (applicableSettings.maxQuantity ? applicableSettings.maxQuantity : 99999999999999999)}
                onClick={() => {
                  setQty(old => old + 1)
                }}
              >
                <FontAwesomeIcon icon={faPlus}/>
              </Button>
            </>}
        </div> :
          <div className="d-flex justify-content-center align-items-center mb-3 mt-auto px-6">
             
              <button
                className={"btn btn-primary btn-shadow primary"}
                onClick={() => {
                  setIsProductSwapClick(true)
                  removeSwapProduct(prodcutToSwap.product, product)                       
                }}
              >
                 {isProductSwapClick ? <div className="appstle_loadersmall"/> :
                'Confirm Swap' }
              </button> 
          </div>
  }

      </Card>
    </Col>
  );
}

export default EditSingleBundleProductContent;
