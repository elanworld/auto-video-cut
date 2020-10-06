# auto-video-cut
auto cut video to a clip what save climax of oringin vidweo file
# 介绍
- 智能选取分割视频场景，合成目标视频
1. 自动化，节省大量的手工剪辑
2. 加入基础AI算法，使剪辑越来越智能，越来越符合目标参数
3. 识别切割场景
4. 可选用javacv opencv ffmpeg任意一种
# 使用指南
1. 安装[ffmpeg](https://ffmpeg.org) ，并配置环境变量。
2. maven 载入最新Javacv 和 opencv 库

#更新
1.处理任务变复杂，处理时间减慢未视频长度5倍（忽略：电脑处理，视性能而定）
2.MainSpeakClipper：录音场景下说话片段识别选取。
3.MainBulletVideo:更具数据库中弹幕资源筛选剪切合成视频。
4.MainAudioRing:铃声高潮批量生成。
5.MainSilence:sox ai去除背景噪音。
