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

import static com.example.bidder.exceptions.Exceptions.*;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {


    @Autowired
    AuctionDao auctionDao;
    @Autowired
    BidDao bidDao;
    @Autowired
    UserDao userDao;

    public Optional<User> getWinner(String itemName) throws Exception {
        log.info("c1 : {}", itemName);
        Auction auction = auctionDao.findByItemNameAndStatus(itemName, "Live").orElseThrow(() -> new RuntimeException(AUCTION_NOT_FOUND));
        if (auction.getWinnerId() != 0)
            return userDao.findById(auction.getWinnerId());
        throw new RuntimeException(WINNER_NOT_FOUND);

    }

    @Transactional
    public Auction createAuction(Auction auction) throws Exception {
        Optional<Auction> pr = auctionDao.findByItemNameAndStatus(auction.getItemName(), "Live");

        long auctionStartTime = auction.getStartTime().toInstant().toEpochMilli();
        long auctionEndTime = auction.getEndTime().toInstant().toEpochMilli();
        if (pr.isPresent()) {
            long prEndTime = pr.get().getEndTime().toInstant().toEpochMilli();
            if (prEndTime > auctionStartTime)
                throw new RuntimeException(AUCTION_ALREADY_PRESENT);
        }

        if (auctionStartTime >= auctionEndTime)
            throw new RuntimeException(START_TIME_ERROR);

        return auctionDao.save(auction);
    }

    public Auction getAuction(String id) throws Exception {
        log.info("Auction {} is available ", getAuction(id));
        Auction auction = auctionDao.findById(id).orElseThrow(() -> new RuntimeException("Unavailable")); //
        return auction;
    }

    public List<Auction> AllAuctions() throws Exception {
        List<Auction> Auctions = (List<Auction>) auctionDao.findAll();
        return Auctions;
    }

    @Transactional
    public Bid placeBid(Bid bid) throws Exception {
        String itemName = bid.getItemName();
        int userId = bid.getUserId();
        Integer bidAmount = bid.getBidAmount();
        Auction auction = auctionDao.findByItemNameAndStatus(itemName, "Live").orElseThrow(() -> new RuntimeException("Unavailable"));
        Optional<User> user = userDao.findById(userId);

        if (user.isPresent()) {
            if (auction.getWinnerId() == 0) {
                auction.setHighestBid(bidAmount);
                auction.setWinnerId(userId);
                bid.setStatus("Accepted");
            } else {
                int max = maxBid(auction.getId());
                if (bidAmount >= max + auction.getStepRate()) {
                    auction.setHighestBid(bidAmount);
                    auction.setWinnerId(userId);
                    bid.setStatus("Accepted");
                } else {
                    bid.setStatus("Not Accepted");
                }
                auctionDao.save(auction);
                bidDao.save(bid);
            }
            return bid;
        }
        throw new RuntimeException(USER_NOT_FOUND);
    }

    public int maxBid(int auction_id) throws Exception {
        Auction auction = auctionDao.findById(auction_id).orElseThrow(() -> new RuntimeException("Unavailable"));
        if (auction.getHighestBid() != null) {
            return auction.getHighestBid();
        }
        return 0;
    }

    public User loginUser(User user) {
        return userDao.save(user);
    }


}