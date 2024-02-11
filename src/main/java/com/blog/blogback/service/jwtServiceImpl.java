package com.blog.blogback.service;
import java.security.Key;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class jwtServiceImpl implements jwtService{
    private String secretKey = "asdafsdg!@#asdsg#%^$%asdazvxb1232454574gasdafsdg!@#asdsg#%^$%asdazvxb1232454574g";
    @Override
    public String getToken(String key, Object value) {
        Date expireTime = new Date();

        expireTime.setTime(expireTime.getTime() * 1000 * 60 * 5);


        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }
    
    @Override
    public Claims getClaims(String token) {
        if(token != null && !"".equals(token)){
            try{
                byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            }catch(ExpiredJwtException e){
                //만료됨
            }catch(JwtException e){
                //유효하지 않음
            }
        }
        return null;
    }
}
