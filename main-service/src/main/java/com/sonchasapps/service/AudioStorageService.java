package com.sonchasapps.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AudioStorageService {

    private final GridFsTemplate gridFs;

    public AudioStorageService(GridFsTemplate gridFs) {
        this.gridFs = gridFs;
    }

    public String store(MultipartFile file) {
        try {
            ObjectId id = gridFs.store(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getContentType()
            );
            return id.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error saving file", e);
        }
    }
}

