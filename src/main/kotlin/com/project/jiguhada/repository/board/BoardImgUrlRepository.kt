package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardImgUrl
import org.springframework.data.jpa.repository.JpaRepository

interface BoardImgUrlRepository: JpaRepository<BoardImgUrl, Long> {
}