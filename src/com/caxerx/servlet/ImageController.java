package com.caxerx.servlet;

import com.caxerx.bean.Image;
import com.caxerx.db.DatabaseConnectionPool;
import com.caxerx.db.ImageDb;
import com.caxerx.response.FailResponse;
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
import java.sql.SQLException;
import java.util.Base64;

@WebServlet(name = "ImageController", urlPatterns = {"/api/image"})
@MultipartConfig
public class ImageController extends HttpServlet {
    ImageDb imageDb;

    @Override
    public void init() throws ServletException {
        DatabaseConnectionPool pool = DatabaseConnectionPool.contextInit(getServletContext());
        imageDb = new ImageDb(pool);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        int rId;
        try {
            rId = Integer.parseInt(id);
        } catch (NullPointerException | NumberFormatException e) {
            PrintWriter out = response.getWriter();
            response.setStatus(400);
            out.print(new FailResponse("Invalid image id"));
            return;
        }
        Image img = imageDb.getImage(rId);
        if (img == null) {
            PrintWriter out = response.getWriter();
            response.setStatus(404);
            out.print(new FailResponse("Image not found"));
            return;
        }

        try (InputStream imgByteStream = img.getImage().getBinaryStream()) {
            byte[] imageByte = ByteStreams.toByteArray(imgByteStream);
            response.setContentType(img.getType());
            response.getOutputStream().write(imageByte);
        } catch (SQLException e) {
            PrintWriter out = response.getWriter();
            e.printStackTrace();
            response.setStatus(500);
            out.print(new FailResponse("Get Image Error"));
        }
    }
}
