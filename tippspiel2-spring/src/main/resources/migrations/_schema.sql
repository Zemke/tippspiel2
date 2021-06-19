--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3 (Ubuntu 13.3-1.pgdg20.04+1)
-- Dumped by pg_dump version 13.3

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

SET default_table_access_method = heap;

--
-- Name: bet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bet (
    id bigint NOT NULL,
    goals_away_team_bet integer NOT NULL,
    goals_home_team_bet integer NOT NULL,
    modified timestamp without time zone,
    betting_game_id bigint NOT NULL,
    fixture_id bigint NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: betting_game; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.betting_game (
    id bigint NOT NULL,
    created timestamp without time zone,
    invitation_token character varying(255),
    name character varying(255),
    competition_id bigint NOT NULL
);


--
-- Name: champion_bet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.champion_bet (
    id bigint NOT NULL,
    modified timestamp without time zone,
    betting_game_id bigint NOT NULL,
    team_id bigint NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: competition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.competition (
    id bigint NOT NULL,
    caption character varying(255),
    champion_bet_allowed boolean NOT NULL,
    current boolean NOT NULL,
    current_matchday integer NOT NULL,
    last_updated timestamp without time zone,
    league character varying(255),
    number_of_available_seasons integer NOT NULL,
    year integer NOT NULL,
    champion_id bigint
);


--
-- Name: fixture; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fixture (
    id bigint NOT NULL,
    date timestamp without time zone,
    goals_away_team integer,
    goals_home_team integer,
    manual boolean NOT NULL,
    matchday integer NOT NULL,
    status character varying(255),
    away_team_id bigint,
    competition_id bigint,
    home_team_id bigint
);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    name character varying(255)
);


--
-- Name: standing; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.standing (
    id bigint NOT NULL,
    exact_bets integer NOT NULL,
    goal_difference_bets integer NOT NULL,
    missed_bets integer NOT NULL,
    points integer NOT NULL,
    winner_bets integer NOT NULL,
    wrong_bets integer NOT NULL,
    betting_game_id bigint NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: team; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.team (
    id bigint NOT NULL,
    name character varying(255),
    competition_id bigint NOT NULL
);


--
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    last_password_reset timestamp without time zone,
    password character varying(255)
);


--
-- Name: user_betting_games; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_betting_games (
    user_id bigint NOT NULL,
    betting_games_id bigint NOT NULL
);


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    roles_id bigint NOT NULL
);


--
-- Name: bet bet_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bet
    ADD CONSTRAINT bet_pkey PRIMARY KEY (id);


--
-- Name: betting_game betting_game_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.betting_game
    ADD CONSTRAINT betting_game_pkey PRIMARY KEY (id);


--
-- Name: champion_bet champion_bet_multiple_bets; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.champion_bet
    ADD CONSTRAINT champion_bet_multiple_bets UNIQUE (user_id, betting_game_id);


--
-- Name: champion_bet champion_bet_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.champion_bet
    ADD CONSTRAINT champion_bet_pkey PRIMARY KEY (id);


--
-- Name: competition competition_caption_league_year; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competition
    ADD CONSTRAINT competition_caption_league_year UNIQUE (caption, league, year);


--
-- Name: competition competition_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competition
    ADD CONSTRAINT competition_pkey PRIMARY KEY (id);


--
-- Name: fixture fixture_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fixture
    ADD CONSTRAINT fixture_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: standing standing_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.standing
    ADD CONSTRAINT standing_pkey PRIMARY KEY (id);


--
-- Name: team team_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: betting_game uk_5nnn4sl9fan619nsws2mv8o0l; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.betting_game
    ADD CONSTRAINT uk_5nnn4sl9fan619nsws2mv8o0l UNIQUE (name);


--
-- Name: betting_game uk_bl0lopsh4a8hfjya69nlwb88y; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.betting_game
    ADD CONSTRAINT uk_bl0lopsh4a8hfjya69nlwb88y UNIQUE (invitation_token);


--
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: fixture fk2o7enkd358sqx3dtuyt80ua5e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fixture
    ADD CONSTRAINT fk2o7enkd358sqx3dtuyt80ua5e FOREIGN KEY (away_team_id) REFERENCES public.team(id);


