package com.et.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.et.domain.ActivityLog} entity.
 */
public class CollectionVM implements Serializable {

    private Long id;

    private String handle;

    private String title;

    private String image;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CollectionVM{" +
            "id=" + id +
            ", handle='" + handle + '\'' +
            ", title='" + title + '\'' +
            ", image='" + image + '\'' +
            '}';
    }
}
