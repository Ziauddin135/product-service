package com.att.microservices.springframework.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.microservices.springframework.commands.ProductForm;
import com.att.microservices.springframework.converters.ProductFormToProduct;
import com.att.microservices.springframework.domain.LineItem;
import com.att.microservices.springframework.domain.Order;
import com.att.microservices.springframework.domain.Product;
import com.att.microservices.springframework.repositories.ProductRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	private ProductRepository productRepository;
	private ProductFormToProduct productFormToProduct;

	@Autowired
	CartServiceClient cartServiceClient;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, ProductFormToProduct productFormToProduct) {
		this.productRepository = productRepository;
		this.productFormToProduct = productFormToProduct;
	}

	@Override
	public List<Product> listAll() {
		List<Product> products = new ArrayList<>();
		productRepository.findAll().forEach(products::add); // fun with Java 8
		System.out.println("listAll>>>>>" + products);
		return products;
	}

	@Override
	public Product getById(String id) {
		return productRepository.findOne(id);
	}

	@Override
	public Product saveOrUpdate(Product product) {
		Map<String, String> map = new HashMap();

		product = productRepository.save(product);

		if (product.getId() != null)

			if (cartServiceClient == null) {
				System.out.println("In cartServiceClient null got cart");
			} else {
				System.out.println("In cartServiceClient not null got cart");
			}
		// cartServiceClient.addToCart(product.getId().toHexString(), "1");
		// List<LineItem> lineItems = cartServiceClient.getCartItems("1");
		return product;
	}

	@Override
	@HystrixCommand(fallbackMethod = "CartNotFound")
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public String addToCart(String id, int price) {

		if (id != null)
			if (cartServiceClient == null) {
				System.out.println("In cartServiceClient null got cart");
			} else {
				System.out.println("In cartServiceClient not null got cart");
			}
		cartServiceClient.addToCart(id, "1", price);
		return id;
	}

	@Override
	@HystrixCommand(fallbackMethod = "itemListNotFound")
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public List<LineItem> getItemLists() {
		if (cartServiceClient == null) {
			System.out.println("In getItemLists null got cart");
		} else {
			System.out.println("In getItemLists not null got cart");
		}
		List<LineItem> items = cartServiceClient.getCartItems("1");
		System.out.println("ProductServiceImpl.getItemLists items>>>>" + items.toString());
		return items;
	}

	@Override
	@HystrixCommand(fallbackMethod = "itemNotFound")
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public void deleteItem(String id) {
		if (cartServiceClient == null) {
			System.out.println("In deleteItem null got cart");
		} else {
			System.out.println("In deleteItem not null got cart");
		}
		cartServiceClient.deleteLineIem(id, "1");
	}

	public String CartNotFound(String id, int price) {

		System.out.println("not founddddddddddddddd");
		return "Not found";
	}

	public List<LineItem> itemListNotFound() {

		System.out.println("itemList not found");
		return new ArrayList<LineItem>();
	}

	public void itemNotFound(String id) {

		System.out.println("Item not founddddddddddddddd");
	}

	@Override
	public void delete(String id) {
		productRepository.delete(id);

	}

	@Override
	public Product saveOrUpdateProductForm(ProductForm productForm) {
		Product savedProduct = saveOrUpdate(productFormToProduct.convert(productForm));

		System.out.println("Saved Product Id: " + savedProduct.getId());
		return savedProduct;
	}

	@Override
	@HystrixCommand(fallbackMethod = "orderCouldNotSave")
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public List<Order> saveOrder(String custId) {
		if (cartServiceClient == null) {
			System.out.println("In deleteItem null got cart");
		} else {
			System.out.println("In deleteItem not null got cart");
		}
		return cartServiceClient.saveOrder(custId);
	}

	@Override
	@HystrixCommand(fallbackMethod = "ordersCouldNotFound")
	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "false")
	public List<Order> getOrdersByCustId(String custId) {
		if (cartServiceClient == null) {
			System.out.println("In getOrdersByCustId null got cart");
		} else {
			System.out.println("In getOrdersByCustId not null got cart");
		}
		List<Order> orders = cartServiceClient.getOrdersByCustId(custId);
		System.out.println("Got orders>>>>>>"+orders);
		return orders;

	}

	public List<Order> orderCouldNotSave(String custId) {
		return new ArrayList<>();
	}

	public List<Order> ordersCouldNotFound(String custId) {
		return new ArrayList<>();
	}
}
