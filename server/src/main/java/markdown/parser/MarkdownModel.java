package markdown.parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarkdownModel {
    private MarkdownType type;
    private List<String> content = new ArrayList<>();
}
