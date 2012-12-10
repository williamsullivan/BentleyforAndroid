package com.course.example;


/**
 * phone entry class that makes organizing phone numbers easier
 * @author williamsullivan
 *
 */
public class phoneEntry {
   
   String name;
   String number;
   
   public phoneEntry(String na, String nu){
   this.name = na;
   this.number = nu;
   
   }
   
   public String getName(){
   return this.name;
   }
   
   public String getNumber(){
   return this.number;
   }
   
   public void setName(String nam){
	   this.name = nam;
	   }
   
   public void setNumber(String num){
	   this.number = num;
	   }
   
   
   public String toString(){
   
   return (name + ":" + number);
   
   }
   
   
   
   
   
   
   
   }
    
    
   

   
