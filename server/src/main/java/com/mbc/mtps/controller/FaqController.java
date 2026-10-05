package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.FaqDto;
import com.mbc.mtps.service.FaqService;

@RestController
public class FaqController {
	
	final FaqService service;
	
	FaqController(FaqService service) {
		this.service = service;
	}
	
	// FAQ 목록 조회
    @GetMapping("faqlist")
    public List<FaqDto> FaqList(String category, String keyword, int pageNum) {
        System.out.println("FaqController FaqList " + new Date());
        return service.FaqList(category, keyword, pageNum);
    }
    
    // FAQ 수
    @GetMapping("faqcount")
    public int faqCount(String category, String keyword) {
        System.out.println("FaqController faqCount" + new Date());
        return service.faqCount(category, keyword);
    }
    
    // FAQ 조회수
    @GetMapping("readfaq")
    public boolean readFaq(Integer faqNo) {
        System.out.println("FaqController readFaq " + new Date());
        return service.readFaq(faqNo);
    }
    
    // FAQ 작성
    @GetMapping("writefaq")
    public boolean writeFaq(FaqDto dto) {
        System.out.println("FaqController writeFaq " + new Date());
        return service.writeFaq(dto);
    }
    
    // FAQ 수정
    @PostMapping("updatefaq")
    public boolean updateFaq(FaqDto dto) {
        System.out.println("FaqController updateFaq " + new Date());
        return service.updateFaq(dto);
    }
    
    // FAQ 삭제
    @PostMapping("deletefaq")
    public boolean deleteFaq(FaqDto dto) {
        System.out.println("FaqController deleteFaq " + new Date());
        return service.deleteFaq(dto);
    }

}
