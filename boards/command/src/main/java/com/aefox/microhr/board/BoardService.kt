package com.aefox.microhr.board

import com.aefox.microhr.board.commands.CreateBoardCommand
import com.aefox.microhr.board.commands.DeleteBoardCommand
import com.aefox.microhr.board.commands.UpdateBoardCommand
import com.aefox.microhr.board.domain.Board
import com.aefox.microhr.board.events.BoardCreatedEvent
import com.aefox.microhr.board.events.BoardDeletedEvent
import com.aefox.microhr.board.events.BoardUpdatedEvent
import com.aefox.microhr.board.events.Event
import com.aefox.microhr.board.repository.BoardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.CountDownLatch

@Service
class BoardService constructor(
    val kafkaTemplate: KafkaTemplate<String, Any>,
    var boardRepository: BoardRepository
) {

    fun getAllBoards(): Flux<Board> = Flux.fromIterable(boardRepository.findAll())

    fun getBoard(id: Long): Mono<out Board> = Mono.justOrEmpty(boardRepository.findById(id))

    @Transactional
    fun process(cmd: CreateBoardCommand): Mono<out BoardCreatedEvent> {
        val newBoard: Board = boardRepository.save(Board(boardName = cmd.boardName))
        val boardCreatedEvent = BoardCreatedEvent(newBoard.id, newBoard.boardName)

        kafkaTemplate.send(buildMessage(boardCreatedEvent))

        return Mono.just(boardCreatedEvent)
    }

    @Transactional
    fun process(cmd: UpdateBoardCommand): Mono<out BoardUpdatedEvent> {
        val updatedOrEmpty = boardRepository.findById(cmd.boardId).map { boardToUpdate ->
            val updatedBoard: Board = boardRepository.save(Board(cmd.boardId, cmd.boardName))
            val boardUpdatedEvent = BoardUpdatedEvent(updatedBoard.id, updatedBoard.boardName)

            kafkaTemplate.send(buildMessage(boardUpdatedEvent))

            boardUpdatedEvent
        }

        // TODO @efox: should return some other event in case of error

        return Mono.justOrEmpty(updatedOrEmpty)
    }

    @Transactional
    fun process(cmd: DeleteBoardCommand): Mono<out BoardDeletedEvent> {
        boardRepository.deleteById(cmd.boardId)
        val boardDeletedEvent = BoardDeletedEvent(cmd.boardId)

        kafkaTemplate.send(buildMessage(boardDeletedEvent))

        return Mono.just(boardDeletedEvent)
    }

    private fun buildMessage(boardCreatedEvent: Event) =
        MessageBuilder
            .withPayload(boardCreatedEvent)
            .setHeader(KafkaHeaders.TOPIC, "boardEventsTopic")
            .build()
}
