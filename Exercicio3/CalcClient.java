import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalcClient {
    public static void main(String[] args) {
        String host = "localhost";
        int porta = 5000;

        try (
                Socket socket = new Socket(host, porta);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println(in.readLine()); // Mensagem de boas-vindas

            while (true) {
                System.out.print("Digite operaçaado (ou SAIR): ");
                String comando = scanner.nextLine();

                if (comando.equalsIgnoreCase("SAIR")) break;

                out.println(comando);
                String resposta = in.readLine();
                System.out.println("Servidor: " + resposta);
            }

        } catch (IOException e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
