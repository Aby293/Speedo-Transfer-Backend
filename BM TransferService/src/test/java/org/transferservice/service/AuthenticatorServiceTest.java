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
import org.transferservice.dto.CreateAccountDTO;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.enums.CardCurrency;
import org.transferservice.dto.enums.Country;
import org.transferservice.dto.enums.Gender;
import org.transferservice.exception.custom.AccountAlreadyExistException;
import org.transferservice.model.Card;
import org.transferservice.model.Account;
import org.transferservice.repository.CardRepository;
import org.transferservice.repository.AccountRepository;
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
    private AccountRepository accountRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticatorService authenticatorService;


    @Test
    void whenRegisterNewCustomerIsSuccessful() throws AccountAlreadyExistException {

        when(accountRepository.existsByEmail(anyString())).thenReturn(false);
        when(accountRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        Card card = Card.builder()
                .cardNumber(String.valueOf(new SecureRandom().nextInt(1000000000)))
                .active(true)
                .currency(CardCurrency.EGP)
                .balance(0.0)
                .build();

        when(cardRepository.save(any(Card.class))).thenReturn(card);

        List<Card> cards = new ArrayList<>();
        cards.add(this.cardRepository.save(card));

        Account savedAccount = Account
                .builder()
                .email("test@example.com")
                .username("User A")
                .phoneNumber("123456789")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.now())
                .country(Country.EGYPT)
                .password(this.encoder.encode("password"))
                .cards(cards)
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        CreateAccountDTO userSignUpRequest = new CreateAccountDTO(1L,"User A","test@example.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        AccountDTO response = authenticatorService.register(userSignUpRequest);

        verify(accountRepository, times(1)).save(any());

        Assertions.assertEquals("test@example.com", response.getEmail());
    }

    @Test
    public void whenUserAlreadyExistingByEmailUserThrows() throws AccountAlreadyExistException {
        when(accountRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(accountRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        CreateAccountDTO userSignUpRequest = new CreateAccountDTO(1L,"User A","test@example.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        Assertions.assertThrows(AccountAlreadyExistException.class, () -> authenticatorService.register(userSignUpRequest));
        verify(accountRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    public void whenUserAlreadyExistingByPhoneUserThrows() throws AccountAlreadyExistException {
        when(accountRepository.existsByPhoneNumber("123456789")).thenReturn(true);
        when(accountRepository.existsByEmail(anyString())).thenReturn(false);

        CreateAccountDTO userSignUpRequest = new CreateAccountDTO(1L,"User A","test@email.com","123456789",
                Country.EGYPT,Gender.MALE,LocalDate.now(),this.encoder.encode("password"));

        Assertions.assertThrows(AccountAlreadyExistException.class, () -> authenticatorService.register(userSignUpRequest));
        verify(accountRepository, times(1)).existsByPhoneNumber("123456789");
    }
}
