import React from 'react';
import {useHistory, useLocation} from 'react-router-dom';
import {useMainContext} from 'app/AppstleMenu/context';

const Header = props => {
  const location = useLocation();
  const history = useHistory();
  const {filterMenuList, selectedFilterMenu, setSelectedFilterMenu, handleChangeFilterMenu} = useMainContext();
  return (
    <div
      className=" as-w-100 as-p-4 as-bg-white as-shadow appstle-header"
      style={{minHeight: 84}}
    >
      <div
        className="as-container as-mx-auto xl:as-justify-center lg:as-justify-center as-flex appstle-menu-header-item-container"
        style={{overflow: 'scroll'}}
      >
        {selectedFilterMenu &&
          filterMenuList?.map((item, index) => {
            return (
              <button
                key={index}
                type="button"
                onClick={() => handleChangeFilterMenu(item)}
                class={`${
                  item.menuType === selectedFilterMenu.menuType && item.menuTitle === selectedFilterMenu.menuTitle
                    ? 'as-bg-indigo-600 as-text-white as-rounded-full appstle-menu-item-selected'
                    : 'as-text-indigo-600 as-rounded-full as-border-solid as-border-2 as-border-indigo-600  appstle-menu-item-not-selected'
                } as-font-medium as-text-sm as-px-5 as-py-2.5 as-text-center as-inline-flex as-items-center as-mr-2 as-mb-2 as-whitespace-nowrap  appstle-tab-item`}
              >
                {item.menuTitle}
              </button>
            );
          })}
      </div>
      {location.pathname === '/subscribe' ? (
        <div className="as-container as-mx-auto as-justify-center as-flex as-mt-5">
          <span className="as-flex">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFCE30" class="as-w-6 as-h-6">
              <path
                fill-rule="evenodd"
                d="M12 1.5c-1.921 0-3.816.111-5.68.327-1.497.174-2.57 1.46-2.57 2.93V21.75a.75.75 0 001.029.696l3.471-1.388 3.472 1.388a.75.75 0 00.556 0l3.472-1.388 3.471 1.388a.75.75 0 001.029-.696V4.757c0-1.47-1.073-2.756-2.57-2.93A49.255 49.255 0 0012 1.5zm3.53 7.28a.75.75 0 00-1.06-1.06l-6 6a.75.75 0 101.06 1.06l6-6zM8.625 9a1.125 1.125 0 112.25 0 1.125 1.125 0 01-2.25 0zm5.625 3.375a1.125 1.125 0 100 2.25 1.125 1.125 0 000-2.25z"
                clip-rule="evenodd"
              />
            </svg>
            <span className="as-ml-2 as-ext-sm as-font-medium as-text-white dark:as-text-white-300">
              25% off first shipment, then 15% off recurring
            </span>
          </span>
          <span className="as-flex as-ml-5">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFCE30" class="as-w-6 as-h-6">
              <path
                d="M12.75 12.75a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM7.5 15.75a.75.75 0 100-1.5.75.75 0 000 1.5zM8.25 17.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM9.75 15.75a.75.75 0 100-1.5.75.75 0 000 1.5zM10.5 17.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM12 15.75a.75.75 0 100-1.5.75.75 0 000 1.5zM12.75 17.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM14.25 15.75a.75.75 0 100-1.5.75.75 0 000 1.5zM15 17.25a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM16.5 15.75a.75.75 0 100-1.5.75.75 0 000 1.5zM15 12.75a.75.75 0 11-1.5 0 .75.75 0 011.5 0zM16.5 13.5a.75.75 0 100-1.5.75.75 0 000 1.5z"/>
              <path
                fill-rule="evenodd"
                d="M6.75 2.25A.75.75 0 017.5 3v1.5h9V3A.75.75 0 0118 3v1.5h.75a3 3 0 013 3v11.25a3 3 0 01-3 3H5.25a3 3 0 01-3-3V7.5a3 3 0 013-3H6V3a.75.75 0 01.75-.75zm13.5 9a1.5 1.5 0 00-1.5-1.5H5.25a1.5 1.5 0 00-1.5 1.5v7.5a1.5 1.5 0 001.5 1.5h13.5a1.5 1.5 0 001.5-1.5v-7.5z"
                clip-rule="evenodd"
              />
            </svg>
            <span className="as-ml-2 as-ext-sm as-font-medium as-text-white dark:as-text-white-300">Skip, swap or cancel anytime</span>
          </span>
          <span className="as-flex as-ml-5">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#FFCE30" class="as-w-6 as-h-6">
              <path
                d="M7.493 19.5c-.425 0-.82-.236-.975-.632A7.48 7.48 0 016 16.125c0-1.75.599-3.358 1.602-4.634.151-.192.373-.309.6-.397.473-.183.89-.514 1.212-.924a9.041 9.041 0 012.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 00.322-1.672V3.75A.75.75 0 0115 3a2.25 2.25 0 012.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 01-2.649 7.521c-.388.482-.987.729-1.605.729H14.23c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 00-1.423-.23h-.777zM2.331 11.727a11.969 11.969 0 00-.831 4.398 12 12 0 00.52 3.507C2.28 20.482 3.105 21 3.994 21H4.9c.445 0 .72-.498.523-.898a8.963 8.963 0 01-.924-3.977c0-1.708.476-3.305 1.302-4.666.245-.403-.028-.959-.5-.959H4.25c-.832 0-1.612.453-1.918 1.227z"/>
            </svg>
            <span className="as-ml-2 as-ext-sm as-font-medium as-text-white dark:as-text-white-300">Free shipping, always</span>
          </span>
        </div>
      ) : null}
    </div>
  );
};
export default Header;
