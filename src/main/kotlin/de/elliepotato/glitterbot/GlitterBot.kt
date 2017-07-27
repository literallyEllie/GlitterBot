package de.elliepotato.glitterbot

import de.elliepotato.glitterbot.backend.CommandManager
import de.elliepotato.glitterbot.backend.GlitterConfig
import de.elliepotato.glitterbot.backend.RaffleListener
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.dv8tion.jda.core.managers.GuildController
import net.dv8tion.jda.core.managers.GuildManager
import java.util.logging.Level

/**
 * Created by Ellie on 25.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class GlitterBot {

    lateinit var jda: JDA

    lateinit var conf: GlitterConfig
    lateinit var cmdMan: CommandManager
    lateinit var raffleListener: RaffleListener

    fun init() {
        val start:Long = System.currentTimeMillis()
        conf = GlitterConfig()
        raffleListener = RaffleListener()
        cmdMan = CommandManager(conf.botPrefix)


        jda = JDABuilder(AccountType.BOT)
                .setToken(conf.token)
                .setGame(Game.of(conf.botGame))
                .setStatus(OnlineStatus.fromKey(conf.botState))
                .addEventListener(cmdMan)
                .addEventListener(raffleListener)
                .addEventListener(object : ListenerAdapter() {

                    override fun onGuildMemberJoin(e: GuildMemberJoinEvent) {
                        GuildController(e.guild).addRolesToMember(e.member, e.guild.getRoleById(334875750345867275L))
                    }

                })
                .buildBlocking()

        log("Loaded in ${System.currentTimeMillis() - start}ms.")
    }

    private object container {
        val instance = GlitterBot()
    }

    companion object {
        val instance: GlitterBot by lazy {
            container.instance
        }
    }

    fun shutdown(status:Int = 0, cause:String = "n/a") {
        log("Shutdown called with status $status due to $cause.")
        cmdMan.commands.clear()
        jda.shutdown()
        System.exit(status)
    }

    fun log(msg:String, level:Level = Level.INFO) = println("[${level.name}] $msg")

}

fun main(args: Array<String>) = GlitterBot.instance.init()
