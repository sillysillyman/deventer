package io.sillysillyman.deventer.controller.admin;

import io.sillysillyman.deventer.dto.category.CategoryRequestDto;
import io.sillysillyman.deventer.dto.category.CategoryResponseDto;
import io.sillysillyman.deventer.service.admin.AdminCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    /**
     * 관리자 권한으로 카테고리를 생성합니다.
     *
     * @param categoryRequestDto 카테고리 요청 DTO
     * @return 카테고리 정보
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
        @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(adminCategoryService.createCategory(categoryRequestDto));
    }

    /**
     * 관리자 권한으로 카테고리 이름을 변경합니다.
     *
     * @param categoryId         변경할 카테고리 고유번호
     * @param categoryRequestDto 변경할 카테고리 요청 DTO
     * @return 수정된 카테고리 정보
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable Long categoryId,
        @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        return ResponseEntity.ok()
            .body(adminCategoryService.updateCategory(categoryId, categoryRequestDto));
    }


    /**
     * 관리자 권한으로 카테고리를 삭제합니다.
     *
     * @param categoryId 삭제할 카테고리 고유번호
     * @return 삭제 완료 메세지
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(adminCategoryService.deleteCategory(categoryId));
    }
}