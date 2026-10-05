package com.mbc.mtps.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.QnaDao;
import com.mbc.mtps.dto.QnaDto;
import com.mbc.mtps.dto.QnaParam;

@Service
public class QnaService {

	final QnaDao dao;

	public QnaService(QnaDao dao) {
		this.dao = dao;
	}
	
	
	// QNA 목록
	public List<QnaDto> qnaList(QnaParam param) {
		
		return dao.qnaList(param);
	}
	
	
	// QNA 전체 글 수
	public int getAllQna(QnaParam param) {
		
		return dao.getAllQna(param);
	}
	
	
	// QNA 글 작성
	// 회원이면 관리자/점주/일반회원 모두 작성 가능
	public boolean qnaWrite(QnaDto dto) {
		
		int count = dao.qnaWrite(dto);
		
		return count > 0;
	}
	
	
	@Transactional
	public QnaDto getQna(int seq) {

	    // 1. 해당 게시글 조회수 +1
	    dao.updateReadcount(seq);

	    // 2. 조회수가 증가된 게시글 다시 조회
	    return dao.getQna(seq);
	}


	// QNA 수정: 작성자 본인 또는 관리자만 가능
	public boolean qnaUpdate(QnaDto dto) {
		return dao.qnaUpdate(dto) > 0;
	}


	// QNA 삭제: 작성자 본인 또는 관리자만 가능
	@Transactional
	public boolean qnaDelete(int seq) {

		return dao.qnaDelete(seq) > 0;
	}
	
	
	// QNA 답글 작성
	// auth = 1 관리자만 가능
	@Transactional
	public int qnaAnswer(QnaDto dto) {
		
		// 작성자의 실제 권한을 DB에서 조회
		Integer auth = dao.getAuth(dto.getId());
		
		// 회원이 없거나 관리자가 아니면 답글 작성 불가
		if (auth == null || auth != 1) {
			return 0;
		}
		
		// 기존 답글들의 step을 뒤로 밀기
		dao.qnaAnswerUpdate(dto);
		
		// 관리자 답글 추가
		int count = dao.qnaAnswerInsert(dto);
		
		return count;
	}
	
	public List<QnaDto> getQnaAnswers(int ref) {
	    return dao.getQnaAnswers(ref);
	}
	
}
