package io.github.tim12135;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring文档配置
 *
 * @author tim12135
 * @since 2023/4/11
 */
@Configuration
public class SpringdocConfiguration {
    @Value("${spring.application.name:未知}")
    private String applicationName;

    @Bean
    public GroupedOpenApi publicApi() {
        String[] contextHeaderNames = new String[]{"M-Token", "M-TenantId", "M-UserId", "M-UserName",
                "M-UserAgent", "M-UserIp", "M-Locale", "M-TimeZone"};
        return GroupedOpenApi.builder()
                .group("tim12135")
                .packagesToScan("io.github.tim12135.demo")
                .pathsToMatch("/**")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    for (String contextHeaderName : contextHeaderNames) {
                        Parameter missingParam = new Parameter()
                                .in(ParameterIn.HEADER.toString())
                                .schema(new StringSchema())
                                .name(contextHeaderName);
                        operation.addParametersItem(missingParam);
                    }
                    return operation;
                })
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title(applicationName).version("1.0.0"));
    }
}
