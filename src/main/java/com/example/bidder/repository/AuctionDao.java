package com.example.bidder.repository;
import com.example.bidder.model.Auction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionDao extends JpaRepository<Auction,String> {


    Optional<Auction> findByItemNameAndStatus(String itemName, String status);

    Optional<Auction> findByItemName(String itemName);

    Optional <Auction>findById(int auction_id);

    Optional <List <Auction>> findAllByStatus(String live);
}
