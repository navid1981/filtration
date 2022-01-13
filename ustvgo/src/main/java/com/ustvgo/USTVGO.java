package com.ustvgo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//public class USTVGO extends TimerTask implements RequestHandler<Object,String> {
public class USTVGO {

    public static String URL="https://h1.ustvgo.la/";
    public static String path="/myStream/playlist.m3u8?wmsAuthSign=";
    public static void main(String[] args)  {
//        Timer time = new Timer(); // Instantiate Timer Object
//        USTVGO st = new USTVGO(); // Instantiate SheduledTask class
//        time.schedule(st, 0, 1000*60*60*2);
//        st.run();
        StringBuffer sb=new StringBuffer();
        sb.append("#EXTM3U x-tvg-url=\"https://raw.githubusercontent.com/nomoney4me/ustvgo/main/output/ustvgo_epg.xml\"");
        sb.append(System.getProperty("line.separator"));
        sb.append(addFarsiChannel());
        sb.append(System.getProperty("line.separator"));
        RestClient rc=new RestClient();
        USFile file=new USFile();
//        List<String> list=file.readFileLine();
        List<String> list=file.returnList();
        System.out.println(list.size());
        String token = rc.getToken();
        for (int i = 0; i < list.size(); i++) {
            String[] line = list.get(i).split(" \\| ");
            String name = line[0].replaceAll("\\s+$", "");
            ;
            String code = line[1];
            String logo = line[2];
//            String m3u = rc.call(code);
            sb.append("#EXTINF:-1 tvg-id=\"" + code + "\" group-title=\"ustvgo\" tvg-logo=\"" + logo + "\", " + name);
            sb.append(System.getProperty("line.separator"));
//            sb.append(m3u);
            sb.append(URL+code+path+token);
            sb.append(System.getProperty("line.separator"));
        }
        file.writeFile(sb.toString());
    }

//    @Override
//    public void run() {
//        StringBuffer sb=new StringBuffer();
//        sb.append("#EXTM3U x-tvg-url=\"https://raw.githubusercontent.com/nomoney4me/ustvgo/main/output/ustvgo_epg.xml\"");
//        sb.append(System.getProperty("line.separator"));
//        RestClient rc=new RestClient();
//        USFile file=new USFile();
////        List<String> list=file.readFileLine();
//        List<String> list=file.returnList();
//        System.out.println(list.size());
//        for (int i = 0; i < list.size(); i++) {
//            String[] line= list.get(i).split(" \\| ");
//            String name = line[0].replaceAll("\\s+$", "");;
//            String code = line[1];
//            String logo = line[2];
//            String m3u = rc.call(code);
//            sb.append("#EXTINF:-1 tvg-id=\""+code+"\" group-title=\"ustvgo\" tvg-logo=\""+logo+"\", "+name);
//            sb.append(System.getProperty("line.separator"));
//            sb.append(m3u);
//            sb.append(System.getProperty("line.separator"));
//        }
//        file.writeFile(sb.toString());
//    }

//    @Override
//    public String handleRequest(Object s, Context context) {
//        USTVGO st = new USTVGO(); // Instantiate SheduledTask class
//        st.run();
//        return "Navid file is uploded";
//    }
    private static String addFarsiChannel(){
        return
                "#EXTINF:-1 tvg-id=\"BBCpersian\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"BBCpersian\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/bbcpersian.stream/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"IranInt\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"IranInt\"\n"+
                "http://cds.c7a2t5h4.hwcdn.net/app/IranIntlJadoo/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"Manoto\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"Manoto\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/prov/manoto/playlist.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"ManotoPlus\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"ManotoPlus\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/manotoplus1/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"IRIB1\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"IRIB1\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/85300.stream/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"IRIB2\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"IRIB2\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/22035.stream/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"IRIB3\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"IRIB3\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/iribtv3/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"IRIB5\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"IRIB5\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/19035.stream/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"CineFilm\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"CineFilm\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/cinefilmfarsi/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"CineSeries\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"CineSeries\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/cineseries/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"MBCPersia\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"MBCPersia\"\n"+ "http://cds.y7c2y5s4.hwcdn.net/app/mbcpersia/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"GEM\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"GEM\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/429075.stream/chunks.m3u8\n"+
                "#EXTINF:-1 tvg-id=\"GEMFilm\" group-title=\"ustvgo\" tvg-logo=\"https://m3uustv.s3.us-east-2.amazonaws.com/IMG_2636.JPG\", tvg-name=\"GEMFilm\"\n"+
                "http://cds.y7c2y5s4.hwcdn.net/app/755225.stream/chunks.m3u8"
                ;
    }
}
