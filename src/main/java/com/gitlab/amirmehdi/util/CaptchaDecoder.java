package com.gitlab.amirmehdi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CaptchaDecoder {
    private final static CaptchaDecoder decoder = new CaptchaDecoder();

    public static CaptchaDecoder getInstance() {
        return decoder;
    }

    private CaptchaDecoder() {
    }

    //for tadbir
    public String mode1Captcha(final String fileName, final String type) throws IOException, InterruptedException {
        Runtime.getRuntime().exec("convert " + fileName + type + " -crop 190x60+0+0 -colorspace Gray -blur 3x1  -morphology Erode Diamond  -level 20%  -channel rgb -auto-level  -crop 4x1-2-2@ +repage +adjoin " + fileName + "_%d.png");
        String captcha = "";
        for (int i = 0; i < 4; i++) {
            List<String> repeat = new ArrayList<>();
            for (int j = -30; j <= 30; j += 10) {
                Runtime.getRuntime().exec(String.format("convert %s_%d.png -rotate %d %s_%d0.png", fileName, i, j, fileName, i));
                Process process = Runtime.getRuntime().exec(String.format("tesseract %s_%d0.png - --psm 13 digits ", fileName, i));
                String num = getProcessOutput(process);
                if (num != null && num.replaceAll("\\D+", "").length() == 1) {
                    repeat.add(num.replaceAll("\\D+", ""));
                }
            }
            if (repeat.isEmpty()) {
                throw new RuntimeException();
            }
            captcha += getPopularElement(repeat);
        }
        return captcha;
    }

    //for sahra
    public String mode2Captcha(String fileName, String type) throws IOException, InterruptedException {
        Runtime.getRuntime().exec(
                "convert " + fileName + type + " -colorspace Gray -blur 3x1 -level 20% -channel rgb -auto-level " + fileName + ".png");
        Process process = Runtime.getRuntime().exec("tesseract " + fileName + ".png" + " - --psm 6 digits");
        String captcha = getProcessOutput(process);
        return captcha.replaceAll("\\D+", "");
    }

    public String getProcessOutput(Process process) throws IOException, InterruptedException {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return output.toString();
        }
        return null;
    }

    public String getPopularElement(List<String> a) {
        return a.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }
}
