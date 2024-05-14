## newman

### Запуск тестов с
```shell
newman run /postman/hw06_Collection.postman_collection.json -e postman/hw06_enviroment.postman_environment.json
```

### Настройка windows
```
- Открываем терминал PowerShell от админа.
- Вставляем и запускаем - Set-ExecutionPolicy RemoteSigned
- На вопрос отвечаем - A

- Возврат - Set-ExecutionPolicy Default
```

### Для тестирования коллекции postman включить настройку:
```
Settings:
    Automatically follow redirects
    Follow HTTP 3xx responses as redirects.
```