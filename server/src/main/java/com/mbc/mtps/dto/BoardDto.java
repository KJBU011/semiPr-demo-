package com.mbc.mtps.dto;

/**
 * [게시판 DTO (Data Transfer Object)]
 * 계층 간 데이터 이동 시 사용되는 객체 (DB 테이블 구조와 1:1 매핑)
 */
public class BoardDto {

    private int boardNo;     // 게시글 번호
    private String title;    // 글 제목
    private String content;  // 글 내용
    private String id;       // 작성자 아이디
    private String regDate;  // 작성일자 (String 타입으로 변경)
    private int viewCnt;     // 조회수

    // Getter / Setter
    public int getBoardNo() { return boardNo; }
    public void setBoardNo(int boardNo) { this.boardNo = boardNo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }

    public int getViewCnt() { return viewCnt; }
    public void setViewCnt(int viewCnt) { this.viewCnt = viewCnt; }
}