package com.aefox.microhr.board

import com.aefox.microhr.board.commands.CreateBoardCommand
import com.aefox.microhr.board.commands.DeleteBoardCommand
import com.aefox.microhr.board.commands.UpdateBoardCommand
import com.aefox.microhr.board.domain.Board
import com.aefox.microhr.board.events.BoardCreatedEvent
import com.aefox.microhr.board.events.BoardDeletedEvent
import com.aefox.microhr.board.events.BoardUpdatedEvent
import com.aefox.microhr.board.repository.BoardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BoardService {

    @Autowired
    lateinit private var boardRepository: BoardRepository

    fun getAllBoards(): Flux<Board> = Flux.fromIterable(boardRepository.findAll())

    fun getBoard(id: Long): Mono<out Board> = Mono.justOrEmpty(boardRepository.findById(id))

    @Transactional("boardTransactionManager")
    fun process(cmd: CreateBoardCommand): Mono<out BoardCreatedEvent> {
        val newBoard: Board = boardRepository.save(Board(boardName = cmd.boardName))

        // post created event to kafka

        return Mono.just(BoardCreatedEvent(newBoard.id, newBoard.boardName))
    }

    @Transactional("boardTransactionManager")
    fun process(cmd: UpdateBoardCommand): Mono<out BoardUpdatedEvent> {
        val updatedOrEmpty = boardRepository.findById(cmd.boardId).map { boardToUpdate ->
            val updatedBoard: Board = boardRepository.save(Board(cmd.boardId, cmd.boardName))

            // post updated event to kafka

            BoardUpdatedEvent(updatedBoard.id, updatedBoard.boardName)
        }

        // TODO @efox: should return some other event

        return Mono.justOrEmpty(updatedOrEmpty)
    }

    @Transactional("boardTransactionManager")
    fun process(cmd: DeleteBoardCommand): Mono<out BoardDeletedEvent> {
        boardRepository.deleteById(cmd.boardId)

        // post deleted event to kafka

        return Mono.just(BoardDeletedEvent(cmd.boardId))
    }
}
