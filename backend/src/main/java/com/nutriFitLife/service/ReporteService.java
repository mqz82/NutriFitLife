package com.nutriFitLife.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.nutriFitLife.dto.ResultadoAntropometricoDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Servicio que genera el informe antropométrico en formato PDF.
 * Utiliza iText 7 para la estructura del documento y JFreeChart
 * para generar los gráficos como imágenes PNG incrustadas.
 */
@Service
public class ReporteService {

    private static final DeviceRgb COLOR_PRIMARIO   = new DeviceRgb(0x00, 0x7B, 0xFF);
    private static final DeviceRgb COLOR_SECUNDARIO = new DeviceRgb(0x28, 0xA7, 0x45);
    private static final DeviceRgb COLOR_FONDO      = new DeviceRgb(0xF8, 0xF9, 0xFA);
    private static final DeviceRgb COLOR_TEXTO      = new DeviceRgb(0x21, 0x25, 0x29);
    private static final DeviceRgb COLOR_GRASA      = new DeviceRgb(0xFF, 0x88, 0x00);
    private static final DeviceRgb COLOR_MAGRA      = new DeviceRgb(0x00, 0x7B, 0xFF);

    /**
     * Genera el PDF completo del informe antropométrico y lo retorna
     * como arreglo de bytes para enviarlo directamente en la respuesta HTTP.
     *
     * @param resultado DTO con todos los valores calculados
     * @return bytes del archivo PDF
     * @throws IOException si ocurre un error al generar el documento
     */
    public byte[] generarPDF(ResultadoAntropometricoDTO resultado) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer     = new PdfWriter(baos);
        PdfDocument pdfDoc   = new PdfDocument(writer);
        Document    document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(20, 30, 20, 30);

        // Fuente base
        PdfFont fontBase   = PdfFontFactory.createFont("Helvetica");
        PdfFont fontNegrita = PdfFontFactory.createFont("Helvetica-Bold");

        // =====================================================================
        // PÁGINA 1
        // =====================================================================
        agregarEncabezado(document, resultado, fontNegrita, fontBase);
        agregarSeccion(document, "FRACCIONAMIENTO 2 MASAS", fontNegrita);
        agregarGraficoPie(document, resultado);
        agregarBarrasGrasaMagra(document, resultado, fontBase);
        agregarDatosIMCPeso(document, resultado, fontNegrita, fontBase);

        // =====================================================================
        // PÁGINA 2
        // =====================================================================
        document.add(new AreaBreak());
        agregarEncabezadoPagina2(document, resultado, fontNegrita, fontBase);
        agregarSumaPliegues(document, resultado, fontNegrita, fontBase);
        agregarTablaMediciones(document, resultado, fontNegrita, fontBase);
        agregarPerfilPliegues(document, resultado, fontNegrita);
        agregarSomatocarta(document, resultado, fontNegrita);
        agregarPerimetros(document, resultado, fontNegrita);

