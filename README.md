# **BestStore Application**

BestStore is a Spring Boot application designed for managing an online store's products. It includes features like adding, editing, viewing, deleting products, and uploading associated images.

---

## **Features**

- **Product Management**: Perform CRUD (Create, Read, Update, Delete) operations on products.
- **Image Uploads**: Upload and manage product images.
- **Validation**: Ensures data integrity with server-side validation.
- **Sorting**: Displays products in descending order by their IDs.
- **Error Logging**: Handles errors gracefully with detailed logging.

---

## **Tech Stack**

- **Backend**: Java, Spring Boot
- **Database**: (Your choice, e.g., MySQL, PostgreSQL)
- **Frontend Templates**: Thymeleaf
- **File Storage**: Local file system for images
- **Build Tool**: Maven

---

## **Setup Instructions**

### Prerequisites

1. Java 17 or later
2. Maven 3.6 or later
3. Database server (e.g., MySQL, PostgreSQL)
4. IDE (e.g., IntelliJ IDEA, Eclipse)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/beststore.git
   cd beststore
   ```

2. Configure `application.properties`:

   Update the database connection details:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/beststore
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   spring.jpa.hibernate.ddl-auto=update
   ```

   Specify the directory for image uploads:
   ```properties
   file.upload.dir=public/images/
   ```

3. Build the application:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

5. Access the application in your browser:
   ```
   http://localhost:8080/products
   ```

---

## **Endpoints**

### **Product Management**

| HTTP Method | Endpoint                 | Description                   |
|-------------|--------------------------|-------------------------------|
| GET         | `/products`             | Display all products          |
| GET         | `/products/create`      | Show product creation form    |
| POST        | `/products/create`      | Add a new product             |
| GET         | `/products/edit?id={id}`| Show edit product form        |
| POST        | `/products/edit?id={id}`| Update product details        |
| GET         | `/products/delete?id={id}` | Delete a product           |

---

## **Folder Structure**

```
src/
├── main/
│   ├── java/com/storeTool/BestStore/
│   │   ├── controllers/          # Handles HTTP requests
│   │   ├── services/             # Business logic
│   │   ├── dao/                  # Repository interfaces
│   │   ├── models/               # Product and ProductDto classes
│   │   ├── converters/           # DTO and entity converters
│   └── resources/
│       ├── static/               # CSS, JS, images
│       ├── templates/            # Thymeleaf templates
│       └── application.properties # Configuration
└── test/                         # Unit and integration tests
```

---

## **Image Uploads**

- Images are stored in the directory specified in `application.properties` (default: `public/images/`).
- Files are renamed with a timestamp to ensure uniqueness.

