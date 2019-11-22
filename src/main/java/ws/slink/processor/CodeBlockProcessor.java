package com.dxfeed.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.ContentModel;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Contexts;
import org.asciidoctor.extension.Name;
import org.asciidoctor.extension.Reader;

import java.util.Map;

@Name("code")
@Contexts ({Contexts.LISTING})
@ContentModel (ContentModel.SIMPLE)
@Slf4j
public class CodeBlockProcessor extends BlockProcessor {

    private static final String MARKER = "$$$$$$";

    @Override
    public Object process (StructuralNode parent, Reader reader, Map<String, Object> attributes) {
        String content = reader.read();
        String language = (String)attributes.get("2");
        if (StringUtils.isBlank(language))
            language = "text";
        Block block = createBlock(parent, "listing",  MARKER + language + MARKER + content + MARKER, attributes);
        return block;
    }

}
