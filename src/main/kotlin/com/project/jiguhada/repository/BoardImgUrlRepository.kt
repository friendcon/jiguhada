package com.project.jiguhada.repository

import com.project.jiguhada.domain.BoardImgUrl
import org.springframework.data.jpa.repository.JpaRepository

interface BoardImgUrlRepository: JpaRepository<BoardImgUrl, Long> {
}