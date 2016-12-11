package com.cloud.configure;

import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by micky on 2016. 12. 11..
 */
@Component
public class IgnoreManager {
    private Set<String> ignore;

    @Autowired
    private void setIgnore(@Value("#{'${file.ignore}'.split(',')}")List<String> ignoreFiles) {
        ignore = Collections.unmodifiableSet(new HashSet<>(ignoreFiles));
    }

    /**
     * check filename is
     * @param filename
     * @return
     */
    public boolean isIgnoreFile(String filename) {
        return ignore.contains(filename);
    }
}
