package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Country;
import com.hyva.restopos.rest.entities.StaffGroup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffGroupRepository extends JpaRepository<StaffGroup,Long> {
    StaffGroup findAllByStaffgroupnameAndStaffgroupidNotIn(String name, Long id);
    StaffGroup findAllByStaffgroupname(String name);
    StaffGroup findAllByStaffgroupid(Long id);
    List<StaffGroup> findAllByStatus(String status);
    List<StaffGroup>findAllByStaffgroupnameContainingAndStatus(String countryName, String status);
    List<StaffGroup>findAllByStaffgroupnameContainingAndStatus(String countryName, String status, Pageable pageable);
    StaffGroup findFirstByStatus(String status, Sort sort);
    List<StaffGroup>findAllByStatus(String status, Pageable pageable);
    StaffGroup findFirstByStaffgroupnameContainingAndStatus(String countryName, String status, Sort sort);

}
