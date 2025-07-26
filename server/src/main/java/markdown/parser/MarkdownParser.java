package markdown.parser;

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
            startCodeBlock(line);
        } else {
            handleRegularLine(line);
        }
    }

    private boolean isCodeBlockStart(String line) {
        return line.startsWith("```");
    }

    private void startCodeBlock(String line) {
        insideCodeBlock = true;
        currentCodeBlock = new MarkdownModel();
        String languageType = line.trim().toLowerCase().substring(3);
        switch(languageType) {
            case "java":
                currentCodeBlock.setType(MarkdownType.CODE_JAVA);
                break;
            case "mermaid":
                currentCodeBlock.setType(MarkdownType.CODE_MERMAID);
                break;
            case "javascript":
                currentCodeBlock.setType(MarkdownType.CODE_JAVASCRIPT);
                break;
            case "html":
                currentCodeBlock.setType(MarkdownType.CODE_HTML);
                break;
            case "css":
                currentCodeBlock.setType(MarkdownType.CODE_CSS);
                break;
            case "yaml":
                currentCodeBlock.setType(MarkdownType.CODE_YAML);
                break;
            case "json":
                currentCodeBlock.setType(MarkdownType.CODE_JSON);
                break;
            default:
        }
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
                type = MarkdownType.HEADING1;
                break;
            case 2:
                type = MarkdownType.HEADING2;
                break;
            case 3:
                type = MarkdownType.HEADING3;
                break;
            default:
                type = MarkdownType.TEXT;
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
        model.setType(MarkdownType.TEXT);
        model.setContent(Collections.singletonList(line));
        return model;
    }
}
