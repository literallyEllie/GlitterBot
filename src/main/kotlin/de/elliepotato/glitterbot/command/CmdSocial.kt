package de.elliepotato.glitterbot.command

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent

/**
 * Created by Ellie on 26.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdSocial: GlitterCmd("social", Permission.MESSAGE_WRITE, "All the socials!") {

    val b:EmbedBuilder = setupEmbedBuilder(title = "Social links for Glitz",
            description = "Here are the links I have!")

    init {
        b.addField("YouTube", "Glitz", false)
        b.addField("Instagram", "@SheSoGlitz", false)
        b.addField("Snpapchat", "SheSoGlitz", false)
    }

    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) = e.channel.sendMessage(b.build()).queue()

}