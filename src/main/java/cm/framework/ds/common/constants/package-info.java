

@org.hibernate.annotations.FilterDefs({
        @FilterDef(name = cm.travelpost.tp.configuration.filters.FilterConstants.ACTIVE_MBR, defaultCondition = "ACTIVE=true"),
        @FilterDef(name = cm.travelpost.tp.configuration.filters.FilterConstants.ACTIVE_MBR_WORK, defaultCondition = "ACTIVE=1"),
        @FilterDef(name = cm.travelpost.tp.configuration.filters.FilterConstants.CANCELLED, defaultCondition = "CANCELLED='0'"),
        @FilterDef(name = cm.travelpost.tp.configuration.filters.FilterConstants.COMPLETED, defaultCondition = "STATUS ='0'"),
        @FilterDef(name = cm.travelpost.tp.configuration.filters.FilterConstants.NOT_COMPLETED, defaultCondition = "STATUS <> 'COMPLETED'"),
        @FilterDef(name = FilterConstants.STATUS, defaultCondition = "STATUS in (:states)",parameters = @ParamDef(name = "states", type = "string")),
        @FilterDef(name = "dateFilter", parameters = @ParamDef(name = "fromDate", type = "timestamp"))

})


package cm.framework.ds.common.constants;

import cm.travelpost.tp.configuration.filters.FilterConstants;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

