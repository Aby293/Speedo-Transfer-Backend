package org.transferservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;
import org.transferservice.exception.custom.InvalidAccountCurrencyException;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.service.security.AuthenticatorService;
import org.transferservice.service.security.JwtUtils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticatorServiceTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuthenticatorService authenticatorService;


    @Test
    void whenRegisterNewCustomerIsSuccessful() throws CustomerAlreadyExistException, InvalidAccountCurrencyException {

        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        Account account = Account.builder()
                .accountNumber(String.valueOf(new SecureRandom().nextInt(1000000000)))
                .active(true)
                .currency(AccountCurrency.EGP)
                .balance(0.0)
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        List<Account> accounts = new ArrayList<>();
        accounts.add(this.accountRepository.save(account));

        Customer savedCustomer = Customer
                .builder()
                .email("test@example.com")
                .username("User A")
                .phoneNumber("123456789")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.now())
                .country(Country.EGYPT)
                .password(this.encoder.encode("password"))
                .accounts(accounts)
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CreateCustomerDTO userSignUpRequest = new CreateCustomerDTO(1L,"User A","test@example.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        CustomerDTO response = authenticatorService.register(userSignUpRequest);

        verify(customerRepository, times(1)).save(any());

        Assertions.assertEquals("test@example.com", response.getEmail());
    }

    @Test
    public void whenUserAlreadyExistingByEmailUserThrows() throws CustomerAlreadyExistException {
        when(customerRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(customerRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        CreateCustomerDTO userSignUpRequest = new CreateCustomerDTO(1L,"User A","test@example.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        Assertions.assertThrows(CustomerAlreadyExistException.class, () -> authenticatorService.register(userSignUpRequest));
        verify(customerRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    public void whenUserAlreadyExistingByPhoneUserThrows() throws CustomerAlreadyExistException {
        when(customerRepository.existsByPhoneNumber("123456789")).thenReturn(true);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);

        CreateCustomerDTO userSignUpRequest = new CreateCustomerDTO(1L,"User A","test@email.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        Assertions.assertThrows(CustomerAlreadyExistException.class, () -> authenticatorService.register(userSignUpRequest));
        verify(customerRepository, times(1)).existsByPhoneNumber("123456789");
    }
}
