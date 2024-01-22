import React from 'react';
import { VariableSizeList as List } from "react-window";
import InfiniteLoader from "react-window-infinite-loader";
import { Row, Col, Button } from "reactstrap"
import { formatPrice } from 'app/shared/util/customer-utils';

function VirtualizedProductList(props) {
    const {
        children,
        hasNextPage,
        isNextPageLoading,
        items,
        loadNextPage,
        listStyle,
        itemSize,
        height,
        width,
        listRef
    } = props;
    const itemCount = hasNextPage ? items.length + 1 : items.length;
    const loadMoreItems = isNextPageLoading ? () => undefined : loadNextPage;
    const isItemLoaded = index => !hasNextPage || index < items.length;


    const RowRenderer = (rowProps) => {
        const product = items[rowProps.index]?.props?.data;
        const child = items[rowProps.index];
        // console.log("RowRenderer", rowProps, child);
        return (
            <>
                {
                    product ?
                        <Row key={rowProps.index} style={{
                            borderBottom: '1px solid #ccc',
                            position: 'relative',
                            // display: 'flex',
                            alignItems: 'center',
                            ...rowProps.style,
                        }}>
                            <Col md={3} className="appstle-text-center" >
                                <img src={product?.payload?.imgSrc} style={{ height: "70px" }} />
                            </Col>
                            <Col md={6}>
                                {/* {child} */}
                                {product?.payload?.title}
                            </Col>
                            <Col md={3}>
                                <span>{formatPrice(product?.payload?.price)}</span>
                                {/* {" "} */}
                                {/* <span>{product?.payload?.currencyCode}</span> */}
                            </Col>
                              {child}
                        </Row>
                         : null
                }
            </>
        )
    }
    return (
        <InfiniteLoader
            isItemLoaded={isItemLoaded}
            itemCount={itemCount}
            loadMoreItems={loadMoreItems}
        >
            {({ onItemsRendered, ref }) => {
                return (
                    <List
                        ref={currentRef => {
                            if (listRef) {
                                listRef(currentRef);
                            }
                            ref(currentRef);
                        }}
                        width={width}
                        height={height}
                        itemCount={itemCount}
                        itemSize={itemSize}
                        style={listStyle}
                        onItemsRendered={onItemsRendered}
                        className="VirtualizedList-Component"
                    >
                        {RowRenderer}
                        {/* {listProps => {
                            return children(listProps);
                        }} */}
                    </List>
                );
            }}
        </InfiniteLoader>
    )
}

export default VirtualizedProductList
