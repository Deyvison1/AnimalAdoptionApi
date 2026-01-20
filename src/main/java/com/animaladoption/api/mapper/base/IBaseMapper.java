package com.animaladoption.api.mapper.base;

import com.animaladoption.api.dto.base.BaseDTO;
import com.animaladoption.api.model.base.BaseEntity;

public interface IBaseMapper<E extends BaseEntity, D extends BaseDTO> {
	D toDto(E entity);

	E toEntity(D dto);

}