package com.stainclo.category.controller;

import com.stainclo.category.dto.request.CategoryRequest;
import com.stainclo.category.dto.response.CategoryResponse;
import com.stainclo.category.enums.CategoryStatus;
import com.stainclo.category.service.CategoryService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //CREATE
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest categoryRequest){
        CategoryResponse categoryResponse = categoryService.create(categoryRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryResponse.categoryId())
                .toUri();
        return ResponseEntity.created(location).body(categoryResponse);
    }

    //READ BY ID
    @GetMapping("{categoryId}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.getById(categoryId));
    }

    //PAGINATED READ
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> findAll(
            @RequestParam(required = false) CategoryStatus categoryStatus,
            @PageableDefault(sort = "categoryId", direction = Sort.Direction.DESC)
            Pageable pageable){
        return ResponseEntity.ok(categoryService.findAll(categoryStatus, pageable));
    }

    //UPDATE
    @PutMapping("{categoryId}")
    public ResponseEntity<CategoryResponse> update(@Valid @RequestBody CategoryRequest categoryRequest, @PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.update(categoryRequest, categoryId));
    }

    //ARCHIVE
    @PatchMapping("{categoryId}/archive")
    public ResponseEntity<CategoryResponse> archive(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.archive(categoryId));
    }

    //RESTORE
    @PatchMapping("{categoryId}/restore")
    public ResponseEntity<CategoryResponse> restore(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.restore(categoryId));
    }
}
