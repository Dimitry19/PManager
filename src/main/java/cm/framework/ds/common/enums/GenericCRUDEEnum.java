package cm.framework.ds.common.enums;


import cm.travelpost.tp.common.utils.StringUtils;

public enum GenericCRUDEEnum {
	CREATE("Creation"),
	READ("Read"),
	UPDATE("Update"),
	DELETE("Delete");

	String value;
	GenericCRUDEEnum(String value) {
		this.value = value;
	}

	public String toValue() {	return value;}

	public static GenericCRUDEEnum fromValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		for (GenericCRUDEEnum e : values()) {
			if (e.value.equals(value)) {
				return e;
			}
		}
		throw new RuntimeException("Valeur  " + value + " non valide");
	}


	public  static GenericCRUDEEnum getGenericCRUDEEnum(String type) {

		for (GenericCRUDEEnum a : GenericCRUDEEnum.values()) {
			if (StringUtils.equals(type, a.toString())) {
				return a;
			}
		}
		throw new RuntimeException("Valeur  " + type + " non valide");
	}
}