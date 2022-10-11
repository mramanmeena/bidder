package com.example.bidder.scheduler;

import com.example.bidder.model.Auction;
import com.example.bidder.repository.AuctionDao;
import com.example.bidder.services.AuctionServiceImpl;
import com.example.bidder.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SetLiveScheduler {
    @Autowired
    AuctionServiceImpl auctionService;
    @Autowired
    EmailService emailService;
    @Autowired
    AuctionDao auctionDao;
    private final String NOT_SET = "Not Set";
    private final String LIVE = "Live";

    public void markLive() throws Exception {
        Date cT = Date.from(java.time.Clock.systemUTC().instant());
        List<Auction> newAuctions = auctionDao.toBeSet(cT, NOT_SET).orElseThrow();
        List<Auction> NotSet = auctionDao.toBeSet(cT, "Live").orElseThrow();
        for (Auction auction : NotSet) {
                log.info("Auction with auction_id {} is live", auction.getId());
                auction.setStatus(LIVE);
                auctionDao.save(auction);
            }
        }
    }
