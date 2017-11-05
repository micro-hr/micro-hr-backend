package com.aefox.microhr.board.domain

import javax.persistence.*

@Entity
data class Board(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
    val boardName: String = ""
)
