package org.transferservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.*;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.exception.custom.*;
import org.transferservice.model.Customer;
import org.transferservice.model.Account;
import org.transferservice.model.Transaction;
import org.transferservice.repository.CountryCurrenciesRepository;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.TransactionRepository;
import org.transferservice.service.security.AuthTokenFilter;
import org.transferservice.service.security.TokenBlacklist;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomer {

    private final CustomerRepository customerRepository ;
    private final TokenBlacklist tokenBlacklist;
    private final AuthTokenFilter authTokenFilter;
    private final AccountRepository accountRepository;
    private final CountryCurrenciesRepository countryCurrenciesRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public CustomerDTO viewCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);
        
        return customer.toDTO();
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CustomerDTO updateCustomerInformation(HttpServletRequest request, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);
        if(updateCustomerDTO.getUsername()!=null)
            customer.setUsername(updateCustomerDTO.getUsername());
        if(updateCustomerDTO.getEmail()!=null)
            customer.setEmail(updateCustomerDTO.getEmail());
        if(updateCustomerDTO.getPhoneNumber()!=null)
            customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
        if(updateCustomerDTO.getDateOfBirth()!=null)
            customer.setDateOfBirth(updateCustomerDTO.getDateOfBirth());
        if(updateCustomerDTO.getCountry()!=null)
            customer.setCountry(updateCustomerDTO.getCountry());
        return customerRepository.save(customer).toDTO();

    }

    @Override
    public void changePassword(UpdatePasswordDTO passwordDTO, HttpServletRequest request) throws CustomerNotFoundException, PasswordMismatchException {
        Customer customer = getCurrentCustomer(request);

        if(!customer.getPassword().equals(passwordDTO.getCurrentPassword()))
            throw new PasswordMismatchException("Current password does not match entered password");

        customer.setPassword(passwordDTO.getNewPassword());
        customerRepository.save(customer);
    }


    @Override
    public ResponseEntity<String> logout(HttpServletRequest request){
        String token = authTokenFilter.parseJwt(request);
        if(token!=null)
            tokenBlacklist.addToBlacklist(token);
        return ResponseEntity.ok("Logged out successfully");
    }



    @Override
    public void transferMoney(TransferDTO transferDTO, HttpServletRequest request)
            throws AccountNotFoundException, InsufficientFundsException, CustomerNotFoundException, InvalidAccountCurrencyException, NoDefaultAccountException {


        Customer sender = getCurrentCustomer(request);

        Account senderAccount = sender.getAccounts().stream().filter(Account::isDefault).findFirst()
                .orElseThrow(()->new NoDefaultAccountException("Customer does not have a default account"));

        Account recepientAccount = accountRepository.findByAccountNumber(transferDTO.getAccountDTO().getAccountNumber())
                .orElseThrow(()->new AccountNotFoundException(String.format("Account with account number %s not found", transferDTO.getAccountDTO().getAccountNumber())));

        if(!recepientAccount.getAccountHolderName().equals(transferDTO.getAccountDTO().getAccountHolderName()))
            throw new AccountNotFoundException("Account holder name does not match");

        if(senderAccount.getCurrency()!=transferDTO.getSendingCurrency())
            throw new InvalidAccountCurrencyException("Sender account currency does not match transfer currency, select " + senderAccount.getCurrency());

        if(recepientAccount.getCurrency()!=transferDTO.getReceivingCurrency())
            throw new InvalidAccountCurrencyException("Recipient account currency does not match transfer currency, select " + recepientAccount.getCurrency());

        Customer recipient = recepientAccount.getCustomer();
        accountRepository.save(recepientAccount);
        accountRepository.save(senderAccount);
        System.out.println("1");
        customerRepository.save(sender);
        customerRepository.save(recipient);
        System.out.println("2");

        Transaction transaction = Transaction.builder()
                .senderAccount(senderAccount)
                .amount(transferDTO.getSentAmount())
                .isSuccessful(false)
                .recipientAccount(recepientAccount)
                .recipientCustomer(recipient)
                .senderCustomer(sender)
                .build();

        transactionRepository.save(transaction);

        System.out.println("3");

        if(senderAccount.getBalance()<transferDTO.getSentAmount()) {
            List<Transaction> transactions = sender.getTransactions();
            transactions.add(transaction);
            sender.setTransactions(transactions);
            throw new InsufficientFundsException("Insufficient funds");
        }
        System.out.println("4");

        transaction.setSuccessful(true);
        transactionRepository.save(transaction);
        List<Transaction> transactions = sender.getTransactions();
        transactions.add(transaction);
        sender.setTransactions(transactions);

        System.out.println("5");

        double sentInDollar = transferDTO.getSentAmount()*countryCurrenciesRepository.findByCurrency(transferDTO.getSendingCurrency())
                        .orElseThrow(()->new InvalidAccountCurrencyException("Currency not found in database"))
                        .getRateToDollar();
        double receivingRate = countryCurrenciesRepository.findByCurrency(transferDTO.getReceivingCurrency())
                .orElseThrow(()->new InvalidAccountCurrencyException("Currency not found in database")).getRateToDollar();

        System.out.println("6");

        Transaction transaction2 = Transaction.builder()
                .senderAccount(recepientAccount)
                .amount(sentInDollar/receivingRate)
                .isSuccessful(true)
                .recipientAccount(senderAccount)
                .recipientCustomer(sender)
                .senderCustomer(recipient)
                .build();

        transactionRepository.save(transaction2);

        System.out.println("7");

        transactions = recipient.getTransactions();
        transactions.add(transaction2);
        recipient.setTransactions(transactions);

        customerRepository.save(sender);
        customerRepository.save(recipient);

        System.out.println("8");

        senderAccount.setBalance(senderAccount.getBalance() - transferDTO.getSentAmount());
        recepientAccount.setBalance(recepientAccount.getBalance() + sentInDollar/receivingRate);
        System.out.println("9");
        accountRepository.save(senderAccount);
        accountRepository.save(recepientAccount);


    }

    @Override
    public List<AccountDTO> addAccount(CreateAccountDTO accountDTO, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);

        Account account = Account.builder()
                .accountNumber(accountDTO.getAccountNumber())
                .accountHolderName(accountDTO.getAccountHolderName())
                .cvv(accountDTO.getCvv())
                .balance(10000.0)
                .active(true)
                .expirationDate(accountDTO.getExpirationDate())
                .isDefault(false)
                .customer(customer)
                .currency(AccountCurrency.EGP)
                .build();

        accountRepository.save(account);


        List<Account> customerAccounts = customer.getAccounts();
        if(customerAccounts.isEmpty()) account.setDefault(true);
        customerAccounts.add(account);
        customer.setAccounts(customerAccounts);
        accountRepository.save(account);
        customerRepository.save(customer);

        return customer.getAccounts().stream().map(Account::toDTO).toList();

    }

    @Override
    public void removeAccount(AccountDTO accountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = getCurrentCustomer(request);

        Account account = accountRepository.findByAccountNumber(accountDTO.getAccountNumber())
                .orElseThrow(()-> new AccountNotFoundException("Account does not exist"));

        List<Account> accountAccounts = customer.getAccounts();
        accountAccounts.remove(account);
        customer.setAccounts(accountAccounts);
        customerRepository.save(customer);

        accountRepository.delete(account);
    }

    @Override
    public List<AccountDTO> viewAccounts(HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);
        return customer.getAccounts().stream().map(Account::toDTO).toList();
    }

    @Override
    public void changeDefault(AccountDTO accountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = getCurrentCustomer(request);

            Account account = accountRepository.findByAccountNumber(accountDTO.getAccountNumber())
                .orElseThrow(()-> new AccountNotFoundException("Account does not exist"));
        if(!account.getCustomer().equals(customer)){
            throw new AccountNotFoundException("Customer does not have this account");
        }

        List<Account> myAccounts = customer.getAccounts();

        for (Account myAccount : myAccounts) {
            myAccount.setDefault(myAccount.equals(account));
            accountRepository.save(myAccount);
        }
        customer.setAccounts(myAccounts);
        customerRepository.save(customer);
    }

    @Override
    public List<AccountDTO> addFavourite(AccountDTO accountDTO, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = getCurrentCustomer(request);

        Account account = accountRepository.findByAccountNumber(accountDTO.getAccountNumber())
                .orElseThrow(()-> new AccountNotFoundException("Account does not exist"));
        if(!account.getAccountHolderName().equals(accountDTO.getAccountHolderName())){
            throw new AccountNotFoundException("Account does not exist");
        }

        List<Account> favourites = customer.getFavoriteRecipients();
        favourites.add(account);
        customer.setFavoriteRecipients(favourites);
        customerRepository.save(customer);

        return customer.getFavoriteRecipients().stream().map(Account::toDTO).toList();

    }

    @Override
    public void removeFavourite(String accountNumber, HttpServletRequest request) throws CustomerNotFoundException, AccountNotFoundException {
        Customer customer = getCurrentCustomer(request);

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("Account does not exist"));

        List<Account> myAccounts = customer.getFavoriteRecipients();
        if(!myAccounts.remove(account))
            throw new AccountNotFoundException("Account does not exist");
        customer.setFavoriteRecipients(myAccounts);
        customerRepository.save(customer);
    }

    @Override
    public List<AccountDTO> viewFavourites(HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);
        return customer.getFavoriteRecipients().stream().map(Account::toDTO).toList();
    }

    @Override
    public List<TransactionDTO> viewTransactions(HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getCurrentCustomer(request);
        return customer.getTransactions().stream().map(Transaction::toDTO).toList();
    }

    @Override
    public String viewBalance(HttpServletRequest request) throws CustomerNotFoundException, NoDefaultAccountException, InvalidAccountCurrencyException {
        Customer customer = getCurrentCustomer(request);

        Account account = customer.getAccounts().stream().filter(Account::isDefault).findFirst()
                .orElseThrow(()->new NoDefaultAccountException("Account does not have a default account"));

        String symbol = countryCurrenciesRepository.findByCurrency(account.getCurrency())
                .orElseThrow(()->new InvalidAccountCurrencyException("No such currency in the database"))
                .getCurrencySymbol();

        return symbol+account.getBalance();
    }

    public Customer getCurrentCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String jwt = authTokenFilter.parseJwt(request);
        String email = authTokenFilter.getUserName(jwt);
        return customerRepository.findByEmail(email)
                .orElseThrow(()-> new CustomerNotFoundException(String.format("Account with email %s not found", email)));
    }

    public List<Customer> getCustomerTable(){
        return customerRepository.findAll();
    }

    public List<Account> getAccountTable(){
        return accountRepository.findAll();
    }

    public List<Transaction> getTransactionTable(){
        return transactionRepository.findAll();
    }


}
