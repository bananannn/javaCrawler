package com.moyan.crawler.scraper;

import com.moyan.crawler.entity.Entity;
import com.moyan.crawler.service.SqlService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
   Class name: MyCrawler
   Description: 1. Detect the urls within a website, then keep on searching in the new website;
                2. Use cache to store the loaded page then upload the expired ones and also check whether
                a website has been loaded.


 */
public class MyCrawler extends WebCrawler {

    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
    private static final Pattern PRICE_FORMAT = Pattern.compile("(\\$|￥|^)(\\d+)(元|美元|$)");
    private static final Pattern URL_FORMAT = Pattern.compile("(https://guangdiu.com)(((/detail|/go|/index)(.*))|^$)");
    private static final Pattern DECODE_COLON = Pattern.compile("%3A");
    private static final Pattern DECODE_SLASH = Pattern.compile("%2F");

    private static final Pattern EXTRACT_FORMAT = Pattern.compile("(https?%3A%2F%2F)[^\\s/$.?#].*\\.(html)");
    private long startTime = System.currentTimeMillis();
    private long endTime;

    public static File storageFolder;
    private static String[] crawlDomains;
    private final SqlService service;

    public MyCrawler(SqlService service) {
        this.service = service;
    }

    public static void configure(String[] domain, String storageFolderName) {

        storageFolder = new File(storageFolderName);

    }

