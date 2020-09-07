package com.carrot.simplelog.config;

import com.carrot.simplelog.service.ICarrotLog;
import com.carrot.simplelog.service.impl.ICarrotLogImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author carrot
 * @date 2020/9/3 17:23
 */
@Configuration
@ConditionalOnClass(ICarrotLog.class)
@EnableConfigurationProperties(CarrotLogProperties.class)
public class CarrotLogStarterAutoConfigure {
	@Autowired
	private CarrotLogProperties properties;

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "carrot.log", value = "enabled", havingValue = "true")
	ICarrotLog starterService (){
		return new ICarrotLogImpl(properties);
	}

}
