package com.caxerx.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class IfTag extends SimpleTagSupport {
    private boolean condition;

    @Override
    public void doTag() throws JspException, IOException {
        if (condition) {
            try {
                getJspBody().invoke(null);
            } catch (Exception e) {
                getJspContext().getOut().print("error on j:if tag");
            }
        }
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }
}
