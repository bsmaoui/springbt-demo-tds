package com.katatds.springbtdemo.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class InputData {

   private  Integer coordX;
   private Integer coordY;
   private String orientation;
   private String control;
   private String line;
    
}