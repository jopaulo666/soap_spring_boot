package com.joaopaulo.soap.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joaopaulo.soap.webservice.bean.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
