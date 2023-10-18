SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


--
-- Name: posts; Type: TABLE; Schema: public; Owner: "spring-user"
--

CREATE TABLE public.posts (
    id bigint NOT NULL,
    uuid character varying(32),
    content text,
    created_at timestamp(6) without time zone,
    image_url character varying(255),
    meta_description character varying(255),
    meta_title character varying(255),
    published boolean,
    published_at timestamp(6) without time zone,
    slug character varying(255),
    subtitle character varying(255),
    title character varying(255),
    updated_at timestamp(6) without time zone,
    author_id bigint
);


ALTER TABLE public.posts OWNER TO "spring-user";


--
-- Name: sequence_post; Type: SEQUENCE; Schema: public; Owner: "spring-user"
--

CREATE SEQUENCE public.sequence_post
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_post OWNER TO "spring-user";


--
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (id);


--
-- Name: tags; Type: TABLE; Schema: public; Owner: "spring-user"
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    uuid character varying(32),
    description character varying(255),
    name character varying(255),
    slug character varying(255)
);


ALTER TABLE public.tags OWNER TO "spring-user";


--
-- Name: sequence_tag; Type: SEQUENCE; Schema: public; Owner: "spring-user"
--

CREATE SEQUENCE public.sequence_tag
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_tag OWNER TO "spring-user";


--
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: tags uk_sn0d91hxu700qcw0n4pebp5vc; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT uk_sn0d91hxu700qcw0n4pebp5vc UNIQUE (slug);


--
-- Name: tags uk_t48xdq560gs3gap9g7jg36kgc; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT uk_t48xdq560gs3gap9g7jg36kgc UNIQUE (name);


--
-- Name: users; Type: TABLE; Schema: public; Owner: "spring-user"
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    uuid character varying(32),
    email character varying(255),
    name character varying(255),
    password character varying(255),
    roles character varying(255)[]
);


ALTER TABLE public.users OWNER TO "spring-user";


--
-- Name: sequence_user; Type: SEQUENCE; Schema: public; Owner: "spring-user"
--

CREATE SEQUENCE public.sequence_user
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sequence_user OWNER TO "spring-user";


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: posts_tags; Type: TABLE; Schema: public; Owner: "spring-user"
--

CREATE TABLE public.posts_tags (
    post_id bigint NOT NULL,
    tags_id bigint NOT NULL
);


ALTER TABLE public.posts_tags OWNER TO "spring-user";


--
-- Name: posts_tags posts_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.posts_tags
    ADD CONSTRAINT posts_tags_pkey PRIMARY KEY (post_id, tags_id);


--
-- Name: posts fk6xvn0811tkyo3nfjk2xvqx6ns; Type: FK CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT fk6xvn0811tkyo3nfjk2xvqx6ns FOREIGN KEY (author_id) REFERENCES public.users(id);


--
-- Name: posts_tags fk79lx4quime8ct09nbmmf6wuao; Type: FK CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.posts_tags
    ADD CONSTRAINT fk79lx4quime8ct09nbmmf6wuao FOREIGN KEY (tags_id) REFERENCES public.tags(id);


--
-- Name: posts_tags fkcreclgob71ibo58gsm6l5wp6; Type: FK CONSTRAINT; Schema: public; Owner: "spring-user"
--

ALTER TABLE ONLY public.posts_tags
    ADD CONSTRAINT fkcreclgob71ibo58gsm6l5wp6 FOREIGN KEY (post_id) REFERENCES public.posts(id);
