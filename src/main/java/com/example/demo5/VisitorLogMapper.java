package com.example.demo5;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitorLogMapper {
    VisitorLogDtoPortal convertToDtoPortal(VisitorLog visitorLog);


}
