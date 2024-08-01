package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.service.security.AuthTokenFilter;
import org.transferservice.service.security.TokenBlacklist;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomer {

    private final CustomerRepository customerRepository;
    private final TokenBlacklist tokenBlacklist;
    private final AuthTokenFilter authTokenFilter;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Customer updateCustomer(Long id, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {

        Customer customer = this.getCustomerById(id);

//        customer.setUsername(updateCustomerDTO.getFirstName());
//        customer.setEmail(updateCustomerDTO.getEmail());
//        customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
//        customer.setAddress(updateCustomerDTO.getAddress());
//        customer.setNationality(updateCustomerDTO.getNationality());
//        customer.setNationalIdNumber(updateCustomerDTO.getNationalIdNumber());
//        customer.setDateOfBirth(updateCustomerDTO.getDateOfBirth());

        return this.customerRepository.save(customer);

    }

    @Override
    public Customer getCustomerById(Long id) throws CustomerNotFoundException {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));
    }

    @Override
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = authTokenFilter.parseJwt(request);
        if(token!=null)
            tokenBlacklist.addToBlacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }

}
