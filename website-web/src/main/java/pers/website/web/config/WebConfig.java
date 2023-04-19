package pers.website.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Web配置类
 * 
 * @author ChenetChen
 * @since 2023/3/7 17:16
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 区域信息解析器手动创建会覆盖系统自动的，所以自己添加到容器中
     * @return localeResolver
     */
    @Bean
    public LocaleResolver localeResolver(){
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("zh", "CN"));
        return localeResolver;
    }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    /*
     * 跨域请求支持
     * addMapping：配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径。
     * allowedOrigins：允许所有的请求域名访问我们的跨域资源，可以固定单条或者多条内容，如："http://www.baidu.com"，只有百度可以访问我们的跨域资源。
     * allowCredentials： 响应头表示是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以
     * allowedMethods：允许输入参数的请求方法访问该跨域资源服务器，如：POST、GET、PUT、OPTIONS、DELETE等。
     * allowedHeaders：允许所有的请求header访问，可以自定义设置任意请求头信息，如："X-YAUTH-TOKEN"
     * maxAge：配置客户端缓存预检请求的响应的时间（以秒为单位）。默认设置为1800秒（30分钟）。
     * 链接：https://www.jianshu.com/p/7705dffe4274
     * @param registry 请求
     */
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE","PUT")
                .allowedHeaders("*");
    }*/

}
