package com.am.popularmoviesstageone.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerList {

    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Trailer> trailers = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override
    public String toString() {
        return "TrailerList{" +
                "id=" + id +
                ", trailers=" + trailers +
                '}';
    }
}
