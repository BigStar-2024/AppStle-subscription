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

const PrdVariantRadioPopup = ({
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
  const [selectedItems, setSelectedItems] = useState(value ? value : {});
  const [fields, setFields] = useState(value ? value : {});
  const [cursor, setCursor] = useState(null);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');

  useEffect(() => {
    // setFields(value ? value : {});
    setSelectedItems(value ? value : {});
  }, [value])
  

  useEffect(() => {
    getPrdVariantOptions(`${null}&next=${false}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}`);
  }, [searchValue])

  useEffect(() => {
    setProductOptionsData({products: [...productOptions?.products], pageInfo: {...productOptions?.pageInfo}});
    setCursor(productOptions?.pageInfo?.cursor);
    setNext(productOptions?.pageInfo?.hasNextPage);
  }, [productOptions])



  const toggle = () => { setSearchValue(''); setModal(!modal);};

  const handleCheck = (varData, imageSrc, title) => {
    if(varData.title == "Default Title")
    {
      varData.displayName = title
    } 
    setSelectedItems({id: varData.id, displayName: varData.displayName, imageSrc: imageSrc})
  }

  const handleSearch = (event) => {
      setCursor(null);
      setNext(false);
    setSearchValue(event.target.value);
  }

  const handleAdd = () => {
    setFields(selectedItems)
    props.onChange(selectedItems)
    toggle()
  }

  const handleCancel = () => {
    toggle();
  }

  const onChangeProduct = (val) => {
    props.onChange(val)
  }

  const handleSeeMore = () => {
    const requestUrl = `/api/data/product-variants?cursor=$${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&cacheBuster=${new Date().getTime()}`;
    axios.get(requestUrl).then(res => {
      setCursor(res.data.pageInfo?.cursor);
      setNext(res.data.pageInfo?.hasNextPage);
      setProductOptionsData({products: [...productOptionsData?.products, ...res?.data?.products], pageInfo: {...res.data.pageInfo}})     
    });
  }

  const remove = (evt, id) => {
    evt.preventDefault();
    setFields({});
  }

  const checkIfProductAlreadySelected = (itemId) => {
    return selectedItems.findIndex(({id}) => (id === itemId)) != -1 ? true : false;
  }
  const closeBtn = <button className="close" onClick={handleCancel}>&times;</button>;
  return (
    <div style={{fontSize: '13px'}}>
      <b>OR </b> <a color="primary" href="javascript:void(0)" onClick={toggle}>Click here</a> to {buttonLabel}
      {/* {
        !(_.isEmpty(fields)) && <ListGroupItem className="multiselect-list-item" key={fields.id} style={{padding: '0.5rem 0.6rem',marginBottom: '8px', borderRadius: '14px'}}>
            <div>
            <img className="swap-product-img shadow-sm mr-2 ml-1 rounded" style={{height: '48px',width: '48px'}} src={fields.imageSrc}/>
                <span className="product-id">{`#${fields.id}`} &nbsp;</span>
                <span>{fields.displayName}</span>
                <Button close onClick={(evt) => remove(evt, fields.id)}></Button>
            </div>
        </ListGroupItem>
      } */}

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
              productOptionsData?.products.length > 0 && productOptionsData?.products.map(({imageSrc, title, id, variants}, index) => {
               return  <ListGroupItem className="multiselect-list-item" key={id}>
                {
                    variants?.map((varData)=> {
                        return (
                            <FormGroup check>
                            <Label className="pt-2">
                                <Input type="radio" name="variantselect" onChange={() => handleCheck(varData, imageSrc, title)}/>
                                <img className=" shadow-sm mr-2 ml-1 rounded" src={imageSrc}/>
                                {varData.title == "Default Title" ? title : varData.displayName }
                            </Label>
                            </FormGroup>
                        )
                    })
                }
              </ListGroupItem>
              })
            }
            {next ?
              <Button color="primary" className="mx-auto mt-2" style={{width: "100px"}} onClick={handleSeeMore}> See More...</Button> : null}
          </ListGroup>
        </ModalBody>
        <ModalFooter className="d-block">
          <div className='d-flex'>
              <Button className="mr-2" outline onClick={handleCancel}>Cancel</Button>
              <Button color="primary" disabled={!selectedItems} onClick={handleAdd}> Add </Button>
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

export default connect(mapStateToProps, mapDispatchToProps)(PrdVariantRadioPopup);
