package com.example.cbvr.services;

import com.example.cbvr.models.VideoMetadata;
import com.example.cbvr.repositories.VideoRepository;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final GridFSBucket gridFSBucket;

    public VideoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.gridFSBucket = GridFSBuckets.create(mongoTemplate.getDb());
    }

    public List<VideoMetadata> getAllVideos() {
        return videoRepository.findAll();
    }

    public Optional<VideoMetadata> getVideoById(String id) {
        return videoRepository.findById(id);
    }

    public VideoMetadata addVideo(VideoMetadata videoMetadata) {
        return videoRepository.save(videoMetadata);
    }

    public void deleteVideo(String id) {
        videoRepository.deleteById(id);
    }

    /*public List<VideoMetadata> searchByCaption(String caption) {
        List<VideoMetadata> results = videoRepository.findByCaptionContainingRegex(caption);
        List<String> capword = results.stream().map(vm -> vm.getCaption()).collect(Collectors.toList());
        List<ObjectId> imageId = results.stream().map(vm -> vm.getImageId()).collect(Collectors.toList());
        System.out.println("Printing caption words "+capword+"and image id "+imageId+"for caption:;::"+caption);
        return results.stream().map(this::attachImageData).collect(Collectors.toList());
    }
    private VideoMetadata attachImageData(VideoMetadata metadata) {
        try {
            ObjectId objectId = metadata.getImageId();
            System.out.println("Printing the IMAGE ID " + objectId);
            GridFSFile gridFSFile = gridFSBucket.find(new org.bson.Document("_id", objectId)).first();
            if (gridFSFile != null) {
                System.out.println("Coming here with gridfs " + gridFSFile.getObjectId());
                try (InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    metadata.setImageData(outputStream.toByteArray());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Image Data length:::" + (metadata.getImageData() != null ? metadata.getImageData().length : "null"));
        return metadata;
    }*/

    public List<VideoMetadata> searchByCaption(String caption) {
        List<VideoMetadata> results = videoRepository.findByCaptionContainingRegex(caption);
        List<String> capword = results.stream().map(vm -> vm.getCaption()).collect(Collectors.toList());
        List<ObjectId> imageId = results.stream().map(vm -> vm.getImageId()).collect(Collectors.toList());
        System.out.println("Printing caption words "+capword+"and image id "+imageId+"for caption:;::"+caption);
        return results.stream().map(this::attachImageData).collect(Collectors.toList());
    }

    private VideoMetadata attachImageData(VideoMetadata metadata) {
        try {
            ObjectId objectId = metadata.getImageId();
            GridFSFile gridFSFile = gridFSBucket.find(new org.bson.Document("_id", objectId)).first();
            if (gridFSFile != null) {
                try (InputStream inputStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] imageData = outputStream.toByteArray();
                    metadata.setImageData(imageData);
                    metadata.setImageDataBase64(Base64.getEncoder().encodeToString(imageData)); // Convert to Base64
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("Image Data length:::" + (metadata.getImageData() != null ? metadata.getImageData().length : "null"));
        //System.out.println("Metadata" +metadata.toString());
        return metadata;
    }
}

