package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.BoardListItemResponse
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import org.springframework.data.domain.Pageable

interface BoardRepositorySupport {
    fun findBoardList(query: String?, orderType: BOARD_ORDER_TYPE?, boardCategory: BOARD_CATEGORY?, page: Pageable): List<BoardListItemResponse>
}