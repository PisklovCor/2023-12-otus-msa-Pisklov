sequenceDiagram
participant User
participant Gateway
participant Message Broker
participant Billing Service
participant Order Service
participant Message Broker
participant Notification service
    User->>+Gateway: POST /api/register {NewAccountDto}
    Gateway->>-Message Broker: publish
    activate Message Broker
    Note right of Message Broker: User registration
    Message Broker-->>Billing Service: consume
    deactivate Message Broker
    activate Billing Service
    Note right of Message Broker: User is registered
    Billing Service->>Message Broker: publish
    deactivate Billing Service
    activate Message Broker
    Message Broker-->>Gateway: consume
    deactivate Message Broker
    activate Gateway
    Gateway-->>User: 201 Created {AccountDto}
    deactivate Gateway
    User->>+Gateway: PUT /api/refill/{login} {AccountDto}
    Gateway->>-Message Broker: publish
    activate Message Broker
    Note right of Message Broker: Top up user account
    Message Broker-->>Billing Service: consume
    deactivate Message Broker
    activate Billing Service
    Note right of Message Broker: Account replenished
    Billing Service->>Message Broker: publish
    deactivate Billing Service
    activate Message Broker
    Message Broker-->>Gateway: consume
    deactivate Message Broker
    activate Gateway
    Gateway-->>User: 202 Accepted {AccountDto}
    deactivate Gateway
    User->>+Gateway: POST /api/create-order {NewOrderDto}
    Gateway->>-Message Broker: publish
    activate Message Broker
        Message Broker-->>+Order Service: consume
        Note right of Message Broker: Placing an order
        Order Service->>-Message Broker: publish
        Message Broker-->>+Billing Service: consume
        Note right of Message Broker: Checking money into accounts
        Billing Service->>-Message Broker: publish
        Message Broker-->>+Order Service: consume
        Order Service->>-Message Broker: publish
        Message Broker-->>+Billing Service: consume
        Note right of Message Broker: Debiting money from an account
        Billing Service->>-Message Broker: publish
        Message Broker-->>+Order Service: consume
        Note right of Message Broker: Creating an order after debiting money
        Order Service->>-Message Broker: publish
        Message Broker-->>Gateway: consume
        activate Gateway
        Gateway-->>User: 201 Created {OrderDto}
        deactivate Gateway
        Message Broker-->>Notification service: consume
    deactivate Message Broker
    activate Notification service
    Note right of Message Broker: Create notification
    Notification service->>Notification service: create notification
    Note right of Notification service: Receiving a message from a queue, saving and sending an email
    deactivate Notification service