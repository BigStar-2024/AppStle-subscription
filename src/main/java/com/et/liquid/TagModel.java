package com.et.liquid;

public class TagModel {

    private Customer customer; //{{customer.id}}
    private SubscriptionContract subscriptionContract; // {{subscriptionContract.id}};
    private Order firstOrder;// {{firstOrder.id}};

    public TagModel() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SubscriptionContract getSubscriptionContract() {
        return subscriptionContract;
    }

    public void setSubscriptionContract(SubscriptionContract subscriptionContract) {
        this.subscriptionContract = subscriptionContract;
    }

    public Order getFirstOrder() {
        return firstOrder;
    }

    public void setFirstOrder(Order firstOrder) {
        this.firstOrder = firstOrder;
    }

    @Override
    public String toString() {
        return "TagModel{" +
                "customer=" + customer +
                ", subscriptionContract=" + subscriptionContract +
                ", firstOrder=" + firstOrder +
                '}';
    }
}
