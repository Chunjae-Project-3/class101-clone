package net.fullstack.class101clone.filter;

public interface JWTProperties {
	public String SECRET = "secret";
//	public int EXPIRATION_TIME = 86400000; // 1일 (1/1000초)
	public int EXPIRATION_TIME = 86400; // 1일
	public String TOKEN_PREFIX = "Bearer ";
	public String HEADER_STRING = "Authorization";
}