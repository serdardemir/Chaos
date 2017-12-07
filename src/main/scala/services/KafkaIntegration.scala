package services

import java.util.Properties
import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

trait KafkaIntegration {

  def Subscribe(topic: String)

  def Publish(topic: String, message: String)
}

class KafkaIntegrationImpl(bootstrapServers: String) extends KafkaIntegration {

  override def Publish(topic: String, message: String) = {

    val producer = new KafkaProducer[String, String](GetProperties)

    def send(message: String): Unit = {

      val record = new ProducerRecord(topic, "key", message)
      producer.send(record)

    }
  }

  override def Subscribe(topic: String) = {

    val consumer = new KafkaConsumer[String, String](GetProperties)
    consumer.subscribe(java.util.Collections.singletonList(topic))

    while (true) {
      
      val records = consumer.poll(100)
      for (record <- records.asScala) {
        println(record)
      }
    }
  }


  def GetProperties(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrapServers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props;

  }
}