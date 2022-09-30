package com.it.dto;

import com.it.entity.Dish;
import com.it.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端传过来的dish实体  多了一个口味
 * @since  2022-9-30
 * @author  hyj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
