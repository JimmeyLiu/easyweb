package org.easyweb.app.scanner;

import org.easyweb.app.FileModified;
import org.easyweb.app.FileModified;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jimmey/shantong
 * Date: 13-7-19
 * Time: 下午11:46
 */
public class TypeFiles {

    private List<File> files = new ArrayList<File>();

    private List<File> modified = new ArrayList<File>();

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void addFile(File file) {
        this.files.add(file);
        if (FileModified.isModified(file)) {
            modified.add(file);
        }
    }

    public List<File> getModified() {
        return modified;
    }
}
