package com.mbc.mtps.dto;

public class FaqDto {
	private int faqNo;	// sequence 글번호
	
	private String faqTitle;	// 제목
	private String faqContent;	// 내용
	private String faqDate;	// 작성일
	
	private String id; // 작성자
	
	private int faqDel;		// 삭제여부 = 0 -> 1
	private int faqRead;	// 조회수
	
	public FaqDto() {
		
	}

	public FaqDto(int faqNo, String faqTitle, String faqContent, String faqDate, String id, int faqDel, int faqRead) {
		super();
		this.faqNo = faqNo;
		this.faqTitle = faqTitle;
		this.faqContent = faqContent;
		this.faqDate = faqDate;
		this.id = id;
		this.faqDel = faqDel;
		this.faqRead = faqRead;
	}

	public int getFaqNo() {
		return faqNo;
	}

	public void setFaqNo(int faqNo) {
		this.faqNo = faqNo;
	}

	public String getFaqTitle() {
		return faqTitle;
	}

	public void setFaqTitle(String faqTitle) {
		this.faqTitle = faqTitle;
	}

	public String getFaqContent() {
		return faqContent;
	}

	public void setFaqContent(String faqContent) {
		this.faqContent = faqContent;
	}

	public String getFaqDate() {
		return faqDate;
	}

	public void setFaqDate(String faqDate) {
		this.faqDate = faqDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFaqDel() {
		return faqDel;
	}

	public void setFaqDel(int faqDel) {
		this.faqDel = faqDel;
	}

	public int getFaqRead() {
		return faqRead;
	}

	public void setFaqRead(int faqRead) {
		this.faqRead = faqRead;
	}
	
	
}
