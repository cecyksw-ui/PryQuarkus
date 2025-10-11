-- Sample data for products table
INSERT INTO products (id, name, description, price, stock) VALUES (1, 'Laptop', 'High performance laptop', 1200.00, 10);
INSERT INTO products (id, name, description, price, stock) VALUES (2, 'Mouse', 'Wireless mouse', 25.50, 50);
INSERT INTO products (id, name, description, price, stock) VALUES (3, 'Keyboard', 'Mechanical keyboard', 85.00, 30);
INSERT INTO products (id, name, description, price, stock) VALUES (4, 'Monitor', '27 inch 4K monitor', 350.00, 15);
INSERT INTO products (id, name, description, price, stock) VALUES (5, 'Headphones', 'Noise cancelling headphones', 150.00, 25);

-- Set sequence to start after initial data
ALTER SEQUENCE products_seq RESTART WITH 6;
