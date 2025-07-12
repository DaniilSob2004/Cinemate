package com.example.cinemate.service.business_db.contentviewhistoryservice;

import com.example.cinemate.dao.contentviewhistory.ContentViewHistoryRepository;
import com.example.cinemate.dto.contentviewhistory.ContentHistoryParamsDto;
import com.example.cinemate.model.db.ContentViewHistory;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentViewHistoryServiceImpl implements ContentViewHistoryService {

    private final ContentViewHistoryRepository contentViewHistoryRepository;

    public ContentViewHistoryServiceImpl(ContentViewHistoryRepository contentViewHistoryRepository) {
        this.contentViewHistoryRepository = contentViewHistoryRepository;
    }

    @Override
    public void save(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.save(contentViewHistory);
    }

    @Override
    public void saveContentViewHistories(List<ContentViewHistory> contentViewHistories) {
        contentViewHistoryRepository.saveAll(contentViewHistories);
    }

    @Override
    public void update(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.save(contentViewHistory);
    }

    @Override
    public void delete(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.delete(contentViewHistory);
    }

    @Override
    public List<ContentViewHistory> findAll() {
        return (List<ContentViewHistory>) contentViewHistoryRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentViewHistoryRepository.deleteAll();
    }


    @Override
    public boolean existsByUserIdAndContentId(Integer userId, Integer contentId) {
        return contentViewHistoryRepository.existsContentViewHistoriesByUserIdAndContentId(userId, contentId);
    }

    @Override
    public Page<ContentViewHistory> getContentViewHistories(ContentHistoryParamsDto contentHistoryParamsDto) {
        Pageable pageable = PaginationUtil.getPageable(contentHistoryParamsDto);
        Specification<ContentViewHistory> specContentHistory = Specification.where(this.searchSpecification(contentHistoryParamsDto));
        return contentViewHistoryRepository.findAll(specContentHistory, pageable);
    }

    private Specification<ContentViewHistory> searchSpecification(final ContentHistoryParamsDto contentHistoryParamsDto) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // фильтр по id пользователя
            if (contentHistoryParamsDto.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), contentHistoryParamsDto.getUserId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Override
    public List<ContentViewHistory> findByUserId(int userId) {
        return contentViewHistoryRepository.findContentViewHistoryByUserId(userId);
    }
}