--
-- Name: standing fk3iq8re15dlocy910778blvk1r; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.standing
    ADD CONSTRAINT fk3iq8re15dlocy910778blvk1r FOREIGN KEY (betting_game_id) REFERENCES public.betting_game(id);


--
-- Name: champion_bet fk3pqp0nhx0cvdc20c5lqdb1uuw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.champion_bet
    ADD CONSTRAINT fk3pqp0nhx0cvdc20c5lqdb1uuw FOREIGN KEY (betting_game_id) REFERENCES public.betting_game(id);


--
-- Name: user_roles fk40cm955hgg5oxf1oax8mqw0c4; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fk40cm955hgg5oxf1oax8mqw0c4 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: bet fk5pt6039k52ehefmlwuf5rmysx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bet
    ADD CONSTRAINT fk5pt6039k52ehefmlwuf5rmysx FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: champion_bet fk6kgdb0ig66s5kom8044d6eh9c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.champion_bet
    ADD CONSTRAINT fk6kgdb0ig66s5kom8044d6eh9c FOREIGN KEY (team_id) REFERENCES public.team(id);


--
-- Name: fixture fkbhduiued9v9j0b3pqvbk3wye; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fixture
    ADD CONSTRAINT fkbhduiued9v9j0b3pqvbk3wye FOREIGN KEY (home_team_id) REFERENCES public.team(id);


--
-- Name: bet fkeplah94r5t6du01vix60a2hem; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bet
    ADD CONSTRAINT fkeplah94r5t6du01vix60a2hem FOREIGN KEY (betting_game_id) REFERENCES public.betting_game(id);


--
-- Name: competition fkfcyj2x9fqemjfswhqo9siu7sy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.competition
    ADD CONSTRAINT fkfcyj2x9fqemjfswhqo9siu7sy FOREIGN KEY (champion_id) REFERENCES public.team(id);


--
-- Name: user_betting_games fkh8pnqbx0gbl7suskr3n39uxy3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_betting_games
    ADD CONSTRAINT fkh8pnqbx0gbl7suskr3n39uxy3 FOREIGN KEY (betting_games_id) REFERENCES public.betting_game(id);


--
-- Name: betting_game fkhr6guynlblsyw9xc38d89kclv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.betting_game
    ADD CONSTRAINT fkhr6guynlblsyw9xc38d89kclv FOREIGN KEY (competition_id) REFERENCES public.competition(id);


--
-- Name: user_betting_games fkhvwkko95ndrfiuu9sfi8r09uc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_betting_games
    ADD CONSTRAINT fkhvwkko95ndrfiuu9sfi8r09uc FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: fixture fkjmvmwakhw0k93g4stb0emw5vq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fixture
    ADD CONSTRAINT fkjmvmwakhw0k93g4stb0emw5vq FOREIGN KEY (competition_id) REFERENCES public.competition(id);


--
-- Name: standing fkjugi4b3wgsvi7c3c9jcvowg22; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.standing
    ADD CONSTRAINT fkjugi4b3wgsvi7c3c9jcvowg22 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: bet fkkocae3ov51g8uadv3jc1hn9en; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bet
    ADD CONSTRAINT fkkocae3ov51g8uadv3jc1hn9en FOREIGN KEY (fixture_id) REFERENCES public.fixture(id);


--
-- Name: champion_bet fklly53y9331t43ednwfurx77rb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.champion_bet
    ADD CONSTRAINT fklly53y9331t43ednwfurx77rb FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: team fkqhhapgh63c9yjo4tc0uf6ynt1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.team
    ADD CONSTRAINT fkqhhapgh63c9yjo4tc0uf6ynt1 FOREIGN KEY (competition_id) REFERENCES public.competition(id);


--
-- Name: user_roles fksoyrbfa9510yyn3n9as9pfcsx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fksoyrbfa9510yyn3n9as9pfcsx FOREIGN KEY (roles_id) REFERENCES public.role(id);


--
-- PostgreSQL database dump complete
--

