package com.doramonz.aligonggoo.util;

import com.doramonz.aligonggoo.dto.DefaultError;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Component
public class AliProductUtil {

    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(10))
            .setReadTimeout(Duration.ofSeconds(10))
            .build();

    private final HttpHeaders headers = new HttpHeaders();

    private HttpEntity<String> entity;

    @PostConstruct
    public void init() {
        headers.add("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36");
        headers.add("Accept", "text/html;charset=UTF-8");
        entity = new HttpEntity<>(headers);
    }

    public AliProductInfo getProductInfo(String url) throws DefaultError {
        log.info("getProductInfo url: {}", url);
        ResponseEntity<String> s;
        try {
            s = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            throw new DefaultError("URL이 올바르지 않습니다.", 400);
        }

        JsonObject jsonObject;
        try {
            String script = s.getBody().split("\"groupItemInfo\":\\[")[1].split("],\"groupStarDTO\"")[0];
            jsonObject = JsonParser.parseString(script).getAsJsonObject();
        } catch (Exception e) {
            throw new DefaultError("닫힌 공구이거나 상품을 찾을 수 없습니다.", 400);
        }

        String imgageUrl = "https:" + jsonObject.get("imageUrl").getAsString();
        String title = jsonObject.get("title").getAsString();
        Integer price = Integer.valueOf(jsonObject.get("localizedMinPriceInfo").getAsString().split("\\|")[1].split("\\|")[0]);
        return new AliProductInfo(imgageUrl, title, price);
    }
}

//package com.doramonz.aligonggoo;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//
//public class Test {
//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36");
//        headers.add("Accept", "text/html;charset=UTF-8");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> s = restTemplate.exchange("https://www.aliexpress.com/gcp/300001014/krgroupbuy-share?invitationCode=Yzg1cGl6RENMaGFZa2RIQm90RHprUHA5K3pUNDk2MTdBeEZDTFBWRDdlWUVsR3Zpc0lTQi9WT0s1MU1hdTAyWg&disableNav=YES&_immersiveMode=true&isSmbAutoCall=false&spreadType=ordershare&spreadCode=Yzg1cGl6RENMaGFZa2RIQm90RHprUHA5K3pUNDk2MTdBeEZDTFBWRDdlWUVsR3Zpc0lTQi9WT0s1MU1hdTAyWg&isSmbShow=false&shareGroupCode=SGC_6re466O565Scc3JjZA&srcSns=sns_Copy&pha_manifest=ssr&bizType=sharegroup&social_params=6000117155169&aff_fcid=81b7fdb346a2461c95ddd2aa27a41d6d-1718977305721-01167-_oDra31y&tt=MG&aff_fsk=_oDra31y&aff_platform=default&sk=_oDra31y&aff_trace_key=81b7fdb346a2461c95ddd2aa27a41d6d-1718977305721-01167-_oDra31y&shareId=6000117155169&businessType=sharegroup&platform=AE&terminal_id=f316976369734e3aba990c79a22719b1", HttpMethod.GET,entity, String.class);
//        String script = s.getBody().split("\"groupItemInfo\":\\[")[1].split("],\"groupStarDTO\"")[0];
//        JsonObject jsonObject = JsonParser.parseString(script).getAsJsonObject();
//        String imgageUrl = "https:"+jsonObject.get("imageUrl").getAsString();
//        String title = jsonObject.get("title").getAsString();
//        Integer price = Integer.valueOf(jsonObject.get("localizedMinPriceInfo").getAsString().split("\\|")[1].split("\\|")[0]);
//        System.out.println(imgageUrl);
//        System.out.println(title);
//        System.out.println(price);
//        ResponseEntity<byte[]> entity2 = restTemplate.getForEntity(imgageUrl, byte[].class);
//        try {
//            Files.write(new File("image_"+title+".jpg").toPath(), entity2.getBody());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
