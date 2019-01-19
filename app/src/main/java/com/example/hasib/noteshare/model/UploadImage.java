package com.example.hasib.noteshare.model;

public class UploadImage {
    private String name;
    private String imageUri;

    public UploadImage()
    {

    }
    public UploadImage(String name, String imageUri)
    {

        if(name.trim().equals(""))
        {
            name="noname";
        }
        this.name=name;
        this.imageUri=imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
