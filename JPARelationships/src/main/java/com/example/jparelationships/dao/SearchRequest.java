package com.example.jparelationships.dao;

import lombok.Data;

@Data
public class SearchRequest {
   private String firstName;
   private String lastName;
   private String eMail;
}
