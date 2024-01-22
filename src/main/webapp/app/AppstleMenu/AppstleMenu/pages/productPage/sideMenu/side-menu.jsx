import React, {useLayoutEffect, useState} from 'react';
import {useMainContext} from 'app/AppstleMenu/context';

const SideMenu = ({tags, filterProductHandler, sideMenuJson, vendors}) => {
  const {
    filterMenuList,
    selectedFilterMenu,
    setSelectedFilterMenu,
    filterValues,
    setFilterValues,
    setInitialQuery,
    initialQuery,
    filterQuery,
    setFilterQuery,
    handleChangeFilterValues
  } = useMainContext();

  const [widthClass, setWidthClass] = useState('');

  const tagsArrayHandler = (filterValue, isChecked, filterType) => {
    if (filterType === 'tag_filter') {
      if (isChecked) {
        handleChangeFilterValues([...filterValues, {filterType: filterType, filterValue: filterValue}]);
      } else {
        if (filterValues?.length) {
          const filterNew = [...filterValues];
          const updatedFilterValues = filterNew.filter(item => !(item.filterType === filterType && item.filterValue == filterValue));
          handleChangeFilterValues(updatedFilterValues);
        }
      }
    } else if (filterType === 'vendor_filter') {
      if (isChecked) {
        handleChangeFilterValues([...filterValues, {filterType: filterType, filterValue: filterValue}]);
      } else {
        const filterNew = [...filterValues];
        const updatedFilterValues = filterNew.filter(item => !(item.filterType === filterType && item.filterValue == filterValue));
        handleChangeFilterValues(updatedFilterValues);
      }
    }
  };

  useLayoutEffect(() => { 
    if (window.innerWidth < 768) {
      setWidthClass('as-flex');
    } else {
      setWidthClass('');
    }
  }, [window.innerWidth]);
  return (
    <div className={`as-w-full ${widthClass}`}>
      <div className="as-mb-3">
        {selectedFilterMenu !== null &&
          selectedFilterMenu?.filterGroups?.map((filterGroup, index) => {
            return (
              <>
                {filterGroup?.filterOptions && filterGroup?.filterOptions.length > 0 ? (
                  <React.Fragment key={index}>
                    <div className="as-mb-2 as-text-2xm as-font-bold as-tracking-tight as-text-black as-mt-2">
                      <span className="as-shadow-sm as-ml-2 as-ext-sm as-font-medium as-text-black appstle-tag-label">
                        {filterGroup?.filterGroupTitle || ''}
                      </span>
                    </div>
                    {filterGroup?.filterOptions?.map(option => {
                      return (
                        <div class="as-flex as-items-center as-ml-2 as-mt-1">
                          <input
                            checked={
                              filterValues.filter(
                                item => item.filterType === filterGroup.filterGroupType && item.filterValue === option.filterOptionValue
                              ).length > 0
                            }
                            id={option.filterOptionValue}
                            type="checkbox"
                            value={option.filterOptionValue}
                            onChange={e => tagsArrayHandler(option.filterOptionValue, e.target.checked, filterGroup.filterGroupType)}
                            style={{ width: 15 }}
                            class="as-w-4 as-h-4 as-text-indigo-600 as-rounded as-border-gray-300 focus:as-ring-indigo-500 dark:focus:as-ring-indigo-600 dark:as-ring-offset-gray-800 focus:as-ring-2 dark:as-border-gray-600 appstle-checkbox"
                          />
                          <label for={option.filterOptionValue} class="as-ml-2 as-ext-sm  as-flex as-flex">
                            <span className=" as-text-black-700 dark:as-text-black-400 as-font-light appstle-checkbox-label">
                              {option?.filterOptionTitle !== '' ? option?.filterOptionTitle : option?.filterOptionValue}
                            </span>
                          </label>
                        </div>
                      );
                    })}
                  </React.Fragment>
                ) : ('')}
              </>
            );
          })}
      </div>
    </div>
  );
};
export default SideMenu;
