package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.board.*
import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardImg
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardCategoryRepository
import com.project.jiguhada.repository.board.BoardImgRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.BOARD_SEARCH_TYPE
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class BoardService(
    private val awsS3Service: AwsS3Service,
    private val boardRepository: BoardRepository,
    private val userEntityRepository: UserEntityRepository,
    private val boardImgRepository: BoardImgRepository,
    private val boardCategoryRepository: BoardCategoryRepository,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @Transactional
    fun createBoard(boardRequest: BoardCreateRequestDto, token: String): BoardResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)

        val board = boardRequest.toEntity(usernameFromToken)
        val imgToEntity = boardRequest.imgList.map {
            toBoardImgEntity(board, it)
        }

        board.boardImgs = imgToEntity.toMutableList()
        return board.toBoardResponse()

        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다.")
    }

    // 게시글 작성시 사용하는 이미지 업로드 fun
    fun uploadBoardImg(multipartFiles: List<MultipartFile>): List<String> {
        return multipartFiles.map {
            awsS3Service.uploadImgToDir(it, "board-img")
        }
    }

    // 게시글 수정시 사용하는 이미지 업로드 fun
    fun uploadUpdateBoardImg(multipartFiles: List<MultipartFile>): List<String> {
        return multipartFiles.map {
            awsS3Service.uploadImgToDir(it, "board-img")
        }
    }

    fun getUpdateBoard(boardId: Long, token: String): BoardUpdateResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val board = boardRepository.findById(boardId).get()
        if(board.userEntity.username.equals(usernameFromToken)) {
            return board.toBoardUpdateResponse()
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    @Transactional
    fun updateBoard(
        requestDto: BoardUpdateRequestDto,
        token: String
    ): BoardResponseDto? {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val board = boardRepository.findById(requestDto.boardId).get()

        if(board.userEntity.username.equals(usernameFromToken)) {
            board.updateBoard(requestDto) // 게시글 정보 수정

            requestDto.deleteImg.forEach {
                val img = boardImgRepository.findById(it.image_id).get()
                img.deleteImg()
            } // 이미지 삭제

            requestDto.boardImg.forEach {
                boardImgRepository.findById(it.image_id)?:boardImgRepository.save(it.toEntity(board))
            } // 이미지 저장

            // 게시글 리턴
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
        return null
    }

    fun BoardImgRequestDto.toEntity(board: Board): BoardImg {
        return BoardImg(
            board,
            imgUrl = image_url,
            isDeleted = false
        )
    }

    fun readBoardList(
        query: String?, orderType: BOARD_ORDER_TYPE?,
        category: BOARD_CATEGORY?, page: Pageable,
        searchType: BOARD_SEARCH_TYPE?
    ): BoardListResponse {
        val list = boardRepository.findBoardList(query, orderType, category, page, searchType)
        val totalCount = boardRepository.count()
        val totalPage = when(totalCount%15) {
            0L -> totalCount/15
            else -> totalCount/15 + 1
        }
        return BoardListResponse(
            totalBoardCount = totalCount,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalPage,
            boardItemList = list
        )
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

    fun toBoardImgEntity(board: Board, imgUrl: String): BoardImg {
        return BoardImg(
            board = board,
            imgUrl = imgUrl,
            isDeleted = false
        )
    }
}