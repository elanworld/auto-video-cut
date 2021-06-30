/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.video;

import org.junit.Test;

public class MovieGenTest {

	@Test
	public void genFromText() {
		new MovieGen().genFromText(
				"中新网客户端北京6月28日电(记者 袁秀月)“到那时，中国的面貌将会被我们改造一新。到那时，到处都是活跃的创造，到处都是日新月异的进步……这时，我们民族就可以无愧色地立在人类的面前……”\n" + "\n"
						+ "86年前，身陷囹圄的方志敏曾在《可爱的中国》中如此描绘他对于未来的期冀。\n" + "\n" + "86年后，方志敏的曾孙女方铭璐跨越时空回应了这一期盼。\n" + "\n"
						+ "她说：“太爷爷，如果真能回到梦里，再次见到您，我一定会大声地告诉您，您在就义前的狱中预见的一切，在新中国、新时代都更好地实现了，您和无数烈士们牺牲时的理想终达所愿。这盛世，可爱的中国，如您所愿。”\n"
						+ "\n"
						+ "近日，中国正能量2021“五个一百”网络精品征集评选展播活动启动。此次评选活动以“奋斗的人民 奋进的中国”为主题，聚焦庆祝中国共产党成立100周年、脱贫攻坚、抗疫成果、全面建成小康社会等，征集评选展播百名网络正能量榜样、百篇网络正能量文字、百幅网络正能量图片、百部网络正能量动漫音视频作品和百项网络正能量专题活动。\n"
						+ "\n" + "评选活动走过五届，数千个作品和榜样带我们见证了可爱中国的发展历程，同样带我们见证了中国正能量的流动轨迹。\n" + "\n"
						+ "不管是沙场阅兵、港珠澳大桥、新中国成立70周年、抗击疫情背后的故事，还是老将军、老党员、烈士的英雄事迹，亦或是普通人的闪光时刻，都传递着奋斗的热情，带给我们向上向善的力量。\n"
						+ "\n"
						+ "可爱的中国，有着一群可爱的人。很多网红人物，也传递着满满的正能量，比如“马背县长”贺娇龙、李子柒、司徒建国等等。在抗击疫情期间，大连海洋大学“抗疫青年突击队”曾冒着风雪为师生运送物资。启动仪式上，他们脱下防护服，面带羞涩的笑容，这才显露出00后本来的模样。\n"
						+ "\n"
						+ "和这群00后同台的，是一群“80后”的爷爷奶奶，他们是平均年龄74岁的“清华学霸合唱团”。他们中的很多人，都将青春岁月留在了祖国边疆。“80后”和00后合唱一首《少年》，唱出了老一辈的理想和信仰，也唱出了青年一代的责任和担当。\n"
						+ "\n" + "这正是正能量的传承。如今，正能量有了更多的含义。它来自过往的历史，也来自年轻人炽热的心中；它来自山间和小溪，也来自高楼与大厦；它来自传统，也来自融合。\n" + "\n"
						+ "它是对青春的不辜负，是对自己生长的土地的热爱，是对工作的认真对待，是不惧辛劳、抵抗诱惑。这样的中国正能量，为奋斗的人们提供着不竭的精神力量。\n" + "\n"
						+ "“要奋斗！要持久地艰苦地奋斗！”我们不能忘记86年前方志敏的那句话，为未达成的事业奋斗，为想要的生活奋斗，为美好的未来奋斗。\n" + "\n"
						+ "没有一座高山不可逾越，只有奋斗才能到达彼岸。“理想信念之火一经点燃，就永远不会熄灭。”我们都在努力奔跑，在追梦的道路上高歌奋进。(完)",
				"out.mp4");
	}
}
