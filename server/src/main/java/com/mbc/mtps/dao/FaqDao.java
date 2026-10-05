package com.mbc.mtps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.FaqDto;

@Mapper
@Repository
public interface FaqDao {
	
	// FAQ 목록 조회
    List<FaqDto> FaqList(@Param("category") String category,
                                      @Param("keyword") String keyword,
                                      @Param("pageNum") int pageNum);
    
    // FAQ 수
    int faqCount(@Param("category") String category,
                                      @Param("keyword") String keyword);
    
    // FAQ 조회수
    int readFaq(@Param("faqNo") Integer faqNo);
    
    // FAQ 작성
    int writeFaq(FaqDto dto);
    
    // FAQ 수정
    int updateFaq(FaqDto dto);
    
    // FAQ 삭제
    int deleteFaq(FaqDto dto);
}
