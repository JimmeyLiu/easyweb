package org.easyweb.app.monitor.impl;


import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by jimmey on 15-7-30.
 */
public class NameFileComparator extends AbstractFileComparator implements Serializable {

    public static final Comparator<File> NAME_COMPARATOR = new NameFileComparator();

    /**
     * Construct a case sensitive file name comparator instance.
     */
    public NameFileComparator() {
    }

    /**
     * Compare the names of two files with the specified case sensitivity.
     *
     * @param file1 The first file to compare
     * @param file2 The second file to compare
     * @return a negative value if the first file's name
     * is less than the second, zero if the names are the
     * same and a positive value if the first files name
     * is greater than the second file.
     */
    public int compare(File file1, File file2) {
        return file1.getName().compareTo(file2.getName());
    }

}
