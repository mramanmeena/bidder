package com.example.bidder.services;


import com.example.bidder.model.EmailDetails;

// Importing required classes
// Interface
public interface EmailService {

    String winningMail(EmailDetails details);
}