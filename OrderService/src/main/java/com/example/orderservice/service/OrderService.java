package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDTO;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.modal.Order;
import com.example.orderservice.modal.OrderLineItem;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements  OrderIntf{

    private final WebClient.Builder webClientBuilder;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemsDtoList().stream().map(this::mapToDto).toList();
        order.setOrderLineItems(orderLineItemList);

        //Get all sku code from the order

        List<String> skuCodes = orderRequest.getOrderLineItemsDtoList().stream()
                .map(OrderLineItemsDTO::getSkuCode).toList();

        //convert to inventory DTO
        //Need to check uri builder
        log.info("Calling inventory service");
        InventoryResponse[] inventoryResponsesList = webClientBuilder.build().get()
                .uri("http://INVENTORY-SERVICE/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCodes",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean isExisting = Arrays.stream(inventoryResponsesList).allMatch(InventoryResponse::isInStock);
        log.debug("Is product in Inventory ",isExisting);
        if(isExisting){
            log.info("Saving Order",order);
            orderRepository.save(order);
        }
        else{
            throw  new RuntimeException("There are some issue with order");
        }
        return "Order is successfull";
    }

    private OrderLineItem mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItem.setPrice(orderLineItemsDTO.getPrice());
        orderLineItem.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItem;
    }


}
