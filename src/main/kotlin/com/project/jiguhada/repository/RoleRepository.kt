package com.project.jiguhada.repository

import com.project.jiguhada.domain.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, String> {
}