 # 🛠️ Installation and Running

---

## 🧩 Technologies and Dependencies Used


| Technology / Dependency | Description                                              | Required         |
|--------------------------|----------------------------------------------------------|------------------|
| Spring Boot              | Java-based web application framework                     | ✅                |
| Docker Compose           | Manages services in a containerized environment          | ✅                |
| PostgreSQL               | Database management system                               | ✅                |
| MinIO                    | S3-compatible object storage system                      | ✅                |
| Lombok                   | Automatically generates boilerplate code like getters    | ✅ (not working)  |
| Spring Data JPA          | ORM with Hibernate support                               | ✅                |
| Maven                    | Project build and dependency management tool             | ✅                |
| core-storage (Repsy)     | External core storage interface module                   | ✅                |
| file-system-storage      | File-system based storage implementation                 | ✅                |
| object-storage           | Object-storage (MinIO) based implementation              | ✅                |

---

## 🔧 1. Requirements

Make sure the following software is installed on your system:

- Java 17 or higher
- Maven 3.8 or higher
- Docker (required for MinIO)
- Git (to clone the project)
- Repsy account (to upload/download packages)

---

## 📥 2. Clone the Project
```sh
git clone https://github.com/sagdicberk/restApi-spring-storage.git
cd restApi
```

---

## ⚙️ 3. Configuration Updates

Edit only the following configuration properties. Avoid changing others unless you know what you're doing, as they are 
tightly integrated with the YAML configuration.

````properties
# file-system or object-storage
storage.strategy=object-storage

# Path for file-system
storage.file.base-path=PackageRepo
````

---

## 🚀 4. Build and Run the Application
````shell
mvn clean install
mvn spring-boot:run
````

---

# 🧪 5. API Guidelines
## 🎯 1. Upload Package
## Endpoint:

```http request
POST /{packageName}/{version}
Content-Type: multipart/form-data
```
## Description:

Uploads both the metadata file (.json) and the package file (.rep) for the specified package name and version.

## 📝 Form Parameters

| Field Name | Description                          | Required |
|------------|--------------------------------------|----------|
| meta       | Metadata file in `.json` format      | ✅        |
| package    | Package file in `.rep` (ZIP) format  | ✅        |

---
````bash
curl -X POST http://localhost:8080/my-library/1.0.0 \
  -F "meta=@meta.json" \
  -F "package=@library.rep"
````
Yanıt: 
````http
200 OK
Package uploaded successfully.
````

---

## 📥 2. Download Package
## Endpoint:

````http
GET /{packageName}/{version}/{fileName}
````
## Description:

Downloads the requested package file using package name, version, and file name.
## Example:


````http
GET /my-library/1.0.0/library.rep
````
## Response:

````http
200 OK
Content-Disposition: attachment; filename="library.rep"
Content-Type: application/octet-stream
````

---

# 🧱 Development Overview

## 1. Model, DTO, and Repository
- **PackageMeta** stores package details (name, version).
- **MetaDto** is used with ObjectMapper.
- A **JPA repository** handles basic CRUD operations.

## 2. Service Layer
- The **MetaService** is written following the abstract-concrete structure.
- A uniqueness check is performed before saving data.
- Includes **DTO ↔ Entity** conversions.

## 3. Storage Layer
- A common interface (**StorageService**) is defined.
- **File system storage**: stores files in the project directory.
- **Object Storage**: MinIO configuration (**MinioConfig**) and bucket management.

## 4. Strategy Design Pattern
- The **StorageStrategyContext** class determines the appropriate strategy based on the configuration file.
- Managed by the Spring **@Service** annotation.

## 5. PackageService and API
- Two main endpoints: upload and download.
- **StrategyContext** is used to choose the storage type.
- Tested using Postman.

## 6. Module Separation
- Three independent modules:
    - **core-storage**: interface and common structures.
    - **file-system-storage**: file system implementation.
    - **object-storage**: MinIO implementation.
- **pom.xml** files are updated to reflect the modular structure.

## 7. Maven Library Structure
- All modules are built independently and can be used individually.
- The main application depends on these modules.

## 8. Publishing to Repsy
- Each module is published to the **Repsy Maven Repository**.
- For testing, the local repository was deleted and packages were pulled from Repsy — successfully worked.

