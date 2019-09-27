module Dicebot{
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.fxml;
	
	requires java.sql;
	requires javax.persistence;
	requires hibernate.core;
	requires javax.json;
	
	
	requires com.jfoenix;
	requires jfxtras.common;
	requires jfxtras.controls;
	requires jfxtras.labs;
	
	requires fontawesomefx;	
	requires controlsfx;
	
	
	exports application;
	exports controllers;
	exports exceptions;
	exports model.bet;
	exports model.dao;
	exports model.entity;
	
	opens controllers;
}