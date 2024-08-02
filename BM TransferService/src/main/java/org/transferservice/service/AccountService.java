package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.CardDTO;
import org.transferservice.dto.CreateCardDTO;
import org.transferservice.dto.UpdateAccountDTO;
import org.transferservice.dto.enums.CardCurrency;
import org.transferservice.exception.custom.*;
import org.transferservice.model.Account;
import org.transferservice.model.Card;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CardRepository;
import org.transferservice.service.security.AuthTokenFilter;
import org.transferservice.service.security.TokenBlacklist;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService implements IAccount {

    private final AccountRepository accountRepository;
    private final TokenBlacklist tokenBlacklist;
    private final AuthTokenFilter authTokenFilter;
    private final CardRepository cardRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Account updateAccount(Long id, UpdateAccountDTO updateAccountDTO) throws AccountNotFoundException {


//        Account.setUsername(updateAccountDTO.getFirstName());
//        Account.setEmail(updateAccountDTO.getEmail());
//        Account.setPhoneNumber(updateAccountDTO.getPhoneNumber());
//        Account.setAddress(updateAccountDTO.getAddress());
//        Account.setNationality(updateAccountDTO.getNationality());
//        Account.setNationalIdNumber(updateAccountDTO.getNationalIdNumber());
//        Account.setDateOfBirth(updateAccountDTO.getDateOfBirth());

        return null;

    }


    @Override
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = authTokenFilter.parseJwt(request);
        if(token!=null)
            tokenBlacklist.addToBlacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }



    @Override
    public void transferMoney(CardDTO cardDTO, double sentAmount, CardCurrency sendingCurrency,
                              CardCurrency receivingCurrency, double receivedAmount, HttpServletRequest request)
            throws CardNotFoundException, InsufficientFundsException, AccountNotFoundException, InvalidCardCurrencyException, NoDefaultCardException {

        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account sender = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card senderCard = sender.getCards().stream().filter(Card::isDefault).findFirst()
                .orElseThrow(()->new NoDefaultCardException("Account does not have a default card"));

        Card recepientCard = cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .orElseThrow(()->new CardNotFoundException(String.format("Card with card number %s not found", cardDTO.getCardNumber())));

        if(!recepientCard.getCardHolderName().equals(cardDTO.getCardHolderName()))
            throw new CardNotFoundException("Card holder name does not match");

        if(senderCard.getCurrency()!=sendingCurrency)
            throw new InvalidCardCurrencyException("Sender card currency does not match transfer currency, select " + senderCard.getCurrency());

        if(recepientCard.getCurrency()!=receivingCurrency)
            throw new InvalidCardCurrencyException("Recipient card currency does not match transfer currency, select " + recepientCard.getCurrency());

        if(senderCard.getBalance()<sentAmount) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        senderCard.setBalance(senderCard.getBalance() - sentAmount);
        recepientCard.setBalance(recepientCard.getBalance() + receivedAmount);

    }

    @Override
    public List<CardDTO> addCard(CreateCardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));;

        Card card = Card.builder()
                .cardNumber(cardDTO.getCardNumber())
                .cardHolderName(cardDTO.getCardHolderName())
                .CVV(cardDTO.getCVV())
                .balance(10000.0)
                .active(true)
                .isDefault(false)
                .account(account)
                .currency(CardCurrency.EGP)
                .build();

        List<Card> accountCards = account.getCards();
        accountCards.add(card);
        account.setCards(accountCards);
        accountRepository.save(account);

        return account.getCards().stream().map(Card::toDTO).toList();

    }

    @Override
    public void removeCard(CardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card card = cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .orElseThrow(()-> new CardNotFoundException("Card does not exist"));

        List<Card> accountCards = account.getCards();
        accountCards.remove(card);
        account.setCards(accountCards);
        accountRepository.save(account);

        cardRepository.delete(card);
    }

    @Override
    public List<CardDTO> viewCards(HttpServletRequest request) throws AccountNotFoundException{
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));
        return account.getCards().stream().map(Card::toDTO).toList();
    }

    @Override
    public void changeDefault(CardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card card = cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .orElseThrow(()-> new CardNotFoundException("Card does not exist"));
        if(!card.getAccount().equals(account)){
            throw new CardNotFoundException("Card does not exist");
        }

        account.getCards().forEach(c -> c.setDefault(c.getCardNumber().equals(cardDTO.getCardNumber())));
        accountRepository.save(account);
    }

    @Override
    public List<CardDTO> addfavourite(CardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card card = cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .orElseThrow(()-> new CardNotFoundException("Card does not exist"));
        if(!card.getCardHolderName().equals(cardDTO.getCardHolderName())){
            throw new CardNotFoundException("Card does not exist");
        }

        List<Card> favourites = account.getFavoriteRecipients();
        favourites.add(card);
        account.setFavoriteRecipients(favourites);
        accountRepository.save(account);

        return account.getFavoriteRecipients().stream().map(Card::toDTO).toList();

    }

    @Override
    public void removefavourite(CardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card card = cardRepository.findByCardNumber(cardDTO.getCardNumber())
                .orElseThrow(()-> new CardNotFoundException("Card does not exist"));

        List<Card> myCards = account.getCards();
        myCards.remove(card);
        account.setCards(myCards);
        accountRepository.save(account);
    }

    @Override
    public List<CardDTO> viewfavourites(HttpServletRequest request) throws AccountNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));
        return account.getFavoriteRecipients().stream().map(Card::toDTO).toList();
    }

    @Override
    public Double viewBalance(HttpServletRequest request) throws AccountNotFoundException, NoDefaultCardException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()-> new AccountNotFoundException(String.format("Account with email %s not found", email)));

        Card card = account.getCards().stream().filter(Card::isDefault).findFirst()
                .orElseThrow(()->new NoDefaultCardException("Account does not have a default card"));

        return card.getBalance();
    }


}