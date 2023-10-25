package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;

public interface OrderIntf {

    String placeOrder(OrderRequest orderRequest);
}
