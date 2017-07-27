package de.elliepotato.glitterbot.command.staff

import de.elliepotato.glitterbot.command.GlitterCmd
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import java.time.format.DateTimeFormatter

/**
 * Created by Ellie on 27.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdRoleInfo: GlitterCmd("roleinfo", Permission.MANAGE_EMOTES, "Get information about a server role.",
        "roleinfo <role name>", 1) {

    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) {
        val c:TextChannel = e.channel

        val rank = args[1]

        val format = setupEmbedBuilder("Information about rank $rank:", "")

        val ranks:List<Role> =  e.guild.getRolesByName(rank, true)
        if(ranks.size > 1) {
            val p: StringBuilder = StringBuilder()
            ranks.forEach { t: Role? -> p.append(t!!.name+"\n")}.toString()
            format.addField("Ambiguous name $rank.", "Please specify which one you mean: $p", false)
        }

        else if(ranks.isEmpty()) {
            format.addField("No result.", "There was no role with the name $rank.", false)
        }
        else {
            val r: Role = ranks[0]
            format.addField("Name:", r.name, true)
            format.addField("Created:", r.creationTime.format(DateTimeFormatter.RFC_1123_DATE_TIME), true)
            format.addField("Color:", "R: ${r.color.red} G: ${r.color.green} B: ${r.color.blue}", true)
            format.addField("ID:", r.id, true)
        }


        c.sendMessage(format.build()).queue()
    }



}