package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.QnaDto;
import com.mbc.mtps.dto.QnaParam;
import com.mbc.mtps.service.QnaService;

@RestController
public class QnaController {

	final QnaService service;

	public QnaController(QnaService service) {
		this.service = service;
	}
	
//================================== QNA 페이지 ======================================
	
	// QNA 목록
	@GetMapping("qnalist")
	public List<QnaDto> qnaList(QnaParam param) {
		System.out.println("QnaController qnaList() " + new Date());
		
		return service.qnaList(param);
	}
	
	
	// QNA 전체 글 수
	@GetMapping("qnacount")
	public int qnaCount(QnaParam param) {
		System.out.println("QnaController qnaCount() " + new Date());
		
		return service.getAllQna(param);
	}
	
	
	// QNA 작성
	@PostMapping("qnawrite")
	public boolean qnaWrite(QnaDto dto) {
		System.out.println("QnaController qnaWrite() " + new Date());
		
		return service.qnaWrite(dto);
	}
	
	
	// QNA 상세보기
	@GetMapping("qnadetail")
	public QnaDto qnaDetail(int seq) {
		System.out.println("QnaController qnaDetail() " + new Date());
		
		return service.getQna(seq);
		

	}


	// QNA 수정
	@PostMapping("qnaupdate")
	public boolean qnaUpdate(QnaDto dto) {
		System.out.println("QnaController qnaUpdate() " + new Date());

		return service.qnaUpdate(dto);
	}


	// QNA 삭제
	@PostMapping("qnadelete")
	public boolean qnaDelete(int seq) {
		System.out.println("QnaController qnaDelete() " + new Date());

		return service.qnaDelete(seq);
	}
	
	
	// QNA 답변 작성
	@PostMapping("qnaanswer")
	public boolean qnaAnswer(QnaDto dto) {
		System.out.println("QnaController qnaAnswer() " + new Date());
		
		int count = service.qnaAnswer(dto);
		
		return count > 0;
	}
	
	@GetMapping("qnaanswers")
	public List<QnaDto> qnaAnswers(int ref) {
	    System.out.println("QnaController qnaAnswers() " + new Date());

	    return service.getQnaAnswers(ref);
	}
}
	
	