        document.close();
        return baos.toByteArray();
    }

    // -------------------------------------------------------------------------
    // Secciones del PDF
    // -------------------------------------------------------------------------

    private void agregarEncabezado(Document doc, ResultadoAntropometricoDTO r,
                                   PdfFont bold, PdfFont regular) {
        Table header = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(COLOR_PRIMARIO)
                .setMarginBottom(10);

        Cell logoCell = new Cell().setBorder(null).setPadding(8);
        logoCell.add(new Paragraph("NutriFitLife")
                .setFont(bold).setFontSize(18)
                .setFontColor(ColorConstants.WHITE));
        logoCell.add(new Paragraph("Sistema de Evaluación Antropométrica")
                .setFont(regular).setFontSize(9)
                .setFontColor(ColorConstants.WHITE));
        header.addCell(logoCell);

        Cell infoCell = new Cell().setBorder(null).setPadding(8)
                .setTextAlignment(TextAlignment.RIGHT);
        infoCell.add(new Paragraph("INFORME ANTROPOMÉTRICO")
                .setFont(bold).setFontSize(14)
                .setFontColor(ColorConstants.WHITE));
        infoCell.add(new Paragraph("Paciente: " + r.getNombreCompleto())
                .setFont(regular).setFontSize(10)
                .setFontColor(ColorConstants.WHITE));
        infoCell.add(new Paragraph(
                String.format("Edad: %.1f años  |  Sexo: %s  |  Fecha: %s",
                        r.getEdadDecimal(), r.getSexo().equals("M") ? "Masculino" : "Femenino",
                        r.getFechaMedicion()))
                .setFont(regular).setFontSize(9)
                .setFontColor(ColorConstants.WHITE));
        header.addCell(infoCell);

        doc.add(header);
    }

    private void agregarEncabezadoPagina2(Document doc, ResultadoAntropometricoDTO r,
                                          PdfFont bold, PdfFont regular) {
        Paragraph titulo = new Paragraph("INFORME ANTROPOMÉTRICO — " + r.getNombreCompleto()
                + " — Página 2")
                .setFont(bold).setFontSize(11)
                .setFontColor(COLOR_PRIMARIO)
                .setMarginBottom(8);
        doc.add(titulo);
    }

    private void agregarSeccion(Document doc, String titulo, PdfFont bold) {
        doc.add(new Paragraph(titulo)
                .setFont(bold).setFontSize(11)
                .setFontColor(COLOR_PRIMARIO)
                .setMarginTop(8).setMarginBottom(4));
    }

    private void agregarGraficoPie(Document doc, ResultadoAntropometricoDTO r) throws IOException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("% Masa Grasa (" + r.getPorcentajeGrasa() + "%)", r.getPorcentajeGrasa());
        dataset.setValue("% Masa Magra (" + r.getPorcentajeMasaMagra() + "%)", r.getPorcentajeMasaMagra());

        JFreeChart chart = ChartFactory.createPieChart("", dataset, true, false, false);
        PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setSectionPaint("% Masa Grasa (" + r.getPorcentajeGrasa() + "%)",
                new Color(0xFF, 0x88, 0x00));
        plot.setSectionPaint("% Masa Magra (" + r.getPorcentajeMasaMagra() + "%)",
                new Color(0x00, 0x7B, 0xFF));

        doc.add(crearImagenGrafico(chart, 280, 200));
    }

    private void agregarBarrasGrasaMagra(Document doc, ResultadoAntropometricoDTO r,
                                         PdfFont regular) throws IOException {
        // Barras % (porcentaje)
        DefaultCategoryDataset dsPct = new DefaultCategoryDataset();
        dsPct.addValue(r.getPorcentajeGrasa(),    "Masa Grasa",   "% Grasa");
        dsPct.addValue(r.getPorcentajeMasaMagra(), "Masa Magra",   "% Magra");

        JFreeChart chartPct = crearGraficoBarras("M. GRASA Y M. MAGRA (%)", dsPct,
                new Color[]{new Color(0xFF, 0x88, 0x00), new Color(0x00, 0x7B, 0xFF)});

        // Barras kg
        DefaultCategoryDataset dsKg = new DefaultCategoryDataset();
        dsKg.addValue(r.getMasaGrasaKg(), "Masa Grasa", "Grasa (kg)");
        dsKg.addValue(r.getMasaMagraKg(), "Masa Magra", "Magra (kg)");

        JFreeChart chartKg = crearGraficoBarras("M. GRASA Y M. MAGRA (kg)", dsKg,
                new Color[]{new Color(0xFF, 0x88, 0x00), new Color(0x00, 0x7B, 0xFF)});

        Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        t.addCell(new Cell().setBorder(null).add(crearImagenGrafico(chartPct, 240, 180)));
        t.addCell(new Cell().setBorder(null).add(crearImagenGrafico(chartKg,  240, 180)));
        doc.add(t);
    }

    private void agregarDatosIMCPeso(Document doc, ResultadoAntropometricoDTO r,
                                     PdfFont bold, PdfFont regular) {
        Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(6);

        t.addCell(crearCeldaDato("MASA CORPORAL", r.getPeso() + " kg", bold, regular));
        t.addCell(crearCeldaDato("ÍNDICE DE MASA CORPORAL",
                String.format("%.1f — %s", r.getImc(), r.getClasificacionIMC()),
                bold, regular));
        doc.add(t);
    }

    private void agregarSumaPliegues(Document doc, ResultadoAntropometricoDTO r,
                                     PdfFont bold, PdfFont regular) throws IOException {
        agregarSeccion(doc, "SUMATORIA DE PLIEGUES (mm)", bold);

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        ds.addValue(r.getSuma6Pliegues(), "Pliegues", "Suma 6");
        ds.addValue(r.getSuma8Pliegues(), "Pliegues", "Suma 8");

        JFreeChart chart = crearGraficoBarras("", ds,
                new Color[]{new Color(0x00, 0x7B, 0xFF)});

        Table t = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .setWidth(UnitValue.createPercentValue(100));
        t.addCell(new Cell().setBorder(null).add(crearImagenGrafico(chart, 240, 150)));
        Cell infoCell = new Cell().setBorder(null).setPadding(10);
        infoCell.add(new Paragraph("Suma 6 pliegues: " + r.getSuma6Pliegues() + " mm")
                .setFont(regular).setFontSize(10));
        infoCell.add(new Paragraph("Suma 8 pliegues: " + r.getSuma8Pliegues() + " mm")
                .setFont(regular).setFontSize(10));
        infoCell.add(new Paragraph("Ratio C-C: " + r.getRatioCinturaCadera()
                + " (" + r.getClasificacionRCC() + ")")
                .setFont(regular).setFontSize(10));
        t.addCell(infoCell);
        doc.add(t);
    }

    private void agregarTablaMediciones(Document doc, ResultadoAntropometricoDTO r,
                                        PdfFont bold, PdfFont regular) {
        agregarSeccion(doc, "MEDICIONES", bold);

        float[] anchos = {2, 1, 2, 1};
        Table t = new Table(UnitValue.createPercentArray(anchos))
                .setWidth(UnitValue.createPercentValue(100))
                .setFontSize(9);

        agregarFilaTabla(t, "Peso", r.getPeso() + " kg",
                         "Talla", r.getTalla() + " cm", bold, regular);
        agregarFilaTabla(t, "IMC", String.format("%.2f", r.getImc()),
                         "Clasificación IMC", r.getClasificacionIMC(), bold, regular);
        agregarFilaTabla(t, "% Masa Grasa", String.format("%.1f%%", r.getPorcentajeGrasa()),
                         "Masa Grasa", String.format("%.1f kg", r.getMasaGrasaKg()), bold, regular);
        agregarFilaTabla(t, "% Masa Magra", String.format("%.1f%%", r.getPorcentajeMasaMagra()),
                         "Masa Magra", String.format("%.1f kg", r.getMasaMagraKg()), bold, regular);
        agregarFilaTabla(t, "Endomorfia", String.format("%.1f", r.getEndomorfia()),
                         "Mesomorfia", String.format("%.1f", r.getMesomorfia()), bold, regular);
        agregarFilaTabla(t, "Ectomorfia", String.format("%.1f", r.getEctomorfia()),
                         "Somatotipo", r.getClasificacionSomatotipo(), bold, regular);

        // Pliegues
        String[] etqs  = r.getEtiquetasPliegues();
        double[]  vals = r.getPerfilesDePliegues();
        for (int i = 0; i < etqs.length - 1; i += 2) {
            agregarFilaTabla(t, etqs[i], vals[i] + " mm",
                             etqs[i+1], vals[i+1] + " mm", bold, regular);
        }

        doc.add(t);
    }

    private void agregarPerfilPliegues(Document doc, ResultadoAntropometricoDTO r,
                                       PdfFont bold) throws IOException {
        agregarSeccion(doc, "PERFIL DE PLIEGUES (mm)", bold);

        XYSeries series = new XYSeries("Pliegues");
        double[] vals = r.getPerfilesDePliegues();
        for (int i = 0; i < vals.length; i++) {
            series.add(i + 1, vals[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "", "", "mm", dataset,
                PlotOrientation.VERTICAL, false, false, false);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(0x00, 0x7B, 0xFF));
        renderer.setSeriesStroke(0, new BasicStroke(2f));
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);

        // Etiquetas del eje X
        plot.getDomainAxis().setLabel("Pliegues: Tri-Sub-Sup-Abd-Mus-Pan-Bic-CI");

        doc.add(crearImagenGrafico(chart, 500, 200));
    }

    private void agregarSomatocarta(Document doc, ResultadoAntropometricoDTO r,
                                    PdfFont bold) throws IOException {
        agregarSeccion(doc, "SOMATOCARTA", bold);

        XYSeriesCollection dataset = new XYSeriesCollection();

        // Punto del paciente
        XYSeries pacienteSeries = new XYSeries(r.getNombreCompleto());
        pacienteSeries.add(r.getXSomatocarta(), r.getYSomatocarta());
        dataset.addSeries(pacienteSeries);

        JFreeChart chart = ChartFactory.createScatterPlot(
                String.format("Endo=%.1f / Meso=%.1f / Ecto=%.1f — %s",
                        r.getEndomorfia(), r.getMesomorfia(), r.getEctomorfia(),
                        r.getClasificacionSomatotipo()),
                "X (Ecto - Endo)", "Y (2×Meso - Endo - Ecto)",
                dataset, PlotOrientation.VERTICAL, true, false, false);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);

        // Líneas de referencia en X=0 e Y=0
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);

        // Rango fijo para la somatocarta estándar
        ((NumberAxis) plot.getDomainAxis()).setRange(-8, 8);
        ((NumberAxis) plot.getRangeAxis()).setRange(-8, 8);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        renderer.setSeriesPaint(0, new Color(0xFF, 0x00, 0x00));
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-6, -6, 12, 12));
        plot.setRenderer(renderer);

        doc.add(crearImagenGrafico(chart, 350, 280));
    }

    private void agregarPerimetros(Document doc, ResultadoAntropometricoDTO r,
                                   PdfFont bold) throws IOException {
        agregarSeccion(doc, "PERÍMETROS CORREGIDOS (cm)", bold);

        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String[] etqs  = r.getEtiquetasPerimetros();
        double[]  vals = r.getPerimetrosCorregidos();
        for (int i = 0; i < etqs.length; i++) {
            ds.addValue(vals[i], "Perímetros", etqs[i]);
        }

        JFreeChart chart = crearGraficoBarras("", ds,
                new Color[]{new Color(0x28, 0xA7, 0x45)});

        doc.add(crearImagenGrafico(chart, 500, 200));
    }

    // -------------------------------------------------------------------------
    // Utilidades de creación de gráficos
    // -------------------------------------------------------------------------

    private JFreeChart crearGraficoBarras(String titulo,
                                          DefaultCategoryDataset dataset,
                                          Color[] colores) {
        JFreeChart chart = ChartFactory.createBarChart(
                titulo, "", "", dataset,
                PlotOrientation.VERTICAL, false, false, false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(null);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        for (int i = 0; i < colores.length; i++) {
            renderer.setSeriesPaint(i, colores[i]);
        }

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setMaximumCategoryLabelWidthRatio(0.5f);

        return chart;
    }

    /**
     * Convierte un JFreeChart en un elemento Image de iText
     * renderizando el gráfico como PNG en memoria.
     */
    private Image crearImagenGrafico(JFreeChart chart, int width, int height)
            throws IOException {
        BufferedImage buffered = chart.createBufferedImage(width, height);
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        ImageIO.write(buffered, "PNG", imgBytes);
        return new Image(ImageDataFactory.create(imgBytes.toByteArray()))
                .setHorizontalAlignment(HorizontalAlignment.CENTER);
    }

    private Cell crearCeldaDato(String etiqueta, String valor,
                                PdfFont bold, PdfFont regular) {
        Cell c = new Cell().setPadding(6)
                .setBackgroundColor(COLOR_FONDO)
                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(4));
        c.add(new Paragraph(etiqueta).setFont(bold).setFontSize(9)
                .setFontColor(COLOR_PRIMARIO));
        c.add(new Paragraph(valor).setFont(regular).setFontSize(14)
                .setFontColor(COLOR_TEXTO));
        return c;
    }

    private void agregarFilaTabla(Table t, String lbl1, String val1,
                                   String lbl2, String val2,
                                   PdfFont bold, PdfFont regular) {
        t.addCell(new Cell().add(new Paragraph(lbl1).setFont(bold).setFontSize(8))
                .setBackgroundColor(COLOR_FONDO));
        t.addCell(new Cell().add(new Paragraph(val1).setFont(regular).setFontSize(8)));
        t.addCell(new Cell().add(new Paragraph(lbl2).setFont(bold).setFontSize(8))
                .setBackgroundColor(COLOR_FONDO));
        t.addCell(new Cell().add(new Paragraph(val2).setFont(regular).setFontSize(8)));
    }
}
