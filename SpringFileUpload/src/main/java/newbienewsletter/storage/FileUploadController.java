package newbienewsletter.storage;

import newbienewsletter.exceptions.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class FileUploadController {

    private final FileSystemStorageService storageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    public FileUploadController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        LOGGER.info("FileUploadController.listUploadedFiles");
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        return "uploadForm";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = GET)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        LOGGER.info("FileUploadController.serveFile");
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/", method = POST)
    public String handleFileUpload(@RequestParam("file") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws IOException {
        LOGGER.info("FileUploadController.handleFileUpload");
        for (MultipartFile file : files) {
            storageService.store(file);
        }
        if (files.size() > 1) redirectAttributes.addFlashAttribute("message",files.size() + " Dateien wurden erfolgreich hochgeladen");
        else redirectAttributes.addFlashAttribute("message",files.size() + " Datei wurde erfolgreich hochgeladen.");
        return "redirect:/";
    }

    @GetMapping("/api/deletefiles")
    public String deleteAllFiles() {
        LOGGER.info("FileUploadController.deleteAllFiles");
        storageService.deleteAll();
        return "uploadForm";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        LOGGER.info("FileUploadController.handleStorageFileNotFound");
        return ResponseEntity.notFound().build();
    }

/*
    @RequestMapping(value = "/files/deleteall", method = GET)
    @ResponseBody
    public ResponseEntity<String> getDocuments() throws IOException, InvalidFormatException {
        storageService.deleteAll();
        return new ResponseEntity<>("All files deleted!", HttpStatus.OK);
    }


    @RequestMapping(value = "/files/deleteall", method = GET)
    public String deleteAllFiles(RedirectAttributes redirectAttributes){
        storageService.deleteAll();
        redirectAttributes.addFlashAttribute("message",
                "All files deleted!");

        return "redirect:/";
    }*/

}
