package br.com.sualoja.util;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Utilitários centralizados de formatação
 */
public class FormatUtils {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");
    private static final NumberFormat FORMATO_MOEDA = NumberFormat.getCurrencyInstance(BRASIL);
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Formata valor monetário (R$)
     */
    public static String formatarMoeda(java.math.BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return FORMATO_MOEDA.format(valor);
    }

    /**
     * Formata data
     */
    public static String formatarData(LocalDateTime data) {
        if (data == null) return "-";
        return data.format(FORMATO_DATA);
    }

    /**
     * Formata data e hora
     */
    public static String formatarDataHora(LocalDateTime data) {
        if (data == null) return "-";
        return data.format(FORMATO_DATA_HORA);
    }

    /**
     * Parse de moeda brasileira para BigDecimal
     */
    public static java.math.BigDecimal parseMoeda(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return java.math.BigDecimal.ZERO;
        }
        try {
            // Remove R$, espaços e substitui vírgula por ponto
            String limpo = texto.replace("R$", "")
                              .replace(" ", "")
                              .replace(".", "")
                              .replace(",", ".");
            return new java.math.BigDecimal(limpo);
        } catch (NumberFormatException e) {
            return java.math.BigDecimal.ZERO;
        }
    }

    /**
     * Formata telefone
     */
    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.isEmpty()) return "-";
        telefone = telefone.replaceAll("\\D", "");
        if (telefone.length() == 10) {
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 6),
                telefone.substring(6, 10));
        } else if (telefone.length() == 11) {
            return String.format("(%s) %s-%s",
                telefone.substring(0, 2),
                telefone.substring(2, 7),
                telefone.substring(7, 11));
        }
        return telefone;
    }

    /**
     * Formata CPF
     */
    public static String formatarCPF(String cpf) {
        if (cpf == null || cpf.isEmpty()) return "-";
        cpf = cpf.replaceAll("\\D", "");
        if (cpf.length() == 11) {
            return String.format("%s.%s.%s-%s",
                cpf.substring(0, 3),
                cpf.substring(3, 6),
                cpf.substring(6, 9),
                cpf.substring(9, 11));
        }
        return cpf;
    }
}