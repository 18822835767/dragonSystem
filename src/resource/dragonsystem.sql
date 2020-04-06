drop database if exists dragonsystem;
create database dragonsystem;

use dragonsystem;

drop table if exists dragongroup;
create table dragongroup(
	dragonGroupId int primary key auto_increment,
	name varchar(255) not null,
	profile varchar(255),
	location varchar(255),
	size float
);

drop table if exists dragon;
create table dragon(
	dragonId int primary key auto_increment,
	dragonGroupId int,
	name varchar(255) not null,
	profile varchar(255),
	training int,
	healthy int,
	sex char(1),
	age int,
	foreign key(dragonGroupId) references dragongroup(dragonGroupId)
);


drop table if exists dragontrainer;
create table dragontrainer(
	dragonTrainerId int primary key auto_increment,
	dragonGroupId int,
	username varchar(255) unique not null,
	password varchar(255) not null,
	name varchar (255),
	foreign key(dragonGroupId) references dragongroup(dragonGroupId)
);

drop table if exists dragonmom;
create table dragonmom(
	dragonMomId int primary key auto_increment,
	name varchar (255),
	username varchar(255) unique not null,
	password varchar(255) not null
);

drop table if exists foreigner;
create table foreigner(
	foreignerId int primary key auto_increment,
	username varchar(255) unique not null,
	password varchar(255) not null,
	name varchar (255),
	money double
);