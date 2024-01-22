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
  Badge,
} from 'reactstrap';
import { KeyboardArrowDown, DragIndicator } from '@mui/icons-material';
import Select from 'react-select';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';

import { connect } from 'react-redux';
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import { getProductFilterData } from '../../../../entities/product-info/product-info.reducer';
import Loader from 'react-loaders';
import { updateProductEntity, deleteProductEntity } from '../../../../entities/subscription-group/subscription-group.reducer';
import { useRouteMatch } from 'react-router-dom';
import { getCollections } from 'app/entities/appstle-menu-settings/appstle-menu-admin.reducer';
import getSymbolFromCurrency from 'currency-symbol-map';

const ProductVariantModalV2 = ({
  getPrdVariantOptions,
  prdVariantOptions,
  getProductFilterData,
  productFilterData,
  isCollectionButtonEnable,
  loadingProductFilter,
  prdVariantLoading,
  updateProductEntity,
  deleteProductEntity,
  updatingProducts,
  selectedProductsAndVariantsData,
  setSelectedProductsAndVariantsData,
  collectionsList,
  ...props
}) => {
  const {
    buttonLabel,
    className,
    header,
    selectedProductIds,
    selectedProductVarIds,
    checkProductStatus,
    selectedProductFilter,
    selectedProductFilterValue,
    productDataInfo,
    loadMoreVariantsData,
    loadMoreProductsData,
    isCreateSubscription,
    productsLoading,
    variantsLoading,
    deleteProductLoading,
    getCollections,
  } = props;

  const [prdVariantData, setPrdVariantData] = useState({ products: [], pageInfo: {} });
  const [modal, setModal] = useState(false);

  const match = useRouteMatch();

  const [selectedItems, setSelectedItems] = useState([]);
  const [selectedVariantItems, setSelectedVariantItems] = useState([]);

  const [preSelectedItems, setPreSelectedItems] = useState(selectedProductIds ? JSON.parse(selectedProductIds) : []);
  const [preSelectedVariantItems, setPreSelectedVariantItems] = useState(selectedProductVarIds ? JSON.parse(selectedProductVarIds) : []);

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
  const [addAllProductsLoading, setAddAllProductsLoading] = useState(false);

  const [updatingProduct, setUpdatingProduct] = useState(false);

  const [collectionOptions, setCollectionOptions] = useState([]);

  const [enableSelectAllProductButton, setEnableSelectAllProductButton] = useState(false);
  const [deleteSelectedIds, setDeleteSelectedIds] = useState({ products: [], variants: [] });

  useEffect(() => {
    setPreSelectedItems(selectedProductIds ? [...JSON.parse(selectedProductIds)] : []);
  }, [selectedProductIds]);

  useEffect(() => {
    setPreSelectedVariantItems(selectedProductVarIds ? [...JSON.parse(selectedProductVarIds)] : []);
  }, [selectedProductVarIds]);

  useEffect(() => {
    getProductFilterData();
  }, []);

  useEffect(() => {
    if (isCollectionButtonEnable) {
      setIsProductFilterByCollection(isCollectionButtonEnable);
    }
  }, [isCollectionButtonEnable]);

  const onChangeFilterData = (value, filterType) => {
    resetProductVariantCursor();
    if (filterType == 'collection') {
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

  // useEffect(() => {
  //   setSelectedItems(selectedProductIds ? [...JSON.parse(selectedProductIds)] : []);
  //   checkProductStatus(selectedProductIds ? [...JSON.parse(selectedProductIds)] : []);
  // }, [selectedProductIds]);

  // useEffect(() => {
  //   setSelectedVariantItems(selectedProductVarIds ? [...JSON.parse(selectedProductVarIds)] : []);
  // }, [selectedProductVarIds]);

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
      setCollectionOptions([...productFilterData?.collection?.map(value => ({ label: value?.title, value: value?.id }))]);
    }
  }, [productFilterData]);

  const resetProductVariantCursor = () => {
    setCursor(null);
    setNext(false);
    setPrdVariantData({ products: [], pageInfo: {} });
  };

  const handleCheck = (data, index, variantIndex, imageSrc, vendor, tags, productType) => {
    let productHandle = data?.variants?.length > 0 && data?.variants[0]?.productHandle ? data?.variants[0]?.productHandle : '';
    const targetItem = prdVariantData?.products[index];
    if (variantIndex === null) {
      const foundIndexItem = selectedItems.findIndex(({ id }) => id === data.id);
      if (foundIndexItem !== -1) {
        setSelectedItems(prevItems => prevItems.filter(({ id }) => id !== data.id));
      } else {
        setSelectedItems(prevItems => [
          ...prevItems,
          {
            id: data.id,
            title: data.displayName ? data?.displayName : data?.title,
            price: data.price,
            imageSrc: imageSrc,
            status: data.status,
            vendor: vendor,
            tags: tags,
            productType: productType,
            handle: productHandle,
          },
        ]);
      }
    } else {
      if (document.getElementById(targetItem.id).checked && !document.getElementById(targetItem.id + '-' + variantIndex).checked) {
        // remove selected product and add product var id to the product Id array.
        AddEditProductIdLogic(targetItem, selectedItems, imageSrc, vendor, tags, productType, productHandle);

        targetItem?.variants?.forEach((element, varIdx) => {
          const foundIndexVar = selectedVariantItems.findIndex(({ id }) => id === element.id);
          if (document.getElementById(targetItem.id + '-' + varIdx).checked) {
            if (foundIndexVar == -1) {
              setSelectedVariantItems(prevItems => [
                ...prevItems,
                {
                  id: element.id,
                  title: element.displayName ? element?.displayName : element?.title,
                  price: element?.price,
                  imageSrc: imageSrc,
                  vendor: vendor,
                  tags: tags,
                  productType: productType,
                  handle: productHandle,
                },
              ]);
            }
          } else {
            setSelectedVariantItems(prevItems => prevItems.filter(({ id }) => id !== element.id));
          }
        });
      } else {
        AddEditProductVariantLogic(data, selectedVariantItems, imageSrc, vendor, tags, productType, productHandle);
      }
    }
  };

  const AddEditProductIdLogic = (data, selectedItems, imageSrc, vendor, tags, productType, productHandle) => {
    if (isProductAdded(selectedProductsAndVariantsData?.products, data.id)) {
      return;
    }
    const foundIndexItem = selectedItems.findIndex(({ id }) => id === data.id);
    if (foundIndexItem !== -1) {
      setSelectedItems(prevItems => prevItems.filter(({ id }) => id !== data.id));
    } else {
      setSelectedItems(prevItems => [
        ...prevItems,
        {
          id: data.id,
          title: data.displayName ? data?.displayName : data?.title,
          price: data.price,
          imageSrc: imageSrc,
          vendor: vendor,
          tags: tags,
          productType: productType,
          handle: productHandle,
        },
      ]);
    }
  };

  const AddEditProductVariantLogic = (data, selectedVariantItems, imageSrc, vendor, tags, productType, productHandle) => {
    if (isProductAdded(selectedProductsAndVariantsData?.variants, data.id)) {
      return;
    }
    const foundIndexVar = selectedVariantItems.findIndex(({ id }) => id === data.id);
    if (foundIndexVar !== -1) {
      setSelectedVariantItems(prevItems => prevItems.filter(({ id }) => id !== data.id));
    } else {
      setSelectedVariantItems(prevItems => [
        ...prevItems,
        {
          id: data.id,
          title: data.displayName ? data?.displayName : data?.title,
          price: data.price,
          imageSrc: imageSrc,
          vendor: vendor,
          tags: tags,
          productType: productType,
          handle: productHandle,
        },
      ]);
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
        setPrdVariantData({ products: [...prdVariantData?.products, ...res?.data?.products], pageInfo: { ...res.data.pageInfo } });
        setProductSeeMoreLoading(false);
        console.log(res);
      })
      .catch(err => {
        console.log(err);
        setProductSeeMoreLoading(false);
      });
  };

  const addAllFilteredProducts = async () => {
    setAddAllProductsLoading(true);
    setProductSeeMoreLoading(true);
    let hasNextPage = true;
    let cursorPage = cursor;
    let productVariantData = prdVariantData;

    while (next && hasNextPage) {
      const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(cursorPage)}&next=${hasNextPage}${
        searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``
      }${filterURL}&cacheBuster=${new Date().getTime()}`;

      try {
        const res = await axios.get(requestUrl);

        if (res.status === 200) {
          const { pageInfo, products } = res.data;

          setCursor(pageInfo?.cursor);
          setNext(pageInfo?.hasNextPage);
          setPrdVariantData(prevData => ({
            products: [...prevData.products, ...products],
            pageInfo: { ...pageInfo },
          }));

          productVariantData = {
            products: [...productVariantData.products, ...products],
            pageInfo: { ...pageInfo },
          };

          hasNextPage = pageInfo?.hasNextPage;
          cursorPage = pageInfo?.cursor;
        }
      } catch (error) {
        // Handle error condition
        console.error('Error occurred while fetching product variants:', error);
        // Set loading state to false to stop loading spinner
        setAddAllProductsLoading(false);
        setProductSeeMoreLoading(false);
        return; // Exit the function early
      }
    }

    if (productVariantData?.products?.length > 0) {
      productVariantData.products.forEach((item, index) => {
        let isExist = selectedItems?.some(i => i?.id === item?.id);
        if (!isExist) {
          handleCheck(item, index, null, item?.imageSrc, item?.vendor, item?.tags, item?.productType);
        }
      });
    }
    setProductSeeMoreLoading(false);
    setAddAllProductsLoading(false);
  };

  const selectAllLoadedProduct = isChecked => {
    setEnableSelectAllProductButton(isChecked);
    if (prdVariantData?.products?.length > 0) {
      prdVariantData.products.forEach((item, index) => {
        let isExist = selectedItems?.some(i => i?.id === item?.id);

        if ((!isExist && isChecked) || (isExist && !isChecked)) {
          handleCheck(item, index, null, item?.imageSrc, item?.vendor, item?.tags, item?.productType);
        }
      });
    }
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
    if (match?.params?.id) {
      let updateEntity = {
        productIds: selectedItems && selectedItems?.length > 0 ? JSON.stringify(selectedItems) : '',
        variantIds: selectedVariantItems && selectedVariantItems?.length > 0 ? JSON.stringify(selectedVariantItems) : '',
      };
      setUpdatingProduct(true);
      updateProductEntity(match?.params?.id, updateEntity);
    } else {
      toggle(isCollectionOpen);
    }

    let products = selectedItems?.filter(({ id: id1 }) => !preSelectedItems?.some(({ id: id2 }) => id2 === id1));
    let variants = selectedVariantItems?.filter(({ id: id1 }) => !preSelectedVariantItems?.some(({ id: id2 }) => id2 === id1));
    onChangeProduct([...preSelectedItems, ...products], [...preSelectedVariantItems, ...variants]);
    checkProductStatus(selectedItems);
    setSelectedProductsAndVariantsData({
      variants: [...selectedProductsAndVariantsData?.variants, ...variants],
      products: [...selectedProductsAndVariantsData?.products, ...products],
    });
  };

  useEffect(() => {
    if (updatingProduct && !updatingProducts) {
      setUpdatingProduct(false);
      toggle(isCollectionOpen);
      setSelectedItems([]);
      setSelectedVariantItems([]);
    }
  }, [updatingProducts]);

  const onChangeProduct = (prdData, varData) => {
    props.onChange(prdData, varData);
  };

  const remove = () => {
    if (match?.params?.id) {
      let event = {
        deleteProductIds: deleteSelectedIds?.products || [],
        deleteVariantIds: deleteSelectedIds?.variants || [],
      };
      deleteProductEntity(match?.params?.id, event)
        .then(() => {
          removeProductFromArray();
        })
        .catch(() => {});
    } else {
      removeProductFromArray();
    }
  };

  const removeProductFromArray = () => {
    let productsCopy = preSelectedItems.filter(product => !deleteSelectedIds?.products?.includes(product.id + ''));
    let varsCopy = preSelectedVariantItems.filter(varData => !deleteSelectedIds?.variants?.includes(varData.id + ''));
    setSelectedItems([...productsCopy]);
    setSelectedVariantItems([...varsCopy]);
    onChangeProduct([...productsCopy], [...varsCopy]);
    let availableProducts = selectedProductsAndVariantsData?.products?.filter(
      product => !deleteSelectedIds?.products?.includes(product.id + ''),
    );
    let availableVariants = selectedProductsAndVariantsData?.variants?.filter(
      varData => !deleteSelectedIds?.variants?.includes(varData.id + ''),
    );
    setSelectedProductsAndVariantsData({
      variants: [...availableVariants],
      products: [...availableProducts],
    });
    setDeleteSelectedIds({ products: [], variants: [] });
  };

  const checkIfProductAlreadySelected = (prdId, varId) => {
    let isVariant = !!varId;
    let id = isVariant ? varId : prdId;
    let addedProductsList = isVariant ? selectedProductsAndVariantsData?.variants : selectedProductsAndVariantsData?.products;
    let selectedProductsList = isVariant ? selectedVariantItems : selectedItems;

    if (isProductAdded(addedProductsList, id)) {
      return { checked: true, disabled: true };
    }

    return { checked: isProductAdded(selectedProductsList, id) };
  };

  const isProductAdded = (list, pId) => {
    return list?.some(item => item.id === pId) || false;
  };

  const deleteProducts = event => {
    const { name, checked, value } = event.target;
    const { products, variants } = deleteSelectedIds;

    if (name === 'product') {
      const updatedProducts = checked ? [...products, value] : products.filter(id => id !== value);

      setDeleteSelectedIds(prevIds => ({ ...prevIds, products: [...new Set(updatedProducts)] }));
    }

    if (name === 'variant') {
      const updatedVariants = checked ? [...variants, value] : variants.filter(id => id !== value);

      setDeleteSelectedIds(prevIds => ({ ...prevIds, variants: [...new Set(updatedVariants)] }));
    }
  };

  const Note = ({ message, message2 }) => (
    <div>
      {message && (
        <p style={{ fontSize: '12px' }}>
          <span className="badge badge-pill badge-success mr-2 mb-1" style={{ letterSpacing: '.25em' }}>
            Note
          </span>
          {message}
        </p>
      )}
      {message2 && (
        <p style={{ fontSize: '12px' }}>
          <span className="badge badge-pill badge-success mr-2 mb-1" style={{ letterSpacing: '.25em' }}>
            Note
          </span>
          {message2}
        </p>
      )}
    </div>
  );

  const [timeoutId, setTimeoutId] = useState(null);
  const searchCollectionText = searchText => {
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    let newTimeoutId = null;
    if (searchText) {
      newTimeoutId = setTimeout(() => {
        getCollections(encodeURIComponent(searchText));
      }, 500);
    }
    setTimeoutId(newTimeoutId);
  };

  useEffect(() => {
    if (collectionsList && collectionsList?.length > 0) {
      let data = JSON.parse(JSON.stringify(collectionOptions));
      let filteredCollection = collectionsList
        .filter(c => !data?.some(t => t.value == c.id))
        ?.map(value => ({ label: value?.title, value: value?.id }));
      if (filteredCollection && filteredCollection.length > 0) {
        setCollectionOptions([...collectionOptions, ...filteredCollection]);
      }
    }
  }, [collectionsList]);

  const [preSelectedProductItemIds, setPreSelectedProductItemIds] = useState([]);
  const [preSelectedVariantItemIds, setPreSelectedVariantItemIds] = useState([]);

  useEffect(() => {
    if (preSelectedItems) {
      setPreSelectedProductItemIds(preSelectedItems);
    }
  }, [preSelectedItems]);

  useEffect(() => {
    if (preSelectedVariantItems) {
      setPreSelectedVariantItemIds(preSelectedVariantItems);
    }
  }, [preSelectedVariantItems]);

  const onDragEnd = (result, type) => {
    if (!result.destination) {
      return;
    }

    let reOrderedItems = [];
    if (type === 'PRODUCT') {
      reOrderedItems = reorder(preSelectedProductItemIds, result.source.index, result.destination.index);
      setPreSelectedProductItemIds(reOrderedItems);
    } else {
      reOrderedItems = reorder(preSelectedVariantItemIds, result.source.index, result.destination.index);
      setPreSelectedVariantItemIds(reOrderedItems);
    }

    if (reOrderedItems) {
      let sq = reOrderedItems?.map(item => item?.id).filter(item => item);
      if (type === 'PRODUCT') {
        preSelectedItems.sort((item1, item2) => {
          const index1 = sq.indexOf(item1.id);
          const index2 = sq.indexOf(item2.id);
          return index1 - index2;
        });
      } else {
        preSelectedVariantItems.sort((item1, item2) => {
          const index1 = sq.indexOf(item1.id);
          const index2 = sq.indexOf(item2.id);
          return index1 - index2;
        });
      }
      if (sq?.length > 0 && match?.params?.id) {
        let entity = {
          productIds: type === 'PRODUCT' ? sq?.join(',') : '',
          variantIds: type === 'VARIANT' ? sq?.join(',') : '',
        };
        const requestUrl = `api/v2/subscription-groups/products-sort/${match?.params?.id}`;
        axios
          .put(requestUrl, entity)
          .then(res => {})
          .catch(err => {});
      }
    }
  };

  const reorder = (list, startIndex, endIndex) => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
  };

  return (
    <div>
      <Row className="grid-divider product-variant-wrap">
        <Col className={preSelectedVariantItems.length !== 0 && !productsLoading ? 'col-sm-12 col-md-6' : 'col-sm-12'}>
          <Note
            message={
              (preSelectedItems?.length !== 0 || (preSelectedItems?.length == 0 && preSelectedVariantItems?.length <= 0)) &&
              'If you select Product, you do not need to select a separate variant, it will add the entire product with all variants.'
            }
            message2={
              preSelectedVariantItems?.length <= 0 &&
              'If you wish to include only particular variants, choose only those specific variations.'
            }
          />

          {preSelectedItems?.length !== 0 && <b for="selectedProducts">Products </b>}
          <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0" flush>
            {!modal && preSelectedItems?.length === 0 && preSelectedVariantItems?.length === 0 ? (
              <ListGroupItem className="multiselect-list-item">No products in selection.</ListGroupItem>
            ) : null}
            {productsLoading ? (
              <div className="d-flex justify-content-center align-items-center">
                <Loader type="line-scale" />
              </div>
            ) : (
              <>
                {((!modal && selectedProductFilter) || (!modal && !selectedProductFilter)) && (
                  <>
                    <DragDropContext onDragEnd={result => onDragEnd(result, 'PRODUCT')}>
                      <Droppable droppableId="droppable">
                        {provided => (
                          <div {...provided.droppableProps} ref={provided.innerRef}>
                            {preSelectedItems?.map((f, index) => (
                              <Draggable key={f.id} draggableId={f.title} index={index}>
                                {provided => (
                                  <div ref={provided.innerRef} {...provided.dragHandleProps} {...provided.draggableProps}>
                                    <ListGroupItem className="multiselect-list-item drag-select" key={f.id}>
                                      <span class="position-absolute" style={{ left: '-5px', top: '9px', color: '#999' }}>
                                        <DragIndicator />
                                      </span>

                                      <span className="checkbox-id">
                                        &nbsp;
                                        <Input
                                          type="checkbox"
                                          checked={deleteSelectedIds?.products?.includes(f.id + '')}
                                          name={'product'}
                                          value={f.id}
                                          onClick={e => deleteProducts(e)}
                                        />
                                      </span>
                                      <span>{f.title}</span>
                                    </ListGroupItem>
                                  </div>
                                )}
                              </Draggable>
                            ))}
                          </div>
                        )}
                      </Droppable>
                    </DragDropContext>
                    {productDataInfo?.productPageInfo?.hasNextPage && (
                      <Button
                        color="primary"
                        className="mx-auto mt-2"
                        disabled={productDataInfo?.productLoading}
                        onClick={() => loadMoreProductsData()}
                      >
                        {productDataInfo?.productLoading ? (
                          <>
                            <span className="appstle_loadersmall d-inline-block" />
                            <span className="pl-2">Please wait...</span>
                          </>
                        ) : (
                          'View More Products'
                        )}
                      </Button>
                    )}
                  </>
                )}
              </>
            )}
          </ListGroup>
        </Col>

        {preSelectedVariantItems?.length > 0 && (
          <Col className={preSelectedItems?.length !== 0 && !productsLoading ? 'col-sm-12 col-md-6' : 'col-sm-12'}>
            <Note
              message={
                preSelectedItems?.length == 0 &&
                'If you select Product, you do not need to select a separate variant, it will add the entire product with all variants.'
              }
              message2={'If you wish to include only particular variants, choose only those specific variations.'}
            />
            <b for="selectedProducts">Variants</b>
            <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0" flush>
              {variantsLoading ? (
                <div className="d-flex justify-content-center align-items-center">
                  <Loader type="line-scale" />
                </div>
              ) : (
                <>
                  {((!modal && selectedProductFilter) || (!modal && !selectedProductFilter)) && (
                    <>
                      <DragDropContext onDragEnd={result => onDragEnd(result, 'VARIANT')}>
                        <Droppable droppableId="droppable">
                          {provided => (
                            <div {...provided.droppableProps} ref={provided.innerRef}>
                              {preSelectedVariantItems?.map((f, index) => (
                                <Draggable key={f.id} draggableId={f.title} index={index}>
                                  {provided => (
                                    <div ref={provided.innerRef} {...provided.dragHandleProps} {...provided.draggableProps}>
                                      <ListGroupItem className="multiselect-list-item drag-select" key={f.id}>
                                        <span class="position-absolute" style={{ left: '-5px', top: '9px', color: '#999' }}>
                                          <DragIndicator />
                                        </span>
                                        <div>
                                          <span className="checkbox-id">
                                            &nbsp;
                                            <Input
                                              type="checkbox"
                                              name={'variant'}
                                              checked={deleteSelectedIds?.variants?.includes(f.id + '')}
                                              value={f.id}
                                              onClick={e => deleteProducts(e)}
                                            />
                                          </span>
                                          <span>{f.title}</span>
                                        </div>
                                      </ListGroupItem>
                                    </div>
                                  )}
                                </Draggable>
                              ))}
                            </div>
                          )}
                        </Droppable>
                      </DragDropContext>
                      {productDataInfo?.variantPageInfo?.hasNextPage && (
                        <Button
                          color="primary"
                          className="mx-auto mt-2"
                          disabled={productDataInfo?.variantLoading}
                          onClick={() => loadMoreVariantsData()}
                        >
                          {productDataInfo?.variantLoading ? (
                            <>
                              <span className="appstle_loadersmall d-inline-block" />
                              <span className="pl-2">Please wait...</span>
                            </>
                          ) : (
                            'View More Variants'
                          )}
                        </Button>
                      )}
                    </>
                  )}
                </>
              )}
            </ListGroup>
          </Col>
        )}
      </Row>

      <Button color="primary mt-2 mr-1" onClick={() => toggle(false)}>
        {buttonLabel}
      </Button>
      {isProductFilterByCollection && (
        <Button color="primary mt-2 mr-1" onClick={() => toggle(true)}>
          {buttonLabel} by Collection
        </Button>
      )}
      {(deleteSelectedIds?.products?.length > 0 || deleteSelectedIds?.variants?.length > 0) && (
        <Button color="danger mt-2 mr-1" disabled={deleteProductLoading} onClick={() => remove()}>
          {deleteProductLoading ? (
            <div className="d-flex">
              <div className="appstle_loadersmall" />
            </div>
          ) : (
            'Delete'
          )}
        </Button>
      )}
      <>
        {preSelectedItems.length === 0 && preSelectedItems.length === 0 ? (
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
                        options={collectionOptions}
                        value={selectedFilterData['collection']?.length > 0 ? selectedFilterData['collection'][0] : null}
                        onChange={e => onChangeFilterData(e, 'collection')}
                        class="as-mt-5 as-bg-gray-50 as-border as-border-gray-300 as-text-gray-900 as-text-sm as-rounded-lg focus:as-ring-blue-500 focus:as-border-blue-500 as-block as-w-full as-p-2.5 dark:as-bg-gray-700 dark:as-border-gray-600 dark:as-placeholder-gray-400 dark:as-text-white dark:focus:as-ring-blue-500 dark:as-focus:border-blue-500"
                        onInputChange={e => {
                          searchCollectionText(e);
                        }}
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
            <div className="d-flex justify-content-center align-items-center">
              <Loader type="line-scale" />
            </div>
          ) : (
            <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
              <ListGroupItem className="py-1">
                <Row>
                  <Col md={12} className="d-flex align-items-center">
                    <FormGroup check>
                      <Label>
                        <Input
                          type="checkbox"
                          className="mt-0"
                          style={{ top: '6px' }}
                          onChange={e => selectAllLoadedProduct(e.target.checked)}
                        />
                      </Label>
                    </FormGroup>
                    {enableSelectAllProductButton && next && (
                      <>
                        {addAllProductsLoading ? (
                          <a className="pl-1 d-flex" href="javascript:;" disabled="disabled">
                            <span className="appstle_loadersmall d-inline-block" style={{ margin: '3px' }} />
                            <span className="pl-1">Please wait...</span>
                          </a>
                        ) : (
                          <a className="pl-1 d-flex" href="javascript:;" onClick={addAllFilteredProducts}>
                            <span className="pl-1">Select All Product</span>
                          </a>
                        )}
                      </>
                    )}
                  </Col>
                </Row>
              </ListGroupItem>
              {prdVariantData?.products &&
                prdVariantData?.products.map((item, index) => (
                  <ListGroupItem className="multiselect-list-item" key={index}>
                    <Row>
                      <Col md={12}>
                        <FormGroup check>
                          <Label className="pt-2">
                            <Input
                              id={item.id}
                              type="checkbox"
                              onChange={() => handleCheck(item, index, null, item?.imageSrc, item?.vendor, item?.tags, item?.productType)}
                              // checked={checkIfProductAlreadySelected(item?.id, null)}
                              {...checkIfProductAlreadySelected(item?.id, null)}
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
                                    id={item.id + '-' + varientIndex}
                                    type="checkbox"
                                    onChange={() =>
                                      handleCheck(element, index, varientIndex, item?.imageSrc, item?.vendor, item?.tags, item?.productType)
                                    }
                                    {...checkIfProductAlreadySelected(item?.id, element?.id)}
                                    // checked={checkIfProductAlreadySelected(item?.id, element?.id)}
                                  />
                                  <Row className="ml-1">
                                    <Col md={8}>{element.title}</Col>
                                    <Col md={4}>
                                      {getSymbolFromCurrency(item?.currencyCode || 'USD')} {element.price}
                                    </Col>
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
                className="mx-auto ml-2"
                color="primary"
                disabled={!(selectedItems.length || selectedVariantItems.length) || updatingProduct}
                onClick={handleAdd}
              >
                {updatingProduct ? (
                  <div className="d-flex">
                    <div className="appstle_loadersmall" />
                  </div>
                ) : (
                  'Add'
                )}
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
  loadingProductFilter: storeState.productInfo.loadingProductFilter,
  updatingProducts: storeState.subscriptionGroup.updatingProducts,
  productsLoading: storeState.subscriptionGroup.productsLoading,
  variantsLoading: storeState.subscriptionGroup.variantsLoading,
  deleteProductLoading: storeState.subscriptionGroup.deleting,
  collectionsList: storeState.appstleMenuAdmin.collections,
});

const mapDispatchToProps = {
  getPrdVariantOptions,
  getProductFilterData,
  updateProductEntity,
  deleteProductEntity,
  getCollections,
};

export default connect(mapStateToProps, mapDispatchToProps)(ProductVariantModalV2);
