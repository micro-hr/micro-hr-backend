package com.aefox.microhr.board.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.MoreObjects

data class BoardUpdatedEvent @JsonCreator constructor(
    @JsonProperty("boardId") var boardId: Long,
    @JsonProperty("boardName")var boardName: String
) : Event {
    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("boardId", boardId)
            .add("boardName", boardName)
            .toString()
    }
}
