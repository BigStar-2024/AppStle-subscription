package com.et.web.rest.vm;

import com.et.domain.MembershipDiscount;
import com.et.domain.MembershipDiscountProducts;

import java.util.List;

public class MembershipDiscountRequest {
    private MembershipDiscount membershipDiscount;
    List<MembershipDiscountProducts> membershipDiscountProducts;

    public MembershipDiscount getMembershipDiscount() {
        return membershipDiscount;
    }

    public void setMembershipDiscount(MembershipDiscount membershipDiscount) {
        this.membershipDiscount = membershipDiscount;
    }

    public List<MembershipDiscountProducts> getMembershipDiscountProducts() {
        return membershipDiscountProducts;
    }

    public void setMembershipDiscountProducts(List<MembershipDiscountProducts> membershipDiscountProducts) {
        this.membershipDiscountProducts = membershipDiscountProducts;
    }
}
