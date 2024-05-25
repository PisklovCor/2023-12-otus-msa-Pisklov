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
    void create() {

        Account account = new Account();
        account.setId(2);
        account.setLogin("test");
        account.setBalance(5000);

        var insertAccount =  repository.create(account);

        assertThat(insertAccount).isNotNull()
                .matches(o -> o.getId() > 0);

        assertThat(repository.findByLogin(insertAccount.getLogin()))
                .isPresent();
        assertThat(repository.findByLogin(insertAccount.getLogin()).get()
                .getBalance()).isEqualTo(insertAccount.getBalance());

    }

    @DisplayName("должен загрузить аккаунт по логину аккаунта")
    @Test
    void findByLogin() {

        var account =  repository.findByLogin("admin");

        assertThat(account).isPresent();
        assertThat(account.get().getLogin()).isEqualTo("admin");

    }

    @DisplayName("должен загрузить список всех аккаунтов")
    @Test
    void findAll() {

        var oaccountList = repository.findAll();

        assertThat(oaccountList).isNotNull().size().isEqualTo(1);
    }

    @DisplayName("должен обновить аккаунт заказа")
    @Test
    void updateAccount() {

        Account account = new Account();
        account.setLogin("admin");
        account.setBalance(4000);

        repository.updateAccount(account);

        var order =  repository.findByLogin("admin");
        assertThat(order).isPresent();
        assertThat(order.get().getBalance()).isEqualTo(4000);
    }
}
