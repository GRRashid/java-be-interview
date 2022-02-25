package com.devexperts.mapper;

import com.devexperts.dto.AccountKeyId;
import com.devexperts.model.account.Account;

public class MapperAccount {
    private MapperAccount() {
    }

    public static Account mapToId(Account account) {
        if (account.getAccountKey() instanceof AccountKeyId) {
            return account;
        }

        AccountKeyId accountKeyId = AccountKeyId.fromAccountKey(account.getAccountKey());

        return new Account(accountKeyId, account.getFirstName(), account.getLastName(), account.getBalance());
    }
}
