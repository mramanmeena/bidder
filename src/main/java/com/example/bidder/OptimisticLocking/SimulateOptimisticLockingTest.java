package com.example.bidder.OptimisticLocking;

import com.example.bidder.model.Auction;
import com.example.bidder.repository.AuctionDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SimulateOptimisticLockingTest {

    @Autowired
    AuctionDao auctionDao;

    public void Simulator(String auctionId) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Auction auction = auctionDao.findById(Integer.parseInt(auctionId)).orElseThrow();

                if (auction != null) {
                    auction.setHighestBid(400000);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    auctionDao.save(auction);
                }
            }
        });

        Thread t2 = new Thread(new Runnable() /{
            @Override
            public void run() {
                Auction auction = auctionDao.findById(Integer.parseInt(auctionId)).orElseThrow();
                if (auction != null) {
                    auction.setHighestBid(390000);
                    auctionDao.save(auction);
                }
            }
        });

        t1.start();
        Thread.sleep(1000);
        t2.start();
    }

}