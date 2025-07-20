package markdown.parser;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class MarkdownFileReader {

    public List<String> getMarkdownFileNames(Path folder) {
        List<String> markdownFileNames = new ArrayList<>();
        try {
            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".md"))
                    .map(path -> path.getFileName().toString()) // ← extract just the file name
                    .forEach(markdownFileNames::add);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read markdown files from folder: " + folder, e);
        }
        return markdownFileNames;
    }
}
