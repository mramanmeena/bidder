package com.example.bidder.repository;

import com.example.bidder.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidDao extends JpaRepository<Bid,String> {
}
