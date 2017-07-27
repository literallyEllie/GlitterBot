package de.elliepotato.glitterbot.util

/**
 * Created by Ellie on 27.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class DiscordVote(val start:Long, val startedBy:String) {

    val participants: HashMap<Long, List<Int>> = HashMap() // user + what they voted for
    var closed: Boolean = false

    var options: HashMap<String, Int> = HashMap()
    var allowMultipleOption:Boolean = false

    fun putInVote(id: Long, vote:Int): String {
        if (!closed) {

            //TODO

            if(!allowMultipleOption && participants.containsKey(id)){
                return "You have already voted for ${participants[id]!![0]}!"
            }

            if(participants.containsKey(id)){
                participants.put(id, listOf(vote))
                return "You have voted for $vote."
            }
            //participants.add(id)
        }
        return ""
    }

    fun finish() {
        participants.clear()
        closed = true

    }

}