    public String getRedirectUrl(String path) throws Exception {
        String redirectedUrl = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpContext context = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(path);

            CloseableHttpResponse response = httpClient.execute(httpGet, context);

            try {
                // get the response entity
                HttpEntity entity = response.getEntity();
                String innerContext = EntityUtils.toString(entity);
                logger.info(print("status: %s", response.getStatusLine()));
                //replace the matched ones
                Matcher m = EXTRACT_FORMAT.matcher(innerContext);
                if (m.find()) {
                    redirectedUrl = m.group(0);
                    redirectedUrl = DECODE_COLON.matcher(redirectedUrl).replaceAll(":");
                    redirectedUrl = DECODE_SLASH.matcher(redirectedUrl).replaceAll("/");
                }

            } finally {
                response.close();

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return redirectedUrl;


    }


    /**
     * whether the given url should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return href.startsWith("https://guangdiu.com/");
    }

    /**
     * Find the uploaded page url from the current homepage
     */
    private String updateHomePage() {
        String fixURL = "https://guangdiu.com/?kf=hb&lid=";
        // append system time to avoid repeatedly loading
        fixURL += System.currentTimeMillis();
        System.out.println(fixURL);
        return fixURL;

    }

    // Count the time to update the home page
    private void checkUpdateTime() {
        /*
           Find out the time passed;
           if it is 10 mins already, add the updated homepage address again
         */
        this.setEndTime(System.currentTimeMillis());

        //logger.info(print("endTime %d", this.getEndTime()));
        //logger.info(print("previous startTime %d", this.getStartTime()));

        if (this.getEndTime() - this.getStartTime() >= 600000) {

            // if the time interval is reasonable to reload, reload the homepage
            String updateURL = this.updateHomePage();
            this.getMyController().addSeed(updateURL);
            this.setStartTime(System.currentTimeMillis());
            //logger.info(print("startTime %d", this.getStartTime()));

        }
    }


    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {

        checkUpdateTime();
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String category = url.substring(url.indexOf("=") + 1);
        String anchor = page.getWebURL().getAnchor();

        // show the log
        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.debug("Anchor text: {}", anchor);

        // One usage of the crawler4j
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            // Parse and grab the message on the main page
            // For instance, the url of main pages: https://guangdiu.com/cate.php?k=makeup
            // The url of detailed pages: https://guangdiu.com/detail.php?id=5555775
            String className = "";
            if (url.equals("https://guangdiu.com/") || url.substring(21, 26).equals("index") ||
                    url.substring(21, 25).equals("cate")) {
                this.parsePage(url, category, "goodname");
            } else if (url.substring(21, 27).equals("detail")) {
                this.parsePage(url, "detail", "drelated");
            } else {
                logger.info("Skip the page");
            }

            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());
        }
    }

    private static String print(String msg, Object... args) {
        //System.out.println(String.format(msg, args));
        return String.format(msg, args);
    }

    private void parsePage(String url, String category, String className) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("cannot get connection");
        }

        //TODO
        Elements productBlocks;
        if (className.equals("drelated")) productBlocks = doc.getElementsByClass(className);
        else productBlocks = doc.select("div.gooditem");

        // For each of the product blocks
        for (Element productBlock : productBlocks) {
            Entity entity = new Entity();
            //TODO Find the redirected url which links to the original website
            Elements gotourl = productBlock.getElementsByClass("innergototobuybtn");
            String linkedurl = gotourl.attr("href");

            //if(LINK_FORMAT.matcher("linkedurl").matches()){
            if (linkedurl.length() >= 6 && linkedurl.substring(0, 6).equals("go.php")) {
                linkedurl = "https://guangdiu.com/" + linkedurl;
                try {
                    linkedurl = this.getRedirectUrl(linkedurl);
                } catch (Exception e) {
                    logger.error("finding the linked url failed", e);
                }
            }
            entity.setLinkedurl(linkedurl);


            Elements innerBlocks = productBlock.getElementsByClass("goodname");
            entity.setProducturl(url);
            entity.setCate(category);

            // All the features included
            boolean shippingFee = false;
            boolean coupon = false;
            boolean special = false;
            String discount = "";
            String priceOff = "";
            String priceNeed = "";
            String realPrice = "";
            int flag = 0;

            // for each of the products within current page
            String productName = productBlock.text();
            entity.setInfo(productName);

            // get the specific id which represent an unique product
            String innerurl = innerBlocks.attr("href");

            String id = innerurl.substring(innerurl.indexOf('=') + 1);
            Long lid = 0L;
            if (id != "") {
                lid = Long.parseLong(id, 10);
            }
            entity.setPid(lid);

            if (className == "derelated") {
                entity.setInfo(productBlock.attr("drelatedword"));
            }


            Elements priceInfos = productBlock.select("span");
            for (Element priceInfo : priceInfos) {

                // whether it has a shipping fee
                String price = priceInfo.text();

                logger.info(print("price: %s", price));

                if (price.equals("包邮")) {
                    shippingFee = true;
                    entity.setShipping(true);
                    //flag++;
                }
                // whether it has used a coupon
                if (price.equals("券") || price.equals("券后")) {
                    coupon = true;
                    entity.setCoupon(true);
                    //flag++;
                }

                // whether it has a generous discount
                if (price.equals("特价")) {
                    special = true;
                    entity.setSpecial(true);
                    //flag++;
                }

                if (price.length() >= 1) {

                    // the discount rate
                    if (price.substring(price.length() - 1, price.length()).equals("折")) {
                        entity.setDiscount(price.substring(0, price.length() - 1));
                        //flag++;
                    }
                }

                //logger.info(print("current price: %s ", price));
                if (entity.getPrice() == 0) {
                    double fprice = 0.0;
                    Matcher m = PRICE_FORMAT.matcher(price);
                    if (m.find()) {
                        fprice = Double.parseDouble(m.group(2));
                        if (!m.group(1).equals("")) entity.setCurrency(m.group(1));
                        else entity.setCurrency(m.group(3));
                    }
                    entity.setPrice(fprice);
                }
                /*else{
                    logger.info(print("could not identify: %s", price));
                }*/
                //flag = 0;
            }
            // Print the related output

            logger.info(print("* %s <%d> 价格：%s, 种类：%s，是否包邮：%b, 是否用券：%b, 是否特价：%b, 折扣：%s, 满%s 减%s",
                    productName, lid, realPrice, category, shippingFee, coupon, special, discount, priceNeed, priceOff));

            try {
                service.save(entity);
            } catch (RuntimeException e) {
                logger.error("Storing failed", e);
            }


            //service.save(entity);

        }
    }

    /**
     * Helper method to get and set the startTime and endTime private variables
     * The following four methods
     */
    public void setStartTime(long time) {

        this.startTime = time;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long time) {
        this.endTime = time;
    }

    public long getEndTime() {
        return this.endTime;
    }
}