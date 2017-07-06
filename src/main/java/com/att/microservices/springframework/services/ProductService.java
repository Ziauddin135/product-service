package com.att.microservices.springframework.services;

import java.util.List;

import com.att.microservices.springframework.commands.ProductForm;
import com.att.microservices.springframework.domain.LineItem;
import com.att.microservices.springframework.domain.Order;
import com.att.microservices.springframework.domain.Product;

public interface ProductService {

	List<Product> listAll();

	Product getById(String id);

	Product saveOrUpdate(Product product);

	void delete(String id);

	Product saveOrUpdateProductForm(ProductForm productForm);

	public List<LineItem> getItemLists();

	public String addToCart(String id, int price);

	public List<Order> saveOrder(String custId);

	public List<Order> getOrdersByCustId(String custId);

	public void deleteItem(String id);
}
