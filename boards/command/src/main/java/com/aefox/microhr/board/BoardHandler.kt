package com.aefox.microhr.board

import com.aefox.microhr.board.commands.CreateBoardCommand
import com.aefox.microhr.board.commands.DeleteBoardCommand
import com.aefox.microhr.board.commands.UpdateBoardCommand
import com.aefox.microhr.board.domain.Board
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class BoardHandler(private val boardService: BoardService) {

    // TODO @aefox: move this to query
    fun getBoards(req: ServerRequest): Mono<ServerResponse> {
        val boards = boardService.getAllBoards()
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(boards, Board::class.java)
    }

    // TODO @aefox: move this to query
    fun getBoard(req: ServerRequest): Mono<ServerResponse> =
        boardService
            .getBoard(req.pathVariable("id").toLong())
            .flatMap { v -> ServerResponse.ok().body(fromObject(v)) }

    // TODO @aefox: the following methods have duplicate code

    fun addBoard(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono(CreateBoardCommand::class.java)
            .flatMap { cmd ->
                boardService.process(cmd)
            }
            .flatMap { event ->
                ServerResponse.status(CREATED).body(fromObject(event))
            }

    fun updateBoard(req: ServerRequest): Mono<ServerResponse> =
        req.bodyToMono(UpdateBoardCommand::class.java)
            .flatMap { cmd ->
                val cmdWithId = cmd.copy(boardId = req.pathVariable("id").toLong())
                boardService.process(cmdWithId)
            }
            .flatMap { event ->
                ServerResponse.status(ACCEPTED).body(fromObject(event))
            }

    fun deleteBoard(req: ServerRequest): Mono<ServerResponse> =
        boardService
            .process(DeleteBoardCommand(req.pathVariable("id").toLong()))
            .flatMap { x ->
                ServerResponse.status(ACCEPTED).body(fromObject(x))
            }
}
