package com.kkobugi.puremarket.common.gcs;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GCSService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    // 이미지 업로드
    @SuppressWarnings("deprecation")
    public String uploadImage(String folderName, MultipartFile multipartFile) throws IOException {

        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String fullPath = folderName+"/"+fileName;
        String type = multipartFile.getContentType();

        storage.create(BlobInfo.newBuilder(bucketName, fullPath)
                        .setContentType(type)
                        .build(), multipartFile.getInputStream());
        return fullPath;
    }

    // 이미지 삭제
    public boolean deleteImage(String imageUrl) {
        String fullPath = imageUrl.substring(("https://storage.googleapis.com/"+bucketName+"/").length());
        return storage.delete(bucketName, fullPath);
    }
}
