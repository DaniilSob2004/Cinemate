package com.example.cinemate.dao.contenttype;

import com.example.cinemate.model.db.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTypeRepository extends JpaRepository<ContentType, Integer> {

}
