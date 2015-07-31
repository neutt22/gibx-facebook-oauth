import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Account;
import com.restfb.types.FacebookType;


public class FacebookPublisher {
	
	private String accessToken;
	private String accountName;
	private FacebookClient myFb;
	private Connection<Account> accounts;
	
	public FacebookPublisher(String token){
		accessToken = token;
		myFb = new DefaultFacebookClient(accessToken, Version.VERSION_2_4);
		accounts = myFb.fetchConnection("me/accounts", Account.class);
		
	}
	
	public String postFeed(int account_index, String feed){
		String accountToken = accounts.getData().get(account_index).getAccessToken();
		accountName = accounts.getData().get(account_index).getId();
		FacebookClient account = new DefaultFacebookClient(accountToken, Version.VERSION_2_4);
		FacebookType pubAcctMsg = account.publish("me/feed", FacebookType.class, Parameter.with("message", feed));
		return pubAcctMsg.getId().split("_")[1];
	}
	
	public String getAccountName(){ return accountName; }

}
