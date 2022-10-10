package com.example.bidder.exceptions;

public interface Exceptions {

    String AUCTION_ALREADY_PRESENT = "Auction with same itemName already present";
    String USER_NOT_FOUND = "No username with give id";
    String AUCTION_NOT_FOUND = "No auctions are available";
    String START_TIME_ERROR = "Start time should be earlier than end time";
    String WINNER_NOT_FOUND = "Winner not found";
    String NOT_LIVE_YET= "Auction is not live yet";



}
