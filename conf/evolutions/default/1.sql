# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "shard_1"."students" ("id" bigint not null default shard_1.id_generator() primary key,"name" VARCHAR(254) NOT NULL);
create table "shard_1"."users" ("id" UUID NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL);

# --- !Downs

drop table "shard_1"."users";
drop table "shard_1"."students";

