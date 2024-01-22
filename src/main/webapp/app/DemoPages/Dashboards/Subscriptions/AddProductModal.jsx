import React, { useEffect, useState } from 'react';
import {
  Button,
  Col,
  FormGroup,
  Input,
  Label,
  ListGroup,
  ListGroupItem,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row
} from 'reactstrap';
import axios from 'axios';
import { connect } from 'react-redux';
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import { KeyboardArrowDown } from '@mui/icons-material';

const AddProductModal = ({ getPrdVariantOptions, prdVariantOptions, selectedProductVarIds, ...props }) => {
  const {
    buttonLabel,
    className,
    header,
    value,
    totalTitle,
    processing,
    sellingPlanIds,
    isFilterBySellingPlan,
    loadingProductPrice,
    productLoading
  } = props;

  const [prdVariantData, setPrdVariantData] = useState({ products: [], pageInfo: {} });
  const [modal, setModal] = useState(false);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [cursor, setCursor] = useState(null);
  const [allCursorArray, setAllCursorArray] = useState([]);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [variantSeeMoreLoading, setVariantSeeMoreLoading] = useState(false);
  const [productSeeMoreLoading, setProductSeeMoreLoading] = useState(false);

  useEffect(() => {
    getPrdVariantOptions(
      `${cursor}&next=${next}${searchValue ? `&search=${searchValue}` : ``}${sellingPlanIds ? `&sellingPlanIds=${sellingPlanIds}` : ``}`
    );
  }, [searchValue]);

  useEffect(() => {
    if (isFilterBySellingPlan) {
      getPrdVariantOptions(
        `${null}&next=${false}${searchValue ? `&search=${searchValue}` : ``}${sellingPlanIds ? `&sellingPlanIds=${sellingPlanIds}` : ``}`
      );
    }
  }, [sellingPlanIds]);

  useEffect(() => {
    setPrdVariantData({ products: prdVariantOptions?.products, pageInfo: prdVariantOptions?.pageInfo });
    setCursor(prdVariantOptions?.pageInfo?.cursor);
    setNext(prdVariantOptions?.pageInfo?.hasNextPage);
  }, [prdVariantOptions]);

  useEffect(() => {
    if (selectedProductVarIds) {
      setSelectedVariantItems([...selectedProductVarIds]);
    }
  }, [selectedProductVarIds]);

  const toggle = () => setModal(!modal);
  const handleCheck = (data, index, varientIndex, imageSrc, productId, requiresSellingPlan) => {
    AddEditProductVariantLogic(data, selectedVariantItems, imageSrc, productId, requiresSellingPlan);
  };

  const AddEditProductVariantLogic = (data, selectedVariantItems, imageSrc, productId, requiresSellingPlan) => {
    const foundIndexvar = selectedVariantItems.findIndex(({ variantId }) => variantId === data.id);
    if (foundIndexvar !== -1) {
      setSelectedVariantItems(prevItems => prevItems.filter(({ variantId }) => variantId !== data.id));
    } else {
      setSelectedVariantItems(prevItems => [
        ...prevItems,
        {
          variantId: data.id,
          title: data.displayName ? data?.displayName : data?.title,
          price: data.price,
          unitPrice: data.price,
          imageSrc: imageSrc,
          productId: productId,
          quantity: 1,
          requiresSellingPlan: requiresSellingPlan
        }
      ]);
    }
  };

  const handleSeeMore = () => {
    setProductSeeMoreLoading(true);
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(cursor)}&next=${next}${
      searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``
    }${sellingPlanIds ? `&sellingPlanIds=${sellingPlanIds}` : ``}&cacheBuster=${new Date().getTime()}`;
    axios
      .get(requestUrl)
      .then(res => {
        setCursor(res.data.pageInfo?.cursor);
        setNext(res.data.pageInfo?.hasNextPage);
        setPrdVariantData({
          products: [...prdVariantData?.products, ...res?.data?.products],
          pageInfo: { ...res.data.pageInfo }
        });
        setProductSeeMoreLoading(false);
        console.log(res);
      })
      .catch(err => {
        console.log(err);
        setProductSeeMoreLoading(false);
      });
  };

  useEffect(() => {
    console.log(allCursorArray);
    if (!allCursorArray.some(data => data === cursor) && cursor) {
      setAllCursorArray(prev => [...prev, cursor]);
    }
  }, [cursor]);

  const handleSearch = event => {
    setCursor(null);
    setNext(false);
    setSearchValue(event.target.value);
  };

  const handleAdd = () => {
    props.addHandler([...selectedVariantItems]);
    // setSelectedVariantItems([]);
    // setQuantity(1);
    toggle();
  };

  const handleCancel = () => {
    // setSelectedVariantItems([]);
    // setQuantity(1);
    toggle();
  };

  const checkIfProductAlreadySelected = varId => {
    return selectedVariantItems.findIndex(({ variantId }) => variantId === varId) != -1 ? true : false;
  };

  const closeBtn = (
    <button className="close" onClick={handleCancel}>
      &times;
    </button>
  );

  const handleVariantsSeeMore = (prdCursorArrayIndex, varCursor, varNext, currentProductId) => {
    let productCursor = null;
    let hasProductNextPage = false;
    setVariantSeeMoreLoading(true);
    let calculateCurrentProductCursor = parseInt(prdCursorArrayIndex / 10);
    if (calculateCurrentProductCursor > 0) {
      productCursor = allCursorArray[calculateCurrentProductCursor - 1];
      hasProductNextPage = true;
    }
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(productCursor)}&next=${hasProductNextPage}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&variantCursor=${encodeURIComponent(varCursor)}&variantNext=${varNext}`;
    axios
      .get(requestUrl)
      .then(res => {
        console.log(res);
        let prdVariantArray = [...prdVariantData?.products];
        let newUpdatedVariantdata = res.data.products.find(data => data.id === currentProductId);
        if (newUpdatedVariantdata?.variants) {
          prdVariantArray[prdCursorArrayIndex].variants = [
            ...prdVariantArray[prdCursorArrayIndex].variants,
            ...newUpdatedVariantdata?.variants
          ];
          prdVariantArray[prdCursorArrayIndex].pageInfo = newUpdatedVariantdata?.pageInfo;
          setPrdVariantData({ products: [...prdVariantArray], pageInfo: { ...prdVariantData?.pageInfo } });
        }
        setVariantSeeMoreLoading(false);
      })
      .catch(err => {
        console.log(err);
        setVariantSeeMoreLoading(false);
      });
  };

  return (
    <div>
      <Button
        color="primary mb-2"
        onClick={toggle}
        disabled={processing || loadingProductPrice || productLoading}
        style={{ minWidth: '120px' }}
      >
        {processing || loadingProductPrice || productLoading ? (
          <div className="d-flex">
            <div className="appstle_loadersmall" />
            <span className="ml-2"> Please Wait...</span>
          </div>
        ) : (
          <span>{buttonLabel}</span>
        )}
      </Button>

      <Modal isOpen={modal} toggle={toggle} className={className}>
        <ModalHeader toggle={toggle} close={closeBtn}>
          {header}
        </ModalHeader>
        <ModalBody className="multiselect-modal-body">
          <div className="form-group has-search mb-20">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text">
                  <i className="pe-7s-search"></i>
                </span>
              </div>
              <input type="text" className="form-control" placeholder="Search" onChange={handleSearch} value={searchValue} />
            </div>
          </div>
          <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
            {prdVariantData?.products &&
              prdVariantData?.products.map((item, index) => (
                <ListGroupItem className="multiselect-list-item" key={index}>
                  <Row>
                    <Col md={12}>
                      <FormGroup check>
                        <Label className="pt-2">
                          {item?.variants.length === 1 && (
                            <Input
                              id={item.id}
                              type="checkbox"
                              onChange={() => handleCheck(item.variants[0], index, 0, item?.imageSrc, item?.id, item.requiresSellingPlan)}
                              checked={checkIfProductAlreadySelected(item.variants[0]?.id)}
                            />
                          )}
                          <img className=" shadow-sm mr-2 ml-1 rounded" src={item.imageSrc} />
                          {item.title}
                          <br />
                        </Label>
                      </FormGroup>
                    </Col>
                  </Row>
                  <Row>
                    <Col md={2}></Col>
                    <Col md={10} style={{ overflow: 'auto', maxHeight: '400px' }}>
                      {item?.variants?.map(
                        (element, varientIndex) =>
                          element.title != 'Default Title' && (
                            <FormGroup check style={{ paddingLeft: '56px' }} key={varientIndex}>
                              <div className="pt-2" key={varientIndex}>
                                <Input
                                  id={item.id + '-' + varientIndex}
                                  type="checkbox"
                                  onChange={() =>
                                    handleCheck(element, index, varientIndex, item?.imageSrc, item?.id, item.requiresSellingPlan)
                                  }
                                  checked={checkIfProductAlreadySelected(element?.id)}
                                />
                                {/* <img className=" shadow-sm mr-2 ml-1 rounded" src={imgSrc} /> */}
                                <Row className="ml-1">
                                  <Col md={8}>{element.title}</Col>
                                  <Col md={4}>$ {element.price}</Col>
                                </Row>
                              </div>
                            </FormGroup>
                          )
                      )}
                      <div>
                        {item?.variants.length > 1 && item?.pageInfo?.hasNextPage ? (
                          variantSeeMoreLoading ? (
                            <div className="d-flex">
                              <div className="appstle_loadersmall" style={{ margin: '3px' }} />
                              <span className="ml-2"> Please Wait..</span>
                            </div>
                          ) : (
                            <a
                              href="javascript:void(0)"
                              onClick={() => handleVariantsSeeMore(index, item?.pageInfo?.cursor, item?.pageInfo?.hasNextPage, item.id)}
                            >
                              <KeyboardArrowDown /> View more variants
                            </a>
                          )
                        ) : null}
                      </div>
                    </Col>
                  </Row>
                </ListGroupItem>
              ))}
            {next ? (
              <Button color="primary" className="mx-auto mt-2" disabled={productSeeMoreLoading} onClick={handleSeeMore}>
                {productSeeMoreLoading ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall" style={{ margin: '3px' }} />
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  'See More...'
                )}
              </Button>
            ) : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter className="d-block">
          <div className="d-flex">
            <div className="mr-auto p-2">{`${selectedVariantItems.length} ${totalTitle} selected`}</div>
            <div>
              <Button className="mr-2" outline onClick={handleCancel}>
                Cancel
              </Button>
              <Button color="primary" disabled={!selectedVariantItems.length} onClick={handleAdd}>
                {' '}
                Add{' '}
              </Button>
            </div>
          </div>
        </ModalFooter>
      </Modal>
    </div>
  );
};

const mapStateToProps = storeState => ({
  prdVariantOptions: storeState.prdVariant.prdVariantOptions,
  productLoading: storeState.prdVariant.loading
});

const mapDispatchToProps = {
  getPrdVariantOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(AddProductModal);
