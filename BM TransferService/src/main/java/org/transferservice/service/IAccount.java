package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.transferservice.dto.*;
import org.transferservice.dto.enums.CardCurrency;
import org.transferservice.exception.custom.*;

import java.util.List;


public interface IAccount {

    /**
     *
     * @param request http request
     * @return account @{@link AccountDTO}
     * @throws AccountNotFoundException if account not found
     */
    AccountDTO viewAccount(HttpServletRequest request) throws AccountNotFoundException;

    /**
     *
     * @param request http request
     * @param updateAccountDTO updated info
     * @return account @{@link AccountDTO}
     * @throws AccountNotFoundException if account not found
     */
    AccountDTO updateAccountInformation(HttpServletRequest request, UpdateAccountDTO updateAccountDTO) throws AccountNotFoundException;

    void changePassword(UpdateAccountDTO accountDTO);

    ResponseEntity<String> logout(HttpServletRequest request);

    void transferMoney(CardDTO cardDTO, double sentAmount, CardCurrency sendingCurrency,
                       CardCurrency receivingCurrency, double receivedAmount, HttpServletRequest request)
            throws CardNotFoundException, InsufficientFundsException, AccountNotFoundException, InvalidCardCurrencyException, NoDefaultCardException;

    Double viewBalance(HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException, NoDefaultCardException;

    List<CardDTO> addCard(CreateCardDTO cardDTO, HttpServletRequest request) throws AccountNotFoundException;

    void removeCard(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> viewCards(HttpServletRequest request) throws AccountNotFoundException;

    void changeDefault(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> addFavourite(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    void removeFavourite(CardDTO CardDTO, HttpServletRequest request) throws AccountNotFoundException, CardNotFoundException;

    List<CardDTO> viewFavourites(HttpServletRequest request) throws AccountNotFoundException;

    List<TransactionDTO> viewTransactions(HttpServletRequest request) throws AccountNotFoundException;



}
