package com.bayneno.backen_manage_property.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.Base64;
import com.bayneno.backen_manage_property.payload.request.FileRequest;
import com.bayneno.backen_manage_property.payload.response.FileResponse;
import com.bayneno.backen_manage_property.properties.AwsProp;
import com.bayneno.backen_manage_property.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    FileStorageService storageService;

    @Autowired
    private AmazonS3 s3client;

    @Autowired
    private AwsProp awsProp;

//    @PostMapping("/file/upload")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
//
//        String message = "";
//        try {
//            storageService.save(file);
//
//            message = "Uploaded the file successfully: " + file.getOriginalFilename();
//            return ResponseEntity.status(HttpStatus.OK).body(message);
//        } catch (Exception e) {
//            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
//        }
//    }

    @PostMapping("v2/file/upload")
    public FileResponse uploadV2(@RequestBody FileRequest fileRequest) throws IOException {

        UUID uuid = UUID.randomUUID();
        String pathName = String.format("%s/%s", uuid.toString(), Objects.requireNonNull(fileRequest.getName()).replaceAll("\\s+","").trim());

        String imageDataBytes = fileRequest.getPath().substring(fileRequest.getPath().indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes()));
        s3client.putObject(awsProp.getBucketName(), pathName, stream, new ObjectMetadata());

        return FileResponse
                .builder()
                .id(pathName)
                .name(Objects.requireNonNull(fileRequest.getName()).replaceAll("\\s+","").trim())
                .path(awsProp.getUrl() + pathName)
                .build();
    }

//    @PostMapping("file/upload")
//    public FileResponse upload(@RequestParam("files") MultipartFile[] files) throws IOException {
//
//        UUID uuid = UUID.randomUUID();
//        String pathName = String.format("%s/%s", uuid.toString(), Objects.requireNonNull(files[0].getOriginalFilename()).replaceAll("\\s+","").trim());
//
//        s3client.putObject(awsProp.getBucketName(), pathName, files[0].getInputStream(), new ObjectMetadata());
//
//        return FileResponse
//                .builder()
//                .id(pathName)
//                .name(Objects.requireNonNull(files[0].getOriginalFilename()).replaceAll("\\s+","").trim())
//                .url(awsProp.getUrl() + pathName)
//                .build();
//    }

    @PostMapping("file/delete")
    public String upload(@RequestParam("id") String pathName) {

        s3client.deleteObject(awsProp.getBucketName(), pathName);

        return "Delete Success";
    }

}
