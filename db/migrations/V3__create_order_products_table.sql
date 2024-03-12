CREATE TABLE order_products (
  id INTEGER PRIMARY KEY,
  order_id INTEGER NOT NULL,
  product_id INTEGER NOT NULL,
  price_id INTEGER NOT NULL
);