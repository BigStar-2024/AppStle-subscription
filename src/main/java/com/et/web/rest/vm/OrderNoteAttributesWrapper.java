package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class OrderNoteAttributesWrapper {

    private List<OrderNoteAttribute> orderNoteAttributesList = new ArrayList<>();

    public OrderNoteAttributesWrapper() {
    }

    public OrderNoteAttributesWrapper(List<OrderNoteAttribute> orderNoteAttributes) {
        this.orderNoteAttributesList = orderNoteAttributes;
    }

    public List<OrderNoteAttribute> getOrderNoteAttributesList() {
        return orderNoteAttributesList;
    }

    public void setOrderNoteAttributesList(List<OrderNoteAttribute> orderNoteAttributesList) {
        this.orderNoteAttributesList = orderNoteAttributesList;
    }
}
