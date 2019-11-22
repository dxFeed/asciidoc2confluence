package com.dxfeed.processor;

import lombok.extern.slf4j.Slf4j;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Postprocessor;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class CodeBlockPostProcessor extends Postprocessor {

    @Override
    public String process (Document document, String convertedDocument) {
        return
            Arrays.stream(convertedDocument.split("\n"))
                .map(this::processString)
                .collect(Collectors.joining("\n"));
    }

    private String processString(String string) {
        Matcher matcher = START_PATTERN.matcher(string);
        if (matcher.matches())
            return matcher.group(1) + codeOpenElement(matcher.group(4)) + matcher.group(6);
        matcher = END_PATTERN.matcher(string);
        if (matcher.matches())
            return string.replace(matcher.group(2) + matcher.group(3), "\n") + codeCloseElement();
        return string;
    }

    // see:
    //    https://confluence.atlassian.com/display/CONF55/Code+Block+Macro
    //    https://stackoverflow.com/questions/30194918/how-do-you-insert-a-confluence-macro-into-a-page-created-by-the-confluence-rest
    private String codeOpenElement(String language) {
        String replacementStr =
        "<ac:structured-macro ac:name=\"code\">" +
        "<ac:parameter ac:name=\"title\"></ac:parameter>" +
        "<ac:parameter ac:name=\"theme\">default</ac:parameter>" +
        "<ac:parameter ac:name=\"linenumbers\">false</ac:parameter>" +
        "<ac:parameter ac:name=\"language\">" + language + "</ac:parameter>" +
        "<ac:parameter ac:name=\"firstline\">0001</ac:parameter>" +
        "<ac:parameter ac:name=\"collapse\">false</ac:parameter>" +
        "<ac:plain-text-body>" +
        "<![CDATA["
        ;
        return replacementStr;
    }
    private String codeCloseElement() {
        String replacementStr =
            "]]>" +
            "</ac:plain-text-body>" +
            "</ac:structured-macro>";
        return replacementStr;
    }

    private static final String CODE_START = "(.*)(<pre>)([$]+)([A-Za-z]+)([$]+)(.*)";
    private static final String CODE_END = "([^$]*)([$]+)(</pre>)(.*)";
    private static final Pattern START_PATTERN = Pattern.compile(CODE_START, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    private static final Pattern END_PATTERN   = Pattern.compile(CODE_END, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
}
