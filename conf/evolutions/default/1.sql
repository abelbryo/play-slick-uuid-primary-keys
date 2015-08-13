# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "users" ("name" VARCHAR(254) NOT NULL,"id" UUID NOT NULL PRIMARY KEY);

# --- !Downs

drop table "users";

