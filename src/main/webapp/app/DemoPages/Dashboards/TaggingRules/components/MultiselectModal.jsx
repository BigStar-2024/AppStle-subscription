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
import "../TagginRules.scss"
import {connect} from "react-redux";
import {onChangeCriteria} from "app/entities/subscription-group/subscription-group.reducer";
import {getProductOptions} from 'app/entities/fields-render/data-product.reducer';

const MultiselectModal = ({
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
    totalTitle,
    hideValidation
  } = props;

  const [productOptionsData, setProductOptionsData] = useState({products: [], pageInfo: {}});
  const [modal, setModal] = useState(false);
  const [selectedItems, setSelectedItems] = useState(value ? JSON.parse(value) : []);
  const [fields, setFields] = useState(value ? JSON.parse(value) : []);
  const [cursor, setCursor] = useState(null);
  const [next, setNext] = useState(false);
  const [searchValue, setSearchValue] = useState('');

  useEffect(() => {
    setFields(value ? [...JSON.parse(value)] : []);
    setSelectedItems(value ? [...JSON.parse(value)] : []);
  }, [value])

  useEffect(() => {
    getProductOptions(`${null}&next=${false}${searchValue ? `&search=${searchValue}` : ``}`);
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

  const toggle = () => setModal(!modal);
  const handleCheck = (index, varientIndex) => {
    const targetItem = productOptionsData?.products[index];
    const foundIndex = selectedItems.findIndex(({id}) => (id === targetItem.id))
    if (foundIndex !== -1) {
      setSelectedItems((prevItems) => prevItems.filter(({id}) => id !== targetItem.id))
    } else {
      setSelectedItems((prevItems) => [...prevItems, {id: targetItem.id, title: targetItem.title}])
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
    const requestUrl = `/api/data/products?cursor=$${cursor}&next=${next}${searchValue ? `&search=${encodeURIComponent(searchValue)}` : ``}&cacheBuster=${new Date().getTime()}`;
    axios.get(requestUrl).then(res => {
      setCursor(res.data.pageInfo?.cursor);
      setNext(res.data.pageInfo?.hasNextPage);
      setProductOptionsData({products: [...productOptionsData?.products, ...res?.data?.products], pageInfo: {...res.data.pageInfo}})     
    });
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
      <Button color="primary mb-2" onClick={toggle}>{buttonLabel}</Button>
      <ListGroup className="multiselect-list overflow-auto mt-20 mb-20 ml-0 mr-0">
        <FormGroup>
          <Input type="hidden"
                 attr='required'
                 value={fields.length}
                 required={true}/>
        </FormGroup>
      {fields.length === 0 ?
        <ListGroupItem className="multiselect-list-item">No Product Selected.</ListGroupItem> : null}
      {fields.map((f, index) => (
        <ListGroupItem className="multiselect-list-item" key={f.id}>
          <span className="product-id">{`#${f.id}`}</span>
          <span>{f.title}</span>
           { <Button close onClick={(evt) => remove(evt, f.id)}></Button>}
        </ListGroupItem>
      ))}
       </ListGroup>
      <>{!hideValidation && fields.length === 0 ? <p className="mt-3"><Badge
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
              productOptionsData?.products.length > 0 && productOptionsData?.products.map(({imageSrc, title, id, variants}, index) => (
                <ListGroupItem className="multiselect-list-item" key={id}>
                  <FormGroup check>
                    <Label className="pt-2">
                      <Input type="checkbox" onChange={() => handleCheck(index)} checked={checkIfProductAlreadySelected(id)}/>
                      <img className=" shadow-sm mr-2 ml-1 rounded" src={imageSrc}/>
                      {title}
                    </Label>
                  </FormGroup>
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
  subscriptionGroupEntity: storeState.subscriptionGroup.entity,
  productOptions: storeState.product.productOptions
});

const mapDispatchToProps = {
  onChangeCriteria,
  getProductOptions
};

export default connect(mapStateToProps, mapDispatchToProps)(MultiselectModal);
