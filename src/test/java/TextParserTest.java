import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raoulgrn.core.models.Data;
import org.raoulgrn.parsers.TextParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TextParserTest {

    private TextParser textParser;

    @BeforeEach
    void setUp(){
        textParser = new TextParser();
    }

    @Test
    void shouldParseSimpleTextFile(){
        String testContent = "Sons of Gondor! Of Rohan! My brothers. I see in your eyes the same fear that would take" +
                " the heart of me. A day may come when the courage of Men fails, when we forsake our" +
                " friends and break all bonds of fellowship, but it is not this day. An hour of wolves and" +
                " shattered shields when the Age of Men comes crashing down, but it is not this day! This" +
                " day we fight! By all that you hold dear on this good earth, I bid you stand, Men of the" +
                " West!";

        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        Data parsedData = textParser.parse(inputStream);

        assertEquals("Sons of Gondor! Of Rohan! My brothers. I see in your eyes the same fear that would take" +
                " the heart of me. A day may come when the courage of Men fails, when we forsake our" +
                " friends and break all bonds of fellowship, but it is not this day. An hour of wolves and" +
                " shattered shields when the Age of Men comes crashing down, but it is not this day! This" +
                " day we fight! By all that you hold dear on this good earth, I bid you stand, Men of the" +
                " West!", parsedData.getContent());

        Map<String, Object> metadata = parsedData.getMetadata();

        assertNotNull(metadata);
        assertEquals("txt", metadata.get("type"));
        assertNotNull(metadata.get("parseTime"));
    }

    @Test
    void shouldParseEmptyFile() {
        String testContent = "";

        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        Data parsedData = textParser.parse(inputStream);

        assertEquals("", parsedData.getContent());

        Map<String, Object> metadata = parsedData.getMetadata();
        assertNotNull(metadata);
        assertEquals("txt", metadata.get("type"));
        assertNotNull(metadata.get("parseTime"));
    }

    @Test
    void shouldParseMultiLineFile() {
        String testContent = "Line 1\nLine 2\nLine 3";
        InputStream inputStream = new ByteArrayInputStream(testContent.getBytes(StandardCharsets.UTF_8));

        Data parsedData = textParser.parse(inputStream);

        assertEquals("Line 1\nLine 2\nLine 3", parsedData.getContent());

        Map<String, Object> metadata = parsedData.getMetadata();

        assertNotNull(metadata);
        assertEquals("txt", metadata.get("type"));
        assertNotNull(metadata.get("parseTime"));

    }

    @Test
    void shouldValidateCorrectFileExtension() {
        assertTrue(textParser.validateFile("test.txt"));
        assertTrue(textParser.validateFile("test.TXT"));
        assertTrue(textParser.validateFile("/path/to/test.txt"));
    }

    @Test
    void shouldRejectInvalidFileExtension(){
        assertFalse(textParser.validateFile("test.doc"));
        assertFalse(textParser.validateFile("test.pdf"));
        assertFalse(textParser.validateFile(null));
        assertFalse(textParser.validateFile(""));
    }

    @Test
    void shouldThrowExceptionForNullInputStream(){
        assertThrows(RuntimeException.class, () -> {
            textParser.parse(null);
        });
    }


}
