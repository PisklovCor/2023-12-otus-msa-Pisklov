package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Order;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с заказами ")
@JdbcTest
@Import(JdbcOrderRepository.class)
class JdbcOrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @DisplayName("должен создать заказ")
    @Test
    void create() {

        Order order = new Order();
        order.setLogin("test");
        order.setAccountInvoice(UUID.randomUUID());
        order.setSumOrder(5000);
        order.setStatus(true);

        var insertOrder =  repository.create(order);

        assertThat(insertOrder).isNotNull()
                .matches(o -> o.getId() > 0);

        assertThat(repository.findByLogin(insertOrder.getLogin()))
                .isPresent();
        assertThat(repository.findByLogin(insertOrder.getLogin()).get()
                .getSumOrder()).isEqualTo(insertOrder.getSumOrder());

    }

    @DisplayName("должен загрузить заказ по логину аккаунта")
    @Test
    void findByLogin() {
        var order =  repository.findByLogin("admin");

        assertThat(order).isPresent();
        assertThat(order.get().getLogin()).isEqualTo("admin");
    }

    @DisplayName("должен загрузить список всех заказов")
    @Test
    void findAll() {
        var orderList = repository.findAll();

        assertThat(orderList).isNotNull().size().isEqualTo(1);
    }
}