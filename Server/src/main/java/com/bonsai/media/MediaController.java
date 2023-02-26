package com.bonsai.media;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MediaController {
    @PostMapping("/api/uploadImage")
    public List<String> uploadImage(@RequestParam("image") List<MultipartFile> files){
        try {
            String uri = ResourceUtils.getFile("classpath:static/images").getAbsolutePath();
            Path path = Paths.get(uri);
            List<String> paths = new ArrayList<>();
            for(MultipartFile file : files){
                String fileName = UUID.randomUUID().toString();
                Files.copy(file.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                String url = "/resource/images/"+fileName;
                paths.add(url);
            }
            return paths;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
