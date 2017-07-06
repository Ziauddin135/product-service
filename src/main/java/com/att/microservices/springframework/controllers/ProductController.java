package com.att.microservices.springframework.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.att.microservices.springframework.commands.ProductForm;
import com.att.microservices.springframework.converters.ProductToProductForm;
import com.att.microservices.springframework.domain.Order;
import com.att.microservices.springframework.domain.Product;
import com.att.microservices.springframework.services.ProductService;


@Controller
public class ProductController {
    private ProductService productService;

    private ProductToProductForm productToProductForm;

    @Autowired
    public void setProductToProductForm(ProductToProductForm productToProductForm) {
        this.productToProductForm = productToProductForm;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("/")
    public String redirToList(){
        return "redirect:/product/list?";
    }

    @RequestMapping({"/product/list", "/product"})
    public String listProducts(Model model){
        model.addAttribute("products", productService.listAll());
        //model.addAttribute("items", items+1);
        return "product/list";
    }

    @RequestMapping("/product/show/{id}")
    public String getProduct(@PathVariable String id, Model model){
        model.addAttribute("product", productService.getById(id));
        return "product/show";
    }

    @RequestMapping("product/edit/{id}")
    public String edit(@PathVariable String id, Model model){
        Product product = productService.getById(id);
        ProductForm productForm = productToProductForm.convert(product);

        model.addAttribute("productForm", productForm);
        return "product/productform";
    }

    @RequestMapping("/product/new")
    public String newProduct(Model model){
        model.addAttribute("productForm", new ProductForm());
        return "product/productform";
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public String saveOrUpdateProduct(@Valid ProductForm productForm, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "product/productform";
        }

        Product savedProduct = productService.saveOrUpdateProductForm(productForm);

        return "redirect:/product/show/" + savedProduct.getId();
    }

    @RequestMapping("/product/delete/{id}")
    public String delete(@PathVariable String id){
        productService.delete(id);
        return "redirect:/product/list";
    }
    
    @RequestMapping("/product/deleteItem/{id}")
    public String deleteItem(@PathVariable String id){
    	productService.deleteItem(id);
        return "redirect:/cart/itemList";
    }
    
    @RequestMapping({"/cart/itemList", "/itemList"})
    public String listItems(Model model){
        model.addAttribute("items", productService.getItemLists());
        //model.addAttribute("items", items+1);
        return "product/itemList";
    }
    
    @RequestMapping("/addToCart")
    public String addToCart(@RequestParam("id")  String id,@RequestParam("custId")  String custId, @RequestParam("price") int price){
       
        System.out.println("reaching...............custId >"+custId);
        System.out.println("reaching...............prodId >"+id);
        productService.addToCart(id, price);
        return "redirect:/product/list";
    }
    
    @RequestMapping(value = "/saveOrder/{custId}", method = RequestMethod.GET)
	public String saveOrder(@PathVariable("custId") String custId,HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("Save Order...............custId >"+custId);
    	productService.saveOrder(custId);
    	return "redirect:/getOrder/1";

	}

	@RequestMapping(value = "/getOrder/{custId}", method = RequestMethod.GET)
	public String getOrdersByCustId(Model model, @PathVariable("custId") String custId) {
		System.out.println("get Order...............custId >"+custId);
		model.addAttribute("orders", productService.getOrdersByCustId(custId));
		return "product/orders";
	}
    
}
