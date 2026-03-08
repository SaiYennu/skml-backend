package com.skml.util;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil 
{
	private static final String SECRET =
            "=9SrikaN0akam1ahal0@ks7hmi7-th2AT2har5ao=skml";

    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
	public static String generateToken(String username) 
	{
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public static  String extractUsername(String token)
	{
		return Jwts.parser()
				.setSigningKey(getSigningKey())
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	public static boolean isTokenValid(String token) {
	    try {
	        Jwts.parser()
	            .setSigningKey(getSigningKey())
	            .parseClaimsJws(token); // 🔥 this checks expiry
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

}
