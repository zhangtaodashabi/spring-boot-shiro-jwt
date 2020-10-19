package com.yyy.system.config.jackson;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * JacksonConfig
 *
 * @author L.cm
 * @author: lengleng
 * @author: lishangbu
 * @date: 2018/10/22
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			builder.failOnUnknownProperties(false);
			builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
			builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			builder.modules(new TimeModule());
		};
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		objectMapper.setLocale(Locale.CHINA);
		objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		// LocalDateTime系列序列化和反序列化模块，继承自jsr310，我们在这里修改了日期格式
		objectMapper.registerModule(new TimeModule());
		return objectMapper;
	}


	/**
	 * LocalDate转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalDate> localDateConverter() {
		return new Converter<String, LocalDate>() {

			@Override
			public LocalDate convert(String source) {
				if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source)) {
					return LocalDate.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
				} else {
					return LocalDate.parse(source.substring(0, 10), DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
				}
			}
		};
	}

	/**
	 * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalDateTime> localDateTimeConverter() {
		return new Converter<String, LocalDateTime>() {
			@Override
			public LocalDateTime convert(String source) {
				if (Pattern.matches("^\\d{4}-\\d{2}-\\d{2}$", source)) {
					return LocalDateTime.parse(source + " 00:00:00", DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
				} else {
					return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
				}

			}
		};
	}

	/**
	 * LocalTime转换器，用于转换RequestParam和PathVariable参数
	 */
	@Bean
	public Converter<String, LocalTime> localTimeConverter() {
		return new Converter<String, LocalTime>() {
			@Override
			public LocalTime convert(String source) {
				return LocalTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
			}
		};
	}

}
