package org.transferservice.service.security;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CreateAccountDTO;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.enums.CardCurrency;
import org.transferservice.exception.custom.AccountAlreadyExistException;
import org.transferservice.model.Card;
import org.transferservice.model.Account;
import org.transferservice.repository.CardRepository;
import org.transferservice.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticatorService implements IAuthenticator {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AccountDTO register(CreateAccountDTO createAccountDTO) throws AccountAlreadyExistException {

        if (this.accountRepository.existsByEmail(createAccountDTO.getEmail())) {
            throw new AccountAlreadyExistException(String.format("Customer with email %s already exists", createAccountDTO.getEmail()));
        }

        if (this.accountRepository.existsByPhoneNumber(createAccountDTO.getPhoneNumber())) {
            throw new AccountAlreadyExistException(String.format("Customer with phone number %s already exists", createAccountDTO.getPhoneNumber()));
        }

        List<Card> cards = new ArrayList<>();
        Card card = null;
        if(createAccountDTO.getCard()!=null){
            card = Card.builder()
                    .cardNumber(createAccountDTO.getCard().getCardNumber())
                    .active(true)
                    .currency(CardCurrency.EGP)
                    .cardHolderName(createAccountDTO.getCard().getCardHolderName())
                    .balance(10000.0)
                    .cvv(createAccountDTO.getCard().getCvv())
                    .isDefault(true)
                    .expirationDate(createAccountDTO.getCard().getExpirationDate())
                    .build();

            cards.add(this.cardRepository.save(card));
        }

        Account account = Account
                .builder()
                .email(createAccountDTO.getEmail())
                .username(createAccountDTO.getUsername())
                .phoneNumber(createAccountDTO.getPhoneNumber())
                .gender(createAccountDTO.getGender())
                .dateOfBirth(createAccountDTO.getDateOfBirth())
                .country(createAccountDTO.getCountry())
                .password(this.encoder.encode(createAccountDTO.getPassword()))
                .cards(cards)
                .build();


        if(card!=null){
            card.setAccount(account);
            cardRepository.save(card);
        }

        return this.accountRepository.save(account).toDTO();
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