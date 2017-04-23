package com.gmail.leetao94cn.searchmovies.MovieSearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leetao on 2016/9/7.
 */

public class PianYuan {

    private String _domain = "http://pianyuan.net";
    private String _searchUrl = "http://pianyuan.net/search";
    private ArrayList _movieSourceList = new ArrayList();

    public List getMovieSoureList(String moviename) {

        Document searchResult = _getSearchResults(_searchUrl,moviename);
        ArrayList detailPageUrlLists = _getMovieDetailsPageUrlList(searchResult);
        for(int i = 0 ; i < detailPageUrlLists.size(); i++){
            String downLoadUrl = _domain + detailPageUrlLists.get(i);
            Document downLoadContent = _getSearchResults(downLoadUrl);
            ArrayList downLoadUrlList = _getDownPagesUrlList(downLoadContent);
            if(downLoadUrlList.size() > 0){
                _movieSourceList.add(downLoadUrlList.get(0));
            }
        }
        return _movieSourceList;
    }

    /**
     * 获取结果的内容
     *
     * @param Url   查询的地址
     *
     * @param moviename  查询的参数
     *
     * @Exception IOException
     *
     * @return  Object  返回对象
     */
    private Document _getSearchResults(String Url,String moviename) {
        Document doc = null;
        try {
            doc = Jsoup.connect(Url).data("q",moviename).get();
        } catch (IOException e) {
            System.out.print(e.toString());
        }
        return doc;
    }

    /**
     * 获取指定网页内容
     *
     * @param Url   查询的地址
     *
     * @Exception IOException
     *
     * @return  Object 返回对象或者null
     */
    private Document _getSearchResults(String Url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(Url).get();
        } catch (IOException e) {
            System.out.print(e.toString());
        }
        return doc;
    }


    /**
     * 根据查询结果对象,获取所有详细页面的地址列表
     *
     * @param doc   页面内容对象
     *
     * @return  List    详细页面地址列表
     */
    private ArrayList _getMovieDetailsPageUrlList(Document doc){
        ArrayList detailsPageUrlList = new ArrayList();
        Elements links = doc.select("a[href]");
        for(Element link : links) {
            if(link.className().equals("ico ico_bt")){
                detailsPageUrlList.add(link.attr("href"));
            }
        }
        return detailsPageUrlList;
    }

    /**
     * 获取下载页面的详细链接
     *
     * @param doc   下载页面的对象
     *
     * @return List  返回下载列表
     */
    private ArrayList _getDownPagesUrlList(Document doc){
        ArrayList downPagesUrlList = new ArrayList();
        Elements links = doc.select("a[href]");
        for(Element link : links){
            if(link.className().equals("btn  btn-primary btn-sm")){
                downPagesUrlList.add(link.attr("href"));
            }
        }
        return downPagesUrlList;
    }

//    public static void main(String args[]){
//        PianYuan pianYuan = new PianYuan();
//        pianYuan.getMovieSoureList("功夫熊猫");
//    }
}
