package vistas;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import javax.swing.*;


import datechooser.beans.DateChooserCombo;
import modelos.Temperatura;

import org.jfree.chart.ChartPanel;

public class TemperaturasVista extends JFrame {

    
    private DateChooserCombo dccDesde, dccHasta;
    private JTabbedPane tpTemperaturas;
    private JPanel pnlGrafica;
    private JPanel pnlEstadisticas;

    JButton btnGraficar, btnCalcularEstadisticas;

    

    public TemperaturasVista() {

        setTitle("Temperaturas por Ciudad");
        setSize(700, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tb = new JToolBar();

        btnGraficar = new JButton();
        btnGraficar.setIcon(new ImageIcon(getClass().getResource("/iconos/Grafica.png")));
        btnGraficar.setToolTipText("Grafica promedio temperatura");
        tb.add(btnGraficar);

        btnCalcularEstadisticas = new JButton();
        btnCalcularEstadisticas.setIcon(new ImageIcon(getClass().getResource("/iconos/Datos.png")));
        btnCalcularEstadisticas.setToolTipText("Ciudad más calurosa y menos calurosa");
        tb.add(btnCalcularEstadisticas);

        
        JPanel pnlCambios = new JPanel();
        pnlCambios.setLayout(new BoxLayout(pnlCambios, BoxLayout.Y_AXIS));

        JPanel pnlDatosProceso = new JPanel();
        pnlDatosProceso.setPreferredSize(new Dimension(pnlDatosProceso.getWidth(), 50)); 
        pnlDatosProceso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        pnlDatosProceso.setLayout(null);

                
        dccDesde = new DateChooserCombo();
        dccDesde.setBounds(220, 10, 100, 25);
        pnlDatosProceso.add(dccDesde);

        dccHasta = new DateChooserCombo();
        dccHasta.setBounds(330, 10, 100, 25);
        dccHasta.setVisible(true);
        pnlDatosProceso.add(dccHasta);

        pnlGrafica = new JPanel();
        JScrollPane spGrafica = new JScrollPane(pnlGrafica);

        pnlEstadisticas = new JPanel();

        tpTemperaturas = new JTabbedPane();
        tpTemperaturas.addTab("Gráfica", spGrafica);
        tpTemperaturas.addTab("Estadísticas", pnlEstadisticas);

        
        pnlCambios.add(pnlDatosProceso);
        pnlCambios.add(tpTemperaturas);

        getContentPane().add(tb, BorderLayout.NORTH);
        getContentPane().add(pnlCambios, BorderLayout.CENTER);

    }

    // getters

    
    public LocalDate getFechaDesde() {
        return dccDesde.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getFechaHasta() {
        return dccHasta.getSelectedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // setters
    public void setGraficarClick(ActionListener evento) {
            btnGraficar.addActionListener(e -> {
                dccHasta.setVisible(true);
                evento.actionPerformed(e);
            });
    }

    public void setCalcularEstadisticasClick(ActionListener evento) {
        btnCalcularEstadisticas.addActionListener(e -> {
            dccHasta.setVisible(false);
            evento.actionPerformed(e);
        });
    }

    
    // metodos publicos
    public void activarPestaña(int posicion) {
        if (posicion >= 0 && posicion < tpTemperaturas.getTabCount()) {
            tpTemperaturas.setSelectedIndex(posicion);
        }
    }

    public void graficar(ChartPanel pnlGraficador) {
        pnlGrafica.removeAll();
        pnlGrafica.setLayout(new BorderLayout());
        pnlGrafica.add(pnlGraficador, BorderLayout.CENTER);
        pnlGrafica.revalidate();
    }

    public void mostrarTemperaturasExtremas(
            Temperatura masCalurosa,
            Temperatura menosCalurosa) {

        pnlEstadisticas.removeAll();
        pnlEstadisticas.setLayout(new GridLayout(2, 1));

        if (masCalurosa == null || menosCalurosa == null) {
            pnlEstadisticas.add(new JLabel(
                    "No hay datos para la fecha seleccionada."));
        } else {
            pnlEstadisticas.add(new JLabel(
                    "Más calurosa: " + masCalurosa.getCiudad()
                    + " --> " + masCalurosa.getTemperatura() + " °C"));

            pnlEstadisticas.add(new JLabel(
                    "Menos calurosa: " + menosCalurosa.getCiudad()
                    + " --> " + menosCalurosa.getTemperatura() + " °C"));
        }

        pnlEstadisticas.revalidate();
        pnlEstadisticas.repaint();
    }
}
