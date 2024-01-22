import React from "react";
import useScreenSize from "use-screen-size";

export default function withScreenSize(Component) {
  return function WrappedComponent(props) {
    const size = useScreenSize()
    return <Component {...props} size={size} />;
  }
}
