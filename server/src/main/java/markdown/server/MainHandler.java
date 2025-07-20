package markdown.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import markdown.parser.MarkdownFileReader;
import markdown.parser.MarkdownParser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;


public class MainHandler extends HttpServlet {
    private MarkdownFileReader markdownFileReader = new MarkdownFileReader();
    String markdownFolder = System.getProperty("markdown.folder");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), markdownFileReader.getMarkdownFileNames(Paths.get(markdownFolder)));
    }
}