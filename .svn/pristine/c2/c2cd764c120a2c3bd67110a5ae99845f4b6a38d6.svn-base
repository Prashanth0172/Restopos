package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.AirPayDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirPayDetailsRepository extends JpaRepository<AirPayDetails,Long> {

    AirPayDetails findByTransactionID(String token);
}
