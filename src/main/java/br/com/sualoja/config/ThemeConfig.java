package br.com.sualoja.config;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;
import java.awt.*;

/**
 * Configuração de tema FlatLaf
 */
public class ThemeConfig {

    private static LookAndFeel temaClaro, temaEscuro;

    // Cores dinâmicas baseadas no Theme
    public static Color COR_SIDEBAR = Theme.SIDEBAR_BG;
    public static Color COR_SIDEBAR_HOVER = Theme.SIDEBAR_ITEM_HOVER;
    public static Color COR_SIDEBAR_SELECTED = Theme.SIDEBAR_ITEM_SELECTED;
    public static Color COR_TEXTO_CLARO = Theme.SIDEBAR_TEXT;
    public static Color COR_BACKGROUND = Theme.BACKGROUND;
    public static Color COR_CARD = Theme.CARD_BG;
    public static Color COR_PRIMARY = Theme.PRIMARY;
    public static Color COR_SECONDARY = Theme.PRIMARY;
    public static Color COR_SUCCESS = Theme.SUCCESS;
    public static Color COR_DANGER = Theme.DANGER;
    public static Color COR_WARNING = Theme.WARNING;
    public static Color COR_INFO = Theme.INFO;
    public static Color COR_TEXTO_PRIMARY = Theme.TEXT_PRIMARY;
    public static Color COR_TEXTO_SECUNDARIO = Theme.TEXT_SECONDARY;
    public static Color COR_TEXTO_PRIMARIO_INV = Theme.TEXT_ON_PRIMARY;
    public static Color COR_BORDA = Theme.BORDER;
    public static Color COR_INPUT = new Color(249, 250, 251);
    public static Color COR_TABELA_CABECALHO = Theme.CARD_BG;
    public static Color COR_TABELA_ALT = new Color(250, 250, 250);

    private static boolean modoEscuro = false;

    public static void inicializarTema() {
        try {
            temaClaro = new FlatIntelliJLaf();
            temaEscuro = new FlatDarculaLaf();
            UIManager.setLookAndFeel(temaClaro);
        } catch (Exception e) {
            System.err.println("Erro ao carregar temas: " + e.getMessage());
        }

        // Configurações de UI
        UIManager.put("Button.toolbar.focusable", true);
        UIManager.put("Component.arc", Theme.CORNER_RADIUS);
        UIManager.put("Button.arc", Theme.CORNER_RADIUS);
        UIManager.put("TextField.arc", Theme.CORNER_RADIUS_SMALL);
        UIManager.put("ComboBox.arc", Theme.CORNER_RADIUS_SMALL);
        UIManager.put("Table.rowHeight", 40);
        UIManager.put("Table.headerHeight", 45);
    }

    public static void aplicarTemaClaro() {
        modoEscuro = false;
        try {
            if (temaClaro != null) UIManager.setLookAndFeel(temaClaro);
        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema claro: " + e.getMessage());
        }
    }

    public static void aplicarTemaEscuro() {
        modoEscuro = true;
        try {
            if (temaEscuro != null) UIManager.setLookAndFeel(temaEscuro);
        } catch (Exception e) {
            System.err.println("Erro ao aplicar tema escuro: " + e.getMessage());
        }
    }

    public static void alternarTema() {
        if (modoEscuro) {
            aplicarTemaClaro();
        } else {
            aplicarTemaEscuro();
        }
    }

    public static boolean isModoEscuro() {
        return modoEscuro;
    }

    public static String getFontFamily() {
        return Theme.FONT_FAMILY;
    }
}