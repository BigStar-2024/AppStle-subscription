package com.et.web.rest;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication;
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme;

public class ShopifyOauth extends DefaultApi20 {

  private final String shop;

  public ShopifyOauth(String shop) {
    this.shop = shop;
  }

  @Override
  public Verb getAccessTokenVerb() {
    return Verb.POST;
  }

  @Override
  public String getAccessTokenEndpoint() {
    return "https://shop_name/admin/oauth/access_token".replace("shop_name", shop);
  }

  @Override
  protected String getAuthorizationBaseUrl() {
    return "https://shop_name/admin/oauth/authorize".replace("shop_name", shop);
  }

  @Override
  public ClientAuthentication getClientAuthentication() {
    return RequestBodyAuthenticationScheme.instance();
  }
}
