package com.example.bidder.scheduler;
import com.example.bidder.model.Auction;
import com.example.bidder.model.EmailDetails;
import com.example.bidder.model.User;
import com.example.bidder.repository.AuctionDao;
import com.example.bidder.services.AuctionServiceImpl;
import com.example.bidder.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ExpiredScheduler {

    @Autowired
    AuctionServiceImpl auctionService;
    @Autowired
    EmailService emailService;
    @Autowired
    AuctionDao auctionDao;


    @Scheduled( cron = "${cron}" )
    public void markExpired() throws Exception {

        List<Auction> auctions = (List<Auction>) auctionService.AllAuctions();
        for (Auction auction: auctions) {
            log.info("auctions list {}{}",auction.getId(),auction.getStatus());

            if ((auction.getStatus()==null  || auction.getStatus().isEmpty()) && (auction.getStartTime().toInstant().toEpochMilli() <= Instant.now().toEpochMilli())) {
                    log.info("Auction with auction_id {} is live", auction.getId());
                    auction.setStatus("Live");
                    auctionDao.save(auction);
            }
            else if (auction.getStatus().equals("Live") && auction.getWinnerId() == 0 &&(auction.getEndTime().toInstant().toEpochMilli() < Instant.now().toEpochMilli() )){
                auction.setStatus("Expired");
                auctionDao.save(auction);
            }
            else if ((auction.getStatus().equals("Live")) && (auction.getWinnerId() != 0 )&& (auction.getEndTime().toInstant().toEpochMilli() < Instant.now().toEpochMilli()) ){
                log.info("Sending Mail to the Winner of the auction with auction_id {} {}",auction.getId(),auction.getStatus());
                Optional<User> winner  = auctionService.getWinner(auction.getItemName());
                EmailDetails details = new EmailDetails();

                 if (winner.isPresent()){
                    details.setRecipient(winner.get().getEmail());
                    details.setMsgBody("You Won");
                    details.setSubject("Congratulations");
                    emailService.winningMail(details);
                    }
                auction.setStatus("Expired");
                auctionDao.save(auction);
            }

            else if (auction.getEndTime().toInstant().toEpochMilli() < Instant.now().toEpochMilli() && !(auction.getStatus().equals("Expired"))) {
//                log.info("Auction with auction_id {} Expired",auction.getAuctionId());
                auction.setStatus("Expired");
                auctionDao.save(auction);
            }

        }
    }
}
