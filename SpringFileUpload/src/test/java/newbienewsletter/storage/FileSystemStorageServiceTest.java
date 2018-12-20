package newbienewsletter.storage;

import newbienewsletter.exceptions.StorageException;
import newbienewsletter.exceptions.StorageFileNotFoundException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) //nachschauen
public class FileSystemStorageServiceTest {

    @InjectMocks
    FileSystemStorageService fileSystemStorageService;

    @Mock
    Environment env;

    @Before
    public void setUp() {
        when(env.getProperty("rootlocation"))
                .thenReturn("src/test/resources/testfiles/testuploads");
        fileSystemStorageService.init();
    }

    @AfterClass
    public static void tearDown() {
        if (Files.exists(Paths.get("src/test/resources/testfiles/testuploads"))) {
            FileSystemUtils.deleteRecursively(Paths.get("src/test/resources/testfiles/testuploads").toFile());
        }
    }

    @Test
    public void storeFileIsOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("test", "test.docx", "text/plain", "someText".getBytes());
        fileSystemStorageService.store(file);
    }

    @Test(expected = StorageException.class)
    public void storeFileIsEmpty() throws Exception {
        MockMultipartFile file = new MockMultipartFile("test", "test.docx", "text/plain", "".getBytes());
        fileSystemStorageService.store(file);
    }

    @Test(expected = StorageException.class)
    public void storeFileHasRelativePath() throws Exception {
        MockMultipartFile file = new MockMultipartFile("test", "..test.docx", "text/plain", "someText".getBytes());
        fileSystemStorageService.store(file);
    }

    @Test(expected = StorageException.class)
    public void storeFailed() throws Exception {
        MockMultipartFile mpfile = new MockMultipartFile("test", "test.docx", "text/plain", "someText".getBytes());
        FileSystemUtils.deleteRecursively(Paths.get("src/test/resources/testfiles/testuploads").toFile());
        fileSystemStorageService.store(mpfile);
    }

    @Test
    public void loadAllIsOK() throws Exception {
        assertTrue(fileSystemStorageService.loadAll() instanceof Stream);
    }

    @Test (expected = StorageException.class)
    public void loadAllFailedToReadStoredFiles() throws Exception {
        when(env.getProperty("rootlocation"))
                .thenReturn("src/test/resources/testfiles/nonexistingfolder");
        fileSystemStorageService.init();
        FileSystemUtils.deleteRecursively(Paths.get("src/test/resources/testfiles/nonexistingfolder").toFile());
        fileSystemStorageService.loadAll();
    }

    @Test
    public void loadIsOK() throws Exception {
        assertEquals(Paths.get(env.getProperty("rootlocation")).resolve("test"), fileSystemStorageService.load("test"));
    }

    @Test
    public void loadAsResourceIsOK() throws Exception {
        when(env.getProperty("rootlocation"))
                .thenReturn("src/test/resources/testfiles");
        fileSystemStorageService.init();
        assertTrue(fileSystemStorageService.loadAsResource("testfileisOK.docx") instanceof Resource);
    }

    @Test(expected = StorageFileNotFoundException.class)
    public void loadAsResourceFileNotFound() throws Exception {
        fileSystemStorageService.loadAsResource("nofile");
    }
}