ALTER TABLE prices
ADD CONSTRAINT fk_model_id
FOREIGN KEY (model_id)
REFERENCES models (id);