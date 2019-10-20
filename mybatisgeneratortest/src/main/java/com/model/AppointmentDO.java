package com.model;

public class AppointmentDO {
    private Long bookId;

    private Long studentId;

    private String linkList;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getLinkList() {
        return linkList;
    }

    public void setLinkList(String linkList) {
        this.linkList = linkList == null ? null : linkList.trim();
    }
}