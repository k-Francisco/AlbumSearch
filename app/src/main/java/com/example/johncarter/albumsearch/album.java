package com.example.johncarter.albumsearch;

/**
 * Created by john carter on 2/12/2017.
 */

public class album {
    private String name;
    private String artist;
    private String url;
    private String image;

    public album() {
    }

    public album(String name, String artist, String url, String image) {
        this.name = name;
        this.artist = artist;
        this.url = url;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
