package com.smartmind.biz.uitl;

import java.util.ArrayList;
import java.util.List;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class LeangingUnitUtil {
	/**
	 * 统计Markdown中的标题数量
	 */
	public static int countHeadings(String markdown) {
		int count = 0;
		for (String line : markdown.split("\n")) {
			String trimLine = line.trim();
			if (trimLine.startsWith("#") && !trimLine.startsWith("####")) {
				count++;
			}
		}
		return count;
	}
	/**
	 * 标题节点内部类
	 */
	private static class HeadingNode {
		final int level;
		final String text;
		
		HeadingNode(int level, String text) {
			this.level = level;
			this.text = text;
		}
	}
	
	/**
	 * 解析Markdown提取标题
	 */
	private List<HeadingNode> parseMarkdown(String markdown) {
		MutableDataSet options = new MutableDataSet();
		Parser parser = Parser.builder(options).build();
		Node document = parser.parse(markdown);
		
		List<HeadingNode> headings = new ArrayList<>();
		Node node = document.getFirstChild();
		
		while (node != null) {
			if (node instanceof Heading) {
				Heading heading = (Heading) node;
				String text = heading.getText().toString().trim();
				int level = heading.getLevel();
				
				// 只处理1-4级标题
				if (level >= 1 && level <= 4 && !text.isEmpty()) {
					headings.add(new HeadingNode(level, text));
				}
			}
			node = node.getNext();
		}
		
		return headings;
	}
}
