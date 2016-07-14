# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "shard_1"."users" ("id" UUID NOT NULL PRIMARY KEY,"name" VARCHAR(254) NOT NULL);

# --- !Downs

drop table "shard_1"."users";

