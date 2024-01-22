import React, { useEffect, useState } from 'react';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Col,
  Collapse,
  FormGroup,
  Input,
  Label,
  Modal,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Row
} from 'reactstrap';

import { connect } from 'react-redux';
import { Field } from 'react-final-form';
import Switch from 'react-switch';
import { MdAdd, MdCancel, MdEdit } from 'react-icons/md';
import { getProductFilterData } from '../../../../entities/product-info/product-info.reducer';

const ManageFilterData = ({
  productFilterConfig,
  setProductFilterConfig,
  values,
  settingEntity,
  productFilterData,
  getProductFilterData,
  isEnabledAndCondtionToggle,
  module,
  ...props
}) => {
  const [isFilterPopupOpen, setIsFilterPopupOpen] = useState(false);
  const [editPopupFilterData, setEditPopupFilterData] = useState({});
  const [item, setItem] = useState({ label: '', value: '' });
  const [items, setItems] = useState([]);
  const [filterTypeList, setFilterTypeList] = useState([]);

  const [options, setOptions] = useState([
    { label: 'Sort Name A - Z', value: 1 },
    { label: 'Sort Name Z - A', value: 2 },
    { label: 'Sort Price Low - High', value: 3 },
    { label: 'Sort Price High - Low', value: 4 }
  ]);

  const addFilterItem = () => {
    setItems(items => [...items, item]);
    setItem({ label: '', value: '' });
  };

  const onClickAddFilter = type => {
    if (type) {
      let data = null;
      if (productFilterConfig?.filters?.length > 0) {
        let filterData = productFilterConfig?.filters?.find(a => a?.basedOn == type);
        if (filterData) {
          data = filterData;
        }
      }
      let d = productFilterConfig?.filters || [];
      if (!data) {
        data = { title: type, basedOn: type, item: [] };
        d.push(data);
        setProductFilterConfig({ ...productFilterConfig, filters: d });
      }
      setEditPopupFilterData(data);
      setIsFilterPopupOpen(true);
    }
  };

  useEffect(() => {
    if (settingEntity?.productFilterConfig) {
      setProductFilterConfig(JSON.parse(settingEntity?.productFilterConfig));
    }
  }, [settingEntity]);

  const saveItemsData = () => {
    setIsFilterPopupOpen(false);
    let updatedFilterData = productFilterConfig?.filters?.map(v =>
      v.basedOn == editPopupFilterData.basedOn ? { ...editPopupFilterData, item: items } : v
    );
    setProductFilterConfig({ ...productFilterConfig, filters: updatedFilterData });
    setItems([]);
    setItem({ label: '', value: '' });
  };

  const editItemData = (element, index) => {
    items[index][element.target.name] = element.target.value;
    setItems(items => [...items]);
  };

  const deleteItemData = index => {
    setItems(items => [...items.filter((v, i) => i != index)]);
  };

  const deleteFilterItemData = index => {
    let filters = productFilterConfig?.filters?.filter((value, i) => index != i);
    setProductFilterConfig({ ...productFilterConfig, filters: filters });
  };

  useEffect(() => {
    if (editPopupFilterData?.item?.length > 0) {
      setItems(editPopupFilterData?.item);
    }
  }, [editPopupFilterData]);

  useEffect(() => {
    if (productFilterConfig && productFilterConfig?.filters?.length > 0) {
      setFilterTypeList(productFilterConfig?.filters?.map(type => type.basedOn));
    }
  }, [productFilterConfig]);

  const addDefaultSearchValue = () => {
    getProductFilterData();
  };

  useEffect(() => {
    if (productFilterData && productFilterData?.hasOwnProperty(editPopupFilterData?.basedOn)) {
      let options = productFilterData[editPopupFilterData.basedOn]?.map(a => {
        return { label: a, value: a };
      });
      setItems(options);
    }
  }, [productFilterData]);

  return (
    <>
      <Row>
        <Col xs={12} sm={12} md={8} lg={8}>
          <FormGroup style={{ display: 'flex' }}>
            <Field
              render={({ input, meta }) => (
                <Switch
                  checked={Boolean(productFilterConfig.enabled)}
                  onColor="#3ac47d"
                  onChange={e => {
                    input.onChange(e);
                    setProductFilterConfig({ ...productFilterConfig, enabled: e });
                  }}
                  handleDiameter={20}
                  boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                  activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                  height={17}
                  width={36}
                  id="material-switch"
                />
              )}
              className="form-control"
              name="productFilterConfig.enabled"
            />
            <Label for="productFilterConfig.enabled" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
              Enable Product Filter
            </Label>
          </FormGroup>
        </Col>
      </Row>
      {productFilterConfig?.enabled && (
        <>
          <Row>
            <Col xs={12} sm={12} md={6} lg={6}>
              <FormGroup className="d-flex align-items-end">
                <div style={{ flex: 1 }}>
                  <Label for="addFilterType">Filter Group Type</Label>
                  <Field
                    render={({ input, meta }) => (
                      <Input {...input} invalid={meta.error && meta.touched ? true : null}>
                        <option value="">-- Select Filter Type -- </option>
                        {!filterTypeList?.includes('vendor') ? <option value="vendor">Vendor</option> : ''}
                        {!filterTypeList?.includes('productType') ? <option value="productType">Product Type</option> : ''}
                        {!filterTypeList?.includes('tags') ? <option value="tags">Tags</option> : ''}
                        {!filterTypeList?.includes('sorting') ? <option value="sorting">Sorting</option> : ''}
                      </Input>
                    )}
                    type="select"
                    className="form-control"
                    name="addFilterType"
                  />
                </div>
                <div className="ml-2">
                  <Label for="addFilter"> </Label>
                  <Button onClick={() => onClickAddFilter(values?.addFilterType)} color="primary" style={{ lineHeight: '1.8' }}>
                    Add filter
                  </Button>
                </div>
              </FormGroup>
            </Col>
            {isEnabledAndCondtionToggle && (
              <Col xs={12} sm={12} md={6} lg={6}>
                <FormGroup className="d-flex align-items-end" style={{ paddingTop: '35px' }}>
                  <Field
                    render={({ input, meta }) => (
                      <Switch
                        checked={Boolean(productFilterConfig?.isEnableAndCondtion)}
                        onColor="#3ac47d"
                        onChange={e => {
                          setProductFilterConfig({ ...productFilterConfig, isEnableAndCondtion: e });
                        }}
                        handleDiameter={20}
                        boxShadow="0px 1px 5px rgba(0, 0, 0, 0.6)"
                        activeBoxShadow="0px 0px 1px 2px rgba(0, 0, 0, 0.2)"
                        height={17}
                        width={36}
                        id="material-switch"
                      />
                    )}
                    className="form-control"
                    name="productFilterConfig.isEnableAndCondtion"
                  />
                  <Label for="productFilterConfig.isEnableAndCondtion" style={{ marginBottom: '0rem', marginLeft: '2rem' }}>
                    Enable And Condtion
                  </Label>
                </FormGroup>
              </Col>
            )}
          </Row>
          <Row>
            {productFilterConfig?.filters?.length > 0 &&
              productFilterConfig?.filters?.map((v, i) => (
                <Col xs={12} sm={12} md={6} lg={6}>
                  <Card className="mt-2">
                    <div className="d-flex align-items-center">
                      <CardHeader
                        style={{ width: '100%' }}
                        className="d-flex justify-content-between"
                        onClick={() => {}}
                        aria-expanded={true}
                        aria-controls={`collapseFour${0}`}
                      >
                        <span>
                          <div>
                            <small className="text-danger">{v.title}</small>
                          </div>
                        </span>
                        <span>
                          <Button type="button" className="mr-1 btn btn-danger" onClick={() => deleteFilterItemData(i)}>
                            <MdCancel color="#fff" size={22} />
                          </Button>
                          <Button type="button" className="mr-1 btn btn-success" onClick={() => onClickAddFilter(v.basedOn)}>
                            <MdEdit color="#fff" size={22} />
                          </Button>
                        </span>
                      </CardHeader>
                    </div>
                    <Collapse isOpen={true} data-parent="#accordion" id={`collapseFour${0}`}>
                      <CardBody>
                        {v?.item?.map(i => (
                          <Row>
                            <Col>
                              {i.label} - {i.value}{' '}
                            </Col>
                          </Row>
                        ))}
                      </CardBody>
                    </Collapse>
                  </Card>
                </Col>
              ))}
          </Row>
          <Modal isOpen={isFilterPopupOpen} toggle={() => setIsFilterPopupOpen(false)} className="modal-lg">
            <ModalHeader toggle={() => setIsFilterPopupOpen(false)}>Add Filter Data</ModalHeader>
            <ModalBody>
              <Row>
                <Col sm={8}>
                  <FormGroup>
                    <Label for="title">Filter Title</Label>
                    <Input
                      type="text"
                      name="title"
                      value={editPopupFilterData?.title}
                      onChange={e => setEditPopupFilterData({ ...editPopupFilterData, title: e.target.value })}
                      className="form-control"
                    />
                  </FormGroup>
                </Col>
                <Col sm={4} className="d-flex align-items-end w-100">
                  <FormGroup>
                    <Button onClick={() => addDefaultSearchValue()} className="btn btn-success">
                      Add default search value
                    </Button>
                  </FormGroup>
                </Col>
              </Row>
              <Row>
                {editPopupFilterData?.basedOn == 'sorting' ? (
                  <Col sm={5}>
                    <FormGroup>
                      <Label for="label">Select Sorting Filter</Label>
                      <Input
                        type="select"
                        onChange={e => {
                          if (e?.target?.value) {
                            let d = options?.find(item => item.value == e?.target?.value);
                            setItem({ label: d.label, value: e.target.value });
                          }
                        }}
                        className="form-control"
                      >
                        <option>Select Sort By</option>
                        {options?.map((option, index) => {
                          return items?.filter(item => option.value == item.value)?.length ? (
                            ''
                          ) : (
                            <option value={option.value}>
                              {option.label} {'->'} {option.value}{' '}
                            </option>
                          );
                        })}
                      </Input>
                    </FormGroup>
                  </Col>
                ) : (
                  <>
                    <Col sm={5}>
                      <FormGroup>
                        <Label for="label">Enter Label</Label>
                        <Input
                          type="text"
                          value={item?.label}
                          onChange={e => setItem({ ...item, label: e.target.value })}
                          className="form-control"
                        />
                      </FormGroup>
                    </Col>
                    <Col sm={5}>
                      <FormGroup>
                        <Label for="value">Enter Value</Label>
                        <Input
                          type="text"
                          value={item?.value}
                          onChange={e => setItem({ ...item, value: e.target.value })}
                          className="form-control"
                        />
                      </FormGroup>
                    </Col>
                  </>
                )}
                <Col sm={2} className="d-flex align-items-end">
                  <FormGroup>
                    <Button color="primary" onClick={() => addFilterItem()}>
                      <MdAdd color="#fff" size={22} />
                    </Button>
                  </FormGroup>
                </Col>
              </Row>
              {items.map((v, index) => (
                <Row>
                  <Col sm={5}>
                    <FormGroup>
                      <Input type="text" value={v.label} name="label" onChange={e => editItemData(e, index)} className="form-control" />
                    </FormGroup>
                  </Col>
                  <Col sm={5}>
                    <FormGroup>
                      <Input
                        type="text"
                        value={v.value}
                        name="value"
                        readOnly={editPopupFilterData?.basedOn == 'sorting' && 'readonly'}
                        onChange={e => editItemData(e, index)}
                        className="form-control"
                      />
                    </FormGroup>
                  </Col>
                  <Col sm={2} className="d-flex align-items-end w-100">
                    <FormGroup>
                      <Button className="btn btn-danger" onClick={() => deleteItemData(index)}>
                        <MdCancel color="#fff" size={22} />
                      </Button>
                    </FormGroup>
                  </Col>
                </Row>
              ))}
            </ModalBody>
            <ModalFooter>
              <Button color="primary" onClick={() => saveItemsData()}>
                Save Filter
              </Button>
            </ModalFooter>
          </Modal>
        </>
      )}
    </>
  );
};
const mapStateToProps = state => ({
  productFilterData: state.productInfo.productFilterData
});
const mapDispatchToProps = { getProductFilterData };
export default connect(mapStateToProps, mapDispatchToProps)(ManageFilterData);
