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
	sex varchar(255),
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
insert into dragonmom(name,username,password) values('mom','admin','123');
insert into dragongroup(name,profile,location,size) values('bawanglongguan','no','shenzhen','100.0');
insert into dragongroup(name,profile,location,size) values('konglongguan','no','shenzhen','100.0');
insert into dragongroup(name,profile,location,size) values('shenlongguan','no','shenzhen','100.0');
insert into dragontrainer(dragonGroupId,username,password,name) values(1,'hhh','123','noname');
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'a','no',0,1,'n',15);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'b','no',0,1,'n',18);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'c','no',0,1,'n',20);
insert into foreigner(username, password, name, money) values('yoyo','123','no',100);
insert into foreigner(username, password, name, money) values('yoyo2','1234','no',100);
insert into foreigner(username, password, name, money) values('yoyo3','1235','no',100);