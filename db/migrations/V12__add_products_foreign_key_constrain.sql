ALTER TABLE products
ADD CONSTRAINT fk_status_id
FOREIGN KEY (status_id)
REFERENCES product_statuses (id);

ALTER TABLE products
ADD CONSTRAINT fk_model_id
FOREIGN KEY (model_id)
REFERENCES models (id);