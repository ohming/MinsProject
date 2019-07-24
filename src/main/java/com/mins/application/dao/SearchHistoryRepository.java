package com.mins.application.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

	@Query("SELECT s FROM SearchHistory s WHERE s.member=?1")
	Page<SearchHistory> findByMember(Member member, Pageable pageable);

	@Query("SELECT count(b) as cnt, b.search_word as search_word FROM SearchHistory b GROUP BY b.search_word ORDER BY cnt DESC")
	List<Map<String, Object>> findSurveyCount();
}
 