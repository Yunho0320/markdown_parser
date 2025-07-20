package markdown.parser;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarkdownParser {

    private boolean insideCodeBlock = false;
    private MarkdownModel currentCodeBlock;
    private List<MarkdownModel> markdownModels;

    public List<MarkdownModel> parse(Path path) {
        markdownModels = new ArrayList<>();
        List<String> lines = readLines(path);
        for (String line : lines) {
            if (line.isEmpty()) continue;
            parseLine(line);
        }
        return markdownModels;
    }

    private List<String> readLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read lines from file: " + path, e);
        }
    }

    private void parseLine(String line) {
        if (insideCodeBlock) {
            handleCodeLine(line);
        } else if (isCodeBlockStart(line)) {
            startCodeBlock();
        } else {
            handleRegularLine(line);
        }
    }

    private boolean isCodeBlockStart(String line) {
        return line.startsWith("```java");
    }

    private void startCodeBlock() {
        insideCodeBlock = true;
        currentCodeBlock = new MarkdownModel();
        currentCodeBlock.setType(MarkdownType.Code);
    }

    private void handleCodeLine(String line) {
        if (line.equals("```")) {
            insideCodeBlock = false;
            markdownModels.add(currentCodeBlock);
        } else {
            currentCodeBlock.getContent().add(line);
        }
    }

    private void handleRegularLine(String line) {
        MarkdownModel model;
        if (line.startsWith("#")) {
            model = parseHeading(line);
        } else {
            model = parseText(line);
        }
        markdownModels.add(model);
    }

    private MarkdownModel parseHeading(String line) {
        int level = 0;
        while (level < line.length() && line.charAt(level) == '#') {
            level++;
        }

        MarkdownType type;
        switch (level) {
            case 1:
                type = MarkdownType.Heading1;
                break;
            case 2:
                type = MarkdownType.Heading2;
                break;
            case 3:
                type = MarkdownType.Heading3;
                break;
            default:
                type = MarkdownType.Text;
                break;
        }

        String content = line.substring(level).trim();
        MarkdownModel model = new MarkdownModel();
        model.setType(type);
        model.setContent(Collections.singletonList(content));
        return model;
    }

    private MarkdownModel parseText(String line) {
        MarkdownModel model = new MarkdownModel();
        model.setType(MarkdownType.Text);
        model.setContent(Collections.singletonList(line));
        return model;
    }
}
