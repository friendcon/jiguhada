package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.refactor.BoardLikeItem
import com.project.jiguhada.controller.dto.board.refactor.QBoardLikeItem
import com.project.jiguhada.domain.board.BoardLike
import com.project.jiguhada.domain.board.QBoardLike.boardLike
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class BoardLikeSupportImpl(
    private val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(BoardLike::class.java), BoardLikeSupport {
    override fun findBoardLikeByBoardId(boardId: Long, pageable: Pageable): List<BoardLikeItem> {
        return queryFactory.select(QBoardLikeItem(
            boardLike.id,
            boardLike.userEntity.id,
            boardLike.userEntity.username,
            boardLike.userEntity.nickname
        ))
            .from(boardLike)
            .where(
                boardLike.board.id.eq(boardId)
            )
            .orderBy(OrderSpecifier(Order.DESC, boardLike.lastModifiedDate))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}