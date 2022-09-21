package com.hilbp.adb.yml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@PropertySource(
		factory = YamlPropertySourceFactory.class, 
		value = {
				"classpath:configScreen/2160x1080.yml",
				"classpath:devices-config.yml"
		}, 
		encoding = "utf-8")
//@Slf4j
public class ActionDataBinder implements ResourceLoaderAware {
	
	@Autowired
	ConfigurableEnvironment environment;

	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		try {
			Resource[] resources = ((ResourcePatternResolver) resourceLoader).getResources("classpath*:configAction/*.yml");
			YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
			PropertiesPropertySource propertiesPropertySource;
			for(Resource r : resources) { 
				factory.setResources(r);
	            factory.afterPropertiesSet();
	            propertiesPropertySource = new PropertiesPropertySource(r.getFilename(), factory.getObject());
	            environment.getPropertySources().addLast(propertiesPropertySource);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public ActionData actionBinderYml(String prefix) {
		Binder binder = Binder.get(applicationContext.getEnvironment());
		ActionData actionData = binder.bind(prefix, Bindable.of(ActionData.class)).get();
		return actionData;
	}

	
}
