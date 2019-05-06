package co.com.ceiba.parqueadero.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.ceiba.parqueadero.dto.VehicleDTO;
import co.com.ceiba.parqueadero.model.Vehicle;
import co.com.ceiba.parqueadero.util.VehicleTypeEnum;

@Configuration
public class ParqueaderoConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
		return mapper;
	}
}
