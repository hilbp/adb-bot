package com.hilbp.adb.util;

import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.Coord;
import com.hilbp.adb.entity.Node;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UiAutoMatorUtil {

	//通过布局文件获取目标node
	public static List<Node> getTargetNode(Action action) {

		List<Node> nodes = new ArrayList<Node>();
		SAXReader reader = new SAXReader();

		try {

			Document document = reader.read(action.getUiSavePath());
			String xpath = action.getXpath();

			List<Element> list = document.selectNodes(xpath);
			for(Element item : list) {
				Attribute bounds = item.attribute("bounds");
				Attribute text = item.attribute("text");
				String str = UiAutoMatorUtil.textFilter(text.getStringValue());

				Node node = new Node();
				node.setXml(item.asXML());
				node.setText(str);
				node.setBounds(bounds.getStringValue());

				nodes.add(node);
			}

		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return nodes;
	}

	//通过布局文件查找点赞图标坐标
	public static List<Coord> getTargetCoord(Action action) {

		List<Coord> coords = new ArrayList<Coord>();
		SAXReader reader = new SAXReader();

		try {

			Document document = reader.read(action.getUiSavePath());
			String xpath = action.getXpath();

			List<Element> list = document.selectNodes(xpath);
			for(Element item : list) {
				Attribute bounds = item.attribute("bounds");
				Coord coord = boundsToCoord(bounds.getStringValue());
				coords.add(coord);
			}

		} catch (DocumentException e) {

			e.printStackTrace();
		}

		return coords;
	}

	//通过node获取坐标
	public static List<Coord> getCoordListFromNodes(List<Node> nodes) {
		List<Coord> coords = new ArrayList<Coord>();
		for(Node node : nodes) {
			Coord coord = boundsToCoord(node.getBounds());
			coords.add(coord);
		}
		return coords;
	}

	//通过布局文件中的bounds属性获取坐标值
	public static Coord boundsToCoord(String bounds) {

		List<Coord> coords = new ArrayList<Coord>();
		String re = "\\[([^\\]]+)\\]";
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(bounds);

		while(m.find()){
			String res = m.group(1);
			List<String> list = Arrays.asList(res.split(","));

			int x = Integer.valueOf(list.get(0));
			int y = Integer.valueOf(list.get(1));
			Coord coord = new Coord();
			coord.setX(x);
			coord.setY(y);
			coords.add(coord);
		}

		Coord res = new Coord();
		res.setX(getRandom(coords.get(1).getX() - 5, coords.get(0).getX() + 5));
		res.setY(getRandom(coords.get(1).getY() - 5, coords.get(0).getY() + 5));

		return res;
	}

	//文本处理过滤掉其中的特殊字符
	public static String textFilter(String str) {

		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}


	//构造数据
	public static <T1, T2> void setEntryData(T1 source, T2 target) {

		Field[] targetFields = target.getClass().getDeclaredFields();

		for(Field targetField : targetFields) {

			try {
				Field sourceField = source.getClass().getDeclaredField(targetField.getName());
				sourceField.setAccessible(true);

				targetField.setAccessible(true);
				targetField.set(target, sourceField.get(source));

			} catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException e) {

				e.printStackTrace();
			}

		}

	}

	//在坐标start与end之间产生随机数
	public static int getRandom(int max, int min) {

		Random random = new Random();
	    int x = random.nextInt(max) % (max - min + 1) + min;
	    return x;
	}
}
