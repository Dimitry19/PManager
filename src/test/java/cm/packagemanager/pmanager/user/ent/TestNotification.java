package cm.packagemanager.pmanager.user.ent;

import cm.packagemanager.pmanager.notification.ent.service.FireBaseNotificationSender;

import java.util.ArrayList;
import java.util.List;

public class TestNotification {

	static List<String> putIds;
	public static void main(String[] args) {


		FireBaseNotificationSender sender = new FireBaseNotificationSender();
		//Just I am passed dummy information
		// DeviceID's

		//String tokenId = "e77GryTYi2mmP9HvPWb4oy:APA91bFVvqGnxo3LmaCwwfUcnK_EBX3bH6Non2_N4jdEDsojX7YstPfISp88jkcG53bFZEt5-gncJPVcjzT761fIxvHstmCXjhnck7AH2UZq53esVzIQnP0hIHMZLzXfbP4nENzYkbOp";
		String tokenId = "e4VrJSunCALBi1tmhoJ0Jy:APA91bGRxCJMlu_L2RDQKsI_GBG0I46mSfmJq5aKsz3M-fOl_53kXuK_2UUI90BHy1ybBnE0IESzwc8xxc7EmqabhuViY9ljM4-UR9LMntb6G7soWE8PkT-JW7Q-m1qX2Z8vEqYYIsWV";
		String tokenId1  = "e4VrJSunCALBi1tmhoJ0Jy:APA91bGRxCJMlu_L2RDQKsI_GBG0I46mSfmJq5aKsz3M-fOl_53kXuK_2UUI90BHy1ybBnE0IESzwc8xxc7EmqabhuViY9ljM4-UR9LMntb6G7soWE8PkT-JW7Q-m1qX2Z8vEqYYIsWV";
		String server_key ="AAAAQAV7XxA:APA91bGcY7R0KVoNP-9-JsNII62F2Vy-r_FzzqzMDqOo4SSKsVxdnieysqpNiQjIZYfqMUIpgUcfg8-Ei9d1CiTvi7xPCAZRlOvT9gl9CjNZxs5aKdZc9DH87LWbxWZh3IjwktFj6jVV";
		String message = "Welcome alankit Push Service.";

		putIds= new ArrayList<>();
		putIds.add(tokenId1);
		putIds.add(tokenId);

		/* for Group*/
		sender.sendNotifications(putIds,tokenId,message);

		/*for indevidual*/
		sender.sendNotification( tokenId,message);
	}
}
