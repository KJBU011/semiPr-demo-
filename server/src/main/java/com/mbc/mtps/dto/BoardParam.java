package com.mbc.mtps.dto;

public class BoardParam {

    private int page = 1;       // 현재 페이지
    private int pageSize = 10;  // 한 페이지에 10개
    private int offset;         // 건너뛸 게시글 수
    private String keyword;     // 검색어

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}