package info.biyesheji.reptile.controller;

import info.biyesheji.reptile.entity.ReptileLog;
import info.biyesheji.reptile.entity.ReptileParam;
import info.biyesheji.reptile.mapper.ReptileLogMapper;
import info.biyesheji.reptile.util.RequestUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static info.biyesheji.reptile.entity.ReptileLog.已下载;
import static info.biyesheji.reptile.entity.ReptileLog.未处理;

@RestController
public class ReptileTaskController {
    private static final Logger logger = LoggerFactory.getLogger(ReptileTaskController.class);
    private static volatile Boolean isStop;
    private static volatile Boolean stopClone = false;
    private static volatile Boolean isStartClone = false;

    @Autowired
    private ReptileLogMapper reptileLogMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    @RequestMapping("/reptileGitHubUrl.html")
    public Object reptileGitUrl(@RequestBody ReptileParam param) {
        if (param.getMoreThread() == 1 && isStop != null && !isStop)
            return RequestUtil.error("gitHub 爬虫已启动!");
        String url = param.getUrl();
        if (url.indexOf("l=Java") < 0) {
            url += "&l=Java";
            param.setUrl(url);
        }
        logger.info("爬取 gitHub 网页 请求路径:  " + url);
        isStop = false;
        ReptileThread reptileThread = new ReptileThread(param, reptileLogMapper);
        new Thread(reptileThread).start();
        return RequestUtil.succ();
    }

    @RequestMapping("/stopReptileGitHub.html")
    public Object stopReptileGitHub() {
        isStop = true;
        return RequestUtil.succ();
    }

    @RequestMapping("/startClone.html")
    public Object startClone(@RequestParam(value = "cloneNum", required = false, defaultValue = "100") Integer cloneNum) {
        List<ReptileLog> reptileLogList = reptileLogMapper.listReptileLogTask(未处理);
        StringBuilder builder = new StringBuilder();
        if (isStartClone)
            return RequestUtil.error("git clone 任务已启动 , 请等待本任务结束后启动!");
        try {
            isStartClone = true;
            new Thread(() -> {
                int count = 0;
                for (ReptileLog reptileLog : reptileLogList) {
                    if (stopClone) break;
                    if (count >= cloneNum) break;

                    File localPath = new File("/usr/data/gitProject" + reptileLog.getProjectName());
//                    File localPath = new File("D:\\gitProject" + reptileLog.getProjectName());
                    try (Git result = Git.cloneRepository()
                            .setURI(reptileLog.getGitUrl())
                            .setDirectory(localPath)
                            .call()) {
                        logger.info("git clone 成功:  " + reptileLog.getGitUrl());
                        reptileLog.setStatus(已下载);
                        reptileLog.setUpdateTime(new Date());
                        builder.append(reptileLog.getGitUrl() + "\n");
                        reptileLogMapper.updateReptileLogByPrimaryId(reptileLog);
                    } catch (GitAPIException e) {
                        e.printStackTrace();
                        logger.info("git clone 失败:  " + reptileLog.getGitUrl());
                    }
                    count++;
                }
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("liren@weyao.com");
                message.setTo("liren@weyao.com");
                message.setSubject("gitHub clone 结束");
                message.setText(builder.toString());
                javaMailSender.send(message);
            }).start();
        } finally {
            isStartClone = false;
        }
        return RequestUtil.succ();
    }

    @RequestMapping("/stopClone.html")
    public Object stopClone() {
        stopClone = true;
        return RequestUtil.succ();
    }

    private static final class ReptileThread implements Runnable {
        private ReptileParam param;
        private ReptileLogMapper mapper;
        private String initUrl;

        public ReptileThread(ReptileParam param, ReptileLogMapper reptileLogMapper) {
            this.param = param;
            mapper = reptileLogMapper;
            initUrl = param.getUrl();
        }

        @Override
        public void run() {

            CloseableHttpClient httpClient = HttpClients.createDefault();
            logger.info("本次爬取url:  " + param.getUrl());
            HttpGet httpGet = new HttpGet(param.getUrl());
            String nextPage = "";
            String body = "";
            try {
                httpGet.setHeader("Cookie", param.getCookie());
                HttpResponse response;
                HttpEntity entity;
                response = httpClient.execute(httpGet);
                entity = response.getEntity();
                body = EntityUtils.toString(entity, "utf-8");

                Document document = Jsoup.parse(body);
                Elements elements = document.select("li.repo-list-item.d-flex.flex-column.flex-md-row.flex-justify-start.py-4.public.source");
                for (Element element : elements) {
                    Elements elements1 = element.select("[itemprop='programmingLanguage']");
                    String value = elements1.text();
                    if (value.equalsIgnoreCase("java")) {
                        String urlStr = element.select("a.v-align-middle").attr("href");
                        if (!StringUtils.isEmpty(urlStr)) {
                            String url = httpGet.getURI().getAuthority() + urlStr;
                            ReptileLog reptileLog = new ReptileLog();
                            reptileLog.setLanguageType(value);
                            reptileLog.setRemark(element.select("p.col-12.col-md-9.d-inline-block.text-gray.mb-2.pr-4").text());
                            reptileLog.setStartNum(element.select("a.muted-link").text());
                            reptileLog.setGitUrl("https://" + url + ".git");
                            reptileLog.setId(reptileLog.getGitUrl().hashCode());
                            reptileLog.setUrl(url);
                            reptileLog.setProjectName(urlStr);
                            reptileLog.setType(1);
                            reptileLog.setStatus(0);
                            reptileLog.setCreateTime(new Date());

                            try {
                                mapper.addReptileLog(reptileLog);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
                nextPage = document.select("a.next_page").attr("href");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!isStop && !StringUtils.isEmpty(nextPage)) {
                URI url = httpGet.getURI();
                String nextPageUrl = url.getAuthority();
                param.setUrl("https://" + nextPageUrl + nextPage);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                run();
            } else {
                if (param.getMoreThread() == 0)
                    isStop = true;
                httpGet.releaseConnection();
                logger.info("本次爬取结束:  " + initUrl);
                logger.info("最后一次页面html:   " + body);
                return;
            }
        }
    }


}
