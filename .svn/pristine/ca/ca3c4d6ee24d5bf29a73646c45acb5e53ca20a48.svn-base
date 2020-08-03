package com.hyva.restopos.rest.pojo;

import java.util.List;

public class BasePojo {
    private int pageNo;
    private  int pageSize;
    private boolean firstPage;
    private boolean lastPage;
    private boolean nextPage;
    private boolean prevPage;
    private boolean filterApplied;
    private String fromDates;
    private String toDate;
    private List list;
    private boolean status;

    public String getFromDates() {
        return fromDates;
    }

    public void setFromDates(String fromDates) {
        this.fromDates = fromDates;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    private String selectedFilter="none";

    public String getSelectedFilter() {
        return selectedFilter;
    }

    public void setSelectedFilter(String selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    public BasePojo(int pageNo, boolean firstPage, boolean lastPage, boolean nextPage, boolean prevPage) {
        this.pageNo = pageNo;
        this.firstPage = firstPage;
        this.lastPage = lastPage;
        this.nextPage = nextPage;
        this.prevPage = prevPage;
    }

    public boolean isFilterApplied() {
        return filterApplied;
    }

    public void setFilterApplied(boolean filterApplied) {
        this.filterApplied = filterApplied;
    }

    public BasePojo(){}

    /**
     * Sets new pageSize.
     *
     * @param pageSize New value of pageSize.
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Gets pageSize.
     *
     * @return Value of pageSize.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Gets pageNo.
     *
     * @return Value of pageNo.
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * Sets new pageNo.
     *
     * @param pageNo New value of pageNo.
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * Sets new firstPage.
     *
     * @param firstPage New value of firstPage.
     */
    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * Gets lastPage.
     *
     * @return Value of lastPage.
     */
    public boolean isLastPage() {
        return lastPage;
    }

    /**
     * Sets new lastPage.
     *
     * @param lastPage New value of lastPage.
     */
    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * Gets firstPage.
     *
     * @return Value of firstPage.
     */
    public boolean isFirstPage() {
        return firstPage;
    }

    /**
     * Sets new prevPage.
     *
     * @param prevPage New value of prevPage.
     */
    public void setPrevPage(boolean prevPage) {
        this.prevPage = prevPage;
    }

    /**
     * Gets nextPage.
     *
     * @return Value of nextPage.
     */
    public boolean isNextPage() {
        return nextPage;
    }

    /**
     * Sets new nextPage.
     *
     * @param nextPage New value of nextPage.
     */
    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * Gets prevPage.
     *
     * @return Value of prevPage.
     */
    public boolean isPrevPage() {
        return prevPage;
    }
}
