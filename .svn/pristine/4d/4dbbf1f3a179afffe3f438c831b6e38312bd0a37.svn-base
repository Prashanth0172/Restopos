package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Category;
import com.hyva.restopos.rest.entities.KitchenMaster;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findAllByItemCategoryNameAndItemCategoryIdNotIn(String name,Long id);
    Category findAllByItemCategoryName(String name);
    Category findAllByItemCategoryId(Long id);
    List<Category> findAllByStatus(String status);
    List<Category>findAllByItemCategoryNameContainingAndStatus(String countryName,String status);
    List<Category>findAllByItemCategoryNameContainingAndStatus(String countryName,String status,Pageable pageable);
    Category findFirstByStatus(String status,Sort sort);
    List<Category>findAllByStatus(String status,Pageable pageable);
}
