package com.zhx.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {

    // 签名密钥，实际生产环境中应从配置文件读取，长度至少 32 字符
    private static final String SECRET = "CakeMallSecretKeyForJwtSigning123456";
    // Token 过期时间：24小时 (毫秒)
    private static final long EXPIRE = 60 * 60 * 24 * 1000;

    // 生成安全密钥
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 生成 Token
     * @param userId 用户ID
     * @return 加密后的 Token 字符串
     */
    public static String generateToken(Integer userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token 获取用户ID
     * @param token 加密 Token
     * @return Claims 对象 (包含 Payload 信息)
     */
    public static Claims getClaimsByToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}