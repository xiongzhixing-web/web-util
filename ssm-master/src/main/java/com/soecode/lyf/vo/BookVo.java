package com.soecode.lyf.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class BookVo extends BaseVo{
    @NotNull(message="bookId不能为空")
    private Integer bookId;
    @NotBlank(message="bookName不能为空")
    private String  bookName;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
