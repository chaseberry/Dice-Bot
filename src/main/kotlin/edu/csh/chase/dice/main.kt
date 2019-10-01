package edu.csh.chase.dice

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

fun main() {

    val jda = JDABuilder(String(Resources.load("key.txt").readBytes())).build()

    jda.awaitReady()

    val dieRegex = Regex("^roll (\\d+)?d(\\d+)\$")

    jda.addEventListener(object : ListenerAdapter() {

        override fun onMessageReceived(event: MessageReceivedEvent) {
            val raw = event.message.contentRaw

            dieRegex.matchEntire(raw)?.let {

                val rolls = it.groupValues.getOrNull(1)?.toIntOrNull() ?: 1

                val dieValue = it.groupValues.getOrNull(2)?.toIntOrNull() ?: 6

                if (rolls > 100) {
                    event.channel.sendMessage("You can't roll more than 100").queue()
                    return
                }

                when {
                    dieValue == 0 -> {
                        event.channel.sendMessage("d0? Heck off.").queue()
                        return
                    }
                    dieValue == 1 -> {
                        event.channel.sendMessage("What even is a d1?").queue()
                        return
                    }
                    dieValue > 50 -> {
                        event.channel.sendMessage("I'm not rolling higher than a d50").queue()
                        return
                    }
                    else -> Unit
                }

                val die = Die(dieValue)

                val rs = (1..rolls).map {
                    die.roll()
                }

                if (rolls == 1) {
                    event.channel.sendMessage("Rolled ${rs.first()}").queue()
                } else {
                    event.channel.sendMessage("Rolled ${rs.joinToString(",", "[", "]")}=${rs.sum()}").queue()
                }

            }
        }
    })

}
