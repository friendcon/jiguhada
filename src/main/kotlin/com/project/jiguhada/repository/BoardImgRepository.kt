package com.project.jiguhada.repository

import com.project.jiguhada.domain.BoardImg
import org.springframework.data.jpa.repository.JpaRepository

interface BoardImgRepository: JpaRepository<BoardImg, Long> {
}