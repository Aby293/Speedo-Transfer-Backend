package org.transferservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.*;
import org.transferservice.exception.custom.*;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.model.Transaction;
import org.transferservice.service.ICustomer;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class CustomerController {

    private final ICustomer customerService;


    @Operation(summary = "Update customer account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/user")
    public CustomerDTO updateCustomer(HttpServletRequest httpServletRequest, @RequestBody UpdateCustomerDTO updateCustomerDTO)
            throws CustomerNotFoundException {
        return customerService.updateCustomerInformation(httpServletRequest, updateCustomerDTO);
    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "405", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return this.customerService.logout(request);
    }


    @Operation(summary = "View customer account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/user")
    public CustomerDTO viewUser(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException {
        return customerService.viewCustomer(httpServletRequest);
    }

    @Operation(summary = "Add new favourite")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/favorites")
    public List<AccountDTO> addFavourite(@RequestBody AccountDTO account, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, AccountNotFoundException {
        return customerService.addFavourite(account,httpServletRequest);
    }

    @Operation(summary = "View favourite")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/favorites")
    public List<AccountDTO> viewFavourite(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException {
        return customerService.viewFavourites(httpServletRequest);
    }

    @Operation(summary = "Delete favorite")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @DeleteMapping("/api/favorites/{accountNumber}")
    public void deleteFavorite(@PathVariable("accountNumber") String accountNumber, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, AccountNotFoundException {
        customerService.removeFavourite(accountNumber,httpServletRequest);
    }


    @Operation(summary = "Add new account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/accounts")
    public List<AccountDTO> addAccount(@RequestBody CreateAccountDTO account, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException {
        return customerService.addAccount(account,httpServletRequest);
    }

    @Operation(summary = "View accounts")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/accounts")
    public List<AccountDTO> viewAccounts(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException {
        return customerService.viewAccounts(httpServletRequest);
    }


    @Operation(summary = "Delete account")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @DeleteMapping("/api/accounts")
    public void deleteAccount(@RequestBody AccountDTO account, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, AccountNotFoundException {
        customerService.removeAccount(account,httpServletRequest);
    }

    @Operation(summary = "Change account password")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/password")
    public void changePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, PasswordMismatchException {
        customerService.changePassword(updatePasswordDTO,httpServletRequest);
    }

    @Operation(summary = "Transfer money to another account")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/transfer")
    public void transferMoney(@RequestBody TransferDTO transferDTO, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, InvalidAccountCurrencyException, InsufficientFundsException, NoDefaultAccountException, AccountNotFoundException {
        customerService.transferMoney(transferDTO,httpServletRequest);
    }

    @Operation(summary = "View transactions history")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/transactions")
    public List<TransactionDTO> transactionsHistory(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException {
        return customerService.viewTransactions(httpServletRequest);
    }

    @Operation(summary = "View balance")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Double.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/balance")
    public String viewBalance(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, NoDefaultAccountException, AccountNotFoundException, InvalidAccountCurrencyException {
        return customerService.viewBalance(httpServletRequest);
    }

    @Operation(summary = "Change default account")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/accounts/default")
    public void changeDefault(AccountDTO accountDTO, HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException, AccountNotFoundException {
        customerService.changeDefault(accountDTO,httpServletRequest);
    }

    @Operation(summary = "View default account")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/accounts/default")
    public AccountDTO getDefault(HttpServletRequest httpServletRequest)
            throws CustomerNotFoundException{
        return customerService.getDefault(httpServletRequest).toDTO();
    }

    @GetMapping("/db/customers")
    public List<Customer> customers() {
        return customerService.getCustomerTable();
    }

    @GetMapping("/db/accounts")
    public List<Account> accounts() {
        return customerService.getAccountTable();
    }

    @GetMapping("/db/transactions")
    public List<Transaction> transactions() {
        return customerService.getTransactionTable();
    }



}
