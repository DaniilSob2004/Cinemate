package com.example.cinemate.service.business_db.contentwarningservice;

import com.example.cinemate.dao.contentwarning.ContentWarningRepository;
import com.example.cinemate.model.db.ContentWarning;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentWarningServiceImpl implements ContentWarningService {

    private final ContentWarningRepository contentWarningRepository;

    public ContentWarningServiceImpl(ContentWarningRepository contentWarningRepository) {
        this.contentWarningRepository = contentWarningRepository;
    }

    @Override
    public void save(ContentWarning contentWarning) {
        contentWarningRepository.save(contentWarning);
    }

    @Override
    public void saveContentWarningsList(List<ContentWarning> contentWarnings) {
        contentWarningRepository.saveAll(contentWarnings);
    }

    @Override
    public void update(ContentWarning contentWarning) {
        contentWarningRepository.save(contentWarning);
    }

    @Override
    public void delete(ContentWarning contentWarning) {
        contentWarningRepository.delete(contentWarning);
    }

    @Override
    public List<ContentWarning> findAll() {
        return contentWarningRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentWarningRepository.deleteAll();
    }

    @Override
    public List<Integer> getIdWarnings(Integer contentId) {
        return contentWarningRepository.getIdWarnings(contentId);
    }

    @Override
    public void deleteByContentIdAndWarningId(Integer contentId, Integer warningId) {
        contentWarningRepository.deleteByContentIdAndWarningId(contentId, warningId);
    }
}
