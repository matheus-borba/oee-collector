DROP TABLE IF EXISTS public.audition_request_response CASCADE;

CREATE TABLE audition_request_response (
    id serial4 NOT NULL,
    request text NOT NULL,
    description VARCHAR(255) NULL,
    factory_id int4 NULL,
    request_method VARCHAR(255),
    request_url VARCHAR(255),
    response text NOT NULL,
    response_status int4 NULL,
    took_seconds VARCHAR(255) NULL,
    created_at timestamp NULL,
    CONSTRAINT audition_request_response_pkey PRIMARY KEY (id),
    CONSTRAINT audition_request_response_request_method_check CHECK (((request_method)::text = ANY (ARRAY[('DELETE'::character varying)::text, ('GET'::character varying)::text, ('OPTIONS'::character varying)::text, ('PATCH'::character varying)::text, ('POST'::character varying)::text, ('PUT'::character varying)::text])))
);