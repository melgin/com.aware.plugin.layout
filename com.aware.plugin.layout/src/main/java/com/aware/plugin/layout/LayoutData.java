package com.aware.plugin.layout;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class LayoutData {

    private String className;
    private String text;
    private String content;
    private LayoutRect rectP;
    private LayoutRect rectS;
    private List<LayoutData> children;
    private List<Integer> actions;
    private boolean enabled;

    public LayoutData() {

    }

    public LayoutData(AccessibilityNodeInfo node) {
        this.className = node.getClassName() == null ? null : node.getClassName().toString().replace("android.widget", "a.w");
        this.content = node.getContentDescription() == null ? null : node.getContentDescription().toString();

        if(! "android.widget.TextView".equals(node.getText())) {
            this.text = node.getText() == null ? null : node.getText().toString();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(this.text == null || "".equals(this.text)) {
                this.text = node.getHintText() == null ? null : node.getHintText().toString();
            }

            this.actions = new ArrayList<>();
            for(AccessibilityNodeInfo.AccessibilityAction action : node.getActionList()) {
                this.actions.add(action.getId());
            }
        }

        Rect screenRect = new Rect();
        node.getBoundsInScreen(screenRect);
        this.rectS = new LayoutRect();
        this.rectS.setT(screenRect.top);
        this.rectS.setL(screenRect.left);
        this.rectS.setB(screenRect.bottom);
        this.rectS.setR(screenRect.right);

        Rect parentRect = new Rect();
        node.getBoundsInScreen(parentRect);
        this.rectP = new LayoutRect();
        this.rectP.setT(parentRect.top);
        this.rectP.setL(parentRect.left);
        this.rectP.setB(parentRect.bottom);
        this.rectP.setR(parentRect.right);

        this.enabled = node.isEnabled();

        if(node.getChildCount() > 0){
            this.children = new ArrayList<>();

            for(int i = 0; i < node.getChildCount(); i++) {
                AccessibilityNodeInfo child = node.getChild(i);

                if(child != null) {
                    this.children.add(new LayoutData(node.getChild(i)));
                }
            }
        }
    }

    public String getClassName() {
        return className;
    }

    public String getText() {
        return text;
    }

    public List<LayoutData> getChildren() {
        return children;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChildren(List<LayoutData> children) {
        this.children = children;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LayoutRect getRectP() {
        return rectP;
    }

    public void setRectP(LayoutRect rectP) {
        this.rectP = rectP;
    }

    public LayoutRect getRectS() {
        return rectS;
    }

    public void setRectS(LayoutRect rectS) {
        this.rectS = rectS;
    }

    public List<Integer> getActions() {
        return actions;
    }

    public void setActions(List<Integer> actions) {
        this.actions = actions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class LayoutRect {
        private int t;
        private int b;
        private int l;
        private int r;

        public int getT() {
            return t;
        }

        public void setT(int t) {
            this.t = t;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

        public int getR() {
            return r;
        }

        public void setR(int r) {
            this.r = r;
        }
    }
}
