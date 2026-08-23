package com.api.dto.request;

import java.util.List;

public class Cart {
	
	
	private String customerId;
    private List<CartItem> items;
    private Double totalAmount;    // computed as items are added/removed
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public List<CartItem> getItems() {
		return items;
	}
	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
    
    
    

}
