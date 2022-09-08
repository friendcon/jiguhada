package com.project.jiguhada.repository

import com.project.jiguhada.domain.BoardComment
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentRepository: JpaRepository<BoardComment, Long> {
}