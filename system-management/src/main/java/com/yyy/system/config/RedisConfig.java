package com.yyy.system.config;

/**
 * @program: hkpi
 * @description: redis配置文件
 * @author: Mr.Liu
 * @create: 2020-07-21 16:20
 **/
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 * Created by Andy.W.Zhang on 2019/3/7.
 */

public class RedisConfig {


    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        //小范围白名单
        ParserConfig.getGlobalInstance().addAccept("com.andy.framdemo.");
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用FastJSON的序列化方式
        template.setValueSerializer(fastJsonRedisSerializer);
        // hash的value序列化方式采用FastJSON的序列化方式
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }


    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        //小范围白名单
        ParserConfig.getGlobalInstance().addAccept("com.andy.framdemo.");
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        stringRedisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        // value序列化方式采用FastJSON的序列化方式
        stringRedisTemplate.setValueSerializer(stringRedisSerializer);
        stringRedisTemplate.afterPropertiesSet();
        return stringRedisTemplate;
    }

}

