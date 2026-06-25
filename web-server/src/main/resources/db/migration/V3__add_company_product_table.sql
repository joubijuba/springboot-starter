CREATE TABLE companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL
);

ALTER TABLE users ADD company_id UUID NOT NULL;
ALTER TABLE users ADD CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES companies(id);
 
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    company_id UUID NOT NULL, 
    Foreign Key (company_id) REFERENCES companies(id)
);
