import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, functions}
import org.apache.spark.sql.functions.{avg, col, count, desc, max, min, stddev_pop}
import java.sql.{Connection, DriverManager, ResultSet}


object App extends Context {

override val appName: String = "spark_practic23"

  def main(args: Array[String]) = {

    import spark.implicits._
    classOf[org.postgresql.Driver]
    Class.forName("org.postgresql.Driver")
    val yellowTaxiDs = spark.read
      .option("inferSchema", "true")
      .option("header", "true")
      .parquet("src/main/resources/data/yellow_taxi_jan_25_2018")
      .as[TaxiInfo]


    val aggregateDataTaxiDs = yellowTaxiDs
      .groupBy(col("PULocationID")as "location_id")
      .agg(count(col("PULocationID")) as "count",
        avg(col("trip_distance")) as "avg_distance",
        max(col("trip_distance")) as "max_distance",
        min(col("trip_distance")) as "min_distance",
        stddev_pop(col("trip_distance")) as "stddev_pop_distance")

    aggregateDataTaxiDs.show()

    writeDsToDb(aggregateDataTaxiDs, getProperties())

    spark.stop()
  }

  def writeDsToDb(ds: DataFrame, dbProperty: java.util.Properties): Unit = {
    ds.write.mode("append").jdbc(dbProperty.getProperty("url"), dbProperty.getProperty("table"), dbProperty)
  }

  def getProperties(): java.util.Properties = {
    val prop = new java.util.Properties
    prop.setProperty("driver", "org.postgresql.Driver")
    prop.setProperty("user", "docker")
    prop.setProperty("password", "docker")
    prop.setProperty("url", "jdbc:postgresql://localhost:5432/docker")
    prop.setProperty("table", "taxi_data_table")

    prop
  }
}







