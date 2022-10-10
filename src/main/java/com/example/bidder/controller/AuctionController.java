package com.example.bidder.controller;

import com.example.bidder.model.Auction;
import com.example.bidder.model.Bid;
import com.example.bidder.model.User;
import com.example.bidder.repository.AuctionDao;
import com.example.bidder.services.AuctionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auction")
public class AuctionController {

    @Autowired
    private AuctionDao auctionDao;
    @Autowired
    private AuctionServiceImpl auctionService;

    @PostMapping("/create")
    public ResponseEntity<Auction> createAuction(@RequestBody  Auction auction) {
        if (!(auction == null || auction.getItemName() == null || auction.getEndTime() == null || auction.getStepRate() == null )) {
            try { return new ResponseEntity<Auction>(auctionService.createAuction(auction), HttpStatus.CREATED);}
            catch (Exception e) { return new ResponseEntity<>(HttpStatus.BAD_REQUEST); }

        } else {
            return new ResponseEntity<Auction>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAuctions")
    public List<Auction> getAuctions() throws Exception {
        return  auctionService.AllAuctions();
    }


    @PostMapping("/place")
    public ResponseEntity<Bid> placeBid(@RequestBody Bid bid){
        try {
            return new ResponseEntity<>(auctionService.placeBid(bid), HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            log.info("Exception occurred{}", e);
            return new ResponseEntity<Bid>(HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("/winner")
    public ResponseEntity<User> GetWinner(@RequestParam String itemName) throws Exception {
        try {
            Optional<User> winner = auctionService.getWinner(itemName);
            return new ResponseEntity<User>( winner.get(),HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws Exception {
        try {
            return new ResponseEntity<User>(auctionService.loginUser(user), HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
        }
    }

}
