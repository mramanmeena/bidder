package com.example.bidder.model;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bid")
public class Bid {

    @Id
    @GeneratedValue
    private int id;
    private int userId;
    private int bidAmount;
    private String status;
    private String itemName;
    private String auctionId;


}
