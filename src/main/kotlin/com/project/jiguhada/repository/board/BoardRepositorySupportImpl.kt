package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.BoardListItemResponse
import com.project.jiguhada.controller.dto.board.QBoardListItemResponse
import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.QBoard.board
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class BoardRepositorySupportImpl(
    private val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(Board::class.java), BoardRepositorySupport {

    override fun findBoardList(
        query: String?,
        orderType: BOARD_ORDER_TYPE?,
        boardCategory: BOARD_CATEGORY?,
        page: Pageable
    ): List<BoardListItemResponse> {
        return queryFactory.select(QBoardListItemResponse(
            board.boardCategory.categoryName,
            board.id,
            board.title,
            board.userEntity.nickname,
            board.createdDate,
            board.view_count,
            board.boardLikes.size().longValue(),
            board.boardCommentsList.size().longValue()
        ))
            .from(board)
            .where(
                isTitleOrContentContainQuery(query),
                isSameCategory(boardCategory)
            )
            .orderBy(
                getOrderSpecifier(orderType)
            )
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    private fun isTitleOrContentContainQuery(query: String?): BooleanExpression? {
        return when(StringUtils.isNullOrEmpty(query)) {
            true -> null
            false -> board.title.containsIgnoreCase(query)
                .or(board.content.containsIgnoreCase(query))
        }
    }

    private fun isSameCategory(boardCategory: BOARD_CATEGORY?): BooleanExpression? {
        println(boardCategory.toString())
        return when(boardCategory) {
            BOARD_CATEGORY.FREE -> board.boardCategory.categoryName.eq(boardCategory)
            BOARD_CATEGORY.RECRUIT -> board.boardCategory.categoryName.eq(boardCategory)
            BOARD_CATEGORY.SHARE -> board.boardCategory.categoryName.eq(boardCategory)
            else -> null
        }
    }

    private fun getOrderSpecifier(orderType: BOARD_ORDER_TYPE?): OrderSpecifier<*> {
        return when(orderType) {
            BOARD_ORDER_TYPE.POPULAR -> OrderSpecifier(Order.DESC, board.boardLikes.size().longValue())
            BOARD_ORDER_TYPE.RECENT -> OrderSpecifier(Order.DESC, board.id)
            null -> OrderSpecifier(Order.DESC, board.id)
        }
    }
}