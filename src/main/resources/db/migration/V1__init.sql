-- Flyway migration: initial schema
CREATE TABLE produit (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(200) NOT NULL UNIQUE,
  categorie VARCHAR(100),
  unite VARCHAR(50)
);

CREATE TABLE marche (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(200) NOT NULL UNIQUE,
  commune VARCHAR(100),
  localisation VARCHAR(255)
);

CREATE TABLE utilisateur (
  id BIGSERIAL PRIMARY KEY,
  nom VARCHAR(200),
  email VARCHAR(200) NOT NULL UNIQUE,
  mot_de_passe VARCHAR(200) NOT NULL,
  role VARCHAR(20)
);

CREATE TABLE prix_produit (
  id BIGSERIAL PRIMARY KEY,
  produit_id BIGINT NOT NULL REFERENCES produit(id) ON DELETE CASCADE,
  marche_id BIGINT NOT NULL REFERENCES marche(id) ON DELETE CASCADE,
  prix NUMERIC(14,2) NOT NULL,
  date DATE NOT NULL
);

CREATE INDEX idx_prix_produit_produit_date ON prix_produit(produit_id, date);

-- Optionnel: sample data (small)
INSERT INTO produit(nom,categorie,unite) VALUES ('Riz','Céréales','kg') ON CONFLICT DO NOTHING;
INSERT INTO marche(nom,commune,localisation) VALUES ('Marché Treichville','Treichville','centre') ON CONFLICT DO NOTHING;
