package com.yongdd.oder_re;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
   String userId;
   ArrayList<Payment> orderMenus;
   int totalPrice;
   String orderDate;
   String orderReceivedTime;
   String orderAcceptedTime;
   String orderCompletedTime;

   public Order(){}

    public Order(String userId, ArrayList<Payment> orderMenus, int totalPrice, String orderDate, String orderReceivedTime, String orderAcceptedTime, String orderCompletedTime) {
        this.userId = userId;
        this.orderMenus = orderMenus;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderReceivedTime = orderReceivedTime;
        this.orderAcceptedTime = orderAcceptedTime;
        this.orderCompletedTime = orderCompletedTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Payment> getOrderMenus() {
        return orderMenus;
    }

    public void setOrderMenus(ArrayList<Payment> orderMenus) {
        this.orderMenus = orderMenus;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderReceivedTime() {
        return orderReceivedTime;
    }

    public void setOrderReceivedTime(String orderReceivedTime) {
        this.orderReceivedTime = orderReceivedTime;
    }

    public String getOrderAcceptedTime() {
        return orderAcceptedTime;
    }

    public void setOrderAcceptedTime(String orderAcceptedTime) {
        this.orderAcceptedTime = orderAcceptedTime;
    }

    public String getOrderCompletedTime() {
        return orderCompletedTime;
    }

    public void setOrderCompletedTime(String orderCompletedTime) {
        this.orderCompletedTime = orderCompletedTime;
    }
}
