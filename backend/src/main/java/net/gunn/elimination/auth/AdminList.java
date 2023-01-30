package net.gunn.elimination.auth;

import java.util.Set;

record AdminList(Set<String> admins) {
    boolean isAdmin(String email) {
        return admins.contains(email);
    }
}
