package com.example.MusicForum.Utils;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

public class FileUtils {

    private static final Path BASE_DIRECTORY = Paths.get("uploads").toAbsolutePath().normalize();

    public static String saveFileSafe(MultipartFile file) throws Exception {
        //Keep original name
        String originalFileName = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString(); // ← quit all path, only leaves the name
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

    public static Resource loadFileSafe(String filename) throws Exception {
        
        //Clean path
        Path filePath = BASE_DIRECTORY.resolve(filename).normalize().toAbsolutePath();

        //(Path Traversal) to read
        if (!filePath.startsWith(BASE_DIRECTORY)) {
            throw new Exception("Intento de salir de la carpeta permitida al leer.");
        }

        //Load file
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new Exception("El archivo no existe o no se puede leer.");
        }
    }

}