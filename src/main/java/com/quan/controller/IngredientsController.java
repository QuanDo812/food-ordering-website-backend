package com.quan.controller;

import com.quan.model.IngredientCategory;
import com.quan.model.IngredientsItem;
import com.quan.request.CreateIngredientCategoryRequest;
import com.quan.request.CreateIngredientRequest;
import com.quan.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientsController {

    @Autowired
    private IngredientsService ingredientService;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody CreateIngredientCategoryRequest req) throws Exception{
        IngredientCategory items=ingredientService.createIngredientsCategory(req.getName(), req.getRestaurantId());
        return new ResponseEntity<>(items, HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<IngredientsItem> createIngredient(
            @RequestBody CreateIngredientRequest req) throws Exception{

        IngredientsItem item=ingredientService.createIngredientsItem(req.getRestaurantId(),req.getName(),req.getIngredientCategoryId(), req.getPrice());
        return new ResponseEntity<>(item,HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stoke")
    public ResponseEntity<IngredientsItem> updateStoke(@PathVariable Long id) throws Exception{
        IngredientsItem item=ingredientService.updateStoke(id);
        return new ResponseEntity<IngredientsItem>(item,HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> restaurantsIngredient(
            @PathVariable Long id) throws Exception{
        List<IngredientsItem> items=ingredientService.findRestaurantsIngredients(id);
        return new ResponseEntity<>(items,HttpStatus.OK);
    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> restaurantsIngredientCategory(
            @PathVariable Long id) throws Exception{
        List<IngredientCategory> items=ingredientService.findIngredientsCategoryByRestaurantId(id);
        return new ResponseEntity<>(items,HttpStatus.OK);
    }

}
