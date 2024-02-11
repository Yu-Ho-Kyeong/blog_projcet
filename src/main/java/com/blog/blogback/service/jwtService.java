package com.blog.blogback.service;

import io.jsonwebtoken.Claims;

public interface jwtService {
    String getToken(String key, Object value);
    
    Claims getClaims(String token);
}
