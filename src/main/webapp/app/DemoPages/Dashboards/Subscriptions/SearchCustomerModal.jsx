import React, {useEffect, useState} from 'react';
import {
  Button,
  Col,
  FormGroup,
  Input,
  Label,
  Modal,
  ModalBody, ModalFooter,
  ModalHeader,
  Row,
  Spinner
} from "reactstrap";
import {connect} from "react-redux";
import {
  getShopifyCustomerBySearch,
  getShopifyCustomerDetails,
  getShopifyCustomerPaymentDetails
} from "app/entities/customers/customer.reducer";
import {
  updatePaymentMethodToState,
  updateStateEntity
} from "app/entities/subscription-group/subscription-group.reducer";


const SearchCustomerModal = ({
                               getShopifyCustomerBySearch,
                               getShopifyCustomerDetails,
                               getShopifyCustomerPaymentDetails,
                               customer,
                               customerDetails,
                               customerPaymentDetails,
                               selectedCustomerIds,
                               subscriptionGroupEntity,
                               updateStateEntity,
                               entity,
                               updatePaymentMethodToState,
                               loading,
                               ...props
                             }) => {
  const {
    buttonLabel,
    header
  } = props;

  const [modal, setModal] = useState(false);
  const [searchValue, setSearchValue] = useState("");
  const [customers, setCustomers] = useState({customersData: [], pageInfo: {}});
  const [selectedCustomers, setSelectedCustomers] = useState(null);

  const [cursor, setCursor] = useState(null);
  const [next, setNext] = useState(false);


  const toggle = () => setModal(!modal);
  const handleCancel = () => {
    toggle();
  }
  const closeBtn = <button className="close" onClick={handleCancel}>&times;</button>;

  const handleSearch = (event) => {
    setSearchValue(event.target.value);
  }

  useEffect(() => {
    if (searchValue && searchValue !== "") {
      getShopifyCustomerBySearch(`/search?${searchValue ? `searchText=${searchValue}` : ''}`)
    }
  }, [searchValue])

  useEffect(() => {
    if (customer) {
      setCustomers({
        customersData: customer?.edges,
        pageInfo: customer?.pageInfo
      })
      setCursor(customer?.edges?.cursor);
      setNext(customer?.pageInfo?.hasNextPage);
    }
  }, [customer])

  useEffect(() => {
    if (selectedCustomers) {
      getShopifyCustomerDetails(`/${selectedCustomers?.id.split('/').pop()}`)
    }
    if (selectedCustomers) {
      getShopifyCustomerPaymentDetails(`/${selectedCustomers?.id.split('/').pop()}/payment-methods`)
    }
  }, [selectedCustomers])

  const handleAdd = () => {
    toggle();
  }

  useEffect(() => {
    if (customerPaymentDetails && customerPaymentDetails?.edges) {
      let paymentMethods = [];
      customerPaymentDetails?.edges.map((data) => {
        const newData = {
          id: data?.node?.id.split('/').pop(),
          paymentType: data?.node?.instrument?.brand,
          maskedNumber: data?.node?.instrument?.maskedNumber,
          paymentName: data?.node?.instrument?.name,
        }
        paymentMethods.push(newData)
      })
      updatePaymentMethodToState(paymentMethods)
    }

    if (customerDetails) {
      updateStateEntity({
        ...entity,
        customerId: customerDetails?.id?.split('/').pop(),
        customerEmail: customerDetails?.email,
        deliveryFirstName: customerDetails?.firstName,
        deliveryLastName: customerDetails?.lastName,
        deliveryPhone: customerDetails?.phone,
        paymentMethodId: "",
        deliveryCity: "",
        deliveryAddress1: "",
        deliveryAddress2: "",
        province: "",
        deliveryProvinceCode: "",
        deliveryZip: "",
        country: "",
        deliveryCountryCode: ""
      })
    }

  }, [selectedCustomers, customerDetails, customerPaymentDetails, customerPaymentDetails?.edges, entity?.paymentMethodId])


  return (
    <div>
      <Button color="primary mb-2" onClick={toggle} style={{minWidth: '120px'}}>
        {buttonLabel}
      </Button>

      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle} close={closeBtn}>{header}</ModalHeader>
        <ModalBody className="multiselect-modal-body">
          <div className="form-group has-search mb-20">
            <div className="input-group">
              <div className="input-group-prepend">
                <span className="input-group-text"><i className="pe-7s-search"/></span>
              </div>
              <input type="text" className="form-control" placeholder="Search customers by Email or Name"
                     onChange={handleSearch} value={searchValue}/>
            </div>
          </div>
          {
            loading ? <Spinner color="primary" className="d-block mx-auto text-center" size="sm"/> : <>
              {
                customers?.customersData && customers?.customersData.map((customer, index) => {
                  return (
                    <Row key={index}>
                      <Col md={12}>
                        <FormGroup check>
                          <Label className="pt-2 ml-4">
                            <Input type="radio" id={customer?.node?.id}
                                   onChange={() => setSelectedCustomers(customer?.node)}/>
                            <p>{customer?.node?.displayName}</p>
                          </Label>
                        </FormGroup>
                      </Col>
                    </Row>

                  )
                })
              }
            </>
          }
        </ModalBody>
        <ModalFooter className="d-block">
          <div className='d-flex'>
            <div>
              <Button className="mr-2" outline onClick={handleCancel}>Cancel</Button>
              <Button color="primary" onClick={handleAdd}> Add </Button>
            </div>
          </div>
        </ModalFooter>
      </Modal>
    </div>
  );
};

const mapStateToProps = state => ({
  customer: state.customer.customer,
  loading: state.customer.loading,
  customerDetails: state.customer.customerDetails,
  customerPaymentDetails: state.customer.customerPaymentDetails,
  subscriptionGroupEntity: state.subscriptionGroup.entity,
});

const mapDispatchToProps = {
  getShopifyCustomerBySearch,
  getShopifyCustomerDetails,
  getShopifyCustomerPaymentDetails,
  updateStateEntity,
  updatePaymentMethodToState
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchCustomerModal);
