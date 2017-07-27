package de.elliepotato.glitterbot.util

import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

/**
 * Created by Ellie on 26.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class DiscordRaffle(val keyWord:String, val start:Long, val startedBy:String) {

    val participants: ArrayList<Long> = ArrayList()
    var closed: Boolean = false

    fun add(id: Long) {
        if (!closed && !participants.contains(id)) {
            participants.add(id)
        }
    }

    fun draw(toDraw: Int): LongArray {
        closed = true
        Collections.shuffle(participants)
        return participants.stream().limit(toDraw.toLong()).toList().toLongArray()
        /*
        val stringBuilder = StringBuilder()
        participants.stream().limit(toDraw.toLong()).forEach { aLong -> stringBuilder.append(" <@").append(aLong).append(">,") }
        return stringBuilder.toString().substring(0, stringBuilder.length - 1)
    */
    }

    fun finish() {
        participants.clear()
        closed = true

    }

}