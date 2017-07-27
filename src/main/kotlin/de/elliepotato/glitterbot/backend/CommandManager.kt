package de.elliepotato.glitterbot.backend

import de.elliepotato.glitterbot.command.GlitterCmd
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.reflections.Reflections

/**
 * Created by Ellie on 25.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CommandManager(val prefix:String) : ListenerAdapter() {

    val commands: HashMap<String, GlitterCmd> = HashMap()

    init {
        val re: Reflections = Reflections("de.elliepotato.glitterbot.command")
        re.getSubTypesOf(GlitterCmd::class.java).forEach { subClass ->
            val cmd = subClass.newInstance()
            commands.put(cmd.label.toLowerCase(), cmd)
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
        val member: Member = event!!.member
        val message: String = event.message.rawContent

        if (member.user.idLong == 339499212322373640L || !message.startsWith(prefix)) return

        val msg: String = message.substring(prefix.length)
        val cmd: String = msg.split(" ".toRegex())[0].toLowerCase()
        commands[cmd]?.execute(event, msg.split(" ".toRegex()).toTypedArray())
    }


    fun toggleCommand(name: String) {
        commands[name.toLowerCase()]?.enabled = false
    }

}