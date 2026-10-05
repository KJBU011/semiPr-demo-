package com.mbc.mtps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.QnaDto;
import com.mbc.mtps.dto.QnaParam;

@Mapper
@Repository
public interface QnaDao {

    // QnA 목록
    List<QnaDto> qnaList(QnaParam param);

    // 전체 개수
    int getAllQna(QnaParam param);

    // 글쓰기
    int qnaWrite(QnaDto dto);

    // 상세조회
    QnaDto getQna(int seq);

    // QNA 수정
    int qnaUpdate(QnaDto dto);

    // QNA 삭제
    int qnaDelete(int seq);

    // ==================== 조회수 증가 ====================
    int updateReadcount(int seq);

    // 답변 조회
    List<QnaDto> getQnaAnswers(int ref);

    // 회원 권한 조회
    Integer getAuth(String id);

    // 답변 step 조정
    int qnaAnswerUpdate(QnaDto dto);

    // 답변 작성
    int qnaAnswerInsert(QnaDto dto);
}
