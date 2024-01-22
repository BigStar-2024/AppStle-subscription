import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import productList from "./products";
import "./style.scss";
import ProductSelection from './ProductSelection';
import axios from "axios";
import _ from "lodash";
const queryString = require('query-string');
import 'react-toastify/dist/ReactToastify.css';
import { getToken } from './Bundle.util';
import { getEntityByShop } from 'app/entities/subscription-bundle-settings/subscription-bundle-settings.reducer';
import Loader from './Loader';

const Bundles = (props) => {
  const bundleSlug = getToken();
  const [products, setProducts] = useState([]);
  const [bundleData, setBundleData] = useState({bundle: {minProductCount: 5}});
  const [loaded, setLoaded] = useState(false)

  useEffect(() => {
    props.getEntityByShop(Shopify.shop);
    axios.get(`/api/subscription-bundlings/external/get-bundle/${bundleSlug}`).then(async resp => {
      let response = resp.data;
      setBundleData(response)
      let processes = [];
      _.each(response.products, prod => {
        if (Shopify.shop === "biltongsh.myshopify.com") {
          processes.push(fetch(`${location.origin}/products/${prod.productHandle}.js`))
        } else {
          processes.push(fetch(`https://${prod.shop}/products/${prod.productHandle}.js`))
        }
      })
      let productListData = [];
      let results = await Promise.all(processes)
      let validResults = results.filter(response => response.ok);
      validResults = await Promise.all(validResults.map(result => result.json()))
      productListData = _.map(validResults, o => ({product: o}));
      setProducts(productListData);
      setLoaded(true);
    })
  }, [])



  return (
    <div className="appstle_bundle as-overflow-hidden" >
      <div className="">
        {
          loaded ?
          <ProductSelection products={products} bundleData={bundleData} bundleSlug={bundleSlug} subscriptionBundleSettingsEntity={props.subscriptionBundleSettingsEntity} /> :
          <div className="as-h-screen as-flex as-items-center as-justify-center as-bg-gray-100">
            <Loader /> <span className="as-ml-2 as-text-sm">Please wait...</span>
          </div>
        }

      </div>
    </div>
  )
}

const mapStateToProps = state => ({
  subscriptionBundleSettingsEntity: state.subscriptionBundleSettings.entity,
});

const mapDispatchToProps = {
  getEntityByShop
};

export default connect(mapStateToProps, mapDispatchToProps)(Bundles);
