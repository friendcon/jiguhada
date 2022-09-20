package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.board.BoardResponseDto
import com.project.jiguhada.controller.dto.board.BoardUpdateResponseDto
import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import org.hibernate.annotations.ColumnDefault
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class Board(

    val title: String,
    val content: String,
    @ColumnDefault("0")
    val view_count: Long,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "board_category_id")
    val boardCategory: BoardCategory,

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    val boardCommentsList: MutableSet<BoardComment> = mutableSetOf(),

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    val boardLikes: MutableSet<BoardLike> = mutableSetOf(),


    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = [CascadeType.PERSIST])
    var boardImgs: MutableSet<BoardImg> = mutableSetOf()
): BaseEntity() {

    fun toBoardResponse(): BoardResponseDto {
        return BoardResponseDto(
            boardId = id!!,
            title = title,
            content = content,
            viewCount = view_count,
            boardCategory = boardCategory.categoryName.toString(),
            username = userEntity.username,
            nickname = userEntity.nickname,
            commentList = boardCommentsList.map{it.toResponse()},
            likeList = boardLikes.map { it.toResponse() },
            imgList = boardImgs.map { it.toResponse() }
        )
    }

    fun toBoardUpdateResponse(): BoardUpdateResponseDto {
        return BoardUpdateResponseDto(
            title = title,
            content = content,
            boardCategory = boardCategory.categoryName.toString(),
            nickname = userEntity.nickname,
            imgList = boardImgs.map { it.toResponse() }
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
