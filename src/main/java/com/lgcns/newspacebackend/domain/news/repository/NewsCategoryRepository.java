package com.lgcns.newspacebackend.domain.news.repository;

import com.lgcns.newspacebackend.domain.news.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

}
