package sena.adso.saludboyaca.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import jakarta.servlet.http.HttpServletResponse;
import sena.adso.saludboyaca.dto.Cita;

import java.awt.Color;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class PDFGenerator {

    public static void generarComprobante(HttpServletResponse response, Cita cita, String lang)
            throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("messages", new Locale(lang));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=comprobante-cita-" + cita.getId() + ".pdf");

        Document doc = new Document(PageSize.A4, 50, 50, 60, 50);
        try {
            PdfWriter.getInstance(doc, response.getOutputStream());
            doc.open();

            // ── Encabezado ────────────────────────────────────────
            Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(26, 82, 118));
            Font fSub = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(44, 62, 80));
            Font fLabel = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(26, 82, 118));
            Font fValue = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(44, 62, 80));
            Font fFooter = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(127, 140, 141));

            Paragraph titulo = new Paragraph("SaludBoyacá", fTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Paragraph inst = new Paragraph(rb.getString("app.institucion"), fSub);
            inst.setAlignment(Element.ALIGN_CENTER);
            inst.setSpacingAfter(5);
            doc.add(inst);

            // Línea separadora
            LineSeparator sep = new LineSeparator(1f, 100, new Color(26, 82, 118), Element.ALIGN_CENTER, -2);
            doc.add(new Chunk(sep));

            Paragraph tituloComp = new Paragraph("\nCOMPROBANTE DE CITA MÉDICA", fTitulo);
            tituloComp.setAlignment(Element.ALIGN_CENTER);
            tituloComp.setSpacingAfter(15);
            doc.add(tituloComp);

            // ── Tabla de datos ──────────────────────────────────
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(90);
            tabla.setSpacingBefore(10);
            tabla.setSpacingAfter(20);
            tabla.setWidths(new float[]{35f, 65f});

            addRow(tabla, rb.getString("cita.paciente") + ":", cita.getNombrePaciente(), fLabel, fValue);
            addRow(tabla, rb.getString("paciente.documento") + ":", cita.getDocumentoPaciente(), fLabel, fValue);
            addRow(tabla, rb.getString("cita.medico") + ":", "Dr(a). " + cita.getNombreMedico(), fLabel, fValue);
            addRow(tabla, rb.getString("cita.especialidad") + ":", cita.getNombreEspecialidad(), fLabel, fValue);
            addRow(tabla, rb.getString("cita.fecha") + ":", cita.getFechaCita().toString(), fLabel, fValue);
            addRow(tabla, rb.getString("cita.hora") + ":", cita.getHoraCita().toString().substring(0, 5), fLabel, fValue);
            addRow(tabla, rb.getString("cita.estado") + ":", traducirEstado(cita.getEstado(), rb), fLabel, fValue);
            if (cita.getMotivo() != null && !cita.getMotivo().isEmpty()) {
                addRow(tabla, rb.getString("cita.motivo") + ":", cita.getMotivo(), fLabel, fValue);
            }
            doc.add(tabla);

            // ── Nota al pie ──────────────────────────────────────
            Paragraph nota = new Paragraph(
                    "Presente este comprobante el día de su cita. ID: #" + cita.getId(), fFooter);
            nota.setAlignment(Element.ALIGN_CENTER);
            doc.add(nota);

            Paragraph footer = new Paragraph("\n" + rb.getString("app.footer"), fFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

        } catch (DocumentException e) {
            throw new IOException("Error generando PDF: " + e.getMessage(), e);
        } finally {
            doc.close();
        }
    }

    private static void addRow(PdfPTable t, String label, String value, Font fLabel, Font fValue) {
        PdfPCell cLabel = new PdfPCell(new Phrase(label, fLabel));
        cLabel.setBorder(Rectangle.BOTTOM);
        cLabel.setPadding(6);
        cLabel.setBackgroundColor(new Color(214, 234, 248));

        PdfPCell cValue = new PdfPCell(new Phrase(value != null ? value : "-", fValue));
        cValue.setBorder(Rectangle.BOTTOM);
        cValue.setPadding(6);

        t.addCell(cLabel);
        t.addCell(cValue);
    }

    private static String traducirEstado(String estado, ResourceBundle rb) {
        if (estado == null) {
            return "-";
        }
        switch (estado) {
            case "PROGRAMADA":
                return rb.getString("cita.estado.programada");
            case "CONFIRMADA":
                return rb.getString("cita.estado.confirmada");
            case "ATENDIDA":
                return rb.getString("cita.estado.atendida");
            case "CANCELADA":
                return rb.getString("cita.estado.cancelada");
            default:
                return estado;
        }
    }
}
