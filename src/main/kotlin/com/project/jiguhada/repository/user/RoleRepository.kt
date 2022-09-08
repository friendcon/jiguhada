package com.project.jiguhada.repository.user

import com.project.jiguhada.domain.user.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, String> {
}