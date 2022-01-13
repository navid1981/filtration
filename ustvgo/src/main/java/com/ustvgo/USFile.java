package com.ustvgo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class USFile {
//    public List<String> readFileLine(){
//        File file=new File("./src/main/resources/ustvgo_channel_info.txt");
//        List<String> list = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                list.add(line);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
    public void writeFile(String s) {
        Regions clientRegion = Regions.US_EAST_2;
        String bucketName = "m3uustv";
        String stringObjKeyName = "ustvgo.m3u";
        String fileObjKeyName = "ustvgo.m3u";
        String fileName = "ustvgo.m3u";

        try {
            AWSCredentials credentials=new AWSCredentials() {
                @Override
                public String getAWSAccessKeyId() {
                    return "AKIAXVNDGGQL6KGVY74V";
                }

                @Override
                public String getAWSSecretKey() {
                    return "Ipq2DNqV/K0V4SeNe+3rGNGFHvTznzlEp6BOWMzm";
                }
            };
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(clientRegion)
                    .build();
            InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));

            ObjectMetadata meta = new ObjectMetadata();
            s3Client.putObject(new PutObjectRequest(bucketName, stringObjKeyName, stream, meta).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    public List<String> returnList(){

        return Arrays.asList(Constant.s);
    }
}
