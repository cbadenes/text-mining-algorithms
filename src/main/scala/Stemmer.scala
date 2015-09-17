import java.io.StringReader

import com.neovisionaries.i18n.LanguageCode
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.snowball.SnowballFilter
import org.apache.lucene.analysis.standard.ClassicTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.util.Version
import org.tartarus.snowball.SnowballProgram


/**
 * Created by cbadenes on 17/09/15.
 */
object Stemmer {

  val LUCENE_VERSION = Version.LUCENE_36;


  val code = LanguageCode.getByLocale(LanguageCode.en.toLocale());

  // snowball stemmizer
  val snowballProgram : SnowballProgram=  Class.forName("org.tartarus.snowball.ext."+code.getName()+"Stemmer").newInstance.asInstanceOf[SnowballProgram]


  def tokenize(word: String): String ={

    val tokenizer : TokenStream = new ClassicTokenizer(LUCENE_VERSION, new StringReader(word))

    val tokenStream = new SnowballFilter(tokenizer, snowballProgram)


    var stems = List.empty[String]

    val token : CharTermAttribute = tokenStream.getAttribute(classOf[CharTermAttribute]);
    // for each token
    while (tokenStream.incrementToken()) {
      // add it in the dedicated set (to keep unicity)
      stems ::= token.toString()
    }

    // if no analyze or 2+ stems have been found, return null
    if (stems.size != 1) {
      return "";
    }

    val stem : String = stems(0);

    // if the analyze has non-alphanumerical chars, return null
    if (!stem.matches("[\\w-]+")) {
      return "";
    }

    return stem
  }




}
