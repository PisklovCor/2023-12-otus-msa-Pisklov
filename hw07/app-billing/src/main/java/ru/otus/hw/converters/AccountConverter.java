package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.AccountDto;
import ru.otus.hw.models.Account;

import java.util.UUID;

@Component
public class AccountConverter {
    public Account mapDtoToModel(AccountDto dto) {
        Account account = new Account();
        account.setLogin(dto.getLogin());
        account.setInvoice(UUID.randomUUID());
        account.setMoney(dto.getMoney());
        account.setFullName(dto.getFullName());
        return account;
    }
}
