package markdown.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MarkdownParser {

    private boolean insideCodeBlock = false;
    private boolean insideTableBlock = false;
    private MarkdownModel currentCodeBlock;
    private MarkdownModel currentTableBlock;
    private List<MarkdownModel> markdownModels;

    public List<MarkdownModel> parse(Path path) {
        markdownModels = new LinkedList<>();
        List<String> lines = readLines(path);
        for (String line : lines) {
            if (line.isEmpty()) continue;
            parseLine(line);
        }
        if (insideCodeBlock && currentCodeBlock != null) {
            markdownModels.add(currentCodeBlock);
            insideCodeBlock = false;
        }
        if (insideTableBlock && currentTableBlock != null) {
            markdownModels.add(currentTableBlock);
            insideTableBlock = false;
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
        }else if (insideTableBlock) {
            handleTableLine(line);
        }  else if (isTableLine(line)) {
            startTableBlock(line);
        } else if (isCodeBlockStart(line)) {
            startCodeBlock(line);
        } else {
            handleNonCodeLines(line);
        }
    }

    private boolean isCodeBlockStart(String line) {
        return line.startsWith("```");
    }
    private boolean isTableLine(String line) {
        return line.startsWith("|");
    }

    private void startCodeBlock(String line) {
        insideCodeBlock = true;
        currentCodeBlock = new MarkdownModel();
        String languageType = line.trim().toLowerCase().substring(3);
        switch(languageType) {
            case "mermaid":
                currentCodeBlock.setType(MarkdownType.CODE_MERMAID);
                break;
            case "js":
                currentCodeBlock.setType(MarkdownType.CODE_JS);
                break;
            case "xml":
                currentCodeBlock.setType(MarkdownType.CODE_XML);
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
                currentCodeBlock.setType(MarkdownType.CODE_JAVA);
                break;
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

    private void startTableBlock(String line) {
        insideTableBlock = true;
        currentTableBlock = new MarkdownModel();
        currentTableBlock.setType(MarkdownType.TABLE);
        currentTableBlock.getContent().add(line);
    }

    private void handleTableLine(String line) {
        if (!line.startsWith("|")) {
            insideTableBlock = false;
            markdownModels.add(currentTableBlock);
            parseLine(line);  // This is key - if it's not in the table, we should parse the line as usual
        } else {
            currentTableBlock.getContent().add(line);
        }
    }

    private void handleNonCodeLines(String line) {
        MarkdownModel model;
        String firstCharacter = line.substring(0, 1);
        switch(firstCharacter) {
            case "#":
                model = parseHeading(line);
                break;
            case "-":
                model = parseNonCode(line, MarkdownType.LIST);
                break;
            default:
                model = parseNonCode(line, MarkdownType.TEXT);
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

    private MarkdownModel parseNonCode(String line, MarkdownType type) {
        MarkdownModel model = new MarkdownModel();
        model.setType(type);
        model.setContent(Collections.singletonList(line));
        return model;
    }
}
