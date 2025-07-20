package markdown.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import markdown.parser.MarkdownModel;
import markdown.parser.MarkdownParser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
public class UploadHandler extends HttpServlet {
    private MarkdownParser markdownParser = new MarkdownParser();
    String markdownFolder = System.getProperty("markdown.folder");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String fileName = req.getPathInfo().substring(1);
        Path filePath = Paths.get(markdownFolder).resolve(fileName + ".md");
        try{
            List<MarkdownModel> parsedMarkdown = markdownParser.parse(filePath);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(resp.getWriter(), parsedMarkdown);
        } catch(Exception e){
            log.error(e.getMessage());
        }


    }
}