package com.animaladoption.api.dto.constants;

public final class MenuConstants {
	private MenuConstants() {
	}

	// Swagger
	public static final String TITLE = "Menus.";
	public static final String DESCRIPTION = "Retorna os menus cadastrados.";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/menu";
	public static final String ADMIN_AUTHORITY = "hasAnyAuthority('ADMIN')";

	public static final String FIND_ALL = "Lista de menus retornada com sucesso.";
}
