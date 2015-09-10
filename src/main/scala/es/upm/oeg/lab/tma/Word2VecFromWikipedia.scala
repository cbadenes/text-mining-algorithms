package es.upm.oeg.lab.tma

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by cbadenes on 10/09/15.
 */
object Word2VecFromWikipedia {

  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "48g").
      set("spark.driver.maxResultSize","0")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    val start = System.currentTimeMillis


    val file = new File("text/model")
    if (file.exists) FileUtils.cleanDirectory(file)

    val input = sc.textFile("/home/cbadenes/test/wikipedia-dump/articles_body.csv").
      map(line => line.split("\",\"")).
      filter(x=>x.size>1).filter(x=> !x(1).startsWith("REDIRECT")).
      map(x=>x(1).split(" ").toSeq)

    val word2vec = new Word2Vec()

    val model = word2vec.fit(input)


    // Create 'topics_words.csv' file
    model.save(sc,"text/model/w2v-wiki")

    val end = System.currentTimeMillis

    println("elapsed time: " + (end-start) + " msecs")

  }

}
