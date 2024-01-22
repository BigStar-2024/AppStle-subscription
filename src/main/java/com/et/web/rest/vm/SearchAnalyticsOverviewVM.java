package com.et.web.rest.vm;

import java.util.ArrayList;
import java.util.List;

public class SearchAnalyticsOverviewVM {

    private long totalSearches;
    private List<SearchPhraseStats> topSearchQueries = new ArrayList<>();
    private List<SearchPhraseStats> topSearchWithNoResults = new ArrayList<>();
    private List<SuggestionSearchStats> suggestionSearchStatsList = new ArrayList<>();
    private List<ProductBoughtStats> productBoughtStatsList = new ArrayList<>();
    private String totalSalesGenerated;
    private double totalSalesGeneratedNumeric;

    public void setTotalSearches(long totalSearches) {
        this.totalSearches = totalSearches;
    }

    public long getTotalSearches() {
        return totalSearches;
    }

    public void setTopSearchQueries(List<SearchPhraseStats> topSearchQueries) {
        this.topSearchQueries = topSearchQueries;
    }

    public List<SearchPhraseStats> getTopSearchQueries() {
        return topSearchQueries;
    }

    public void setTopSearchWithNoResults(List<SearchPhraseStats> topSearchWithNoResults) {
        this.topSearchWithNoResults = topSearchWithNoResults;
    }

    public List<SearchPhraseStats> getTopSearchWithNoResults() {
        return topSearchWithNoResults;
    }


    public void setSuggestionSearchStatsList(List<SuggestionSearchStats> suggestionSearchStatsList) {
        this.suggestionSearchStatsList = suggestionSearchStatsList;
    }

    public List<SuggestionSearchStats> getSuggestionSearchStatsList() {
        return suggestionSearchStatsList;
    }

    public void setProductBoughtStatsList(List<ProductBoughtStats> productBoughtStatsList) {
        this.productBoughtStatsList = productBoughtStatsList;
    }

    public List<ProductBoughtStats> getProductBoughtStatsList() {
        return productBoughtStatsList;
    }

    public void setTotalSalesGenerated(String totalSalesGenerated) {
        this.totalSalesGenerated = totalSalesGenerated;
    }

    public String getTotalSalesGenerated() {
        return totalSalesGenerated;
    }

    public void setTotalSalesGeneratedNumeric(double totalSalesGeneratedNumeric) {
        this.totalSalesGeneratedNumeric = totalSalesGeneratedNumeric;
    }

    public double getTotalSalesGeneratedNumeric() {
        return totalSalesGeneratedNumeric;
    }
}
