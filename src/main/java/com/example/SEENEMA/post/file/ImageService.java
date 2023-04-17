package com.example.SEENEMA.post.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ImageService {

    private final AmazonS3 amazonS3Client;

    private String S3Bucket = "seenema-github-actions-s3-bucket";

    public List<Image> uploadFiles(List<MultipartFile> files) {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalName = file.getOriginalFilename(); // 파일 이름
            long size = file.getSize(); // 파일 크기

            ObjectMetadata objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(file.getContentType());
            objectMetaData.setContentLength(size);

            try {
                // S3에 업로드
                amazonS3Client.putObject(
                        new PutObjectRequest(S3Bucket, originalName, file.getInputStream(), objectMetaData)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );

                String imgUrl = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기

                Image image = new Image();
                image.setImgUrl(imgUrl);
                images.add(image);

            } catch (IOException e) {
            }
        }

        return images;
    }
}