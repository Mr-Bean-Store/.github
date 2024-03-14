ALTER TABLE orders
ADD CONSTRAINT fk_customer_id
FOREIGN KEY (customer_id)
REFERENCES customers (id);

ALTER TABLE orders
ADD CONSTRAINT fk_delivery_address_id
FOREIGN KEY (delivery_addess_id)
REFERENCES addresses (id);