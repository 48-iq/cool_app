package ru.kuchko.cool_app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kuchko.cool_app.entities.ImageEntity;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.ImageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final UuidService uuidService;
    private final ImageRepository imageRepository;
    private static final String OUTPUT_STREAM_CLOSE_ER_MESSAGE = "output stream IOException on close action";

    @Value("${app.images.default.filepath}")
    private String filepath;

    public ImageEntity save(MultipartFile image) {
        String filename = uuidService.generate();
        OutputStream os = null;
        try {
            if (!Files.exists(Path.of(filepath))) {
                Files.createDirectory(Path.of(filepath));
            }
            if (!Files.exists(Path.of( filepath + "/" + filename))) {
                Files.createFile(Path.of(filepath + "/" + filename));
            }
            os = new FileOutputStream(filepath + "/" + filename);
            os.write(image.getBytes());
            ImageEntity imageEntity = ImageEntity.builder()
                    .path(filepath)
                    .build();
            ImageEntity savedImage = imageRepository.save(imageEntity);
            return savedImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                os.close();
            } catch (IOException | NullPointerException e) {
                log.error(OUTPUT_STREAM_CLOSE_ER_MESSAGE);
            }
        }
    }

    public Resource get(Integer imageId) {
        Optional<ImageEntity> imageOptional = imageRepository.findById(imageId);
        if (imageOptional.isEmpty())
            throw new EntityNoFoundByIdException("image", imageId);
        ImageEntity image = imageOptional.get();
        try {
            InputStream is = new FileInputStream(image.getPath());
            return new InputStreamResource(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Integer imageId, MultipartFile image) {
        Optional<ImageEntity> imageOptional = imageRepository.findById(imageId);
        if (imageOptional.isEmpty())
            throw new EntityNoFoundByIdException("image", imageId);
        ImageEntity imageEntity = imageOptional.get();
        OutputStream os = null;
        try {
            os = new FileOutputStream(imageEntity.getPath());
            os.write(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                os.close();
            } catch (IOException | NullPointerException e) {
                log.error(OUTPUT_STREAM_CLOSE_ER_MESSAGE);
            }
        }
    }

    public void delete(Integer imageId) {
        Optional<ImageEntity> imageOptional = imageRepository.findById(imageId);
        if (imageOptional.isPresent()) {
            ImageEntity image = imageOptional.get();
            try {
                if (Files.exists(Path.of(image.getPath())))
                    Files.delete(Path.of(image.getPath()));
                imageRepository.deleteById(image.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteAll(List<Integer> imageIdList) {
        for (Integer imageId: imageIdList) {
            delete(imageId);
        }
    }
}
