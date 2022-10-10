package com.example.bidder.model;

import lombok.*;

import javax.persistence.*;
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
    @Version
    private long version;
    private Date endTime;
    private Integer stepRate;
    private String itemName;
    private String status;
    private int winnerId;
    private Integer highestBid;

}