package com.animaladoption.api.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuDTO {
	private String label;
	private String routerLink;
	private String icon;
	private Set<String> roles;
	Set<MenuDTO> items;
}
