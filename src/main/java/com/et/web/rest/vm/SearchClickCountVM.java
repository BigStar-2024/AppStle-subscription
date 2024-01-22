package com.et.web.rest.vm;

public class SearchClickCountVM {

    private String id;
    private long count;
    private String title;

    public SearchClickCountVM(String id, long count) {
        this.id = id;
        this.count = count;
    }

    public SearchClickCountVM() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
