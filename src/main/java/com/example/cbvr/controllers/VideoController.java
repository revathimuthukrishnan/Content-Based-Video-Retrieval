package com.example.cbvr.controllers;

import com.example.cbvr.models.VideoMetadata;
import com.example.cbvr.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoController {

    @Autowired
    private VideoService videoService;


    @GetMapping
    public List<VideoMetadata> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public Optional<VideoMetadata> getVideoById(@PathVariable String id) {
        return videoService.getVideoById(id);
    }

    @PostMapping
    public VideoMetadata addVideo(@RequestBody VideoMetadata videoMetadata) {
        return videoService.addVideo(videoMetadata);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable String id) {
        videoService.deleteVideo(id);
    }

  /*  @GetMapping("/search")
    public ResponseEntity<List<VideoMetadata>> searchVideos(@RequestParam("query") String query) {
        List<VideoMetadata> results = videoService.searchByCaption(query);
        System.out.println("Results:::"+results.stream().map(vv -> vv.getImageData()));
        return new ResponseEntity<>(results, HttpStatus.OK);
    }*/

/*    @GetMapping("/search")
    public ResponseEntity<List<VideoMetadata>> searchVideos(@RequestParam("query") String query) {
        List<VideoMetadata> results = videoService.searchByCaption(query);

        // Convert image data to Base64 string for each result
        List<VideoMetadata> base64Results = results.stream().map(metadata -> {
            if (metadata.getImageData() != null) {
                metadata.setImageData(Base64.getEncoder().encode(metadata.getImageData()));
            }
            return metadata;
        }).collect(Collectors.toList());

        System.out.println("Results:::" + results.stream().map(vv -> new String(vv.getImageData() != null ? vv.getImageData() : new byte[0])));
        return new ResponseEntity<>(base64Results, HttpStatus.OK);
    }*/

    @GetMapping("/search")
    public ResponseEntity<List<VideoMetadata>> searchVideos(@RequestParam("query") String query) {
        List<VideoMetadata> results = videoService.searchByCaption(query);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}

