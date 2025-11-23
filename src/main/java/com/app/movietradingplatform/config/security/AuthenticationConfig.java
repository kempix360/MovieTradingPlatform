package com.app.movietradingplatform.config.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "MovieRealm")
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/MovieDb",
        callerQuery = "select password from users where username = ?",
        groupsQuery = "select role from user_roles where user_id = (select id from users where username = ?)",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class AuthenticationConfig {
}