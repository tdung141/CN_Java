package lab05.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class PhoneDocumentFilter extends DocumentFilter {
    private static final String PHONE_PATTERN = "\\d{0,10}";

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null) return;
        String newText = ketQuaSauKhiChen(fb, offset, string, 0);
        if (newText.matches(PHONE_PATTERN)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        String newText = ketQuaSauKhiChen(fb, offset, text == null ? "" : text, length);
        if (newText.matches(PHONE_PATTERN)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private String ketQuaSauKhiChen(FilterBypass fb, int offset, String insert, int removeLength)
            throws BadLocationException {
        String current = fb.getDocument().getText(0, fb.getDocument().getLength());
        return current.substring(0, offset) + insert + current.substring(offset + removeLength);
    }
}
