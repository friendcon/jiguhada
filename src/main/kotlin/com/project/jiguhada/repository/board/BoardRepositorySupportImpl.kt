package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.BoardListItemResponse
import com.project.jiguhada.controller.dto.board.QBoardListItemResponse
import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.QBoard.board
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.project.jiguhada.util.BOARD_SEARCH_TYPE
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
        page: Pageable,
        searchType: BOARD_SEARCH_TYPE?
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
                isTitleOrContentContainQuery(query, searchType),
                isSameCategory(boardCategory)
            )
            .orderBy(
                getOrderSpecifier(orderType)
            )
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    override fun findBoardListByUserId(userId: Long, page: Pageable): List<BoardListItemResponse> {
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
                board.userEntity.id.eq(userId)
            )
            .orderBy(OrderSpecifier(Order.DESC, board.createdDate))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    /*override fun getBoard(boardId: Long): BoardResponseDto? {
        return queryFactory.select(QBoardResponseDto(
            board.id,
            board.title,
            board.content,
            board.view_count,
            board.boardCategory.categoryName.stringValue(),
            board.userEntity.username,
            board.userEntity.nickname,
            board.boardCommentsList,
            board.boardLikes,
            boardImg
        ))
            .from(board)
            .leftJoin(board.boardImgs, boardImg)
            .on(board.id.eq(boardImg.id))
            .groupBy(board.id)
            .fetchOne()
    }*/

    private fun isTitleOrContentContainQuery(query: String?, searchType: BOARD_SEARCH_TYPE?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(query)) {
            return null
        }

        return when(searchType) {
            BOARD_SEARCH_TYPE.TITLE -> board.title.containsIgnoreCase(query)
            BOARD_SEARCH_TYPE.CONTENT -> board.content.containsIgnoreCase(query)
            BOARD_SEARCH_TYPE.WRITER -> board.userEntity.nickname.containsIgnoreCase(query)
            null -> null
        }
    }

    private fun isSameCategory(boardCategory: BOARD_CATEGORY?): BooleanExpression? {
        println(boardCategory.toString())
        return when(boardCategory) {
            BOARD_CATEGORY.FREE -> board.boardCategory.categoryName.eq(boardCategory)
            BOARD_CATEGORY.ENVIRONMENT -> board.boardCategory.categoryName.eq(boardCategory)
            BOARD_CATEGORY.QUESTION -> board.boardCategory.categoryName.eq(boardCategory)
            BOARD_CATEGORY.VEGAN -> board.boardCategory.categoryName.eq(boardCategory)
            else -> null
        }
    }

    private fun getOrderSpecifier(orderType: BOARD_ORDER_TYPE?): OrderSpecifier<*> {
        return when(orderType) {
            BOARD_ORDER_TYPE.POPULAR -> OrderSpecifier(Order.DESC, board.boardLikes.size().longValue())
            BOARD_ORDER_TYPE.RECENT -> OrderSpecifier(Order.DESC, board.id)
            BOARD_ORDER_TYPE.VIEW -> OrderSpecifier(Order.DESC, board.view_count)
            BOARD_ORDER_TYPE.COMMENT_COUNT -> OrderSpecifier(Order.DESC, board.id)
            null -> OrderSpecifier(Order.DESC, board.id)
        }
    }
}