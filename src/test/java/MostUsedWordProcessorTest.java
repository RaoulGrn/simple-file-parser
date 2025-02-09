import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raoulgrn.core.models.Data;
import org.raoulgrn.processors.MostUsedWordProcessor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MostUsedWordProcessorTest {
    private MostUsedWordProcessor processor;
    private Data testData;


    @BeforeEach
    void setUp(){
        processor = new MostUsedWordProcessor();
        testData = new Data("Sons of Gondor! Of Rohan! My brothers. I see in your eyes the same fear that would take" +
                " the heart of me. A day may come when the courage of Men fails, when we forsake our" +
                " friends and break all bonds of fellowship, but it is not this day. An hour of wolves and" +
                " shattered shields when the Age of Men comes crashing down, but it is not this day! This" +
                " day we fight! By all that you hold dear on this good earth, I bid you stand, Men of the" +
                " West!", new HashMap<>());
    }

    @Test
    void shouldFindMostUsedWord(){
        processor.process(testData);
        Map<String,Object> result = (Map<String, Object>) processor.getResult();

        assertEquals("of", result.get("word"));
        assertEquals(8L, result.get("count"));
    }

    @Test
    void shouldHandleEmptyString() {
        testData = new Data("", new HashMap<>());

        processor.process(testData);
        Map<String, Object> result = (Map<String, Object>) processor.getResult();

        assertEquals("", result.get("word"));
        assertEquals(0L, result.get("count"));
    }

    @Test
    void shouldBeCaseInsensitive() {
        testData = new Data("test TEST TesT Test", new HashMap<>());

        processor.process(testData);
        Map<String, Object> result = (Map<String, Object>) processor.getResult();

        assertEquals("test", result.get("word"));
        assertEquals(4L, result.get("count"));
    }

}
