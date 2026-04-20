package com.brewingcode.coffriend_servidor.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.Arrays;

/**
 * Authorization service for role checking.
 */
@Service
public class AuthorizationService {

    /**
     * Check if the authenticated user has a specific role.
     * 
     * @param auth the Spring Security Authentication object
     * @param role the required role
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(Authentication auth, RoleEnum role) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals(role.getSpringSecurityRole()));
    }

    /**
     * Check if the authenticated user has ANY of the given roles.
     * 
     * @param auth the Spring Security Authentication object
     * @param roles the roles to check for
     * @return true if the user has at least one of the roles, false otherwise
     */
    public boolean hasAnyRole(Authentication auth, RoleEnum... roles) {
        if (auth == null || !auth.isAuthenticated() || roles == null || roles.length == 0) {
            return false;
        }
        return auth.getAuthorities().stream()
            .anyMatch(a -> Arrays.stream(roles)
                .anyMatch(role -> a.getAuthority().equals(role.getSpringSecurityRole())));
    }

    /**
     * Check if the authenticated user has ALL of the given roles.
     * 
     * @param auth the Spring Security Authentication object
     * @param roles the required roles
     * @return true if the user has all the roles, false otherwise
     */
    public boolean hasAllRoles(Authentication auth, RoleEnum... roles) {
        if (auth == null || !auth.isAuthenticated() || roles == null || roles.length == 0) {
            return false;
        }
        return Arrays.stream(roles).allMatch(role -> 
            auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role.getSpringSecurityRole()))
        );
    }

    /**
     * Enforce that the user has a specific role, throwing an exception if not.
     * 
     * @param auth the Spring Security Authentication object
     * @param role the required role
     * @throws AccessDeniedException if the user does not have the role
     */
    public void requireRole(Authentication auth, RoleEnum role) {
        if (!hasRole(auth, role)) {
            throw new AccessDeniedException(
                "Access denied: required role " + role.getSpringSecurityRole()
            );
        }
    }

    /**
     * Enforce that the user has any of the given roles, throwing an exception if not.
     * 
     * @param auth the Spring Security Authentication object
     * @param roles the required roles
     * @throws AccessDeniedException if the user does not have any of the roles
     */
    public void requireAnyRole(Authentication auth, RoleEnum... roles) {
        if (!hasAnyRole(auth, roles)) {
            String roleList = Arrays.stream(roles)
                .map(RoleEnum::getSpringSecurityRole)
                .reduce((a, b) -> a + ", " + b)
                .orElse("(none)");
            throw new AccessDeniedException(
                "Access denied: required any of roles [" + roleList + "]"
            );
        }
    }

    /**
     * Check if a user can manage another user's data.
     * 
     * @param auth the Spring Security Authentication object
     * @param targetUserId the user ID being managed/accessed
     * @return true if the current user can manage the target user, false otherwise
     */
    public boolean canManageUser(Authentication auth, Integer targetUserId) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        
        if (targetUserId == null) {
            return false;
        }
        
        Integer currentUserId = (Integer) auth.getPrincipal();
        
        if (hasRole(auth, RoleEnum.ADMIN)) {
            return true;
        }
        
        return currentUserId != null && currentUserId.equals(targetUserId);
    }

    /**
     * Enforce user can manage another user, throwing an exception if not.
     * 
     * @param auth the Spring Security Authentication object
     * @param targetUserId the user ID being managed/accessed
     * @throws AccessDeniedException if the user cannot manage the target user
     */
    public void requireCanManageUser(Authentication auth, Integer targetUserId) {
        if (!canManageUser(auth, targetUserId)) {
            throw new AccessDeniedException(
                "Access denied: cannot manage user " + targetUserId
            );
        }
    }

    /**
     * Get the current authenticated user's ID.
     * 
     * @param auth the Spring Security Authentication object
     * @return the user ID, or null if not authenticated
     */
    public Integer getCurrentUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        try {
            return (Integer) auth.getPrincipal();
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Check if the user is authenticated.
     * 
     * @param auth the Spring Security Authentication object
     * @return true if the user is authenticated, false otherwise
     */
    public boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated() && 
               !auth.getPrincipal().equals("anonymousUser");
    }
}
