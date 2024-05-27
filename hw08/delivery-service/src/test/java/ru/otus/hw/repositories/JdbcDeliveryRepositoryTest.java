package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Delivery;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.dto.Status.CANCELED;
import static ru.otus.hw.dto.Status.CREATED;

@DisplayName("Репозиторий на основе Jpa для работы с товарами ")
@JdbcTest
@Import(JdbcDeliveryRepository.class)
class JdbcDeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository repository;

    @DisplayName("должен создать товар")
    @Test
    void create() {

        Delivery delivery = new Delivery();
        delivery.setPaymentId(2);
        delivery.setOrderId(2);
        delivery.setLogin("test");
        delivery.setAccountInvoice(UUID.randomUUID());
        delivery.setDescriptionOrder("test description");
        delivery.setSumOrder(5000);
        delivery.setStatus(CREATED.toString());

        var insertDelivery =  repository.create(delivery);

        assertThat(insertDelivery).isNotNull()
                .matches(o -> o.getId() > 0);

        assertThat(repository.findByLogin(insertDelivery.getLogin()))
                .isPresent();
        assertThat(repository.findByLogin(insertDelivery.getLogin()).get()
                .getSumOrder()).isEqualTo(insertDelivery.getSumOrder());

    }

    @DisplayName("должен загрузить товар по uuid аккаунта")
    @Test
    void findByUUID() {
        var delivery =  repository.findByUUID(UUID.fromString("32fff079-610b-466c-a094-241a98eca2f0"));

        assertThat(delivery).isPresent();
        assertThat(delivery.get().getLogin()).isEqualTo("admin");
    }

    @DisplayName("должен загрузить товар по логину аккаунта")
    @Test
    void findByLogin() {
        var delivery =  repository.findByLogin("admin");

        assertThat(delivery).isPresent();
        assertThat(delivery.get().getLogin()).isEqualTo("admin");
    }

    @DisplayName("должен загрузить список всех товаров")
    @Test
    void findAll() {
        var deliveryList = repository.findAll();

        assertThat(deliveryList).isNotNull().size().isEqualTo(1);
    }

    @DisplayName("должен обновить статус товара")
    @Test
    void updateOrderStatus() {
        repository.updateDeliveryStatus(1, CANCELED);

        var delivery =  repository.findByLogin("admin");
        assertThat(delivery).isPresent();
        assertThat(delivery.get().getStatus()).isEqualTo("CANCELED");
    }
}