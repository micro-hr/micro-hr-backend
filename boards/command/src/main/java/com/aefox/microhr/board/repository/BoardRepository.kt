package com.aefox.microhr.board.repository

import com.aefox.microhr.board.domain.Board
import org.springframework.data.repository.CrudRepository

interface BoardRepository: CrudRepository<Board, Long>
