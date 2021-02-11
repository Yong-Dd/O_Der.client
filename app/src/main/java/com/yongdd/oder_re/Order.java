package com.yongdd.oder_re;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    LocalDateTime orderReceivedDate;
    LocalDateTime orderAcceptedDate;
    LocalDateTime orderCompletedDate;
    int totalPrice;

    public Order(LocalDateTime orderReceivedDate, LocalDateTime orderAcceptedDate, LocalDateTime orderCompletedDate, int totalPrice) {
        this.orderReceivedDate = orderReceivedDate;
        this.orderAcceptedDate = orderAcceptedDate;
        this.orderCompletedDate = orderCompletedDate;
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderReceivedDate() {
        return orderReceivedDate;
    }

    public void setOrderReceivedDate(LocalDateTime orderReceivedDate) {
        this.orderReceivedDate = orderReceivedDate;
    }

    public LocalDateTime getOrderAcceptedDate() {
        return orderAcceptedDate;
    }

    public void setOrderAcceptedDate(LocalDateTime orderAcceptedDate) {
        this.orderAcceptedDate = orderAcceptedDate;
    }

    public LocalDateTime getOrderCompletedDate() {
        return orderCompletedDate;
    }

    public void setOrderCompletedDate(LocalDateTime orderCompletedDate) {
        this.orderCompletedDate = orderCompletedDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
