package newbienewsletter.details;

import newbienewsletter.document.Document;
import newbienewsletter.document.DocumentService;
import newbienewsletter.exceptions.PatternNotFoundException;
import newbienewsletter.storage.FileSystemStorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DetailsService {

    @Autowired
    FileSystemStorageService storageService;
    @Autowired
    DocumentService documentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DetailsService.class);
    private static Document currentDocument;

    public Details getDetailsFromDocument(Document document) throws IOException, InvalidFormatException {
        LOGGER.info("DetailsService.getDetailsFromDocument");
        currentDocument = document;
        Details details = new Details();
        details.setPosition(matchStringtoDetails("Position:", "Nachname:"));
        details.setNachname(matchStringtoDetails("Nachname:", "Vorname:"));
        details.setVorname(matchStringtoDetails("Vorname:", "Zuordnung Division / Department:"));
        details.setDepartment(matchStringtoDetails("Zuordnung Division / Department:", "Zuordnung Team:"));
        details.setTeam(matchStringtoDetails("Zuordnung Team:", "Zuordnung Führungskraft:"));
        details.setSupervisor(matchStringtoDetails("Zuordnung Führungskraft:", "Zugeordneter Mentor:"));
        details.setMentor(matchStringtoDetails("Zugeordneter Mentor:", "Text für Newbie E-Mail:"));
        details.setText(matchStringtoDetails("Text für Newbie E-Mail:", "Anmerkungen Sonstiges:"));
        return details;
    }

    public String matchStringtoDetails(String pattern1, String pattern2) {
        LOGGER.info("DetailsService.matchStringtoDetails");
        try {
            Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
            Matcher m = p.matcher(documentService.getDocumentContent(currentDocument));
            if (m.find(1)) {
                LOGGER.info("Regex Match");
                return m.group(1);
            }
            throw new PatternNotFoundException();
        } catch (PatternNotFoundException | InvalidFormatException | IOException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}