package com.et.web.rest.vm;

public class SearchPhraseStats {

    private String phrase;
    private long count;
    private double percentageOfSearch;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getPercentageOfSearch() {
        return percentageOfSearch;
    }

    public void setPercentageOfSearch(double percentageOfSearch) {
        this.percentageOfSearch = percentageOfSearch;
    }
}
