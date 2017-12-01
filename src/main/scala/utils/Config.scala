package utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("database")
  private val redisConfig = config.getConfig("redis")
  private val elasticSearchConfig = config.getConfig("elasticSearch")
  private val kafkaConfig = config.getConfig("kafka")


  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val jdbcUrl = databaseConfig.getString("url")
  val dbUser = databaseConfig.getString("user")
  val dbPassword = databaseConfig.getString("password")
  val dbCreateSampleData = databaseConfig.getBoolean("createSampleData")

  val redisHost = redisConfig.getString("host")
  val redisPort = redisConfig.getInt("port")

  val elasticSearchHost = elasticSearchConfig.getString("host")
  val elasticSearchPort = elasticSearchConfig.getInt("port")

  val kafkaBootstrapServers = kafkaConfig.getString("bootstrapServers")
  val kafkaTopic = kafkaConfig.getString("topic")


}
