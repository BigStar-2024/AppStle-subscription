package com.et.web.rest.vm.payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentSettings {
    private List<DigitalWallet> supportedDigitalWallets = new ArrayList<>();

    public List<DigitalWallet> getSupportedDigitalWallets() {
        return supportedDigitalWallets;
    }

    public void setSupportedDigitalWallets(List<DigitalWallet> supportedDigitalWallets) {
        this.supportedDigitalWallets = supportedDigitalWallets;
    }
}
