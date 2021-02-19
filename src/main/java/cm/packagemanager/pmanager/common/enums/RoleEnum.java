package cm.packagemanager.pmanager.common.enums;

import cm.packagemanager.pmanager.common.utils.StringUtils;

public enum RoleEnum {
	ADMIN("ADMIN"),
	USER("USER");
	private String value;

	private RoleEnum(String value) {
		this.value = value;
	}

	public String toValue(){
		return value;
	}

	public static RoleEnum fromValue(String value){
		if(StringUtils.isEmpty(value)){
			return null;
		}

		for (RoleEnum e : values()) {
			if(e.value.equals(value)){
				return e;
			}
		}
		throw new RuntimeException("Valore " + value + " non valido");
	}
}
