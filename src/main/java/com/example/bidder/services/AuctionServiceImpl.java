package com.example.bidder.services;

import com.example.bidder.model.Auction;
import com.example.bidder.model.Bid;
import com.example.bidder.model.User;
import com.example.bidder.repository.AuctionDao;
import com.example.bidder.repository.BidDao;
import com.example.bidder.repository.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {


    @Autowired
    AuctionDao auctionDao; //name change
    @Autowired
    BidDao bidDao; //name change
    @Autowired
    UserDao userDao;

    public Optional < User > getWinner(String itemName) throws Exception {
        log.info("c1 : {}", itemName);
        Auction auction = auctionDao.findByItemNameAndStatus(itemName,"Live").orElseThrow(() -> new RuntimeException("Unavailable"));
        if ( auction.getWinnerId() != 0 )
            return userDao.findById(auction.getWinnerId());
        throw new RuntimeException("No winner found");

    }
    @Transactional
    public Auction createAuction(Auction auction) throws Exception {
        Optional<Auction>pr = auctionDao.findByItemNameAndStatus(auction.getItemName(),"Live");
        if (pr.isPresent() )
            throw new RuntimeException("auction for given item already exists");

        if (auction.getStartTime().toInstant().toEpochMilli() >= auction.getEndTime().toInstant().toEpochMilli())
            throw new RuntimeException("Start time should be earlier than end time");

        return auctionDao.save(auction);
    }

    public Auction  getAuction(String id) throws Exception {
        log.info("Auction {} is available " , getAuction(id));
        Auction auction =  auctionDao.findById(id).orElseThrow(() -> new RuntimeException("Unavailable")); //
        return auction;
    }

    public List < Auction > AllAuctions() throws Exception {
        List <Auction>  Auctions = (List<Auction>) auctionDao.findAll();
        return Auctions;
    }

    @Transactional
    public Bid placeBid(Bid bid) throws Exception {
        String itemName = bid.getItemName();
        int user_id = bid.getUserId();
        Integer bid_amount = bid.getBidAmount();
        Auction auction = auctionDao.findByItemName(itemName).orElseThrow(() -> new RuntimeException("Unavailable"));

        if (auction.getStatus().equals("Live")) {

            Optional<User> user = userDao.findById(user_id);

            if (user.isPresent()) {
                if (auction.getWinnerId() == 0 ) {
                    auction.setHighestBid(bid_amount);
                    auction.setWinnerId(user_id);
                    bid.setStatus("Accepted");
                } else {
                    int max = maxBid(auction.getId());
                    if (bid_amount >= max + auction.getStepRate()) {
                        auction.setHighestBid(bid_amount);
                        auction.setWinnerId(user_id);
                        bid.setStatus("Accepted");
                    } else {
                        bid.setStatus("Not Accepted");
                    }
                    auctionDao.save(auction);
                    bidDao.save(bid);
                }
            }
            return bid;
        }
        throw new RuntimeException("Auction is not live yet");
    }

    public int maxBid(int auction_id) throws Exception {
        Optional < Auction > auction = auctionDao.findById(String.valueOf(auction_id));
        if (auction.get().getHighestBid()!=null) {
            return auction.get().getHighestBid();
        }
        return 0;
    }

    public User loginUser(User user) {
        return userDao.save(user);
    }


}