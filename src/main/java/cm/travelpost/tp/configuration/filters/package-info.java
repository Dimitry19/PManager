

@org.hibernate.annotations.FilterDefs({
        @FilterDef(name = FilterConstants.ACTIVE_MBR, defaultCondition = "ACTIVE=true"),
        @FilterDef(name = FilterConstants.ACTIVE_MBR_WORK, defaultCondition = "ACTIVE=1"),
        @FilterDef(name = FilterConstants.CANCELLED, defaultCondition = "CANCELLED='0'"),
        @FilterDef(name = FilterConstants.COMPLETED, defaultCondition = "STATUS ='0'"),
        @FilterDef(name = FilterConstants.NOT_COMPLETED, defaultCondition = "STATUS <> 'COMPLETED'"),
        @FilterDef(name = FilterConstants.STATUS, defaultCondition = "STATUS in (:states)",parameters = @ParamDef(name = "states", type = "string")),
        @FilterDef(name = "dateFilter", parameters = @ParamDef(name = "fromDate", type = "timestamp"))

})


package cm.travelpost.tp.configuration.filters;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

