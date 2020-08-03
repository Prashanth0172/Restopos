package com.hyva.restopos.util;

public class POSPrinterBuilder {

    String commandSet = "";

    public String initialize() {
        final byte[] Init = {27, 64};
        commandSet += new String(Init);
        return new String(Init);
    }

    public String chooseFont(int Options) {
        String s = "";
        final byte[] ChooseFontA = {27, 33, 10};
        final byte[] ChooseFontB = {27, 77, 1};
        final byte[] ChooseFontC = {27, 77, 48};
        final byte[] ChooseFontD = {27, 77, 49};

        final byte[] normalSize = {0x1B, 0x21, 0x03};
        final byte[] bold = {0x1B, 0x21, 0x08};
        final byte[] boldMedium = {0x1B, 0x21, 0x20};
        final byte[] boldLarge = {0x1B, 0x21, 0x10};

        final byte[] mediumLarge = {27,33,16};
        final byte[] xtraLarge = {27,33,48};
        final byte[] mediumLargeWithSpacing = {27,33,16};


        switch (Options) {
            case 1:
                s = new String(normalSize);
                break;

            case 2:
                s = new String(bold);
                break;

            case 3:
                s = new String(boldMedium);
                break;

            case 4:
                s = new String(boldLarge);
                break;

            case 5:
                s = new String(ChooseFontA);
                break;

            case 6:
                s = new String(ChooseFontB);
                break;

            case 7:
                s = new String(ChooseFontC);
                break;

            case 8:
                s = new String(ChooseFontD);
                break;

            case 9:
                s = new String(mediumLarge);
                break;

            case 10:
                s = new String(xtraLarge);
                break;

            case 11:
                s = new String(mediumLargeWithSpacing);
                break;

            default:
                s = new String(normalSize);
        }
        commandSet += s;
        return new String(s);
    }

    public String feedBack(byte lines) {
        final byte[] Feed = {27, 101, lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String feed(byte lines) {
        final byte[] Feed = {27, 100, lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String alignLeft() {
        final byte[] AlignLeft = {27, 97, 48};
        String s = new String(AlignLeft);
        commandSet += s;
        return s;
    }

    public String alignCenter() {
        final byte[] AlignCenter = {27, 97, 49};
        String s = new String(AlignCenter);
        commandSet += s;
        return s;
    }

    public String alignRight() {
        final byte[] AlignRight = {27, 97, 50};
        String s = new String(AlignRight);
        commandSet += s;
        return s;
    }

    public String newLine() {
        final byte[] LF = {10};
        String s = new String(LF);
        commandSet += s;
        return s;
    }

    public String reverseColorMode(boolean enabled) {
        final byte[] ReverseModeColorOn = {29, 66, 1};
        final byte[] ReverseModeColorOff = {29, 66, 0};

        String s = "";
        if (enabled)
            s = new String(ReverseModeColorOn);
        else
            s = new String(ReverseModeColorOff);

        commandSet += s;
        return s;
    }

    public String doubleStrik(boolean enabled) {
        final byte[] DoubleStrikeModeOn = {27, 71, 1};
        final byte[] DoubleStrikeModeOff = {27, 71, 0};

        String s = "";
        if (enabled)
            s = new String(DoubleStrikeModeOn);
        else
            s = new String(DoubleStrikeModeOff);

        commandSet += s;
        return s;
    }

    public String doubleHeight(boolean enabled) {
        final byte[] DoubleHeight = {27, 33, 17};
        final byte[] UnDoubleHeight = {27, 33, 0};

        String s = "";
        if (enabled)
            s = new String(DoubleHeight);
        else
            s = new String(UnDoubleHeight);

        commandSet += s;
        return s;
    }

    public String emphasized(boolean enabled) {
        final byte[] EmphasizedOff = {27, 0};
        final byte[] EmphasizedOn = {27, 1};

        String s = "";
        if (enabled)
            s = new String(EmphasizedOn);
        else
            s = new String(EmphasizedOff);

        commandSet += s;
        return s;
    }

    public String underLine(int Options) {
        final byte[] UnderLine2Dot = {27, 45, 50};
        final byte[] UnderLine1Dot = {27, 45, 49};
        final byte[] NoUnderLine = {27, 45, 48};

        String s = "";
        switch (Options) {
            case 0:
                s = new String(NoUnderLine);
                break;

            case 1:
                s = new String(UnderLine1Dot);
                break;

            default:
                s = new String(UnderLine2Dot);
        }
        commandSet += s;
        return new String(s);
    }

    public String color(int Options) {
        final byte[] ColorRed = {27, 114, 49};
        final byte[] ColorBlack = {27, 114, 48};

        String s = "";
        switch (Options) {
            case 0:
                s = new String(ColorBlack);
                break;

            case 1:
                s = new String(ColorRed);
                break;

            default:
                s = new String(ColorBlack);
        }
        commandSet += s;
        return s;
    }

    public String finit() {
        final byte[] FeedAndCut = {29, 'V', 66, 0};

        String s = new String(FeedAndCut);

        final byte[] DrawerKick = {27, 70, 0, 60, 120};
        s += new String(DrawerKick);

        commandSet += s;
        return s;
    }

    public String addNormalSizeLineSeperator() {
        String lineSpace = "----------------------------------------------------------------";
        commandSet += lineSpace;
        return lineSpace;
    }

    public String addBoldLargeLineSeperator() {
        String lineSpace = "------------------------------------------------";
        commandSet += lineSpace;
        return lineSpace;
    }

    public void resetAll() {
        commandSet = "";
    }

    public void setText(String s) {
        commandSet += s;
    }

    public String finalCommandSet() {
        return commandSet;
    }
}