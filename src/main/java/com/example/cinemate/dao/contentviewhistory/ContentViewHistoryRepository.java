package com.example.cinemate.dao.contentviewhistory;

import com.example.cinemate.model.db.ContentViewHistory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentViewHistoryRepository extends PagingAndSortingRepository<ContentViewHistory, Integer>, JpaSpecificationExecutor<ContentViewHistory> {
    List<ContentViewHistory> findContentViewHistoryByUserId(int userId);
}
