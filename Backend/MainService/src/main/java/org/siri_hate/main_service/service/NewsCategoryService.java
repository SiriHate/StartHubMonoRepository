package org.siri_hate.main_service.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.siri_hate.main_service.dto.NewsCategoryFullResponseDTO;
import org.siri_hate.main_service.dto.NewsCategoryRequestDTO;
import org.siri_hate.main_service.dto.NewsCategorySummaryResponseDTO;
import org.siri_hate.main_service.model.mapper.NewsCategoryMapper;
import org.siri_hate.main_service.model.entity.news.NewsCategory;
import org.siri_hate.main_service.repository.NewsCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsCategoryMapper newsCategoryMapper;

    @Autowired
    public NewsCategoryService(
            NewsCategoryRepository newsCategoryRepository,
            NewsCategoryMapper newsCategoryMapper
    )
    {
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsCategoryMapper = newsCategoryMapper;
    }

    @Transactional
    public NewsCategoryFullResponseDTO createNewsCategory(NewsCategoryRequestDTO request) {
        NewsCategory domain = newsCategoryMapper.toNewsCategory(request);
        newsCategoryRepository.save(domain);
        return newsCategoryMapper.toNewsCategoryFullResponse(domain);
    }

    public NewsCategory getNewsCategoryEntityById(Long id) {
        return newsCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<NewsCategorySummaryResponseDTO> getNewsCategories() {
        List<NewsCategory> newsCategories = newsCategoryRepository.findAll();
        if (newsCategories.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return newsCategoryMapper.toNewsCategorySummaryResponseList(newsCategories);
    }

    public NewsCategoryFullResponseDTO getNewsCategory(Long id) {
        NewsCategory newsCategory = newsCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return newsCategoryMapper.toNewsCategoryFullResponse(newsCategory);
    }

    @Transactional
    public NewsCategoryFullResponseDTO updateNewsCategory(Long id, NewsCategoryRequestDTO request) {
        NewsCategory newsCategory = newsCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        newsCategory = newsCategoryMapper.updateNewsCategory(request, newsCategory);
        newsCategoryRepository.save(newsCategory);
        return newsCategoryMapper.toNewsCategoryFullResponse(newsCategory);
    }

    @Transactional
    public void deleteNewsCategory(Long id) {
        NewsCategory newsCategory = newsCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        newsCategoryRepository.delete(newsCategory);
    }
}