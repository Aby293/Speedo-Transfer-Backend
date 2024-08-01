package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;


public interface ICustomer {

    /**
     * Update customer details
     *
     * @param id                customer id
     * @param updateCustomerDTO customer details
     * @return updated customer @{@link Customer}
     * @throws CustomerNotFoundException if customer not found
     */
    Customer updateCustomer(Long id, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException;

    Customer getCustomerById(Long id) throws CustomerNotFoundException;

    ResponseEntity<String> logout(HttpServletRequest request);

}
