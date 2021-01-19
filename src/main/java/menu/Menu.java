package menu;

import burp.*;
import common.PrintUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Menu implements IContextMenuFactory {

    private IBurpExtenderCallbacks callbacks;

    public Menu(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList<>();
        JMenu xffMenu = new JMenu("XFF Helper");
        JMenuItem doAdd = new JMenuItem("Add XFF");
        JMenuItem config = new JMenuItem("Config");
        xffMenu.add(doAdd);
        xffMenu.addSeparator();
        xffMenu.add(config);

        if (invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST) {
            doAdd.setEnabled(false);
        }

        doAdd.addActionListener(e -> {
            String[] xffList = new String[]{
                    "X-Forwarded-For",
                    "X-Forwarded-Host",
                    "X-Remote-IP",
                    "X-Originating-IP",
                    "X-Remote-Addr",
                    "True-Client-IP",
                    "Client-IP",
                    "X-Client-IP",
                    "X-Real-IP"};
            IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
            byte[] request = iReqResp.getRequest();
            IRequestInfo requestInfo = this.callbacks.getHelpers().analyzeRequest(request);
            int bodyOffset = requestInfo.getBodyOffset();
            int body_length = request.length - bodyOffset;
            String body = new String(request, bodyOffset, body_length, StandardCharsets.UTF_8);
            List<String> headers = this.callbacks.getHelpers().analyzeRequest(request).getHeaders();
            for (String xff : xffList) {
                for (String header : headers) {
                    if(header.toLowerCase().contains(xff.toLowerCase())){
                        headers.remove(header);
                        break;
                    }
                }
                headers.add(xff + ": 127.0.0.1");
            }
            request = this.callbacks.getHelpers().buildHttpMessage(headers, body.getBytes(StandardCharsets.UTF_8));
            iReqResp.setRequest(request);
        });

        menus.add(xffMenu);
        return menus;
    }
}