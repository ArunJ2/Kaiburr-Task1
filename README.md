# Kaiburr Task Manager - REST API

A Java Spring Boot REST API for managing task objects that represent shell commands. The application includes full CRUD operations, command execution, and MongoDB storage.

---

## 🚀 Quick Start

### Prerequisites

- **Java JDK 17+**
- **MongoDB 8.2**
- **Maven 3.9+**
- **Postman** (for testing)

---

### 🛠️ Installation & Run

1. **Start MongoDB**  
   To start MongoDB, use the following command:

   ```bash
   net start MongoDB
   ```

2. **Run Application**  
   Navigate to the project directory and start the Spring Boot application:

   ```bash
   cd task1-backend
   mvn spring-boot:run
   ```

3. **Verify Server**  
   The server should be running at:  
   [http://localhost:8081](http://localhost:8081)

---

## 📊 API Endpoints

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| GET    | `/api/tasks`                      | Get all tasks            |
| GET    | `/api/tasks?id={id}`              | Get task by ID           |
| PUT    | `/api/tasks`                      | Create/update task       |
| DELETE | `/api/tasks/{id}`                 | Delete task              |
| GET    | `/api/tasks/search?name={name}`   | Search tasks by name     |
| PUT    | `/api/tasks/{id}/execute`         | Execute task command     |

---

## 🧪 Testing with Postman

### ➕ Sample Task Creation

**PUT Request**  
URL: `http://localhost:8081/api/tasks`  
Content-Type: `application/json`

```json
{
  "id": "task-1",
  "name": "List Directory",
  "owner": "John Doe",
  "command": "dir"
}
```

---

### ✅ Sample Success Response

```json
{
  "message": "Task saved successfully",
  "task": {
    "id": "task-1",
    "name": "List Directory",
    "owner": "John Doe",
    "command": "dir",
    "createdAt": "2025-09-28T14:30:00Z"
  },
  "status": "200"
}
```

---

### ⚙️ Sample Command Execution Response

```json
{
  "message": "Command executed successfully",
  "execution": {
    "startTime": "2025-09-28T15:12:41Z",
    "endTime": "2025-09-28T15:12:42Z",
    "output": "Directory listing output..."
  },
  "status": "200"
}
```

---

## 🛡️ Features

- ✅ Complete CRUD Operations
- ✅ Shell Command Execution
- ✅ Command Safety Validation
- ✅ MongoDB Integration
- ✅ Search by Name
- ✅ Execution History Tracking

---

## 📁 Project Structure

```
task1-backend/
├── controller/TaskController.java
├── model/
│   ├── Task.java
│   └── TaskExecution.java
├── repository/TaskRepository.java
├── service/TaskService.java
└── resources/application.properties
```

---

## ⚙️ Configuration

**application.properties**

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/taskdb
server.port=8081
```

---

### Build & Run

```bash
mvn clean compile
mvn spring-boot:run
```

---

## 🎯 What's Implemented

- REST API with all required endpoints
- MongoDB database integration
- Command validation for safety
- Shell command execution with output capture
- Comprehensive error handling
- Ready for React frontend integration! 🚀

