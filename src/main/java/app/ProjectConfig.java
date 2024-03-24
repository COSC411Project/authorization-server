package app;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import app.converters.*;

@Configuration
public class ProjectConfig implements WebMvcConfigurer {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new GrantTypeConverter());
		registry.addConverter(new ResponseTypeConverter());
		registry.addConverter(new ScopeConverter());
	}
}
