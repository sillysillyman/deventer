package io.sillysillyman.deventer.service;

import io.sillysillyman.deventer.dto.category.CategoryResponseDto;
import io.sillysillyman.deventer.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponseDto::new).toList();
    }
}