package org.siri_hate.main_service.controller;

import org.siri_hate.main_service.api.NewsCategoryApi;
import org.siri_hate.main_service.dto.NewsCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.NewsCategoryRequestDTO;
import org.siri_hate.main_service.dto.NewsCategorySummaryResponseDTO;
import org.siri_hate.main_service.service.NewsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NewsCategoryController implements NewsCategoryApi {

    private final NewsCategoryService newsCategoryService;

    @Autowired
    public NewsCategoryController(NewsCategoryService newsCategoryService) {
        this.newsCategoryService = newsCategoryService;
    }

    @Override
    public ResponseEntity<NewsCategoryFullResponseDTO> createNewsCategory(NewsCategoryRequestDTO newsCategoryRequestDTO) {
        NewsCategoryFullResponseDTO response = newsCategoryService.createNewsCategory(newsCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteNewsCategory(Long id) {
        newsCategoryService.deleteNewsCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<NewsCategorySummaryResponseDTO>> getNewsCategories() {
        List<NewsCategorySummaryResponseDTO> newsCategories = newsCategoryService.getNewsCategories();
        return new ResponseEntity<>(newsCategories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsCategoryFullResponseDTO> getNewsCategory(Long id) {
        NewsCategoryFullResponseDTO newsCategory = newsCategoryService.getNewsCategory(id);
        return new ResponseEntity<>(newsCategory, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<NewsCategoryFullResponseDTO> updateNewsCategory(Long id, NewsCategoryRequestDTO newsCategoryRequestDTO) {
        NewsCategoryFullResponseDTO response = newsCategoryService.updateNewsCategory(id, newsCategoryRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
