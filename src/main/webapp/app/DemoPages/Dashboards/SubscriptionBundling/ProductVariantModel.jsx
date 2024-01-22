import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {
  Badge,
  Button,
  Col,
  FormGroup,
  FormText,
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
import {KeyboardArrowDown} from '@mui/icons-material';

import {connect} from 'react-redux';
import {getPrdVariantOptions} from 'app/entities/fields-render/data-product-variant.reducer';

const ProductVariantModal = ({getPrdVariantOptions, prdVariantOptions, ...props}) => {
  const {
    buttonLabel,
    className,
    header,
    selectedProductIds,
    selectedProductVarIds,
    bundleLevel,
    checkProductStatus
  } = props;

  const [prdVariantData, setPrdVariantData] = useState({products: [], pageInfo: {}});
  const [modal, setModal] = useState(false);
  const [selectedItems, setSelectedItems] = useState([]);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);
  const [cursor, setCursor] = useState(null);
  const [allCursorArray, setAllCursorArray] = useState([]);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [variantSeeMoreLoading, setVariantSeeMoreLoading] = useState(false);
  const [productSeeMoreLoading, setProductSeeMoreLoading] = useState(false);

  useEffect(() => {
    setSelectedItems([...selectedProductIds]);
    checkProductStatus(selectedProductIds);
  }, [selectedProductIds]);

  useEffect(() => {
    setSelectedVariantItems([...selectedProductVarIds]);
  }, [selectedProductVarIds]);

  useEffect(() => {
    getPrdVariantOptions(`${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}`);
  }, [searchValue]);

  useEffect(() => {
    setPrdVariantData({products: prdVariantOptions?.products, pageInfo: prdVariantOptions?.pageInfo});
    setCursor(prdVariantOptions?.pageInfo?.cursor);
    setNext(prdVariantOptions?.pageInfo?.hasNextPage);
  }, [prdVariantOptions]);

  const toggle = () => {
    setSearchValue('');
    setModal(!modal);
  };

  const handleCheck = (data, index, varientIndex, imageSrc, productHandle) => {
    const targetItem = prdVariantData?.products[index];
    const isVariant = (bundleLevel === 'VARIANT' && data?.variants?.length === 1);
    if (isVariant) {
      varientIndex = 0;
      data = isVariant ? data?.variants[0] : data;
    }
    if (varientIndex === null) {
      const foundIndexItem = selectedItems.findIndex(({id}) => id === data.id);
      if (foundIndexItem !== -1) {
        setSelectedItems(prevItems => prevItems.filter(({id}) => id !== data.id));
      } else {
        setSelectedItems(prevItems => [
          ...prevItems,
          {
            id: data.id,
            type: 'PRODUCT',
            title: data.displayName ? data?.displayName : data?.title,
            imageSrc: imageSrc,
            productHandle: productHandle,
            status: data.status
          }
        ]);
      }
    } else {
      const foundIndexvar = selectedVariantItems.findIndex(({id}) => id === data.id);
      if (foundIndexvar == -1) {
        setSelectedVariantItems(prevItems => [
          ...prevItems,
          {
            id: data.id,
            type: 'VARIANT',
            title: data.displayName ? data?.displayName : data?.title,
            imageSrc: imageSrc,
            productHandle: productHandle
          }
        ]);
      } else {
        setSelectedVariantItems(prevItems => prevItems.filter(({id}) => id !== data.id));
      }
    }
  };

  const handleSearch = event => {
    setCursor(null);
    setNext(false);
    setSearchValue(event.target.value);
  };

  const handleSeeMore = () => {
    setProductSeeMoreLoading(true);
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(cursor)}&next=${next}${
      searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``
    }&cacheBuster=${new Date().getTime()}`;
    axios
      .get(requestUrl)
      .then(res => {
        setCursor(res.data.pageInfo?.cursor);
        setNext(res.data.pageInfo?.hasNextPage);
        setPrdVariantData({
          products: [...prdVariantData?.products, ...res?.data?.products],
          pageInfo: {...res.data.pageInfo}
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
    if (!allCursorArray.some(data => data === cursor) && cursor) {
      setAllCursorArray(prev => [...prev, cursor]);
    }
  }, [cursor]);

  const handleVarinatSeeMore = (prdCursorArrayIndex, varCursor, varNext, currentProductId) => {
    let productCursor = null;
    let hasProductNextPage = false;
    setVariantSeeMoreLoading(true);
    let calculateCurrentProductCursor = parseInt(prdCursorArrayIndex / 10);
    if (calculateCurrentProductCursor > 0) {
      productCursor = allCursorArray[calculateCurrentProductCursor - 1];
      hasProductNextPage = true;
    }
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(productCursor)}&next=${hasProductNextPage}${
      searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``
    }&variantCursor=${encodeURIComponent(varCursor)}&variantNext=${varNext}`;
    axios
      .get(requestUrl)
      .then(res => {
        console.log(res);
        let prdVariantArray = [...prdVariantData?.products];
        // let newUpdatedVariantdata = prdVariantArray[prdCursorArrayIndex];
        let newUpdatedVariantdata = res.data.products.find(data => data.id === currentProductId);
        if (newUpdatedVariantdata?.variants) {
          prdVariantArray[prdCursorArrayIndex].variants = [
            ...prdVariantArray[prdCursorArrayIndex].variants,
            ...newUpdatedVariantdata?.variants
          ];
          prdVariantArray[prdCursorArrayIndex].pageInfo = newUpdatedVariantdata?.pageInfo;
          setPrdVariantData({products: [...prdVariantArray], pageInfo: {...prdVariantData?.pageInfo}});
        }
        setVariantSeeMoreLoading(false);
      })
      .catch(err => {
        console.log(err);
        setVariantSeeMoreLoading(false);
      });
  };

  const handleAdd = () => {
    onChangeProduct([...selectedItems], [...selectedVariantItems]);
    toggle();
    checkProductStatus(selectedItems);
  };

  const onChangeProduct = (prdData, varData) => {
    props.onChange(prdData, varData);
  };

  const remove = (evt, id) => {
    evt.preventDefault();
    let productsCopy = selectedItems.filter(product => product.id !== id);
    let varsCopy = selectedVariantItems.filter(varData => varData.id !== id);
    setSelectedItems([...productsCopy]);
    setSelectedVariantItems([...varsCopy]);
    onChangeProduct([...productsCopy], [...varsCopy]);
  };

  const checkIfProductAlreadySelected = (prdId, varId) => {
    if (varId) {
      return selectedVariantItems.findIndex(({id}) => id === varId) != -1 ? true : false;
    } else if (prdId) {
      return selectedItems.findIndex(({id}) => id === prdId) != -1 ? true : false;
    }
  };

  return (
    <div>
      <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
        {!modal && selectedItems.length === 0 && selectedVariantItems.length === 0 ? (
          <ListGroupItem className="multiselect-list-item">No products in selection.</ListGroupItem>
        ) : null}
        {!modal &&
          selectedItems?.map((f, index) => (
            <ListGroupItem className="multiselect-list-item" key={f.id}>
              <span className="product-id">{`#${f.id}`} &nbsp;</span>
              <span>{f.title}</span>
              <Button close onClick={evt => remove(evt, f.id)}></Button>
            </ListGroupItem>
          ))}

        {!modal &&
          selectedVariantItems?.map((f, index) => (
            <ListGroupItem className="multiselect-list-item" key={f.id}>
              <div>
                <span className="product-id">{`#${f.id}`} &nbsp;</span>
                <span>{f.title}</span>
                <Button close onClick={evt => remove(evt, f.id)}></Button>
              </div>
            </ListGroupItem>
          ))}
      </ListGroup>
      <Button color="primary mt-2" onClick={toggle}>
        {buttonLabel}
      </Button>

      <>
        {selectedItems.length === 0 && selectedVariantItems.length === 0 ? (
          <p className="mt-3">
            <Badge className="tag-badge-readonly toRemove ml-0 font-size-badge pb-2"> incomplete </Badge>
            <FormText className="d-inline-block">
              <span className="text-danger">Please ensure you complete all the fields</span>
            </FormText>
          </p>
        ) : (
          ''
        )}
      </>
      <Modal isOpen={modal} className={className} size="lg">
        <ModalHeader toggle={toggle}>{header}</ModalHeader>
        <ModalBody className="multiselect-modal-body">
          <div className="form-group has-search mb-20">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text">
                  <i className="pe-7s-search"></i>
                </span>
              </div>
              <input type="text" className="form-control" placeholder="Search" onChange={handleSearch}/>
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
                          <Input
                            disabled={bundleLevel === 'VARIANT' && item?.variants?.length > 1}
                            id={item.id}
                            type="checkbox"
                            onChange={() => handleCheck(item, index, null, item?.imageSrc, item?.variants[0]?.productHandle)}
                            checked={checkIfProductAlreadySelected(item?.id, (bundleLevel === 'VARIANT' && item?.variants?.length === 1) ? item.variants[0]?.id : null)}
                          />
                          <img className=" shadow-sm mr-2 ml-1 rounded"
                               src={item.imageSrc ?? require('./BlankImage.jpg')}/>
                          {item.title}
                        </Label>
                      </FormGroup>
                    </Col>
                  </Row>
                  <Row>
                    <Col md={2}></Col>
                    <Col md={10} style={{overflow: 'auto', maxHeight: '400px'}}>
                      {item?.variants?.map(
                        (element, varientIndex) =>
                          element.title != 'Default Title' && (
                            <FormGroup check style={{paddingLeft: '56px'}} key={varientIndex}>
                              <div className="pt-2" key={varientIndex}>
                                <Input
                                  disabled={bundleLevel === 'PRODUCT'}
                                  id={item.id + '-' + varientIndex}
                                  type="checkbox"
                                  onChange={() => handleCheck(element, index, varientIndex, element?.imageSrc, item?.variants[0]?.productHandle)}
                                  checked={checkIfProductAlreadySelected(item?.id, element?.id)}
                                />
                                <Row className="ml-1">
                                  <Col md={8}>{element.title}</Col>
                                </Row>
                              </div>
                            </FormGroup>
                          )
                      )}
                      <div>
                        {item?.variants.length > 1 && item?.pageInfo?.hasNextPage ? (
                          variantSeeMoreLoading ? (
                            <div className="d-flex">
                              <div className="appstle_loadersmall" style={{margin: '3px'}}/>
                              <span className="ml-2"> Please Wait..</span>
                            </div>
                          ) : (
                            <a
                              href="javascript:void(0)"
                              onClick={() => handleVarinatSeeMore(index, item?.pageInfo?.cursor, item?.pageInfo?.hasNextPage, item.id)}
                            >
                              <KeyboardArrowDown/> View more variants
                            </a>
                          )
                        ) : null}
                      </div>
                    </Col>
                  </Row>
                </ListGroupItem>
              ))}
            <hr/>
            {next ? (
              <Button color="primary" className="mx-auto mt-2" onClick={handleSeeMore}>
                {productSeeMoreLoading ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall"/>
                    <span className="ml-2"> Please Wait..</span>
                  </div>
                ) : (
                  <>
                    <KeyboardArrowDown/> View More Products
                  </>
                )}
              </Button>
            ) : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter className="d-block">
          <div className="d-flex">
            <div
              className="mr-auto p-2">{`${selectedItems?.length} product(s) and ${selectedVariantItems?.length} variant(s) selected`}</div>
            <div>
              <Button className="mr-2" outline onClick={toggle}>
                Cancel
              </Button>
              <Button color="primary" disabled={!(selectedItems.length || selectedVariantItems.length)}
                      onClick={handleAdd}>
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
  triggerRuleEntity: storeState.triggerRule.entity,
  prdVariantOptions: storeState.prdVariant.prdVariantOptions
});

const mapDispatchToProps = {
  getPrdVariantOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantModal);
