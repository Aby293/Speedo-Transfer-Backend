package org.transferservice.service.security;

import org.transferservice.dto.CreateAccountDTO;
import org.transferservice.dto.*;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.exception.custom.AccountAlreadyExistException;

public interface IAuthenticator {

    /**
     * Register a new customer
     *
     * @param createAccountDTO customer details
     * @return registered customer @{@link AccountDTO}
     * @throws AccountAlreadyExistException if customer already exist
     */
    AccountDTO register(CreateAccountDTO createAccountDTO) throws AccountAlreadyExistException;

    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
