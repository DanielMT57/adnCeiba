package co.com.ceiba.parqueadero.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParqueaderoConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
