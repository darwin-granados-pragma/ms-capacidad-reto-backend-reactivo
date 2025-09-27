package co.com.capacidad.api.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pragma MS-CAPACIDAD API",
    version = "1.0.0",
    description = "API for managing capacity information",
    license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    )
)
)
public class OpenApiConfig {

}
