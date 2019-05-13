package co.com.ceiba.parqueadero.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    @Autowired
    private ModelMapper modelMapper;

    public <T, Q> Q map(T element, Class<Q> clazz) {
        return modelMapper.map(element, clazz);
    }
}
