public class SistemaMonitoramento {

    public static void main(String[] args) {
        System.out.println("Iniciando o sistema de monitoramento...\n");

        // Criação das threads com o mesmo Runnable (Sensor) para diferentes tipos
        Thread t1 = new Thread(new Sensor("Temp"));
        Thread t2 = new Thread(new Sensor("Umid"));
        Thread t3 = new Thread(new Sensor("Press"));

        // Inicia as threads
        t1.start();
        t2.start();
        t3.start();

        // Aguarda todas terminarem com join()
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("A thread principal foi interrompida.");
        }

        System.out.println("\nTodas as leituras foram concluídas. Encerrando o sistema.");
    }
}

// Classe Sensor implementando Runnable
class Sensor implements Runnable {
    private String tipo;

    public Sensor(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            double leitura = gerarLeitura();
            System.out.printf("[%s] Leitura %d: %.2f %s%n",
                    tipo, i, leitura, unidade());
            try {
                Thread.sleep(2000); // Espera 2 segundos entre as leituras
            } catch (InterruptedException e) {
                System.out.println("Thread do sensor " + tipo + " interrompida.");
            }
        }
        System.out.println("Sensor " + tipo + " finalizado.");
    }

    // Gera uma leitura simulada de acordo com o tipo
    private double gerarLeitura() {
        switch (tipo) {
            case "Temp":  // Temperatura (20 a 30 °C)
                return 20 + Math.random() * 10;
            case "Umid":  // Umidade (40 a 70 %)
                return 40 + Math.random() * 30;
            case "Press": // Pressão (990 a 1010 hPa)
                return 990 + Math.random() * 20;
            default:
                return 0.0;
        }
    }

    // Define a unidade exibida conforme o tipo do sensor
    private String unidade() {
        switch (tipo) {
            case "Temp":
                return "°C";
            case "Umid":
                return "%";
            case "Press":
                return "hPa";
            default:
                return "";
        }
    }
}
