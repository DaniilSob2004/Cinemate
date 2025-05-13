package com.example.cinemate.dao.content;

import com.example.cinemate.model.db.Content;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends PagingAndSortingRepository<Content, Integer>, JpaSpecificationExecutor<Content> {
    long countContentsByIsActiveIsTrue();
    Optional<Content> findContentById(Integer id);
    Optional<Content> findContentByNameIgnoreCase(String name);
    List<Content> findContentByContentTypeId(Integer contentTypeId);
}
