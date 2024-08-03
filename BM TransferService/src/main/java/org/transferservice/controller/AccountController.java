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
import org.transferservice.service.IAccount;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class AccountController {

    private final IAccount customerService;

    @Operation(summary = "Update customer account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/account")
    public AccountDTO updateAccount(HttpServletRequest httpServletRequest, @RequestBody UpdateAccountDTO updateAccountDTO)
            throws AccountNotFoundException {
        return customerService.updateAccountInformation(httpServletRequest, updateAccountDTO);
    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "405", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return this.customerService.logout(request);
    }


    @Operation(summary = "View customer account")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/account")
    public AccountDTO viewAccount(HttpServletRequest httpServletRequest)
            throws AccountNotFoundException {
        return customerService.viewAccount(httpServletRequest);
    }

    @Operation(summary = "Add new favourite")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/favorites")
    public List<CardDTO> addFavourite(@RequestBody CardDTO card, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, CardNotFoundException {
        return customerService.addFavourite(card,httpServletRequest);
    }

    @Operation(summary = "View favourite")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/favorites")
    public List<CardDTO> viewFavourite(HttpServletRequest httpServletRequest)
            throws AccountNotFoundException{
        return customerService.viewFavourites(httpServletRequest);
    }

    @Operation(summary = "Delete favorite")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @DeleteMapping("/api/favorites")
    public void deleteFavorite(@RequestBody CardDTO card, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, CardNotFoundException {
        customerService.removeFavourite(card,httpServletRequest);
    }


    @Operation(summary = "Add new card")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/cards")
    public List<CardDTO> addCard(@RequestBody CreateCardDTO card, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException {
        return customerService.addCard(card,httpServletRequest);
    }

    @Operation(summary = "View cards")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/cards")
    public List<CardDTO> viewCards(HttpServletRequest httpServletRequest)
            throws AccountNotFoundException{
        return customerService.viewCards(httpServletRequest);
    }


    @Operation(summary = "Delete card")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @DeleteMapping("/api/cards")
    public void deleteCard(@RequestBody CardDTO card, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, CardNotFoundException {
        customerService.removeCard(card,httpServletRequest);
    }

    @Operation(summary = "Change account password")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/password")
    public void changePassword(@RequestBody UpdateAccountDTO account, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException {
        customerService.changePassword(account,httpServletRequest);
    }

    @Operation(summary = "Transfer money to another account")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/transfer")
    public void transferMoney(@RequestBody TransferDTO transferDTO, HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, InvalidCardCurrencyException, InsufficientFundsException, NoDefaultCardException, CardNotFoundException {
        customerService.transferMoney(transferDTO,httpServletRequest);
    }

    @Operation(summary = "View transactions history")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/transactions")
    public List<TransactionDTO> transactionsHistory(HttpServletRequest httpServletRequest)
            throws AccountNotFoundException{
        return customerService.viewTransactions(httpServletRequest);
    }

    @Operation(summary = "View balance")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Double.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/api/balance")
    public double viewBalance(HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, NoDefaultCardException, CardNotFoundException {
        return customerService.viewBalance(httpServletRequest);
    }

    @Operation(summary = "Change default card")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/api/cards/default")
    public void changeDefault(CardDTO cardDTO,HttpServletRequest httpServletRequest)
            throws AccountNotFoundException, CardNotFoundException {
        customerService.changeDefault(cardDTO,httpServletRequest);
    }

}
