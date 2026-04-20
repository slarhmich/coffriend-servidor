package com.brewingcode.coffriend_servidor.security;

/**
 * Enum for all valid application roles.
 * 
 * Provides a single source of truth for:
 * - Database/entity role values (lowercase)
 * - Spring Security role format (ROLE_* prefix)
 * - Role descriptions
 * 
 * Valid roles: ADMIN, STAFF, CLIENT
 */
public enum RoleEnum {
    ADMIN("admin", "Admin, full system access"),
    STAFF("staff", "Shop staff member, can manage orders and customer interactions"),
    CLIENT("client", "Customer, has gamification features (levels, points, badges)");

    private final String dbValue;
    private final String description;

    RoleEnum(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    /**
     * Get the database/entity representation of this role.
     * @return lowercase role string (e.g., "admin", "staff", "client")
     */
    public String getDbValue() {
        return dbValue;
    }

    /**
     * Get the Spring Security representation of this role.
     * 
     * @return Spring Security role format (e.g., "ROLE_admin", "ROLE_staff", "ROLE_client")
     */
    public String getSpringSecurityRole() {
        return "ROLE_" + dbValue;
    }

    /**
     * Get a description of this role's responsibilities.
     * 
     * @return role description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Convert a database/entity role string to RoleEnum.
     * Validates that the provided role string is a known role.
     * 
     * @param dbValue the database role string
     * @return the corresponding RoleEnum
     * @throws IllegalArgumentException if the role string is not recognized or role value is null.
     */
    public static RoleEnum fromDbValue(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("Role value cannot be null");
        }
        for (RoleEnum role : RoleEnum.values()) {
            if (role.dbValue.equals(dbValue.toLowerCase())) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + dbValue + ". Valid roles are: ADMIN, STAFF, CLIENT");
    }

    /**
     * Check if this role matches the provided role string.
     * 
     * @param roleString the role string to check
     * @return true if the role matches, false otherwise
     */
    public boolean matches(String roleString) {
        if (roleString == null) {
            return false;
        }
        return this.dbValue.equals(roleString.toLowerCase()) || 
               this.getSpringSecurityRole().equals(roleString);
    }
}
