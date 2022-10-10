package com.example.bidder.services;
import com.example.bidder.model.Auction;
import com.example.bidder.model.Bid;
import com.example.bidder.model.User;

import java.util.List;
import java.util.Optional;

public interface AuctionService {

    public Optional<User> getWinner(String itemName) throws Exception;
    public Auction createAuction(Auction auction) throws Exception;
    public Auction  getAuction(String id) throws Exception;
    public Bid placeBid(Bid bid) throws Exception;
    public int maxBid(int auction_id) throws Exception;
    public User loginUser(User user);

}
