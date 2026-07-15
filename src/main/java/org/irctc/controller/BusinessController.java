package org.irctc.controller;

import java.util.List;

import org.irctc.request.BusinessRequest;
import org.irctc.response.BusinessResponse;
import org.irctc.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business_v1.0/")
public class BusinessController {

	@Autowired
	BusinessService service;
	
	@PostMapping("addBusiness")
	public BusinessResponse doBusiness(@RequestBody BusinessRequest request) {
		System.out.println("doBusiness() started from controller......");
		BusinessResponse response=service.addBusiness(request);
		System.out.println();
		System.out.println("doBusiness() ended......");
		return response;
	}
	
	
	@GetMapping("getBusiness")
	public List<BusinessResponse> getAllBusiness(@RequestParam String categoryName,
			@RequestParam int pageNumber, @RequestParam int pageSize){
		System.out.println();
		System.out.println("from controller getAllBusiness() started.......");
		List<BusinessResponse> result=service.getResponse(categoryName, pageNumber, pageSize);
		System.out.println("from controller getAllBusiness() ended.......");
		return result;
	}
}
