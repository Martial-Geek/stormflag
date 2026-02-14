# StormFlag

StormFlag is a distributed feature flag service built in Java using a custom implementation of the Raft consensus algorithm.

This project focuses on building distributed systems fundamentals from scratch rather than relying on existing libraries.

---

## 🚀 Current Capabilities

### Multi-Node Cluster Simulation

- Run 3 independent nodes locally
- Each node has unique identity and peer awareness
- Inter-node HTTP communication

### Raft Leader Election

- Randomized election timeouts (3–6 seconds)
- Term tracking
- One vote per term
- Majority-based leader election
- Leader heartbeat mechanism
- Automatic leader failover on crash

### Fault Tolerance

- Kill the leader → cluster elects a new one
- Followers remain stable under active leader
- No split-brain under normal conditions

---

## 🧠 Architecture Overview

Each node maintains:

- Node state (FOLLOWER / CANDIDATE / LEADER)
- Current term
- Voted-for record
- Election timeout
- Heartbeat scheduling

Nodes communicate via internal REST endpoints:

```
POST /internal/request-vote
POST /internal/heartbeat
```

Leader election is fully autonomous.

---

## 🛠 Running a 3-Node Cluster Locally

Build once:

```
./mvnw clean package
```

Run nodes:

### Node 1

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8081 --storm.node-id=node-1 --storm.peers=http://localhost:8082,http://localhost:8083
```

### Node 2

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8082 --storm.node-id=node-2 --storm.peers=http://localhost:8081,http://localhost:8083
```

### Node 3

```
java -jar target/stormflag-0.0.1-SNAPSHOT.jar --server.port=8083 --storm.node-id=node-3 --storm.peers=http://localhost:8081,http://localhost:8082
```

Kill the leader process to observe automatic re-election.

---

## 📌 Roadmap

- Log replication
- Majority commit rule
- Distributed feature flag state machine
- Persistent storage
- Network partition handling

---

## 🎯 Purpose of This Project

This project is designed to deeply understand:

- Distributed consensus
- Failure detection
- Leader election
- Term synchronization
- Fault-tolerant system design
