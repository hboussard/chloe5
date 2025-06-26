package fr.inrae.act.bagap.chloe.script;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.image.WritableRaster;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.RasterFactory;

import com.sun.media.imageio.plugins.tiff.TIFFImageWriteParam;

/*
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdal.gdalconstConstants;
*/

public class GeoTiffExporter {
	
	public static void main(String[] args) throws Exception {
		
		//littleMap();
		//hugeMap();
		
	}

    public static void littleMap() throws Exception {
        int width = 1000;
        int height = 1000;

        // Création d'une image avec une seule bande (grayscale)
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();

        // Remplissage de l'image avec des valeurs (par exemple un dégradé)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = (x + y) % 256; // valeur entre 0-255
                raster.setSample(x, y, 0, value);
            }
        }

        // Spécification des coordonnées géographiques (emprise)
        double minX = -5.0;
        double minY = 47.0;
        double maxX = -4.0;
        double maxY = 48.0;

        CoordinateReferenceSystem crs = CRS.decode("EPSG:2154");
        
        GeneralEnvelope envelope = new GeneralEnvelope(2);
        envelope.setCoordinateReferenceSystem(crs);
        envelope.setRange(0, minX, maxX);
        envelope.setRange(1, minY, maxY);

        // Création du coverage
        GridCoverageFactory gcf = new GridCoverageFactory();
        GridCoverage2D coverage = gcf.create("Example", image, envelope);

        // Export en GeoTIFF
        File outputFile = new File("C:/Data/temp/export/exported_image.tif");
        GeoTiffWriter writer = new GeoTiffWriter(outputFile);
        writer.write(coverage, null);

        System.out.println("GeoTIFF exported: " + outputFile.getAbsolutePath());
    }
    
    /*
    public static void mergeGDAL() throws Exception {
    	
    	// === Initialisation GDAL ===
        gdal.AllRegister();

        String tileFolder = "tiles";
        String outputPath = "merged_gdal.tif";

        // === Liste des fichiers GeoTIFF à fusionner ===
        File dir = new File(tileFolder);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".tif"));

        if (files == null || files.length == 0) {
            System.err.println("No TIFF tiles found in " + tileFolder);
            return;
        }

        List<String> tilePaths = new ArrayList<>();
        for (File file : files) {
            tilePaths.add(file.getAbsolutePath());
        }

        // === Utilisation de gdal.Warp pour fusionner les images ===
        Dataset merged = gdal.Warp(
            outputPath,
            tilePaths.toArray(new String[0]), 
            new gdal.WarpOptions(new String[]{
                "-of", "GTiff",         // Format de sortie
                "-co", "BIGTIFF=YES",   // Support fichiers > 4 Go
                "-co", "TILED=YES",     // Écriture tuilée
                "-co", "COMPRESS=LZW"   // Compression (optionnelle)
            })
        );

        if (merged == null) {
            System.err.println("❌ GDAL warp failed.");
            return;
        }

        System.out.println("✅ Merged GeoTIFF created at: " + outputPath);

        merged.delete(); // Libère le dataset
    	
    }
    */
    
    public static void hugeMap() throws Exception {
    	
    	 final int tileSize = 1024;
         final int tilesX = 100; // 1024 * 100 = 102400 px
         final int tilesY = 100;

         double pixelSize = 1.0; // en mètres/pixel
         double originX = 500_000.0;
         double originY = 6_300_000.0;

         CoordinateReferenceSystem crs = CRS.decode("EPSG:2154", true);
         GridCoverageFactory gcf = new GridCoverageFactory();

         for (int ty = 0; ty < tilesY; ty++) {
             for (int tx = 0; tx < tilesX; tx++) {
                 int tileWidth = tileSize;
                 int tileHeight = tileSize;

                 BufferedImage image = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_BYTE_GRAY);
                 WritableRaster raster = image.getRaster();

                 for (int j = 0; j < tileHeight; j++) {
                     for (int i = 0; i < tileWidth; i++) {
                         int globalX = tx * tileSize + i;
                         int globalY = ty * tileSize + j;
                         int value = ((globalX / 100) + (globalY / 100)) % 256;
                         raster.setSample(i, j, 0, value);
                     }
                 }

                 // Coordonnées géographiques de la tuile
                 double minX = originX + tx * tileSize * pixelSize;
                 double maxY = originY - ty * tileSize * pixelSize;
                 double maxX = minX + tileSize * pixelSize;
                 double minY = maxY - tileSize * pixelSize;

                 GeneralEnvelope envelope = new GeneralEnvelope(2);
                 envelope.setCoordinateReferenceSystem(crs);
                 envelope.setRange(0, minX, maxX);
                 envelope.setRange(1, minY, maxY);

                 GridCoverage2D coverage = gcf.create("tile_" + tx + "_" + ty, image, envelope);

                 File tileFile = new File("C:/Data/temp/export/tiles/tile_" + ty + "_" + tx + ".tif");
                 tileFile.getParentFile().mkdirs();

                 GeoTiffWriter writer = new GeoTiffWriter(tileFile);
                 writer.write(coverage, null);

                 System.out.println("🧩 Wrote tile: " + tileFile.getName());
             }
         }

         System.out.println("✅ All tiles written.");
    	
    }
    
    public static void hugeMap4() throws Exception {
    	
    	// === Paramètres d’image ===
        final int width = 50000;
        final int height = 50000;
        final int tileSize = 1024;
        final double pixelSize = 1.0;

        // === Emprise géographique en EPSG:2154 (Lambert 93) ===
        final double xMin = 500_000.0;
        final double yMax = 6_300_000.0;
        final double xMax = xMin + width * pixelSize;
        final double yMin = yMax - height * pixelSize;

        // === CRS ===
        CoordinateReferenceSystem crs = CRS.decode("EPSG:2154", true);

        // === Crée un SampleModel tuilé ===
        SampleModel sampleModel = RasterFactory.createPixelInterleavedSampleModel(
                DataBuffer.TYPE_BYTE, tileSize, tileSize, 1);

        // === Crée un DataBuffer pour toute l’image (en RAM : attention à la taille !) ===
        long numPixels = (long) width * height;
        if (numPixels > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Raster too large for in-memory buffer. Try chunked writing to disk.");
        }

        DataBufferByte dataBuffer = new DataBufferByte((int) numPixels);
        WritableRaster raster = Raster.createWritableRaster(
                sampleModel.createCompatibleSampleModel(width, height),
                dataBuffer,
                new Point(0, 0)
        );

        // === Remplissage tuile par tuile ===
        for (int y = 0; y < height; y += tileSize) {
            for (int x = 0; x < width; x += tileSize) {
                int w = Math.min(tileSize, width - x);
                int h = Math.min(tileSize, height - y);
                byte[] tilePixels = new byte[w * h];

                // Exemple de motif ou chargement depuis source externe
                for (int j = 0; j < h; j++) {
                    for (int i = 0; i < w; i++) {
                        int value = ((x + i) / 100 + (y + j) / 100) % 256;
                        tilePixels[j * w + i] = (byte) value;
                    }
                }

                // Injection dans le raster principal
                raster.setDataElements(x, y, w, h, tilePixels);
            }
        }

        // === Crée une image à partir du raster (pas de copie mémoire) ===
        /*ColorModel colorModel = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);*/
        //BufferedImage image = new BufferedImage(colorModel, raster, false, null);
        BufferedImage image = new BufferedImage(null, raster, false, null);

        // === Définir l’enveloppe spatiale ===
        GeneralEnvelope envelope = new GeneralEnvelope(2);
        envelope.setCoordinateReferenceSystem(crs);
        envelope.setRange(0, xMin, xMax);
        envelope.setRange(1, yMin, yMax);

        // === Crée le GridCoverage2D et écris le GeoTIFF ===
        GridCoverageFactory gcf = new GridCoverageFactory();
        GridCoverage2D coverage = gcf.create("tiled_epsg2154", image, envelope);

        File outputFile = new File("C:/Data/temp/export/geo2_large_image.tif");
        GeoTiffWriter writer = new GeoTiffWriter(outputFile);
        writer.write(coverage, null);

        System.out.println("✅ Tiled GeoTIFF written to: " + outputFile.getAbsolutePath());
    }
    
    public static void hugeMap3() throws Exception {

    	 final int width = 10000;
         final int height = 10000;

         // === Définir la résolution spatiale ===
         double pixelSize = 1.0; // en mètres/pixel
         double xMin = 500_000.0; // coin haut-gauche (EPSG:2154)
         double yMax = 6_300_000.0;

         double xMax = xMin + width * pixelSize;
         double yMin = yMax - height * pixelSize;

         // === Créer l'image (attention à la taille RAM ici) ===
         BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
         WritableRaster raster = image.getRaster();

         // Remplissage de l'image (ex : dégradé)
         for (int y = 0; y < height; y++) {
             for (int x = 0; x < width; x++) {
                 int value = ((x + y) / 100) % 256;
                 raster.setSample(x, y, 0, value);
             }
         }

         // === Définir l'enveloppe spatiale ===
         GeneralEnvelope envelope = new GeneralEnvelope(2);
         CoordinateReferenceSystem crs = CRS.decode("EPSG:2154", true);
         envelope.setCoordinateReferenceSystem(crs);
         envelope.setRange(0, xMin, xMax); // X (est)
         envelope.setRange(1, yMin, yMax); // Y (nord)

         // === Créer et exporter le coverage GeoTIFF ===
         GridCoverageFactory gcf = new GridCoverageFactory();
         GridCoverage2D coverage = gcf.create("georef_image", image, envelope);

         File outputFile = new File("C:/Data/temp/export/geo_large_image.tif");
         GeoTiffWriter writer = new GeoTiffWriter(outputFile);
         writer.write(coverage, null);

         System.out.println("✅ GeoTIFF EPSG:2154 exported: " + outputFile.getAbsolutePath());
     }
    
    public static void hugeMap2() throws Exception {
    	
    	 final int width = 10000;
         final int height = 10000;
         final int tileSize = 1024;

         // Sélection du writer TIFF
         Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("TIFF");
         if (!writers.hasNext()) throw new IllegalStateException("No TIFF writer found.");
         ImageWriter writer = writers.next();

         // Paramètres d'écriture avec tiling explicite
         TIFFImageWriteParam writeParam = new TIFFImageWriteParam(Locale.ENGLISH);
         writeParam.setCompressionMode(TIFFImageWriteParam.MODE_DISABLED);
         writeParam.setTilingMode(TIFFImageWriteParam.MODE_EXPLICIT);
         writeParam.setTiling(tileSize, tileSize, 0, 0);

         // Création du flux de sortie
         File outputFile = new File("C:/Data/temp/export/large_image.tif");
         try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
             writer.setOutput(ios);

             // Déclaration de l’image modèle (taille d’une tuile seulement)
             BufferedImage tileSample = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_BYTE_GRAY);
             ImageTypeSpecifier typeSpecifier = new ImageTypeSpecifier(tileSample);

             writer.prepareWriteSequence(null);

             // Boucle par tuile (l’image entière n’est JAMAIS en mémoire)
             for (int y = 0; y < height; y += tileSize) {
                 for (int x = 0; x < width; x += tileSize) {
                     int w = Math.min(tileSize, width - x);
                     int h = Math.min(tileSize, height - y);

                     BufferedImage tile = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
                     WritableRaster raster = tile.getRaster();

                     // Exemple : remplissage de la tuile avec un motif ou vos données
                     for (int j = 0; j < h; j++) {
                         for (int i = 0; i < w; i++) {
                             int value = ((x + i) / 100 + (y + j) / 100) % 256; // motif simple
                             raster.setSample(i, j, 0, value);
                         }
                     }

                     // Écriture de la tuile comme image entière (chaque tuile = une image logique)
                     IIOImage iioImage = new IIOImage(tile, null, null);
                     writer.writeToSequence(iioImage, writeParam);
                 }
             }

             writer.endWriteSequence();
             System.out.println("Large GeoTIFF successfully written: " + outputFile.getAbsolutePath());
         }
    }
    
}
