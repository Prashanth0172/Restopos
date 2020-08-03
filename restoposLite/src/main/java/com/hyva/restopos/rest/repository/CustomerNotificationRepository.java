package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.CustomerNotifications;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerNotificationRepository extends JpaRepository<CustomerNotifications,Long> {
    CustomerNotifications findFirstByCustomerNotificationOrderByCustNotiIdDesc(String customerNotification);
    List<CustomerNotifications> findAllByStatus(String status);
    List<CustomerNotifications> findAllByStatusNotInAndTableNameIsNull(String status,Sort sort);
    List<CustomerNotifications> findAllByStatusNotInAndTableNameNotNull(String status,Sort sort);
    List<CustomerNotifications>  findAllByStatusInAndTableNameIsNull(List<String> status,Sort sort);
    List<CustomerNotifications>  findAllByStatusInAndTableNameNotNull(List<String> status,Sort sort);
    List<CustomerNotifications>  findAllByStatusAndTableNameNotNull(List<String> status,Sort sort);
    CustomerNotifications  findFirstByStatus(String status,Sort sort);
    List<CustomerNotifications> findAllByStatus(String status, Pageable pageable);

}
