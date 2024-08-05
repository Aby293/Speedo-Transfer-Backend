package org.transferservice.service.security;

import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.*;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.exception.custom.InvalidAccountCurrencyException;
import org.transferservice.exception.custom.CustomerAlreadyExistException;

public interface IAuthenticator {

    /**
     * Register a new customer
     *
     * @param createCustomerDTO customer details
     * @return registered customer @{@link CustomerDTO}
     * @throws CustomerAlreadyExistException if customer already exist
     */
    CustomerDTO register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException, InvalidAccountCurrencyException;

    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
