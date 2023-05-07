package com.example.SEENEMA.post.view.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/v1/view-review/")
//public class TicketController {
//    @ApiOperation("티켓 인식 테스트")
//    @PostMapping(value = "/ticket", consumes = {"multipart/form-data"})
//    public String extractText(@RequestParam("ticket") MultipartFile imageFile) {
//
//        // Check if file is empty
//        if (imageFile.isEmpty()) {
//            return "Uploaded file is empty";
//        }
//
//        try {
//            // Convert MultipartFile to OpenCV Mat
//            byte[] bytes = imageFile.getBytes();
//            Mat img = Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_UNCHANGED);
//
//            // Convert image to grayscale and apply thresholding using OpenCV
//            Mat gray = new Mat();
//            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
//            Imgproc.medianBlur(gray, gray, 3);
//            Imgproc.threshold(gray, gray, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//
//            // Extract text from image using Tesseract OCR
//            ITesseract tesseract = new Tesseract();
//            tesseract.setDatapath("src/main/resources/lib/tessdata");
//            tesseract.setLanguage("kor+eng");
//
//            // Convert OpenCV Mat to BufferedImage
//            MatOfByte matOfByte = new MatOfByte();
//            Imgcodecs.imencode(".jpg", gray, matOfByte);
//            byte[] bytes2 = matOfByte.toArray();
//            BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(bytes2));
//
//            String result = null;
//            try {
//                result = tesseract.doOCR(bufImage);
//            } catch (TesseractException e) {
//                throw new RuntimeException(e);
//            }
//
//            // Check if text was extracted successfully
//            if (StringUtils.isEmpty(result)) {
//                return "Unable to extract text from the image";
//            } else {
//                return result;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error occurred while processing the image";
//        }
//    }
//}
