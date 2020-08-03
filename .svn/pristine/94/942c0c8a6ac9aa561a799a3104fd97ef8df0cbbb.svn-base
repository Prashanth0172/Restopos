package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Category;
import com.hyva.restopos.rest.pojo.CategoryPojo;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category mapPojoToEntity (CategoryPojo categoryPojo){
        Category category = new Category();
        category.setItemCategoryId(categoryPojo.getCategory_id());
        category.setCode(categoryPojo.getCode());
        category.setItemCategoryName(categoryPojo.getName());
        category.setItemCategoryDesc(categoryPojo.getDescription());
        category.setImage(categoryPojo.getImage());
        category.setStatus(categoryPojo.getStatus());
        return category;
    }

    public static List<CategoryPojo> mapEntityToPojo(List<Category> List) {
        List<CategoryPojo> list = new ArrayList<>();
        for (Category config : List) {
            CategoryPojo categoryPojo = new CategoryPojo();
            categoryPojo.setCategory_id(config.getItemCategoryId());
            categoryPojo.setCode(config.getCode());
            categoryPojo.setDescription(config.getItemCategoryDesc());
            categoryPojo.setImage(config.getImage());
            categoryPojo.setStatus(config.getStatus());
            categoryPojo.setName(config.getItemCategoryName());
            list.add(categoryPojo);
        }
        return list;
    }

}
