package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.board.BoardUpdateRequestDto
import com.project.jiguhada.controller.dto.board.BoardUpdateResponseDto
import com.project.jiguhada.controller.dto.board.refactor.BoardResponse
import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import org.hibernate.annotations.ColumnDefault
import javax.persistence.*

@Entity
data class Board(

    var title: String,
    @Column(length = 2000)
    var content: String,
    @ColumnDefault("0")
    var view_count: Long,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "board_category_id")
    var boardCategory: BoardCategory,

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    val boardCommentsList: MutableList<BoardComment> = mutableListOf(),

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    val boardLikes: MutableList<BoardLike> = mutableListOf(),


    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    var boardImgs: MutableList<BoardImg> = mutableListOf()
): BaseEntity() {
    fun updateViewCount(): Board {
        view_count++
        return this
    }
    fun updateBoard(boardUpdateRequestDto: BoardUpdateRequestDto): Board {
        title = boardUpdateRequestDto.title
        content = boardUpdateRequestDto.content
        return this
    }

    fun updateCategory(boardCategory: BoardCategory): Board {
        this.boardCategory = boardCategory
        return this
    }

    fun toBoardResponse(): BoardResponse {
        return BoardResponse(
            id!!,
            title,
            content,
            view_count,
            boardCategory.categoryName.toString(),
            userEntity.username,
            userEntity.id!!,
            userEntity.userImageUrl,
            userEntity.nickname,
            boardCommentsList.size.toLong(),
            boardLikes.size.toLong(),
            createdDate,
            lastModifiedDate
        )
    }
    fun toBoardUpdateResponse(): BoardUpdateResponseDto {
        return BoardUpdateResponseDto(
            title = title,
            content = content,
            boardCategory = boardCategory.categoryName.toString(),
            nickname = userEntity.nickname,
            imgList = boardImgs.filter { !it.isDeleted }.map { it.toResponse() }
        )
    }
    override fun toString(): String {
        return "Board(title='$title', content='$content', view_count=$view_count, boardCategory=$boardCategory)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Board

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
