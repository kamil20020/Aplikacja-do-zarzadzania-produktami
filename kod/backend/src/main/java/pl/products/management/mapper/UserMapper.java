package pl.products.management.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pl.products.management.model.api.request.RegisterRequest;
import pl.products.management.model.entity.UserEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity map(RegisterRequest request);
}
