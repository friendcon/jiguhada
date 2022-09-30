package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.boardcomment.BoardCommentItem
import com.project.jiguhada.controller.dto.boardcomment.QBoardCommentItem
import com.project.jiguhada.domain.board.QBoard.board
import com.project.jiguhada.domain.board.QBoardComment.boardComment1
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable

class BoardCommentSupportImpl(
    private val queryFactory: JPAQueryFactory
): BoardCommentSupport {

    override fun findCommentByUserId(userId: Long, page: Pageable): List<BoardCommentItem> {
        return queryFactory.select(QBoardCommentItem(
            board.id,
            board.title,
            board.boardCategory.categoryName,
            boardComment1.id,
            boardComment1.content,
            board.boardCommentsList.size().longValue(),
            boardComment1.userEntity.nickname,
            boardComment1.userEntity.id,
            boardComment1.userEntity.userImageUrl,
            boardComment1.createdDate
        ))
            .from(boardComment1)
            .leftJoin(board)
            .on(boardComment1.board.id.eq(board.id))
            .where(
                boardComment1.userEntity.id.eq(userId)
            )
            .orderBy(OrderSpecifier(Order.DESC, boardComment1.createdDate))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()

    }
}