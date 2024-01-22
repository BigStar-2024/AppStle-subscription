import React from 'react'
import {
    Button,
    Table,
    InputGroup,
    Input,
    Modal,
    ModalHeader,
    ModalBody,
    ModalFooter ,
    Label,
    FormGroup,
    Row,
    Col,
    Card,
    CardBody,
    ListGroup,
    Dropdown,
    UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle,
    ListGroupItem,
    PaginationItem,
    PaginationLink
  } from 'reactstrap';

const SubAnalyticsViewModel = (props) => {

    const {
        toggle,
        analyticsData,
        isToggle,
        subContractId
    } = props
    return (
            <Modal isOpen={toggle} toggle={isToggle} className="modal-lg">
                <ModalHeader toggle={isToggle}>Analysis of #{subContractId}</ModalHeader>
                <ModalBody>
              <Row>
                  <Col md={6}>
                  <div className="card no-shadow bg-transparent widget-chart  p-0   text-right">
                    <Card className="main-card mb-3" style={{width: '100%'}}>
                      <CardBody>
                        <div><b>Total Order Count</b></div>
                        <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                        {analyticsData?.totalOrderCount}
                        </div>
                      </CardBody>
                    </Card>
                  </div>
                    </Col>
                    <Col md={6}>
                    <div className="card no-shadow bg-transparent widget-chart  p-0   text-right">
                    <Card className="main-card mb-3" style={{width: '100%'}}>
                      <CardBody>
                        <div><b>Total Order Revenue</b></div>
                        <div className="widget-numbers text-success" style={{fontSize: '2.0rem'}}>
                        {analyticsData?.totalOrderRevenue}
                        </div>
                      </CardBody>
                    </Card>
                  </div>
                    </Col>
              </Row>
                </ModalBody>
                {/* <ModalFooter>
                <Button color="primary" onClick={isToggle}>Do Something</Button>{' '}
                <Button color="secondary" onClick={isToggle}>Cancel</Button>
                </ModalFooter> */}
            </Modal>
    )
}

export default SubAnalyticsViewModel
