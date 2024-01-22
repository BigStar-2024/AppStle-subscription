import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  ListGroup,
  ListGroupItem,
  Input,
  Label,
  Row,
  Col,
  FormGroup,
  FormText,
  Badge
} from 'reactstrap';
import { KeyboardArrowDown } from '@mui/icons-material';
import Select from 'react-select';

import { connect } from 'react-redux';
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import { getProductFilterData } from '../../../../entities/product-info/product-info.reducer';
import Loader from 'react-loaders';

const ProductVariantModal = ({
  getPrdVariantOptions,
  prdVariantOptions,
  getProductFilterData,
  productFilterData,
  isCollectionButtonEnable,
  loadingProductFilter,
  prdVariantLoading,
  ...props
}) => {
  const {
    buttonLabel,
    className,
    header,
    selectedProductIds,
    selectedProductVarIds,
    bundleLevel,
    checkProductStatus
  } = props;

  const [prdVariantData, setPrdVariantData] = useState({ products: [], pageInfo: {} });
  const [modal, setModal] = useState(false);
  const [selectedItems, setSelectedItems] = useState(selectedProductIds ? selectedProductIds : []);
  const [selectedVariantItems, setSelectedVariantItems] = useState(selectedProductVarIds ? selectedProductVarIds : []);
  // const [fields, setFields] = useState(value ? JSON.parse(value) : []);
  const [cursor, setCursor] = useState(null);
  const [allCursorArray, setAllCursorArray] = useState([]);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [variantSeeMoreLoading, setVariantSeeMoreLoading] = useState(false);
  const [productSeeMoreLoading, setProductSeeMoreLoading] = useState(false);
  const [selectedFilterData, setSelectedFilterData] = useState({ vendor: [], tags: [], productType: [], collection: [] });
  const [filterURL, setFilterURL] = useState('');
  const [isProductFilterByCollection, setIsProductFilterByCollection] = useState(false);
  const [isCollectionOpen, setIsCollectionOpen] = useState(false);
  const [addColllectionAllProdLoading, setAddColllectionAllProdLoading] = useState(false);

  useEffect(() => {
    getProductFilterData();
  }, []);

  useEffect(() => {
    if (isCollectionButtonEnable) {
      setIsProductFilterByCollection(isCollectionButtonEnable);
    }
  }, [isCollectionButtonEnable]);

  const onChangeFilterData = (value, filterType) => {
    if (filterType == 'collection') {
      resetProductVariantCursor();
      setSelectedFilterData({ vendor: [], tags: [], productType: [], collection: [value] });
    } else {
      setSelectedFilterData({ ...selectedFilterData, [filterType]: value, collection: [] });
    }
  };

  useEffect(() => {
    let tempURL = '';
    for (var key in selectedFilterData) {
      if (selectedFilterData?.hasOwnProperty(key)) {
        var val = selectedFilterData[key];
        if (val && val?.length > 0) {
          tempURL += '&' + key + '=' + val?.map(k => k?.value)?.join(',');
        }
      }
    }
    setFilterURL(encodeURI(tempURL));
  }, [selectedFilterData]);

  useEffect(() => {
    // setFields(selectedProductIds ? [...JSON.parse(selectedProductIds)] : []);
    setSelectedItems(selectedProductIds ? [...selectedProductIds] : []);
    checkProductStatus(selectedProductIds ? [...selectedProductIds] : []);
  }, [selectedProductIds]);

  useEffect(() => {
    // setFields(selectedProductVarIds ? [...JSON.parse(selectedProductVarIds)] : []);
    setSelectedVariantItems(selectedProductVarIds ? [...selectedProductVarIds] : []);
  }, [selectedProductVarIds]);

  useEffect(() => {
    getPrdVariantOptions(`${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}${filterURL}`);
  }, [searchValue, filterURL]);

  useEffect(() => {
    setPrdVariantData({ products: prdVariantOptions?.products, pageInfo: prdVariantOptions?.pageInfo });
    setCursor(prdVariantOptions?.pageInfo?.cursor);
    setNext(prdVariantOptions?.pageInfo?.hasNextPage);
  }, [prdVariantOptions]);

  const toggle = isCollection => {
    if (!modal && isCollection) {
      let collection = [];
      if (productFilterData?.collection?.length > 0) {
        let t = productFilterData?.collection[0];
        collection = [{ label: t?.title, value: t?.id }];
      }
      if (collection?.length > 0 && collection[0]?.value != selectedFilterData?.collection[0]?.value) {
        resetProductVariantCursor();
      }
      setSelectedFilterData({ vendor: [], tags: [], productType: [], collection: collection });
    } else if (!modal) {
      setSelectedFilterData({ vendor: [], tags: [], productType: [], collection: [] });
    }
    setIsCollectionOpen(!modal ? isCollection : false);
    setSearchValue('');
    setModal(!modal);
  };

  useEffect(() => {
    if (isCollectionOpen && modal && productFilterData && productFilterData?.collection?.length > 0) {
      let t = productFilterData?.collection[0];
      let collection = [{ label: t?.title, value: t?.id }];
      if (collection?.length > 0 && collection[0]?.value != selectedFilterData?.collection[0]?.value) {
        resetProductVariantCursor();
      }
      setSelectedFilterData({ vendor: [], tags: [], productType: [], collection: collection });
    }
  }, [productFilterData]);

  const resetProductVariantCursor = () => {
    setCursor(null);
    setNext(false);
    setPrdVariantData({ products: [], pageInfo: {} });
  };

  const handleCheck = (data, index, varientIndex, imageSrc, productHandle) => {
    const targetItem = prdVariantData?.products[index];
    const isVariant = (bundleLevel === 'VARIANT' && data?.variants?.length === 1);
    if (isVariant) {
      varientIndex = 0;
      data = isVariant ? data?.variants[0] : data;
    }
    if (varientIndex === null) {
      const foundIndexItem = selectedItems.findIndex(({ id }) => id === data.id);
      if (foundIndexItem !== -1) {
        setSelectedItems(prevItems => prevItems.filter(({ id }) => id !== data.id));
      } else {
        setSelectedItems(prevItems => [
          ...prevItems,
          {
            id: data.id,
            type: 'PRODUCT',
            title: data.displayName ? data?.displayName : data?.title,
            price: data.price,
            imageSrc: imageSrc,
            productHandle: productHandle,
            status:data.status
          }
        ]);
      }
    } else {
        const foundIndexvar = selectedVariantItems.findIndex(({ id }) => id === data.id);
            if (foundIndexvar == -1) {
              setSelectedVariantItems(prevItems => [
                ...prevItems,
                {
                id: data.id,
                type: 'VARIANT',
                title: data.displayName ? data?.displayName : data?.title,
                price: data?.price,
                  imageSrc: imageSrc,
                productHandle: productHandle
                }
              ]);
          } else {
            setSelectedVariantItems(prevItems => prevItems.filter(({ id }) => id !== data.id));
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
    }${filterURL}&cacheBuster=${new Date().getTime()}`;
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
    }&variantCursor=${encodeURIComponent(varCursor)}&variantNext=${varNext}${filterURL}`;
    axios
      .get(requestUrl)
      .then(res => {
        console.log(res);
        let prdVariantArray = [...prdVariantData?.products];
        let newUpdatedVariantdata = res.data.products.find(data => data.id === currentProductId);
        if (newUpdatedVariantdata?.variants) {
          prdVariantArray[prdCursorArrayIndex].variants = [
            ...prdVariantArray[prdCursorArrayIndex].variants,
            ...newUpdatedVariantdata?.variants,
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

  const handleAdd = () => {
    onChangeProduct([...selectedItems], [...selectedVariantItems]);
    toggle(isCollectionOpen);
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
      return selectedVariantItems.findIndex(({ id }) => id === varId) != -1 ? true : false;
    } else if (prdId) {
      return selectedItems.findIndex(({ id }) => id === prdId) != -1 ? true : false;
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
      <Button color="primary mt-2 mr-1" onClick={() => toggle(false)}>
        {buttonLabel}
      </Button>
      {isProductFilterByCollection && (
        <Button color="primary mt-2" onClick={() => toggle(true)}>
          {buttonLabel} by Collection
        </Button>
      )}
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
        <ModalHeader toggle={() => toggle(isCollectionOpen)}>{header}</ModalHeader>
        <ModalBody className="multiselect-modal-body">
          {isCollectionOpen ? (
            <div className="form-group has-search mb-20">
              <div className="input-group">
                <Row className="w-100">
                  {productFilterData && productFilterData?.productType && productFilterData?.productType?.length > 0 && (
                    <Col md={12} className="py-2">
                      <Label className="mb-0">Select Collection</Label>
                      <Select
                        options={productFilterData?.collection?.map(value => {
                          return { label: value?.title, value: value?.id };
                        })}
                        value={selectedFilterData['collection']?.length > 0 ? selectedFilterData['collection'][0] : null}
                        onChange={e => onChangeFilterData(e, 'collection')}
                        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500 focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
                      />
                    </Col>
                  )}
                </Row>
              </div>
            </div>
          ) : (
            <div className="form-group has-search mb-20">
              <div className="input-group">
                <div className="input-group-prepend">
                  <span className="input-group-text">
                    <i className="pe-7s-search"></i>
                  </span>
                </div>
                <input type="text" className="form-control" placeholder="Search" onChange={handleSearch} />
              </div>
              <div className="input-group">
                <Row className="w-100">
                  {productFilterData && productFilterData?.productType && productFilterData?.productType?.length > 0 && (
                    <Col md={4} className="py-2">
                      <Label className="mb-0">Select Product Type</Label>
                      <Select
                        options={productFilterData?.productType?.map(value => {
                          return { label: value, value: value };
                        })}
                        isMulti={true}
                        value={selectedFilterData['productType']}
                        onChange={e => onChangeFilterData(e, 'productType')}
                        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
                      />
                    </Col>
                  )}
                  {productFilterData && productFilterData?.vendor && productFilterData?.vendor?.length > 0 && (
                    <Col md={4} className="py-2">
                      <Label className="mb-0">Select Vendor</Label>
                      <Select
                        options={productFilterData?.vendor?.map(value => {
                          return { label: value, value: value };
                        })}
                        isMulti={true}
                        value={selectedFilterData['vendor']}
                        onChange={e => onChangeFilterData(e, 'vendor')}
                        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
                      />
                    </Col>
                  )}
                  {productFilterData && productFilterData?.tags && productFilterData?.tags?.length > 0 && (
                    <Col md={4} className="py-2">
                      <Label className="mb-0">Select Tags</Label>
                      <Select
                        options={productFilterData?.tags?.map(value => {
                          return { label: value, value: value };
                        })}
                        isMulti={true}
                        value={selectedFilterData['tags']}
                        onChange={e => onChangeFilterData(e, 'tags')}
                        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500   focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
                      />
                    </Col>
                  )}
                </Row>
              </div>
            </div>
          )}
          {loadingProductFilter || prdVariantLoading ? (
            <>
              <div className="d-flex justify-content-center align-items-center">
                <Loader type="line-scale" />
              </div>
            </>
          ) : (
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
                            <img className=" shadow-sm mr-2 ml-1 rounded" src={item.imageSrc ?? require('./BlankImage.jpg')} />
                            {item.title}
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
                                  disabled={bundleLevel === 'PRODUCT'}
                                    id={item.id + '-' + varientIndex}
                                    type="checkbox"
                                  onChange={() => handleCheck(element, index, varientIndex, item?.imageSrc, item?.variants[0]?.productHandle)}
                                    checked={checkIfProductAlreadySelected(item?.id, element?.id)}
                                  />
                                  <Row className="ml-1">
                                    <Col md={8}>{element.title}</Col>
                                    <Col md={4}>$ {element.price}</Col>
                                  </Row>
                                </div>
                              </FormGroup>
                          ),
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
                                onClick={() => handleVarinatSeeMore(index, item?.pageInfo?.cursor, item?.pageInfo?.hasNextPage, item.id)}
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
              <hr />
              {next ? (
                <Button color="primary" className="mx-auto mt-2" onClick={handleSeeMore}>
                  {productSeeMoreLoading ? (
                    <div className="d-flex">
                      <div className="appstle_loadersmall" />
                      <span className="ml-2"> Please Wait..</span>
                    </div>
                  ) : (
                    <>
                      <KeyboardArrowDown /> View More Products
                    </>
                  )}
                </Button>
              ) : null}
            </ListGroup>
          )}
        </ModalBody>
        <ModalFooter className="d-block">
          <div className="d-flex">
            <div className="mr-auto p-2">{`${selectedItems?.length} product(s) and ${selectedVariantItems?.length} variant(s) selected`}</div>
            <div>
              <Button className="mr-2" outline onClick={() => toggle(isCollectionOpen)}>
                Cancel
              </Button>
              <Button
                className="ml-2"
                color="primary"
                disabled={!(selectedItems.length || selectedVariantItems.length)}
                onClick={handleAdd}
              >
                Add
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
  prdVariantOptions: storeState.prdVariant.prdVariantOptions,
  prdVariantLoading: storeState.prdVariant.loading,
  productFilterData: storeState.productInfo.productFilterData,
  loadingProductFilter: storeState.productInfo.loadingProductFilter
});

const mapDispatchToProps = {
  getPrdVariantOptions,
  getProductFilterData
};

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantModal);
