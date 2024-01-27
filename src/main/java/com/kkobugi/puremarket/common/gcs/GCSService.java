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
    public BlobInfo uploadImage(MultipartFile multipartFile) throws IOException {

        String fileName = UUID.randomUUID() + multipartFile.getOriginalFilename();
        String type = multipartFile.getContentType();

        return storage.create(
                BlobInfo.newBuilder(bucketName, fileName)
                        .setContentType(type)
                        .build(),
                multipartFile.getInputStream());
//        InputStream content = new ByteArrayInputStream(multipartFile.getBytes());
//        BlobId blobId = BlobId.of(bucketName, fileName);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(type).build();
//        Blob blob = storage.create(blobInfo, content);
    }
}
