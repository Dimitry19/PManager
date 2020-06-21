/*
@TypeDefs({
		@TypeDef(name = "printer_type_enum", typeClass = com.grupposinapsi.jpcore.persistence.hibernate.types.GenericEnumUserType.class, parameters = { @org.hibernate.annotations.Parameter(name = "enumClass", value = "it.fracm.dal.production.ent.vo.enums.PrinterTypeEnum") }),
		@TypeDef(name = "activity_status_enum", typeClass = com.grupposinapsi.jpcore.persistence.hibernate.types.GenericEnumUserType.class, parameters = { @org.hibernate.annotations.Parameter(name = "enumClass", value = "it.fracm.dal.production.ent.vo.enums.ActivityStatusEnum") }),
		@TypeDef(name = "activity_dep_status_enum", typeClass = com.grupposinapsi.jpcore.persistence.hibernate.types.GenericEnumUserType.class, parameters = { @org.hibernate.annotations.Parameter(name = "enumClass", value = "it.fracm.dal.production.ent.vo.enums.ActivityDepStatusEnum") }),
		@TypeDef(name = "authorization_enum", typeClass = com.grupposinapsi.jpcore.persistence.hibernate.types.GenericEnumUserType.class, parameters = { @org.hibernate.annotations.Parameter(name = "enumClass", value = "it.fracm.dal.profile.ent.vo.enums.Authorization") }),
		@TypeDef(name = "status_enum", typeClass = com.grupposinapsi.jpcore.persistence.hibernate.types.GenericEnumUserType.class, parameters = { @org.hibernate.annotations.Parameter(name = "enumClass", value = "it.fracm.dal.common.ent.vo.StatusEnum") }),
		
})
*/

@org.hibernate.annotations.FilterDefs({
		@FilterDef (name= FilterConstants.ACTIVE_MBR        ,defaultCondition="ACTIVE=true" ),
		@FilterDef (name= FilterConstants.CANCELLED         ,defaultCondition="CANCELLED=false"),
})

package cm.packagemanager.pmanager.configuration.filters;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
