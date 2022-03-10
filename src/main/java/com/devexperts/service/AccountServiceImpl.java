package com.devexperts.service;

import com.devexperts.dto.AccountKeyId;
import com.devexperts.exception.InsufficientAccountBalanceException;
import com.devexperts.mapper.MapperAccount;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.model.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        Account createdAccount = MapperAccount.mapToId(account);

        accounts.put(createdAccount.getAccountKey(), createdAccount);
    }

    @Override
    public Account getAccount(long id) {
        Account account = accounts.get(AccountKeyId.valueOf(id));

        if (account == null) {
            throw new NoSuchElementException("Account not found");
        }

        return accounts.get(AccountKeyId.valueOf(id));
    }

    @Override
    // @Transactional
    public void transfer(Account source, Account target, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount is invalid");
        }

        if (source.getBalance() < amount) {
            throw new InsufficientAccountBalanceException("Insufficient account balance of source");
        }

        Account minAccount = MapperAccount.mapToId(source);
        Account maxAccount = MapperAccount.mapToId(target);
        long minAccountId = ((AccountKeyId) minAccount.getAccountKey()).getId();
        long maxAccountId = ((AccountKeyId) maxAccount.getAccountKey()).getId();

        minAccount = minAccountId < maxAccountId ? source : target;
        maxAccount = minAccountId < maxAccountId ? target : source;

        synchronized (minAccount) {
            synchronized (maxAccount) {
                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);
            }
        }
    }
}
