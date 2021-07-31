/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.text;

import java.util.ArrayList;
import java.util.List;

import com.alan.common.data.main.entity.SubtitleVo;
import com.alan.auto.service.DataQueryService;

/**
 * @Description: 实现功能
 * @Author: Alan
 * @Date: 2021/6/30
 */
public class SubtitleService {

	/**
	 * 查询文章最合适的字幕
	 *
	 * @param text
	 * @return
	 */
	public List<SubtitleVo> queryLikeSubtle(List<String> text) {
		List<SubtitleVo> subtitleList = new ArrayList<>();
		DataQueryService queryService = new DataQueryService();
		text.forEach(word -> {
			List<SubtitleVo> subtitles = queryService.queryLikeSubtitle(word);
			if (!subtitles.isEmpty()) {
				subtitles.get(0).setOrigin(word);
				subtitleList.add(subtitles.get(0));
			}
		});
		return subtitleList;
	}
}
