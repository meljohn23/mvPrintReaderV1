package com.theironyard.repository;

import com.theironyard.entity.RssEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RssEntryRepository extends JpaRepository<RssEntry, String> {
    @Autowired
    Page<RssEntry> findByTypeIn(Collection<String> types, Pageable pageable);
}
