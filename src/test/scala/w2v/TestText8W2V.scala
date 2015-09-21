package w2v

import breeze.linalg.DenseVector
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.mllib.linalg.Vectors

/**
 * Created by cbadenes on 18/09/15.
 */
object TestText8W2V {

  def synonymsBySum(model: Word2VecModel, words: List[String]): Unit ={
    println(s"######   Topic for keywords by sum: $words")
    //val sumVector = words.map(word => DenseVector(model.transform(word).toArray)).reduce((v1,v2) => v1 + v2)
    val sumVector = words.map(word => DenseVector(model.transform(word).toArray).toArray).reduce((v1,v2) => v1.zip(v2).map(x=>x._1+x._2))
    model.findSynonyms(Vectors.dense(sumVector.toArray), 10).foreach{case (synonym, cosineSimilarity) => println(s"$synonym $cosineSimilarity")}
  }

  def synonymsByMultiplication(model: Word2VecModel, words: List[String]): Unit ={
    val mulVector = words.map(word => DenseVector(model.transform(word).toArray).toArray).reduce((v1,v2) => v1.zip(v2).map((x)=>x._1*x._2))
    println(s"######   Topic for keywords by mult: $words")
    model.findSynonyms(Vectors.dense(mulVector.toArray), 10).foreach{case (synonym, cosineSimilarity) => println(s"$synonym $cosineSimilarity")}
  }


  def main(args: Array[String]): Unit={

    // Spark Configuration
    val conf = new SparkConf().
      setMaster(s"local[*]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "1g").
      set("spark.driver.maxResultSize","0")
    val sc = new SparkContext(conf)


    val model = Word2VecModel.load(sc, "/Users/cbadenes/Projects/epnoi-matching-metrics/word2vec/models/word2vect_text8_500")


    val examples = List(
      List("science", "star", "planet", "galaxy", "survey", "study", "distance", "knowledge", "scientific", "education", "astronomy"),
      List("science", "star"),
      List("science", "galaxy"),
      List("science", "education"),
      List("science", "star", "planet", "galaxy", "survey", "study", "distance"),
      List("white","black")
    )


    examples.foreach{words=>
      synonymsBySum(model,words)
      synonymsByMultiplication(model,words)
    }


  }

}
