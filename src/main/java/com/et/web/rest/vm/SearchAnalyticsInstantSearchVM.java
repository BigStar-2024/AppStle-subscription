package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class SearchAnalyticsInstantSearchVM {

    private long totalProductClickCount;
    private long totalCategoryClickCount;
    private long totalSuggestionClickCount;
    private long totalPageClickCount;
    private List<SearchClickCountVM> pageClicks = new ArrayList<>();
    private List<SearchClickCountVM> productClicks = new ArrayList<>();
    private List<SearchClickCountVM> suggestionClicks = new ArrayList<>();
    private List<SearchClickCountVM> categoryClicks = new ArrayList<>();


    public void setTotalProductClickCount(long totalProductClickCount) {
        this.totalProductClickCount = totalProductClickCount;
    }

    public long getTotalProductClickCount() {
        return totalProductClickCount;
    }


    public void setTotalCategoryClickCount(long totalCategoryClickCount) {
        this.totalCategoryClickCount = totalCategoryClickCount;
    }

    public long getTotalCategoryClickCount() {
        return totalCategoryClickCount;
    }

    public void setTotalSuggestionClickCount(long totalSuggestionClickCount) {
        this.totalSuggestionClickCount = totalSuggestionClickCount;
    }

    public long getTotalSuggestionClickCount() {
        return totalSuggestionClickCount;
    }

    public void setTotalPageClickCount(long totalPageClickCount) {
        this.totalPageClickCount = totalPageClickCount;
    }

    public long getTotalPageClickCount() {
        return totalPageClickCount;
    }

    public List<SearchClickCountVM> getPageClicks() {
        return pageClicks;
    }

    public void setPageClicks(List<SearchClickCountVM> pageClicks) {
        this.pageClicks = pageClicks;
    }

    public List<SearchClickCountVM> getProductClicks() {
        return productClicks;
    }

    public void setProductClicks(List<SearchClickCountVM> productClicks) {
        this.productClicks = productClicks;
    }

    public List<SearchClickCountVM> getSuggestionClicks() {
        return suggestionClicks;
    }

    public void setSuggestionClicks(List<SearchClickCountVM> suggestionClicks) {
        this.suggestionClicks = suggestionClicks;
    }

    public List<SearchClickCountVM> getCategoryClicks() {
        return categoryClicks;
    }

    public void setCategoryClicks(List<SearchClickCountVM> categoryClicks) {
        this.categoryClicks = categoryClicks;
    }
}
