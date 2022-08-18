package it.polimi.tiw.projects.utils;

import javax.servlet.ServletContext;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Handler of the ThymeLeaf templates
 */
public class TemplateHandler {
	
	public static TemplateEngine getEngine(ServletContext context, String suffix) {
		
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
		templateResolver.setSuffix(suffix);
		
		return templateEngine;
	}
}