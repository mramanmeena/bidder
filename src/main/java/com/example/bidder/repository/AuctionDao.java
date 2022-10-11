package com.example.bidder.repository;
import com.example.bidder.model.Auction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AuctionDao extends JpaRepository<Auction,String> {


    Optional<Auction> findByItemNameAndStatus(String itemName, String status);

    Optional<Auction> findByItemName(String itemName);

    Optional <Auction>findById(int auction_id);

    Optional <List <Auction>> findAllByStatus(String live);

    @Query(value = "Select * from auction as a where  a.end_time <   :currentTime  AND a.status = :status",nativeQuery = true)
    Optional<List<Auction>> findEndedWithStatus(@Param("currentTime") Date currentTime,@Param("status") String status);


    @Query(value = "Select * from auction as a where  a.start_time < a.end_time  AND a.status = :status a.end_time >= :currentTime ",nativeQuery = true)
    Optional<List<Auction>> toBeSet(@Param("currentTime") Date currentTime, @Param("status") String not_set);
}
