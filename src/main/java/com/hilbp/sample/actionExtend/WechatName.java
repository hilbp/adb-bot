package com.hilbp.sample.actionExtend;

import com.hilbp.adb.action.type.Impl.Custom;
import com.hilbp.adb.action.type.Impl.SetTextToInput;
import com.hilbp.adb.entity.Action;
import com.hilbp.adb.entity.Node;
import com.hilbp.adb.state.RunningStatus;
import com.hilbp.adb.state.SaveActionState;
import com.hilbp.adb.util.FileSaveUtil;
import com.hilbp.adb.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vidstige.jadb.JadbDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
@Slf4j
public class WechatName implements SetTextToInput.TextSource, Custom.CustomOperate {

    private List<String> wechats = getData();

    private static int index = 0;

    @Autowired
    private SaveActionState saveActionState;

    @Override
    public String get(JadbDevice device) {

        if(index >= wechats.size()) {
            log.info("over!");
            RunningStatus.setFinished(device);
            System.exit(0);
        }
        return wechats.get(index++);
    }

    @Override
    public String get(JadbDevice device, String input) {
        return null;
    }

    @Override
    public void operate(JadbDevice device, Action action) {

        String actionStateName = action.getActionStateName();
        List<Node> nodes = saveActionState.getStateData(device, actionStateName);

        nodes.forEach(item -> {
            try {
                Document document = DocumentHelper.parseText(item.getXml());
                StringJoiner res = new StringJoiner(",");
                this.getNodeText(document.getRootElement(), res);
                log.info("wechat: {}", wechats.get(index - 1));
                log.info("text: {}", res.toString());
//                log.info("text1: {}", item.getXml().replaceAll("[^0-9\u4E00-\u9FA5]",""));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });

    }

    public List<String> getData() {

        String filePath = "E:/capturedata/shanxi_company/wechat.txt";
        List<String> data = new ArrayList<>();
        List<String> lines = null;
        try {
            lines = FileSaveUtil.fileToList(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.forEach(item -> {

            String[] temp = item.split(":");
            data.add(temp[2]);
        });

        return data;
    }

    public void getNodeText(Element element, StringJoiner res) {

        String text = element.attribute("text").getText();
        if(StringUtil.isNotEmpty(text)) {
            res.add(text);
        }
        List<Element> elements = element.elements();
        elements.forEach(item -> {
            getNodeText(item, res);
        });
    }
}
