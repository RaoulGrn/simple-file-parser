package org.raoulgrn.parsers;

import org.raoulgrn.core.interfaces.FileType;
import org.raoulgrn.core.models.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;



public class TextParser implements FileType {

    @Override
    public Data parse(InputStream input) {
        long startTime = System.currentTimeMillis();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            StringBuilder content = new StringBuilder();
            String line;
            boolean firstLine = true;
            while((line = reader.readLine()) != null){
                if (!firstLine) {
                    content.append("\n");
                }
                content.append(line);
                firstLine = false;
            }

            if (content.length() == 0) {
                content = new StringBuilder("");
            }

            long duration = System.currentTimeMillis() - startTime;
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("type", "txt");
            metadata.put("parseTime", String.format("%d milliseconds (%.3f seconds)",
                    duration,
                    duration / 1000.0));

            return new Data(content.toString(), metadata);
        } catch (IOException exception){
            throw new RuntimeException("Error parsing TXT file", exception);
        }
    }

    @Override
    public boolean validateFile(String path) {
        System.out.println("Validating file: " + path);
        boolean isValid = path != null && path.toLowerCase().endsWith(".txt");
        System.out.println("File validation result: " + isValid);
        return isValid;
    }
}
