package com.ts.interceptor.webservice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.lang.StringUtils;
import org.apache.ws.security.WSPasswordCallback;


public class ClientPasswordCallback implements CallbackHandler {
	
	private Map<String, String> users;

	public ClientPasswordCallback() {
		users = new HashMap<String, String>();
		String user = "";
		String password = "";
		try {
			user = Aes.getDecrypt(ApplicationProperties.getPropertyValue("ws.user"));
			password = Aes.getDecrypt(ApplicationProperties.getPropertyValue("ws.password"));
			if(StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
				user = "temp";
				password = "temp";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		users.put(user, password);
	}
	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
            WSPasswordCallback pc = (WSPasswordCallback)callbacks[i];

            String pass = users.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }
	}
}