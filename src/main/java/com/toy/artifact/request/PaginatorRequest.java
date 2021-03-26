package com.toy.artifact.request;

public class PaginatorRequest {
    protected Long page;
    protected Integer size;

    public Long getPage() {
        return page == null ? 0 : page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Integer getSize() {
        return size == null ? 15 : size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
