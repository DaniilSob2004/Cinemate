package com.example.cinemate.dao.contentviewhistory;

import com.example.cinemate.model.db.ContentViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentViewHistoryRepository extends JpaRepository<ContentViewHistory, Integer> {

}
