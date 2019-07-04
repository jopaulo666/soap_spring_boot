package com.joaopaulo.soap.webservice.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.joaopaulo.soap.webservice.bean.Customer;
import com.joaopaulo.soap.webservice.service.CustomerDetailService;
import com.joaopaulo.soap.webservice.soap.exception.CustomerNotFoundException;

import br.com.jopaulo.CustomerDetail;
import br.com.jopaulo.DeleteCustomerRequest;
import br.com.jopaulo.DeleteCustomerResponse;
import br.com.jopaulo.GetAllCustomerDetailRequest;
import br.com.jopaulo.GetAllCustomerDetailResponse;
import br.com.jopaulo.GetCustomerDetailRequest;
import br.com.jopaulo.GetCustomerDetailResponse;
import br.com.jopaulo.InsertCustomerRequest;
import br.com.jopaulo.InsertCustomerResponse;

@Endpoint
public class CustomerDetailEndPoint {
	
	@Autowired
	CustomerDetailService service;
	
	@PayloadRoot(namespace="http://jopaulo.com.br", localPart="GetCustomerDetailRequest")
	@ResponsePayload
	public GetCustomerDetailResponse processCustomerDetailRequest(@RequestPayload GetCustomerDetailRequest req) throws Exception {
		Customer customer = service.findById(req.getId());
		if (customer == null) {
			throw new CustomerNotFoundException("Código '" + req.getId() + "' do cliente não encontrado ");
		}
		return convertToGetCustomerDetailResponse(customer);
	}
	
	private GetCustomerDetailResponse convertToGetCustomerDetailResponse(Customer customer) {
		GetCustomerDetailResponse response = new GetCustomerDetailResponse();
		response.setCustomerDetail(convertToCustomerDetail(customer));
		return response;
	}
	
	private CustomerDetail convertToCustomerDetail(Customer customer) {
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setId(customer.getId());
		customerDetail.setName(customer.getName());
		customerDetail.setPhone(customer.getPhone());
		customerDetail.setEmail(customer.getEmail());
		return customerDetail;
	}
	
	@PayloadRoot(namespace="http://jopaulo.com.br", localPart="GetAllCustomerDetailRequest")
	@ResponsePayload
	public GetAllCustomerDetailResponse processGetAllCustomerDetailRequest(@RequestPayload GetAllCustomerDetailRequest request) {
		List<Customer> customers = service.findAll();
		return convertToGetAllCustomerDetailResponse(customers);
	}
	
	private GetAllCustomerDetailResponse convertToGetAllCustomerDetailResponse(List<Customer> customers) {
		GetAllCustomerDetailResponse response = new GetAllCustomerDetailResponse();
		customers.stream().forEach(c -> response.getCustomerDetail().add(convertToCustomerDetail(c)));
		return response;
	}
	
	@PayloadRoot(namespace="http://jopaulo.com.br", localPart="DeleteCustomerRequest")
	@ResponsePayload
	public DeleteCustomerResponse deleteCustomerRequest(@RequestPayload DeleteCustomerRequest request) {
		DeleteCustomerResponse response = new DeleteCustomerResponse();
		response.setStatus(convertStatusSoap(service.deleteById(request.getId())));
		return response;
	}
	
	private br.com.jopaulo.Status convertStatusSoap(com.joaopaulo.soap.webservice.helper.Status statusService){
		if (statusService == com.joaopaulo.soap.webservice.helper.Status.ERRO) {
			return br.com.jopaulo.Status.ERRO;
		}
		return br.com.jopaulo.Status.SUCESSO;
	}
	
	@PayloadRoot(namespace="http://jopaulo.com.br", localPart="InsertCustomerRequest")
	@ResponsePayload
	public InsertCustomerResponse insertCustomerRequest(@RequestPayload InsertCustomerRequest request) {
		InsertCustomerResponse response = new InsertCustomerResponse();
		response.setStatus(convertStatusSoap(service.insert(convertCustomer(request.getCustomerDetail()))));
		return response;
	}
	
	private Customer convertCustomer(CustomerDetail customerDetail) {
		return new Customer
				(customerDetail.getId(), customerDetail.getName(), customerDetail.getPhone(), customerDetail.getEmail());
	}
}
