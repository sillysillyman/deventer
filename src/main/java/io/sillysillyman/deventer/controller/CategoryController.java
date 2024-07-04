package io.sillysillyman.deventer.controller;

import io.sillysillyman.deventer.dto.category.CategoryResponseDto;
import io.sillysillyman.deventer.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }
}
