package w2v

import org.apache.log4j.{Level, Logger}
import org.apache.spark._
import org.apache.spark.rdd._
import org.apache.spark.SparkContext._
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

/**
 * Created by cbadenes on 17/09/15.
 */
object TestISWCW2V {

  def main(args: Array[String]) : Unit={

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[2]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "1g").
      set("spark.driver.maxResultSize","0")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    val input = sc.textFile("/Users/cbadenes/Projects/epnoi-harvester/output.txt").map(line => line.split(" ").toSeq)

    val word2vec = new Word2Vec()

    val model = word2vec.fit(input)

    // Save and load model
    model.save(sc, "model/iswc")


//    val sameModel = Word2VecModel.load(sc, "model/iswc")
//
//    val synonyms = sameModel.findSynonyms("research", 40)
//
//    for((synonym, cosineSimilarity) <- synonyms) {
//      println(s"$synonym $cosineSimilarity")
//    }


  }

}
