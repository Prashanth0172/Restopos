package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Pusher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PusherRepository extends CrudRepository<Pusher, Long> {
}
