package org.hkxconvert;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@Data
@AllArgsConstructor
public class FilePath {
    public File txt;
    public File hkx;
}
