package com.theironyard.repository;

import com.theironyard.entity.RssEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RssEntryRepository extends JpaRepository<RssEntry, String> {
    @Autowired
    Page<RssEntry> findByTypeInAndTitleNotLikeAndDescriptionNotLike(Collection<String> types, String exclude, String exclude2, Pageable pageable);
}

