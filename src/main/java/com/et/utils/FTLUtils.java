package com.et.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Component
public class FTLUtils {

    private final Logger log = LoggerFactory.getLogger(FTLUtils.class);

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    public String generateContentFor(
        String shop, Map<String, Object> templateData, String templateName) throws IOException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate(templateName);

        String fileContent = null;
        try (StringWriter out = new StringWriter()) {

            template.process(templateData, out);
            fileContent = out.getBuffer().toString();

            out.flush();
        } catch (TemplateException e) {
            log.error(
                "An error occurred while rendering from template"
                    + " exception="
                    + e.toString()
                    + " shop="
                    + shop,
                e);
        }
        return fileContent;
    }

    public String generateContentFor(
        String shop, Map<String, Object> templateData, String templateName, String templateContent) throws IOException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();

        Template template = new Template(templateName, new StringReader(templateContent), configuration);

        String fileContent = null;
        try (StringWriter out = new StringWriter()) {

            template.process(templateData, out);
            fileContent = out.getBuffer().toString();

            out.flush();
        } catch (TemplateException e) {
            log.error(
                "An error occurred while rendering from template"
                    + " exception="
                    + e.toString()
                    + " shop="
                    + shop,
                e);
        }
        return fileContent;
    }
}
