package com.example.cbvr.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;

@Document(collection = "images")
public class VideoMetadata {

    @Id
    private ObjectId id; // This is the document ID in the images collection
    @Field("image_id") // Explicitly map the image_id field
    private ObjectId imageId; // This corresponds to the _id in fs.files collection
    @Field("image_file")
    private String imageFile; // This is the filename in fs.files collection
    private String caption;
    private float[] imageEmbedding;
    private float[] textEmbedding;

    private byte[] imageData;

    private String imageDataBase64;

    public String getImageDataBase64() {
        return imageDataBase64;
    }

    public void setImageDataBase64(String imageDataBase64) {
        this.imageDataBase64 = imageDataBase64;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }



    // Getters and setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getImageId() {
        return imageId;
    }

    public void setImageId(ObjectId imageId) {
        this.imageId = imageId;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public float[] getImageEmbedding() {
        return imageEmbedding;
    }

    public void setImageEmbedding(float[] imageEmbedding) {
        this.imageEmbedding = imageEmbedding;
    }

    public float[] getTextEmbedding() {
        return textEmbedding;
    }

    public void setTextEmbedding(float[] textEmbedding) {
        this.textEmbedding = textEmbedding;
    }

    @Override
    public String toString() {
        return "VideoMetadata{" +
                "id='" + id + '\'' +
                ", imageId=" + imageId +
                ", filename='" + imageFile + '\'' +
                ", caption='" + caption + '\'' +
                ", imageEmbedding=" + Arrays.toString(imageEmbedding) +
                ", textEmbedding=" + Arrays.toString(textEmbedding) +
                ", imageData=" + Arrays.toString(imageData) +
                ", imageDataBase64=" +imageDataBase64 + '\'' +
                '}';
    }
}
