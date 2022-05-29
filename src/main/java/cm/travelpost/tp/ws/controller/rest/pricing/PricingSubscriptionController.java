package cm.travelpost.tp.ws.controller.rest.pricing;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( WSConstants.PRICING_WS)
@Api(value = "Pricing and Subscription -service", description = "Pricing and Subscription Operations",tags ="Pricing and Subscription" )
public class PricingSubscriptionController extends CommonController {

}