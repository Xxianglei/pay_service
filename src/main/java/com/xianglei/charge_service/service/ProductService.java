package com.xianglei.charge_service.service;

import com.xianglei.charge_service.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> listProduct();

    Product findById(int id);


}
