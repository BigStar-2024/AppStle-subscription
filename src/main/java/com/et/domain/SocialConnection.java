package com.et.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A SocialConnection.
 */
@Entity
@Table(name = "social_connection")
public class SocialConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotNull
    @Column(name = "prover_id", nullable = false)
    private String proverId;

    @NotNull
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "rest_rate_limit")
    private Integer restRateLimit;

    @Column(name = "graphql_rate_limit")
    private Integer graphqlRateLimit;

    @Column(name = "access_token_1")
    private String accessToken1;

    @Column(name = "access_token_2")
    private String accessToken2;

    @Column(name = "access_token_3")
    private String accessToken3;

    @Column(name = "access_token_4")
    private String accessToken4;

    @Column(name = "public_access_token")
    private String publicAccessToken;

    @Column(name = "public_access_token_1")
    private String publicAccessToken1;

    @Column(name = "public_access_token_2")
    private String publicAccessToken2;

    @Column(name = "public_access_token_3")
    private String publicAccessToken3;

    @Column(name = "public_access_token_4")
    private String publicAccessToken4;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public SocialConnection userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProverId() {
        return proverId;
    }

    public SocialConnection proverId(String proverId) {
        this.proverId = proverId;
        return this;
    }

    public void setProverId(String proverId) {
        this.proverId = proverId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public SocialConnection accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getRestRateLimit() {
        return restRateLimit;
    }

    public SocialConnection restRateLimit(Integer restRateLimit) {
        this.restRateLimit = restRateLimit;
        return this;
    }

    public void setRestRateLimit(Integer restRateLimit) {
        this.restRateLimit = restRateLimit;
    }

    public Integer getGraphqlRateLimit() {
        return graphqlRateLimit;
    }

    public SocialConnection graphqlRateLimit(Integer graphqlRateLimit) {
        this.graphqlRateLimit = graphqlRateLimit;
        return this;
    }

    public void setGraphqlRateLimit(Integer graphqlRateLimit) {
        this.graphqlRateLimit = graphqlRateLimit;
    }

    public String getAccessToken1() {
        return accessToken1;
    }

    public SocialConnection accessToken1(String accessToken1) {
        this.accessToken1 = accessToken1;
        return this;
    }

    public void setAccessToken1(String accessToken1) {
        this.accessToken1 = accessToken1;
    }

    public String getAccessToken2() {
        return accessToken2;
    }

    public SocialConnection accessToken2(String accessToken2) {
        this.accessToken2 = accessToken2;
        return this;
    }

    public void setAccessToken2(String accessToken2) {
        this.accessToken2 = accessToken2;
    }

    public String getAccessToken3() {
        return accessToken3;
    }

    public SocialConnection accessToken3(String accessToken3) {
        this.accessToken3 = accessToken3;
        return this;
    }

    public void setAccessToken3(String accessToken3) {
        this.accessToken3 = accessToken3;
    }

    public String getAccessToken4() {
        return accessToken4;
    }

    public SocialConnection accessToken4(String accessToken4) {
        this.accessToken4 = accessToken4;
        return this;
    }

    public void setAccessToken4(String accessToken4) {
        this.accessToken4 = accessToken4;
    }

    public String getPublicAccessToken() {
        return publicAccessToken;
    }

    public SocialConnection publicAccessToken(String publicAccessToken) {
        this.publicAccessToken = publicAccessToken;
        return this;
    }

    public void setPublicAccessToken(String publicAccessToken) {
        this.publicAccessToken = publicAccessToken;
    }

    public String getPublicAccessToken1() {
        return publicAccessToken1;
    }

    public SocialConnection publicAccessToken1(String publicAccessToken1) {
        this.publicAccessToken1 = publicAccessToken1;
        return this;
    }

    public void setPublicAccessToken1(String publicAccessToken1) {
        this.publicAccessToken1 = publicAccessToken1;
    }

    public String getPublicAccessToken2() {
        return publicAccessToken2;
    }

    public SocialConnection publicAccessToken2(String publicAccessToken2) {
        this.publicAccessToken2 = publicAccessToken2;
        return this;
    }

    public void setPublicAccessToken2(String publicAccessToken2) {
        this.publicAccessToken2 = publicAccessToken2;
    }

    public String getPublicAccessToken3() {
        return publicAccessToken3;
    }

    public SocialConnection publicAccessToken3(String publicAccessToken3) {
        this.publicAccessToken3 = publicAccessToken3;
        return this;
    }

    public void setPublicAccessToken3(String publicAccessToken3) {
        this.publicAccessToken3 = publicAccessToken3;
    }

    public String getPublicAccessToken4() {
        return publicAccessToken4;
    }

    public SocialConnection publicAccessToken4(String publicAccessToken4) {
        this.publicAccessToken4 = publicAccessToken4;
        return this;
    }

    public void setPublicAccessToken4(String publicAccessToken4) {
        this.publicAccessToken4 = publicAccessToken4;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialConnection)) {
            return false;
        }
        return id != null && id.equals(((SocialConnection) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialConnection{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", proverId='" + getProverId() + "'" +
            ", accessToken='" + getAccessToken() + "'" +
            ", restRateLimit=" + getRestRateLimit() +
            ", graphqlRateLimit=" + getGraphqlRateLimit() +
            ", accessToken1='" + getAccessToken1() + "'" +
            ", accessToken2='" + getAccessToken2() + "'" +
            ", accessToken3='" + getAccessToken3() + "'" +
            ", accessToken4='" + getAccessToken4() + "'" +
            ", publicAccessToken='" + getPublicAccessToken() + "'" +
            ", publicAccessToken1='" + getPublicAccessToken1() + "'" +
            ", publicAccessToken2='" + getPublicAccessToken2() + "'" +
            ", publicAccessToken3='" + getPublicAccessToken3() + "'" +
            ", publicAccessToken4='" + getPublicAccessToken4() + "'" +
            "}";
    }
}
