package com.devexperts.dto;

import com.devexperts.model.account.AccountKey;

import java.lang.reflect.Field;

public class AccountKeyId extends AccountKey {
    protected AccountKeyId(long accountId) {
        super(accountId);
    }

    public static AccountKeyId fromAccountKey(AccountKey accountKey) {
        if (accountKey instanceof AccountKeyId) {
            return (AccountKeyId) accountKey;
        } else {
            try {
                Field field = AccountKey.class.getDeclaredField("accountId");
                field.trySetAccessible();
                long id = (long) field.get(accountKey);
                return new AccountKeyId(id);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                return null;
            }
        }
    }

    public static AccountKeyId valueOf(long accountId) {
        return new AccountKeyId(accountId);
    }

    public long getId() {
        return accountId;
    }

    @Override
    public int hashCode() {
        return (int) accountId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccountKeyId accountKeyId = (AccountKeyId) obj;
        return accountId == accountKeyId.accountId;
    }
}
