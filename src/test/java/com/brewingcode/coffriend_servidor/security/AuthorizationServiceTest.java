package com.brewingcode.coffriend_servidor.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthorizationService.
 * 
 * Tests cover:
 * - Role checking (hasRole, hasAnyRole, hasAllRoles)
 * - Authorization enforcement (requireRole, requireAnyRole)
 * - User management checks (canManageUser)
 * - Edge cases (null auth, unauthenticated users)
 */
@DisplayName("AuthorizationService Tests")
public class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    private Authentication adminAuth;
    private Authentication staffAuth;
    private Authentication clientAuth;
    private Authentication nullAuth;
    private Authentication anonymousAuth;

    @BeforeEach
    void setUp() {
        authorizationService = new AuthorizationService();

        // Admin authentication
        adminAuth = new UsernamePasswordAuthenticationToken(
            1,
            null,
            Collections.singletonList(new SimpleGrantedAuthority(RoleEnum.ADMIN.getSpringSecurityRole()))
        );

        // Staff authentication
        staffAuth = new UsernamePasswordAuthenticationToken(
            2,
            null,
            Collections.singletonList(new SimpleGrantedAuthority(RoleEnum.STAFF.getSpringSecurityRole()))
        );

        // Client authentication
        clientAuth = new UsernamePasswordAuthenticationToken(
            3,
            null,
            Collections.singletonList(new SimpleGrantedAuthority(RoleEnum.CLIENT.getSpringSecurityRole()))
        );

        // Null authentication
        nullAuth = null;

        // Anonymous authentication
        anonymousAuth = new UsernamePasswordAuthenticationToken(
            "anonymousUser",
            null,
            Collections.emptyList()
        );
    }

    // ============ hasRole Tests ============

    @Test
    @DisplayName("hasRole returns true when user has the required role")
    void testHasRoleWithMatchingRole() {
        assertTrue(authorizationService.hasRole(adminAuth, RoleEnum.ADMIN));
        assertTrue(authorizationService.hasRole(staffAuth, RoleEnum.STAFF));
        assertTrue(authorizationService.hasRole(clientAuth, RoleEnum.CLIENT));
    }

    @Test
    @DisplayName("hasRole returns false when user lacks the required role")
    void testHasRoleWithMismatchedRole() {
        assertFalse(authorizationService.hasRole(adminAuth, RoleEnum.STAFF));
        assertFalse(authorizationService.hasRole(staffAuth, RoleEnum.ADMIN));
        assertFalse(authorizationService.hasRole(clientAuth, RoleEnum.ADMIN));
    }

    @Test
    @DisplayName("hasRole returns false for null authentication")
    void testHasRoleWithNullAuth() {
        assertFalse(authorizationService.hasRole(nullAuth, RoleEnum.ADMIN));
    }

    @Test
    @DisplayName("hasRole returns false for anonymous authentication")
    void testHasRoleWithAnonymousAuth() {
        assertFalse(authorizationService.hasRole(anonymousAuth, RoleEnum.ADMIN));
    }

    // ============ hasAnyRole Tests ============

    @Test
    @DisplayName("hasAnyRole returns true when user has any of the specified roles")
    void testHasAnyRoleWithMatchingRole() {
        assertTrue(authorizationService.hasAnyRole(adminAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
        assertTrue(authorizationService.hasAnyRole(staffAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
        assertTrue(authorizationService.hasAnyRole(clientAuth, RoleEnum.STAFF, RoleEnum.CLIENT));
    }

    @Test
    @DisplayName("hasAnyRole returns false when user has none of the specified roles")
    void testHasAnyRoleWithMismatchedRoles() {
        assertFalse(authorizationService.hasAnyRole(adminAuth, RoleEnum.STAFF, RoleEnum.CLIENT));
        assertFalse(authorizationService.hasAnyRole(staffAuth, RoleEnum.ADMIN, RoleEnum.CLIENT));
    }

    @Test
    @DisplayName("hasAnyRole returns false for null authentication")
    void testHasAnyRoleWithNullAuth() {
        assertFalse(authorizationService.hasAnyRole(nullAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
    }

    @Test
    @DisplayName("hasAnyRole returns false for empty role list")
    void testHasAnyRoleWithEmptyRoles() {
        assertFalse(authorizationService.hasAnyRole(adminAuth));
    }

    // ============ hasAllRoles Tests ============

    @Test
    @DisplayName("hasAllRoles returns true when user has all specified roles (single role)")
    void testHasAllRolesWithSingleRole() {
        assertTrue(authorizationService.hasAllRoles(adminAuth, RoleEnum.ADMIN));
        assertTrue(authorizationService.hasAllRoles(staffAuth, RoleEnum.STAFF));
    }

    @Test
    @DisplayName("hasAllRoles returns false when user lacks any specified role")
    void testHasAllRolesWithMultipleRoles() {
        // Most users won't have multiple roles in practice
        assertFalse(authorizationService.hasAllRoles(adminAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
        assertFalse(authorizationService.hasAllRoles(staffAuth, RoleEnum.STAFF, RoleEnum.ADMIN));
    }

    @Test
    @DisplayName("hasAllRoles returns false for null authentication")
    void testHasAllRolesWithNullAuth() {
        assertFalse(authorizationService.hasAllRoles(nullAuth, RoleEnum.ADMIN));
    }

    // ============ canManageUser Tests ============

    @Test
    @DisplayName("canManageUser returns true for admin accessing any user")
    void testCanManageUserAsAdmin() {
        assertTrue(authorizationService.canManageUser(adminAuth, 100));
        assertTrue(authorizationService.canManageUser(adminAuth, 999));
    }

    @Test
    @DisplayName("canManageUser returns true for client accessing own account")
    void testCanManageUserAsClientOwnAccount() {
        assertTrue(authorizationService.canManageUser(clientAuth, 3)); // clientAuth principal is 3
    }

    @Test
    @DisplayName("canManageUser returns false for client accessing different account")
    void testCanManageUserAsClientOtherAccount() {
        assertFalse(authorizationService.canManageUser(clientAuth, 100));
    }

    @Test
    @DisplayName("canManageUser returns false for staff (only admins and self)")
    void testCanManageUserAsStaff() {
        // Staff can only manage their own account
        assertTrue(authorizationService.canManageUser(staffAuth, 2));   // self = 2
        assertFalse(authorizationService.canManageUser(staffAuth, 100)); // other
    }

    @Test
    @DisplayName("canManageUser returns false for null authentication")
    void testCanManageUserWithNullAuth() {
        assertFalse(authorizationService.canManageUser(nullAuth, 1));
    }

    @Test
    @DisplayName("canManageUser returns false when target user ID is null")
    void testCanManageUserWithNullTargetId() {
        // Admins should also return false when targeting null
        assertFalse(authorizationService.canManageUser(adminAuth, null));
        assertFalse(authorizationService.canManageUser(clientAuth, null));
    }

    // ============ requireRole Tests ============

    @Test
    @DisplayName("requireRole succeeds for matching role")
    void testRequireRoleSuccess() {
        assertDoesNotThrow(() -> authorizationService.requireRole(adminAuth, RoleEnum.ADMIN));
    }

    @Test
    @DisplayName("requireRole throws AccessDeniedException for mismatched role")
    void testRequireRoleThrows() {
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
            () -> authorizationService.requireRole(staffAuth, RoleEnum.ADMIN));
    }

    // ============ requireAnyRole Tests ============

    @Test
    @DisplayName("requireAnyRole succeeds when user has any specified role")
    void testRequireAnyRoleSuccess() {
        assertDoesNotThrow(() -> authorizationService.requireAnyRole(adminAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
        assertDoesNotThrow(() -> authorizationService.requireAnyRole(staffAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
    }

    @Test
    @DisplayName("requireAnyRole throws AccessDeniedException when user has no specified role")
    void testRequireAnyRoleThrows() {
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
            () -> authorizationService.requireAnyRole(clientAuth, RoleEnum.ADMIN, RoleEnum.STAFF));
    }

    // ============ requireCanManageUser Tests ============

    @Test
    @DisplayName("requireCanManageUser succeeds for admin")
    void testRequireCanManageUserAsAdmin() {
        assertDoesNotThrow(() -> authorizationService.requireCanManageUser(adminAuth, 100));
    }

    @Test
    @DisplayName("requireCanManageUser succeeds for client managing own account")
    void testRequireCanManageUserAsClientSelf() {
        assertDoesNotThrow(() -> authorizationService.requireCanManageUser(clientAuth, 3));
    }

    @Test
    @DisplayName("requireCanManageUser throws for client managing other account")
    void testRequireCanManageUserAsClientOther() {
        assertThrows(org.springframework.security.access.AccessDeniedException.class,
            () -> authorizationService.requireCanManageUser(clientAuth, 100));
    }

    // ============ getCurrentUserId Tests ============

    @Test
    @DisplayName("getCurrentUserId returns user ID from authentication principal")
    void testGetCurrentUserIdSuccess() {
        assertEquals(Integer.valueOf(1), authorizationService.getCurrentUserId(adminAuth));
        assertEquals(Integer.valueOf(2), authorizationService.getCurrentUserId(staffAuth));
        assertEquals(Integer.valueOf(3), authorizationService.getCurrentUserId(clientAuth));
    }

    @Test
    @DisplayName("getCurrentUserId returns null for null authentication")
    void testGetCurrentUserIdWithNullAuth() {
        assertNull(authorizationService.getCurrentUserId(nullAuth));
    }

    @Test
    @DisplayName("getCurrentUserId returns null when principal is not an Integer")
    void testGetCurrentUserIdWithNonIntegerPrincipal() {
        assertNull(authorizationService.getCurrentUserId(anonymousAuth));
    }

    // ============ isAuthenticated Tests ============

    @Test
    @DisplayName("isAuthenticated returns true for valid authentication")
    void testIsAuthenticatedTrue() {
        assertTrue(authorizationService.isAuthenticated(adminAuth));
        assertTrue(authorizationService.isAuthenticated(staffAuth));
        assertTrue(authorizationService.isAuthenticated(clientAuth));
    }

    @Test
    @DisplayName("isAuthenticated returns false for null authentication")
    void testIsAuthenticatedWithNullAuth() {
        assertFalse(authorizationService.isAuthenticated(nullAuth));
    }

    @Test
    @DisplayName("isAuthenticated returns false for anonymous authentication")
    void testIsAuthenticatedWithAnonymousAuth() {
        assertFalse(authorizationService.isAuthenticated(anonymousAuth));
    }
}
