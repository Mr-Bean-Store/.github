ALTER TABLE order_products
ADD CONSTRAINT fk_order_id
FOREIGN KEY (order_id)
REFERENCES orders (id);

ALTER TABLE order_products
ADD CONSTRAINT fk_product_id
FOREIGN KEY (product_id)
REFERENCES products (id);

ALTER TABLE order_products
ADD CONSTRAINT fk_price_id
FOREIGN KEY (price_id)
REFERENCES prices (id);