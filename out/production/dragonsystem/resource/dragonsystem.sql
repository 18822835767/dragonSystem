drop database if exists dragonsystem;
create database dragonsystem;

use dragonsystem;

drop table if exists dragongroup;
create table dragongroup(
	dragonGroupId int primary key auto_increment,
	name varchar(255) unique not null,
	profile varchar(255),
	location varchar(255),
	size float
);

drop table if exists dragon;
create table dragon(
	dragonId int primary key auto_increment,
	dragonGroupId int not null,
	name varchar(255) unique not null,
	profile varchar(255),
	training int,
	healthy int,
	sex varchar(255) not null,
	age int not null,
	foreign key(dragonGroupId) references dragongroup(dragonGroupId)
);


drop table if exists dragontrainer;
create table dragontrainer(
	dragonTrainerId int primary key auto_increment,
	dragonGroupId int not null ,
	username varchar(255) unique not null,
	password varchar(255) not null,
	name varchar(255) not null,
	foreign key(dragonGroupId) references dragongroup(dragonGroupId)
);

drop table if exists dragonmom;
create table dragonmom(
	dragonMomId int primary key auto_increment,
	name varchar(255) not null,
	username varchar(255) unique not null,
	password varchar(255) not null,
	moneyTub float
);

drop table if exists foreigner;
create table foreigner(
	foreignerId int primary key auto_increment,
	username varchar(255) unique not null,
	password varchar(255) not null,
	name varchar(255) not null,
	money double default 100.0
);

drop table if exists ticket;
create table ticket(
    ticketId int primary key auto_increment,
    foreignerId int unique not null,
    price float,
    type varchar(255),
    buyTime varchar(255),
    times int,
    backing int,
    foreign key(foreignerId) references foreigner(foreignerId)
);

drop table if exists activity;
create table activity(
    activityId int primary key auto_increment,
    dragonGroupId int,
    name varchar(255),
    content varchar(255),
    startTime varchar(255),
    overTime varchar(255),
    foreign key(dragonGroupId) references dragongroup(dragonGroupId)
);

drop table if exists evaluation;
create table evaluation(
    evaluationId int primary key auto_increment,
    activityId int,
    foreignerId int,
    rank int,
    content varchar(255),
    evaluateTime varchar(255),
    foreign key(activityId) references Activity(activityId),
    foreign key(foreignerId) references foreigner(foreignerId)
);


insert into dragonmom(name,username,password,moneyTub) values('mom','admin','090087074',0);#解密后密码是123

insert into dragongroup(name,profile,location,size) values('bawanglongguan','no','shenzhen','100.0');
insert into dragongroup(name,profile,location,size) values('konglongguan','no','shenzhen','100.0');
insert into dragongroup(name,profile,location,size) values('shenlongguan','no','shenzhen','100.0');

insert into dragontrainer(dragonGroupId,username,password,name) values(1,'hhh','090087074','noname');#解密后密码是123
insert into dragontrainer(dragonGroupId,username,password,name) values(2,'hhh2','090087074','noname');#解密后密码是123
insert into dragontrainer(dragonGroupId,username,password,name) values(3,'hhh3','090087074','noname');#解密后密码是123

insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'a','no',0,1,'n',15);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'b','no',0,1,'n',18);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(1,'c','no',0,1,'n',20);

insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(2,'aa','no',0,1,'n',15);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(2,'bb','no',0,1,'n',18);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(2,'cc','no',0,1,'n',20);

insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(3,'aaa','no',0,1,'n',15);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(3,'bbb','no',0,1,'n',18);
insert into dragon(dragonGroupId,name,profile,training,healthy,sex,age) values(3,'ccc','no',0,1,'n',20);

insert into foreigner(username, password, name, money) values('yoyo','090087074','no1',100);#解密后密码是123
insert into foreigner(username, password, name, money) values('yoyo2','090087074','no2',100);#解密后密码是123
insert into foreigner(username, password, name, money) values('yoyo3','090087074','no3',100);#解密后密码是123
