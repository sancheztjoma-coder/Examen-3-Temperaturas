package controladores;

import modelos.Temperatura;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import servicios.TemperaturaServicio;
import vistas.TemperaturasVista;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TemperaturasControlador {

    private final TemperaturasVista vista;
    private List<Temperatura> temperaturas;

    public TemperaturasControlador(TemperaturasVista vista) {
        this.vista = vista;

        this.vista.setGraficarClick(evento -> graficar());
        this.vista.setMasMenosCalurosaClick(evento -> calcularEstadisticas());
        cargarDatos();
    }

    private void cargarDatos() {
        String nombreArchivo = System.getProperty("user.dir") + "/src/datos/Temperaturas.csv";
        temperaturas = TemperaturaServicio.getDatos(nombreArchivo);
        
    }

    private void graficar() {

        LocalDate desde = vista.getFechaDesde();
        LocalDate hasta = vista.getFechaHasta();

        var datosFiltrados = TemperaturaServicio.filtrar(
                temperaturas, desde, hasta);

        var promedios = TemperaturaServicio.getPromediosPorCiudad(
                datosFiltrados);

        DefaultCategoryDataset datosGrafica = new DefaultCategoryDataset();

        for (var dato : promedios.entrySet()) {

            String ciudad = dato.getKey();
            double promedio = dato.getValue();

            datosGrafica.addValue(promedio, "Temperatura", ciudad);
        }

        JFreeChart graficador = ChartFactory.createBarChart(
                "Temperatura promedio por ciudad",
                "Ciudad",
                "Temperatura (°C)",
                datosGrafica
        );

        ChartPanel pnlGraficador = new ChartPanel(graficador);
        pnlGraficador.setPreferredSize(new Dimension(500, 270));

        vista.graficar(pnlGraficador);

        vista.activarPestaña(0);
    }

    private void calcularEstadisticas() {
        LocalDate fecha = vista.getFechaDesde();

        var masCalurosa = TemperaturaServicio.getMasCalurosa(
                temperaturas, fecha);

        var menosCalurosa = TemperaturaServicio.getMenosCalurosa(
                temperaturas, fecha);

        vista.mostrarTemperaturasExtremas(masCalurosa, menosCalurosa);

        vista.activarPestaña(1);
    }
}
