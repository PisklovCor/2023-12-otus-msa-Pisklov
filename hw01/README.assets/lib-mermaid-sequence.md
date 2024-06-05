sequenceDiagram
participant User
participant Gateway
participant Account service
participant Library service
participant Order service
participant Message Broker
participant Notification service

    User->>+Gateway: POST /api/account/register {AccountDto}
    Note over User,Gateway: Регистрация пользователя
    Gateway->>+Account service: POST /api/account/register {AccountDto}
    Account service-->>-Gateway: 201 Created {AccountDto}
    Gateway-->>-User: 201 Created {AccountDto}

    User->>+Gateway: POST /api/account/login {AccountDto}
    Note over User,Gateway: Вход пользователя
    Gateway->>+Account service: POST /api/account/login {AccountDto}
    Account service-->>-Gateway: 202 Accepted {AccountDto}
    Gateway-->>-User: 202 Accepted {AccountDto}

    User->>+Gateway: GET /api/book {AccountDto}
    Note over User,Gateway: Получение списка книг
    Gateway->>+Library service: POST /api/book {AccountDto}
    Library service-->>-Gateway: 200 OK {[BookDto, ...]}
    Gateway-->>-User: 200 OK {[BookDto, ...]}

    User->>+Gateway: POST /api/book/{bookId}
    Note over User,Gateway: Получить книгу из библиотеки
    Gateway->>+Library service: POST /api/book/{bookId}
    Library service->>+Message Broker: publish
    Message Broker->>-Notification service: consume
    activate Notification service
    Library service-->>-Gateway: 202 Accepted {BookDto}
    Gateway-->>-User: 202 Accepted {BookDto}
    Notification service->>Notification service: create notification
    deactivate Notification service

    User->>+Gateway: POST /api/book/add {BookDto}
    Note over User,Gateway: Добавить свою книгу в библиотеку
    Gateway->>+Library service: POST /api/book/add {BookDto}
    Library service->>+Message Broker: publish
    Message Broker->>-Notification service: consume
    activate Notification service
    Library service-->>-Gateway: 201 Created {BookDto}
    Gateway-->>-User: 201 Created {BookDto}
    Notification service->>Notification service: create notification
    deactivate Notification service

    User->>+Gateway: POST /api/book/bid {BookDto}
    Note over User,Gateway: Оставить запрос на книгу 
    Gateway->>+Library service: POST /api/book/bid {BookDto}
    Library service->>+Message Broker: publish
    Message Broker->>-Notification service: consume
    activate Notification service
    Library service-->>-Gateway: 202 Accepted {BookDto}
    Gateway-->>-User: 202 Accepted {BookDto}
    Notification service->>Notification service: create notification
    deactivate Notification service