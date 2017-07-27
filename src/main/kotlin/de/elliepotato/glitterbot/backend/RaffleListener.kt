package de.elliepotato.glitterbot.backend

import de.elliepotato.glitterbot.util.DiscordRaffle
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Created by Ellie on 26.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class RaffleListener: ListenerAdapter() {

    var current:DiscordRaffle? = null

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
        if(current == null) return
        val member: Member = event!!.member
        val message: String = event.message.rawContent.toLowerCase()

        if (member.user.idLong == 339499212322373640L || !message.contains(current!!.keyWord, true)) return
        current!!.add(member.user.idLong)
    }

    fun isEnabled(): Boolean {
        return current != null
    }

    fun start(keyWord:String, starter: String) {
        if(!isEnabled()){
            current = DiscordRaffle(keyWord, System.currentTimeMillis(), starter)
        }
    }

    fun stop(){
        if(isEnabled()){
            current!!.finish()
            current = null
        }
    }

}