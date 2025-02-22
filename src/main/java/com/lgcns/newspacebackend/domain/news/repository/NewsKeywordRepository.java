package com.lgcns.newspacebackend.domain.news.repository;

import com.lgcns.newspacebackend.domain.news.entity.NewsKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsKeywordRepository extends JpaRepository<NewsKeyword, Long> {
}
