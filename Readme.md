# Local Setup

## Prerequisites

Step 1: Prepare Your Environment
Install Required Tools:

JDK: Install Java 17.
Gradle: Install Gradle (or use the Gradle Wrapper included in the project).
Database Configuration:<BR>
MYSQL: Install MySQL.

Step 2: Create a database for the project (e.g., buy_recipes).
Run the necessary SQL scripts to set up the database tables:

```CREATE TABLE products (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(255) NOT NULL,
type ENUM('PRODUCT', 'RECIPE') NOT NULL,
price_in_cents INT NOT NULL
);

CREATE TABLE carts (
id INT AUTO_INCREMENT PRIMARY KEY,
total_in_cents INT NOT NULL
);

CREATE TABLE cart_items (
id INT AUTO_INCREMENT PRIMARY KEY,
cart_id INT NOT NULL,
product_id INT NOT NULL,
FOREIGN KEY (cart_id) REFERENCES carts(id),
FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE product_recipes (
id INT AUTO_INCREMENT PRIMARY KEY,
recipe_id INT NOT NULL,
product_id INT NOT NULL,
FOREIGN KEY (recipe_id) REFERENCES products(id),
FOREIGN KEY (product_id) REFERENCES products(id)
);

```
Step 3: Update application.properties or application.yml: Edit the src/main/resources/application.properties file to include your database configuration:
```
spring.datasource.url=jdbc:mysql://localhost:3306/buy_recipes
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Step 4: Clone the Repository
Clone the project repository:
```
git clone https://github.com/Abhishek-Dev09/Assignment.git
cd Assignment

```
Step 5: Build the Project
Build Using Gradle Wrapper: Use the Gradle wrapper script included in the project:

```
./gradlew build
```
This will:

Compile the project.
Run tests.
Package the application into a runnable JAR file.
Verify the Build: Ensure the build completes successfully, and a build/libs/ directory is created with the generated JAR file.

Step 6: Run the Project
Run the Spring Boot Application: Use the Gradle wrapper to run the application:

```
./gradlew bootRun
```
Verify the Application: By default, the application will be available at http://localhost:8080.

# Assumptions- 

#### Existing Functionality:

- The current e-commerce platform supports purchasing simple products only.
The functionality to manage and purchase individual products is already implemented and does not require changes.
#### New Requirement:

- Extend the platform to allow users to purchase entire recipes.
A recipe is defined as a collection of products (ingredients) that are already available in the system.
#### Cart Functionality:

- The platform already has cart functionality that supports adding, removing, and calculating prices for products.
This functionality will be extended to support recipes in addition to individual products.
Recipe Details:

- Each recipe is treated as a special type of product.
Recipes include multiple products, and the system should compute the price based on their ingredients.
Database Enhancements:

- A new relationship between products and recipes is established through the product_recipes table.
This table maps individual products (ingredients) to recipes.

## Approach to solve the problem:

### **1\. Key Changes**

*   **Added a type column to the products table**:

    *   Values: PRODUCT (for simple products) and RECIPE (for recipes).

    *   Differentiates between regular products and recipes while reusing the same table for both.

*   **Introduced a mapping table (product\_recipes)**:

    *   Establishes a many-to-many relationship between recipes and their ingredients (products).

    *   Each row in this table maps a recipe to one of its constituent products.


### **2\. Comparison of Approaches**

#### **Reusing the Products Table**

##### **Advantages (Performance)**:

1.  **Simpler Queries**:

    *   ```SELECT * FROM products WHERE id IN (...);```

    *   Avoids additional joins for basic queries.

2.  **Reduced Disk I/O**:

    *   No additional table lookups required for recipes.

3.  **Indexing Simplicity**:

    *   Only the ```products``` table requires indexing, simplifying maintenance.


##### **Disadvantages (Performance)**:

1.  **Table Bloat**:

    *   Over time, the products table may grow large, slowing down queries that operate on specific subsets (e.g., PRODUCT only).

2.  **Complex Recipe Queries**:

    *   ``` SELECT p.* FROM product_recipes pr JOIN products p ON pr.product_id = p.idWHERE pr.recipe_id = :recipeId;```

3.  **Mixed Data Overhead**:

    *   Storing distinct entities (```PRODUCT``` and ```RECIPE```) in a single table can lead to complex queries and potential confusion.


#### **Using a Separate Recipes Table**

##### **Advantages (Performance)**:

1.  **Focused Tables**:

    *   Queries specific to recipes or products operate on smaller, dedicated tables.

2.  **Scalability**:

    *   Adding recipe-specific features (e.g., preparation\_time) doesn’t bloat unrelated tables.

3.  **Efficient Recipe Queries**:

```
    * SELECT * FROM recipes;
    * SELECT * FROM recipe_products WHERE recipe_id = :recipeId;
