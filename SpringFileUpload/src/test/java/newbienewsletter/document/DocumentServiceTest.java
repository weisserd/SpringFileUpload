package newbienewsletter.document;

import newbienewsletter.details.DetailsService;
import newbienewsletter.document.Document;
import newbienewsletter.document.DocumentService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) //nachschauen
public class DocumentServiceTest {

    @InjectMocks //nachschauen
            DocumentService documentService;

    @Mock
    Environment env;
    @Mock
    DetailsService detailsService;
    @Mock
    Document document = mock(Document.class);

    @Before
    public void init() throws IOException, InvalidFormatException {
        when(env.getProperty("rootlocation"))
                .thenReturn("src/test/resources/testfiles");
        //when(detailsService.getDetailsFromFile(Mockito.anyString()))
            //    .thenReturn(null);
    }

    @Test
    public void getAllDocumentsIsOk() throws Exception {
        assertEquals(2, documentService.getAllDocuments().size());
    }

    @Test
    public void getDocumentFromFileIsOk() throws Exception {
        assertEquals("testIsOk", documentService.getDocumentFromFile("testIsOk").getFilename());
    }

    @Test
    public void listFilesIsOk() throws Exception {
        List<String> files = documentService.listFiles();
        assertEquals("testfileisOK.docx", files.get(0));
        assertEquals("testfilewrongvalues.docx", files.get(1));
    }

    @Test
    public void getDocumentContentisOK() throws IOException, InvalidFormatException {
        when(document.getFilename()).thenReturn("testfileisOK.docx");
        assertTrue(documentService.getDocumentContent(document) instanceof String);
    }
}
