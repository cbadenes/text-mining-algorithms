package w2v

/**
 * Created by cbadenes on 17/09/15.
 */
object TestWikiW2V {

  def main(args: Array[String]) : Unit={
    ModelBuilder.main(List("2","1g","/Users/cbadenes/Projects/siminwikart-challenge4/text/wikipedia/articles_body10000.csv","model/w2v").toArray)
  }

}
