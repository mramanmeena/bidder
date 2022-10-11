package com.example.bidder.services;
import com.example.bidder.model.EmailDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired  private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")  private String sender;
    public String winningMail(EmailDetails details)
    {

        try {

            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

            catch (Exception e) {
            log.info("Exception" , e.getMessage());
            return "Error while Sending Mail";
        }
    }
}