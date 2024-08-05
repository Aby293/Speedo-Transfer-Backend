package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.transferservice.dto.*;
import org.transferservice.exception.custom.*;

import java.util.List;


public interface ICustomer {

    /**
     *
     * @param request http request
     * @return account @{@link CustomerDTO}
     * @throws CustomerNotFoundException if account not found
     */
    CustomerDTO viewCustomer(HttpServletRequest request) throws CustomerNotFoundException;

    /**
     *
     * @param request http request
     * @param updateCustomerDTO updated info
     * @return account @{@link CustomerDTO}
     * @throws CustomerNotFoundException if account not found
     */
    CustomerDTO updateCustomerInformation(HttpServletRequest request, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException ;

    void changePassword(UpdatePasswordDTO passwordDTO, HttpServletRequest request) throws CustomerNotFoundException, PasswordMismatchException;

    ResponseEntity<String> logout(HttpServletRequest request);

    void transferMoney( TransferDTO transferDTO, HttpServletRequest request)
            throws AccountNotFoundException, InsufficientFundsException, CustomerNotFoundException, InvalidAccountCurrencyException, NoDefaultAccountException;

    String viewBalance(HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException, NoDefaultAccountException, InvalidAccountCurrencyException;

    List<AccountDTO> addAccount(CreateAccountDTO accountDTO, HttpServletRequest request) throws CustomerNotFoundException;

    void removeAccount(AccountDTO AccountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException;

    List<AccountDTO> viewAccounts(HttpServletRequest request) throws CustomerNotFoundException;

    void changeDefault(AccountDTO AccountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException;

    List<AccountDTO> addFavourite(AccountDTO AccountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException;

    void removeFavourite(String accountNumber, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException;

    List<AccountDTO> viewFavourites(HttpServletRequest request) throws CustomerNotFoundException;

    List<TransactionDTO> viewTransactions(HttpServletRequest request) throws CustomerNotFoundException;


}
