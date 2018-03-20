package com.michal;

import com.michal.downloader.ImageDownloader;
import com.michal.enums.EncoderType;
import com.michal.enums.InputType;
import com.michal.enums.OutputType;

import java.io.*;
import java.net.UnknownHostException;


public class Application {

    public static void main(String[] args) throws IOException {

        int timeout;
        int depth;
        String url;
        String outFormat;
        String filePath;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Url: ");
        url = bufferedReader.readLine();


        System.out.print("Set Timeout: (default: 1500) ");
        String timeS;
        try {
            timeS = bufferedReader.readLine();
            timeout = Integer.parseInt(timeS);
        } catch (Exception ignored) {
            timeout = 1500;
        }

        System.out.print("Download depth: (default: all) ");
        String depthS;
        try {
            depthS = bufferedReader.readLine();
            depth = Integer.parseInt(depthS);
        } catch (Exception ignored) {
            depth = Integer.MAX_VALUE;
        }

        System.out.print("Specify output format: (json, xml) ");
        outFormat = bufferedReader.readLine();


        OutputType outputType;

        switch (outFormat) {
            case "xml":
                outputType = OutputType.XML;
                break;
            case "json":
                outputType = OutputType.JSON;
                break;
            default:
                System.out.println("Wrong output type. Run again and choose one from avaible (json, xml)");
                return;
        }

        System.out.print("Output file name: ");
        filePath = bufferedReader.readLine();
        File saveFile = new File(filePath);


        WebPageParser webPageParser = WebPageParser.WebPageParserBuilder.buildWebPageParser(
                InputType.CENEO,
                outputType,
                EncoderType.BASE64
        );

        String parsed;
        try {
            parsed = webPageParser.getParsedProducts(url, timeout, depth);
        } catch (Exception e) {
            if (e.getClass().equals(ParsingException.class)) {
                System.out.println(e.getMessage() + " Check given URL or change parser engine\n given URL: " + url);
                return;
            } else if (e.getClass().equals(UnknownHostException.class)) {
                System.out.println("Could not connect given website. Please check URL or your internet connection");
                return;
            } else {
                e.printStackTrace();
                return;
            }
        }

        if (ImageDownloader.errCounter != 0) {
            System.out.println("Stats: " + ImageDownloader.errCounter + " images could not be downloaded, " +
                    "\n\tcause: Could not get input stream from url");
        }

        System.out.println("Saving to file..");
        writeStringToFile(saveFile, parsed);
        System.out.println("done");

    }

    private static void writeStringToFile(File file, String string) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
