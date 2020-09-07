package com.alan.video;

/**
 * special format case
 */
public class FFmpegCmdCase extends FFmpegCmd {

    public class FiltersSet extends FFmpegCmd.FiltersSet {
        /**
         * mix two audio to one,request main audio is at second
         * @return
         */
        public FiltersSet setAudioMixLoop() {
            filters.add(String.format("aloop=loop=3:size=2e+09,amix=inputs=%s:duration=first:dropout_transition=2", inputFiles.size()));
            return this;
        }
    }

    public FFmpegCmdCase baiduAipPCM() {
        String pcm = "-acodec pcm_s16le -f s16le -ac 1 -ar 16000";
        cmdMap.replace("codec", pcm);
        return this;
    }

}
