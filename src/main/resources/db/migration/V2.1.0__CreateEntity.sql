DROP TABLE IF EXISTS public.machine;
DROP TABLE IF EXISTS public.production;

CREATE TABLE factory (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    location TEXT,
    manager_name VARCHAR(255),
    contact_email VARCHAR(255),
    phone_number VARCHAR(20),
    status boolean
);

CREATE TABLE machine (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(255),
    production_capacity INTEGER,
    planned_production_time INTEGER,
    location TEXT,
    status VARCHAR(255),
    oee_percentage DOUBLE PRECISION,
    factory_id INTEGER,
    FOREIGN KEY (factory_id) REFERENCES factory(id)
);

CREATE TABLE production (
    id SERIAL PRIMARY KEY,
    production_time INTEGER,
    items_produced INTEGER,
    defective_items INTEGER,
    production_date BIGINT,
    shift VARCHAR(255),
    oee_percentage DOUBLE PRECISION,
    machine_id INTEGER,
    FOREIGN KEY (machine_id) REFERENCES machine(id)
);

