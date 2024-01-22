import React, {useState, useEffect} from 'react';
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
  FormGroup, FormText, Badge
} from 'reactstrap';
import axios from 'axios';
import {connect} from "react-redux";
import {getProductOptions} from 'app/entities/fields-render/data-product.reducer';
import {getPrdVariantOptions} from 'app/entities/fields-render/data-product-variant.reducer';
import { KeyboardArrowDown } from '@mui/icons-material';

const PrdVariantModel = ({
                            getProductOptions,
                            getPrdCollectionOptions,
                            getPrdVariantOptions,
                            productOptions,
                            prdCollectionOptions,
                            prdVariantOptions,
                            ...props
                          }) => {
  const {
    buttonLabel,
    className,
    header,
    value,
    isSource,
    totalTitle
  } = props;

  const [productOptionsData, setProductOptionsData] = useState({products: [], pageInfo: {}});
  const [modal, setModal] = useState(false);
  const [selectedItems, setSelectedItems] = useState(value ? JSON.parse(value) : []);
  const [fields, setFields] = useState(value ? JSON.parse(value) : []);
  const [cursor, setCursor] = useState(null);
  const [allCursorArray, setAllCursorArray] = useState([]);

  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [productSeeMoreLoading, setProductSeeMoreLoading] = useState(false);
  const [variantSeeMoreLoading, setVariantSeeMoreLoading] = useState(false);

  useEffect(() => {
    setFields(value ? [...JSON.parse(value)] : []);
    setSelectedItems(value ? [...JSON.parse(value)] : []);
  }, [value])


  useEffect(() => {
    getPrdVariantOptions(`${null}&next=${false}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}`);
  }, [searchValue])

  useEffect(() => {
    setProductOptionsData({products: [...productOptions?.products], pageInfo: {...productOptions?.pageInfo}});
    setCursor(productOptions?.pageInfo?.cursor);
    setNext(productOptions?.pageInfo?.hasNextPage);
  }, [productOptions])

  useEffect(() => {
    setSelectedItems([...fields]);
    onChangeProduct([...fields]);
  }, [fields])

  const toggle = () => { setSearchValue(''); setModal(!modal);};
  const handleCheck = (varData, imageSrc, title) => {
    // const targetItem = productOptionsData?.products[index];
    if(varData.title == "Default Title")
    {
      varData.displayName = title
    }

    const foundIndex = selectedItems.findIndex(({id}) => (id === varData.id))
    if (foundIndex !== -1) {
      setSelectedItems((prevItems) => prevItems.filter(({id}) => id !== varData.id))
    } else {
      setSelectedItems((prevItems) => [...prevItems, {id: varData.id, displayName: varData.displayName, imageSrc: imageSrc}])
    }
  }

  const handleSearch = (event) => {
    // if (event.target.value !== searchValue) {
      setCursor(null);
      setNext(false);
   //  }
    setSearchValue(event.target.value);
  }

  const handleAdd = () => {
    // selectedItems.forEach((item) => {
    //   const found = fields.length && fields.find(({id}) => (item.id === id))
    //   if (!found) {
    //     fields.push(item);
    //   }
    // })
    setFields([...selectedItems])
    // setSelectedItems([])
    toggle()
  }

  const handleCancel = () => {
    setSelectedItems([...fields]);
    toggle();
  }

  const onChangeProduct = (val) => {
    const s = JSON.stringify(val);
    props.onChange(s)
  }

  const handleSeeMore = () => {
    setProductSeeMoreLoading(true);
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(cursor)}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&cacheBuster=${new Date().getTime()}`;
    axios.get(requestUrl).then(res => {
      setCursor(res.data.pageInfo?.cursor);
      setNext(res.data.pageInfo?.hasNextPage);
      setProductOptionsData({products: [...productOptionsData?.products, ...res?.data?.products], pageInfo: {...res.data.pageInfo}});
      setProductSeeMoreLoading(false);
    }).catch(err => {console.log(err); setProductSeeMoreLoading(false);});
  }
  useEffect(() => {
    console.log(allCursorArray);
    if (!allCursorArray.some(data => data === cursor) && cursor) {
      setAllCursorArray(prev => [...prev, cursor]);
    }
  }, [cursor]);

  const handleVarinatSeeMore = (prdCursorArrayIndex, varCursor, varNext, currentProductId) => {
    let productCursor = null;
    let hasProductNextPage = false;
    setVariantSeeMoreLoading(true);
    let calculateCurrentProductCursor = parseInt(prdCursorArrayIndex/10)
    if(calculateCurrentProductCursor > 0)
    {
      productCursor = allCursorArray[calculateCurrentProductCursor - 1];
      hasProductNextPage = true;
    }
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(productCursor)}&next=${hasProductNextPage}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&variantCursor=${encodeURIComponent(varCursor)}&variantNext=${varNext}`;
    axios.get(requestUrl).then(res => {
      console.log(res)
      let prdVariantArray = [...productOptionsData?.products]
      // let newUpdatedVariantdata = prdVariantArray[prdCursorArrayIndex];
      let newUpdatedVariantdata = res.data.products.find(data => data.id === currentProductId)
      if(newUpdatedVariantdata?.variants)
      {
        prdVariantArray[prdCursorArrayIndex].variants = [...prdVariantArray[prdCursorArrayIndex].variants, ...newUpdatedVariantdata?.variants];
        prdVariantArray[prdCursorArrayIndex].pageInfo = newUpdatedVariantdata?.pageInfo
        setProductOptionsData({ products: [...prdVariantArray], pageInfo: { ...productOptionsData?.pageInfo } })
      }
      setVariantSeeMoreLoading(false);
    }).catch(err =>  {console.log(err); setVariantSeeMoreLoading(false);});
  }

  const remove = (evt, id) => {
    evt.preventDefault();
    let productsCopy = fields.filter((product) => product.id !== id);
    setFields([...productsCopy]);
    // onChangeProduct(productsCopy);
  }

  const checkIfProductAlreadySelected = (itemId) => {
    return selectedItems.findIndex(({id}) => (id === itemId)) != -1 ? true : false;
  }
  const closeBtn = <button className="close" onClick={handleCancel}>&times;</button>;
  return (
    <div>
      <Button color="primary mb-2" style={{width:'100%'}} onClick={toggle}>{buttonLabel}</Button>
      <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0" >
        <FormGroup>
          <Input type="hidden"
                 attr='required'
                 value={fields.length}
                 required={true}/>
        </FormGroup>
      </ListGroup>
      {fields.length === 0 ?
        <ListGroupItem className="multiselect-list-item">No Product Selected.</ListGroupItem> : null}
      {fields.map((f, index) => (
        <ListGroupItem className="multiselect-list-item" key={f.id} style={{padding: '0.5rem 0.6rem',marginBottom: '8px', borderRadius: '14px'}}>
            <div>
            <img className="swap-product-img shadow-sm mr-2 ml-1 rounded" style={{height: '48px',width: '48px'}} src={f.imageSrc}/>
                {/* <span className="product-id">{`#${f.id}`} &nbsp;</span> */}
                <span>{f.displayName}</span>
                <Button close onClick={(evt) => remove(evt, f.id)}></Button>
            </div>
        </ListGroupItem>
      ))}
      <>{fields.length === 0 ? <p className="mt-3"><Badge
        className="tag-badge-readonly toRemove ml-0 font-size-badge pb-2"> incomplete </Badge><FormText
        className="d-inline-block"><span
        className="text-danger">Please ensure you select at least one product.</span></FormText></p> : ''}</>
      <Modal isOpen={modal} toggle={toggle} className={className}>
        <ModalHeader toggle={toggle} close={closeBtn}>{header}</ModalHeader>
        <ModalBody className="multiselect-modal-body">
          <div className="form-group has-search mb-20">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text"><i className="pe-7s-search"></i></span>
              </div>
              <input type="text" className="form-control" placeholder="Search" onChange={handleSearch} value={searchValue} />
            </div>
          </div>
          <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
            {
              productOptionsData?.products.length > 0 && productOptionsData?.products.map((item, index) => {
               return  <ListGroupItem className="multiselect-list-item" key={item?.id}>
                        {
                            item?.variants?.map((varData)=> {
                                return (
                                    <FormGroup check>
                                    <Label className="pt-2">
                                        <Input type="checkbox" onChange={() => handleCheck(varData, item?.imageSrc, item?.title)} checked={checkIfProductAlreadySelected(varData.id)}/>
                                        <img className=" shadow-sm mr-2 ml-1 rounded" src={item?.imageSrc}/>
                                        {varData.title == "Default Title" ? item?.title : varData?.displayName }
                                    </Label>
                                    </FormGroup>
                                )
                            })
                        }
                  <div>
                    { item?.variants?.length > 1 && item?.pageInfo?.hasNextPage ? (
                      variantSeeMoreLoading ? (
                        <div className="d-flex">
                          <div className="appstle_loadersmall" style={{ margin: '3px' }} />
                          <span className="ml-2"> Please Wait..</span>
                        </div>
                      ) : (
                        <a
                        className='d-flex'
                          href="javascript:void(0)"
                          onClick={() => handleVarinatSeeMore(index, item?.pageInfo?.cursor, item?.pageInfo?.hasNextPage, item.id)}
                        >
                          <KeyboardArrowDown /> <p>View more variants</p>
                        </a>
                      )
                    ) : null}
                  </div>
              </ListGroupItem>
              })
            }
           <hr />
            {next ? <Button color="primary" className="mx-auto mt-2" onClick={handleSeeMore}>
              {
                productSeeMoreLoading ?
                        <div className="d-flex">
                              <div className="appstle_loadersmall"/>
                              <span className="ml-2"> Please Wait..</span>
                            </div>
                            :
                            <>
                            <KeyboardArrowDown/> View More Products</>
              }
             </Button> : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter className="d-block">
          <div className='d-flex'>
            <div className="mr-auto p-2">{`${selectedItems.length} ${totalTitle} selected`}</div>
            <div>
              <Button className="mr-2" outline onClick={handleCancel}>Cancel</Button>
              <Button color="primary" disabled={!selectedItems.length} onClick={handleAdd}> Add </Button>
            </div>
          </div>
        </ModalFooter>
      </Modal>
    </div>
  );
}


const mapStateToProps = storeState => ({
  // productOptions: storeState.product.productOptions,
  productOptions: storeState.prdVariant.prdVariantOptions
});

const mapDispatchToProps = {
  // getProductOptions,
  getPrdVariantOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(PrdVariantModel);
