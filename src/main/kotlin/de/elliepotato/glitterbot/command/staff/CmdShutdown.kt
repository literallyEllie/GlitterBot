package de.elliepotato.glitterbot.command.staff

import de.elliepotato.glitterbot.GlitterBot
import de.elliepotato.glitterbot.command.GlitterCmd
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent

/**
 * Created by Ellie on 25.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdShutdown: GlitterCmd("shutdown", Permission.MANAGE_EMOTES, "Put the bot to sleep in a questionable manner.") {

    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) =
            GlitterBot.instance.shutdown(cause = "command (${e.member.user.name})")

}