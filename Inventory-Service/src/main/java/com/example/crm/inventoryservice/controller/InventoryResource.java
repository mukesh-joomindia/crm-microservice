package com.example.crm.inventoryservice.controller;

import com.example.crm.inventoryservice.dto.InventoryResponse;
import com.example.crm.inventoryservice.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequiredArgsConstructor
@Slf4j
public class InventoryResource {

    private  final InventoryService inventoryService;
    //@RequestMapping("api/inventory")
    public Boolean getAllItems(){
        return true;
        //return InventoryResponse.builder().skuCode("001").isInStock(true).build();
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("api/inventory")
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodes){
        log.info("received request for ");
        return  inventoryService.isInStock(skuCodes);
    }


}
