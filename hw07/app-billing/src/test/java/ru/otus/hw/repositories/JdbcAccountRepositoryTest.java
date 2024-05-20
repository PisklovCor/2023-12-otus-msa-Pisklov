package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Account;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с аккаунтом ")
@JdbcTest
@Import(JdbcAccountRepository.class)
class JdbcAccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @DisplayName("должен создать аккаунт")
    @Test
    void createAccount() {

        Account account = new Account();
        account.setLogin("test");
        account.setFullName("test_test");
        account.setMoney(2000);

        var returneAccount = repository.createAccount(account);

        assertThat(returneAccount).isNotNull()
                .matches(a -> a.getId() > 0);

        assertThat(repository.findAccountByLogin(returneAccount.getLogin()))
                .isPresent();
        assertThat(repository.findAccountByLogin(returneAccount.getLogin()).get()
                .getMoney()).isEqualTo(returneAccount.getMoney());
    }

    @DisplayName("должен загрузить аккаунт")
    @Test
    void findAccountByLogin() {

        var optionalAccount = repository.findAccountByLogin("admin");
        assertThat(optionalAccount).isPresent();

        assertThat(optionalAccount.get().getLogin()).isEqualTo("admin");
        assertThat(optionalAccount.get().getFullName()).isEqualTo("admin_admin");
        assertThat(optionalAccount.get().getMoney()).isEqualTo(1000);
    }

    @DisplayName("должен пополнять аккаунт")
    @Test
    void updateMoney() {

        Account account = new Account();
        account.setLogin("admin");
        account.setMoney(1000);

        var returneAccount = repository.updateMoney(account, 3000);

        assertThat(returneAccount).isNotNull()
                .matches(a -> a.getId() > 0);
        assertThat(returneAccount.getMoney()).isEqualTo(4000);
    }

    @DisplayName("должен снимать деньги с аккаунта")
    @Test
    void updateMoney_withdrawal() {

        Account account = new Account();
        account.setLogin("admin");
        account.setMoney(1000);

        var returneAccount = repository.updateMoney(account, -500);

        assertThat(returneAccount).isNotNull()
                .matches(a -> a.getId() > 0);
        assertThat(returneAccount.getMoney()).isEqualTo(500);
    }
}