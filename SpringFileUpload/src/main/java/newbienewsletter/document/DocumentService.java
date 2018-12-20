package newbienewsletter.document;

import newbienewsletter.details.DetailsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    DetailsService detailsService;
    @Autowired
    private Environment env;

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);

    public List<Document> getAllDocuments() throws IOException, InvalidFormatException, MessagingException {
        LOGGER.info("DocumentService.getAllDocuments");
        List<String> files = listFiles();
        List<Document> documents = new ArrayList<>();
        for (String file : files) {
            documents.add(getDocumentFromFile(file));
        }
        return documents;
    }

    public Document getDocumentFromFile(String file) throws IOException, InvalidFormatException {
        LOGGER.info("DocumentService.getDocumentFromFile");
        Document doc = new Document();
        doc.setFilename(file);
        doc.setDetails(detailsService.getDetailsFromDocument(doc));
        return doc;
    }

    public List<String> listFiles() {
        LOGGER.info("DocumentService.listFiles");
        String dirPath = env.getProperty("rootlocation");
        File dir = new File(dirPath);
        return Arrays.asList(dir.list());
    }

    public String getDocumentContent(Document document) throws IOException, InvalidFormatException {
        LOGGER.info("DocumentService.getDocumentContent");
        FileInputStream fis = new FileInputStream(env.getProperty("rootlocation") + "/" + document.getFilename());
        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
        return stripSpecialCharacter(extractor.getText());
    }

    public String stripSpecialCharacter(String input) {
        LOGGER.info("DocumentService.stripSpecialCharacter");
        input = StringUtils.replace(input, "\r", "");
        input = StringUtils.replace(input, "\t", "");
        input = StringUtils.replace(input, "\n", "");
        return input;
    }
}
