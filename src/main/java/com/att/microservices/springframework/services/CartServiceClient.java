package com.att.microservices.springframework.services;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.att.microservices.springframework.domain.LineItem;
import com.att.microservices.springframework.domain.Order;

@FeignClient("purchase-service")
public interface CartServiceClient {

	@RequestMapping(method = RequestMethod.GET, value = "/purchase-service/v1/cart/addToCart/{id}/{custId}/{price}")
	public void addToCart(@PathVariable("id") String id, @PathVariable("custId") String custId, @PathVariable("price") int price);

	@RequestMapping(method = RequestMethod.GET, value = "/purchase-service/v1/cart/getCartItems/{custId}")
	public @ResponseBody List<LineItem> getCartItems(@PathVariable("custId") String custId);

	@RequestMapping(method = RequestMethod.GET, value = "/purchase-service/v1/cart/deleteItem/{id}/{custId}")
	public void deleteLineIem(@PathVariable("id") String id, @PathVariable("custId") String custId);
	
	@RequestMapping(method = RequestMethod.GET , value = "/purchase-service/v1/cart/saveOrder/{custId}")
	public @ResponseBody List<Order> saveOrder(@PathVariable("custId") String custId);
				
	@RequestMapping(method = RequestMethod.GET, value = "/purchase-service/v1/cart/getOrder/{custId}")
	public @ResponseBody List<Order> getOrdersByCustId(@PathVariable("custId") String custId);
}