package com.caxerx.servlet;

import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.ImageDb;
import com.caxerx.response.FailIdResponse;
import com.caxerx.response.FailResponse;
import com.caxerx.response.FileUploadResponseContent;
import com.caxerx.response.SuccessResponse;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Base64;

@WebServlet(name = "UploadFileController", urlPatterns = {"/api/upload"})
@MultipartConfig
public class UploadFileController extends HttpServlet {
    ImageDb imageDb;

    @Override
    public void init() throws ServletException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.contextInit(getServletContext());
        imageDb = new ImageDb(pool);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String fileType = getServletContext().getMimeType(fileName);
        InputStream fileContent = filePart.getInputStream();
        int imgId = imageDb.uploadImage(fileContent, fileType);
        if (imgId < 0) {
            response.setStatus(500);
            out.print(new FailIdResponse("Fail to upload image", imgId));
            return;
        }
        out.print(new SuccessResponse(new FileUploadResponseContent(imgId)));
    }
}
