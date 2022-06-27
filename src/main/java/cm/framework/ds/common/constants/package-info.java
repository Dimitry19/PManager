

@org.hibernate.annotations.FilterDefs({
        @FilterDef(name = cm.framework.ds.common.constants.DefaultFilterConstants.CANCELLED, defaultCondition = "CANCELLED is false"),
        @FilterDef(name = cm.framework.ds.common.constants.DefaultFilterConstants.COMPLETED, defaultCondition = "STATUS ='0'"),
        @FilterDef(name = cm.framework.ds.common.constants.DefaultFilterConstants.NOT_COMPLETED, defaultCondition = "STATUS <> 'COMPLETED'"),
        @FilterDef(name = cm.framework.ds.common.constants.DefaultFilterConstants.STATUS, defaultCondition = "STATUS in (:states)",parameters = @ParamDef(name = "states", type = "string")),
        @FilterDef(name = "dateFilter", parameters = @ParamDef(name = "fromDate", type = "timestamp"))

})


package cm.framework.ds.common.constants;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

