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
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.model.Customer;
import org.transferservice.service.ICustomer;
import org.transferservice.service.security.AuthTokenFilter;
import org.transferservice.service.security.TokenBlacklist;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class CustomerController {

    private final ICustomer customerService;

    @Operation(summary = "Update Customer by ID")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id,
                                   @RequestBody UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {
        return customerService.updateCustomer(id, updateCustomerDTO);
    }

    @Operation(summary = "Logout")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseEntity.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "405", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return this.customerService.logout(request);
    }

}
