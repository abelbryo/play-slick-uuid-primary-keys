create schema shard_1;
create sequence shard_1.global_id_sequence;
CREATE OR REPLACE FUNCTION shard_1.id_generator(OUT result bigint) AS $$
DECLARE
    our_epoch bigint := 1314220021721;
    seq_id bigint;
    now_millis bigint;
    -- the id of this DB shard, must be set for each
    -- schema shard you have - you could pass this as a parameter too
    shard_id int := 1;

    BEGIN
        SELECT nextval('shard_1.global_id_sequence') % 1024 INTO seq_id;

        SELECT FLOOR(EXTRACT(EPOCH FROM clock_timestamp()) * 1000) INTO now_millis;
        result := (now_millis - our_epoch) << 23;
        result := result | (shard_id << 10);
        result := result | (seq_id);
    END;
$$ LANGUAGE PLPGSQL;

SELECT shard_1.id_generator();

-- ============================================
-- USAGE
-- ============================================

--   create table shard_1.users(
--      id bigint not null default shard_1.id_generator() primary key,
--      email varchar(255) not null unique,
--      first_name varchar(50),
--      last_name varchar(50)
--  );

-- put the current schema (shard_1) on the default path so we don't have to refer
-- to the tables with the fully qualified name.
-- SET search_path TO shard_1, public;
