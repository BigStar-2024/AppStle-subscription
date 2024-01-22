import React, {useState} from 'react';
import {FiPlus} from 'react-icons/fi';
import {HiOutlineFilter} from 'react-icons/hi';
import {MdCancel} from 'react-icons/md';
import './buildAppstleMenu.scss';
import {
  Button,
  Card,
  CardBody,
  CardHeader,
  Collapse,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  ModalBody,
  ModalFooter,
  ModalHeader
} from 'reactstrap';

import Rodal from 'rodal';
import {connect} from "react-redux";

const AppstleMenuModal = ({
                            show,
                            setShow,
                            selected,
                            addToMenuHandler,
                            openAddFilter,
                            setOpenAddFilter,
                            addTagHandler,
                            filterInputValue,
                            setFilterInputValue,
                            setFilterInputLabel,
                            filterInputLabel,
                            deleteTagHanfler,
                            modalConditionCase,
                            initStateAppstleMenu,
                            deleteGroupHandler,
                            toggleAccordion,
                            accordion,
                            setAccordion, addFilterOptionHandler,
                            setInitStateAppstleMenu,
                            subscriptionGroupEntities,
                            collections
                          }) => {
  let submit;
  const [formErrors, setFormErrors] = useState(null);

  return (
    <Rodal visible={show} onClose={() => {
      setShow(false), setAccordion([])
    }} animation={'zoom'} showMask={false}>
      <ModalHeader>{modalConditionCase === 'add' ? 'Create' : 'Edit'} Collection</ModalHeader>
      <ModalBody style={{maxHeight: '550px', overflow: 'auto'}}>
        <p>{selected?.label}</p>
        <div className="d-flex align-items-center justify-content-between mt-2">
          <small>Add a title to display to your customer</small>
          <div style={{maxWidth: '200px'}}>
            <Input
              name="select"
              type="text"
              placeholder="Enter Title"
              value={initStateAppstleMenu?.menuTitle}
              onChange={e => {
                setInitStateAppstleMenu({...initStateAppstleMenu, menuTitle: e.target.value});
              }}
            />
          </div>
        </div>
        {selected?.value === 'BUNDLE' ?
          <div className="d-flex align-items-center justify-content-between mt-2">
            <small>Select your source collections in shopify</small>
            <Input
              name="select"
              type="select"
              placeholder="select collection"
              value={initStateAppstleMenu?.sourceCollection}
              onChange={e => {
                setInitStateAppstleMenu({...initStateAppstleMenu, sourceCollection: e.target.value});
              }}>
              <option selected value="testingID12345">
                Subscription Plan
              </option>
              <option value="testingID6789">Testing Plan</option>
              <option value="testingID678934334">Order Plan</option>
            </Input>
          </div> : selected?.value === 'SUBSCRIBE' ?
            <div className="d-flex align-items-center justify-content-between mt-2">
              <small>Select your source Suscription group</small>
              <Input
                name="select"
                type="select"
                placeholder="Select subscription group"
                value={initStateAppstleMenu?.subscriptionGroup}
                onChange={e => {
                  setInitStateAppstleMenu({
                    ...initStateAppstleMenu,
                    subscriptionGroup: e.target.value
                  });
                }}
              >
                <option>Select subscription group</option>
                {subscriptionGroupEntities.length && subscriptionGroupEntities.map((item, index) => (
                  <option value={item.id}>
                    {item.groupName}
                  </option>
                ))}
              </Input>
            </div> : selected?.value === 'ONE_TIME' ?
              <div className="d-flex align-items-center justify-content-between mt-2">
                <small>Select your source Collection</small>
                <Input
                  name="select"
                  type="select"
                  placeholder="Select collection"
                  value={initStateAppstleMenu?.sourceCollection}
                  onChange={e => {
                    setInitStateAppstleMenu({
                      ...initStateAppstleMenu,
                      sourceCollection: e.target.value
                    });
                  }}
                >
                  <option>Select Collection</option>
                  {collections.length && collections.map((item, index) => (
                    <option value={item.id}>
                      {item.title}
                    </option>
                  ))}
                </Input>
              </div> : ""}
        {openAddFilter && initStateAppstleMenu?.filterGroups?.length
          ? initStateAppstleMenu?.filterGroups.map((groupFilter, index) => {
            return (
              <Card className="mt-2">
                <div className='d-flex align-items-center'>

                  <CardHeader
                    style={{width: '100%'}}
                    className="d-flex justify-content-between"
                    onClick={() => toggleAccordion(index, accordion)}
                    aria-expanded={accordion[index]}
                    aria-controls={`collapse${index}`}
                  >
                    <span>
                      {groupFilter.filterGroupFieldNameInValid ? (
                        <div>
                          <small className="text-danger">
                            {groupFilter.filterGroupFieldNameInValid && !groupFilter.filterGroupFieldNameErrorText
                              ? `please provide Filter Name`
                              : groupFilter.filterGroupFieldNameInValid && groupFilter.filterGroupFieldNameErrorText
                                ? groupFilter.filterGroupFieldNameErrorText
                                : null}
                          </small>
                        </div>
                      ) : groupFilter.filterGroupTitle ? (
                        groupFilter.filterGroupTitle
                      ) : (
                        'Please Enter Filter Name'
                      )}
                    </span>
                  </CardHeader>
                  <MdCancel
                    color="#ff000082"
                    className='mr-1'
                    size={22}
                    onClick={() => {
                      deleteGroupHandler(index);
                    }}
                  />
                </div>
                <Collapse isOpen={accordion[index]} data-parent="#accordion"
                          id={`collapseFour${index}`}>
                  <CardBody>
                    <div className="custom-filter-options mt-3 p-2">
                      <p>
                        <small>Add a filter and filter options</small>
                      </p>
                      <div className="d-flex justify-content-around align-items-center mt-1">
                        <FormGroup className={"w-100"}>
                          <InputGroup className={"align-items-center"}>
                            <InputGroupAddon addonType="append">
                              <HiOutlineFilter color="black"
                                               size={22}/></InputGroupAddon>
                            <Input
                              name="select"
                              type="text"
                              placeholder="Filter name"
                              value={groupFilter.filterGroupTitle}
                              invalid={groupFilter.filterGroupFieldNameInValid ? groupFilter.filterGroupFieldNameInValid : null}
                              onChange={e => {
                                if (initStateAppstleMenu.filterGroups.find(item => item.filterGroupTitle === e.target.value)) {
                                  let duplicateObject = {
                                    ...initStateAppstleMenu.filterGroups[index],
                                    filterGroupFieldNameInValid: true,
                                    filterGroupFieldNameErrorText: e.target.value + ' ' + 'is already added'
                                  };
                                  initStateAppstleMenu.filterGroups[index] = duplicateObject;
                                  setInitStateAppstleMenu({...initStateAppstleMenu});
                                } else {
                                  initStateAppstleMenu.filterGroups[index].filterGroupTitle = e.target.value;
                                  initStateAppstleMenu.filterGroups[index].filterGroupFieldNameErrorText = '';
                                  setInitStateAppstleMenu({...initStateAppstleMenu});
                                }
                                const indexes = initStateAppstleMenu?.filterGroups.reduce(
                                  (r, v, i) => r.concat(v.filterGroupTitle === '' ? i : []),
                                  []
                                );
                                if (indexes?.length) {
                                  indexes.map(indexes =>
                                    initStateAppstleMenu.filterGroups.forEach((item, index) =>
                                      !item.filterGroupTitle
                                        ? (item.filterGroupFieldNameInValid = true)
                                        : (item.filterGroupFieldNameInValid = false)
                                    )
                                  );
                                  setInitStateAppstleMenu({...initStateAppstleMenu});
                                } else {
                                  initStateAppstleMenu.filterGroups.forEach((item, index) =>
                                    !item.filterGroupTitle
                                      ? (item.filterGroupFieldNameInValid = true)
                                      : (item.filterGroupFieldNameInValid = false)
                                  );
                                  setInitStateAppstleMenu({...initStateAppstleMenu});
                                }
                              }}
                            />
                            <InputGroupAddon addonType="prepend">
                              <Input
                                name="select"
                                type="select"
                                placeholder="select filter"
                                value={groupFilter?.filterGroupType}
                                onChange={e => {
                                  initStateAppstleMenu.filterGroups[index].filterGroupType = e.target.value;
                                  setInitStateAppstleMenu({...initStateAppstleMenu});
                                }}
                              >
                                <option selected value="tag_filter">
                                  Tag filters
                                </option>
                                <option value="vendor_filter">Vendor Filter</option>
                                {/*<option value="collection_filter">Collection Filter</option>*/}
                              </Input>
                            </InputGroupAddon>
                          </InputGroup>
                        </FormGroup>
                      </div>
                      {groupFilter.filterGroupFieldNameInValid ? (
                        <div>
                          <small className="text-danger">
                            {groupFilter.filterGroupFieldNameInValid && !groupFilter.filterGroupFieldNameErrorText
                              ? `please provide Filter Name`
                              : groupFilter.filterGroupFieldNameInValid && groupFilter.filterGroupFieldNameErrorText
                                ? groupFilter.filterGroupFieldNameErrorText
                                : null}
                          </small>
                        </div>
                      ) : null}
                      <hr></hr>
                      <div className="justify-content-around align-items-center mt-1">
                        <div className="tag-input d-flex align-items-center">
                          <Input
                            autoFocus
                            placeholder={
                              groupFilter.filterGroupType === 'tag_filter'
                                ? 'Product tag Label'
                                : groupFilter.filterGroupType === 'vendor_filter'
                                  ? 'Vendor Name Label'
                                  : groupFilter.filterGroupType === 'collection_filter'
                                    ? 'Collection Lebel'
                                    : ''
                            }
                            value={filterInputLabel}
                            /*onKeyPress={e => {
                              if (e.key === 'Enter') {
                                initStateAppstleMenu?.filterGroups[index].filterOptions.push({
                                  filterOptionTitle: filterInputLabel,
                                  filterOptionValue: filterInputValue
                                });
                                setInitStateAppstleMenu({...initStateAppstleMenu});
                                setFilterInputValue('');
                                setFilterInputLabel('');
                              }
                            }}*/
                            onChange={e => setFilterInputLabel(e.target.value)}
                            invalid={groupFilter.filterGroupFieldInValid ? groupFilter.filterGroupFieldInValid : null}
                          />{' '}


                          <Input
                            autoFocus
                            placeholder={
                              groupFilter.filterGroupType === 'tag_filter'
                                ? 'Product tag value'
                                : groupFilter.filterGroupType === 'vendor_filter'
                                  ? 'Vendor Name'
                                  : groupFilter.filterGroupType === 'collection_filter'
                                    ? 'Collection'
                                    : ''
                            }
                            value={filterInputValue}
                            /*onKeyPress={e => {
                              if (e.key === 'Enter') {
                                initStateAppstleMenu?.filterGroups[index].filterOptions.push({
                                  filterOptionTitle: filterInputLabel,
                                  filterOptionValue: filterInputValue
                                });
                                setInitStateAppstleMenu({...initStateAppstleMenu});
                                setFilterInputValue('');
                                setFilterInputLabel('');
                              }
                            }}*/
                            onChange={e => setFilterInputValue(e.target.value)}
                            invalid={groupFilter.filterGroupFieldInValid ? groupFilter.filterGroupFieldInValid : null}
                          />{' '}


                          <Button className={"ml-1"} color={"primary"} size={"sm"} onClick={() => addTagHandler(index)}>
                            <FiPlus className="ml-2" size={24}
                                  />
                          </Button>

                        </div>
                        {groupFilter.filterGroupFieldInValid ? (
                          <div>
                            <small className="text-danger">
                              {groupFilter.filterGroupFieldInValid
                                ? `please provide ${
                                  groupFilter.filterGroupType === 'tag_filter'
                                    ? 'Product tags'
                                    : groupFilter.filterGroupType === 'vendor_filter'
                                      ? 'Vender Name'
                                      : groupFilter.filterGroupType === 'collection_filter'
                                        ? 'Collection'
                                        : ''
                                }`
                                : null}
                            </small>
                          </div>
                        ) : null}
                        <div className="mt-3 justify-content-around">
                          <b>{groupFilter?.filterGroupTitle}</b>
                          {groupFilter?.filterOptions?.length
                            ? groupFilter?.filterOptions.map(tag => {
                              return (
                                <div className="tags ml-2 mt-2">
                                  {tag?.filterOptionTitle} - ({tag?.filterOptionValue})
                                  <MdCancel
                                    color="#ff000082"
                                    size={22}
                                    onClick={() => deleteTagHanfler(index, tag?.filterOptionValue)}
                                  />
                                </div>
                              );
                            })
                            : null}
                        </div>
                      </div>
                    </div>
                  </CardBody>
                </Collapse>
              </Card>
            );
          })
          : null}
      </ModalBody>
      <ModalFooter>
        <Button color="primary" onClick={addFilterOptionHandler}>
          Add filter
        </Button>
        <Button color="primary" onClick={addToMenuHandler}>
          Save
        </Button>
      </ModalFooter>
    </Rodal>
  );
};
const mapStateToProps = state => ({
  subscriptionGroupEntities: state.subscriptionGroup.entities,
  collections: state.appstleMenuAdmin.collections,
});

const mapDispatchToProps = {};

export default connect(mapStateToProps, mapDispatchToProps)(AppstleMenuModal);
