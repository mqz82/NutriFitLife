package com.nutriFitLife.controller;

import com.nutriFitLife.dto.ResultadoAntropometricoDTO;
import com.nutriFitLife.service.MedicionService;
import com.nutriFitLife.service.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controlador REST para la generación de reportes.
 * Expone el endpoint GET /api/reportes/pdf/{medicionId}.
 */
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final MedicionService medicionService;
    private final ReporteService  reporteService;

    public ReporteController(MedicionService medicionService, ReporteService reporteService) {
        this.medicionService = medicionService;
        this.reporteService  = reporteService;
    }

    /**
     * GET /api/reportes/pdf/{medicionId}
     * Genera y descarga el informe PDF de una medición.
     */
    @GetMapping("/pdf/{medicionId}")
    public ResponseEntity<byte[]> descargarPDF(@PathVariable Long medicionId) {
        try {
            ResultadoAntropometricoDTO resultado = medicionService.calcularResultado(medicionId);
            byte[] pdf = reporteService.generarPDF(resultado);

            String nombreArchivo = "informe-" + resultado.getNombreCompleto()
                    .replace(" ", "_") + "-" + resultado.getFechaMedicion() + ".pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nombreArchivo);
            headers.setContentLength(pdf.length);

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
