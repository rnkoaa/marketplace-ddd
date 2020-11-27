
```
{ _id = 1, aggregate_id = 1, version = 1, event = { … } }
{ _id = 2, aggregate_id = 1, version = 2, event = { … } }
{ _id = 3, aggregate_id = 1, version = 3, event = { … } }

In the new schema, it would look like this:

{ _id = 1, aggregate_id = 1, version = 1, events = [ { … }, { … }, { … }, { … } ] }
```

[Building Event sourcing with mongodb](https://blog.insiderattack.net/implementing-event-sourcing-and-cqrs-pattern-with-mongodb-66991e7b72be)