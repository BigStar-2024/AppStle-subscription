import React from 'react';
import { AiOutlineClose } from 'react-icons/ai';

const Drawer = ({ showSidebar, setShowSidebar, children }) => {
  return (
    <>
      {showSidebar && (
        <div
          className={`as-fixed as-right-0 as-h-screen as-text-black as-py-4 as-px-3 as-overflow-hidden as-bg-white as-shadow-xl as-delay-400 as-duration-500 as-ease-in-out as-transition-all as-transform ${
            showSidebar ? 'as-transition-opacity as-opacity-100 as-duration-500' : 'as-transition-all as-delay-500 as-opacity-0'
          }`}
          style={{ zIndex: '9999', width: '90%' }}
        >
          <div className="as-flex as-justify-between">
            <AiOutlineClose className="as-cursor-pointer as-ml-1" size={20} onClick={() => setShowSidebar(!showSidebar)} />
            <div />
            <h2 className="as-text-black as-font-semibold">Filters</h2>
          </div>
          {children}
        </div>
      )}
    </>
  );
};

export default Drawer;
