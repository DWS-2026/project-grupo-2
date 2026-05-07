package com.example.MusicForum.Utils;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtils {

    private static final Path BASE_DIRECTORY = Paths.get("uploads").toAbsolutePath().normalize();

    public static String saveFileSafe(MultipartFile file) throws Exception {
        //Keep original name
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        
        //Clean path
        Path destinationPath = BASE_DIRECTORY.resolve(originalFileName).normalize();

        //(Path Traversal)
        //If the name has"../", remove it
        if (!destinationPath.startsWith(BASE_DIRECTORY)) {
            throw new Exception("Intento de salir de la carpeta permitida.");
        }

        //Crete folder if !exists
        if (!Files.exists(BASE_DIRECTORY)) {
            Files.createDirectories(BASE_DIRECTORY);
        }

        //Save file
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        return originalFileName;
    }
}