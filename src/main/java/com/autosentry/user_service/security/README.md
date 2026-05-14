# Security Module - AutoSentry User Service

## Overview
This package handles the core Identity and Access Management (IAM) for the AutoSentry ecosystem. It implements a **Stateless Authentication Architecture** using JSON Web Tokens (JWT) and Spring Security.

By remaining stateless, the User Service does not store session data in memory, allowing the authentication layer to scale horizontally and respond with minimal latency.

## Key Components

* **`SecurityConfig.java`**: The central configuration class. It disables CSRF (as we are not using browser-based sessions), sets the session management policy to `STATELESS`, and defines route authorization rules (e.g., leaving `/api/auth/**` open while securing all other endpoints). It also provides the `BCryptPasswordEncoder` bean for secure password hashing.
* **`JwtUtil.java`**: The cryptographic utility class. It is responsible for generating, parsing, and validating JWTs using the `jjwt` library and the HMAC SHA-256 algorithm. It handles token expiration (default 24 hours) and securely extracts the secret key from environment variables.
* **`CustomUserDetailsService.java`**: Bridges the custom `User` PostgreSQL entity with Spring Security's internal `UserDetails` interface for database-backed authentication.
* **`JwtAuthenticationFilter.java`**: A custom `OncePerRequestFilter` that intercepts incoming HTTP requests, extracts the Bearer token from the Authorization header, validates the cryptographic signature via `JwtUtil`, and sets the security context.

## Authentication Flow
1. User submits credentials to the `/api/auth/login` endpoint.
2. The system hashes the provided password and compares it against the BCrypt hash in PostgreSQL.
3. Upon success, `JwtUtil` issues a signed JWT containing the user's identity.
4. For subsequent requests, the client includes this JWT in the `Authorization: Bearer <token>` header.
5. The system mathematically verifies the token's signature on the fly, granting access without requiring a database lookup for session validation.