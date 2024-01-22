import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import "./style.scss";
import ProductSelection from './ProductSelection';
import axios from "axios";
import _ from "lodash";
const queryString = require('query-string');
import 'react-toastify/dist/ReactToastify.css';
import { getToken } from './Bundle.util';
import { getEntityByShop } from 'app/entities/subscription-bundle-settings/subscription-bundle-settings.reducer';

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
    <div className="appstle_bundle" >
      <div className="">
        {
          loaded ?
          <ProductSelection products={products} bundleData={bundleData} bundleSlug={bundleSlug} subscriptionBundleSettingsEntity={props.subscriptionBundleSettingsEntity} /> :
          (<div style={{ margin: '10% 0 0 43%', flexDirection: 'column' }} className="loader-wrapper d-flex justify-content-center align-items-center">
                <div class="appstle_preloader appstle_loader--big"></div>
            </div>)
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
