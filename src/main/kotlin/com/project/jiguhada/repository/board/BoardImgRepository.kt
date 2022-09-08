package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardImg
import org.springframework.data.jpa.repository.JpaRepository

interface BoardImgRepository: JpaRepository<BoardImg, Long> {
}