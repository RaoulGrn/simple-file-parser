package org.raoulgrn.core.interfaces;

import org.raoulgrn.core.models.Data;

import java.io.InputStream;

public interface FileType {
    Data parse(InputStream input);
    boolean validateFile(String path);
    }

