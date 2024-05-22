sequenceDiagram
    participant User
    participant Billing Service
    participant Order Service
    participant Message Broker
    participant Notification service
    User->>+Billing Service: POST /api/register
    Billing Service-->>-User: Response
    User->>+Billing Service: PUT /api/refill/{login}
    Billing Service-->>-User: Response
    User->>+Order Service: POST /api/create-order
    Note over Order Service,User: Placing an order
    par Formation of an order
    Order Service->>+Billing Service: GET /api/account/{login}
    Note over Order Service,Billing Service: Checking money into accounts
    Billing Service-->>-Order Service: Response
    Order Service->>+Billing Service: PUT /api/refill/{login}
    Note over Order Service,Billing Service: Debiting money from an account
    Billing Service-->>-Order Service: Response
    Order Service->>+Message Broker: publish
    end
    Message Broker-->>-Notification service: cunsume
    activate Notification service
    Notification service->>Notification service: create notification
    deactivate Notification service
    Order Service-->>-User: Response