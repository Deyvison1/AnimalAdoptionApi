package com.animaladoption.api.dto.constants;

/**
 * Constantes relacionadas ao gerenciamento de imagens de animais.
 * Inclui descrições do Swagger, caminhos base da API, e mensagens de resposta.
 */
public final class AnimalImageConstants {

	private AnimalImageConstants() {
	}

	// ============================================================
	// 🔹 SWAGGER
	// ============================================================
	public static final String TITLE = "Animal Image API";
	public static final String DESCRIPTION = "Gerencia o upload, download, ativação e exclusão de imagens associadas aos animais.";

	// ============================================================
	// 🔹 PATHS / ENDPOINTS
	// ============================================================
	public static final String BASE_API = "/api";
	public static final String BASE_PATH = BASE_API + "/animal-image";

	// ============================================================
	// 🔹 SECURITY (AUTHORITIES)
	// ============================================================
	public static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";
	public static final String ADMIN_READ_AUTHORITY = "hasAnyAuthority('ADMIN', 'ADMIN_READ')";

	// ============================================================
	// 🔹 RESPONSE DESCRIPTIONS
	// ============================================================
	public static final String FIND_BY_ID = "Busca do registro de imagem pelo identificador realizada com sucesso.";
	public static final String FIND_ALL = "Busca das imagens com paginação realizada com sucesso.";
	public static final String CREATED = "Imagem do animal criada com sucesso.";
	public static final String UPDATED = "Imagem do animal atualizada com sucesso.";
	public static final String DELETED = "Imagem do animal excluída com sucesso.";

	public static final String UPLOAD = "Upload de imagem do animal realizado com sucesso.";
	public static final String DOWNLOAD = "Download da imagem do animal realizado com sucesso.";
	public static final String ACTIVE_IMAGE = "Imagem do animal ativada com sucesso.";

	// ============================================================
	// 🔹 PARAM DESCRIPTIONS (para Swagger / documentação)
	// ============================================================
	public static final String PARAM_ANIMAL_ID = "Identificador único (UUID) do animal.";
	public static final String PARAM_IMAGE_ID = "Identificador único (UUID) da imagem.";
	public static final String PARAM_FILE = "Arquivo de imagem a ser enviado (formato multipart).";
	public static final String PARAM_ACTIVE = "Define se a imagem será marcada como ativa após o upload (valor padrão: false).";

	// ============================================================
	// 🔹 OPERATION SUMMARIES
	// ============================================================
	public static final String OP_UPLOAD_SUMMARY = "Realiza o upload de uma nova imagem associada a um animal.";
	public static final String OP_DOWNLOAD_SUMMARY = "Realiza o download de uma imagem específica do animal.";
	public static final String OP_DELETE_SUMMARY = "Remove uma imagem associada a um animal pelo identificador.";
	public static final String OP_ACTIVATE_SUMMARY = "Ativa uma imagem específica do animal, tornando-a principal.";
}
