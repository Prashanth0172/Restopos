package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.SMSServer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsServerRepository  extends JpaRepository<SMSServer,Long>{
    SMSServer findAllById(Long id);
    List<SMSServer> findAllByApiKey(String search);
}
