## newman

### Проверка версии
```shell
newman -v
```

### Запуск тестов с
```shell
newman run hw04.1_Collection.postman_collection.json -e hw04_environments.postman_environment.json
```

### Настройка windows
```
- Открываем терминал PowerShell от админа.
- Вставляем и запускаем - Set-ExecutionPolicy RemoteSigned
- На вопрос отвечаем - A

- Возврат - Set-ExecutionPolicy Default
```

### Проверка версий
```
node -v
npm -v
newman -v
```

### Для тестирования коллекции postman включить настройку:
```
Settings:
    Automatically follow redirects
    Follow HTTP 3xx responses as redirects.
```