package ru.otus.hw.repositories;

import ru.otus.hw.dto.Status;
import ru.otus.hw.models.Store;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

    Store create(Store store);

    Optional<Store> findById(long id);

    Optional<Store> findByUUID(UUID uuid);

    Optional<Store> findByLogin(String login);

    List<Store> findAll();

    void updateStoreStatus(long id, Status status);
}
