package pl.products.management.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DateMapper {

    default Date map(LocalDateTime rawDate){

        Instant rawDateInstant = rawDate.toInstant(ZoneOffset.UTC);

        return Date.from(rawDateInstant);
    }
}
