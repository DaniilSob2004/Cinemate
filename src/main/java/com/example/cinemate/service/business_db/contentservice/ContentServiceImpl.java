package com.example.cinemate.service.business_db.contentservice;

import com.example.cinemate.dao.content.ContentRepository;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.model.db.Content;
import com.example.cinemate.utils.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Predicate;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Content save(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public void saveContentsList(List<Content> contents) {
        contentRepository.saveAll(contents);
    }

    @Override
    public void update(Content content) {
        contentRepository.save(content);
    }

    @Override
    public void delete(Content content) {
        contentRepository.delete(content);
    }

    @Override
    public List<Content> findAll() {
        return (List<Content>)contentRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentRepository.deleteAll();
    }


    @Override
    public Page<Content> getContents(ContentSearchParamsDto contentSearchParamsDto) {
        Pageable pageable = PaginationUtil.getPageable(contentSearchParamsDto);
        Specification<Content> specContent = Specification.where(this.searchSpecification(contentSearchParamsDto));
        return contentRepository.findAll(specContent, pageable);
    }

    private Specification<Content> searchSpecification(final ContentSearchParamsDto contentSearchParamsDto) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // фильтр по активности
            if (contentSearchParamsDto.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), contentSearchParamsDto.getIsActive()));
            }

            // фильтр по названию (например, name)
            if (contentSearchParamsDto.getSearchStr() != null && !contentSearchParamsDto.getSearchStr().isBlank()) {
                String pattern = "%" + contentSearchParamsDto.getSearchStr().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            }

            // фильтр по типу контента
            if (contentSearchParamsDto.getContentTypeId() != null) {
                predicates.add(cb.equal(root.get("contentType"), contentSearchParamsDto.getContentTypeId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


    @Override
    public Optional<Content> findById(Integer id) {
        return contentRepository.findContentById(id);
    }

    @Override
    public Optional<Content> findByName(String name) {
        return contentRepository.findContentByNameIgnoreCase(name);
    }

    @Override
    public List<Content> findByContentTypeId(Integer contentTypeId) {
        return contentRepository.findContentByContentTypeId(contentTypeId);
    }
}
