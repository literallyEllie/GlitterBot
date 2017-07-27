package de.elliepotato.glitterbot.command

import de.elliepotato.glitterbot.GlitterBot
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.utils.PermissionUtil
import org.apache.commons.lang3.StringUtils
import java.awt.Color
import java.util.logging.Level

/**
 * Created by Ellie on 25.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
abstract class GlitterCmd(val label:String, val permission:Permission,
                          val description:String, val syntax:String = "",
                          val minArgs:Int = 0, var enabled:Boolean = true)  {

    val aliases:ArrayList<String> = ArrayList()

    abstract fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>)

    fun execute(e: GuildMessageReceivedEvent, args: Array<String>){
        if(!enabled) return

        if(!PermissionUtil.checkPermission(e.channel, e.member, permission)){
            return message(e.channel, "No permission!")
        }

        if(args.size < minArgs){
            return message(e.channel, correctUsage())
        }

        try{
            abstractExec(e, args)
            GlitterBot.instance.log("[$label] ${e.member.user.name}#${e.member.user.discriminator} executed with params ${args.contentToString()}")
        }catch(ex:Throwable){
            message(e.channel, "Oh no a bad happened whilst doing that thing you said. (${ex.message}) <@123188806349357062>!!")
            GlitterBot.instance.log("Failed to execute command $label! Args: ${args.contentToString()}", Level.SEVERE)
            ex.printStackTrace()
        }

    }

    fun message(c:TextChannel, msg:String) {
        c.sendMessage(":sparkles:${StringUtils.capitalize(label)}:sparkles: $msg").queue()
    }

    fun correctUsage(): String {
        return "Correcto usageo: $label $syntax **-** $description"
    }

    fun setupEmbedBuilder(title: String, description: String, footer: String = "Bot by Ellie#0006"): EmbedBuilder {
        return EmbedBuilder()
                .setTitle(title)
                .setFooter(footer, null)
                .setColor(Color(0xA83D90))
                .setDescription(description)
    }

}