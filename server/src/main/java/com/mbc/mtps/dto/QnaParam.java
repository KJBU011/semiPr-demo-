package com.mbc.mtps.dto;

import java.io.Serializable;

public class QnaParam implements Serializable {

    private String category;
    private String keyword;
    private int pageNumber;

    public QnaParam() {
    }

    public QnaParam(String category, String keyword, int pageNumber) {
        this.category = category;
        this.keyword = keyword;
        this.pageNumber = pageNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public String toString() {
        return "QnaParam [category=" + category
                + ", keyword=" + keyword
                + ", pageNumber=" + pageNumber + "]";
    }
}