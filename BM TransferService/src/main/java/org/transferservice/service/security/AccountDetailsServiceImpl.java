package org.transferservice.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.model.Account;
import org.transferservice.repository.AccountRepository;


@Service
@RequiredArgsConstructor
public class AccountDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findAccountByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer Not Found with email: " + username));

        return AccountDetailsImpl.builder()
                .email(account.getEmail())
                .password(account.getPassword()).build();
    }
}
