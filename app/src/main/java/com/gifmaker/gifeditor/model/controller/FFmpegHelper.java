package com.gifmaker.gifeditor.model.controller;

import android.content.Context;

import com.gifmaker.gifeditor.utils.Develop;
import com.gifmaker.gifeditor.utils.Logger;
import com.gifmaker.gifeditor.utils.StringUtils;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mr.logic on 7/28/2017.
 */

public class FFmpegHelper {

    private final static String TAG = FFmpegHelper.class.getSimpleName();
    private final static String FPS_EXTRACT_ALLFRAME_DEFAULT = "2/1";//2 frame / 1 second
    public final static int TOTAL_IMAGE_EXTRACT_ALL_FRAME = 20;

    public static String convertCommandToString(String[] commands) {

        String str_command = null;
        for (String c : commands) {
            str_command = str_command + " " + c;
        }
        Logger.e(Develop.TAG, "command:" + str_command);
        return str_command;
    }

    public static void executeCommand(Context context, String[] command, OnExecuteCommandFFmpeg onExecuteCommandFFmpeg) {

        try {
            FFmpeg ffmpeg = FFmpeg.getInstance(context);
            ffmpeg.execute(command, onExecuteCommandFFmpeg);
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            if (onExecuteCommandFFmpeg != null)
                onExecuteCommandFFmpeg.onError(e.toString());
        }

    }

    public static interface OnExecuteCommandFFmpeg extends FFmpegExecuteResponseHandler {
        public void onError(String msg);
    }

    private static String convertSecondToStringDate(int secondTime) {
        String timeToCut = "";
        int hour = secondTime / (60 * 60);
        int minute = (secondTime - hour * (60 * 60)) / 60;
        int second = (secondTime - hour * (60 * 60) - minute * 60);
        int millisecond = new Random().nextInt(10);
        timeToCut = "" + (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second) + ".00" + millisecond;
        return timeToCut;
    }

    public static String[] makeCommandExtractAllFrameVideo(String inputPathVideo, String outputPathImage, int seektime, int duration, int qualiy) {
        ArrayList<String> commands = new ArrayList<>();

        String fps = 18 / StringUtils.UCLN(18, duration) + "/" + duration / StringUtils.UCLN(18, duration);

        if (seektime > 0) {
            commands.add("-ss");
            commands.add(seektime + "");
        }
        if (duration > 0) {
            commands.add("-t");
            commands.add(duration + "");
        }
        commands.add("-i");
        commands.add(inputPathVideo);
        commands.add("-r");
        commands.add(fps);
        if (qualiy < 0 || qualiy > 10)
            qualiy = 5;
        commands.add("-qscale:v");
        commands.add(qualiy + "");
        //output
        commands.add("-y");
        commands.add(outputPathImage);
        convertCommandToString(commands.toArray(new String[0]));
        return commands.toArray(new String[0]);
    }


    public static String[] makeCommandExtractAllFrameGif(String inputPathGif, String outputPathImage, int qualiy) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("-i");
        commands.add(inputPathGif);
        commands.add("-vsync");
        commands.add("vfr");
        if (qualiy < 0 || qualiy > 10)
            qualiy = 5;
        commands.add("-qscale:v");
        commands.add(qualiy + "");
        //output
        commands.add("-y");
        commands.add(outputPathImage);
        convertCommandToString(commands.toArray(new String[0]));
        return commands.toArray(new String[0]);
    }

    public static String[] makeCommandExtractAllFrameVideo(String inputPathVideo, String outputPathImage, int seektime, int qualiy) {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("-ss");
        commands.add(convertSecondToStringDate(seektime));
        commands.add("-i");
        commands.add(inputPathVideo);
        commands.add("-vframes");
        commands.add("1");
        commands.add("-vsync");
        commands.add("vfr");
        if (qualiy < 0 || qualiy > 10)
            qualiy = 5;
        commands.add("-qscale:v");
        commands.add(qualiy + "");
        //output
        commands.add("-y");
        commands.add(outputPathImage);
        convertCommandToString(commands.toArray(new String[0]));
        return commands.toArray(new String[0]);
    }

    public static String[] makeCommandExtractOneFrameVideo(String inputPathVideo, String outputPathImage, int videoWidth, int ratioSpect, int seekSecondTime) {
        String[] command = new String[14];
        command[0] = "-ss";
        command[1] = convertSecondToStringDate(seekSecondTime);
        command[2] = "-y";
        command[3] = "-i";
        command[4] = inputPathVideo;
        command[5] = "-vframes";
        command[6] = "1";
        command[7] = "-vf";
        command[8] = "\"select=eq(pict_type\\,I)\"";
        command[9] = "-vsync";
        command[10] = "vfr";
        command[11] = "-vf";
        command[12] = "scale=" + videoWidth + "/" + ratioSpect + ":-1";
        command[13] = outputPathImage;
        return command;
    }


    public static String[] makeCommandMakeGifFromImage(String inputPath, String outputPathGifImage, int scale, double fps, String mimeTypeImage) {

        return makeCommandMakeGif(inputPath, outputPathGifImage, 0, 0, scale, fps, 0, mimeTypeImage);
    }

    private static String[] makeCommandMakeGifFromVideo(String inputPath, String outputPathGifImage, int seektime, int duration, int scale, double fps, int quality) {
        return makeCommandMakeGif(inputPath, outputPathGifImage, seektime, duration, scale, fps, quality, "png");
    }

    private static String[] makeCommandMakeGif(String inputPath, String outputPathGifImage, int seektime, int duration, int scale, double fps, int quality, String mimeTypeImage) {
        ArrayList<String> commands = new ArrayList<>();
        if (fps <= 0) {
            fps =25;
        } else {
            fps = 25 / fps;
        }
        if (scale <= 0)
            scale = 320;
        commands.add("-v");
        commands.add("warning");
        if (seektime > 0) {
            commands.add("-ss");
            commands.add("" + seektime);
        }
        if (duration > 0) {
            commands.add("-t");
            commands.add("" + duration);
        }
        commands.add("-i");
        commands.add(inputPath + "%04d." + mimeTypeImage);
        commands.add("-filter:v");
        commands.add("setpts=" + fps + "*PTS");
//        commands.add("-r");
//        commands.add("10");
        commands.add("-y");
        commands.add(outputPathGifImage);
        convertCommandToString(commands.toArray(new String[0]));
        return commands.toArray(new String[0]);
    }

}
