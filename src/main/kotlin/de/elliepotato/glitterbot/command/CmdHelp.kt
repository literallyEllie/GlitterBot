package de.elliepotato.glitterbot.command

import de.elliepotato.glitterbot.GlitterBot
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.utils.PermissionUtil
import java.util.function.Consumer

/**
 * Created by Ellie on 26.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdHelp: GlitterCmd("help", Permission.MESSAGE_WRITE, "Help for the bot.") {

    val b: EmbedBuilder = setupEmbedBuilder(title = "Help Menu",
            description = "Help central for the bot!")
    val sE: EmbedBuilder = setupEmbedBuilder(title = "Staff Help Menu",
            description = "Help central for the bot!")
    val p: String = GlitterBot.instance.conf.botPrefix

    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) {
        if(args.size == 2 && PermissionUtil.checkPermission(e.member, Permission.MANAGE_EMOTES)){
            sE.clearFields()
            GlitterBot.instance.cmdMan.commands.values.forEach(Consumer { cmd ->
                if (cmd.enabled && cmd.permission != Permission.MESSAGE_WRITE) sE.addField(":point_right: $p${cmd.label} ${cmd.syntax}", cmd.description, false)
            })
            e.channel.sendMessage(sE.build()).queue()

        }else {
            b.clearFields()
            GlitterBot.instance.cmdMan.commands.values.forEach(Consumer { cmd ->
                if (cmd.enabled && cmd.permission == Permission.MESSAGE_WRITE) b.addField(":point_right: $p${cmd.label} ${cmd.syntax}", cmd.description, false)
            })
            e.channel.sendMessage(b.build()).queue()
        }

    }

}