package br.com.sualoja.config;

import javax.swing.*;
import java.awt.*;

/**
 * Sistema de tema centralizado - Design SaaS Moderno
 */
public class Theme {

    // ============================================
    // CORES PRINCIPAIS
    // ============================================
    public static final Color PRIMARY = new Color(37, 99, 235);      // #2563EB
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);
    public static final Color SUCCESS = new Color(34, 197, 94);      // #22C55E
    public static final Color DANGER = new Color(239, 68, 68);      // #EF4444
    public static final Color WARNING = new Color(245, 158, 11);    // #F59E0B
    public static final Color INFO = new Color(6, 182, 212);        // #06B6D4

    // ============================================
    // CORES DE FUNDO
    // ============================================
    public static final Color BACKGROUND = new Color(245, 247, 251); // #F5F7FB
    public static final Color CARD_BG = Color.WHITE;                 // #FFFFFF
    public static final Color SIDEBAR_BG = new Color(17, 24, 39);    // #111827

    // ============================================
    // CORES DE TEXTO
    // ============================================
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);  // #111827
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128); // #6B7280
    public static final Color TEXT_LIGHT = new Color(156, 163, 175);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;

    // ============================================
    // CORES DE BORDA
    // ============================================
    public static final Color BORDER = new Color(229, 231, 235);    // #E5E7EB
    public static final Color BORDER_FOCUS = PRIMARY;

    // ============================================
    // CORES DA SIDEBAR
    // ============================================
    public static final Color SIDEBAR_ITEM_HOVER = new Color(31, 41, 55);
    public static final Color SIDEBAR_ITEM_SELECTED = new Color(55, 65, 81);
    public static final Color SIDEBAR_TEXT = new Color(209, 213, 219);
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;
    public static final Color TRANSPARENT_WHITE = new Color(255, 255, 255, 180);

    // ============================================
    // TIPOGRAFIA
    // ============================================
    public static final String FONT_FAMILY = "Segoe UI";

    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font FONT_BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BODY_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_LABEL = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font(FONT_FAMILY, Font.BOLD, 14);

    // ============================================
    // ESPACAMENTO E TAMANHOS
    // ============================================
    public static final int CORNER_RADIUS = 8;
    public static final int CORNER_RADIUS_SMALL = 6;
    public static final int CORNER_RADIUS_LARGE = 12;

    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int PADDING_XLARGE = 32;

    public static final int SIDEBAR_WIDTH_OPEN = 220;
    public static final int SIDEBAR_WIDTH_CLOSED = 72;

    // ============================================
    // SOMBRA DOS CARDS
    // ============================================
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 20);
    public static final int SHADOW_OFFSET_Y = 2;

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    public static Color transparent(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }
}