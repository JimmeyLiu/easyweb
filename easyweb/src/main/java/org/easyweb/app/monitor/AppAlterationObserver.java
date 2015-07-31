package org.easyweb.app.monitor;

import org.easyweb.app.App;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by jimmey on 15-7-31.
 */
public class AppAlterationObserver {

    AppAlterationListener listener;
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private final FileEntry rootEntry;
    private final FileFilter fileFilter;
    private final Comparator<File> comparator;
    private final App app;

    public AppAlterationObserver(App app, File file, AppAlterationListener listener) {
        this(app, new FileEntry(null, file), listener);
    }

    protected AppAlterationObserver(final App app, FileEntry rootEntry, AppAlterationListener listener) {
        this.app = app;
        this.listener = listener;
        this.rootEntry = rootEntry;
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.getAbsolutePath().startsWith(app.getClasspath())
                        && !pathname.getName().startsWith(".");
            }
        };
        this.comparator = new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    public App getApp() {
        return app;
    }

    public void checkAndNotify() {
        File rootFile = rootEntry.getFile();
        if (rootFile.exists()) {
            checkAndNotify(rootEntry, rootEntry.getChildren(), listFiles(rootFile));
        } else if (rootEntry.isExists()) {
            checkAndNotify(rootEntry, rootEntry.getChildren(), EMPTY_FILE_ARRAY);
        }
        listener.onFinish();
    }

    private void checkAndNotify(FileEntry parent, FileEntry[] previous, File[] files) {
        int c = 0;
        FileEntry[] current = files.length > 0 ? new FileEntry[files.length] : FileEntry.EMPTY_ENTRIES;
        for (FileEntry entry : previous) {
            while (c < files.length && comparator.compare(entry.getFile(), files[c]) > 0) {
                current[c] = createFileEntry(parent, files[c]);
                doCreate(current[c]);
                c++;
            }
            if (c < files.length && comparator.compare(entry.getFile(), files[c]) == 0) {
                doChange(entry, files[c]);
                checkAndNotify(entry, entry.getChildren(), listFiles(files[c]));
                current[c] = entry;
                c++;
            } else {
                checkAndNotify(entry, entry.getChildren(), EMPTY_FILE_ARRAY);
                doDelete(entry);
            }
        }
        for (; c < files.length; c++) {
            current[c] = createFileEntry(parent, files[c]);
            doCreate(current[c]);
        }
        parent.setChildren(current);
    }

    private FileEntry createFileEntry(FileEntry parent, File file) {
        FileEntry entry = parent.newChildInstance(file);
        entry.refresh(file);
        File[] files = listFiles(file);
        FileEntry[] children = files.length > 0 ? new FileEntry[files.length] : FileEntry.EMPTY_ENTRIES;
        for (int i = 0; i < files.length; i++) {
            children[i] = createFileEntry(entry, files[i]);
        }
        entry.setChildren(children);
        entry.setFileType(AppFileType.getFileType(app, file));
        return entry;
    }

    private File[] listFiles(File file) {
        File[] children = null;
        if (file.isDirectory()) {
            children = fileFilter == null ? file.listFiles() : file.listFiles(fileFilter);
        }
        if (children == null) {
            children = EMPTY_FILE_ARRAY;
        }
        if (comparator != null && children.length > 1) {
            Arrays.sort(children, comparator);
        }
        return children;
    }

    private void doCreate(FileEntry fileEntry) {
        if (fileEntry.isDirectory()) {
            FileEntry[] children = fileEntry.getChildren();
            for (FileEntry aChildren : children) {
                doCreate(aChildren);
            }
        } else {
            listener.onCreate(fileEntry.getFileType(), fileEntry.getFile());
        }
    }

    private void doDelete(FileEntry fileEntry) {
        if (!fileEntry.isDirectory()) {
            listener.onDelete(fileEntry.getFileType(), fileEntry.getFile());
        }
    }

    private void doChange(FileEntry entry, File file) {
        if (entry.refresh(file)) {
            if (!file.isDirectory()) {
                listener.onChange(entry.getFileType(), file);
            }
        }
    }

}
