import java.io.*;
import java.net.*;

public class CalcServer {

    public static void main(String[] args) {
        int porta = 5000;
        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor de cálculo iniciado na porta " + porta);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Novo cliente conectado: " + cliente.getInetAddress());
                new Thread(new ClienteHandler(cliente)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interna que trata cada cliente
    static class ClienteHandler implements Runnable {
        private Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                out.println("Conectado ao servidor de cálculo. Use: ADD 5 3, SUB 10 4, MUL 2 6, DIV 8 2");

                String linha;
                while ((linha = in.readLine()) != null) {
                    String resposta = processarComando(linha);
                    out.println(resposta);
                }
            } catch (IOException e) {
                System.out.println("Cliente desconectado.");
            }
        }

        private String processarComando(String comando) {
            try {
                String[] partes = comando.trim().split(" ");
                if (partes.length != 3) return "ERRO: formato inválido. Ex: ADD 5 3";

                String op = partes[0].toUpperCase();
                double a = Double.parseDouble(partes[1]);
                double b = Double.parseDouble(partes[2]);
                double resultado;

                switch (op) {
                    case "ADD": resultado = a + b; break;
                    case "SUB": resultado = a - b; break;
                    case "MUL": resultado = a * b; break;
                    case "DIV":
                        if (b == 0) return "ERRO: divisão por zero";
                        resultado = a / b;
                        break;
                    default: return "ERRO: operação desconhecida";
                }

                return "RESULT " + resultado;
            } catch (Exception e) {
                return "ERRO: comando inválido";
            }
        }
    }
}
