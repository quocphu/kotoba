create database kotoba;

create table lesson (
	id INTEGER primary key AUTOINCREMENT,
	  title char(100),
	  priority INTEGER,
	  create_Date DateTime,
	  update_Date DateTime
);

create table kotoba (
	id INTEGER primary key AUTOINCREMENT ,
	lesson_id INTEGER,
	  japanase varchar(255),
	  kanji varchar(255),
	  mean varchar(255),
	  audio varchar(255),
	 priority INTEGER,
	 
	  create_Date DateTime,
	  update_Date DateTime 
);
