package com.mins.application.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mins.application.dao.Member;
import com.mins.application.dao.SearchHistory;
import com.mins.application.dao.SearchHistoryRepository;

@Service
public class SearchHistoryService {
	@Autowired
	SearchHistoryRepository seaerchHistoryRepository;

	@Transactional
	public void save(SearchHistory entity) {
		seaerchHistoryRepository.save(entity);
	}

	public Page<SearchHistory> findByMember(Member member, Pageable pageable) {
		//
		return seaerchHistoryRepository.findByMember(member, pageable);
	}
	
	public List<Map<String, Object>> findSurveyCount() {
		//
		return seaerchHistoryRepository.findSurveyCount();
	}

}
