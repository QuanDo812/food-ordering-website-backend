package com.quan.controller;

import com.quan.model.Category;
import com.quan.model.User;
import com.quan.service.CategoryService;
import com.quan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @PostMapping("/admin/category")
    public ResponseEntity<Category> createdCategory(
            @RequestHeader("Authorization")String jwt,
            @RequestBody Category category) throws Exception{
        User user=userService.findUserByJwt(jwt);

        Category createdCategory=categoryService.createCategory(category.getName(), user.getId());
        return new ResponseEntity<Category>(createdCategory,HttpStatus.OK);
    }

    @GetMapping("/category/restaurant/{id}")
    public ResponseEntity<List<Category>> getRestaurantsCategory(
            @PathVariable Long id
            ) throws Exception{
        List<Category> categories=categoryService.findCategoryByRestaurantId(id);
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }



}
