package newbienewsletter.document;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class DocumentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
    @Autowired
    DocumentService documentService;
    @RequestMapping(value = "/api/documents", method = GET)
    public ResponseEntity<List<Document>> getDocuments() throws IOException, InvalidFormatException, MessagingException {
        LOGGER.info("DocumentController.getDocuments");
        List<Document> list = documentService.getAllDocuments();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/api/list")
    @ResponseBody
    public List<String> listAll() {
        LOGGER.info("DocumentController.listAll");
        return documentService.listFiles();
    }
}
