package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.transferservice.dto.CardDTO;
import org.transferservice.dto.CreateCardDTO;
import org.transferservice.dto.UpdateAccountDTO;
import org.transferservice.dto.enums.CardCurrency;
import org.transferservice.exception.custom.*;
import org.transferservice.model.Account;

import java.util.List;


public interface IAccount {

    /**
     * Update customer details
     *
     * @param id                customer id
     * @param updateAccountDTO customer details
     * @return updated customer @{@link Account}
     * @throws AccountNotFoundException if customer not found
     */
    Account updateAccount(Long id, UpdateAccountDTO updateAccountDTO) throws AccountNotFoundException;


    ResponseEntity<String> logout(HttpServletRequest request);

    void transferMoney(CardDTO cardDTO, double sentAmount, CardCurrency sendingCurrency,
                       CardCurrency receivingCurrency, double receivedAmount, HttpServletRequest request)
            throws CardNotFoundException, InsufficientFundsException, AccountNotFoundException, InvalidCardCurrencyException, NoDefaultCardException;

    Double viewBalance(HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException, NoDefaultCardException;

    List<CardDTO> addCard(CreateCardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException;

    void removeCard(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> viewCards(HttpServletRequest request) throws AccountNotFoundException;

    void changeDefault(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> addfavourite(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    void removefavourite(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> viewfavourites(HttpServletRequest request) throws AccountNotFoundException;




}
