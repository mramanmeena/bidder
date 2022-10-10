package com.example.bidder.model;

import lombok.*;
import  javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Auction")
public class Auction {

    @Id
    @GeneratedValue
    private int id;
    private String basePrice;
    private Date startTime;
    private Date endTime;
    private Integer stepRate;
    private String itemName;
    private String status;
    private int winnerId;
    private Integer highestBid;

}