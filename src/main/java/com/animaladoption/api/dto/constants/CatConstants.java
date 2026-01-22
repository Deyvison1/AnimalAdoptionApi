package com.animaladoption.api.dto.constants;

public class CatConstants {
	private CatConstants() {
	}

	// Swagger
	public static final String TITLE = "Cats.";
	public static final String DESCRIPTION = "Gerencia os gatos.";

	// Entity
	public static final String SCHEMA = "animal_adoption";

	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/cat";

	// Roles
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
	public static final String ADMIN_READ_AUTHORITY = "hasAnyAuthority('ADMIN', 'ADMIN_READ')";
	public static final String ADMIN_PUBLISH_AUTHORITY = "hasAuthority('ADMIN_PUBLISH')";

	// Response descriptions
	public static final String FIND_BY_ID = "Busca dos do registro pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca dos registros com paginação realizada com sucesso.";
	public static final String CREATED = "Gato criado com sucesso.";
	public static final String UPDATED = "Gato atualizado com sucesso.";
	public static final String DELETED = "Gato excluído com sucesso.";

	public static final String IS_PUBLISH = "Deixa o animal publicado, ira aparecer para o usuario final.";

	public static final String NOT_PUBLISH = "Deixa o animal nao publicado, nao ira aparecer para o usuario final, somente para gerenciamento do administrador.";
}
