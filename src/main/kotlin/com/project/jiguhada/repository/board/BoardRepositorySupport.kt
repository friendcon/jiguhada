package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.BoardListItemResponse
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.project.jiguhada.util.BOARD_SEARCH_TYPE
import org.springframework.data.domain.Pageable

interface BoardRepositorySupport {
    fun findBoardList(query: String?, orderType: BOARD_ORDER_TYPE?,
                      boardCategory: BOARD_CATEGORY?, page: Pageable,
                      searchType: BOARD_SEARCH_TYPE?
    ): List<BoardListItemResponse>

    fun findBoardListByUserId(userId: Long, page: Pageable): List<BoardListItemResponse>

    fun countBoardByCategoryAndBoardSearch(query: String?, searchType: BOARD_SEARCH_TYPE?, boardCategory: BOARD_CATEGORY?): Long
    fun countBoardByCategory(category: BOARD_CATEGORY): Long

    fun countBoardBySearch(searchType: BOARD_SEARCH_TYPE?, query: String?): Long
}