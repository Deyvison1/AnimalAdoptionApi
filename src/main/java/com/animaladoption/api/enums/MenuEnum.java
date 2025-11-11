package com.animaladoption.api.enums;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.animaladoption.api.dto.MenuDTO;

import lombok.Getter;

@Getter
public enum MenuEnum {

	// --- MENUS PRINCIPAIS ---
	CONTROL_TYPE_BREED("Controle de tipos e raças", null, "pi pi-file", getRolesAdmin()),
	CONTROL_ANIMAL("Controle de animais", null, "pi pi-file", getRolesAdmin()),
	// --- SUBMENUS
	BREED("Raças", null, "pi pi-list", getRolesAdmin(), CONTROL_TYPE_BREED),
	TYPE("Tipos", null, "pi pi-list", getRolesAdmin(), CONTROL_TYPE_BREED),

	ANIMAL_DOG("Cachorros", null, "pi pi-list", getRolesAdmin(), CONTROL_ANIMAL),
	ANIMAL_CAT("Gatos", null, "pi pi-list", getRolesAdmin(), CONTROL_ANIMAL),
	// --- SUBMENUS

	CREATE_ALL_BREED("Nova raça", "/admin/breed/form", "pi pi-plus", getRolesAdmin(), BREED),
	FIND_ALL_BREED("Buscar raças", "/admin/breed/list", "pi pi-search", getRolesAdminRead(), BREED),

	CREATE_ALL_TYPE("Novo tipo", "/admin/animal-type/form", "pi pi-plus", getRolesAdmin(), TYPE),
	FIND_ALL_TYPE("Buscar tipos", "/admin/animal-type/list", "pi pi-search", getRolesAdminRead(), TYPE),

	// DOG MENU
	CREATE_ALL_ANIMAL_DOG("Novo cachorro", "/admin/dog/form", "pi pi-plus", getRolesAdmin(), ANIMAL_DOG),
	FIND_ALL_ANIMAL_DOG("Buscar cachorros", "/admin/dog/list", "pi pi-search", getRolesAdminRead(), ANIMAL_DOG),

	// CAT MENU
	CREATE_ALL_ANIMAL_CAT("Novo gato", "/admin/cat/form", "pi pi-plus", getRolesAdmin(), ANIMAL_CAT),
	FIND_ALL_ANIMAL_CAT("Buscar gatos", "/admin/cat/list", "pi pi-search", getRolesAdminRead(), ANIMAL_CAT);

	private final String label;
	private final String routerLink;
	private final String icon;
	private final Set<String> roles;
	private final MenuEnum items;

	MenuEnum(String label, String url, String icon, Set<String> roles) {
		this(label, url, icon, roles, null);
	}

	MenuEnum(String label, String routerLink, String icon, Set<String> roles, MenuEnum item) {
		this.label = label;
		this.routerLink = routerLink;
		this.icon = icon;
		this.roles = roles;
		this.items = item;
	}

	// ================================================================
	public static Set<MenuDTO> findAll() {
		return Arrays.stream(values()).filter(menu -> menu.getItems() == null).map(MenuEnum::toDTO)
				.sorted(Comparator.comparing(MenuDTO::getLabel)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private static MenuDTO toDTO(MenuEnum menu) {
		Set<MenuDTO> submenus = Arrays.stream(values()).filter(sub -> menu.equals(sub.getItems())).map(MenuEnum::toDTO)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return new MenuDTO(menu.getLabel(), menu.getRouterLink(), menu.getIcon(), menu.getRoles(), submenus);
	}
	
	private static Set<String> getRolesAdmin() {
		return Set.of("ADMIN");
	}
	
	private static Set<String> getRolesAdminRead() {
		return Set.of("ADMIN", "ADMIN_READ");
	}
}
