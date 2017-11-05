package com.aefox.microhr.board.repository

import com.aefox.microhr.board.domain.Board
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

//@Component
interface BoardRepository: CrudRepository<Board, Long> {
//    fun findByBoardName(boardName: String): List<Board>
}
