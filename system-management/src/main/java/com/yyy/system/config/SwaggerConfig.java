package com.yyy.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger文档
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

	@Bean
	public Docket api() {
		//添加head参数
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("token").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		pars.add(tokenPar.build());

		//可以添加多个header或参数
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.wanwei.system.controller"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("绩效考核系统API")
				.version("1.0")
				.description("luohuiiq-2020-07-15")
				.contact(new Contact("绩效考核系统", "", "cherryblossom1024@163.com"))
				.build();
	}

//	/**
//	 * 防止@EnableMvc把默认的静态资源路径覆盖了，手动设置的方式
//	 *
//	 * @param registry
//	 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		// 解决静态资源无法访问
//		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//		// 解决swagger无法访问
//		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//		// 解决swagger的js文件无法访问
//		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//	}
}
