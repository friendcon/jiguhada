package com.project.jiguhada.repository

import com.project.jiguhada.domain.BoardLike
import org.springframework.data.jpa.repository.JpaRepository

interface BoardLikeRepository: JpaRepository<BoardLike, Long> {
}