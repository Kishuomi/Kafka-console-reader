package dev.kishuomi.kafka_console_reader

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import java.time.Duration
import java.util.*

const val TOPIC = "topic"
const val SERVER = "server"
const val GROUP_ID = "groupId"

fun main(args: Array<String>) {
    println("Ran with args=${args.asList()}")
    val params = mutableMapOf<String, String>()
    for (arg in args) {
        val split = arg.split("=")
        if (split.first().startsWith("-D", ignoreCase = true)) {
            params[split.first().substring(2).lowercase()] = split.last()
        }
    }
    val consumer = createStringByteArrayConsumer(
        groupId = params.getOrDefault(GROUP_ID.lowercase(), "logs-reader"),
        server = params.getOrDefault(SERVER, "localhost:9092")
    ).also {
        it.subscribe(listOf(params.getOrDefault(TOPIC, "logs")))
        Runtime.getRuntime().addShutdownHook(Thread(it::close))
    }


    while (true) {
        consumer.poll(Duration.ofMillis(500L)).map {
            val value = String(it.value())
            println(value)
        }
        consumer.commitSync()
    }
}

/**
 * Consumer for reading logs in json format.
 * Key byte array
 * Value string as byte array
 */
fun createStringByteArrayConsumer(groupId: String, server: String): Consumer<String, ByteArray> = Properties()
    .also {
        it[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = server
        it[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = ByteArrayDeserializer::class.java.name
        it[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ByteArrayDeserializer::class.java.name
        it[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        it[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 1 //read only one record per poll
        it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        it[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "false"
    }
    .let {
        KafkaConsumer(it)
    }