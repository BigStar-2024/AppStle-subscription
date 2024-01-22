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
  FormGroup, FormText, Badge,
  Row,
  Col
} from 'reactstrap';
import axios from 'axios';
import {connect} from "react-redux";
import {getProductOptions} from 'app/entities/fields-render/data-product.reducer';
// import  "./loader.scss";
import { getPrdVariantOptions } from 'app/entities/fields-render/data-product-variant.reducer';
import { KeyboardArrowDown } from "@mui/icons-material";

const AddProductModalSingle = ({getProductOptions, productOptions,getPrdVariantOptions, prdVariantOptions,selectedProductVarIds, ...props}) => {
  const {
    buttonLabel,
    className,
    header,
    value,
    totalTitle,
    processing,
    modal,
    setModal
  } = props;

  const [prdVariantData, setPrdVariantData] = useState({ products: [], pageInfo: {} });

  const [selectedVariantItems, setSelectedVariantItems] = useState([])
  const [cursor, setCursor] = useState(null);
  const [allCursorArray, setAllCursorArray] = useState([]);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');
  const [variantSeeMoreLoading, setVariantSeeMoreLoading] = useState(false);
  const [productSeeMoreLoading, setProductSeeMoreLoading] = useState(false);
  // const [purchaseOption, setPurchaseOption] = useState("SUBSCRIBE")
  // const [quantity, setQuantity] = useState(1)

  useEffect(() => {
    getPrdVariantOptions(`${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}`);
  }, [searchValue])

  useEffect(() => {
    setPrdVariantData({ products: prdVariantOptions?.products, pageInfo: prdVariantOptions?.pageInfo,  });
    setCursor(prdVariantOptions?.pageInfo?.cursor);
    setNext(prdVariantOptions?.pageInfo?.hasNextPage);
  }, [prdVariantOptions])

  useEffect(() => {
    if (selectedProductVarIds) {
      setSelectedVariantItems([...selectedProductVarIds])
    }
  }, [selectedProductVarIds])

  const toggle = () => setModal(!modal);
  const handleCheck = (data, index, varientIndex, imageSrc, productId) => {
    AddEditProductVariantLogic(data, selectedVariantItems, imageSrc, productId);
  }

  const AddEditProductVariantLogic = (data, selectedVariantItems, imageSrc, productId) => {
    // const foundIndexvar = selectedVariantItems.findIndex(({ variantId }) => (variantId === data.id))
    // if (foundIndexvar !== -1) {
    //   setSelectedVariantItems((prevItems) => prevItems.filter(({ variantId }) => variantId !== data.id))
    // }
    // else {
    //   setSelectedVariantItems((prevItems) => [...prevItems, { variantId: data.id, title: data.displayName ? data?.displayName : data?.title, price: data.price, unitPrice: data.price, imageSrc: imageSrc, productId: productId, quantity: 1 }])
    // }
    setSelectedVariantItems([{ variantId: data.id, title: data.displayName ? data?.displayName : data?.title, price: data.price, unitPrice: data.price, imageSrc: imageSrc, productId: productId, quantity: 1, productHandle: data?.productHandle }])
  }

  const handleSeeMore = () => {
    setProductSeeMoreLoading(true);
    const requestUrl = `/api/data/product-variants?cursor=${encodeURIComponent(cursor)}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&cacheBuster=${new Date().getTime()}`;
    axios.get(requestUrl).then(res => {
      setCursor(res.data.pageInfo?.cursor);
      setNext(res.data.pageInfo?.hasNextPage);
      setPrdVariantData({ products: [...prdVariantData?.products, ...res?.data?.products], pageInfo: { ...res.data.pageInfo } })
      setProductSeeMoreLoading(false);
      console.log(res)
    }).catch(err => {console.log(err); setProductSeeMoreLoading(false);});
  }

  useEffect(() => {
    console.log(allCursorArray);
    if(!allCursorArray.some(data => data === cursor) && cursor)
    {
      setAllCursorArray(prev => [...prev, cursor])
    }
  }, [cursor])

  const handleSearch = (event) => {
    setCursor(null);
    setNext(false);
    setSearchValue(event.target.value);
  }

  const handleAdd = () => {
    props.addHandler([...selectedVariantItems])
    // setSelectedVariantItems([]);
    // setQuantity(1);
    toggle();
  }

  const handleCancel = () => {
    // setSelectedVariantItems([]);
    // setQuantity(1);
    toggle();
  }

  const changeQuantity = (event) => {
    // setQuantity(event.target.value)
  }

  const checkIfProductAlreadySelected = (varId) => {
      return selectedVariantItems.findIndex(({ variantId }) => (variantId === varId)) != -1 ? true : false;
  }

  const closeBtn = <button className="close" onClick={handleCancel}>&times;</button>;

  return (
    <div>
      {/* <Button color="primary mb-2" onClick={toggle} style={{minWidth: '120px'}}>
        {!processing && <span>{buttonLabel}</span>}
        {processing && (<div className="d-flex"><div className="appstle_loadersmall" /> <span className="ml-2"> Please Wait</span></div> )}
      </Button> */}

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
              prdVariantData?.products && prdVariantData?.products.map((item, index) => (
                <ListGroupItem className="multiselect-list-item" key={index}>
                <Row>
                  <Col md={12}>
                  <FormGroup check>
                    <Label className="pt-2">
                     {item?.variants.length === 1 && <Input id={item.id} type="radio" onChange={() => handleCheck(item.variants[0], index, 0, item?.imageSrc, item?.id)} checked={checkIfProductAlreadySelected(item.variants[0]?.id)} />}
                      <img className=" shadow-sm mr-2 ml-1 rounded" src={item.imageSrc} />
                      {item.title}
                    </Label>
                  </FormGroup>
                  </Col>
                  </Row>
                    <Row>
                    <Col md={2}>
                    </Col>
                    <Col md={10} style={{overflow:'auto', maxHeight: '400px'}}>
                        {
                          item?.variants?.map((element, varientIndex) => (
                            element.title != "Default Title" &&
                            <FormGroup check style={{ paddingLeft: "56px" }} key={varientIndex}>
                              <div className="pt-2" key={varientIndex}>
                                <Input id={item.id + '-' + varientIndex} type="radio" onChange={() => handleCheck(element, index, varientIndex, item?.imageSrc, item?.id)} checked={checkIfProductAlreadySelected(element?.id)} />
                                {/* <img className=" shadow-sm mr-2 ml-1 rounded" src={imgSrc} /> */}
                                <Row className="ml-1">
                                  <Col md={8}>
                                    {element.title}
                                  </Col>
                                  <Col md={4}>
                                    $ {element.price}
                                  </Col>
                                </Row>
                              </div>
                            </FormGroup>
                          ))
                        }
                        <div>
                        {item?.variants.length > 1 && item?.pageInfo?.hasNextPage ?

                          ( variantSeeMoreLoading ?
                            <div className="d-flex">
                              <div className="appstle_loadersmall" style={{margin: '3px'}}/>
                              <span className="ml-2"> Please Wait..</span>
                            </div>
                          : <a
                            href='javascript:void(0)'
                            onClick={() => handleVarinatSeeMore(index, item?.pageInfo?.cursor, item?.pageInfo?.hasNextPage, item.id )}>
                            <KeyboardArrowDown/> View more variants</a>) : null}

                       </div>
                    </Col>
                  </Row>
                </ListGroupItem>
              ))
            }
            {next ?
              <Button color="primary" className="mx-auto mt-2" style={{width: "100px"}} onClick={handleSeeMore}> See
                More...</Button> : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter className="d-block">
          <div className='d-flex'>
            <div className="mr-auto p-2">{`${selectedVariantItems.length} ${totalTitle} selected`}</div>
            <div>
              <Button className="mr-2" outline onClick={handleCancel}>Cancel</Button>
              <Button color="primary" disabled={!selectedVariantItems.length} onClick={handleAdd}> Add </Button>
            </div>
          </div>
        </ModalFooter>
      </Modal>
    </div>
  );
}


const mapStateToProps = storeState => ({
  productOptions: storeState.product.productOptions,
  prdVariantOptions: storeState.prdVariant.prdVariantOptions,
});

const mapDispatchToProps = {
  getProductOptions,
  getPrdVariantOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(AddProductModalSingle);
