package org.easyweb.request.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.easyweb.util.EasywebLogger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * User: jimmey/shantong
 * Date: 13-6-25
 * Time: 下午6:46
 */
public class FileUploader {

    public static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    public static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    public static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    static ServletFileUpload upload;

    static {
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // sets memory threshold - beyond which files are stored in disk
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // sets temporary location to store files
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        upload = new ServletFileUpload(factory);
        // sets maximum size of upload file
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // sets maximum size of request (include file + form data)
        upload.setSizeMax(MAX_REQUEST_SIZE);
    }

    public static void process(HttpServletRequest request) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return;
        }
        upload.setHeaderEncoding("utf-8");
        try {
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                for (FileItem item : formItems) {
                    if (!item.isFormField()) {
                        request.setAttribute("_file_", item.get());
                        request.setAttribute("_file_name_", item.getName());
                    } else {
                        request.setAttribute(item.getFieldName(), new String(item.get()));
                    }
                }
            }
        } catch (Exception ex) {
            EasywebLogger.error("FileUpload Error", ex);
            return;
        }
    }

}
