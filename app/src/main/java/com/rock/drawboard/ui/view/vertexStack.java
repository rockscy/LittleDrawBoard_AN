package com.rock.drawboard.ui.view;

public class vertexStack {


	vertexDefine[] m_elements;
    int m_size = 0;
  
    public vertexStack(int len) {
        m_elements = new vertexDefine[len];
        m_size = 0;
    }

    public vertexStack() {
        this(100);
    }

    //  insert onto stack
    public void push(vertexDefine element) {
        m_elements[m_size] = element;
        m_size++;
    }

    // return and remove the top element
    public vertexDefine pop() {
        if (!this.isEmpty()) {
        	vertexDefine obj = m_elements[m_size - 1];
            m_elements[m_size - 1] = null;
            m_size--;

            return obj;
        } else {
            return null;
        }
    }

    // return the top element
    public vertexDefine top() {
        if (!this.isEmpty()) {
            return m_elements[m_size - 1];
        } else {
            return null;
        }
    }

    // return 1   --> is empty
    // return 0   --> is not empty
    public boolean isEmpty() {
        return this.getSize() == 0;
    }

    public int getSize() {
        return m_size;
    }
}
