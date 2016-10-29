package com.feeder.android.views;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.feeder.android.utils.Constants;
import com.feeder.android.utils.DateUtil;
import com.feeder.common.SPManager;
import com.feeder.model.Article;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import me.zsr.feeder.R;

/**
 * @description:
 * @author: Match
 * @date: 10/23/16
 */

public class ArticleActivity extends BaseActivity {
    private HtmlTextView mContentTextView;
    private TextView mTitleTextView;
    private TextView mSubscriptionNameTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initViews();
//        Long articleId = getIntent().getExtras().getLong(Constants.KEY_BUNDLE_ITEM_ID);
        loadDataAsync(0L);
    }

    private void initViews() {
        mTitleTextView = (TextView) findViewById(R.id.article_title);
        mDateTextView = (TextView) findViewById(R.id.article_date);
        mTimeTextView = (TextView) findViewById(R.id.article_time);
        mSubscriptionNameTextView = (TextView) findViewById(R.id.subscription_name);

        mContentTextView = (HtmlTextView) findViewById(R.id.article_content);
        switch (SPManager.getInt(SettingsActivity.KEY_FONT_SIZE,
                SettingsActivity.FONT_SIZE_MEDIUM)) {
            case SettingsActivity.FONT_SIZE_SMALL:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_small));
                break;
            case SettingsActivity.FONT_SIZE_MEDIUM:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_medium));
                break;
            case SettingsActivity.FONT_SIZE_BIG:
                mContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_big));
                break;
        }
    }

    private void loadDataAsync(Long articleId) {
        Article article = new Article();
        article.setTitle("如何读懂并写出装逼的函数式代码");
        article.setPublished(com.feeder.common.DateUtil.parseRfc822("Sun, 23 Oct 2016 09:56:29 +0000").getTime());
        article.setContent("<![CDATA[<p><img src=\"http://coolshell.cn//wp-content/uploads/2016/10/drawing-recursive-300x204.jpg\" alt=\"drawing-recursive\" width=\"300\" height=\"204\" class=\"alignright size-medium wp-image-17535\" srcset=\"http://coolshell.cn//wp-content/uploads/2016/10/drawing-recursive-300x204.jpg 300w, http://coolshell.cn//wp-content/uploads/2016/10/drawing-recursive-768x522.jpg 768w, http://coolshell.cn//wp-content/uploads/2016/10/drawing-recursive-1024x696.jpg 1024w, http://coolshell.cn//wp-content/uploads/2016/10/drawing-recursive.jpg 1100w\" sizes=\"(max-width: 300px) 100vw, 300px\" />今天在微博上看到了 有人<a href=\"http://weibo.com/1655747731/Ee4gU0qNn\" target=\"_blank\">分享了下面的这段函数式代码</a>，我把代码贴到下面，不过我对原来的代码略有改动，对于函数式的版本，咋一看，的确令人非常费解，仔细看一下，你可能就晕掉了，似乎完全就是天书，看上去非常装逼，哈哈。不过，我感觉解析那段函数式的代码可能会一个比较有趣过程，而且，我以前写过一篇《<a href=\"http://coolshell.cn/articles/10822.html\" target=\"_blank\">函数式编程</a>》的入门式的文章，正好可以用这个例子，再升华一下原来的那篇文章，顺便可以向大家更好的介绍很多基础知识，所以写下这篇文章。</p>\n" +
                "<h4>先看代码</h4>\n" +
                "<p>这个代码平淡无奇，就是从一个数组中找到一个数，O(n)的算法，找不到就返回 null。</p>\n" +
                "<p>下面是正常的 old-school 的方式。不用多说。</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">//正常的版本\n" +
                "function find (x, y) {\n" +
                "  for ( let i = 0; i &lt; x.length; i++ ) {\n" +
                "    if ( x[i] == y ) return i;\n" +
                "  }\n" +
                "  return null;\n" +
                "}\n" +
                "\n" +
                "let arr = [0,1,2,3,4,5]\n" +
                "console.log(find(arr, 2))\n" +
                "console.log(find(arr, 8))</pre>\n" +
                "<p>结果到了函数式成了下面这个样子（好像上面的那些代码在下面若影若现，不过又有点不太一样，为了消掉if语言，让其看上去更像一个表达式，动用了 ? 号表达式）：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">//函数式的版本\n" +
                "const find = ( f =&gt; f(f) ) ( f =&gt;\n" +
                "  (next =&gt; (x, y, i = 0) =&gt;\n" +
                "    ( i &gt;= x.length) ?  null :\n" +
                "      ( x[i] == y ) ? 1 :\n" +
                "        next(x, y, i+1))((...args) =&gt;\n" +
                "          (f(f))(...args)))\n" +
                "\n" +
                "let arr = [0,1,2,3,4,5]\n" +
                "console.log(find(arr, 2))\n" +
                "console.log(find(arr, 8))</pre>\n" +
                "<p>为了讲清这个代码，需要先补充一些知识。</p>\n" +
                "<p><span id=\"more-17524\"></span></p>\n" +
                "<h4>Javascript的箭头函数</h4>\n" +
                "<p>首先先简单掉一下，ECMAScript2015 引入的箭头表达式。箭头函数其实都是匿名函数，其基本语法如下：</p>\n" +
                "<blockquote>\n" +
                "<pre class=\"brush: jscript; gutter: false; title: ; notranslate\">(param1, param2, …, paramN) =&gt; { statements } \n" +
                "(param1, param2, …, paramN) =&gt; expression\n" +
                "     // 等于 :  =&gt; { return expression; } \n" +
                "\n" +
                "// 只有一个参数时,括号才可以不加:\n" +
                "(singleParam) =&gt; { statements }\n" +
                "singleParam =&gt; { statements }\n" +
                "\n" +
                "//如果没有参数,就一定要加括号:\n" +
                "() =&gt; { statements }</pre>\n" +
                "</blockquote>\n" +
                "<p>下面是一些示例：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">var simple = a =&gt; a &gt; 15 ? 15 : a; \n" +
                "simple(16); // 15\n" +
                "simple(10); // 10\n" +
                "\n" +
                "let max = (a, b) =&gt; a &gt; b ? a : b;\n" +
                "\n" +
                "// Easy array filtering, mapping, ...\n" +
                "\n" +
                "var arr = [5, 6, 13, 0, 1, 18, 23];\n" +
                "var sum = arr.reduce((a, b) =&gt; a + b);  // 66\n" +
                "var even = arr.filter(v =&gt; v % 2 == 0); // [6, 0, 18]\n" +
                "var double = arr.map(v =&gt; v * 2);       // [10, 12, 26, 0, 2, 36, 46]</pre>\n" +
                "<p>看上去不复杂吧。不过，上面前两个 simple 和 max 的例子都把这箭头函数赋值给了一个变量，于是它就有了一个名字。有时候，某些函数在声明的时候就是调用的时候，尤其是函数式编程中，一个函数还对外返回函数的时候。比如下在这个例子：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">function MakePowerFn(power) {\n" +
                "  return function PowerFn(base) {\n" +
                "    return Math.pow(base, power);\n" +
                "  } \n" +
                "}\n" +
                "\n" +
                "power3 = MakePowerFn(3); //制造一个X的3次方的函数\n" +
                "power2 = MakePowerFn(2); //制造一个X的2次方的函数\n" +
                "\n" +
                "console.log(power3(10)); //10的3次方 = 1000\n" +
                "console.log(power2(10)); //10的2次方 = 100</pre>\n" +
                "<p>其实，在 MakePowerFn 函数里的那个 PowerFn 根本不需要命名，完全可以写成：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">function MakePowerFn(power) {\n" +
                "  return function(base) {\n" +
                "    return Math.pow(base, power);\n" +
                "  } \n" +
                "}</pre>\n" +
                "<p>如果用箭头函数，可以写成：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">MakePowerFn = power  =&gt; {\n" +
                "  return base =&gt; {\n" +
                "    return Math.pow(base, power);\n" +
                "  } \n" +
                "}</pre>\n" +
                "<p>我们还可以写得更简洁（如果用表达式的话，就不需要 { 和 }， 以及 return 语句 ）：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">MakePowerFn = power =&gt; base =&gt; Math.pow(base, power)</pre>\n" +
                "<p>我还是加上括号，和换行可能会更清楚一些：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">MakePowerFn = (power) =&gt; (\n" +
                "  (base) =&gt; (Math.pow(base, power))\n" +
                ")</pre>\n" +
                "<p>好了，有了上面的知识，我们就可以进入一个更高级的话题——匿名函数的递归。</p>\n" +
                "<h4>匿名函数的递归</h4>\n" +
                "<p>函数式编程立志于用函数表达式消除有状态的函数，以及for/while循环，所以，在函数式编程的世界里是不应该用for/while循环的，而要改用递归（递归的性能很差，所以，一般是用尾递归来做优化，也就是把函数的计算的状态当成参数一层一层的往下传递，这样语言的编译器或解释器就不需要用函数栈来帮你保存函数的内部变量的状态了）。</p>\n" +
                "<p>好了，那么，匿名函数的递归该怎么做？</p>\n" +
                "<p>一般来说，递归的代码就是函数自己调用自己，比如我们求阶乘的代码：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">\n" +
                "function fact(n){\n" +
                "  return n==0 ? 1 :  n * fact(n-1);\n" +
                "};\n" +
                "result = fact(5);\n" +
                "</pre>\n" +
                "<p>在匿名函数下，这个递归该怎么写呢？对于匿名函数来说，<b>我们可以把匿名函数当成一个参数传给另外一个函数，因为函数的参数有名字，所以就可以调用自己了</b>。 如下所示：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">function combinator(func) {\n" +
                "  func(func);\n" +
                "}</pre>\n" +
                "<p>这个是不是有点作弊的嫌疑？Anyway，我们再往下，把上面这个函数整成箭头函数式的匿名函数的样子。</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">（func) =&gt; (func(func)) </pre>\n" +
                "<p>现在你似乎就不像作弊了吧。把上面那个求阶乘的函数套进来是这个样子：</p>\n" +
                "<p>首先，先重构一下fact，把fact中自己调用自己的名字去掉：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">function fact(func, n) {\n" +
                "  return n==0 ? 1 :  n * func(func, n-1);\n" +
                "}\n" +
                "\n" +
                "fact(fact, 5); //输出120\n" +
                "</pre>\n" +
                "<p>然后，我们再把上面这个版本变成箭头函数的匿名函数版：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">\n" +
                "var fact = (func, n) =&gt; ( n==0 ? 1 :  n * func(func, n-1) )\n" +
                "fact(fact, 5)\n" +
                "</pre>\n" +
                "<p>这里，我们依然还要用一个fact来保存这个匿名函数，我们继续，我们要让匿名函数声明的时候，就自己调用自己。</p>\n" +
                "<p>也就是说，我们要把 </p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">(func, n) =&gt; ( n==0 ? 1 :  n * func(func, n-1) )</pre>\n" +
                "<p>这个函数当成调用参数，传给下面这个函数：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">(func, x) =&gt; func(func, x) </pre>\n" +
                "<p>最终我们得到下面的代码：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\"> \n" +
                "( (func, x) =&gt; func(func, x) ) (  //函数体\n" +
                "  (func, n) =&gt; ( n==0 ? 1 :  n * func(func, n-1) ), //第一个调用参数\n" +
                "  5 //第二调用参数\n" +
                "); </pre>\n" +
                "<p>好像有点绕，anyway, 你看懂了吗？没事，我们继续。</p>\n" +
                "<h4>动用高阶函数的递归</h4>\n" +
                "<p>但是上面这个递归的匿名函数在自己调用自己，所以，代码中有hard code的实参。我们想实参去掉，如何去掉呢？我们可以参考前面说过的那个 MakePowerFn 的例子，不过这回是递归版的高阶函数了。</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">HighOrderFact = function(func){\n" +
                "  return function(n){\n" +
                "    return n==0 ? 1 : n * func(func)(n-1);\n" +
                "  };\n" +
                "};</pre>\n" +
                "<p>我们可以看，上面的代码简单说来就是，<b>需要一个函数做参数，然后返回这个函数的递归版本</b>。那么，我们怎么调用呢？</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">fact = HighOrderFact(HighOrderFact);\n" +
                "fact(5); </pre>\n" +
                "<p>连起来写就是：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">HighOrderFact ( HighOrderFact ) ( 5 )</pre>\n" +
                "<p>但是，这样让用户来调用很不爽，所以，以我们一个函数把 <b> HighOrderFact ( HighOrderFact ) </b> 给代理一下：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">fact = function ( hifunc ) {\n" +
                "  return hifunc ( hifunc );\n" +
                "} (\n" +
                "  //调用参数是一个函数\n" +
                "  function (func) { \n" +
                "    return function(n){\n" +
                "      return n==0 ? 1 : n * func(func)(n-1);\n" +
                "    };\n" +
                "  }\n" +
                ");\n" +
                "\n" +
                "fact(5); //于是我们就可以直接使用了</pre>\n" +
                "<p>用箭头函数重构一下，是不是简洁了一些？</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">fact = (highfunc =&gt; highfunc ( highfunc ) ) (\n" +
                "  func =&gt; n =&gt;  n==0 ? 1 : n * func(func)(n-1)\n" +
                ");</pre>\n" +
                "<p>上面就是我们最终版的阶乘的函数式代码。</p>\n" +
                "<h4>回顾之前的程序</h4>\n" +
                "<p>我们再来看那个查找数组的正常程序：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">//正常的版本\n" +
                "function find (x, y) {\n" +
                "  for ( let i = 0; i &lt; x.length; i++ ) {\n" +
                "    if ( x[i] == y ) return i;\n" +
                "  }\n" +
                "  return null;\n" +
                "}</pre>\n" +
                "<p>先把for干掉，搞成递归版本：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">function find (x, y, i=0) {\n" +
                "  if ( i &gt;= x.length ) return null;\n" +
                "  if ( x[i] == y ) return i;\n" +
                "  return find(x, y, i+1);\n" +
                "}</pre>\n" +
                "<p>然后，写出带实参的匿名函数的版本（注：其中的if代码被重构成了 ？号表达式）：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">( (func, x, y, i) =&gt; func(func, x, y, i) ) (  //函数体\n" +
                "  (func, x, y, i=0) =&gt; (\n" +
                "      i &gt;= x.length ?  null :\n" +
                "         x[i] == y  ?  i : func (func, x, y, i+1)\n" +
                "  ), //第一个调用参数\n" +
                "  arr, //第二调用参数\n" +
                "  2 //第三调用参数\n" +
                ")</pre>\n" +
                "<p>最后，引入高阶函数，去除实参：</p>\n" +
                "<pre class=\"brush: jscript; title: ; notranslate\">const find = ( highfunc =&gt; highfunc( highfunc ) ) (\n" +
                "   func =&gt; (x, y, i = 0) =&gt; (\n" +
                "     i &gt;= x.length ?  null :\n" +
                "           x[i] == y  ?  i : func (func) (x, y, i+1)\n" +
                "   )\n" +
                ");</pre>\n" +
                "<p>注：函数式编程装逼时一定要用const字符，这表示我写的函数里的状态是 immutable 的，天生骄傲！</p>\n" +
                "<p>再注：我写的这个比原来版的那个简单了很多，原来版本的那个又在函数中套了一套 next， 而且还动用了不定参数，当然，如果你想装逼装到天上的，理论上来说，你可以套N层，呵呵。</p>\n" +
                "<p><b>现在，你可以体会到，如此逼装的是怎么来的了吧？</b>。</p>\n" +
                "<h4>其它</h4>\n" +
                "<p>你还别说这就是装逼，简单来说，我们可以使用数学的方式来完成对复杂问题的描述，那怕是递归。其实，这并不是新鲜的东西，这是Alonzo Church 和 Haskell Curry 上世纪30年代提出来的东西，这个就是 Y Combinator 的玩法，关于这个东西，你可以看看下面两篇文章：</p>\n" +
                "<p>《<a href=\"http://mvanier.livejournal.com/2897.html\" target=\"_blank\">The Y Combinator (Slight Return)</a>》，</p>\n" +
                "<p>《<a href=\"https://en.wikipedia.org/wiki/Fixed-point_combinator\" target=\"_blank\">Wikipedia: Fixed-point combinator</a>》</p>\n" +
                "<p>（全文完）</p>\n" +
                "<p align=\"center\"><img src=http://coolshell.cn//wp-content/uploads/2009/04/qrcode_for_gh_dd9d8c843f20_860-300x300.jpg><br />关注CoolShell微信公众账号可以在手机端搜索文章</p>\n" +
                "<div style=\"margin-top: 15px; font-size: 11px;color: #cc0000;\">\n" +
                "<p align=center><strong>（转载本站文章请注明作者和出处 <a href=\"http://coolshell.cn/\">酷 壳 &#8211; CoolShell.cn</a> ，请勿用于任何商业用途）</strong></div>\n" +
                "<div style=\"text-align:center;padding:0px;font-size: 14px;margin-bottom: 50px;\">——=== <b>访问 <a href=http://coolshell.cn/404/ target=_blank>酷壳404页面</a> 寻找遗失儿童。</b> ===——</div>\n" +
                "<p align=center><a href=http://gold.xitu.io/welcome/?utm_source=coolshell&#038;utm_medium=banner&#038;utm_content=gaoshou&#038;utm_campaign=q3_website target=_blank><img src=http://coolshell.cn/wp-content/uploads/2016/08/xitu.io_.jpg></a><br /><span style=\"font-size: 12px;\">本广告收入已由广告主捐给Wikipedia</span></p>\n" +
                "\n" +
                "<div class=\"wp_rp_wrap  wp_rp_plain\" id=\"wp_rp_first\"><div class=\"wp_rp_content\"><h3 class=\"related_post_title\">相关文章</h3><ul class=\"related_post wp_rp\"><li data-position=\"0\" data-poid=\"in-10822\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2013年12月27日</small> <a href=\"http://coolshell.cn/articles/10822.html\" class=\"wp_rp_title\">函数式编程</a></li><li data-position=\"1\" data-poid=\"in-11265\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2014年03月20日</small> <a href=\"http://coolshell.cn/articles/11265.html\" class=\"wp_rp_title\">Python修饰器的函数式编程</a></li><li data-position=\"2\" data-poid=\"in-8309\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2012年09月20日</small> <a href=\"http://coolshell.cn/articles/8309.html\" class=\"wp_rp_title\">C/C++语言中闭包的探究及比较</a></li><li data-position=\"3\" data-poid=\"in-5709\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2011年10月31日</small> <a href=\"http://coolshell.cn/articles/5709.html\" class=\"wp_rp_title\">API设计：用流畅接口构造内部DSL</a></li><li data-position=\"4\" data-poid=\"in-10739\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2013年12月03日</small> <a href=\"http://coolshell.cn/articles/10739.html\" class=\"wp_rp_title\">Lua简明教程</a></li><li data-position=\"5\" data-poid=\"in-2053\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2010年01月21日</small> <a href=\"http://coolshell.cn/articles/2053.html\" class=\"wp_rp_title\">最为奇怪的程序语言的特性</a></li><li data-position=\"6\" data-poid=\"in-10337\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2013年08月09日</small> <a href=\"http://coolshell.cn/articles/10337.html\" class=\"wp_rp_title\">数据即代码：元驱动编程</a></li><li data-position=\"7\" data-poid=\"in-2492\" data-post-type=\"none\" ><small class=\"wp_rp_publish_date\">2010年06月02日</small> <a href=\"http://coolshell.cn/articles/2492.html\" class=\"wp_rp_title\">WTF Javascript</a></li></ul></div></div>\n" +
                "]]>");
        setData(article, "酷壳");
    }

    private void setData(Article article, String subscriptionName) {
        mTitleTextView.setText(article.getTitle());
        mDateTextView.setText(DateUtil.formatDate(this, article.getPublished()));
        mTimeTextView.setText(DateUtil.formatTime(article.getPublished()));
        mSubscriptionNameTextView.setText(subscriptionName);
        mContentTextView.setHtml(article.getContent(), new HtmlHttpImageGetter(mContentTextView));
    }
}
