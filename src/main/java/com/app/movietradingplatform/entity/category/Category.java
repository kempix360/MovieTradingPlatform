package com.app.movietradingplatform.entity.category;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Category<T> implements Serializable {
    private String name;
    private List<T> elements;

    public Category(String name, List<T> elements) {
        this.name = name;
        this.elements = elements;
    }

    public int getCount() {
        return elements != null ? elements.size() : 0;
    }
}
