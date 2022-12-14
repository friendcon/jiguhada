package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.board.*
import com.project.jiguhada.controller.dto.board.refactor.BoardResponse
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardCommentLike
import com.project.jiguhada.domain.board.BoardImg
import com.project.jiguhada.exception.LimitFileCountException
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.*
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.project.jiguhada.util.BOARD_SEARCH_TYPE
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class BoardService(
    private val awsS3Service: AwsS3Service,
    private val boardRepository: BoardRepository,
    private val userEntityRepository: UserEntityRepository,
    private val boardCategoryRepository: BoardCategoryRepository,
    private val boardImgRepository: BoardImgRepository,
    private val boardCommentLikeRepository: BoardCommentLikeRepository,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @Transactional
    fun createBoard(boardRequest: BoardCreateRequestDto, token: String): BoardResponse {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)

        if(boardRequest.imgList.size > 3) {
            throw LimitFileCountException("업로드 할 수 있는 파일 개수를 초과하였습니다.")
        }
        val board = boardRequest.toEntity(usernameFromToken)
        boardRepository.save(board)
        val commentToEntity = boardRequest.imgList.map {
            BoardImg(
                board = board,
                imgUrl = it,
                isDeleted = false
            )
        }

        if(boardRequest.imgList.size == null) {
            return board.toBoardResponse()
        }

        boardImgRepository.saveAll(commentToEntity)
        board.boardImgs = commentToEntity.toMutableList()

        return board.toBoardResponse()
    }

    fun uploadBoardImg(multipartFile: MultipartFile): ImgUrlResponseDto {
        return ImgUrlResponseDto(
            imgUrl = awsS3Service.uploadImgToDir(multipartFile, "board-img")
        )
    }

    @Transactional
    fun readsBoard(
        boardId: Long
    ): BoardResponse {
        val response = boardRepository.findById(boardId).get()
        response.updateViewCount()
        return response.toBoardResponse()
    }

    fun readBoardList(
        query: String?, orderType: BOARD_ORDER_TYPE?,
        category: BOARD_CATEGORY?, page: Pageable,
        searchType: BOARD_SEARCH_TYPE?
    ): BoardListResponse {
        val list = boardRepository.findBoardList(query, orderType, category, page, searchType)

        val totalBoardCount = when (category) {
            null -> boardRepository.count()
            else -> boardRepository.countBoardByBoardCategory(boardCategoryRepository.findByCategoryName(category))
        }
        var totalBoardCounts = 0L
        var totalBoardPage = 0L

        if(category != null && searchType != null) {
            totalBoardCounts = boardRepository.countBoardByCategoryAndBoardSearch(query, searchType, category)
            totalBoardPage = getBoardTotalPage(totalBoardCounts)
        } else if(category != null && searchType == null) {
            totalBoardCounts = boardRepository.countBoardByCategory(category)
            totalBoardPage = getBoardTotalPage(totalBoardCounts)
        } else if(category == null && searchType != null) {
            totalBoardCounts = boardRepository.countBoardBySearch(searchType, query)
            totalBoardPage = getBoardTotalPage(totalBoardCounts)
        } else {
            totalBoardCounts = boardRepository.count()
            totalBoardPage = getBoardTotalPage(totalBoardCounts)
        }

        return BoardListResponse(
            totalBoardCount = totalBoardCounts,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalBoardPage,
            boardItemList = list
        )
    }

    fun getBoardTotalPage(totalBoardCounts: Long): Long {
        return when(totalBoardCounts%15) {
            0L -> totalBoardCounts/15
            else -> totalBoardCounts/15 + 1
        }
    }

    // 업데이트할 게시글 가져옴
    @Transactional
    fun getUpdateBoard(boardId: Long, token: String): BoardUpdateResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val board = boardRepository.findById(boardId).get()

        if(board.userEntity.username.equals(usernameFromToken)) {
            boardRepository.findById(boardId)
            return board.toBoardUpdateResponse()
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    @Transactional
    fun updateBoard(boardUpdateRequestDto: BoardUpdateRequestDto, token: String): BoardResponse {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val board = boardRepository.findById(boardUpdateRequestDto.boardId).get()
        if(board.userEntity.username.equals(usernameFromToken)) {
            deleteBoardImg(boardUpdateRequestDto.deleteImg) // 이미지 삭제
            updateBoardImg(board, boardUpdateRequestDto.boardImg) // 이미지 업데이트
            val category = boardCategoryRepository.findById(boardUpdateRequestDto.boardCategory).get()
            board.updateCategory(category) // 카테고리 수정
            board.updateBoard(boardUpdateRequestDto) // 다른 게시글 정보 수정
            return board.toBoardResponse()
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    fun updateBoardImg(board: Board, updateList: List<BoardImgResponseDto>) {
        val imgListId = board.boardImgs.filter { !it.isDeleted }
        val newImgCount = updateList.filter { !boardImgRepository.existsById(it.imgId)}.size

        if(imgListId.size + newImgCount <= 3) {
            val imgEntity = updateList.map { it.toEntity(board, it.imgUrl) }
            boardImgRepository.saveAll(imgEntity)
        } else {
            throw LimitFileCountException("업로드 할 수 있는 파일 개수를 초과하였습니다.")
        }
    }

    fun deleteBoardImg(deleteList: List<BoardImgResponseDto>) {
        deleteList.forEach {
            val boardImg = boardImgRepository.findById(it.imgId).get()
            boardImg.deleteImg()
        }
    }

    fun removeBoard(boardId: Long, token: String): CommonResponseDto {
        // token 에서 id 가져오기
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val board = boardRepository.findById(boardId).get()
        if(board.userEntity.username.equals(usernameFromToken)) {
            boardRepository.delete(board)
            return CommonResponseDto(200, "게시글 삭제 성공")
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    fun BoardCreateRequestDto.toEntity(username: String): Board {
        val user = userEntityRepository.findByUsername(username).get()
        val category = boardCategoryRepository.findByCategoryName(category)
        val board = Board(
            title = title,
            content = content,
            boardCategory = category,
            userEntity = user,
            view_count = 0
        )

        return boardRepository.save(board)
    }

    fun BoardImgResponseDto.toEntity(board: Board, imgUrl: String): BoardImg {
        return BoardImg(
            board = board,
            imgUrl = imgUrl,
            isDeleted = false
        )
    }
}