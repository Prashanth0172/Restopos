package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Category;
import com.hyva.restopos.rest.entities.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRespository extends JpaRepository<Item,Long> {
    Item findByItemNameOrItemCode(String itemName,String itemCode);
    @Query("select i from Item as i where (i.itemCode=:itemCode or i.itemName=:itemName) and i.itemId not like :itemId")
    Item findByItemNameOrItemCodeAndItemIdNotIn(@Param(value = "itemName") String itemName,@Param(value = "itemCode") String itemCode,@Param(value = "itemId") Long id);
    Item findAllByItemName(String name);
    Item findAllByItemId(Long id);
    Item findAllByItemCode(Long id);
    List<Item> findByIdItemCategory(Category name);
    List<Item>findAllByItemNameContainingAndItemStatus(String countryName,String status);
    List<Item>findAllByItemNameContainingAndItemStatus(String countryName,String status,Pageable pageable);
    Item findFirstByItemStatus(String status,Sort sort);
    List<Item>findAllByItemStatus(String status,Pageable pageable);
    Item findFirstByItemNameContainingAndItemStatus(String countryName,String status,Sort sort);
    List<Item> findAllByItemStatus(String status);

    Item findAllByItemNameAndItemIdNotIn(String name,Long id);
}
