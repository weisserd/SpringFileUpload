package newbienewsletter.details;

import newbienewsletter.details.Details;
import newbienewsletter.details.DetailsService;
import newbienewsletter.document.Document;
import newbienewsletter.document.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.regex.Matcher;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) //nachschauen
public class DetailsServiceTest {

    @InjectMocks //nachschauen
            DetailsService detailsService;

    @Mock
    Environment env;

    @Mock
    DocumentService documentService = mock(DocumentService.class);

    @Before
    public void init() {
        when(env.getProperty("rootlocation"))
                .thenReturn("src/test/resources/testfiles");
    }

    @Test
    public void matchStringtoDetailsisOK() throws Exception {

        when(documentService.getDocumentContent(any())).thenReturn("this is a teststring with a keyword and some text between the second drowyek");
        String pattern1 = "keyword";
        String pattern2 = "drowyek";
        assertEquals(" and some text between the second ",detailsService.matchStringtoDetails(pattern1, pattern2));
    }

    @Test
    public void matchStringtoDetailsnoMatch() throws Exception {

        when(documentService.getDocumentContent(any())).thenReturn("teststringwithnoregexmatch");
        assertEquals(null,detailsService.matchStringtoDetails("1","2"));
    }

   /*
    @Test
    public void getDetailsFromDocumentRegexMatchisOK() throws Exception {
        Document docexpected = mock(Document.class);
        when(docexpected.getDetails().getNachname())
                .thenReturn("Hier wird der Nachname gematcht");
        when(docexpected.getDetails().getVorname())
                .thenReturn("Hier wird der Vorname gematcht");
        when(docexpected.getDetails().getDepartment())
                .thenReturn("Hier kommt das Department oder die Division");
        when(docexpected.getDetails().getTeam())
                .thenReturn("An dieser Stelle wird das Team angegeben");
        when(docexpected.getDetails().getSupervisor())
                .thenReturn("Wer ist die Führungskraft?");
        when(docexpected.getDetails().getMentor())
                .thenReturn("Mentor is who?");
        when(docexpected.getDetails().getText())
                .thenReturn("startTVorstellung der neues mitarbeiters lorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsum jetzt sind 3 Zeilen voll");

        detailsexpected.setNachname("Hier wird der Nachname gematcht");
        detailsexpected.setVorname("Hier wird der Vorname gematcht");
        detailsexpected.setDepartment("Hier kommt das Department oder die Division");
        detailsexpected.setTeam("An dieser Stelle wird das Team angegeben");
        detailsexpected.setSupervisor("Wer ist die Führungskraft?");
        detailsexpected.setMentor("Mentor is who?");
        detailsexpected.setText("startTVorstellung der neues mitarbeiters lorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsumlorem ipsum jetzt sind 3 Zeilen voll");
        Document docexpected = new Document();
        docexpected.setDetails(detailsexpected);

        assertEquals(docexpected.getDetails().getNachname(), docactual.getDetails().getNachname());
        assertEquals(docexpected.getDetails().getVorname(), docactual.getDetails().getVorname());
        assertEquals(docexpected.getDetails().getDepartment(), docactual.getDetails().getDepartment());
        assertEquals(docexpected.getDetails().getTeam(), docactual.getDetails().getTeam());
        assertEquals(docexpected.getDetails().getSupervisor(), docactual.getDetails().getSupervisor());
        assertEquals(docexpected.getDetails().getMentor(), docactual.getDetails().getMentor());
        assertEquals(docexpected.getDetails().getText(), docactual.getDetails().getText());
        }


    @Test
    public void getDetailsfromFileNoRegexMatch() throws Exception {

        Details detailsactual = detailsService.getDetailsFromFile("testfilewrongvalues.docx");

        assertEquals("no matcherinos found", detailsactual.getNachname());
        assertEquals("no matcherinos found", detailsactual.getVorname());
        assertEquals("no matcherinos found", detailsactual.getDepartment());
        assertEquals("no matcherinos found", detailsactual.getTeam());
        assertEquals("no matcherinos found", detailsactual.getSupervisor());
        assertEquals("no matcherinos found", detailsactual.getMentor());
        assertEquals("no matcherinos found", detailsactual.getText());
    }

*/
}