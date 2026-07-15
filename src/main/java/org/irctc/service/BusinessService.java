package org.irctc.service;

import java.util.ArrayList;
import java.util.List;

import org.irctc.entity.BusinessEntity;
import org.irctc.entity.PaymentEntity;
import org.irctc.exception.InvalidException;
import org.irctc.repository.BusinessRepos;
import org.irctc.repository.PaymentRepos;
import org.irctc.request.BusinessRequest;
import org.irctc.response.BusinessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class BusinessService {
	
	@Autowired
	BusinessRepos business;
	
	@Autowired
	PaymentRepos paymentRepos;
	
	@Transactional
	public BusinessResponse addBusiness(BusinessRequest request) {
		System.out.println();
		System.out.println("addBusiness() from service started........");
		BusinessEntity entity=new BusinessEntity();
		
		entity.setName(request.getName());
		entity.setEmail(request.getEmail());
		entity.setMobile(request.getMobile());
		entity.setAddress(request.getAddress());
	
		entity.setBalance(request.getBalance());
		entity.setCategory(request.getCategory());
		entity.setBusinessStatus("BUSINESS-INIT");
		
		PaymentEntity payment=new PaymentEntity();
		payment.setAmount(4400);
		payment.setBusinessId(entity.getId());
		
		
		try {
			
			String paymentStatus=null;
			payment.setStatus(paymentStatus.concat("TXN"));
			
		}catch(Exception e) {
			throw new InvalidException("Enter valid payment status..... from addBusiness() ");
		}
		
		PaymentEntity paymentResponse=paymentRepos.save(payment);
		BusinessResponse response=null;
		if(paymentResponse.getPayId()>0) {
			entity.setPayStatus("SUCCESS");
			
			BusinessEntity updated=business.save(entity);
			response.setName(updated.getName());
			response.setCategory(updated.getCategory());
			response.setStatus("DONE");
		}
		
		System.out.println("addBusiness() from service ended.....");
		
		
		return response;
	}
	
//	public List<BusinessResponse> getResponse() {
//		BusinessResponse  br=new BusinessResponse();
//		
//		List<BusinessEntity> details=business.findAll();
//		List<BusinessResponse> response=new ArrayList<>();
//		
//		for(BusinessEntity entity:details) {
//				BusinessResponse res=new BusinessResponse();
//				res.setName(entity.getName());
//				res.setCategory(entity.getCategory());
//				res.setStatus("DONE");
//		}
//		 
//		
////		int res=10;
////		System.out.println("Value is "+res);
////		
////		try {
////		int result=10/0;
////		}catch (Exception e) {
////			// TODO: handle exception
////			throw new InvalidException("User provide unnesaccary math operation");
////		}
////		
////		br.setName("Gaming");
////		br.setCategory("Epic/Mytho");
//		
//		return response;
//	}
	
	
	@Transactional
	public List<BusinessResponse> getResponse(String category, int pageNumber, int pageSize) {
		System.out.println();
		System.out.println("getResponse() from service started.................");
		BusinessResponse  br=new BusinessResponse();
		
		Pageable pageRequest=PageRequest.of(pageNumber, pageSize);
		
		
		//List<BusinessEntity> details=business.findAll();
		Page<BusinessEntity>  page=business.findAll(pageRequest);
		List<BusinessResponse> response=new ArrayList<>();
		
		for(BusinessEntity entity:page) {
				BusinessResponse res=new BusinessResponse();
				res.setName(entity.getName());
				res.setCategory(entity.getCategory());
				res.setStatus("DONE");
		}
		 
		int res=10;
		System.out.println("Value is "+res);
		
		try {
		int result=10/0;
		}catch (Exception e) {
			// TODO: handle exception
			throw new InvalidException("User provide unnesaccary math operation");
		}
//		int res=10;
//		System.out.println("Value is "+res);
//		
//		try {
//		int result=10/0;
//		}catch (Exception e) {
//			// TODO: handle exceptionu
//			throw new InvalidException("User provide unnesaccary math operation");
//		}
//		
//		br.setName("Gaming");
//		br.setCategory("Epic/Mytho");
		System.out.println("getResponse() from service ended.......");
		System.out.println();
		
		return response;
	}


}
