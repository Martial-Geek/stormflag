# StormFlag

StormFlag is a lightweight distributed feature flag service built in Java.

## Current Architecture

### Single Node Features

- REST API for feature flags
- Thread-safe in-memory state store
- Clean service layering

### Multi-Node Simulation

- Each node has:
  - Unique node ID
  - Peer awareness
- Nodes run on separate ports
- Inter-node HTTP communication implemented

### API Structure

Public API:

- GET /flags
- GET /flags/{key}
- POST /flags

Cluster Debug API:

- GET /cluster/node-info
- GET /cluster/ping

Internal Node API:

- GET /internal/ping

## Running 3 Nodes Locally

Example:

Node 1:

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8081 --storm.node-id=node-1 --storm.peers=http://localhost:8082,http://localhost:8083
```

Node 2:

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8082 --storm.node-id=node-2 --storm.peers=http://localhost:8081,http://localhost:8083
```

Node 3:

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8083 --storm.node-id=node-3 --storm.peers=http://localhost:8081,http://localhost:8082
```

## Next Steps

- Automatic heartbeat loop
- Leader election (Raft phase 1)
- Log replication
- Distributed consistency
