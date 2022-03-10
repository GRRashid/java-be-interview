package com.devexperts.rest;

import com.devexperts.exception.InsufficientAccountBalanceException;
import com.devexperts.model.account.Account;
import com.devexperts.model.rest.AbstractAccountController;
import com.devexperts.model.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class AccountController implements AbstractAccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("operations/transfer")
    public ResponseEntity<Void> transfer(@RequestParam("source_id") long sourceId,
                                         @RequestParam("target_id") long targetId,
                                         @RequestParam("amount") double amount) {
        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);

        accountService.transfer(source, target, amount);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void illegalArgument() {
        // Nothing to do
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void noSuchElement() {
        // Nothing to do
    }

    @ResponseStatus
    @ExceptionHandler(InsufficientAccountBalanceException.class)
    public void invalidBalance() {
        // Nothing to do
    }
}
