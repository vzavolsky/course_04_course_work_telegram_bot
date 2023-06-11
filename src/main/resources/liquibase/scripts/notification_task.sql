-- liquibase formatted sql

-- changeset botadmin:1
create table notification_task
(
    id      integer
        constraint notification_task_pk
            primary key,
    date    date    not null,
    text    text    not null,
    chatId integer not null
);