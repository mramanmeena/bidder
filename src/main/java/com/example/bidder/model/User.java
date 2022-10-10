package com.example.bidder.model;
import lombok.*;
import  javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User")

public class User {

    @Id
    @GeneratedValue
    private int id;
    private String email;
    private String name;

}




