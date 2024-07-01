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
//        ResponseEntity<String> s = restTemplate.exchange("https://www.aliexpress.com/gcp/300001014/krgroupbuy-share?shareGroupCode=SGC_6re466O565Scc3lrMA&invitationCode=dDljSzRDZTFPYXloNXJSZG1YOTVNa0ZUN3N5enlJakZkYWdyTi9sUGtDTU5DcjZTcndBcUVRPT0&disableNav=YES&_immersiveMode=true&isSmbAutoCall=false&srcSns=sns_Copy&pha_manifest=ssr&spreadType=paidShare01&bizType=sharegroup&social_params=6000117495799&spreadCode=dDljSzRDZTFPYXloNXJSZG1YOTVNa0ZUN3N5enlJakZkYWdyTi9sUGtDTU5DcjZTcndBcUVRPT0&isSmbShow=false&aff_fcid=0315c3cb9cae4e24b5ccf868382a4123-1719127364316-05503-_oBuYXrk&tt=MG&aff_fsk=_oBuYXrk&aff_platform=default&sk=_oBuYXrk&aff_trace_key=0315c3cb9cae4e24b5ccf868382a4123-1719127364316-05503-_oBuYXrk&shareId=6000117495799&businessType=sharegroup&platform=AE&terminal_id=f316976369734e3aba990c79a22719b1", HttpMethod.GET,entity, String.class);
//        System.out.println(s.getBody());
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
