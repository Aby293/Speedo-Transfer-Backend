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
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.UpdateAccountDTO;
import org.transferservice.exception.custom.AccountNotFoundException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.model.Account;
import org.transferservice.service.IAccount;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class AccountController {

    private final IAccount customerService;

//    @Operation(summary = "Update Customer by ID")
//    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = AccountDTO.class), mediaType = "application/json")})
//    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
//    @PutMapping("/{id}")
//    public Account updateCustomer(@PathVariable Long id,
//                                  @RequestBody UpdateAccountDTO updateAccountDTO) throws AccountNotFoundException {
//        return customerService.updateAccount(id, updateAccountDTO);
//    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "405", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return this.customerService.logout(request);
    }

}
