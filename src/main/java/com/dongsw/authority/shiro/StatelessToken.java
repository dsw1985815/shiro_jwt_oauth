package com.dongsw.authority.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class StatelessToken implements AuthenticationToken {
	private static final long	serialVersionUID	= -8000346805338984862L;

	private String				username;
	private String				jwtToken;
	private String				clientId;
	private Integer				loginPlat;

	public StatelessToken(String username, String jwtToken, String clientId, Integer loginType)
	{
		this.username = username;
		this.jwtToken = jwtToken;
		this.clientId = clientId;
		this.loginPlat = loginType;
	}

	//省略部分代码
	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return jwtToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getLoginPlat() {
		return loginPlat;
	}

	public void setLoginPlat(Integer loginPlat) {
		this.loginPlat = loginPlat;
	}

}