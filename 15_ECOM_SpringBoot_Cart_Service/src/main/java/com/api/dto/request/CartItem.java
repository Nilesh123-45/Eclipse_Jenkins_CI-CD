package com.api.dto.request;

public class CartItem {
	
	
    private String productName;
    private int quantity;
    private Double pricePerUnit;   // fetched from Product Service via Feign, not client-supplied
    
    
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
    
    
    

}