```

##### **Disadvantages (Performance)**:

1.  **Join Overhead**:

    *   ``` SELECT * FROM cart_items ci LEFT JOIN products p ON ci.product_id = p.id LEFT JOIN recipes r ON ci.recipe_id = r.id;```

2.  **Index Maintenance**:

    *   Indexes across multiple tables require more database overhead.


### **3\. Performance Comparison**

| **Scenario**               | **Reusing Products Table** | **Separate Recipes Table** |
| -------------------------- | -------------------------- | -------------------------- |
| **Querying a Cart**        | Faster (single table)      | Slightly slower (joins)    |
| **Fetching All Recipes**   | Slower (filtering needed)  | Faster (smaller table)     |
| **Adding Recipe Features** | Slower (affects all rows)  | Cleaner, more performant   |
### **4\. Final Decision**

Based on the assumptions:

*   Recipes are **not expected to grow rapidly**.

*   Recipes currently have **no heavy, recipe-specific attributes** beyond the mapping of products.


The **single-table approach with a product\_recipes mapping table** is chosen for simplicity and performance.

### **5\. Database Design**

#### **Products Table**

| Field          | Type                      | Null | Key | Extra          |
| -------------- | ------------------------- | ---- | --- | -------------- |
| id             | INT                       | NO   | PRI | AUTO_INCREMENT |
| name           | VARCHAR(255)              | NO   |     |                |
| type           | ENUM('PRODUCT', 'RECIPE') | NO   |     |                |
| price_in_cents | INT                       | NO   |     |                |

#### **Product\_Recipes Table**


| Field      | Type | Null | Key | Extra          |
| ---------- | ---- | ---- | --- | -------------- |
| id         | INT  | NO   | PRI | AUTO_INCREMENT |
| recipe_id  | INT  | NO   | MUL |                |
| product_id | INT  | NO   | MUL |                |
This approach strikes a balance between performance and scalability while meeting the current requirements.

### API docs:
Here are the APIs for both \`CartController\` and \`RecipeController\`:

Here are the APIs for both `CartController` and `RecipeController`:


### CartController APIs


- **Get Cart by ID**
    - **Endpoint**: `GET /carts/{id}`
    - **Description**: Retrieves the cart details by cart ID.


- **Add Recipe to Cart**
    - **Endpoint**: `POST /carts/{cartId}/recipes/{recipeId}`
    - **Description**: Adds a recipe to the cart.


- **Add Product to Cart**
    - **Endpoint**: `POST /carts/{cartId}/products/{productId}`
    - **Description**: Adds a product to the cart.


- **Remove Recipe from Cart**
    - **Endpoint**: `DELETE /carts/{cartId}/recipes/{recipeId}`
    - **Description**: Removes a recipe from the cart.


- **Remove Product from Cart**
    - **Endpoint**: `DELETE /carts/{cartId}/products/{productId}`
    - **Description**: Removes a product from the cart.


### RecipeController APIs


- **Get All Recipes**
    - **Endpoint**: `GET /recipe`
    - **Description**: Retrieves all recipes.


- **Get Recipe by ID**
    - **Endpoint**: `GET /recipe/{id}`
    - **Description**: Retrieves the recipe details by recipe ID.
