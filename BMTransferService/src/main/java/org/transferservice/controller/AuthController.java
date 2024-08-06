package org.transferservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.exception.custom.InvalidAccountCurrencyException;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.model.Account;
import org.transferservice.model.CountryCurrency;
import org.transferservice.model.Customer;
import org.transferservice.service.CountryCurrenciesService;
import org.transferservice.service.security.IAuthenticator;

import java.util.List;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@Validated
@Tag(name = "Customer Auth Controller", description = "Customer Auth controller")
public class AuthController {

    private final IAuthenticator authenticatorService;
    private final CountryCurrenciesService countryCurrenciesService;

    @Operation(summary = "Register new Customer")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/register")
    public CustomerDTO register(@RequestBody @Valid CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException, InvalidAccountCurrencyException {
        return this.authenticatorService.register(createCustomerDTO);
    }

    @Operation(summary = "Login and generate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return this.authenticatorService.login(loginRequestDTO);
    }

    @PostMapping("/db/countries")
    public List<CountryCurrency> countryCurrencies() {
        return countryCurrenciesService.getCurrencies();
    }




}
