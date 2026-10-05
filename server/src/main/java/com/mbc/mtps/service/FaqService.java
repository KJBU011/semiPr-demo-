package com.mbc.mtps.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.FaqDao;
import com.mbc.mtps.dto.FaqDto;

@Service
@Transactional
public class FaqService {
	
	final FaqDao dao;
	
	FaqService(FaqDao dao) {
		this.dao = dao;
	}
	
	// FAQ 목록 조회
    public List<FaqDto> FaqList(String category, String keyword, int pageNum) {
        return dao.FaqList(category, keyword, pageNum);
    }
    
    // FAQ 글 수
    public int faqCount(String category, String keyword) {
        return dao.faqCount(category, keyword);
    }
    
    // FAQ 조회수
    public boolean readFaq(Integer faqNo) {

        int count = dao.readFaq(faqNo);
        
        return count > 0 ? true : false;
    }
    
    // FAQ 작성
    public boolean writeFaq(FaqDto dto) {

        int count = dao.writeFaq(dto);
        
        return count > 0 ? true : false;
    }
    
    // FAQ 수정
    public boolean updateFaq(FaqDto dto) {

        int count = dao.updateFaq(dto);
        
        return count > 0 ? true : false;
    }
    
    // FAQ 삭제
    public boolean deleteFaq(FaqDto dto) {

        int count = dao.deleteFaq(dto);
        
        return count > 0 ? true : false;
    }
}
