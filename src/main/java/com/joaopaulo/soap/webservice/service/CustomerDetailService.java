package com.joaopaulo.soap.webservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joaopaulo.soap.webservice.bean.Customer;
import com.joaopaulo.soap.webservice.helper.Status;
import com.joaopaulo.soap.webservice.repository.CustomerRepository;

@Component
public class CustomerDetailService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	//retorna o cliente pelo ID
	public Customer findById(Integer id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			return customerOptional.get();
		}
		return null;
	}
	
	public List<Customer> findAll(){
		return customerRepository.findAll();
	}
	
	public Status deleteById(Integer id) {
		try {
			customerRepository.deleteById(id);
			return Status.SUCESSO;
		} catch (Exception e) {
			return Status.ERRO;
		}
	}
	
	public Status insert(Customer customer) {
		try {
			customerRepository.save(customer);
			return Status.SUCESSO;
		} catch (Exception e) {
			return Status.ERRO;
		}
	}

}
