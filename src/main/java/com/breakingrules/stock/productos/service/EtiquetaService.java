package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class EtiquetaService {

    private static final float MM_TO_POINTS = 2.83465f;

    public byte[] generarEtiquetas(List<VarianteProducto> variantes) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        float width = 38 * MM_TO_POINTS;
        float height = 20 * MM_TO_POINTS;

        Rectangle pageSize = new Rectangle(width, height);
        Document document = new Document(pageSize, 0, 0, 0, 0);

        PdfWriter.getInstance(document, baos);
        document.open();

        for (VarianteProducto v : variantes) {

            // UNA SOLA DESCRIPCIÓN (chica)
            String descripcion = v.getProducto().getNombre() + " "
                    + v.getColor().name() + " "
                    + v.getTalle().name();

            if (descripcion.length() > 22) {
                descripcion = descripcion.substring(0, 22);
            }

            Font fontNombre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 5);
            Paragraph nombre = new Paragraph(descripcion, fontNombre);
            nombre.setAlignment(Element.ALIGN_CENTER);

            document.add(nombre);


            Code128Writer writer = new Code128Writer();

            BitMatrix bitMatrix = writer.encode(
                    v.getCodigoBarras(), // numérico
                    BarcodeFormat.CODE_128,
                    160,   // 🔻 más chico = barras más gruesas
                    90
            );

            ByteArrayOutputStream barcodeStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", barcodeStream);

            Image barcode = Image.getInstance(barcodeStream.toByteArray());

            // ocupa casi toda la etiqueta
            barcode.scaleToFit(36 * MM_TO_POINTS, 14 * MM_TO_POINTS);
            barcode.setAlignment(Image.ALIGN_CENTER);

            document.add(barcode);

            document.newPage();
        }

        document.close();

        return baos.toByteArray();
    }
}