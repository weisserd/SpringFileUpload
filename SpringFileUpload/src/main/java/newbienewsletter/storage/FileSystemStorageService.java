package newbienewsletter.storage;

import newbienewsletter.exceptions.StorageException;
import newbienewsletter.exceptions.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService {

    @Autowired
    private Environment env;

    private Path root;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemStorageService.class);

    public void store(MultipartFile file) {
        LOGGER.info("FileSystemStorageService.store()");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new StorageException(
                    "Cannot store file with relative path outside current directory " + filename);
        }
        if (filename.contains(".doc") == false && filename.contains(".docx") == false) {
            // This is a security check
            throw new StorageException(
                    "Wrong format for file: " + filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, root.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    public Stream<Path> loadAll() {
        LOGGER.info("FileSystemStorageService.loadAll");
        try {
            return Files.walk(root, 1)
                    .filter(path -> !path.equals(root))
                    .map(root::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Path load(String filename) {
        return root.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        LOGGER.info("FileSystemStorageService.loadAsResource");
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public void deleteAll() {
        LOGGER.info("FileSystemStorageService.deleteAll");
        FileSystemUtils.deleteRecursively(root.toFile());
        init();
    }

    @PostConstruct
    public void init() {
        root = Paths.get(env.getProperty("rootlocation"));
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
