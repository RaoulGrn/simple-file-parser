import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.raoulgrn.core.models.Data;
import org.raoulgrn.processors.DotCountProcessor;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DotCountProcessorTest {
    private DotCountProcessor processor;
    private Data testData;

    @BeforeEach
    void setUp(){
        processor = new DotCountProcessor();
        testData = new Data("Sons of Gondor! Of Rohan! My brothers. I see in your eyes the same fear that would take" +
                " the heart of me. A day may come when the courage of Men fails, when we forsake our" +
                " friends and break all bonds of fellowship, but it is not this day. An hour of wolves and" +
                " shattered shields when the Age of Men comes crashing down, but it is not this day! This" +
                " day we fight! By all that you hold dear on this good earth, I bid you stand, Men of the" +
                " West!", new HashMap<>());
    }


    @Test
    void shouldCountDotsCorrectly() {

        processor.process(testData);
        int dotCount = (int) processor.getResult();

        assertEquals(3, dotCount);
    }

    @Test
    void shouldHandleEmptyString() {

        testData = new Data("", new HashMap<>());

        processor.process(testData);
        int dotCount = (int) processor.getResult();

        assertEquals(0, dotCount);
    }

    @Test
    void shouldOnlyCountDots() {

        processor.process(testData);
        int dotCount = (int) processor.getResult();

        assertEquals(3, dotCount);
    }
}
