sequenceDiagram
    participant User
    participant Billing Service
    participant Order Service
    participant Message Broker
    participant Notification service
    User->>+Billing Service: POST /api/register {NewAccountDto}
    Billing Service-->>-User: 201 Created {AccountDto}
    User->>+Billing Service: PUT /api/refill/{login} {AccountDto}
    Billing Service-->>-User: 202 Accepted {AccountDto}
    User->>+Order Service: POST /api/create-order {NewOrderDto}
    Note right of Billing Service: Placing an order
    par Formation of an order
    Order Service->>+Billing Service: GET /api/account/{login}
    Note right of Billing Service: Checking money into accounts
    Billing Service-->>-Order Service: 200 OK {AccountDto}
    Order Service->>+Billing Service: PUT /api/refill/{login} {AccountDto}
    Note right of Billing Service: Debiting money from an account
    Billing Service-->>-Order Service: 202 Accepted {AccountDto}
    end
    Order Service->>+Message Broker: publish
    Message Broker-->>-Notification service: cunsume
    activate Notification service
    Notification service->>Notification service: create notification
    Note right of Notification service: Receiving a message from a queue, saving and sending an email
    deactivate Notification service
    Order Service-->>-User: 201 Created {OrderDto}