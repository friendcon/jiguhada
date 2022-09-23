package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.BoardListResponse
import com.project.jiguhada.controller.dto.boardcomment.BoardCommentList
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.SecurityUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class MypageService(
    private val boardRepository: BoardRepository,
    private val boardCommentRepository: BoardCommentRepository,
    private val userEntityRepository: UserEntityRepository
) {
    fun getUserBoards(page: Pageable, token: String): BoardListResponse {
        val userId = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get().id
        val list = boardRepository.findBoardListByUserId(userId!!, page)
        val totalBoardCount = boardRepository.countBoardByUserEntityId(userId!!)
        val totalPage = when(totalBoardCount%15) {
            0L -> totalBoardCount/15
            else -> totalBoardCount/15 + 1
        }

        return BoardListResponse(
            totalBoardCount = totalBoardCount,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalPage,
            boardItemList = list
        )
    }

    fun getUserComments(page: Pageable, token: String): BoardCommentList {
        val userId = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get().id
        val list = boardCommentRepository.findCommentByUserId(userId!!, page)
        val totalCommentCount = boardCommentRepository.countByUserEntityId(userId)
        val totalPage = when(totalCommentCount%15) {
            0L -> totalCommentCount/15
            else -> totalCommentCount/15 + 1
        }

        return BoardCommentList(
            totalCommentCount = totalCommentCount,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalPage,
            commentList = list
        )
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }
}
