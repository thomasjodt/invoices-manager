<div align="center">
  <h1>Invoices Manager</h1>
  A project for manage invoices and payments.
</div>
<br>
<br>

## Getting Started
1. Clone this repository:

  ```bash
  git clone https://github.com/thomasjodt/invoices-manager.git
  ```

2. Download Apache <a href="https://tomcat.apache.org/download-10.cgi" target="_blank">Tomcat 10</a>
3. Download mysql-connector-j driver and copy to `/lib` on the Tomcat directory.
4. Edit the `ConnectionManager.java` propperties according to your credentials.
5. Create the Schema and the tables.
   
   ```SQL
   CREATE SCHEMA invoice_app;
   USE invoice_app;

   CREATE TABLE vendors (
       id INT NOT NULL AUTO_INCREMENT,
       name VARCHAR(50),
       CONSTRAINT vendor_pk PRIMARY KEY (id)
   );
    
   CREATE TABLE invoices (
       id INT NOT NULL AUTO_INCREMENT,
       invoice_number VARCHAR(13),
       emission_date DATE,
       due_date DATE,
       amount DECIMAL(10,2),
       vendor_id INT NOT NULL,
       CONSTRAINT invoice_pk PRIMARY KEY (id),
       FOREIGN KEY (vendor_id) REFERENCES vendors(id)
   );
    
   CREATE TABLE payments (
       id INT NOT NULL AUTO_INCREMENT,
       amount DECIMAL(10,2),
       payment_date DATE,
       invoice_id INT NOT NULL,
       CONSTRAINT payment_pk PRIMARY KEY (id),
       FOREIGN KEY (invoice_id) REFERENCES invoices(id)
   );
   ```
6. Populate the database with some values.
7. Run the application.

### DOCS

**Paths:**
- `/invoices` - `POST`, `GET`
- `/invoices/:id` - `GET`, `DELETE`
- `vendors` - `POST`, `GET`, `DELETE`
- `vendors/:id` - `GET`, `DELETE`
- `/payments` - `POST`, `GET`, `DELETE`
- `/payments/:id` - `GET`, `DELETE`
