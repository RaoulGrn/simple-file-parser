package org.raoulgrn.source;

import org.raoulgrn.core.interfaces.DataSource;
import org.raoulgrn.core.interfaces.FileType;
import org.raoulgrn.core.models.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class FileSource implements DataSource {
    private String path;
    private FileType fileType;
    private Data data;

    public FileSource(FileType fileType, String path) {
        this.fileType = fileType;
        this.path = path;
    }

    @Override
    public Data getData(){
        if(data == null) {
            try (InputStream input = new FileInputStream(path)){
                data = fileType.parse(input);
            } catch (IOException exception) {
                throw new RuntimeException("Error reading file: " + path, exception);
            }
        }
        return data;
    }

    @Override
    public Map<String, Object> getMetadata(){
        return getData().getMetadata();
    }

    public String getPath() {
        return path;
    }

    public FileType getFileType() {
        return fileType;
    }
}
