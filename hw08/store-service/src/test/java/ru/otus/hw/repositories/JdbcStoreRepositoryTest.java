package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Store;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.dto.Status.CANCELED;
import static ru.otus.hw.dto.Status.CREATED;

@DisplayName("Репозиторий на основе Jpa для работы с товарами ")
@JdbcTest
@Import(JdbcStoreRepository.class)
class JdbcStoreRepositoryTest {

    @Autowired
    private StoreRepository repository;

    @DisplayName("должен создать товар")
    @Test
    void create() {

        Store store = new Store();
        store.setPaymentId(2);
        store.setOrderId(2);
        store.setLogin("test");
        store.setAccountInvoice(UUID.randomUUID());
        store.setDescriptionOrder("test description");
        store.setSumOrder(5000);
        store.setStatus(CREATED.toString());

        var insertOrder =  repository.create(store);

        assertThat(insertOrder).isNotNull()
                .matches(o -> o.getId() > 0);

        assertThat(repository.findByLogin(insertOrder.getLogin()))
                .isPresent();
        assertThat(repository.findByLogin(insertOrder.getLogin()).get()
                .getSumOrder()).isEqualTo(insertOrder.getSumOrder());

    }

    @DisplayName("должен загрузить товар по uuid аккаунта")
    @Test
    void findByUUID() {
        var order =  repository.findByUUID(UUID.fromString("32fff079-610b-466c-a094-241a98eca2f0"));

        assertThat(order).isPresent();
        assertThat(order.get().getLogin()).isEqualTo("admin");
    }

    @DisplayName("должен загрузить товар по логину аккаунта")
    @Test
    void findByLogin() {
        var order =  repository.findByLogin("admin");

        assertThat(order).isPresent();
        assertThat(order.get().getLogin()).isEqualTo("admin");
    }

    @DisplayName("должен загрузить список всех товаров")
    @Test
    void findAll() {
        var orderList = repository.findAll();

        assertThat(orderList).isNotNull().size().isEqualTo(1);
    }

    @DisplayName("должен обновить статус товара")
    @Test
    void updateOrderStatus() {
        repository.updateStoreStatus(1, CANCELED);

        var order =  repository.findByLogin("admin");
        assertThat(order).isPresent();
        assertThat(order.get().getStatus()).isEqualTo("CANCELED");
    }
}