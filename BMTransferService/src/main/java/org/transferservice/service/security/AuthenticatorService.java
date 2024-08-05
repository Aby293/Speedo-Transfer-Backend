package org.transferservice.service.security;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.exception.custom.InvalidAccountCurrencyException;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CountryCurrenciesRepository;
import org.transferservice.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticatorService implements IAuthenticator {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CountryCurrenciesRepository currenciesRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public CustomerDTO register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException, InvalidAccountCurrencyException {

        if (this.customerRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new CustomerAlreadyExistException(String.format("Customer with email %s already exists", createCustomerDTO.getEmail()));
        }

        if (this.customerRepository.existsByPhoneNumber(createCustomerDTO.getPhoneNumber())) {
            throw new CustomerAlreadyExistException(String.format("Customer with phone number %s already exists", createCustomerDTO.getPhoneNumber()));
        }

        String accountNum = RandomStringUtils.randomNumeric(16);
        while (this.accountRepository.findByAccountNumber(accountNum).isPresent()) {
            accountNum = RandomStringUtils.randomNumeric(16);
        }

        AccountCurrency currency = currenciesRepository.findByCountry(createCustomerDTO.getCountry())
                .orElseThrow(()->new InvalidAccountCurrencyException("Country not found in database"))
                .getCurrency();

        List<Account> accounts = new ArrayList<>();
        Account account = Account.builder()
                    .accountNumber(accountNum)
                    .active(true)
                    .currency(currency)
                    .accountHolderName(createCustomerDTO.getUsername())
                    .balance(10000.0)
                    .cvv(RandomStringUtils.randomNumeric(3))
                    .isDefault(true)
                    .expirationDate("27/8")
                    .build();

        accounts.add(this.accountRepository.save(account));


        Customer user = Customer
                .builder()
                .email(createCustomerDTO.getEmail())
                .username(createCustomerDTO.getUsername())
                .phoneNumber(createCustomerDTO.getPhoneNumber())
                .gender(createCustomerDTO.getGender())
                .dateOfBirth(createCustomerDTO.getDateOfBirth())
                .country(createCustomerDTO.getCountry())
                .password(this.encoder.encode(createCustomerDTO.getPassword()))
                .accounts(accounts)
                .build();


        account.setCustomer(user);
        accountRepository.save(account);

        return this.customerRepository.save(user).toDTO();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();
    }

}