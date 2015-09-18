import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 10/09/15.
 */
object Word2VecModel {

  def main(args: Array[String]): Unit = {

    val threads = args(0)
    val memory  = args(1)
    val path    = args(2)
    val output  = args(3)

    // Spark Configuration
    val conf = new SparkConf().
      setMaster(s"local[$threads]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", s"$memory").
      set("spark.driver.maxResultSize","0")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    val start = System.currentTimeMillis


    val file = new File(s"$output")
    if (file.exists) FileUtils.cleanDirectory(file)

    val input = sc.textFile(s"$path").
      map(line => line.split("\",\"")).
      filter(x=>x.size>1).filter(x=> !x(1).startsWith("REDIRECT")).
      map(x=>x(1).split(" ").map(Stemmer.tokenize(_)).filter(_.trim.nonEmpty).toSeq)

    val word2vec = new Word2Vec()
    word2vec.
      setVectorSize(100).
      setSeed(42L).
      setNumIterations(5).
      setNumPartitions(36).
      setMinCount(5)


    Logger.getLogger("Word2VecModel").warn("Building the model..")

    val model = word2vec.fit(input)


    // Create 'topics_words.csv' file
    model.save(sc,s"$output")

    val end = System.currentTimeMillis

    println("elapsed time: " + (end-start) + " msecs")

  }

}
