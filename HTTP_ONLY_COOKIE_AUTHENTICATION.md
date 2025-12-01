# HTTP-Only Cookie Authentication Implementation

## Overview
This document describes the implementation of secure HTTP-only cookie-based authentication with access and refresh tokens following industry best practices.

## What Changed?

### 1. **LoginController** - Cookie-Based Token Delivery
- **Access Token**: Sent as HTTP-only cookie named `accessToken`
- **Refresh Token**: Sent as HTTP-only cookie named `refreshToken`
- **Response Body**: Now returns only user data (no tokens in JSON)

#### Endpoints:

**POST `/api/v1/login/authentication`**
- Authenticates user and sets both access and refresh tokens as HTTP-only cookies
- Returns user information in response body:
```json
{
  "code": 200,
  "message": "Login successful",
  "data": {
    "id": 12,
    "firstName": "Nimal",
    "lastName": "Perera",
    "email": "sample12@gmail.com",
    "role": "CUSTOMER"
  }
}
```

**POST `/api/v1/login/refresh-token`**
- Automatically reads refresh token from cookie (no request body needed)
- Issues new access token as HTTP-only cookie
- Returns success message

**POST `/api/v1/login/logout`**
- Clears both access and refresh token cookies
- Returns success message

### 2. **JwtRequestFilter** - Cookie-Based Token Reading
- **Primary Method**: Reads `accessToken` from HTTP-only cookie
- **Fallback Method**: Still supports Authorization header (for backwards compatibility/testing)
- Browser automatically sends cookies with every request

### 3. **WebSecurityConfiguration** - Security Updates
- Added logout and refresh-token endpoints to public access list
- CORS configured with `allowCredentials: true` to support cookies

## Cookie Configuration

### Access Token Cookie:
```java
Cookie Properties:
- Name: "accessToken"
- HttpOnly: true (JavaScript cannot access)
- Secure: false (set to true in production with HTTPS)
- Path: "/"
- MaxAge: {jwt.token-validity} / 1000 seconds
```

### Refresh Token Cookie:
```java
Cookie Properties:
- Name: "refreshToken"
- HttpOnly: true (JavaScript cannot access)
- Secure: false (set to true in production with HTTPS)
- Path: "/"
- MaxAge: {jwt.refresh-token-validity} / 1000 seconds
```

## Frontend Integration

### Login Request:
```javascript
fetch('http://localhost:8081/api/v1/login/authentication', {
  method: 'POST',
  credentials: 'include', // IMPORTANT: Allow sending/receiving cookies
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    username: 'sample12@gmail.com',
    password: 'password123'
  })
})
.then(response => response.json())
.then(data => {
  // Cookies are automatically stored by browser
  console.log('User:', data.data);
});
```

### Protected API Requests:
```javascript
fetch('http://localhost:8081/api/v1/user/profile', {
  method: 'GET',
  credentials: 'include', // Browser automatically sends accessToken cookie
})
.then(response => response.json())
.then(data => console.log(data));
```

### Refresh Token:
```javascript
fetch('http://localhost:8081/api/v1/login/refresh-token', {
  method: 'POST',
  credentials: 'include', // Browser automatically sends refreshToken cookie
})
.then(response => response.json())
.then(data => {
  console.log('Token refreshed');
  // New accessToken cookie automatically updated
});
```

### Logout:
```javascript
fetch('http://localhost:8081/api/v1/login/logout', {
  method: 'POST',
  credentials: 'include',
})
.then(response => response.json())
.then(data => {
  console.log('Logged out successfully');
  // Cookies automatically cleared
});
```

## Security Benefits

### 1. **XSS Protection**
- `HttpOnly` flag prevents JavaScript access to tokens
- Even if XSS vulnerability exists, attacker cannot steal tokens

### 2. **Automatic Token Management**
- Browser handles cookie storage and transmission
- No manual localStorage/sessionStorage manipulation needed
- No risk of developers accidentally logging tokens

### 3. **CSRF Protection** (Future Enhancement)
- Can be enhanced with SameSite cookie attribute
- Can implement CSRF tokens for state-changing operations

### 4. **Token Separation**
- Access token: Short-lived (15 minutes typical)
- Refresh token: Long-lived (7 days typical)
- Separate cookies make rotation easier

## Testing with Postman/Swagger

### Using Postman:
1. Send login request to `/api/v1/login/authentication`
2. Check "Cookies" tab - you'll see `accessToken` and `refreshToken`
3. For subsequent requests, cookies are automatically sent
4. You can manually inspect cookies in Postman's Cookie Manager

### Using Swagger:
Swagger UI might have limitations with cookies. For testing:
- Use Postman for cookie-based authentication
- Or temporarily use Authorization header fallback for Swagger

## Production Checklist

Before deploying to production:

- [ ] Set `cookie.setSecure(true)` in LoginController (requires HTTPS)
- [ ] Update CORS allowedOriginPattern to production domain
- [ ] Consider adding `SameSite` attribute to cookies:
  ```java
  // For Spring Boot 2.6+
  response.setHeader("Set-Cookie", 
    "accessToken=" + token + 
    "; HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=" + maxAge);
  ```
- [ ] Implement CSRF protection if needed
- [ ] Set appropriate cookie expiry times in application.properties
- [ ] Remove console.log statements from JwtRequestFilter

## Configuration Properties

Ensure your `application.properties` has:
```properties
jwt.secret=your-secret-key-here
jwt.token-validity=900000          # 15 minutes in milliseconds
jwt.refresh-token-validity=604800000  # 7 days in milliseconds
```

## Backwards Compatibility

The implementation maintains backwards compatibility:
- Authorization header with Bearer token still works
- Gradually migrate frontend to use cookies
- Both methods can coexist during transition

## Token Flow Diagram

```
┌─────────────┐                    ┌─────────────┐
│   Browser   │                    │   Backend   │
└──────┬──────┘                    └──────┬──────┘
       │                                  │
       │  POST /login/authentication      │
       │  {username, password}            │
       ├─────────────────────────────────>│
       │                                  │
       │  200 OK + Set-Cookie headers     │
       │  Set-Cookie: accessToken=...     │
       │  Set-Cookie: refreshToken=...    │
       │  Body: {user data}               │
       │<─────────────────────────────────┤
       │                                  │
       │  GET /api/v1/user/profile        │
       │  Cookie: accessToken=...         │
       ├─────────────────────────────────>│
       │                                  │
       │  200 OK {profile data}           │
       │<─────────────────────────────────┤
       │                                  │
       │  POST /login/refresh-token       │
       │  Cookie: refreshToken=...        │
       ├─────────────────────────────────>│
       │                                  │
       │  200 OK + Set-Cookie             │
       │  Set-Cookie: accessToken=...     │
       │<─────────────────────────────────┤
       │                                  │
```

## Troubleshooting

### Cookies Not Being Set:
- Check CORS configuration includes `allowCredentials: true`
- Ensure frontend uses `credentials: 'include'`
- Check browser DevTools > Application > Cookies

### Cookies Not Being Sent:
- Verify `credentials: 'include'` in fetch requests
- Check cookie Path matches request URL
- Ensure cookies haven't expired

### Authentication Failing:
- Check browser console for cookie presence
- Verify JwtRequestFilter is reading cookie correctly
- Check backend logs for token validation errors

## References
- [OWASP Secure Cookie Guidelines](https://owasp.org/www-community/controls/SecureCookieAttribute)
- [MDN HTTP Cookies](https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

