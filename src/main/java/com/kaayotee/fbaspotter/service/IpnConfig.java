package com.kaayotee.fbaspotter.service;

public class IpnConfig {

    private String ipnUrl;
    private String receiverEmail;
    private String paymentAmount;
    private String paymentCurrency;

    public IpnConfig (String ipnUrl, String receiverEmail, String paymentAmount, String paymentCurrency) {
        this.ipnUrl = ipnUrl;
        this.receiverEmail = receiverEmail;
        this.paymentAmount = paymentAmount;
        this.paymentCurrency = paymentCurrency;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

}
