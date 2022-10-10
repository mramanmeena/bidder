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

import static com.example.bidder.constants.Constants.MESSAGE_BODY;
import static com.example.bidder.constants.Constants.WINNING_MESSAGE;

@Slf4j
@Component
public class ExpiredScheduler {

    @Autowired
    AuctionServiceImpl auctionService;
    @Autowired
    EmailService emailService;
    @Autowired
    AuctionDao auctionDao;

    private final String EXPIRED = "Expired";
    private final String LIVE = "Live";
    private final String NOT_SET = "Not Set";

    @Scheduled(cron = "${cron}")
    public void markExpired() throws Exception {

        List<Auction> liveAuctions = auctionDao.findAllByStatus(LIVE).orElseThrow();
        List<Auction> newAuctions = auctionDao.findAllByStatus(NOT_SET).orElseThrow();

        for (Auction auction : liveAuctions) {
            log.info("auctions list {}  {}", auction.getId(), auction.getStatus());
            long currentTime = Instant.now().toEpochMilli();
            long startTime = auction.getStartTime().toInstant().toEpochMilli();
            long endTime = auction.getEndTime().toInstant().toEpochMilli();
            if (endTime < currentTime) {
                switch (auction.getWinnerId()) {
                    case 0:
                        auction.setStatus(EXPIRED);
                        auctionDao.save(auction);
                        break;
                    default:
                        log.info("Sending Mail to the Winner of the auction with auction_id {} {}", auction.getId(), auction.getStatus());
                        Optional<User> winner = auctionService.getWinner(auction.getItemName());
                        EmailDetails details = new EmailDetails();

                        if (winner.isPresent()) {
                            details.setRecipient(winner.get().getEmail());
                            details.setMsgBody(MESSAGE_BODY);
                            details.setSubject(WINNING_MESSAGE);
                            emailService.winningMail(details);
                        }
                        auction.setStatus(EXPIRED);
                        auctionDao.save(auction);
                }
            }
        }
        for (Auction auction : newAuctions) {
            long currentTime = Instant.now().toEpochMilli();
            long startTime = auction.getStartTime().toInstant().toEpochMilli();
            long endTime = auction.getEndTime().toInstant().toEpochMilli();

            if ((startTime <= currentTime) && (endTime >= currentTime)) {
                log.info("Auction with auction_id {} is live", auction.getId());
                auction.setStatus(LIVE);
                auctionDao.save(auction);
            }
        }

    }
}

