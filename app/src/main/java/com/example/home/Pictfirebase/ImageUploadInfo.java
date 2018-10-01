package com.example.home.Pictfirebase;

/**
 * Created by home on 10/1/2018.
 */

public class ImageUploadInfo
{
    String title,description,image;


    public ImageUploadInfo() {
    }

    public ImageUploadInfo(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
